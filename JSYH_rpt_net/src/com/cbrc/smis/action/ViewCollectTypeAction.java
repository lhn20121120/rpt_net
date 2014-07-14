package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.adapter.StrutsReportInRelaseETLDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.util.FitechEXCELReport;
import com.cbrc.smis.util.FitechException;

import excelToHTML.cell.Cell;
import excelToHTML.engine.ToHTMLEngine;
import excelToHTML.usermodel.ReportDefine;



/**
 * 对Excel的关联关系做维护
 * @author zyl_xh
 *
 */
public class ViewCollectTypeAction  extends Action{
	private FitechException log=new FitechException(AddYCBHAction.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
//		String filePath = "";
		try {
			String childID=request.getParameter("childRepId");
			String versionID=request.getParameter("versionId");
		
			/*
			 *  取得MChildReport中的对应的文件名
			 */
			ReportDefine rd=new ReportDefine(request.getSession().getServletContext().getRealPath("/Reports/templates/"+childID+"_"+versionID+".xls"));
			List list=rd.getCellSet();
			List result=StrutsMCellDelegate.getCells(childID,versionID);
			if(result==null){
				// System.out.println("结果是空");
			}
			int rowMax=0;
			int colMax=0;
			int dataMove=FitechEXCELReport.getOffsetRows(childID,versionID);
			rowMax=rd.getLastRowNum();
			colMax=rd.getLastColNum();
			/*
			 * 	得到需要的Excel中的有效字段
			 */
			for(int i=0;i<result.size();i++){
				MCellForm mcell=(MCellForm)result.get(i);
				if(!mcell.getDataType().equals(Config.NUMBER_CELL_TYPE))
					continue;
				String col=mcell.getColId();
				Integer row=new Integer(mcell.getRowId().intValue()-dataMove);
				/*
				 * 	对字段做遍历插入单元格中
				 */
				for(int j=0;j<list.size();j++){
					Cell cell=(Cell)list.get(j);
					if(cell.getRowNum()!=row.intValue())
						continue;
					int colInt=0;
					
					colInt=StringToInt(col)-dataMove;
					if(cell.getColNum()!=colInt)
						continue;
					
					int temp=0;
					if(mcell.getCollectType()!=null){
						temp=mcell.getCollectType().intValue();
					}
					else{
						temp=0;
					}
					/*
					 * 	插入select
					 */
					String outPut="<select id="+colInt+"_"+row+"_"+" name=state_"+col+"_"+row+" class=select_150 onchange=\"parent.changeSelect('"+colInt+"_"+row+"_"+"')\">";
					String outPut0="<option value="+Config.COLLECT_TYPE_NO_COLLECT.intValue()+">不汇总</option>";
					String outPut1="<option value="+Config.COLLECT_TYPE_SUM.intValue()+">累加</option>";
					String outPut2="<option value="+Config.COLLECT_TYPE_AVG.intValue()+">取平均</option>";
					String outPut3="<option value="+Config.COLLECT_TYPE_MAX.intValue()+">最大值</option>";
					String outPut4="<option value="+Config.COLLECT_TYPE_MIN.intValue()+">最小值</option>";
					if(temp==Config.COLLECT_TYPE_NO_COLLECT.intValue()){
						outPut0="<option value="+Config.COLLECT_TYPE_NO_COLLECT.intValue()+" selected>不汇总</option>";
					}
					else if(temp==Config.COLLECT_TYPE_SUM.intValue()){
						outPut1="<option value="+Config.COLLECT_TYPE_SUM.intValue()+" selected>累加</option>";
					}
					else if(temp==Config.COLLECT_TYPE_AVG.intValue()){
						outPut2="<option value="+Config.COLLECT_TYPE_AVG.intValue()+" selected>取平均</option>";
					}
					else if(temp==Config.COLLECT_TYPE_MAX.intValue()){
						outPut3="<option value="+Config.COLLECT_TYPE_MAX.intValue()+" selected>最大值</option>";
					}
					else if(temp==Config.COLLECT_TYPE_MIN.intValue()){
						outPut4="<option value="+Config.COLLECT_TYPE_MIN.intValue()+" selected>最小值</option>";
					}
					outPut=outPut+outPut0+outPut1+outPut2+outPut3+outPut4+"</select>";
					cell.setCellValue(outPut);
					list.set(j,cell);
				}
			}
			
			/*
			 * 	做成html给jsp调用
			 */

			String fileName=System.currentTimeMillis()+".html";
			(new ToHTMLEngine()).generateHTML(rd,Config.TEMP_DIR+Config.FILESEPARATOR+fileName);
			
			/*
			 * 	把需要的条件放入request中提高给jsp调用
			 */
			request.setAttribute("colMax",new Integer(colMax));
			request.setAttribute("rowMax",new Integer(rowMax));
			request.setAttribute("childRepId",childID);
			request.setAttribute("versionId",versionID);
			request.setAttribute("HTML",request.getContextPath()+"/tmp/"+fileName);
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		return mapping.findForward("show_collectRelation");
	}
	/**
	 * 	通过string得到想对应的int数值
	 * @param col
	 * @return
	 */
	public int StringToInt(String col){
		int colInt=0;
		for(int x=0;x<col.getBytes().length;x++){
			int b=(int)col.getBytes()[x]-64;
			colInt=colInt+b;
			
		}
		return colInt;
		
	}
}
