package com.fitech.gznx.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsMRepFreqDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.service.AFOrgDelegate;

public class SeachDataInitAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//��ѯ���б���
		List<MCurrForm> currList = StrutsMCurrDelegate.findAll();
		//��ѯ����Ƶ��
		List<MDataRgTypeForm> datargList = StrutsMDataRgTypeDelegate.findAll();
		//��ѯ����ģ����Ϣ
		List<MChildReportForm> reportList = StrutsMChildReportDelegate.findAll();
		
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_NAME);
		String userId=String.valueOf(operator.getOperatorId());
		 
		//���ɻ���json�ļ�
		String treeData = createAFOrgDataJSON(operator);
		String filePath = Config.WEBROOTPATH + Config.FILESEPARATOR
		+ "json" + Config.FILESEPARATOR + "tree_data_"+userId+".json";
		File file = new File(filePath);
		if(file.exists());
			file.delete();
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));

		bw.write(treeData);
		bw.flush();
		bw.close();
		System.out.println("����JSONд��ɹ�");
		
		//����reqeuest������
		request.setAttribute("currList", currList);
		request.setAttribute("datargList", datargList);
		request.setAttribute("reportList", reportList);
		// TODO Auto-generated method stub
		return mapping.findForward("index");
	}
	
	/***
	 * ���ɻ���JSON�ļ�
	 */
	private String createAFOrgDataJSON(Operator operator){
		List orgList = null;
		if(operator.isSuperManager())//�����û���ѯ���л���
			orgList= AFOrgDelegate.getAllFirstOrg();
		else//��ѯ�û�����
			orgList = AFOrgDelegate.getFirstOrgById(operator.getOrgId());
		StringBuffer treeJSON = new StringBuffer("[");
		
		for(int i=0;i<orgList.size();i++){
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\""+strs[0]+"\",");
			treeJSON.append("\"text\":"+"\""+strs[1]+"\"");
			
			treeJSON.append(iteratorCreate(strs[0]));//�ݹ�����ӻ���
			
			treeJSON.append("}");
			if(i!=orgList.size()-1)
				treeJSON.append(",");
		}
		
		treeJSON.append("]") ;
		System.out.println(treeJSON);
		return treeJSON.toString();
	}
	
	private String iteratorCreate(String id){
		List orgList = AFOrgDelegate.getChildListByOrgId(id);
		
		if (orgList == null || orgList.size() == 0)
			return "";
		StringBuffer treeJSON = new StringBuffer(",\"children\":[");
		for(int i=0;i<orgList.size();i++){
			String[] strs = (String[]) orgList.get(i);
			treeJSON.append("{\"id\":\""+strs[0]+"\",\"text\":\""+strs[1]+"\"");
			
			treeJSON.append(iteratorCreate(strs[0]));
			treeJSON.append("}");
			if(i!=orgList.size()-1)
				treeJSON.append(",");
			
		}
		treeJSON.append("]");
		return treeJSON.toString();
	}
}
