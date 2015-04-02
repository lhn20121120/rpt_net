<%@ page contentType="text/html;charset=GBK"%>
<jsp:directive.page import="com.cbrc.smis.common.Config" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.apache.struts.util.RequestUtils"%>
<%@ page import="com.fitech.net.form.TargetDefineForm" %>

<html:html locale="true">
     <head>	
	    <title>在线报送</title> 
	    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
	    <meta http-equiv="Pragma" content="no-cache">
	    <meta http-equiv="Cache-Control" content="no-cache">
	    <meta http-equiv="Expires" content="0">		    
	    <LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	    <script language="JavaScript" src="<bean:write name="obtain_js"/>"></script>
	     <script type="text/javascript">
	     //保存公式
	      function saveFormula(saveUrl,reportName,hrefUrl)
		    { 
			   try
			   {
				   var saveResult;
				
				   document.all.FramerControl1.DsoHttpInit();
			
				   document.all.FramerControl1.DsoHttpAddPostCurrFile("reportFile", reportName); 
				   saveResult = document.all.FramerControl1.DsoHttpPost(saveUrl);
				 
				 	if(saveResult){
				 		alert("保存成功!");
				 	
				 	}
				   window.location.href=hrefUrl;
				  
			   	}
				catch(e)
				{
					alert('系统忙，请稍后再试...！');
				}
			}
	     
	     
	     </script>
    </head> 

    
    
    
   
    <body onload="javascript:document.all.FramerControl1.Open('<bean:write name="fumalUrl"/>', true, 'Excel.Sheet');">	    
       <table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
       <html:form action="/target/targetSave.do">
          <tr class="titletab">
          	<th align="center">
            	指标公式定义
          	</th>
          </tr>         
	      <tr>
	         <td bgcolor="#ffffff">	            	         
	         	<INPUT class="input-button" id="Button2" type="button" value="保存公式"  onclick="saveFormula('<bean:write name="saveUrl"/>','<bean:write name="reportName"/>','<bean:write name="hrefUrl"/>');">
	         	<INPUT class="input-button" id="Button2" type="button" value="返    回" onclick="javascript:history.go(-1)">
	         </td>
	      </tr>	      
	      <tr>
      	    <td bgcolor="#ffffff">
      		   <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
		          <tr>
				      <TD width="100%" height="100%" align=center >
						  <script language="javascript" src="<%=Config.WEBROOTULR%>/js/createDSOFrame.js"></script>
				     </TD>
				   </tr>
	           </table>
      		</td>
      	</tr>
      </html:form>
      </table>
    </body>   
</html:html>    