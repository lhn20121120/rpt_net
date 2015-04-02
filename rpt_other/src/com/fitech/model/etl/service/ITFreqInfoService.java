package com.fitech.model.etl.service;

import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.TFreqInfo;
import com.fitech.model.etl.model.vo.TETLFreqInfoDataVo;
import com.fitech.model.etl.model.vo.TFreqInfoVo;

/***
 * 频度信息服务接口
 * @author admin
 *
 */
public interface ITFreqInfoService extends IBaseService<TFreqInfo, Integer>{
	/**查询所有频度信息*/
	public List<TFreqInfoVo> findTFreqInfoList() throws Exception;
	/**更新频度信息*/
	public void updateTFreqInfo(TFreqInfoVo vo) throws Exception;
	
	/**查询所有的T_FREQ_INFO,ETL_FREQ_INFO,ETL_TIME_TYPE表的数据*/
	public List<TETLFreqInfoDataVo> findTETLFreqInfoDataList()throws Exception;
	
	/**根据usingFlag确定findTETLFreqInfoDataList方法是否被调用*/
	public List<TETLFreqInfoDataVo> findTETLFreqInfoDataList(Integer usingFlag) throws Exception;
}
