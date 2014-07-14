package com.cbrc.smis.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFReportDealDelegate;

/***
 * ��ȡreport_in��ע��Ϣ����׷�ӱ�ע��Ϣ
 * @author ���Ը� 2012-01-20
 *
 */
public class AddReportInDescAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		//��ȡreportin��������Ϣ
		String repInId = request.getParameter("repInId");
		
		//��ȡ����ӵı�ע��Ϣ
		String addDesc=request.getParameter("addDesc");
		
		
		boolean isOK=true;
		/**��addDescΪnull ��û����ӱ�ע��Ϣ������Ҫ���±�ע��Ϣ
		 *�������������Ҫִ�� ���Ը� 2012-01-20*/
		if(addDesc!=null && !addDesc.trim().equals(""))
		{
			isOK=false;
			
			/**���Ը�  ��ӱ���ı�ע��Ϣ*/
			//��ȡ���еı�ע��Ϣ
			//String desc=new String(request.getParameter("desc").getBytes("ISO-8859-1"),"UTF-8");
			
			/** ����ѡ�б�־ **/
			String reportFlg = "0";
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
			
			//��sessiong�л�ȡ�û���Ϣ
			Operator operator=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			//��ȡ��ǰ����
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//ƴ���ַ��������ϱ�ע�淶
			;
			String msg="--�û�[��¼��:" + operator.getUserName() + ",����:" + operator.getOperatorName() + "]"+" "+sdf.format(new Date())+"\n";
			//ƴ������������޸���Ϣ
			String addDescMsg=addDesc.trim()+"\n"+msg;
			System.out.println(addDescMsg);
			//��ȡreportInForm
			//����ᱨ��
			if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
				ReportIn reportIn=StrutsReportInDelegate.getReportInByReportInId(Integer.valueOf(repInId));
				//�������ñ�ע��Ϣ,����ӷǿ��ж�
				if(reportIn.getRepDesc()!=null && !reportIn.getRepDesc().trim().equals(""))
					reportIn.setRepDesc(addDescMsg+reportIn.getRepDesc());
				else {
					reportIn.setRepDesc(addDescMsg);
				}
				//����reportin��
				isOK=StrutsReportInDelegate.updateReportIn(reportIn);
			}else{//����
				AfReport afReport=AFReportDealDelegate.getAFReportByRepId(Long.valueOf(repInId));
				if(afReport.getRepDesc()!=null && !afReport.getRepDesc().trim().equals(""))
					afReport.setRepDesc(addDescMsg+afReport.getRepDesc());
				else
					afReport.setRepDesc(addDescMsg);
				//����afreport��
				isOK=AFReportDealDelegate.updateAFReport(afReport);
			}
		}
		
		return null;
	}
	
}
