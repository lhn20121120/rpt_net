<html>
<head>
<title>collect_state</title>

<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<link href="../../../css/index.css" rel="stylesheet" type="text/css" />
<link href="../../../css/common0.css" rel="stylesheet" type="text/css" />
<link href="../../../css/common.css" rel="stylesheet" type="text/css" />
<link href="../../../css/table.css" rel="stylesheet" type="text/css" />
<link href="../../../css/globalStyle.css" rel="stylesheet"
	type="text/css" />
<link href="../../../css/thd.css" rel="stylesheet" type="text/css" />
<link href="../../../css/animate/theme/jquery-ui-1.8.11.redmond.css"
	rel="stylesheet" />
	<script type="text/javascript">
	function getChecked(){
			var nodes = $('#jigouqu').tree('getChecked');
			var s = '';
			for(var i=0; i<nodes.length; i++){
				if (s != '') s += ',';
				s += nodes[i].text;
			}
			alert(s);
		}
		
		$(function(){
			$('#jigouqu').tree({
				checkbox: true,
				cascadeCheck:false,
				url: '<%=request.getContextPath()%>/json/tree_data_<%=userId%>.json',
				onClick:function(node){
					$(this).tree('toggle', node.target);
				},
				onContextMenu: function(e, node){
					e.preventDefault();
					$('#jigouqu').tree('select', node.target);
					$('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				}
			});
		});
	
	
	
	
	</script>
</head>
<body>

	<div style="width:100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td nowrap="nowrap">
					<div class="toolbar">
						<div class="tableinfo">
							<span><img src="../../../images/icon01.gif" /> </span><b>当前位置
								>> 系统管理 >> 任务定义 >> 流程定义</b>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<span align="left">分行业务部门填报</span>
	<div align="center">
			<fieldset style="width:600px ">
		<form>
				<legend>流程定义</legend>

				<br />

				<table border="0" cellspacing="20" cellpadding="10"
					style="width: 100%">
					<tr>
						<td colspan="">节点名称&nbsp;&nbsp;&nbsp; <input value="人行季报第一批次" />
						</td>
						<td colspan="">报送时间&nbsp;&nbsp;&nbsp; <input value="3" /></td>
					</tr>
				</table>

				<fieldset style="width:550px; " align="center">

					<legend align="center">报表处理</legend>
					<table border="0" cellspacing="20" cellpadding="10"
						style="width: 100%">
						<tr>
							<td width="50%">
								<fieldset style="width: 95%;height: 250px ;align:center">
									<legend>机构区</legend>
									</br>
									<div id="jigouqu"
										style="width: 80%;height: 230px ;background: ; margin-left: 10 ;margin-right: 10 ;margin-bottom: 10;BORDER-BOTTOM-STYLE: groove; BORDER-RIGHT-STYLE: groove; WIDTH: 220px; BORDER-TOP-STYLE: groove; COLOR: #b7b7b7; BORDER-LEFT-STYLE: groove"></div>

								</fieldset>
							</td>
							<td width="50%">
								<fieldset style="width: 95%;height: 250px ">
									<legend>报表区</legend>
									</br>
									<div
										style="width: 80%;height: 230px ;background:  ;margin-left: 10 ;margin-right: 10 ;margin-bottom: 10 ;BORDER-BOTTOM-STYLE: groove; BORDER-RIGHT-STYLE: groove; WIDTH: 220px; BORDER-TOP-STYLE: groove; COLOR: #b7b7b7; BORDER-LEFT-STYLE: groove"></div>

								</fieldset></td>
						</tr>
						<tr>
							<td colspan="2">
								<fieldset style="width: 100%;height: 80px ">
									<legend>处理类型</legend>
									</br>
									<table style="width: 100%">
										<tr>
											<td align="center"><input type="radio" name="leixin">
												&nbsp; &nbsp;填 &nbsp; &nbsp;报 &nbsp; &nbsp;<img
												src="../../../images/bianji.jpg" align="center"></img>
											</td>

											<td align="center"><input type="radio" name="leixin">
												&nbsp; &nbsp;复 &nbsp; &nbsp;核 &nbsp; &nbsp;<img
												src="../../../images/icons/find.png" align="center"></img>
											</td>
											</td>

										</tr>


									</table>
								</fieldset></td>
						</tr>
					</table>
				</fieldset>
				<br />
				<br />
				<fieldset style="width:550px " align="center" height="40px">
					<legend>参与角色</legend>
					</br>&nbsp; &nbsp; &nbsp; &nbsp; 角色列表 ：<img src="../../../images/m5.gif"></img>
					<SELECT>
						<OPTION selected value="填报人员">填报人员</OPTION>
						<OPTION value="审核人员">审核人员</OPTION>
					</SELECT> </br>
					</br>
				</fieldset>

				<br />
				<br />
				<fieldset style="width:550px " align="center" height="40px">
					<legend>查关联任务设置</legend>
					</br>&nbsp; &nbsp; &nbsp; &nbsp; 选择任务 ：<img src="../../../images/icons/document-library.png"></img>
					<SELECT style="width: 150">
						<OPTION value=""></OPTION>
						<OPTION value=""></OPTION>
					</SELECT> </br></br>
					<font color="orange">&nbsp; &nbsp; &nbsp; &nbsp;！这里选择的关联任务将以子任务的形式显示出现在待处理任务下方</font>
					</br></br>
				</fieldset>
				<br />
				<br />
				<table
					style="width: 100% ">
				<tr>
					<td colspan="2" align="center">
						<button>下一步</button></td>
				</tr>
				</table>
			<br/>
			<br/>
		
</from>
</fieldset>
	</div></body>