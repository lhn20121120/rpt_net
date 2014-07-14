<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">

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
	
	
	


<style>
#tooltip{
	position:absolute;
	border:1px solid #333;
	background:#f7f5d1;
	padding:2px 5px;
	color:#333;
	display:none;
	}	

</style>
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
			$( "#infoedit" ).dialog({
				autoOpen: false
			});
 
			$( "#infoeditbtn" ).click(function() {
				createDiv();
				$( "#infoedit" ).dialog( "open" );
				return false;
			});
		});

		$(function() { 
		var term=$( "#datepicker" ).val();
								$( "#datepicker" ).datepicker({
				      changeMonth: true,
				      changeYear: true
				     });
			$( "#datepicker" ).datepicker( "option", "dateFormat", "yy-mm-dd");
			$( "#datepicker" ).val(term);
		});

		function createDiv()
{
    var shadow = document.createElement("div");
    shadow.setAttribute("id","shadow");
    shadow.style.zIndex="10";

    document.body.appendChild(shadow);
}
		
function hideDiv()
{
    var textUser=document.getElementById("textUser");
    var user=document.getElementById("user");
    user.value=textUser.value;

    var box=document.getElementById("box");
    var shadow=document.getElementById("shadow");
    var btnShow=document.getElementById("btnShow");
    
    document.body.removeChild(box);
    document.body.removeChild(shadow);
    btnShow.disabled=false;   
}
</script>


<SCRIPT language="javascript">
	
		var curPage="1";
	
	function _submit(form){
		if(form.orgId.value=="0"){
			alert("请选择报送机构！");
			form.orgId.focus();
			return false;
		}
		if(form.year.value==""){
			alert("请输入报表时间!");
			form.year.focus();
			return false;
		}
		if(form.setDate.value==""){
			alert("请输入报表时间！");
			form.setDate.focus();
			return false;
		}
		if(isNaN(form.year.value)){ 
		   alert("请输入正确的报表时间！"); 
		   form.year.focus(); 
		   return false; 
		}
		if(isNaN(form.setDate.value)){ 
		   alert("请输入正确的报表时间！"); 
		   form.setDate.focus(); 
		   return false; 
		}
		if(form.setDate.value <1 || form.setDate.value > 12){
			alert("请输入正确的报表时间！");
			form.setDate.focus();
			return false;
		}
	}
	
 function couductTran(){
					 showProcessBar();
				}
 		    
	function _viewdetial(templateId, templateCode){
		document.getElementById("validatepage").src="/qyts/bstj_overreportview.action?bstjVo.templateId="+templateId+"&bstjVo.templateCode="+templateCode+"&bstjVo.term=2012-10-31";
		$( "#dialog" ).dialog( "open" );
	}
	function _viewdetial2(templateId, templateCode, orgclsid){
		document.getElementById("validatepage").src="<%=request.getContextPath()%>/bstj/overreportview.jsp";
		$( "#dialog" ).dialog( "open" );
	}
	$(function() {
		$( "#dialog" ).dialog({
			width: 700,
			autoOpen: false
			//show: "blind",
			//hide: "explode"
		});
	});

	function ExportExcel(){
		form1.action="bstj_exportExcel.action";
		form1.submit();
		form1.action="bstj_overview.action";
	}
	function dochange(){
		document.getElementById("exportExcel").disabled = true;
	}
</SCRIPT>
</head>

<body style="background-color: #FFFFFF">
	
<div class="location" align="left">
              <div class="name"> 当前位置 >>&nbsp;&nbsp;报送统计>>&nbsp;&nbsp;数据上报统计</div>
		    </div>        
<form id="form1" name="form1" onsubmit="showProcessBar();" action="<%=request.getContextPath() %>/bstj/bstj_overview.jsp" method="post">
	<div class="univer_model" style="background-color:#fafafa ">
				
				<TABLE id="paramTable" width="98%" border="0" cellpadding="0" cellspacing="0" style="background-color:#fafafa; height:40px;padding:0px;margin:0px">
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
									<input type="button" value=" 导 出 " class="input-button" id="exportExcel" onclick="ExportExcel()" disabled="disabled"/>
								</td>
							</tr>
							<tr>
								
								<td colspan=2>
									报表频度： <input type="checkbox" name="bstjVo.repFreqId" value="4" id="bstjVo.repFreqId-1" checked="checked"/>
<label for="bstjVo.repFreqId-1" class="checkboxLabel">月报</label>
<input type="checkbox" name="bstjVo.repFreqId" value="3" id="bstjVo.repFreqId-2" checked="checked"/>
<label for="bstjVo.repFreqId-2" class="checkboxLabel">季报</label>
<input type="checkbox" name="bstjVo.repFreqId" value="2" id="bstjVo.repFreqId-3" checked="checked"/>
<label for="bstjVo.repFreqId-3" class="checkboxLabel">半年报</label>
<input type="checkbox" name="bstjVo.repFreqId" value="1" id="bstjVo.repFreqId-4" checked="checked"/>
<label for="bstjVo.repFreqId-4" class="checkboxLabel">年报</label>
<input type="hidden" id="__multiselect_bstjVo.repFreqId" name="__multiselect_bstjVo.repFreqId" value="" /> 
									 </td>
									 
								<td>
									 							</td>
							</tr>
					</table>		
								
	</div>
	<TABLE width="100%" cellSpacing="0" border="0" align="center" cellpadding="4">
		
		<TR>
			<TD> 
			
				<TABLE  width="100%" cellPadding="4"  border="0"  class="datalist">
				 
					<tr  class="titletab">
					    <th class="location1" align="center"  rowspan="2"  ><strong>报表编号</strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：2<br>应报：8<br>实报：2<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：5<br>应报：20<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：7<br>应报：28<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：4<br>应报：16<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：1<br>应报：4<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：1<br>应报：4<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：12<br>应报：48<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：3<br>应报：12<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：1<br>应报：4<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：1<br>应报：4<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：1<br>应报：4<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：1<br>应报：4<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
						
						
						
						
						 <th class="location1" align="center"  colspan="2" ><strong><A href="<%=request.getContextPath() %>/bstj/orgview.jsp"  class="tooltip" 
						 title="机构：0<br>应报：0<br>实报：0<br>审核：0">XX分行</A></strong></th>
						
					 </tr>
					<TR class="middle"> 
					
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
						
						 <TD align="center" width="300"><strong>上报</strong></TD>
						 <TD align="center" width="300"><strong>审核</strong></TD>
									 
					</TR>
					
					 
					
					<tr>
						<td><a href="javascript:_viewdetial('Q011210','Q01')">Q01(&#26376;&#25253;)</a></td>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','B')">
												<img style="border:none" alt="&#24050;&#25253;2/&#24212;&#25253;2"
													src="<%=request.getContextPath()%>/image/check_right.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','B')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;2"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','C')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','C')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','E')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','E')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','F')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','F')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','J')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','J')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','K')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','K')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','O')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','O')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','P')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','P')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','R')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','R')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','S')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','S')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','T')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','T')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q011210','Q01','V')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q011210','Q01','V')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
					</tr>
					
					 
					
					 
					
					<tr>
						<td><a href="javascript:_viewdetial('Q021210','Q02')">Q02(&#26376;&#25253;)</a></td>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','B')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;2"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','B')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;2"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','C')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','C')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','E')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','E')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','F')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','F')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','J')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','J')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','K')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','K')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','O')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','O')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','P')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','P')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','R')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','R')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','S')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','S')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','T')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','T')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q021210','Q02','V')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q021210','Q02','V')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
					</tr>
					
					 
					
					 
					
					<tr>
						<td><a href="javascript:_viewdetial('Q031210','Q03')">Q03(&#26376;&#25253;)</a></td>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','B')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;2"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','B')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;2"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','C')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','C')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','E')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','E')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','F')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','F')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','J')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','J')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','K')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','K')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','O')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','O')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','P')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','P')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','R')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','R')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','S')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','S')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','T')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','T')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q031210','Q03','V')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q031210','Q03','V')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
					</tr>
					
					 
					
					 
					
					<tr>
						<td><a href="javascript:_viewdetial('Q041210','Q04')">Q04(&#26376;&#25253;)</a></td>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','B')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;2"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','B')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;2"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','C')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','C')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;5"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','E')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','E')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;7"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','F')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','F')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;4"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','J')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','J')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','K')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','K')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','O')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','O')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;12"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','P')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','P')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;3"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','R')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','R')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','S')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','S')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','T')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','T')">
												<img style="border:none" alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 	
											
											<a href="javascript:_viewdetial2('Q041210','Q04','V')">
												<img style="border:none" alt="&#24050;&#25253;0/&#24212;&#25253;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
												</TD>
											
										<TD align="center"> 
										 	
										
											<a href="javascript:_viewdetial2('Q041210','Q04','V')">
												<img style="border:none"  alt="&#24050;&#26680;0/&#24212;&#26680;1"
													src="<%=request.getContextPath()%>/image/check_error.gif" />
											</a>
											
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
											</TD>
						
						
						
						
						
										<TD align="center">
										 --	
											
												</TD>
											
										<TD align="center"> 
										 --	
										
											
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
