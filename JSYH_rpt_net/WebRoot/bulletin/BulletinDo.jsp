<%@ page language="java" pageEncoding="GB2312"%>
<jsp:directive.page import="java.sql.Connection"/>
<jsp:directive.page import="com.cbrc.smis.dao.DBConn"/>
<jsp:directive.page import="java.sql.ResultSet"/>
<%@ page import="com.jspsmart.upload.SmartUpload" %>
<%@ page import="com.jspsmart.upload.File" %>


<%@ page import="org.apache.commons.lang.SystemUtils" %>
<%@ page import="com.jspsmart.upload.Request" %>

<%

  	SmartUpload smartUpload=new SmartUpload();
    //初始化
    smartUpload.initialize(pageContext); 
    //设置上载的最大值
    smartUpload.setMaxFileSize(50 * 1024*1024);
    
    //如果是删除公告
    String type = request.getParameter("type");
    Request req = smartUpload.getRequest();
    //如果是新增或修改公告
    if(type == null)
    {   
   	  //上载文件
		try
		{
			smartUpload.upload();
			type= req.getParameter("type"); 	
		}
		catch(SecurityException se)
		{
			response.getWriter().print("<html><head><script language=\"javascript\">alert('上传文件不能大于50M!');");
			response.getWriter().print("window.location.href('BulletinList.jsp');");
			response.getWriter().print("</script></head></html>");
		}
	}
    
	
	

if(type!=null){
	DBConn dbconn=new DBConn();
	try{

		Connection conn=dbconn.beginTransaction().connection();
		if(type.equals("insert")){
			
		
	  
	   	//取得上载的文件
	  		File file = smartUpload.getFiles().getFile(0); 
			String bullTitle=req.getParameter("bullTitle");
			
			String bullState=req.getParameter("bullState");
			String bullContent=req.getParameter("bullContent");
			System.out.println(bullTitle);
			
			//if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==0){
     			//if(bullTitle!=null)bullTitle=new String(bullTitle.getBytes("iso-8859-1"), "gb2312");
     			//if(bullContent!=null)bullContent=new String(bullContent.getBytes("iso-8859-1"), "gb2312");
     		// }
			java.sql.PreparedStatement ps = null;
			String sql="";
			int id = -1;
			if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle")){
				ResultSet rs=conn.createStatement().executeQuery("select SEQ_REPORT_IN.nextval from dual");
				rs.next();
				id=rs.getInt(1);
				sql="insert into BULLETIN(BULL_ID,BULL_TITLE,BULL_CONTENT,BULL_STATE,ADD_TIME) "+
				"values("+id+",?,?,"+bullState+",?)";
				//System.out.println(sql);
			}

			if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("db2")){
				ResultSet rs=conn.createStatement().executeQuery("values nextval for SEQ_REPORT_IN");
				rs.next();
				id=rs.getInt(1);
				sql="insert into BULLETIN(BULL_ID,BULL_TITLE,BULL_CONTENT,BULL_STATE,ADD_TIME) "+
				"values("+id+",?,?,"+bullState+",?)";
			}
			if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sybase")){
				sql="insert into BULLETIN(BULL_TITLE,BULL_CONTENT,BULL_STATE,ADD_TIME) "+
				"values(?,?,"+bullState+",?)";
				//System.out.println(sql);
			}
			//如果上传文件
			if (!file.isMissing())
		    {
			   //取得上载的文件的文件名
			    String fileName		= file.getFileName();
			  //  if(fileName!=null)if(fileName!=null)fileName=new String(fileName.getBytes("iso-8859-1"), "gb2312");
			     //设定放在服务器上的文件名
			    String virtualName  = System.currentTimeMillis() + "." + file.getFileExt();
			    //设置服务器上的物理文件路径
			    String physicalPath = request.getRealPath("/bulliten_upload") + SystemUtils.FILE_SEPARATOR ;
			    	   
			   //System.out.println(physicalPath);
			   java.io.File f = new java.io.File(physicalPath);
			   if(!f.exists())
			   	f.mkdir();			  
			    //保存文件
			    file.saveAs(physicalPath + virtualName);
			  
			    //构建SQL 语句
			    if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sybase")){
				sql = "insert into BULLETIN(BULL_TITLE,BULL_CONTENT,BULL_STATE,ADD_TIME,UPLOAD_FILENAME,VIRTUAL_NAME) "+
				"values(?,?,"+bullState+",?,?,?)";
			}else{
			    sql = "insert into BULLETIN(BULL_ID,BULL_TITLE,BULL_CONTENT,BULL_STATE,ADD_TIME,UPLOAD_FILENAME,VIRTUAL_NAME) "+
				"values("+id+",?,?,"+bullState+",?,?,?)";
				}
				ps=conn.prepareStatement(sql);
			    //ps.setBinaryStream(4,fis,(int)tempFile.length());
			    ps.setString(4,fileName);
			    ps.setString(5,physicalPath + virtualName);
			    
			 }
			 else
			 {
			 	 ps=conn.prepareStatement(sql);
			 }
			 
			ps.setString(1,bullTitle);
			ps.setString(2,bullContent);
			ps.setString(3,com.cbrc.smis.util.FitechUtil.getDateString(new java.util.Date()));
			ps.execute();
			conn.commit();
		
		}
		
	if(type.equals("update")){		
		  
		   //取得上载的文件
		   File file = smartUpload.getFiles().getFile(0); 
			String bullTitle=req.getParameter("bullTitle");
			
			String bullState=req.getParameter("bullState");
			String bullContent=req.getParameter("bullContent");
			String bullId=req.getParameter("bullId");
			
			//if(com.cbrc.smis.common.Config.WEB_SERVER_TYPE==0){
     		//	if(bullTitle!=null)bullTitle=new String(bullTitle.getBytes("iso-8859-1"), "gb2312");
     		//  if(bullContent!=null)bullContent=new String(bullContent.getBytes("iso-8859-1"), "gb2312");
     		// }
     		
     		String sql="update  BULLETIN set BULL_TITLE=? ,BULL_CONTENT=?,BULL_STATE="+bullState+" where BULL_ID= "+bullId;
     		
     		java.sql.PreparedStatement ps= null;
			//如果上传文件
			if (!file.isMissing())
		    {
		    //System.out.println("delete begin");
		    	//删除原先的上传文件
		    	String findOld = "select VIRTUAL_NAME from BULLETIN where BULL_ID = " + bullId;
		    	ResultSet rs = conn.createStatement().executeQuery(findOld);
		    	if(rs.next())
		    	{
		    		String deleteFile = rs.getString("VIRTUAL_NAME");
		    		if(deleteFile != null)
		    		{
		    			java.io.File f = new java.io.File(deleteFile);
		    			if(f.exists())
		    				f.delete();
					}
		    	}
		    	if(rs != null)
		    	   	rs.close();
			   //取得上载的文件的文件名
			    String fileName		= file.getFileName();
			//    if(fileName!=null)if(fileName!=null)bullTitle=new String(fileName.getBytes("iso-8859-1"), "gb2312");
			    //设定放在服务器上的文件名
			    String virtualName  = System.currentTimeMillis() + "." + file.getFileExt();
			    //设置服务器上的物理文件路径
			    String physicalPath = request.getRealPath("/bulliten_upload") + SystemUtils.FILE_SEPARATOR ;
			    	   
			   //System.out.println(physicalPath);
			   //如果文件夹不存在建文件夹
			   java.io.File f = new java.io.File(physicalPath);
			   if(!f.exists())
			   	f.mkdir();			  
			    //保存文件
			    file.saveAs(physicalPath + virtualName);
			  
			    //构建SQL 语句
			    sql = "Update BULLETIN set BULL_TITLE=? ,BULL_CONTENT=?,BULL_STATE="+bullState+",UPLOAD_FILENAME=?,VIRTUAL_NAME = ? "
			       + " where BULL_ID= "+bullId;
				ps=conn.prepareStatement(sql);
			    //ps.setBinaryStream(4,fis,(int)tempFile.length());
			    ps.setString(3,fileName);
			    ps.setString(4,physicalPath + virtualName);
			    
			 }
			 else
			 {
			 	 ps=conn.prepareStatement(sql);
			 }
			
			ps.setString(1,bullTitle);
			ps.setString(2,bullContent);
			ps.execute();
			conn.commit();
		
		}
	if(type.equals("delete")){
		
		String bullId=request.getParameter("bullId");
		//删除原先的上传文件
		String findOld = "select VIRTUAL_NAME from BULLETIN where BULL_ID = " + bullId;
    	ResultSet rs = conn.createStatement().executeQuery(findOld);
    	if(rs.next())
    	{
    		String deleteFile = rs.getString("VIRTUAL_NAME");
    		if(deleteFile != null)
    		{
    			java.io.File f = new java.io.File(deleteFile);
    			if(f.exists())
    				f.delete();
			}
    	}
    	if(rs != null)
    		rs.close();		
	
		conn.createStatement().execute("delete from BULLETIN where BULL_ID="+bullId);

		conn.commit();
		}
			
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		dbconn.endTransaction(true);
		dbconn.closeSession();
		response.sendRedirect("BulletinList.jsp");
	}
}


%>
<html>
<head>
	<title>公告管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">

</head>
<body>

</body>
</html>
