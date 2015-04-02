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
	String orgId = request.getParameter("orgId");
    String year = request.getParameter("year") != null ? request.getParameter("year") : "";
    String term = request.getParameter("term") != null ? request.getParameter("term") : "";
    
    pageContext.getServletContext().getRealPath("/");
	pageContext.getServletContext().getRealPath("/WEB-INF/upload");
	pageContext.getServletContext().getRealPath("/WEB-INF/web.xml");

    myUpload.initialize(pageContext);	
    myUpload.setMaxFileSize(1048576);
    myUpload.upload();
    
    com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);
    String fName = myUpload.getFiles().getFile(0).getFileName(); 
    
    String excelFilePath = Config.getCollectExcelFolder() + File.separator  + year + "_" + term + File.separator +  orgId;
   
    File excelFile = new File(excelFilePath);
    if(!excelFile.exists()) excelFile.mkdirs();
    
    fName = excelFilePath + File.separator + fName;

   	myFile.saveAs(fName); 
  
    String versionId = request.getParameter("versionId");
    String childRepId = request.getParameter("childRepId");
    
    ReportInForm reportInForm = new ReportInForm();
    reportInForm.setOrgId(orgId);
    reportInForm.setChildRepId(childRepId);
    reportInForm.setVersionId(versionId);
    
    InputData.isOnLine = null;
    InputData.messageInfo = "";
    InputData inputData = new InputData();
    File xmlFile = inputData.getXMLFile(childRepId,versionId,orgId);
   
    
    try{
    	boolean bool = inputData.conductOnLineJYXML(xmlFile,reportInForm,year,term);
    	if(bool){
    		com.cbrc.smis.system.cb.InputData.isOnLine = "true";
    		com.cbrc.smis.system.cb.InputData.messageInfo= "����У����ϣ�����У����ϸҳ�棡";
    	%>
	    	<script language="javascript">
	    		alert("����У����ϣ�");
	    	</script>
    	<%
    	}else{
    		com.cbrc.smis.system.cb.InputData.isOnLine = "true";
    		if(com.cbrc.smis.system.cb.InputData.messageInfo.equals(""))
    			com.cbrc.smis.system.cb.InputData.messageInfo= "����У��ʧ�ܣ�";
    		else if(com.cbrc.smis.system.cb.InputData.messageInfo.indexOf("������ʧ�ܣ������뱨�����ڣ�") > -1)
    			com.cbrc.smis.system.cb.InputData.messageInfo="����У��ʧ�ܣ������뱨�����ڣ�";
    	%>
	    	<script language="javascript">
	    		alert("����У��ʧ�ܣ�");
	    	</script>
    	<%
    	}
    }catch(Exception ex){
    	com.cbrc.smis.system.cb.InputData.isOnLine = "true";
    	if(com.cbrc.smis.system.cb.InputData.messageInfo.equals(""))
    		com.cbrc.smis.system.cb.InputData.messageInfo= "����У��ʧ�ܣ�";
    	else if(com.cbrc.smis.system.cb.InputData.messageInfo.indexOf("������ʧ�ܣ������뱨�����ڣ�") > -1)
    			com.cbrc.smis.system.cb.InputData.messageInfo="����У��ʧ�ܣ������뱨�����ڣ�";
    %>
    	<script language="javascript">
    		alert("����У��ʧ�ܣ�");
    	</script>
    <%
    }
%>