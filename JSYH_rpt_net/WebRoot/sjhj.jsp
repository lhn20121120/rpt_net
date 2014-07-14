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
	/** ����ѡ�б�־ **/
	String reportFlg = "0";
	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	Map reportMap = (Map)request.getAttribute("reportParam");
	
	StringBuffer param = new StringBuffer("");
	String filename = (String)reportMap.get("filename");
	String reqquestUrl = "/editAFReport.do?"+(String)reportMap.get("requestUrl")+"&saveFlg=1";
	String repId = (String)reportMap.get("repId");
	//��ȡ�����ͺ�
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
			font-family:����,Arial;
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
			font-family:����,Arial;
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
			font-family:����,Arial;
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
		var dataArray = new Array();//�����޸�����
		var index = 0;
		var nextIndex = 0;
		var oriNum = 0;//ԭʼֵ
		var changeNum = 0;//����ֵ
		var finalNum = 0;//�������
		var oriId = "";//ĳ����Ԫ���ID
		var desc = "";//��ע
		var isHave = false;
		var top = 0;//�ؼ���ʾ��top
		var left = 0;//�ؼ���ʾ��left
		var obj = "";//��Ԫ�����
		var beizhu="<table style='width:100%' id='henjiTable' align='center' cellpadding='0' cellspacing='0'>";
		
		window.onload = function(){
			var temp=new Array(); 
			var row=document.getElementById("report1").rows.length; //��ע��˴���д�� 
			for(var i=0;i<row;i++) 
			{ 
				temp[i]=new Array(); 
				var currRow=document.getElementById("report1").rows[i]; 
				for(var col=0;col<currRow.cells.length;col++) 
				{ 
					var currCell=currRow.cells[col]; 
					//currCell.onclick = ""; 
					if(currCell.onclick.toString().indexOf("_displayEditor")>0){
						currCell.onclick=noFunction;//�˴�������Ǭ�Զ�����޸ĵ�Ԫ����Ϣ��js����
					}
				} 
			}
		}

		function noFunction(){
		}

		//ɾ����Ϣ
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
				$("#finalDIV").dialog('close');//�رպۼ�����
				showHenji();
			});
			
		}

				
		
		function showHenji(){

			desc="";
			var cellName = oriId.substring(oriId.indexOf("_")+1);
			//��õ�ǰʱ�� ��ֹajax����
			var curDate = "<%=System.currentTimeMillis()%>";
	
			var beizhu="<table style='width:100%;' align='center' align='center' cellpadding='0' cellspacing='0'><tr>";

			//��ѯ����Ԫ������ֵ
			var searchOriData = "findOriDataAFDataTrace.do?repInId=<%=repInId%>&&cellName="+cellName+"&&curDate="+curDate;
			var orIData = "";
			var isOk = false;
			$.post(searchOriData,function(oriData){
				orIData = oriData;
				if(orIData=="")
					orIData = obj.text();
				beizhu +="<tr><th style='width:50px'>�޸���</th><th style='width:200px'>�޸�ʱ��</th><th style='width:100px'>ԭʼֵ</th><th style='width:50px'>����ֵ</th><th style='width:50px'>�������</th><th style='width:150px;border-right:0px'>��ע</th><th style='border-left:0px'><span style='visibility: hidden'>&nbsp;<img src='<%=request.getContextPath() %>/image/check_right.gif'  /></span></th></tr>";
				<%if(request.getAttribute("type")==null || request.getAttribute("type").equals("marge")){%>
				var hava = false;
				
				for(i=0;i<dataArray.length;i++){
					if(dataArray[i]==oriId){//�ж��Ƿ��޸Ĺ�
						//for(j=dataArray[oriId].length-1;j>=0;j--){
							var henji = dataArray[oriId][0];//����޸ĵĺۼ�����
							//ƴ������
							beizhu +="<tr id='changeTr'><td>"+henji.userName+"</td><td>"
									  +henji.changeTime+"</td><td><span id='firstTD'>"
									  +henji.oriData+"</span></td><td><input  id='changeInput' style='width:100%' value='"
									  +henji.changeData+"'/></td><td><input id='finalInput' style='width:100%' value='"
									  +henji.finalData+"'/></td><td><input type='text' id='descInput' style='width:100%' value='"
									  +henji.desc+"'/></td><td><span id='changeSpan' style='visibility: hidden'>&nbsp;<img src='<%=request.getContextPath() %>/image/check_right.gif' alt='�����޸�' style='cursor:hand' onclick='saveData()'/></span></td></tr>";
							
						//}
						hava = true;//�Ѿ����޸�
						oriNum = henji.oriData;//���޸� ���¼�ϴ��޸ĵ�ֵ
					}
				}
				if(!hava){
					//û�б��޸�
					beizhu += "<tr id='changeTr' onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\" ><td>admin</td><td>��ǰʱ��</td><td><span id='firstTD'>"
								+obj.val()+"</span></td><td><input  id='changeInput' style='width:100%'/></td><td><input id='finalInput' style='width:100%'/></td><td><input type='text' id='descInput' style='width:100%' /></td><td><span id='changeSpan' style='visibility: hidden'>&nbsp;<img src='<%=request.getContextPath() %>/image/check_right.gif' alt='�����޸�' style='cursor:hand' onclick='saveData()'/></span></td></tr>";
					oriNum = obj.text();//ԭʼֵ
				}
				<%}%>
				isOk = true;
				var url = "findAFDataTrace.do?repInId=<%=repInId%>&&cellName="+cellName+"&&curDate="+curDate;
				$.post(url,function(data){
					beizhu +=data;
					beizhu +="</table>";

					if(isOk){
						//�򿪺ۼ���
						$("#innerFinalDIV").html(beizhu);
						$('#finalDIV').dialog({
							top:top,
							left:left,
							autoOpen:true,
							title:"���ݺۼ�("+orIData+")"
						});
					}

					<%if(request.getAttribute("type")==null || request.getAttribute("type").equals("marge")){%>
					//�Ƿ������ֵ�����
					var isNum = /^(\d*|\d+\.\d*)$/;
					//���η������ַ�
					$("#changeInput").bind("keydown",function(){
						if(!(event.keyCode==190 || event.keyCode==110 
								|| (event.keyCode>=96 && event.keyCode<=105 )|| (event.keyCode>=48 && event.keyCode<=57) || event.keyCode==8)){
							event.keyCode=0;
							event.cancelBubble = true;
							return false;
						}
					});
					//����ֵ������¼��ı�������
					$("#changeInput").bind("keyup",function(){
						if(!isNum.test($(this).val())){//�Ƿ������ָ�ʽ
							//
							$(this).val($(this).val().substring(0,$(this).val().length-1));
							return;
						}
						if($(this).val()==""){
							$("#finalInput").val("");
							$("#changeSpan").css("visibility","hidden");//�����޸İ�ť����
						}
						else{
							var firstData = $("#firstTD").text();
							if(firstData=="")
								firstData=0;
							//����ֵ
							$("#finalInput").val(parseFloat(firstData)+parseFloat($(this).val()));

							changeNum = $(this).val();//����ֵ
							finalNum = $("#finalInput").val();//����ֵ
							

							$("#changeSpan").css("visibility","visible");//�����޸İ�ť����
							
						}
					});

					//����ֵ�¼���
					$("#finalInput").bind("keydown",function(){
						//���η����ְ���
						if(!(event.keyCode==190 || event.keyCode==110 
								|| (event.keyCode>=96 && event.keyCode<=105 )|| (event.keyCode>=48 && event.keyCode<=57) || event.keyCode==8)){
							event.keyCode=0;//ȡ������
							event.cancelBubble = true;//ȡ���¼�ð��
							return false;
						}
					});
					//�������������¼��ı����ֵ
					$("#finalInput").bind("keyup",function(){
						if(!isNum.test($(this).val())){//�Ƿ������ָ�ʽ
							$(this).val($(this).val().substring(0,$(this).val().length-1));
							return;
						}
						if($(this).val()==""){
							$("#changeInput").val("");
							$("#changeSpan").css("visibility","visible");//�����޸İ�ť����
						}
						else{
							//����ֵ
							var firstData = $("#firstTD").text();
							if(firstData=="")
								firstData=0;
							$("#changeInput").val(parseFloat($(this).val())-parseFloat(firstData));

							changeNum = $(this).val();//����ֵ
							finalNum = $("#finalInput").val();//����ֵ

							$("#changeSpan").css("visibility","visible");//�����޸İ�ť����
						}
					});
					//��ע�¼���
					$("#descInput").bind("change",function(){
						desc = $(this).val();
					});
					<%}%>
				});
			});

			
			
			
			
			//������������
			//beizhu +="<tr onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\"><td>admin</td><td>2012-07-05</td><td>1.0</td><td>2.0</td><td>3.0</td><td colspan='2'>����׼ȷ</td></tr>";
		//	beizhu +="<tr onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\"><td>shd</td><td>2012-07-05</td><td>3.0</td><td>2.0</td><td>1.0</td><td colspan='2' >���ݲ���</td></tr>";
		//	beizhu +="<tr onmouseover=\"this.style.backgroundColor='#EEEEFF'\" onmouseout=\"this.style.backgroundColor='white'\"><td>byg</td><td>2012-07-05</td><td>5.0</td><td>2.0</td><td>3.0</td><td colspan='2'	>ҵ��Ҫ��</td></tr>";
			
		}

		function saveEdit(ctl){
			
			var pnt=$(ctl).parent();
			oriId = pnt.attr("id");//��ȡ�ε�Ԫ���ID
			var have = false;
			for(i=0;i<dataArray.length;i++){
				if(dataArray[i]==oriId){//�ж��Ƿ��޸Ĺ�
					var henji = dataArray[oriId][0];//����޸ĵĺۼ�����
					have = true;//�Ѿ����޸�
					oriNum = henji.oriData;//���޸� ���¼�ϴ��޸ĵ�ֵ
				}
			}
			if(oriNum!=$(ctl).attr("value")){
				
				
				finalNum = $(ctl).attr("value");//����ֵ
				changeNum = parseFloat(finalNum)-parseFloat(oriNum)==''?0:(parseFloat(finalNum)-parseFloat(oriNum));//����ֵ
				desc="ҳ��ֱ���޸� �ޱ�ע";//��ע��Ϊ0����̨����
				saveData()
			}
			$(pnt).html($(ctl).attr("value"));//��ֵ�Ż�ԭ��Ԫ��
			$(pnt).attr("value",$(ctl).attr("value"));//����ǰֵ���浽orig������
			$(ctl).remove();//�Ƴ��༭���������Ҫ
			
			bindEvent($(pnt));
		}
					

		function bindEvent(o){
			o.unbind("click");
			o.bind("click",function(){
			  var writable=$(this).attr("writable");
			  if(writable=="true"){//start if
				var val=$(this).val();
				oriNum = $(this).val();//ԭʼֵ
				obj = $(this);
				$(this).html("<input type='text' onblur='saveEdit(this)' value='"+val+"' style='border:1px solid red;height:100%;width:100%' >");//�����Ԫ��ʱ����̬����һ��input����򣬻�ȡ��ǰ��Ԫ���ֵ���������ʧȥ����ʱ����ֵ
				$(this).children("input").select();
				$(this).unbind("click");//ȡ����ǰ��Ԫ���¼�����Ȼһ���������ʱ�����ٴ������¼��������������벻���ı���Ч��
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
				//�ж��Ƿ��ǿ��Ըı�ĵ�Ԫ��
				if($(this).attr("onClick").toString().indexOf("noFunction")>0){
					var offset = $(this).offset();//��ÿؼ���ǰλ��
					top = offset.top+$(this).height();
					left = offset.left;
					$('#mm').menu('show', {
						left: left,
						top: top
					});
					obj = $(this);//��õ�ǰ����
					oriId = obj.attr("id");//��ȡ�ε�Ԫ���ID
					return false;
				}
			});
			
			$("#tabsDiv").tabs({
				onSelect:function(title){
					if(title=='ԭʼ���'){
						$('#finalDIV').dialog('close'); 
						locationtb('report2');
					}
				}
			});
			
		});

		//�����޸ĺۼ�����
		function saveData(){
			//��������
			if(desc==""){
				desc="��";	
			}
			var henji = new henJi();
			henji.userName = "<%=operator.getUserName()%>";//�޸���
			var time = new Date();//�޸�ʱ��
			henji.changeTime = time.getYear()+"-"+(time.getMonth()+1)+"-"+time.getDate();
			henji.oriData = oriNum;//ԭʼֵ
			henji.changeData = changeNum;//����ֵ
			henji.finalData = finalNum;//�������
			henji.desc = desc;//��ע
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
			obj.text(finalNum);//�޸ĵ�Ԫ��ֵ
			obj.attr("value",finalNum);
			report1_autoCalc(oriId.substring(oriId.indexOf("_")+1));
			$("#finalDIV").dialog('close');//�رպۼ�����
		}
		
		//������Ǭ���ɵ��ϱ���ť
		function doload(){
			 var os = document.getElementsByTagName("span");
			 var o = null;
			 var i=0;
			 for(i=0;i<os.length;i++){
			    if(os[i].innerHTML.toLowerCase().indexOf("submit")!=-1 || os[i].innerHTML.indexOf("�ύ")!=-1){
			      os[i].style.visibility="hidden";
			      os[i].style.position="absolute";
			 	}
			}
		}
		//�����ɵı�����
		function locationtb(objName){
			var tb = document.getElementById(objName);	        
			var top=tb.style.top; // ���ඥ�߸߶�
			var height=tb.clientHeight; 
			var endHeight = top + height;  // ���ײ��߶�
			var except = document.body.clientHeight-endHeight; // ����ײ��߶�
			var except2=except/2;
			if(except>200){
				tb.style.position="absolute";
				tb.style.top=top+except2-50;
			}
			var left=tb.style.left;
			var width=tb.clientWidth;
			var rwidth=left + width;
			except = document.body.clientWidth-rwidth; // ����ײ��߶�
			except2=except/2;
			if(except>200){
				tb.style.position="absolute";
				tb.style.left=left+except2-20;
			}
		}

		/**���ݺۼ�����*/
		var henJi = function(){
			this.userName;//�޸���
			this.changeTime;//�޸�ʱ��
			this.oriData;//ԭʼֵ
			this.changeData;//����ֵ
			this.finalData;//�������
			this.desc;//��ע
			this.cellName;
		}

		function saveReport(){
			//���ӱ�ע��Ϣ
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
			if(confirm("��ȷ��Ҫ����У����?\n")==true){
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
					alert('ϵͳæ�����Ժ�����...��');
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
				     if(confirm('У��ʧ�ܣ��Ƿ���Ҫ�鿴У����Ϣ?')){
					     if(scriptReportFlg=="1"){
					     window.open("<%=request.getContextPath()%>/report/viewDataJYInfo.do?repInId=<%=repId%>",'У����','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
						
					     }else{
					        window.open("<%=request.getContextPath()%>/report/viewValidateInfo.do?repInId=<%=repId%>",'У����','scrollbars=yes,height=600,width=500,status=yes,toolbar=no,menubar=no,location=no,resizable=yes');
						}
					
					}
				  }	
				  else if(result == 'true')
				  {
					 alert('У��ͨ����');	
				  }
				document.getElementById("jiaoyanButton").disabled=false;
			}
			catch(e)
			{}
	    }

	    //ʧ�ܴ���
	    function reportError(request)
	    {
	        alert('ϵͳæ�����Ժ�����...��');
	    }


	    function _view_sjbs(){
			if(confirm('ȷ�����͸ñ���')){
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
		   		alert('ϵͳæ�����Ժ�����...��T');
		   	}
		}
		}

		//����Handler
		function upReportHandler(request)
		{
			try
			{
				var result =  request.getElementsByTagName('result')[0].firstChild.data;;
				  if(result == 'true')
				  {
					 alert('���ͳɹ���');
					 if(scriptReportFlg=="1"){	
					 location.href="<%=request.getContextPath()%>/viewDataReport.do?<%=backQry%>"; 
					 }else{
						 location.href="<%=request.getContextPath()%>/viewNXDataReport.do?<%=backQry%>"; 
					 }					 
				  }
				  else  if(result == 'BJ_VALIDATE_NOTPASS')
				  {
				     alert("���У�鲻ͨ���������ϱ��ñ���");
				  }else if( result == 'BN_VALIDATE_NOTPASS'){
				  
				 	 alert("����У�鲻ͨ���������ϱ��ñ���");
				  }
				  else{
				 	 alert('ϵͳæ�����Ժ�����...��');
				  
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
	 //����js�ű�������Ԫ����ʽ�л��� IF �����ж�ʱ�����������㣬ģ�� excel IF
	
	function EXCEL_IF(a,b,c){
	  if(a==true)
	  	return b;
	  else
	    return c;
	}
	//max����
	function EXCEL_MAX(a,b){
		var r ;
		if(a>=b)
			r = a ;
		else 
			r = b;
		return r ;
	}
	//min���� 
	function EXCEL_MIN(a,b){
		var r ;
		if(a>=b)
			r = b ;
		else 
			r = a;
		return r ;
	}
	//����ֵ
	function EXCEL_ABS(r){
		if(r<0){
			r*=-1;
		}
		return(r);
	}
	//round����
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

<div region="center" title="<%=request.getAttribute("type")!=null && request.getAttribute("type").equals("search")?"���߲鿴":"�����޸�" %>" style="overflow:hidden;" >
		<div class="easyui-tabs" fit="true"  border="false" id="tabsDiv" style="overflow: hidden;">
			
			
			<div title="<%=repname%>" style="padding:5px;overflow:auto;background-color: #F4F4F4;text-align: center;"id="lastDataDiv"> 
				<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0  align="center"><tr>
			<%if(request.getAttribute("type")==null || !String.valueOf(request.getAttribute("type")).equals("search")){
				
			
			%>
		<td height="25" width=100% valign="middle"  style="font-size:13px" background="<%=request.getContextPath()%>/image/toolbar-bg.gif">
		
			
		
		<table width="100%">
			<tr align="center">
				<td>
					<input type="button" class="input-button" value=" �� �� " onclick="saveReport()" id="saveButton">
					&nbsp;&nbsp;&nbsp;
					<%if(isdata.equals("1")){ %>
					<input type="button" class="input-button" value=" У �� " onclick="_validate()" id="jiaoyanButton">
					&nbsp;&nbsp;&nbsp;
					<input type="button" class="input-button" value=" �� �� "onclick="_view_sjbs()">	
					&nbsp;&nbsp;&nbsp;	
					<%} %>					
					<%if(isdata.equals("1")){ %>
						<%
							if(reportFlg.equals("2") || reportFlg.equals("3")){
							%>
							<input type="button" class="input-button" value=" ����EXCEL" onclick="report1_saveAsExcel();return false;">
							<%	
							}else{
							%>
							<input type="button" class="input-button" value=" ����EXCEL" onclick="report1_saveAsExcel1('${requestScope.backQry}',${requestScope.repInId})">
							<%								
							}
						%>
						
					<%}else{%>
						<input type="button" disabled class="input-button" value=" ����EXCEL">
					<% } %>	
					&nbsp;&nbsp;&nbsp;
					<input type="button" class="input-button" value=" �� �� " onclick="_back()">
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
				<input type="button" class="input-button" value=" �رմ��� " onclick="javascript:window.close()">
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
				<div id="finalDIV" class="easyui-dialog" title="���ݺۼ�" style="width:600px;height:200px;left:100px;top:150px; "
					 resizable="true"  maximizable="true">
					<div id="innerFinalDIV"></div>
				</div>
				<%
            		Exception e = (Exception)request.getAttribute("exception");
            		if(e!=null){
						out.println( "<h1>��Ϣ��</h1><div style='color:red'>" + e.getMessage() + "</div>" );
					}
				%>
				<!-- 
				<div id="lastDIV" class="easyui-dialog" title="�¼��鿴" style="width:550px;height:200px;left:100px;top:150px; "
					 resizable="true"  maximizable="true">
					<div id="findLastDIV">
						<table style='width:100%;' align='center' align='center' cellpadding='0' cellspacing='0' >
							<tr align='center' onmouseover="this.backgroundColor='#EEEEFF'" onmouseout="this.backgroundColor='white'">
								<th style='width:150px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�¼�����</th>
								<th style='width:80px'>����ֵ</th>
								<th style='width:80px'>����ֵ</th>
								<th style='width:80px'>�����</th>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>��������</td>
								<td>632,323,233,32</td>
								<td>532,323,233,32</td>
								<td>432,423,233,32</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>�Ͼ�����</td>
								<td>232,323,233,32</td>
								<td>332,323,233,32</td>
								<td>232,423,233,32</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>�Ϻ�����</td>
								<td>232,323,233,32</td>
								<td>332,323,233,32</td>
								<td>232,423,233,32</td>
							</tr>
							<tr align='center' onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
								<td>��������</td>
								<td>232,323,233,32</td>
								<td>332,323,233,32</td>
								<td>232,423,233,32</td>
							</tr>
						</table>
					</div>
				</div>
				-->
				<div id="mm" class="easyui-menu" style="width:120px;">
					<div iconCls="icon-redo" onclick="javascript:showHenji()">���ݺۼ�</div>
					<%-- <div iconCls="icon-undo" onclick="javascript:window.open('sjfc/fcsql.jsp')">���ݷ���</div>--%>
					<%--<div iconCls="icon-undo" onclick="javascript:showXiaji()">�¼��鿴</div>--%>
					<div class="menu-sep"></div>
					<div>Exit</div>
				</div>
   		   </div>		
		
		</div>
		<div id="test" class="easyui-window" closed="true" modal="true" title="��ȷ��ѯ" style="width:400px;height:300px;">
		</div>
</div>
</body>
</html>
