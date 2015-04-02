<%@ page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="com.fitech.net.config.Config"%>
<%@ page import="java.io.File"%>
<%@ page import="com.cbrc.smis.form.ReportInForm" %>
<%@ page import="com.cbrc.smis.system.cb.InputData" %>
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
    
    fName = Config.REAL_ROOT_PATH + File.separator + "targetFumal" + File.separator + "temp" + File.separator + fName;	
    myFile.saveAs(fName);    
%>