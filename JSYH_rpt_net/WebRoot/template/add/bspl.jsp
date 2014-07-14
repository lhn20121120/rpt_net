<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<%
	String childRepId=null,versionId=null,reportStyle=null,reportName=null;
	
	if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
	if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");
	if(request.getParameter("reportStyle")!=null) reportStyle=(String)request.getParameter("reportStyle");
	if(request.getParameter("reportName")!=null)  reportName=request.getParameter("reportName");

	if(childRepId!=null) request.setAttribute("ChildRepID",childRepId);
	if(versionId!=null) request.setAttribute("VersionID",versionId);
	if(reportStyle!=null) request.setAttribute("ReportStyle",reportStyle);
	if(reportName!=null) request.setAttribute("ReportName",reportName);
%>

<html:html locale="true">
	<head>
		<html:base/>
		<title>报表数据范围及报送频度设定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		
		<script type="text/javascript" src="../../js/func.js">
		</script>
		
		<script language="javascript">	
			//验证表单信息
			function _bsplValidate(){
				var objTbl=document.getElementById('tbl');
				var objForm=document.forms['form1'];
				var objRepFreqIds=objForm.elements['repFreqIds'];
				var objNormalTimes=objForm.elements['normalTimes'];
				var objDelayTimes=objForm.elements['delayTimes'];
				
				var flag=false;

				for(var i=3;i<objTbl.rows.length;i++){

					if(eval(objRepFreqIds[i-3].value>-1)){
						if(isEmpty(objNormalTimes[i-3].value)==true){
							alert("请输入报送时间!\n");
							objNormalTimes[i-3].focus();
							return false;
						}else{
							if(CheckNum(objNormalTimes[i-3].value)==false){
								alert("请输入正确的报送时间!\n");
								objNormalTimes[i-3].focus();
								objNormalTimes[i-3].select();
								return false;
							}
						}
						if(isEmpty(objDelayTimes[i-3].value)==false && CheckNum(objDelayTimes[i-3].value)==false){
							alert("请输入正确的补报时间 !\n");
							objDelayTimes[i-3].focus();
							objDelayTimes[i-3].select();
							return false;
						}
						flag=true;
					}
				}
				
				if(flag==false){
					alert("请正确设定报表并表范围及报送频度时间!\n");
					return false;
				}else{
					return true;
				}
			}
			
			function _validate()
			{
				<bean:size id="length" name="utilForm" property="dataRgTypes"/>
				var length = '<bean:write name="length"/>';
							
				if(length==0)
					return false;
					
				if(length==1)
				{
					var normalTimeVal = document.form1.normalTimes.value;
					var delayTimeVal = document.form1.delayTimes.value;
					if(normalTimeVal != "")
					{
						if(CheckNum(normalTimeVal)==false)
						{
							alert('时间必须是数字');
							document.form1.normalTimes.focus();
							return false;
						}
					}
				
					if(delayTimeVal != "")
					{
						if(CheckNum(delayTimeVal)==false)
						{
							alert('补报时间必须是数字');
							document.form1.delayTimes.focus();
							return false;
						}	
					}
					if( !(normalTimeVal != "" && document.form1.repFreqId.selectedIndex!=0) )
					{
						alert("你必须设定一个完整报送频度时间！");
						return false;
					}
					return true;
				}
				var emptyRow = 0;
				for(var i=0;i<length;i++)
				{
					var normalTimeVal = document.form1.normalTimes[i].value;
					var delayTimeVal = document.form1.delayTimes[i].value;
					
					if(normalTimeVal !="")
					{
						if(CheckNum(normalTimeVal)==false)
						{
							alert('时间必须是数字');
							document.form1.normalTimes[i].focus();
							return false;
						}
					}
					if( delayTimeVal != "")
					{
						if(CheckNum(delayTimeVal)==false)
						{
							alert('补报时间必须是数字');
							document.form1.delayTimes[i].focus();
							return false;
						}		
					}
					if(!(normalTimeVal != "" && document.form1.repFreqId[i].selectedIndex!=0))
					{
						emptyRow++;
					}			
				}
				if(emptyRow == length)
				{
					alert("你必须设定一个完整的报送频度时间！");
					return false;
				}
				return true;			
	  		} 	  		
	  		
	  		/**
	  		 * 下一步事件
	  		 */
	  		 function _next(){ 	
	  		//    window.location="<%=request.getContextPath()%>/template/add/viewYCBHInit.do?" +
	  		 window.location="<%=request.getContextPath()%>/template/add/bpfb.jsp?" +
	  		    	"childRepId=<%=childRepId%>" +
	  		    	"&versionId=<%=versionId%>" +
	  		    	"&ReportName=<%=reportName%>" +
	  		    	"&reportStyle=<%=reportStyle%>";
	  		    	


	
	  		 }
		</script>
	</head>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body background="../image/total.gif">
		<html:form  styleId="form1" action="/template/updateMActuRept" method="Post" onsubmit="return _bsplValidate()">
		<table border="0" cellspacing="0" cellpadding="4" width="90%" align="center">
					<tr><td height="8"></td></tr>
					<tr>
						<td>
							当前位置 >> 报表管理 >> 报送频度设定
						</td>
					</tr>
					<tr><td height="10"></td></tr>		
		</table>
		<TABLE cellSpacing="1" cellPadding="4" width="90%" border="0" class="tbcolor" id="tbl">
			<TR class="tbcolor1">
				<th colspan="4" align="center" id="list" height="30">
					<span style="FONT-SIZE: 11pt">报表并表范围及报送频度时间设定</span>
				</th>
			</TR>
			<tr>
				<td colspan="4" bgcolor="#FFFFFF">
					<table border="0" cellpadding="4" cellspacing="0" width="100%">
						<tr>
							<td align="center" colspan="4">
								<b>
									<logic:present name="ObjForm" scope="request">
									《<bean:write name="ObjForm" property="reportName"/>》
								</logic:present>
									
								</b>
							</td>			
						</tr>
					</table>
				</td>
			</tr>
			<TR bgcolor="#FFFFFF">
				<TD align="center" width="40%" class="tableHeader">
					<b>数据范围</b>
				</TD>
				<TD align="center" width="20%" class="tableHeader">
					<b>频度</b>
				</TD>
				<TD align="center" width="20%" class="tableHeader">
					<b>时间</b>
				</TD>
				<TD align="center" width="20%" class="tableHeader">
					<b>补报时间</b>
				</TD>
			</tr>
				<logic:present name="ChildRepID" scope="request">
					<input type="hidden" name="childRepId" value="<bean:write name="ChildRepID"/>">
				</logic:present>
				<logic:present name="VersionID" scope="request">
					<input type="hidden" name="versionId" value="<bean:write name="VersionID"/>">
				</logic:present>
				<logic:present name="ReportStyle" scope="request">
					<input type="hidden" name="reportStyle" value="<bean:write name='ReportStyle'/>">
				</logic:present>
				<logic:present name="ReportName" scope="request">
					<input type="hidden" name="reportName" value="<bean:write name='ReportName'/>">
				</logic:present>
				<logic:present name="utilForm" scope="page">
					<logic:iterate id="item" name="utilForm" property="dataRgTypes">
						<tr bgcolor="#FFFFFF">
							<TD align="center">
								<bean:write name="item" property="label"/>
								<input type="hidden" name="dataRangeIds" value="<bean:write name="item" property="value"/>"/>			
							</TD>
							<td align="center">
								<html:select property="repFreqIds">
									<html:option value="-1">-------</html:option>
									<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
								</html:select>
							</td>
							<TD align="center">
								<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="3">
							</TD>
							<TD align="center">
								<input class="input-text" type="text" size="6" name="delayTimes" maxlength="3">
							</TD>
						</tr>
					</logic:iterate>
				</logic:present>
				
				<logic:notPresent name="utilForm" scope="page">
					<tr bgcolor="#FFFFFF">
						<td colspan="4">
							无匹配记录
						</td>
					</tr>
				</logic:notPresent>
		</TABLE>
		
		<table border="0" cellspacing="0" cellpadding="4" width="90%" align="center">
			<TR>
				<TD align="center">
					<input type="submit" value=" 保 存 " class="input-button">&nbsp;
					<!-- <input class="input-button" type="button" value=" 返 回 " onclick="alert('暂无')">-->
<%--					<input type="button" value=" 下一步 " class="input-button" onclick="_next()">--%>
				</TD>
			</TR>
		</table>
		</html:form>
	</body>
</html:html>
