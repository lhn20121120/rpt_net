package com.cbrc.smis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.entity.AFDataTrace;
import com.cbrc.smis.service.IAFDataTraceService;
import com.cbrc.smis.service.impl.AFDataTraceServiceImpl;
import com.cbrc.smis.util.FitechLog;

public class UpdateAFDataTraceAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String traceId = request.getParameter("traceId");//����ID
		String repInId = request.getParameter("repInId");//����ID
		String cellName = request.getParameter("cellName");//��Ԫ�����
		String status = request.getParameter("status");//״̬
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		if(traceId==null || traceId.equals(""))
			return null;
		//��ȡ��ݺۼ�����ID
		Integer traceIdInt = Integer.valueOf(traceId);
		//ʵ�������
		IAFDataTraceService dataTraceService = new AFDataTraceServiceImpl();
		//ʵ��ʵ����
		AFDataTrace af = new AFDataTrace();
		af.setTraceId(traceIdInt);//����ID
		af.setRepInId(repInId);//����ID
		af.setCellName(cellName);//��Ԫ�����
		try {
			dataTraceService.updateAFDataTraceStatusById(traceIdInt,Integer.valueOf(status));
			//��һ������2Ϊɾ����ݺۼ���¼��־
			FitechLog.writeTraceLog(2, request, af, true ,reportFlg);
		} catch (Exception e) {
			e.printStackTrace();
			//��һ������2Ϊɾ����ݺۼ���¼��־
			FitechLog.writeTraceLog(2, request, af, false,reportFlg);
		}
		return null;		
	}
	
}
