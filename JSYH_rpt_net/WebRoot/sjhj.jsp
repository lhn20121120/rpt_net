<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.cbrc.smis.security.Operator"%>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report"%>
<%@ page session="true" import="java.lang.StringBuffer,java.util.Map"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	String appPath = request.getContextPath();
	/** 报表选中标志 **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	Map reportMap = (Map)request.getAttribute("reportParam");
	
	StringBuffer param = new StringBuffer("");
	String filename = (String)reportMap.get("filename");
	String reqquestUrl = "/editAFReport.do?"+(String)reportMap.get("requestUrl")+"&saveFlg=1";
	String repId = (String)reportMap.get("repId");
	//获取参数和宏
	param.append("RangeID").append("=").append(reportMap.get("RangeID")).append(";");
	param.append("Freq").append("=").append(reportMap.get("Freq")).append(";");
	param.append("CCY").append("=").append(reportMap.get("CCY")).append(";");
	param.append("ReptDate").append("=").append(reportMap.get("ReptDate")).append(";");
	param.append("OrgID").append("=").append(reportMap.get("OrgID")).append(";");
	
	String backQry = "";
	if (session.getAttribute("backQry") != null){
		backQry = (String) session.getAttribute("backQry");
	}
		
	String isdata = (String)reportMap.get("isdata");
	String templateId = (String)reportMap.get("templateId");
	String versionId = (String)reportMap.get("versionId");
	String repname = (String)reportMap.get("repname");
	String repInId = (String)reportMap.get("repId");
	
	Operator operator = null; 
	if(request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
		operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/script/jquery.easyui.min.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>

	
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	<link href="css/common.css" type="text/css" rel="stylesheet">
	-->
	
	<style type="text/css">
		td{
			font-family:宋体,Arial;
			font-size: 9pt;
			color: #003366;
			text-align:center;
		}
		td,input,th,select,textarea {
			font-size: 12px;
		}
		body {
			scrollbar-face-color: #DEE3E7;
			scrollbar-highlight-color: #FFFFFF;
			scrollbar-shadow-color: #DEE3E7;
			scrollbar-3dlight-color: #D1D7DC;
			scrollbar-arrow-color: #006699;
			scrollbar-track-color: #EFEFEF;
			scrollbar-darkshadow-color: #98AAB1;
			text-align : center;
			margin : 0;
			font-family:宋体,Arial;
			background-color:#F6F8F7;
			font-size: 9pt;
		}
		
		#innerFinalDIV td,#findLastDIV td{
			border:1px dotted #c4c4c4;
			border-top:0px;
			padding:4px 0px 4px 0px;
			border-right:0px;
		}
		#innerFinalDIV th,#findLastDIV th{
			background-color:#F8F8F8;
			font-size:12px;
			font-family:宋体,Arial;
			font-weight:normal;
			border-left:1px dotted #c4c4c4;
			border-bottom:1px dotted #c4c4c4;
			padding:4px 4px 4px 4px;
		}	
		#changeTr td{
			padding:0px 0px 0px 0px;
		}
	</style>
	<script type="text/javascript">
		var scriptReportFlg=<%=reportFlg%>;
		var dataArray = new Array();//保存修改数据
		var index = 0;
		var nextIndex = 0;
		var oriNum = 0;//原始值
		var changeNum = 0;//调整值
		var finalNum = 0;//调整结果
		var oriId = "";//某个单元格的ID
		var desc = "";//备注
		var isHave = false;
		var top = 0;//控件显示的top
		var left = 0;//控件显示的left
		var obj = "";//单元格对象
		var beizhu="<table style='width:100%' id='henjiTable' align='center' cellpadding='0' cellspacing='0'>";
		
		window.onload = function(){
			var temp=new Array(); 
			var row=document.getElementById("report1").rows.length; //请注意此处的写法 
			for(var i=0;i<row;i++) 
			{ 
				temp[i]=new Array(); 
				var currRow=document.getElementById("report1").rows[i]; 
				for(var col=0;col<currRow.cells.length;col++) 
				{ 
					var currCell=currRow.cells[col]; 
					//currCell.onclick = ""; 
					if(currCell.onclick.toString().indexOf("_displayEditor")>0){
						currCell.onclick=noFunction;//此处屏蔽润乾自定义的修改单元格信息的js方法
					}
				} 
			}
		}

		function noFunction(){
		}

		//删除信息
		function removeTrace(objs,changeData,status){
			var cellName = oriId.substring(oriId.indexOf("_")+1);
			//alert(cellName);
			var url = "updateDataTraceAction.do?traceId="+objs+"&repInId="+<%=repInId%>+"&cellName="+cellName+"&status="+status;
			$.post(url,function(){
				//var oriData = obj.val();
				//var finalData = parseFloat(oriData)-parseFloat(changeData);
				//alert(finalData);
				//obj.text(finalData);
				//obj.attr("value",finalData);
				$("#finalDIV").dialog('close');//关闭痕迹窗体
				showHenji();
			});
			
		}

				
		
		function showHenji(){

			desc="";
			var cellName = oriId.substring(oriId.indexOf("_")+1);
			//获得当前时间 防止ajax缓存
			var curDate = "<%=System.currentTimeMillis()%>";
	
			var beizhu="<table style='width:100%;' align='center' align='center' cellpadding='0' cellspacing='0'><tr>";

			//查询出单元格的最初值
			var searchOriData = "findOriDataAFDataTrace.do?repInId=<%=repInId%>&&cellName="+cellName+"&&curDate="+curDate;
			var orIData = "";
			var isOk = false;
			$.post(searchOriData,function(oriData){
				orIData = oriData;
				if(orIData=="")
					orIData = obj.text();
				beizhu +="<tr><th style='width:50px'>修改人</th><th style='width:200px'>修改时间</th><th style='width:100px'>原始值</th><th style='width:50px'>调整值</th><th style='width:50px'>调整结果</th><th style='width:150px;border-right:0px'>备注</th><th style='border-left:0px'><span style='visibility: hidden'>&nbsp;<img src='<%=request.getContextPath() %>/image/check_right.gif'  /></span></th></tr>";
				<%if(request.getAttribute("type")==null || request.getAttribute("type").equals("marge")){%>
				var hava = false;
				
				for(i=0;i<dataArray.length;i++){
					if(dataArray[i]==oriId){//判断是否被修改过
						//for(j=dataArray[oriId].length-1;j>=0;j--){
							var henji = dataArray[oriId][0];//获得修改的痕迹对象
							//拼接数据
							beizhu +="<tr id='changeTr'><td>"+henji.userName+"</td><td>"
									  +henji.changeTime+"</td><td><span id='firstTD'>"
									  +henji.oriData+"</span></td><td><input  id='changeInput' style='width:100%' value='"
									  +henji.changeData+"'/></td><td><input id='finalInput' style='width:100%' value='"
									  +henji.finalData+"'/></td><td><input type='text' id='descInput' style='width:100%' value='"
									  +henji.desc+"'/></td><td><span id='changeSpan' style='visibility: hidden'>&nbsp;<img src='<%=request.getContextPath() %>/image/check_right.gif' alt='保存修改' style='cursor:hand' onclick='saveData()'/></span></td></tr>";
							
						//}
						hava = true;//已经被修改
						oriNum = henji.oriData;//被修改 则记录上次修改的值
					}
				}
				if(!hava){
					//没有被修改
					beizhu += "<tr id='changeTr' onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\" ><td>admin</td><td>当前时间</td><td><span id='firstTD'>"
								+obj.val()+"</span></td><td><input  id='changeInput' style='width:100%'/></td><td><input id='finalInput' style='width:100%'/></td><td><input type='text' id='descInput' style='width:100%' /></td><td><span id='changeSpan' style='visibility: hidden'>&nbsp;<img src='<%=request.getContextPath() %>/image/check_right.gif' alt='保存修改' style='cursor:hand' onclick='saveData()'/></span></td></tr>";
					oriNum = obj.text();//原始值
				}
				<%}%>
				isOk = true;
				var url = "findAFDataTrace.do?repInId=<%=repInId%>&&cellName="+cellName+"&&curDate="+curDate;
				$.post(url,function(data){
					beizhu +=data;
					beizhu +="</table>";

					if(isOk){
						//打开痕迹层
						$("#innerFinalDIV").html(beizhu);
						$('#finalDIV').dialog({
							top:top,
							left:left,
							autoOpen:true,
							title:"数据痕迹("+orIData+")"
						});
					}

					<%if(request.getAttribute("type")==null || request.getAttribute("type").equals("marge")){%>
					//是否是数字的正则
					var isNum = /^(\d*|\d+\.\d*)$/;
					//屏蔽非数字字符
					$("#changeInput").bind("keydown",function(){
						if(!(event.keyCode==190 || event.keyCode==110 
								|| (event.keyCode>=96 && event.keyCode<=105 )|| (event.keyCode>=48 && event.keyCode<=57) || event.keyCode==8)){
							event.keyCode=0;
							event.cancelBubble = true;
							return false;
						}
					});
					//调整值输入绑定事件改变调整结果
					$("#changeInput").bind("keyup",function(){
						if(!isNum.test($(this).val())){//是否是数字格式
							//
							$(this).val($(this).val().substring(0,$(this).val().length-1));
							return;
						}
						if($(this).val()==""){
							$("#finalInput").val("");
							$("#changeSpan").css("visibility","hidden");//保存修改按钮隐藏
						}
						else{
							var firstData = $("#firstTD").text();
							if(firstData=="")
								firstData=0;
							//最终值
							$("#finalInput").val(parseFloat(firstData)+parseFloat($(this).val()));

							changeNum = $(this).val();//调整值
							finalNum = $("#finalInput").val();//最终值
							

							$("#changeSpan").css("visibility","visible");//保存修改按钮显现
							
						}
					});

					//调整值事件绑定
					$("#finalInput").bind("keydown",function(){
						//屏蔽非数字按键
						if(!(event.keyCode==190 || event.keyCode==110 
								|| (event.keyCode>=96 && event.keyCode<=105 )|| (event.keyCode>=48 && event.keyCode<=57) || event.keyCode==8)){
							event.keyCode=0;//取消按键
							event.cancelBubble = true;//取消事件冒泡
							return false;
						}
					});
					//调整结果输入绑定事件改变调整值
					$("#finalInput").bind("keyup",function(){
						if(!isNum.test($(this).val())){//是否是数字格式
							$(this).val($(this).val().substring(0,$(this).val().length-1));
							return;
						}
						if($(this).val()==""){
							$("#changeInput").val("");
							$("#changeSpan").css("visibility","visible");//保存修改按钮隐藏
						}
						else{
							//调整值
							var firstData = $("#firstTD").text();
							if(firstData=="")
								firstData=0;
							$("#changeInput").val(parseFloat($(this).val())-parseFloat(firstData));

							changeNum = $(this).val();//调整值
							finalNum = $("#finalInput").val();//最终值

							$("#changeSpan").css("visibility","visible");//保存修改按钮显现
						}
					});
					//备注事件绑定
					$("#descInput").bind("change",function(){
						desc = $(this).val();
					});
					<%}%>
				});
			});

			
			
			
			
			//测试虚拟数据
			//beizhu +="<tr onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\"><td>admin</td><td>2012-07-05</td><td>1.0</td><td>2.0</td><td>3.0</td><td colspan='2'>数据准确</td></tr>";
		//	beizhu +="<tr onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\"><td>shd</td><td>2012-07-05</td><td>3.0</td><td>2.0</td><td>1.0</td><td colspan='2' >数据不对</td></tr>";
		//	beizhu +="<tr onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\"><td>byg</td><td>2012-07-05</td><td>5.0</td><td>2.0</td><td>3.0</td><td colspan='2'	>业务要求</td></tr>";
			
		}

		function saveEdit(ctl){
			
			var pnt=$(ctl).parent();
			oriId = pnt.attr("id");//获取次单元格的ID
			var have = false;
			for(i=0;i<dataArray.length;i++){
				if(dataArray[i]==oriId){//判断是否被修改过
					var henji = dataArray[oriId][0];//获得修改的痕迹对象
					have = true;//已经被修改
					oriNum = henji.oriData;//被修改 则记录上次修改的值
				}
			}
			if(oriNum!=$(ctl).attr("value")){
				
				
				finalNum = $(ctl).attr("value");//最终值
				changeNum = parseFloat(finalNum)-parseFloat(oriNum)==''?0:(parseFloat(finalNum)-parseFloat(oriNum));//调整值
				desc="页面直接修改 无备注";//备注置为0，后台给空
				saveData()
			}
			$(pnt).html($(ctl).attr("value"));//将值放回原单元格
			$(pnt).attr("value",$(ctl).attr("value"));//将当前值保存到orig属性中
			$(ctl).remove();//移除编辑组件，很重要
			
			bindEvent($(pnt));
		}
					

		function bindEvent(o){
			o.unbind("click");
			o.bind("click",function(){
			  var writable=$(this).attr("writable");
			  if(writable=="true"){//start if
				var val=$(this).val();
				oriNum = $(this).val();//原始值
				obj = $(this);
				$(this).html("<input type='text' onblur='saveEdit(this)' value='"+val+"' style='border:1px solid red;height:100%;width:100%' >");//点击单元格时，动态插入一个input输入框，获取当前单元格的值，当输入框失去焦点时保存值
				$(this).children("input").select();
				$(this).unbind("click");//取消当前单元格事件，不然一会点击输入框时还会再触发该事件，会有让你意想不到的崩溃效果
			  }//end if
			});
		}
					
		$(function(){
			locationtb('report1');
			doload();

			<%if(request.getAttribute("type")==null || request.getAttribute("type").equals("marge")){
				//if(false){
			%>
			$("#report1 td").bind("click",function(){
				bindEvent($(this));
			})
			<%}%>
			
			var dialog = null;
			$('#finalDIV').dialog('close'); 
			$('#lastDIV').dialog('close'); 
			
			$("#report1 td").bind('contextmenu',function(e){
				//判断是否是可以改变的单元格
				if($(this).attr("onClick").toString().indexOf("noFunction")>0){
					var offset = $(this).offset();//获得控件当前位置
					top = offset.top+$(this).height();
					left = offset.left;
					$('#mm').menu('show', {
						left: left,
						top: top
					});
					obj = $(this);//获得当前对象
					oriId = obj.attr("id");//获取次单元格的ID
					return false;
				}
			});
			
			$("#tabsDiv").tabs({
				onSelect:function(title){
					if(title=='原始结果'){
						$('#finalDIV').dialog('close'); 
						locationtb('report2');
					}
				}
			});
			
		});

		//保存修改痕迹数据
		function saveData(){
			//保存数据
			if(desc==""){
				desc="无";	
			}
			var henji = new henJi();
			henji.userName = "<%=operator.getUserName()%>";//修改人
			var time = new Date();//修改时间
			henji.changeTime = time.getYear()+"-"+(time.getMonth()+1)+"-"+time.getDate();
			henji.oriData = oriNum;//原始值
			henji.changeData = changeNum;//调整值
			henji.finalData = finalNum;//调整结果
			henji.desc = desc;//备注
			henji.cellName = oriId.substring(oriId.indexOf("_")+1);
			//alert(henji.cellName);
			if(dataArray.length>0){
				for(i=0;i<dataArray.length;i++){
					if(dataArray[i]==oriId){
						isHave = true;
						dataArray[oriId][0] = henji;
					}
				}
			}
			if(!isHave){
				index++;
				dataArray[index] = oriId;
				dataArray[oriId] = new Array();	
				dataArray[oriId][0] = henji;
			}
			obj.text(finalNum);//修改单元格值
			obj.attr("value",finalNum);
			report1_autoCalc(oriId.substring(oriId.indexOf("_")+1));
			$("#finalDIV").dialog('close');//关闭痕迹窗体
		}
		
		//屏蔽润乾生成的上报按钮
		function doload(){
			 var os = document.getElementsByTagName("span");
			 var o = null;
			 var i=0;
			 for(i=0;i<os.length;i++){
			    if(os[i].innerHTML.toLowerCase().indexOf("submit")!=-1 || os[i].innerHTML.indexOf("提交")!=-1){
			      os[i].style.visibility="hidden";
			      os[i].style.position="absolute";
			 	}
			}
		}
		//讲生成的表格居中
		function locationtb(objName){
			var tb = document.getElementById(objName);	        
			var top=tb.style.top; // 表格距顶边高度
			var height=tb.clientHeight; 
			var endHeight = top + height;  // 表格底部高度
			var except = document.body.clientHeight-endHeight; // 表格距底部高度
			var except2=except/2;
			if(except>200){
				tb.style.position="absolute";
				tb.style.top=top+except2-50;
			}
			var left=tb.style.left;
			var width=tb.clientWidth;
			var rwidth=left + width;
			except = document.body.clientWidth-rwidth; // 表格距底部高度
			except2=except/2;
			if(except>200){
				tb.style.position="absolute";
				tb.style.left=left+except2-20;
			}
		}

		/**数据痕迹对象*/
		var henJi = function(){
			this.userName;//修改人
			this.changeTime;//修改时间
			this.oriData;//原始值
			this.changeData;//调整值
			this.finalData;//调整结果
			this.desc;//备注
			this.cellName;
		}

		function saveReport(){
			//增加备注信息
			document.getElementById("saveButton").disabled="disabled";
			<%if(Config.ISADDDESC){%>
				addReportInDesc();
			<%}%>
			saveSjhj();
			_submitTable( report1 );
			//writeLog();
		}

		function saveSjhj(){
			var param="";
			var first = true;
			for(i=0;i<dataArray.length;i++){
				if(dataArray[i]!=null && dataArray[i]!="")
				{
					var henji = dataArray[dataArray[i]][0];
				    var ss = first?"?":"&&";
				    first = false;
				    param+=ss+"cellName="+(henji.cellName==""||henji.cellName==null?"''":henji.cellName)
							+"&&originalData="+(henji.oriData==""||henji.oriData==null?"''":henji.oriData)
							+"&&changeData="+(henji.changeData==""||henji.changeData==null?"''":henji.changeData)
							+"&&finalData="+(henji.finalData==""||henji.finalData==null?"''":henji.finalData)
							+(henji.desc==null||henji.desc==""?"":"&&desc="+henji.desc);
				}
			}
			if(!first)
				param+="&&repInId=<%=repInId%>";
			//alert(param);
			var url="<%=request.getContextPath()%>/addAFDataTrace.do"+param;
			url = encodeURI(url);
			url = encodeURI(url);
			//alert(url);
			//var param = "dataList="+dataArray;
			$.post(url)
			//new Ajax.Request(url,{method: 'post',parameters:param,onComplete:"",onFailure: ""});
		}

		function showXiaji(){
			$('#lastDIV').dialog({
				top:top,
				left:left,
				autoOpen:true
			}); 
		}

		function _validate(){
			if(confirm("您确定要进行校验吗?\n")==true){
				try{
					document.getElementById("jiaoyanButton").disabled="disabled";
					if(scriptReportFlg=="1"){
					  	var upReportURL ="<%=request.getContextPath()%>/report/validateOnLineReport.do?repInId=<%=repId%>";
					  	upReportURL += "&&radom="+Math.random();

					    $.post(upReportURL,function(result){
					    	validateReportHandler(result);
						});
				   	}else{
				   		var upReportURL ="<%=request.getContextPath()%>/report/validateOnLineReportNX.do?repInId=<%=repId%>";
				   		upReportURL += "&&radom="+Math.random();
					    //var param = "radom="+Math.random();
					     $.post(upReportURL,function(result){
					    	 validateReportHandler(result);
						});
					  
				   	}
				}catch(e){
					alert('系统忙，请稍后再试...！');
					return;
				}			
			}	
		}

		function validateReportHandler(request)
		{
			try
			{
				var result = request.getElementsByTagName('result')[0].firstChild.data;;
				if(result == 'false')  
				  {
				     if(confirm('校验失败！是否需要查看校验信息?')){
					     if(scriptReportFlg=="1"){
					     window.open("<%=request.getContextPath()%>/report/viewDataJYInfo.do?repInId=<%=repId%>",'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
						
					     }else{
					        window.open("<%=request.getContextPath()%>/report/viewValidateInfo.do?repInId=<%=repId%>",'校验结果','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
						}
					
					}
				  }	
				  else if(result == 'true')
				  {
					 alert('校验通过！');	
				  }
				document.getElementById("jiaoyanButton").disabled=false;
			}
			catch(e)
			{}
	    }

	    //失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
	    }


	    function _view_sjbs(){
			if(confirm('确定报送该报表？')){
		 	try
		 	 {
		 	 	if(scriptReportFlg=="1"){
				  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineReport.do?repInId=<%=repId%>"+"&radom="+Math.random();;
				   // var param = 
				    $.post(upReportURL,function(result){
				    	upReportHandler(result);
					});
				   //	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}else{
			   		var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineNXReport.do?repInId=<%=repId%>"+"&radom="+Math.random();;
				   // var param = 
				    $.post(upReportURL,function(result){
					    upReportHandler(result);
					});    
				  // 	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
			   	}
		   	
		   	}
		   	catch(e)
		   	{
			   	alert(e);
		   		alert('系统忙，请稍后再试...！T');
		   	}
		}
		}

		//报送Handler
		function upReportHandler(request)
		{
			try
			{
				var result =  request.getElementsByTagName('result')[0].firstChild.data;;
				  if(result == 'true')
				  {
					 alert('报送成功！');
					 if(scriptReportFlg=="1"){	
					 location.href="<%=request.getContextPath()%>/viewDataReport.do?<%=backQry%>"; 
					 }else{
						 location.href="<%=request.getContextPath()%>/viewNXDataReport.do?<%=backQry%>"; 
					 }					 
				  }
				  else  if(result == 'BJ_VALIDATE_NOTPASS')
				  {
				     alert("表间校验不通过，不能上报该报表！");
				  }else if( result == 'BN_VALIDATE_NOTPASS'){
				  
				 	 alert("表内校验不通过，不能上报该报表！");
				  }
				  else{
				 	 alert('系统忙，请稍后再试...！');
				  
				  }
			}
			catch(e)
			{}
	    }
	    
	 function _back(){
			if(scriptReportFlg=="1"){
				window.location = "viewDataReport.do?<%=backQry%>";
			}else{
				window.location = "viewNXDataReport.do?<%=backQry%>";		
			}	
		}
	 //避免js脚本遇到单元格表达式中还有 IF 条件判断时不能正常运算，模拟 excel IF
	
	function EXCEL_IF(a,b,c){
	  if(a==true)
	  	return b;
	  else
	    return c;
	}
	//max函数
	function EXCEL_MAX(a,b){
		var r ;
		if(a>=b)
			r = a ;
		else 
			r = b;
		return r ;
	}
	//min函数 
	function EXCEL_MIN(a,b){
		var r ;
		if(a>=b)
			r = b ;
		else 
			r = a;
		return r ;
	}
	//绝对值
	function EXCEL_ABS(r){
		if(r<0){
			r*=-1;
		}
		return(r);
	}
	//round函数
	function EXCEL_ROUND(v,e){
		return v.toFixed(e);
	}
	
	function report1_saveAsExcel1(backQry,repInId){
			<%if(isdata.equals("1") || reportFlg.equals("3")){ %>
				window.location.href='/rpt_net/expExcel.do?repInId='+repInId+"&backQry="+backQry;
			<%}%>
	}
	
	</script>
  </head>
 <body class="easyui-layout">

<div region="center" title="<%=request.getAttribute("type")!=null && request.getAttribute("type").equals("search")?"在线查看":"在线修改" %>" style="overflow:hidden;" >
		<div class="easyui-tabs" fit="true"  border="false" id="tabsDiv" style="overflow: hidden;">
			
			
			<div title="<%=repname%>" style="padding:5px;overflow:auto;background-color: #F4F4F4;text-align: center;"id="lastDataDiv"> 
				<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0  align="center"><tr>
			<%if(request.getAttribute("type")==null || !String.valueOf(request.getAttribute("type")).equals("search")){
				
			
			%>
		<td height="25" width=100% valign="middle"  style="font-size:13px" background="<%=request.getContextPath()%>/image/toolbar-bg.gif">
		
			
		
		<table width="100%">
			<tr align="center">
				<td>
					<input type="button" class="input-button" value=" 保 存 " onclick="saveReport()" id="saveButton">
					&nbsp;&nbsp;&nbsp;
					<%if(isdata.equals("1")){ %>
					<input type="button" class="input-button" value=" 校 验 " onclick="_validate()" id="jiaoyanButton">
					&nbsp;&nbsp;&nbsp;
					<input type="button" class="input-button" value=" 报 送 "onclick="_view_sjbs()">	
					&nbsp;&nbsp;&nbsp;	
					<%} %>					
					<%if(isdata.equals("1")){ %>
						<%
							if(reportFlg.equals("2") || reportFlg.equals("3")){
							%>
							<input type="button" class="input-button" value=" 导出EXCEL" onclick="report1_saveAsExcel();return false;">
							<%	
							}else{
							%>
							<input type="button" class="input-button" value=" 导出EXCEL" onclick="report1_saveAsExcel1('${requestScope.backQry}',${requestScope.repInId})">
							<%								
							}
						%>
						
					<%}else{%>
						<input type="button" disabled class="input-button" value=" 导出EXCEL">
					<% } %>	
					&nbsp;&nbsp;&nbsp;
					<input type="button" class="input-button" value=" 返 回 " onclick="_back()">
					&nbsp;&nbsp;&nbsp;
				</td>			
			</tr>			
			<tr>
			
			</tr>
		</table>
		</td>
		<%}else if(request.getAttribute("type")!=null && String.valueOf(request.getAttribute("type")).equals("search")){
			%>
			<td height="25" width=100% valign="left"  style="font-size:13px" background="<%=request.getContextPath()%>/image/toolbar-bg.gif">
			<table width="100%" border="0" cellpadding="2" cellspacing="1" align="center">
			<tr>
				<td> 
				<input type="button" class="input-button" value=" 关闭窗口 " onclick="javascript:window.close()">
				</td>
			
			</tr>
		</table>
		</td>
		<%
		}
		%>
		</tr></table>
				<report:html name="report1"               
 					reportFileName="<%=filename%>"
					funcBarLocation="top" 
					needPageMark="no"
					generateParamForm="no"
					needLinkStyle="yes"	 
					params="<%=param.toString()%>"
					saveAsName = "<%=templateId%>"
					validOnSubmit="no"
					needScroll="yes"  
					scrollWidth="100%"
					scrollHeight="100%"
					selectText="yes"
					backAndRefresh="yes"  
	  				promptAfterSave="yes"  
					submitTarget="_self" 					
			 		keyRepeatError="yes" 
					excelPageStyle="0"
					inputExceptionPage="/gznx/reportadd/myError2.jsp"  
					saveDataByListener="no" 
					backAndRefresh="<%=reqquestUrl%>"/>	
				<div id="finalDIV" class="easyui-dialog" title="数据痕迹" style="width:600px;height:200px;left:100px;top:150px; "
					 resizable="true"  maximizable="true">
					<div id="innerFinalDIV"></div>
				</div>
				<%
            		Exception e = (Exception)request.getAttribute("exception");
            		if(e!=null){
						out.println( "<h1>信息：</h1><div style='color:red'>" + e.getMessage() + "</div>" );
					}
				%>
				<!-- 
				<div id="lastDIV" class="easyui-dialog" title="下级查看" style="width:550px;height:200px;left:100px;top:150px; "
					 resizable="true"  maximizable="true">
					<div id="findLastDIV">
						<table style='width:100%;' align='center' align='center' cellpadding='0' cellspacing='0' >
							<tr align='center' onmouseover="this.backgroundColor='#EEEEFF'" onmouseout="this.backgroundColor='white'">
								<th style='width:150px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;下级机构</th>
								<th style='width:80px'>本期值</th>
								<th style='width:80px'>上期值</th>
								<th style='width:80px'>上年初</th>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>江苏银行</td>
								<td>632,323,233,32</td>
								<td>532,323,233,32</td>
								<td>432,423,233,32</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>南京分行</td>
								<td>232,323,233,32</td>
								<td>332,323,233,32</td>
								<td>232,423,233,32</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>上海分行</td>
								<td>232,323,233,32</td>
								<td>332,323,233,32</td>
								<td>232,423,233,32</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>无锡分行</td>
								<td>232,323,233,32</td>
								<td>332,323,233,32</td>
								<td>232,423,233,32</td>
							</tr>
						</table>
					</div>
				</div>
				-->
				<div id="mm" class="easyui-menu" style="width:120px;">
					<div iconCls="icon-redo" onclick="javascript:showHenji()">数据痕迹</div>
					<%-- <div iconCls="icon-undo" onclick="javascript:window.open('sjfc/fcsql.jsp')">数据反查</div>--%>
					<%--<div iconCls="icon-undo" onclick="javascript:showXiaji()">下级查看</div>--%>
					<div class="menu-sep"></div>
					<div>Exit</div>
				</div>
   		   </div>		
		
		</div>
		<div id="test" class="easyui-window" closed="true" modal="true" title="精确查询" style="width:400px;height:300px;">
		</div>
</div>
</body>
</html>
