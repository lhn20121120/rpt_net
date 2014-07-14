<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<html:html locale="true">
<head>
	<html:base/>
	<title>
		��־����
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">
	<jsp:include page="../calendar.jsp" flush="true">
		<jsp:param name="path" value="../" />
	</jsp:include>
	<script language="javascript" src="../js/func.js"></script>
	<script type="text/javascript" src="../calendar/calendar.js"></script>
<!-- import the language module -->
<script type="text/javascript" src="../calendar/calendar-cn.js"></script>
<script language="javascript" src="../calendar/calendar-func.js"></script>
	 <script language="javascript">
	 	//ɾ��ǰ����
	  	function _alertDelete(){
      //----------------------------------------------------------
      //gongming 2008-07-25 ��дΪѡ�к�ſ���ɾ��
          var del_log = document.getElementsByTagName("INPUT");
          var del = false;
          for(var i = 0; i < del_log.length;i ++)
          {
            if(del_log[i].checked == true)
            {
              del = true;
              break;
            }
          }
          if(del)
          {
            if(confirm("��ȷ��Ҫɾ����Щ��־��")) 
                  return true;
               return false;
          }
          //------------------------------
          alert('��ѡ��Ҫɾ������־!');
          return false;	  		
         //--------------------------------
	  	}
	  	//�����ѯ������
	  	function _clearForm(){
	  		document.form1.userName.value ="";
	  		document.form1.startDate.value = "";
	  		document.form1.endDate.value = "";
	  		document.form1.operation.value="";
	  		document.form1.logTypeId.selectedIndex = 0;
	  	}
	  	//ȡ����һ�εĲ�ѯ����
	  	function _setQuery(){
	  		document.form1.userName.value = "<bean:write name='form' property='userName'/>";
	  		document.form1.operation.value ="<bean:write name='form' property='operation'/>";
	  		document.form1.startDate.value="<bean:write name='form' property='startDate'/>";
	  		document.form1.endDate.value="<bean:write name='form' property='endDate'/>";

	  		var listObj = document.form1.logTypeId;
	  		for(var i=0;i<listObj.length;i++)
	  		{
	  			if(listObj.options[i].value == "<bean:write name='form' property='logTypeId'/>")
	  			{		
	  				listObj.selectedIndex = i;
	  				break;
	  			}
	  		}

	  		//document.form1.logTypeId.selectedIndex ="<bean:write name='form' property='logTypeId'/>";
	  	}
	  	//ȫѡ
	  	function _selectAll(){
	  		for(var i =0;i<document.form2.deleteLogInId.length;i++)
	  		{
	  			document.form2.deleteLogInId[i].checked = true;
	  		}
	  		
	  	}
	  	//ȫȡ��
	  	function _cancelAll(){
	  		for(var i=0;i<document.form2.deleteLogInId.length;i++)
	  		{
	  			document.form2.deleteLogInId[i].checked = false;
	  		}
	  		
	  	}
	  	function select(form){
	  	   if(isEmpty(form.endDate.value)==false && isEmpty(form.startDate.value)==false){
			 		if(form.startDate.value>form.endDate.value){
			 			alert("�������ڲ���С����ʼ����!\n");
			 			return false;
			 	}	
			 }
	  	
	  	
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
 
		<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>��ǰλ�� >> ϵͳ���� >> ��־����</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<br>
	<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center" height="216">
	 
		<tr>
			<td height="5" colspan="2">
				<div align="center">
					<table width="94%" border="0">
						<html:form styleId="form1" action="/system_mgr/viewLogIn" method="Post" onsubmit="return  select(this)">
							<tr>
								<td align="left">
									��־���ͣ�
								</td>
								<td  align="left">
									<html:select property="logTypeId">
										<html:option value="0">ȫ����־</html:option>
										<html:optionsCollection name="utilForm" property="logTypes" label="label" value="value" />
									</html:select>
								</td>
								<td align="left">
									����Ա��
								</td>
								<td align="left">								
									<html:text property="userName" size="20" styleClass="input-text" style="text" />					
								</td>
								
							</tr>
							<tr>
								<td  align="left">
									��ʼʱ�䣺
								</td>
								<td  align="left">
									<html:text property="startDate" size="20" styleClass="input-text" style="text" readonly="true" /><html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');"/>
								</td>
								<td  align="left">
									����ʱ�䣺
								</td>
								<td align="left">
									<html:text property="endDate" size="20" styleClass="input-text" style="text" readonly="true" /><html:img border="0" src="../image/calendar.gif" onclick="return showCalendar('endDate', 'y-mm-dd');"/>
								</td>
								
							</tr>
                            <tr>
                                <td align="left">
                                        ��־���ݣ�
                                </td>
                             <td  align="left" colspan="2">
									<html:text property="operation" size="60" styleClass="input-text" style="text" />
								</td>
								<td align="left">
									<html:submit styleClass="input-button" value="��   ѯ"/>&nbsp;<html:button property="cancel" value="ȡ   ��" styleClass="input-button" onclick="return _clearForm()"/>
								</td>
                            </tr>
						</html:form>
					</table>
					<logic:present name="form" scope="request">
						<script language="javascript">
							_setQuery();
						</script>
					</logic:present>
				</div>
			</td>
		</tr>
		<tr>
			<td height="10" valign="top" colspan="2">
			</td>
		</tr>
		<tr>
			<td height="123" valign="top" colspan="2">
				<div align="center">

					<table width="94%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
						<tr class="titletab" id="tbcolor">
							<th colspan="5" align="center" id="list">
								<strong>
									ϵͳ��־�б�
								</strong>
							</th>
						</tr>
						<tr align="center" class="middle">
							<td width="4%"></td>
							<td width="15%">
								��־����
							</td>
							<td width="9%">
								����Ա
							</td>
							<td width="60%">
								��־����
							</td>
							<td width="11%">
								����ʱ��
							</td>
						</tr>
						<html:form action="/system_mgr/deleteLogIn" styleId="form2" method="POST" onsubmit="return _alertDelete()">
							<logic:present name="Records" scope="request">
								<logic:iterate id="item" name="Records">
									<tr align="center">
										<td bgcolor="#ffffff">
											<INPUT type="checkbox" name="deleteLogInId" value="<bean:write name="item" property="logInId"/>" />
										</td>
										<td bgcolor="#ffffff">
											<logic:notEmpty name="item" property="logType">
												<bean:write name="item" property="logType" />
											</logic:notEmpty>
											<logic:empty name="item" property="logType">
												δ֪
											</logic:empty>
										</td>
										<td bgcolor="#ffffff">
											<logic:notEmpty name="item" property="userName">
												<bean:write name="item" property="userName" />
											</logic:notEmpty>
											<logic:empty name="item" property="userName">
												δ֪
											</logic:empty>
											
										</td>
										<td bgcolor="#ffffff">
											<logic:notEmpty name="item" property="operation">
												<bean:write name="item" property="operation" />
											</logic:notEmpty>
											<logic:empty name="item" property="operation">
												δ֪
											</logic:empty>
										</td>
										<td bgcolor="#ffffff">
											<logic:notEmpty name="item" property="logTime">
												<bean:write name="item" property="logTime" />
											</logic:notEmpty>
											<logic:empty name="item" property="logTime">
												δ֪
											</logic:empty>
										</td>
										<logic:present name="form" scope="request">
											<input type="hidden" name="logTypeId" value="<bean:write name="form" property="logTypeId"/>" />
											<input type="hidden" name="operation" value="<bean:write name="form" property="operation"/>" />
											<input type="hidden" name="startDate" value="<bean:write name="form" property="startDate"/>" />
											<input type="hidden" name="endDate" value="<bean:write name="form" property="endDate"/>" />
											<input type="hidden" name="userName" value="<bean:write name="form" property="userName"/>" />
										</logic:present>
									</tr>
								</logic:iterate>
							</logic:present>
							<logic:notPresent name="Records" scope="request">
								<tr align="left">
									<td bgcolor="#ffffff" colspan="5">
										��ƥ����־
									</td>
								</tr>
							</logic:notPresent>
					</table>
				</div>
			</td>
		</tr>	
		<tr>
			<td>
				<div align="center">
					<table width="94%" border="0" cellpadding="4" cellspacing="1">
					<tr>
						<td width="100%" align="left">
                        <logic:present name="Records" scope="request">
							<html:button property="selectAll" value="ȫ    ѡ" styleClass="input-button" onclick="return _selectAll()"/>&nbsp;<html:button property="cancelAll" styleClass="input-button"  value="ȡ    ��" onclick="return _cancelAll()"/>
                        </logic:present>
						</td>
					</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div align="center">
					<table width="94%" border="0">
						<TR>
							<TD width="80%">
								<jsp:include page="../apartpage.jsp" flush="true">
									<jsp:param name="url" value="viewLogIn.do" />
								</jsp:include>
							</TD>
							<logic:present name="Records" scope="request">
							<TD width="20%">
								<html:submit styleClass="input-button" style="FLOAT: right" value="��   ��" />
							</TD>
							</logic:present>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		</html:form>
	</table>
</body>
</html:html>

