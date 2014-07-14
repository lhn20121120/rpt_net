<%@ page language="java"  pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.gather.common.StringUtil"%>
<%
   String reason=request.getParameter("reason");
          if(reason==null){
			  reason="";
		  }else{
		      reason=new String(reason.getBytes("iso8859-1"), "gb2312");
		      //reason=reason;
			  //if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==1){
				//  reason=new String(reason.getBytes("iso-8859-1"), "gb2312");
				//}
		  }
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
</head>
<body  bgcolor="336699">
  <div align="center">
   <table width="100%" border='0'>
      <tr>
	     <td align="center"><font color="ffffff">具体原因</font></td>
	  </tr>
	   <tr height='150'>
	     <td align="left" valign="top"><font color="ffffff"><%=reason%></font></td>
	  </tr>
	  <tr>
	     <td align="center">
		    <a href="javascript:window.close()"><font color="ffffff">关闭窗口</font></a>
		 </td>
	  </tr>
   </table>
   </div>
   </body>
</html>