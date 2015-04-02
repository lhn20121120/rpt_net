<%@ page contentType="text/html;charset=gb2312"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.*,java.io.*,com.fitech.gznx.form.CustomViewForm,java.util.List,org.apache.struts.util.LabelValueBean,com.fitech.gznx.common.*"%>
<%
	CustomViewForm customViewForm = new CustomViewForm();
	if (request.getAttribute("form") != null) {
		customViewForm = (CustomViewForm) request.getAttribute("form");
	}

	String orgfilename = (String)request.getAttribute("xmlorgname");

%>
<html:html locale="true">
<head>
	<html:base />
	<title>自定义查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/globalStyle.css" rel="stylesheet" type="text/css">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<link href="../../css/calendar-blue.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../calendar/calendar.js"></script>
	<script type="text/javascript" src="../../calendar/calendar-cn.js"></script>
	<script language="javascript" src="../../calendar/calendar-func.js"></script>
	<script language="javascript" src="../../js/func.js"></script>

	<script src="../../js/Tree_for_xml.js"></script>

	<style rel="STYLESHEET" type="text/css">
			.defaultTreeTable{margin : 0;padding : 0;border : 0;}
			.containerTableStyle { overflow : auto;}
			.standartTreeRow{	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 	font-size : 14px; -moz-user-select: none; }
			.selectedTreeRow{ background-color : navy; color:white; font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 		font-size : 14x;  -moz-user-select: none;  }
			.standartTreeImage{ width:14x; height:1px;  overflow:hidden; border:0; padding:0; margin:0; }			
			.hiddenRow { width:1px;   overflow:hidden;  }
			.dragSpanDiv{ 	font-size : 12px; 	border: thin solid 1 1 1 1; }
</style>

<script language="javascript">
		 function dosubmit(){  

		 	var curId = document.getElementById('curId');
		 	var repFreqId = document.getElementById('repFreqId');
		 	var startDate = document.getElementById('startDate');
		 	var endDate = document.getElementById('endDate');
		 	
		 	if(submitOnRight()==""){
		 		alert("请选择指标！");
				return false;	
		 	}

		 	if(curId.value==""){
		 		alert("请选择币种！");
				return false;	
		 	}
		 	if(repFreqId.value==""){
		 		alert("请选择频度！");
				return false;	
		 	}
		 	if(startDate.value==""){
		 		alert("请选择起始时间！");
				return false;	
		 	}
		 	if(endDate.value==""){
		 		alert("请选择结束时间！");
				return false;	
		 	}
			var checkId=tree2.getAllChecked();	
			if(checkId.replace(/(^[\s]*)|([\s]*$)/g, "")==''){
				alert("请选择报表的范围!");
				return false;
			}
			var templateId = document.getElementById('templateId');
			var versionId = document.getElementById('versionId');
		 	
			window.open("<%=request.getContextPath()%>/viewCustomAFReportDetail.do?forwardflg=1&orgList=" + checkId+"&templateId="+templateId.value+"&versionId="+versionId.value+"&curId="+curId.value+"&repFreqId="+repFreqId.value+"&startDate="+startDate.value+"&endDate="+endDate.value+"&meaStr="+submitOnRight()); 
			 
	     }

	
		function leftselect(){
	    
	       var leftList = document.getElementById('leftList');
	       
	       var rightList = document.getElementById('rightList');
	       
	       var idLeft = leftList.selectedIndex;  
	       
	       while(idLeft>-1){
	       
	       	   var isSelected = false;
	           
	           var off = 0;
	           
	           var size = rightList.options.length;
	           
	           for(off;off<size;off++){
	     
	               if(rightList.options[off].value == leftList.options[idLeft].value){
	               　    isSelected = true;
	                  break;
	               }
	           
	           }
	           if(!isSelected){
	           
	               leftList.options[idLeft].style.color ="red";
	               
	               var newOption = new Option(leftList.options[idLeft].text,leftList.options[idLeft].value);
	           
	               rightList.options[rightList.length]=newOption;
	           }
	           leftList.options[idLeft].selected = false;
	           
	           rightList = document.getElementById('rightList');   //更新右边列表
	           
	           idLeft = leftList.selectedIndex;                    //更新选中序号
	    
	       }
	      
	     
	    }
	    
	    function selectXM1(){
	   	 
	    	var leftList = document.getElementById('leftList');
	    	var selectMXList = document.getElementById('selectMXList');
	    	var selectXX = document.getElementById('selectXM').value;
	    	
	    	if(selectXX !=''){
	    		var len = leftList.options.length;
	    		for(var i=0; i<len; i++){ 
	    			var selectName = leftList.options[i].text;
	    			if(!validateff(selectName,selectXX)){
		    			var newOption = new Option(leftList.options[i].text,leftList.options[i].value);
		           
						selectMXList.options[selectMXList.length]=newOption;
	    				leftList.options[i]=null;
	    				i--;	    				
	    			}
				}
				
				len = selectMXList.options.length;
	    		for(var i=0; i<len; i++){ 
	    			var selectName = selectMXList.options[i].text;
	    			if(validateff(selectName,selectXX)){
		    			var newOption = new Option(selectMXList.options[i].text,selectMXList.options[i].value);
		           
						leftList.options[leftList.length]=newOption;
	    				selectMXList.options[i]=null;
	    				i--;	    				
	    			}
				}
				
			
	    	} else {
	    		
	    		var len1 = selectMXList.options.length;
	 
	    		for(var j=0; j<len1; j++){	    		   		
	    			var newOption = new Option(selectMXList.options[j].text,selectMXList.options[j].value);
	           
					leftList.options[leftList.length]=newOption;
    				selectMXList.options[j]=null;
    				j--;
				}
	    	
	    	}
	    }
	    
	    function validateff(selectName,selectXX){
	   
	    	if(selectName.indexOf(selectXX)>-1){
	    		return true;
	    	}
	    	return false;
	    }
	    
	    function rightselect(){
	    
	        var leftList = document.getElementById('leftList');
	       
	        var rightList = document.getElementById('rightList');
	        
	        var  size = leftList.options.length;
	      	
	      	var idRight = rightList.selectedIndex;  
	       
	        while(idRight>-1){
	       
	            var off = 0;
	          
	            for(off;off<size;off++){
	          
	                if(leftList.options[off].value==rightList.options[idRight].value){
	              
	                    leftList.options[off].style.color ="black";
	                  
	                    break;
	                  
	                }
	          
	            }
	           rightList.options[idRight]=null;
	       	   
	       	   idRight = rightList.selectedIndex;
	  	   }
	  	  
	  	   
	    
	    }
	    
	   
	    
	    function leftAll(){　　//该方法用于对左边的报表列表框全选
	    
	        var leftList = document.getElementById('leftList');
	        
	        var i = 0;
	        
	        while(i<leftList.length){
	        
	            leftList.options[i].selected=true;
	            
	            i++;
	            
	        }
	    
	    }
	    function submitOnRight(){  //该方法用于将右边的列表转化为ID串 逗号分隔
	    
	        var rightList = document.getElementById('rightList');
	        var meaStr='';
	        var i = 0;
	        
	      	while(i<rightList.length){
		        
		       if (i == 0){
					meaStr += rightList.options[i].value;
					}
				else{
					meaStr +=  "," + rightList.options[i].value;
					}
		          i++; 
	        }
	       return meaStr;
		
	    }
	   
	   function submitOnRightTwo(){  //该方法用于将右边的列表转化为ID串 zx字符分隔
	    
	        var rightList = document.getElementById('rightList');
	        var meaStr2='';
	        var i = 0;
	        
	      	while(i<rightList.length){
		        
		       if (i == 0){
					meaStr2 +="xy"+ rightList.options[i].value+"xy";
					}
				else{
					meaStr2 += "zxxy"+ rightList.options[i].value +"xy" ;
					}
		          i++; 
	        }
	       return meaStr2;
		
	    }
	    
		function _back(){			   	 
			 window.location="<%=request.getContextPath()%>/customViewAFReport.do?<%=(String)request.getAttribute("backQry")%>";
		}
	</script>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<form action="<%=request.getContextPath()%>/viewCustomAFReportDetail.do"
		method="post" >

		<table width="90%" border="0" align="center" cellpadding="4" cellspacing="0">
			<tr>
				<td height="30" colspan="2">
					当前位置 >> 报表查询 >> 灵活查询
				<td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" align="center" cellpadding="4"
						cellspacing="1" class="tbcolor">
						<tr class="titletab">
							<th align="center">
								自定义查询
							</th>
						</tr>
						<tr>
							<td bgcolor="#ffffff">
								<table width="100%" border="0" align="center" cellpadding="4"
									cellspacing="1">
									<INPUT type="hidden" id="meaStr" name="meaStr">
									<INPUT type="hidden" id="orgList" name="orgList">
									<INPUT type="hidden" id="templateId" name="templateId"
										value="<bean:write name='form' property='templateId'/>">
									<INPUT type="hidden" id="orgId" name="orgId">
									<INPUT type="hidden" id="versionId" name="versionId"
										value="<bean:write name='form' property='versionId'/>">
									<INPUT type="hidden" id="templateName" name="templateName"
										value="<bean:write name='form' property='templateName'/>">
									<INPUT type="hidden" id="forwardflg" name="forwardflg" value="1">
									<tr>
										<td align="left" bgcolor="#ffffff" width="30%" colspan="4">
											选择项目名：<input type="text" id="selectXM"  class="input-text" onclick="selectXM1();">
										</td>
										
									</tr>
									<tr>
										<td align="center" bgcolor="#ffffff" width="80%" colspan="4">
											<TABLE border="0" width="100%" align="center" cellpadding="0"
												cellspacing="0">
												<TR>
													<TD align="center" valign="middle" width="35%">
														<select Id="leftList" name="leftList" multiple="multiple"
															size="16" style="width: 80%">
															<%
																List list = customViewForm.getCellNameList();
																	for (int i = 0; i < list.size(); i++) {
																		LabelValueBean lb = (LabelValueBean) list.get(i);
																		String measureId = lb.getLabel();
																		String measureName = lb.getValue();
															%>
															<option value="<%=measureId%>">
																<%=measureName%>
															</option>
															<%
																}
															%>
														</select>
													</TD>
													<TD  style="display:none">
														<select multiple="multiple" size="16" style="width: 80%"
															Id="selectMXList">
														</select>
													</TD>
													<TD align="center" valign="middle" width="10%">

														<p>
															<input class="input-button" type="button" name="add"
																value="添加---->" onClick="leftselect()">
														</p>
														<p>
															<input class="input-button" type="button" name="del"
																value="<----删除" onClick="rightselect()">
														</p>

														<p>
															<input type="button" class="input-button" value="全　 　选"
																onClick="leftAll()" />
														</p>
													</TD>
													<TD align="center" valign="middle" width="35%">
														<select multiple="multiple" size="16" style="width: 80%"
															Id="rightList">
														</select>
													</TD>
												</TR>
											</TABLE>
										</td>
									</tr>
									<tr>
										<td align="center" bgcolor="#ffffff" width="80%" colspan="4">
											<TABLE border="0" width="100%" align="center" cellpadding="0"
												cellspacing="0">
												<tr>
													<td align="left" bgcolor="#ffffff" width="30%" colspan="4">
														选择机构：
													</td>
												</tr>
												<tr>
													<td align="left" bgcolor="#ffffff" width="4.5%" valign="top">
													</td>
													<td align="left" bgcolor="#ffffff" width="54%" valign="top">
														<div id="treeboxbox_tree2" style="width: 63%; height: 200;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
													</td>
													<td width="40%">
														<table align="center">
															<tr>
																<td align="left" bgcolor="#ffffff" width="15%" valign="top">
																	选择币种：
																</td>
																<td align="left" bgcolor="#ffffff" width="30%" valign="top">
																	<select Id="curId" name="curId">

																		<%
																			List list1 = customViewForm.getCurList();
																				for (int i = 0; i < list1.size(); i++) {
																					LabelValueBean lb = (LabelValueBean) list1.get(i);
																					String curId = lb.getLabel();
																					String ccyName = lb.getValue();
																		%>
																		<option value="<%=curId%>">
																			<%=ccyName%>
																		</option>
																		<%
																			}
																		%>
																	</select>
																</td>
															</tr>
															<tr>
																<td align="left" bgcolor="#ffffff" width="15%">
																	选择频度：
																</td>
																<td align="left" bgcolor="#ffffff" width="30%">
																	<select Id="repFreqId" name="repFreqId">
																		<%
																			List list2 = customViewForm.getRepFreqList();
																				for (int i = 0; i < list2.size(); i++) {
																					LabelValueBean lb = (LabelValueBean) list2.get(i);
																					String repFreqId = lb.getLabel();
																					String freqName = lb.getValue();
																		%>
																		<option value="<%=repFreqId%>">
																			<%=freqName%>
																		</option>
																		<%
																			}
																		%>
																	</select>
																</td>
															</tr>
															<tr>
																<td align="left" bgcolor="#ffffff" width="15%">
																	起始时间:
																</td>
																<td align="left" width="30%">
																	<input type="text" id="startDate" name="startDate" size="10"
																		class="input-text" readonly="true">
																	<img src="../../image/calendar.gif" border="0"
																		onclick="return showCalendar('startDate', 'y-mm-dd');">
																</td>
															</tr>
															<tr>
																<td align="left" bgcolor="#ffffff" width="15%">
																	结束时间:
																</td>
																<td align="left" width="30%">
																	<input type="text" id="endDate" name="endDate" size="10"
																		class="input-text" readonly="true">
																	<img src="../../image/calendar.gif" border="0"
																		onclick="return showCalendar('endDate', 'y-mm-dd');">
																</td>
															</tr>
															<br>
															<tr>
																<td colspan="4" bgcolor="#ffffff" align="center">
																	<input type="button" value="查询" Class="input-button" 
																		onclick="dosubmit()" />
																	&nbsp;&nbsp;
																	<INPUT type="button" value="返回" Class="input-button"
																		onclick="_back()" />
																</TD>
															</tr>
														</table>
													</td>

												</tr>

											</table>

										</td>
									</tr>
								</table>

							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>					
	</form>
	<script>
		tree2=new dhtmlXTreeObject("treeboxbox_tree2","100%","100%",0);
		tree2.setImagePath("../../image/treeImgs/");
		tree2.enableCheckBoxes(1);
		tree2.enableThreeStateCheckboxes(true);
		tree2.loadXML("<%=request.getContextPath()%>/xml/<%=orgfilename%>");	
					
	</script>
</body>
</html:html>
