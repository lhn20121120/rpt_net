<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.cbrc.smis.security.Operator,com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.other.Expression"%>
<%
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String childRepCheckPodedom = operator != null ? operator.getChildRepCheckPodedom() 
				+" and viewOrgRep.childRepId in (select tmpl.id.templateId from AfTemplate tmpl where tmpl.templateType=" + reportFlg +")" : "";
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="childRepCheckPodedom" name="utilSubOrgForm" value="<%=childRepCheckPodedom%>"/>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="orgPodedom" name="FormBean" value="<%=childRepCheckPodedom%>"/>
<html:html locale="true">
	<head>
		<html:base/>		
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">		
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
		<script language="javascript">
			/**
			 * �����ر��¼�
			 */
			 function _add(){
				location.href="<%=request.getContextPath()%>/report/repSearch.do";
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
		</script>
	</head>
<jsp:include page="../../calendar.jsp" flush="true">
  <jsp:param name="path" value="../../"/>
</jsp:include>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
		<html:form action="/selForseReportAgain" method="post">
		<html:hidden property="orgId"/>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
			<tr>
				<td height="5"></td>
			</tr>
			<tr>
				 <td>��ǰλ�� >> 	������ >> �����ر�</td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
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
									���������
									<html:text property="orgName" readonly="true" size="25" style="width:150px;cursor:hand" onclick="return showTree1()" style="input-text" ></html:text>
									<div id="orgpreTree" style="left:316px;top:70px;width:150px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
									<script type="text/javascript">
										<bean:write  name="FormBean"  property="orgReportPodedomTree" filter="false"/>
									    var tree1= new ListTree("tree1", TREE2_NODES,DEF_TREE_FORMAT,"","treeOnClick1('#KEY#','#CAPTION#');");
								      	tree1.init();
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
								<td>
									<input type="submit" class="input-button" value=" �� ѯ ">&nbsp;&nbsp;
									<input type="button" class="input-button" value="�����ر�" onclick="_add()">
								</td>
							</tr>
							<tr>
								<td height="3"></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
			
		</table>
		</html:form>
		<table cellSpacing="1" cellPadding="4" width="98%" border="0"  class="tbcolor">
			<tr>
				<th width="6%" align="center">���</th>
				<th width="22%" align="center">��������</th>
				<th width="6%" align="center">�汾��</th>
				<th width="10%" align="center">���ͻ���</th>
				<th width="10%" align="center">����ھ�</th>
				<th width="8%" align="center">����</th>
				<th width="6%" align="center">����Ƶ��</th>
				<th width="9%" align="center">����ʱ��</th>				
				<th width="9%" align="center">�ر��趨ʱ��</th>
				<th width="13%" align="center">�ر�ԭ��</th>
			</tr>
			<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records">
			    <TR>
			      <TD bgcolor="#ffffff" align="center">
			      	<bean:write name="item" property="childRepId"/>
			      </TD>
				  <TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="repName"/>
				  </TD>
				  <TD bgcolor="#ffffff" align="center">
			      	<bean:write name="item" property="versionId"/>
			      </TD>
			      <TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="orgName"/>
				  </TD>
				  <TD bgcolor="#ffffff" align="center">
					<bean:write name="item" property="dataRangeDes"/>
				  </TD>
			      <TD bgcolor="#ffffff" align="center">
			      	<bean:write name="item" property="currName"/>
			      </TD>
			      <TD bgcolor="#ffffff" align="center">
			      	<bean:write name="item" property="actuFreqName"/>
			      </TD>
				  <TD bgcolor="#ffffff" align="center">
			      	<bean:write name="item" property="reportDate"/>
			      </TD>
				  <TD bgcolor="#ffffff" align="center"> 
				  	<bean:write name="item" property="setDateStr"/>
				  </TD>
				  <TD bgcolor="#ffffff" align="center">
				  	<bean:write name="item" property="cause"/>
				  </TD>
				</TR>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="10">
						��ƥ���¼
				</td>
			</tr>
		</logic:notPresent>			
		</table>
		<TABLE  cellSpacing="0" cellPadding="0" width="98%" border="0">
			<TR>
				<TD>
					<jsp:include page="../../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value="../../selForseReportAgain.do"/>
				  	</jsp:include>
				</TD>
			</tr>
		</TABLE>
		</body>
</html:html>