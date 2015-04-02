<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	String roleName = "";
	if (request.getAttribute("RoleName") != null) {
		roleName = (String) request.getAttribute("RoleName");
	} else {
		if (request.getParameter("RoleName") != null)
			//reportName=FitechUtil.getParameter(request,"reportName");
			roleName = request.getParameter("RoleName");

	}
 %>
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

		//���ӹ���
		function addMenu()
		{
			//ȫ�������б��
			var allMenuList = document.getElementById('allMenu');
			//ѡ�й����б��
			var selectMenuList= document.getElementById('selectMenu');
			//���й����б���е���Ŀ
			var allMenuSelOptions = allMenuList.options;
			//ѡ�й����б���е���Ŀ
			var selectMenuSelOptions = selectMenuList.options;
			
			var len = allMenuSelOptions.length;
			for(var i=0; i<len; i++)
			{
				if(allMenuList.options[i].selected)
				{
					//�鿴�Ƿ��Ѿ�ѡ�й�
					var isExit = false; 
					for (var j=0;j<selectMenuSelOptions.length;j++)
					{
						if(allMenuList.options[i].value==selectMenuList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false)
					{
						selectMenuList.options[selectMenuList.length] = new Option(allMenuList.options[i].text,allMenuList.options[i].value);
			    		allMenuList.options[i].style.color ="gray";
			    		//allMenuList.options[allMenuList.length] = new Option(allMenuList.options[i].text,allMenuList.options[i].value);
			    		//allMenuList.options[allMenuList.length].style.color ="gray";
			    		//allMenuList.options.remove(i);
			    		//i--;
			    		//len--;	
			    	}
			    }
					
			}
		}
	
		//ɾ������
		function delMenu()
		{
			//ѡ�й����б��
			var selectMenuList= document.getElementById('selectMenu');
			
			//ѡ�й����б���е���Ŀ
			var selectMenuSelOptions = selectMenuList.options;
			
			//ȫ�������б��
			var allMenuList = document.getElementById('allMenu');
			//���й����б���е���Ŀ
			var allMenuSelOptions = allMenuList.options;
			
			
			var optionLen = selectMenuSelOptions.length;
			
			var offset = 0; 
			for(var i=0; i<optionLen; i++)
			{
				if(selectMenuList.options[i-offset].selected)
				{
					//�ı�ȫ�����ܿ��ж�Ӧ����ɫ
					for(var j=0; j<allMenuSelOptions.length; j++)
					{
						if(allMenuList.options[j].value==selectMenuList.options[i-offset].value)
							allMenuList.options[j].style.color ="black";

					}
					
					selectMenuList.remove(i-offset);			
					offset++;
				}
			}
		}
		
		//����--�ύ����
		function submitData()
		{
			//ѡ�й����б��
			var selectMenuList= document.getElementById('selectMenu');
			//ѡ�й����б���е���Ŀ
			var selectMenuSelOptions = selectMenuList.options;
			
			if(selectMenuSelOptions.length==0)
			{
				alert('�ý�ɫ����߱�һ�����ܣ�');
				return;
			}
			//��ȡѡ������ݣ��������ӳ��ַ�����֮���á�,����
			var selectData ="";
			for(var i=0; i<selectMenuSelOptions.length; i++)
			{
				selectData += (selectMenuList.options[i].value + ",");
			}
			selectData = selectData.substring(0,selectData.lastIndexOf(","));
			//alert(selectData);		
			window.location = "updateRoleTool.do?roleId=<bean:write name="RoleId"/>&selectedMenuIds="+selectData;
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
			 <td>��ǰλ�� >> ϵͳ���� >> ��ɫ����</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
    <br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				��ɫ����: &nbsp;
				
				<logic:present name="RoleName" scope="request">								
					 <%=roleName%>					
				</logic:present>
				
				<logic:present name="ReportName" scope="request">
									
								
				</logic:present>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>��ɫȨ������</strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<html:form action="/popedom_mgr/updateRoleTool" method="post">
		<table width="60%"  border="0" align="center">
			    <tr>
			      <td width="38%" align="center" valign="middle">      
			      	ȫ������
			      </td>
			      <td width="23%">
				  
				 
			     <td width="39%" align="center" valign="middle">�߱��Ĺ���</td>
			    </tr>
		    	<tr>
			      <td align="center">
					  <html:select styleId="allMenu" property="menuId" size="18" multiple="true" style="width:250">
					  	<logic:present name="AllMenu" scope="request">
					  		<html:optionsCollection name="AllMenu" label="label" value="value" />
				      	</logic:present>
				      </html:select>
			      </td>
		        <td align="center" valign="middle">
			        <p>
			       		 <html:button property="add" value="�����" styleClass="input-button" onclick="addMenu()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="��ɾ��" styleClass="input-button" onclick="delMenu()"/>
			        </p>
		        </td>
		        
		      	<td align="center">
		          <html:select styleId="selectMenu" property="selectedMenuIds" size="18" multiple="true"  style="width:250">
		          	<logic:present name="RoleMenu" scope="request">
		          		<html:optionsCollection name="RoleMenu" label="label" value="value" />
		          	</logic:present>
		          </html:select>      
		        </td>
		    </tr>
		  </table>
		   <table width="80%"  border="0" align="center">
		   		<tr>
		   			<td>
		   				<div id=location></div>
		   			</td>
		   		</tr>
 		  		<tr>
   					<td align="right">
   						<html:button property="submit" value="����" styleClass="input-button" onclick="submitData()"/>&nbsp;
  						<html:button property="back" value="����" styleClass="input-button" onclick="window.location.assign('../popedom_mgr/viewRole.do')"/>
   					</td>
   				</tr>
 		  </table>
 		  </html:form>
</body>
</html:html>
