<!-- ��ҳ���Ǳ�����ǰչʾexcelҳ�� -->
<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report"%>
<%@ page session="true" import="java.lang.StringBuffer,java.util.Map"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%	
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
%>
<html>
<title>��ҵ����</title>
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="<%=appPath%>/js/jquery-1.4.2.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<style type="text/css">
   		#descDiv td{border:1px solid #86898C;}
   		#descTable td{border:none;}
   		.dialogcontainter{height:400px; width:400px; border:1px solid #14495f; position:absolute; font-size:13px;} 
		.dialogtitle{height:26px; width:auto; background-image:url('<%=appPath%>/image/103444839_p.gif');cursor:move} 
		.dialogtitleinfo{height:20px; margin-top:2px; line-height:20px; vertical-align:middle; color:#FFFFFF; font-weight:bold; } 
		.dialogtitleico{float:right; height:20px; width:21px; margin-top:2px; margin-right:5px;text-align:center; line-height:20px; vertical-align:middle; background-image:url('<%=appPath%>/image/103419495_p.gif');background-position:-21px 0px;} 
		.dialogbody{ padding:10px; width:auto; background-color: #FFFFFF;} 
		.dialogbottom{ 
			bottom:1px; right:1px;cursor:nw-resize; 
			position:absolute; 
			background-image:url('<%=appPath%>/image/103419495_p.gif'); 
			background-position:-42px -10px; 
			width:10px; 
			height:10px; 
			font-size:0;}
   	</style>
	<SCRIPT language="javascript">
	var scriptReportFlg=<%=reportFlg%>;
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
	  
	  //��˲鿴������Ҫ��ʾ���ӱ�ע�Ĺ���
	 <%if(Config.ISADDDESC){%>
		  if("${type}"=="search")
		  {
		  	document.getElementById("addRemarkTr").style.display="none";
		  }
		  //�����޸ģ�����ʾ���ӱ�ע�Ĺ���
		  else if("${type}"=="marge")
		  	document.getElementById("addRemarkTr").style.display="block";
		  //������� ����Ϊ��ʾ���ӱ�ע
		  else
		  	document.getElementById("addRemarkTr").style.display="block";
		  //
		  var o = document.getElementById("repDesc");
		  if(o.value=="null")
		 	 o.value=document.getElementById("tempDesc").value;
	 <%}%>
	  
	  
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
			  	var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineReport.do?repInId=<%=repId%>";
			    var param = "radom="+Math.random();
			   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
		   	}else{
		   		var upReportURL ="<%=request.getContextPath()%>/upLoadOnLineNXReport.do?repInId=<%=repId%>";
			    var param = "radom="+Math.random();
			   	new Ajax.Request(upReportURL,{method: 'post',parameters:param,onComplete:upReportHandler,onFailure: reportError});
		   	}
	   	
	   	}
	   	catch(e)
	   	{
	   		alert('ϵͳæ�����Ժ�����...��T');
	   	}
	}
	}
			//����Handler
		function upReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
					
				  if(result == 'true')
				  {
					 alert('���ͳɹ���');
					 if(scriptReportFlg=="1"){	
					 window.location="<%=request.getContextPath()%>/viewDataReport.do?<%=backQry%>"; 
					 }else{
					 window.location="<%=request.getContextPath()%>/viewNXDataReport.do?<%=backQry%>"; 
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
	
	
	function _validate(){
		if(confirm("��ȷ��Ҫ����У����?\n")==true){
			try{
				if(scriptReportFlg=="1"){	
					var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReport.do?repInId=<%=repId%>"; 
					var param = "radom="+Math.random();
					new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
				}else{	
					var validateURL = "<%=request.getContextPath()%>/report/validateOnLineReportNX.do?repInId=<%=repId%>"; 
					var param = "radom="+Math.random();
					new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
				}
			}catch(e){
				alert('ϵͳæ�����Ժ�����...��');
				return;
			}			
		}	
	}
	
			//У��Handler
		function validateReportHandler(request)
		{
			try
			{
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;;
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
			}
			catch(e)
			{}
	    }
   
	function saveReport(){
		//���ӱ�ע��Ϣ
		<%if(Config.ISADDDESC){%>
			addReportInDesc();
		<%}%>
		_submitTable( report1 );
		writeLog();
	}
	//������־��Ϣ
	function writeLog(){
		var logurl = "<%=request.getContextPath()%>/system_mgr/insertFillLog.do"; 
		var param = "repInId=<%=repId%>";
		new Ajax.Request(logurl,{method: 'post',parameters:param,onComplete:"",onFailure: ""});
	}
	
	//���ӱ�ע��Ϣ
	function addReportInDesc(){
		var url="<%=request.getContextPath()%>/system_mgr/addReportInDesc.do";
		var param = "repInId=<%=repId%>&&addDesc="+document.getElementById("descTextArea").value;
		new Ajax.Request(url,{method: 'post',parameters:param,onComplete:"",onFailure: ""});
	}
	function _back(){
		if(scriptReportFlg=="1"){
			window.location = "viewDataReport.do?<%=backQry%>";
		}else{
			window.location = "viewNXDataReport.do?<%=backQry%>";		
		}	
	}
	
	        function locationtb(objName){
	   //     self.moveTo(0,0);
       //     self.resizeTo(screen.availWidth,screen.availHeight);
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
	     		function fillColor(objName){
		  var tb = document.getElementById(objName);
		  var tds = tb.getElementsByTagName('td');
		  var i;
		  for(i=0;i<tds.length;i++){
		    var tmp=tds[i].getAttribute('inputDataType');
		    if(tmp!=null && tmp!=''){
		     tds[i].style.background='#D1F2FE';
	//	     tds[i].style.borderColor='#FB3004';
		     tds[i].title='���';
		    }	         
		  }        
		}
		var win = this; 
		var n = 0; 

	function findInPage(str) 
	{ 
	var txt, i, found; 
	if (str == "") 
	return false; 


	txt = win.document.body.createTextRange(); 
	for (i = 0; i <= n && (found = txt.findText(str)) != false; i++) 
	{ 
	txt.moveStart("character", 1); 
	txt.moveEnd("textedit"); 
	} 
	if (found) 
	{ 
		txt.moveStart("character", -1); 
		txt.findText(str); 
		txt.select(); 
		txt.scrollIntoView(); 
		n++; 
	} 
	else 
	{ 
	if (n > 0) 
	{ 
	n = 0; 
	findInPage(str); 
	} 
	else 
	alert(str + "... ��Ҫ�ҵ����ֲ�����"); 
	} 
	
	return false; 
	}
	
	/****���ƶ�div��ע��					************/
	var currentMoveObj=null;
   		var relLeft,relTop;
   		
   		function ondown(obj,e)
   		{
   			currentMoveObj=document.getElementById("descDiv");//��ȡ�˶���
   			var x=e.clientX;//��ȡ��ǰx,y����
   			var y=e.clientY;
   			
   			relLeft=x-currentMoveObj.style.pixelLeft;//��ǰx�����굽�ؼ���߿�ľ���
   			relTop=y-currentMoveObj.style.pixelTop;//���ϱ߿�ľ���
   			currentMoveObj.style.position="absolute";//���Զ�λ
   		}
   		
   		window.document.onmouseup=function()
   		{
   			currentMoveObj=null;//���
   		}
   		
   		function moves(e)
   		{
   		 	if(currentMoveObj!=null)
   		 	{
   		 		var x=e.clientX;//��ȡ��ǰx,y����
   				var y=e.clientY;
   		 		currentMoveObj.style.pixelLeft=x-relLeft;//ͨ����ǰ�����x�����굽�ؼ���߿�ľ������ �õ��ƶ���Ŀؼ�����߾�
   		 		currentMoveObj.style.pixelTop=y-relTop;//ͬ�ϻ���ϱ߾�
   		 	}
   		}
   		
   		function showthis(obj,e)
   		{
   			alert(e.clientX);
   			alert(e.clientY);
   		}

   		function report1_saveAsExcel1(backQry,repInId){
			<%if(isdata.equals("1")){ %>
				window.location.href='/rpt_net/expExcel.do?repInId='+repInId+"&backQry="+backQry;
			<%}%>
		}
   	/****end *���ƶ�div��ע��					************/
	</SCRIPT>		
</head>
<body onload="doload()" SCROLL="yes">
<input type="hidden"  id="tempDesc" value='<%= request.getAttribute("repDesc") %>'>
<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
    <table border="0" width="100%" align="center">
			<tr height="30">
				<td>
					��ǰλ�� &gt;&gt; ������ &gt;&gt; ���������
				</td>
			</tr>
		</table>
		<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0  align="center"><tr>
		<td height="25" width=100% valign="middle"  style="font-size:13px" background="<%=request.getContextPath()%>/image/toolbar-bg.gif">
		<table width="100%">
			<tr align="center">
				<td>
					<input type="button" class="input-button" value=" �� �� " onclick="saveReport()">
					&nbsp;&nbsp;&nbsp;
					<%if(isdata.equals("1")){ %>
					<input type="button" class="input-button" value=" У �� " onclick="_validate()">
					&nbsp;&nbsp;&nbsp;
					<input type="button" class="input-button" value=" �� �� "onclick="_view_sjbs()">	
					&nbsp;&nbsp;&nbsp;	
					<%} %>					
					<input type="button" class="input-button" value=" ����EXCEL "onclick="report1_saveAsExcel1('${requestScope.backQry}',${requestScope.repInId})">
					&nbsp;&nbsp;&nbsp;
					<input type="button" class="input-button" value=" �� �� "onclick="_back()">
					&nbsp;&nbsp;&nbsp;
				</td>			
			</tr>			
			<tr>
			
			</tr>
		</table>
		</td>
		</tr></table>
		<table height="25">
		<form name="search1" onSubmit="return findInPage(this.Word.value);">
		<table>
				<tr >
					<td height="2" colspan="2" align="center">
						<input type="text" name="Word" size="30"  >
					</td>
					<td width="61" align="center">
						<input type="submit" name="Submit" value="����ҳ��">
					</td>
				</tr>
				</table>
			</form>
			</table>	
			
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
		
    <script type="text/javascript"><!-- 
var z=1,i=1,left=10 
var isIE = (document.all) ? true : false; 

//���id
var $ = function (id) { 
return document.getElementById(id); 
}; 

var Extend = function(destination, source) { 
for (var property in source) { 
destination[property] = source[property]; 
} 
} 

var Bind = function(object, fun,args) { 
return function() { 
return fun.apply(object,args||[]); 
} 
} 

var BindAsEventListener = function(object, fun) { 
var args = Array.prototype.slice.call(arguments).slice(2); 
return function(event) { 
return fun.apply(object, [event || window.event].concat(args)); 
} 
} 

var CurrentStyle = function(element){ 
return element.currentStyle || document.defaultView.getComputedStyle(element, null); 
} 

function create(elm,parent,fn){var element = document.createElement(elm);fn&&fn(element); parent&&parent.appendChild(element);return element}; 
function addListener(element,e,fn){ element.addEventListener?element.addEventListener(e,fn,false):element.attachEvent("on" + e,fn)}; 
function removeListener(element,e,fn){ element.removeEventListener?element.removeEventListener(e,fn,false):element.detachEvent("on" + e,fn)}; 

var Class = function(properties){ 
var _class = function(){return (arguments[0] !== null && this.initialize && typeof(this.initialize) == 'function') ? this.initialize.apply(this, arguments) : this;}; 
_class.prototype = properties; 
return _class; 
}; 

var Dialog = new Class({ 
options:{ 
Width : 315, 
Height : 325, 
Left : 100, 
Top : 100, 
Titleheight : 26, 
Minwidth : 200, 
Minheight : 200, 
CancelIco : true, 
ResizeIco : false, 
Info : "", 
Content : "", 
Zindex : 2 
}, 
initialize:function(options){ 
this._dragobj = null; 
this._resize = null; 
this._cancel = null; 
this._body = null; 
this._x = 0; 
this._y = 0; 
this._fM = BindAsEventListener(this, this.Move); 
this._fS = Bind(this, this.Stop); 
this._isdrag = null; 
this._Css = null; 
//////////////////////////////////////////////////////////////////////////////// 
this.Width = this.options.Width; 
this.Height = this.options.Height; 
this.Left = this.options.Left; 
this.Top = this.options.Top; 
this.CancelIco = this.options.CancelIco; 
this.Info = this.options.Info; 
this.Content = this.options.Content; 
this.Minwidth = this.options.Minwidth; 
this.Minheight = this.options.Minheight; 
this.Titleheight= this.options.Titleheight; 
this.Zindex = this.options.Zindex; 
Extend(this,options); 
Dialog.Zindex = this.Zindex 
//////////////////////////////////////////////////////////////////////////////// ����dialog 
//var obj = ['dialogcontainter','dialogtitle','dialogtitleinfo','dialogtitleico','dialogbody','dialogbottom']; 
var obj = ['dialogcontainter','dialogtitle','dialogtitleinfo','dialogbody','dialogbottom']; 
for(var i = 0;i<obj.length;i++) 
{ obj[i]=create('div',null,function(elm){elm.className = obj[i];}); 
	//obj[i]=create('textarea',null,function(elm){elm.className = obj[i];}); 
} 
obj[2].innerHTML = this.Info; 
//obj[4].innerHTML = this.Content; 
obj[3].innerHTML = this.Content; 
obj[1].appendChild(obj[2]); 
//obj[1].appendChild(obj[3]); 
obj[0].appendChild(obj[1]); 
//obj[0].appendChild(obj[4]); 
obj[0].appendChild(obj[3]); 
//obj[0].appendChild(obj[5]); 
obj[0].appendChild(obj[4]); 
document.body.appendChild(obj[0]); 
this._dragobj = obj[0]; 
//this._resize = obj[5]; 
this._resize = obj[4]; 
//this._cancel = obj[3]; 
//this._body = obj[4]; 
this._body = obj[3]; 
////////////////////////////////////////////////////////////////////////////////o,x1,x2 
////����Dialog�ĳ� �� ,left ,top 
with(this._dragobj.style){ 
     height = this.Height + "px";top = this.Top + "px";width = this.Width +"px";left = this.Left + "px";zIndex = this.Zindex; 
} 
this._body.style.height = this.Height - this.Titleheight-parseInt(CurrentStyle(this._body).paddingLeft)*2+'px'; 
/////////////////////////////////////////////////////////////////////////////// ����¼� 
addListener(this._dragobj,'mousedown',BindAsEventListener(this, this.Start,true)); 
//addListener(this._cancel,'mouseover',Bind(this,this.Changebg,[this._cancel,'0px 0px','-21px 0px'])); 
//addListener(this._cancel,'mouseout',Bind(this,this.Changebg,[this._cancel,'0px 0px','-21px 0px'])); 
//addListener(this._cancel,'mousedown',BindAsEventListener(this,this.Disappear)); 
addListener(this._body,'mousedown',BindAsEventListener(this, this.Cancelbubble)); 
addListener(this._resize,'mousedown',BindAsEventListener(this, this.Start,false)); 
}, 
Disappear:function(e){ 
     this.Cancelbubble(e); 
     document.body.removeChild(this._dragobj); 
}, 
Cancelbubble:function(e){ 
     this._dragobj.style.zIndex = ++Dialog.Zindex; 
     document.all?(e.cancelBubble=true):(e.stopPropagation()) 
}, 
Changebg:function(o,x1,x2){ 
     o.style.backgroundPosition =(o.style.backgroundPosition==x1)?x2:x1; 
}, 
Start:function(e,isdrag){ 
     if(!isdrag){this.Cancelbubble(e);} 
     this._Css = isdrag?{x:"left",y:"top"}:{x:"width",y:"height"} 
     this._dragobj.style.zIndex = ++Dialog.Zindex; 
     this._isdrag = isdrag; 
     this._x = isdrag?(e.clientX - this._dragobj.offsetLeft||0):(this._dragobj.offsetLeft||0) ; 
     this._y = isdrag?(e.clientY - this._dragobj.offsetTop ||0):(this._dragobj.offsetTop||0); 
     if(isIE) 
     { 
         addListener(this._dragobj, "losecapture", this._fS); 
         this._dragobj.setCapture(); 
     } 
     else 
     { 
         e.preventDefault(); 
         addListener(window, "blur", this._fS); 
     } 
     addListener(document,'mousemove',this._fM) 
     addListener(document,'mouseup',this._fS) 
}, 
Move:function(e){ 
     window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty(); 
     var i_x = e.clientX - this._x, i_y = e.clientY - this._y; 
     this._dragobj.style[this._Css.x] = (this._isdrag?Math.max(i_x,0):Math.max(i_x,this.Minwidth))+'px'; 
     this._dragobj.style[this._Css.y] = (this._isdrag?Math.max(i_y,0):Math.max(i_y,this.Minheight))+'px';
     if(!this._isdrag) 
	 {
		 this._body.style.height = Math.max(i_y -this.Titleheight,this.Minheight-this.Titleheight)-2*parseInt(CurrentStyle(this._body).paddingLeft)+'px'; 
		 
		 var repDesc=document.getElementById("repDesc");
		 var addRepDesc=document.getElementById("descTextarea");
		 
		 repDesc.style.height=parseInt(this._body.style.height)-parseInt(addRepDesc.style.height)-20;
		 repDesc.style.width=parseInt(this._dragobj.style[this._Css.x])-28;
		 
		 addRepDesc.style.height=parseInt(this._body.style.height)-parseInt(repDesc.style.height)-20;
		 addRepDesc.style.width=parseInt(this._dragobj.style[this._Css.x])-28;
	 }
}, 
Stop:function(){ 
     removeListener(document,'mousemove',this._fM); 
     removeListener(document,'mouseup',this._fS); 
     if(isIE) 
     { 
         removeListener(this._dragobj, "losecapture", this._fS); 
         this._dragobj.releaseCapture(); 
         } 
         else 
            { 
            removeListener(window, "blur", this._fS); 
         }; 
     } 
}) 
<%if(Config.ISADDDESC){%>
var beizhu="<table style='width:100%;height:100%'><tr><td>";
<%
	if(request.getAttribute("repDesc")!=null && !request.getAttribute("repDesc").equals(""))
	{
%>
		beizhu+="<textarea  id='repDesc' style='width:272px;height:65px;overflow:auto;background-color: #f7f4f4' readonly='readonly' >null</textarea>";
<%
	}else{
%>		
		beizhu+="<textarea  id='repDesc' style='width:272px;height:65px;overflow:auto;background-color: #f7f4f4' readonly='readonly' >���ޱ�ע��Ϣ</textarea>";
<%
	}
%>
beizhu+="</tr></td><tr id='addRemarkTr'><td><textarea id='descTextarea' style='width:272px;height:65px;overflow:auto'></textarea></td></tr></table>";
new Dialog({Info:"��ע��",Content:beizhu,Width:300,Height:150,Left:850,Top:120}); 
<%  
}
%>

// --></script> 
    <script language="javascript">
       locationtb('report1');
    </script>
    
    
    <br>
    <br>
</body>
</html>
