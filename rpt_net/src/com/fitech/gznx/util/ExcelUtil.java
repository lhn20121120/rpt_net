package com.fitech.gznx.util;

import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.UtilForExcelAndRaq;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFPbocReportDelegate;
import com.fitech.net.adapter.StrutsExcelData;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.util.ReportUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ExcelUtil {
	
	public static void writeDataExcel(Map map,HttpServletResponse response,String operatorId) throws Exception{
		String fileName=writeExcel(map,operatorId);
		if(fileName!=null){
			downLoadExcel(fileName, response);
			
			new File(fileName).delete();
			new File(fileName).getParentFile().delete();
		}
	}
	
	/**
	 * 将从数据库中查询的数据放入map中，并写入excel
	 * @param cellMap
	 * @return
	 * @throws Exception
	 */
	public static String writeExcel(Map cellMap,String operatorId) throws Exception
	{
		String path=(String)cellMap.get("path");
		String temp=Config.TEMP_DIR + operatorId + DateUtil.getToday("yyyyMMddHHmmss") + Config.FILESEPARATOR;
		/*创建目录*/
		String newpath=path.substring(path.lastIndexOf(Config.FILESEPARATOR)+1,path.length());
		File fl=new File(path);
		if(!fl.exists()){throw new Exception(path+"无此excel模版!");}
		File file = new File(new File(temp),newpath);
		if(!file.getParentFile().exists()){file.getParentFile().mkdirs();}
		FileUtil.copyFile(fl, file);
		HSSFWorkbook sourceWb = UtilForExcelAndRaq.writeExcelToData(cellMap);
		FileOutputStream stream = new FileOutputStream(file);
		
		sourceWb.write(stream);
		stream.flush();
		stream.close();
		
		return file.getPath();
	}
	
	public static void main(String[] args) {
		
		//Double a = new Double(0.817378*100);
//		Double a = Double.parseDouble("0.817378") * 100;
//		BigDecimal b= new BigDecimal(a);
//		System.out.println(b.setScale(4,BigDecimal.ROUND_HALF_UP));
		//String str = "0.817378".substring("0.817378".indexOf(".")+1);
//		System.out.println(Arrays.toString("0.00".split("\\.")));
//		System.out.println("7451690000000001".substring(0, 6));
		//System.out.println(subZeroAndDot("0.37994700000000003"));
//		0.379947
		//System.out.println(37.9947/100);
	}
	public static String subZeroAndDot(String s){   
        if(s.indexOf(".") > 0){   
            s = s.replaceAll("0+?$", "");//去掉多余的0   
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉   
        }   
        return s;   
    } 
	/**
	 * 下载excel
	 * @param fileName
	 * @param response
	 */
	public static void downLoadExcel(String fileName,HttpServletResponse response) {
		try {
			String newpath=fileName.substring(fileName.lastIndexOf(Config.FILESEPARATOR)+1,fileName.lastIndexOf(".xls"));
			String path = java.net.URLDecoder.decode(fileName, "utf-8");
			File file = new File(path);
			if (!file.exists()) {
				response.setContentType("text/html;charset=utf-8");
				System.out.println("指定的文件"+file.getPath()+"不存在!");
				return;
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + newpath+".xls");
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			OutputStream out = response.getOutputStream();
			long j=0;
			byte[] b=new byte[1024];
			while(j<file.length()){
				int k=bis.read(b, 0, 1024);
				j+=k;
				out.write(b, 0, k);
			}
			out.flush();
			out.close();
			bis.close();
			fis.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
