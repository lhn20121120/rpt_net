<%@ page contentType="text/html;charset=gb2312" errorPage=""%>
<%@ page session="true"%>
<%@ page import="com.fitech.gznx.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html>
<head>
	<title>�쵼��ҳ</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="Thu,   01   Dec   1900   16:00:00   GMT">

	<link href="../../css/globalStyle.css" type="text/css" rel="stylesheet">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../calendar/calendar.js"></script>
	<script type="text/javascript" src="../../calendar/calendar-cn.js"></script>
	<script language="javascript" src="../../calendar/calendar-func.js"></script>
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
				
			        function dosubmit(){
			            var v2=document.getElementById("waitDiv");
			            v2.style.display='';
			            return true;
			         }
			         
			         function doClick(){
			            var v1=document.getElementById("runqianDiv");
			            v1.style.display='none';
			            var v2=document.getElementById("waitDiv");
			            v2.style.display='';
			         }
			         
			         function selectOnClick(){
	                   var v = document.getElementById("selectOrgs");
	                   var s = v.options[0].value;
	                   if(s.indexOf('a')!=-1){
	                     v.options[0]=null;
	                     v.options[0].checked;
	                   }
	                 }
	                //ͼƬ�Ŵ�չʾ
	                 function showBigImg(src,random)
	                 {
	                 	
	                 	var bigImgPath = src+"Big.jpg?random="+random;
	                 	var Img_td = document.getElementById("bigImg_td");
	                 	
	                 	//document.getElementById("ccyId").style.display="none";
	                 	window.location="<%=request.getContextPath()%>/manager_frontpage/general_view/big_chart.jsp?src="+bigImgPath;
	                 	//Img_td.innerHTML = "<img id='bigImg' src='"+bigImgPath+"'>"
	                 	//document.getElementById("bigImgDiv").style.height = "800";
	                 	//document.getElementById("bigImgDiv").style.visibility = "visible";
	                 	//window.scroll(0,0);
	                 	
					}
	                
	                 //ͼƬ�Ŵ�չʾ�ر�
	                function closeBigImg(){
	                 	document.getElementById("ccyId").style.display="";
	                 	document.getElementById("bigImgDiv").style.visibility = "hidden";
	                 	document.getElementById("bigImgDiv").style.height = "0";
	                 
	                 }
	                 
	                 function treeOnClick(id,value)
					{
						
						document.getElementById('orgId').value = id;
						document.getElementById('orgName').value = value;
						document.getElementById("orgTree").style.height = "0";
						document.getElementById('orgTree').style.visibility="hidden"; 
					}
			

		//��ʾ,�ر����Ͳ˵�
	function showTree(){
		if(document.getElementById('orgTree').style.visibility =='hidden'){
		    var textname = document.getElementById('selectedOrgName');
			document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgTree").style.height = "200";
			document.getElementById("orgTree").style.visibility = "visible";   // ��ʾ���Ͳ˵�
		}

		else if(document.getElementById("orgTree").style.visibility == "visible"){
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden";      //�ر����Ͳ˵�
		}
	}
	
	function closeTree(objTxt,objTree){	  
	   var obj = event.srcElement;
	   if(obj!=objTxt && obj!=objTree){
	     
	     objTree.style.height = "0";
	     objTree.style.visibility="hidden";      //�ر����Ͳ˵�
	   }
	}
	function closeOrgTree(){
	  var selectedOrgName = document.getElementById("selectedOrgName");
      var orgTree = document.getElementById("orgTree");
      closeTree(selectedOrgName,orgTree);
	}		
	
	//�����ı����ˮƽ���λ��
	function getObjectLeft(e)   
	{   
		var l=e.offsetLeft;   
		while(e=e.offsetParent)   
			l += e.offsetLeft;   
		return   l;   
	}   
	//�����ı���Ĵ�ֱ���λ��
	function getObjectTop(e)   
	{   
		var t=e.offsetTop;   
		while(e=e.offsetParent)   
			t += e.offsetTop;   
		return   t;   
	}
	
		   
			        
	    </script>

	<script language="javascript">
	    	var flagValue = '<logic:present name="QueryForm"><bean:write name="QueryForm" property="flagId"/></logic:present>';
			var freqValue = '<logic:present name="QueryForm"><bean:write name="QueryForm" property="freq"/></logic:present>';
			var yearValue = '<logic:present name="QueryForm"><bean:write name="QueryForm" property="dataYear"/></logic:present>';
			var freqDateValue = '<logic:present name="QueryForm"><bean:write name="QueryForm" property="freqDate"/></logic:present>';
			var dataDateValue = '<logic:present name="QueryForm"><bean:write name="QueryForm" property="dataDate"/></logic:present>';
	    
	   </script>
</head>
<body>

	<div id="bigImgDiv" align="left" style="width:98%; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden;position:absolute; z-index:2;">


		<table align="center" border="0">
			<tr>
				<td align="right">
					<br>
				</td>
			</tr>
			<tr>
				<td align="center" id="bigImg_td">
					<img id="bigImg" src="">
				</td>
			</tr>
		</table>
	</div>
	<table border="0" width="98%" align="center">
		<tr>
			<td>
				��ǰλ�� &gt;&gt; �쵼��ҳ &gt;&gt; ��Ӫ״��һ�� &gt;&gt;
				<logic:present name="gs" scope="session">
					<bean:write name="gs" property="naviGraphName" />
				</logic:present>
			</td>
		</tr>
		<tr>
			<td align="right">
				<img src="../../image/backOnLending.gif" border="0" style="cursor:hand" alt="�������" onClick="javascript:window.location='<%=request.getContextPath()+ "/manager_frontpage/general_view/view_index.jsp" %>'">
			</td>
		</tr>
	</table>
	
	<!-- ----------------------------��Ǭͼ�β�������(begin)----------------------------------- -->

	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
		<html:form action="/manager_frontpage/general_view/ViewLeadingAction" onsubmit="return dosubmit()">
		<INPUT type="hidden" id="orgId" name="orgId">
			<tr align="center">
				<td align="center" width="100%">
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<TR>
								<td width="20%">
									<br>
									&nbsp;&nbsp;&nbsp;ʱ��:
									<html:text property="repDate" size="10" styleClass="input-text" readonly="true" />
									<img src="../../image/calendar.gif" border="0" onclick="return showCalendar('repDate', 'y-mm-dd');">
						
								</td>
								<td width="20%">
									<br>
									&nbsp;&nbsp;&nbsp;����:
									<input type="text" id="selectedOrgName" readonly name="orgName" value="<bean:write  name="gs"  property="orgName"/>" size="10" style="width:200px;cursor:hand" onclick="return showTree()"
												Class="input-text" />
									<div id="orgTree"  
											style="left:316px;top:70px;width:200px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
											
											<script type="text/javascript">
												<bean:write  name="gs"  property="orgList" filter="false"/>
											    var tree= new ListTree("tree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
										      	tree.init();
										 	</script>
									
								</td>
								<td width="20%">
									<br>
									<input type="submit" name="measure" class="input-button" value="��ѯ">
								</td>
							</TR>
						</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<logic:present name="gs" scope="session">
		<!-- �����-->
		<logic:equal name="gs" property="naviGraph" value="cundaikuanqingkuang">
			<jsp:include page="cundaikuanqingkuang.jsp" />
		</logic:equal>
		<!--������֧-->
		<logic:equal name="gs" property="naviGraph" value="caiwushouzhi">
			<jsp:include page="caiwushouzhi.jsp" />
		</logic:equal>
		<!-- ӯ����-->
		<logic:equal name="gs" property="naviGraph" value="yingliqingkuang">
			<jsp:include page="yingliqingkuang.jsp" />
		</logic:equal>
		<!-- �ʲ���ծͳ��-->
		<logic:equal name="gs" property="naviGraph" value="zichanfuzhaizongti">
			<jsp:include page="zichanfuzhaizongti.jsp" />
		</logic:equal>
		<!-- ������-->
		<logic:equal name="gs" property="naviGraph" value="liudongxingqingkuang">
			<jsp:include page="liudongxingqingkuang.jsp" />
		</logic:equal>
		<!-- ����ƻ�ִ��-->
		<logic:equal name="gs" property="naviGraph" value="jingyingjihuazhixingqingkuang">
			<jsp:include page="jingyingjihuazhixingqingkuang.jsp" />
		</logic:equal>
		<!-- ��ҵͶ��-->
		<logic:equal name="gs" property="naviGraph" value="hangyetoufangqingkuang">
			<jsp:include page="hangyetoufangqingkuang.jsp" />
		</logic:equal>
		<!-- ҵ����-->
		<logic:equal name="gs" property="naviGraph" value="yewuliangqingkuang">
			<jsp:include page="yewuliangqingkuang.jsp" />
		</logic:equal>
		<!--ͬҵ�Ա�-->
		<logic:equal name="gs" property="naviGraph" value="tongyeduibi">
			<jsp:include page="tongyeduibi.jsp" />
		</logic:equal>
	</logic:present>

	<!-- ----------------------------��Ǭͼ�β�������(end)----------------------------------- -->
	<div id="waitDiv" style="display:none">
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>
		<br>

		<h4>
			<FONT color="#FF8000">���Ժ�......</FONT>
		</h4>

	</div>
	<br>
</body>
</html:html>

<script language=javascript ></script>