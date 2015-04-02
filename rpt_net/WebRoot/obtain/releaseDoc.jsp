<%@ page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="com.fitech.net.config.Config"%>
<%@ page import="com.fitech.net.obtain.excel.SheetHandle"%>
<%@ page import="java.io.File"%>
<jsp:useBean id="myUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />
<%
   response.addHeader("Pragma", "no-cache");
   response.addHeader("Cache-Control", "no-cache");
   response.addHeader("Expires", "Thu,01 Jan 1970 00:00:01 GMT");
%>

<%
    myUpload.initialize(pageContext);	
    myUpload.setMaxFileSize(1048576);
    myUpload.upload();
    
    com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);
    String fName = myUpload.getFiles().getFile(0).getFileName(); 
    String ObtainTemplates = Config.getObtainTemplateFolderRealPath() + File.separator + fName;
    String releaseTemplates = Config.getReleaseTemplatePath() + File.separator + fName;
    
    //fName = Config.getObtainTemplateFolderRealPath() + File.separator + fName;
 
    myFile.saveAs(ObtainTemplates);
    /*
     *ÐÞ¸ÄÅäÖÃÎÄ¼þ
     */
     String repID = request.getParameter("repID");
     String versionID = request.getParameter("versionID"); 
     SheetHandle sh = new SheetHandle(repID, versionID);
     sh.releaseTemplate(ObtainTemplates, releaseTemplates, fName);
%>