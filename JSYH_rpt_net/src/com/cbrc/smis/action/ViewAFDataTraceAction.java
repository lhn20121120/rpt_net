package com.cbrc.smis.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.service.IAFDataTraceService;
import com.cbrc.smis.service.impl.AFDataTraceServiceImpl;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.action.ViewDataReportAction;

public class ViewAFDataTraceAction extends Action{
	private static FitechException log=new FitechException(ViewAFDataTraceAction.class);
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/**��ȡ��ѯ����-------��ʼ*/
		String reportTerm = request.getParameter("reportTerm");
		String repName = request.getParameter("repName");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String orgId = request.getParameter("orgId");
		String orgName = request.getParameter("orgName");
		AFDataTraceForm traceForm = (AFDataTraceForm)form;
		if(traceForm==null)
			traceForm = new AFDataTraceForm();
		
		traceForm.setReportTerm(reportTerm);
		traceForm.setRepName(repName);
		traceForm.setBeginDate(beginDate);
		traceForm.setEndDate(endDate);
		/**��ȡ��ѯ����-------����*/
		
		IAFDataTraceService traceService = new AFDataTraceServiceImpl();//������
		List<AFDataTraceForm> fromList = null;
		int offset=0; //ƫ����
        int limit=0;  //ÿҳ��ʾ�ļ�¼����
		String strCurPage=request.getParameter("curPage");
		ApartPage aPage=new ApartPage();
	    if(strCurPage!=null && !strCurPage.equals("")){
	    	aPage.setCurPage(new Integer(strCurPage).intValue());
	    }
	    else
	        aPage.setCurPage(1);
	        
	    //����ƫ����
	    offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
	    limit = Config.PER_PAGE_ROWS;   
	    
	    
	    /***
         * ȡ�õ�ǰ�û���Ȩ����Ϣ
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);          

		int recordCount =0; //��¼����		
		ReportInForm reportInForm = new ReportInForm();
		
		if(traceForm.getReportTerm() == null || traceForm.getReportTerm().equals("")){//��������
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setYear(new Integer(yestoday.substring(0,4)));		   
			reportInForm.setTerm(new Integer(yestoday.substring(5, 7)));
			reportInForm.setDay(new Integer(yestoday.substring(8,10)));
			reportInForm.setDate(yestoday.substring(0, 10));
			traceForm.setReportTerm(yestoday.substring(0, 10));

		}else{
			reportInForm.setYear(new Integer(traceForm.getReportTerm().substring(0, 4)));
			reportInForm.setTerm(new Integer(traceForm.getReportTerm().substring(5, 7)));
			reportInForm.setDate(traceForm.getReportTerm());//���뱨������
		}
		reportInForm.setOrgId(operator.getOrgId());//�����û�����
		//List����ĳ�ʼ��
		List<Aditing> resList=null;
		try{
			if(reportInForm.getOrgId() == null)
				reportInForm.setOrgId(operator.getOrgId());
			StrutsMRepRangeDelegate delegate=new StrutsMRepRangeDelegate();
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			recordCount=delegate.selectDBReportCount(reportInForm, operator);
			if(recordCount > 0){
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				resList=delegate.selectDBReportRecord(reportInForm, operator, offset, limit);
			}
		}catch(Exception ex){
			log.printStackTrace(ex);
		}
		if(resList!=null && resList.size()>0){
			List<String> repList = new ArrayList<String>();
			String repInIds = "(";
			for(int i=0;i<resList.size();i++){
				Aditing a = resList.get(i);
				if(a.getRepInId()!=null && !a.getRepInId().equals("")){
					repList.add(a.getRepInId().toString());		
				}
			}
			if(repList.size()==1)
				repInIds += "'"+repList.get(0)+"'";
			if(repList.size()>=2){
				for(int i=0;i<repList.size();i++){
					if(i!=0)
						repInIds += ",";
					repInIds +=	"'"+repList.get(i)+"'";
				}
			}
			repInIds += ")";
			if(!repInIds.equals("()"))
				traceForm.setRepInIds(repInIds);
		}
		if(traceForm.getRepInIds()!=null)
			fromList = traceService.findListByAFDataTrace(traceForm, limit, offset);
	    request.setAttribute("resList", resList);
	    request.setAttribute("afReportForm", traceForm);
		request.setAttribute("formList", fromList);
		request.setAttribute("orgId",reportInForm.getOrgId());
		request.setAttribute("orgName", orgName);
		request.setAttribute("orgId", orgId);
		
		 //��ApartPage��������request��Χ��
		int counts = (fromList==null || fromList.size()==0)?0:fromList.size();
		aPage.setCount(counts);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		return mapping.findForward("index");
	}
	
}
