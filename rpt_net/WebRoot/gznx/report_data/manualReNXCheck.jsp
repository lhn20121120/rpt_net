<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.other.Expression"%>

<%
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	Integer systemSchemaFlag=0;
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepVerifyPodedom = operator != null ? operator.getChildRepCheckPodedom()
			+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType='" + reportFlg +"')" : "";
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			if(request.getAttribute("system_schema_flag")!=null){
				systemSchemaFlag=(Integer)request.getAttribute("system_schema_flag");
			}
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepVerifyPodedom" name="utilSubOrgForm" value="<%=childRepVerifyPodedom%>"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepVerifyPodedom%>"/>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="<%=Config.WEBROOTULR%>js/prototype-1.4.0.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	
	<SCRIPT language="javascript">
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    /**
	     * ����ͨ����ʶ
	     */	    
		var FLAG_OK=1;
	    /**
	     * ���˲�ͨ����ʶ
	     */ 
		var FLAG_NO=-1;
	    /**
	     * ����У���ʶ
	     */
		var FLAG_BL="<%=Expression.FLAG_BL%>";
	    /**
	     * ���У���ʶ
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
			window.open("<%=request.getContextPath()%>/gznx/report_data/viewNXDetail.do?" + 
				"flag=" + FLAG_BL + "&repInId=" + repInId);
		}
		     
	    /**
	     * �鿴���У����ϸ
	     */ 
		function _view_bjjy(repInId){
			window.open("<%=request.getContextPath()%>/gznx/report_data/viewNXDetail.do?" + 
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
			if(isEmpty(form.date.value)){
				alert("��������ȷ�ı���Ĳ�ѯʱ��!\n");
	  	 		form.date.select();
	  	 		form.date.focus();
	  	 		return false;
	  	 	}
			return true;
		}
		
		/**
		 * �������� 
	  	 * 
	  	 * @param flag ��flag=1ʱ����������ͨ������flag=-1ʱ���������˲�ͨ��
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
				alert("��ѡ��Ҫ���˵ı���!\n");
				return;
			}else{
				var objFrm=document.forms['frm'];
				var allFlags="";
		  	  		
				var qry="repInIds=" + repInIds + 
						"&flag=" + flag +
						"&templateId=" + objFrm.elements['templateId'].value + 
						"&repName=" + objFrm.elements['repName'].value + 
						"&bak1=" + objFrm.elements['bak1'].value + 
						"&orgId=" + objFrm.elements['orgId'].value + 
						"&repFreqId=" + objFrm.elements['repFreqId'].value + 
						"&date=" + objFrm.elements['date'].value +
						"&curPage=" + curPage;

		  	  		
				if(flag==FLAG_OK){
					if(confirm("��ȷ������ͨ����?\n")==true){
						window.location="<%=request.getContextPath()%>/dateQuary/saveCheckRepRecheckNX.do?" + qry;			  	  				
					}
				}else{
					if(confirm("��ȷ�����˲�ͨ����?\n")==true){
						window.location="<%=request.getContextPath()%>/gznx/report_data/report_again_add_nx.jsp?" + qry;			  	  				
					}
				}
			}
		}
		
		/**
		 * ȫ������ͨ��
		 *
		 */		  	  
		function _batchAllCheck(flag){ 	  		
			var objFrm=document.forms['frm'];
		  	  		
			var qry="flag=" + flag +
					"&templateId=" + objFrm.elements['templateId'].value + 
					"&repName=" + objFrm.elements['repName'].value + 
					"&bak1=" + objFrm.elements['bak1'].value + 
					"&orgId=" + objFrm.elements['orgId'].value + 
					"&repFreqId=" + objFrm.elements['repFreqId'].value + 
					"&date=" + objFrm.elements['date'].value +
					"&curPage=" + curPage;
				
			if(flag==FLAG_OK){
				if(confirm("��ȷ��Ҫ����ͨ�����б�����?\n")==true){
					window.location="<%=request.getContextPath()%>/dateQuary/saveCheckRepRecheckNX.do?" + qry;	  	  				
				}
			}else{
			//	if(confirm("��ȷ��Ҫ���˲�ͨ�����б�����?\n")==true){
			//		window.location="<%=request.getContextPath()%>/dateQuary/saveAllCheckRepCheck.do?" + qry;			
			//	}
				alert("�޿ɸ��˱���");
			}
		} 	  
		
		/**
		 * ���˲���
		 *
		 * @param flag ���˱�ʶ
		 * @param repInId ʵ���ӱ�ID
		 */
		function _check(flag,repInId){
			var objFrm=document.forms['frm'];
		  	  	
			var qry="repInIds=" + repInId + 
					"&flag=" + flag + 
					"&templateId=" + objFrm.elements['templateId'].value + 
					"&repName=" + objFrm.elements['repName'].value + 
		  	  		"&bak1=" + objFrm.elements['bak1'].value + 
		  	  		"&orgId=" + objFrm.elements['orgId'].value + 
		  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 		  	  			
					"&curPage=" + curPage;

			if(objFrm.elements['date'].value != "")
				qry += "&date=" + objFrm.elements['date'].value;
			
			if(flag==FLAG_OK){
				if(confirm("��ȷ����ǰ�ĸ���ͨ��������?\n")==true){
					window.location="<%=request.getContextPath()%>/dateQuary/saveCheckRepRecheckNX.do?" + qry;
				}
			}else{
				if(confirm("��ȷ����ǰ�ĸ��˲�ͨ��������?\n")==true){
					window.location="<%=request.getContextPath()%>/gznx/report_data/report_again_add_nx.jsp?" + qry;
				}
			}
		}
		
		/**
		 * �������У��
		 */
		function _batchVal(){
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
				alert("��ѡ��ҪУ��ı���!\n");
				return;
			}else{
				if(confirm("��ȷ��Ҫ��������У����?\n")==true){
					try{
						
						var validateURL = "<%=request.getContextPath()%>/dateQuary/validateManualCheckRep.do?repInIds=" + repInIds
																								+ "&curPage=" + curPage
																								+ "&formFlg=check";
						var param = "radom="+Math.random();
						
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
						
						prodress1.style.display = "" ;
				   		prodress.style.display = "none" ;
					}catch(e){
						//alert('ϵͳæ�����Ժ�����...��B');
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
				  	alert('У����ɣ���ϸУ����Ϣ��鿴��ر���');	
				}else{
					alert('ϵͳæ�����Ժ����ԣ�VS');
				//	 if(confirm('У��δͨ�����Ƿ���Ҫ�鿴У����Ϣ?')){
				//        window.open("<%=request.getContextPath()%>/report/viewMoreDataJYInfo.do?" + "repInIds=" + result,'У����','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');  
				//      }
				}

				selectReport();

			}catch(e){}
	    }
		  
		  	  
		/**
		 * У��ȫ��
		 */
		function _batchAllVal(){		  	  		
			var objFrm=document.forms['frmChk'];			
		  	var flag =objFrm.elements['chk1'];

	  		if(confirm("У��ȫ����Ҫ�ϳ�ʱ�䣬��ȷ��Ҫ����У��ȫ��������!")==true){

				try{

					var qry="templateId=" + document.getElementById("templateId").value + 
		  	  			"&repName=" + document.getElementById("repName").value + 
		  	  			"&bak1=" + document.getElementById("bak1").value + 			  	  		
			  	  		"&orgId=" + document.getElementById("orgId").value + 
			  	  		"&repFreqId=" + document.getElementById("repFreqId").value + 
			  	  		"&date=" + document.getElementById("date").value +
			  	  		"&curPage=" + curPage +
			  	  		"&formFlg=check";
			  	
					var validateURL = "<%=request.getContextPath()%>/dateQuary/validateManualCheckRep.do?" + qry;
					var param = "radom="+Math.random();
					
					new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateAllReportHandler,onFailure: reportError});
				
					prodress1.style.display = "";
		   			prodress.style.display = "none" ;	
				}catch(e){
					//alert('ϵͳæ�����Ժ�����...��L');
					return;
				}
			}
		}
		
	    //У��ȫ��Handler
		function validateAllReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				if(result == 'true'){
				  	alert('ȫ��У����ɣ���ϸУ����Ϣ��鿴��ر���');	
				}else if(result == 'exception'){
					alert('У���쳣�����Ժ�����...!');
				}else{
					alert('ϵͳæ�����Ժ����ԣ�VA');					
				//	var failedRepIds = result.split(",");				
				//	if(confirm('У�����,��'+failedRepIds.length+'��У��δͨ�����Ƿ���Ҫ�鿴У����Ϣ?'))
				//	{
				//        window.open("<%=request.getContextPath()%>/report/viewMoreDataJYInfo.do?" + "repInIds=" + result,'У����','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');  	  
				//  }
				}

				selectReport();

			}catch(e){}
	    }
	    
	    //ʧ�ܴ���
	    function reportError(request){
	        alert('ϵͳæ�����Ժ�����...��');
	    }
	    
	    // ��ѯ����
		function selectReport(){
			
			var objForm=document.getElementById("frmChk");
		  	var count=objForm.elements['countChk'].value;
			
			var qry="templateId=" + objFrm.elements['templateId'].value + 
  	  			"&repName=" + objFrm.elements['repName'].value + 
  	  			"&bak1=" + objFrm.elements['bak1'].value + 			  	  		
	  	  		"&orgId=" + objFrm.elements['orgId'].value + 
	  	  		"&repFreqId=" + objFrm.elements['repFreqId'].value + 
	  	  		"&curPage=" + curPage;
			  	
			if(objFrm.elements['date'].value != "")
				qry += "&date=" + objFrm.elements['date'].value;
				
			var url = "<%=request.getContextPath()%>/dateQuary/manualCheckRepNXRecheck.do?" + qry;
			window.location= url;
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
	<%--	function viewPdf(repInId){
			window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
		}--%>
		
		/**
		 * �鿴У�鲻ͨ������Ϣ
		 */
		function _view_XSYY(repInId){
			//window.open("<%=request.getContextPath()%>/report/viewNXNotPassInfo.do?" + "repInId=" + repInId + "&tblOuterValidateFlag=" + FLAG_BJ);
			window.open("<%=request.getContextPath()%>/report/viewValidateInfo.do?" + "repInId=" + repInId,'У����','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
		}
		
		/**
		 * new�鿴��������
		 */ 
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
			}
			else{
				document.getElementById("queryTable").style.display="block";
			}
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

	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center" id="queryTable">
		<html:form action="/dateQuary/manualCheckRepNXRecheck" method="post" styleId="frm" >
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
									<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text" ></html:text>
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

	<logic:present name="form" scope="request">
		<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
			<html:form action="/dateQuary/saveAllCheckRepNXRecheck" method="post" styleId="frm1">
				<tr>
					<td>
						<input type="button" value="У��ѡ��" class="input-button" onclick="return _batchVal()">
						&nbsp;
						<input type="button" value="У��ȫ��" class="input-button" onclick="return _batchAllVal()">
						&nbsp;&nbsp;��&nbsp;&nbsp;
						<input type="button" value="���ͨ��" class="input-button" onclick="_batchCheck(FLAG_OK)">
						&nbsp;
						<input type="button" value="ȫ��ͨ��" class="input-button" onclick="return _batchAllCheck(FLAG_OK)">
						&nbsp;&nbsp;��&nbsp;&nbsp;
						<input type="button" value="��˲�ͨ��" class="input-button" onclick="_batchCheck(FLAG_NO)">
						
					</td>
				</tr>				
			</html:form>
		</table>
	</logic:present>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<html:form action="/dateQuary/saveCheckRepRecheck" method="post" styleId="frmChk">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th width="4%" align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="_selAll()">
							</th>
							<th width="6%" align="center" valign="middle">
								������
							</th>
							<th width="35%" align="center" valign="middle">
								��������
							</th>
							<%-- 
							<th width="11%" align="center" valign="middle">
								����ھ�
							</th>
							--%>
							<th width="5%" align="center" valign="middle">
								Ƶ��
							</th>
							<th width="10%" align="center" valign="middle">
								����
							</th>
							<th width="10%" align="center" valign="middle">
								����ʱ��
							</th>
							<th width="10%" align="center" valign="middle">
								����ʱ��
							</th>
							<th width="11%" align="center" valign="middle">
								���ͻ���
							</th>
							<Th width="9%" align="center" valign="middle">
								״̬
							</Th>
							<%-- 
							<th width="9%" align="center" valign="middle">
								��ϸ��Ϣ
							</th>
							 
							<th width="6%" align="center" valign="middle">
								����
							</th>
							--%>
						</tr>
						<%
						int i = 0;
						%>
						<logic:present name="form" scope="request">
							<logic:iterate id="viewReportIn" name="form">
								<%i++;%>				
								<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
									<td align="center">	
										<input type="checkbox" name="chk<%=i%>" value="<bean:write name="viewReportIn" property="repInId"/>">
									</td>
									<TD align="center">
										<bean:write name="viewReportIn" property="childRepId" />
									</TD>
									<TD align="center">
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
									</TD>
									<!-- 
									<TD align="center">
										<bean:write name="viewReportIn" property="dataRgTypeName" />
									</TD>
									 -->
									<TD align="center">
										<bean:write name="viewReportIn" property="actuFreqName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="currName" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />-<bean:write name="viewReportIn" property="day" />
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="reportDate" format="yyyy-MM-dd HH:mm:ss"/>
									</TD>
									<TD align="center">
										<bean:write name="viewReportIn" property="orgName" />
									</TD>
									<TD align="center">
										<logic:present name="viewReportIn" property="tblOuterValidateFlag">
											<logic:equal name="viewReportIn" property="tblOuterValidateFlag" value="0">
												<font color="#FF9900">����δ��ȫ</font>
											</logic:equal>
											<logic:equal name="viewReportIn" property="tblOuterValidateFlag" value="1">
												У��ͨ��
											</logic:equal>
											<logic:equal name="viewReportIn" property="tblOuterValidateFlag" value="-1">
												<a href="javascript:_view_XSYY(<bean:write name='viewReportIn' property='repInId'/>)"><font color="red">У�鲻ͨ��</font> </a>
											</logic:equal>
										</logic:present>
										<logic:notPresent name="viewReportIn" property="tblOuterValidateFlag">
											δУ��
										</logic:notPresent>
									</TD>
									<%--
									<TD align="center">
										<img src="../../image/bnjy.gif" border="0" title="�鿴����У����ϸ" style="cursor:hand" onclick="_view_bnjy(<bean:write name='viewReportIn' property='repInId'/>)">
										<img src="../../image/bjjy.gif" border="0" title="�鿴���У����ϸ" style="cursor:hand" onclick="_view_bjjy(<bean:write name='viewReportIn' property='repInId'/>)">
										<img src="../../image/excel.bmp" border="0" title="�鿴��������" style="cursor:hand" onclick="javascript:viewPdf(<bean:write name='viewReportIn' property='repInId'/>)">
									</TD>
									
									<TD noWrap align="center" height="30">
										<logic:equal name="viewReportIn" property="checkFlag" value="0">
											<a href="javascript:_check(FLAG_OK,<bean:write name="viewReportIn" property="repInId"/>)"> ͨ�� </a>
											<a href="javascript:_check(FLAG_NO,<bean:write name="viewReportIn" property="repInId"/>)"> ��ͨ�� </a>
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="1">
											<a href="javascript:_check(FLAG_NO,<bean:write name="viewReportIn" property="repInId"/>)"> ��ͨ�� </a>
										</logic:equal>

										<logic:equal name="viewReportIn" property="checkFlag" value="-111">
											<a href="javascript:_check(FLAG_OK,<bean:write name="viewReportIn" property="repInId"/>)"> ͨ�� </a>&nbsp;
										</logic:equal>

									</TD>
									--%>
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
					<jsp:param name="url" value="../../dateQuary/manualCheckRepNXRecheck.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
</body>
</html:html>
