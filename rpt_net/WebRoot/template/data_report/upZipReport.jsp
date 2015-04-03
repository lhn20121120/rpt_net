<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepReportPodedom = operator != null ? operator.getChildRepReportPopedom() : "";
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;
	String year = request.getAttribute("year") != null ? request.getAttribute("year").toString() : "";
	String term = request.getAttribute("month") != null ? request.getAttribute("month").toString() : "";
	String day = request.getAttribute("day") != null ? request.getAttribute("day").toString() : "";
	String url = request.getAttribute("url") != null ? request.getAttribute("url").toString() : "";

	String curpage =(String) request.getAttribute("curPage") != null ? request.getAttribute("curPage").toString() : "1";
	Integer systemSchemaFlag = Config.SYSTEM_SCHEMA_FLAG;
	String orgName = "";
	if(request.getAttribute("orgName")!=null && !request.getAttribute("orgName").toString().equals(""))
		orgName = request.getAttribute("orgName").toString();
	else
		orgName = operator != null ? operator.getOrgName() : "";
	
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" /> 
<jsp:setProperty property="childRepReportPodedom" name="utilSubOrgForm" value="<%=childRepReportPodedom%>" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
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
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">  
	  var BLJY = 'BN_VALIDATE_NOTPASS';
	   var BJJY = 'BJ_VALIDATE_NOTPASS';	 
	    var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    var requestParam = "<logic:present name='RequestParam'><bean:write name='RequestParam'/></logic:present>";	  
	  		
			
			function why(why){
		  	 	window.open ("<%=request.getContextPath()%>/template/data_report/tip.jsp?reason="+why,why, "height=250, width=250, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");		  	 	  	 		  	 	
		  	}
			function _sjbs(){
		     	window.location="<%=request.getContextPath()%>/template/intoTem.do?curPage=" + curPage + "&orgId=<%=selectOrgId%>";
		    }
		    function _SJJY(){
		     	window.location="<%=request.getContextPath()%>/report/dataLDJY.do?curPage=" + curPage + "&orgId=<%=selectOrgId%>";
		    }
		   	
		    function _submit2(form){
		    	//alert(form.formFile.value);
				if(form.formFile.value==""){
					alert("上传文件不能为空");
					form.formFile.focus();
					return false;
				}
				if(getExt(form.formFile.value)!=EXT_ZIP){
			 		alert("选择的报送文件必须是zip文件!");
			 		form.formFile.focus();
			 		return false;
			 	}
			 	if(form.orgName.value=="全部机构"){
			 		alert("请重新选择机构！");
			 		form.orgName.focus();
			 		return false;
			 	}
			 	
		
				return true;
				
			}
			
			
			//校验报表
		function validateReport(){
				
			var reportInIds = "";
			var countRep = document.getElementById("countRep").value;
			//alert(countRep);
			var obj = null;
			for(var i=1;i<=countRep;i++){
		  	  	obj = document.getElementById("repId"+i);			  	
			  	reportInIds+=(reportInIds==""?"":",") + obj.value;
		  	}
		  	//alert(reportInIds);
		  	try{
				var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReport.do?repInIds="+reportInIds; 
				var param = "radom="+Math.random();
				new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
		   	}catch(e){
		   		alert('系统忙，请稍后再试...！');
		   		return;
			}	  	
		}
		
			
	    //校验Handler
		function validateReportHandler(request){
			try{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'true'){
				  	alert('校验通过！');				     
				}else{
					 if(confirm('校验失败！是否需要查看校验信息?'))
				        window.open("<%=request.getContextPath()%>/report/viewMoreDataJYInfo.do?" + "repInIds=" + result,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
				}
			}catch(e){}
	    }
	    	    
	    //失败处理
	    function reportError(request){
	        alert('系统忙，请稍后再试...！');
	    }
	    
	    //报表报送 
		function sendReport(){
			if(confirm('确定报送该报表？')){
				var reportInIds = "";
				var countRep = document.getElementById("countRep").value;			
				var obj = null;
				for(var i=1;i<=countRep;i++){
			  	  	obj = document.getElementById("repId"+i);			  	
				  	reportInIds+=(reportInIds==""?"":",") + obj.value;
			  	}
			 	try{				  	
				  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineMoreReport.do?&repInIds=" + reportInIds ;
				    var param = "radom="+Math.random();
				   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}catch(e){
			   		alert('系统忙，请稍后再试...！');
			   	}
			}
		}
		
		//报送Handler
		function upReportHandler(request){
			try{
			//alert("adsfasdf");
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				//alert(result);
				if(result == 'true'){
					alert('报送成功！');	
					window.location="<%=request.getContextPath()%>/viewDataReport.do?"+requestParam; 
				}else{
				   var str = '';
				    if(result.indexOf(BLJY)>-1){
				       str+='表内';
				    }
				    if(result.indexOf(BJJY)>-1){
				       str+='表间';
				    }
					alert(str+"校验未通过，不能上报！");					
				}
			}catch(e){}
		}
		//返回
		
				//校验报表
	
	    
	    //失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
	    }
	    
	  
		
		 function viewPdf(repInId){
				 window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
			 }
		
			//返回
		function back()
		{
			//alert("requestParam="+requestParam);
			window.location.href= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
		}
		
		/* function showTree(){
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
		} */
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
		
	function search(){
		var orgName = document.getElementById("orgName1").value;
		//alert(orgName);
		 window.location="<%=request.getContextPath()%>/viewDataReport.do?orgName="+orgName+"&date1=<%=year %>-<%=term %>";
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
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 数据上报 &gt;&gt; 数据离线上报
			</td>
		</tr>
	</table>


	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor" align="center">
			<tr class="titletab">
				<th align="center" colspan="4">
					数据报送
				</th>
			</tr>

				<tr>
							<td colspan="2" ><font style="color: red;font-size: larger;font-weight: bold" >压缩包内不能包含多层目录，必须全部是excel文件</font></td>
				</tr>	
<tr bgcolor="#FFFFFF">
				<td>
					<html:form method="post" action="/template/uploadZipFile" enctype="multipart/form-data" onsubmit="return _submit2(this)" >
						<div align="center">
						
						报表机构：&nbsp;
						<html:text property="orgName" readonly="true" size="23" style="width:150px;cursor:hand" onclick="return showTree1()" styleClass="input-text" value="<%=orgName %>" styleId="orgName" ></html:text>
						<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
						<script type="text/javascript">
							<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
						    var tree1= new ListTree("orgpreTree", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
					      	tree1.init();
					 	</script>
					 	
					 	</div>&nbsp;&nbsp;
							<html:file property="formFile" size="80" styleClass="input-button" />
							<html:hidden property="orgId" value="<%=selectOrgId %>" name="orgId"/>
							<html:hidden property="year" value="<%=year %>" name="year"/>
							<html:hidden property="term" value="<%=term %>" name="term"/> 
							<html:hidden property="day" value="<%=day %>" name="day"/> 
							<html:hidden property="curPage" value="<%=curpage %>"/>
							<html:hidden property="url" value="<%=url %>"/>
							
							<html:submit styleClass="input-button" value=" 载 入 "  />
						</div>
					</html:form>
				</td>
			</tr>
		</table>
	</table>
	<p />
		<br />
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<tr>
			<td>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<TR class="titletab">
						<th width="8%" align="center" valign="middle">
							编号
						</th>
						<th width="30%" align="center" valign="middle">
							报表名称
						</th>
						<th width="10%" align="center" valign="middle">
							版本号
						</th>
						<th width="8%" align="center" valign="middle">
							币种
						</th>
						<th width="15%" align="center" valign="middle">
							报表口径
						</th>
						<th width="5%" align="center" valign="middle">
							频度
						</th>
						<th width="8%" align="center" valign="middle">
							报表时间
						</th>
						<th width="10%" align="center" valign="middle">
							机构
						</th>
						<Th width="10%" align="center" valign="middle">
							状态
						</Th>
					</TR>
					
					<%
						int i = 0;
					%>
				<logic:present name="Records" scope="request">
				<logic:iterate id="aditing" name="Records">	
				<%i++;%>
						<TR bgcolor="#FFFFFF">

							<TD align="center">
								<bean:write name="aditing" property="childRepId" />
							</TD>
							<logic:present name="notshow" scope="request">
								<TD align="center">
									<a href="javascript:viewPdf('<bean:write name="aditing" property="repInId"/>')"><bean:write name="aditing" property="repName" />
									</a>
								</TD>
							</logic:present>
							<logic:notPresent name="notshow" scope="request">
								<TD align="center">
									<bean:write name="aditing" property="repName" />
								</TD>
							</logic:notPresent>
							<TD align="center">
								<bean:write name="aditing" property="versionId" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="currName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="dataRgTypeName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="actuFreqName" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="year" />
								-
								<bean:write name="aditing" property="term" />
							</TD>
							<TD align="center">
								<bean:write name="aditing" property="orgName" />
							</TD>
					
								<TD align="center">
									<logic:equal name="aditing" property="checkFlag" value="0">
										<span class="txt-main" style="color:#FF3300">载入失败</span>
									</logic:equal>
									<logic:equal name="aditing" property="checkFlag" value="-1">未载入</logic:equal>
									<logic:equal name="aditing" property="checkFlag" value="3">载入成功</logic:equal>
								</TD>
							<input type="hidden" name="repId<%=i%>" value='<bean:write name="aditing" property="repInId"/>'>				
						</TR>

						</logic:iterate>
					</logic:present>
					<input type="hidden" name="countRep" value="<%=i%>">
					<logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="9">
								暂无符合条件的记录
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>

	</table>
<%-- 	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<TR>
			<TD>
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<logic:present name="notshow" scope="request">
					<tr>
						<td colspan="4">
							<DIV align="right">
									<input class="input-button" type="reset" value="校  验" onclick="validateReport()" />
								&nbsp;
								<input class="input-button" type="reset" value="报  送" onclick="sendReport()" />
								&nbsp;
								<INPUT class="input-button" id="back" type="button" value=" 返  回 " name="butBack" onclick="back()">
							</DIV>
						</td>
					</tr>
				</logic:present>
				
				<logic:notPresent name="notshow" scope="request">
					<tr>
						<td colspan="4">
							<DIV align="right">
							<%if(systemSchemaFlag ==0){%>
								<INPUT class="input-button" id="back" type="button" value=" 返  回 " name="butBack" onclick="back()">
							<%} %>
							</DIV>
						</td>
					</tr>
				</logic:notPresent>

		</TD>
		</TR>
	</table> --%>


</body>
<script>
	//block
	//   document.getElementById("isShowZT").style.display="none";
	</script>
</html:html>
