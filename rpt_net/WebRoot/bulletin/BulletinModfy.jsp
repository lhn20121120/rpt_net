<%@ page language="java" pageEncoding="GBK"%>
<jsp:directive.page import="java.sql.Connection"/>
<jsp:directive.page import="com.cbrc.smis.dao.DBConn"/>
<jsp:directive.page import="java.sql.ResultSet"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
String bullId=request.getParameter("bullId");



DBConn dbconn=new DBConn();
String bullContent="";
String bullTitle="";
String addTime="";
int bullState=0;
				try{
	
		Connection conn=dbconn.openSession().connection();
		ResultSet rs=conn.createStatement().executeQuery("select BULL_ID,BULL_TITLE,BULL_STATE,BULL_CONTENT,ADD_TIME from BULLETIN where BULL_ID="+bullId);
		
		if(rs.next()){
			bullContent=rs.getString("BULL_CONTENT");
			bullTitle=rs.getString("BULL_TITLE");
			addTime=rs.getString("ADD_TIME");
			bullState=rs.getInt("BULL_STATE");
		
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbconn.closeSession();
		}
		%>
<html lang="zh">
	<head>
		<title>��ӹ���</title>
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
				alert('���ⲻ��Ϊ�գ�');
				return false;
			}
			/*if(form.bullContent=''){
				alert('���ݲ���Ϊ�գ�');
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
				��ǰλ�� >> �������
			</td>
	
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
		<table width="100%" height=100% border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td align="right" valign="top">
				<form action="BulletinDo.jsp"    enctype="multipart/form-data"  onsubmit="return _submit(this)" method="post">
				
						<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
							<tr id="tbcolor">
								<th align="center">
									�޸Ĺ���</th>
							</tr>
							<tr>
								<td height="204" align="right" bgcolor="#FFFFFF">
									<table width="100%" border="0">
										<tr>
											<td align="left"><input type="hidden" name="type" value="update"/>
											<input type="hidden" name="bullId" value="<%=bullId %>"/>
												����: <INPUT name="bullTitle" value="<%=bullTitle %>" size="50"/>&nbsp;&nbsp;״̬��
												<select name="bullState">
												<option value="1" <%if(bullState==1) out.print("selected"); %>>��½��ʾ</option>
												<option value="2" <%if(bullState==2) out.print("selected"); %>>��½����ʾ</option>
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
										<textarea  name="bullContent"><%=bullContent %></textarea>
												<script language="javascript1.2">
  generate_wysiwyg('bullContent');
</script>
											</td>

										</tr>
										<tr>
											<td>
												�ϴ�����:<input type="file"  name="file" size="65" class="input-button1">
											</td>
										</tr>
										<tr>
											<td align="right">
												<input type="submit" name="Submit22" value="����"  class="input-button1">
												&nbsp;&nbsp;
												<input type="button" name="button" value="����" class="input-button1" onclick='javascript: history.go(-1)'>
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
