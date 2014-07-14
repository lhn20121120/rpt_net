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
		<title>角色信息添加</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">

		//增加功能
		function addMenu()
		{
			//全部功能列表框
			var allMenuList = document.getElementById('allMenu');
			//选中功能列表框
			var selectMenuList= document.getElementById('selectMenu');
			//所有功能列表框中的项目
			var allMenuSelOptions = allMenuList.options;
			//选中功能列表框中的项目
			var selectMenuSelOptions = selectMenuList.options;
			
			var len = allMenuSelOptions.length;
			for(var i=0; i<len; i++)
			{
				if(allMenuList.options[i].selected)
				{
					//查看是否已经选中过
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
	
		//删除功能
		function delMenu()
		{
			//选中功能列表框
			var selectMenuList= document.getElementById('selectMenu');
			
			//选中功能列表框中的项目
			var selectMenuSelOptions = selectMenuList.options;
			
			//全部功能列表框
			var allMenuList = document.getElementById('allMenu');
			//所有功能列表框中的项目
			var allMenuSelOptions = allMenuList.options;
			
			
			var optionLen = selectMenuSelOptions.length;
			
			var offset = 0; 
			for(var i=0; i<optionLen; i++)
			{
				if(selectMenuList.options[i-offset].selected)
				{
					//改变全部功能框中对应的颜色
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
		
		//保存--提交数据
		function submitData()
		{
			//选中功能列表框
			var selectMenuList= document.getElementById('selectMenu');
			//选中功能列表框中的项目
			var selectMenuSelOptions = selectMenuList.options;
			
			if(selectMenuSelOptions.length==0)
			{
				alert('该角色必须具备一个功能！');
				return;
			}
			//提取选择的内容，把它连接成字符串，之间用“,”格开
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
			 <td>当前位置 >> 系统管理 >> 角色管理</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
    <br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				角色名称: &nbsp;
				
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
                 	<strong>角色权限设置</strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<html:form action="/popedom_mgr/updateRoleTool" method="post">
		<table width="60%"  border="0" align="center">
			    <tr>
			      <td width="38%" align="center" valign="middle">      
			      	全部功能
			      </td>
			      <td width="23%">
				  
				 
			     <td width="39%" align="center" valign="middle">具备的功能</td>
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
			       		 <html:button property="add" value="→添加" styleClass="input-button" onclick="addMenu()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="←删除" styleClass="input-button" onclick="delMenu()"/>
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
   						<html:button property="submit" value="保存" styleClass="input-button" onclick="submitData()"/>&nbsp;
  						<html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../popedom_mgr/viewRole.do')"/>
   					</td>
   				</tr>
 		  </table>
 		  </html:form>
</body>
</html:html>
