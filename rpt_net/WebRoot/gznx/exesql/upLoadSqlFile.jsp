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
<link href="<%=Config.WEBROOTULR%>/css/common.css" type="text/css" rel="stylesheet">
<link href="<%=Config.WEBROOTULR%>/css/table.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=Config.WEBROOTULR%>/js/func.js"></script>
<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
<script type="text/javascript" src="<%=Config.WEBROOTULR%>/js/tree/tree.js"></script>
<script type="text/javascript" src="<%=Config.WEBROOTULR%>/js/tree/defTreeFormat.js"></script>
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

//����
function back()
{
    //alert("requestParam="+requestParam);
    window.location.href= "<%=request.getContextPath()%>/afreportDefine.do?"+requestParam;
}



function _submit2(form){
    //alert("from.action"+form.action);

    //alert(form.formFile.value);
    if(form.formFile.value==""){
        alert("�ϴ��ļ�����Ϊ��");
        form.formFile.focus();
        return false;
    }

    showProcessBar();
    return true;
}


function oldZHUpload(){
    //alert("new"+document.getElementById("uploadForm").action);
    refreshOnTime();
    document.getElementById("uploadZForm").action="<%=request.getContextPath()%>/fitetl.do?method=importFile";

}

function downLoad(){
    //alert("new"+document.getElementById("uploadForm").action);
    refreshOnTime();
    document.getElementById("downLoadZForm").action="<%=request.getContextPath()%>/fitetl.do?method=downLoadFile";

}

function rollBakTable(bakTable){
    alert("��ԭ��ʼ");
    window.location.href="<%=request.getContextPath()%>/fitetl.do?method=rollBakTable&tableName=af_validateformula&bakTime="+bakTable;
}

function deleteBak(bakTable){
	if(confirm("��ȷ��Ҫɾ���ñ�������ô��"))
	window.location.href="<%=request.getContextPath()%>/fitetl.do?method=deleteBak&tableName=af_validateformula&bakTime="+bakTable;
}

function refreshOnTime(){ 
	
    //7����ظ�ִ�иú��� 
    setInterval("doNothing()", 1000*60*1); 
} 

function doNothing()
{
	$.ajax({
        type: 'POST',
        url: '<%=request.getContextPath()%>/fitetl.do?method=getOverFlag',
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


var progressBar = new ProgressBar("���ڴ����С�����");
function showProcessBar(){
	setTimeout(function(){}, 1000*60*60*24);
  progressBar.show();
}
function closeProgressBar(){
		progressBar.close();
}

function goback()
{
	window.location="<%=request.getContextPath()%>/afreportDefine.do";
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
            ��ǰλ�� &gt;&gt; ������� &gt;&gt; sql�ļ��ϴ�
        </td>
    </tr>
</table>

<div class="tab_div2">
  <table width="100%" border="0" cellspacing="1" cellpadding="4" class="tab_content">
  <tr>
    <th align="center" colspan="2">sql�ļ����룺</th>
 <tr>
    <td height="50" align="right">sqlִ���ļ���
          </td>
 <td> <div class="file-box"> 
                <html:form  styleId="uploadZForm" method="post" action="/fitetl" enctype="multipart/form-data"  onsubmit="return _submit2(this)">
                    <div align="left" style="padding-top: 15px">
                        <html:file property="formFile" size="30" styleClass="input-button" />
                        <html:submit onclick="oldZHUpload()" styleClass="input-button" value="����"  />
                    </div>
                </html:form>
	  </div></td>
    </tr>
  </table>
</div>


 <table border="0" cellspacing="1" cellpadding="4" class="tab_content" style="width:70% ">
  <tr>
    <th colspan="3" align="center" style="text-align: center;height:35px " ><span style="font-size:20px">��ʽ��ԭ�б�</span></th>
   </tr>
    <tr>
    <th align="center">
    	���
    </th>
    <th height="25" align="center">����ʱ�䣺
     </th>
 <th align="center"> 
	����
</th>
    </tr>
    
 <logic:present name="infoList" scope="request">
						<logic:iterate id="item" name="infoList" indexId="index">
							<tr bgcolor="#FFFFFF">
								<td align="center">
									<%=((Integer) index).intValue() + 1%>
								</td>
								<td align="center">
									<bean:write name="item" property="bakTime" />
								</td>
								<td align="center">
								<a href="javascript:rollBakTable(<bean:write name="item" property="bakTime" />)">
									��ԭ
								</a>
								<a href="javascript:deleteBak(<bean:write name="item" property="bakTime" />)">
									ɾ��
								</a>
									
									
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
					<logic:notPresent name="infoList" scope="request">
						<tr>
							<td bgcolor="#FFFFFF" colspan="3">
								��У�鹫ʽ
							</td>
						</tr>
					</logic:notPresent>
  </table>




<table cellspacing="0" cellpadding="0" width="100%" border="0" class="mar1">
  <tr>
    <td align="center"><input type="button" class="buttonStyle1" value="����" onClick="goback()"/></td>
  </tr>
</table>


<p />
<br />

</body>
</html:html>
