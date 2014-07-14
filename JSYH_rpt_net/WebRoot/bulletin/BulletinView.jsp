<%@ page language="java" pageEncoding="GBK"%>

<jsp:directive.page import="java.sql.Connection"/>
<jsp:directive.page import="com.cbrc.smis.dao.DBConn"/>
<jsp:directive.page import="java.sql.ResultSet"/><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
<%
String bullId=request.getParameter("bullId");



%>
<%

				DBConn dbconn=new DBConn();
String bullContent="";
String bullTitle="";
String addTime="";
String uploadFileName="";
String virtualName="";

				try{
	
		Connection conn=dbconn.openSession().connection();
		ResultSet rs=conn.createStatement().executeQuery("select BULL_ID,BULL_TITLE,BULL_STATE,BULL_CONTENT,ADD_TIME,UPLOAD_FILENAME,VIRTUAL_NAME from BULLETIN where BULL_ID="+bullId);
		
		if(rs.next()){
			bullContent=rs.getString("BULL_CONTENT");
			bullTitle=rs.getString("BULL_TITLE");
			addTime=rs.getString("ADD_TIME");
			uploadFileName=rs.getString("UPLOAD_FILENAME");
			virtualName=rs.getString("VIRTUAL_NAME");
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbconn.closeSession();
		}
		%>
<title> 系统公告！</title>
</head>

<body bgcolor="#FFFFFF">
<table width="470" border="0" cellpadding="0" cellspacing="0" style="background-repeat:no-repeat; background-position:left top;">
  <tr> 
    <td height="4"></td>
  </tr>
  <tr> 
    <td width="490" height="22" background="<%=request.getContextPath()%>/bulletin/image/bg_md_21.gif" class="cpx_ffffff" style="text-indent:27px;"><div align="left"><strong><font color="#1A82D1"><font color="#DD0000" font-size="14px"><strong> 
        <%=bullTitle %></strong></font><br />
        </font></strong></div></td>
  </tr>
  <tr> 
    <td height="10"></td>
  </tr>
  <tr style="background:url(<%=request.getContextPath()%>/bulletin/image/bg_08.gif) no-repeat 0% 0%"> 
    <td ><div style="margin:1px 8px;line-height: 160%;"> 
        <div align="left"> 
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr> 
              <td height="10"></td>
            </tr>
            <tr> 
              <td > 
                <div style="margin:5px 15px;line-height: 160%;"> 
                  <p><%=bullContent %> </p>
                  <%if(uploadFileName!=null&&!uploadFileName.equals("")) {%>
                    <p align=right>附件：<a href="<%=request.getContextPath() %>/servlets/DownloadServlet?filePath=<%=virtualName %>&fileName=<%=uploadFileName %>"><%=uploadFileName %></a></p>
                    <%} %>
                   <p align=right><%=addTime %></p>
                </div>
                </td>
            </tr>

            <tr> 
              <td height="10" background="<%=request.getContextPath()%>/bulletin/image/gonggaodot.gif"></td>
            </tr>
          </table>
        </div>
      </div></td>
  </tr>
  <tr> 
    <td align="center"><a href="javascript:window.close()"><img src="<%=request.getContextPath()%>/bulletin/image/img_close.gif" width="81" height="19" vspace="4"  border="0" align="absmiddle"></a></td>
  </tr>
</table>
</body>
</html>
 
