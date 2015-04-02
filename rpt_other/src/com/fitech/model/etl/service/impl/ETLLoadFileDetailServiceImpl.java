package com.fitech.model.etl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.etl.model.pojo.EtlLoadFileDetail;
import com.fitech.model.etl.model.vo.ETLLoadFileDetailVo;
import com.fitech.model.etl.service.IETLLoadFileDetailService;

public class ETLLoadFileDetailServiceImpl extends DefaultBaseService<EtlLoadFileDetail, Integer> implements IETLLoadFileDetailService{
	
	/***
	 * 根据文件ID检索所有列
	 */
	@Override
	public List<ETLLoadFileDetailVo> findETLLoadFileDetailByFileId(
			Integer fileId) throws Exception {
		if(fileId==null || fileId.equals(""))
			return null;
		//定义集合
		List<EtlLoadFileDetail> detailList=null;
		List<ETLLoadFileDetailVo> voList=null;
		String hql="from EtlLoadFileDetail e where e.id.fileId="+fileId+" order by e.id.fileColumnId";
		detailList=this.findListByHsql(hql, null);//根据条件检索得出集合
		if(detailList==null || detailList.size()==0)//非空判断
			return null;
		voList=new ArrayList<ETLLoadFileDetailVo>();
		for(EtlLoadFileDetail e : detailList){//遍历集合
			ETLLoadFileDetailVo vo=new ETLLoadFileDetailVo();
			BeanUtils.copyProperties(vo, e);
			voList.add(vo);//添加进业务集合
		}
		return voList;//返回
	}
	
	/***
	 * 增加或者更新
	 */
	@Override
	public void saveOrUpdateETLLoadFileDetail(ETLLoadFileDetailVo vo)
			throws Exception {
		if(vo==null)//对象为空抛出异常
			throw new Exception();
		
		//在添加进数据库
		EtlLoadFileDetail detail=new EtlLoadFileDetail();
		BeanUtils.copyProperties(detail, vo);
		this.save(detail);
	}
	
	/***
	 * 根据任务ID删除文件列字段信息数据
	 */
	@Override
	public void deleteETLLoadFileDetailByFileId(Integer fileId,Integer columnId)
			throws Exception {
		EtlLoadFileDetail detail=new EtlLoadFileDetail();
		detail.getId().setFileId(fileId);
		detail.getId().setFileColumnId(columnId);
		this.delete(detail);
	}
	
	
	
}
