package com.fitech.gznx.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfCodelib;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfPboccell;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AfPboccellDelegate;
import com.fitech.gznx.service.StrutsAFActuRepDelegate;
import com.fitech.gznx.service.StrutsCodeLibDelegate;

public class CreateRHReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
        	Operator operator = null; 
    		if(request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		String filePath = request.getParameter("filePath");
            String deleteFile = request.getParameter("deleteFile");
            String fileName = request.getParameter("fileName");

            File file = new File(filePath);
            if(!file.exists())
                throw new FileNotFoundException();
    		
    		String repInIdString = request.getParameter("repInIds") != null ? request.getParameter("repInIds") : "";
    		String reqorgId = request.getParameter("org");
    		String date = request.getParameter("date");
    	
    		if(StringUtil.isEmpty(repInIdString)){
    			return;
    		}
			String[] repFreqIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);
			HashMap map = new HashMap();
			List repFreqIdList = new ArrayList();
			for(int i=0;i<repFreqIds.length;i++){
				String repInId = repFreqIds[i].split(":")[2];
				if(StringUtil.isEmpty(repInId)){
					continue;
				}
				if(!map.containsKey(repInId)){
					map.put(repInId, repInId);
					repFreqIdList.add(repInId);
				}
			}
			if(repFreqIdList != null && repFreqIdList.size()>0){
				for(int t=0;t<repFreqIdList.size();t++){
					String reqfreqId = (String) repFreqIdList.get(t);
					
					// 人行机构代码未定
					String  rhOrgId = "";    	    			
	    			AfOrg aforginfo = AFOrgDelegate.getOrgInfo(reqorgId);
	    			String regionID = "";
	    			if(aforginfo.getRegionId() != null){
	    				regionID = String.valueOf(aforginfo.getRegionId().intValue());
	    			}
	    			if(StringUtil.isEmpty(regionID)){
	    				regionID = "4401000";
	    			}
	    			AfCodelib afcodelib = StrutsCodeLibDelegate.getCodeLib(
	    					com.fitech.gznx.common.Config.RHORGIDTYPE,com.fitech.gznx.common.Config.RHORGIDID);
	    			if(afcodelib != null && afcodelib.getCodeName()!=null){
	    				rhOrgId = afcodelib.getCodeName();
	    			}
	    			if(reqfreqId.equals("9")){
	    				ReportInForm reportInForm = AFReportDelegate.getReportIn(new Integer(repInIdString.split(Config.SPLIT_SYMBOL_COMMA)[0].split(":")[0]));

	            		String templateId = reportInForm.getChildRepId();
	            		String versionId = reportInForm.getVersionId();
	            		List feqList = StrutsAFActuRepDelegate.select(templateId,versionId);
	            		for(int i=0;i<feqList.size();i++){
	            			MActuRepForm reqform = (MActuRepForm) feqList.get(i);
	            			if(reqform!=null && reqform.getRepFreqId().intValue()!=9){
	            				reqfreqId = String.valueOf(reqform.getRepFreqId());
	            				break;
	            			}
	            		}
	    			}
	    			String rhrepfreqid = "";
	    			if(reqfreqId.equals("1")){
	    				rhrepfreqid = "4";
	    			}else if(reqfreqId.equals("2")){
	    				rhrepfreqid = "3";
	    			}else if(reqfreqId.equals("3")){
	    				rhrepfreqid = "1";
	    			}else if(reqfreqId.equals("4")){
	    				rhrepfreqid = "0";
	    			}else if(reqfreqId.equals("6")){
	    				rhrepfreqid = "7";
	    			} 
	    			StringBuffer filename =new  StringBuffer();
	    			filename.append(rhOrgId).append(regionID).append(date.replaceAll("-", "")).append(rhrepfreqid);

	    			// 头文件
	    			FileWriter ai = null;
	    			// 数据文件
	    			FileWriter aj = null;
	    			// 数据说明文件
	    			FileWriter ad = null;
	    			try {
//					ai = new FileWriter(srcFileName + File.separator +"AI"+filename+".IDX");
//					aj = new FileWriter(srcFileName + File.separator +"AJ"+filename+".DAT");
//					ad = new FileWriter(srcFileName + File.separator +"AD"+filename+".TXT");
					
					ad.write(date+"广州市联社数据说明文件");
	    			
	    			String[] repInIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);
	    			int inum =1;
	        		for(int i=0;i<repInIds.length;i++){
	        			String repInId = repInIds[i].split(":")[0];
	            		String orgId = repInIds[i].split(":")[1];
	            		String reqFpId = repInIds[i].split(":")[2];
	            		if(!reqfreqId.equals(reqFpId)){
	            			continue;
	            		}
	            		ReportInForm reportInForm = AFReportDelegate.getReportIn(new Integer(repInId));
	 
	            		StringBuffer aiindex = null;
	            		String templateId = reportInForm.getChildRepId();
	            		String versionId = reportInForm.getVersionId();
	            		List pboc = AfPboccellDelegate.getAFPbocCell(templateId,versionId);
	            		if(pboc != null && pboc.size()>0){
	            		for(int j=0;j<pboc.size();j++){
	            			AfPboccell cell = (AfPboccell) pboc.get(i);
	            			aiindex = new StringBuffer();
	            			aiindex.append("I").append(getfullName(String.valueOf(inum),5))
	            			.append("|").append(templateId).append("|").append(rhOrgId)
	            			.append("|").append(regionID).append("|").append(cell.getDataType())
	            			.append("|").append(cell.getCurId()).append("|").append(cell.getDanweiId())
	            			.append("|");
	            			boolean result = AfPboccellDelegate.isAFPbocCell(reportInForm,cell.getId().getColId());
	            			if(result){
	            				aiindex.append("1");
	            			}else{
	            				aiindex.append("0");
	            			}
	            			aiindex.append("|").append(cell.getPsuziType());
	            			ai.write(aiindex.toString());
	            			if(result){
	            				 List cellList = AfPboccellDelegate.getAFPbocCellList(reportInForm,cell.getId().getColId());
	            				 if(cellList != null && cellList.size()>0){
	            					 for(int cx=0;cx<cellList.size();cx++){
	            						 AfCellinfo cellinfo = (AfCellinfo) cellList.get(cx);            						 
	            						 String cellData = AfPboccellDelegate.getAFPbocCellData(reportInForm,cellinfo.getCellId());
	            						 if(!StringUtil.isEmpty(cellData)){
	            							 StringBuffer ajstr = new StringBuffer();
	            							 ajstr.append("I").append(getfullName(String.valueOf(inum),5))
	            		            			.append("|").append(cellinfo.getCellPid()).append("|").append(cellData);
	            							 aj.write(ajstr.toString());
	            						 }
	            					 }
	            				 }
	            			}
	            			inum++;
	            		}
	            		}
	            	}
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}finally{
	    				ai.close();
	    				aj.close();
	    				ad.close();
	    			}
					
				}
			}

			response.reset();
			response.setContentType("application/octet-stream;charset=gb2312"); 
			response.addHeader("Content-Disposition","attachment; filename="+ URLEncoder.encode(fileName,"gb2312"));
			response.setHeader("Accept-ranges", "bytes");
									
			OutputStream os = response.getOutputStream();
			byte b[] = new byte[500];
			    
			long fileLength = file.length();
			String length = String.valueOf(fileLength);
			response.setHeader("Content-length", length);
			FileInputStream in = new FileInputStream(file);
			for(int n = 0; (n = in.read(b)) != -1;)
			    os.write(b, 0, n);
			
			in.close();
			os.close();

			
		}catch (Exception e){
            e.printStackTrace();
            ErrorOutPut(response);
		}

	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request,response);
	}
    
    private String getfullName(String fname,int num){
    	StringBuffer ss = new StringBuffer();
    	if(num>fname.length()){
    		
    	for(int i=0;i<num-fname.length();i++){
    		ss.append("0");
    	}
    	
    	}
    	ss.append(fname);
    	return ss.toString();
    }

	/**
     * 错误输出
     * @param response
     */
	private void ErrorOutPut(HttpServletResponse response)
    {
        response.reset();
        response.setContentType("text/html;charset=GB2312");
        PrintWriter out = null;
        try 
        {
            out = response.getWriter();
        } 
        catch (IOException e1) 
        {
            e1.printStackTrace();
        }
        out.println("<font color=\"blue\">没有需要导出的数据文件!</font>");
        out.close();     
    }
}
