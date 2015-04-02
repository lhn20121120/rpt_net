
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utiltargetDefineForm" scope="page" class="com.fitech.net.form.UtilTargetDefineForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<jsp:useBean id="figure" scope="request" class="com.fitech.net.bean.Figure"/>
<html:html locale="true">
<head>
	<html:base/>
<title> 指标定义</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<jsp:include page="../../calendar.jsp" flush="true">
		  <jsp:param name="path" value="../../"/>
		</jsp:include>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="JavaScript" >
          
    </script>
	
</head>
<body>
		<logic:present name="Message" scope="request">
			<logic:greaterThan name="Message" property="size" value="0">
				<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
			</logic:greaterThan>
		</logic:present>


   <table>
   <tr>
   <td height="10"></td>
   </tr>
   </table>
			<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="90%" border="0" class="tbcolor" align="center">
      				<tr id="tbcolor">
            			<th align="center">
	            			
	            			<strong>
		                    <font face="Arial" size="2">
												
			                      比上期信息 
												
		                     </font>
	                         </strong>
            			</th>
      				</tr>
   					<tr>
       					 <td height="204" align="right" bgcolor="#FFFFFF">   
       					  <html:form  action="/target/viewPreStandDetail" method="Post" styleId="form1" >
       					        	
						<table width="100%"  border="0" >
									
									<TR>
										<TD align="right">
											 指标名称
										</TD>
										<TD>
										<html:text styleId="targetDefineName" property="targetDefineName" size="20" styleClass="input-text" readonly="true" />
										</TD>
										<TD align="right"> &nbsp;</TD>
										
										</TR><TR>
										<TD align="right"> &nbsp;比上期值</TD>
										<TD>
										<html:text styleId="preStandardValue" property="preStandardValue" size="20" styleClass="input-text" readonly="true" />
											
										</TD>
										<TD align="right">下限</TD>
										<TD>
										<html:text styleId="temp1" property="temp1" size="15" styleClass="input-text" style="text" readonly="true" />
										</TD></TR><tr>
										<td align="right">上限</td>
										<td>
										<html:text styleId="temp2" property="temp2" size="20" styleClass="input-text"  readonly="true"/>
										</td>
										<td align="right"> &nbsp;颜色</td>
										<TD >
						  						<logic:present name="actuTargetResultForm" property="color">							
														<label  style="background:<bean:write name="actuTargetResultForm" property="color"/>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
												</logic:present>					
										</TD>
					                </tr>
									<tr>
										<td align="right">公式定义</td>
										<td colspan="3">
											<html:text  property="preFormula" size="70" styleClass="input-text" readonly="true" />
										</td>
									</tr>
									
								</table>
							
							</html:form>
		  </td>
   </tr>    
 </table>
 <logic:present name="figure" scope="request">
 <TABLE name="tb2" id="tb2" cellSpacing="1" cellPadding="1" width="90%" border="0" class="tbcolor" align="center">
      	   <tr id="tbcolor">
            	<th align="center">
	            			
	            	<strong>
		            <font face="Arial" size="2">												
			                    图示 												
		            </font>
	                 </strong>
            	</th>
      		</tr>      	
			<TD width="100%" height="304" align="right" bgcolor="#FFFFFF">
					<jsp:include page="../figure/warnFigure.jsp" flush="true">
						<jsp:param name="targetName"          value="<%=figure.getTargetDefineName()%>"/>
						<jsp:param name="allWarnMessageColor" value="<%=figure.getAllWarnMessage()%>"/>
						<jsp:param name="currentValue"        value="<%=figure.getCurrentValue()%>"/>	
					</jsp:include>
			</TD>		 
 </TABLE>
 </logic:present>
	<table width="86%">
	<tr>
	<td colspan="6" align="right">
	 <html:button property="back" value="返  回" styleClass="input-button" onclick="history.back()" />
    </td>
    </tr>
    </table>
    <br>
</body>

</html:html>