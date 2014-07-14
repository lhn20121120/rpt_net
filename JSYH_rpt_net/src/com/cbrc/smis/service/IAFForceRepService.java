package com.cbrc.smis.service;

import com.cbrc.smis.entity.AFForceRep;
import com.cbrc.smis.form.AFForceRepForm;

public interface IAFForceRepService {
	/***
	 * ��ѯ���ű����Ƿ���ǿ���ر�
	 * @param repInId
	 * @return
	 * @throws Exception
	 */
	public AFForceRep findAFForceRepByRepInId(Integer repInId) throws Exception;
	
	/***
	 * ����ǿ���ر���Ϣ
	 * @param rep
	 * @throws Exception
	 */
	public void addAFForceRep(AFForceRepForm repForm)throws Exception;
	
	/**
	 * ɾ��ǿ���ر���Ϣ
	 * @param repInId
	 * @throws Exception
	 */
	public void deleteAFForceRep(Integer repInId) throws Exception;
}
