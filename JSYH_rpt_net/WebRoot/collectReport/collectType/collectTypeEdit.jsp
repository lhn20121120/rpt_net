<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<jsp:useBean id="utilForm" scope="page" class="com.cbrc.org.form.UtilForm" />

<html:html locale="true">
<head>
	<html:base/>
		<title>修改汇总方式</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<%
		String collectName = request.getAttribute("collectName") != null ? request.getAttribute("collectName").toString() : "";
	%>
	<script language="javascript">

		//增加汇总方式关联机构
		function addOrg(){
			//子行列表框
			var orgList = document.getElementById('collectOrgs');			
			//选中机构列表框
			var selectOrgList= document.getElementById('selectedCollectOrgs');			
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
	
		//删除汇总方式关联机构
		function delOrg(){
			//机构列表框
			var orgList = document.getElementById('collectOrgs');			
			//选中机构列表框
			var selectOrgList= document.getElementById('selectedCollectOrgs');			
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
		
		//增加汇总方式关联报表
		function addReport(){
			//子行列表框
			var reportList = document.getElementById('collectReports');			
			//选中机构列表框
			var selectReportList= document.getElementById('selectedCollectReport');			
			//选中机构列表框中的项目
			var selectReportSelOptions = selectReportList.options;			
			//所有机构列表框中的项目
			var reportListOptions = reportList.options;
			
			for(var i=0; i<reportListOptions.length; i++){
				if(reportList.options[i].selected){
					//查看是否已经选中过
					var isExit = false; 
					for (var j=0;j<selectReportList.length;j++){
						if(reportList.options[i].value==selectReportList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false){
						selectReportList.options[selectReportList.length] = new Option(reportList.options[i].text,reportList.options[i].value);
			    		reportList.options[i].style.color ="gray";
			    	}
			    }					
			}
		}
	
		//删除汇总方式报表
		function delReport(){
			//机构列表框
			var reportList = document.getElementById('collectReports');			
			//选中机构列表框
			var selectReportList= document.getElementById('selectedCollectReport');			
			//选中机构列表框中的项目
			var selectReportSelOptions = selectReportList.options;			
			//所有机构列表框中的项目
			var reportListOptions = reportList.options;			
			var optionLen = selectReportSelOptions.length;
			var offset = 0; 
			
			for(var i=0; i<optionLen; i++){
				if(selectReportList.options[i-offset].selected){	
					//改变全部机构框中对应的颜色
					for(var j=0; j<reportListOptions.length; j++){
						if(reportList.options[j].value==selectReportList.options[i-offset].value)
							reportList.options[j].style.color ="black";
					}					
					selectReportList.remove(i-offset);			
					offset++;
				}
			}
		}
		
		//保存--提交数据
		function submitData(){
			var collectName = document.getElementById("collectName");
			if(collectName.value.Trim() == ""){
				collectName.focus();
				alert("请输入汇总方式名称!");
				return;
			}
			
			//选中机构列表框
			var selectOrgList = document.getElementById('selectedCollectOrgs');
			//选中机构列表框中的项目
			var selectOrgSelOptions = selectOrgList.options;			
			//已经选中的机构id字符串(中间用“，”号隔开)
			var selectOrgIds ="";
			
			if(selectOrgSelOptions.length==0){
				alert("请选择该汇总方式需要汇总的机构！");
				return;
			}
			//选中的报表列表框
			var selectReportList = document.getElementById('selectedCollectReport');
			var selectReportSelOptions = selectReportList.options;
			var selectReports = "";
			
			if(selectReportSelOptions.length == 0){
				alert("请选择该汇总方式需要汇总的报表！");
				return;
			}
			
			for(var i=0; i<selectOrgSelOptions.length; i++){
				selectOrgIds += (selectOrgList.options[i].value + ",");				
			}
			
			for(var i=0;i<selectReportSelOptions.length;i++){
				selectReports += (selectReportList.options[i].value + ",");				
			}
			
			//去处字串最后的“，”号
			selectOrgIds = selectOrgIds.substring(0,selectOrgIds.lastIndexOf(","));	
			selectReports = selectReports.substring(0,selectReports.lastIndexOf(","));
			
			//把连成后的字串给隐藏域			
			document.form1.collectOrgId.value = selectOrgIds;				
			document.form1.childRepId.value = selectReports;
			
			document.form1.submit();
		}
		
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}
		function _goBack(){
			window.location = "<%=request.getContextPath()%>/collectType/viewCollectType.do";
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
	<br>
	<table width="80%" border="0" align="center">		
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>
                 		<logic:present name="orgName" scope="request">
			          		<bean:write name="orgName"/>
			          	</logic:present>
                 		汇总方式设置
                 	</strong>
                 </div>
                </div>
			</td>
		</tr>	
	</table>
	<html:form action="/collectType/updateCollectType" method="post" styleId="form1">	
		<input type="hidden" id="childRepId" name="childRepId">
		<input type="hidden" id="collectOrgId" name="collectOrgId">
		<input type="hidden" id="orgId" name="orgId" value="<bean:write name="orgId"/>">
		<input type="hidden" id="collectId" name="collectId"  value="<bean:write name="collectId"/>"/>
		
		<table width="60%" border="0" align="center">
			<tr align="left">
				<td>汇总方式名称：<html:text property="collectName" size="30" styleClass="input-text" maxlength="20" value="<%=collectName%>"/></td>
			</tr>
		</table><br>	
		<table width="60%" border="0" align="center">
			<tr>
				<td width="38%" align="center" valign="middle">全部子行</td>
			    <td width="23%">
			    <td width="39%" align="center" valign="middle">已选择需要汇总机构</td>
			</tr>
			<tr>
				<td align="center">
					<p>
						<html:select styleId="collectOrgs" property="collectOrgs" size="10" multiple="true" style="width:220">
							<logic:present name="subOrgList"scope="request">
								<html:optionsCollection name="subOrgList" label="label" value="value" />
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
			          <html:select styleId="selectedCollectOrgs" property="selectedCollectOrgs" size="10" multiple="true"  style="width:220">
			          	<logic:present name="selectedSubOrgs" scope="request">
			          		<html:optionsCollection name="selectedSubOrgs" label="label" value="value"/>
			          	</logic:present>
			          </html:select>
		        </td>
			</tr>
		</table><br>		
		<table width="60%" border="0" align="center">
			<tr>
				<td width="38%" align="center" valign="middle">全部报表</td>
			    <td width="23%">
			    <td width="39%" align="center" valign="middle">已选择需要汇总报表</td>
			</tr>
			<tr>
				<td align="center">
					<p>
						<html:select styleId="collectReports" property="collectReports" size="20" multiple="true" style="width:220">
							<logic:present name="reportList"scope="request">
								<html:optionsCollection name="reportList" label="label" value="value" />
					  		</logic:present>
						</html:select>
				    </p>
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
			          <html:select styleId="selectedCollectReport" property="selectedCollectReport" size="20" multiple="true"  style="width:220">
			          	<logic:present name="selectedReportList" scope="request">
			          		<html:optionsCollection name="selectedReportList" label="label" value="value"/>
			          	</logic:present>
			          </html:select>
		        </td>
			</tr>
		</table>
		<table width="80%"  border="0" align="center">
			<tr>
				<td><div id=location></div></td>
		   	</tr>
 		  	<tr>
   				<td align="right">
   					<html:button property="save" value="保存修改" styleClass="input-button" onclick="submitData()"/>&nbsp;   					
  					<html:button property="back" value="返    回" styleClass="input-button" onclick="javascript:_goBack()"/>
   				</td>
   			</tr>
		</table>
	</html:form>
</body>
</html:html>
