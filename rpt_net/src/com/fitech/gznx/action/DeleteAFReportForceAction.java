package com.fitech.gznx.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fitech.gznx.po.AfReportForceRep;
import com.fitech.gznx.service.AFReportForceService;

/**
 * ȡ��ǿ���ϱ�������
 * @author ������
 *	2013-06-21
 */
public class DeleteAFReportForceAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		AFReportForceService forceService = new AFReportForceService();
		String repId = request.getParameter("repId");  //������
		if(repId.indexOf(",") > 0){
			String[] array = repId.split(",");
			for(int i =0; i < array.length; i++){
				/*��ȡ�Ƿ�˱����Ѿ�ǿ���ϱ���!����о�ɾ��*/
				List<AfReportForceRep> afReportForce = forceService.findAFReportForce(Long.valueOf(array[i]),null);
				if(afReportForce != null){
					for(int j = 0; j < afReportForce.size(); j++){
						forceService.deleteAFReportForce(Long.valueOf(array[i]),afReportForce.get(j).getId().getForceTypeId());
					}
				}
			}
		}else{
			List<AfReportForceRep> afReportForce = forceService.findAFReportForce(Long.valueOf(repId),null);
			if(afReportForce != null){
				for(int j = 0; j < afReportForce.size(); j++){
					forceService.deleteAFReportForce(Long.valueOf(repId),afReportForce.get(j).getId().getForceTypeId());
				}
			}
		}
		response.getWriter().print(1);
		return null;
	}

}
