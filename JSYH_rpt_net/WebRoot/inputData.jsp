<%@ page contentType="text/html; charset=gb2312" language="java" import="com.cbrc.smis.system.cb.Control" import="com.cbrc.smis.common.Config" import="java.io.File" errorPage=""%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title>
			�ޱ����ĵ�
		</title>
		<style type="text/css">
<!--
.style1 {font-size: 12px}
.style2 {font-size: 18px}
body {
	background-color: #ECECFF;
}
-->
        </style>
	</head>

	<body>
		<center>
			<h1>
				�����������
				<span class="style1">
					�����ɼ�ϵͳ
				</span>
			</h1>
		</center>
		<hr>
		<h6 align="center" class="style1">&nbsp;
			
		</h6>

		<div align="right">
			<p>
				<%String s = request.getParameter("begin");

			if (s != null) {
				try{
					new Control().conductZips();
				}catch(Exception e){
					e.printStackTrace();
				}
				out.println("<script language='javascript'>alert('����������');</script>");
			}
			
			String name = com.cbrc.smis.common.Config.ADDRESSZIP;

			File file = new File(name);

			File[] files = file.listFiles();

			int length = files.length;

			if (length != 0) {

			%>
				<A HREF="inputData.jsp?begin=y" class="style2">
					������ǩ���
				</A>
				
			</p>

			<p align="center">
				����ָ����ZIPĿ¼�¹���<%=length%>��ZIP�ļ�!
			</p>

			<%} else {

			%>
			<p align="center">
				����ָ����ZIPĿ¼��û��ZIP�ļ�
			</p>

			<%}%>
			<p align="center">&nbsp;
				
			</p>
		</div>
	</body>
</html>
