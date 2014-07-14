<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<html:html locale="true">
	<head>
		<html:base/>
		<title>报表汇总设定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script type="text/javascript" src="../../js/comm.js"></script>
		<script type="text/javascript" src="../../js/func.js"></script>
		<script language="javascript">
			var recode;	
			/**
			*	处理下拉列表
			*/
			function memberSelect(select,selectValue)
			{  
				var i=0;
				for(i;i<select.options.length;i++){
				    if(select.options[i].value==selectValue){
				        select.options[i].selected= i;
				        break;
				    }
				}
			}
	
			/**
			*	处理单元字母放置
			*/
			function changeSelect(id){
				recode=id;
				document.getElementById('test').style.display='block';
			}
			/**
			*	全行变化
			*/
			function allRow(){
				
				var name=recode.split("_");
				
				var tValue=window.frames["frame1"].document.getElementById(recode).value;
				var colMax=document.getElementById("colMax").value;
				for(var i=1;i<colMax;i++){
					var str = i+"_"+ name[1]+"_";
			
				    var tar = window.frames["frame1"].document.getElementById(str);
				    if(tar !=undefined){		 		
				    	memberSelect(tar,tValue);
				    }
				} 
				document.getElementById('test').style.display='none';
			}
			/**
			*全列变化
			*/
			function allCol(){
				var name=recode.split("_");
				var tValue=window.frames["frame1"].document.getElementById(recode).value;
				var rowMax=document.getElementById("rowMax").value;
				for(var i=1;i<rowMax;i++){
					var str = name[0]+"_"+i+"_";
			
				    var tar = window.frames["frame1"].document.getElementById(str);
				
				    if(tar !=undefined){
				 
				    	memberSelect(tar,tValue);
				    }
				} 
				document.getElementById('test').style.display='none';
			}
			
			
			
			/**
			*全部变化
			*/
			function allChange(){
				var tValue=window.frames["frame1"].document.getElementById(recode).value;
				var rowMax=document.getElementById("rowMax").value;
				var colMax=document.getElementById("colMax").value;
				for(var i=0;i<rowMax;i++){
					for(var j=0;j<colMax;j++){
						var cid=window.frames["frame1"].document.getElementById(j+"_"+i+"_");
							if(cid !=undefined){
					    	memberSelect(cid,tValue);
					    	
					    }
					}
				} 
				document.getElementById('test').style.display='none';
			}
			/**
			*	单独变化
			*/
			function onlyOne(){
				document.getElementById('test').style.display='none';
			}
			/**
			*	提交数据
			*/
			function sub(){

				var row=document.getElementById("table1").insertRow();
				var rowMax=document.getElementById("rowMax").value;
				var colMax=document.getElementById("colMax").value;
				
				for(var i=0;i<rowMax;i++){
					for(var j=0;j<colMax;j++){
						var cid=window.frames["frame1"].document.getElementById(j+"_"+i+"_");
							if(cid !=undefined){
					 		var cell=row.insertCell();
							cell.innerHTML="<input type=hidden name=state_"+j+"_"+i+" value="+cid.value+" />";
					    	var test=document.getElementsByName("state_"+j+"_"+i);
					    }
					}
				} 
				document.forms[0].submit();
			}
			
		</script>
	</head>
	<body background="../image/total.gif">
		<br><h3>汇总关系设定</h3><br>
	<html:form  styleId="form1"  action="/template/mod/saveCollectTypeAction"  method="Post" onsubmit="">
		<tr>
		<Iframe id="frame1"   name="frame1" src="<%=request.getAttribute("HTML") %>" width="90%" height="85%" scrolling="yes" frameborder="0">
		</iframe>
		</tr>
		<TR>
				<td>
				</td>
				<TD >
				<p>
					<input type="button" value=" 保 存 " class="input-button" onclick="if(confirm('您确定提交这个汇总关系设定吗？\n')==true){sub()}">&nbsp;
				</TD>
				<TD>
					<input class="input-button" type="button" value=" 返 回 " onclick="javascript:history.back(-1)">
				</TD>
		</TR>
		<table id="table1">
		
		<input type="hidden" id="rowMax" name="rowMax" value=<%=request.getAttribute("rowMax") %> >
		<input type="hidden" id="colMax" name="colMax" value=<%=request.getAttribute("rowMax") %> >
		<input type="hidden" name="versionId"  value=<%=request.getAttribute("versionId") %> >
		<input type="hidden" name="childRepId" value=<%=request.getAttribute("childRepId") %> >
		</table>
	</html:form>
	
	<div   id="test"    style="position:absolute;   left:   100px;   top:   100px;" class="body">   
  	<table  background="../image/total.gif" width="50%"     border="0"   cellspacing="0"   cellpadding="0">   
      <tr >   
          <td class="td2"><input class="buttonView" type="button" value="全行使用" onclick="allRow()"/></td>   
          <td class="td2"><input class="buttonView" type="button" value="全列使用" onclick="allCol()"/></td>
   <!--
           <td class="td2"><input class="buttonView" type="button" value="全部使用" onclick="allChange()"/></td>
   -->
          <td class="td2"><input class="buttonView" type="button" value="本格使用" onclick="onlyOne()"/></td>  
      </tr>   
  	</table></div>   
	<script language="javascript">
		document.getElementById('test').style.display='none';
		window.frames["frame1"].location.reload();
	</script>
	</body>
</html:html>
