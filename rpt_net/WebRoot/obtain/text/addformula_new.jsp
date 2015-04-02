<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html:html locale="true">
  <head>
      
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/func.js"></script>
    <script language="JavaScript" >
          function _submit()
			{
				
				var textname = document.getElementById('dataSourceEname');
				var organization = document.getElementById('organization');
				var excelname = document.getElementById('childReportId');
				var rowcolumn = document.getElementById('rowColumn');
				var separater = document.getElementById('splitChar');
				var version = document.getElementById('versionId');
				
				var formula=document.getElementById('formula');
				
				
//				 formula=selectf+column;
			//	 document.all.formula.value = selectf.value +"("+ column.value+")" ;
				
				if(formula.value=="")
				{
				alert("公式不能为空！");
					formula.focus();
					return false;
				}
				if(textname.value=="")
				{
					alert("text文件名不能为空！");
					textname.focus();
					return false;
				}
				if(organization.value=="" || isNaN(organization.value))
				{
					alert("请输入真确的机构列！");
					organization.focus();
					return false;
				}
				if(excelname.value=="")
				{
					alert("excel名字不能为空！");
					excelname.focus();
					return false;
				}
				if (version.value=="" || isNaN(version.value))
				{
				alert("请输入真确的版本号！");
					version.focus();
					return false;
				}
				
				
				
				if(rowcolumn.value=="" || isNaN(rowcolumn.value))
				{
					alert("请输入真确的行列号！");
					rowcolumn.focus();
					return false;
				}
				if(separater.value=="")
				{
					alert("分隔符不能为空！");
					separater.focus();
					return false;
				}
				
			}
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
<table border="0" width="100%" align="center">
		
		<tr>
			 <td>当前位置 >> 	ETL取数 >> 文本取数</td>
			 
		</tr>
		<tr><td height="10"></td></tr>
	</table>
<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">
	
	<tr> 
    	<td align="right" valign="top">
   <html:form method="post" action="/obtain/text/addformula" enctype="multipart/form-data"  onsubmit="return _submit()">
			<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
      				<tr id="tbcolor">
            			<th align="center">添加文本取数规则</th>
      				</tr>
   					<tr>
       					 <td height="204" align="right" bgcolor="#FFFFFF">          
						<table width="100%"  border="0" >
									
									<TR>
										<TD align="right">
											 text文件名
										</TD>
										<TD>
											<html:text styleId="dataSourceEname" property="dataSourceEname" size="20" styleClass="input-text" />
										</TD>
										<TD align="right"> 
										
										机构列
										
										</TD>
										<TD>
										<html:text styleId="orgId" property="orgId" size="20" styleClass="input-text" />
										</TD>
										
										</TR>
										
										
										<TR>
										<TD align="right"> &nbsp;模板ID</TD>
										<TD>
											<html:text styleId="childReportId" property="childReportId" size="20" styleClass="input-text" />
										</TD>
										<TD align="right">行列</TD>
										<TD>
											<html:text styleId="rowColumn" property="rowColumn" size="20" styleClass="input-text" />
										</TD></TR><tr>
										<td align="right">版本号</td>
										<td>
										<html:text styleId="versionId" property="versionId" size="20" styleClass="input-text" />
										</td>
										<td align="right"> &nbsp;分隔符</td>
										<td>
											<html:text styleId="splitChar" property="splitChar" size="20" styleClass="input-text" />
											
										</td></tr>
									
									<tr>
										<td align="right">
											 公式
										</td>
										<td colspan="3">
											<html:text styleId="formula" property="formula" size="20" style="width:100% " styleClass="input-text" />
										</td>
									</tr>
									<TR>
										<TD align="right">描述</TD>
										<TD colspan="3">
											<html:textarea  property="des"   rows="4" style="width:100% "/></TD></TR>
									<tr>
										<td colspan="6" align="right">
											<html:submit value="确定" styleClass="input-button" />
                   		 &nbsp;<html:button property="back" value="返回" styleClass="input-button" onclick="history.back()" />
										</td>
									</tr>
								</table>
		  </td>
   </tr>    
 </table>
 </html:form>
</body>

</html:html>
