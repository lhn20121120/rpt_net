<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>
<%@ page import="java.util.List"%>

<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepSearchPodedom = operator != null ? operator.getChildRepSearchPopedom() : "";
	/** ����ѡ�б�־ **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";	
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepSearchPodedom" name="utilSubOrgForm" value="<%=childRepSearchPodedom%>"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepSearchPodedom%>"/>
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>			
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>    
	      
		/**
		 * ���ύ��֤����֤�����ѯ�����Ƿ���Ч��
		 * 
		 * @author jcm
		 * @date 2008-01-15
		 */
		function _submit(form){
				if(form.date.value==""){
					alert("�����뱨��ʱ��!");
					form.date.focus();
					return false;
				}
		}
		
		/**
		 * ���Excel��������
		 * 
		 * @author jcm
		 * @date 2008-01-15
		 */
		function viewExcel(repInId){
			window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}
		/**
	     * ȫ����������
		 */
		function _allExport(){
			var objFrm=document.forms['frm'];
			
		  	var qry="childRepId=" + objFrm.elements['childRepId'].value + 
	 	  			"&repName=" + objFrm.elements['repName'].value + 
	  	  			"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 	
	  	  			"&orgId=" + objFrm.elements['orgId'].value +   	  		
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 
		  	  		"&curPage=" + curPage;
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&year=" + objFrm.elements['date'].value.split("-")[0];
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&term=" + objFrm.elements['date'].value.split("-")[1];
			  	
		  	if(objFrm.elements['checkFlag'].value != '-999')
		  		qry += "&checkFlag=" + objFrm.elements['checkFlag'].value;
		  		
			if(confirm("��ȷ��Ҫ����ȫ�������ļ���?\n")==true){
				window.location="<%=request.getContextPath()%>/servlets/createSubOrgReportServlet?"+qry;
			}
		}	
			
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
					else{						
						eval("formObj.elements['chk" + i + "'].checked=true");
					}
				}catch(e){}
			}
		}
		  	 		  	 
		/**
		 * ��ѡ�������� 
		 * @return void
		 */
		function _selExport(){
			var objForm=document.getElementById("frmChk");
			var count=objForm.elements['countChk'].value;
			var repInIds="";
			var obj=null;
			for(var i=1;i<=count;i++){
				try{
					obj=eval("objForm.elements['chk" + i + "']");
					if(obj.checked==true){					
						repInIds+=(repInIds==""?"":",") + obj.value;
					}
				}catch(e){}
			}	
			if(repInIds==""){
				alert("��ѡ��Ҫ�����ı���!\n");
				return;
			}else{				
				window.location="<%=request.getContextPath()%>/servlets/createSubOrgReportServlet?repInIds=" + repInIds;
			}
		}
		/**
	     * ȫ�������������
		 */
		function _Export(){
			var objFrm=document.forms['frm'];
		  	var qry="childRepId=" + objFrm.elements['childRepId'].value + 
	 	  			"&repName=" + objFrm.elements['repName'].value + 
	  	  			"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 	
	  	  			"&orgId=" + objFrm.elements['orgId'].value +   	  		
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 
		  	  		"&curPage=" + curPage;
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&year=" + objFrm.elements['date'].value.split("-")[0];
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&term=" + objFrm.elements['date'].value.split("-")[1];
			  	
		  	if(objFrm.elements['checkFlag'].value != '-999')
		  		qry += "&checkFlag=" + objFrm.elements['checkFlag'].value;
		  		
			window.location="<%=request.getContextPath()%>/reportSearch/viewReportStatAction.do?method=export&"+qry;
			
		}	
			
		function viewPdf(repInId,templateId,versionId,curId,repFreqId,year,term,orgId){
				 window.open("<%=request.getContextPath()%>/editAFReport.do?statusFlg=1&repInId=" + repInId+"&templateId="+templateId+"&versionId="+versionId+"&curId="+curId+"&repFreqId="+repFreqId+"&year="+year+"&term="+term+"&orgId="+orgId); 
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
		// add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
	</SCRIPT>
</head>
<body>
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
				��ǰλ�� &gt;&gt; �����ѯ &gt;&gt; �����ѯ
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/reportSearch/viewReportStatAction" method="post" styleId="frm" onsubmit="return _submit(this)">
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
								<td height="25" align="left">
									ģ�����ͣ�
									<html:select property="frOrFzType" size="1">
										<html:option value="-999">--ȫ��--</html:option>
										<html:option value="1">����ģ��</html:option>
										<html:option value="2">��֧ģ��</html:option>
									</html:select>																													
								</td>
								<td hight="25" align="left">
									����Ƶ�ȣ�
									<html:select property="repFreqId">
										<html:option value="-999">--ȫ��--</html:option>
										<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
									</html:select>
								</td>
							</tr>
							<tr><td height="2"></td></tr>											
							<tr>
				<%--			<td height="25">&nbsp;
									����ʱ�䣺
									<html:text property="year" maxlength="4" size="4" styleClass="input-text" />
									��
									<html:text property="term" maxlength="2" size="2" styleClass="input-text" />
									��
								</td>	--%>
								<td height="25">				
							&nbsp;&nbsp;����ʱ�䣺
								<html:text property="date" styleClass="input-text" size="10" styleId="date1"
										readonly="true" onclick="return showCalendar('date1', 'y-mm');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date1', 'y-mm');">
								</td>
								
								<td height="25" align="left">
									���������
									<html:text property="orgName" readonly="true" size="25" style="width:150px;cursor:hand" onclick="return showTree1()" styleClass="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("tree1", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>
								</td>
								<td height="25" align="left">
									����״̬��
									<html:select property="checkFlag" size="1">
										<html:option value="-999">--ȫ��--</html:option>
										<!-- ��ʱ��-10 -->
										<html:option value="-10">δ����</html:option>
										<html:option value="0">δ���</html:option>
										<html:option value="1">���ͨ��</html:option>
										<html:option value="-1">���δͨ��</html:option>
									</html:select>			
								</td>
								<td align="center"><html:submit styleClass="input-button" value=" �� ѯ " />
								<!--  <input type="button" value="�������" class="input-button" onclick="_Export()">	 --></td>
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
	<br/>
	<%-- 
	<logic:present name="Records" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<tr>
				<td>
					<input type="button" value="��ѡ����" class="input-button" onclick="_selExport()">
					&nbsp;
					<input type="button" value="ȫ������" class="input-button" onclick="_allExport()">					
				</td>
			</tr>
		</table>
	</logic:present>
	<logic:notPresent name="Records" scope="request">
		<br/>
	</logic:notPresent>
	 --%>
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<html:form action="/reportSearch/searchReport" method="post" styleId="frmChk">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">

							<th align="center" valign="middle">
								������
							</th>
							<th  width="25%"  align="center" valign="middle">
								��������
							</th>
							<th  align="center" valign="middle">
								�汾��
							</th>
							<th align="center" valign="middle">
								����ھ�
							</th>
							<th align="center" valign="middle">
								����
							</th>
							<th align="center" valign="middle">
								Ƶ��
							</th>
							<th align="center" valign="middle">
								����ʱ��
							</th>
							<th align="center" valign="middle">
								���ͻ���
							</th>
							<Th align="center" valign="middle">
								״̬
							</Th>
						</TR>
						<%int i = 0;%>
						<logic:present name="Records" scope="request">
							<logic:iterate id="viewReportInInfo" name="Records">
								
								<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">

									<TD align="center">
										<bean:write name="viewReportInInfo" property="childRepId"/>
									</TD>
									<TD align="center">
									<logic:notEmpty name='viewReportInInfo' property='repInId'>	
										<a href="javascript:viewPdf('<bean:write name='viewReportInInfo' property='repInId'/>',
											'<bean:write name='viewReportInInfo' property='childRepId'/>',
											'<bean:write name='viewReportInInfo' property='versionId'/>',
											'<bean:write name='viewReportInInfo' property='curId'/>',
											'<bean:write name='viewReportInInfo' property='actuFreqID'/>',
											'<bean:write name='viewReportInInfo' property='year'/>',
											'<bean:write name='viewReportInInfo' property='term'/>',
											'<bean:write name='viewReportInInfo' property='orgId'/>')">
											<bean:write name="viewReportInInfo" property="repName" />
										</a>
									</logic:notEmpty>
									<logic:empty name='viewReportInInfo' property='repInId'>	
										<bean:write name="viewReportInInfo" property="repName" />
									</logic:empty>	
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="versionId" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="dataRgTypeName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="year" />-<bean:write name="viewReportInInfo" property="term" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportInInfo" property="orgName" />
									</TD>
									<TD align="center">
									<logic:empty name="viewReportInInfo" property="checkFlag">δ����</logic:empty>
									<logic:notEmpty name="viewReportInInfo" property="checkFlag">
										<logic:equal name="viewReportInInfo" property="checkFlag" value="0"><font face="����" color="#000000" size="2">δ���</font></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="1"><font size="2" color="#33CC33">��ǩͨ��</font></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="-1"><font face="����" color="#FF0000" size="2">��ǩδͨ��</font></logic:equal>
										<logic:equal name="viewReportInInfo" property="checkFlag" value="-5"><font face="����" color="#FF0000" size="2">����δͨ��</font></logic:equal>
										<logic:greaterThan  name="viewReportInInfo" property="checkFlag" value="1">δ����</logic:greaterThan>
									</logic:notEmpty>							
									</TD>
								</TR>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr align="left">
								<td bgcolor="#ffffff" colspan="10">
									���޷��������ļ�¼
								</td>
							</tr>
						</logic:notPresent>
					</table>
				</td>
			</tr>
			<input type="hidden" name="countChk" value="<%=i%>">
		</html:form>
	</table>
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../reportSearch/viewReportStatAction.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
