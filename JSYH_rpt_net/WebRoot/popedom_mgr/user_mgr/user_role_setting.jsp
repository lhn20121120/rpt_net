<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>�û���ɫ����</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">

		//���ӹ���
		function addRole()
		{
			//ȫ�������б��
			var allRoleList = document.getElementById('allRole');
			//ѡ�й����б��
			var selectRoleList= document.getElementById('selectRole');
			//���й����б���е���Ŀ
			var allRoleListOptions = allRoleList.options;
			//ѡ�й����б���е���Ŀ
			var selectRoleListOptions = selectRoleList.options;
			
			var len = allRoleListOptions.length;
			
			for(var i=0; i<len; i++)
			{
				if(allRoleList.options[i].selected)
				{
					//�鿴�Ƿ��Ѿ�ѡ�й�
					var isExit = false; 
					for (var j=0;j<selectRoleListOptions.length;j++)
					{
						if(allRoleList.options[i].value==selectRoleList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false)
					{	
						selectRoleList.options[selectRoleList.length] = new Option(allRoleList.options[i].text,allRoleList.options[i].value);
			    		allRoleList.options[i].style.color ="gray";
			    		//allRoleList.options[allRoleList.length] = new Option(allRoleList.options[i].text,allRoleList.options[i].value);
			    		//allRoleList.options[allRoleList.length].style.color ="gray";
			    		//allRoleList.options.remove(i);
			    		//i--;
			    		//len--;	
			    	}
			    }
					
			}
		}
	
		//ɾ������
		function delRole()
		{	
			//ȫ�������б��
			var allRoleList = document.getElementById('allRole');
			//ѡ�й����б��
			var selectRoleList= document.getElementById('selectRole');
			//���й����б���е���Ŀ
			var allRoleListOptions = allRoleList.options;
			//ѡ�й����б���е���Ŀ
			var selectRoleListOptions = selectRoleList.options;
			
			var optionLen = selectRoleListOptions.length;
			
			var offset = 0; 
			for(var i=0; i<optionLen; i++)
			{
				if(selectRoleList.options[i-offset].selected)
				{
					//�ı�ȫ�����ܿ��ж�Ӧ����ɫ
					for(var j=0; j<allRoleListOptions.length; j++)
					{
						if(allRoleList.options[j].value==selectRoleList.options[i-offset].value)
							allRoleList.options[j].style.color ="black";

					}
					
					selectRoleList.remove(i-offset);			
					offset++;
				}
			}
		}
		
		//����--�ύ����
		function submitData()
		{
			//ѡ�й����б��
			var selectRoleList= document.getElementById('selectRole');
			
			//ѡ�й����б���е���Ŀ
			var selectRoleListOptions = selectRoleList.options;
			
			if(selectRoleListOptions.length==0)
			{
				alert('���û�����Ҫ����һ����ɫ��');
				return;
			}
			//��ȡѡ������ݣ��������ӳ��ַ�����֮���á�,����
			var selectData ="";
			for(var i=0; i<selectRoleListOptions.length; i++)
			{
				selectData += (selectRoleList.options[i].value + ",");
			}
			selectData = selectData.substring(0,selectData.lastIndexOf(","));	
			window.location = "updateUserRole.do?userId=<bean:write name="UserId"/>&selectedRoleIds="+selectData+"&curPage=<%=request.getParameter("curPage")%>";
		}
		//1.11�޸�
		function _back() {
			window.location = "../popedom_mgr/viewOperator.do?curPage=<%=request.getParameter("curPage")%>";
		}
		//1.11�޸�
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
			 <td>��ǰλ�� >> Ȩ�޹��� >>�û���ɫ����</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				�û���: &nbsp;
				<logic:present name="UserName" scope="request">
					<bean:write name="UserName"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>��ɫ����</strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<html:form action="/popedom_mgr/updateUserRole" method="post">
		<table width="60%"  border="0" align="center">
			    <tr>
			      <td width="38%" align="center" valign="middle">      
			      	��ɫ
			      </td>
			      <td width="23%">
				  
				 
			     <td width="39%" align="center" valign="middle">������</td>
			    </tr>
		    	<tr>
			      <td align="center">
					  <html:select styleId="allRole" property="allRole" size="18" multiple="true" style="width:180">
					  	<logic:present name="AllRole" scope="request">
					  		<html:optionsCollection name="AllRole" label="label" value="value" />
				      	</logic:present>
				      </html:select>
			      </td>
		        <td align="center" valign="middle">
			        <p>
			       		 <html:button property="add" value="�����" styleClass="input-button" onclick="addRole()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="��ɾ��" styleClass="input-button" onclick="delRole()"/>
			        </p>
		        </td>
		        
		      	<td align="center">
		          <html:select styleId="selectRole" property="selectedRoleIds" size="18" multiple="true"  style="width:180">
		          	<logic:present name="UserSetRole" scope="request">
		          		<html:optionsCollection name="UserSetRole" label="label" value="value" />
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
   						<!-- 1.11��
  						<html:button property="back" value="����" styleClass="input-button" onclick="window.location.assign('../popedom_mgr/viewOperator.do')"/>
   						�޸� -->
   						<html:button property="back" value="����" styleClass="input-button" onclick="_back()"/>
   						<!-- 1.11xiugai -->
   					</td>
   				</tr>
 		  </table>
 		  </html:form>
</body>
</html:html>
