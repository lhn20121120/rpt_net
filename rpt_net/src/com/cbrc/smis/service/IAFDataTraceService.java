package com.cbrc.smis.service;

import java.util.List;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.entity.AFDataTrace;

public interface IAFDataTraceService {
	/***
	 * 增加数据痕迹
	 * @param af
	 * @throws Exception
	 */
	public void  addAFDataTrace(AFDataTrace af) throws Exception;
	
	/***
	 * 根据模板ID和版本ID 单元格名称查出单个模板的数据痕迹信息
	 * @param templateID
	 * @param versionId
	 * @return
	 * @throws Exception
	 */
	public List<AFDataTraceForm> findListByTemplateIDandVersionId(String repInId,String cellName) throws Exception;
	
	
	/***
	 * 根据主键ID修改单条数据痕迹的状态位(模拟删除数据操作)
	 * 设置单条数据痕迹的状态为1 (0:可读,1:不可读)
	 * @param traceId
	 * @throws Exception
	 */
	public void updateAFDataTraceStatusById(Integer traceId,Integer status) throws Exception;
	
	/***
	 * 根据主键ID批量修改数据痕迹的状态位(模拟批量删除操作)
	 * 设置数据痕迹的状态为1(0:可读,1:不可读)
	 * @param traceId
	 * @throws Exception
	 */
	public void updateAFDataTraceStatusById(List<Integer> traceIds,Integer status) throws Exception;
	
	/***
	 * 查出单元格的原始值
	 * @param repInId
	 * @param cellName
	 * @throws Exception
	 */
	public String findOriDataByTemplateIDAndVersionId(String repInId,String cellName) throws Exception;
	
	/***
	 * 多条件带分页检索数据痕迹
	 * @param traceForm 痕迹对象
	 * @param pageSize 每页个数
	 * @param pageNo 页号
	 * @return
	 * @throws Exception
	 */
	public List<AFDataTraceForm> findListByAFDataTrace(AFDataTraceForm traceForm,int pageSize,int pageNo,String reportFlag) throws Exception;
	
	/***
	 * 不带分页
	 * @param traceForm
	 * @return
	 * @throws Exception
	 */
	public List<AFDataTraceForm> findListByAFDataTrace(AFDataTraceForm traceForm,String reportFlag) throws Exception;
}
