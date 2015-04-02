<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css">
		<link href="<%=request.getContextPath() %>/css/tree.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/defTreeFormat.js"></script>
		
		<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>
	<script language="javascript">
	
			 function history(){
			 	 
			 	 window.location="<%=request.getContextPath()%>/afreportmerger.do";
			 }
			 	  
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="<bean:write name='<%=Config.APART_PAGE_OBJECT%>' property='curPage' scope='request'/>";
		</logic:present>
		<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var _curPage="0";
		</logic:notPresent>
		<!--
			/**
			 * 公式类型:表内校验
			 */
			 var CELL_CHECK_INNER="1";
			/**
			 * 公式类型:表间校验
			 */
			 var CELL_CHECK_INNER="2";
			/**
			 *分隔符1
			 */
			 var SPLIT_SMYBOL_COMMA=",";
			 /**
			 *分隔符2
			 */
			 var SPLIT_SMYBOL_ESP="&";
			 
			/**
			 * 关系表达式定义文件后缀
			 */
			 var EXT_TXT="txt";
			
			/**
			 * 导入表内表间关系表达式事件
			 */
			 function _load_gx(){
			 	 //openDialog("load_gx.jsp");
			 	 var objForm=document.forms['frmBJGX'];
			 	 
			 	 window.location="load_gx.jsp?" + 
			 	 	"childRepId=" + objForm.elements['childRepId'].value + 
			 	 	"&versionId=" + objForm.elements['versionId'].value + 
			 	 	"&reportName=" + objForm.elements['reportName'].value +
			 	 	"&reportStyle=" + objForm.elements['reportStyle'].value +
			 	 	"&curPage=" + objForm.elements['curPage'].value;
			 }
			 
			 /**
			  * 表单提交事件
			  */
			  function _submit(){
			  	var objCount=document.getElementById("rCount");
			  	var count=eval(objCount.value);
			  	var delCellFormuIds="";
			  	for(var i=0;i<count;i++){
			  		var objChk=eval("document.getElementById(\"chk" + i + "\")");
			  		if(objChk.checked==true)
			  			delCellFormuIds+=(isEmpty(delCellFormuIds)==true?"":SPLIT_SMYBOL_COMMA) + objChk.value;
			  	}
			  	
			  	if(isEmpty(delCellFormuIds)==true){
			  		alert("请选择要删除的表达式!\n");
			  		return false;
			  	}
			  	if(confirm("您确定要删除当前选中的表达式吗?\n")==false){
			  		return false;
			  	}
				
				document.forms['frmBJGX'].elements['cellFormuIds'].value=delCellFormuIds;
			  	return true;
			  }
			  
			  /**
			   * 返回事件
			   */
			   function _back(){
			   	 var form=document.forms['frmBJGX'];
			   	 window.location="/smis_in_net/template/viewMChildReport.do?" + 
			   	 	"curPage=" + form.elements['curPage'].value;
			   }
			   
		function toExcel(templateId,versionId){
		
			window.location="<%=request.getContextPath()%>/servlets/toReadExcelServlet?forflg=1&templateId=" + templateId+"&versionId="+versionId;
		}			   
		
		</script>
			<%
		
		String templateId=null,versionId=null;
		if(request.getParameter("templateId")!=null) templateId=(String)request.getParameter("templateId");
		if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");
		 %>
</head>
<body background="<%=request.getContextPath() %>/image/total.gif">
		<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
		</logic:present>
	<table border="0" cellspacing="0" cellpadding="4" width="80%" align="center">		
		<tr><td height="8"></td></tr>
		<tr>
			<td>
				当前位置 >> 数据反查 >> 报表反查SQL
			</td>
		</tr>
		<tr><td height="10"></td></tr>		
		<tr>
			<td>
				<fieldset id="fieldset">
					<div id="server">
					<table id="tbl" border="0" cellpadding="4" cellspacing="1" width="100%" class="bgcolor" align="center">
						<tr class="titletab">
							<th colspan="5" align="center">
								<strong>									
										金融机构资产负债项目月报（人民币结转表） 
								</strong>
							</th>
						</tr>
						<tr>
							<td class="tableHeader" width="8%">
								序号
							</td>
							<td class="tableHeader" width="8%">
								单元格
							</td>
							<td class="tableHeader" width="30%">
								单元格名称
							</td>
							<td class="tableHeader">
								反查公式
							</td>
						</tr>
							<tr bgcolor="#FFFFFF">
								
									<td align="center">
									1
									</td>	
									<td align="center" bgcolor="#ffffff">
										D6
									</td>
									<td align="center" bgcolor="#ffffff">
										2:1. 现金_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;curr_value&#39;)) from e_accounting where vs_id in(10001,10002,10003,10004);
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									2
									</td>	
									<td align="center" bgcolor="#ffffff">
										D7
									</td>
									<td align="center" bgcolor="#ffffff">
										3:2. 贵金属_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;curr_value&#39;)) from e_accounting vs_id in(10005,10006,10003,10004);
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									3
									</td>	
									<td align="center" bgcolor="#ffffff">
										D8
									</td>
									<td align="center" bgcolor="#ffffff">
										4:3. 存放中央银行款项_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;1002001&#39;,&#39;1002002&#39;,&#39;1002003&#39;)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									4
									</td>	
									<td align="center" bgcolor="#ffffff">
										D9
									</td>
									<td align="center" bgcolor="#ffffff">
										5:4. 存放同业款项_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select curr_value from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									5
									</td>	
									<td align="center" bgcolor="#ffffff">
										D10
									</td>
									<td align="center" bgcolor="#ffffff">
										6:4.1 境内同业_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;1011&#39;,&#39;1012&#39;,&#39;1031&#39;))-sum((&#39;1011100&#39;,&#39;1011101&#39;,&#39;1011102&#39;,&#39;1011103&#39;,&#39;1011200&#39;,&#39;1011201&#39;,&#39;1011202&#39;,&#39;1011203&#39;)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									6
									</td>	
									<td align="center" bgcolor="#ffffff">
										D11
									</td>
									<td align="center" bgcolor="#ffffff">
										7:4.2 境外同业_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum( (&#39;1011100&#39;,&#39;1011101&#39;,&#39;1011102&#39;,&#39;1011103&#39;,&#39;1011200&#39;,&#39;1011201&#39;,&#39;1011202&#39;,&#39;1011203&#39;)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									7
									</td>	
									<td align="center" bgcolor="#ffffff">
										D12
									</td>
									<td align="center" bgcolor="#ffffff">
										8:5. 应收利息_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;1132&#39;,&#39;1181&#39;))-sum((&#39;1132065&#39;)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									8
									</td>	
									<td align="center" bgcolor="#ffffff">
										D13
									</td>
									<td align="center" bgcolor="#ffffff">
										9:6. 贷款_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum(&#39;1301&#39;,&#39;1302&#39;,&#39;1303&#39;,&#39;1304&#39;,&#39;1305&#39;,&#39;1308&#39;,&#39;1382&#39;))-abs(LXTZ.sum(人民币借方余额)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									9
									</td>	
									<td align="center" bgcolor="#ffffff">
										D14
									</td>
									<td align="center" bgcolor="#ffffff">
										10:7. 贸易融资_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum(&#39;1307&#39;)-abs(sum(&#39;1307100&#39;,&#39;1307101&#39;,&#39;1307102&#39;,&#39;1307103&#39;,&#39;1307104&#39;,&#39;1307105&#39;,&#39;1307106&#39;,&#39;1307107&#39;,&#39;1307108&#39;,&#39;1307109&#39;,&#39;1307110&#39;,&#39;1307111&#39;,&#39;1307112&#39;,&#39;1307113&#39;))) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									10
									</td>	
									<td align="center" bgcolor="#ffffff">
										D15
									</td>
									<td align="center" bgcolor="#ffffff">
										11:8. 贴现及买断式转贴现_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum('1306001',&#39;1306002&#39;,&#39;1306003&#39;,&#39;1306004&#39;,&#39;1306005&#39;,&#39;1306006&#39;,&#39;1306007&#39;,&#39;1306008&#39;)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									11
									</td>	
									<td align="center" bgcolor="#ffffff">
										D17
									</td>
									<td align="center" bgcolor="#ffffff">
										13:10. 拆放同业_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum('1013','1014') from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									12
									</td>	
									<td align="center" bgcolor="#ffffff">
										D18
									</td>
									<td align="center" bgcolor="#ffffff">
										14:11. 其他应收款_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum('1221','1131') from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									13
									</td>	
									<td align="center" bgcolor="#ffffff">
										D19
									</td>
									<td align="center" bgcolor="#ffffff">
										15:12. 投资_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select D20+D21+D22 from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									14
									</td>	
									<td align="center" bgcolor="#ffffff">
										D20
									</td>
									<td align="center" bgcolor="#ffffff">
										16:12.1 债券_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;1101&#39;,&#39;1501&#39;,&#39;1503&#39;,&#39;1531&#39;))-sum((&#39;1101025&#39;,&#39;1101050&#39;,&#39;1101099&#39;,&#39;1101125&#39;,&#39;1101150&#39;,&#39;1101199&#39;,&#39;1501050&#39;,&#39;1501099&#39;,&#39;1501150&#39;,&#39;1501199&#39;,&#39;1501250&#39;,&#39;1501299&#39;,&#39;1503050&#39;,&#39;1503075&#39;,&#39;1503099&#39;,&#39;1503150&#39;,&#39;1503199&#39;,&#39;1503250&#39;,&#39;1503299&#39;,&#39;1503350&#39;,&#39;1503375&#39;,&#39;1503399&#39;,&#39;1531099&#39;,&#39;1531199&#39;,&#39;1531299&#39;))-sum((&#39;1101&#39;,&#39;1501&#39;,&#39;1503&#39;,&#39;1531&#39;,&#39;1101025&#39;,&#39;1101050&#39;,&#39;1101099&#39;,&#39;1101125&#39;,&#39;1101150&#39;,&#39;1101199&#39;,&#39;1501050&#39;,&#39;1501099&#39;,&#39;1501150&#39;,&#39;1501199&#39;,&#39;1501250&#39;,&#39;1501299&#39;,&#39;1503050&#39;,&#39;1503075&#39;,&#39;1503099&#39;,&#39;1503150&#39;,&#39;1503199&#39;,&#39;1503250&#39;,&#39;1503299&#39;,&#39;1503350&#39;,&#39;1503375&#39;,&#39;1503399&#39;,&#39;1531099&#39;,&#39;1531199&#39;,&#39;1531299&#39;)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									15
									</td>	
									<td align="center" bgcolor="#ffffff">
										D21
									</td>
									<td align="center" bgcolor="#ffffff">
										17:12.2 股票_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;1101050&#39;,&#39;1503075&#39;,&#39;1101150&#39;,&#39;1503375&#39;))-sum((&#39;1101050&#39;,&#39;1503375&#39;)) from e_accounting
										</div>
										
									</td>
									
									
								
								</tr>
								
								<tr bgcolor="#FFFFFF">
								
									<td align="center">
									16
									</td>	
									<td align="center" bgcolor="#ffffff">
										D22
									</td>
									<td align="center" bgcolor="#ffffff">
										18:12.3 其他_人民币
									</td>							
									<td align="center" bgcolor="#ffffff" style="word-break: break-all;padding:5px 0px 5px 0px;font-size:14px;">
										<div style="margin: 0px 0px 0px 0px;width:100%;height:100%;" contentEditable=true>
											select sum((&#39;1101025&#39;,&#39;1101099&#39;,&#39;1501050&#39;,&#39;1501099&#39;,&#39;1501150&#39;,&#39;1501199&#39;,&#39;1503050&#39;,&#39;1503099&#39;,&#39;1503150&#39;,&#39;1503199&#39;,&#39;1511&#39;,&#39;1531099&#39;,&#39;1531199&#39;,&#39;1531299&#39;,&#39;1101125&#39;,&#39;1101199&#39;,&#39;1501250&#39;,&#39;1501299&#39;,&#39;1503250&#39;,&#39;1503299&#39;,&#39;1503350&#39;,&#39;1503399&#39;))-sum((&#39;1101125&#39;,&#39;1101199&#39;,&#39;1501250&#39;,&#39;1501299&#39;,&#39;1503250&#39;,&#39;1503299&#39;,&#39;1503350&#39;,&#39;1503399&#39;)) from e_accounting 
										</div>
										
									</td>					
								</tr>					
					</table>
					</div>
					
						<table border="0" cellpadding="0" cellspacing="4" width="90%" align="center">
							<tr>
								<td align="center">  
									<input type="button" class="input-button" onclick="toExcel('<%=templateId %>','<%=versionId %>')" value=" 导出excel ">
								</td>
								<td>
									<input type="button" class="input-button"  value=" 保存 ">
								</td>
								<td align="center">  
									<input type="button" class="input-button" onclick="history()" value=" 返 回 ">
								</td>
							</tr>
						</table>
						
				</fieldset>
			</td>
		</tr>

	</table>
	<script>
function exportFunc()
{
  var o=document.getElementById("server");
  var win=window.open("");
  win.document.open();
  win.document.charset="gb2312";
  win.document.write(o.innerHTML);
  win.document.execCommand("saveas",true,"SF2101_081.txt");
  win.opener=null;
  win.close();
}
function exportFunc1()
{
  document.execCommand("saveas",true,"SF2101_081.txt");
}

</script>
</body>
</html:html>

