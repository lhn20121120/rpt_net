package com.cbrc.smis.service;

import com.cbrc.smis.entity.AFForceRep;
import com.cbrc.smis.form.AFForceRepForm;

public interface IAFForceRepService {
	/***
	 * 查询单张报表是否有强制重报
	 * @param repInId
	 * @return
	 * @throws Exception
	 */
	public AFForceRep findAFForceRepByRepInId(Integer repInId) throws Exception;
	
	/***
	 * 增加强制重报信息
	 * @param rep
	 * @throws Exception
	 */
	public void addAFForceRep(AFForceRepForm repForm)throws Exception;
	
	/**
	 * 删除强制重报信息
	 * @param repInId
	 * @throws Exception
	 */
	public void deleteAFForceRep(Integer repInId) throws Exception;
}
