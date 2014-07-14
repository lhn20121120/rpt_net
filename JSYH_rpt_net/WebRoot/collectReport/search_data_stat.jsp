<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config,com.cbrc.smis.security.Operator"%>
<%
	String reportFlg = "1";
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom()+
	 	" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" + reportFlg +")" : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = orgId;
	request.setAttribute("orgId",orgId);
	/******************************************/
	int rNum = 0; //��¼������
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";			
	String term = request.getAttribute("term") != null ? request
			.getAttribute("term").toString() : request
			.getParameter("term");
	String year = request.getAttribute("year") != null ? request
			.getAttribute("year").toString() : request
			.getParameter("year");
	String repName = request.getParameter("repName") != null ? request
			.getParameter("repName") : "";
	String childRepId = request.getParameter("childRepId") != null
			? request.getParameter("childRepId")
			: "";
	String frOrFzType = request.getParameter("frOrFzType") != null
			? request.getParameter("frOrFzType")
			: "-999";
%>
<jsp:useBean id="collectUtil" class="com.fitech.dataCollect.DB2ExcelHandler"></jsp:useBean>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>

<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepReportPodedom%>"/>

<html:html locale="true">
<head>
	<html:base />
	<title>���������б�</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<link rel="alternate stylesheet" type="text/css" media="all" href="<%=basePath%>calendar/calendar-blue.css" title="winter">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=basePath%>calendar/calendar-brown.css" title="summer">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=basePath%>calendar/calendar-green.css" title="green">
<link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>calendar/calendar-win2k-1.css" title="win2k-1">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=basePath%>calendar/calendar-win2k-2.css" title="win2k-2">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=basePath%>calendar/calendar-win2k-cold-1.css" title="win2k-cold-1">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=basePath%>calendar/calendar-win2k-cold-2.css" title="win2k-cold-2">
<script type="text/javascript" src="<%=basePath%>calendar/calendar.js"></script>
<script type="text/javascript" src="<%=basePath%>calendar/calendar-cn.js"></script>
<script language="javascript" src="<%=basePath%>calendar/calendar-func.js"></script>
	<script language="javascript" src="../js/comm.js"></script>
	<script language="javascript" src="../js/func.js"></script>

	<script type="text/javascript" src="../js/tree/tree.js"></script>
	<script type="text/javascript" src="../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
	<script language="javascript">
	
		//����ID
		var repInId =-1;
		
		/**
		 * ���ܵ�������
		 */
		function _collect1(orgId,repId,versionId,dataRangeId,curId,year,term,donum){
			var objFrm=document.forms['frm'];
			var qry="childRepId=" + objFrm.elements['childRepId'].value +
					"&orgId="+orgId+ 
					"&repName=" + objFrm.elements['repName'].value + 
					"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 
					"&repId=" + repId + 
					"&versionId=" + versionId + 
					"&dataRangeId=" + dataRangeId + 
					"&curId=" + curId + 
					"&year=" + year + 
					"&term="  + term + "&type=one";
							
			if(donum==0){
				alert("û�������ϱ�,�޷�����!");
			}else{
				if(confirm("ȷ��Ҫ������")){
					location.href="<%=request.getContextPath()%>/collectReport/collectReportData.do?" + qry;
					prodress1.style.display = "none" ;
		   			prodress.style.display = "" ;	
				}
			}
		}

		//ת�ϱ�����
		function translate(repInId,year,term){
			var objFrm=document.forms['frm'];
			var qry="childRepId=" + objFrm.elements['childRepId'].value + 
					"&repName=" + objFrm.elements['repName'].value + 
					"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 
					"&repInId=" + repInId + 
					"&year=" + year + 
					"&term="  + term;
					
			if(confirm("ȷ��Ҫ������ת�ϱ�������")){
				location.href="<%=request.getContextPath()%>/collectReport/translationReport.do?" + qry;
			}
		}

		/**
		 * �������б���
		*/ 
	<%--	function _collectAll(year,month){
			window.location="<%=request.getContextPath()%>/collectReport/docollect.jsp?year="+year+"&month="+month+"&type=all";
		}
		
		function _collectAll1(year,month){			
			if(confirm("ȷ��Ҫȫ��������")){
				window.location="<%=request.getContextPath()%>/collectReport/testDoCollect.jsp?year="+year+"&month="+month+"&type=all";
			}
		}
		--%>
		/**
		 * ���ύ��֤
		 */
		function _submit(form){
			if(form.date.value==""){
					alert("�����뱨��ʱ��!");
					form.date.focus();
					return false;
				}
			//alert(document.getElementsByName("orgId")[0].value);
		}

		/**
		 * ȫѡ����
		 */
		function _selectAll(){
			if(document.getElementById('data_collectAll').checked==false){
				_cancelAll()
			}else{
		  		for(var i =0;i<document.form1.data_collect.length;i++){
		  			document.form1.data_collect[i].checked = true;
		  		}
			}
	  	}
	  	
	  	/**
		 * ȫȡ������
		 */
	  	function _cancelAll(){
	  		for(var i=0;i<document.form1.data_collect.length;i++)
	  		{
	  			document.form1.data_collect[i].checked = false;
	  			document.form1.data_collectAll.checked = false;
	  		}
	  	}
	  	
	  	/**
		 * ������ѡ�����
		 */
		function _collect_select(form){
			var flag=false;
	   		var values="";
	   		var rNum=0;
	   		var sum=0;
	   		var checks=document.getElementsByName("data_collect");
	   		for(var k=0;k<checks.length;k++){
				if(checks[k].checked){
					sum++;
				}
			}
			
	  		var j=0;
	  		
	   		for(var i=0;i<checks.length;i++){
				if(checks[i].checked){
					rNum++;
	  		  		flag=true;
	  		  		values+=Trim(checks[i].value)+"#";	
	  		  		if(rNum%20==0){
						document.getElementById('select_data_collect_id'+[j]).value=values;	  		 		  
	  		  		  	j++;
	  		  		  	sum-=20;
	  		  		  	values="";
	  		  		}
					if(sum<20 ){ 
						if(sum==1){
		  		  			document.getElementById('select_data_collect_id'+[j]).value=values;	
	  		  			}else{
	  		  				sum--;
	  		  			}
					}	  		  	
	  			}
	  		}
	  		 
	  		if(flag==true){	  		
	  				 prodress1.style.display = "none" ;
		   			 prodress.style.display = "" ;	
	  				return true;
	  		}else{
	  			alert("��ѡ�������!");
	  			return false;
	  		} 	  	
		}
		
		/**
		 * �鿴��������
		 */
		function viewExcel(repInId){
			location.href="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}
		
		/**
		 * �鿴���ܹ�ϵ
		 */
		function viewCollectInfo(orgId,childRepId,versionId,dataRangeId,year,mon,currId){
			window.open("<%=request.getContextPath()%>/viewCollectOrgInfo.do?" +
					"orgId=" + orgId +
					"&childRepId=" + childRepId +
					"&versionId=" + versionId +
					"&dataRangeId=" + dataRangeId +
					"&year=" + year +
					"&mon=" + mon +
					"&currId=" + currId
					,'���ܱ����ϱ���Ϣ�鿴'
					,'scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
					
		
		}
		
		
			//��ʾ,�ر����Ͳ˵�
		function showTree(){
			if(document.getElementById('orgTree').style.visibility =='hidden'){
			    var textname = document.getElementById('selectedTypeName');
				document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
				document.getElementById("orgTree").style.left = getObjectLeft(textname);
				
				document.getElementById("orgTree").style.height = "200";
				document.getElementById("orgTree").style.visibility = "visible";   // ��ʾ���Ͳ˵�
			}
	
			else if(document.getElementById("orgTree").style.visibility == "visible"){
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden";      //�ر����Ͳ˵�
			}
		}
		function treeOnClick1(id,value)
	{
		document.getElementById('orgId').value = id;
		document.getElementById('orgName').value = value;
		document.getElementById("orgpreTree").style.height = "0";
		document.getElementById('orgpreTree').style.visibility="hidden"; 
	}
	function showTree1(){
		if(document.getElementById('orgpreTree').style.visibility =='hidden'){
		    var textname = document.getElementById('orgName');
			document.getElementById("orgpreTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgpreTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgpreTree").style.height = "200";
			document.getElementById("orgpreTree").style.visibility = "visible";   // ��ʾ���Ͳ˵�
		}

		else if(document.getElementById("orgpreTree").style.visibility == "visible"){
			document.getElementById("orgpreTree").style.height = "0";
			document.getElementById('orgpreTree').style.visibility="hidden";      //�ر����Ͳ˵�
		}
	}
	
		function closeTree(objTxt,objTree){	  
		   var obj = event.srcElement;
		   if(obj!=objTxt && obj!=objTree){
		     
		     objTree.style.height = "0";
		     objTree.style.visibility="hidden";      //�ر����Ͳ˵�
		   }
		}
		
		//�����ı����ˮƽ���λ��
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}   
		//�����ı���Ĵ�ֱ���λ��
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}
	</script>
</head>
<body>
	<label id="prodress" style="display:none">
		<span class="txt-main" style="color:#FF3300">���ڻ��ܣ����Ժ�......</span>
	</label>
	<label id="prodress1" >
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
					��ǰλ�� &gt;&gt; ������ &gt;&gt; �������
				</td>
			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</table>
		<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
			<html:form action="/viewCollectData" method="post" styleId="frm" onsubmit="return _submit(this)">
			<html:hidden property="orgId"/>
				<tr>
					<td>
						<fieldset id="fieldset">
							<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
								<tr>
									<td height="5"></td>
								</tr>
								<tr>
									<td height="25">&nbsp;
										�����ţ�
										<html:text property="childRepId" maxlength="6" size="6" styleClass="input-text"/>
									</td>
									<td height="25" align="left">
										�������ƣ�
										<html:text property="repName" size="25" styleClass="input-text" />
									</td>																
								</tr>
								<tr><td height="2"></td></tr>											
								<tr>
							<%--	
									<td height="25">&nbsp;
										����ʱ�䣺
										<html:text property="year" maxlength="4" size="4" styleClass="input-text" />
										��
										<html:text property="term" maxlength="2" size="2" styleClass="input-text" />
										��
									</td>
									--%>
									<td height="25">
										&nbsp;&nbsp;����ʱ�䣺
										<html:text property="date" styleClass="input-text" size="10"
											styleId="date1" readonly="true"
											onclick="return showCalendar('date1', 'y-mm');" />
										<img border="0" src="<%=basePath%>image/calendar.gif"
											onclick="return showCalendar('date1', 'y-mm');">
									</td>
									<td height="25" align="left">
										ģ�����ͣ�
										<html:select property="frOrFzType" size="1">
											<html:option value="-999">--ȫ��--</html:option>
											<html:option value="1">����ģ��</html:option>
											<html:option value="2">��֧ģ��</html:option>
										</html:select>																													
									</td>
									<td height="23" align="left">
										���������
										<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text" ></html:text>
										<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
										<script type="text/javascript">
											<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
										    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
									      	tree1.init();
									 	</script>
									 	</div>
									</td>								
									<td><html:submit styleClass="input-button" value=" �� ѯ " /></td>
								</tr>
								<tr>
									<td height="3"></td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</html:form>
		</table>
		<br />
		<form name="form1">		
			<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
				<tr class="titletab" id="tbcolor">
					<th height="24" align="center" id="list" colspan="11">
						���������б�
					</th>
				</tr>
				<TR class="middle">
					<TD  align="center" width="4%"><!-- 
						<logic:present name="Records" scope="request">
							<INPUT type="checkbox" name="data_collectAll" id="data_collectAll" value="" onClick="_selectAll()" />
						</logic:present> -->
					</TD>
					<TD  align="center" width="4%">
						<strong>���</strong>
					</TD>
					<TD  align="center" width="">
						<strong>��������</strong>
					</TD>
					<TD  align="center" width="5%">
						<strong>�汾��</strong>
					</TD>
					<TD  align="center" width="11%">
						<strong>����ھ�</strong>
					</TD>
					<TD  align="center" width="4%">
						<strong>Ƶ��</strong>
					</TD>
					<TD  align="center" width="6%">
						<strong>����</strong>
					</TD>
					<TD  align="center" width="10%">
						<strong>����</strong>
					</TD>
					<TD  align="center" width="8%">
						<strong>�������<br>(ʵ��/Ӧ��)</strong>
					</TD>
					<TD align="center" width="14%">
						<strong>����</strong>
					</TD>
				</TR>
				<logic:present name="Records" scope="request">
					<logic:iterate id="viewReportIn" name="Records" indexId="index">
						<%
							rNum++;
						%>
						<tr bgcolor="#FFFFFF">
							<td align="center">
								<INPUT type="checkbox" name="data_collect"		
							value="<bean:write name="viewReportIn" property="childRepId"/>,<bean:write name="viewReportIn" property="versionId"/>,<bean:write name='viewReportIn' property='curId' />,<bean:write name='viewReportIn' property='dataRgId' />,<bean:write name='viewReportIn' property='donum' />,<bean:write name='viewReportIn' property='orgId' />" />
							</td>
							<td align="center">
								<bean:write name="viewReportIn" property="childRepId" />
							</td>
							<td align="center">
	
								<!-- ����ǻ��ܹ��ı���,����ʾ����   -->
								<logic:equal name="viewReportIn" property="isCollected" value="1">
									<a href="javascript:viewExcel('<bean:write name="viewReportIn" property="repInId" />')"><bean:write
											name="viewReportIn" property="repName" />
									</a>
								</logic:equal>
								<!-- û�л��ܵ�  -->
								<logic:equal name="viewReportIn" property="isCollected" value="0">
									<bean:write name="viewReportIn" property="repName" />
								</logic:equal>
							</td>
							<td align="center">
								<bean:write name="viewReportIn" property="versionId" />
							</td>
							<td align="center">
								<bean:write name="viewReportIn" property="dataRgTypeName" />
							</td>
							<td align="center">
								<bean:write name="viewReportIn" property="actuFreqName" />
							</td>
							<td align="center">
								<bean:write name="viewReportIn" property="currName" />
							</td>
							<td align="center">
								<bean:write name="viewReportIn" property="orgName" />
							</td>
							<td align="center">
								<a href="javascript:viewCollectInfo(
									'<bean:write name="viewReportIn" property="orgId" />',
									'<bean:write name="viewReportIn" property="childRepId" />',
									'<bean:write name="viewReportIn" property="versionId" />',
									'<bean:write name='viewReportIn' property='dataRgId' />',
									'<bean:write name='viewReportIn' property='year' />',
									'<bean:write name='viewReportIn' property='term' />',
									'<bean:write name='viewReportIn' property='curId' />')">
									<bean:write name="viewReportIn" property="repInFo" /></a>
							</td>
							<td align="center">
								<input type="button" class="input-button"
									onClick="_collect1(
									 '<bean:write name="viewReportIn" property="orgId" />',
									 '<bean:write name="viewReportIn" property="childRepId" />',
									 '<bean:write name="viewReportIn" property="versionId" />',
							  		 '<bean:write name='viewReportIn' property='dataRgId' />',
							  		 '<bean:write name='viewReportIn' property='curId' />',
							  		 '<bean:write name='viewReportIn' property='year' />',
							  		 '<bean:write name='viewReportIn' property='term' />',
							  		  '<bean:write name='viewReportIn' property='donum' />')"
									value="�� ��">
								
								<!-- ���ܹ���  -->
								<logic:equal name="viewReportIn" property="isCollected" value="1">
									<!-- ���ܹ���  -->
									<input type="button" class="input-button"
										onClick="translate(
										'<bean:write name="viewReportIn" property="repInId" />', 
										'<bean:write name='viewReportIn' property='year' />',
										'<bean:write name='viewReportIn' property='term' />')"
										value="ת�ϱ�����">
								</logic:equal>
								<!-- û�л��ܵ�  -->
								<logic:equal name="viewReportIn" property="isCollected" value="0">
									<input type="button" disabled class="input-button" value="ת�ϱ�����">
								</logic:equal>
	
							</td>
						</tr>
					</logic:iterate>
				</logic:present>
	
				<logic:notPresent name="Records" scope="request">
					<tr bgcolor="#FFFFFF">
						<td colspan="10">
							���޻�������
						</td>
					</tr>
				</logic:notPresent>
	
			</table>
			<br />
		</form>
		<form name="form2" action="<%=request.getContextPath()%>/collectReport/collectReportData.do"
			onsubmit="return _collect_select(this)">
			<logic:present name="Records" scope="request">
				<table width="99%" border="0" cellpadding="4" cellspacing="1">
					<%
						for (int m = 0; m < rNum / 20 + 1; m++) {
					%>
						<INPUT type="hidden" name="select_data_collect_id<%=m%>" id="select_data_collect_id<%=m%>" Value="" />
					<%
						}
					%>
					<INPUT type="hidden" name="select_collect_type" id="select_collect_type" Value="multi" />
					<INPUT type="hidden" name="year" id="year" Value="<%=year%>" />
					<INPUT type="hidden" name="term" id="term" Value="<%=term%>" />
					<INPUT type="hidden" name="repName" id="repName" Value="<%=repName%>" />
					<INPUT type="hidden" name="childRepId" id="childRepId" Value="<%=childRepId%>" />
					<INPUT type="hidden" name="frOrFzType" id="frOrFzType" Value="<%=frOrFzType%>" />
					<INPUT type="hidden" name="donum" id="donum" Value="<bean:write name='viewReportIn' property='donum' />" />
					<tr>
						<td>
							<input type="submit" class="input-button" value="����ѡ��">
							<!-- &nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" class="input-button" onClick="_cancelAll()" value="ȫ��ȡ��"> -->
						</td>
					</tr>
				</table>
			</logic:present>
		</form>
	</label>
</body>
</html:html>