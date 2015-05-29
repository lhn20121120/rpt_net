<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.security.Operator"%>

<%
	int rNum = 0; //��¼������	
	String date = request.getAttribute("date") != null ? request.getAttribute("date").toString() : request.getParameter("date");
	String repName = request.getParameter("repName") != null ? request.getParameter("repName") : "";
	String templateId = request.getParameter("templateId") != null ? request.getParameter("templateId") : "";
	String bak1 = request.getParameter("bak1") != null ? request.getParameter("bak1") : "-999";
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	
	String add_old_collect =Config.ADD_OLD_COLLECT+"";
	request.setAttribute("add_old_collect",add_old_collect);
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom()
		+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='" + reportFlg +"')" : "";
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";

	String backQry = "";
	if(request.getAttribute("backQry")!=null)
		backQry = request.getAttribute("backQry").toString();
	
	
	request.setAttribute("backQry",backQry);

%>
<jsp:useBean id="collectUtil" class="com.fitech.dataCollect.DB2ExcelHandler"></jsp:useBean>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepReportPodedom%>"/>
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<html:html locale="true">
<head>
	<html:base />
	<title>���������б�</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="<%=request.getContextPath() %>/js/comm.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/js/progressBar.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	
	<script language="javascript">
		var  progressBar=new ProgressBar("���ڲ�ѯ�����Ժ�........");
		//����ID
		var repInId =-1;
		
		/**
		 * ���ܵ�������
		 */
		function _collect(templateId,versionId,year,term,day,repFreqId,curId,orgId,donum){
			var objFrm=document.forms['frm'];
			var qry="templateId=" + templateId
						+ "&versionId=" + versionId
						+ "&year=" + year
						+ "&term=" + term
						+ "&day=" + day
						+ "&repFreqId=" + repFreqId
						+ "&curId=" + curId
						+ "&orgId=" + orgId
						+ "&repId=" + repInId
						+ "&backStr=" + document.getElementById("date").value+"_"+document.getElementById("templateId").value+"_"+document.getElementById("repName").value+"_"+document.getElementById("orgId").value+"_"+document.getElementById("bak1").value+"_"+document.getElementById("repFreqId").value;
						+ "&type=one";

			if(donum==0){
				alert("û�������ϱ�,�޷�����!");			
			}else{
				if(confirm("ȷ��Ҫ������")){
					window.location="<%=request.getContextPath()%>/collectReportNX.do?" + qry;
					prodress1.style.display = "none" ;
		   			prodress.style.display = "" ;	
				}
			}
		}
		//ת�ϱ�����
		function translate(repInId,year,term,day){
			var objFrm=document.forms['frm'];
			var qry="templateId=" + objFrm.elements['templateId'].value + 
					"&repName=" + objFrm.elements['repName'].value + 
					"&bak1=" + objFrm.elements['bak1'].value + 
					"&repInId=" + repInId + 
					"&date=" + objFrm.elements['date'].value;
					
			if(confirm("ȷ��Ҫ������ת�ϱ�������")){
				window.location="<%=request.getContextPath()%>/translationNXReport.do?" + qry;
			}
		}

		//�鿴���ϱ�������Ϣ
		function viewCollectInfo(templateId,versionId,year,term,day,repFreqId,curId,orgId){
			
			window.open("<%=request.getContextPath()%>/viewCollectOrgNX.do?templateId=" + templateId
						+ "&versionId=" + versionId
						+ "&year=" + year
						+ "&term=" + term
						+ "&day=" + day
						+ "&repFreqId=" + repFreqId
						+ "&curId=" + curId
						+ "&orgId=" + orgId
						+ "&date=<%=date%>"
					,'���ܱ����ϱ���Ϣ�鿴'
					,'scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');

		}

		/**
		 * �������б���
		 */
		function _collectAll(date){
			window.location="<%=request.getContextPath()%>/collectReport/docollect.jsp?date=" + date + "&type=all";
		}
		
		function _collectAll1(date){
			if(confirm("ȷ��Ҫȫ��������")){
				window.location="<%=request.getContextPath()%>/collectReport/testDoCollect.jsp?date=" + date + "&type=all";
			}
		}
		
		/**
		 * ���ύ��֤
		 */
		function _submit(form){
			if(form.date.value==""){
				alert("�����뱨��ʱ��!");
				form.date.focus();
				return false;
			}
			progressBar.show();
			return true;
			
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
	   		var cks = document.getElementsByName("data_collect");
	   		for(var k=0;k<cks.length;k++){
				if(cks[k].checked == true){
					sum++;
				}
			}
	  		var j=0;
	   		for(var i=0;i<cks.length;i++){
				if(cks[i].checked == true){
					rNum++;
	  		  		flag=true;
	  		  		values+=Trim(cks[i].value)+"#";	
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
		   			 var searchOrgId = document.getElementById("frm").orgId.value;
		   			 var searchOrgName = document.getElementById("frm").orgName.value;
		   			 document.getElementById("collect_orgId").value = searchOrgId;
		   			 document.getElementById("collect_orgName").value = searchOrgName;
	  				return true;
	  		}else{
	  			alert("��ѡ�������!");
	  			return false;
	  		} 	  	
		}
		function _collect_select2(obj){
			document.getElementById('iterFlag').value="0";
			if(_collect_select(form2)){
				
				form2.submit();
				progressBar.show();
			}
			
		}
		/**
		 * �鿴��������
		 */
		function viewExcel(repInId){
			window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}
		
		 function treeOnClick(id,value)
			{
				document.getElementById('bak1').value = id;
				document.getElementById('templateTypeName').value = value;
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden"; 
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
		var page_donum=0;
		function initMethod(body){
			document.getElementById('donum').value=page_donum;
		}
		// add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
		
		function viewPdf(repInId,templateId,versionId,curId,repFreqId,year,term,day,orgId){
			window.open("<%=request.getContextPath()%>/editAFReport.do?statusFlg=1&repInId=" + repInId
														+"&templateId="+templateId
														+"&versionId="+versionId
														+"&curId="+curId
														+"&repFreqId="+repFreqId
														+"&year="+year
														+"&term="+term
														+"&day="+day
														+"&orgId="+orgId); 
		}
	</script>
</head>
<body onload="initMethod(this)">
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
			<html:form action="/viewCollectNX" method="post" styleId="frm" onsubmit="return _submit(this)">
				<html:hidden property="bak1"/>
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
										<html:text property="templateId" maxlength="10" size="10" styleClass="input-text"/>
									</td>
													
									<td height="25" align="left">
										�������ƣ�
										<html:text property="repName" size="23" styleClass="input-text" />
										
									</td>
									<td height="25" align="left">
										����Ƶ�ȣ�
										<html:select property="repFreqId">
											<html:option value="-999">--ȫ��--</html:option>
											<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
										</html:select>
										
									</td>	
								</tr>
								<tr><td height="2"></td></tr>											
								<tr>
									<td>&nbsp;
										����ʱ�䣺
										<html:text property="date" styleClass="input-text" size="10"
											styleId="date1" readonly="true"
											onclick="return showCalendar('date1', 'y-mm-dd');" />
										<img border="0" src="<%=basePath%>image/calendar.gif"
											onclick="return showCalendar('date1', 'y-mm-dd');">
									</td>
									<!-- 
									<td height="25" align="left">
										�������ͣ�
										<html:text property="templateTypeName" styleId="selectedTypeName" size="10" style="width:150px;cursor:hand" onclick="return showTree()" styleClass="input-text" ></html:text>
										<div id="orgTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
										<script type="text/javascript">
											<bean:write  name="FormBean"  property="templateTypeTree" filter="false"/>
										    var tree= new ListTree("orgTree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
									      	tree.init();
									 	</script>
									 	</div>
									</td>
									 -->
									 <td height="25" align="left">
										���������
										<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" styleClass="input-text" ></html:text>
										<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
										<script type="text/javascript">
											<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
										    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
									      	tree1.init();
									 	</script>
									 	</div>
										
									</td>	
									<td height="25" align="left">
									�������Σ�
									<html:select property="supplementFlag" styleId="supplementFlag">
										<html:option value="-999">ȫ��</html:option>
										<html:option value="0">������&nbsp;&nbsp;</html:option>
										<html:option value="1">��һ��</html:option>
										<html:option value="2">�ڶ���</html:option>
									</html:select>
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
		<form name="form2" action="<%=request.getContextPath()%>/collectReportNX.do"
			onsubmit="return _collect_select(this)" method="post">
			<logic:present name="Records" scope="request">
				<table width="99%" border="0" cellpadding="4" cellspacing="1">
					<%
						for (int m = 0; m < rNum / 20 + 1; m++)
						{
					%>
						<INPUT type="hidden" name="select_data_collect_id<%=m%>" id="select_data_collect_id<%=m%>" Value="" />
					<%
						}
					%>
					<input type="hidden" id="collect_orgId" name ="orgId"/>
					<input type="hidden" id="collect_orgName" name ="orgName"/>
					<INPUT type="hidden" name="select_collect_type" id="select_collect_type" Value="multi" />
					<INPUT type="hidden" name="date" id="date" Value="<%=date%>" />
					<INPUT type="hidden" name="repName" id="repName" Value="<%=repName%>" />
					<INPUT type="hidden" name="templateId" id="templateId" Value="<%=templateId%>" />
					<INPUT type="hidden" name="bak1" id="bak1" Value="<%=bak1%>" />

					<INPUT type="hidden" name="donum" id="donum" Value="" />
					
					<input type="hidden" value="1" id="iterFlag" name="iterFlag"/>
					<tr>
						<td>
							<input type="submit" class="input-button" value="�ݹ����">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" class="input-button" value="��   ��" onclick="_collect_select2(this)">
							<%-- 
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" class="input-button" onClick="_cancelAll()" value="ȫ��ȡ��">
							 --%>
						</td>
					</tr>
				</table>
			</logic:present>
		</form>
		<form name="form1" method="post">
		<logic:present name="add_old_collect">
			<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<logic:equal value="0" name="add_old_collect">
					<th height="24" align="center" id="list" colspan="7">
					���������б�
				</th>
				</logic:equal>
				<logic:equal value="1" name="add_old_collect">
					<th height="24" align="center" id="list" colspan="8">
					���������б�
				</th>
				</logic:equal>
			</tr>
			<TR class="middle">
				<TD  align="center" width="3%">
				<!-- 	<logic:present name="Records" scope="request">
						<INPUT type="checkbox" name="data_collectAll" id="data_collectAll" value="" onClick="_selectAll()" />
					</logic:present>
				-->
				</TD>
				<TD  align="center" width="5%">
					<strong>���</strong>
				</TD>
				<TD  align="center" width="32%">
					<strong>��������</strong>
				</TD>
				<!-- 
				<TD  align="center">
					<strong>�汾��</strong>
				</TD>
				 -->
				<TD  align="center" width="15%">
					<strong>����</strong>
				</TD>
				<TD  align="center" width="7%">
					<strong>Ƶ��</strong>
				</TD>
				<TD  align="center" width="7%">
					<strong>����</strong>
				</TD>
				<!-- <TD  align="center" width="7%">
					<strong>���ܷ�ʽ</strong>
				</TD> -->
				<TD  align="center" width="10%">
					<strong>�������<br/>(ʵ��/Ӧ��)</strong>
				</TD>
				<logic:equal value="1" name="add_old_collect">
					<TD align="center" width="10%">
						<strong>����</strong>
					</TD>
				</logic:equal>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="viewReportIn" name="Records" indexId="index">
					<%
					rNum++;
					%>
					<tr bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
						<td align="center">
							<INPUT type="checkbox" name="data_collect"		
						value="<bean:write name="viewReportIn" property="templateId"/>,
						<bean:write name="viewReportIn" property="versionId"/>,
						<bean:write name='viewReportIn' property='orgId' />,
						<bean:write name='viewReportIn' property='curId' />,
						<bean:write name='viewReportIn' property='actuFreqID' />,
						<bean:write name='viewReportIn' property='donum' />,
						<bean:write name='viewReportIn' property='year' />,
						<bean:write name='viewReportIn' property='term' />,
						<bean:write name='viewReportIn' property='day' />" />
						</td>
						<td align="center">
							<bean:write name="viewReportIn" property="templateId" />
						</td>
						<td align="center">
							<%-- 
							<!-- ����ǻ��ܹ��ı���,����ʾ����   -->
							<logic:equal name="viewReportIn" property="isCollected" value="1">
								<a href="javascript:viewExcel('<bean:write name="viewReportIn" property="repInId" />')"><bean:write
										name="viewReportIn" property="repName" />
								</a>
							</logic:equal>
							<!-- û�л��ܵ�  -->
							<logic:equal name="viewReportIn" property="isCollected" value="0"></logic:equal> 
								<bean:write name="viewReportIn" property="repName" />
							--%>
							
							<a style="text-decoration:underline" href="javascript:viewPdf('<bean:write name='viewReportIn' property='repInId'/>',
									 								'<bean:write name='viewReportIn' property='childRepId'/>',
									 								'<bean:write name='viewReportIn' property='versionId'/>',
									 								'<bean:write name='viewReportIn' property='curId'/>',
									 								'<bean:write name='viewReportIn' property='actuFreqID'/>',
									 								'<bean:write name='viewReportIn' property='year'/>',
									 								'<bean:write name='viewReportIn' property='term'/>',
									 								'<bean:write name='viewReportIn' property='day'/>',
									 								'<bean:write name='viewReportIn' property='orgId'/>')">
											<bean:write name="viewReportIn" property="repName" /></a>
							
						</td>
						<td align="center">
							<bean:write name="viewReportIn" property="orgName" />
						</td>
						<!-- 
						<td align="center">
							<bean:write name="viewReportIn" property="versionId" />
						</td>
						 -->
						<td align="center">
							<bean:write name="viewReportIn" property="actuFreqName" />
						</td>
						<td align="center">
							<bean:write name="viewReportIn" property="currName" />
						</td>
						
						<%-- <td align="center">
							<bean:write name="viewReportIn" property="collectType" />
						</td> --%>
						
						<td align="center">
							<a style="text-decoration:underline" href="javascript:viewCollectInfo(
								'<bean:write name="viewReportIn" property="templateId" />',
								'<bean:write name="viewReportIn" property="versionId" />',
						  		'<bean:write name='viewReportIn' property='year' />',
						  		'<bean:write name='viewReportIn' property='term' />',
						  		'<bean:write name='viewReportIn' property='day' />',
						  		'<bean:write name='viewReportIn' property='actuFreqID' />',
						  		'<bean:write name='viewReportIn' property='curId' />',
						  		'<bean:write name='viewReportIn' property='orgId' />')">
							<bean:write name="viewReportIn" property="repInFo" /></a>
						</td>
						<logic:equal value="1" name="add_old_collect">
							<td align="center">
								<logic:equal name="viewReportIn" property="isCollected" value="0">
									<input type="button" class="input-button"
										onClick="_collect(
										'<bean:write name="viewReportIn" property="templateId" />',
										'<bean:write name="viewReportIn" property="versionId" />',
								  		'<bean:write name='viewReportIn' property='year' />',
								  		'<bean:write name='viewReportIn' property='term' />',
								  		'<bean:write name='viewReportIn' property='day' />',
								  		'<bean:write name='viewReportIn' property='actuFreqID' />',
								  		'<bean:write name='viewReportIn' property='curId' />',
								  		'<bean:write name='viewReportIn' property='orgId' />',
							  		    '<bean:write name='viewReportIn' property='donum' />')"
										value="�� ��">
								</logic:equal>
								<!-- �Ѿ��ϱ���  -->
								<logic:equal name="viewReportIn" property="isCollected" value="1">
									<input type="button" disabled class="input-button" value="�� ��">
								</logic:equal>
								<%--
								<!-- ���ܹ���  -->
								<logic:equal name="viewReportIn" property="isCollected" value="1">
									<!-- ���ܹ���  -->
									<input type="button" class="input-button"
										onClick="translate('<bean:write name="viewReportIn" property="repInId" />', 
										'<bean:write name='viewReportIn' property='year' />',
										'<bean:write name='viewReportIn' property='term' />',
										'<bean:write name='viewReportIn' property='day' />')"
										value="ת�ϱ�����">
								</logic:equal>
								<!-- û�л��ܵ�  -->
								<logic:equal name="viewReportIn" property="isCollected" value="0">
									<input type="button" disabled class="input-button" value="ת�ϱ�����">
								</logic:equal>
								 --%>
							</td>
						</logic:equal>
					</tr>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="Records" scope="request">
				<tr bgcolor="#FFFFFF">
					<logic:equal value="1" name="add_old_collect">
						<td colspan="8">
							���޻�������
						</td>
					</logic:equal>
					<logic:equal value="0" name="add_old_collect">
						<td colspan="7">
							���޻�������
						</td>
					</logic:equal>
				</tr>
			</logic:notPresent>
		</table>			
		</logic:present>
		<logic:notPresent name="add_old_collect">
			<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				
				<th height="24" align="center" id="list" colspan="8">
					���������б�
				</th>
				
			</tr>
			<TR class="middle">
				<TD  align="center" width="3%">
				<!-- 	<logic:present name="Records" scope="request">
						<INPUT type="checkbox" name="data_collectAll" id="data_collectAll" value="" onClick="_selectAll()" />
					</logic:present>
				-->
				</TD>
				<TD  align="center" width="5%">
					<strong>���</strong>
				</TD>
				<TD  align="center" width="35%">
					<strong>��������</strong>
				</TD>
				<!-- 
				<TD  align="center">
					<strong>�汾��</strong>
				</TD>
				 -->
				<TD  align="center" width="10%">
					<strong>����</strong>
				</TD>
				<TD  align="center" width="8%">
					<strong>Ƶ��</strong>
				</TD>
				<TD  align="center" width="7%">
					<strong>����</strong>
				</TD>
				<TD  align="center" width="12%">
					<strong>�������<br/>(ʵ��/Ӧ��)</strong>
				</TD>
				<TD align="center" width="10%">
					<strong>����</strong>
				</TD>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="viewReportIn" name="Records" indexId="index">
					<%
					rNum++;
					%>
					<tr bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
						<td align="center">
							<INPUT type="checkbox" name="data_collect"		
						value="<bean:write name="viewReportIn" property="templateId"/>,<bean:write name="viewReportIn" property="versionId"/>,<bean:write name='viewReportIn' property='orgId' />,<bean:write name='viewReportIn' property='curId' />,<bean:write name='viewReportIn' property='actuFreqID' />,<bean:write name='viewReportIn' property='donum' />,<bean:write name='viewReportIn' property='year' />,<bean:write name='viewReportIn' property='term' />,<bean:write name='viewReportIn' property='day' />" />
						</td>
						<td align="center">
							<bean:write name="viewReportIn" property="templateId" />
						</td>
						<td align="center">
							<%--
							<!-- ����ǻ��ܹ��ı���,����ʾ����   -->
							<logic:equal name="viewReportIn" property="isCollected" value="1">
								<a href="javascript:viewExcel('<bean:write name="viewReportIn" property="repInId" />')"><bean:write
										name="viewReportIn" property="repName" />
								</a>
							</logic:equal>
							<!-- û�л��ܵ�  -->
							<logic:equal name="viewReportIn" property="isCollected" value="0"></logic:equal>
								<bean:write name="viewReportIn" property="repName" /> --%>
								<a href="javascript:viewPdf('<bean:write name='viewReportIn' property='repInId'/>',
									 								'<bean:write name='viewReportIn' property='childRepId'/>',
									 								'<bean:write name='viewReportIn' property='versionId'/>',
									 								'<bean:write name='viewReportIn' property='curId'/>',
									 								'<bean:write name='viewReportIn' property='actuFreqID'/>',
									 								'<bean:write name='viewReportIn' property='year'/>',
									 								'<bean:write name='viewReportIn' property='term'/>',
									 								'<bean:write name='viewReportIn' property='day'/>',
									 								'<bean:write name='viewReportIn' property='orgId'/>')">
											<bean:write name="viewReportIn" property="repName" /></a>
							
						</td>
						<td align="center">
							<bean:write name="viewReportIn" property="orgName" />
						</td>
						<!-- 
						<td align="center">
							<bean:write name="viewReportIn" property="versionId" />
						</td>
						 -->
						<td align="center">
							<bean:write name="viewReportIn" property="actuFreqName" />
						</td>
						<td align="center">
							<bean:write name="viewReportIn" property="currName" />
						</td>
						<td align="center">
							<a href="javascript:viewCollectInfo(
								'<bean:write name="viewReportIn" property="templateId" />',
								'<bean:write name="viewReportIn" property="versionId" />',
						  		'<bean:write name='viewReportIn' property='year' />',
						  		'<bean:write name='viewReportIn' property='term' />',
						  		'<bean:write name='viewReportIn' property='day' />',
						  		'<bean:write name='viewReportIn' property='actuFreqID' />',
						  		'<bean:write name='viewReportIn' property='curId' />',
						  		'<bean:write name='viewReportIn' property='orgId' />')">
							<bean:write name="viewReportIn" property="repInFo" /></a>
						</td>
						<td align="center">
							<logic:equal name="viewReportIn" property="isCollected" value="0">
								<input type="button" class="input-button"
									onClick="_collect(
									'<bean:write name="viewReportIn" property="templateId" />',
									'<bean:write name="viewReportIn" property="versionId" />',
							  		'<bean:write name='viewReportIn' property='year' />',
							  		'<bean:write name='viewReportIn' property='term' />',
							  		'<bean:write name='viewReportIn' property='day' />',
							  		'<bean:write name='viewReportIn' property='actuFreqID' />',
							  		'<bean:write name='viewReportIn' property='curId' />',
							  		'<bean:write name='viewReportIn' property='orgId' />',
						  		    '<bean:write name='viewReportIn' property='donum' />')"
									value="�� ��">
							</logic:equal>
							<!-- �Ѿ��ϱ���  -->
							<logic:equal name="viewReportIn" property="isCollected" value="1">
								<input type="button" disabled class="input-button" value="�� ��">
							</logic:equal>
							<%--
							<!-- ���ܹ���  -->
							<logic:equal name="viewReportIn" property="isCollected" value="1">
								<!-- ���ܹ���  -->
								<input type="button" class="input-button"
									onClick="translate('<bean:write name="viewReportIn" property="repInId" />', 
									'<bean:write name='viewReportIn' property='year' />',
									'<bean:write name='viewReportIn' property='term' />',
									'<bean:write name='viewReportIn' property='day' />')"
									value="ת�ϱ�����">
							</logic:equal>
							<!-- û�л��ܵ�  -->
							<logic:equal name="viewReportIn" property="isCollected" value="0">
								<input type="button" disabled class="input-button" value="ת�ϱ�����">
							</logic:equal>
							 --%>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
			<logic:notPresent name="Records" scope="request">
				<tr bgcolor="#FFFFFF">
					<td colspan="8">
						���޻�������
					</td>
				</tr>
			</logic:notPresent>
		</table>
		</logic:notPresent>
		</form>
	</label>
	<logic:present name="viewReportIn">
		<script type="text/javascript">
		 page_donum=<bean:write name='viewReportIn' property='donum' />;
		</script>
	</logic:present>
</body>
</html:html>