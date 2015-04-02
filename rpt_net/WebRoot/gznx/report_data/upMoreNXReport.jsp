<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);	
	String orgId = operator != null ? operator.getOrgId() : "";
	String selectOrgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : orgId;

	String date = request.getParameter("date") != null ? request.getParameter("date").toString() : request.getAttribute("date").toString();
	
	String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage").toString():"1";
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//任务模式
	Integer systemSchemaFlag = Config.SYSTEM_SCHEMA_FLAG;
%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
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
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>

	<SCRIPT language="javascript">	
	   var BLJY = 'BN_VALIDATE_NOTPASS';
	   var BJJY = 'BJ_VALIDATE_NOTPASS';	 
	    var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";	    	
	    var EXT_ZIP="<%=configBean.EXT_ZIP%>";
	    var requestParam = "date=<%=date%>&curPage=<%=curPage%>";  	
	   	var reportInId = null;	   	
			
		function _submit(form){
			if(form.date.value==""){
				alert("请输入报表时间!");
				form.date.focus();
				return false;
			}
		}
		
		function _submit2(form){			
			if(form.formFile.value==""){
				alert("上传文件不能为空");
				form.formFile.focus();
				return false;
			}
			if(getExt(form.formFile.value)!=EXT_ZIP){
			 	alert("选择的报送文件必须是Zip包!");
			 	form.formFile.focus();
			 	return false;
			}			
			var date_kj = document.getElementById("date");	
			var version_kj = document.getElementById("versionId");	
			version_kj.value = date_kj.value + "_<%=selectOrgId%>_" + <%=curPage%>;
			prodress1.style.display = "none" ;
		    prodress.style.display = "" ;
			return true;
		}
						
		//校验报表
		function validateReport(){
			var reportInIds = "";
			var countRep = document.getElementById("countRep").value;			
			var obj = null;
			for(var i=1;i<=countRep;i++){
		  	  	obj = document.getElementById("repId"+i);			  	
			  	reportInIds+=(reportInIds==""?"":",") + obj.value;
		  	}
		  	try{
				var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReportNX.do?repInIds="+reportInIds; 
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
				        window.open("<%=request.getContextPath()%>/report/viewMoreValidateInfo.do?" + "repInIds=" + result,'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
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
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				
				if(result == 'true'){
					alert('报送成功！');	
					window.location="<%=request.getContextPath()%>/viewNXDataReport.do?"+requestParam; 
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
		function back()
{
			window.location.href= "<%=request.getContextPath()%>/viewNXDataReport.do?"+requestParam;
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
	
	<label id="prodress" style="display:none">
			<span class="txt-main" style="color:#FF3300">正在批量载入，请稍后......</span>
		</label>
  <label   id="prodress1" >
	<table border="0" width="90%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 报表处理 &gt;&gt; 数据上报 &gt;&gt; 批量报送
			</td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<html:form action="/viewNXDataReport.do" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
				<fieldset id="fieldset">
				<br/>
					<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
						<tr>							
							<td>
								&nbsp;报表时间：
								<html:text property="date" styleClass="input-text" size="10"
									styleId="date1" readonly="true"
									onclick="return showCalendar('date1', 'y-mm-dd');" />
								<img border="0" src="<%=basePath%>image/calendar.gif"
									onclick="return showCalendar('date1', 'y-mm-dd');">
							</td>															
							<td>
								<html:submit styleClass="input-button" value=" 查 询 " />
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
		<br/>
		<table cellSpacing="1" cellPadding="4" border="0" width="90%" class="tbcolor" align="center">
			<tr class="titletab"><th align="center" colspan="4">数据报送（批量）</th></tr>
			<tr bgcolor="#FFFFFF">
				<td width="20%">
					<html:form method="post" action="/report/uploadMoreFileNX" enctype="multipart/form-data" onsubmit="return _submit2(this)">
						<div align="center">
							<html:file  property="formFile" size="80" styleClass="input-button"/>
							<html:hidden property="versionId"/>
							<html:submit styleClass="input-button" value="载入"/>
                        </div>
                    </html:form>
                </td>
            </tr>
		</table>
	</table>
	<p/><br/>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<tr>
			<td>
				<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
					<TR class="titletab">
						<th width="10%" align="center" valign="middle">编号</th>
						<th width="33%" align="center" valign="middle">报表名称</th>
						<th width="10%" align="center" valign="middle">版本号</th>
						<th width="10%" align="center" valign="middle">币种</th>
						<%--<th width="15%" align="center" valign="middle">报表口径</th>--%>
						<th width="5%" align="center" valign="middle">频度</th>
						<th width="9%" align="center" valign="middle">报表时间</th>							
						<Th width="11%" align="center" valign="middle">状态</Th>
					</TR>
					<%
						int i = 0;
					%>
					<logic:present name="Records" scope="request">
						<logic:iterate id="viewReportIn" name="Records">
							<%i++;%>
							<TR bgcolor="#FFFFFF">					
								<TD align="center"><bean:write name="viewReportIn" property="childRepId" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="repName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="versionId" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="currName" /></TD>
								<%-- <TD align="center"><bean:write name="viewReportIn" property="dataRgTypeName" /></TD>--%>
								<TD align="center"><bean:write name="viewReportIn" property="actuFreqName" /></TD>
								<TD align="center"><bean:write name="viewReportIn" property="year" />-<bean:write name="viewReportIn" property="term" />-<bean:write name="viewReportIn" property="day" />
								</TD>
								<TD align="center">
									<logic:equal name="viewReportIn" property="checkFlag" value="0">
										<span class="txt-main" style="color:#FF3300">载入失败</span>
									</logic:equal>
									<logic:equal name="viewReportIn" property="checkFlag" value="3">载入成功</logic:equal>
								</TD>
								<input type="hidden" name="repId<%=i%>" value='<bean:write name="viewReportIn" property="repInId"/>'>										
							</TR>
						</logic:iterate>
					</logic:present>
					<input type="hidden" name="countRep" value="<%=i%>">
					<logic:notPresent name="Records" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="8">
								暂无符合条件的记录
							</td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>		
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
		<TR><TD>&nbsp;</TD></TR>
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
			</TD>
		</TR>
	</table>
	</lable>
</body>
</html:html>
