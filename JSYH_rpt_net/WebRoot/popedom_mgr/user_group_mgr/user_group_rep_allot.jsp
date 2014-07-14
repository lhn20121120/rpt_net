<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>ģ�����</title>
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
		function back(){
			window.location="<%=request.getContextPath()%>/popedom_mgr/viewMUserGrp.do";
			
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
	<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>��ǰλ�� >> Ȩ�޹��� >>�û��鱨�����</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
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
                 	<strong>�û��鱨�ͱ������</strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<html:form action="/popedom_mgr/updateReportAllot" method="post" styleId="form1">
			
			<input type="hidden" name="userGrpNm" value="<bean:write name="UserGrpNm"/>">
			<input type="hidden" name="userGrpId" value="<bean:write name="UserGrpId"/>">
			<input type="hidden" id="selectRepIds" name="selectRepIds">
			
		<table width="60%"  border="0" align="center">
			    <tr>
			      <td width="38%" align="center" valign="middle">      
			      	ȫ������
			      </td>
			      <td width="23%"> 
			     <td width="39%" align="center" valign="middle">���еı���</td>
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
   				</tr>
 		  </table>
 		  
</body>
</html:html>
