
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <html:base />
    
    <title>targetDefine.jsp</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
    
    <jsp:include page="../calendar.jsp" flush="true">
		<jsp:param name="path" value="../" />
	</jsp:include>
  </head>
  
  <body>
  <table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 指标分析 >> 指标定义
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<br>
	<br>
	<table>
	
	    <html:form action="/targetDefine" method="post">
				<table border="0" width="50%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
					<TR class="tableHeader" >
						<TD width="15%" valign="bottom">
						指标设定
						</TD>
					</TR>
					
					<TR align="center" valign="middle" bgcolor="#FFFFFF">
					  <TD>
						<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						  <TR>
						    <TD align="right">指标名称：</TD>
						    <TD><html:text property="targetName" size="20" styleClass="input-text" maxlength="20"/></TD>
				          </TR>
				          <TR>
						    <TD align="right">指标类型：</TD>
						    <TD><select name="targetType">
					       <option selected>主要指标
					       <option>辅助指标
					       <option>特色指标
					       <option>合规指标
					  		</select></TD>
				          </TR>
				          <TR>
				          	<TD align="right">业务类型：</TD>
							<TD>
								<select name="targetOpType">
						       <option selected>信用风险
						       <option>流动性风险
						       <option>市场风险
						       <option>资本充足
						       <option>盈利性
						       <option>其他
					  			</select>
							</TD>
				          </TR>
				          <TR>
				          	<TD align="right">开始时间：</TD>
							<TD>
								<input type="text" name="startDate" size="10" value="" readonly="readonly" style="text" class="input-text"><img src="../image/calendar.gif" border="0" onclick="return showCalendar('startDate', 'y-mm-dd');">
							</TD>
				          </TR>
					      
					      <TR>
				          	<TD align="right">结束时间：</TD>
							<TD>
								<input type="text" name="endDate" size="10" value="" readonly="readonly" style="text" class="input-text"><img src="../image/calendar.gif" border="0" onclick="return showCalendar('endDate', 'y-mm-dd');">
							</TD>
				          </TR>
				          <TR>
				          	<TD align="right">公式定义：</TD>
							<TD>
								<input type="text" name="expreDefine" value="" size="40">
								
							</TD>
				          </TR>
				          <TR>
				          	<TD align="right">法律法规：</TD>
							<TD>
								<input type="text" name="lawDefine" value="" size="40">
							</TD>
				          </TR>
					      <TR>
					        <TD align="center" colspan="2">
				               <html:submit styleClass="input-button"  value="保存"/>
				            </TD>
				          </TR>
				        </table>
				      </TD>
					</TR>
				</table>
		</html:form>
	    
    </table>
  </body>
</html:html>
