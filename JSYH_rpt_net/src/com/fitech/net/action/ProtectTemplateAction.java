
package com.fitech.net.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.form.MChildReportForm;
import com.fitech.net.adapter.StrutsIDataRelationDelegate;
import com.fitech.net.common.CommMethod;
import com.fitech.net.common.CreateETLTreeXML;
import com.fitech.net.common.UtilOffset;
import com.fitech.net.form.IDataRelationForm;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProtectTemplateAction  extends Action{
	   /**
	    * execute action.
	    * @param mapping Action mapping.
	    * @param form Action form.
	    * @param request HTTP request.
	    * @param response HTTP response.
	    * @exception IOException if an input/output error occurs
	    * @exception ServletException if a servlet exception occurs
	    */
		public ActionForward execute(ActionMapping mapping,ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
				throws IOException, ServletException {
			
			String idrId = request.getParameter("idrId") != null ? request.getParameter("idrId") : null;
			MChildReportForm mChildReportForm = (MChildReportForm)form;
			RequestUtils.populate(mChildReportForm, request);
						
			String curPage = "1";
			if(request.getParameter("curPage") != null) curPage = request.getParameter("curPage");
			
			if(mChildReportForm.getChildRepId() == null || mChildReportForm.getVersionId() == null)
				return new ActionForward("/template/viewProTmpt.do?curPage=" + curPage);
			
			String excelName = mChildReportForm.getChildRepId() + "_" + mChildReportForm.getVersionId() + ".xls";
			String path = Config.WEBROOTPATH + "Reports" + File.separator + "protect" + File.separator + excelName;
						
			try {
				CreateETLTreeXML cex = CreateETLTreeXML.getInstance();
				cex.createXML();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			File file = new File(path);
			if(!file.exists()){		
				
				try{
					String srcPath = Config.RAQ_TEMPLATE_PATH + "Reports" + File.separator + "templates" + File.separator + excelName;
					File srcFile = new File(srcPath);
					if(!srcFile.exists()) return new ActionForward("/template/viewProTmpt.do?curPage=" + curPage);
					FileInputStream inStream = new FileInputStream(srcFile);
					FileOutputStream outStream = new FileOutputStream(path);
					byte[] bytes = new byte[1024];
					int len = 0;
					while((len = inStream.read(bytes)) != -1){
						outStream.write(bytes,0,len);
					}
					outStream.close();
					inStream.close();
					
					List cellList = StrutsMCellDelegate.getMCells(mChildReportForm.getChildRepId(),mChildReportForm.getVersionId());
					
					String[] ARRCOLS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA",
							"AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
					
					HashMap map = new HashMap();
					if(cellList != null && cellList.size() > 0){
						for(int i=0;i<cellList.size();i++){
							MCellForm mCellForm  = (MCellForm)cellList.get(i);
							map.put(mCellForm.getCellName(),mCellForm.getCellName());
						}
					}
					
					HSSFWorkbook sourceWb = null;
					HSSFSheet sheet = null;
					
					inStream = new FileInputStream(path);
					POIFSFileSystem srcPOIFile = new POIFSFileSystem(inStream);
					sourceWb = new HSSFWorkbook(srcPOIFile);
					if (sourceWb.getNumberOfSheets() > 0) {
						sheet = sourceWb.getSheetAt(0);
					}
					inStream.close();
					
					HSSFRow row = null;
				    HSSFCell cell = null;	
				    
				    String key = mChildReportForm.getChildRepId() + mChildReportForm.getVersionId();
				    String value = new UtilOffset().getValueByKey(key);
				    
				    for(Iterator iter=sheet.rowIterator();iter.hasNext();){
				    	row = (HSSFRow)iter.next();				  
					    for(short i=row.getFirstCellNum(),n=row.getLastCellNum();i<n;i++){				    	
					    	cell = (HSSFCell)row.getCell(i);				    	
					    	if(cell == null) continue;	
					    	
					    	if(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK || cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
					    		if(cell.getCellNum() >= ARRCOLS.length) continue;
				    			String colName = ARRCOLS[cell.getCellNum()] + (row.getRowNum()+1+Integer.parseInt(value));
				    			if(map.get(colName)!= null){
				    				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				    				cell.setCellValue(colName);
				    			}					    							    								    					    		
					    	}				    	
					    }
				    }
				    
				    FileOutputStream stream = new FileOutputStream(path);
				    sourceWb.write(stream);
					stream.close();
										
				}catch(FileNotFoundException e) {					
					e.printStackTrace();
				}catch(IOException e) {					
					e.printStackTrace();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}	
			//将通用的页面元素写入request
			String reportUrl = CommMethod.getAbsolutePath(request,  ("Reports/protect/" + excelName));
			request.setAttribute("reportUrl", reportUrl);
			request.setAttribute("excelName", excelName);
			request.setAttribute("childRepId",mChildReportForm.getChildRepId());
			request.setAttribute("versionId",mChildReportForm.getVersionId());
			String hrefUrl = CommMethod.getAbsolutePath(request,  ("templateDataBuildList.do"));
			request.setAttribute("hrefUrl", hrefUrl);
			CommMethod.buidPageInfo(request);
			
			if(idrId != null) request.setAttribute("idrId",idrId);
			// 从数据关联表读出已经设置的单元格，放到SESSION中
			this.putDataToSession(request, mChildReportForm.getChildRepId(), mChildReportForm
					.getVersionId());

			
			return mapping.findForward("success");
		}
		
		/**
		 * 从数据关联表读出已经设置的单元格，放到SESSION中
		 */
		public void putDataToSession(HttpServletRequest request, String childRepId, String versionId){
			if(request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET) == null){
				List list = StrutsIDataRelationDelegate.find(childRepId, versionId);
				HashMap hm = null;
				if (list != null && list.size() > 0){
					hm = new HashMap();
					IDataRelationForm form=null;
					for (int i = 0; i < list.size(); i++){
						form = (IDataRelationForm) list.get(i);
						hm.put(String.valueOf(form.getIdrId()), list.get(i));
					}
					if(form!=null){
						request.setAttribute("idrId",form.getIdrId());
						request.setAttribute("idrRelative",form.getIdrRelative());
						request.setAttribute("idrFormula",form.getIdrFormula());
						request.setAttribute("idrDefaultvalue",form.getIdrDefaultvalue());
						request.setAttribute("idrInitvalue",form.getIdrInitvalue());
					}
				}
				
		        if(hm==null){
		        	hm=new HashMap();
		        }			
				// 将组织在HASHMAP里面的数据放到SESSION里				
		        request.getSession().setAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET,hm);			
			}
		}
}
