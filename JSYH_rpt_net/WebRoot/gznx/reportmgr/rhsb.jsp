<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String addflg ="";
	if( request.getAttribute("addflg")!= null){
		addflg =  (String)request.getAttribute("addflg");
	}
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
		function addRow(){
			
			document.getElementById("deleteIndex").value = "-1";
			
			document.getElementById("form2").submit();
		}
		
		function deleteRow(index) {	
			if(confirm("你确定要删除该异常项吗?")){	
				document.getElementById("saveFlg").value ="del";			
				document.getElementById("deletecolId").value =index;
				document.getElementById("form2").submit();
			
			}
		}
		
		function editRow(index) {				
			
			document.getElementById("deleteIndex").value =index;

			document.getElementById("form2").submit();
		}
		
		function save(){
			if(isVaild()){
				document.getElementById("saveFlg").value ="save";
				document.getElementById("form2").submit();
			}
		}
		
		function isVaild(){
			if(isEmpty(document.getElementById("colId"),"列名不能为空！")){
				
				document.getElementById("colId").focus;
				return false;
			}
			if(isEmpty(document.getElementById("curId"),"币种不能为空！")){
			
				document.getElementById("curId").focus;
				return false;
			}
			if(isEmpty(document.getElementById("dataType"),"数据属性不能为空！")){
				
				document.getElementById("dataType").focus;
				return false;
			}
			if(isEmpty(document.getElementById("psuziType"),"数值类型不能为空！")){
	
				document.getElementById("psuziType").focus;
				return false;
			}
			if(isEmpty(document.getElementById("danweiId"),"单位编码不能为空！")){
				
				document.getElementById("danweiId").focus;
				return false;
			}
			return true;
		}
		function isEmpty(obj,msg){
			 	 if(obj.value.replace(" ","")==""){
			 	 	 alert(msg);
			 	 	 return true;
			 	 }else{
			 	   return false;
			 	 }
 			} 
		
			function back(){
				document.getElementById("form2").action="../afreportDefine.do"

				document.getElementById("form2").submit();

			}
			//1.14 yql
			   function _back(){
			   	var templateId = document.getElementById("templateId").value;
			   	var versionId = document.getElementById("versionId").value;
			   	window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+templateId+"&versionId="+versionId+"&bak2=2";
			   }
			   //1.14 yql
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
		<html:form styleId="form2" method="post" action="gznx/addRHReport.do">
		<input type="hidden" name="saveFlg" />
		<div align="center">
				<table border="0"width="95%">
					<tr>
						<td height="8">
						</td>
					</tr>
					<tr>
						 <td>
						 	当前位置 >> 报表管理 >> 模板查看
						 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
					<TR>
					<th colspan="8" align="center" id="list">
					<strong>
						<logic:present name="ReportName" scope="request">
							<bean:write name="ReportName"/>
						</logic:present>
						</strong>
						</th>
					</TR>
				</table>
		</div>
            
			<table cellSpacing="2" width="100%" border="0" align="center"
				cellpadding="5"  class="tbcolor">
				
				<TR class="middle">
					<td class="tableHeader" rowspan="1" width=10%>
						列名
					</td>
					<td class="tableHeader" rowspan="1" width=15%>
						币种
					</td>
					<td class="tableHeader" rowspan="1" width=15%>
						数据属性
					</td>
					<td class="tableHeader" rowspan="1" width=12.5%>
						数值类型
					</td>
					<td class="tableHeader" rowspan="1" width=12.5%>
						单位编码
					</td>

					<td class="tableHeader" rowspan="1" width=12.5%>
						操作
					</td>
				</TR>
				<TR>
					<TD>
						<logic:present name="Records" scope="request">
							<%
								int count = 0;
							%>
							<logic:iterate id="item" name="Records" >
								<tr bgcolor='#FFFFFF' height="20">								
									<logic:equal  name="item" property="flag" value="1">
									<td align="center">
										<logic:present name="rhReportForm" property="colIdList" scope="request">
											<html:select property="colId" styleId="colId" >
												<html:optionsCollection name="item" property="colIdList"/>
											</html:select>
										</logic:present>
										<logic:notPresent name="rhReportForm" property="colIdList" scope="request">
											没有单元格信息
										</logic:notPresent>
									</td>
									<td align="center">
									<html:select property="curId" styleId="curId" >
										<html:optionsCollection name="item" property="curIdList"/>
										</html:select>
										
										
									</td>
									<td align="center">
										<html:select property="dataType" styleId="dataType" >
										<html:optionsCollection name="item" property="dataTypeList"/>
										</html:select>
										
									</td>
									<td align="center">
									<html:select property="psuziType" styleId="psuziType">
										<html:optionsCollection name="item" property="psuziTypeList"/>
										</html:select>
										
									</td>
									<td align="center">
									<html:select property="danweiId" styleId="danweiId" >
										<html:optionsCollection name="item" property="danweiIdList"/>
										</html:select>
										
									</td>
									<td align="center">
										
									</td>
									</logic:equal>
									<logic:notEqual  name="item" property="flag" value="1">
									<td align="center">										
										<bean:write name="item" property="colName" />										
									</td>
									<td align="center">
										<bean:write name="item" property="curName" />
										
									</td>
									<td align="center">
										<bean:write name="item" property="dataTypeName" />
										
									</td>
									<td align="center">
										<bean:write name="item" property="psuziTypeName" />
									</td>
									<td align="center">
										<bean:write name="item" property="danweiName" />
									</td>
									<td align="center">
										<a href="javascript:editRow('<%=count%>');" >编辑</a>
			 							<a href="javascript:deleteRow('<bean:write name="item" property="colId" />');">删除</a> 
									</td>
									</logic:notEqual>
								</tr>
								<%
									    count++;
								%>
							</logic:iterate>
						</logic:present>
						<logic:notPresent name="Records" scope="request">
							<tr bgcolor='#FFFFFF'>
								<td colspan="8" align="left">
									没有符合条件的记录!
								</td>
							</tr>
						</logic:notPresent>
					</TD>
				</TR>
			</table>
			<table border="0" width="100%" align="center">
				<tr>
				
				<% if(!addflg.equals("1")){ %>
					<td align="right">
						<input type="button" class="input-button" value="增加" onclick="addRow()">
					</td>
					<% } %>
					<td align="center">
						<input type="button" class="input-button" value="保存" onclick="save()">
					</td>
					<td align="left">
						<!-- 1.14 yql -->
						<input type="button" class="input-button" value="返回" onclick="_back()">
					</td>
				</tr>
			</table>
			<input type="hidden" name="deleteIndex">
			<input type="hidden" name="deletecolId">
			<logic:present name="ObjForm" scope="request">				
				<input type="hidden" name="templateId" value="<bean:write name='ObjForm' property='childRepId'/>">
				<input type="hidden" name="versionId" value="<bean:write name='ObjForm' property='versionId'/>">
				<input type="hidden" name="templateName" value="<bean:write name='ObjForm' property='reportName'/>" />
			</logic:present>

			
		</html:form>
		
</body>
</html:html>