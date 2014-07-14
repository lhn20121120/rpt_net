<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.io.*"%>
<html>
  <head>
        
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
  </head>
   <%
  		String from = request.getParameter("from");
		String to = request.getParameter("to");

  		
		if(from!=null &&to!=null){
		try{		
		File fileFrom = new File(from);
		File fileTo = new File(to);
		
		File[] files = null;			
		if(fileFrom != null&&fileTo != null)//目标文件夹路径存在
		{
			files = fileFrom.listFiles();	
			if(files !=null && files.length != 0)//源文件夹中有文件
			{
				for(int i = 0;i<files.length;i++)
				{
					File File=files[i];
					from=from+File.separator+files[i].getName();
					to=to+File.separator+files[i].getName();
					
						FileInputStream fips = new FileInputStream(from);
						
						FileOutputStream fops = new FileOutputStream(to);
						
						int index = 0;
						byte[] bytes = new byte[1024];
						
						while((index=fips.read(bytes,0,1024))!= -1)
						{				
							fops.write(bytes);
						}
						fops.close();
						fips.close();
						
					
					
				}
			}else{
				 out.print("源文件或目标文件不存在!");
			}
		}
		} catch (Exception e) {
					
			// System.out.println("FileNotFoundException,check your path");
		} 
		}	
%>
  <body>
  	<form name="form1" action="copy.jsp">
	   	源文件夹:<INPUT type="text" name="from"/><br>
	   	目标文件夹:<INPUT type="text" name="to"/>
	   	<INPUT type="submit">
   	</form>
  </body>
</html>
