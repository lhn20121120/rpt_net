<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>用户组权限修改</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">

		//增加子行权限
		function addOrg(){
			//子行列表框
			var orgList = document.getElementById('orgList');		
			//选中机构列表框
			var selectOrgList= document.getElementById('selectOrg');			
			//选中机构列表框中的项目
			var selectOrgSelOptions = selectOrgList.options;			
			//所有机构列表框中的项目
			var orgListOptions = orgList.options;
						
			for(var i=0; i<orgListOptions.length; i++){
				if(orgList.options[i].selected){
					//查看是否已经选中过
					var isExit = false; 
					for (var j=0;j<selectOrgSelOptions.length;j++){
						if(orgList.options[i].value==selectOrgList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false){
						selectOrgList.options[selectOrgList.length] = new Option(orgList.options[i].text,orgList.options[i].value);
			    		orgList.options[i].style.color ="gray";
			    	}
			    }					
			}
		}
	
		//删除机构权限
		function delOrg(){
			//机构列表框
			var orgList = document.getElementById('orgList');			
			//选中机构列表框
			var selectOrgList= document.getElementById('selectOrg');			
			//选中机构列表框中的项目
			var selectOrgSelOptions = selectOrgList.options;			
			//所有机构列表框中的项目
			var orgListOptions = orgList.options;
			
			var optionLen = selectOrgSelOptions.length;
			var offset = 0; 
			for(var i=0; i<optionLen; i++){
				if(selectOrgList.options[i-offset].selected){	
					//改变全部机构框中对应的颜色
					for(var j=0; j<orgListOptions.length; j++){
						if(orgList.options[j].value==selectOrgList.options[i-offset].value)
							orgList.options[j].style.color ="black";
					}					
					selectOrgList.remove(i-offset);			
					offset++;
				}
			}
		}
		
		//增加行级权限
		function addBankLevel(){
			//行级列表框
			var bankLevelList = document.getElementById('bankLevelList');			
			//选中行级列表框
			var selectBankLevelList= document.getElementById('selectBankLevel');			
			//选中行级列表框中的项目
			var selectBankLevelOptions = selectBankLevelList.options;			
			//所有行级列表框中的项目
			var orgListOptions = bankLevelList.options;
			
			for(var i=0; i<orgListOptions.length; i++){
				if(bankLevelList.options[i].selected){
					//查看是否已经选中过
					var isExit = false; 
					for (var j=0;j<selectBankLevelOptions.length;j++){
						if(bankLevelList.options[i].value==selectBankLevelList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false){
						selectBankLevelList.options[selectBankLevelList.length] = new Option(bankLevelList.options[i].text,bankLevelList.options[i].value);
			    		bankLevelList.options[i].style.color ="gray";
			    	}
			    }					
			}
		}
		
		//删除行级权限
		function delBankLevel(){
			//行级列表框
			var bankLevelList = document.getElementById('bankLevelList');			
			//选中行级列表框
			var selectBankLevelList= document.getElementById('selectBankLevel');			
			//选中行级列表框中的项目
			var selectBankLevelOptions = selectBankLevelList.options;			
			//所有行级列表框中的项目
			var orgListOptions = bankLevelList.options;
			
			var optionLen = selectBankLevelOptions.length;
			var offset = 0; 
			for(var i=0; i<optionLen; i++){
				if(selectBankLevelList.options[i-offset].selected){	
					//改变全部行级列表框中对应的颜色
					for(var j=0; j<orgListOptions.length; j++){
						if(bankLevelList.options[j].value==selectBankLevelList.options[i-offset].value)
							bankLevelList.options[j].style.color ="black";
					}					
					selectBankLevelList.remove(i-offset);			
					offset++;
				}
			}
		}
		
		//保存--提交数据
		function submitData(){
			//选中功能列表框
			var selectOrgList= document.getElementById('selectOrg');
			//选中功能列表框中的项目
			var selectOrgSelOptions = selectOrgList.options;
			//选中行级列表框
			var selectBankLevelList = document.getElementById('selectBankLevel');
			//选中行级列表框中的项目
			var selectBankLevelOptions = selectBankLevelList.options;
			
			//已经选中的机构id字符串(中间用“，”号隔开)
			var selectOrgIds ="";
			//已经选中的机构名称字符串(中间用“，”号隔开)
			var selectOrgNames="";
			//已经选中的行级id字符串(中间用“”号隔开)
			var selectBankLevelIds="";
			//已经选中的行级名称字符串(中间用“”号隔开)
			var selectBankLevelNames="";
			
			if(selectOrgSelOptions.length==0 && selectBankLevelOptions.length==0){
				alert('该用户组必须具备一个行级或子行权限！');
				return;
			}
			
			for(var i=0; i<selectOrgSelOptions.length; i++){
				selectOrgIds += (selectOrgList.options[i].value + ",");
				selectOrgNames += (selectOrgList.options[i].text + ",");
			}			
			for(var i=0; i<selectBankLevelOptions.length; i++){
				selectBankLevelIds += (selectBankLevelList.options[i].value + ",");
				selectBankLevelNames += (selectBankLevelList.options[i].text + ",");
			}
			
			//去处字串最后的“，”号
			selectOrgIds = selectOrgIds.substring(0,selectOrgIds.lastIndexOf(","));	
			selectOrgNames = selectOrgNames.substring(0,selectOrgNames.lastIndexOf(","));
			selectBankLevelIds = selectBankLevelIds.substring(0,selectBankLevelIds.lastIndexOf(","));
			selectBankLevelNames = selectBankLevelNames.substring(0,selectBankLevelNames.lastIndexOf(","));
			
			//把连成后的字串给隐藏域
			document.form2.selectOrgIds.value = selectOrgIds;	
			document.form2.selectOrgNames.value = selectOrgNames;	
			document.form2.selectBankLevelIds.value = selectBankLevelIds;
			document.form2.selectBankLevelNames.value = selectBankLevelNames;

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
		powDes = "（审核权限）";
	else if(powType.equals(Config.POWERTYPEREPORT.toString()))
		powDes = "（报送权限）";
	else if(powType.equals(Config.POWERTYPESEARCH.toString()))
		powDes = "（查看权限）";               			                 			
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
			 <td>当前位置 >> 权限管理 >>用户组权限修改</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	
	<br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				用户组名称: &nbsp;
				<logic:present name="UserGrpNm" scope="request">					
					<%=userGrpNm%>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>机构权限设置<%=powDes%></strong>
                 </div>
                </div>
			</td>
		</tr>	
	</table>
	<html:form action="/popedom_mgr/viewMUserOrg" method="post" styleId="form1">
		<table width="60%" border="0" align="center">
			<tr>
				<td width="38%" align="center" valign="middle">按行级设定</td>
			    <td width="23%">
			    <td width="39%" align="center" valign="middle">已设定的行级</td>
			</tr>
			<tr>
				<td align="center">
					<p>
						<html:select styleId="bankLevelList" property="bankLevelList" size="10" multiple="true" style="width:190">
							<logic:present name="UserGrpBankLevel" scope="request">
								<html:optionsCollection name="UserGrpBankLevel" label="label" value="value" />
					  		</logic:present>
						</html:select>
				    </p>
				</td>
		        <td align="center" valign="middle">
			        <p>
			       		 <html:button property="add" value="→添加" styleClass="input-button" onclick="addBankLevel()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="←删除" styleClass="input-button" onclick="delBankLevel()"/>
			        </p>
		        </td>
		        
		      	<td align="center">
			          <html:select styleId="selectBankLevel" property="selectBankLevel" size="10" multiple="true"  style="width:190">
			          	<logic:present name="UserGrpBankLevelPopedom" scope="request">
			          		<html:optionsCollection name="UserGrpBankLevelPopedom" label="label" value="value"/>
			          	</logic:present>
			          </html:select>
		        </td>
			</tr>
		</table><br>
		<table width="60%" border="0" align="center">
			  <tr>
				  <td width="38%" align="center" valign="middle">按机构设定</td>
			      <td width="23%">
			      <td width="39%" align="center" valign="middle">已设定的机构</td>
			    </tr>
		    	<tr>
			      <td align="center">
			      	<p>
					  <html:select styleId="orgList" property="orgList" size="15" multiple="true" style="width:190">
					  		<logic:present name="LowerOrgNetList"scope="request">
					  			<html:optionsCollection name="LowerOrgNetList" label="label" value="value" />
					  		</logic:present>
					  </html:select>
				    </p>
			      </td>
		        <td align="center" valign="middle">
			        <p>
			       		 <html:button property="add" value="→添加" styleClass="input-button" onclick="addOrg()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="←删除" styleClass="input-button" onclick="delOrg()"/>
			        </p>
		        </td>
		        
		      	<td align="center">
			          <html:select styleId="selectOrg" property="selectOrg" size="15" multiple="true"  style="width:190">
			          	<logic:present name="UserGrpOrgPopedom" scope="request">
			          		<html:optionsCollection name="UserGrpOrgPopedom" label="label" value="value"/>
			          	</logic:present>
			          </html:select>
		        </td>
		    </tr>
		  </table>
  </html:form>
	  <html:form action="/popedom_mgr/viewUserGrpRepPopedom" method="post" styleId="form2">
	  	   
	  	   <input type="hidden" name="userGrpNm" value="<%=userGrpNm%>">
		   <input type="hidden" name="userGrpId" value="<bean:write name="UserGrpId"/>">
		   <input type="hidden" name="powType" value="<bean:write name="powType"/>">
		   
		   <input type="hidden" id="selectOrgNames" name="selectOrgNames">
		   <input type="hidden" id="selectOrgIds" name="selectOrgIds">
		   <input type="hidden" id="selectBankLevelIds" name="selectBankLevelIds">
		   <input type="hidden" id="selectBankLevelNames" name="selectBankLevelNames">
		   <%
						String curPage=null;
						if(request.getAttribute("curPage")!=null){
							curPage=(String)request.getAttribute("curPage");
						}
						else
							curPage="1";
			%>
			<input type="hidden" name="curPage" value="<%=curPage%>">
		   <table width="80%"  border="0" align="center">
		   		<tr>
		   			<td>
		   				<div id=location></div>
		   			</td>
		   		</tr>
 		  		<tr>
   					<td align="right">
   						<html:button property="next" value="下一步" styleClass="input-button" onclick="submitData()"/>&nbsp;
   						<%if(Config.ROLE_GROUP_UNION_FLAG!=1){ %>
  						<input type="button" property="back" value="返回" class="input-button" onclick="location.href='<%=request.getContextPath() %>/popedom_mgr/viewMUserGrp.do'"/>
   					<%}else{ %>
  						<input type="button" property="back" value="返回" class="input-button" onclick="location.href='<%=request.getContextPath() %>/popedom_mgr/viewRole.do'"/>
   					<%} %>
   					</td>
   				</tr>
 		  </table>
 		  </html:form>
</body>
</html:html>
