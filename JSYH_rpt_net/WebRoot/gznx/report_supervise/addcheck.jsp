<%@ page contentType="text/html;charset=gb2312"%>
<%@page import="com.fitech.gznx.form.AddcheckForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.hibernate.ValidateType"%>
<%@ page import="com.cbrc.smis.form.MCellFormuForm,com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="expressionBean" scope="page" class="com.cbrc.smis.other.Expression"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<html:html>
	<head>
		<title>У�鹫ʽ���</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
		<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
		<link rel="stylesheet" type="text/css" media="all" href="../../js/jscalendar/calendar.css" title="Aqua" />
		<script type="text/javascript" src="../../js/jscalendar/calendar.js"></script>
		<script type="text/javascript" src="../../js/jscalendar/lang/calendar-zh.js"></script>
		<script type="text/javascript" src="../../js/jscalendar/pageInclude.js"></script>
	<script language="javascript">
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
			
			
			 function history(){
			 	 var objForm=document.forms['MCellFormuForm'];
			 	 window.location="<%=request.getContextPath()%>/check.do?"+
			 	 "childRepId=" + objForm.elements['childRepId'].value + 
			 	 "&versionId=" + objForm.elements['versionId'].value; 
			 }
			 	  
			function _submit(){
			//��ʽ����
			var cellFormuView = document.getElementById('cellFormuView');
			//��ʽ
			var _cellFormu = document.getElementById('_cellFormu');
			if(formulaName.value=="")
			{
				alert('���Ķ��岻��Ϊ�գ�');
				cellFormuView.focus();
				return false;
			}
			if(formulaValue.value=="")
			{
				alert('��ʽ����Ϊ�գ�');
				_cellFormu.focus();
				return false;
			}
			return ture;
			}
		</script>
		<%
		String flag_kp=String.valueOf(expressionBean.FLAG_KP);		
		String flag_bl=String.valueOf(expressionBean.FLAG_BL);
		String flag_bj=String.valueOf(expressionBean.FLAG_BJ);
		String flag_js=String.valueOf(expressionBean.FLAG_JS);
		String childRepId="",versionId="" ,reportName="",reportStyle="";
		if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
		if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");
		if(request.getParameter("reportName")!=null) reportName=FitechUtil.getParameter(request,"reportName");
		if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==1){
		 reportName=new String(reportName.getBytes("iso-8859-1"), "gb2312");
		 }
		if(request.getParameter("reportStyle")!=null) reportStyle=(String)request.getParameter("reportStyle");
%>
	</head>
	<body>
		<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<form name="MCellFormuForm" method="post" action="<%=request.getContextPath()%>/gznx/addsaveCheck.do" onsubmit="return _submit()">
		<table cellSpacing="0" cellPadding="0" width="100%" border="0" align="center">
			<TR height="40">
				<TD align="left" width="100%" style="color:red;">
					
				</TD>
			</TR>
		</TABLE>
			<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">

				<tr>
					<td align="right" valign="top">
						<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor" id="tb">

							
							
							<tr>
								<td height="70" align="center" bgcolor="#FFFFFF">
									<table width="100%" border="0">													
										<TR align="center">
											<TD >
												��ӱ��ڱ���ϵ��ʽ
											</TD>
											
										</TR>
									</table>
							<tr>
								<td bgcolor="#FFFFFF" align="center">
									<table id="tb1">
											<TR height="40" >
											<TD align="center" colspan="4">
											<SELECT id="_formuType" name="_formuType">
												<OPTION value="1">
													����У��
												</OPTION>
												<OPTION value="2">
													���У��
												</OPTION>
											</SELECT>
											</td>
											</TR>
											<tr>
												<td align="right">
													��ʽ����
												</td>
												<td colspan="3">
													<textarea name="_cellFormu" property="_cellFormu" cols="100" rows="6" id="_cellFormu"></textarea>
												</td>
											</tr>
											<TR>
												<TD align="right">
													���ʽ˵��
												</TD>
												<TD colspan="4">
													<textarea name="cellFormuView" property="cellFormuView" cols="100" rows="6" id="cellFormuView"></textarea>
												</TD>
											</TR>
									</table>
								</td>
							</tr>
							
							<tr>
								<td bgcolor="#FFFFFF" align="center">
									<table>
										<tr>
											<td colspan="6" align="right">
												<input type="submit" class="input-button" value=" �� �� " />
												<input type="button" name="back" value=" �� �� " class="input-button" onclick="history()" />
											</td>
										</tr>
										
									</table>
								</td>
							</tr>
							<input type="hidden" name="childRepId" value="<%=childRepId%>">
							<input type="hidden" name="versionId" value="<%=versionId%>">
							<input type="hidden" name="reportName" value="<%=reportName%>">
							<input type="hidden" name="reportStyle" value="<%=reportStyle%>">
			</table>	
	</td>
	</tr>
	</table>
	</form>
			
	</body>
</html:html>
