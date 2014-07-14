<%@ page language="java" import="com.cbrc.smis.util.FitechMessages" pageEncoding="GB2312"%>
<%@ page import="java.util.*"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<jsp:useBean id="collect" class="com.fitech.net.collect.util.ExecuteCollect"></jsp:useBean>
<%
	String isMulti = request.getParameter("select_collect_type");			
	String year = request.getParameter("year");
	String month = request.getParameter("month");
	boolean flag = false;
	boolean fullFlag = false;
	FitechMessages messages = new FitechMessages();
		if (isMulti != null) {
			if (isMulti.equals("multi")) {				
				Operator operator1 = (Operator) session.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				    int m=0;
				    String parameterList="";
					while(m>=0)
				   {
					 String parameters = request.getParameter("select_data_collect_id"+m);		
					 if(parameters!=null && !parameters.equals("")){
					     m++;
					     parameterList+=parameters;
					 }else{
					 	break;
					 }
					}
				parameterList = parameterList.substring(0, parameterList.length() - 1);					
				String parArr[] = parameterList.split("#");
				String strArr[]=null;
				for (int i = 0; i < parArr.length; i++) {
					strArr = parArr[i].split(",");
					String childRepId =strArr[0];
					String versionId = strArr[1];
					String reportName ="";
					Integer dataRangeId =new Integer(strArr[2]);
					String dataRangeDesc = "";
					String curId = strArr[3];
					//boolean bool = collect.collectReport(childRepId,versionId,reportName,
					//			dataRangeId, dataRangeDesc,Integer.parseInt(year),Integer.parseInt(month), operator1,new Integer(curId));
					boolean bool = collect.collectReports(childRepId,versionId,reportName,
						dataRangeId, dataRangeDesc,Integer.parseInt(year),Integer.parseInt(month), operator1,new Integer(curId));
					if(bool == true) flag = true;				
				}
			}
		}else{
			String childRepId=request.getParameter("childRepId");
			String versionId=request.getParameter("versionId");
			String reportName=request.getParameter("reportName");
			String dataRangeId=request.getParameter("dataRangeId");
			String dataRangeDesc=request.getParameter("dataRangeDesc");
			String curIdStr=request.getParameter("curId");
			
			//String term=request.getParameter("term");
			String type=request.getParameter("type");
			Operator operator=(Operator)session.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			List records=(List)session.getAttribute("Records");
		
			//汇总单个报表
			if(type.equals("one")){
				if(childRepId!=null&&versionId!=null&&reportName!=null&&
						dataRangeId!=null&&dataRangeDesc!=null&&year!=null&&month!=null&&curIdStr!=null){
//2007-09-12 修改			if(collect.collectReport(childRepId,versionId,reportName,new Integer(dataRangeId),dataRangeDesc,

					if(collect.collectReports(childRepId,versionId,reportName,new Integer(dataRangeId),dataRangeDesc,
						Integer.parseInt(year),Integer.parseInt(month),operator,new Integer(curIdStr))){
						
						messages.add("数据汇总成功！");	
					
					}else{
						messages.add("数据汇总失败！");
					};
				}else{
					messages.add("数据汇总成功！");	
				}
			}
			//汇总所有报表
			if(type.equals("all")&&year!=null&&month!=null){
				try{
				  // collect.execute_DB2Excel(records,Integer.parseInt(year),Integer.parseInt(month),operator);
				    if(collect.collectAllReport(records,Integer.parseInt(year),Integer.parseInt(month),operator))
				    	fullFlag = true;
				    else
				    	messages.add("汇总全部数据失败！");
				}catch(Exception e){
				   e.printStackTrace();
				   messages.add("汇总全部数据失败！");
				}
			}	
		}
		
		if(flag == true) messages.add("完成数据汇总！");
		if(fullFlag == true) messages.add("完成所有上报数据汇总！");
		if(messages.getMessages() != null && messages.getMessages().size() > 0)
				   	  request.setAttribute("Message",messages);
		RequestDispatcher a=request.getRequestDispatcher("/viewCollectData.do?year="+year+"&term="+month);
		a.forward(request,response);		
%>
