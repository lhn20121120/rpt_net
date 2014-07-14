<%@ page language="java" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html lang="zh">
	<head>
		<title>添加公告</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css">
		<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath() %>/bulletin/editor/wysiwyg.jsp"></script>
		<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>

		<script language="javascript">
		
		function _submit(form){
			if(form.bullTitle.value.length == 0){
				alert('标题不能为空！');
				form.bullTitle.focus();
				return false;
			}
			/*if(form.bullContent.value.length == 0){
				alert('内容不能为空！');
				form.bullContent.focus();
				return false;
			}*/
			return true;
		}
	</script>

	</head>
	<body>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 公告管理
			</td>
	
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
		<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td align="right" valign="top">
				<form action="BulletinDo.jsp"   enctype="multipart/form-data" method="post" onsubmit="return _submit(this)">
				
						<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
							<tr id="tbcolor">
								<th align="center">
									添加公告</th>
							</tr>
							<tr>
								<td height="204" align="right" bgcolor="#FFFFFF">
									<table width="100%" border="0">
										<tr>
											<td align="left"><input type="hidden" name="type" value="insert"/>
												标题: <INPUT name="bullTitle" value="" size="50"/>&nbsp;&nbsp;状态：
												<select name="bullState">
												<option value="1">登陆提示</option>
												<option value="2">登陆不提示</option>
											</select>
											</td>
									    </tr>							
																
										<tr>
											<td align="right">
												<div id=location>
												  <div align="left"></div>
												</div>
											</td>

										</tr>
										<tr>
										<td>
										<textarea  name="bullContent"></textarea>
												<script language="javascript1.2">
  generate_wysiwyg('bullContent');
</script>
											</td>

										</tr>
										<tr>
											<td>
												上传附件:<input type="file"  name="file" size="65" class="input-button1">
											</td>
										</tr>
										<tr>
											<td align="right">
												<input type="submit" name="Submit22" value="保存"  class="input-button1">
												&nbsp;&nbsp;
												<input type="button" name="button" value="返回" class="input-button1" onclick='javascript: history.go(-1)'>
											</td>
										</tr>

									</table>
								</td>
							</tr>
						</table>
					</form>
					</td>
					</tr>
			</table>
	</body>

</html>
