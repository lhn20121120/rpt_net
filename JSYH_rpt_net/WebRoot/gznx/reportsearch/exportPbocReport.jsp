<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>

		<logic:present name="Message" scope="request">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:present>
		<script language="javascript">	

			/**
		     * 人行导出操作
			 */
			function rhExport(filepath,filename){
				window.location="<%=request.getContextPath()%>/servlets/DownloadServlet?filePath=" + filepath+"&fileName="+filename;	
			}
			function _back(){
			var versionFlag=<%=request.getAttribute("versionFlag")%>;
			if(versionFlag==0){
				window.location="<%=request.getContextPath()%>/exportRhAFReport.do?styleFlg=new&<%=request.getAttribute("lastParam")%>";
				}else{
				window.location="<%=request.getContextPath()%>/exportRhAFReport.do?<%=request.getAttribute("lastParam")%>";
				}
			}
	</script>
	</head>
	<body style="TEXT-ALIGN: center">
		<table border="0" width="90%" align="center">
			<tr><td height="3"></td></tr>
			<tr>
				<td>
					当前位置 &gt;&gt; 报表查询 &gt;&gt; 人行报表数据下载
				</td>
			</tr>
		</table>
		<html:form action="/exportPbocAFReport.do" method="POST" styleId="frmChk" >
				
			<TABLE cellSpacing="0" width="98%" border="0" align="center" cellpadding="4">					
				<TR>
					<TD>
						<TABLE cellSpacing="1" cellPadding="5" width="100%" border="0" class="tbcolor">
							<tr class="titletab">
								<th colspan="10" align="center" id="list"><strong>下载列表</strong></th>
							</tr>
							<TR class="middle">
								<TD class="tableHeader" width="30%">文件名</td>						
								<TD class="tableHeader" width="15%">批次	</td>	
								<TD class="tableHeader" width="15%">频度</td>								
								<TD class="tableHeader" width="20%">操作</TD>
							</TR>
							
							<logic:present name="Records" scope="request">
								<logic:iterate id="item" name="Records">
									
									<TR bgcolor="#FFFFFF">									
										<td align="center"><bean:write name="item" property="fileName"/></td>											
										<TD align="center"><bean:write name="item" property="batchName" /></TD>
										<td align="center"><bean:write name="item" property="repFreqName"/></td>
										<td align="center"><a href="javascript:rhExport('<bean:write name="item" property="filePath"/>','<bean:write name="item" property="fileName"/>')">下载	</a></td>							
									</TR>
								</logic:iterate>
							</logic:present>
								
								<tr>
								<th class="tableHeader" width="30%">
								文件名：
								</th>
								<th class="tableHeader" width="30%">
								</th>
								<th class="tableHeader" width="20%">
								</th>
								<th class="tableHeader" width="20%">
								</th>
								</tr>
							<logic:present name="adList" scope="request">
								<logic:iterate id="item1" name="adList">
								<tr align="center">
								<td align="center" width="30%">
									<bean:write name="item1" property="fileName"/>
								</td>
								<td align="center" width="30%">
								   说明文件
								</td>
								<td align="center" width="20%">
								<a href="javascript:rhExport('<bean:write name="item1" property="filePath"/>','<bean:write name="item1" property="fileName"/>')">
									下载
								</a>
								</td>
								</tr>
								</logic:iterate>
							</logic:present>
							
							<logic:notPresent name="Records" scope="request">
								<tr bgcolor="#FFFFFF">
									<td colspan="10">暂无记录</td>
								</tr>
							</logic:notPresent>
							
						</TABLE>
						<table align="center" height="20">
						<tr>
						</tr>
						<tr>
							<td>
								<INPUT type="button" value="返 回" class="input-button" onclick="_back()">
							</td>
							</tr>
						</table>
					</TD>
				</TR>
			</TABLE>	
		</html:form>
	</body>
</html:html>
