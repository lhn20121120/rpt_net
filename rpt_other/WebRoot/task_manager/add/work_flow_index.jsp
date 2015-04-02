<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>work_flow_index</title>
 <base href="<%=basePath%>">
<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
  <link href="css/index.css" rel="stylesheet" type="text/css" />
    <link href="css/common.css" rel="stylesheet" type="text/css" />
	<link href="css/common0.css" rel="stylesheet" type="text/css" />
	<link href="css/table.css" rel="stylesheet" type="text/css" />
	<link href="css/globalStyle.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="js/jquery-1.4.min.js"></script>
	<script type="text/javascript" src="js/layout.js"></script>
    <script type="text/javascript" src="js/layer.js"></script>

    <link href="css/animate/theme/jquery-ui-1.8.11.redmond.css" rel="stylesheet" />
	<script src="scripts/animate/jquery.ui.core.js"></script>
	<script src="scripts/animate/jquery.ui.widget.js"></script>
	<script src="scripts/animate/jquery.bgiframe-2.1.2.js"></script>
	<script src="scripts/animate/jquery.ui.mouse.js"></script>
	<script src="scripts/animate/jquery.ui.draggable.js"></script>
	<script src="scripts/animate/jquery.ui.position.js"></script>
	<script src="scripts/animate/jquery.ui.resizable.js"></script>
	<script src="scripts/animate/jquery.ui.dialog.js"></script>

	<script src="scripts/datepicker/jquery.ui.datepicker.js"></script> 
	<script language="javascript" src="scripts/progressBar.js"></script>
	<link rel="stylesheet" href="css/datepicker/demos.css"/>
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
<script type="text/javascript">
var  progressBar=new ProgressBar("正在跳转，请稍后........");
		$(function(){
			$( "#datepicker_1" ).datepicker({
				  changeMonth: true,
				  changeYear: true
			});
			$( "#datepicker_2" ).datepicker({
				   changeMonth: true,
				   changeYear: true
			});
	    	$( "#datepicker_1" ).datepicker( "option", "dateFormat", "yy-mm-dd");
			$( "#datepicker_2" ).datepicker( "option", "dateFormat", "yy-mm-dd");

		});
		
		function check(){
			var  flag  = true ;
			var taskName = $("#taskName").val();
			var triggerShifting = $("#triggerShifting").val();
			var datepicker_2 = $("#datepicker_2").val();
			var datepicker_1 = $("#datepicker_1").val();
			var taskTypeId = $("#taskTypeId").val();
			var freq = $("#freq").val();
			
			if(taskName==""){
				flag =  false;
				alert("任务名称不能为空！");
				$("#taskName").focus();
				return ;
			}
			if(triggerShifting==""){
				flag =  false;
				alert("触发偏移不能为空！");
				$("#triggerShifting").focus();
				return ;
			}
			/*if(triggerShifting!=""){
				var reg = /^[0-9]$/;
				if(!reg.test(triggerShifting)){
					flag =  false;
					alert("触发偏移超出(0~9)合理值！");
					$("#triggerShifting").focus();
					return;
				}
			}*/
			if(taskTypeId=="-1"){
				flag =  false;
				alert("请选择任务类型！");
				$("#taskTypeId").focus();
				return ;
			}
			if(datepicker_1==""){
				flag =  false;
				alert("开始时间不能为空！");
				$("#datepicker_1").focus();
				return ;
			}
			if(datepicker_2==""){
				flag =  false;
				alert("结束时间不能为空！");
				$("#datepicker_2").focus();
				return ;
			}
			
			if(compareDate($("input[name='task.startDate']").val(),$("input[name='task.endDate']").val())==-1){
				alert("开始时间不能大于结束时间");
				$("#datepicker_1").focus();
				return false;
			}
			if(flag){
				$("#form1").submit();
				progressBar.show();
			}
		}
		
		function compareDate(startDate,endDate){
			var arr1 = startDate.split("-");    
			var date1 = new Date(arr1[0],parseInt(arr1[1])-1,arr1[2]);    
			   
			var arr2 = endDate.split("-");    
			var date2 = new Date(arr2[0],parseInt(arr2[1])-1,arr2[2]);    
			   
			if(date1>date2)
				return -1;
			return 1;
	}
		
		
		
		function checkStartAndEndTime(obj){
		//开始时间 
		var beginSearchTime=$("input[name='task.startDate']").val();
		//结束时间
		//alert(beginSearchTime);
		var endSearchTime=$("input[name='task.endDate']").val();
		var bTimes=beginSearchTime.split("-");
		var eTimes=endSearchTime.split("-");
		
		var beginDate=new Date(bTimes[0],bTimes[1],bTimes[2]);
		var endDate=new Date(eTimes[0],eTimes[1],eTimes[2]);
		var name=$(obj).attr("name");
		if(beginDate>endDate)
		{
			alert("结束时间不能早于开始时间！")
			if(name=='task.endDate')
				$("input[name='task.endDate']").val($("#_endDate").val());
			else
				$("input[name='task.startDate']").val($("#_startDate").val());
		}
		
	}
	$(document).ready(function(){
	<s:if test="task.triggerShifting==null">
		$("#triggerShifting").val("0");
	</s:if>
	
	}
		
	);
</script>
</head>

<body>
	<div style="width:100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<td nowrap="nowrap">
					<div class="toolbar">
						<div class="tableinfo">
							<span><img src="images/icon01.gif" /> </span><b style="font-size:12px;"> 当前位置
								>> 报表处理 >> 任务定制 >> 基础信息</b>
						</div>
					</div></td>
			</tr>
		</table>
	</div>
	<br />
	<br />

	<div><font color="red"><s:fielderror/></font></div>
	<div id="main_container" align="center">

		<fieldset style="width:400px;">
			<legend>基础信息</legend>
			<s:form action="save_workTaskInfoAction" method="post" namespace="/" id="form1" >
				<table cellpadding="12" cellspacing="15">

					<tr>
						<td width="150" align="right"><img
							src="images/icons/feed_document.png"></img></td>
						<td>任务名称</td>
						<td width="250"><s:textfield id="taskName"  size="25" 
							 name="task.taskName" />
						</td>
					</tr>
					<tr>
						<td align="right"><img src="images/icons/drive_go.png"></img>
						</td>
						<td>频&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度</td>
						<td>
						
						<s:select cssStyle="width:150px"  name="task.freqId" id="freq"  list="freqMap" listKey="key" listValue="value"  > 
						</s:select>
						</td>
					</tr>
					<tr>
						<td align="right"><img
							src="images/icons/document-library.png"></img></td>
						<td>业务条线</td>
						<td><s:select id="busiLineId" name="task.busiLineId" cssStyle="width:150px"
							list="busiLineList" listKey="busiLineId" listValue="busiLineName" >
						</s:select>
					</tr>
					<tr>
						<td align="right"><img src="images/icons/database.png"></img>
						</td>
						<td>任务类型</td>
						<td><s:select  id="taskTypeId"  name="task.taskTypeId" cssStyle="width:150px"
						list="taskTypeList" headerKey="-1" headerValue="---请选择任务类型---" listKey="taskTypeId" listValue="taskTypeName" >
						</s:select></td>
					</tr>
					<tr>
						<td align="right"><img src="images/icons/computer.png"></img>
						</td>
						<td>触发方式</td>
						<td><s:select id="triggerType"  name="task.trrigerId" cssStyle="width:150px"
							list="#{'auto':'系统触发'}" ></s:select>
						</td>
					</tr>
								
					<tr>
						<td align="right"><img
							src="images/icons/communication.png"></img></td>
						<td>报送偏移</td>
						<td><s:textfield id="triggerShifting"  size="25"
							name="task.triggerShifting" value="0"/></td>
					</tr>

					<tr>
						<td align="right"><img src="images/icons/full-time.jpg"></img>
						</td>
						<td>起始时间</td>
						<td><s:textfield name="task.startDate"
							 id="datepicker_1" readonly="readonly" size="25"  onchange ="checkStartAndEndTime(this)"
							/></td>
					<tr>
						<td align="right"><img src="images/icons/full-time.jpg"></img>
						</td>
						<td>结束时间</td>
						<td><s:textfield name="task.endDate"
							id="datepicker_2" readonly="readonly" size="25" onchange ="checkStartAndEndTime(this)"
							/></td>
					</tr>
					<%-- <tr>
						<td width="150" align="right"><img src="images/icons/gjzf.png"></img></td>
						<td>任务时间限&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;定</td>
						<td width="250"><s:textfield id="taskTime" value="" size="25" 
							 name="task.taskTime" />
						</td>
					</tr> --%>
					<tr>
						<td align="right"><img src="images/icons/feed_document.png"></img>
						</td>
						<td>是否生效</td>
						<td><s:select id="publicFlag"  name="task.publicFlag" cssStyle="width:150px"
							list="#{0:'否',1:'是'}" ></s:select>
						</td>
					</tr>
					
					
					<tr>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td ><input type="button" value="返   回" onclick="history.back()"></td>
						<td >&nbsp;<input type="reset" value="清   空">&nbsp;&nbsp;&nbsp; <input type="button" value="下一步" onclick="check()" ></td>
					</tr>
				</table>
			</s:form>
		</fieldset>
	</div>
</body>