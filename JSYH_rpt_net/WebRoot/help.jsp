<%
	String path =com.cbrc.smis.common.Config.WEBROOTPATH+"help.zip";
	String name ="HELP.zip";

	response.setContentType("unknown");
	response.addHeader("Content-Disposition", "filename=\""+name+"\"");
	try{
		java.io.OutputStream os = response.getOutputStream();
		java.io.FileInputStream fis = new java.io.FileInputStream(path);

		byte[] b = new byte[1024];
		int i = 0;

		while ( (i = fis.read(b)) > 0 ) {
			os.write(b, 0, i);
		}
		os.flush();
		fis.close();

		os.close();
	}catch ( Exception e ){
		//e.printStackTrace();
	}
%>