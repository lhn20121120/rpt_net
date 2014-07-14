
package com.fitech.net.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.form.MCellForm;
/**
 *
 * 把常用的下拉列表的值放入此Form
 */
public class UtilCellForm implements Serializable{
	
	private String childRepId = null;
	
	private String versionId = null;
	
	private List cellList = null;
	
	public List getCellList() {
		cellList = new ArrayList();
		if(childRepId != null && versionId != null)
			try{
				List list = null;
				//点对点式可输入单元格列表
				list = StrutsMCellDelegate.getCells(this.childRepId,this.versionId);
				
				if(list!=null && list.size()>0){
					cellList = new ArrayList();
					MCellForm mCellForm = null;
					for(int i=0;i<list.size();i++){
						mCellForm=(MCellForm)list.get(i);
						cellList.add(new LabelValueBean(mCellForm.getCellName(),
								String.valueOf(mCellForm.getCellId())));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		return cellList;
	}

	public void setCellList(List cellList) {
		this.cellList = cellList;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getChildRepId() {
		return childRepId;
	}

	public void setChildRepId(String childRepId) {
		this.childRepId = childRepId;
	}
}