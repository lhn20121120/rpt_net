<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	String repInIds = request.getParameter("repInIds")!=null?request.getParameter("repInIds"):"";
	String orgId = request.getParameter("orgId")!=null?request.getParameter("orgId"):"";
	String date = request.getParameter("date")!=null?request.getParameter("date"):"";
	String repFreqId = request.getParameter("repFreqId")!=null?request.getParameter("repFreqId"):"";
	String supplementFlag = request.getParameter("supplementFlag")!=null?request.getParameter("supplementFlag"):"";
	String versionFlg = request.getParameter("versionFlg")!=null?request.getParameter("versionFlg"):"";
	String styleFlg = request.getParameter("styleFlg")!=null?request.getParameter("styleFlg"):"";
%>

<html:html locale="true">
<head>
	<title></title>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/prototype-1.4.0.js"></script>
	<script language="javascript">
			/**
		     * 人行导出操作
			 */
			function _back(){
				var objForm=document.getElementById("frmChk");
				window.location="<%=request.getContextPath()%>/exportRhAFReport.do?repFreqId=<%=repFreqId%>&supplementFlag=<%=supplementFlag%>&orgId="+objForm.orgId.value+"&date="+objForm.date.value+"&styleFlg="+objForm.styleFlg.value;
				
			}
			
			function _submit(form){
				LockWindows.style.display="";
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
	
	<div id="LockWindows" style="display:none" class="black_overlay">
	<br><br><br><font color="red">操作执行中，请稍后……</font><br><br>
	<img src="../../image/loading.gif">
	</div>
	
	<table width="90%" border="0" align="center">
		<tr>
			<td height="20">
				当前位置 &gt;&gt; 报表处理 &gt;&gt; 录入说明
			<td>
		</tr>
		<tr>
			<td height="5">
			<td>
		</tr>
	</table>	
	<input type="hidden" id="userIdStr" name="userIdStr">
	<html:form action="/exportPbocAFReport.do" method="POST" styleId="frmChk" onsubmit="return _submit(this)">
	 <html:hidden property="repInIds" value="<%=repInIds%>"/>
	 <html:hidden property="orgId" value="<%=orgId%>"/>
	 <html:hidden property="date" value="<%=date%>"/>
	 <html:hidden property="repFreqId" value="<%=repFreqId%>"/>
	 <html:hidden property="supplementFlag" value="<%=supplementFlag%>"/>
	 <html:hidden property="versionFlg" value="<%=versionFlg%>"/>
	 <html:hidden property="styleFlg" value="<%=styleFlg%>"/>
	<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		<tr class="titletab">
			<th align="center">
				文件说明
			</th>
		</tr>
		<tr>
			<td bgcolor="#ffffff" align="center">
				<table width="100%" border="0" align="left" cellpadding="2" cellspacing="1">
					<tr bgcolor="#ffffff">
						<td height="5">
						</td>
					</tr>
					
					<tr bgcolor="#ffffff">
						<td valign="top" align="right">
							人行文件说明 :
						</td>
						<td colspan="1" bgcolor="#ffffff" align="left">
							<textarea name="contents" class="input-text" rows="10" cols="100" id="contents"></textarea>
							(非必须填写)
						</td>
					</tr>

					<tr>
						<TD height="12"></TD>
					</tr>
					<tr>
						<td colspan="4" align="right" bgcolor="#ffffff">
							<html:submit styleClass="input-button" value=" 提 交 " />
							&nbsp;
							<INPUT type="button" value="返 回" class="input-button" onclick="_back()">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</html:form>
</body>
</html:html>
