
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.net.form.TargetDefineForm" %>
<jsp:useBean id="utiltargetDefineForm" scope="page" class="com.fitech.net.form.UtilTargetDefineForm" />

<html:html locale="true">
<head>
	<html:base/>
<title> ָ�궨��</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<jsp:include page="../../calendar.jsp" flush="true">
		  <jsp:param name="path" value="../../"/>
		</jsp:include>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="JavaScript" >
		function validate(){
			var defineName = document.getElementById('defineName');
			var version = document.getElementById('version');
			var startDate = document.getElementById('startDate');
			var endDate = document.getElementById('endDate');
			var formula = document.getElementById('formula');
			var result = false;
				
			if(defineName.value==""){
				alert("ָ�궨�����ֲ���Ϊ�գ�");
				defineName.focus();
				return result;
			}	
			if(version.value==""){
			    alert("ָ��汾����Ϊ��");
			    version.focus();
			    return result;
			}
			if(CheckNumber(version.value) == false)  //�Ƿ��ַ�
			{
				alert("�Բ���,ָ��汾ֻ�������ֻ���ĸ!");
				version.focus();
				return false;
			}
			
			if(startDate.value=="" ){
				alert("��ʼʱ�䲻��Ϊ�գ�");	
				return result;
			}	
			if (endDate.value==""){
				alert("����ʱ�䲻��Ϊ�գ�");
				return result;
			}
			if(isEmpty(endDate.value)==false){
			 		if(startDate.value>endDate.value){
			 			alert("ָ��Ľ������ڲ���С����ʼ����!\n");
			 			return false;
			 	}	
			}
			
			if(formula.value=="" ){
				alert("��ʽ����Ϊ�գ�");
				formula.focus();
				return result;
			}
		     // checkVersion(defineName.value,version.vluse)
			  return true;
		}
		
		function Check( reg, str ){
				if( reg.test( str ) ){
					return true;
				}
				return false;
			}
			// �������
			function CheckNumber( str ){
				// var reg = /^\d*(?:$|\.\d*$)/;
			     var reg = /^[A-Za-z0-9]+$/;
				return Check( reg, str );
			}
		/**
		function checkVersion(defineName,version){
			 try
		 	 {
			  	var validateURL = "<%=request.getContextPath()%>/target/checkTargetDefineVersion.do?defineName="+defineName+"&version="+version;
			  	var param = "radom="+Math.random(); 
			   	new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:checkVersionHandler,onFailure: reportError});
		   	}
		   	catch(e)
		   	{
		   		alert('>ϵͳæ�����Ժ�����...��');
		   		validate.result = false;
		   	}  
		}
		function checkVersionHandler(request){
		   try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
				if(isEmpty(result)==false{				  	
				    if(result=="true"){
				       alert('ָ�꣨��ͬ�汾�ģ��Ѿ����ڣ�');
				       validate.result =  false;
				    }
				   validate.result =  true;
				  }else{
					 alert('>>ϵͳæ�����Ժ�����...��');
					 validate.result = false;
				  }
			}
			catch(e)
			{}
		}
	   //ʧ�ܴ���
	    function reportError(request)
	    {
	        alert('>>>ϵͳæ�����Ժ�����...��');
	        validate.result = false;
	    }
	**/
		function addFormula(){
			var defineName = document.getElementById('defineName');
			var startDate = document.getElementById('startDate');
			var endDate = document.getElementById('endDate');
			var formula = document.getElementById('formula');
			var law = document.getElementById('law');
			var des = document.getElementById('des');				
			window.location="<%=request.getContextPath()%>/target/targetDefine.do?defineName="+defineName.value
				+"&startDate="+startDate.value+"&endDate="+endDate.value+"&formula="+formula.value+"&law="+law.value+"&des="+des.value;
		}
		function goBack(){
			window.location="<%=request.getContextPath()%>/target/viewTargetDefine.do";
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
<br>
<br>
<table border="0" width="100%" align="center">
		<tr>
			 <td>��ǰλ�� >> 	ָ�궨�� >> ����ָ�궨��</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">
	
	<tr> 
    	<td align="right" valign="top">
    <html:form action="/target/insertTargetDefine" method="Post" styleId="form1" onsubmit="return validate()">
			<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
      				<tr id="tbcolor">
            			<th align="center">���ָ�궨��</th>
      				</tr>
   					<tr>
       					 <td height="204" align="right" bgcolor="#FFFFFF">          
						<table width="100%"  border="0" >
									
									<TR>
										<TD align="right">
											 ָ������
										</TD>
										<TD>
											<html:text styleId="defineName" property="defineName" size="20" maxlength="80"  styleClass="input-text" />
										</TD>
										<TD align="right"> ָ��汾</TD>
										<td>
										    <html:text styleId="version" property="version" size="20" maxlength="4"  styleClass="input-text" />
										</td>
										</TR><TR>
										<TD align="right"> &nbsp;ָ��ҵ������</TD>
										<TD>
											<html:select property="businessId" >
									            
									             <html:optionsCollection name="utiltargetDefineForm" property="businessList"/>
								           </html:select>
										</TD>
										<TD align="right">��ʼʱ��</TD>
										<TD>
											<html:text property="startDate" size="15" styleClass="input-text" style="text" readonly="true" /><html:img border="0" src="../../image/calendar.gif" onclick="return showCalendar('startDate', 'y-mm-dd');"/>
										</TD></TR><tr>
										<td align="right">ָ������</td>
										<td>
										<html:select property="normalId" >
									            
									             <html:optionsCollection name="utiltargetDefineForm" property="normalList"/>
								           </html:select>
										</td>
										<td align="right"> &nbsp;����ʱ��</td>
										<td>
											<html:text property="endDate" size="15" styleClass="input-text" style="text" readonly="true" /><html:img border="0" src="../../image/calendar.gif" onclick="return showCalendar('endDate', 'y-mm-dd');"/>
											
										</td></tr>
									<tr>
										<td align="right">��ʽ����</td>
										<td colspan="3">
											<html:text property="formula" size="70" styleClass="input-text" readonly="false"><bean:write name="targetForm" property="formula"/></html:text>
<%--											<html:button property="back" styleClass="input-button" value="..." onclick="addFormula()"/>																	--%>
										</td>
									</tr>
									<tr>
										<td align="right">
											 ���ɷ���
										</td>
										<td colspan="3">
											<html:textarea  property="law"   rows="3" style="width:100% "/>
										</td>
									</tr>
									<TR>
										<TD align="right">ָ������</TD>
										<TD colspan="3">
											<html:textarea  property="des"   rows="4" style="width:100% "/></TD></TR>
									<tr>
										<td colspan="6" align="right">
											<html:submit value="��һ��" styleClass="input-button" />
                   		 					&nbsp;<html:button property="back" value="����" styleClass="input-button" onclick="javascript:goBack();" />
										</td>
									</tr>
								</table>
		  </td>
   </tr>    
 </table>
 </html:form>
</body>

</html:html>