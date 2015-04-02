<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="/qyts/css/globalStyle.css" type="text/css" rel="stylesheet">
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery-1.5.1.js"></script>
	
	<link href="<%=request.getContextPath() %>/script/qyts/css/globalStyle.css" type="text/css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/script/qyts/css/jquery-ui-1.8.11.redmond.css" rel="stylesheet" />
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.ui.core.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.ui.widget.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.bgiframe-2.1.2.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.ui.mouse.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.ui.draggable.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.ui.position.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.ui.resizable.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.ui.dialog.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.effects.core.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.effects.blind.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/animate/jquery.effects.explode.js"></script>
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/datepicker/jquery.ui.datepicker.js"></script> 
	<script src="<%=request.getContextPath()%>/script/qyts/scripts/datepicker/jquery.ui.datepicker-zh-CN.js"></script>
	
	<script src="<%=request.getContextPath() %>/script/qyts/scripts/tooltip.js"></script> 
	
	<script type="text/javascript"
			src="<%=request.getContextPath() %>/js/progressBar.js"></script>
<link href="<%=request.getContextPath() %>/script/qyts/css/progressBar.css"
			rel="stylesheet" type="text/css" />
<script language="javascript">
	 var progressBar = new ProgressBar("正在处理中。。。");
	       function showProcessBar(){
	         progressBar.show();
	       }
	       function closeProgressBar(){
		 		progressBar.close();
	       }
	       function   getObjectLeft(e){   
	           var   l=e.offsetLeft;   
	           while(e=e.offsetParent)   
	      	     l   +=   e.offsetLeft;   
	           return   l;   
	       }   
	       function   getObjectTop(e){ 
	           var   t=e.offsetTop;   
	            while(e=e.offsetParent)   
	      	      t   +=   e.offsetTop;   
	           return   t;   
	       }	
$(function() {
		$( "#dialog" ).dialog({
			width: 700,
			autoOpen: false
			//show: "blind",
			//hide: "explode"
		});
 
});
$(function() { 
			var term=$( "#datepicker" ).val();
			$( "#datepicker" ).datepicker();
			$( "#datepicker" ).datepicker( "option", "dateFormat", "yy-mm-dd");
			$( "#datepicker" ).val(term);
		});

function _viewdetial(templateId, templateCode){
document.getElementById("validatepage").src="/qyts/bstj_reportview.action?bstjVo.templateId="+templateId+"&bstjVo.templateCode="+templateCode+"&bstjVo.orgclsid=B&bstjVo.term=2012-10-31";
	$( "#dialog" ).dialog( "open" );
}

	function ExportExcel(){
		_form.action="bstj_exportExcelORG.action";
		_form.submit();
		_form.action="bstj_orgview.action";
	}
	function dochange(){
		document.getElementById("exportExcel").disabled = true;
	}
</script>
 
</head>

<body>
	
<div class="location" align="left">
              <div class="name"> 当前位置 >>&nbsp;&nbsp;报送统计>>&nbsp;&nbsp; 数据上报统计</div>
		    </div>
				        
<form id="_form" name="_form" onsubmit="showProcessBar();" action="<%=request.getContextPath() %>/bstj/orgview.jsp" method="post">
	<div class="univer_model" style="background-color:#fafafa ">
				<TABLE id="paramTable" width="98%" border="0" cellpadding="0" cellspacing="0" style="background-color:#fafafa ">
							<tr>
								
								<td>
									报表时间：<input type="text" name="bstjVo.term" size="10" value="2012-10-31" readonly="readonly" id="datepicker" onchange="dochange()"/>
									 </td>
									<td>
									 
									 
								</td>
								<td>
									<input type="submit" value=" 查 询 " class="input-button" />
								</td>
								<td>
									<input type="button" value=" 导 出 " class="input-button" id="exportExcel" onclick="ExportExcel()"  disabled="disabled" />
								</td>
								<td>
									<input name="button" type="button" class="input-button" value=" 返 回 " onClick="window.location='<%=request.getContextPath() %>/bstj/bstj_overview.jsp'"/>
								</td>
							</tr>
							<tr>
								
								<td>
									报表频度： <input type="checkbox" name="bstjVo.repFreqId" value="4" id="bstjVo.repFreqId-1" checked="checked"/>
<label for="bstjVo.repFreqId-1" class="checkboxLabel">月报</label>
<input type="checkbox" name="bstjVo.repFreqId" value="3" id="bstjVo.repFreqId-2" checked="checked"/>
<label for="bstjVo.repFreqId-2" class="checkboxLabel">季报</label>
<input type="checkbox" name="bstjVo.repFreqId" value="2" id="bstjVo.repFreqId-3" checked="checked"/>
<label for="bstjVo.repFreqId-3" class="checkboxLabel">半年报</label>
<input type="checkbox" name="bstjVo.repFreqId" value="1" id="bstjVo.repFreqId-4" checked="checked"/>
<label for="bstjVo.repFreqId-4" class="checkboxLabel">年报</label>
<input type="hidden" id="__multiselect__form_bstjVo_repFreqId" name="__multiselect_bstjVo.repFreqId" value="" /> 
									 </td>
									 <td colspan="3">
									机构类别：<select name="bstjVo.orgclsid" id="_form_bstjVo_orgclsid" onchange="dochange()">
    <option value=""
    >--全部--</option>
    <option value="B" selected="selected">政策性银行</option>
    <option value="C">国有商业银行</option>
    <option value="D">资产管理公司</option>
    <option value="E">股份制银行</option>
    <option value="F">城市商业银行</option>
    <option value="G">城市信用社</option>
    <option value="H">农村合作银行</option>
    <option value="I">农村信用合作社（含联社）</option>
    <option value="J">邮政储蓄机构</option>
    <option value="K">信托公司</option>
    <option value="L">财务公司</option>
    <option value="M">金融租赁公司</option>
    <option value="N">汽车金融公司</option>
    <option value="O">外资银行法人</option>
    <option value="P">外资银行分支</option>
    <option value="Q">贷款公司</option>
    <option value="R">农村商业银行</option>
    <option value="S">村镇银行</option>
    <option value="T">特殊金融机构</option>
    <option value="U">农村资金互助社</option>
    <option value="V">国家开发银行</option>
    <option value="Y">消费金融公司</option>
    <option value="Z">其他类金融机构</option>
    <option value="ZA">货币经纪公司</option>


</select>

									 
								</td>
								<td>
									 							</td>
							</tr>
					</table>		
								
	</div>
	<TABLE width="100%" cellSpacing="0" border="0" align="center" cellpadding="4">
		
		<TR>
			<TD>
				<TABLE width="100%" cellPadding="4"  border="0"  class="datalist">
				 
					<tr  class="titletab">
					    <th align="center" width="60" rowspan="2"><strong>报表编号</strong></th>
						
						 <th align="center"  width="100" colspan="2" ><strong>XX分行-机构1</strong></th>
						
						 <th align="center"  width="100" colspan="2" ><strong>XX分行-机构2</strong></th>
						
					 </tr>
					<TR class="middle"> 
					
						 <TD align="center"><strong>上报</strong></TD>
						 <TD align="center"><strong>审核</strong></TD>
						
						 <TD align="center"><strong>上报</strong></TD>
						 <TD align="center"><strong>审核</strong></TD>
									 
					</TR>
					
				 	
					
					
					<tr>
						<td><a href="javascript:_viewdetial('Q011210','Q01')">Q01(&#26376;&#25253;)</a></td>
						
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_right.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_right.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						 
 
					</tr>
					
				 	
					
					
					<tr>
						<td><a href="javascript:_viewdetial('Q021210','Q02')">Q02(&#26376;&#25253;)</a></td>
						
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						 
 
					</tr>
					
				 	
					
					
					<tr>
						<td><a href="javascript:_viewdetial('Q031210','Q03')">Q03(&#26376;&#25253;)</a></td>
						
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						 
 
					</tr>
					
				 	
					
					
					<tr>
						<td><a href="javascript:_viewdetial('Q041210','Q04')">Q04(&#26376;&#25253;)</a></td>
						
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						
						
						
						 <TD align="center" > 
						  	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						 </TD>
						<TD align="center" > 
						 	
										
												<img
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											
											
						</TD>
					
						
						 
 
					</tr>
					
					 
			</TABLE>

      
			</TD>
		</TR>
	</TABLE>
	</form>



	
	  <div id="dialog" title="报送情况">
	      <iframe id="validatepage" src="" frameborder="0" scrolling="auto" width="680" height="400"></iframe>
         </div>
</body>
</html>
