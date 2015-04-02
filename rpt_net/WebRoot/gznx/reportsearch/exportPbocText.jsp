<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.other.Aditing"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%
	Operator operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPodedom = operator != null ? operator
			.getChildRepSearchPopedom() : "";
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
		reportFlg = (String) session
				.getAttribute(Config.REPORT_SESSION_FLG);
	}
	String orgName = "";
	if(request.getAttribute("orgName")!=null && !request.getAttribute("orgName").toString().equals(""))
		orgName = request.getAttribute("orgName").toString();
	else
		orgName = operator != null ? operator.getOrgName() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	Map selectCheckOrgIds=(HashMap)request.getAttribute("selectCheckOrgIds");
	int systemSchemaFlag =0;
	if(request.getAttribute("system_schema_flag")!=null){
		systemSchemaFlag=(Integer)request.getAttribute("system_schema_flag");
	}
	String showmenu = (String)request.getAttribute("showmenu");
	if(showmenu==null)
		showmenu = "1";
%>
<jsp:useBean id="utilSubOrgForm" scope="page"
	class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepSearchPodedom" name="utilSubOrgForm"
	value="<%=childRepSearchPodedom%>" />
<jsp:useBean id="configBean" scope="page"
	class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page"
	class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="FormBean" scope="page"
	class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean"
	value="<%=reportFlg%>" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepSearchPodedom%>"/>

	
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css"
		type="text/css" rel="stylesheet">
	<script language="javascript"
		src="<%=request.getContextPath()%>/js/func.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
	<script language="javascript"
		src="<%=request.getContextPath()%>/js/func.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<logic:present name="Message" scope="request">
		<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
	</logic:present>
	<script language="javascript">	
		var scriptReportFlg=<%=reportFlg%>;
		var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	/**
     * ȫѡ����
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
		 * ��ѡ�������� 
		 * @return void
		 */
		function _selExport(){
			var workTaskTemp = document.getElementById("workTaskTemp_").value;
			var workTaskTerm = document.getElementById("workTaskTerm_").value;
			var workTaskOrgId = document.getElementById("workTaskOrgId_").value;
			var urlParam = document.getElementById("urlParam_").value;
			var parOrgId = document.getElementById("parOrgId").value;
			//alert("urlParam="+urlParam);
			//alert("��ѡ�������� ");
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;
			var systemSchemaFlag="<%=systemSchemaFlag%>";
			var repForm=document.getElementById("repForm");
			//�°��־λ
			var versionFlg = 0;
			//if(document.getElementById("versionFlg").checked==true){
			//	versionFlg = 1;
			//}
			<%
			if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
					
		    %>
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){					
						repInIds+=(repInIds==""?"":",")+obj.value;
						
					}
				}catch(e){}
			}	
		//	alert("repInIds��"+repInIds);
			if(repInIds==""){
				alert("��ѡ��Ҫ�����ı���!\n");
				return;
			}
			document.getElementById("repInIds_r").value=repInIds;
					document.getElementById("orgId_r").value=objForm.orgId.value;
					document.getElementById("date_r").value=objForm.date.value;
					document.getElementById("repFreqId_r").value=objForm.repFreqId.value;
					document.getElementById("supplementFlag_r").value=objForm.supplementFlag.value;
					document.getElementById("versionFlg_r").value=versionFlg;
					document.getElementById("workTaskTemp_r").value=workTaskTemp;
					document.getElementById("workTaskTerm_r").value=workTaskTerm;
					document.getElementById("workTaskOrgId_r").value=workTaskOrgId;
					document.getElementById("urlParam_r").value=urlParam;
					document.getElementById("systemSchemaFlag_r").value=systemSchemaFlag;
			if(confirm("�Ƿ�¼��˵��?")){
					repForm.action="<%=request.getContextPath()%>/gznx/reportsearch/pbocexplane.jsp?styleFlg=new&parOrgId="+parOrgId;
					
						repForm.submit();
			}else{
					 repForm.action="<%=request.getContextPath()%>/exportPbocAFReport.do?parOrgId="+parOrgId;
					LockWindows.style.display="";
					hidd();
					repForm.submit();
			}
			<%
		    }else{
			%>
				alert("���޼�¼���޷�����");
			<%
				}
			%>
		}
			
		function _downLoadSingle(repInIds){
			var workTaskTemp = document.getElementById("workTaskTemp_").value;
			var workTaskTerm = document.getElementById("workTaskTerm_").value;
			var workTaskOrgId = document.getElementById("workTaskOrgId_").value;
			var urlParam = document.getElementById("urlParam_").value;
			var parOrgId = document.getElementById("parOrgId").value;
			var objForm=document.getElementById("frmChk");
			var systemSchemaFlag="<%=systemSchemaFlag%>";
			//�°��־λ
			var versionFlg = 0;
			//if(document.getElementById("versionFlg").checked==true){
			//	versionFlg = 1;
			//}
			var repInId=repInIds;
			<%
			if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
					
		    %>
		    
			if(confirm("�Ƿ�¼��˵��?")){
					window.location="<%=request.getContextPath()%>/gznx/reportsearch/pbocexplane.jsp?repInIds=" +repInId
						+"&orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg+"&workTaskTemp="+workTaskTemp
						+"&workTaskTerm="+workTaskTerm
						+"&workTaskOrgId="+workTaskOrgId
						+"&urlParam="+urlParam
						+"&parOrgId="+parOrgId
						+"&systemSchemaFlag="+systemSchemaFlag;
						
			}else{
					window.location="<%=request.getContextPath()%>/exportPbocAFReport.do?repInIds=" +repInId
						+"&orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg+"&workTaskTemp="+workTaskTemp
						+"&workTaskTerm="+workTaskTerm
						+"&workTaskOrgId="+workTaskOrgId
						+"&urlParam="+urlParam
						+"&parOrgId="+parOrgId
						+"&systemSchemaFlag="+systemSchemaFlag;				
			}
			<%
		    }else{
			%>
				alert("���޼�¼���޷�����");
			<%
				}
			%>
		}
		
		
		function _downLoadAll(){
			var workTaskTemp = document.getElementById("workTaskTemp_").value;
			var workTaskTerm = document.getElementById("workTaskTerm_").value;
			var workTaskOrgId = document.getElementById("workTaskOrgId_").value;
			var urlParam = document.getElementById("urlParam_").value;
			var objForm=document.getElementById("frmChk");
			var parOrgId = document.getElementById("parOrgId").value;
			var systemSchemaFlag="<%=systemSchemaFlag%>";
			//�°��־λ
			<%
				if(request.getAttribute("Records")!=null &&((List)request.getAttribute("Records")).size()>0){
						
			%>
			var versionFlg = 0;
			if(confirm("ȫ������ʱ��ȴ���ȷ��Ҫȫ��������")){
				if(confirm("�Ƿ�¼��˵��?")){
					window.location="<%=request.getContextPath()%>/gznx/reportsearch/pbocexplane.jsp?orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg+"&workTaskTemp="+workTaskTemp
						+"&workTaskTerm="+workTaskTerm
						+"&workTaskOrgId="+workTaskOrgId
						+"&urlParam="+urlParam
						+"&parOrgId="+parOrgId
						+"&systemSchemaFlag="+systemSchemaFlag;
				}else{
					window.location="<%=request.getContextPath()%>/exportPbocAFReport.do?orgId="+objForm.orgId.value
						+"&date="+objForm.date.value
						+"&repFreqId="+objForm.repFreqId.value
						+"&supplementFlag="+objForm.supplementFlag.value
						+"&versionFlg="+versionFlg+"&workTaskTemp="+workTaskTemp
						+"&workTaskTerm="+workTaskTerm
						+"&workTaskOrgId="+workTaskOrgId
						+"&urlParam="+urlParam
						+"&parOrgId="+parOrgId
						+"&systemSchemaFlag="+systemSchemaFlag;
					LockWindows.style.display="";
					hidd();
				}
			}else{
				disp();
			}
			<%
			    }else{
				%>
					alert("���޼�¼���޷�����");
				<%
					}
				%>
		}
		function hidd(){

			checkTab.style.display="none";
		
		}
		
		function disp(){
			checkTab.style.display="";

		}



		
		function _returnOld(){
			//alert("juban");
			var systemSchemaFlag="<%=systemSchemaFlag%>";
			var workTaskTemp = document.getElementById("workTaskTemp_").value;
			var workTaskTerm = document.getElementById("workTaskTerm_").value;
			var workTaskOrgId = document.getElementById("workTaskOrgId_").value;
			var parOrgId = document.getElementById("parOrgId").value;
			var repForm=document.getElementById("repForm");
			//alert(parOrgId);
			var urlParam = document.getElementById("urlParam_").value;
			
					document.getElementById("workTaskTerm_r").value=workTaskTerm;
					document.getElementById("workTaskOrgId_r").value=workTaskOrgId;
					document.getElementById("urlParam_r").value=urlParam;
					document.getElementById("systemSchemaFlag_r").value=systemSchemaFlag;
					
					repForm.action="<%=request.getContextPath()%>/exportRhAFReport.do?parOrgId="+parOrgId+"&showmenu=<%=showmenu%>";
					
						repForm.submit();
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
		var objForm=document.getElementById("frmChk");
		objForm.orgId.value = id;
		//document.getElementById('frmChk').orgId.value = id;
		//alert(document.getElementById('frmChk'));
		//document.getElementById('orgId').value = id;
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
		// add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
		// add by ������  ����ϵͳ����ģʽ�������Ƿ����ز�ѯ����
		function isHiddenQueryTable(){
			
			var systemSchemaFlag="<%=showmenu%>";
			
			if(systemSchemaFlag=="0"){
				document.getElementById("queryTable").style.display="none";
				document.getElementById("listOrder").style.display="none";
			}
			else{
				document.getElementById("queryTable").style.display="block";
				document.getElementById("listOrder").style.display="block";
			}
		}
		function _dataImport(){
			window.location='<%=request.getContextPath()%>/gznx/reportsearch/pboc_data_import.jsp';
		}
	</script>
</head>
<body style="TEXT-ALIGN: center"  onload="isHiddenQueryTable()">
<form method="post" id="repForm">
		<input type="hidden" name="repInIds" id="repInIds_r"/>
		<input type="hidden" name="orgId" id="orgId_r"/>
		<input type="hidden" name="date" id="date_r"/>
		<input type="hidden" name="repFreqId" id="repFreqId_r"/>
		<input type="hidden" name="supplementFlag" id="supplementFlag_r"/>
		<input type="hidden" name="versionFlg" id="versionFlg_r"/>
		<input type="hidden" name="workTaskTemp" id="workTaskTemp_r"/>
		<input type="hidden" name="workTaskTerm" id="workTaskTerm_r"/>
		<input type="hidden" name="workTaskOrgId" id="workTaskOrgId_r"/>
		<input type="hidden" name="urlParam" id="urlParam_r"/>
		<input type="hidden" name="systemSchemaFlag" id="systemSchemaFlag_r"/>
	</form>
	<div id="LockWindows" style="display:none" class="black_overlay">
	<br><br><br><font color="red">����ִ���У����Ժ󡭡�</font><br><br>
	<img src="../../image/loading.gif">
	</div>

	<table border="0" width="98%" align="center" id="listOrder">
		<tr>
			<td height="3"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� &gt;&gt; ������ &gt;&gt; ������ 
			</td>
		</tr>
	</table>
	<html:form action="/exportRhAFReport" method="POST" styleId="frmChk">
	<html:hidden property="styleFlg" value="new"/>	
	<html:hidden property="orgId"/>
	<input type="hidden" id="workTaskTemp_" name="workTaskTemp" value="${ workTaskTemp}"/>
	<input type="hidden" id="workTaskTerm_" name="workTaskTerm" value="${ workTaskTerm}"/>
	<input type="hidden" id="workTaskOrgId_" name="workTaskOrgId" value="${ workTaskOrgId}"/>
	<input type="hidden" id="urlParam_" name="urlParam" value="${ urlParam}"/>
	<input type="hidden" id="parOrgId" name="parOrgId" value="${ parOrgId}"/>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center" id="queryTable">
		<tr><td>
		<fieldset id="fieldset">
		<table cellSpacing="0" cellPadding="4" width="100%" border="0"
			align="center" id="checkTab">
			<tr><td height="5"></td></tr>
			<tr>
				<td width="30%">
					&nbsp;����ʱ�䣺
					<html:text property="date" readonly="true" size="10"
						styleId="date1" style="text" onclick="return showCalendar('date1', 'y-mm-dd');"/>
					<img src="../../image/calendar.gif" border="0"
						onclick="return showCalendar('date1', 'y-mm-dd');">
				</td>
				<td width="25%">
					�����ţ�
					<html:text property="templateId" styleId="templateId" size="10" />
				</td>
				<td width="35%">
					�������ƣ�
					<html:text property="repName" styleId="repName" size="20" />
				</td>
				<td width="10%">
				</td>
			</tr>
			<tr>
				<td height="2"></td>
			</tr>
			<tr>
				<td height="25" align="left">
					&nbsp;���ͻ�����
					<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" value="<%=orgName %>" onclick="return showTree1()" style="input-text" ></html:text>
					<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
					<script type="text/javascript">
									<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
								    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
							      	tree1.init();
					</script>
				 	</div>
				</td>
				<td height="25" align="left">
					����Ƶ�ȣ�
					<html:select property="repFreqId" styleId="repFreqId">
						<html:option value="1">��</html:option>
						<html:option value="2">��</html:option>
						<html:option value="4">��</html:option>
						<html:option value="6">��</html:option>
						<html:option value="91">�����ת���£�</html:option>
						<html:option value="92">�����ת������</html:option>
						<html:option value="93">�����ת���꣩</html:option>
					</html:select>
				</td>
				<td height="25" align="left">
					�������Σ�
					<html:select property="supplementFlag" styleId="supplementFlag">
						<html:option value="-999"  >ȫ��</html:option>
						<html:option value="0">������</html:option>
						<html:option value="1">��һ��</html:option>
						<html:option value="2">�ڶ���</html:option>
					</html:select>
				</td>
				<td align="left">
					<html:submit styleClass="input-button" value=" �� ѯ " />
					
				</td>
				
			</tr>
		</table>
		</fieldset>
		</td></tr>
		</table>
		<br/>
		<table cellSpacing="0" cellPadding="4" width="98%" border="0"
			align="center">
			<tr>
				<td>
					<input type="button" value="��������" class="input-button"
						onclick="_selExport()">
					&nbsp;&nbsp;&nbsp;
					<%if("1".equals(showmenu)){
						%>
						<input type="button" value="ȫ������" class="input-button"
						onclick="_downLoadAll()">
					&nbsp;&nbsp;&nbsp;
						<%
					} %>
					<input type="button" value="�ϱ��ļ�����" class="input-button" onclick="_dataImport()"/>
					<!-- <input type="checkbox" id="versionFlg"><font color="red">�ɰ��ʽ</font> -->
				</td>
				<td align="center">
					<input type="button" value="��    ��" class="input-button" onclick="_returnOld()">
				</td>
			</tr>
		
		</table>

		<TABLE cellSpacing="0" width="98%" border="0" align="center"
			cellpadding="4">
			<TR>
				<TD>
					<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"
						class="tbcolor">
						<tr class="titletab">
							<th colspan="10" align="center" id="list">
								<strong>�����б�</strong>
							</th>
						</tr>
						<TR class="middle">
							<td width="5%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</td>
							<TD class="tableHeader" align="left">
								������
							</TD>
							<TD class="tableHeader" align="left">
								��������
							</TD>
							<TD class="tableHeader" align="left">
								���ͻ���
							</TD>
						</TR>
						<%
							int i = 0;
						%>
						<logic:present name="Records" scope="request">
							<logic:iterate id="item" name="Records">
								<TR bgcolor="#FFFFFF"   onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
								
									<TD align="center">
										<%
											i++;
										%>
										<input type="checkbox" name="chk<%=i%>"
											value='<bean:write name="item" property="orgId"/>:<bean:write name="item" property="repInId"/>:<bean:write name="item" property="actuFreqID"/>:<bean:write name="item" property="templateId"/>:<bean:write name='item' property='versionId'/>:<bean:write name='item' property='batchId'/>'>
									</TD>
									<td align="center" width="8%" >
										<bean:write name="item" property="templateId" />
									</td>
									<td align="left">
										&nbsp;(<bean:write name="item" property="actuFreqName" />)&nbsp;<bean:write name="item" property="repName" />
									</td>
									<td align="left">
										<bean:write name="item" property="orgName" />
									</td>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="Records" scope="request">
							<tr bgcolor="#FFFFFF">
								<td colspan="10">
									���޼�¼
								</td>
							</tr>
						</logic:notPresent>
					</TABLE>
					
					<table cellSpacing="1" cellPadding="0" width="100%" border="0">
						<tr>
							<TD colspan="8" bgcolor="#FFFFFF">
							<jsp:include page="../../apartpage.jsp" flush="true"><jsp:param name="url" value="../../exportRhAFReport.do" />
								</jsp:include>
							</TD>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</html:form>
</body>
</html:html>
