<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String childRepId = request.getParameter("childRepId");
	String versionId = request.getParameter("versionId");
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
	    <LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	    <script language="JavaScript" src="<bean:write name="obtain_js"/>"></script>
	</head>
	<body onload="javascript:document.all.FramerControl1.Open('<bean:write name="reportUrl"/>',true,'Excel.Sheet');">	
           <table width="95%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor"> 
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
           <table width="95%" border="0" align="center">
           		<tr class="titletab">
                 <td>
      		    	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		      		<html:form action="/template/saveProTmpt.do" enctype="multipart/form-data">
      		          		<input type="hidden" name="childRepId" value="<bean:write name="childRepId"/>">
      		          		<input type="hidden" name="versionId" value="<bean:write name="versionId"/>">
      		          		<input type="hidden" name="excelName" value="<bean:write name="excelName"/>">
      		       		<tr>
      		          		<td valign="top">&nbsp;
								单元格项：
								<html:select property="idrId">									
									<html:optionsCollection name="utilCellForm" property="cellList"/>
								</html:select>
							</td>
							<td>
								<table>
									<tr>
										<td>关联方式：</td>
										<td>
											<label><input type="radio" name="idrRelative" value="1" checked>业务系统生成</label><br>
			   		             		    <label><input type="radio" name="idrRelative" value="2">手工维护</label><br>
			   		             		    <label><input type="radio" name="idrRelative" value="3">计算项</label><br>	    
									  	</td>
									</tr>
							  </table>   		             		             		    
      		          		</td>
      		          		<td>
      		             		<INPUT class="input-button" id="Button3" type="button" value=" 直接返回 " name="butBack" onclick="window.history.back();">
      		          		</td>
      		      		</tr>
      		      		<tr>
      		      			<td><input type="checkbox" name="isInit" value="1">是否初始化数据</td>
      		      		</tr>
      		       		</html:form>
      		    	</table>
                 </td>
              </tr>  
              <tr>
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
		function radioOnChange(){
			
		}
	</script>  
</html:html>