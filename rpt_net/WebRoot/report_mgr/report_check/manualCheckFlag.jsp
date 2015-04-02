<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.other.Expression"%>

<%

	String nodeFlag=null;
	if(request.getAttribute("nodeFlag")!=null){
		nodeFlag = (String)request.getAttribute("nodeFlag");
	}
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	Integer systemSchemaFlag=0;
	if(request.getAttribute("system_schema_flag")!=null){
		systemSchemaFlag=(Integer)request.getAttribute("system_schema_flag");
		
	}
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepCheckPodedom = operator != null ? operator.getChildRepCheckPodedom()
				+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='" + reportFlg +"')" : "";

%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepCheckPodedom" name="utilSubOrgForm" value="<%=childRepCheckPodedom%>"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepCheckPodedom%>"/>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="../../js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<SCRIPT language="javascript">
		$(function(){
			<logic:present name="schemaRepIds" scope="request">
	    		var schemaRepIds="<bean:write name='schemaRepIds' />";
	    		
	    		$.post(
	    			'/rpt_other/pendingTaskAction!saveRepInfo.action',
	    			{"repIds":schemaRepIds},
	    			function(rv){}
	    		);
	   		 </logic:present>
		});
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    /**
	     * ���ͨ����ʶ
	     */	    
		var FLAG_OK=1;
	    /**
	     * ��˲�ͨ����ʶ
	     */ 
		var FLAG_NO=-1;
	    /**
	     * ����У���ʶ
	     */
		var FLAG_BL="<%=Expression.FLAG_BL%>";
	    /**
	     * ����У���ʶ
	     */
		var FLAG_BJ="<%=Expression.FLAG_BJ%>";
	    /**
	     * ���ŷָ���
	     */
		var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	     
	    /**
	     * �鿴����У����ϸ
	     */
		function _view_bnjy(repInId){
			window.open("<%=request.getContextPath()%>/report/report_check/viewJYDetail.do?" + 
				"flag=" + FLAG_BL + "&repInId=" + repInId);
		}
		     
	    /**
	     * �鿴����У����ϸ
	     */ 
		function _view_bjjy(repInId){
			window.open("<%=request.getContextPath()%>/report/report_check/viewJYDetail.do?" + 
				"flag=" + FLAG_BJ + "&repInId=" + repInId);
		}
    
	    /**
	     * �쳣�仯��ϸ
	     */
		function _view_ycbh(repInId){
			window.open("<%=request.getContextPath()%>/report/report_check/viewYCBHDetail.do?" + 
				"repInId=" + repInId);
		}
	     
	    /**
	  	 * �ύ��ѯ��֤
	  	 */
		function _submit(form){
				if(form.date.value==""){
					alert("�����뱨��ʱ��!");
					form.date.focus();
					return false;
				}
		}
		
		/**
		 * ������� 
	  	 * 
	  	 * @param flag ��flag=1ʱ���������ͨ������flag=-1ʱ��������˲�ͨ��
	  	 * @return void
		 */
		function _batchCheck(flag){
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
				alert("��ѡ��Ҫ��˵ı���!\n");
				return;
			}else{
				var objFrm=document.forms['frm'];
				var allFlags="";
		  	  		
				var qry="repInIds=" + repInIds + 
						"&flag=" + flag +
						"&childRepId=" + objFrm.elements['childRepId'].value + 
						"&repName=" + objFrm.elements['repName'].value + 
						"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 
						"&orgId=" + objFrm.elements['orgId'].value + 
						"&repFreqId=" + objFrm.elements['repFreqId'].value +
						"&workTaskTerm=" + "${workTaskTerm}"+
						"&workTaskOrgId=" + "${workTaskOrgId}"+
						"&workTaskTemp=" + "${workTaskTemp}"+ 
						"&curPage=" + curPage;
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&year=" + objFrm.elements['date'].value.split("-")[0];
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&term=" + objFrm.elements['date'].value.split("-")[1];

				if(flag==FLAG_OK){
					if(confirm("��ȷ��Ҫ����ǰ�����ͨ��������?\n")==true){
						location.href="<%=request.getContextPath()%>/dateQuary/saveCheckRep.do?" + qry;			  	  				
					}
				}else{
					if(confirm("��ȷ��Ҫ����ǰ����˲�ͨ��������?\n")==true){
						location.href="<%=request.getContextPath()%>/report_mgr/report_check/report_again_add.jsp?" + qry+"&updateFlag=1";			  	  				
					}
				}
			}
		}
		
		/**
		 * ȫ�����ͨ��
		 *
		 */		  	  
		function _batchAllCheck(flag){ 	  		
			var objFrm=document.forms['frm'];
		  	  		
			var qry="flag=" + flag +
					"&childRepId=" + objFrm.elements['childRepId'].value + 
					"&repName=" + objFrm.elements['repName'].value + 
					"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 
					"&orgId=" + objFrm.elements['orgId'].value + 
					"&repFreqId=" + objFrm.elements['repFreqId'].value + 
					"&workTaskTerm=" + "${workTaskTerm}"+
					"&workTaskOrgId=" + "${workTaskOrgId}"+
					"&workTaskTemp=" + "${workTaskTemp}"+ 
					"&curPage=" + curPage;
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&year=" + objFrm.elements['date'].value.split("-")[0];
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&term=" + objFrm.elements['date'].value.split("-")[1];

			if(flag==FLAG_OK){
				if(confirm("��ȷ��Ҫ���ͨ�����б�����?\n")==true){
					location.href="<%=request.getContextPath()%>/dateQuary/saveAllCheckRep.do?" + qry;	  	  				
				}
			}else{
				if(confirm("��ȷ��Ҫ��˲�ͨ�����б�����?\n")==true){
					location.href="<%=request.getContextPath()%>/dateQuary/saveAllCheckRep.do?" + qry;			
				}
			}
		} 	  
		
		/**
		 * ��˲���
		 *
		 * @param flag ��˱�ʶ
		 * @param repInId ʵ���ӱ�ID
		 */
		function _check(flag,repInId){
			var objFrm=document.forms['frm'];
		  	  	
			var qry="repInIds=" + repInId + 
					"&flag=" + flag + 
					"&childRepId=" + objFrm.elements['childRepId'].value + 
					"&repName=" + objFrm.elements['repName'].value + 
		  	  		"&frOrFzType=" + objFrm.elements['frOrFzType'].value + 
		  	  		"&orgId=" + objFrm.elements['orgId'].value + 
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value +
					"&curPage=" + curPage;
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&year=" + objFrm.elements['date'].value.split("-")[0];
			  	if(objFrm.elements['date'].value != "")
			  		qry += "&term=" + objFrm.elements['date'].value.split("-")[1];

			if(flag==FLAG_OK){
				if(confirm("��ȷ����ǰ�����ͨ��������?\n")==true){
					window.location="<%=request.getContextPath()%>/dateQuary/saveCheckRep.do?" + qry;
				}
			}else{
				if(confirm("��ȷ����ǰ����˲�ͨ��������?\n")==true){
					window.location="<%=request.getContextPath()%>/report_mgr/report_check/report_again_add.jsp?" + qry;
				}
			}
		}
		
		/**
		 * ȫѡ����
		 *
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
		 * �鿴��������
		 */ 
	//	function viewPdf(repInId){
	//		window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
	//	}
		
		/**
		 * �鿴У�鲻ͨ������Ϣ
		 */
		function _view_XSYY(repInId){
			window.open("<%=request.getContextPath()%>/report/viewJYNotPassInfo.do?" + "repInId=" + repInId + "&tblOuterValidateFlag=" + FLAG_BJ);
		}
		
		function viewPdf(repInId,templateId,versionId,curId,repFreqId,year,term,orgId){
			 window.open("<%=request.getContextPath()%>/editAFReport.do?statusFlg=2&repInId=" + repInId+"&templateId="+templateId+"&versionId="+versionId+"&curId="+curId+"&repFreqId="+repFreqId+"&year="+year+"&term="+term+"&orgId="+orgId+"&type=search"); 
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
		// add by ������  ����ϵͳ����ģʽ�������Ƿ����ز�ѯ����
		function isHiddenQueryTable(){
			
			var sysSchemaFlag="<%=systemSchemaFlag%>";
		
			if(sysSchemaFlag=="1"){
				document.getElementById("queryTable").style.display="none";
				document.getElementById("listOrder").style.display="none";
			}
			else{
				document.getElementById("queryTable").style.display="block";
				
				document.getElementById("listOrder").style.display="block";
			}
		}
		
		/**
		 * У��ѡ�еı���
		 */
		function _validate_Select(){
			var count = document.getElementById("countChk").value;
			var cks = $("input[type=checkbox]:checked").length;
			if(cks==0){
				alert("��ѡ�񱨱�!");
				return;
			}
			var checkObj = null;
			var repInIds = "";
			for(var i=1;i<=count;i++){
				try{
					checkObj = eval("document.getElementById('chk" + i + "')");
					if(checkObj.checked == true){
						if(checkObj.value!=""){
							repInIds+=(repInIds==""?"":",") + checkObj.value;						
						}
					}
				}catch(e){}				
			}
			
			if(repInIds==""){
		  	  	alert("����δ������ܽ���У��!\n");
		  	  	return;
			}else{		  	  		
		  	  	if(confirm("��ȷ��Ҫ��������У����?\n")==true){
					try{
						var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReport.do?repInIds="+repInIds; 
						var param = "radom="+Math.random()+"&updateCheckFlag=0";
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
					}catch(e){
						alert('ϵͳæ�����Ժ�����...��VS');
						
						return;
					}  	  				
				}
			}
		}
		
		//У��Handler
		function validateReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				if(result == 'true'){
				  	alert('У��ͨ����');	
				  
				 	//window.location.reload();		     
				}else{
					 if(confirm('У��δͨ�����Ƿ���Ҫ�鿴У����Ϣ?')){
				        window.open("<%=request.getContextPath()%>/report/viewMoreDataJYInfo.do?" + "repInIds=" + result,'У����','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				       
				       
				      }
				}
				prodress1.style.display = "" ;
		   		prodress.style.display = "none" ;	
				selectReport();

		   		
			}catch(e){}
	    }

		//ʧ�ܴ���
	    function reportError(request)
	    {
	        alert('ϵͳæ�����Ժ�����...��');
	    }
		
	</SCRIPT>
</head>
<body onload="isHiddenQueryTable()">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="98%" align="center" id="listOrder">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� &gt;&gt; �������� &gt;&gt; �������
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>

	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center" id="queryTable">
		<html:form action="/dateQuary/manualCheckRep" method="post" styleId="frm" onsubmit="return _submit(this)">
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
									������ţ�
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
							</tr>
							<tr><td height="2"></td></tr>											
							<tr>
			<%--				<td height="25">&nbsp;
									����ʱ�䣺
									<html:text property="year" maxlength="4" size="4" styleClass="input-text" />
									��
									<html:text property="term" maxlength="2" size="2" styleClass="input-text" />
									��
								</td>  --%>
								<td height="25">				
							&nbsp;&nbsp;����ʱ�䣺
								<html:text property="date" styleClass="input-text" size="10" styleId="date1"
										readonly="true" onclick="return showCalendar('date1', 'y-mm');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date1', 'y-mm');">
								</td>
								<td height="25" align="left">
									����������
									<html:text property="orgName" readonly="true" size="25" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
									<%if(systemSchemaFlag==0){%>
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("tree1", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								      <%}%>
								 	</script>
								 	</div>
								</td>
								<td hight="25" align="left">
									����Ƶ�ȣ�
									<html:select property="repFreqId">
										<html:option value="-999">--ȫ��--</html:option>
										<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
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

	<logic:present name="form" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<html:form action="/dateQuary/saveAllCheckRep" method="post" styleId="frm1">
				<tr>
					<td>
						<%
						//�����ǰ����״̬�����ڴ�������������˲�ͨ���ͽ���ǰ��ť״̬��Ϊ������
						if(nodeFlag!=null && !nodeFlag.equals("2") && !nodeFlag.equals("5") ){ %>
						<input class="input-button" onclick="_validate_Select()" disabled="disabled" type="button" value="У��ѡ��">
						&nbsp;
						<input type="button" value="���ͨ��" disabled="disabled" class="input-button" onclick="_batchCheck(FLAG_OK)">
						&nbsp;
						<input type="button" value="��˲�ͨ��" disabled="disabled" class="input-button" onclick="_batchCheck(FLAG_NO)">
						<%}else{ %>
						<input class="input-button" onclick="_validate_Select()" type="button" value="У��ѡ��">
						&nbsp;
						<input type="button" value="���ͨ��" class="input-button" onclick="_batchCheck(FLAG_OK)">
						&nbsp;
						<input type="button" value="��˲�ͨ��" class="input-button" onclick="_batchCheck(FLAG_NO)">
						<%} %>
						<!--input type="button" value="ȫ���������ͨ��" class="input-button" title="���ͨ����������" onclick="return _batchAllCheck(FLAG_OK)"-->
					</td>
				</tr>				
			</html:form>
		</table>
	</logic:present>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<html:form action="/dateQuary/saveCheckRep" method="post" styleId="frmChk">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="4%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							<th width="23%" align="center" valign="middle">
								��������
							</th>
							<th width="11%" align="center" valign="middle">
								�����ھ�
							</th>
							<th width="5%" align="center" valign="middle">
								Ƶ��
							</th>
							<th width="7%" align="center" valign="middle">
								����
							</th>
							<th width="6%" align="center" valign="middle">
								����ʱ��
							</th>
							<th width="6%" align="center" valign="middle">
								����ʱ��
							</th>
							<th width="10%" align="center" valign="middle">
								���ͻ���
							</th>
							<Th width="8%" align="center" valign="middle">
								״̬
							</Th>
							<th width="10%" align="center" valign="middle">
								У����Ϣ
							</th>
							<!-- 
							 
							<th width="6%" align="center" valign="middle">
								���
							</th>-->
						</tr>
						<%
						int i = 0;
						%>
						<logic:present name="form" scope="request">
							<logic:iterate id="viewReportIn" name="form">
								<%
								i++;
								%>
								<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
									<td align="center">
									<input type="hidden" name="sync<%=i%>" id="sync<%=i%>" value="<bean:write name="viewReportIn" property="childRepId"/>_<bean:write name="viewReportIn" property="versionId"/>_<bean:write name='viewReportIn' property='dataRgId'/>_<bean:write name="viewReportIn" property="actuFreqID" />_<bean:write name="viewReportIn" property="curId" />_<bean:write name="viewReportIn" property="orgId" />"/>							
										<input type="checkbox" name="chk<%=i%>" value="<bean:write name="viewReportIn" property="repInId"/>">
									</td>
									<TD align="center">
										<a href="javascript:viewPdf('<bean:write name='viewReportIn' property='repInId'/>',
											'<bean:write name='viewReportIn' property='childRepId'/>',
											'<bean:write name='viewReportIn' property='versionId'/>',
											'<bean:write name='viewReportIn' property='curId'/>',
											'<bean:write name='viewReportIn' property='actuFreqID'/>',
											'<bean:write name='viewReportIn' property='year'/>',
											'<bean:write name='viewReportIn' property='term'/>',
											'<bean:write name='viewReportIn' property='orgId'/>')">
											<bean:write name="viewReportIn" property="repName" />
										</a>
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="dataRgTypeName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="reportDate" format="yyyy-MM-dd HH:mm:ss"/>
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="orgName" />
									</TD>
									<TD align="center">
										<logic:present name="viewReportIn" property="checkFlag">
												<logic:equal name="viewReportIn" property="checkFlag" value = "1">
													���ͨ��
												</logic:equal>
												<logic:equal name="viewReportIn" property="checkFlag" value = "0">
													δ���
												</logic:equal>
												<logic:equal name="viewReportIn" property="checkFlag" value = "-1">
													��˲�ͨ��
												</logic:equal>
												<logic:equal name="viewReportIn" property="checkFlag" value = "2">
													��У��		
												</logic:equal>
												<logic:equal name="viewReportIn" property="checkFlag" value = "3">
													δ����
												</logic:equal>
												<logic:equal name="viewReportIn" property="checkFlag" value = "4">
													���
												</logic:equal>
											</logic:present>
									</TD>
									<TD align="center">
										<img src="../../image/bnjy.gif" border="0" title="�鿴����У����ϸ" style="cursor:hand" onclick="_view_bnjy(<bean:write name='viewReportIn' property='repInId'/>)">
										<img src="../../image/bjjy.gif" border="0" title="�鿴����У����ϸ" style="cursor:hand" onclick="_view_bjjy(<bean:write name='viewReportIn' property='repInId'/>)">
										<%--<img src="../../image/ycbh.gif" border="0" title="�鿴�쳣�仯��ϸ" style="cursor:hand" onclick="_view_ycbh(<bean:write name='viewReportIn' property='repInId'/>)">
										<img src="../../image/excel.bmp" border="0" title="�鿴��������" style="cursor:hand" onclick="javascript:viewPdf(<bean:write name='viewReportIn' property='repInId'/>)">
									--%></TD>
									<%--
									<TD noWrap align="center" height="30">
										<logic:equal name="viewReportIn" property="checkFlag" value="5">
											<a href="javascript:_check(FLAG_OK,<bean:write name="viewReportIn" property="repInId"/>)"> ͨ�� </a>
											<a href="javascript:_check(FLAG_NO,<bean:write name="viewReportIn" property="repInId"/>)"> ��ͨ�� </a>
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="1">
											<a href="javascript:_check(FLAG_NO,<bean:write name="viewReportIn" property="repInId"/>)"> ��ͨ�� </a>
										</logic:equal>

										<logic:equal name="viewReportIn" property="checkFlag" value="-111">
											<a href="javascript:_check(FLAG_OK,<bean:write name="viewReportIn" property="repInId"/>)"> ͨ�� </a>&nbsp;
										</logic:equal>

									</TD>--%>
								</TR>
							</logic:iterate>
						</logic:present>
						<input type="hidden" name="countChk" value="<%=i%>">
						<logic:notPresent name="form" scope="request">
							<tr align="center">
								<td bgcolor="#ffffff" colspan="11" align="left">
									���޷��������ļ�¼
								</td>
							</tr>
						</logic:notPresent>
					</table>
				</td>
			</tr>

		</html:form>
	</table>

	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../dateQuary/manualCheckRep.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>