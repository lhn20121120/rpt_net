<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.dao.DBConn"%>
<%@ page import="net.sf.hibernate.Session"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String data = request.getAttribute("workTaskTerm") != null ? request.getAttribute("workTaskTerm").toString() : request.getParameter("workTaskTerm");
	String workTaskTemp = request.getParameter("workTaskTemp") != null ? request.getParameter("workTaskTemp") : "";
	String workTaskOrgId = request.getParameter("workTaskOrgId") != null ? request.getParameter("workTaskOrgId") : "";
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom() : "";
	String orgId = "";
	String orgName = "";
	String searchOrgId = "";
	String  url = "";
	Integer systemSchemaFlag=0;
	if(request.getAttribute("system_schema_flag")!=null){
		
		systemSchemaFlag=(Integer)request.getAttribute("system_schema_flag");
	}
	if(request.getAttribute("orgId")!=null && !request.getAttribute("orgId").toString().equals(""))
		orgId = request.getAttribute("orgId").toString();
	else
		orgId = operator != null ? operator.getOrgId() : "";
	if(request.getAttribute("url")!=null && !request.getAttribute("url").toString().equals(""))
		url = request.getAttribute("url").toString();
	String operatorOrgId = operator != null ? operator.getOrgId() : "";
	if(request.getAttribute("orgName")!=null && !request.getAttribute("orgName").toString().equals(""))
		orgName = request.getAttribute("orgName").toString();
	else
		orgName = operator != null ? operator.getOrgName() : "";
	String selectOrgId = orgId;
	searchOrgId = orgId;
	
	String curpage = (String)request.getAttribute("curPage");
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	boolean yjhlxzr = operator.isExitsThisUrl("YJHLXZR");//��������Ȩ��
	
	//���±�������(begin)
	boolean result = false;
	// ���ӺͻỰ����ĳ�ʼ��
	DBConn conn = null;
	Session se = null;
	Statement stat = null;
	try {
		conn = new DBConn();
		se = conn.beginTransaction();
		Connection con = se.connection();
		String sql = "update report_in r set r.rep_name=(select max(report_name) from m_child_report where child_rep_id=r.child_rep_id and version_id=r.version_id) where r.rep_name is null or r.rep_name=''";
		stat = con.createStatement();
		stat.execute(sql);
		se.flush();
		result = true;
	}catch(Exception e){
		result = false;
		e.printStackTrace();
	}finally{
		if(stat!=null)
			stat.close();
		if (conn!= null)
			conn.endTransaction(result);
	}
	//���±�������(end);
	
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepReportPodedom%>"/>

<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/js/progressBar.js"></script>
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">
		var  progressBar=new ProgressBar("����ͬ�����ݣ����Ժ�........");
	
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>	 

 		  var _year ;	
		  var _term ;		
	    
	    	var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    	var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    	
	    	var isOnLine = "<%=com.cbrc.smis.system.cb.InputData.isOnLine%>";
	    	var messageInfo = "<%=com.cbrc.smis.system.cb.InputData.messageInfo%>";
	    
	    	if(isOnLine != null && isOnLine == "true"){
	    		alert(messageInfo);
	    	}
	    		    	    	
		  	function _submit(form){
				if(form.date.value==""){
					alert("�����뱨��ʱ��!");
					form.date.focus();
					return false;
				}
			}
			//�ر�ԭ��鿴
			function why(why){
		  	 	window.open ("<%=request.getContextPath()%>/template/data_report/tip.jsp?reason="+why,"", "height=250, width=250, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");		  	 	  	 		  	 	
		  	}
		  	//�����	
		  	function _view_zxbs(childRepId,versionId,year,term,curId,dataRgId,repName){  	
		  		  		
		     	location.href="<%=request.getContextPath()%>/onLineReport.do?" + "childRepId=" + childRepId + "&versionId=" + versionId + "&year=" + year + "&term=" + term +"&curId=" + curId + "&curPage=" + curPage + "&orgId=<%=selectOrgId%>"+"&dataRangeId="+dataRgId;
		    }
		    //�����޸�
		    function _view_zxxg(repInId){
		     	location.href="<%=request.getContextPath()%>/failedReportEdit.do?" + "repInId=" + repInId + "&curPage=" + curPage;
		    }
		    // ���߱���
		    function _view_LSBS(childRepId,versionId,year,term,curId,dataRgId,actuFreqID,orgId){
		    
		    	var dates = document.getElementById("date");	
		    	var repNames = document.getElementById("repName");	
		    	var workTaskTemp = document.getElementById("workTaskTemp").value;
				var workTaskTerm = document.getElementById("workTaskTerm").value;
			    var workTaskOrgId = document.getElementById("workTaskOrgId").value;
		    	 if(<%=systemSchemaFlag%>==1){
					location.href="<%=request.getContextPath()%>/offLineReport.do?" 
			    	 + "childRepId=" + childRepId 
			    	 + "&versionId=" + versionId 
			    	 + "&year=" + year 
			    	 + "&term=" + term 
			    	 + "&curId=" + curId 
			    	 + "&curPage=" + curPage 
			    	 //+ "&orgId=<%=selectOrgId%>"
			    	 + "&orgId=" + orgId
			    	 + "&dataRangeId="+dataRgId
			    	 + "&flag="+true
			    	 + "&actuFreqID="+actuFreqID
			    	 + "&backQry=" +dates.value+"_"+repNames.value+"_"+orgId+"_"+curPage
			    	 +"&workTaskTemp="+workTaskTemp
					 +"&workTaskTerm="+workTaskTerm
					 +"&workTaskOrgId="+workTaskOrgId
			    	 +"&searchOrgId=<%=searchOrgId%>"; 
			    }else{
			    	location.href="<%=request.getContextPath()%>/offLineReport.do?" 
			    	 + "childRepId=" + childRepId 
			    	 + "&versionId=" + versionId 
			    	 + "&year=" + year 
			    	 + "&term=" + term 
			    	 + "&curId=" + curId 
			    	 + "&curPage=" + curPage 
			    	 //+ "&orgId=<%=selectOrgId%>"
			    	 + "&orgId=" + orgId
			    	 + "&dataRangeId="+dataRgId
			    	 + "&flag="+true
			    	 + "&actuFreqID="+actuFreqID
			    	 + "&backQry=" +dates.value+"_"+repNames.value+"_"+orgId+"_"+curPage
			    	 +"&searchOrgId=<%=searchOrgId%>"; 
			    }	
		    	 		    
		    }

			function lixianzairu(){
				
				var term = document.getElementById("date1").value;	
				//alert(term);
				document.getElementById("hidTerm").value= term;	
				document.getElementById("banchOffLineReportForm").submit();
		    	//var orgId = document.getElementById("orgId1").value;	
		    	//alert(orgId);
		    	//alert("url = "+"<%=url%>");
				//location.href="<%=request.getContextPath()%>/banchOffLineReport.do?"
					//	+ "term=" + term 						
				//		+ "&curPage= 1 " 
				//		+ "&orgId=<%=selectOrgId%>"	
				//		+ "&url=<%=url%>"	
				//		;
			}
		    //�����ϴ�
		    function _view_PLBS(){
				var date = document.getElementById("date1").value;	
				var year = date.substr(0,4);
				var term = date.substr(6,1);
				
				alert(term);
		    	location.href="<%=request.getContextPath()%>/template/data_report/upMoreReport.jsp?year="+ year +"&term=" + term+"&curPage"+curPage;
		    }
		    
		    function _submit2(form){			
				if(form.formFile.value==""){
					alert("�ϴ��ļ�����Ϊ��");
					form.formFile.focus();
					return false;
				}
				if(getExt(form.formFile.value)!=EXT_EXCEL && getExt(form.formFile.value)!=EXT_ZIP){
			 		alert("ѡ��ı����ļ�������Excel��zip���ļ�!");
			 		form.formFile.focus();
			 		return false;
			 	}
				var year_kj = document.getElementById("year");	
				var term_kj = document.getElementById("term");
				var version_kj = document.getElementById("versionId");	
				version_kj.value = year_kj.value + "_" + term_kj.value + "_<%=selectOrgId%>_" + curPage;
				return true;
			}	
			function _view_jyxx(repInId){
		     	window.open("<%=request.getContextPath()%>/report/viewDataJYInfo.do?" + "repInId=" + repInId,'У����','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
		    }
		    function _view_XSYY(repInId){
		     	//window.open("<%=request.getContextPath()%>/report/viewJYNotPassInfo.do?" + "repInId=" + repInId,'У����','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
		     	window.open("<%=request.getContextPath()%>/report/viewDataJYInfo.do?" + "repInId=" + repInId,'У����','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
		    }
		//ʧ�ܴ���
	    function reportError(request)
	    {
	        alert('ϵͳæ�����Ժ�����...��');
	    }

 		var reportInId = null;

  
	    //������  
		function _view_sjbs(repInId,year,term)
		{
		
			var date = document.getElementById("date");	
			var repName = document.getElementById("repName");
		
			if(confirm('ȷ�����͸ñ���')){
			 	try
			 	 {
				  	reportInId=repInId;
				  	_year = year;
				  	_term = term;
				  	_date = date;
				  	_repName = repName;
				  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineReport.do?repInId=" + repInId ;
				    var param = "radom="+Math.random();
				   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}
			   	catch(e)
			   	{
			   		alert('ϵͳæ�����Ժ�����...��PS');
			   	}
			}
		} 
		//����Handler
		function upReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
					
				  if(result == 'true')
				  {
					 alert('���ͳɹ���');	
					 //window.location="<%=request.getContextPath()%>/viewDataReport.do?" + "year="+_year+"&term="+_term+"&curPage="+curPage; 
				 	 location.href="<%=request.getContextPath()%>/viewDataReport.do?" + "date="+_date.value+"&repName="+_repName.value+"&curPage="+curPage; 
				  }
				  else  if(result == 'BJ_VALIDATE_NOTPASS')
				  {
				     alert("���У�鲻ͨ���������ϱ��ñ���");
				  }else if( result == 'BN_VALIDATE_NOTPASS'){
				  
				 	 alert("����У�鲻ͨ���������ϱ��ñ���");
				  }
				  else{
				 	 alert('ϵͳæ�����Ժ�����...��P');
				  
				  }
			}
			catch(e)
			{}
	    }
	    
	    /**
	     * ȫѡ����
	     */
		function _selAll(){			
			var count = document.getElementById("countChk").value;
		  	  	
			for(var i=1;i<=count;i++){
				try{
					if(document.getElementById('chkAll').checked==false)
						eval("document.getElementById('chk" + i + "').checked=false");
					else
						eval("document.getElementById('chk" + i + "').checked=true");
				}catch(e){}
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
						var param = "radom="+Math.random();
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
	    
		/**
		 * У��ȫ������
		 */
		function _validate_All(){
			if(confirm("У��ȫ����Ҫ�ϳ�ʱ�䣬��ȷ��Ҫ����У��ȫ��������!")==true){
				try{
					var date = document.getElementById("date").value;
					var validateURL = "<%=request.getContextPath()%>/report/validateAllReport.do?date="+date+"&orgId=<%=orgId%>";
					var param = "radom="+Math.random();
					new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateAllReportHandler,onFailure: reportError});

				}catch(e){
					alert('ϵͳæ�����Ժ�����...��VL');
					return;
				}
			}
		}				
		
	    //У��ȫ��Handler
		function validateAllReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				if(result == 'true'){
				  	alert('ȫ��У��ͨ����');	
				  	     
				}else if(result == 'exception'){
					alert('У���쳣�����Ժ�����...!');
				}else{					
					var failedRepIds = result.split(",");				
					if(confirm('У�����,��'+failedRepIds.length+'��У��δͨ�����Ƿ���Ҫ�鿴У����Ϣ?'))
					{
				        window.open("<%=request.getContextPath()%>/report/viewMoreDataJYInfo.do?" + "repInIds=" + result,'У����','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				  	  
				    }
				}
				prodress1.style.display = "" ;
		   		prodress.style.display = "none" ;	
				selectReport();

			}catch(e){}
	    }
	    
	    /**
		 * ����ѡ�еı���
		 */
		function _report_Select(){
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
		  	  	alert("����δ�,�����ϱ�!\n");
		  	  	return;
			}else{
		  	  	if(confirm("��ȷ��Ҫ��������������?��������ֻ����У��ͨ���ı���\n")==true){
					try{
						var validateURL = "<%=request.getContextPath()%>/upLoadReport.do?type=select&repInIds="+repInIds; 
						var param = "radom="+Math.random();
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:reportReportHandler,onFailure: reportError});
											
					}catch(e){
						alert('ϵͳæ�����Ժ�����...��RS');
						return;
					}  	  				
				}
			}
		}
		
		//����Handler
		function reportReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				if(result == 'true'){
				  	alert('���ͳɹ���');	
				  
				}else{
					alert("������ɣ�������"+result+"�ű�������У��δͨ���޷��ϱ���");					 
				}
								
				prodress1.style.display = "" ;
		  		prodress.style.display = "none" ;
				selectReport();

		   		
			}catch(e){}
	    }
	    
		/**
		 * ����ȫ������
		 */
		function _report_All(){
			if(confirm("����ȫ��ֻ����У��ͨ���ı����ұ���ȫ����Ҫ�ϳ�ʱ�䣬��ȷ��Ҫ���б���ȫ��������!")==true){
				try{
					var date = document.getElementById("date").value;
					var repName = document.getElementById("repName").value;
					
					var validateURL = "<%=request.getContextPath()%>/upLoadReport.do?type=all&date="+ date +"&repName="+repName+"&orgId=<%=orgId%>";
					var param = "radom="+Math.random();
					
					new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:reportAllReportHandler,onFailure: reportError});

				}catch(e){
					alert('ϵͳæ�����Ժ�����...��');
					return;
				}
			}
		}				
		
	    /**
	     * ȫ������Handler
	     */
		function reportAllReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				if(result == 'true'){
				  	alert('ȫ������ͨ����');	
				}else if(result == 'exception'){
					alert('�����쳣�����Ժ�����...!');
				}else{					
					alert("������ɣ�������"+result+"�ű�������У��δͨ���޷��ϱ���");
				}
				
				prodress1.style.display = "" ;
		   		prodress.style.display = "none" ;	
				selectReport();

			}catch(e){}
	    }
	    	    
	    //ʧ�ܴ���
	    function reportError(request){
	        alert('ϵͳæ�����Ժ�����...��');
	    }
	    
	    //���ݱȶ�
		function check(repInId,compRepInId){
			if(compRepInId == 0){
				alert("ȱ�ٻ������ݣ��޷��ȶԣ�");
				return false;
			}
			window.open("<%=request.getContextPath()%>/viewCheckReport.do?repInId="+repInId+"&compRepInId="+compRepInId,"Check");
		}
		
		// ��ѯ����
		function selectReport(){
		 	var date = document.getElementById("date").value;
		  	var repName = document.getElementById("repName").value;
		  	var orgId = document.getElementById("orgId").value;
		  	var workTaskTemp = document.getElementById("workTaskTemp").value;
		    var workTaskTerm = document.getElementById("workTaskTerm").value;
		    var workTaskOrgId = document.getElementById("workTaskOrgId").value;
		  	
			var url = "<%=request.getContextPath()%>/viewDataReport.do?date="+date+"&repName="+repName+"&orgId="+orgId+"&curPage="+curPage+"&workTaskTemp="+workTaskTemp+"&workTaskTerm="+workTaskTerm+"&workTaskOrgId="+workTaskOrgId;
			location.href= url;
		}
		
		function viewPdf(repInId,year,term,day,orgId){
		
		    var dates = document.getElementById("date");	
		    var repNames = document.getElementById("repName");
	        var workTaskTemp = document.getElementById("workTaskTemp").value;
		    var workTaskTerm = document.getElementById("workTaskTerm").value;
			var workTaskOrgId = document.getElementById("workTaskOrgId").value;	
		    if(<%=systemSchemaFlag%>==1){
		    	location.href = "<%=request.getContextPath()%>/editAFReport.do?statusFlg=2&repInId=" + repInId
				+"&year="+year
				+"&term="+term
				+"&day="+day
				+"&orgId="+orgId
				+"&backQry=" +dates.value+"_"+repNames.value+"_"+orgId+"_"+curPage
				+"&type=marge"
				+"&workTaskTemp="+workTaskTemp
				+"&workTaskTerm="+workTaskTerm
				+"&workTaskOrgId="+workTaskOrgId
				+"&searchOrgId=<%=searchOrgId%>";
		    }else{
		    	location.href = "<%=request.getContextPath()%>/editAFReport.do?statusFlg=2&repInId=" + repInId
				+"&year="+year
				+"&term="+term
				+"&day="+day
				+"&orgId="+orgId
				+"&backQry=" +dates.value+"_"+repNames.value+"_"+orgId+"_"+curPage
				+"&type=marge"
				+"&searchOrgId=<%=searchOrgId%>";
		    }	
					    
			 
		}
		function editExcel(templateId,versionId,curId,repFreqId,year,term,day,orgId){
		
			var dates = document.getElementById("date");	
		    var repNames = document.getElementById("repName");	
		    var workTaskTemp = document.getElementById("workTaskTemp").value;
			var workTaskTerm = document.getElementById("workTaskTerm").value;
		    var workTaskOrgId = document.getElementById("workTaskOrgId").value;
		     if(<%=systemSchemaFlag%>==1){
		    	location.href = "<%=request.getContextPath()%>/editAFReport.do?statusFlg=3&templateId=" + templateId
				+"&versionId="+versionId
				+"&curId="+curId
				+"&repFreqId="+repFreqId
				+"&year="+year
				+"&term="+term
				+"&day="+day
				+"&orgId="+orgId
				+"&backQry=" +dates.value+"_"+repNames.value+"_"+orgId+"_"+curPage
				+"&type=marge"
				+"&workTaskTemp="+workTaskTemp
				+"&workTaskTerm="+workTaskTerm
				+"&workTaskOrgId="+workTaskOrgId
				+"&searchOrgId=<%=searchOrgId%>";
		    }else{
		    	location.href = "<%=request.getContextPath()%>/editAFReport.do?statusFlg=3&templateId=" + templateId
				+"&versionId="+versionId
				+"&curId="+curId
				+"&repFreqId="+repFreqId
				+"&year="+year
				+"&term="+term
				+"&day="+day
				+"&orgId="+orgId
				+"&backQry=" +dates.value+"_"+repNames.value+"_"+orgId+"_"+curPage
				+"&type=marge"
				+"&searchOrgId=<%=searchOrgId%>";
		    }
					    
			 
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

		function setAFForceRep(){
			var count = document.getElementById("countChk").value;

			var checkObj = null;
			var repInIds = "";
			for(var i=1;i<=count;i++){
				try{
					checkObj = eval("document.getElementById('chk" + i + "')");
					if(checkObj.checked == true)
						repInIds+=(repInIds==""?"":",") + checkObj.value;
				}catch(e){}				
			}
			
			if(repInIds==""){
		  	  	alert("��ѡ��Ҫǿ���ϱ��ı���!\n");
		  	  	return;
			}else{
				if(confirm("��ȷ��Ҫ����ǿ���ϱ��趨��?\n")){
					try{
						var validateURL = "<%=request.getContextPath()%>/addAFForceRepAction.do?repInIds="+repInIds; 
						var param = "radom="+Math.random();
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:addAFForceRepHandler,onFailure: reportError});
					}catch(e){
						alert('ϵͳæ�����Ժ�����...��VS');
						return;
					}  	  		
				}
			}
		}

		function addAFForceRepHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				if(result == 'true'){
				  	alert('ǿ���ϱ��趨�ɹ���');	
				  
				 	//window.location.reload();		     
				}else{
					alert("ǿ���ϱ��趨ʧ��");
				}
				prodress1.style.display = "" ;
		   		prodress.style.display = "none" ;	
				selectReport();

		   		
			}catch(e){}
	    }

		function killAFForceRep(){
			var count = document.getElementById("countChk").value;

			var checkObj = null;
			var repInIds = "";
			for(var i=1;i<=count;i++){
				try{
					checkObj = eval("document.getElementById('chk" + i + "')");
					if(checkObj.checked == true)
						repInIds+=(repInIds==""?"":",") + checkObj.value;
				}catch(e){}				
			}
			
			if(repInIds==""){
		  	  	alert("��ѡ��Ҫȡ��ǿ���ϱ��ı���!\n");
		  	  	return;
			}else{
				if(confirm("��ȷ��Ҫȡ��ǿ���ϱ��趨��?\n")){
					try{
						var validateURL = "<%=request.getContextPath()%>/deleteAFForceRepAction.do?repInIds="+repInIds; 
						var param = "radom="+Math.random();
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:deleteAFForceRepHandler,onFailure: reportError});
					}catch(e){
						alert('ϵͳæ�����Ժ�����...��VS');
						return;
					}  	  		
				}
			}
		}
		function deleteAFForceRepHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				if(result == 'true'){
				  	alert('ȡ��ǿ���ϱ��趨�ɹ���');	
				  
				 	//window.location.reload();		     
				}else{
					alert("ȡ��ǿ���ϱ��趨ʧ��");
				}
				prodress1.style.display = "" ;
		   		prodress.style.display = "none" ;	
				selectReport();

		   		
			}catch(e){}
	    }
		// add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
		
		function viewPdf2(repInId,templateId,versionId,curId,repFreqId,year,term,day,orgId){
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
		
		//add by jmq ͬ������
		function _syncData(obj){
			var count = document.getElementById("countChk").value;
 			var date = document.getElementById("date").value;
		  	var orgId = document.getElementById("orgId").value;
			var checkObj = null;
			var repNames = "";
			for(var i=1;i<=count;i++){
				try{
					checkObj = eval("document.getElementById('chk" + i + "')");
					if(checkObj.checked == true)
						repNames+=(repNames==""?"":",") + document.getElementById("sync"+i).value;
				}catch(e){}				
			}
			if(repNames==""){
		  	  	alert("��ѡ��Ҫͬ��������!\n");
		  	  	return;
			}else{		  	  		
		  	  	try{	obj.disabled=true;
						var isAddTrace = false;
						//if(confirm("�Ƿ������ݵ���"))
							//isAddTrace = true;													
						var syncURL = "<%=request.getContextPath()%>/synsDataAction.do?repNames="+repNames+"&date="+date+"&orgId="+orgId+"&isaddtrace="+isAddTrace+"&curPage="+curPage+"&radom="+Math.random();
						var param = "radom="+Math.random();
						$.post(syncURL,function(result){
					   	 alert(result);
					   	selectReport();
						 });
		  				prodress1.style.display = "" ;
		  				prodress.style.display = "none" ;
					}catch(e){
						alert('ϵͳæ�����Ժ�����...��');
						return;
					}  
			}
		}
	</SCRIPT>
</head>
<% 
	com.cbrc.smis.system.cb.InputData.isOnLine = null;
	com.cbrc.smis.system.cb.InputData.messageInfo = "";
%>
<body onload="isHiddenQueryTable()">
	<input type="hidden" id="" name ="workTaskTemp" value="<%=workTaskTemp%>"/>
	<input type="hidden" id="" name ="workTaskTerm" value="<%=data%>"/>
	<input type="hidden" id="" name ="workTaskOrgId" value="<%=workTaskOrgId%>"/>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
<label id="prodress" style="display:none">
			<span class="txt-main" style="color:#FF3300">����У�鱨��,���Ժ�......</span>
		</label>
  <label   id="prodress1" >
	<table border="0" width="98%" align="center" id="listOrder">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� &gt;&gt; ���ݴ��� &gt;&gt; �����ϱ�
			</td>
			
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center" id="queryTable">
		<html:form action="/viewDataReport.do" method="post" styleId="frm" onsubmit="return _submit(this)">
			<html:hidden property="orgId"/>
			<tr>
				<td>
				<fieldset id="fieldset">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
								<td height="5"></td>
							</tr>
						<tr>
							<td height="25">				
							&nbsp;����ʱ�䣺
								<html:text property="date" styleClass="input-text" size="10" styleId="date1"
										readonly="true" onclick="return showCalendar('date1', 'y-mm-dd');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
										onclick="return showCalendar('date1', 'y-mm-dd');">
							</td>
							<td>
								�������ƣ�
								<html:text property="repName" styleId="_repName" size="20" styleClass="input-text"/>
							</td>
							<td height="23" align="left">
								���������
								<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" styleClass="input-text" value="<%=orgName %>" ></html:text>
								<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
								<script type="text/javascript">
								<%if(systemSchemaFlag==0){%>
								
									<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
								    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
							      	tree1.init();
							<%}%>
							 	</script>
							 	</div>
							</td>
							<td>
								<html:submit styleClass="input-button" value=" �� ѯ " />
								<!--  <input class="input-button" onclick="_view_PLBS()" type="button" value="��������">								 
								 -->
							</td>
						</tr>

					</table>
					</fieldset>
				</td>
			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</html:form>
	</table>
	<br />	
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<logic:present name="Records" scope="request">
			<tr>
				<td>
					<input class="input-button" onclick="_validate_Select()" type="button" value="У��ѡ��">
					<%if(systemSchemaFlag == 0) {%>
					<input class="input-button" onclick="_validate_All()" type="button" value="У��ȫ��">
					<%} %>
					&nbsp;&nbsp;��&nbsp;&nbsp;
					<input class="input-button" onclick="_report_Select()" type="button" value="����ѡ��">
					<%if(systemSchemaFlag == 0){ %>
					<input class="input-button" onclick="_report_All()" type="button" value="����ȫ��">	
					<%} %>
					<%if(systemSchemaFlag == 1 && -1==Config.NODE_ID){ %>
					&nbsp;&nbsp;��&nbsp;&nbsp;
					<input type="button" value="ͬ������" class="input-button" onclick="return _syncData(this)">
					<%} %>
					<%
						if(true){//���������޴˹���
							String searchOrgId_2 = (String)request.getAttribute("orgId");
							//��¼�û�Ϊ�����û������Ҳ�ѯ�ӻ���ʱ����ʾ�趨ǿ���ر�
							System.out.println("searchOrgId_2:"+searchOrgId_2);
							System.out.println("OrgId:"+orgId);
							System.out.println("operatorOrgId:"+operatorOrgId);
							System.out.println("HEAD_ORG_ID:"+com.fitech.gznx.common.Config.HEAD_ORG_ID);
							if(Config.ISFORCEREP){
								if(operatorOrgId!=null && operatorOrgId.equals(com.fitech.gznx.common.Config.HEAD_ORG_ID)){
							%>
								&nbsp;&nbsp;��&nbsp;&nbsp;
								<input class="input-button" onclick="setAFForceRep()" type="button" value="ǿ���ϱ��趨" />
								<input class="input-button" onclick="killAFForceRep()" type="button" value="ȡ��ǿ���ϱ�" />
							<%
								}
							}
						}
						
					%>	
					
					&nbsp;&nbsp;��&nbsp;&nbsp;
					<input type="button" value="��������" class="input-button" onclick="lixianzairu()" title="����������������������">								
				</td>				
			</tr>
		</logic:present>
		<tr>
			<td>
			<br/>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th align="center" colspan="11">
							���ݱ���
						</th>
					</tr>
					<TR class="middle">
						<td width="4%" align="center" valign="middle">
							<input type="checkbox" name="chkAll" onclick="_selAll()">
						</td>
						<td align="center" valign="middle"  width="5%">
							���
						</td>
						<td align="center" valign="middle"  width="30%">
							��������
						</td>
						<td align="center" valign="middle" width="5%" NOWRAP>
							�汾��
						</td>
						<td align="center" valign="middle" width="6%">
							����
						</td>
						<td align="center" valign="middle" width="11%">
							����ھ�
						</td>
						<td align="center" valign="middle" width="3%" NOWRAP>
							Ƶ��
						</td>
						<td align="center" valign="middle" width="10%" NOWRAP>
							����
						</td>
						<td align="center" valign="middle" width="9%">
							����ʱ��
						</td>
						<Td align="center" valign="middle" width="20%">
							״̬(����)
						</Td>

					</TR>
					<%
						int i = 0;
					%>
					<logic:present name="Records" scope="request">
						<logic:iterate id="viewReportIn" name="Records">							
							<TR bgcolor="#FFFFFF" onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
								<TD align="center">
									<!--
									�����ݿ����Ѿ��м�¼�ı������Ҫ����У��
									�������еļ�¼����˲�ͨ�������ͨ���ı�����������У��
									δ��ı���Ҳ����Ҫ��У��
									-->
									<input type="hidden" value="<bean:write name='viewReportIn' property='checkFlag'/>"/>
										<logic:notEmpty name="viewReportIn" property="repInId" >
										<logic:equal name="viewReportIn" property="checkFlag" value="4">
											<%i++;%>
											<input type="hidden" name="sync<%=i%>" id="sync<%=i%>" value="<bean:write name="viewReportIn" property="childRepId"/>_<bean:write name="viewReportIn" property="versionId"/>_<bean:write name='viewReportIn' property='dataRgId'/>_<bean:write name="viewReportIn" property="actuFreqID" />_<bean:write name="viewReportIn" property="curId" />_<bean:write name="viewReportIn" property="orgId" />"/>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="-1">
											<%i++;%>
											<input type="hidden" name="sync<%=i%>" id="sync<%=i%>" value="<bean:write name="viewReportIn" property="childRepId"/>_<bean:write name="viewReportIn" property="versionId"/>_<bean:write name='viewReportIn' property='dataRgId'/>_<bean:write name="viewReportIn" property="actuFreqID" />_<bean:write name="viewReportIn" property="curId" />_<bean:write name="viewReportIn" property="orgId" />"/>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="2">
											<%i++;%>
											<input type="hidden" name="sync<%=i%>" id="sync<%=i%>" value="<bean:write name="viewReportIn" property="childRepId"/>_<bean:write name="viewReportIn" property="versionId"/>_<bean:write name='viewReportIn' property='dataRgId'/>_<bean:write name="viewReportIn" property="actuFreqID" />_<bean:write name="viewReportIn" property="curId" />_<bean:write name="viewReportIn" property="orgId" />"/>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="3">
											<%i++;%>
											<input type="hidden" name="sync<%=i%>" id="sync<%=i%>" value="<bean:write name="viewReportIn" property="childRepId"/>_<bean:write name="viewReportIn" property="versionId"/>_<bean:write name='viewReportIn' property='dataRgId'/>_<bean:write name="viewReportIn" property="actuFreqID" />_<bean:write name="viewReportIn" property="curId" />_<bean:write name="viewReportIn" property="orgId" />"/>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>	
										<%if(systemSchemaFlag == 0){ %>
										<logic:equal name="viewReportIn" property="checkFlag" value="-1">
											--
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="1">
											--
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="3">
											<%i++;%>
											<input type="hidden" name="sync<%=i%>" id="sync<%=i%>" value="<bean:write name="viewReportIn" property="childRepId"/>_<bean:write name="viewReportIn" property="versionId"/>_<bean:write name='viewReportIn' property='dataRgId'/>_<bean:write name="viewReportIn" property="actuFreqID" />_<bean:write name="viewReportIn" property="curId" />_<bean:write name="viewReportIn" property="orgId" />"/>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>	
										<logic:equal name="viewReportIn" property="checkFlag" value="0">
											--
										</logic:equal>
										<%} %>
									</logic:notEmpty>
									<logic:empty name="viewReportIn" property="repInId">
										<%i++;%>
											<input type="hidden" name="sync<%=i%>" id="sync<%=i%>" value="<bean:write name="viewReportIn" property="childRepId"/>_<bean:write name="viewReportIn" property="versionId"/>_<bean:write name='viewReportIn' property='dataRgId'/>_<bean:write name="viewReportIn" property="actuFreqID" />_<bean:write name="viewReportIn" property="curId" />_<bean:write name="viewReportIn" property="orgId" />"/>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
									</logic:empty>
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="childRepId" />
								</TD>
								<TD align="center">
										<logic:equal name="viewReportIn" property="checkFlag" value="1" >
										<a style="text-decoration:underline" href="javascript:viewPdf2('<bean:write name='viewReportIn' property='repInId'/>',
									 								'<bean:write name='viewReportIn' property='childRepId'/>',
									 								'<bean:write name='viewReportIn' property='versionId'/>',
									 								'<bean:write name='viewReportIn' property='curId'/>',
									 								'<bean:write name='viewReportIn' property='actuFreqID'/>',
									 								'<bean:write name='viewReportIn' property='year'/>',
									 								'<bean:write name='viewReportIn' property='term'/>',
									 								'<bean:write name='viewReportIn' property='day'/>',
									 								'<bean:write name='viewReportIn' property='orgId'/>')">
											<bean:write name="viewReportIn" property="repName" /></a>
								</logic:equal>
								<logic:equal name="viewReportIn" property="checkFlag" value="0" >
										<a style="text-decoration:underline" href="javascript:viewPdf2('<bean:write name='viewReportIn' property='repInId'/>',
									 								'<bean:write name='viewReportIn' property='childRepId'/>',
									 								'<bean:write name='viewReportIn' property='versionId'/>',
									 								'<bean:write name='viewReportIn' property='curId'/>',
									 								'<bean:write name='viewReportIn' property='actuFreqID'/>',
									 								'<bean:write name='viewReportIn' property='year'/>',
									 								'<bean:write name='viewReportIn' property='term'/>',
									 								'<bean:write name='viewReportIn' property='day'/>',
									 								'<bean:write name='viewReportIn' property='orgId'/>')">
											<bean:write name="viewReportIn" property="repName" /></a>
								</logic:equal>
								<logic:greaterEqual name="viewReportIn" property="checkFlag" value="2">
											<bean:write name="viewReportIn" property="repName" />
								</logic:greaterEqual>
								<logic:equal name="viewReportIn" property="checkFlag" value="-1">
											<bean:write name="viewReportIn" property="repName" />
								</logic:equal>
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="versionId" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="currName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="dataRgTypeName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="actuFreqName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="orgName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="year" />
									-
									<bean:write name="viewReportIn" property="term" />
									-
									<bean:write name="viewReportIn" property="day" />
								</TD>
								<TD align="center" colspan="2">

									<!-- ���ύ���� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="0">�ѱ���</logic:equal>
									<!-- ���ͨ�� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="1">
										<font color="#00CC00">�ϸ�</font>
									</logic:equal>
									<!-- ���ύ��� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="5">�Ѹ���</logic:equal>
									<!-- ���˲�ͨ�� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="-5">
										<a href="javascript:why('<bean:write name="viewReportIn" property="why"/>')"
											title='<bean:write name="viewReportIn" property="why"/>'><font color="#FF0000">���˲��ϸ�</font> </a>
<%--									<input class="input-button" onclick="_view_zxxg(<bean:write name='viewReportIn' property='repInId'/>)" type="button" value="�޸�">--%>
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
											<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="�޸�">
										</logic:notEqual>
<%--										<logic:notEqual name="viewReportIn" property="compRepInId" value="0">
											<INPUT class="input-button" type="button" value="���ݱȶ�" onclick="check('<bean:write name="viewReportIn" property="repInId"/>','<bean:write name="viewReportIn" property="compRepInId"/>')">
										</logic:notEqual>--%>
										<!-- 
											����������ʱ�ſ���������Ȩ��
											����������Ҫ��������Ȩ��
										 -->
										<%//if(yjhlxzr){
											if(true){
										%>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='dataRgId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
										'<bean:write name='viewReportIn' property='orgId' />')" type="button" value="����">
										<%} %>
									
									</logic:equal>
									
									<!-- ��˲�ͨ�� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="-1">
										<a href="javascript:why('<bean:write name="viewReportIn" property="why"/>')"
											title='<bean:write name="viewReportIn" property="why"/>'><font color="#FF0000">��˲��ϸ�</font> </a>
<%--									<input class="input-button" onclick="_view_zxxg(<bean:write name='viewReportIn' property='repInId'/>)" type="button" value="�޸�">--%>
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
											<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="�޸�">
										</logic:notEqual>
<%--										<logic:notEqual name="viewReportIn" property="compRepInId" value="0">
											<INPUT class="input-button" type="button" value="���ݱȶ�" onclick="check('<bean:write name="viewReportIn" property="repInId"/>','<bean:write name="viewReportIn" property="compRepInId"/>')">
										</logic:notEqual>--%>
										<!-- 
											����������ʱ�ſ���������Ȩ��
											����������Ҫ��������Ȩ��
										 -->
										<%//if(yjhlxzr){
											if(true){
										%>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='dataRgId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
										'<bean:write name='viewReportIn' property='orgId' />')" type="button" value="����">
										<%} %>
									
									</logic:equal>

									<!-- δ� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="3">									
										δ�
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
	 									   	 
										<logic:empty  name="viewReportIn" property="repInId">
											<input class="input-button" onclick="editExcel('<bean:write name="viewReportIn" property="childRepId"/>',
	 									   					'<bean:write name="viewReportIn" property="versionId"/>',
	 									   					'<bean:write name='viewReportIn' property='curId' />',
	 									   					'<bean:write name='viewReportIn' property='actuFreqID' />',
	 									   					'<bean:write name="viewReportIn" property="year" />',
	 									   					'<bean:write name="viewReportIn" property="term" />',
	 									   					'<bean:write name='viewReportIn' property='day' />',
	 									   					'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="����">
										</logic:empty>
										<logic:notEmpty name="viewReportIn" property="repInId">
											<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="����">
										</logic:notEmpty>
										</logic:notEqual>
										<!-- 
											����������ʱ�ſ���������Ȩ��
											����������Ҫ��������Ȩ��
										 -->
										<%//if(yjhlxzr){
											if(true){
										%>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='dataRgId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
										'<bean:write name='viewReportIn' property='orgId' />')" type="button" value="����">
										<%} %>
									</logic:equal>
									<!-- δУ�� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="4">										
										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="0">
											<font color="#CD853F">��������</font>
										</logic:equal>
										<logic:notEqual name="viewReportIn" property="tblInnerValidateFlag" value="0">
											<font color="#FF0000">δУ��</font>
										</logic:notEqual>
<%--										<input class="input-button" onclick="_view_zxxg(<bean:write name='viewReportIn' property='repInId'/>)" type="button" value="�޸�">--%>
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">										
											<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="�޸�">
									    </logic:notEqual>
									    <%--
									    <logic:notEqual name="viewReportIn" property="compRepInId" value="0">
											<INPUT class="input-button" type="button" value="���ݱȶ�" onclick="check('<bean:write name="viewReportIn" property="repInId"/>','<bean:write name="viewReportIn" property="compRepInId"/>')">
										</logic:notEqual>
										 --%>
										 <%if(systemSchemaFlag == 0) {%>
										<input class="input-button" onclick="_view_sjbs('<bean:write name="viewReportIn" property="repInId"/>',										
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='curId' />')" type="button" value="����">
										<%} %>
										<!-- 
											����������ʱ�ſ���������Ȩ��
											����������Ҫ��������Ȩ��
										 -->
										<%//if(yjhlxzr){
											if(true){
										%>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='dataRgId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
										'<bean:write name='viewReportIn' property='orgId' />')" type="button" value="����">
										<%} %>	
									</logic:equal>
									<!-- ��У�� -->
									<logic:equal name="viewReportIn" property="checkFlag" value="2">

										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="-1">
											<a href="javascript:_view_XSYY(<bean:write name='viewReportIn' property='repInId'/>)"><font color="#FF0000">У�鲻ͨ��</font>
											</a>
										</logic:equal>
										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="0">
											<font color="#CD853F">��������</font>
										</logic:equal>
										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="1">
											<logic:notEmpty  name="viewReportIn" property="tblOuterValidateFlag" >
												<logic:equal name="viewReportIn" property="tblOuterValidateFlag" value="-1">
													<a href="javascript:_view_XSYY(<bean:write name='viewReportIn' property='repInId'/>)"><font color="#FF0000">У�鲻ͨ��</font>
													</a>
												</logic:equal>
												<logic:notEqual name="viewReportIn" property="tblOuterValidateFlag" value="-1">
													У��ͨ��
												</logic:notEqual>	
											</logic:notEmpty>	
											<logic:empty  name="viewReportIn" property="tblOuterValidateFlag" >
												У��ͨ��
											</logic:empty>							
										</logic:equal>
										<logic:empty  name="viewReportIn" property="tblOuterValidateFlag" >
											<logic:empty  name="viewReportIn" property="tblInnerValidateFlag" >											
													δУ��
											</logic:empty>
										</logic:empty>	
<%--										<logic:notEqual name="viewReportIn" property="compRepInId" value="0">
											<INPUT class="input-button" type="button" value="���ݱȶ�" onclick="check('<bean:write name="viewReportIn" property="repInId"/>','<bean:write name="viewReportIn" property="compRepInId"/>')">
										</logic:notEqual> --%>
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
											<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="�޸�">
										</logic:notEqual>
										 <%if(systemSchemaFlag == 0) {%>
										<input class="input-button" onclick="_view_sjbs('<bean:write name="viewReportIn" property="repInId"/>',										
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='curId' />')" type="button" value="����">
										<%} %>
										<!-- 
											����������ʱ�ſ���������Ȩ��
											����������Ҫ��������Ȩ��
										 -->
										<%//if(yjhlxzr){
											if(true){
										%>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='dataRgId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
										'<bean:write name='viewReportIn' property='orgId' />')" type="button" value="����">
										<%} %>
									</logic:equal>
								</TD>
							</TR>
						</logic:iterate>
					</logic:present>
					<input type="hidden" name="countChk" value="<%=i%>">
					<logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="11">
								���޷��������ļ�¼
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>
	</table>
	 <table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../viewDataReport.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
	</label>
	<form action="<%=request.getContextPath()%>/banchOffLineReport.do" id ="banchOffLineReportForm" method="post">
	<input type="hidden" name ="term" value="" id="hidTerm" />
	<input type="hidden" name ="curPage" value="1" />
	<input type="hidden" name ="orgId" value="<%=selectOrgId%>" />
	<input type="hidden" name ="url" value="<%=url%>" />
	</form>
</body>
</html:html>
