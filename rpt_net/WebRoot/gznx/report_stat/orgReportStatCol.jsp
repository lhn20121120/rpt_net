<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>

<html:html locale="true">
<head>
	<html:base />
	<title>�����������ͳ��</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
	<%
		String reportFlg = "0";
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
				reportFlg = (String) session
						.getAttribute(Config.REPORT_SESSION_FLG);
			}
			
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ path + "/";
	%>

	<script type="text/javascript">
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var curPage="<bean:write name='ApartPage' property='curPage'/>";
		</logic:present>
	    <logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
		   
	    //�鿴�¼�����
	    function viewsubOrg(orgId){
	    	if($('img_'+orgId).src.indexOf('add.gif')>0){     		
				$('tr_'+orgId).style.display="";
     	 		$('img_'+orgId).src="<%=request.getContextPath()%>/image/subtract.gif";
     	 	}else {
				$('tr_'+orgId).style.display="none";
				$('img_'+orgId).src="<%=request.getContextPath()%>/image/add.gif";
     	 	}
	    }
	    
	    var reportFlg = <%=reportFlg %>;
	    
	    //�鿴����ͳ����ϸ
	    function  _viewOrgReportStat(date,orgId,orgName){
	    	//var objFrm=document.forms['frm'];
	    	//var date = document.getElementById("date");
	    	var year = date.substring(0,4);
	    	var term = date.substring(5,7);
	    	
	    	if(reportFlg==1){
	    		var qry="orgId=" + orgId +
	    			"&orgName=" + orgName + 
	    		//	"&year=" + objFrm.elements['year'].value +
	    		//	"&term=" + objFrm.elements['term'].value
	    			"&year=" + year +
	    			"&term=" + term;
	    			
	    		window.open("<%=request.getContextPath()%>/searchDataStat.do?" + qry,'�������','scrollbars=yes,height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
	    		
	    	}else{
	    	
	    		var qry="orgId=" + orgId +
	    			"&orgName=" + orgName + 
	    			"&date=" + date;
	    			
	    		window.open("<%=request.getContextPath()%>/searchReportStat.do?" + qry,'�������','scrollbars=yes,height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
	    	}
	    }
	    
	    //�鿴����ͳ����ϸ
	    function  _viewOrgLATERREPReportStat(date,orgId,orgName){
	    	//var objFrm=document.forms['frm'];
	    	//var date = document.getElementById("date");
	    	var year = date.substring(0,4);
	    	var term = date.substring(5,7);
	    	
	    	if(reportFlg==1){
	    		var qry="orgId=" + orgId +
	    			"&orgName=" + orgName + 
	    		//	"&year=" + objFrm.elements['year'].value +
	    		//	"&term=" + objFrm.elements['term'].value
	    			"&year=" + year +
	    			"&term=" + term;
	    			
	    		window.open("<%=request.getContextPath()%>/searchDataStat.do?" + qry+"&searchType=laterRep",'�������','scrollbars=yes,height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
	    		
	    	}else{
	    	
	    		var qry="orgId=" + orgId +
	    			"&orgName=" + orgName + 
	    			"&date=" + date;
	    			
	    		window.open("<%=request.getContextPath()%>/searchReportStat.do?" + qry+"&searchType=laterRep",'�������','scrollbars=yes,height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
	    	}
	    }
	    
	    //�鿴����ͳ����ϸ
	    function  _viewOrgREPReportStat(date,orgId,orgName){
	    	//var objFrm=document.forms['frm'];
	    	//var date = document.getElementById("date");
	    	var year = date.substring(0,4);
	    	var term = date.substring(5,7);
	    	
	    	if(reportFlg==1){
	    		var qry="orgId=" + orgId +
	    			"&orgName=" + orgName + 
	    		//	"&year=" + objFrm.elements['year'].value +
	    		//	"&term=" + objFrm.elements['term'].value
	    			"&year=" + year +
	    			"&term=" + term;
	    			
	    		window.open("<%=request.getContextPath()%>/searchDataStat.do?" + qry+"&searchType=repRep",'�������','scrollbars=yes,height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
	    		
	    	}else{
	    	
	    		var qry="orgId=" + orgId +
	    			"&orgName=" + orgName + 
	    			"&date=" + date;
	    			
	    		window.open("<%=request.getContextPath()%>/searchReportStat.do?" + qry+"&searchType=repRep",'�������','scrollbars=yes,height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
	    	}
	    }
	    // add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
	</script>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<label id="prodress1">
		<table border="0" width="98%" align="center">
			<tr>
				<td height="4"></td>
			</tr>
			<tr>
				<td>
					��ǰλ�� &gt;&gt; �����ѯ &gt;&gt; ����ͳ��
				</td>
			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</table>
		<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
			<html:form method="post" styleId="frm" action="/reportStatisticsCollect.do">
				<tr>
					<td>
						<fieldset id="fieldset" >
							<br />
							<table cellspacing="0" cellpadding="0" border="0" width="98%"
								align="center">
								<tr>
									<td align="left">
										�������ƣ�
										<html:text property="org_name" size="20"
											styleClass="input-text" />
									</td>
									<td height="25" align="left">&nbsp;
										��ʼʱ�䣺
										<html:text property="startDate" styleClass="input-text" size="10" styleId="date1"
												readonly="true" onclick="return showCalendar('date1', 'y-mm');" />
										<img border="0" src="<%=basePath%>image/calendar.gif"
												onclick="return showCalendar('date1', 'y-mm');">
									</td>
									<td height="25" align="left">&nbsp;
										����ʱ�䣺
										<html:text property="endDate" styleClass="input-text" size="10" styleId="date2"
												readonly="true" onclick="return showCalendar('date2', 'y-mm');" />
										<img border="0" src="<%=basePath%>image/calendar.gif"
												onclick="return showCalendar('date2', 'y-mm');">
									</td>
									<%--
									<td align="left">
										������
										<html:select property="condition">
											<option value="" selected="selected">--ȫ��--</option>					
											<option value="1">�ѱ�</option>					
											<option value="2">�����</option>
											<option value="3">�ر�</option>
											<option value="4">�ٱ�</option>
										</html:select>
									</td>
									 --%>
									<td>
										<input class="input-button" type="submit" name="Submit"
											value="��  ѯ">
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</html:form>
		</table>
		<br />
		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					�����������ͳ��
				</th>
			</tr>
			<tr>
			<TR class="middle">
				<TD align="center" width="8%">
					<strong>����</strong>
				</TD>
				<TD align="center" width="10%">
					<strong>�������</strong>
				</TD>
				<TD align="center" width="20%">
					<strong>��������</strong>
				</TD><!-- 
				<TD align="center" width="15%">
					<strong>�������</strong>
				</TD> -->
				<TD align="center" width="10%">
					<strong>Ӧ������</strong>
				</TD>
				<TD align="center" width="10%">
					<strong>���ϴ�����</strong>
				</TD>
				<%-- if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {%>
					<TD align="center" width="10%">
						<strong>�Ѹ�������</strong>
					</TD>
					<TD align="center" width="10%">
						<strong>����ǩ����</strong>
					</TD>
				<% } else {--%>
					<TD align="center" width="10%">
						<strong>���������</strong>
					</TD>
				<%-- }--%>
				<TD align="center" width="10%">
					<strong>�ر�����</strong>
				</TD>
				<TD align="center" width="10%">
					<strong>�ٱ�����</strong>
				</TD>
				<!--<TD align="center" width="10%">
					<strong>©������</strong>
				</TD> -->
				<TD align="center" width="10%">
					<strong>����</strong>
				</TD>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records">
					<TR bgcolor="#ffffff" onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
						<TD align="center">
							<bean:write name="item" property="date" />
						</TD>
						<TD  align="center">
							<bean:write name="item" property="org_id" />
						</TD>
						<TD  align="center">
							<bean:write name="item" property="org_name" />
						</TD>
						<%-- 
						<TD bgcolor="#ffffff" align="center">
							<bean:write name="item" property="org_type_name" />
						</TD> --%>
						<TD  align="center">
							<bean:write name="item" property="ybReportNum" />
						</TD>
						<TD  align="center">
							<bean:write name="item" property="bsReportNum" />
						</TD>
						<%-- if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {%>
							<TD bgcolor="#ffffff" align="center">
								<bean:write name="item" property="fhReportNum" />
							</TD>
							<TD bgcolor="#ffffff" align="center">
								<bean:write name="item" property="sqReportNum" />
							</TD>
						<% } else {--%>
							<TD  align="center">
								<bean:write name="item" property="sqReportNum" />
							</TD>
						<%-- }--%>
						
						<TD  align="center">
							<a href="javascript:;" style="color:blue;text-decoration:underline;"
							 onclick="_viewOrgREPReportStat('<bean:write name="item" property="date" />',
													'<bean:write name="item" property="org_id" />',
													'<bean:write name="item" property="org_name" />')"><bean:write name="item" property="cbReportNum" /></a>
						</TD>
						<TD  align="center">
							<a href="javascript:;" style="color:blue;text-decoration:underline;"
							 onclick="_viewOrgLATERREPReportStat('<bean:write name="item" property="date" />',
													'<bean:write name="item" property="org_id" />',
													'<bean:write name="item" property="org_name" />')"><bean:write name="item" property="zbReportNum" /></a>
						</TD>
						<%--
						<TD bgcolor="#ffffff" align="center">
							<bean:write name="item" property="lbReportNum" />
						</TD> --%>
						<TD  align="center">
							<input type="button" class="input-button" value="�鿴��ϸ"
						onclick="_viewOrgReportStat('<bean:write name="item" property="date" />',
													'<bean:write name="item" property="org_id" />',
													'<bean:write name="item" property="org_name" />')">
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
		<table cellSpacing="1" cellPadding="4" width="98%" border="0">
			<tr>
				<TD colspan="7" bgcolor="#FFFFFF">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../../reportStatisticsCollect.do" />
					</jsp:include>
				</TD>
			</tr>
		</table>
</body>
</html:html>
