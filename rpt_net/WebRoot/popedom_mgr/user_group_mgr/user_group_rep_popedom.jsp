<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config,java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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
	List cbrclist =(List)request.getAttribute("cbrc");
	List pboclist =(List)request.getAttribute("pboc");
	List otherlist =(List)request.getAttribute("other");
%>

<jsp:useBean id="utilForm" scope="page"	class="com.fitech.gznx.util.FormUtil" />
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
		var cbrclist = '<%=cbrclist%>';
		var pboclist = '<%=pboclist%>';
		var otherlist = '<%=otherlist%>';
		//增加报表权限
		function addReport()
		{
			//全部报表列表框
			var allReportList = document.getElementById('allReport');
			
			//选中报表列表框
			var selectReportList= document.getElementById('selectReport');
			
			//所有报表列表框中的项目
			var allReportListOptions = allReportList.options;
			
			//选中报表列表框中的项目
			var selectReportOptions = selectReportList.options;
			
			for(var i=0; i<allReportListOptions.length; i++)
			{
				if(allReportList.options[i].selected)
				{
					//查看是否已经选中过
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
	
		//删除报表权限
		function delReport()
		{
			//全部报表列表框
			var allReportList = document.getElementById('allReport');
			
			//选中报表列表框
			var selectReportList= document.getElementById('selectReport');
			
			//所有报表列表框中的项目
			var allReportListOptions = allReportList.options;
			
			//选中报表列表框中的项目
			var selectReportOptions = selectReportList.options;
						
			var optionLen = selectReportOptions.length;
			
			var offset = 0; 
			
			for(var i=0; i<optionLen; i++)
			{
				if(selectReportList.options[i-offset].selected)
				{
					//改变全部功能框中对应的颜色
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
		
		//保存--提交数据
		function submitData()
		{
			//选中报表列表框
			var selectReportList= document.getElementById('selectReport');
			//选中功能列表框中的项目
			var selectReportListOptions = selectReportList.options;
			
			if(selectReportListOptions.length==0)
			{
				alert('该用户组必须具备一个报表权限！');
				return;
			}
			//提取选择的内容，把它连接成字符串，之间用“,”格开
			var selectData ="";
			
			for(var i=0; i<selectReportListOptions.length; i++)
			{
				selectData += (selectReportList.options[i].value + ",");
			}
			selectData = selectData.substring(0,selectData.lastIndexOf(","));
			
			document.form1.selectRepIds.value = selectData;
			<%if(Config.SYSTEM_SCHEMA_FLAG==1&&Config.ROLE_GROUP_UNION_FLAG==1){%>
				document.form1.action="<%=request.getContextPath()%>/popedom_mgr/updateMPurBankLevelToRole.do";
			<%}else{%>
				document.form1.action="<%=request.getContextPath()%>/popedom_mgr/updateMPurBankLevel.do";
			<%}%>
			
			document.form1.submit();
			
		}
		//返回上级菜单
		function back()
		{		
			document.form2.submit();
		}
		
		function changeReport(reportObj){
			var index=reportObj.selectedIndex;
			var reportFlg=reportObj.options[index].value;	
			//全部报表列表框
			var allReportList = document.getElementById('allReport');
			//所有报表列表框中的项目
			var allReportListOptions = allReportList.options;
			
			for(var i=allReportListOptions.length; i>=0; --i)
			{
				allReportList.remove(i);
			}
			
			if(reportFlg=="1"){
				var arrcbrc = cbrclist.split("],");
				for(var i=0;i<arrcbrc.length;i++){
					var cbrcdd = arrcbrc[i];
					cbrcdd = cbrcdd.replace("LabelValueBean[","");
					cbrcdd = cbrcdd.replace("[","");
					cbrcdd = cbrcdd.replace("]]","");
					var cbrcqq = cbrcdd.split(",");

					allReportList.options[i] = new Option("["+cbrcqq[1].Trim()+"]"+cbrcqq[0].Trim(),cbrcqq[1].Trim());
				 } 
			}else if(reportFlg=="2"){
				var arrpboc = pboclist.split("],");
				 for(var i=0;i<arrpboc.length;i++){			
					var cbrcdd = arrpboc[i];
					cbrcdd = cbrcdd.replace("LabelValueBean[","");
					cbrcdd = cbrcdd.replace("[","");
					cbrcdd = cbrcdd.replace("]]","");
					var cbrcqq = cbrcdd.split(",");
					allReportList.options[i] = new Option("["+cbrcqq[1].Trim()+"]"+cbrcqq[0].Trim(),cbrcqq[1].Trim());
				 } 
			}else if(reportFlg=="3"){
				var arrother = otherlist.split("],");
				 for(var i=0;i<arrother.length;i++){			
					var cbrcdd = arrother[i];
					cbrcdd = cbrcdd.replace("LabelValueBean[","");
					cbrcdd = cbrcdd.replace("[","");
					cbrcdd = cbrcdd.replace("]]","");
					var cbrcqq = cbrcdd.split(",");
					allReportList.options[i] = new Option("["+cbrcqq[1].Trim()+"]"+cbrcqq[0].Trim(),cbrcqq[1].Trim());
				 } 
			}
		}
		
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
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
			 <td>当前位置 >> 权限管理 >>报表权限设置</td>
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
				用户组名称: &nbsp;
				<logic:present name="UserGrpNm" scope="request">
	
									<bean:write name="UserGrpNm"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>报表权限设置<%=powDes%></strong>
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
			<table width="78%"  border="0" align="center">
			    <tr>
			      <td width="38%" align="left" valign="middle">      
			      	&nbsp;&nbsp;请选择报表&nbsp;
			      	<select id="reportFlg"  onchange="changeReport(this)">
			      		<option value="0">  </option>
				    	<option value="1">银监会报表</option>
				    	<option value="2">人行报表</option>
				    	<option value="3">其他报表</option>
			    	</select>
			      </td>
			      <td width="23%"> </td>
			     <td width="39%" align="center" valign="middle">可以查看的报表</td>
			    </tr>

		    	<tr>
			      <td align="center">
			      <span style="overflow-x:scroll;width=300px;">
					  <html:select styleId="allReport"  property="childRepId" size="18" multiple="true" style="width:500;overflow-x:scroll">
					  	
				     </html:select>
				  </span>
			      </td>
		        <td align="center" valign="middle">
			        <p>
			       		 <html:button property="add" value="→添加" styleClass="input-button" onclick="addReport()"/>
			        </p>
			        <p>
			        	<html:button property="delete" value="←删除" styleClass="input-button" onclick="delReport()"/>
			        </p>
		        </td>
		        
		      	<td align="center">
		      	  <span style="overflow-x:scroll;width=300px">
		          <html:select styleId="selectReport" property="selectRepList" size="18" multiple="true"  style="width:500;overflow-x:auto">
		          	<logic:present name="UserGrpRepPopedom" scope="request">
		          		<html:optionsCollection name="UserGrpRepPopedom" label="label" value="value" style="overflow:auto;height:100" />
		          	</logic:present>
		          </html:select>  
		          </span>    
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

   					<td align="center">
   						<html:button property="submit" value="保存" styleClass="input-button" onclick="submitData()"/>	   			
		   				<html:button property="back" value="返回" styleClass="input-button" onclick="back()"/>
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
