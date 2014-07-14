/*
 * Created on 2006-5-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.obtain.excel;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fitech.net.common.XmlParser;
import com.fitech.net.config.Config;
import com.fitech.net.obtain.excel.dao.ObtainManager;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TemplateObtainConfigure {
	private String templateID;
	private String versionID;
	private Document doc;
	private String folder;
	private String excelName;
	
	public TemplateObtainConfigure(String templateID, String versisonID){
		this.setTemplateID(templateID);
		this.setVersionID(versisonID);
		init();
	}
	
	/**
	 * ��ʼ������
	 */
	private void init(){
		//doc = XmlParser.parseXml(Config.getObtainConfigFilePath());
		doc = XmlParser.parseXml(Config.getSystemFolderPath() + File.separator + Config.CONFIGURATION_OBTAIN_FILE);
		this.setExcelName(ObtainManager.getExcelName
							(this.getTemplateID(), this.getVersionID()));
		//this.setExcelName("srcG11.xls");

	}

	/**	 
	 * @return�����ر���ģ��ľ���·��
	 */
	public String getTemplateFullPath(){
		String fullName = Config.getReportsFolderRealPath() + File.separator;
		String uri = getTemplateAttributeValue("uri");
		if (uri != null && !"".equals(uri)) {
			fullName += uri + File.separator;
		}
		fullName += this.getExcelName(); 
		
		return fullName;
	}
	
	/**
	 * ����ĳ������Դ�ľ���·��
	 * @param sheetName sheet����
	 * @return������ĳ������Դ�ľ���·��
	 */
	public String getDatasourceFullPath(String sheetName){
		String fullName = Config.DATA_SOURCE_EXCEL + File.separator;
		String uri = getDatasourceAttributeValue(sheetName, "uri");
		String xlsName = getDatasourceAttributeValue(sheetName, "xlsName");
		if (uri != null && !"".equals(uri)) {
			fullName += uri + File.separator;
		}
		fullName += xlsName; 
		return fullName;
	}
	
	/**
	 * ȡ��ģ�����
	 * @return ȡ��ģ����� 
	 */
	public TemplateObj getTemplateObj(){
		
		TemplateObj templateObj = new TemplateObj();		
		Element rootEle = doc.getRootElement();		
		Element templateEle = (Element)rootEle.selectSingleNode("templates/template[@version='"+ versionID +"' and @repID='"+ templateID +"']");
		
		if (templateEle != null) {
			templateObj.setRepID(this.getTemplateID());
			templateObj.setVersionID(this.getVersionID());
			templateObj.setExcelName(this.getExcelName());
			templateObj.setMode(templateEle.valueOf("@mode"));
			templateObj.setUri(templateEle.valueOf("@uri"));
			templateObj.setGuid(templateEle.valueOf("@guid"));
			List dList = templateEle.selectNodes("datasource");
			HashMap dataMap = new HashMap();
			for (int i=0,n=dList.size(); i<n; i++) {
				Element dsoEle = (Element)dList.get(i);
				DataSourceObj dso = new DataSourceObj();
				String guid = dsoEle.valueOf("@guid");
				dso.setSheetName(dsoEle.valueOf("@sheetName")); 
				dso.setXlsName(dsoEle.valueOf("@xlsName"));
				dso.setState(dsoEle.valueOf("@state"));
				dso.setUri(dsoEle.valueOf("@uri"));
				dataMap.put(guid, dso);				
			}
			templateObj.setDataSourceMap(dataMap);
		}
		
		return templateObj;
	}	
	
	
	/**
	 * ����ģ��ĳһ�����Ե�ֵ
	 * @param AttibuteName����������
	 * @return
	 */
	public String getTemplateAttributeValue(String attributeName){
		String value = null;
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		Element rootEle = doc.getRootElement();
		
		Attribute attr = (Attribute)rootEle.selectSingleNode("templates/template[@repID='"+ repID +"' and @version='"+ version +"']/@" + attributeName);
		if (attr != null) {
			value = attr.getText();
		}
		
		return value;
	}
	
	/**
	 * ��������Դ��ĳһ�����Ե�ֵ
	 * @param attributeName����������
	 * @return����������Դ��ĳһ�����Ե�ֵ
	 */
	public String getDatasourceAttributeValue(String guid, String attributeName){
		String value = null;
		if ((guid != null && !"".equals(guid)) 
				&& (attributeName != null && !"".equals(attributeName))) {			
			String repID = this.getTemplateID();
			String version = this.getVersionID();
			Element rootEle = doc.getRootElement();
			Attribute attr = (Attribute)rootEle.selectSingleNode(
					"templates/template[@repID='"+ repID +"' and @version='"+ version +"']" +
					"/datasource[@guid='"+ guid +"']/@" + attributeName);
			if (attr != null) {
				value = attr.getText();
			}
		}
		
		return value;
	}
	
	/**
	 * ȡ������Դ����
	 * @param sheetName������Ӧ��sheet����
	 * @return������Դ����
	 */
	public DataSourceObj getDataSourceObj(String guid){
		DataSourceObj dataObj = null;
		Element rootEle = doc.getRootElement();	
		Element dataEle = (Element)rootEle.selectSingleNode(
				"templates/template[@version='"+ versionID +"' and @repID='"+ templateID +"']/datasource[@guid='"+ guid +"']");
		if (dataEle != null) {
			dataObj = new DataSourceObj();
			dataObj.setGuid(guid);
			dataObj.setSheetName("@sheetName");
			dataObj.setState(dataEle.valueOf("@state"));
			dataObj.setXlsName(dataEle.valueOf("@xlsName"));
			dataObj.setUri(dataEle.valueOf("@uri"));
		}
		return dataObj;
	}
	
	/**
	 * ���DataSource state�������Ƿ���temp
	 * @return DataSource state��������ֵΪtemp����false ���򷵻�true
	 *
	 */
	public boolean checkDataSourceElement(){
		boolean resultFlag = true;
		Element rootEle = doc.getRootElement();
		List invaildElements = rootEle.selectNodes("templates/template[@version='"+ versionID +"' and @repID='"+ templateID +"']/datasource[@state='"+ Config.DATA_SOURCE_STATE_TEMP +"']");
		if (invaildElements != null) {
			if (invaildElements.size() > 0) {
				resultFlag = false;
			}
		}
		return resultFlag;
	}
	
	/**
	 * ��ȡDatasource�ڵ�ģ��������Ժ��ļ���
	 * @return��HashMap key:@guid value:@xlsName
	 */
	public HashMap getDatasourceXlsName(){
		HashMap map = new HashMap();
		Element rootEle = doc.getRootElement();
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		
		List dsoList = rootEle.selectNodes("templates/template[@version='"+ versionID +"' and @repID='"+ templateID +"']/datasource");
		if (dsoList != null){
			for(int i=0,n=dsoList.size(); i<n; i++){
				Element dso = (Element)dsoList.get(i);
				map.put(dso.valueOf("@guid"), dso.valueOf("@xlsName"));
			}
		}
		
		return map;
	}
	
	/**
	 * ���DataSource,
	 * �ж�ÿ��datasource�ڵ��state����,�����temp��ɾ���ڵ�
	 *
	 */
	public HashMap checkAndDelInvalidEle(){
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		HashMap invalidMap = new HashMap();
		Element rootEle = doc.getRootElement();
		Element templateEle = (Element)rootEle.selectSingleNode("templates/template[@version='"+ versionID +"' and @repID='"+ templateID +"']");
		if (templateEle != null) {			
			List invaildElements = templateEle.selectNodes("datasource[@state='"+ Config.DATA_SOURCE_STATE_TEMP +"']");
			if (invaildElements != null) {
				int n = invaildElements.size();
				for(int i=0; i<n; i++){				
					Element dsoe = (Element)invaildElements.get(i);
					String guid = dsoe.valueOf("@guid");
					invalidMap.put(guid, guid);
					templateEle.remove(dsoe);
				}
				if (n > 0) {				
					XmlParser.saveXml(doc, Config.getObtainConfigFilePath(), "UTF-8");
				}
			}
		}
		return invalidMap;
	}	
	
	/**
	 * �޸�����Դ�ڵ������
	 * @param guid ������Դ���Ե�GUID
	 * @param attributeName ��Ҫ�޸ĵ���������
	 * @param attributeValue �޸ĺ����ֵ
	 */
	public void updateDataSourceAttribute(String guid,
									      String attributeName,
										  String attributeValue){
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		Element rootEle = doc.getRootElement();
		if ((repID != null && !"".equals(repID))
				&& (version != null && !"".equals(version))
				&& (attributeName!= null && !"".equals(attributeName)) 
				&& (attributeValue != null && !"".equals(attributeValue))
				&& (guid != null && !"".equals(guid))) {			
			Attribute attr = (Attribute)rootEle.selectSingleNode(
					"templates/template[@repID='"+ repID +"' and @version='"+ version +"']/datasource[@guid='"+ guid +"']/@" + attributeName);
			if (attr != null) {
				attr.setValue(attributeValue);
			}
		}
		
	}
	
	
	/**
	 * ����ģ��������Ϣ
	 * @param attributeName ��Ҫ�޸ĵ���������
	 * @param attributeValue �޸ĺ����ֵ
	 */
	public void updateTemplateAttribute(String attributeName,
										String attributeValue){
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		Element rootEle = doc.getRootElement();
		if ((repID != null && !"".equals(repID))
				&& (version != null && !"".equals(version))
				&& (attributeName!= null && !"".equals(attributeName)) 
				& (attributeValue != null && !"".equals(attributeValue))) {			
			Attribute attr = (Attribute)rootEle.selectSingleNode("templates/template[@repID='"+ repID +"' and @version='"+ version +"']/@" + attributeName);
			if (attr != null) {
				attr.setValue(attributeValue);
			}
		}
		XmlParser.saveXml(doc, Config.getObtainConfigFilePath(), "UTF-8");
	}
	
	/**
	 *  �Ƴ���ģ��
	 *
	 */
	public void deleteTemplate(){
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		Element rootEle = doc.getRootElement();
		if ((repID != null && !"".equals(repID))
				&& (version != null && !"".equals(version))) {
			Element templatesEle = (Element)rootEle.selectSingleNode("templates");
			Element deletedObj = (Element)templatesEle.selectSingleNode("template[@repID='"+ repID +"' and @version='"+ version +"']");
			if (deletedObj != null){
				templatesEle.remove(deletedObj);
			}
		}
		XmlParser.saveXml(doc, Config.getObtainConfigFilePath(), "UTF-8");
	}
	
	/**
	 * ɾ������Դ�ڵ�
	 * @param guid ��ɾ��������Դ��GUID
	 */
	public void deleteDataSource(String guid){
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		Element rootEle = doc.getRootElement();
		if ((repID != null && !"".equals(repID))
				&& (version != null && !"".equals(version)) 
				&& (guid != null && !"".equals(guid))) {
			Element templateEle = (Element)rootEle.selectSingleNode("templates/template[@repID='"+ repID +"' and @version='"+ version +"']");
			Element deletedObj = (Element)templateEle.selectSingleNode("datasource[@guid='"+ guid +"']");
			if (deletedObj != null){
				templateEle.remove(deletedObj);
			}
		}		
	}
	
	
	/**
	 * ����ģ������Դ��Ϣ ���dso == null����ɾ�����������ֵ,
	 * ���δ�ҵ���Ӧ�ڵ��������ýڵ�

	 * @param sheetName ��Ҫ�޸ĵ�����Դ��ӦSheet����
	 * @param dso ����Դ����
	 */
	public void updateTemplateDatasource(String guid,
										 DataSourceObj dso){
		String repID = this.getTemplateID();
		String version = this.getVersionID();
		Element rootEle = doc.getRootElement();
		if ((repID != null && !"".equals(repID))
				&& (version != null && !"".equals(version)) 
				&& (guid != null && !"".equals(guid))){
			Element templateEle = (Element)rootEle.selectSingleNode("templates/template[@repID='"+ repID +"' and @version='"+ version +"']");
			
			Element dataEle = null;
			if (templateEle != null){				
				dataEle = (Element)templateEle.selectSingleNode(
						"datasource[@guid='"+ guid +"']");
				if (dataEle != null) {
					/*
					 * �����dso == null �������Ƴ��������
					 */
					if (dso == null) {
						/* remove����ֻ���Ƴ���ֱ���ӽڵ㣬
						 * ����Ҫ������ȡtemplateEle��dataEle
						 */
						templateEle.remove(dataEle);						
					}else{
						dataEle.addAttribute("sheetName", dso.getSheetName());
						dataEle.addAttribute("xlsName", dso.getXlsName());
						dataEle.addAttribute("state", dso.getState());
					}
				}else if (dso != null){
					dataEle = DocumentHelper.createElement("datasource");
					dataEle.addAttribute("guid", guid);
					dataEle.addAttribute("sheetName", dso.getSheetName());
					dataEle.addAttribute("xlsName", dso.getXlsName());
					dataEle.addAttribute("state", dso.getState());
					templateEle.add(dataEle);
				}
			}
		XmlParser.saveXml(doc, Config.getObtainConfigFilePath(), "UTF-8");
		}
	}
	
	/**
	 * ����һ���µ�ģ��ڵ�
	 * @param guid����ģ���guid
	 */
	public void createNewTemplateNode(String guid){
		
		Element rootEle = doc.getRootElement();
		Element templatesEle = (Element)rootEle.selectSingleNode("templates");
		Element templateEle = DocumentHelper.createElement("template");
		templateEle.addAttribute("guid", guid);
		templateEle.addAttribute("repID", this.getTemplateID());
		templateEle.addAttribute("version", this.getVersionID());
		templateEle.addAttribute("mode", Config.OBTAIN_MODE_EXCEL);
		templateEle.addAttribute("uri", Config.OBTAIN_TEMPLATE);
		templatesEle.add(templateEle);
		XmlParser.saveXml(doc, Config.getObtainConfigFilePath(), "UTF-8");
	}
	
	/**
	 * ����ģ�������ļ�
	 *
	 */
	public void saveTemplateDoc(){
		XmlParser.saveXml(doc, Config.getObtainConfigFilePath(), "UTF-8");
	}
	/**
	 * @return Returns the templateID.
	 */
	public  String getTemplateID() {
		return templateID;
	}
	/**
	 * @param templateID The templateID to set.
	 */
	private void setTemplateID(String templateID) {
		this.templateID = templateID;
	}
	/**
	 * @return Returns the versionID.
	 */
	public String getVersionID() {
		return versionID;
	}
	/**
	 * @param versionID The versionID to set.
	 */
	private void setVersionID(String versionID) {
		this.versionID = versionID;
	}
	/**
	 * @return Returns the folder.
	 */
	public String getFolder() {
		return folder;
	}
	/**
	 * @param folder The folder to set.
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}
	/**
	 * @return Returns the excelName.
	 */
	public String getExcelName() {
		return excelName;
	}
	/**
	 * @param excelName The excelName to set.
	 */
	private void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	
	public static void main(String args[]){
		
		TemplateObtainConfigure t = new TemplateObtainConfigure("0001", "0512");
		//// System.out.println("fullP = " + t.getTemplateFullPath());
		//// System.out.println("dsFullp = " + t.getDatasourceFullPath("targetSheet1"));
		//TemplateObj to = t.getTemplateObj();
//		// System.out.println("to.regID = " + to.getExcelName());
//		// System.out.println("to.mode = " + to.getMode());
//		// System.out.println("to.uri = " + to.getUri());
//		HashMap tmpMap = to.getDataSrouceMap();
//		Iterator iter = tmpMap.keySet().iterator();
//		while(iter.hasNext()){
//			// System.out.println("*****************************************");
//			String key = (String)iter.next();
//			DataSourceObj dso = (DataSourceObj)tmpMap.get(key);
//			// System.out.println("dso.sheetName = " + dso.getSheetName());
//			// System.out.println("dso.xlsName = " + dso.getXlsName());
//			// System.out.println("dso.state = " + dso.getState());
//			// System.out.println("dso.uri = " + dso.getUri());
//			// System.out.println("*****************************************");
//		}
		
//		// System.out.println("to.regID = " + t.getTemplateAttributeValue("mode"));
//		// System.out.println("to.uri = " + t.getTemplateAttributeValue("uri"));
//		DataSourceObj dso = t.getDataSourceObj("targetSheet1");
//		// System.out.println(dso.getSheetName());
//		// System.out.println(dso.getState());
//		// System.out.println(dso.getUri());
//		// System.out.println(dso.getXlsName());
		
		//t.updateTemplateAttribute("mode", "txt");
		DataSourceObj dso = new DataSourceObj();		
		//dso.setState("saved");
		dso.setUri("dd");
		dso.setXlsName("wng.xls");
		t.updateTemplateDatasource("targetSheet1", dso);
		XmlParser.saveXml(t.doc, "D:\\eclipse\\workspace\\smis_in_net\\WebRoot\\system\\obtainConfig.xml", "UTF-8");
	}
	

    
}
