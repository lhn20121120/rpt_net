package com.fitech.model.etl.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.model.etl.model.pojo.EtlLoadFileDetail;
import com.fitech.model.etl.model.pojo.EtlLoadFileInfo;
import com.fitech.model.etl.model.vo.ETLFileInfoDetailDataVo;
import com.fitech.model.etl.model.vo.ETLLoadFileDetailVo;
import com.fitech.model.etl.model.vo.ETLLoadFileInfoVo;

public interface IETLLoadFileInfoService extends IBaseService<EtlLoadFileInfo, Integer>{
	/**根据任务类型检索数据装载配置*/
	public List<ETLLoadFileInfoVo> findETLLoadFileInfoByTaskId(ETLLoadFileInfoVo etlLoadFileInfoVo)throws Exception;
	
	/**根据数据库类型判断，并创建对应视图，根据视图查询用户下面所有的表名*/
	public List<String> findAllTableNameByUser()throws Exception;
	
	/**根据表名查询该表下所有的列名*/
	public List<String> findColumnNamesByTableName(String tableName)throws Exception;
	
	/**增加或保存数据装载配置*/
	public Integer saveOrUpdateFileInfoByFileId(ETLLoadFileInfoVo vo) throws Exception;
	
	public Integer saveOrUpdateFileInfo(ETLLoadFileInfoVo vo,List<ETLLoadFileDetailVo> detailVoList) throws Exception;
	
	/**
	 * 根据ID检索数据装载配置
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public ETLLoadFileInfoVo findETLLoadFileInfoByFileId(Integer fileId) throws Exception;
	
	/***
	 * 根据主键删除数据装载信息
	 * 并一并删除文件列字段信息
	 * @param fileId
	 * @throws Exception
	 */
	public void deleteETLLoadFileInfoVoAndDetailByFileId(Integer fileId,List<Integer> columnId) throws Exception;
	
	/***
	 * 根据主键查询数据装载信息和文件列字段信息
	 * @param fileId
	 * @throws Exception
	 */
	public ETLFileInfoDetailDataVo findETLFileInfoDetailDataVoByFileId(Integer fileId) throws Exception;
	
	/***
	 * 创建视图
	 * @throws Exception
	 */
	public  void createViewSearchTableName()throws Exception;
	
	/***
	 * 根据路径将文件上传至服务端
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public void upLoadFile(File file,String fileName);
}
