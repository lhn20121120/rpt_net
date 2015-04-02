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
	String  userName = operator.getUserName();
	String  jsonPath = Config.WEBROOTPATH ;
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>待办任务</title>
	
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
	<script type="text/javascript" src="../js/layer.js"></script>
	<script src="<%=request.getContextPath() %>/scripts/datepicker/jquery.ui.datepicker.js" type="text/javascript" ></script> 
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/tree/tree.js"></script>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/datepicker/demos.css" charset="utf-8"/>
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/themes/icon.css"/>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/easyui-lang-zh_CN.js"></script>
		<script language="javascript" src="<%=request.getContextPath() %>/scripts/progressBar.js"></script>
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
		try{
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
var progressBar=new ProgressBar("正在跳转，请稍后........");
	function changeImg(){
		var fl = getId("fs");
		if(fl.style.display =='none'){
			fl.style.display = 'block';
		//	getId('d2').style.visibility = 'hidden';
			getId("tl").style.height = "50%";
			getId("ifm").style.height="70%";
			getId("taskImg").src = "<%=request.getContextPath() %>/images/task_up.gif";
		}else{
			fl.style.display ='none';
		//	getId('d1').style.visibility = 'hidden';
			//$("#d1 img").attr("src",);
			
			getId("taskImg").src = "<%=request.getContextPath() %>/images/taskwork_down.gif";
			getId("tl").style.height = "100%";
			getId("ifm").style.height="100%";
		}
	}
	function init(taskTerm){
		if(taskTerm!=undefined && taskTerm!=""){
			try{
			$("#datepicker_2").val(taskTerm);}catch(e){};
		}
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
		$("#main").scroll(function(){
			scrollTopNum=document.getElementById('main').scrollTop;
			scrollLeftNum=document.getElementById('main').scrollLeft;
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
		$('#tt2').tree({
			checkbox: false,
			cascadeCheck:false,
			
			url: '<%=request.getContextPath()%>/json/<s:property value="orgTreeFileName"/>',
			onClick:function(node){
				$(this).tree('toggle', node.target);
				$("#orgId").val(node.id);
				$("#orgName").val(node.text);
				getId("jigouqu").style.display='none';

				search();
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
	
	function commitTask(){
		if($("input[name=cks]:checked").length<=0){
				alert("请选中一项任务执行!");
				return;
			}
			var res = confirm("确认要提交吗?");
			if(res){
				getId("shForm").action="<%=request.getContextPath() %>/pendingTaskAction!commitTask.action";
				/*getId("fm").action+="?pendingTaskQueryConditions.orgId="+getId("orgId").value;
				getId("taskfreqId").value=getId("freqId").value;
				getId("tasknodeFlag").value=getId("nodeFlag").value;
				getId("taskOrgName").value=getId("orgName").value;
				getId("wxwtaskTerm").value=getId("datepicker_2").value;
				getId("wxwtaskId").value=getId("taskId").value;*/
				getId("shForm").submit();
				progressBar.show();
			}
			
	}
	
	var scrollTopNum=0;	
	var scrollLeftNum=0;
	function _backWorkTask(){
		if($("input[name=cks]:checked").length<=0){
			alert("请选中一项任务执行!");
			return false;
		}
		var res = confirm("确认要退回吗?");
		if(res){
			getId("shForm").action="<%=request.getContextPath() %>/pendingTaskAction!backTask.action";
			/*getId("fm").action+="?pendingTaskQueryConditions.orgId="+getId("orgId").value;
			getId("taskfreqId").value=getId("freqId").value;
			getId("tasknodeFlag").value=getId("nodeFlag").value;
			getId("taskOrgName").value=getId("orgName").value;
			getId("wxwtaskTerm").value=getId("datepicker_2").value;
			getId("wxwtaskId").value=getId("taskId").value;*/
			getId("shForm").submit();
			progressBar.show();
		}
	}
	
	function _toBbscURL(taskMoniId,nodeId,orgId,orgName,taskName,freqId){
		//alert("_toBbscURL");
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
						//alert(data);
						if(data.indexOf("#")>-1){
						
							var param = data.split("#");
							//alert(param[0]);
							//0:报表id,1:业务条线,2:期数,3:参数url,4:form表单名称,5:机构id
							if(param!=undefined && param.length==6){
								var repForm = getId("repForm");
								repForm.action="/rpt_net/exportRhAFReport.do";
								getId("workTaskTerm").value=param[2];
								getId("workTaskOrgId").value=param[5];
								getId("templateIds").value=param[0];
								getId("styleFlg").value="new";
								
								repForm.submit();

								getId("ft").style.display='block';
								//$("#th").css("background-color","red");
								document.getElementById(th).style.backgroundColor='lavender';
								$("#tble tr").each(function(i){
									if(i>1){
										$(this).css("background-color","");
									}
								});
								document.getElementById(th).style.backgroundColor='lavender';
								getId("f1").innerText= orgName;
								getId("f2").innerText= taskName;
								
							}
						
						}
					}
				}
			);
		
		
	}
	
	
	function getParams(taskMoniId,nodeId,orgId){
		var ddata ="";
		var url = "<%=request.getContextPath() %>/pendingTaskAction!handlerReport.action";
		
		$.ajax({ 
	          type : "post", 
	          url : "<%=request.getContextPath() %>/pendingTaskAction!handlerReport.action?wMoni.id.taskMoniId="+taskMoniId+
					"&wMoni.id.nodeId="+nodeId+
					"&wMoni.id.orgId="+orgId,
	          async : false, 
	          success : function(data){ 
	        	 // alert(data);
	        	  ddata=  data;
	          } 
	          }); 		
		return ddata;
	}
	
	/**
	*多任务报文导出 。
	*/
	function _allBbscURL(){
		//alert("多任务导出报文");	
		var list = $("input[name=cks]:checked");
		var count=$("input[name=cks]:checked").length;
		var params = "";
		var obj=null;
		//var pp = "";
		for(var i=0;i<count;i++){			
				obj=list[i];
				params+=(params==""?"":"##")+obj.value;			
		}	
		//alert("params="+params);
		var _params = params.split("##");
		//alert(_params.length);
		for(var i=0;i<_params.length;i++){			
			var pars = _params[i].split(",");
			var taskMoniId = pars[0];
			var nodeId = pars[1];
			var orgId = pars[2];
			var ddata = getParams(taskMoniId,nodeId,orgId);
			//alert("data="+ddata);
		
			if(ddata!=undefined && ddata!=""){
				//alert(ddata);
				if(ddata.indexOf("#")>-1){
				
					var p = ddata.split("#");
					//alert("p="+p);
					//alert(param[0]);
					//0:报表id,1:业务条线,2:期数,3:参数url,4:form表单名称,5:机构id
					if(p!=undefined && p.length==6){
	
						getId("workTaskTerm").value+=(getId("workTaskTerm").value==""?"":",")+p[2];
						//alert(p[2]);
						getId("workTaskOrgId").value+=(getId("workTaskOrgId").value==""?"":",")+p[5];
						//alert(p[0]);
						getId("templateIds").value+=(getId("templateIds").value==""?"":",")+p[0];
					}
				}
			}
		}
		//alert("templateIds="+getId("templateIds").value)
		goSubmit();
	}
	
	
	
	function goSubmit(){
		var repForm = getId("repForm");
		repForm.action="/rpt_net/exportRhAFReport.do";
		getId("styleFlg").value="new";
		repForm.submit();

		getId("ft").style.display='block';
		//$("#th").css("background-color","red");
		document.getElementById(th).style.backgroundColor='lavender';
		$("#tble tr").each(function(i){
			if(i>1){
				$(this).css("background-color","");
			}
		});
		
		getId("f1").innerText= orgName;
		getId("f2").innerText= "报文导出";
		
	}
	var isOpenBar = 0;//控制是否出现屏蔽进度条,0是不出现，1是出现
	function sychTask(taskMoniId,taskId,nodeId,orgId,orgName,taskName,th){
		//alert(taskId);
		//_toUrl(taskMoniId,taskId,nodeId,orgId,orgName,taskName,th) ;
		isOpenBar = 1;
		var a = event.srcElement;
		var checx = a.parentNode.parentNode.getElementsByTagName('input')[0];
		var result = false;
		var key = taskMoniId+'_' + nodeId + '_' + orgId;
		$.post("<%=request.getContextPath() %>/taskSynchronizeAction!taskSynchronize.action?","taskId="+key+"&userId=<%=userName%>" ,function(date){
		    if(date != "0" ){
				alert("该任务被"+date+"占用请稍后操作！");
				getId("ft").style.display='none';
				checx.disabled=true;
		    }
		    else{
			    try{
				    	_toUrl(taskMoniId,taskId,nodeId,orgId,orgName,taskName,th) ;
						checx.disabled=false;
						progressBar.show();
				    }catch(e){
					    	progressBar.close();
					    }
				
		    }
		  });
	}
				
	function vemoveUserTaskMap(){
		//alert("离开页面！");
		//$.post("<%=request.getContextPath() %>/taskSynchronizeAction!moveMapDate.action?", ,function(date){
		//var url = "<%=request.getContextPath() %>/taskSynchronizeAction!moveMapDate.action?";
		//$.post(url,"userId=<%=userName%>" ,function(date){
			//alert(date);
		  //  if(date == "1" ){
			//	alert("任务被释放！");
		  //  }else{
		   // 	alert("释放失败！");
		   // }
		  //});
		
		$.ajax({   
	          type : "post",   
	          url : "<%=request.getContextPath() %>/taskSynchronizeAction!moveMapDate.action?",   
	          async : true,   
	          success : function(data){ 
	        	  
	          	alert(date);
	        	 if(date == "1" ){
	  				alert("任务被释放！");
	  		     }else{
	  		    	alert("释放失败！");
	  		    }
	          }   
        });
	}
	
	function _toUrl(taskMoniId,taskId,nodeId,orgId,orgName,taskName,th){
		document.getElementById("wMoni.id.taskMoniId").value=taskMoniId;
		document.getElementById("wMoni.id.nodeId").value=nodeId;
		document.getElementById("wMoni.id.orgId").value=orgId;
		var url = "<%=request.getContextPath()%>/pendingTaskAction!handlerReport.action";
		$.post(
				url,
				{
				"wMoni.id.taskMoniId":taskMoniId,
				"wMoni.id.nodeId":nodeId,
				"wMoni.id.orgId":orgId,
				"taskId":taskId
					},
				function(data){
					if(data!=undefined && data!=""){
						if(data.indexOf("#")>-1){
							var param = data.split("#");
							//alert(param[0]);
							//0:报表id,1:业务条线,2:期数,3:参数url,4:form表单名称,5:机构id,6:任务频度,7:任务状态,8:节点id
							if(param!=undefined){
								var repForm = getId("repForm");
							//	alert(param[0]+"-"+param[1]+"-"+
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
								
								getId("templateIds").value=param[0];
								getId("workTaskBusiLine").value=param[1];
								getId("workTaskFreq").value=param[6];
	
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
						}
					}else{
						alert("数据异常!");
						progressBar.close();
					}
				}
			);
	}

	function move(th){
		if(getId("fs").style.display=='none'){
			
			getId("d2").style.visibility = "visible";
		}else{
			getId("d1").style.visibility = "visible";
		}
	}

	function out(th){
		if(getId("d2").style.visibility=='visible'){
			getId('d2').style.visibility = 'hidden';
		}else{
			getId('d1').style.visibility = 'hidden';
		}
	}

	function go_backTaskDetail(taskMoniId,nodeId,orgId){
		var url = "<%=request.getContextPath() %>/pendingTaskAction!findBackTaskDetail.action?wMoni.id.taskMoniId="+taskMoniId+"&wMoni.id.orgId="+orgId+"&wMoni.id.nodeId="+nodeId;
		window.showModalDialog(url, null,"dialogWidth:624px;dialogHeight:468px;resizable:yes;scroll:yes");
	}
	
	function taskMove(taskId,th,curStateId,dIndex){
		if(taskId!=undefined && parseInt(taskId)>0){
			var x = event.clientX;
			var y = event.clientY;
			var res = true;
			$.post(
					"<%=request.getContextPath()%>/pendingTaskAction!findAllNodeMonis.action",
					{"taskId":taskId},
					function(rv){
						var count =1;
						var content = "";
						if(rv!=undefined && rv.length>0){
							getId("content").innerHTML="";
							content+="<img src='images/search.png'/><span class='taskSpan' style='color:#2194E0;margin-left:2px;'>任务跟踪</span><br><br>";
							for(var i in rv){
								if(rv[i].taskNodeName!=undefined){
									if(res){
										if(rv[i].nodeId==curStateId){
											res=false;
											content +="&nbsp;&nbsp;<span class='taskSpan' style='color:#ebad06;'>["+rv[i].taskNodeName+"]</span>&nbsp;&nbsp;";
										}else{
											content +="&nbsp;&nbsp;<span class='taskSpan' style='color:#CCCCCC;'>["+rv[i].taskNodeName+"]</span>&nbsp;&nbsp;";
										}
									}else{
										content +="&nbsp;&nbsp;<span class='taskSpan' style='color:#2194E0'>["+rv[i].taskNodeName+"]</span>&nbsp;&nbsp;";
									}
									var num = rv.length;
									//alert(i+"---"+(num-1));
									if(i<(num-1)){
										content+="<img src='images/pright.gif'>";
									}			
									if(count%4==0){
										content +="<br><br>";		
									}
									count++;
								}
							}
							getId("taskFlowing").style.top=getObjectTop(getId("d"+dIndex))-scrollTopNum;
							getId("taskFlowing").style.left=(getObjectLeft(getId("d"+dIndex))-350)-scrollLeftNum;
							getId("taskFlowing").style.display='block';

							getId("content").innerHTML=content;
						}
					}
			);
			
		}
	}
	var timeTigger;
	function taskOut(th){
		timeTigger=setTimeout("getId('taskFlowing').style.display='none'",100);
	}
	function search(){
		var form = document.getElementById("shForm");
		form.submit();
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
	
	function taskFlowingMove(th){
		clearTimeout(timeTigger);
		th.style.display="block";
	}
	function taskFlowingOut(th){
		th.style.display="none";
	}
	
	function initDate1(){
			var  txtLeft = getObjectLeft(document.getElementById("datepicker_2"));
			
			var txtTop = getObjectTop(document.getElementById("datepicker_2"));
			
			var date1 = document.getElementById("date1");
			
			date1.style.top = txtTop+1;
			date1.style.left = txtLeft+73;
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
		
		function _splitTask(){
			var taskName = document.getElementById("splittaskName").value;
			if(taskName==""){
				alert("请填写任务名称!");
				return;
			}
			var count = window.frames.rptFrame.document.getElementById("countChk").value;
			var templateIds = "";
			
			for(var i=1;i<=count;i++){
				var ck = window.frames.rptFrame.document.getElementById("chk"+i);
				if(ck.checked){
						if(templateIds!="")
							templateIds+=",";
						try{
							var vals = window.frames.rptFrame.document.getElementById("sync"+i);
							var vl = vals.value;
							if(vl.indexOf("_")>-1)
								templateIds+=vl.substr(0,vl.indexOf("_"));	
						}catch(e){
							if(ck.value!="")
								templateIds+=ck.value;
						}
				}
			}
			if(templateIds!=""){
				document.getElementById("taskName").value=taskName;
				document.getElementById("taskTemplateIds").value= templateIds;
				document.getElementById("shForm").action = "<%=request.getContextPath()%>/pendingTaskAction!splitTask.action";
				document.getElementById("shForm").submit();
			}
			closeAction();
		}	
		
		function showdiv(){
			try{
				var count = window.frames.rptFrame.document.getElementById("countChk").value;
				if(count==undefined || count==""){
					alert("请选择需要拆分的报表!");
					return;
				}
				var res = 0;
				for(var i=1;i<=count;i++){
					var ck = window.frames.rptFrame.document.getElementById("chk"+i);
					if(ck.checked){
						res++;
					}
				}
				if(res==0){
					alert("请选择需要拆分的报表!");
					return;
				}
				layerAction('add');
			}catch(e){
				alert("请选择需要拆分的报表!");
				return;
			}
		}
		function setSelectTitle(){
			var supplierSelect = document.getElementById('taskId'); 

			for(var i=0; i<supplierSelect.options.length; i++){ 
			     supplierSelect.options[i].setAttribute('title',supplierSelect.options[i].text); 
			} 



		}
		function _isOnloadValidate(){
			try{
				if(isOpenBar==1){
					progressBar.close();
					isOpenBar=0;
				}
			}catch(e){
				isOpenBar=0;
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
		width:502px;
		height:140px;
		display:none;
		border:1 solid #CCCCCC;
		position:absolute;
		background-color:#FFFFFF;
		z-index:1;
		white-space:nowrap;
		padding:3px;
	}
	#taskFlowing div{
		width:500px;
		padding:5px;
		height:138px;
		margin:0px;
		text-align:left;
		overflow:auto;
		border:1 solid #CCCCCC;
	}
	.taskSpan{
		font-family:Arial;font-size:12px;font-weight:bold;font-style:normal;text-decoration:none;
	}
	.selectClass{
		vertical-align: middle;overflow:auto;
	}
</style>
</head>

<body onload="init('<s:property value="pendingTaskQueryConditions.taskTerm"/>'),setSelectTitle()"  onunload="vemoveUserTaskMap()" style="overflow:scroll;">

<div style="margin-top:5px;">
<table width="100%" height="20px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
		<div class="tableinfo" ><span style="font-size:12px;"><img src="<%=request.getContextPath() %>/images/icon01.gif" alt="列表" /></span><b style="font-size:12px;">当前位置 >>报表处理 >>待办任务</b></div>
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
<fieldset style="background-color:white;padding:0px;margin:0px;height:250px;" id="fs">
<legend><b><font color="#075587">任务查询栏</font></b></legend>
<div id="jigouqu"  region="east" iconCls="icon-reload" title="机构" split="true" style="left:316px;top:70px;display:none;width: 150px;height: 150px ;background-color:#f5f5f5;border :1px solid Silver;overflow:auto; position:absolute; z-index:2;" >
	<ul  class="easyui-tree" style id="tt2" style="width: 200;background-color:#EFEFEF"></ul>
</div>
<form action="<%=request.getContextPath() %>/pendingTaskAction.action" id="shForm" method="post">
<div style="width:99%;margin-bottom:7px;" align="center">
<table border="0" width="100%" align="center" cellpadding="0" cellspacing="0" >
		<tr>
			<td width="21%"  align="left" >
			    <input type="hidden" name="taskName" />
				<input type="hidden" name="tasktemplateIds" id="taskTemplateIds"/>
				<input type="hidden" name="wMoni.id.taskMoniId"/>
				<input type="hidden" name="wMoni.id.nodeId"/>
				<input type="hidden" name="wMoni.id.orgId"/>
			   任务<s:select cssStyle="width:80%;" list="workTaskInfos" id="taskId" onchange="search()" headerKey="-1" headerValue="------全部------" name="pendingTaskQueryConditions.taskId" listKey="taskId" listValue="taskName" cssClass="selectClass"></s:select>
			   	</td>
            <td width="10%" align="left">
            	频度 <select name="pendingTaskQueryConditions.freqId" id="freqId" onchange="search()" style="width: 80px;vertical-align: middle">
				  <option value="-1" selected="selected">---全部---</option>
	              	<option <s:if test="pendingTaskQueryConditions.freqId=='day'">selected="selected"</s:if>  value="day">日频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='month'">selected="selected"</s:if> value="month">月频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='season'">selected="selected"</s:if> value="season">季频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='halfyear'">selected="selected"</s:if> value="halfyear">半年频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId=='year'">selected="selected"</s:if> value="year">年频度</option>
					<option <s:if test="pendingTaskQueryConditions.freqId =='yearbegincarry'">selected="selected"</s:if> value="yearbegincarry">年初结转</option>
			    </select>
            </td>
            <td width="12%" align="left">
            	操作 <select name="pendingTaskQueryConditions.condTypeId" id="condTypeId" onchange="search()" style="width: 80px;vertical-align: middle">
				  <option value="-1" selected="selected">---全部---</option>
	              	<option <s:if test="pendingTaskQueryConditions.condTypeId=='fill'">selected="selected"</s:if>  value="fill">填报</option>
					<option <s:if test="pendingTaskQueryConditions.condTypeId=='check'">selected="selected"</s:if> value="check">审核</option>
			    </select>
            </td>
            <td width="15%" align="left" valign="middle">
            		<input type="hidden" id="orgId" name="pendingTaskQueryConditions.orgId" value="<s:property value="pendingTaskQueryConditions.orgId"/>"/>
            		机构  <input type="text" id="orgName" readonly="readonly" onclick="mousefocus()"  style="width:135px;cursor:hand" size="25" name="pendingTaskQueryConditions.orgName" value="<s:property value="pendingTaskQueryConditions.orgName"/>" />
            </td>
            <td width="14%" align="left" valign="middle">
            		机构名称  <input type="text" id="likeOrgName"  style="width:100px;" size="25" name="pendingTaskQueryConditions.likeOrgName" value="<s:property value="pendingTaskQueryConditions.likeOrgName"/>" />
            </td>
            <td width="12%" align="left" valign="middle">
            	期数  <input id="datepicker_2"  readonly="readonly" onmouseout="hiddenDate1()" onmousemove="showDate1()" style="cursor: hand;width:90px;height: 18px;vertical-align: middle" value="<s:property value="pendingTaskQueryConditions.taskTerm"/>" name="pendingTaskQueryConditions.taskTerm" onchange="search(),checkStartAndEndTime(this)"/> 
            	<span onmousemove="cleartimeout()" onmouseout="this.style.display='none'" onclick="document.getElementById('datepicker_2').value='';this.style.display='none';change();" style="border:0 solid red;width:15px;display:none;height:15px;cursor:hand;position:absolute;" id="date1" ><img src="<%=request.getContextPath()%>/images/clear.bmp"/></span>
            	</td>
            <td width="12%" align="left" valign="middle">
            	状态
            	<select name="pendingTaskQueryConditions.nodeFlag" id="nodeFlag" onchange="search()" style="width: 90px;vertical-align: middle">
				 <option value="-1" <s:if test="pendingTaskQueryConditions.nodeFlag==-1">selected="selected"</s:if>>---全部---</option>
				  <option value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WADC"/>" <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WADC">selected="selected"</s:if>>待处理</option>
	              	<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT">selected="selected"</s:if>  value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WAIT"/>">等待</option>
					<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_COMM">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_COMM"/>">已提交</option>
					<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_PASS">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_PASS"/>">审核通过</option>
					<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU"/>">审核未通过</option>
					<option <s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA">selected="selected"</s:if> value="<s:property value="@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REWA"/>">退回等待</option>
			    </select>
            </td>
            <td width="8%" align="left">
            	<input type="submit" name="button" id="button" value="查询"  class="searchbtn" />&nbsp;&nbsp;
            </td>
		</tr>
 	</table>

</div>

<div id="main" style="width:100%;height:180px;overflow:auto;">

<table id="tble" width="100%" border="0" cellspacing="0" cellpadding="0"  class="ntable" align="center">
	<tr >
   		<td colspan="12" bgcolor="#FFF0D0" >
			<input type="hidden"  name="pendingTaskQueryConditions.busiLine" value="<s:property value="pendingTaskQueryConditions.busiLine"/>"/>
   		<b>任务处理列表</b>
   		</td>
  </tr>
  
  <tr >
   		<td colspan="12" align="left" style="height:35px;">
   		<input type="button" onclick="commitTask()" class="searchbtn" value="提交" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   		<input type="button" onclick="_backWorkTask()" class="searchbtn" value="退回" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   		<s:if test="@com.fitech.model.worktask.common.WorkTaskConfig@MANUAL_SPLIT_TASK_FLAG==1">
   			<s:if test="pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WADC ||
	   		pendingTaskQueryConditions.nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
	   			<input type="button" onclick="showdiv()" class="searchbtn" value="拆分任务" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   		</s:if>
   		</s:if>
   		</td>
  </tr>
  <tr>
    <td width="3%" bgcolor="#FFF0D0" align="center"><input type="checkbox" id="checkAll"/></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>机构名称</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>任务名称</strong></td>
    <td width="6%" bgcolor="#FFF0D0" align="center"><strong>操作类型</strong></td>
    <td width="6%" bgcolor="#FFF0D0" align="center"><strong>任务频度</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>期数</strong></td>
    <td width="8%" bgcolor="#FFF0D0" align="center"><strong>报送截止日期</strong></td>
    <td width="8%" bgcolor="#FFF0D0" align="center"><strong>上一节点日期</strong></td>
    <td width="12%" bgcolor="#FFF0D0" align="center"><strong>报送阶段</strong></td>
    <td width="10%" bgcolor="#FFF0D0" align="center"><strong>任务导航</strong></td>
    <td width="7%" bgcolor="#FFF0D0" align="center"><strong>状态</strong></td>
    <td width="7%" bgcolor="#FFF0D0" align="center"><strong>报送状态</strong></td>
    
 	</tr>
  <s:if test="pageResults!=null && pageResults.results.size()!=0">
  		<s:iterator value="pageResults.results" status="i">
  			<tr id="th<s:property value='#i.index'/>">
			  	<td align="center" id="con<s:property value="#i.index"/>">
			  		<s:if test="nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_WADC ||
			  	nodeFlag==@com.fitech.model.worktask.common.WorkTaskConfig@NODE_FLAG_REFU">
			  		<s:if test="taskMoniIds!=null && taskMoniIds==taskMoniId 
			  	&& nodeIds!=null && nodeIds==nodeId && orgIds!=null && orgIds==orgId && false">
			  		<script type="text/javascript">
			  				var url = "<%=request.getContextPath() %>/pendingTaskAction!searchPowType.action";
							$.post(
									url,
									{
										"wMoni.id.orgId":'<s:property value="orgId"/>',
										"wMoni.id.nodeId":<s:property value="nodeId"/>
										},
										function(rv){
											if(parseInt(rv)==1){
												$("#con"+<s:property value="#i.index"/>).html("<input type='checkbox' checked name='cks' value='<s:property value="taskMoniId"/>,<s:property value="nodeId"/>,<s:property value="orgId"/>,<s:property value="busiLine"/>,<s:property value="lateRepDate"/>'/>");
											}else{
												$("#con"+<s:property value="#i.index"/>).html("----");
												}
										}
									);		  		
			  		</script>
			  		<script type="text/javascript">
			  		function _toUrl(){
			  			var url = "<%=request.getContextPath() %>/pendingTaskAction!handlerReport.action";
			  			$.post(
			  					url,
			  					{
			  					"wMoni.id.taskMoniId":<s:property value="taskMoniId"/>,
			  					"wMoni.id.nodeId":<s:property value="nodeId"/>,
			  					"wMoni.id.orgId":'<s:property value="orgId"/>'
			  						},
			  					function(data){
			  						if(data!=undefined && data!=""){
			  							if(data.indexOf("#")>-1){
			  							
			  								var param = data.split("#");
			  								//alert(param[0]);
			  								//0:报表id,1:业务条线,2:期数,3:参数url,4:form表单名称,5:机构id,6:任务频度,7:任务状态,8:节点id
			  								if(param!=undefined){
			  									var repForm = getId("repForm");
			  								//	alert(param[0]+"-"+param[1]+"-"+
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
			  									
			  									getId("templateIds").value=param[0];

			  									getId("nodeFlag").value=param[7];
			  									repForm.submit();
			  									
			  									getId("ft").style.display='block';

			  									getId("f1").innerText= '<s:property value="orgName"/>';
			  									getId("f2").innerText= '<s:property value="taskName"/>';
			  								}
			  							}
			  						}
			  					}
			  				);
			  			
			  		}
			  		_toUrl();
			  		</script>
			  	</s:if>
			  	<s:else>
			  		<script type="text/javascript">
			  				var url = "<%=request.getContextPath() %>/pendingTaskAction!searchPowType.action";
							$.post(
									url,
									{
										"wMoni.id.orgId":'<s:property value="orgId"/>',
										"wMoni.id.nodeId":<s:property value="nodeId"/>
										},
										function(rv){
											if(parseInt(rv)==1){
												$("#con"+<s:property value="#i.index"/>).html("<input type='checkbox' name='cks' value='<s:property value="taskMoniId"/>,<s:property value="nodeId"/>,<s:property value="orgId"/>,<s:property value="busiLine"/>,<s:property value="lateRepDate"/>'/>");
											}else{
												$("#con"+<s:property value="#i.index"/>).html("----");
												}
										}
									);		  		
			  		</script>
			  	</s:else>
			  	</s:if>
			  	<s:else>----</s:else>
			  	</td>
			  	<td align="center"><s:property value="orgName"/></td>
			  	<td align="center">
				 <a href="javascript:;" onclick="sychTask(<s:property value="taskMoniId"/>,<s:property value="taskId"/>,<s:property value="nodeId"/>,'<s:property value="orgId"/>','<s:property value="orgName"/>','<s:property value="taskName"/>','th<s:property value='#i.index'/>')" target="rptFrame" style="color:blue;text-decoration:underline" onmousemove="this.style.color='red';" onmouseout="this.style.color='blue'"><s:property value="taskName"/></a>
			  	</td>
			  	<td align="center"><font color="red"><s:property value="condTypeId"/></font></td>
			  	<td align="center"><s:property value="freqId"/></td>
			  	<td align="center"><s:date name="taskTerm" format="yyyy-MM-dd"/></td>
			  	<td align="center">
				  	<s:if test="'day'==@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT">
				  		<s:date name="lateRepDate" format="yyyy-MM-dd"/>
				  	</s:if>
				  	<s:elseif test="'hour'==@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT">
				  		<s:date name="lateRepDate" format="yyyy-MM-dd HH:mm:ss"/>
				  	</s:elseif>
			  	</td>
			  	<td align="center">
				  	<s:if test="'day'==@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT">
				  		<s:if test="lastNodeDate!=null">
				  			<s:date name="lastNodeDate" format="yyyy-MM-dd"/>
				  		</s:if>
				  		<s:else>
				  			------------
				  		</s:else>
				  	</s:if>
				  	<s:elseif test="'hour'==@com.fitech.model.worktask.common.WorkTaskConfig@REPORT_TIME_UNIT">
				  		<s:if test="lastNodeDate!=null">
				  			<s:date name="lastNodeDate" format="yyyy-MM-dd HH:mm:ss"/>
				  		</s:if>
				  		<s:else>
				  			------------
				  		</s:else>
				  	</s:elseif>
			  	</td>
			  	<td align="center" ><s:property value="taskNodeName"/></td>
			  	<td align="center" >
			  	<font id="ft1" style="text-decoration:underline;" onmouseover="taskMove(<s:property value="taskId"/>,this,<s:property value="curStateId"/>,<s:property value="#i.index"/>)" onmouseout="taskOut(this)"><s:property value="curState"/></font>
			 	<div id="d<s:property value="#i.index"/>" style="position:absolute;border:0 solid red;width:10px;margin-top:18px;"></div>
			  	 </td> 
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
			<input type="hidden" name='hid<s:property value="#i.index"/>' id='hid<s:property value="#i.index"/>' value='<s:property value="taskMoniId"/>,<s:property value="nodeId"/>,<s:property value="orgId"/>'/>
  		</s:iterator>
  </s:if>
 
  <s:else>
  	 <tr >
   		<td colspan="11">无任务处理</td>
 	 </tr>
  </s:else>
</table>
</div>
</form>
		<div style="border:0 solid #CCCCCC;">
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
			</table>
		</div>
　</fieldset>

<!-- 弹出层 -->
<div id="cBg" style="display:none;"></div>
<!-- 关闭层 -->
<div class="commonLayer quit" style="background-image:url('<%=request.getContextPath() %>/images/quitbg.png');background-repeat: repeat;" id="add">
	<form action="" method="post" id="for">
    	<div class="ttitle" align="left">拆分任务</div>
        <div class="qinfo">
        <p>任务名称:
        	<input type="text" name="splittaskName"/><br/>
        	<font color="red">(!请填写拆分任务后的任务名称)</font>
        </p>
        </div>
        <div class="qbtn1" style="margin-top:60px;">
        <input name="fileServerInfoVo.serverId" type="hidden" />
        <input name="" type="button"  style="background-image: url('<%=request.getContextPath() %>/images/qbtnbg.gif');background-repeat: repeat;" value="保存" onclick="_splitTask();" class="tbtn" /> &nbsp;&nbsp;&nbsp;
        <input name="" type="button" style="background-image: url('<%=request.getContextPath() %>/images/qbtnbg.gif');background-repeat: repeat;"  value="取消" onclick="closeAction()" class="tbtn"/></div>
   </form>
</div>

<div id="taskFlowing" onmousemove="taskFlowingMove(this)" onmouseout="taskFlowingOut(this)">
	<div id="content"></div>
</div>
<div id="d1" ><img id="taskImg" src="<%=request.getContextPath() %>/images/task_up.gif"  onclick="changeImg()" style="cursor:hand;"/></div>	
	<table style="margin-top:3px;overflow:auto;" width="100%" height="50%" id="tl" border="0" cellspacing="0" cellpadding="0"  class="ntable" align="center">
	<tr>
   		<td colspan="7" height="5%" bgcolor="#FFF0D0"><b><font id="f1">任务处理列表</font>>><font id="f2">报表处理列表</font></b></td>
  	</tr>
  	<tr >
  		<td colspan="7" id="ft" style="display:none;">
  			<iframe id="ifm" width="100%" height="70%" name="rptFrame"  onload="_isOnloadValidate()" src="" style="overflow:auto;"></iframe>
  		</td>
  	</tr>
</table>
</body>
</html>