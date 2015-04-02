<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css">
		<link href="<%=request.getContextPath() %>/css/tree.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/defTreeFormat.js"></script>
		
		<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>
	<script language="javascript">
	
			 function history(){
			 	 
			 	 window.location="<%=request.getContextPath()%>/afreportmerger.do";
			 }
			 	  
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="<bean:write name='<%=Config.APART_PAGE_OBJECT%>' property='curPage' scope='request'/>";
		</logic:present>
		<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="0";
		</logic:notPresent>
		<!--
			/**
			 * ��ʽ����:����У��
			 */
			 var CELL_CHECK_INNER="1";
			/**
			 * ��ʽ����:���У��
			 */
			 var CELL_CHECK_INNER="2";
			/**
			 *�ָ���1
			 */
			 var SPLIT_SMYBOL_COMMA=",";
			 /**
			 *�ָ���2
			 */
			 var SPLIT_SMYBOL_ESP="&";
			 
			/**
			 * ��ϵ���ʽ�����ļ���׺
			 */
			 var EXT_TXT="txt";
			
			/**
			 * ������ڱ���ϵ���ʽ�¼�
			 */
			 function _load_gx(){
			 	 //openDialog("load_gx.jsp");
			 	 var objForm=document.forms['frmBJGX'];
			 	 
			 	 window.location="load_gx.jsp?" + 
			 	 	"childRepId=" + objForm.elements['childRepId'].value + 
			 	 	"&versionId=" + objForm.elements['versionId'].value + 
			 	 	"&reportName=" + objForm.elements['reportName'].value +
			 	 	"&reportStyle=" + objForm.elements['reportStyle'].value +
			 	 	"&curPage=" + objForm.elements['curPage'].value;
			 }
			 
			 /**
			  * ���ύ�¼�
			  */
			  function _submit(){
			  	var objCount=document.getElementById("rCount");
			  	var count=eval(objCount.value);
			  	var delCellFormuIds="";
			  	for(var i=0;i<count;i++){
			  		var objChk=eval("document.getElementById(\"chk" + i + "\")");
			  		if(objChk.checked==true)
			  			delCellFormuIds+=(isEmpty(delCellFormuIds)==true?"":SPLIT_SMYBOL_COMMA) + objChk.value;
			  	}
			  	
			  	if(isEmpty(delCellFormuIds)==true){
			  		alert("��ѡ��Ҫɾ���ı��ʽ!\n");
			  		return false;
			  	}
			  	if(confirm("��ȷ��Ҫɾ����ǰѡ�еı��ʽ��?\n")==false){
			  		return false;
			  	}
				
				document.forms['frmBJGX'].elements['cellFormuIds'].value=delCellFormuIds;
			  	return true;
			  }
			  
			  /**
			   * �����¼�
			   */
			   function _back(){
			   	 var form=document.forms['frmBJGX'];
			   	 window.location="/smis_in_net/template/viewMChildReport.do?" + 
			   	 	"curPage=" + form.elements['curPage'].value;
			   }
			   
		function toExcel(templateId,versionId){
		
			window.location="<%=request.getContextPath()%>/servlets/toReadExcelServlet?forflg=1&templateId=" + templateId+"&versionId="+versionId;
		}			   
		
		</script>
			<%
		
		String templateId=null,versionId=null;
		if(request.getParameter("templateId")!=null) templateId=(String)request.getParameter("templateId");
		if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");
		 %>
</head>
<body background="<%=request.getContextPath() %>/image/total.gif">
		<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
		</logic:present>
	<table border="0" cellspacing="0" cellpadding="4" width="80%" align="center">		
		<tr><td height="8"></td></tr>
		<tr>
			<td>
				��ǰλ�� >> ���ݷ���
			</td>
		</tr>
		<tr><td height="10"></td></tr>		
		<tr>
			<td>
				<fieldset id="fieldset">
					<div id="server">
					<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="100%" class="bgcolor" align="center">
						<tr class="titletab">
							<th colspan="4" align="center">
								<strong>									
										���ڻ����ʲ���ծ��Ŀ�±�������ҽ�ת�� 
								</strong>
							</th>
						</tr>
						<tr>
							<td>��Ԫ�����ƣ�</td>
							<td colspan="3">�ʲ�|�����</td>
						</tr>
						<tr>
							<td>����SQL��</td>
							<td colspan="3" align="left" style="word-break: break-all;padding:5px 0px 5px 0px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;font-size: 14px " contentEditable=true>
											select curr_deb_val from e_accounting where vs_id in(10001,10002,10003,10004,10005,10006,10007,10008,10009,10010)
										</div>
										
							</td>
						</tr>
						<tr>
							<td class="tableHeader" width="12%">
								���
							</td>
							<td class="tableHeader" width="20%">
								��Ŀ
							</td>
							<td class="tableHeader" align="center">
								��Ŀ����
							</td>
							<td class="tableHeader" align="center">
								�跽
							</td>
						</tr>
						<%
						int vsid=10000;
						for(int i=0;i<10;i++){ 
							vsid++;
							double numD = Math.random()*1000000;
							String num = String.valueOf(numD);
							double numD_2 = Math.random()*1000000;
							String num2 = String.valueOf(numD_2);
						%>
						<tr>
							<td align="center"><%=i %></td>
							<td align="center"><%=vsid%></td>
							<td align="center">��Ŀ����_<%=i %></td>
							<td align="center"><%=num.substring(0,num.indexOf(".")+3)%></td>
						</tr>
						<%} %>	
					</table>
					</div>
				</fieldset>
			</td>
		</tr>

	</table>
	<script>
function exportFunc()
{
  var o=document.getElementById("server");
  var win=window.open("");
  win.document.open();
  win.document.charset="gb2312";
  win.document.write(o.innerHTML);
  win.document.execCommand("saveas",true,"SF2101_081.txt");
  win.opener=null;
  win.close();
}
function exportFunc1()
{
  document.execCommand("saveas",true,"SF2101_081.txt");
}

</script>
</body>
</html:html>

