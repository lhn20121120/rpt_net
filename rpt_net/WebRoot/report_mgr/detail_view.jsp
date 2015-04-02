<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta content="BroadContact Business Software Co." name="Author">
		<LINK href="../css/common.css" type="text/css" rel="stylesheet">
			<script language="javascript" src="../script/globalScript.js" type="text/javascript"></script>
	</head>
	<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" height="24">
			<tr class="tbcolor1" id="tbcolor">
				<th id="list" align="center" width="100%">
					<p align="left"><font face="Arial" size="2">&nbsp; G01资产负债项目统计表，</font><FONT face="Arial" color="#000000" size="2">2004/8/22，北京工行</FONT></p>
				</th>
			</tr>
		</table>
		<table cellSpacing="0" cellPadding="0" width="100%" border="0">
			<tr>
				<td width="50%">
					<table cellSpacing="0" cellPadding="0" width="100%" background="../../image/top.gif" border="0">
						<tr height="200">
							<td noWrap align="left" width="90%" height="33">
								<p class="MsoNormal" style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"><span style="FONT-FAMILY: 宋体">&nbsp; <font color="#000080">表内校验结果：</font></span></p>
							</td>
							<td vAlign="top" noWrap align="left" width="10%" height="33">
								<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px">
								<font color="#FFFFFF">1</font></p>
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="90%" height="31"><span lang="EN-US">&nbsp; 
            [10.]=[1.]+[2.]+[3.]+[4.]+[5.]+[6.]+[7.]+[8.]+[9.] </span></td>
							<td noWrap align="center" width="10%" height="31">
								<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px" align="left"><strong><font face="宋体" color="#ff0000" size="2">×</font></strong></p>
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="90%" height="31">&nbsp; [A]≥[B]</td>
							<td noWrap align="center" width="10%" height="31">
								<p align="left"><strong style="FONT-WEIGHT: 400"><font face="宋体" color="#008000" size="2">√</font></strong></p>
							</td>
						</tr>
					</table>
				</td>
				<td width="50%">
					<table cellSpacing="0" cellPadding="0" width="100%" background="../image/middle.gif" border="0">
						<tr height="200">
							<td noWrap align="left" height="33">
								<p class="MsoNormal" style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px">&nbsp; <font color="#000080">
										<span style="FONT-FAMILY: 宋体">数据异常变化情况：</span></font></p>
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" height="31"><font color="#008000">&nbsp;</font> 数据未超出异常变化标准</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" height="31">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="50%">
					<table cellSpacing="0" cellPadding="0" width="100%" background="../image/bottom.gif" border="0">
						<tr height="200">
							<td noWrap align="left" width="90%" height="34">
								<p class="MsoNormal" style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"><span style="FONT-FAMILY: 宋体">&nbsp; <font color="#000080">表间校验结果：</font></span></p>
							</td>
							<td vAlign="top" noWrap align="left" width="10%" height="34">
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="90%" height="31"><span lang="EN-US">&nbsp; 
            <font face="宋体">[10.A]= </font></span><font face="宋体"><span lang="EN-US" style="COLOR: blue">G01</span></font><span style="COLOR: blue; FONT-FAMILY: 宋体">附注</span><font face="宋体"><span lang="EN-US" style="COLOR: blue">_III</span><span lang="EN-US">_ [2.C]</span></font></td>
							<td noWrap align="left" width="10%" height="31"><strong><font face="宋体" color="#ff0000" size="2">×</font></strong></td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="90%" height="31"><font face="宋体"><span lang="EN-US">&nbsp; [10.B]= </span><span lang="EN-US" style="COLOR: blue">G04</span><span lang="EN-US">_</span></font><span style="FONT-FAMILY: 宋体">［</span><font face="宋体"><span lang="EN-US">27.C</span></font><span style="FONT-FAMILY: 宋体">］</span></td>
							<td noWrap align="left" width="10%" height="31"><strong style="FONT-WEIGHT: 400"><font face="宋体" color="#008000" size="2">√</font></strong></td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="90%" height="31"><font face="宋体">&nbsp; [10.C]= 
									G01附注_III_ [1.C]</font></td>
							<td noWrap align="left" width="10%" height="31"><strong style="FONT-WEIGHT: 400"><font face="宋体" color="#008000" size="2">√</font></strong></td>
						</tr>
					</table>
				</td>
				<td width="50%">
					<table cellSpacing="0" cellPadding="0" width="100%" background="../image/top.gif" border="0">
						<tr height="200">
							<td noWrap align="left" height="34">
								<p class="MsoNormal" style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px">&nbsp; <font color="#000080">
										迟报、漏报检验结果：</font></p>
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" height="31">&nbsp; <b><font color="#ff0000">逾期4天</font></b></td>
						</tr>
						<tr height="200">
							<td noWrap align="left" height="31">
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" height="31">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="50%">
					<table cellSpacing="0" cellPadding="0" width="100%" background="../image/middle.gif" border="0">
						<tr height="200">
							<td noWrap align="left" width="50%" height="36">
								<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"></p>
								<p class="MsoNormal" style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px"><span style="FONT-FAMILY: 宋体">&nbsp; <font color="#000080">报送关系 
											、频率校验结果：</font></span></p>
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="50%" height="35"><span lang="EN-US">&nbsp; 
            </span><span style="FONT-FAMILY: 宋体">范围：（北京工行属于）国有</span><span lang="EN-US"></span></td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="50%" height="35"><span style="FONT-FAMILY: 宋体">&nbsp; 频度：法人汇总数据 
      月报（10）</span></td>
						</tr>
					</table>
				</td>
				<td width="50%">
					<table cellSpacing="0" cellPadding="0" width="100%" background="../image/bottom.gif" border="0">
						<tr height="200">
							<td noWrap align="left" colSpan="2" height="36">
								<p class="MsoNormal" style="MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px">&nbsp; <font color="#000080">
										错报检验结果：</font></p>
							</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" colSpan="2" height="35">&nbsp; 无</td>
						</tr>
						<tr height="200">
							<td noWrap align="left" width="87%" height="35">
							</td>
							<td vAlign="bottom" noWrap align="left" width="13%" height="35"><input class="input-button" id="OK2" onclick="window.history.back()" type="submit" value=" 返 回 "
									name="OK2"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<!--</div>
				</td>
			</tr>
		</table>-->
	</body>
</html:html>
