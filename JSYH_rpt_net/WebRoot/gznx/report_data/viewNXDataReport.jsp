<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "0";

	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}

	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom()
			+ " and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType="
			+ reportFlg + ")"
			: "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = orgId;
	String orgName = "";
	if(request.getAttribute("orgName")!=null && !request.getAttribute("orgName").toString().equals(""))
		orgName = request.getAttribute("orgName").toString();
	//else
	//	orgName = operator != null ? operator.getOrgName() : "";
	String curpage = request.getAttribute("curPage") != null ? (String) request.getAttribute("curPage") : "";

	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String date = request.getAttribute("date").toString();
%>
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
	<title>人行报表上报</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
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
					alert("请输入报表时间!");
					form.date.focus();
					return false;
				}

			}
			
			//重报原因查看
			function why(why){
		  	 	window.open ("<%=request.getContextPath()%>/template/data_report/tip.jsp?reason="+why,"", "height=250, width=250, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");		  	 	  	 		  	 	
		  	}
		  	//在线填报	
		  	function _view_zxbs(childRepId,versionId,date,curId,repName){  	
		  		  		
		     	window.location="<%=request.getContextPath()%>/onLineReport.do?" + "childRepId=" + childRepId + "&versionId=" + versionId + "&date=" + date +"&curId=" + curId + "&curPage=" + curPage + "&orgId=<%=selectOrgId%>";
		    }
		    //在线修改
		    function _view_zxxg(repInId){
		     	window.location="<%=request.getContextPath()%>/failedReportEdit.do?" + "repInId=" + repInId + "&curPage=" + curPage;
		    }
		    //离线报送
		    function _view_LSBS(childRepId,versionId,year,term,day,curId,actuFreqID,orgId){
		    
			    var repNames = document.getElementById("repName").value;
			    var orgIds = document.getElementById("orgId").value;
			    var repFreqIds = document.getElementById("repFreqId").value;
			    var bak1 = document.getElementById("bak1").value;
			     var templateId = document.getElementById("templateId").value;
		    	 window.location="<%=request.getContextPath()%>/offLineNXReport.do?" + 
		    	 "childRepId=" + childRepId + 
		    	 "&versionId=" + versionId + 
		    	 "&year=" + year+ "&term=" + term+ "&day=" + day +
		    	 "&curId=" + curId + 
		    	 "&curPage=" + curPage + 
		    	 "&flag=" + true + 
		    	 "&actuFreqID=" + actuFreqID + 
		    	 "&orgId=" + orgId + 
		    	 "&backQry=<%=date%>_"+repNames+"_"+orgIds+"_"+repFreqIds+"_"+bak1+"_"+templateId;		    
		    }
		    //批量上传
		    function _view_PLBS(){
		    	var date = document.getElementById("date");
		    	alert(		    	"<%=request.getContextPath()%>/gznx/report_data/upMoreNXReport.jsp?date="+ date.value +"&curPage="+curPage
		    	);
		    	window.location="<%=request.getContextPath()%>/gznx/report_data/upMoreNXReport.jsp?date="+ date.value +"&curPage="+curPage;
		    }
		    
		    function _submit2(form){			
				if(form.formFile.value==""){
					alert("上传文件不能为空");
					form.formFile.focus();
					return false;
				}
				if(getExt(form.formFile.value)!=EXT_EXCEL && getExt(form.formFile.value)!=EXT_ZIP){
			 		alert("选择的报送文件必须是Excel或zip包文件!");
			 		form.formFile.focus();
			 		return false;
			 	}
				var date_kj = document.getElementById("date");	
				var version_kj = document.getElementById("versionId");	
				version_kj.value = date_kj.value + "_<%=selectOrgId%>_" + curPage;
				return true;
			}
			
			function _view_jyxx(repInId){
		     	window.open("<%=request.getContextPath()%>/report/viewValidateInfo.do?" + "repInId=" + repInId,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
		    }
		    function _view_XSYY(repInId){
		     	window.open("<%=request.getContextPath()%>/report/viewValidateInfo.do?" + "repInId=" + repInId,'校验结果','scrollbars=yes,height=600,width=600,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
		    }
		//失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
	    }

  var reportInId = null;

  
	    //报表报送  
		function _view_sjbs(repInId,year,term,day,curId)
		{
		var repFreqId = document.getElementById("repFreqId");
		var orgId = document.getElementById("orgId");
		var repName = document.getElementById("repName");
		
			if(confirm('确定报送该报表？')){
			 	try
			 	 {
				  	reportInId=repInId;
				  	_date = <%=date%>;
				  	_year = year;
				  	_term = term;
				  	_day = day;
				  	_curId = curId;
				  	_repFreqId = repFreqId
				  	_orgId = orgId;
				  	_repName = repName;
				  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineNXReport.do?repInId=" + repInId ;
				    var param = "radom="+Math.random();
				   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}
			   	catch(e)
			   	{
			   		alert('系统忙，请稍后再试...！T');
			   	}
			}
		} 
		//报送Handler
		function upReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				  if(result == 'true')
				  {
					 alert('报送成功！');	
					 window.location="<%=request.getContextPath()%>/viewNXDataReport.do?date=<%=date%>&repFreqId="+_repFreqId.value+"&orgId="+_orgId.value+"&repName="+_repName.value+"&curPage="+curPage; 
				  }
				  else  if(result == 'BJ_VALIDATE_NOTPASS')
				  {
				     alert("表间校验不通过，不能上报该报表！");
				  }else if( result == 'BN_VALIDATE_NOTPASS'){
				  
				 	 alert("表内校验不通过，不能上报该报表！");
				  }
				  else{
				 	 alert('系统忙，请稍后再试...！V');
				  
				  }
			}
			catch(e)
			{}
	    }
	    
	    /**
	     * 全选操作
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
		 * 校验选中的报表
		 */
		function _validate_Select(){
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
		  	  	alert("请选择要批量校验的报表!\n");
		  	  	return;
			}else{		  	  		
		  	  	if(confirm("您确定要进行批量校验吗?\n")==true){
					try{
						var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReportNX.do?repInIds="+repInIds; 
						var param = "radom="+Math.random();
						
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
						
						prodress1.style.display = "" ;
		  				prodress.style.display = "none" ;	
					}catch(e){
						alert('系统忙，请稍后再试...！');
						return;
					}  	  				
				}
			}
		}
	
		
		
		
		//校验Handler
		function validateReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				if(result == 'true'){
				  	alert('校验通过！');	
				  
				 	//window.location.reload();		     
				}else{
					 if(confirm('校验未通过！是否需要查看校验信息?')){
				        window.open("<%=request.getContextPath()%>/report/viewMoreValidateInfo.do?" + "repInIds=" + result,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				       
				       
				      }
				}
				selectReport();

		   		
			}catch(e){}
	    }
		
		
			//校验Handler
		function bulingReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				if(result == 'true'){
				  	alert('补零成功！');	
				  
				 	//window.location.reload();		     
				}else{
					alert('补零未成功！');
				}
				selectReport();
			}catch(e){}
	    }
	    
		/**
		 * 校验全部报表
		 */
		function _validate_All(){
			if(confirm("校验全部需要较长时间，您确定要进行校验全部操作吗!")==true){
				try{
					var date = document.getElementById("date").value;
					var repName = document.getElementById("repName").value;
		  			var repFreqId = document.getElementById("repFreqId").value;
					var orgId = document.getElementById("orgId").value;
					var bak1 = document.getElementById("bak1").value;
					var templateId1 = document.getElementById("templateId").value;
					
					var validateURL = "<%=request.getContextPath()%>/report/validateAllReportNX.do?date="+ date 
																		+ "&repName=" +repName														+ "&repName="+repName
																		+ "&repFreqId="+repFreqId
																		+ "&orgId="+orgId
																		+ "&bak1="+bak1
																		+ "&templateId="+templateId1;
					var param = "radom="+Math.random();
					
					new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateAllReportHandler,onFailure: reportError});
	
				 	prodress1.style.display = "" ;
		   		 	prodress.style.display = "none" ;	
	
				}catch(e){
					alert('系统忙，请稍后再试...！VL');
					return;
				}
			}
		}				
		
	    //校验全部Handler
		function validateAllReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				if(result == 'true'){
				  	alert('全部校验通过！');
					//window.location.reload();
				}else if(result == 'exception'){
					alert('校验异常，请稍候再试...!');
					//window.location.reload();
				}else{
					var failedRepIds = result.split(",");				
					if(confirm('校验完成,有'+failedRepIds.length+'张校验未通过！是否需要查看校验信息?'))
					{
				        window.open("<%=request.getContextPath()%>/report/viewMoreValidateInfo.do?" 
					        + "repInIds=" 
					        + result,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				  	  
				    }
				}
				selectReport();

			}catch(e){}
	    }
	    
	    /**
		 * 报送选中的报表
		 */
		function _report_Select(){
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
		  	  	alert("请选择要批量报送的报表!\n");
		  	  	return;
			}else{		  	  		
		  	  	if(confirm("您确定要进行批量报送吗?\n")==true){
					try{
						var validateURL = "<%=request.getContextPath()%>/upLoadNXReport.do?type=select&repInIds="+repInIds; 
						var param = "radom="+Math.random();
						new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:reportReportHandler,onFailure: reportError});
					
						prodress1.style.display = "" ;
		  				prodress.style.display = "none" ;	
					}catch(e){
						alert('系统忙，请稍后再试...！');
						return;
					}  	  				
				}
			}
		}
		
	    
	     /**
		 * 选中补零
		 */
		function _buling_Select(){
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
		  	  	alert("请选择要补零报送的报表!\n");
		  	  	return;
			}else{		  	  		
		  	  	if(confirm("您确定要进行批量补零吗?\n")==true){
					try{
						
					//	var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReportNX.do?repInIds="+repInIds; 
						var bulingURL = "<%=request.getContextPath()%>/bulingReport.do?type=select&repInIds="+repInIds ; 
						var param = "radom="+Math.random();
						new Ajax.Request(bulingURL,{method: 'post',parameters:param,onComplete: bulingReportHandler,onFailure: reportError});
						prodress1.style.display = "" ;
		  				prodress.style.display = "none" ;	
					}catch(e){
						alert('系统忙，请稍后再试...！');
						return;
					}  	  				
				}
			}
		}
			
	     function _buling_All(){
	    	 
		  var rdate = document.getElementById("date").value;
		  var repName = document.getElementById("repName").value;
		  var repFreqId = document.getElementById("repFreqId").value;
		  var orgId = document.getElementById("orgId").value;
	    	 if(confirm("全部补零将根据您查询的结果一键补零所有满足条件的报表，\n请确认是否执行?\n")==true){
					try{
						var bulingURL = "<%=request.getContextPath()%>/bulingReport.do?type=all&date=" + rdate + 
													"&repName="+repName+ 
													"&repFreqId="+repFreqId+ 
													"&orgId="+orgId;
													
						var param = "radom="+Math.random();
						new Ajax.Request(bulingURL,{method: 'post',parameters:param,onComplete: bulingReportHandler,onFailure: reportError});
						prodress1.style.display = "" ;
		  				prodress.style.display = "none" ;	
					}catch(e){
						alert('系统忙，请稍后再试...！');
						return;
					}  	  				
				}
	     }
		
	     
	     
	     
	     
	     
	     
	     
	   
		//报送Handler
		function reportReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				
				if(result == 'true'){
				  	alert('报送成功！');	
				  
				}else{
					alert('报送完成，其中'+result+'张报表由于校验未通过无法上报！');
					 
				}
				
				//window.location.reload();		     
				

				selectReport();

		   		
			}catch(e){}
	    }
	    
		/**
		 * 报送全部报表
		 */
		function _report_All(){
			if(confirm("校验全部需要较长时间，您确定要进行校验全部操作吗!")==true){
				try{
					var date = document.getElementById("date").value;
					var repName = document.getElementById("repName").value;
		  			var repFreqId = document.getElementById("repFreqId").value;
					var orgId = document.getElementById("orgId").value;
					var bak1 = document.getElementById("bak1").value;
					var templateId1 = document.getElementById("templateId").value;
					var validateURL = "<%=request.getContextPath()%>/upLoadNXReport.do?type=all&date="+ date 
																		+ "&repName="+repName
																		+ "&repFreqId="+repFreqId
																		+ "&orgId="+orgId
																		+ "&bak1="+bak1
																		+ "&templateId="+templateId1;
					var param = "radom="+Math.random();
					new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:reportAllReportHandler,onFailure: reportError});

					prodress1.style.display = "" ;
			   		prodress.style.display = "none" ;	
				}catch(e){
					alert('系统忙，请稍后再试...！');
					return;
				}
			}
		}				
		
	    /**
	     * 全部报送Handler
	     */
		function reportAllReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				if(result == 'true'){
				  	alert('全部报送通过！');	
				}else if(result == 'exception'){
					alert('报送异常，请稍候再试...!');
				}else{					
					alert('报送完成，其中'+result+'张报表由于校验未通过无法上报！');
				}
				//window.location.reload();
				
				selectReport();

			}catch(e){}
	    }
	    
	    
	    	    
	    //失败处理
	    function reportError(request){
	        alert('系统忙，请稍后再试...！X');
	    }
	    
	    //数据比对
		function check(repInId,compRepInId){
			if(compRepInId == 0){
				alert("缺少汇总数据，无法比对！");
				return false;
			}
			window.open("<%=request.getContextPath()%>/viewCheckReport.do?repInId="+repInId+"&compRepInId="+compRepInId,"Check");
		}
		
		// 查询报表
		function selectReport(){
		  var templateId = document.getElementById("templateId").value;
		  var repName = document.getElementById("repName").value;
		  var repFreqId = document.getElementById("repFreqId").value;
		  var rdate = document.getElementById("date").value;
		  var orgId = document.getElementById("orgId").value;
		  
		var url = "<%=request.getContextPath()%>/viewNXDataReport.do?date=" + rdate + 
													"&templateId="+templateId+ "&repName="+repName+ 
													"&repFreqId="+repFreqId+ 
													"&orgId="+orgId+ 
													"&curPage=" + curPage;
		window.location= url;
		}
		
		function viewPdf(repInId,year,term,day,orgId){
		
				var repNames = document.getElementById("repName").value;
			    var orgIds = document.getElementById("orgId").value;
			    var repFreqIds = document.getElementById("repFreqId").value;
			    var templateId = document.getElementById("templateId").value;
			    var bak1 = document.getElementById("bak1").value;
			window.location = "<%=request.getContextPath()%>/editAFReport.do?statusFlg=2&repInId=" + repInId
							+ "&year="+year
							+ "&term="+term
							+ "&day="+day
							+ "&orgId="+orgId
					    	+ "&backQry=<%=date%>_"+repNames+"_"+orgIds+"_"+repFreqIds+"_"+templateId+"_"+bak1+"_"+curPage;
			; 
		}
		function editExcel(templateId,versionId,curId,repFreqId,year,term,day,orgId){
		
				var repNames = document.getElementById("repName").value;
			    var orgIds = document.getElementById("orgId").value;
			    var repFreqIds = document.getElementById("repFreqId").value;
			    var bak1 = document.getElementById("bak1").value;
			     var templateIdS = document.getElementById("templateId").value;
			window.location = "<%=request.getContextPath()%>/editAFReport.do?statusFlg=3&templateId=" + templateId+
									"&versionId="+versionId+
									"&curId="+curId+
									"&repFreqId="+repFreqId+
									"&year="+year+
									"&term="+term+
									"&day="+day+
									"&orgId="+orgId+
									"&backQry=<%=date%>_"+repNames+"_"+orgIds+"_"+repFreqIds+"_"+templateIdS+"_"+bak1+"_"+curPage;
		}
					function treeOnClick(id,value)
			{
				document.getElementById('bak1').value = id;
				document.getElementById('templateTypeName').value = value;
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden"; 
			}
			

		//显示,关闭树型菜单
		function showTree(){
			if(document.getElementById('orgTree').style.visibility =='hidden'){
			    var textname = document.getElementById('selectedTypeName');
				document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
				document.getElementById("orgTree").style.left = getObjectLeft(textname);
				
				document.getElementById("orgTree").style.height = "200";
				document.getElementById("orgTree").style.visibility = "visible";   // 显示树型菜单
			}
	
			else if(document.getElementById("orgTree").style.visibility == "visible"){
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden";      //关闭树型菜单
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
			document.getElementById("orgpreTree").style.visibility = "visible";   // 显示树型菜单
		}

		else if(document.getElementById("orgpreTree").style.visibility == "visible"){
			document.getElementById("orgpreTree").style.height = "0";
			document.getElementById('orgpreTree').style.visibility="hidden";      //关闭树型菜单
		}
	}
	
		function closeTree(objTxt,objTree){	  
		   var obj = event.srcElement;
		   if(obj!=objTxt && obj!=objTree){
		     
		     objTree.style.height = "0";
		     objTree.style.visibility="hidden";      //关闭树型菜单
		   }
		}
		
		//距离文本框的水平相对位置
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}   
		//距离文本框的垂直相对位置
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}
		
		//一键补零触发按钮
		function _buling(btn){
				var button 
			
			if(btn.value=="激活补零"){
				
				button = document.getElementById("btn1");
				button.disabled = true;
				button = document.getElementById("btn2");
				button.disabled = true;
				button = document.getElementById("btn3");
				button.disabled = true;
				button = document.getElementById("btn4");
				button.disabled = true;
				btn.value="取消补零";					
			}
			else {
				
				btn.value="激活补零";
				button = document.getElementById("btn1");
				button.disabled = false;
				button = document.getElementById("btn2");
				button.disabled = false;
				button = document.getElementById("btn3");
				button.disabled = false;
				button = document.getElementById("btn4");
				button.disabled = false;
			}
		}
		//查看报表数据
		function viewReport(repInId,templateId,versionId,curId,repFreqId,year,term,day,orgId){
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
	</SCRIPT>
</head>
<%
	com.cbrc.smis.system.cb.InputData.isOnLine = null;
		com.cbrc.smis.system.cb.InputData.messageInfo = "";
%>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<label id="prodress" style="display:none">
			<span class="txt-main" style="color:#FF3300">正在校验报表,请稍后......</span>
		</label>
  	<label id="prodress1" >
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表处理 &gt;&gt; 报表上传
			</td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/viewNXDataReport.do" method="post" styleId="frm" onsubmit="return _submit(this)">
		<html:hidden property="orgId"/>
		<html:hidden property="bak1"/>
			<tr>
				<td>
				<fieldset id="fieldset">
				<br/>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>
							<td height="25">
									&nbsp;报表编号：
									<html:text property="templateId" maxlength="10" size="10" styleClass="input-text"/>
								</td>
								
								
								<td height="25" align="left">
									报表名称：
									<html:text property="repName" size="23" styleClass="input-text" />
									
								</td>
								<td height="25" align="left">
								报送频度：
								<html:select property="repFreqId">
									<html:option value="-999">--全部--</html:option>
									<html:optionsCollection name="utilForm" property="repFreqs" label="label" value="value" />
								</html:select>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;报表时间：
									<html:text property="date" readonly="true"  size="10"  styleId="date" style="text" onclick="return showCalendar('date', 'y-mm-dd');"/>
									<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('date', 'y-mm-dd');">									
								</td>
								<!-- 
								<%if (!reportFlg.equals("1")) {%>
								<td height="25" align="left">
									报表类型：
									<html:text property="templateTypeName" styleId="selectedTypeName" size="10" readonly="true" style="width:150px;cursor:hand" onclick="return showTree()" styleClass="input-text" ></html:text>
									<div id="orgTree" style="left:316px;top:70px;width:200px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="templateTypeTree" filter="false"/>
									    var tree= new ListTree("orgTree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
								      	tree.init();
								 	</script>
								 	</div>										
								</td>
								<%}%>
								 -->
								 <td height="23" align="left">
									报表机构：
									<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text"></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
								 	</script>
								 	</div>
									
								</td>
							<td align="left">
								<html:submit styleClass="input-button" value=" 查 询 " />&nbsp;&nbsp;&nbsp;&nbsp;
								<!-- <input class="input-button" onclick="_view_PLBS()" type="button" value="批量报送">&nbsp;&nbsp;	 -->
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
	<br/>	
	<table border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
		<logic:present name="Records" scope="request">
			<tr>
				<td>
					<input class="input-button" onclick="_validate_Select()" type="button" value="校验选中" id="btn1">
					<input class="input-button" onclick="_validate_All()" type="button" value="校验全部" name="btn2">
					&nbsp;&nbsp;∷&nbsp;&nbsp;
					<input class="input-button" onclick="_report_Select()" type="button" value="报送选中" name="btn3">
					<input class="input-button" onclick="_report_All()" type="button" value="报送全部" name="btn4">
					<%--&nbsp;&nbsp;∷&nbsp;&nbsp;
					<input class="input-button" onclick="_buling(this)" type="button" value="激活补零" id="bulingBTN">
					<input class="input-button" onclick="_buling_Select()" type="button" value="补零选中" id="bulingBTN">
					<input class="input-button" onclick="_buling_All()" type="button" value="全部补零">--%>
				</td>
			</tr>
		</logic:present>
		<tr>
			<td>
				<br/>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<tr class="titletab">
						<th align="center" colspan="11">
							数据报送
						</th>
					</tr>
					<TR class="middle">
						<td width="4%" align="center" valign="middle">
							<input type="checkbox" name="chkAll" onclick="_selAll()">
						</td>
						<td align="center" valign="middle"  width="5%">
							编号
						</td>
						<td align="center" valign="middle"  width="33%">
							报表名称
						</td>
						<!-- 
						<td align="center" valign="middle" width="5%" NOWRAP>
							版本号
						</td>
						 -->
						<td align="center" valign="middle" width="10%">
							机构
						</td>
						<td align="center" valign="middle" width="6%">
							币种
						</td>
						<td align="center" valign="middle" width="3%" NOWRAP>
							频度
						</td>
						<td align="center" valign="middle" width="9%">
							报表时间
						</td>
						<Td align="center" valign="middle" width="20%">
							状态(操作)
						</Td>

					</TR>
					<%
						int i = 0;
					%>
					<logic:present name="Records" scope="request">
						<logic:iterate id="viewReportIn" name="Records">							
							<TR bgcolor="#FFFFFF">
								<TD align="center">
									<!--
									对数据库中已经有记录的报表才需要进行校验
									库中已有的记录中审核不通过和审核通过的报表无需再作校验
									未填报的报表也不需要作校验
									-->
									<logic:notEmpty name="viewReportIn" property="repInId" >
										<logic:equal name="viewReportIn" property="checkFlag" value="-5">
											<%
												i++;
											%>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="5">
											--
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="4">
											<%
												i++;
											%>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="2">
											<%
												i++;
											%>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="-1">
											<%
												i++;
											%>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="1">
											--
										</logic:equal>
										<logic:equal name="viewReportIn" property="checkFlag" value="3">
											<%
												i++;
											%>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
										</logic:equal>	
										<logic:equal name="viewReportIn" property="checkFlag" value="0">
											--
										</logic:equal>
									</logic:notEmpty>
									<%--未填报，无报表--%>
									<logic:empty name="viewReportIn" property="repInId">
											<%
												i++;
											%>
											<input type="checkbox" name="chk<%=i%>" value="<bean:write name='viewReportIn' property='repInId'/>">
									
									</logic:empty>
									
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="childRepId" />
								</TD>
								<TD align="center">
									<logic:notEmpty name='viewReportIn' property='repInId'>
										<a href="javascript:viewReport('<bean:write name='viewReportIn' property='repInId'/>',
									 								'<bean:write name='viewReportIn' property='childRepId'/>',
									 								'<bean:write name='viewReportIn' property='versionId'/>',
									 								'<bean:write name='viewReportIn' property='curId'/>',
									 								'<bean:write name='viewReportIn' property='actuFreqID'/>',
									 								'<bean:write name='viewReportIn' property='year'/>',
									 								'<bean:write name='viewReportIn' property='term'/>',
									 								'<bean:write name='viewReportIn' property='day'/>',
									 								'<bean:write name='viewReportIn' property='orgId'/>')">
											<bean:write name="viewReportIn" property="repName" />
										</a>
									</logic:notEmpty>
									<logic:empty name='viewReportIn' property='repInId'>	
										<bean:write name="viewReportIn" property="repName" />
									</logic:empty>
								</TD>
								<!-- 
								<TD align="center">
									<bean:write name="viewReportIn" property="versionId" />
								</TD>
								 -->
								<TD align="center">
									<bean:write name="viewReportIn" property="orgName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="currName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="actuFreqName" />
								</TD>
								<TD align="center">
									<bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />-<bean:write name="viewReportIn" property="day" />
								</TD>
								<TD align="center" colspan="2">

									<!-- 已提交复核 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="0">已报送</logic:equal>
									<!-- 审核通过 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="1">
										<font color="#00CC00">合格</font>
									</logic:equal>
									<!-- 已提交审核 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="5">已审核</logic:equal>
									<!-- 复核不通过 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="-5">
										<a href="javascript:why('<bean:write name="viewReportIn" property="why"/>')"
											title='<bean:write name="viewReportIn" property="why"/>'><font color="#FF0000">审核不合格</font> </a>
									<logic:notEqual name="viewReportIn" property="reportStyle" value="2">										
										<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='day' />',
										'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="修改">
									</logic:notEqual>
									<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
									'<bean:write name="viewReportIn" property="versionId"/>',
									'<bean:write name="viewReportIn" property="year" />',
									'<bean:write name="viewReportIn" property="term" />',
									'<bean:write name="viewReportIn" property="day" />',
									'<bean:write name='viewReportIn' property='curId' />',
									'<bean:write name='viewReportIn' property='actuFreqID' />',
									'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="离线">
								
									
									</logic:equal>
									
									<!-- 审核不通过 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="-1">
										<a href="javascript:why('<bean:write name="viewReportIn" property="why"/>')"
											title='<bean:write name="viewReportIn" property="why"/>'><font color="#FF0000">审核不合格</font> </a>
									<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
										<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='day' />',
										'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="修改">										
									</logic:notEqual>
									<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name="viewReportIn" property="day" />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
 									   	'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="离线">
									
									
									</logic:equal>

									<!-- 未填报 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="3">									
										未报送
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
	 									   	<input class="input-button" onclick="editExcel('<bean:write name="viewReportIn" property="childRepId"/>',
	 									   					'<bean:write name="viewReportIn" property="versionId"/>',
	 									   					'<bean:write name='viewReportIn' property='curId' />',
	 									   					'<bean:write name='viewReportIn' property='actuFreqID' />',
	 									   					'<bean:write name="viewReportIn" property="year" />',
	 									   					'<bean:write name="viewReportIn" property="term" />',
	 									   					'<bean:write name='viewReportIn' property='day' />',
	 									   					'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="在线"> 
										</logic:notEqual>
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='day' />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
 									   	'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="离线">
									
									</logic:equal>
									<!-- 未校验 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="4">	
										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="0">
											<font color="#CD853F">表无数据</font>
										</logic:equal>
										<logic:notEqual name="viewReportIn" property="tblInnerValidateFlag" value="0">
											<font color="#FF0000">未校验</font>
										</logic:notEqual>
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
											<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="修改">
										</logic:notEqual>
										<input class="input-button" onclick="_view_sjbs('<bean:write name="viewReportIn" property="repInId"/>',										
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name="viewReportIn" property="day" />',
										'<bean:write name='viewReportIn' property='curId' />')" type="button" value="报送">
										
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='day' />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
 									   	'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="离线">
											
									</logic:equal>
									<!-- 已校验 -->
									<logic:equal name="viewReportIn" property="checkFlag" value="2">

										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="-1">
											<a href="javascript:_view_XSYY(<bean:write name='viewReportIn' property='repInId'/>)"><font color="#FF0000">校验不通过</font>
											</a>
										</logic:equal>
										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="0">
											<font color="#CD853F">表无数据</font>
										</logic:equal>
										<logic:equal name="viewReportIn" property="tblInnerValidateFlag" value="1">
											<logic:notEmpty  name="viewReportIn" property="tblOuterValidateFlag" >
												<logic:equal name="viewReportIn" property="tblOuterValidateFlag" value="-1">
													<a href="javascript:_view_XSYY(<bean:write name='viewReportIn' property='repInId'/>)"><font color="#FF0000">校验不通过</font>
													</a>
												</logic:equal>
												<logic:notEqual name="viewReportIn" property="tblOuterValidateFlag" value="-1">
													校验通过
												</logic:notEqual>
											</logic:notEmpty>	
											<logic:empty  name="viewReportIn" property="tblOuterValidateFlag" >
												校验通过
											</logic:empty>							
										</logic:equal>
										<logic:empty  name="viewReportIn" property="tblOuterValidateFlag" >
											<logic:empty  name="viewReportIn" property="tblInnerValidateFlag" >											
													校验通过
											</logic:empty>
										</logic:empty>	
										<!--
										<logic:notEqual name="viewReportIn" property="compRepInId" value="0">
											<INPUT class="input-button" type="button" value="数据比对" onclick="check('<bean:write name="viewReportIn" property="repInId"/>','<bean:write name="viewReportIn" property="compRepInId"/>')">
										</logic:notEqual>
										-->
										<logic:notEqual name="viewReportIn" property="reportStyle" value="2">
											<input class="input-button" onclick="viewPdf('<bean:write name="viewReportIn" property="repInId"/>',
											'<bean:write name="viewReportIn" property="year" />',
											'<bean:write name="viewReportIn" property="term" />',
											'<bean:write name='viewReportIn' property='day' />',
											'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="修改">
										</logic:notEqual>
										<input class="input-button" onclick="_view_sjbs('<bean:write name="viewReportIn" property="repInId"/>',										
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name="viewReportIn" property="day" />',
										'<bean:write name='viewReportIn' property='curId' />')" type="button" value="报送">
										<input class="input-button" onclick="_view_LSBS('<bean:write name="viewReportIn" property="childRepId"/>',
										'<bean:write name="viewReportIn" property="versionId"/>',
										'<bean:write name="viewReportIn" property="year" />',
										'<bean:write name="viewReportIn" property="term" />',
										'<bean:write name='viewReportIn' property='day' />',
										'<bean:write name='viewReportIn' property='curId' />',
										'<bean:write name='viewReportIn' property='actuFreqID' />',
 									   	'<bean:write name='viewReportIn' property='orgId'/>')" type="button" value="离线">

									</logic:equal>
								</TD>
							</TR>
						</logic:iterate>
					</logic:present>
					<input type="hidden" name="countChk" value="<%=i%>">
					<logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="11">
								暂无符合条件的记录
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>
	</table>
	<table cellSpacing="0" cellPadding="0" width="100%" border="0">
		<TR>
			<TD>
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../viewNXDataReport.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
	</label>
</body>
</html:html>
