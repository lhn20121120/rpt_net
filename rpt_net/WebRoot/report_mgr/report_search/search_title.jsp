<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<title>Title</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	</head>
	<body topmargin="0" marginheight="0" leftmargin="0" marginwidth="0" onkeypress="keyEnter()">
		<table class="tabButton" cellspacing="0" cellpadding="0" border="0" style="border-width:0px; BACKGROUND-IMAGE:url('../../image/inside_index_bg4.jpg'); WIDTH:806px; " height="74">
				<TBODY>
					<tr>
						<td align="left" height="50%" vAlign="top" width="121" colSpan="1">
							<p>
							<img height="48" alt="" src="../../image/query.gif" width="115" align="left"></td>
						<td align="center" height="50%" vAlign="bottom" width="116" background="inside_index_bg4.jpg">
							　</td>
						<td align="center" height="50%" vAlign="bottom" width="355" colspan="2" background="../../image/inside_index_bg4.jpg">
							<p align="left">　</td>
						<td align="center" vAlign="bottom" colspan="2" background="../../image/inside_index_bg4.jpg">
							<input name="OK" type="button" class="input-button" id="OK" value=" 查 询 " style="float: right"></td>
					</tr>
					<tr><td height="3" background="../../image/inside_index_bg4.jpg"></td></tr>
					<tr>
						<td align="left" height="53%" width="121" background="../../image/inside_index_bg4.jpg">
							<p align="center">报送机构：</td>
						<td align="left" height="53%" width="116" background="../../image/inside_index_bg4.jpg">
							<input name="T1" size="17" style="float: left"></td>
						<td align="left" height="53%" width="42" background="../../image/inside_index_bg4.jpg">
							<p align="center">模板：</td>
						<td align="left" height="53%" width="313" background="../../image/inside_index_bg4.jpg">
								<select style='Z-INDEX:-1' name="select1" size="1" class="input-text">
									<option selected>（所有模板）</option>
									<OPTION value="">G01资产负债项目统计表</OPTION>
									<OPTION value="">G01资产负债项目统计表附注</OPTION>
									<OPTION value="">G02表外业务统计表</OPTION>
									<OPTION value="">G04利润表</OPTION>
									<OPTION value="">G05利润分配表</OPTION>
									<OPTION value="">G11资产质量五级分类情况表</OPTION>
									<OPTION value="">G12贷款质量迁徙情况表</OPTION>
									<OPTION value="">G13最大十家关注类/次级类/可疑类/损失类贷款情况表</OPTION>
									<OPTION value="">G14授信集中情况表</OPTION>
									<OPTION value="">G15最大二十家关联方关联交易情况表</OPTION>
									<OPTION value="">G16 抵债资产账龄情况表</OPTION>
									<OPTION value="">……</OPTION>
									<OPTION value=""></OPTION>
								</select></td>
						<td align="left" height="53%" width="61" background="../../image/inside_index_bg4.jpg">
							<p align="center">报送范围：</td>
						<td align="left" height="53%" width="153" background="../../image/inside_index_bg4.jpg">
							<SELECT style="WIDTH: 153; HEIGHT: 19" name="select2" size="1" class="input-text">
								<OPTION selected>（所有机构）</OPTION>
								<OPTION>政策性银行</OPTION>
								<OPTION>国有商业银行</OPTION>
								<OPTION>股份制商业银行</OPTION>
								<OPTION>城市商业银行</OPTION>
								<OPTION>农村商业银行</OPTION>
								<OPTION>农村合作银行</OPTION>
								<OPTION>外资法人机构</OPTION>
								<OPTION>外国银行分行</OPTION>
								<OPTION>城市信用社</OPTION>
								<option>农村信用社</option>
								<option>企业集团财务公司</option>
								<option>信托投资公司</option>
								<option>金融租赁公司</option>
								<option>汽车金融公司</option>
								<option>邮政储蓄机构</option>
								<OPTION>金融资产管理公司</OPTION>
							</SELECT></td>
					</tr>
			</TBODY>
		</table>
		<table class="tabButton" cellspacing="0" cellpadding="0" border="0" style="border-width:0px; BACKGROUND-IMAGE:url('inside_index_bg4.jpg'); WIDTH:805px; HEIGHT:36px; " id="table1">
			<tr>
				<td align="center" width="244">
					时间：<input name="startDate" type="text" maxlength="10" class="input-text" size="10">
					-&nbsp;<input name="endDate" type="text" maxlength="10" class="input-text" size="10">
				</td>
				<td align="center">
					<FONT face="宋体"><INPUT id="Radio9" type="radio" value="Radio20" name="RadioGroup">&nbsp;错误记录</FONT><strong><font face="宋体" color="#FF0000" size="2">×</font></strong><FONT face="宋体">&nbsp;<INPUT id="Radio5" type="radio" value="Radio16" name="RadioGroup">&nbsp;正确记录</FONT><strong><font face="宋体" color="#33CC33" size="2">√</font></strong><FONT face="宋体">&nbsp;<INPUT id="Radio6" type="radio" value="Radio17" name="RadioGroup">&nbsp;错报记录<strong><font size="2" color="#FF0000">※</font></strong>&nbsp;<INPUT id="Radio7" type="radio" value="Radio18" name="RadioGroup">&nbsp;漏报记录<strong><font size="2" color="#FF0000">¤</font></strong>&nbsp;<INPUT id="Radio10" type="radio" value="Radio21" name="RadioGroup">&nbsp;异常记录<font color="#FF0000" style="font-size: 9pt"><strong>？</strong></font>&nbsp;<INPUT id="Radio8" type="radio" value="Radio19" name="RadioGroup" CHECKED>&nbsp;全部记录</FONT>
				</td>
			</tr>
		</table>
		
	</body>
</html:html>
