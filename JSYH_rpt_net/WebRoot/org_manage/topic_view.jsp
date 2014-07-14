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
		function deleteOrg(Id)
		{
			if(confirm("你确定要删除该主题报送机构吗?"))
				window.location = "../orgmanage/deleteorg.do?id="+Id;
			
		}
		function viewOrg(id)
		{
		window.location="../orgmanage/view_topic_org.do?id="+id;
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
		    
		    	<html:button property="addMOrgCls" value="增加主题报送机构类别" styleClass="input-button" onclick="location.assign('../org_manage/topic_add.jsp')"/>
		    </td>
		  </tr>
		  <tr >
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="7" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="7"> 主题报送机构分类列表</th>
        </tr>
        <tr align="center" class="middle">
	          <td align="center" width="5%" >
	          	序号
	          </td>
	          <td align="center" width="15%" >
	          	 主题报送机构分类ID
	           </td>
	          <td align="center" width="30%"> 主题报送机构名称</td>
			  <td align="center" width="20%">
	          	代表机构
	          </td>
	          <td align="center" width="7%">
	          	修改
	          </td>
	          <td align="center" width="7%">
	          	删除
	          </td>
	          <td align="center" width="15%">
	            机构查看
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
				                <logic:notEmpty name="item" property="name">
				                	<bean:write name="item" property="name"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="name">
				                	未知
				                </logic:empty>
			                </td>
			                 <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="rep_org">
				                	<bean:write name="item" property="rep_org"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="rep_org">
				                	未知
				                </logic:empty>
			                </td>
			                <td bgcolor="#ffffff">
			    <a href="../orgmanage/editorg.do?id=<bean:write name="item" property="id"/>">修改</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteOrg('<bean:write name="item" property="id"/>')">删除</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:viewOrg('<bean:write name="item" property="id"/>')">查看</a>
			                </td>
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="4">
						暂无部门信息
					</td>
				</tr>
			</logic:notPresent>
						
    </table>
    <table width="70%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../orgmanage/vieworg.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
