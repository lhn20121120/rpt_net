<%@ page language="java"  pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ page import="com.cbrc.smis.security.Operator" %>
<html:html locale="true">
<head>
	<html:base />
	<title>汇总数据列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../js/comm.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/prototype-1.4.0.js"></script>
	
	
	<script language="javascript">
	var curPage=1;
	<logic:present name="ApartPage" scope="request">
		curPage=<bean:write name="ApartPage" property="curPage" />;
	</logic:present>
	function checkAll(name) {     
	var el = document.getElementsByTagName('input'); 
	var len = el.length;     
	for(var i=0; i<len; i++){ 
		if((el[i].type=="checkbox") && (el[i].name==name)){
			el[i].checked = true;         
		}     
	} 
	}
	function clearAll(name) {     
		var el = document.getElementsByTagName('input');     
			var len = el.length;     for(var i=0; i<len; i++){       
			if((el[i].type=="checkbox") && (el[i].name==name)) {       
			el[i].checked = false;         }     } } 
	
	/**
	*汇总所有报表
	*/
	function _compare(repInId){
	var year=document.all.year.value;
	var term=document.all.term.value;
	var repName=document.all.repName.value;
	var orgId=document.all.orgId.value;
		window.location="<%=request.getContextPath()%>/analysis/InsertACompareLog.do?repInIds="+repInId+"&year="+year+"&term="
		+term+"&orgId="+orgId+"&repName="+repName+"&acType=1"+"&curPage="+curPage;
	}
	//比对所选择的项
	function _comparedSelected(){
	var repInIds='';
	   		var year=document.all.year.value;
	var term=document.all.term.value;
	var repName=document.all.repName.value;
	var orgId=document.all.orgId.value;
	if(document.all.repInId.length==undefined){
		if(document.all.repInId.checked == true){
			repInIds=document.all.repInId.value;
		}
	}
	   		for(var k=0;k<document.all.repInId.length;k++){
				if(document.all.repInId[k].checked == true){
				
					if(repInIds==''){
						repInIds=document.all.repInId[k].value;
					}else{
						repInIds=repInIds+','+document.all.repInId[k].value;
					}
				}
			}
			
	   		if(repInIds==''){
	   		    alert("请选择比对报表项!");
	  			return false;
	   		}
	  		
		window.location="<%=request.getContextPath()%>/analysis/InsertACompareLog.do?repInIds="+repInIds+"&year="+year+"&term="
		+term+"&orgId="+orgId+"&repName="+repName+"&acType=1"+"&curPage="+curPage;;
  	
			
	   }

		//查看比对
		function _view(repInId){
			window.open("<%=request.getContextPath()%>/viewCheckReport.do?reportID="+repInId+"&checkID="+repInId,"Check");
		}

		

		
</script>
	<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
%>
</head>
<body>
		<label id="prodress" style="display:none">
			<span class="txt-main" style="color:#FF3300">正在汇总，请稍后......</span>
		</label>

  <label   id="prodress1" >
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 数据统计 >> 数据比对
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/analysis/selectACompareLog" method="post" styleId="frm" onsubmit="return _submit(this)">
			<input type="hidden" name="orgId" value="<%=operator.getOrgId() %>"/>
			<input type="hidden" name="acType" value="1"/>
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
							<tr>
								<td>
									报表时间：
									<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
									年
									<html:text property="term" maxlength="2" size="4" styleClass="input-text" />
									月
								</td>
								<td>
								报表名称：
									<html:text property="repName" size="25" styleClass="input-text" />
								</td>
								<td>
									<html:submit styleClass="input-button" value=" 查 询 " />   <input type="button" class="input-button" onclick="_comparedSelected()" value="比对选中"/>
								</td>
							</tr>
						</table>

					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<br />
		<logic:present name="Records" scope="request">
			<table width="99%" border="0" cellpadding="4" cellspacing="1">
				<tr>
					<td>
						
					</td>
				</tr>
			</table>
		</logic:present>
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					数据比对列表
				</th>
			</tr>
			<tr>
			<TR class="middle">
				<TD  align="center">
					<logic:present name="Records" scope="request">
						<INPUT type="checkbox" name="repInIds"  value="" onClick="if(this.checked==true) { checkAll('repInId'); } else { clearAll('repInId'); }" />
					</logic:present>
				</TD>
				<TD  align="center">
					<strong>报表编号</strong>
				</TD>
				<TD  align="center">
					<strong>版本号</strong>
				</TD>
				<TD  align="center">
					<strong>报表名称</strong>
				</TD>
				<TD  align="center">
					<strong>年份-期数</strong>
				</TD>
				
				<TD  align="center">
					<strong>报表口径</strong>
				</TD>
				<TD  align="center">
					<strong>币种</strong>
				</TD>
				<TD  align="center">
					<strong>比对结果</strong>
				</TD>
				<TD align="center">
					<strong>操作</strong>
				</TD>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="iterm" name="Records">
					
					<tr bgcolor="#FFFFFF">
						<td align="center">
							<INPUT type="checkbox" name="repInId" value='<bean:write name="iterm" property="repInId" />'/>
						</td>
						<td align="center">
							<bean:write name="iterm" property="childRepId" />
						</td>
						<td align="center">
						<bean:write name="iterm" property="versionId" />
			            </td>
						<td align="center">
							<bean:write name="iterm" property="repName" />
						</td>
						<td align="center">
							<bean:write name="iterm" property="year" />-<bean:write name="iterm" property="term" />
						</td>
						<td align="center">
							<bean:write name="iterm" property="dataRgTypeName" />
						</td>
						<td align="center">
							<bean:write name="iterm" property="currName" />
						</td>
						<td align="center">
						<logic:notEmpty name="iterm" property="acLog" >
							<logic:match name="iterm" property="acLog"  value="不一致">
								<font color="red"><bean:write name="iterm" property="acLog" /></font>
							</logic:match>
							<logic:match name="iterm" property="acLog"  value="报表不存在">
								<font color="red"><bean:write name="iterm" property="acLog" /></font>
							</logic:match>
							<logic:equal name="iterm" property="acLog"  value="一致">
								 <bean:write name="iterm" property="acLog" /> 
							</logic:equal>
						</logic:notEmpty>
						</td>


						<td align="center">
						<input type="button" class="input-button" onClick="_view(<bean:write name="iterm" property="repInId" />)" value="查 看"/>
							<input type="button" class="input-button" value="比 对" onClick="_compare(<bean:write name="iterm" property="repInId" />)">
							</td>
					</tr>
				</logic:iterate>
			
		<TR>
			<TD  bgcolor="#FFFFFF" colspan="9">
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../analysis/selectACompareLog.do" />
				</jsp:include>
			</TD>
		</TR>

			</logic:present>
			
	
	
			<logic:notPresent name="Records" scope="request">
				<tr bgcolor="#FFFFFF">
					<td colspan="10">
						暂无数据
					</td>
				</tr>
			</logic:notPresent>

		</table>
		<br />
	</label>
</body>
</html:html>
