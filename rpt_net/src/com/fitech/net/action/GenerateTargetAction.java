package com.fitech.net.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsActuTargetResultDelegate;
import com.fitech.net.form.ActuTargetResultForm;
import com.fitech.net.hibernate.TargetDefine;

/**
 * 生成指标
 * @author jcm
 * @2008-03-28
 *
 */
public final class GenerateTargetAction extends Action {
	/**异常处理*/
	private static FitechException log=new FitechException(GenerateTargetAction.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response)throws IOException, ServletException {
		
		ActuTargetResultForm atrForm = (ActuTargetResultForm)form;
		RequestUtils.populate(atrForm, request);
		FitechMessages messages = new FitechMessages();
		
		Calendar calendar = Calendar.getInstance();		
		if(atrForm.getYear() == null || "".equals(atrForm.getYear()))			   
			atrForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
		if(atrForm.getMonth() == null || "".equals(atrForm.getMonth()))			   
			atrForm.setMonth(new Integer(calendar.get(Calendar.MONTH))); //查询上月的指标
		
		/**生成当前用户所在机构的指标数据*/
		HttpSession session = request.getSession();
	    Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_NAME);
	    atrForm.setOrgId(operator.getOrgId());
		
		try{
			int count = StrutsReportInDelegate.getCount(atrForm);
			if(count != 0){
				/**查出当前机构需要生成的指标列表*/
				List list=StrutsActuTargetResultDelegate.searchTargetList(atrForm.getOrgId(),atrForm.getYear(),atrForm.getMonth());
				if(list != null && list.size() > 0){
					/**删除旧的指标结果集*/
					boolean deleteFlag = StrutsActuTargetResultDelegate.deleteResult(list,atrForm.getOrgId(),atrForm.getYear(),atrForm.getMonth());
					if(deleteFlag == true){
						TargetDefine targetDefine = null; //指标对象
						ActuTargetResultForm atrFormTemp = null; //指标值对象
						String fChildRepId = null; //指标公式中的第一个报表
						String fVersionId = null; //指标公式中第一个报表对应版本号
						String formula = null; //指标公式
						boolean bool = false;
						
						for(Iterator iter=list.iterator();iter.hasNext();){
							targetDefine = (TargetDefine)iter.next();
							formula = targetDefine.getFormula();
							try{
								fChildRepId = formula.substring(formula.indexOf("G")<=-1?(formula.indexOf("S")<=-1?0:formula.indexOf("S")):formula.indexOf("G")
										,formula.indexOf("_")); //获取指标公式中第一个报表
							}catch(StringIndexOutOfBoundsException e){
								messages.add("指标["+targetDefine.getDefineName().trim()+"]：生成失败--指标公式错误！");
								continue;
							}
							
							fVersionId = StrutsMRepRangeDelegate.getVerionId(fChildRepId,atrForm.getYear(),atrForm.getMonth());
							if(fVersionId != null){
								/**获得公式中第一个报表的报送频度，分别按不同口径生成指标数据*/
								List mActuRepList = StrutsMActuRepDelegate.getMActuReps(fChildRepId,fVersionId,String.valueOf(atrForm.getMonth()));
								if(mActuRepList == null || mActuRepList.size() <=0) continue;
								MActuRepForm mActuRepForm = null;
								for(Iterator actuIter=mActuRepList.iterator();actuIter.hasNext();){
									mActuRepForm = (MActuRepForm)actuIter.next();
									atrFormTemp = new ActuTargetResultForm();
									
									atrFormTemp.setDataRangeId(mActuRepForm.getDataRangeId());
									atrFormTemp.setRepFreId(mActuRepForm.getRepFreqId());
									atrFormTemp.setTargetDefineId(targetDefine.getTargetDefineId());
									atrFormTemp.setYear(atrForm.getYear());
									atrFormTemp.setMonth(atrForm.getMonth());
									atrFormTemp.setOrgId(atrForm.getOrgId());
									atrFormTemp.setCurUnitId(new Integer(1)); //此处只有人民币（多币种在程序里面已经做特殊处理了）
									bool = StrutsActuTargetResultDelegate.generateTargetResult(atrFormTemp,mActuRepForm,formula);
									
									/**指标生成失败，记录失败信息*/
									if(bool == false){
										messages.add("指标["+targetDefine.getDefineName().trim()+"]:《"+mActuRepForm.getDataRgDesc()+"》生成失败"
												+(atrFormTemp.getAllWarnMessage()!=null?"（"+atrFormTemp.getAllWarnMessage()+"）":""));
									}else
										messages.add("指标["+targetDefine.getDefineName().trim()+"]:《"+mActuRepForm.getDataRgDesc()+"》生成成功!");
								}
							}else 
								messages.add("指标["+targetDefine.getDefineName().trim()+"]：生成失败--未找到版本信息！");
						}
					}else
						messages.add("旧指标结果集删除失败！");
				}else
					messages.add("没有需要生成的指标！");
			}else
				messages.add("缺少该月份数据！");
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add("指标生成失败！");
		}

		/**生成指标完成后，查询指标*/
		List resList = StrutsActuTargetResultDelegate.search(atrForm);
		
		if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
		
	 	if(resList != null && resList.size() > 0){
	 		String yearAndMonth = "";
	 		for(int i=0;i<resList.size();i++){		 			
	 			yearAndMonth+=((ActuTargetResultForm)resList.get(i)).getYearAndMonth()+"##";
	 		}
	 		resList=targetAdd(resList);
	 		request.setAttribute("yearAndMonth",yearAndMonth); 
	 		request.setAttribute(Config.RECORDS,resList); 
	 	}
	 	
		return mapping.findForward("toView");
	}
	
	/**
	 * 单元格合并的标记方法
	 * 
	 * @param list
	 * @return
	 */
	private List targetAdd(List list){
		List result=list;
		String tName="";
		String nName="";
		int tNumber=1;
		int nNumber=1;
		
		for(int i=list.size()-1;i>=0;i--){
			ActuTargetResultForm bean=(ActuTargetResultForm)list.get(i);
			if(!tName.equals(bean.getBusinessName())){
				if(i+1<list.size()){
					ActuTargetResultForm aBean=((ActuTargetResultForm)result.get(i+1));
					aBean.setChange(new Integer(tNumber));
					aBean.setNorChange(new Integer(nNumber));
					result.set(i+1,aBean);
				}
				tName=bean.getBusinessName();
				nName=bean.getNormalName();
				//以下2行是修改指标查看的格式问题增加的
				bean.setChange(new Integer(tNumber));
				bean.setNorChange(new Integer(nNumber));
				tNumber=1;
				nNumber=1;
			}else{
				tNumber++;
				
				if(!nName.equals(bean.getNormalName())){
					ActuTargetResultForm aBean=((ActuTargetResultForm)result.get(i+1));
					aBean.setNorChange(new Integer(nNumber));
					result.set(i+1,aBean);
					nName=bean.getNormalName();
					nNumber=1;
				}else
					nNumber++;
				
				if(i==0){
					bean.setChange(new Integer(tNumber));
					bean.setNorChange(new Integer(nNumber));
					result.set(0,bean);
				}
			}
			result.set(i,bean);
		}
		return result;
	}
}