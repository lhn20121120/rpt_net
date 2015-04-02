<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>用户用户组设置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">

		//增加功能
		function addUserGrp()
		{
			//全部功能列表框
			var allUserGrpList = document.getElementById('allUserGrp');
			//选中功能列表框
			var selectUserGrpList= document.getElementById('selectUserGrp');
			//所有功能列表框中的项目
			var allUserGrpListOptions = allUserGrpList.options;
			//选中功能列表框中的项目
			var selectUserGrpListOptions = selectUserGrpList.options;
			
			for(var i=0; i<allUserGrpListOptions.length; i++)
			{
				if(allUserGrpList.options[i].selected)
				{
					//查看是否已经选中过
					var isExit = false; 
					for (var j=0;j<selectUserGrpListOptions.length;j++)
					{
						if(allUserGrpList.options[i].value==selectUserGrpList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false)
					{
						selectUserGrpList.options[selectUserGrpList.length] = new Option(allUserGrpList.options[i].text,allUserGrpList.options[i].value);
			    		allUserGrpList.options[i].style.color ="gray";
			    	}
			    }
					
			}
		}
	
		//删除功能
		function delUserGrp()
		{
			//全部功能列表框
			var allUserGrpList = document.getElementById('allUserGrp');
			//选中功能列表框
			var selectUserGrpList= document.getElementById('selectUserGrp');
			//所有功能列表框中的项目
			var allUserGrpListOptions = allUserGrpList.options;
			//选中功能列表框中的项目
			var selectUserGrpListOptions = selectUserGrpList.options;
			
			var optionLen = selectUserGrpListOptions.length;
			
			var offset = 0; 
			for(var i=0; i<optionLen; i++)
			{
				if(selectUserGrpList.options[i-offset].selected)
				{
					//改变全部功能框中对应的颜色
					for(var j=0; j<allUserGrpListOptions.length; j++)
					{
						if(allUserGrpList.options[j].value==selectUserGrpList.options[i-offset].value)
							allUserGrpList.options[j].style.color ="black";

					}
					selectUserGrpList.remove(i-offset);			
					offset++;
				}
			}
		}
		
		//保存--提交数据
		function submitData()
		{
			//选中功能列表框
			var selectUserGrpList= document.getElementById('selectUserGrp');
			
			//选中功能列表框中的项目
			var selectUserGrpListOptions = selectUserGrpList.options;
			
//			if(selectUserGrpListOptions.length==0)
//			{
//				alert('该用户必须要属于一个用户组！');
//				return;
//		}
			//提取选择的内容，把它连接成字符串，之间用“,”格开
			var selectData ="";
			for(var i=0; selectUserGrpListOptions.length>0&&i<selectUserGrpListOptions.length; i++)
			{
				selectData += (selectUserGrpList.options[i].value + ",");
			}
			selectData = selectData.substring(0,selectData.lastIndexOf(","));	
			window.location = "updateMUserToGrp.do?userId=<bean:write name="UserId"/>&selectedUserGrpIds="+selectData+"&curPage=<%=request.getParameter("curPage")%>";
		}
		function _back(){
			window.location = "../popedom_mgr/viewOperator.do?curPage=<%=request.getParameter("curPage")%>";
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
			 <td>当前位置 >> 权限管理 >>用户组设置</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				用户名: &nbsp;
				<logic:present name="UserName" scope="request">
					<bean:write name="UserName"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>用户组设置</strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<html:form action="/popedom_mgr/updateMUserToGrp" method="post">
		<table width="60%"  border="0" align="center">
			    <tr>
			      <td width="38%" align="center" valign="middle">      
			      	用户组
			      </td>
			      <td width="23%">
				  
				 
			     <td width="39%" align="center" valign="middle">隶属于</td>
			    </tr>
		    	<tr>
			      <td align="center">
					  <html:select styleId="allUserGrp" property="allUserGrp" size="18" multiple="true" style="width:180">
					  	<logic:present name="AllUserGrp" scope="request">
					  		<html:optionsCollection name="AllUserGrp" label="label" value="value" />
				      	</logic:present>
				      </html:select>
			      </td>
		        <td align="center" valign="middle">
			        <p>
			       		 <html:button property="add" value="→添加" styleClass="input-button" onclick="addUserGrp()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="←删除" styleClass="input-button" onclick="delUserGrp()"/>
			        </p>
		        </td>
		        
		      	<td align="center">
		          <html:select styleId="selectUserGrp" property="selectedUserGrpIds" size="18" multiple="true"  style="width:180">
		          	<logic:present name="UserSetUserGrp" scope="request">
		          		<html:optionsCollection name="UserSetUserGrp" label="label" value="value" />
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
   						<%-- <html:button property="back" value="返回" styleClass="input-button" onclick="window.location.assign('../popedom_mgr/viewOperator.do')"/>--%>
  						<html:button property="back" value="返回" styleClass="input-button" onclick="_back()"/>
   					</td>
   				</tr>
 		  </table>
 		  </html:form>
</body>
</html:html>
