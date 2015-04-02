<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
				<script type="text/javascript">
				 
				function turntoPage(obj,inputName){
							     var pageNo=$("#form1_pageNo").val();
							     if(pageNo==""){
							    	 alert("请先输入页码");
							    	 return;
							    	 }
							     var regu = "^[0-9]+$";//验证是否为正整数
							     var re = new RegExp(regu);
							     if (pageNo.search(re) != - 1) { 
							        $("#"+obj+" input[name='"+inputName+"']").val(pageNo);
							        $("#"+obj).submit();  
							     }
							     else {
							    	 alert("请输入合法页码！");
							         return;
							     }
							 	 
							     
										    }
							    function turntoFirst(obj,inputName){
								    var pageNo=$("#"+obj+" input[name='"+inputName+"']").val();
								    if(pageNo=='1'){
								    	 alert("当前已经是首页！");
								         return;
									    }
								    $("#"+obj+" input[name='"+inputName+"']").val(pageNo);
								   
								    $("#"+obj).submit();  

								    }
							    function turntoLast(obj,inputName){
								    var pageNo=$("#"+obj+" input[name='currentPage']").val();
								    var pageCount=$("#"+obj+" input[name='pageCount']").val();
								    if(pageNo==pageCount){
								    	 alert("当前已经是最后一页！");
								         return;
									    }
								    $("#"+obj+" input[name='"+inputName+"']").val(pageNo);
								   
								    $("#"+obj).submit();  

								    }
							    function up(obj,inputName){
							    	 var pageNo=$("#"+obj+" input[name='currentPage']").val();
									    var pageCount=$("#"+obj+" input[name='pageCount']").val();
							    	  if(pageNo=="1"){
							    	  alert("已经是第一页");
							    	  return;
							    	  }else{
							    	pageNo=parseInt(pageNo)-1;
							    	 $("#"+obj+" input[name='"+inputName+"']").val(pageNo);
									   
									    $("#"+obj).submit();  
							    	  }
							    	  }
							    	
							    function down(obj,inputName){
							    	var pageNo=$("#"+obj+" input[name='currentPage']").val();
								    var pageCount=$("#"+obj+" input[name='pageCount']").val();
							    	   if(pageNo==pageCount){
							    	  alert("已经是最后一页");
							    	  return;
							    	  }else{
							     pageNo=parseInt(pageNo)+1;
							     $("#"+obj+" input[name='"+inputName+"']").val(pageNo);
								   
								    $("#"+obj).submit();  
							    	 
							    	  }
							    	  }
				 


						


</script>