<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../../js/func.js"></script>
		<script language="javascript">
			
		</script>
	</head>
	<script language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>	
	    
	    var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	    
	    /**
		 * 表单提交验证
		 */
		 function _submit(form){
			if(form.year.value.Trim() != ""){
		  		if(isNaN(form.year.value)){ 
				   alert("请输入正确的报表时间！"); 
				   form.year.focus(); 
				   return false; 
				}
		  	}				
			if(form.term.value.Trim() != ""){
				if(isNaN(form.term.value)){ 
				   alert("请输入正确的报表时间！"); 
				   form.term.focus(); 
				   return false; 
				}
				if(form.term.value <1 || form.term.value >12){
					alert("请输入正确的报表时间！");
					form.term.focus();
					return false;
				}
			}
			return true;
		}
		
	    /**
		 * 全选操作
		 */
		function _selAll(){		
			var formObj=document.getElementById("frmChk");
			var count=formObj.elements['countChk'].value;
		  	  	
			for(var i=1;i<=count;i++){
				try{
					if(formObj.elements['chkAll'].checked==false)
			  	  		eval("formObj.elements['chk" + i + "'].checked=false");
			  	  	else
			  	  		eval("formObj.elements['chk" + i + "'].checked=true");
		  	  	}catch(e){}
			}
		}
		
	    /**
		 * 批量下载报表 
		 * @return void
		 */
		function _downLoadSelectReport(){
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){
			  	  		repInIds+=(repInIds==""?"":SPLIT_SYMBOL_COMMA) + obj.value;
			  	  	}
		  	  	}catch(e){}
			}	
		  	if(repInIds==""){
		  	  	alert("请选择要下载的报表!\n");
		  	  	return;
			}else{		  	  	  	
		  		window.location="<%=request.getContextPath()%>/servlets/downLoadOtherCollectServlet?repInIds=" + repInIds;		
		  	}
		}
		/**
		 * 下载全部汇总方式汇总的报表
		 */
		function _downLoadAll(){
		 	var year = document.getElementById("year");
		  	var term = document.getElementById("term");		  	
		  	var repName = document.getElementById("repName");
		  	var qry = "year="+year.value+"&tterm="+term.value+"&repName="+repName.value;				  	 	 	
		  	window.location="<%=request.getContextPath()%>/servlets/downLoadOtherCollectServlet?" + qry;		
		}
		 
		function _downLoadSingle(repInId){
			window.location="<%=request.getContextPath()%>/servlets/downLoadOtherCollectServlet?repInIds=" + repInId;		
		}	 
		function viewPdf(repInId){
			window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}
		
	</script>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body style="TEXT-ALIGN: center">
			<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 数据报送 >> 其它汇总数据
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center">
		<html:form action="/collectType/viewOtherCollect" method="post" styleId="frm">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="4" border="0" width="60%" align="left">
							<tr>
								<td height="3"></td>
							</tr>
							<tr>
								<td>
									报表名称：
									<html:text property="repName" size="25" styleClass="input-text" />
								</td>
								<td>
									报表时间：
									<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
									年
									<html:text property="term" maxlength="2" size="4" styleClass="input-text" />
									月
								</td>
								<td>
									<html:submit styleClass="input-button" value=" 查 询 " />
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<logic:present name="records" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<input type="button" value="下载报表数据" class="input-button" onclick="_downLoadSelectReport()">
					<input type="button" value="下载全部报表" class="input-button" onclick="_downLoadAll()">
				</td>
			</tr>
		</table>
	</logic:present>
	<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">
		<html:form action="/collectType/viewOtherCollect" method="post" styleId="frmChk">
			<TR>
				<TD>
					<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
						<TR class="titletab">
							<th width="5%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							<th width="8%" align="center" valign="middle">报表编号</th>
							<th width="29%" align="center" valign="middle">报表名称</th>
							<th width="7%" align="center" valign="middle">版本号</th>
							<th width="10%" align="center" valign="middle">报表口径</th>
							<th width="10%" align="center" valign="middle">币种</th>
							<th width="5%" align="center" valign="middle">频度</th>
							<th width="10%" align="center" valign="middle">报表时间</th>
							<th width="10%" align="center" valign="middle">汇总方式</th>
							<Th width="10%" align="center" valign="middle">操作</Th>
						</tr>
						<%int i = 0;%>
						<logic:present name="records" scope="request">
							<logic:iterate id="viewReportIn" name="records">
								<%i++;%>
								<TR bgcolor="#FFFFFF">
									<TD align="center">									
										<input type="checkbox" name="chk<%=i%>" value="<bean:write name="viewReportIn" property="repInId"/>">										
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="childRepId" />
									</TD>
									<TD align="center">
										<a href="javascript:viewPdf(<bean:write name='viewReportIn' property='repInId'/>)">
											<bean:write name="viewReportIn" property="repName" />
										</a>
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="versionId" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="dataRgTypeName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="year"/>-<bean:write name="viewReportIn" property="term"/>
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="collectType" />
									</TD>
									<TD align="center">									
										<a href ="javascript:_downLoadSingle(<bean:write name='viewReportIn' property='repInId'/>)">下载</a>									
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="records" scope="request">
							<tr align="center">
								<td bgcolor="#ffffff" colspan="10" align="left">
									暂无符合条件的记录
								</td>
							</tr>
						</logic:notPresent>
					</TABLE>
				</TD>
			</TR>
		</html:form>
	</TABLE>	
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../collectType/viewOtherCollect.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>			
	</body>
</html:html>
