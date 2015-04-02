package com.fitech.model.etl.model.vo;

import java.util.ArrayList;
import java.util.List;

public class ETLFileInfoDetailDataVo {
	private ETLLoadFileInfoVo loadFileInfoVo=new ETLLoadFileInfoVo();
	private List<ETLLoadFileDetailVo> detailVoList=new ArrayList<ETLLoadFileDetailVo>();
	
	
	public ETLLoadFileInfoVo getLoadFileInfoVo() {
		return loadFileInfoVo;
	}
	public void setLoadFileInfoVo(ETLLoadFileInfoVo loadFileInfoVo) {
		this.loadFileInfoVo = loadFileInfoVo;
	}
	public List<ETLLoadFileDetailVo> getDetailVoList() {
		return detailVoList;
	}
	public void setDetailVoList(List<ETLLoadFileDetailVo> detailVoList) {
		this.detailVoList = detailVoList;
	}
	
	
}
