<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.fitech.model.worktask.common.WorkTaskConfig" %>
<%@page import="com.fitech.framework.core.util.DateUtil"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>待办任务    </title>
	
	<link href="<%=request.getContextPath() %>/css/index.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/common0.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/globalStyle.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
    
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/layout.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/layer.js"></script>

    <link href="<%=request.getContextPath() %>/css/animate/theme/jquery-ui-1.8.11.redmond.css" rel="stylesheet" />
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.ui.core.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.ui.widget.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.bgiframe-2.1.2.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.ui.mouse.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.ui.draggable.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.ui.position.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.ui.resizable.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/animate/jquery.ui.dialog.js"></script> 
	<script language="javascript" src="scripts/progressBar.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/datepicker/jquery.ui.datepicker.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/tree.js"></script>
	 <script type="text/javascript" charset="utf-8">
            jQuery(function($){
                $.datepicker.regional['zh-CN'] =
     {
        clearText: '清除', clearStatus: '清除已选日期',
        closeText: '关闭', closeStatus: '不改变当前选择',
        prevText: '&lt;上月', prevStatus: '显示上月',
        nextText: '下月&gt;', nextStatus: '显示下月',
        currentText: '今天', currentStatus: '显示本月',
        monthNames: ['一月','二月','三月','四月','五月','六月',
        '七月','八月','九月','十月','十一月','十二月'],
        monthNamesShort: ['一','二','三','四','五','六',
        '七','八','九','十','十一','十二'],
        monthStatus: '选择月份', yearStatus: '选择年份',
        weekHeader: '周', weekStatus: '年内周次',
        dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
        dayNamesMin: ['日','一','二','三','四','五','六'],
        dayStatus: '设置 DD 为一周起始', dateStatus: '选择 m月 d日, DD',
        dateFormat: 'yy-mm-dd', firstDay: 1,
        initStatus: '请选择日期', isRTL: false
   };

           $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
           $("#dateinput").datepicker();
               
         });
        </script>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/datepicker/demos.css"/>
	<script type="text/javascript">
		var  progressBar=new ProgressBar("正在处理，请稍后........");
		$(function(){
			$( "#dialog" ).dialog({
				autoOpen: false,
				height:'auto',
				width:'300px',
				style:'font-size:14px'
			});
			$( "#datepicker_2" ).datepicker({
				   changeMonth: true,
				   changeYear: true
			});
			$( "#datepicker_2" ).datepicker( "option", "dateFormat", "yy-mm-dd");
			$("#datepicker_2").val($("#_startDate").val());
		});
	</script>
<script type="text/javascript">
	function getId(id){
		return document.getElementById(id);
	}
	
	function turntoPage(currentPage,pageCount){
		var vl = getId("form1_pageNo").value;
		if(vl!=""){
			if(isNaN(vl) || vl.indexOf(".")>0 || parseInt(vl)<=0){
				alert("请输入正确的页码(1...n)");
				getId("form1_pageNo").focus();
				getId("form1_pageNo").select();
				 return ;
			}
			if(parseInt(vl)>pageCount){
				alert("跳转页不能超过最大页!");
				getId("form1_pageNo").focus();
				getId("form1_pageNo").select();
				return;
			}
			location.href="<%=request.getContextPath() %>/workTaskInfoOrMoniAction.action?pageResults.currentPage="+parseInt(vl);
		}
	};
	//全选和全不选操作
	 $(document).ready(function(){
			$("#checkAll").click(function(){
				$("input[name=cks]").each(function(i){
					if($(this).attr("checked"))
						$(this).attr("checked",false);
					else
						$(this).attr("checked",true);
				});
			});
			$("input[name=cks]").each(function(i){
				$(this).click(function(){
					if(!$(this).attr("checked"))
						$("#checkAll").attr("checked",false);
					else{
						var length1 = $("input[name=cks]:checked").length;
						var length2 = $("input[name=cks]").length;
						if(length1==length2)
							$("#checkAll").attr("checked",true);
					}
				});	
			});
			initDate1();
		});
	//启用和禁用
	function forceAppear(exec,page){
		var count = document.getElementById("countChk").value;
		var checkObj = null;
		var repInIds = "";
		for(var i=1;i<=count;i++){
			if(repInIds!="")
				repInIds+=",";
			try{
				checkObj = eval("document.getElementById('cks" + i + "')");
				if(checkObj.checked == true){
			
					repInIds += checkObj.value;
					
						
				}
			}catch(e){}				
		}
		
		if(repInIds==""){
	  	  	alert("请选择要修改的任务!\n");
	  	  	return;
		}else{
			setformValue();
			$("#fm").attr("action",$("#fm").attr("action")+"?flag="+exec+"&pageResults.currentPage="+page);
			$("#fm").submit();
		}
	}
	// 增加刷新按钮
	function fresh(page){
		var exec = window.confirm("是否同步已生成的任务?");
		var date=document.getElementById("datepicker_2").value;
		location.href="<%=request.getContextPath() %>/workTaskInfoOrMoniAction!findWorkTaskInfoOrMoni.action?date="+date+"&pageResults.currentPage="+page+"&type=fresh&workTaskInfoOrMoniVo.taskTerm="+date+"&exec_flag_no="+exec;
		progressBar.show();
	}
	
	function change(){
		var from = document.getElementById("for");
		from.submit();
	}
	
	//距离文本框的水平相对位置
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}
	
	//距离文本框的垂直相对位置
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}
	
	
	function initDate1(){
	
			var  txtLeft = getObjectLeft(document.getElementById("datepicker_2"));
			
			var txtTop = getObjectTop(document.getElementById("datepicker_2"));
			var date1 = document.getElementById("date1");
			
			date1.style.top = txtTop+1;
			date1.style.left = txtLeft+105;
		}
	
	function showDate1(){
			document.getElementById("date1").style.display='block';
		}
		var dv;
		function hiddenDate1(){
			dv = setTimeout("document.getElementById('date1').style.display='none'",1000);
		}
		
		function cleartimeout(){
			clearTimeout(dv);
		}
		
		window.onresize=function(){
			initDate1();
		}
	function updateTaskFlag(execflag,cks,currentPage,tasDate){
		setformValue();
		$("#fm").attr("action",$("#fm").attr("action")+"?flag="+execflag+"&pageResults.currentPage="+currentPage+"&cks="+cks);
		$("#fm").submit();
		//location.href="workTaskInfoOrMoniAction!updateTaskMoni.action?flag="+execflag+"&cks="+cks+"&pageResults.currentPage="+currentPage+"&date="+tasDate;
	}
	
	function setformValue(){
		var forForm = document.getElementById("for");
		var taskName = forForm['workTaskInfoOrMoniVo.taskName'].value;
		var freqId  = forForm['workTaskInfoOrMoniVo.freqId'].value;
		var taskDate = forForm['date'].value;
		
		var fmForm = document.getElementById("fm");
		fmForm['workTaskInfoOrMoniVo.taskName'].value = taskName;
		fmForm['workTaskInfoOrMoniVo.freqId'].value = freqId;
		fmForm['date'].value = taskDate;
	}
</script>
<s:if test="msg!=null">
	<script type="text/javascript">
		alert("<s:property value='msg'/>");
	</script>
</s:if>
</head>

<body>
<div style="margin-top:5px;overflow:auto">
<table width="100%" height="20px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
		<div class="tableinfo" ><span style="font-size:12px;"><img src="<%=request.getContextPath() %>/images/icon01.gif" alt="列表" /></span><b style="font-size:12px;">当前位置 >>报表处理 >>任务管理</b></div>
    </td>
  </tr>
</table>
</div>
<form action="" id="repForm" target="rptFrame">
	<input type="hidden" id="templateIds" />
</form>
<fieldset style="background-color:white;" id="fs">
<legend><b><font color="#075587">任务查询栏</font></b></legend>
<div style="width:100%;margin-bottom:5px" align="center">
<input type="hidden" id="_startDate" value="<s:property value="date"/>"/>
<form action="<%=request.getContextPath() %>/workTaskInfoOrMoniAction.action" id="for" method="post">
<table border="0" width="98%" align="center" cellpadding="0" cellspacing="0" >
		<tr>
			<td width="20%"  align="center" >任务名称
				<input type="text" onchange="change()" name="workTaskInfoOrMoniVo.taskName" size="25" value="<s:property value="workTaskInfoOrMoniVo.taskName"/>" id="templateId"/>
            <td width="30%" align="center">
            	频度 <select onchange="change()" name="workTaskInfoOrMoniVo.freqId" style="width: 100px;vertical-align: middle">
				  <option value="-1" selected="selected">---全部---</option>
	              	<option <s:if test="workTaskInfoOrMoniVo.freqId =='day'">selected="selected"</s:if>  value="day">日频度</option>
					<option <s:if test="workTaskInfoOrMoniVo.freqId =='month'">selected="selected"</s:if> value="month">月频度</option>
					<option <s:if test="workTaskInfoOrMoniVo.freqId =='season'">selected="selected"</s:if> value="season">季频度</option>
					<option <s:if test="workTaskInfoOrMoniVo.freqId =='halfyear'">selected="selected"</s:if> value="halfyear">半年频度</option>
					<option <s:if test="workTaskInfoOrMoniVo.freqId =='year'">selected="selected"</s:if> value="year">年频度</option>
					<option <s:if test="workTaskInfoOrMoniVo.freqId =='yearbegincarry'">selected="selected"</s:if> value="yearbegincarry">年初结转</option>
			    </select>
			    <input type="hidden" name="moniIds" id="moniIdsInput"/>
			    <input type="hidden" name="query" />
            </td>
            <td width="35%" align="center" valign="middle">
            	期数  <input width="100px" id="datepicker_2" readonly="readonly" onmouseout="hiddenDate1()" onmousemove="showDate1()" value="" name="date" onchange="change(),checkStartAndEndTime(this)"/>
            	<span onmousemove="cleartimeout()" onmouseout="this.style.display='none'" onclick="document.getElementById('datepicker_2').value='';this.style.display='none';change();" style="border:0 solid red;width:15px;display:none;height:15px;cursor:hand;position:absolute;" id="date1" ><img src="<%=request.getContextPath()%>/images/clear.bmp"/></span>
            	</td>
            <td width="12x%" align="center" valign="middle">
            	<input type="submit" name="button" id="button" value="查询" class="searchbtn" />&nbsp;&nbsp;
            </td>
		</tr>
 	</table>

</form>
</div>
<div id="main" style="width:100%;height:100%;overflow:auto;">
<form id="fm" action="<%=request.getContextPath() %>/workTaskInfoOrMoniAction!updateTaskMoni.action" method="post">
	<input type="hidden" name="workTaskInfoOrMoniVo.taskName">
	<input type="hidden" name="workTaskInfoOrMoniVo.freqId">
	<input type="hidden" name="date">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"  class="ntable" align="center">
	<tr >
   		<td colspan="9" bgcolor="#FFF0D0" ><b>任务处理列表</b>
   		<input name="pendingTaskQueryConditions.roleId" type="hidden" value="<s:property value="pendingTaskQueryConditions.roleId"/>"/>
   		</td>
  </tr>
  
  <tr >
   		<td colspan="9" align="left" style="height:35px;">
   		<input type="button" class="searchbtn" onclick="forceAppear(1,'<s:property value="pageResults.currentPage"/>')" value="启用" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   		<input type="button" class="searchbtn" onclick="forceAppear(0,'<s:property value="pageResults.currentPage"/>')" value="禁用" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   		<input type="button" class="searchbtn" onclick="fresh('<s:property value="pageResults.currentPage"/>')" value="生成/刷新" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   		</td>
  </tr>
  <tr>
    <td width="5%" bgcolor="#FFF0D0" >&nbsp;&nbsp;<input type="checkbox" id="checkAll"/></td>
    <td width="13%" bgcolor="#FFF0D0" align="center"><strong>任务名称</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>任务频度</strong></td>
    <td width="12%" bgcolor="#FFF0D0" align="center"><strong>期数</strong></td>
    <td width="12%" bgcolor="#FFF0D0" align="center"><strong>触发方式</strong></td>
    <td width="15%" bgcolor="#FFF0D0" align="center"><strong>业务条线</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>当前状态</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>操作</strong></td>
  </tr>
  </tr>
  <% int i =0; %>
  <s:if test="pageResults!=null && pageResults.results.size()!=0">
  		<s:iterator value="pageResults.results" id="m">
  		<% i++; %>
  			<tr>
			  	<td >&nbsp;&nbsp;<input type="checkbox" name="cks" id="cks<%=i %>" value="<s:property value="#m[6]"/>"/></td>
			  	<td align="center"><s:property value="#m[0]"/></td>
			  	<td align="center">
			  	<s:if test="#m[1] =='day'">日</s:if>
			  	<s:if test="#m[1] =='month'">月</s:if>
			  	<s:if test="#m[1] =='season'">季</s:if>
			  	<s:if test="#m[1] =='halfyear'">半年</s:if>
			  	<s:if test="#m[1] =='year'">年</s:if>
			  	<s:if test="#m[1] =='yearbegincarry'">年初结转</s:if>
			  	<td align="center"><s:date name="#m[4]" format="yyyy-MM-dd"/></td>
			  	<td align="center"><s:property value="#m[2]"/></td>
			  	<td align="center"><s:property value="#m[3]"/></td>
			  	<td align="center">
				  	<s:if test="#m[5] == 1">
				  		已发布
				  	</s:if>
				  	<s:else>
				  		未发布
				  	</s:else>
			  	</td>
			  	<td align="center">
				  	<s:if test="#m[5] == 1">
				  		
				
				<a style="text-decoration:underline;" href="javascript:;" onclick="updateTaskFlag(0,<s:property value="#m[6]"/>,<s:property value="pageResults.currentPage"/>,'<s:date name="#m[4]" format="yyyy-MM-dd"/>')">禁用</a>
				  	</s:if>
				  	<s:else>
				  	<a style="text-decoration:underline;" href="javascript:;" onclick="updateTaskFlag(1,<s:property value="#m[6]"/>,<s:property value="pageResults.currentPage"/>,'<s:date name="#m[4]" format="yyyy-MM-dd"/>')">发布</a>
				  		
				  	</s:else>
			  	</td>
			</tr>
  		</s:iterator>
  </s:if>
  <input type="hidden" name="countChk" value="<%=i%>">
  <s:else>
  	 <tr >
   		<td colspan="9">无任务处理</td>
 	 </tr>
  </s:else>
</table>
</form>
</div>
<div style="margin-top:7px;">
            <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
			  <tr>
				<td align="left"><img src="<%=request.getContextPath() %>/images/jt1.gif" width="4" height="7" />
				 共<s:property value="pageResults.totalCount"/> 条记录，每页<s:property value="pageResults.pageSize"/> 条</td>
				<td style="text-indent:opx; padding-right:8px;"><table width="320" border="0" align="right" cellpadding="0" cellspacing="0">
				  <tr>
					<td><a href="<%=request.getContextPath() %>/workTaskInfoOrMoniAction.action?pageResults.currentPage=1">首页</a></td>
					<td><img src="<%=request.getContextPath() %>/images/pleft.gif" width="12" height="12" style="cursor:hand;" onclick="location.href='<%=request.getContextPath() %>/workTaskInfoOrMoniAction.action?pageResults.currentPage=<s:property value="pageResults.currentPage-1"/>'"/></td>
					<td><s:property value="pageResults.currentPage"/>/<s:property value="pageResults.pageCount"/>页</td>
					<td><img src="<%=request.getContextPath() %>/images/pright.gif" width="12" height="12" style="cursor:hand;" 
					<s:if test="(pageResults.currentPage+1)<=pageResults.pageCount"> onclick="location.href='<%=request.getContextPath() %>/workTaskInfoOrMoniAction.action?pageResults.currentPage=<s:property value="pageResults.currentPage+1"/>'"</s:if>/></td>
					<td><a href="<%=request.getContextPath() %>/workTaskInfoOrMoniAction.action?pageResults.currentPage=<s:property value="pageResults.pageCount"/>" >尾页</a></td>
					<td>转到第</td>
					<td><label>
					  <input type="text" name="textfield" id="form1_pageNo" class="pageinput" />
					</label></td>
					<td>页</td>
					<td><img src="<%=request.getContextPath() %>/images/go.gif" width="16" height="16" onclick="turntoPage(<s:property value="pageResults.currentPage"/>,<s:property value="pageResults.pageCount"/>)" style="cursor:hand;"/></td>
					<td>转</td>
				  </tr>
				</table></td>
			  </tr>
			</table></div>
　</fieldset>
</body>
</html>