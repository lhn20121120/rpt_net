<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.fitech.model.worktask.common.WorkTaskConfig" %>
<%@page import="com.fitech.model.worktask.security.Operator"%>
<%@page import="com.fitech.framework.core.common.Config"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//System.out.println("path:"+path);
//System.out.println("basePath:"+basePath);
Operator operator = null;
	if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
		operator = (Operator) session
				.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
	//String userId=String.valueOf(operator.getOperatorId());
	//String 	userId= operator.getOperatorId();//测试用
	//System.out.println("userId:"+userId);
	String 	userId= operator.getOperatorId()+"";
	
	String  jsonPath = Config.WEBROOTPATH ;
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<title>新重报任务</title>
	
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
	
	<script src="<%=request.getContextPath() %>/scripts/datepicker/jquery.ui.datepicker.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/tree.js"></script>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/datepicker/demos.css"/>
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/themes/icon.css"/>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/easyui-lang-zh_CN.js"></script>
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
		
		$(function(){
			$( "#dialog" ).dialog({
				autoOpen: false,
				height:'auto',
				width:'300px',
				style:'font-size:14px'
			});
			
			//任务选中
			$("select[name='etlTaskMoniVo.taskId'] option").each(function(){
				if($(this).val()=="${etlTaskMoniVo.taskId}"){
					$(this).attr("selected","selected");				
				}
			});
			
			//任务状态选中
			$("select[name='etlTaskMoniVo.overFlag'] option").each(function(){
				if($(this).val()=="${etlTaskMoniVo.overFlag}"){
					$(this).attr("selected","selected");
				}
			});
			
			//任务被选中
			if(${moniIds!=null && moniIds!=""}){
				var moniIds="${moniIds}";
				var moniArray=moniIds.split(",");
				for(i=0;i<moniArray.length;i++){
					$(":checkbox").each(function(){
						if($(this).val()==moniArray[i])
							$(this).attr("checked","checked");
					});
				}
			}
				
		try{	$( "#datepicker_1" ).datepicker({
				  changeMonth: true,
				  changeYear: true
			});
			$( "#datepicker_2" ).datepicker({
				   changeMonth: true,
				   changeYear: true
			});
	    	$( "#datepicker_1" ).datepicker( "option", "dateFormat", "yy-mm-dd");
			$( "#datepicker_2" ).datepicker( "option", "dateFormat", "yy-mm-dd");
		}catch(e){};
			var date=new Date();
			var year=date.getFullYear();
			var month=date.getMonth()+1;
			if(month<10)
				month="0"+month;
			var day=date.getDate();
			
			var nowDate=year+"-"+month+"-"+day;
			var beginSearchTime="${etlTaskMoniVo.beginSearchTime}";
			var endSearchTime="${etlTaskMoniVo.endSearchTime}";
			//查询开始时间赋初始值为当前时间
			if(beginSearchTime==null || beginSearchTime=="")
				$("input[name='etlTaskMoniVo.beginSearchTime']").val(nowDate);
			else{
				$("input[name='etlTaskMoniVo.beginSearchTime']").val(beginSearchTime);
			}
			//查询结束时间赋初始值为当前时间
			if(endSearchTime==null || endSearchTime=="")
				$("input[name='etlTaskMoniVo.endSearchTime']").val(nowDate);
			else
				$("input[name='etlTaskMoniVo.endSearchTime']").val(endSearchTime);
		});
		
		function dialogClose(){
			$( "#dialog" ).dialog("close");
		}
		
		function dialogOpen(taskMoniId,procId,taskName){
			$("#spanTaskName").text(taskName);
			etlTaskProcStatus.findEtlTaskProcStatusById(taskMoniId,procId,function(proc){
				if(proc!=null || proc!="null"){
					$("#errorMessageTD").text(proc.problemSource);
					$( "#dialog" ).css("padding-top","15");
					$( "#dialog" ).dialog("open");
				}
			});
    		
   	 	}	
   	 	
	</script>
<script type="text/javascript">
	function changeImg(){
		var fl = getId("fs");
		if(fl.style.display =='none'){
			fl.style.display = 'block';
		//	getId('d2').style.visibility = 'hidden';
			getId("tl").style.height = "50%";
			$("#d1 img").attr("src","<%=request.getContextPath() %>/images/task_up.gif");
		}else{
			fl.style.display ='none';
		//	getId('d1').style.visibility = 'hidden';
			$("#d1 img").attr("src","<%=request.getContextPath() %>/images/taskwork_down.gif");
			getId("tl").style.height = "100%";
		}
	}
	function init(taskTerm){
		//if(getId("fs").style.dispaly=='none')
			//getId("d1").style.display = 'none';
		//else
			//getId("d2").style.display = 'none';
		//getId("d1").style.visibility='hidden';
		//getId("d2").style.visibility='hidden';
		if(taskTerm!=undefined && taskTerm!=""){
			$("#datepicker_2").val(taskTerm);
		}
	}
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
			
			getId("shForm").action+="?pageResults.currentPage="+parseInt(vl);
			getId("shForm").submit();
		}
	}

	function turntoPage1(currentPage,pageCount){
		if(currentPage!=undefined && currentPage>0){
			getId("shForm").action+="?pageResults.currentPage="+currentPage;
			getId("shForm").submit();
		}
	}

	
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
		/* $("#fm").submit(function(){
			if($("input[name=cks]:checked").length<=0){
				alert("请选中一项任务执行!");
				return false;
			}
			var res = confirm("确认要重报吗?");
			if(res){
				getId("fm").action+="?pendingTaskQueryConditions.orgId="+getId("orgId").value;
							//"&pendingTaskQueryConditions.orgName="+getId("orgName").value;
				//alert(1);getId("nodeFlag1").value=getId("nodeFlag").value;
				//getId("taskTerm1").value=getId("taskTerm").value;
				//getId("orgId1").value=getId("orgId").value;
				
				//getId("freqId").value="'"+getId("freqId1").value+"'";
				//getId("taskId").value="'"+getId("taskId1").value+"'";
				//getId("orgName").value="'"+getId("orgName1").value+"'";
				//alert(1);
				getId("taskfreqId").value=getId("freqId").value;
				getId("tasknodeFlag").value=getId("nodeFlag").value;
				getId("taskOrgName").value=getId("orgName").value;
				//	alert(getId("taskTerm").value);
				getId("wxwtaskTerm").value=getId("datepicker_2").value;
				//alert(1);
				getId("wxwtaskId").value=getId("taskId").value;
				getId("workbusiLine").value=getId("busiLine").value;
				
				return true;
			}
			else{
				return false;
			}
		}); */
		$('#tt2').tree({
			checkbox: false,
			cascadeCheck:false,
			
			url: '<%=request.getContextPath()%>/json/org_tree_data_<%=userId%>.json',
			
			onClick:function(node){
				$(this).tree('toggle', node.target);
				$("#orgId").val(node.id);
				$("#orgName").val(node.text);
				
				search();
				getId("jigouqu").style.display='none';
			},
			onContextMenu: function(e, node){
				e.preventDefault();
				$('#tt2').tree('select', node.target);
				//$('#mm').menu('show', {
				//	left: e.pageX,
				//	top: e.pageY
				//});
			}
		});
		initDate1();
	});
	function _backWorkTask(){
		if($("input[name=cks]:checked").length<=0){
			alert("请选中一项任务执行!");
			return false;
		}
		var res = confirm("确认要退回吗?");
		if(res){
			getId("fm").action="<%=request.getContextPath() %>/pendingTaskAction!backTask.action";
			getId("fm").submit();
		}
	}
	
	function _toUrl(taskMoniId,nodeId,orgId,orgName,taskName,th,freqId,taskTrem){
		var index  = th.substr(2);
		getId("wMoni.id.taskMoniId").value = taskMoniId;
		getId("wMoni.id.nodeId").value = nodeId;
		getId("wMoni.id.orgId").value = orgId;
		getId("taskName").value = taskName;
		getId("ifreqId").value = freqId;
		getId("iterm").value = taskTrem;
		var taskTaget = getId("cks"+index).value;
		getId("taskTaget").value = taskTaget;
		var url = "<%=request.getContextPath() %>/pendingTaskAction!handlerReport.action";
		$.post(
				url,
				{
				"wMoni.id.taskMoniId":taskMoniId,
				"wMoni.id.nodeId":nodeId,
				"wMoni.id.orgId":orgId
					},
				function(data){
					if(data!=undefined && data!=""){
						if(data.indexOf("#")>-1){
						
							var param = data.split("#");
							//alert(param[0]);
							//0:报表id,1:业务条线,2:期数,3:参数url,4:form表单名称,5:机构id
							if(param!=undefined){
								var repForm = getId("repForm");
								//alert(param[0]+"-"+param[1]+"-"+
								//		param[2]+"-"+param[3]+"-"+param[4]);
								if(param[3]==""){
									alert("url参数异常。。。");
									return;
								}
								repForm.action="/rpt_net/"+param[3];
								
								repForm.name=param[4];

								getId("workTaskTerm").value=param[2];
								
								getId("nodeId").value=param[8];

								getId("workTaskOrgId").value=param[5];
								
								getId("workTaskBusiLine").value=param[1];
								
								getId("workTaskFreq").value=param[6];
								
								getId("templateIds").value=param[0];
								
								getId("nodeFlag").value=param[7];
								
								getId("url").value="/rpt_net/"+param[3]+"?workTaskTerm="+param[2]+"&workTaskOrgId="+param[5]+"&workTaskTemp="+param[0];
								//alert(getId("url").value);
								repForm.submit();

								getId("ft").style.display='block';
								//$("#th").css("background-color","red");
								$("#tble tr").each(function(i){
									if(i>1){
										$(this).css("background-color","");
									}
								});
								document.getElementById(th).style.backgroundColor='lavender';
								getId("f1").innerText= orgName;
								getId("f2").innerText= taskName;
							}
							/*
							var param=data.substr(0,data.indexOf("-")-1);
							var busiLine = data.substr(data.indexOf("-")+1,data.length);
							getId("ft").style.display='block';
							if(busiLine=='yjtx'){
								window.rptFrame.location.href='/rpt_net/viewDataReport.do';
							}else if(busiLine=='rhtx'){
								window.rptFrame.location.href='/rpt_net/viewNXDataReport.do';
							}else if(busiLine=='qttx'){
								window.rptFrame.location.href='/rpt_net/viewNXDataReport.do';
							}*/
						}
					}
				}
			);
		
	}

	function move(th){
		//th.style.cursor = 'hand';
		if(getId("fs").style.display=='none'){
			
			getId("d2").style.visibility = "visible";
		}else{
			getId("d1").style.visibility = "visible";
		}
	}

	function out(th){
		//getId("d1").style.display = 'none';
	//	th.style.cursor = 'none';
		if(getId("d2").style.visibility=='visible'){
			//setTimeout("getId('d2').style.visibility = 'hidden'",3000);
			getId('d2').style.visibility = 'hidden';
		}else{
			//setTimeout("getId('d1').style.visibility = 'hidden'",3000);
			getId('d1').style.visibility = 'hidden';
		}
	}

	function go_backTaskDetail(taskMoniId,nodeId,orgId){
		var url = "<%=request.getContextPath() %>/pendingTaskAction!findBackTaskDetail.action?wMoni.id.taskMoniId="+taskMoniId+"&wMoni.id.orgId="+orgId+"&wMoni.id.nodeId="+nodeId;
		window.showModalDialog(url, null,"dialogWidth:624px;dialogHeight:468px;resizable:yes;scroll:yes");
	}

	function taskMove(taskMoniId,th){
		//th.onmousemove="";
		if(taskMoniId!=undefined && parseInt(taskMoniId)>0){
			var x = event.clientX;
			var y = event.clientY;
			$.post(
					"<%=request.getContextPath()%>/pendingTaskAction!findAllNodeMonis.action",
					{"wMoni.id.taskMoniId":taskMoniId},
					function(rv){
						var count =1;
						var content = "";
						if(rv!=undefined && rv.length>0){
							getId("content").innerHTML="";
							for(var i in rv){
								if(rv[i].nodeFlag==<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WADC"/>){
									content +="<span class='taskSpan' style='color:#ebad06;'>["+rv[i].taskNodeName+"]</span><img src='images/pright.gif'>";
								}else if(rv[i].nodeFlag==<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_PASS"/>){
									content +="<span class='taskSpan' style='color:#8ba1aa;'>["+rv[i].taskNodeName+"]</span><img src='images/pright.gif'>";
								}else{
									content +="<span class='taskSpan' style='color:#2194E0'>["+rv[i].taskNodeName+"]</span><img src='images/pright.gif'>";
								}
								if(count%4==0){
									content +="<br><br>";		
								}
								count++;
							}
							getId("taskFlowing").style.top=y+5;
							getId("taskFlowing").style.left=x-550;
							getId("taskFlowing").style.display='block';

							getId("content").innerHTML=content;
						}
					}
			);
			
		}
	}

	function taskOut(th){
		getId("taskFlowing").style.display='none';
	}

	function search(){
		$("#shForm").submit();
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

	function mousefocus(){
		getId("jigouqu").style.top =getObjectTop(getId("orgName"))+20;
		getId("jigouqu").style.left =getObjectLeft(getId("orgName"));
		if(getId("jigouqu").style.display=='none'){
			getId("jigouqu").style.display='block';
		}else
			getId("jigouqu").style.display='none';
	}
	window.onresize=function(){
		getId("jigouqu").style.top =getObjectTop(getId("orgName"))+20;
		getId("jigouqu").style.left =getObjectLeft(getId("orgName"));
		initDate1();
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
</script>
<script>
function showdivold(){
	
	if($("input[name=cks]:checked").length<=0){
		alert("请选中一项任务执行!");
		return false;
	}
	var res = confirm("确认要重报该批任务下的所有报表吗?");
	if(res){
		getId("fm").action="<%=request.getContextPath() %>/pendingTaskAction!turnToRep.action";
		getId("fm").action+="?pendingTaskQueryConditions.orgId="+getId("orgId").value;
					//"&pendingTaskQueryConditions.orgName="+getId("orgName").value;
		/*alert(1);getId("nodeFlag1").value=getId("nodeFlag").value;
		getId("taskTerm1").value=getId("taskTerm").value;
		getId("orgId1").value=getId("orgId").value;
		
		getId("freqId").value="'"+getId("freqId1").value+"'";
		getId("taskId").value="'"+getId("taskId1").value+"'";
		getId("orgName").value="'"+getId("orgName1").value+"'";
		alert(1);*/
		getId("taskfreqId").value=getId("freqId").value;
		getId("tasknodeFlag").value=getId("nodeFlag").value;
		getId("taskOrgName").value=getId("orgName").value;
		//	alert(getId("taskTerm").value);
		getId("wxwtaskTerm").value=getId("datepicker_2").value;
		//alert(1);
		getId("wxwtaskId").value=getId("taskId").value;
		getId("workbusiLine").value=getId("busiLine").value;
		
		return false;
	}
	else{
		return false;
	}
	
}

	function showdiv(){
		var flag  = true;
		var taskTaget = getId("taskTaget").value;
		
         var res = confirm("确认要重报所选的报表吗?");
     	if(res){
			try{
				getId("fm").action="<%=request.getContextPath() %>/pendingTaskAction!newTurnToRep.action";
				var count = window.frames.rptFrame.document.getElementById("countChk").value;
				if(count==undefined || count==""){
					alert("请点击任务名称选择要重报的报表!");
					return false;
				}
				var res = 0;
				var reStr = "" ;
				for(var i=1;i<=count;i++){
					var ck = window.frames.rptFrame.document.getElementById("chk"+i);
					if(ck.checked){
						res++;
						reStr  += ck.templateVersion +",";
					}
				}
				if(res==0){
					alert("请点击任务名称选择要重报的报表!");
					return false;
				}else{
					if($("input[name=cks]:checked").length>0){
						alert("选择报表后请不要在勾选任务");
						return false;
					}
				}
				reStr   = reStr .substr (0 ,reStr.length-1);
				getId("tasktemplateIds").value=reStr;
				
				return true;
				//layerAction('add');
			}catch(e){
				alert("请点击任务名称选择要重报的报表!");
				return false;
			}
     	}else{
     		return false;
     	}
	}
</script>
<s:if test="msg!=null">
	<script type="text/javascript">
		alert("<s:property value='msg'/>");
	</script>
</s:if>
<style type="text/css">
	#taskFlowing{
		width:688px;
		height:140px;
		display:none;
		border:0 solid black;
		background-image:url("<%=request.getContextPath() %>/images/task.jpg");
		position:absolute;
		z-index:1;
	}
	#taskFlowing div{
		width:663px;
		height:82px;
		margin-top:45px;
		text-align:left;
	/*	color:#8ba1aa;*/
		/*color:#ebad06;*/
		/*overflow-y:scroll;*/
		overflow:auto;
		border:0 solid black;
	}
	.taskSpan{
		font-family:Arial;font-size:15px;font-weight:bold;font-style:normal;text-decoration:none;
	}
	.selectClass{
		width: 100px;vertical-align: middle;
	}
</style>
</head>

<body onload="init('<s:property value="pendingTaskQueryConditions.taskTerm"/>')">
<div id="taskFlowing" >
	<div id="content">
		<!-- <table >
			<tr>
			<s:set name="count" value="nodeNameList.size"></s:set>
			<s:if test="nodeNameList!=null&&nodeNameList.size()>0"   >
				<s:iterator id="n" value="nodeNameList" status="s"   >
					<td >
						<span align="center" style="font-family:Arial;font-size:15px;font-weight:bold;font-style:normal;text-decoration:none;color: red">[<s:property value="#n.nodeName" />]</span>
					</td>	
					<s:if test="(#count-#s.index-1)>0">
							<td><img alt="" src="images/pright.gif"></td>
					</s:if>
				</s:iterator>
			</s:if>
			</tr>
		</table> -->
		
	</div>
</div>
<div style="margin-top:5px;">
<table width="100%" height="20px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
		<div class="tableinfo" ><span style="font-size:12px;"><img src="<%=request.getContextPath() %>/images/icon01.gif" alt="列表" /></span><b style="font-size:12px;">当前位置 >>报表处理 >>重报任务</b></div>
    </td>
  </tr>
</table>
</div>
<form action="" id="repForm" target="rptFrame" method="post">
	<input type="hidden" id="templateIds" name="workTaskTemp"/>
	<input type="hidden" id="nodeFlag" name="nodeFlag"/>
	<input type="hidden" id="workTaskTerm" name="workTaskTerm"/>
	<input type="hidden" id="nodeId" name="nodeId"/>
	<input type="hidden" id="workTaskOrgId" name="workTaskOrgId"/>
	<input type="hidden" id="workTaskBusiLine" name="workTaskBusiLine"/>
	<input type="hidden" id="workTaskFreq" name="workTaskFreq"/>
	<input type="hidden" id="url" name="url"/>
	<input type="hidden" id="styleFlg" name="styleFlg"/>
</form>
<fieldset style="background-color:white" id="fs">
<legend><b><font color="#075587">任务查询栏</font></b></legend>
<div id="jigouqu"  region="east" iconCls="icon-reload" title="机构" split="true" style="left:316px;top:70px;display:none;width: 150px;height: 150px ;background-color:#f5f5f5;border :1px solid Silver;overflow:auto; position:absolute; z-index:2;" >
	<ul  class="easyui-tree" id="tt2" style="width: 200;background-color:#EFEFEF"></ul>
</div>
<div style="width:100%;margin-bottom:5px" align="center">
<form action="<%=request.getContextPath() %>/pendingTaskAction!findAllRepTaskInfo.action" id="shForm" method="post">
<table border="0" width="98%" align="center" cellpadding="0" cellspacing="0" >
		<tr>
			<td width="20%"  align="left" >
				<!-- <input type="text" name="pendingTaskQueryConditions.taskName" size="25" value="<s:property value="pendingTaskQueryConditions.taskName"/>" id="templateId"/> -->
				<!-- 任务列表 <select name="pendingTaskQueryConditions.freqId" style="width: 100px;vertical-align: middle">
			   		
			    </select> -->
			   	任务列表
			   	<s:select cssStyle="width:75%" list="workTaskInfos" id="taskId" onchange="search()" headerKey="-1" headerValue="----------------全部---------------------------" name="pendingTaskQueryConditions.taskId" listKey="taskId" listValue="taskName" cssClass="selectClass"></s:select>
            <td width="13%" align="center">
            	频度 <select onchange="search()" name="pendingTaskQueryConditions.freqId" style="width: 100px;vertical-align: middle">
				  <option value="-1" selected="selected">---全部---</option>
	              	<option <s:if test="pendingTaskQueryConditions.freqId=='day'">selected="selected"</s:if>  value="day">日频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='month'">selected="selected"</s:if> value="month">月频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='season'">selected="selected"</s:if> value="season">季频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='halfyear'">selected="selected"</s:if> value="halfyear">半年频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='year'">selected="selected"</s:if> value="year">年频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId =='yearbegincarry'">selected="selected"</s:if> value="yearbegincarry">年初结转</option>
			    </select>
			   <!--  <input type="hidden" name="moniIds" id="moniIdsInput"/>
			    <input type="hidden" name="query" /> -->
            </td>
            <td width="18%" align="center" valign="middle">
            		<input type="hidden" id="orgId" name="pendingTaskQueryConditions.orgId" value="<s:property value="pendingTaskQueryConditions.orgId"/>"/>
            		机构  <input type="text"  id="orgName" onclick="mousefocus()"  style="width:150px;cursor:hand" size="25" name="pendingTaskQueryConditions.orgName" value="<s:property value="pendingTaskQueryConditions.orgName"/>" />
            </td>
            <td width="15%" align="center" valign="middle">
            	期数  <input width="100px"  id="datepicker_2"  readonly="readonly" onmouseout="hiddenDate1()" onmousemove="showDate1()" style="cursor: hand;height: 18px;vertical-align: middle" value="<s:property value="pendingTaskQueryConditions.taskTerm"/>" name="pendingTaskQueryConditions.taskTerm" onchange="search()"/> 
            	<span onmousemove="cleartimeout()" onmouseout="this.style.display='none'" onclick="document.getElementById('datepicker_2').value='';this.style.display='none';change();" style="border:0 solid red;width:15px;display:none;height:15px;cursor:hand;position:absolute;" id="date1" ><img src="<%=request.getContextPath()%>/images/clear.bmp"/></span>
            	</td>
            <td width="15%" align="center" valign="middle" style="display:none;">
            	任务状态
            	<select onchange="search()" id="nodeFlag" name="pendingTaskQueryConditions.nodeFlag" style="width: 100px;vertical-align: middle">
<%--				 <option value="-1" <s:if test="pendingTaskQueryConditions.nodeFlag==-1">selected="selected"</s:if>>---全部---</option>--%>
<%--				  <option value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WADC"/>" <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WADC">selected="selected"</s:if>>待处理</option>--%>
<%--	              	<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">selected="selected"</s:if>  value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT"/>">等待</option>--%>
					<option selected="selected" value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_COMM"/>">已提交</option>
<%--					<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_PASS">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_PASS"/>">审核通过</option>--%>
<%--					<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU"/>">审核未通过</option>--%>
<%--					<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA"/>">退回等待</option>--%>
			    </select>
            </td>
            <td width="14%" align="center" valign="middle">
            	业务条线
            	<select disabled="disabled" onchange="search()" id="busiLine" name="pendingTaskQueryConditions.busiLine" style="width: 100px;vertical-align: middle">
	              	<option <s:if test="pendingTaskQueryConditions.busiLine==@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX">selected="selected"</s:if>  value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_YJTX"/>">银监</option>
					<option <s:if test="pendingTaskQueryConditions.busiLine==@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_RHTX"/>">人行</option>
					<option <s:if test="pendingTaskQueryConditions.busiLine==@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@BUSI_LINE_QTTX"/>">其他</option>
			    </select>
            </td>
            <td width="8%" align="center">
            	<input type="submit" name="button" id="button" value="查询" class="searchbtn" />&nbsp;&nbsp;
            </td>
		</tr>
 	</table>

</form>
</div>
<form id="fm" action="<%=request.getContextPath() %>/pendingTaskAction!newTurnToRep.action" method="post">
<table id="tble" width="100%" border="0" cellspacing="0" cellpadding="0"  class="ntable" align="center">
	<tr >
   		<td colspan="9" bgcolor="#FFF0D0" >
   		<!--  	<input type="hidden"  name="pendingTaskQueryConditions.nodeFlag" id="nodeFlag1" />
			<input type="hidden"  name="pendingTaskQueryConditions.taskTerm" id="taskTerm1"/>-->
			<input type="hidden" name="pendingTaskQueryConditions.orgName" id="taskOrgName"/>
			<input type="hidden" name="pendingTaskQueryConditions.freqId" id="taskfreqId"/>
			<input type="hidden" name="pendingTaskQueryConditions.nodeFlag" id="tasknodeFlag"/>
			<input type="hidden"  name="pendingTaskQueryConditions.busiLine" id="workbusiLine"/>
			<input type="hidden" name="pendingTaskQueryConditions.taskTerm" id="wxwtaskTerm"/>
			<input type="hidden" name="pendingTaskQueryConditions.taskId" id="wxwtaskId"/>
			
			
			<input type="hidden" name="taskName" value="<s:property value='taskName'/>"/>
			<input type="hidden" name="tasktemplateIds" id="taskTemplateIds"/>
			<input type="hidden" name="wMoni.id.taskMoniId" id="wMoni.id.taskMoniId" />
			<input type="hidden" name="wMoni.id.nodeId" id="wMoni.id.nodeId" />
			<input type="hidden" name="wMoni.id.orgId" id="wMoni.id.nodeId" />
			<input type="hidden" name="taskTaget" id="taskTaget" />
			<input type="hidden" name="ifreqId" id="ifreqId" />
			<input type="hidden" name="iterm" id="iterm" />
			<!--<input type="hidden"  name="pendingTaskQueryConditions.orgId" id="orgId1"/>
			<input type="hidden"  name="pendingTaskQueryConditions.orgName" id="orgName1"/>
			<input type="hidden"  name="pendingTaskQueryConditions.freqId" id="freqId1"/>
			<input type="hidden"  name="pendingTaskQueryConditions.taskId" id="taskId1"/>-->
   		<b>任务处理列表</b>
   		<input name="pendingTaskQueryConditions.roleId" type="hidden" value="<s:property value="pendingTaskQueryConditions.roleId"/>"/>
   		</td>
  </tr>
  
  <tr >
   		<td colspan="9" align="left" style="height:35px;">
   		<!-- <input type="submit"  onclick="return showdivold()" class="searchbtn" value="重报" /> -->
   		<input type="submit" onclick="return showdiv()" class="searchbtn"  value="报表重报" />
   		</td>
  </tr>
  <tr>
    <td width="5%" bgcolor="#FFF0D0" align="center"><input type="checkbox" id="checkAll"/></td>
    <td width="13%" bgcolor="#FFF0D0" align="center"><strong>机构名称</strong></td>
    <td width="13%" bgcolor="#FFF0D0" align="center"><strong>任务名称</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>任务频度</strong></td>
    <td width="12%" bgcolor="#FFF0D0" align="center"><strong>期数</strong></td>
    <td width="16%" bgcolor="#FFF0D0" align="center"><strong>报送截止日期</strong></td>
    <td width="13%" bgcolor="#FFF0D0" align="center"><strong>报送阶段</strong></td>
    <td width="8%" bgcolor="#FFF0D0" align="center"><strong>状态</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>报送状态</strong></td>
  </tr>
  <s:if test="pageResults!=null && pageResults.results.size()!=0">
  		<s:iterator value="pageResults.results" status="i">
  			<tr id="th<s:property value='#i.index'/>">
			  	<td align="center">
			  	<input type="checkbox" name="cks" id="cks<s:property value='#i.index'/>" term="<s:date name='taskTerm' format='yyyy-MM-dd'/>" freqId="<s:property value='freqId'/>" value="<s:property value="taskMoniId"/>,<s:property value="nodeId"/>,<s:property value="orgId"/>,<s:property value="busiLine"/>,<s:property value="lateRepDate"/>"/>
			  	</td>
			  	<td align="center"><s:property value="orgName"/></td>
			  	<td align="center">
				 <a href="javascript:;" onclick="_toUrl(<s:property value="taskMoniId"/>,<s:property value="nodeId"/>,'<s:property value="orgId"/>','<s:property value="orgName"/>','<s:property value="taskName"/>','th<s:property value="#i.index"/>','<s:property value="freqId"/>' ,'<s:date name="taskTerm" format="yyyy-MM-dd"/>')" target="rptFrame" style="color:blue;text-decoration:underline" onmousemove="this.style.color='red';" onmouseout="this.style.color='blue'"><s:property value="taskName"/></a>
			  	</td>
			  	<td align="center"><s:property value="freqId"/></td>
			  	<td align="center"><s:date name="taskTerm" format="yyyy-MM-dd"/></td>
			  	<td align="center">
			  		<s:if test="@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT==@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT_DAY">
						<s:date name="lateRepDate" format="yyyy-MM-dd"/>  		
			  		</s:if>
			  		<s:elseif test="@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT==@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT_HOUR">
			  			<s:date name="lateRepDate" format="yyyy-MM-dd HH:mm:ss"/>
			  		</s:elseif>
			  	</td>
			  	<td align="center" ><s:property value="taskNodeName"/></td>
			  <!-- 	<td align="center" ><font onmouseover="taskMove(<s:property value="taskMoniId"/>,this)" onmouseout="taskOut(this)"><s:property value="taskNodeName"/></font></td> -->
			  	<td align="center">
				  	<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
				  		<a href="javascript:;" onclick="go_backTaskDetail(<s:property value="taskMoniId"/>,<s:property value="nodeId"/>,'<s:property value="orgId"/>')" style="color:blue;text-decoration:underline"><s:property value="taskState"/></a>
				  	</s:if>
				  	<s:else>
				  		<s:property value="taskState"/>
				  	</s:else>
			  	</td>
			  	<td align="center"><s:property value="lateRepFlagName"/></td>
			</tr>
  		</s:iterator>
  </s:if>
  <s:else>
  	 <tr >
   		<td colspan="9">无重报任务处理</td>
 	 </tr>
  </s:else>
</table>
</form>
<div style="margin-top:7px;">
            <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
			  <tr>
				<td align="left"><img src="<%=request.getContextPath() %>/images/jt1.gif" width="4" height="7" />
				 共<s:property value="pageResults.totalCount"/> 条记录，每页<s:property value="pageResults.pageSize"/> 条</td>
				<td style="text-indent:opx; padding-right:8px;"><table width="320" border="0" align="right" cellpadding="0" cellspacing="0">
				  <tr>
					<td><a href="javascript:;" onclick="turntoPage1(1,<s:property value="pageResults.pageCount"/>)">首页</a></td>
					<td><img src="<%=request.getContextPath() %>/images/pleft.gif" width="12" height="12" style="cursor:hand;" onclick="turntoPage1(<s:property value="pageResults.currentPage-1"/>,<s:property value="pageResults.pageCount"/>)" /></td>
					<td><s:property value="pageResults.currentPage"/>/<s:property value="pageResults.pageCount"/>页</td>
					<td><img src="<%=request.getContextPath() %>/images/pright.gif" width="12" height="12" style="cursor:hand;" 
			<s:if test="(pageResults.currentPage+1)<=pageResults.pageCount">onclick="turntoPage1(<s:property value="pageResults.currentPage+1"/>,<s:property value="pageResults.pageCount"/>)"</s:if>		/></td>
					<td><a href="javascript:;" onclick="turntoPage1(<s:property value="pageResults.pageCount"/>,<s:property value="pageResults.pageCount"/>)">尾页</a></td>
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
 <div id="d1" ><img src="<%=request.getContextPath() %>/images/task_up.gif"  onclick="changeImg()" style="cursor:hand;"/></div>	
	<table style="margin-top:3px;" width="100%" height="50%" id="tl" border="0" cellspacing="0" cellpadding="0"  class="ntable" align="center">
	<tr>
   		<td colspan="7" height="5%" bgcolor="#e6effb"><b><font id="f1">任务处理列表</font>>><font id="f2">报表处理列表</font></b></td>
  	</tr>
  	<tr >
  		<td colspan="7" id="ft" style="display:none;">
  			<iframe width="100%" height="100%" name="rptFrame"  src=""></iframe>
  		</td>
  	</tr>
</table> 
</body>
</html>