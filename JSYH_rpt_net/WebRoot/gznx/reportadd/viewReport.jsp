<!-- ��ҳ���Ǳ�����ǰչʾexcelҳ�� -->
<%@ page contentType="text/html; charset=gb2312" language="java" errorPage=""  import="java.io.*"%>
<%@ page session="true" import="java.lang.StringBuffer,java.util.Map, com.runqian.report4.util.ReportUtils,com.runqian.report4.*"%>
<%@page import="com.cbrc.smis.security.Operator"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report" %>
<%
	String appPath = request.getContextPath();
           

	Map reportMap = (Map)request.getAttribute("reportParam");
	
	StringBuffer param = new StringBuffer("");
	String filename = (String)reportMap.get("filename");				
	//��ȡ�����ͺ�
	param.append("RangeID").append("=").append(reportMap.get("RangeID")).append(";");
	param.append("Freq").append("=").append(reportMap.get("Freq")).append(";");
	param.append("CCY").append("=").append(reportMap.get("CCY")).append(";");
	param.append("ReptDate").append("=").append(reportMap.get("ReptDate")).append(";");
	param.append("OrgID").append("=").append(reportMap.get("OrgID")).append(";");		
	if(reportMap.get("RepID")!=null){
		param.append("RepID").append("=").append(reportMap.get("RepID")).append(";");
	}
	String templateId = (String)reportMap.get("templateId");
	String versionId = (String)reportMap.get("versionId");

	
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
	<script language="javascript" src="../../runqianReport.js"></script>
	 <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/script/jquery.easyui.min.js"></script>
	<style type="text/css">
   		#descDiv td{border:1px solid black;}
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
</head>
<body  >
<input type="hidden"  id="tempDesc" value='<%= request.getAttribute("repDesc") %>'>
<SCRIPT language="javascript">
  		
  		window.onload=function(){
  			//��˲鿴������Ҫ��ʾ���ӱ�ע�Ĺ���
		  /**if("${type}"=="search")
		  {
		  	document.getElementById("addRemarkTr").style.display="none";
		  }
		  //�����޸ģ�����ʾ���ӱ�ע�Ĺ���
		  else if("${type}"=="marge")
		  	document.getElementById("addRemarkTr").style.display="block";
		  //������� ����Ϊ��ʾ���ӱ�ע
		  else
		  	document.getElementById("addRemarkTr").style.display="block";
		  **/
			 var o = document.getElementById("repDesc");
		  	 if(o.value=="null")
		 	 o.value=document.getElementById("tempDesc").value;
  		}

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

   		$(function(){
   			$('#finalDIV').dialog('close'); 
			$('#lastDIV').dialog('close'); 
   	   	});


   		function showHenji(){
			var beizhu="<table style='width:100%;' align='center' align='center' cellpadding='0' cellspacing='0'><tr>";
			
			beizhu +="<tr><th>�޸���</th><th>�޸�ʱ��</th><th style='width:50px'>ԭʼֵ</th><th style='width:50px'>����ֵ</th><th style='width:50px'>�������</th><th style='width:150px;border-right:0px'>��ע</th><th style='border-left:0px'><span style='visibility: hidden'>&nbsp;<img src='<%=request.getContextPath() %>/image/check_right.gif'  /></span></th></tr>";
		
			

			var cellName = oriId.substring(oriId.indexOf("_")+1);
			//��õ�ǰʱ�� ��ֹajax����
			var curDate = "<%=System.currentTimeMillis()%>";
			var url = "findAFDataTrace.do?versionId=<%=versionId%>&&templateId=<%=templateId%>&&cellName="+cellName+"&&curDate="+curDate;
			$.post(url,function(data){
				beizhu +=data;

				beizhu +="</table>";

				//�򿪺ۼ���
				$("#innerFinalDIV").html(beizhu);
				$('#finalDIV').dialog({
					top:top,
					left:left,
					autoOpen:true
				}); 

				
			});
			
			
		}
   	/****end *���ƶ�div��ע��					************/
</SCRIPT>
    
<jsp:include page="toolbar.jsp" flush="false" />
		<table width="100%" border="0" cellpadding="2" cellspacing="1" align="center">
			<tr>
				<td> 
				<input type="button" class="input-button" value=" �رմ��� " onclick="javascript:window.close()">
				</td>
			
			</tr>
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
					 />	
	
	
				<div id="finalDIV" class="easyui-dialog" title="���ݺۼ�" style="width:550px;height:200px;left:100px;top:150px; "
					 resizable="true"  maximizable="true">
					<div id="innerFinalDIV"></div>
				</div>
				<div id="lastDIV" class="easyui-dialog" title="�¼��鿴" style="width:550px;height:200px;left:100px;top:150px; "
					 resizable="true"  maximizable="true">
					<div id="findLastDIV">
						<table style='width:100%;' align='center' align='center' cellpadding='0' cellspacing='0'>
							<tr>
								<th style='width:300px'>�¼�����</th>
								<th style='width:80px'>����ֵ</th>
								<th style='width:80px'>����ֵ</th>
								<th style='width:80px'>�����</th>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
						</table>
					</div>
				</div>
				<div id="mm" class="easyui-menu" style="width:120px;">
					<div iconCls="icon-redo" onclick="javascript:showHenji()">���ݺۼ�</div>
					<%-- <div iconCls="icon-undo" onclick="javascript:window.open('sjfc/fcsql.jsp')">���ݷ���</div>--%>
					<div iconCls="icon-undo" onclick="javascript:showXiaji()">�¼��鿴</div>
					<div class="menu-sep"></div>
					<div>Exit</div>
				</div>
	<%
		if(request.getAttribute("repDesc")!=null && !request.getAttribute("repDesc").equals(""))
		{
	 %>
	
    
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
Minheight : 150, 
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
		// var addRepDesc=document.getElementById("descTextarea");
		 
		// repDesc.style.height=parseInt(this._body.style.height)-parseInt(addRepDesc.style.height)-20;
		 repDesc.style.height=parseInt(this._body.style.height)-20;
		 repDesc.style.width=parseInt(this._dragobj.style[this._Css.x])-28;
		 
		// addRepDesc.style.height=parseInt(this._body.style.height)-parseInt(repDesc.style.height)-20;
		// addRepDesc.style.width=parseInt(this._dragobj.style[this._Css.x])-28;
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

var beizhu="<table style='width:100%'><tr><td>";
<%
	if(request.getAttribute("repDesc")!=null && !request.getAttribute("repDesc").equals(""))
	{
%>
		beizhu+="<textarea  id='repDesc' style='width:272px;height:85px;overflow:auto;background-color: #f7f4f4' readonly='readonly' >null</textarea>";
<%
	}else{
%>		
		beizhu+="<textarea  id='repDesc' style='width:272px;height:85px;overflow:auto;background-color: #f7f4f4' readonly='readonly' >���ޱ�ע��Ϣ</textarea>";
<%
	}
%>
beizhu+="</tr></td></table>";
new Dialog({Info:"��ע��",Content:beizhu,Width:300,Height:150,Left:950,Top:55}); 

// --></script> 		
    <%
    	}
     %>
    <script language="javascript">       
       locationtb('report1');
    </script>
</body>
</html>
