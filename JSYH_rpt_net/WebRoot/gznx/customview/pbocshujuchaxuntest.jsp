<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.*,java.io.*,com.fitech.gznx.form.CustomViewForm,java.util.List,org.apache.struts.util.LabelValueBean"%>
<%@page import="com.cbrc.smis.form.MCurrForm"%>
<%@page import="com.cbrc.smis.form.MActuRepForm"%>
<%@page import="com.cbrc.smis.form.MChildReportForm"%>
<%@page import="com.cbrc.smis.form.MRepFreqForm"%>
<%@page import="com.cbrc.smis.common.Config"%>
<%@page import="com.cbrc.smis.security.Operator"%>
<%@page import="com.cbrc.smis.form.MDataRgTypeForm"%>
<%@page import="com.fitech.gznx.form.AFTemplateForm"%>
<%

	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	CustomViewForm customViewForm = new CustomViewForm();
	if (request.getAttribute("form") != null) {
		customViewForm = (CustomViewForm) request.getAttribute("form");
	}
	
	Operator operator = null;
	if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
		operator = (Operator) session
				.getAttribute(Config.OPERATOR_SESSION_NAME);
	String userId=String.valueOf(operator.getOperatorId());
	
	String orgfilename = (String)request.getAttribute("xmlorgname");
	
	String excelPath =Config.RAQ_TEMPLATE_PATH+"templateFiles" + File.separator +"excel" + File.separator;
	excelPath=excelPath.replace("\\","\\\\");
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
	 <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/script/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/script/easyui-lang-zh_CN.js"></script>

	<style type="text/css">
		#innerFinalDIV td,#findLastDIV td{
			border:1px dotted #c4c4c4;
			border-top:0px;
			padding:4px 0px 4px 0px;
			border-right:0px;
		}
		#innerFinalDIV th,#findLastDIV th{
			background-color:#F8F8F8;
			font-size:12px;
			font-family:宋体,Arial;
			font-weight:normal;
			border-left:1px dotted #c4c4c4;
			border-bottom:1px dotted #c4c4c4;
			padding:4px 4px 4px 4px;
		}
	</style>
	
	<script>
		var ISSELECTED = false;//是否已选择报表全局变量
		function showEditOnline(){
			var url = "../FitEditOnline/excelToHtml!editOnline.action?tempFilePath=0101_1010_01900_1_1_20120731.xls"
			var mes = window.showModalDialog(url,null,"dialogWidth:1024px;dialogHeight:768px;resizable:yes;scroll:yes");
			var isHave = false;
			$("#rightList option").each(function(){
				if($(this).val()==mes.toString())
					isHave = true;
			});
			if(!isHave){
				var op = $("<option value='"+mes+"'>GO1-"+mes+"</option>");
				$("#rightList").append(op);
			}
		}

		function getChecked(){
			var nodes = $('#tt2').tree('getChecked');
			var s = '';
			for(var i=0; i<nodes.length; i++){
				if (s != '') s += ',';
				s += nodes[i].text;
			}
			alert(s);
		}
		
		$(function(){
			$('#tt2').tree({
				checkbox: true,
				cascadeCheck:false,
				url: '<%=request.getContextPath()%>/json/tree_data_<%=userId%>.json',
				onClick:function(node){
					$(this).tree('toggle', node.target);
				},
				onContextMenu: function(e, node){
					e.preventDefault();
					$('#tt2').tree('select', node.target);
					$('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				}
			});

			$("#startDate").calendar({
				months:['1月', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']

			});
		});

		/**展开报表*/
		function showEditOnline(obj){
			var param ="<%=excelPath%>";
			var url = "../FitEditOnline/excelToHtml!editOnline.action?tempFilePath="+param+"/"+$(obj).val()+".xls";
			var mes = window.showModalDialog(url,null,"dialogWidth:1024px;dialogHeight:768px;resizable:yes;scroll:yes");
			mes = mes.split("@td@")[0];
			var isHave = false;
			
			$("#shuomingDIV").css("display","none");
			$("#findLastDIV").css("display","block");
			if(mes!=""){
				try{
					var mess = mes.toString();
					//获得模板ID和版本ID
					var objVal = $(obj).val()[0];	
					var templateId = objVal.split("_")[0];
					var versionId = objVal.split("_")[1];
					//异步请求获得列名和行名
					var url = "findColAndRow.do?templateId="+templateId+"&versionId="+versionId+"&cellName="+mes;
					$.post(url,function(colNameAndRowName){
						if(colNameAndRowName!=null && colNameAndRowName!="" && colNameAndRowName.indexOf("_")>0){
							var colName = colNameAndRowName.split("_")[0];//获得列名
							var rowName = colNameAndRowName.split("_")[1];//获得行名
							
							var newTR=$("<tr align='center' onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\"></tr>");
							var newTD_1 = $("<td><input type='checkbox'></td>");//单选
							var newTD_2 = $("<td>"+mes+"<input type='hidden' name='templateIdAndVersionIdAndCellName' value='"+templateId+"_"+versionId+"_"+mes+"' /></td>");
							var newTD_3 = $("<td>"+rowName+"</td>");
							var newTD_4 = $("<td>"+colName+"</td>");
							var newTD_5 = $("<td>"+$("#leftList option:selected").text()+"</td>");
							
							newTR.append(newTD_1);
							newTR.append(newTD_5);
							newTR.append(newTD_2);
							newTR.append(newTD_3);
							newTR.append(newTD_4);
							
							$("#dataTable").append(newTR);	

							ISSELECTED = true;
						}
					});
					
				}catch(e){
					return;
				}
				
			}
			
			
		}
		
		

		
		function deleteData(){
			$("#dataTable td input[type='checkbox']").each(function(){
				if($(this).attr("checked")){
					$(this).parent().parent().empty();
				}
			});
		}

		//查询按钮 查询数据
		function doSearch(){
			//币种隐藏域赋值
			$("#searchForm input[name='mcurrId']").val($('#curId').combobox('getValue'));
			//口径隐藏域赋值
			$("#searchForm input[name='freqId']").val( $('#repFreqId').combobox('getValue'));
			//开始时间隐藏域赋值
			$("#searchForm input[name='startTeam']").val($("#startDate").datebox("getValue"));
			//结束时间
			$("#searchForm input[name='endTeam']").val($("#endDate").datebox("getValue"));
			//机构隐藏域赋值
			var nodes = $('#tt2').tree('getChecked');
			var orgIds = '';
			for(var i=0; i<nodes.length; i++){
				if (orgIds != '') orgIds += ',';
				orgIds += nodes[i].id;
			}

			if(!ISSELECTED){
				alert("请选择报表单元格!");
				return;
			}
			
			if(orgIds==""){
				alert("请选择机构!");
				return;
			}
			$("#searchForm input[name='orgIds']").val(orgIds);
			
			$("#searchForm").submit();
		}
	</script>
</head>
<body class="easyui-layout">

	<div region="north" title="查询条件" split="true" style="height:90px;padding:8px;background:#efefef;">
		<div class="easyui-layout" fit="true" style="background:#ccc;">
		<div region="center" style="padding: 3px">
		<table style="width: 100%;height: 100%;text-align: center;">
			<tr>
				<td style="width:20%;">
					<span style="vertical-align: bottom;">币种：</span><select Id="curId" name="curId" class="easyui-combobox"  editable="false">
					<%
						List<MCurrForm> currList = (List<MCurrForm>)request.getAttribute("currList");
						
						for (MCurrForm m : currList) {
							
							Integer curId = m.getCurId();
							String ccyName = m.getCurName();
					%>
						<option value="<%=curId%>"><%=ccyName%></option>
					<%
						}
					%>
					</select>
				</td>
				<td style="width:20%;">
					<span style="vertical-align: bottom;">口径：</span><select Id="repFreqId" name="repFreqId" class="easyui-combobox" editable="false">
						<%
							List<MDataRgTypeForm> datargList = (List<MDataRgTypeForm>)request.getAttribute("datargList");
							for (MDataRgTypeForm m : datargList) {
								
								Integer dataRangeId = m.getDataRangeId();
								String rgDesc = m.getDataRgDesc();
						%>
						<option value="<%=dataRangeId%>"><%=rgDesc%></option>
						<%
							}
						%>
					</select>
				</td>
				<td style="width:20%;">
					<span style="vertical-align: bottom;">开始时间：</span><input id="startDate" name="startDate"   class="easyui-datebox"  editable="false"></input>
				</td>
				<td>
					<span style="vertical-align: bottom;">结束时间：</span><input id="endDate" name="endDate"   class="easyui-datebox"  editable="false">
				</td>
				<td style="width:20%;text-align: left;">
					<a href="javascript:doSearch()"  class="easyui-linkbutton" plain="true" iconCls="icon-search">查询</a>
				</TD>
			</tr>
		</table>
		</div>
		</div>
	</div>
	<!--  
	<div region="south" title="South Title" split="true" style="height:100px;padding:10px;background:#efefef;">
		<div class="easyui-layout" fit="true" style="background:#ccc;">
			<div region="center">sub center</div>
		</div>
	</div>
	-->
	<div region="east" iconCls="icon-reload" title="机构" split="true" style="width:180px;">
		<ul  class="easyui-tree" id="tt2"></ul>
	</div>
	<div region="west" split="true" maximizable="false" title="报表" style="width:280px;padding1:1px;overflow:hidden;">
		<div class="easyui-accordion" fit="true" border="false">
			<select id="leftList" name="leftList" multiple="multiple" size="16" style="width: 100%;height:100%" ondblclick="showEditOnline(this)">
				<%
					if(request.getAttribute("reportList")!=null && !request.getAttribute("reportList").equals("")){
						List<AFTemplateForm> reportList = (List<AFTemplateForm>)request.getAttribute("reportList");
						if(reportList!=null && reportList.size()>0){
							for(AFTemplateForm m : reportList){
								String repName = m.getTemplateName();
								String versionId = m.getVersionId();
								String reportId = m.getTemplateId();
								String relPath = reportId+"_"+versionId;
				%>
					<option value="<%=relPath %>"><%=repName %> </option>
				<%	
							}
						}
					}
				%>
														
			</select>
			
		</div>
	</div>
	<div region="center" title="单元格" style="overflow:hidden;">
			<div title="Tab1" id="shuomingDIV" style="padding:20px;overflow:hidden;display: block" > 
				<div style="margin-top:20px;">
						<h3>数据查询说明.</h3>
						<li>单击报表，选择单元格，单元格信息将被添加到此区域</li> 
						<li>选择机构</li> 
						<li>选择查询条件，执行查询</li> 
				</div>
			</div>
				<div id="findLastDIV" style="overflow: auto;display: none">
					<form action="searchPBOCDataAction.do" id="searchForm" method="post">
						<table style='width:100%;' id="dataTable" align='center' align='center' cellpadding='0' cellspacing='0' >
							<tr>
								<th colspan="6" align="left">
									<a href="javascript:deleteData()" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
									<input type="hidden" name="mcurrId" /><!-- 币种 -->
									<input type="hidden" name="freqId" /><!-- 口径 -->
									<input type="hidden" name="startTeam"/><!-- 开始时间 -->
									<input type="hidden" name="orgIds"/><!-- 机构 -->
									<input type="hidden" name="endTeam" /><!-- 结束时间 -->
								</th>
							</tr>
							  
							<tr align='center' onmouseover="this.backgroundColor='#EEEEFF'" onmouseout="this.backgroundColor='white'" onclick="checkedData(this)">
								<th style="width: 10px"><input type="checkbox"></th>
								<th>报表名称</th>
								<th>单元格名称</th>
								<th>横向</th>
								<th>纵向</th>
								
							</tr>
							<!--
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="checkedData(this)">
								<td><input type="checkbox"></td>
								<td>E4</td>
								<td>2.1委托代理资金</td>
								<td>人名币</td>
								<td>2.1</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="checkedData(this)">
								<td><input type="checkbox"></td>
								<td>E6</td>
								<td>2.2委托代理资金</td>
								<td>人名币</td>
								<td>2.2</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="checkedData(this)"> 
								<td><input type="checkbox"></td>
								<td>D1</td>
								<td>2.3委托代理资金</td>
								<td>人名币</td>
								<td>2.3</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'" onclick="checkedData(this)">
								<td><input type="checkbox"></td>
								<td>D2</td>
								<td>2.4委托代理资金</td>
								<td>人名币</td>
								<td>2.4</td>
							</tr>
							-->
						</table>
						</form>
					</div>
	</div>
	
</body>
</html>