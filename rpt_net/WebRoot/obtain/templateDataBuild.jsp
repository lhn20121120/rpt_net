<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
        <head>	
	    <title>模板列表</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=gbk">
	    <meta http-equiv="Pragma" content="no-cache">
	    <meta http-equiv="Cache-Control" content="no-cache">
	    <meta http-equiv="Expires" content="0">
	    <LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	    <script language="JavaScript" src="<bean:write name="obtain_js"/>"></script>
	</head>
	<body onload="javascript:document.all.FramerControl1.Open('<bean:write name="reportUrl"/>',true,'Excel.Sheet');">	
           <table width="95%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
              <tr class="titletab">
                 <td>
                    <INPUT class="input-button" id="Button2" type="button" value=" 数据发布 " name="ButtonSave" onclick="javascript:createExcel('<bean:write name="guid"/>','<bean:write name="excelName"/>','<bean:write name="repID"/>','<bean:write name="versionID"/>');">
                 </td>
              </tr>
              <tr>
      	         <td bgcolor="#ffffff">
      		   <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
		          <tr>
				      <TD width="100%" height="100%" align=center >
					  <object classid="clsid:00460182-9E5E-11d5-B7C8-B8269041DD57"  CodeBase="<bean:write name="ocx"/>#V1,4,0,4" id="FramerControl1" width="800" height="530">
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
	<script language="javascript">
		function createExcel(guid,excelName,repID,versionID){
			window.location="<%=request.getContextPath()%>/templateDataBuild.do?guid="+guid+"&excelName="+excelName+"&repID="+repID+"&versionID="+versionID;
		}
	</script> 
</html:html>