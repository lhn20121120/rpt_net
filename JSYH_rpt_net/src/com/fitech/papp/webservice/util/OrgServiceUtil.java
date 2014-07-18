package com.fitech.papp.webservice.util;

import org.springframework.beans.BeanUtils;

import com.fitech.papp.webservice.client.OrgWebServiceStub;
import com.fitech.papp.webservice.client.OrgWebServiceStub.DeleteOrgResponse;
import com.fitech.papp.webservice.client.OrgWebServiceStub.InsertOrgResponse;
import com.fitech.papp.webservice.client.OrgWebServiceStub.UpdateOrgResponse;
import com.fitech.papp.webservice.pojo.WebSysOrg;

public class OrgServiceUtil {

	public static String excute(String wsdlPath, String mothed, WebSysOrg org)
			throws Exception {
		String result = "";
		
		if(com.cbrc.smis.common.Config.PORTAL){
			if (mothed.equalsIgnoreCase("insert")) {
				result = insert(wsdlPath, org);
			}
			if (mothed.equalsIgnoreCase("delete")) {
				result = deleteOrg(wsdlPath, org);
			}
			if (mothed.equalsIgnoreCase("update")) {
				result = update(wsdlPath, org);
			}
		}else{
			result = "0";
		}

		return result;

	}

	private static String update(String wsdlPath, WebSysOrg org)
			throws Exception {
		OrgWebServiceStub stub = null;
		String result = "";
		// "http://localhost:8080/cbrc/services/OrgWebService?wsdl"
		stub = new OrgWebServiceStub(wsdlPath+"/services/OrgWebService?wsdl");
		OrgWebServiceStub.UpdateOrg updateOrg = new OrgWebServiceStub.UpdateOrg();
		OrgWebServiceStub.WebSysOrg webOrg = new OrgWebServiceStub.WebSysOrg();
		BeanUtils.copyProperties(org, webOrg);
		updateOrg.setOrg(webOrg);
		UpdateOrgResponse deur = stub.updateOrg(updateOrg);
		result = deur.get_return();
		System.out.println(result);
		return result;
	}

	private static String deleteOrg(String wsdlPath, WebSysOrg org)
			throws Exception {
		OrgWebServiceStub stub = null;
		String result = "";
		// "http://localhost:8080/cbrc/services/OrgWebService?wsdl"
		stub = new OrgWebServiceStub(wsdlPath+"/services/OrgWebService?wsdl");
		OrgWebServiceStub.DeleteOrg deleteOrg = new OrgWebServiceStub.DeleteOrg();
		OrgWebServiceStub.WebSysOrg webOrg = new OrgWebServiceStub.WebSysOrg();
		BeanUtils.copyProperties(org, webOrg);
		deleteOrg.setOrg(webOrg);
		DeleteOrgResponse deur = stub.deleteOrg(deleteOrg);
		result = deur.get_return();
		System.out.println(result);
		return result;
	}

	private static String insert(String wsdlPath, WebSysOrg org)
			throws Exception {
		String result = "";
		OrgWebServiceStub stub = null;
		stub = new OrgWebServiceStub(wsdlPath+"/services/OrgWebService?wsdl");
		OrgWebServiceStub.InsertOrg insertOrg = new OrgWebServiceStub.InsertOrg();
		OrgWebServiceStub.WebSysOrg webOrg = new OrgWebServiceStub.WebSysOrg();
		BeanUtils.copyProperties(org, webOrg);
		insertOrg.setOrg(webOrg);
		InsertOrgResponse eur = stub.insertOrg(insertOrg);
		result = eur.get_return();
		return result;
	}
}
