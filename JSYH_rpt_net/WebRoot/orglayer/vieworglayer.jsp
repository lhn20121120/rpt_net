<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base/>
<title>地区信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript">
		function deleteRegionTyp(regionTypNo)
		{
			if(confirm("你确定要删除该机构层次信息吗?"))
				window.location = "./deleteOrgLayerMgr.do?orgLayerId="+regionTypNo;
			
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
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 机构层次管理 >> 机构层次主页
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
 	<table width="90%"  border="0" align="center">
		  <tr >
		  	<td height="20">
		  	<td>
		  </tr>
		  <tr>
		    <td align="right">
		    	<html:button property="aa" value="增加机构层次" styleClass="input-button" onclick="location.assign('./orglayer/orglayer_add.jsp')"/>
		    </td>
		  </tr>
		  <tr>
		  	<td height="5">
		  	<td>
		  </tr>
   </table>
  <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
       <tr class="titletab">
       	   <th height="25" align="center" colspan="6">
       	   	机构层次列表
       	   </th>
        </tr>
        <tr align="center" class="middle">
              <td align="center" width="5%" >
	          	序号
	          </td>    
	          <td align="center" width="15%" >
	          	机构层次Id
	          </td>
	          <td align="center">
	          	机构层次名称
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
				                <logic:notEmpty name="item" property="orgLayerId">
				                	<bean:write name="item" property="orgLayerId"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgLayerId">
				                	未知
				                </logic:empty>
			                </td>
			                
			               
			                
			                <td bgcolor="#ffffff">
				                <logic:notEmpty name="item" property="orgLayer">
				                	<bean:write name="item" property="orgLayer"/>
				                </logic:notEmpty>
				                <logic:empty name="item" property="orgLayer">
				                	未知
				                </logic:empty>
			                </td>
			    <td bgcolor="#ffffff">
			    <a href="../orglayer/orglayer_update.jsp?orgLayer=<bean:write name="item" property="orgLayer"/>&orgLayerId=<bean:write name="item" property="orgLayerId"/>">修改</a>
			                </td>
			                <td bgcolor="#ffffff">
			                	<a href="javascript:deleteRegionTyp('<bean:write name="item" property="orgLayerId"/>')">删除</a>
			                </td>              
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6">
						暂无地区信息
					</td>
				</tr>
			</logic:notPresent>
    </table>
    <table width="90%" border="0">
			<TR>
				<TD width="80%">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../viewOrgLayerMgr.do"/>
					</jsp:include>
				</TD>
			</tr>
	</table>
</body>
</html:html>
