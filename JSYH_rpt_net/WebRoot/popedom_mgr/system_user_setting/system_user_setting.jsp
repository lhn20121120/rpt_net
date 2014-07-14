
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
	<head>
		<html:base/>
		<title>部门信息管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			//下拉框的当前选择项是当前系统用户
			function setCurrUser(productUserId)
			{
				var listObj = document.form1.productUserId;
				for(var i=0;i<listObj.length;i++)
		  		{
		  			if(listObj.options[i].value == productUserId)
		  			{		
		  				listObj.selectedIndex = i;
		  				break;
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
			<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 权限管理 >>系统用户</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
    <br>
		<html:form action="/popedom_mgr/updateProductUser" method="post" styleId="form1" >
	    <table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		    
		      <tr class="titletab">
		            <th align="center">设置当前系统用户</th>
		      </tr>
		   
		      <tr>
			      <td bgcolor="#ffffff">
			      <table width="100%" border="0" align="center">
						  <tr bgcolor="#ffffff">
					      	<td height="7">
					      	</td>
					      </tr>		      
			          	  <tr>
				      	  	<td width="35%">
			           	  	</td>
				         	 <td align="left" bgcolor="#ffffff">
				               	当前系统用户名称:&nbsp;
				               	  	<logic:present name="currUser" scope="request">
					               		<bean:write name="currUser" property="productUserName"/>
					               	</logic:present>
				             </td>
				          </tr>
				          <tr>
				          	<td width="35%">
			          	  	</td>
				         	 <td align="left" bgcolor="#ffffff">
				               	新系统用户名称:&nbsp;&nbsp;&nbsp;
				               	<html:select property="productUserId">
									<logic:present name="productUserList" scope="request">
										<html:optionsCollection name="productUserList" label="label" value="value" />
									</logic:present>
								</html:select>
								<logic:present name="currUser" scope="request">
									<script language="javascript">
										setCurrUser(<bean:write name="currUser" property="productUserId"/>);
									</script>
								</logic:present>
				             </td>
				          </tr>
				          <tr>
				          	<td colspan="2">
				          		  <div id="location"></div>
				          	</td>
				          </tr>
				          <tr>
				              <td colspan="2" align="right" bgcolor="#ffffff">
				            	 <html:submit value="保存" styleClass="input-button"/>
				     	
				              </td>
				          </tr>
   	
			      	</table>
			      </td> 
		       </tr>
		      </table>
        </html:form>
	</body>
</html:html >
