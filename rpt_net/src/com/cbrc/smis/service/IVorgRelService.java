package com.cbrc.smis.service;

import java.util.List;

import com.cbrc.org.entity.SysFlag;
import com.fitech.gznx.po.vOrgRel;

public interface IVorgRelService {
	/***
	 * 根据主键查询单个信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public vOrgRel findVOrgRelByOrgId(String orgId) throws Exception;
	
	/***
	 * 保存信息
	 * @param rel
	 * @throws Exception
	 */
	public void saveVorgRel(vOrgRel rel) throws Exception;
	
	/***
	 * 将所有可以设置为上级机构的数据添加进集合
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public List<vOrgRel> searchPreRelList(String orgId) throws Exception;
	
	/***
	 * 查询所有的映射信息
	 * @return
	 * @throws Exception
	 */
	public List<vOrgRel> findVorgRelAll() throws Exception;
	
	/***
	 * 查询所有映射信息 带分页
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<vOrgRel> findVorgRelAll(int pageNo,int pageSize) throws Exception;
	
	/***
	 * 更新映射信息
	 * @param rel
	 * @throws Exception
	 */
	public void updateVorgRel(vOrgRel rel) throws Exception;
	
	/***
	 * 查询所有数据的条数
	 * @return
	 * @throws Exception
	 */
	public int selectCount() throws Exception;
	
	/***
	 * 根据主键ID 删除单条数据
	 * @param orgId
	 * @throws Exception
	 */
	public void deleteVorgRel(vOrgRel rel) throws Exception;
	
	/***
	 * 查询所有系统标志位
	 * @return
	 * @throws Exception
	 */
	public List<SysFlag> findAllSysFlag() throws Exception;
}
