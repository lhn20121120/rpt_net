<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<html:base/>		
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">		
		<link href="../css/common.css" rel="stylesheet" type="text/css">
<script language="javascript">
			/**
			 * 删除文件
			 */
			 function _delete(infoFilesId)
			 {
			   if(confirm("你确定要删除当前文件吗?")==true)
			   {
			          window.location="<%=request.getContextPath()%>/delInfoFiles.do?infoFilesId=" +infoFilesId+ 
			       "&curPage=" + curPage + "&path=viewUpinfoFiles.do"+
			       (term==""?"":"&" + term);
			   }
			 }
			 /**
			  * 下载文件
			  */
			function _down(infoFilesId)
			{
			    window.location="<%=request.getContextPath()%>/servlets/DownLoadInfoFiles?infoFilesId="+infoFilesId;
			}
		
		<logic:present name="ApartPage" scope="request">
		  <logic:lessEqual name="ApartPage" property="curPage" value="0">
		    var curPage=1;
		  </logic:lessEqual>
		  <logic:greaterThan name="ApartPage" property="curPage" value="0">
		    var curPage=<bean:write name="ApartPage" property="curPage"/>;
		  </logic:greaterThan>
		  <logic:equal name="ApartPage" property="term" value="">
		    var term="";
		  </logic:equal>
		  <logic:notEqual name="ApartPage" property="term" value="">
		    var term="<bean:write name='ApartPage' property='term'/>";
		  </logic:notEqual>
		</logic:present>
		<logic:notPresent name="ApartPage" scope="request">
		  var curPage=1;
		  var term="";
		</logic:notPresent> 
</script>
	</head>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
		<table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr><td height="8"></td></tr>
			<tr background="../image/inside_index_bg4.jpg">
				 <td>当前位置 >> 	信息交换 >> 信息查看</td>
			</tr>
			<tr>
				<td height="1" background="../image/line_bg.gif"></td>
			</tr>
			<tr>
				
			</tr>
		</table><br>
		<tr>
			   <td><br>
			   <table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor" align="center">
			       <tr class="titletab">
				     <th align="center" colspan="4">上传文件</th>
			       </tr>
			       <tr bgcolor="#FFFFFF">
				     <td width="20%">
				       <html:form method="post" action="/upInfoFiles" enctype="multipart/form-data">			
                        	 <div align="center">
                        	      <html:file  property="infoFile" size="80" styleClass="input-button"/>
		                          <html:submit styleClass="input-button" value="上传"/>
                        	</div>
                      </html:form>
                     </td>
			     </tr>
		        </table>
			   </td>
			</tr><br><br><br>
		<table width="80%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th width="10%" align="center">上传人</th>
				<th width="30%" align="center">文件名</th>
				<th width="20%" align="center">上传时间</th>
				<th width="20%" align="center">文件大小</th> 
				<th width="20%" align="center">操作</th>
			</tr>
			<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records">
		    <TR>
					<TD align="center" bgcolor="#ffffff">
						<logic:notEmpty name="item" property="userName">
							<bean:write name="item" property="userName"/>
						</logic:notEmpty>
						<logic:empty name="item" property="userName">
							无
						</logic:empty>
					</TD>
					<TD align="center" bgcolor="#ffffff">
						<logic:notEmpty name="item" property="infoFileName">
							<bean:write name="item" property="infoFileName"/>
						</logic:notEmpty>
						<logic:empty name="item" property="infoFileName">
							无
						</logic:empty>
					</TD>
					<TD align="center" bgcolor="#ffffff">
					   	<logic:notEmpty name="item" property="recordTime">
							<bean:write name="item" property="recordTime"/>
						</logic:notEmpty>
						<logic:empty name="item" property="recordTime">
							无
						</logic:empty>
					</TD>
					<TD align="center" bgcolor="#ffffff">
						<logic:notEmpty name="item" property="infoFileSize">
							<bean:write name="item" property="infoFileSize"/>
						</logic:notEmpty>
						<logic:empty name="item" property="infoFileSize">
							无
						</logic:empty>
					</TD>
					<TD align="center" bgcolor="#ffffff">
						<a href="javascript:_down(<bean:write name="item" property="infoFilesId"/>)">下载</a> <a href="javascript:_delete(<bean:write name="item" property="infoFilesId"/>)">删除</a>
					</TD>
			</TR>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="5">
						无匹配记录
				</td>
			</tr>
		</logic:notPresent>			
		</table>
		<TABLE  cellSpacing="0" cellPadding="0" width="80%" border="0">
			<TR>
				<TD>
					<jsp:include page="../apartpage.jsp" flush="true">
				  		<jsp:param name="url" value="../viewUpInfoFiles.do"/>
				  	</jsp:include>
				</TD>
			</tr>
		</TABLE>
		</body>
</html:html>