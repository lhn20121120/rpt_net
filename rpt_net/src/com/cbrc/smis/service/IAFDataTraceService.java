package com.cbrc.smis.service;

import java.util.List;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.entity.AFDataTrace;

public interface IAFDataTraceService {
	/***
	 * �������ݺۼ�
	 * @param af
	 * @throws Exception
	 */
	public void  addAFDataTrace(AFDataTrace af) throws Exception;
	
	/***
	 * ����ģ��ID�Ͱ汾ID ��Ԫ�����Ʋ������ģ������ݺۼ���Ϣ
	 * @param templateID
	 * @param versionId
	 * @return
	 * @throws Exception
	 */
	public List<AFDataTraceForm> findListByTemplateIDandVersionId(String repInId,String cellName) throws Exception;
	
	
	/***
	 * ��������ID�޸ĵ������ݺۼ���״̬λ(ģ��ɾ�����ݲ���)
	 * ���õ������ݺۼ���״̬Ϊ1 (0:�ɶ�,1:���ɶ�)
	 * @param traceId
	 * @throws Exception
	 */
	public void updateAFDataTraceStatusById(Integer traceId,Integer status) throws Exception;
	
	/***
	 * ��������ID�����޸����ݺۼ���״̬λ(ģ������ɾ������)
	 * �������ݺۼ���״̬Ϊ1(0:�ɶ�,1:���ɶ�)
	 * @param traceId
	 * @throws Exception
	 */
	public void updateAFDataTraceStatusById(List<Integer> traceIds,Integer status) throws Exception;
	
	/***
	 * �����Ԫ���ԭʼֵ
	 * @param repInId
	 * @param cellName
	 * @throws Exception
	 */
	public String findOriDataByTemplateIDAndVersionId(String repInId,String cellName) throws Exception;
	
	/***
	 * ����������ҳ�������ݺۼ�
	 * @param traceForm �ۼ�����
	 * @param pageSize ÿҳ����
	 * @param pageNo ҳ��
	 * @return
	 * @throws Exception
	 */
	public List<AFDataTraceForm> findListByAFDataTrace(AFDataTraceForm traceForm,int pageSize,int pageNo,String reportFlag) throws Exception;
	
	/***
	 * ������ҳ
	 * @param traceForm
	 * @return
	 * @throws Exception
	 */
	public List<AFDataTraceForm> findListByAFDataTrace(AFDataTraceForm traceForm,String reportFlag) throws Exception;
}
