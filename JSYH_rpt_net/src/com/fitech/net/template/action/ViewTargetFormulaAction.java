	package com.fitech.net.template.action;

	import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsFormulaDelegate;
import com.fitech.net.form.EtlIndexForm;
	/**
	 * 取数公式的指标查看,
	 * @author wh
	 * 2007-4-25
	 */
	public final class ViewTargetFormulaAction extends Action {
		private FitechException log=new FitechException(ViewTargetFormulaAction.class);
		
		
	   /**
	    * @param result 查询返回标志,如果成功返回true,否则返回false
	    * @param OrgTypeForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
		public ActionForward execute(
	      ActionMapping mapping,
	      ActionForm form,
	      HttpServletRequest request,
	      HttpServletResponse response
		)throws IOException, ServletException {

			FitechMessages messages = new FitechMessages();
			MessageResources resources = getResources(request);

			// 是否有Request
		    EtlIndexForm etlIndexForm = (EtlIndexForm)form;	   
			//RequestUtils.populate(etlIndexForm, request);
			int recordCount = 0; // 记录总数
			int offset = 0; // 偏移量
			int limit = 10; // 每页显示的记录条数

			List resList = null;
		   
			ApartPage aPage = new ApartPage();
		   	String strCurPage = request.getParameter("curPage");
			if(strCurPage != null){
			    if(!strCurPage.equals(""))
			      aPage.setCurPage(new Integer(strCurPage).intValue());
			}
			else
				aPage.setCurPage(1);
			//计算偏移量
			offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
			limit = Config.PER_PAGE_ROWS;	

			try{
		   		//取得记录总数
				recordCount = StrutsFormulaDelegate.getEtlIndexRecordCount(etlIndexForm);
		   			   		
		   		//显示分页后的记录
		   		if(recordCount > 0)
			   	    resList = StrutsFormulaDelegate.selectEtlIndex(etlIndexForm,offset,limit); 
		   		
		   	}catch (Exception e){
				log.printStackTrace(e);
				messages.add(resources.getMessage("公式指标查找失败!"));		
			}
			//移除request或session范围内的属性
			FitechUtil.removeAttribute(mapping,request);
			//把ApartPage对象存放在request范围内
		   	
		 	aPage.setTerm(this.getTerm(etlIndexForm));
		 	aPage.setCount(recordCount);
		 	request.setAttribute(Config.APART_PAGE_OBJECT, aPage);
		 	
		 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
			   	  request.setAttribute(Config.MESSAGES,messages);
		 	 
		 	 if(resList!=null && resList.size()>0)
		 		 request.setAttribute(Config.RECORDS,resList);
		 	 
		 	 return mapping.findForward("view");
		}

	   
		public String getTerm(EtlIndexForm etlIndexForm){
			String term="";
			
			if (etlIndexForm != null && etlIndexForm.getIndexName() != null) {
				String  indexName = etlIndexForm.getIndexName();
				
				if(indexName != null){
					term += (term.indexOf("") >= 0 ? "" : "&");
					term += "vpId=" + indexName.toString();
				}

			}
		   return term;
		}
	}
	  