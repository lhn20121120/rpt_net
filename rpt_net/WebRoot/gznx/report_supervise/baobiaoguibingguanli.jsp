<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "0";	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}		
%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script language="javascript">
		function treeOnClick(id,value)
			{
				document.getElementById('templateType').value = id;
				document.getElementById('templateTypeName').value = value;
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden"; 
			}
			

		//显示,关闭树型菜单
		function showTree(){
		if(document.getElementById('orgTree').style.visibility =='hidden'){
		    var textname = document.getElementById('selectedTypeName');
			document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgTree").style.height = "200";
			document.getElementById("orgTree").style.visibility = "visible";   // 显示树型菜单
		}

		else if(document.getElementById("orgTree").style.visibility == "visible"){
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden";      //关闭树型菜单
		}
	}
	
		function closeTree(objTxt,objTree){	  
		   var obj = event.srcElement;
		   if(obj!=objTxt && obj!=objTree){
		     
		     objTree.style.height = "0";
		     objTree.style.visibility="hidden";      //关闭树型菜单
		   }
		}
		
		//距离文本框的水平相对位置
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}   
		//距离文本框的垂直相对位置
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}
		
		function _selExport(){
			var objFrm=document.getElementById("frmChk");
	
		  	var qry="templateType=" + objFrm.elements['templateType'].value + 
	 	  			"&templateName=" + objFrm.elements['templateName'].value +
	 	  			"&loopflg=1" +
		  	  		"&forflg=1"+
		  	  		"&type=归并关系";

			if(confirm("您确定要导出全部公式吗?\n")==true){
				window.location="<%=request.getContextPath()%>/servlets/toReadExcelServlet?"+qry;
			}
				
		}
		
		// add by 王明明
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
		</script>
	</head>
	<body>
		<table border="0" width="98%" align="center">
		<tr><td height="10"></td></tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表管理 &gt;&gt; 归并关系管理
			</td>
		</tr>
		</table>
		
		<html:form action="afreportmerger.do" method="POST" styleId="frmChk">
		<input type="hidden" name="templateType" value="<bean:write  name="form"  property="templateType"/>"/>
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<fieldset id="fieldset">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
								<td height="5"></td>
							</tr>
						<TR>
						<TD width="20%" align="right">
							报表名称：
						</TD>
						<TD width="20%" align="center">
							<html:text name="form" property="templateName" size="20"  styleClass="input-text"/>
						</TD>
						<td  width="30%" align="left">
							<html:submit value="查  询" styleClass="input-button" />
						</td>
						<td  width="30%" align="center">
							<input type="button" value="全部导出" class="input-button" onclick="_selExport()">&nbsp;
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>	
					</table>
					</fieldset>
				</td>
			</tr>			
		</table>
		</html:form>	
		
		<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">
			<!-- <TR><TD><FONT color="#FF0000">红色显示为自定义模板</FONT></TD></TR>-->
			<TR>
				<TD>
					<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
						<tr class="titletab">
							<th colspan="8" align="center" id="list">
								<strong>
									模板列表
								</strong>
							</th>
						</tr>
						<TR class="middle">
						<TD class="tableHeader" width="8%">
								<b>报表编号 </b>
							</TD>

							<TD class="tableHeader" width="33%">
								<b>报表名称</b>
							</TD>
							<TD class="tableHeader" width="8%">
								<b>版本号</b>
							</td>
							
							<TD class="tableHeader" width="10%">
								<b>起始时间</b>
							</td>
							<TD class="tableHeader" width="10%">
								<b>结束时间</b>
							</td>
							<TD class="tableHeader" width="8%">
								<b>详细信息</b>
							</TD>									
						</TR>

						<logic:present name="Records" scope="request">
							<logic:iterate id="item" name="Records">
								<TR bgcolor="#FFFFFF" onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
									<td align="center">
										<bean:write name="item" property="templateId"/>
									</td>	
									<TD align="center">
										<bean:write name="item" property="templateName"/>
									</TD>
									<td align="center">
										<bean:write name="item" property="versionId"/>
									</td>
									<td align="center">
										<bean:write name="item" property="startDate"/>
									</td>
									<td align="center">
										<bean:write name="item" property="endDate"/>
									</td>
									<TD align="center">
										<a href="<%=request.getContextPath()%>/guibin.do?templateId=<bean:write name="item" property="templateId"/>&reportName=<bean:write name="item" property="templateName"/>&versionId=<bean:write name="item" property="versionId"/>">查看</a>
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr bgcolor="#FFFFFF">
								<td colspan="8">
									暂无记录
								</td>
							</tr>
						</logic:notPresent>
						</TABLE>
						
						<table cellSpacing="1" cellPadding="4" width="100%" border="0">
							<tr>
								<TD colspan="7" bgcolor="#FFFFFF">
								<jsp:include page="../../apartpage.jsp" flush="true">
									<jsp:param name="url" value="../../afreportmerger.do" />
								</jsp:include>
								</TD>
							</tr>
						</table>
				</TD>
			</TR>
		</TABLE>	
			
	</body>
</html:html>
