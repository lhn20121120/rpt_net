<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>��ɫ��Ϣ���</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">

		//���ӱ���Ȩ��
		function addReport()
		{
			//ȫ�������б��
			var allReportList = document.getElementById('allReport');
			
			//ѡ�б����б��
			var selectReportList= document.getElementById('selectReport');
			
			//���б����б���е���Ŀ
			var allReportListOptions = allReportList.options;
			
			//ѡ�б����б���е���Ŀ
			var selectReportOptions = selectReportList.options;
			
			for(var i=0; i<allReportListOptions.length; i++)
			{
				if(allReportList.options[i].selected)
				{
					//�鿴�Ƿ��Ѿ�ѡ�й�
					var isExit = false; 
					for (var j=0;j<selectReportOptions.length;j++)
					{
						if(allReportList.options[i].value==selectReportList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false)
					{
						selectReportList.options[selectReportList.length] = new Option(allReportList.options[i].text,allReportList.options[i].value);
			    		allReportList.options[i].style.color ="gray";
			    	}
			    }
					
			}
		}
	
		//ɾ������Ȩ��
		function delReport()
		{
			//ȫ�������б��
			var allReportList = document.getElementById('allReport');
			
			//ѡ�б����б��
			var selectReportList= document.getElementById('selectReport');
			
			//���б����б���е���Ŀ
			var allReportListOptions = allReportList.options;
			
			//ѡ�б����б���е���Ŀ
			var selectReportOptions = selectReportList.options;
						
			var optionLen = selectReportOptions.length;
			
			var offset = 0; 
			
			for(var i=0; i<optionLen; i++)
			{
				if(selectReportList.options[i-offset].selected)
				{
					//�ı�ȫ�����ܿ��ж�Ӧ����ɫ
					for(var j=0; j<allReportListOptions.length; j++)
					{
						if(allReportList.options[j].value==selectReportList.options[i-offset].value)
							allReportList.options[j].style.color ="black";

					}
					
					selectReportList.remove(i-offset);		
					offset++;
				}
			}
		}
		
		//����--�ύ����
		function submitData()
		{
			//ѡ�б����б��
			var selectReportList= document.getElementById('selectReport');
			//ѡ�й����б���е���Ŀ
			var selectReportListOptions = selectReportList.options;
			
			if(selectReportListOptions.length==0)
			{
				alert('���û������߱�һ������Ȩ�ޣ�');
				return;
			}
			//��ȡѡ������ݣ��������ӳ��ַ�����֮���á�,����
			var selectData ="";
			
			for(var i=0; i<selectReportListOptions.length; i++)
			{
				selectData += (selectReportList.options[i].value + ",");
			}
			selectData = selectData.substring(0,selectData.lastIndexOf(","));
			
			document.form1.selectRepIds.value = selectData;			
			document.form1.submit();
			
		}
		//�����ϼ��˵�
		function back()
		{		
			document.form2.submit();
		}
	</script>
</head>
<%
	String userGrpNm = "";
	if (request.getAttribute("UserGrpNm") != null) {
		userGrpNm = (String) request.getAttribute("UserGrpNm");
	} else {
		if (request.getParameter("userGrpNm") != null)
			//reportName=FitechUtil.getParameter(request,"reportName");
			userGrpNm = request.getParameter("userGrpNm");

	}
	String powDes = "";
	String powType = request.getAttribute("powType") != null ? request.getAttribute("powType").toString() : null;
	if(powType.equals(Config.POWERTYPECHECK.toString()))
		powDes = "�����Ȩ�ޣ�";
	else if(powType.equals(Config.POWERTYPEREPORT.toString()))
		powDes = "������Ȩ�ޣ�";
	else if(powType.equals(Config.POWERTYPESEARCH.toString()))
		powDes = "���鿴Ȩ�ޣ�";               			                 			
%>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
			<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>��ǰλ�� >> Ȩ�޹��� >>����Ȩ������</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
	
	<br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				�û�������: &nbsp;
				<logic:present name="UserGrpNm" scope="request">
	
									<bean:write name="UserGrpNm"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>����Ȩ������<%=powDes%></strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<html:form action="/popedom_mgr/updateMPurBankLevel" method="post" styleId="form1">			
			<input type="hidden" name="selectOrgIds" value="<bean:write name="selectOrgIds"/>">
			<input type="hidden" name="selectOrgNames" value="<bean:write name="selectOrgNames"/>">
			<input type="hidden" name="selectBankLevelIds" value="<bean:write name="selectBankLevelIds"/>">
			<input type="hidden" name="selectBankLevelNames" value="<bean:write name="selectBankLevelNames"/>">
			<input type="hidden" name="userGrpNm" value="<bean:write name="UserGrpNm"/>">
			<input type="hidden" name="userGrpId" value="<bean:write name="UserGrpId"/>">
			<input type="hidden" name="powType" value="<bean:write name="powType"/>">
			<input type="hidden" id="selectRepIds" name="selectRepIds">
			<%
				String curPage=null;
						if(request.getAttribute("curPage")!=null){
							curPage=(String)request.getAttribute("curPage");
						}
						else
							curPage="1";
			%>
			<input type="hidden" name="curPage" value="<%=curPage%>">
		<table width="60%"  border="0" align="center">
			    <tr>
			      <td width="38%" align="center" valign="middle">      
			      	ȫ������
			      </td>
			      <td width="23%"> 
			     <td width="39%" align="center" valign="middle">���Բ鿴�ı���</td>
			    </tr>
		    	<tr>
			      <td align="center">
					  <html:select styleId="allReport" property="childRepId" size="18" multiple="true" style="width:250">
					  	<logic:present name="AllReport" scope="request">
					  		<html:optionsCollection name="AllReport" label="label" value="value" />
				      	</logic:present>
				      </html:select>
			      </td>
		        <td align="center" valign="middle">
			        <p>
			       		 <html:button property="add" value="�����" styleClass="input-button" onclick="addReport()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="��ɾ��" styleClass="input-button" onclick="delReport()"/>
			        </p>
		        </td>
		        
		      	<td align="center">
		          <html:select styleId="selectReport" property="selectRepList" size="18" multiple="true"  style="width:250">
		          	<logic:present name="UserGrpRepPopedom" scope="request">
		          		<html:optionsCollection name="UserGrpRepPopedom" label="label" value="value" />
		          	</logic:present>
		          </html:select>      
		        </td>
		    </tr>
		  </table>
		  </html:form>
		   <table width="80%"  border="0" align="center">
		   		<tr>
		   			<td colspan="3">
		   				<div id=location></div>
		   			</td>
		   		</tr>
 		  		<tr>

   					<td align="right">
   						<html:button property="submit" value="����" styleClass="input-button" onclick="submitData()"/>	   			
		   				<html:button property="back" value="����" styleClass="input-button" onclick="back()"/>
   					</td>
   					<html:form action="/popedom_mgr/viewMUserOrg" method="post" styleId="form2">							
							<input type="hidden" name="selectBankLevelIds" value="<bean:write name="selectBankLevelIds"/>">
							<input type="hidden" name="selectBankLevelNames" value="<bean:write name="selectBankLevelNames"/>">							
							<input type="hidden" id="selectOrgIds" name="selectOrgIds" value="<bean:write name="selectOrgIds"/>">
							<input type="hidden" id="selectOrgNames" name="selectOrgNames" value="<bean:write name="selectOrgNames"/>">
							<input type="hidden" name="userGrpNm" value="<bean:write name="UserGrpNm"/>">
							<input type="hidden" name="userGrpId" value="<bean:write name="UserGrpId"/>">
							<input type="hidden" name="powType" value="<bean:write name="powType"/>">
		  			</html:form>
   				</tr>
 		  </table>
 		  
</body>
</html:html>
