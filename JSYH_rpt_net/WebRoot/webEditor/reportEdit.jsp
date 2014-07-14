<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
     <head>	
	    <title>报表编辑</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
	    <meta http-equiv="Pragma" content="no-cache">
	    <meta http-equiv="Cache-Control" content="no-cache">
	    <meta http-equiv="Expires" content="0">	
	    <LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	    <script language="JavaScript" src="<bean:write name="obtain_js"/>"></script>
    </head> 
    <body onload="javascript:document.all.FramerControl1.Open('<bean:write name="reportUrl"/>', true, 'Excel.Sheet');">	
       <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
          <tr class="titletab">
            <th align="center">
            	报表编辑
            </th>
         </tr>
	      <tr>
	         <td bgcolor="#ffffff">	         	
	            &nbsp;&nbsp;&nbsp;
	            <INPUT class="input-button" id="Button2" type="button" value=" 保存修改 " name="ButtonSave" onclick="javascript:objSave('<bean:write name="saveUrl"/>','<bean:write name="reportName"/>','<bean:write name="hrefUrl"/>');">
	            &nbsp;&nbsp;&nbsp;
	            <INPUT class="input-button" id="Button3" type="button" value=" 直接返回 " name="butBack" onclick="window.location.href='<bean:write name="hrefUrl"/>';">
	         </td>
	      </tr>         
         <tr>
      	    <td bgcolor="#ffffff">
      		   <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
		          <tr>
				      <TD width="100%" height="100%" align=center >
					  <object classid="clsid:00460182-9E5E-11d5-B7C8-B8269041DD57"  CodeBase="<bean:write name="ocx"/>#V1,4,0,4" id="FramerControl1" width="100％" height="650">
					    <param name="BorderStyle" value="1">
					    <param name="TitlebarColor" value="52479">
					    <param name="TitlebarTextColor" value="0">
					    <param name="Titlebar" value="0">
						<param name="Menubar" value="0">
						<param name="Toolbars" value="0">					    
					 </object>
				     </TD>  
				   </tr>
	           </table>
      	</td>
      </tr>
      </table>
    <script language="VBScript">                 
          Dim x
   		  On Error Resume Next
   		  x = document.all.FramerControl1.Titlebar
          document.all.FramerControl1.Titlebar = Not x           
    </script>  

    </body>   
</html:html>    