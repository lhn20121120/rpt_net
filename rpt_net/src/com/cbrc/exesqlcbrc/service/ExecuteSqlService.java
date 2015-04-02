package com.cbrc.exesqlcbrc.service;

import java.util.List;
import java.util.Map;

import com.cbrc.smis.util.FitechMessages;

public interface ExecuteSqlService
{

    /***
     * Ö´ÐÐupdate,delete,insert ²Ù×÷
     * 
     */

    public boolean execSQL(Map<String, String> sqlMap, FitechMessages messages) throws Exception;

    public void bakData(Map<String, String> mCellMap, Map<String, String> mCellToMap);

    public List<String> getAllList();

    public void bakUp(String bakTime);
}
