package com.fitech.net.obtain.excel.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;

/**
 * @机构Action
 * @author jcm
 *
 */
public final class ClearHistoryAction extends Action {
	private FitechException log=new FitechException(ClearHistoryAction.class);
	
	
   /**
    * @param result 查询返回标志,如果成功返回true,否则返回false 
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
	   
		
		//String childRepId = request.getParameter("childRepId");
		//String versionId = request.getParameter("versionId");
		
		//String fileName = childRepId + "_" +versionId + ".xls";
		
		String obtainTemplate = com.fitech.net.config.Config.getObtainTemplateFolderRealPath();
		String releaseTemplate = com.fitech.net.config.Config.getReleaseTemplatePath();
		
		MChildReportForm mChildReportForm = (MChildReportForm) form;
		
		//先删除配置文件信息
		if(this.restoreConfig()){
			try{
				File obtainFile = new File(obtainTemplate);
				File releaseFile = new File(releaseTemplate);
				
				File[] releaseFileList = releaseFile.listFiles();
				for(int i=0;i<releaseFileList.length;i++){
					if(releaseFileList[i].getName().indexOf(".xls") > -1){
						releaseFileList[i].delete();
					}
				}
				String mostLowerOrgIds = StrutsOrgNetDelegate.getMostLowerOrgIds();
				String[] orgIds = mostLowerOrgIds.split(",");
				for(int i=0;i<orgIds.length;i++){
					String orgFileName = releaseTemplate + File.separator +orgIds[i];
					File orgFile = new File(orgFileName);
					File[] orgFiles = orgFile.listFiles();
					if(orgFiles == null) orgFiles = new File[0];
					for(int j=0;j<orgFiles.length;j++){
						if(orgFiles[j].getName().indexOf(".xls") > -1){
							orgFiles[j].delete();
						}
					}
				}
				
				File[] obtainFileList = obtainFile.listFiles();
				for(int i=0;i<obtainFileList.length;i++){
					if(obtainFileList[i].getName().indexOf(".xls") > -1){
						boolean bool = obtainFileList[i].delete();
					}
				}
				
				messages.add(resources.getMessage("clear.history.success"));
			}catch(Exception ex){
				ex.printStackTrace();
				
			}
		}else{
			messages.add(resources.getMessage("clear.history.failed"));		
		}
		
	 	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
	 	 
	 	 //return mapping.findForward("view");
	 	 return new ActionForward("/viewTemplateNet.do?childRepId=&versionId=");
	}
	
	private boolean clearConfig(String childRepId,String versionId){
		boolean bool =false;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder;
		
		String configFile = Config.WEBROOTPATH + File.separator + "system" + File.separator + "obtainConfig.xml";
		try {
			builder = factory.newDocumentBuilder();			
			Document document = (Document) builder.parse(new File(configFile));			
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
	         for(int i=0;i<nodeList.getLength();i++){
	         	Node node = nodeList.item(i);
	         	if(node.getNodeName().equals("templates")){
	         		NodeList templatesNodeList = node.getChildNodes();
	         		for(int j=0;j<templatesNodeList.getLength();j++){
	         			Node templateNode = templatesNodeList.item(j);
	         			if(templateNode.getNodeName().equals("template")){
	         				NamedNodeMap map = templateNode.getAttributes();
	         				if(map != null && map.getLength() != 0){
	         					String repID = map.getNamedItem("repID").toString().replaceAll("\"","").substring(("repID=").length());
	         					String versionID = map.getNamedItem("version").toString().replaceAll("\"","").substring(("version=").length());
	         					
	         					if(repID.equals(childRepId) && versionID.equals(versionId)){
	         						Node pareNode = templateNode.getParentNode();
	         						pareNode.removeChild(templateNode);
	         						
	         						TransformerFactory tFactory = TransformerFactory.newInstance();
	         					    Transformer transformer = tFactory.newTransformer();
									
	         					    DOMSource source = new DOMSource(document);
	         					    StreamResult result = new StreamResult(new File("C:\\obtainConfig.xml"));
	         					    transformer.transform(source, result);
	         					    bool = true;
	         					    break;
	         					}
	         				}
	         			}	
	         		}
	         	}
	         }
		}catch (ParserConfigurationException e) {
	 			// TODO Auto-generated catch block
			bool = false;
	 		e.printStackTrace();
	 	} catch (SAXException e) {
	 			// TODO Auto-generated catch block
	 		bool = false;
	 		e.printStackTrace();
	 	} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
	 		bool = false;
			e.printStackTrace();
		}catch (TransformerException e) {
				// TODO Auto-generated catch block
			bool = false;
			e.printStackTrace();
		}catch (IOException e) {
	 			// TODO Auto-generated catch block
			bool = false;
	 		e.printStackTrace();
	 	} 
		return bool;
	}
	
	private boolean restoreConfig(){
		boolean bool = false;
		String srcFileName = Config.WEBROOTPATH + "system" + File.separator + "back.xml";
		String objFileName = Config.WEBROOTPATH + "system" + File.separator + "obtainConfig.xml"; 
        
		try {
			FileInputStream inStream = new FileInputStream(srcFileName);
			byte butter[] = new byte[inStream.available()];
	        inStream.read(butter);
	        
	        File file = new File(objFileName);
	        if(file.exists()) file.delete();
	        
	        FileOutputStream stream = new FileOutputStream(objFileName);
	        stream.write(butter);
	        inStream.close();
	        stream.close();
	        bool = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			bool = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			bool = false;
			e.printStackTrace();
		}
		return bool;
	}
}