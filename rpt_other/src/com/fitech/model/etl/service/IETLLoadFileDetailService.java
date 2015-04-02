package com.fitech.model.etl.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlLoadFileDetail;
import com.fitech.model.etl.model.vo.ETLLoadFileDetailVo;

public interface IETLLoadFileDetailService extends IBaseService<EtlLoadFileDetail, Integer>{
	/***
	 * 根据文件ID检索对应的列
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public List<ETLLoadFileDetailVo> findETLLoadFileDetailByFileId(Integer fileId) throws Exception;
	
	/***
	 * 保存或更新
	 * @param vo
	 * @throws Exception
	 */
	public void saveOrUpdateETLLoadFileDetail(ETLLoadFileDetailVo vo) throws Exception;
	
	/**根据任务ID删除文件列字段信息数据*/
	public void deleteETLLoadFileDetailByFileId(Integer fileId,Integer columnId) throws Exception;
	
	
}
