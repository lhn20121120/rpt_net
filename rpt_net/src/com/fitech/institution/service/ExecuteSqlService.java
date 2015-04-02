package com.fitech.institution.service;

import java.util.List;
import java.util.Map;

import com.fitech.institution.po.BakTableInfoVo;

public interface ExecuteSqlService
{

    /***
     * 执行update,delete,insert 操作
     * 
     */

    public boolean execSQL(Map<String, String> sqlMap) throws Exception;

    public void bakData(Map<String, String> mCellMap, Map<String, String> mCellToMap);
    
    public Boolean bakTable(String tableName, String backTableName);
    
    public Boolean saveBakInfo(String bakTime,String tableName, String backTableName);

    public Boolean rollBakTable(String tableName, String bakTableName);

    public Boolean truncateTable(String tableName);

	public List<BakTableInfoVo> getBakInfoList();

	public Boolean deleteBak(String bakTime, String tableName);

	public Boolean dropTable(String tableName);
    
}
