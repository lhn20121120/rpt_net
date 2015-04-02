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
					</div></td>
			</tr>
		</table>
	</div>
	<br />
	<br />
	<br />
	<div align="center">
		<fieldset style="width:600px ">
			<form>
				<legend> 选择报表模板 </legend>
				<br/><br/><br/>
							<table width="78%" border="0" align="center" cellspacing="20"
					cellpadding="10">
				
					<tr>

					
						<td align="center" width="38%" >
											<fieldset style="width:300px ">
							<legend> 模板类型列表 </legend>
							<br/>
							<div style="width:300 ;height:280" align="center">
						<html:select styleId="allReport"
								property="childRepId" size="18" multiple="true" 
								style="width:280">

							</html:select></td>
							</fieldset>
							</div>
						<td align="center" valign="middle" width="23%">
							<p>
								<html:button property="add" value="→添加"
									styleClass="input-button" onclick="addReport()" />
							</p>
							<br/><br/><br/>
							<p>
								<html:button property="delete" value="←删除"
									styleClass="input-button" onclick="delReport()" />
							</p></td>

						<td align="center" width="39%">
						<fieldset style="width:300px ">
							<legend> 模板类型选中 </legend>
							<br/>
							<div style="width:300 ;height:280" align="center">
						<html:select styleId="selectReport"
								property="selectRepList" size="18" multiple="true"
								style="width:280">
								<logic:present name="UserGrpRepPopedom" scope="request">
									<html:optionsCollection name="UserGrpRepPopedom" label="label"
										value="value" />
								</logic:present>
							</html:select>
							</div>
							</fieldset>
							</td>
					</tr>
					<tr align="center">
					<td><button>上一步</button></td><td></td><td><button>下一步</button></td>
					</tr>
				</table>

				</from>
		</fieldset>
	</div>
</body>