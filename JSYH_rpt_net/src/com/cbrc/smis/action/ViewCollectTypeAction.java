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
 * ��Excel�Ĺ�����ϵ��ά��
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
			 *  ȡ��MChildReport�еĶ�Ӧ���ļ���
			 */
			ReportDefine rd=new ReportDefine(request.getSession().getServletContext().getRealPath("/Reports/templates/"+childID+"_"+versionID+".xls"));
			List list=rd.getCellSet();
			List result=StrutsMCellDelegate.getCells(childID,versionID);
			if(result==null){
				// System.out.println("����ǿ�");
			}
			int rowMax=0;
			int colMax=0;
			int dataMove=FitechEXCELReport.getOffsetRows(childID,versionID);
			rowMax=rd.getLastRowNum();
			colMax=rd.getLastColNum();
			/*
			 * 	�õ���Ҫ��Excel�е���Ч�ֶ�
			 */
			for(int i=0;i<result.size();i++){
				MCellForm mcell=(MCellForm)result.get(i);
				if(!mcell.getDataType().equals(Config.NUMBER_CELL_TYPE))
					continue;
				String col=mcell.getColId();
				Integer row=new Integer(mcell.getRowId().intValue()-dataMove);
				/*
				 * 	���ֶ����������뵥Ԫ����
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
					 * 	����select
					 */
					String outPut="<select id="+colInt+"_"+row+"_"+" name=state_"+col+"_"+row+" class=select_150 onchange=\"parent.changeSelect('"+colInt+"_"+row+"_"+"')\">";
					String outPut0="<option value="+Config.COLLECT_TYPE_NO_COLLECT.intValue()+">������</option>";
					String outPut1="<option value="+Config.COLLECT_TYPE_SUM.intValue()+">�ۼ�</option>";
					String outPut2="<option value="+Config.COLLECT_TYPE_AVG.intValue()+">ȡƽ��</option>";
					String outPut3="<option value="+Config.COLLECT_TYPE_MAX.intValue()+">���ֵ</option>";
					String outPut4="<option value="+Config.COLLECT_TYPE_MIN.intValue()+">��Сֵ</option>";
					if(temp==Config.COLLECT_TYPE_NO_COLLECT.intValue()){
						outPut0="<option value="+Config.COLLECT_TYPE_NO_COLLECT.intValue()+" selected>������</option>";
					}
					else if(temp==Config.COLLECT_TYPE_SUM.intValue()){
						outPut1="<option value="+Config.COLLECT_TYPE_SUM.intValue()+" selected>�ۼ�</option>";
					}
					else if(temp==Config.COLLECT_TYPE_AVG.intValue()){
						outPut2="<option value="+Config.COLLECT_TYPE_AVG.intValue()+" selected>ȡƽ��</option>";
					}
					else if(temp==Config.COLLECT_TYPE_MAX.intValue()){
						outPut3="<option value="+Config.COLLECT_TYPE_MAX.intValue()+" selected>���ֵ</option>";
					}
					else if(temp==Config.COLLECT_TYPE_MIN.intValue()){
						outPut4="<option value="+Config.COLLECT_TYPE_MIN.intValue()+" selected>��Сֵ</option>";
					}
					outPut=outPut+outPut0+outPut1+outPut2+outPut3+outPut4+"</select>";
					cell.setCellValue(outPut);
					list.set(j,cell);
				}
			}
			
			/*
			 * 	����html��jsp����
			 */

			String fileName=System.currentTimeMillis()+".html";
			(new ToHTMLEngine()).generateHTML(rd,Config.TEMP_DIR+Config.FILESEPARATOR+fileName);
			
			/*
			 * 	����Ҫ����������request����߸�jsp����
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
	 * 	ͨ��string�õ����Ӧ��int��ֵ
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
