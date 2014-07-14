package com.cbrc.smis.service;

import java.util.List;

import com.cbrc.org.entity.SysFlag;
import com.fitech.gznx.po.vOrgRel;

public interface IVorgRelService {
	/***
	 * ����������ѯ������Ϣ
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public vOrgRel findVOrgRelByOrgId(String orgId) throws Exception;
	
	/***
	 * ������Ϣ
	 * @param rel
	 * @throws Exception
	 */
	public void saveVorgRel(vOrgRel rel) throws Exception;
	
	/***
	 * �����п�������Ϊ�ϼ�������������ӽ�����
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public List<vOrgRel> searchPreRelList(String orgId) throws Exception;
	
	/***
	 * ��ѯ���е�ӳ����Ϣ
	 * @return
	 * @throws Exception
	 */
	public List<vOrgRel> findVorgRelAll() throws Exception;
	
	/***
	 * ��ѯ����ӳ����Ϣ ����ҳ
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<vOrgRel> findVorgRelAll(int pageNo,int pageSize) throws Exception;
	
	/***
	 * ����ӳ����Ϣ
	 * @param rel
	 * @throws Exception
	 */
	public void updateVorgRel(vOrgRel rel) throws Exception;
	
	/***
	 * ��ѯ�������ݵ�����
	 * @return
	 * @throws Exception
	 */
	public int selectCount() throws Exception;
	
	/***
	 * ��������ID ɾ����������
	 * @param orgId
	 * @throws Exception
	 */
	public void deleteVorgRel(vOrgRel rel) throws Exception;
	
	/***
	 * ��ѯ����ϵͳ��־λ
	 * @return
	 * @throws Exception
	 */
	public List<SysFlag> findAllSysFlag() throws Exception;
}
