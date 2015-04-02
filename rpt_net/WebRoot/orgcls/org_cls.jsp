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
			if(confirm("你确定要删除该子行分类信息吗?"))
				window.location = "../orgcls/deleteorgCls.do?orgClsId="+orgclsId;
			
		}
	</script>
	
</head>

<body >
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
 	<table width="95%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    
		    	<html:button property="addMOrgCls" value="增加子行类别" styleClass="input-button" onclick="location.assign('./orgcls_add.jsp')"/>
		    </td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="6" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="6">
       	   	子行分类列表</th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%" >
	          	序号
	          </td>
	          <td align="center" width="15%" >
	          	分类编号
	          </td>
	          <td align="center" width="40%">
	          	子行分类名称
	          </td>
			  <td align="center" width="10%">
	          	分类类型
	          </td>
	          <td align="center" width="15%">
	          	修改
	          </td>
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
				                <logic:notEmpty name="item" property="orgClsId">
				                	<bean:write name="item" property="orgClsId"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgClsId">
				                	未知
				                </logic:empty>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgClsNm">
				                	<bean:write name="item" property="orgClsNm"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgClsNm">
				                	未知
				                </logic:empty>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgClsType">
				                	<bean:write name="item" property="orgClsType"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgClsType">
				                	未知
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
			    <a href="../orgcls/orgcls_update.jsp?orgClsNm=<bean:write name="item" property="orgClsNm"/>&orgClsId=<bean:write name="item" property="orgClsId"/>">修改</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteOrgCls('<bean:write name="item" property="orgClsId"/>')">删除</a>
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="10">
						暂无部门信息
					</td>
				</tr>
			</logic:notPresent>
						
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../orgcls/orgCls.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
