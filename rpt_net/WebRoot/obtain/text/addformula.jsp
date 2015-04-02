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
	<html:form method="post" action="/obtain/text/addformula" enctype="multipart/form-data"  onsubmit="return _submit()">
  
  <table width="100%"   border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td>
        
        
        <logic:notPresent name="formula" scope="request">
      <table width="48%"  border="1" cellspacing="0" cellpadding="0" align="center" >
        <tr> &nbsp;</tr>
        <tr> &nbsp;</tr>
        <tr> &nbsp;</tr>
        <tr>
          <td width="29%" height="47" align="left"> 公式</td>
          <td width="71%" align="left">
		  
		
		  <input  class="input-text" type="text" name="formula">
			

							</td>
        </tr>
        <tr>
          <td  height="57" align="left">text文件名</td>
          <td  align="left"><input class="input-text" type="text" name="dataSourceEname">
          </td>
        </tr>
        <tr>
          <td height="56" align="left">机构列</td>
          <td align="left"><input class="input-text" type="text" name="orgId">
          </td>
        </tr>
        <tr>
          <td height="58" align="left">模板ID</td>
          <td align="left"><input class="input-text" type="text" name="childReportId"></td>
        </tr>
        <tr>
          <td height="58" align="left">版本号</td>
          <td align="left"><input  class="input-text" type="text" name="versionId">
          </td>
        </tr>
        <tr>
          <td height="58" align="left">行列</td>
          <td align="left"><input class="input-text" type="text" name="rowColumn">
            </td>
        </tr>
        <tr>
          <td height="58" align="left">分隔符</td>
          <td align="left"><input class="input-text" type="text" name="splitChar">
          </td>
        </tr>
        <tr>
          <td align="left">结果描述</td>
          <td height="58" align="left"><input class="input-text" type="text" name="des"></td>
        </tr>
        <tr>
          <td height="58" align="left"> </td>
          <td height="58" align="left"><input  class="input-button"  type="submit" name="Submit" value="确定">
          </td>
        </tr>
      </table>
      </logic:notPresent>
      
      
      
      <logic:present name="formula" scope="request">
      <table width="48%"  border="1" cellspacing="0" cellpadding="0" align="center" >
        <tr> &nbsp;</tr>
        <tr> &nbsp;</tr>
        <tr> &nbsp;</tr>
        <tr>
          <td width="29%" height="47" align="left"> 公式</td>
          <td width="71%" align="left">
		  
		
		  <input  class="input-text" type="text" name="formula" Value=<bean:write name="formula" property="formula"/>>
			

							</td>
        </tr>
        <tr>
          <td  height="57" align="left">text文件名</td>
          <td  align="left"><input class="input-text" type="text" name="dataSourceEname" Value=<bean:write name="formula" property="dataSourceEname"/>>
          </td>
        </tr>
        <tr>
          <td height="56" align="left">机构列</td>
          <td align="left"><input class="input-text" type="text" name="orgId"   Value=<bean:write name="formula" property="orgId"/>>
          </td>
        </tr>
        <tr>
          <td height="58" align="left">模板ID</td>
          <td align="left"><input class="input-text" type="text" name="childReportId"  Value=<bean:write name="formula" property="childReportId"/>></td>
        </tr>
        <tr>
          <td height="58" align="left">版本号</td>
          <td align="left"><input  class="input-text" type="text" name="versionId"  Value=<bean:write name="formula" property="versionId"/>>
          </td>
        </tr>
        <tr>
          <td height="58" align="left">行列</td>
          <td align="left"><input class="input-text" type="text" name="rowColumn"  Value=<bean:write name="formula" property="rowColumn"/>>
            </td>
        </tr>
        <tr>
          <td height="58" align="left">分隔符</td>
          <td align="left"><input class="input-text" type="text" name="splitChar"  Value=<bean:write name="formula" property="splitChar"/>>
          </td>
        </tr>
        <tr>
          <td align="left">结果描述</td>
          <td height="58" align="left"><input class="input-text" type="text" name="des"  Value=<bean:write name="formula" property="des"/>></td>
        </tr>
        <tr>
          <td height="58" align="left"> </td>
          <td height="58" align="left"><input  class="input-button"  type="submit" name="Submit" value="确定">
          </td>
        </tr>
      </table>
      </logic:present>
      
      </td>
    </tr>
  </table>
</html:form>
  </body>
</html:html>
