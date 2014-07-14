<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.cbrc.smis.form.MActuRepForm" %>

<jsp:useBean id="collectUtil" class="com.fitech.dataCollect.DB2ExcelHandler"></jsp:useBean>
<html:html locale="true">
<head>
<html:base/>
<title>汇总数据列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<link href="../css/common.css" type="text/css" rel="stylesheet">
<script language="javascript" src="../js/comm.js"></script>
</head>
<body>
<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="96%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 报表管理 >> 数据汇总下载
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="80%" align="center">
		<html:form action="/viewCollectDown" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
							<tr>
								<td>
									报表时间：
									<html:text property="year" maxlength="5" size="5" styleClass="input-text" />
									年
									
									<select name="setDate">
								    <option value="01">1月</option>
								    <option value="02">2月</option>
									<option value="03">3月</option>
									<option value="04">4月</option>
									<option value="05">5月</option>
									<option value="06">6月</option>
									<option value="07">7月</option>
									<option value="08">8月</option>
									<option value="09">9月</option>
									<option value="10">10月</option>
									<option value="11">11月</option>
									<option value="12">12月</option>
								  </select>									
								</td>
								<td>
									<html:submit styleClass="input-button" value=" 查 询 " />
								</td>
							</tr>
						</table>
						
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
<br>
  <table width="80%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
	<tr class="titletab" id="tbcolor"> 
    	<th height="24" align="center" id="list" colspan="4">汇总数据下载列表</th>
  	</tr>
  <tr>
  		   <TR class="middle">			 
			 <TD width="10%" align="center"><strong>类型</strong></TD>
			 <TD align="center"><strong>文件名</strong></TD>
			 <TD width="20%" align="center"><strong>数据调平</strong></TD>
			 <TD width="10%" align="center"><strong>下载</strong></TD>
			</TR>
			<logic:present name="Records" scope="request">
			 <logic:iterate id="iterm" name="Records">
				<tr  bgcolor="#FFFFFF">
				  
				  <td align="center"><img src="../image/excel.bmp" ></td>
				  <td align="center"><bean:write name="iterm" property="name" /></td> 
				  <td align="center"><input type="checkbox"></td>
				  <td align="center"><a href="<bean:write name="iterm" property="url" />">下载</a></td>
				</tr>
			 </logic:iterate>
			</logic:present>
			
			 <logic:notPresent name="Records" scope="request">
			<tr bgcolor="#FFFFFF">
		  	<td colspan="4">
		  	   暂无下载数据
		  	</td>
		  </tr>
		   </logic:notPresent>

</table>
<logic:present name="Records" scope="request">
<br>
<input type="button" class="input-button"  value="全部下载">
</logic:present>
</body>
</html:html>