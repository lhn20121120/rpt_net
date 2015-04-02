<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.MCellFormuForm,com.cbrc.smis.util.FitechUtil,com.cbrc.smis.common.Config"%>
<jsp:useBean id="expressionBean" scope="page" class="com.cbrc.smis.other.Expression" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<%    
	String appPath = request.getContextPath();
	String flag_kp = String.valueOf(expressionBean.FLAG_KP);
	String flag_bl = String.valueOf(expressionBean.FLAG_BL);
	String flag_bj = String.valueOf(expressionBean.FLAG_BJ);
	String flag_js = String.valueOf(expressionBean.FLAG_JS);

	String childRepId = null, versionId = null, reportName = null, reportStyle = null;

	if (request.getParameter("childRepId") != null)
		childRepId = (String) request.getParameter("childRepId");
	if (request.getParameter("versionId") != null)
		versionId = (String) request.getParameter("versionId");
	if(request.getAttribute("ReportName")!=null){
		reportName=(String)request.getAttribute("reportName");
	}else{
		if (request.getParameter("reportName") != null)
			reportName = FitechUtil.getParameter(request, "reportName");
	}
	if (request.getParameter("reportStyle") != null)
		reportStyle = (String) request.getParameter("reportStyle");

	if (request.getAttribute("ObjForm")==null && childRepId != null && versionId != null && reportName != null
		&& reportStyle != null) {
		MCellFormuForm mCellForumForm = new MCellFormuForm();
		mCellForumForm.setChildRepId(childRepId);
		mCellForumForm.setVersionId(versionId);
		mCellForumForm.setReportName(reportName);
		mCellForumForm.setReportStyle(reportStyle);

		request.setAttribute("ObjForm", mCellForumForm);
	}
%>
<html:html locale="true">
<head>
	<html:base />
<%--	<title>--%>
<%--		���ڡ�����ϵ�趨--%>
<%--	</title>--%>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/comm.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
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
			 var SPLIT_SMYBOL_COMMA="<%=configBean.SPLIT_SYMBOL_COMMA%>";
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
			 	 
			 	 window.location="<%=appPath%>/gznx/qdvalidate/addValidateFormula.jsp?" + 
			 	 	"templateId=" + objForm.elements['templateId'].value + 
			 	 	"&versionId=" + objForm.elements['versionId'].value + 
			 	 	"&reportName=" + objForm.elements['reportName'].value +
			 	 	"&reportStyle=" + objForm.elements['reportStyle'].value +
			 	 	"&curPage=" + objForm.elements['curPage'].value;
			 }
			 
			 /**
			  * ���ύ�¼�
			  */
			  function _submit(){
			  	var objCount=document.getElementById("rCount");
			  	var count=eval(objCount.value);
			  	var delCellFormuIds="";
			  	for(var i=0;i<count;i++){
			  		var objChk=eval("document.getElementById(\"chk" + i + "\")");
			  		if(objChk.checked==true)
			  			delCellFormuIds+=(isEmpty(delCellFormuIds)==true?"":SPLIT_SMYBOL_COMMA) + objChk.value;
			  	}
			  	
			  	if(isEmpty(delCellFormuIds)==true){
			  		alert("��ѡ��Ҫɾ���ı��ʽ!\n");
			  		return false;
			  	}
			  	if(confirm("��ȷ��Ҫɾ����ǰѡ�еı��ʽ��?\n")==false){
			  		return false;
			  	}
				
				document.forms['frmBJGX'].elements['formuIds'].value=delCellFormuIds;
			  	return true;
			  }
			  
			  /**
			   * �����¼�
			   */
			   function _back(templateId,versionId){
			   	 var form=document.forms['frmBJGX'];
			   	 window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+templateId+"&versionId="+versionId+"&bak2=2";
			   }
			   
			  /**
		  	  * ȫѡ����
		  	  */
		  	  function _selAll(){
		  	  	var objSelAll=document.getElementById('chkAll');
		  	  	var count=document.getElementById('rCount').value;
		  	  	var objChk=null;
		  	  	for(var i=0;i<count;i++){
		  	  		objChk=eval("document.getElementById('chk" + i + "')");
		  	  		if(objSelAll.checked==false){
		  	  			objChk.checked=false;
		  	  		}else{
		  	  			objChk.checked=true;
		  	  		}
		  	  	}
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
	<table border="0" cellspacing="0" cellpadding="4" width="100%" align="center">		
		<tr><td height="8"></td></tr>
		<tr>
			<td>
				��ǰλ�� >> ������� >> ģ��ά�� >> У���ϵ�޸�
			</td>
		</tr>
		<tr><td height="10"></td></tr>		
		<tr>
			<td>
				<fieldset id="fieldset">

					<div id="server">
					<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="100%" class="bgcolor" align="center">
						<tr class="titletab">
							<th colspan="4" align="center">
								<strong>
									<logic:present name="ObjForm" scope="request">
										��<bean:write name="ObjForm" property="reportName" />��
									</logic:present>									
								</strong>
							</th>
						</tr>
						<tr>
							<td class="tableHeader" width="3%">
								<input type="checkbox" id="chkAll" onclick="_selAll()">
							</td>						
							<td class="tableHeader" width="8%">
								����
							</td>
							<td class="tableHeader" width="5%">
								�к�
							</td>
							<td class="tableHeader" width="8%">
								У������
							</td>
							<td class="tableHeader" width="20%">
								У�鹫ʽ
							</td>
							<td class="tableHeader" width="20%">
								��ʽ˵��
							</td>
							<td class="tableHeader" width="20%">
								������Ϣ
							</td>				
							<td class="tableHeader" width="12%">
								����
							</td>
							<td class="tableHeader" width="10%">
								<b>����</b>
							</td>
						</tr>
						<logic:present name="<%=Config.RECORDS%>" scope="request">
							<%int count = 0;%>							
							<logic:iterate id="item" name="<%=Config.RECORDS%>" scope="request" indexId="index">
								<tr bgcolor="#FFFFFF">
									<td align="center">
										<input type="checkbox" name="chk<bean:write name='index'/>" id="chk<bean:write name='index'/>" value="<bean:write name='item' property='formulaId'/>">
									</td>
														
									<td align="center">
										<bean:write name="item" property="colName" />
									</td>
									<td align="center">
										<bean:write name="item" property="colNum" />
									</td>
									<td align="center">
										<bean:write name="item" property="formuName" />
									</td>
									<td align="center">
										<bean:write name="item" property="formuValue" />
									</td>
									<td align="center">
										<bean:write name="item" property="formuDes" />
									</td>
									<td align="center">
										<bean:write name="item" property="validateMessage" />
									</td>
									<td align="center">
										<logic:equal name="item" property="formuType" value="1">
											<font color="#008066">
												���ڵ�һУ��
											</font>
										</logic:equal>
										<logic:equal name="item" property="formuType" value="2">
											<font color="#CC0000">
												�������У��
											</font>
										</logic:equal>
										<logic:equal name="item" property="formuType" value="3">
											<font color="#CC0000">
												������У��
											</font>
										</logic:equal>										
									</td>
									<td align="center">
									<a
										href="<%=request.getContextPath()%>/gznx/editQDValidate.do?templateId=<bean:write name="item" property="templateId"/>&versionId=<bean:write name="item" property="versionId"/>&formulaId=<bean:write name="item" property="formulaId"/>">�޸�</a>
									</td>
								</tr>
								<%count++;%>
							</logic:iterate>
							
							<input type="hidden" name="rCount" id="rCount" value="<%=count%>">
						</logic:present>
						<logic:notPresent name="<%=Config.RECORDS%>" scope="request">
							<tr>
								<td colspan="4" bgcolor="#FFFFFF">
									���޹�ϵ���ʽ
								</td>
							</tr>
						</logic:notPresent>
					</table>					
					</div>
					<br>
					<form name="frmBJGX" method="post" action="<%=request.getContextPath()%>/gznx/deleteQDValidate.do" onsubmit="return _submit(this)">
						<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
							<tr>
								<td align="center">  
								
									<input type="button" value="���ӱ��ʽ" class="input-button" onclick="_load_gx()">
									&nbsp;&nbsp;
     
                                    <logic:present name="<%=Config.RECORDS%>" scope="request">
                                     <input type="submit" value=" ɾ �� " class="input-button">
                                    </logic:present>

									&nbsp;&nbsp;
									<input type="button" class="input-button" onclick="_back('<bean:write name='ObjForm' property='templateId'/>','<bean:write name='ObjForm' property='versionId'/>')" value=" �� �� ">
								</td>
							</tr>
						</table>
						<logic:present name="ObjForm" scope="request">
							<input type="hidden" name="templateId" value="<bean:write name='ObjForm' property='templateId'/>">
							<input type="hidden" name="versionId" value="<bean:write name='ObjForm' property='versionId'/>">
							<input type="hidden" name="reportName" value="<bean:write name='ObjForm' property='reportName'/>">
							<input type="hidden" name="reportStyle" value="<bean:write name='ObjForm' property='reportStyle'/>">
						</logic:present>
						<logic:notPresent name="ObjForm" scope="request">
							<html:hidden property="templateId" />
							<html:hidden property="versionId" />
							<html:hidden property="reportName" />
							<html:hidden property="reportStyle" />
						</logic:notPresent>


						<input type="hidden" name="formuIds" value="">
						<logic:present name="CurPage" scope="request">
							<input type="hidden" name="curPage" value="<bean:write name='CurPage' scope='request'/>">
						</logic:present>
						<logic:notPresent name="CurPage" scope="request">
							<input type="hidden" name="curPage" value="0">
						</logic:notPresent>
					</form>
				</fieldset>
			</td>
		</tr>

	</table>
	
</body>
</html:html>
