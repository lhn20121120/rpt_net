<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<jsp:directive.page import="com.cbrc.smis.common.Config" />
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<html:html locale="true">
<head>
	<html:base />
	<title>ϵͳ�����趨</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>

	<script language="javascript">
	    function goAddPage()
	    {
	    	window.location.href="unitTypeAdd.jsp";
	    }
	    
	    <logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    
	   // ��������
	  
	  function saveSetting(parType,parName,radioname)
		{   //�õ�ѡ�е�ֵ
	
			var selectedValue = "";
		    var form1 = document.getElementById("frm");
		    var i = 0;
		    var seletName= document.getElementsByName(radioname);
		    
		    for (i=0; i<seletName.length; i++)
		    {
		        if (seletName[i].checked)
		        {
		            selectedValue=seletName[i].value;
		            break;
		        }
		    }
		     var validateURL = "<%=request.getContextPath()%>/config/SaveSysPar.do"; 
			 var param = "parType="+parType+"&parName="+parName+"&parValue="+selectedValue+"&radom="+Math.random();
			 
				
		    // ���ѡ�С��ϱ������ļ��Ƿ����ͨ�����У�顱����ô����ѡ��"����ʱ���У�鰴ť�Ƿ���б��У�� "
		    if(parName=="BJ_VALIDATE" && selectedValue==1){
		      if(confirm('�Ƿ�ͬʱ�趨\"����ʱ���У�鰴ť�Ƿ���б��У��\"Ϊ��?')){
			        try
			 		 {  
					    var param1 = "parType=�ϱ�У��&parName=UP_VALIDATE_BJ&parValue=1&radom="+Math.random();
					    param1 = encodeURI(param1);
					    param1 = encodeURI(param1);
				        new Ajax.Request(validateURL,{method: 'post',parameters:param1,onComplete:validateReportHandler1,onFailure: reportError});
			        }catch(e1)
			   		{
			   			alert('ϵͳæ�����Ժ�����...��');
			   		}
		      }
		       
		    }
		    
			 try
		 	 {
			  	
			    //����ת��
				param = encodeURI(param);
			    param = encodeURI(param);
			   	new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
			   	prodress.style.display = "" ;
		   	}
		   	catch(e)
		   	{
		   		alert('ϵͳæ�����Ժ�����...��');
		   	}
		}
		
		//У��Handler
		function validateReportHandler(request)
		{
			try
			{
			
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				  	alert('����ʧ�ܣ�');
				  }	
				  else if(result == 'true')
				  {
					 alert('����ɹ���');
				  }
				 
				  prodress.style.display = "none" ;
				  enableValidate = false;
			}
			catch(e)
			{
				prodress.style.display = "none" ;
			}
	    }
	    function validateReportHandler1(request)
		{
			  window.location.reload()
	    }
	    //��ѯʧ�ܴ���
	    function reportError(request)
	    {
	        alert('ϵͳæ�����Ժ�����...��');
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
	<table border="0" width="100%" align="center">
		<tr>
			<td height="8"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> �����趨 >> ϵͳ�����趨
			</td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
		<tr id="prodress" style="display:none">
			<td bgcolor="#FFFFFF">
				&nbsp;&nbsp;&nbsp;
				<span class="txt-main" style="color:#FF3300">���ڱ���,���Ժ�...</span>
			</td>
		</tr>
		
	</table>

	<html:form method="post" styleId="frm" action="/config/SaveSysPar" >

		<TABLE border="0" width="100%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
			<tr class="titletab">
				<th height="25" align="center" colspan="6">
					ϵͳ�����趨
				</th>
			</tr>
			<TR class="tableHeader">
				<TD width="6%" align="center">
					���
				</TD>
<%--				<TD width="12%" align="center">--%>
<%--					�������--%>
<%--				</TD>--%>
<%--				<TD width="15%" align="center">--%>
<%--					��������--%>
<%--				</TD>--%>
				<TD width="12%" align="center">
					����ֵ
				</TD>
				<TD width="45%" align="center">
					����
				</TD>
				<TD width="10%" align="center">
					����
				</TD>
			</TR>
			<logic:present name="Records" scope="request">
				<logic:iterate id="viewSysPar" name="Records" indexId="index">

					<TR bgcolor="#FFFFFF">
						<TD align="center">
							<%=((Integer) index).intValue() + 1%>
						</TD>
<%--						<TD align="center">--%>
<%--							<bean:write name="viewSysPar" property="parType" />--%>
<%--						</TD>--%>
<%--						<TD align="center">--%>
<%--							<bean:write name="viewSysPar" property="parName" />--%>
<%--						</TD>--%>
						<TD align="center">
							<logic:equal name="viewSysPar" property="parValue" value="0">
								<input type="radio" name="parValues<%=((Integer) index).intValue() + 1%>" value="0" checked />&nbsp;��
								<input type="radio" name="parValues<%=((Integer) index).intValue() + 1%>"  value="1">&nbsp;��
							</logic:equal>
							<logic:equal name="viewSysPar" property="parValue" value="1">
								<input type="radio" name="parValues<%=((Integer) index).intValue() + 1%>" value="0" />&nbsp;��
								<input type="radio" name="parValues<%=((Integer) index).intValue() + 1%>" value="1" checked>&nbsp;��
						</logic:equal>

						</TD>

						<TD align="center">
							<bean:write name="viewSysPar" property="description" />
						</TD>
						<TD align="center">
							<INPUT class="input-button" id="ButtonSave" type="button" value=" ��  �� " name="ButtonSave"
								onclick="saveSetting('<bean:write name="viewSysPar" property="parType" />','<bean:write name="viewSysPar" property="parName" />','parValues<%=((Integer) index).intValue() + 1%>')">
						</TD>

					</TR>
				</logic:iterate>
			</logic:present>

			<logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6">
						��ƥ���¼
					</td>
				</tr>
			</logic:notPresent>
			
		</TABLE>
</html:form>
		<TABLE align="center" border="0" width="80%">
			<TR>
				<TD>
					<jsp:include page="../apartpage.jsp" flush="true">
						<jsp:param name="url" value="ViewSysPar.do" />
					</jsp:include>
				</TD>
			</TR>
		</TABLE>
</body>
</html:html>
