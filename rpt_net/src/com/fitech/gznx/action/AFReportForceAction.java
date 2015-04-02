package com.fitech.gznx.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fitech.gznx.po.AfReportForceRep;
import com.fitech.gznx.po.AfReportForceRepId;
import com.fitech.gznx.service.AFReportForceService;
/**
 * ǿ���ϱ�������
 * @author ������
 * 2013-6-21
 */
public class AFReportForceAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		AFReportForceService forceService = new AFReportForceService();
		String repId = request.getParameter("repId");  //������
		Long forceTypeId = Long.valueOf(request.getParameter("forceTypeId")); //�����
		/*�жϱ����Ƿ��Ƕ��*/
		try {
			if(repId.indexOf(",") > 0){
				String[] array = repId.split(",");
				for(int i =0; i < array.length; i++){
					AfReportForceRep force = new AfReportForceRep(new AfReportForceRepId());
					force.getId().setForceTypeId(forceTypeId);
					force.getId().setRepId(Long.valueOf(array[i]));
					forceService.addAFReportForce(force);
				}
			}else{
				AfReportForceRep afReportForce = null;
				if(afReportForce == null){
					AfReportForceRep force = new AfReportForceRep( new AfReportForceRepId());
					force.getId().setForceTypeId(forceTypeId);
					force.getId().setRepId(Long.valueOf(repId));
					forceService.addAFReportForce(force);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().print(1);
		return null;
	}

}
