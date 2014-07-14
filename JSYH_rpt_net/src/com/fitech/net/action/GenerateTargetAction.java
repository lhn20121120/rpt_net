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
 * ����ָ��
 * @author jcm
 * @2008-03-28
 *
 */
public final class GenerateTargetAction extends Action {
	/**�쳣����*/
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
			atrForm.setMonth(new Integer(calendar.get(Calendar.MONTH))); //��ѯ���µ�ָ��
		
		/**���ɵ�ǰ�û����ڻ�����ָ������*/
		HttpSession session = request.getSession();
	    Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_NAME);
	    atrForm.setOrgId(operator.getOrgId());
		
		try{
			int count = StrutsReportInDelegate.getCount(atrForm);
			if(count != 0){
				/**�����ǰ������Ҫ���ɵ�ָ���б�*/
				List list=StrutsActuTargetResultDelegate.searchTargetList(atrForm.getOrgId(),atrForm.getYear(),atrForm.getMonth());
				if(list != null && list.size() > 0){
					/**ɾ���ɵ�ָ������*/
					boolean deleteFlag = StrutsActuTargetResultDelegate.deleteResult(list,atrForm.getOrgId(),atrForm.getYear(),atrForm.getMonth());
					if(deleteFlag == true){
						TargetDefine targetDefine = null; //ָ�����
						ActuTargetResultForm atrFormTemp = null; //ָ��ֵ����
						String fChildRepId = null; //ָ�깫ʽ�еĵ�һ������
						String fVersionId = null; //ָ�깫ʽ�е�һ�������Ӧ�汾��
						String formula = null; //ָ�깫ʽ
						boolean bool = false;
						
						for(Iterator iter=list.iterator();iter.hasNext();){
							targetDefine = (TargetDefine)iter.next();
							formula = targetDefine.getFormula();
							try{
								fChildRepId = formula.substring(formula.indexOf("G")<=-1?(formula.indexOf("S")<=-1?0:formula.indexOf("S")):formula.indexOf("G")
										,formula.indexOf("_")); //��ȡָ�깫ʽ�е�һ������
							}catch(StringIndexOutOfBoundsException e){
								messages.add("ָ��["+targetDefine.getDefineName().trim()+"]������ʧ��--ָ�깫ʽ����");
								continue;
							}
							
							fVersionId = StrutsMRepRangeDelegate.getVerionId(fChildRepId,atrForm.getYear(),atrForm.getMonth());
							if(fVersionId != null){
								/**��ù�ʽ�е�һ������ı���Ƶ�ȣ��ֱ𰴲�ͬ�ھ�����ָ������*/
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
									atrFormTemp.setCurUnitId(new Integer(1)); //�˴�ֻ������ң�������ڳ��������Ѿ������⴦���ˣ�
									bool = StrutsActuTargetResultDelegate.generateTargetResult(atrFormTemp,mActuRepForm,formula);
									
									/**ָ������ʧ�ܣ���¼ʧ����Ϣ*/
									if(bool == false){
										messages.add("ָ��["+targetDefine.getDefineName().trim()+"]:��"+mActuRepForm.getDataRgDesc()+"������ʧ��"
												+(atrFormTemp.getAllWarnMessage()!=null?"��"+atrFormTemp.getAllWarnMessage()+"��":""));
									}else
										messages.add("ָ��["+targetDefine.getDefineName().trim()+"]:��"+mActuRepForm.getDataRgDesc()+"�����ɳɹ�!");
								}
							}else 
								messages.add("ָ��["+targetDefine.getDefineName().trim()+"]������ʧ��--δ�ҵ��汾��Ϣ��");
						}
					}else
						messages.add("��ָ������ɾ��ʧ�ܣ�");
				}else
					messages.add("û����Ҫ���ɵ�ָ�꣡");
			}else
				messages.add("ȱ�ٸ��·����ݣ�");
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add("ָ������ʧ�ܣ�");
		}

		/**����ָ����ɺ󣬲�ѯָ��*/
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
	 * ��Ԫ��ϲ��ı�Ƿ���
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
				//����2�����޸�ָ��鿴�ĸ�ʽ�������ӵ�
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