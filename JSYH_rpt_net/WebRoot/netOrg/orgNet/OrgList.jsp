<%@ page language="java"   pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base />
	<title>机构列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>
	<%
	com.cbrc.smis.security.Operator operator = (com.cbrc.smis.security.Operator) session.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	com.fitech.net.form.OrgNetForm orgForm=com.fitech.net.adapter.StrutsOrgNetDelegate.selectOne(operator.getOrgId(),true);
	pageContext.setAttribute("orgForm",orgForm);
	java.util.List orgList = request.getAttribute("Records") != null ? (java.util.List)request.getAttribute("Records") : null;
	%>
	<script type="text/javascript">
	var curPage=<bean:write name="ApartPage" property="curPage" />;
	var orgType=null;
   <%if(orgForm.getOrg_type_id().equals(new Integer(1))){ %> orgType="2";<%};
	if(orgForm.getOrg_type_id().equals(new Integer(2))){ %> orgType="3";<%;};
	%>
	var preOrgId='<%=operator.getOrgId()%>';
	
				//新增机构
		function goAddPage(){
	    	window.location.href='<%=request.getContextPath()%>/netOrg/newAddOrg/AddOrg2.jsp?orgType='+orgType+'&curPage='+curPage+'&preOrgId='+preOrgId;
	    }
	    function goAddPage2(type,orgId){
	    	window.location.href='<%=request.getContextPath()%>/netOrg/newAddOrg/AddOrg2.jsp?orgType='+type+'&curPage='+curPage+'&preOrgId='+orgId;
	    }
	    function getHTML(id,url){
	        var pars = "radom="+Math.random();
	        var myAjax = new Ajax.Updater(id, url, {method: 'get', parameters: pars});
  		}
	    //查看下级机构
	    function viewsubOrg(orgId){
	   		 //var url='<%=request.getContextPath() %>/netOrg/orgNet/subOrgList.jsp?orgId='+orgId;
	    	if($('img_'+orgId).src.indexOf('add.gif')>0){     		
     	 	
     	 		//getHTML('div_'+orgId,url);  
				$('tr_'+orgId).style.display="";
     	 		$('img_'+orgId).src="<%=request.getContextPath() %>/image/subtract.gif";
     	 	}else {
				$('tr_'+orgId).style.display="none";
				$('img_'+orgId).src="<%=request.getContextPath() %>/image/add.gif";
     	 	}
	    	
	    	  	
	    }
	   function  _edit(orgId){
	    	window.location.href='<%=request.getContextPath()%>/netOrg/orgNet/orgNetEdit2.jsp?orgId='+orgId+'&curPage='+curPage;
	    }
	    function  _delete(orgId){
	    	window.location.href='<%=request.getContextPath()%>/deleteOrgNet.do?org_id='+orgId+'&curPage='+curPage;
	    }
	    //报送范围
	function _range(orgId){
		window.location="<%=request.getContextPath()%>/showOrgBSFW.do?org_id="+ orgId+"&curPage="+curPage;
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
  <label   id="prodress1" >
  
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt;机构管理 &gt;&gt; 机构设定
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
  <logic:equal value="3" name="orgForm" property="org_type_id">
  <br>
  <br>
    <font size="5" color="red"><strong>对不起，您属于三级机构，不能使用该功能!</strong></font>
  </logic:equal>
  <logic:lessThan value="3" name="orgForm" property="org_type_id">
  <logic:greaterEqual value="1" name="orgForm" property="org_type_id">
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">		
		<html:form method="post" styleId="frm1" action="/org/selectOrgNet.do" >
			<tr>
				<td>
					<fieldset id="fieldset" height="40">
					<br/>
						<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
							<tr>
								<td align="left" >
									机构名称:<html:text property="org_name" size="50" styleClass="input-text" />
								</td>
								
								<td >
								    <input type="hidden" name="select" value="select">
									<input class="input-button" type="submit" name="Submit" value="查  询">                                   
                                    <input type="button"   class="input-button" value="新增机构" onclick="goAddPage()">                                								
								</td>							
							</tr>
						</table>
					</fieldset>
				</td>				
			</tr>
		</html:form>
	</table>
	<br/>
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					机构列表
				</th>
			</tr>
			<tr>
			<TR class="middle">
				<TD  align="center">
					<strong>机构编号</strong>
				</TD>
				<TD  align="center">
					<strong>机构名称</strong>
				</TD>
				<TD  align="center">
					<strong>机构类别</strong>
				</TD>
				<TD align="center">
					<strong>操作</strong>
				</TD>
			</TR>
			<%
				if(orgList != null && orgList.size() > 0){
					for(int i=0;i<orgList.size();i++){
						com.fitech.net.hibernate.OrgNet orgNet = (com.fitech.net.hibernate.OrgNet)orgList.get(i);
						java.util.List subOrgList = com.fitech.net.adapter.StrutsOrgNetDelegate.selectLowerOrgList(orgNet.getOrgId());
						if(orgNet != null){							
							%>
							<tr bgcolor="#FFFFFF">
								<td align=left>
								<%if(orgNet.getOrgType().getOrgTypeId().equals(new Integer(2))){ %>
								<img id='img_<%=orgNet.getOrgId()%>' 
								src="<%=request.getContextPath() %>/image/add.gif" onclick="javascript:viewsubOrg('<%=orgNet.getOrgId()%>');"/>
									<%} %>
									<%=orgNet.getOrgId()%>
								</td>
								<td align="center"><%=orgNet.getOrgName()%></td>
								<td align="center"><%=orgNet.getOrgType().getOrgTypeName()%></td>
								<td align="center">
								<input type="button"   class="input-button" value="修改" onclick="_edit('<%=orgNet.getOrgId()%>')">
								<input type="button"   class="input-button" value="删除" onclick="_delete('<%=orgNet.getOrgId()%>')">
								<input type="button"   class="input-button" value="报送范围" onclick="_range('<%=orgNet.getOrgId()%>')" >
								<%if(orgNet.getOrgType().getOrgTypeId().equals(new Integer(2))){ %>
								<input type="button"   class="input-button" value="新增子机构"  onclick="goAddPage2(3,'<%=orgNet.getOrgId()%>')">
								<%} %>
								</td>								
							</tr>
							<tr id='tr_<%=orgNet.getOrgId()%>' bgcolor="#FFFFFF" style="display:none">
								<td align="center" colspan=4>
								<div id='div_<%=orgNet.getOrgId()%>'>
									
									<table width="98%" border="0"  cellspacing="1" class="tbcolor">		
										<%
											if(subOrgList != null && subOrgList.size() > 0){
												for(int j=0;j<subOrgList.size();j++){
													com.fitech.net.hibernate.OrgNet subOrgNet = (com.fitech.net.hibernate.OrgNet)subOrgList.get(j);		
										%>
										<tr bgcolor="#FFFFFF">
											<td align="center"><%=subOrgNet.getOrgId()%></td>
											<td align="center"><%=subOrgNet.getOrgName()%></td>
											<td align="center"><%=subOrgNet.getOrgType().getOrgTypeName()%></td>
											<td align="center">
												<input type="button"   class="input-button" value="修改"  onclick="_edit('<%=subOrgNet.getOrgId()%>')">
												<input type="button"   class="input-button" value="删除" onclick="_delete('<%=subOrgNet.getOrgId()%>')">
												<input type="button"   class="input-button" value="报送范围" onclick="_range('<%=subOrgNet.getOrgId()%>')">
											</td>
										</tr>
										<%
												}
											}else{
										%>
										<tr bgcolor="#FFFFFF">
											<td colspan="10">
												暂无机构信息
											</td>
										</tr>
										<%
											}
										%>
									</table>
								</div>
								</td>
								
							</tr>
							<%
						}
					}
				}else{
				%>
					<tr bgcolor="#FFFFFF">
						<td colspan="10">
							暂无机构信息
						</td>
					</tr>
				<%
				}
			%>
		</table>
		<table cellSpacing="1" cellPadding="4" width="98%" border="0">
			<tr>
				<TD colspan="7" bgcolor="#FFFFFF">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../../org/selectOrgNet.do" />
					</jsp:include>	
				</TD>
			</tr>
		</table>
    </logic:greaterEqual>
</logic:lessThan>
</body>
</html:html>
