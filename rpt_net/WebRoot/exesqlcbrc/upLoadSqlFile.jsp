<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="java.util.List"%>
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
    //Integer systemSchemaFlag = Config.SYSTEM_SCHEMA_FLAG;
    String orgName = "";
    if(request.getAttribute("orgName")!=null && !request.getAttribute("orgName").toString().equals(""))
        orgName = request.getAttribute("orgName").toString();
    else
        orgName = operator != null ? operator.getOrgName() : "";
    
    List<String> timeList = (List<String>)request.getAttribute(Config.RECORDS);

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
<link href="<%=request.getContextPath() %>/css/common.css" type="text/css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/table.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/defTreeFormat.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/progressBar.js"></script>
		<link
			href="<%=request.getContextPath()%>/css/progressBar.css"
			rel="stylesheet" type="text/css" />


<SCRIPT language="javascript">
var BLJY = 'BN_VALIDATE_NOTPASS';
var BJJY = 'BJ_VALIDATE_NOTPASS';
var EXT_EXCEL="<%=configBean.EXT_EXCEL%>";
var EXT_ZIP="<%=configBean.EXT_ZIP%>";
var requestParam = "<logic:present name='RequestParam'><bean:write name='RequestParam'/></logic:present>";

window.onload=function()
{   
	//refreshOnTime();
}

//返回
function back()
{
    //alert("requestParam="+requestParam);
    window.location.href= "<%=request.getContextPath()%>/viewDataReport.do?"+requestParam;
}



function _submit2(form){
    //alert("from.action"+form.action);

    //alert(form.formFile.value);
    if(form.formFile.value==""){
        alert("上传文件不能为空");
        form.formFile.focus();
        return false;
    }

    showProcessBar();
    return true;
}


function oldZHUpload(){
    //alert("new"+document.getElementById("uploadForm").action);
    refreshOnTime();
    document.getElementById("uploadZForm").action="<%=request.getContextPath()%>/fitetlcbrc.do?method=importFile";

}

function downLoad(){
    //alert("new"+document.getElementById("uploadForm").action);
    refreshOnTime();
    document.getElementById("downLoadZForm").action="<%=request.getContextPath()%>/fitetlcbrc.do?method=downLoadFile";

}


function refreshOnTime(){ 
	
    //7秒后重复执行该函数 
    setInterval("doNothing()", 1000*60*1); 
} 

function doNothing()
{

	$.ajax({
        type: 'POST',
        url: '<%=request.getContextPath()%>/fitetlcbrc.do?method=getOverFlag',
        data: {},
        timeout: 10000,
        async:false,
        success: function (data) {
        	if(null!=data&&""!=data)
        	{
        		window.location="<%=request.getContextPath()%>/exesql/upLoadZipPac.jsp";
        	}
        }
    });

}


var progressBar = new ProgressBar("正在处理中。。。");
function showProcessBar(){
	setTimeout(function(){}, 1000*60*60*24);
  progressBar.show();
}
function closeProgressBar(){
		progressBar.close();
}

function goback()
{
	window.location="<%=request.getContextPath()%>/template/viewTemplate.do";
}
</SCRIPT>
<style type="text/css">
	#t1
	{
		width: 60%;
		margin:auto;
	}
</style>
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
            当前位置 &gt;&gt; 报表管理 &gt;&gt; sql文件上传
        </td>
    </tr>
</table>

<div class="tab_div2">
  <table width="100%" border="0" cellspacing="1" cellpadding="4" class="tab_content">
  <tr>
    <th align="left">请选择sql文件：</th>
    <th align="left">&nbsp;</th>
    </tr>
    
    <td height="50" align="right">sql执行文件：
          </td>
 <td> <div class="file-box"> 
                <html:form  styleId="uploadZForm" method="post" action="/fitetlcbrc" enctype="multipart/form-data"  onsubmit="return _submit2(this)">
                    <div align="left" style="padding-top: 15px">
                        <html:file property="formFile" size="30" styleClass="input-button" />
                        <html:submit onclick="oldZHUpload()" styleClass="input-button" value="载入"  />
                    </div>
                </html:form>
	  </div></td>
    </tr>
  </table>
</div>

<html:form action="/template/viewTemplate" method="POST">

							<TABLE id="t1" cellSpacing="1" cellPadding="4" width="30%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="8" align="center" id="list">
										<strong>
											备份列表
										</strong>
									</th>
								</tr>
								<TR class="middle">

									<TD class="tableHeader" width="30%">
										<b>备份时间</b>
									</td>
									<TD class="tableHeader" width="33%">
										<b>操作</b>
									</TD>
									
																	
								</TR>
									<%
										if(!timeList.isEmpty())
										{
											for(String timeCell:timeList)
											{%>
												<tr>
											    	<td align="center" bgcolor="white"><%=timeCell%></td>
											    	<td align="center" bgcolor="white"><a href="<%=request.getContextPath()%>/fitetlcbrc.do?method=backUp&bakTime=<%=timeCell%>">还原</a></td>
												</tr>
											<%}
										}
										else
										{
										    %>
										    <tr bgcolor="#FFFFFF">
												<td colspan="8">
													暂无记录
												</td>
											</tr>
										    <%
										}
									%>
									<!--<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="8">
												暂无记录
											</td>
										</tr>
									</logic:notPresent>-->
								</TABLE>

	   
			</html:form>	
			
<table cellspacing="0" cellpadding="0" width="100%" border="0" class="mar1">
  <tr>
    <td align="center"><input type="button" class="buttonStyle1" value="返回" onClick="goback()"/></td>
  </tr>
</table>


<p />
<br />

</body>
</html:html>
