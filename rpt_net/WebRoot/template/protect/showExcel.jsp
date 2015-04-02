<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String childRepId = request.getParameter("childRepId");
	String versionId = request.getParameter("versionId");
	String ocx=request.getParameter("ocx");
	String reportUrl = request.getParameter("reportUrl");
%>
<jsp:useBean id="utilCellForm" scope="page" class="com.fitech.net.form.UtilCellForm" />
<jsp:setProperty property="childRepId" name="utilCellForm" value="<%=childRepId%>"/>
<jsp:setProperty property="versionId" name="utilCellForm" value="<%=versionId%>"/>

<html:html locale="true">
        <head>	
	    <title>模板列表</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=gbk">
	    <meta http-equiv="Pragma" content="no-cache">
	    <meta http-equiv="Cache-Control" content="no-cache">
	    <meta http-equiv="Expires" content="0">
	    <LINK REL="StyleSheet" HREF="<%=request.getParameter("css")%>" TYPE="text/css">
	    <script language="JavaScript" src="<%=request.getParameter("obtain_js")%>"></script>
	</head>
	<body onload="javascript:document.all.FramerControl1.Open('<%=request.getParameter("reportUrl")%>',true,'Excel.Sheet');">	
           <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbcolor"> 
           	  <tr>           
      	         <td bgcolor="#ffffff">
      		   		<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
		          		<tr>
				      		<TD width="100%" height="100%" align=center >
					  			<object classid="clsid:00460182-9E5E-11d5-B7C8-B8269041DD57"  CodeBase="<%=ocx%>#V1,4,0,4" id="FramerControl1" width="800" height="530">
					    			<param name="BorderStyle" value="0">
					    			<param name="TitlebarColor" value="52479">
					    			<param name="TitlebarTextColor" value="0">
					    			<param name="Menubar" value="0">					    
					 			</object>
				     		</TD>
			 	  		</tr>			 	  		
	           		</table>
      		 	</td>
      	      </tr>        	      
           </table>

	    <script language="VBScript">                 
	          Dim x
	          Dim y
	          Dim z
	          Dim m
	          On Error Resume Next
	   	  x = document.all.FramerControl1.Titlebar
	          document.all.FramerControl1.Titlebar = Not x   
	          y = document.all.FramerControl1.Toolbars
	          document.all.FramerControl1.Toolbars = Not y
	          z = document.all.FramerControl1.ModalState  
	          document.all.FramerControl1.ModalState = Not z  
	    </script>
	</body>   
</html:html>