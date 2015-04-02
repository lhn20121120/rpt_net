<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.MCellFormuForm,com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="expressionBean" scope="page" class="com.cbrc.smis.other.Expression"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%

	if(session.getAttribute("SelectedOrgIds")!=null)
				session.removeAttribute("SelectedOrgIds");
	
	String flag_kp=String.valueOf(expressionBean.FLAG_KP);		
	String flag_bl=String.valueOf(expressionBean.FLAG_BL);
	String flag_bj=String.valueOf(expressionBean.FLAG_BJ);
	String flag_js=String.valueOf(expressionBean.FLAG_JS);
	
	String childRepId=null,versionId=null,reportName=null,reportStyle=null;
	
	if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
	if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");
	if(request.getAttribute("ReportName")!=null){  
		reportName=(String)request.getAttribute("ReportName");
		//// System.out.println("[bjgx.jsp(Attribute)]ReportName:" + reportName);
	}else{
		reportName=request.getParameter("reportName")==null?null:FitechUtil.getParameter(request,"reportName");		
		//// System.out.println("[bjgx.jsp(Request)]ReportName:" + reportName);
	}
	if(request.getParameter("reportStyle")!=null) reportStyle=(String)request.getParameter("reportStyle");
	
	if(childRepId!=null && versionId!=null && reportName!=null && reportStyle!=null){
		MCellFormuForm mCellForumForm = new MCellFormuForm();
		mCellForumForm.setChildRepId(childRepId);
		mCellForumForm.setVersionId(versionId);
		mCellForumForm.setReportName(reportName);
		mCellForumForm.setReportStyle(reportStyle);
		
		request.setAttribute("ObjForm",mCellForumForm);
	}	
	
%>
<html:html locale="true">
<head>
	<html:base />
	<title>
		���ڡ�����ϵ�趨
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/comm.js"></script>
	<script language=javascript>
		<!--
			/**
			 * ��ʽ����:����У��
			 */
			 var CELL_CHECK_INNER="<%=configBean.CELL_CHECK_INNER%>";
			/**
			 * ��ʽ����:���У��
			 */
			 var CELL_CHECK_INNER="<%=configBean.CELL_CHECK_BETWEEN%>";
			/**
			 *�ָ���1
			 */
			 //var SPLIT_SMYBOL_COMMA="<%=configBean.SPLIT_SYMBOL_COMMA%>";
			 
			 /*������У�鹫ʽ���ظ������Բ���˫�ܵ������зָ���������⣬�ɸ����������� @author Yao*/
			 var SPLIT_SMYBOL_COMMA="<%=configBean.SPLIT_SYMBOL_SPECIAL%>";
			 
			 /**
			 *�ָ���2
			 */
			 var SPLIT_SMYBOL_ESP="<%=configBean.SPLIT_SYMBOL_ESP%>";
			 
			/**
			 * ��ϵ���ʽ�����ļ���׺
			 */
			 var EXT_TXT="<%=configBean.EXT_TXT%>";
			
			/**
			 * ������ڱ���ϵ���ʽ�¼�
			 */
			 function _load_gx(){
			 	 //openDialog("load_gx.jsp");
			 	 var objForm=document.forms['frmBJGX'];
			 	 
			 	 window.location="<%=request.getContextPath()%>/template/add/load_gx.jsp?" + 
			 	 	"childRepId=" + objForm.elements['childRepId'].value + 
			 	 	"&versionId=" + objForm.elements['versionId'].value + 
			 	 	"&reportName=" + objForm.elements['reportName'].value +
			 	 	"&reportStyle=" + objForm.elements['reportStyle'].value;
			 }
			 
			 /**
			  * ���ύ�¼�
			  */
			  function _submit(){
			  	var objTbl=document.getElementById('tbl');
			  	
			  	var rows=objTbl.rows.length;
			  	
			  	var qry="";
			  	for(var i=1;i<rows;i++){

			  		qry+=tbl.rows[i].cells[1].innerText +  SPLIT_SMYBOL_COMMA + 
			  			tbl.rows[i].cells[2].id + 
			  			SPLIT_SMYBOL_ESP;
			  	}
			  	
			  	var form=document.forms['frmBJGX'];
			  	form.expression.value=qry;
			  	
			  	if(form.expression.value.replace(" ","")==""){
			  		alert("���ޱ��ڱ����ʽ��������ֹ!\n");
			  		return false;
			  	}
			  				  	
			  	return true;
			  }
			  
			  /**
			   * ��һ���¼�
			   */
			   function _next(){
			     	var form=document.forms['frmBJGX'];
				  	//window.location="<%=request.getContextPath()%>/template/mod/preEditBSFW.do" + 
			  		window.location="<%=request.getContextPath()%>/template/viewBSFW.do" + 
			  		"?childRepId=" + form.childRepId.value + 
			 		"&versionId=" + form.versionId.value + 
			 		"&reportName="+form.reportName.value;			 		
			 		
			  }
		//-->
		</script>
</head>
<body background="../../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
					<tr><td height="8"></td></tr>
					<tr>
						<td>
							��ǰλ�� >> ������� >> ���ڱ���ϵ�趨
						</td>
					</tr>
					<tr><td height="10"></td></tr>		
	</table>
	<table border="0" cellspacing="0" cellpadding="4" width="80%" align="center">
		<tr>
			<td height="10"></td>
		</tr>
		<tr>
			<td>
				<fieldset id="fieldset">
					<legend>
						<strong>
							���ڱ���ϵ�趨
						</strong>
					</legend>
					<br>
					<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="90%" class="bgcolor" align="center">
						<tr>
							<td class="tableHeader" width="8%">
								���
							</td>
							<td class="tableHeader" width="80%">
								���ʽ
							</td>
							<td class="tableHeader" width="12%">
								����
							</td>
						</tr>
						<logic:present name="Expressions" scope="request">
							<logic:iterate id="item" name="Expressions" scope="request" indexId="index">
								<tr bgcolor="#FFFFFF">
									<td align="center">
										<%=((Integer)index).intValue() + 1%>
									</td>
									<td align="center">
										<bean:write name="item" property="content"/>
									</td>
									<td align="center" id="<bean:write name='item' property='type'/>">
										<logic:equal name="item" property="type" value="<%=flag_bl%>">
											<font color="#008066">���ڹ�ʽ</font>
										</logic:equal>
										<logic:equal name="item" property="type" value="<%=flag_bj%>">
											<font color="#CC0000">��乫ʽ</font>
										</logic:equal>
										<logic:equal name="item" property="type" value="<%=flag_kp%>">
											<font color="#CC0000">��Ƶ�ȹ�ʽ</font>
										</logic:equal>										
										<logic:equal name="item" property="type" value="<%=flag_js%>">
											<font color="blue">���㹫ʽ</font>
										</logic:equal>
									</td>
								</tr>
							</logic:iterate>
						</logic:present>
					</table>
					<br>
					<form name="frmBJGX" method="post" action="../saveBJGX.do" onsubmit="return _submit(this)">
						<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
							<tr>
								<td align="center">
									<input type="button" value="������ʽ" class="input-button" onclick="_load_gx()">
									&nbsp;&nbsp;
									<input type="submit" value=" �� �� " class="input-button">
									&nbsp;&nbsp;
									<input type="button" value="��һ��" class="input-button" onclick="_next()">
									&nbsp;&nbsp;
									<!-- <input type="button" class="input-button" onclick="" value=" �� �� ">-->
								</td>
							</tr>
						</table>
						<logic:present name="ObjForm" scope="request">
							<input type="hidden" name="childRepId" value="<bean:write name='ObjForm' property='childRepId'/>">
							<input type="hidden" name="versionId" value="<bean:write name='ObjForm' property='versionId'/>">
							<input type="hidden" name="reportName" value="<bean:write name='ObjForm' property='reportName'/>" />
							<input type="hidden" name="reportStyle" value="<bean:write name='ObjForm' property='reportStyle'/>"/>
						</logic:present>
						<logic:notPresent name="ObjForm" scope="request">
							<html:hidden property="childRepId" />
							<html:hidden property="versionId" />
							<html:hidden property="reportName" />
							<html:hidden property="reportStyle"/>
						</logic:notPresent>
						<input type="hidden" name="expression" value="">
					</form>
				</fieldset>
			</td>
		</tr>
		
	</table>
</body>
</html:html>
