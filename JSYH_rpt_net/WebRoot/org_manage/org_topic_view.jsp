<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base/>
<title>子行类别</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteOrgCls(orgclsId)
		{
			if(confirm("你确定要删除该行吗?"))
				window.location = "../orgmanage/delete_topic_org.do?id="+orgclsId;
			
		}
		function _add()
		{
		window.location= "../orgmanage/getallorg.do";
		}
		function _return()
		{
		window.location="../orgmanage/vieworg.do";
		}
	</script>
	
</head>

<body >

<html:form method="post" action="/orgmanage/view_topic_org" >
  <table width="68%" height="54"  border="0" cellpadding="0" cellspacing="0">
  <tr>
				<td width="25%" height="54">
					主题报送机构ID
					<input type="text" name="id" size="17">
				</td>
				<td width="24%">
					机构ID
					<input type="text" name="orgId" size="17">
					
				</td>
				<td width="19%">
					<input class="input-button" type="submit" name="Submit" value="查询">
				</td>
				<TD>
					<INPUT class="input-button" type="Button" name="设置报送机构" value="增加" onclick="return _add()"/>
					
				</TD>
				<TD>
					<INPUT class="input-button" type="Button" name="返回" value="返回" onclick="return _return()"/>
					
				</TD>
			</tr>
</table>
</html:form>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
 	
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="4"> 主题报送机构分类列表</th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%" >
	          	序号
	          </td>
	          <td align="center" width="15%" >
	          	主题报送机构ID
	           </td>
	          <td align="center" width="40%"> 机构ID</td>
			 
	          
	          <td align="center" width="15%">
	          	删除
	          </td>
	         
         </tr>		  
		<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" indexId="index">
					 <tr align="center" >
			                <td bgcolor="#ffffff">
			                	<%=((Integer)index).intValue() + 1%>
			                </td>
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="id">
				                	<bean:write name="item" property="id"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="id">
				                	未知
				                </logic:empty>
			                </td>
			                
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgId">
				                	<bean:write name="item" property="orgId"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgId">
				                	未知
				                </logic:empty>
			                </td>
			                
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteOrgCls('<bean:write name="item" property="orgId"/>')">删除</a>
			                </td>
			                
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="4">
						暂无信息
					</td>
				</tr>
			</logic:notPresent>
						
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../orgmanage/view_topic_org.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
