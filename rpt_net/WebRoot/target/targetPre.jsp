
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
  <head>
    <html:base />
    
    <title>targetPre.jsp</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
  </head>
  
  <body>
  <table border="0" width="96%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 指标分析 >> 指标预警
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table>
	    <html:form action="/targetPre" method="post">
	    
	      <table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
	       <tr>
	       	<td align="center">
	       	年:<input type="text" name="year" value="" size="5">
	       	</td>
	       	
	       	
	       	<td align="center">
	       	月:<input type="text" name="month" value="" size="5">
	       	</td>
	      
	       <td></td>
	       		<td align="center">
				       指标类型:
				      <select name="targetType">
					       <option selected>主要指标
					       <option>辅助指标
					       <option>特色指标
					       <option>合规指标
					  </select>
			      </td>
			      <td></td>
			      <td align="center">
				       指标业务类型:
				      <select name="targetOpType">
					       <option selected>信用风险
					       <option>流动性风险
					       <option>市场风险
					       <option>资本充足
					       <option>盈利性
					       <option>其他
					  </select>
			      </td>
			      <td>
			      <html:submit value="查询" styleClass="input-button"/>
			      </td>
		      </tr>
	      </table>
	    </html:form>
    </table>
    
    <table width="90%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
		<tr class="titletab" id="tbcolor"> 
	    	<th height="24" align="center" id="list" colspan="8">查询结果</th>
	  	</tr>
  		<tr>
  		   <TR class="middle">
			 
			 <TD width="20%"  align="center">指标名称</TD>
			 <TD  align="center">指标值</TD>
			 <TD width="30%"  align="center">比上期</TD>

		</TR>
		 <tr class="middle">
		 	 <TD width="20%"  align="center">资产负债比</TD>
			 <TD  align="center">80%</TD>
			 <TD width="30%"  align="center">+10%</TD>
		 
		 </tr>
		 <tr class="middle">
		 	 <TD width="20%"  align="center">流动性比率</TD>
			 <TD  align="center">50%</TD>
			 <TD width="30%"  align="center">-5%</TD>
		 
		 </tr>
		 <tr class="middle">
		 	 <TD width="20%"  align="center">资本充足率</TD>
			 <TD  align="center">70%</TD>
			 <TD width="30%"  align="center">+15%</TD>
		 
		 </tr>
	</table>
  </body>
</html:html>
