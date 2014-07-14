package com.cbrc.smis.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbrc.smis.util.FitechUtil;
/**
 * 读取报表的XML内容，并在IE中显示
 * 
 * @author rds
 * @serialData 2005-12-18
 */
public class ReadPDFReport extends HttpServlet {
	/**
	 * doGet方法
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 * @exception IOException,ServletException
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		Integer repInId=null;
		
		if(request.getParameter("repInId")!=null){ 
			repInId=Integer.valueOf((String)request.getParameter("repInId"));
		}
		
		InputStream in=null;
		try{
			in=FitechUtil.readRepInXML(repInId);
		}catch(Exception e){
			e.printStackTrace();
			in=null;
		}
		
		/*String xmlFileName=null;
		try{
			xmlFileName=FitechUtil.readRepInXML(repInId);
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
		if(in!=null){
			String xdpHeader=FitechUtil.getXDPHeader();
			String xdpTailer=FitechUtil.getXDPTailer();
			
			response.setContentType("application/vnd.adobe.xdp+xml");
			//response.setContentLength(in.available() + xdpHeader.getBytes().length + xdpTailer.getBytes().length);
				
			//response.setHeader("Content-Disposition", "inline;filename=somepdf.pdf"); 
			//response.setHeader("Content-disposition","attachment;filename=foobar.xdp");
			//response.setContentLength(content.getBytes().length);
			
			byte[] CHARA=new String("<?").getBytes();
			byte[] CHARAEND=new String("?>").getBytes();
			
			byte[] line=new byte[in.available()];
			int preLen=0,lastLen=38;
			
			if(in.read(line)!=-1){
				int i=0;
				for(;i<line.length;i++){
					if(line[i]==CHARA[0] && (i<line.length-1 && line[i+1]==CHARA[1])){
						preLen=i;
						break;
					}
				}
				for(i=0;i<line.length;i++){
					if(line[i]==CHARAEND[0] && (i<line.length-1 && line[i+1]==CHARAEND[1])){
						lastLen=i+2;
						break;
					}
				}
				response.setContentLength((line.length-preLen) + xdpHeader.getBytes().length + xdpTailer.getBytes().length);
				
				BufferedOutputStream out=new BufferedOutputStream(response.getOutputStream());
				out.write(line,preLen,lastLen);
				out.write(xdpHeader.getBytes());
				
				out.write(line,lastLen+preLen,line.length-lastLen-preLen);
				out.write(xdpTailer.getBytes());
				
				out.flush();
				out.close();
			}			
			
			//response.resetBuffer();

		}else{
			error(response);
		}
	}	
		
	/**
	 * doPost方法
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return void
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		doGet(request,response);
	}
	
	/**
	 * 错误处理
	 * 
	 * @param response HttpServletResponse
	 * @return void
	 * @exception IOException,ServletException
	 */
	private void error(HttpServletResponse response) throws IOException,ServletException{
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out=response.getWriter();
		out.println("读取报表数据失败!");
	}
}
