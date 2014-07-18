package com.fitech.papp.webservice.client;

import com.fitech.papp.webservice.client.OrgWebServiceStub.DeleteOrgResponse;
import com.fitech.papp.webservice.client.OrgWebServiceStub.InsertOrg;
import com.fitech.papp.webservice.client.OrgWebServiceStub.InsertOrgResponse;
import com.fitech.papp.webservice.pojo.WebSysOrg;


public class Test {

	public static void main(String args[]) {

		try {
			OrgWebServiceStub stub = new OrgWebServiceStub("http://localhost:8080/cbrc/services/OrgWebService?wsdl");
			OrgWebServiceStub.InsertOrg insertOrg = new OrgWebServiceStub.InsertOrg();
			OrgWebServiceStub.DeleteOrg deleteOrg = new OrgWebServiceStub.DeleteOrg();
			OrgWebServiceStub.WebSysOrg org = new OrgWebServiceStub.WebSysOrg();
			org.setAddress("china");
			org.setOrgType("0");
			org.setOrgName("testInert");
			org.setOrgId("1111");
			org.setOrgAreaId("0");
			
			insertOrg.setOrg(org);
			String s  ="";
			//返回值也被封装
			InsertOrgResponse eur = stub.insertOrg(insertOrg);
			s = eur.get_return();
			//returnUser.getId()//在服务器端动态重置过
			System.out.println(s);
			
			deleteOrg.setOrg(org);
			DeleteOrgResponse deur = stub.deleteOrg(deleteOrg);
			s = deur.get_return();
			System.out.println(s);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
