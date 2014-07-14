package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.util.FitechException;
/**
 * 将jsp的Excel关联信息做处理
 * @author zyl_xh
 *
 */
public class SaveCollectTypeAction extends Action{
	private FitechException log=new FitechException(AddYCBHAction.class);
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{
			int colMax = Integer.parseInt(request.getParameter("colMax"));
			int rowMax=Integer.parseInt(request.getParameter("rowMax"));
			String childId=request.getParameter("childRepId");
			String versionId=request.getParameter("versionId");
			List list=StrutsMCellDelegate.getCells(childId,versionId);
			// System.out.println(list.size());
			/*
			 *  通过childRepId和versionId获得相关的MCell
			 */
			/*
			 * 	列级获取
			 */
			for(int i=1;i<colMax;i++){
				String col=intToStr(i-1);
				/*
				 * 	行级获取
				 */
				for(int j=1;j<rowMax;j++){
					String value=request.getParameter("state_"+i+"_"+j);
		
					if(value!=null){
					/*
					 * 	对照获取
					 */
						for(int k=0;k<list.size();k++){
							MCellForm cell=(MCellForm)list.get(k);						
							if(cell.getColId().equals(col)&&cell.getRowId().intValue()==j){
								
								cell.setCollectType(new Integer(value));
								list.set(k,cell);
							}
						}
					}
					
				}
			}
			/*
			 * 	更新进入数据库
			 */
			StrutsMCellDelegate.updateCell(list);
			request.setAttribute("childRepId",childId);
			request.setAttribute("versionId",versionId);
		}
		catch(Exception e){
			log.printStackTrace(e);
			// System.out.println(e);
		}
		int length=request.getRequestURL().lastIndexOf("/");
		String url = request.getRequestURL().toString();
		String content = url.substring(0,length);
		// System.out.println(content+"/viewCollectTypeAction.do");
		return new ActionForward("/template/mod/viewCollectTypeAction.do");
	//	return mapping.findForward(content+"/viewCollectTypeAction.do");
		
	//	return new ViewCollectTypeAction().execute(mapping,form,request,response);
	}
	/**
	 * 	通过i转换为Excel中的列值
	 * @param i
	 * @return
	 */
	public String intToStr(int i){
		char c=(char)((i%26)+65);
		String result=((i/26)>=1?intToStr(i/26-1):"")+c;
		return result;
	}
}
