package com.fitech.instiution.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.instiution.dao.InstitutionDao;
import com.gather.common.Log;

public class InstitutionDaoImpl implements InstitutionDao
{

    private Log logger = new Log(InstitutionDaoImpl.class);

    private FitechException log = new FitechException(InstitutionDaoImpl.class);

    public static InstitutionDaoImpl instance = new InstitutionDaoImpl();

    private InstitutionDaoImpl()
    {
    }

    public static InstitutionDaoImpl getInstance()
    {
        return instance;
    }

    /**
     * 逻辑删除
     */
    @Override
    public Map<String, String> deletemCellFormulaStandard(String bankFlag)
    {
        DBConn conn = null;
        Session session = null;
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psQueryAll = null;

        PreparedStatement psCreateTab = null;
        PreparedStatement psIntoRecord = null;
        PreparedStatement psFormuExcel = null;

        String sqlFormu = "";
        Map<String, String> allDataMap = new HashMap<String, String>();
        try
        {
            if (null == bankFlag)
            {
                return null;
            }
            conn = new DBConn();
            session = conn.openSession();
            connection = session.connection();

            // bak data

            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(currentTime);
            
            // bakup m_cell_formu and m_cell_to_formu
            if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver")){
                //do nothing
            }
            if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle")){
                String createMCellBakTab = "create table cell_" + dateString + " as select * from m_cell_formu";
                String createMCellToBakTab = "create table cell_to_" + dateString + " as select * from m_cell_to_formu";
                String createMCellStBakTab = "create table cell_st_" + dateString
                        + " as select * from m_cell_formu_standard";
                String createMCellStToBakTab = "create table cell_st_to_" + dateString
                        + " as select * from m_cell_to_formu_standard";
                psCreateTab = connection.prepareStatement(createMCellBakTab);
                psCreateTab.execute();

                psCreateTab = connection.prepareStatement(createMCellToBakTab);
                psCreateTab.execute();

                psCreateTab = connection.prepareStatement(createMCellStBakTab);
                psCreateTab.execute();

                psCreateTab = connection.prepareStatement(createMCellStToBakTab);
                psCreateTab.execute();

                psCreateTab.close();
            }else if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("MySQL")){
                //do nothing           
            }else if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("db2")){
                String createMCellBakTab = "create table cell_" + dateString + " as (select * from m_cell_formu) definition only";
                String createMCellBakTabInsert = "insert into cell_" + dateString + " (select * from m_cell_formu)";
                String createMCellToBakTab = "create table cell_to_" + dateString + " as (select * from m_cell_to_formu) definition only";
                String createMCellToBakTabInsert = "insert into cell_to_" + dateString + " (select * from m_cell_to_formu)";
                String createMCellStBakTab = "create table cell_st_" + dateString
                        + " as (select * from m_cell_formu_standard) definition only";
                String createMCellStBakTabInsert = "insert into cell_st_" + dateString + " (select * from m_cell_formu_standard)";
                String createMCellStToBakTab = "create table cell_st_to_" + dateString
                        + " as (select * from m_cell_to_formu_standard) definition only";
                String createMCellStToBakTabInsert = "insert into cell_st_to_" + dateString + " (select * from m_cell_to_formu_standard)";
                psCreateTab = connection.prepareStatement(createMCellBakTab);
                psCreateTab.execute();
                psCreateTab = connection.prepareStatement(createMCellBakTabInsert);
                psCreateTab.execute();
                connection.commit();
                
                psCreateTab = connection.prepareStatement(createMCellToBakTab);
                psCreateTab.execute();
                psCreateTab = connection.prepareStatement(createMCellToBakTabInsert);
                psCreateTab.execute();
                connection.commit();

                psCreateTab = connection.prepareStatement(createMCellStBakTab);
                psCreateTab.execute();
                psCreateTab = connection.prepareStatement(createMCellStBakTabInsert);
                psCreateTab.execute();
                connection.commit();

                psCreateTab = connection.prepareStatement(createMCellStToBakTab);
                psCreateTab.execute();
                psCreateTab = connection.prepareStatement(createMCellStToBakTabInsert);
                psCreateTab.execute();
                connection.commit();

                psCreateTab.close();
            }
            
            

            String mCellBakName = "cell_" + dateString;
            String mCellToBakName = "cell_to_" + dateString;
            String mCellSTBakName = "cell_st_" + dateString;
            String mCellSTToBakName = "cell_st_to_" + dateString;

            // insert record to BAK_CELL_FORMU_RECORD
            String bakRecord = "insert into BAK_CELL_FORMU_RECORD(M_CELL_NAME,M_CELL_TO_NAME,M_CELL_ST_NAME,M_CELL_ST_TO_NAME)values('"
                    + mCellBakName + "','" + mCellToBakName + "','" + mCellSTBakName + "','" + mCellSTToBakName + "')";
            psIntoRecord = connection.prepareStatement(bakRecord);
            psIntoRecord.execute();
            psIntoRecord.close();
            connection.commit();

            // end

            long startTime = System.currentTimeMillis();
            // 将所有记录先保存下来，用于比较记录是否相同
            String queryAllData = "select t.cell_formu_id,t1.version_id,t1.child_rep_id,t.cell_formu,t.cell_formu_view from M_CELL_FORMU t, M_CELL_TO_FORMU t1 where t.cell_formu_id = t1.cell_formu_id";
            psQueryAll = connection.prepareStatement(queryAllData);
            ResultSet result = psQueryAll.executeQuery();
            String key = "";
            List<String> valList;
            while (result.next())
            {
                valList = new ArrayList<String>();
                key = result.getString(1);
                valList.add(result.getString(2));
                valList.add(result.getString(3));
                valList.add(result.getString(4));
                valList.add(result.getString(5));
                allDataMap.put(result.getString(2) + result.getString(3) + result.getString(4) + result.getString(5),
                        key);
            }

            // 删除M_CELL_TO_FORMU表
            sqlFormu = "update M_CELL_TO_FORMU set version_id='9999' where cell_to_formu_id in (select t.cell_to_formu_id from M_CELL_TO_FORMU_STANDARD t where t.level_='"
                    + bankFlag + "')";
            ps = connection.prepareStatement(sqlFormu);
            ps.execute();
            connection.commit();
            ps.close();
            System.out.println("update M_CELL_TO_FORMU over,time : " + (System.currentTimeMillis() - startTime));

            long startTimeCell = System.currentTimeMillis();
            // 更新M_CELL_FORMU_STANDARD表
            sqlFormu = "delete from M_CELL_FORMU_STANDARD where level_='" + bankFlag + "'";
            ps = connection.prepareStatement(sqlFormu);
            ps.execute();
            connection.commit();
            ps.close();
            System.out.println("delete from M_CELL_FORMU_STANDARD over,time : "
                    + (System.currentTimeMillis() - startTimeCell));

            long startTimeSTANDARD = System.currentTimeMillis();
            // 更新M_CELL_TO_FORMU_STANDARD表
            sqlFormu = "delete from M_CELL_TO_FORMU_STANDARD where level_='" + bankFlag + "'";
            ps = connection.prepareStatement(sqlFormu);
            ps.execute();
            connection.commit();
            ps.close();
            System.out.println("del M_CELL_TO_FORMU_STANDARD over,time : "
                    + (System.currentTimeMillis() - startTimeSTANDARD));
            // DEL FORMU_EXCEL
            sqlFormu = "delete from FORMU_EXCEL where level_='" + bankFlag + "'";
            ps = connection.prepareStatement(sqlFormu);
            ps.execute();
            connection.commit();
            ps.close();
            System.out.println("del FORMU_EXCEL over,time : " + (System.currentTimeMillis() - startTimeSTANDARD));

        }
        catch (Exception e)
        {
            log.printStackTrace(e);
            e.printStackTrace();
        }
        finally
        {
            if (null != conn)
                conn.closeSession();
        }
        return allDataMap;

    }

    @Override
    public void saveCellFormulaStandard(Map<String, List<String>> funWebMap,
            Map<String, List<String>> funModelFunWebMap, String bankType, Map<String, String> allDataMap,
            Map<String, List<String>> excelMap)
    {
        long startTime = System.currentTimeMillis();
        DBConn conn = null;
        Session session = null;
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psFormula = null;
        PreparedStatement psFormulaStd = null;
        PreparedStatement psToFormula = null;
        PreparedStatement psToFormulaStd = null;
        PreparedStatement psToFormuExcel = null;
        PreparedStatement psToUpdateToFormu = null;
        PreparedStatement psTodelFormuToSt = null;
        // PreparedStatement psSameRecord = null;
        PreparedStatement psReback = null;
        PreparedStatement psBatch = null;

        int batchAddSize = 0;

        // String sqlFormu = "";
        List<String> cellForIdList = new ArrayList<String>();
        // List<String> toCellForIdList = new ArrayList<String>();
        Map<String, List<String>> funModelFunWebMapCopy = new HashMap<String, List<String>>();
        funModelFunWebMapCopy.putAll(funModelFunWebMap);
        try
        {

            conn = new DBConn();
            session = conn.openSession();
            connection = session.connection();
            connection.setAutoCommit(false);

            Set<Entry<String, List<String>>> funWebSet = funWebMap.entrySet();
            Iterator<Entry<String, List<String>>> it = funWebSet.iterator();
            // 插入M_CELL_FORMU表
            String insertCellFormuSql = "insert into M_CELL_FORMU(CELL_FORMU_ID,CELL_FORMU,FORMU_TYPE,CELL_FORMU_VIEW)values(?,?,?,?)";
            psFormula = connection.prepareStatement(insertCellFormuSql);

            // 插入M_CELL_FORMU_STANDARD表
            String insertCellFormuStandardSql = "insert into M_CELL_FORMU_STANDARD(CELL_FORMU_ID,CELL_FORMU,FORMU_TYPE,CELL_FORMU_VIEW,LEVEL_)values(?,?,?,?,?)";
            psFormulaStd = connection.prepareStatement(insertCellFormuStandardSql);

            String insertCellToFormuSql = "INSERT INTO M_CELL_TO_FORMU(CELL_TO_FORMU_ID,CELL_FORMU_ID,CHILD_REP_ID,VERSION_ID)VALUES(?,?,?,?)";
            psToFormula = connection.prepareStatement(insertCellToFormuSql);

            // 插入M_CELL_TO_FORMU_STANDARD表
            String insertCellToFormuStandardSql = "INSERT INTO M_CELL_TO_FORMU_STANDARD(CELL_TO_FORMU_ID, CELL_FORMU_ID, CHILD_REP_ID, VERSION_ID, LEVEL_)values(?,?,?,?,?)";
            psToFormulaStd = connection.prepareStatement(insertCellToFormuStandardSql);

            String insertFormuExcel = "INSERT INTO FORMU_EXCEL(ORGID,CELL_FORMU_ID,BEGINTIME,ENDTIME,DATARANGE,LEVEL_)values(?,?,?,?,?,?)";
            psToFormuExcel = connection.prepareStatement(insertFormuExcel);
            
            String updateVersionSql = "update m_cell_to_formu set version_id = '9999' where cell_formu_id in (select cell_formu_id from formu_excel where level_='"+bankType+"')";
            psToUpdateToFormu=connection.prepareStatement(updateVersionSql);
            
            String delForToStSql = "delete from m_cell_to_formu_standard where cell_formu_id in (select cell_formu_id from formu_excel where level_='"+bankType+"')";
            psTodelFormuToSt = connection.prepareStatement(delForToStSql);
            // String querySamerecordSql =
            // "select t1.cell_formu_id from M_CELL_FORMU t1,M_CELL_TO_FORMU t2 where t1.cell_formu_id=t2.cell_to_formu_id and t2.child_rep_id=? and t2.version_id=? and t1.cell_formu=? and t1.cell_formu_view=?";
            // psSameRecord = connection.prepareStatement(querySamerecordSql);

            String reBackSql = "update M_CELL_TO_FORMU t set t.version_id=? where t.cell_formu_id=?";
            psReback = connection.prepareStatement(reBackSql);

            String querySysParamSql = "select par_value from SYS_PARAMETER where par_name='BATCHSIZE'";
            psBatch = connection.prepareStatement(querySysParamSql);
            ResultSet addBatch = psBatch.executeQuery();
            while (addBatch.next())
            {
                batchAddSize = addBatch.getInt(1);
            }
            int num = 0;
            int toNum = 0;

            String tempStr;

            while (it.hasNext())
            {
                String sameFormuId = null;
                Entry<String, List<String>> funWebEntry = it.next();
                String formulaId = funWebEntry.getKey();
                String formulaType = funWebEntry.getValue().get(0);
                String cell_formu = funWebEntry.getValue().get(1);
                String cell_formu_view = funWebEntry.getValue().get(2);

                funModelFunWebMapCopy.remove(formulaId);
                // 插入M_CELL_TO_FORMU表
                List<String> modelArrayList = funModelFunWebMap.get(formulaId);

                // 得到sequence
                String sequenceSql ="";

                if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver")){
                    //do nothing
                }
                if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle")){
                    sequenceSql= "select seq_m_cell_formu.nextval from dual";
                }else if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("MySQL")){
                    //do nothing           
                }else if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("db2")){
                    sequenceSql="values nextval for seq_m_cell_formu";
                }
                ps = connection.prepareStatement(sequenceSql);
                ResultSet resultSet = ps.executeQuery();
                String cellForId = "";
                while (resultSet.next())
                {
                    cellForId = resultSet.getString(1);
                    cellForIdList.add(cellForId);
                }
                ps.close();

                // 查询相同记录
                // psSameRecord.setObject(1, modelArrayList.get(0));
                // psSameRecord.setObject(2, modelArrayList.get(1));
                // psSameRecord.setObject(3, cell_formu);
                // psSameRecord.setObject(4, cell_formu_view);
                // ResultSet sameResultSetId = psSameRecord.executeQuery();
                //
                // while (sameResultSetId.next())
                // {
                // sameFormuId = sameResultSetId.getString(1);
                // }

                // modelArrayList.get(0)是child_rep_id,modelArrayList.get(1)是version_id
                tempStr = modelArrayList.get(1) + modelArrayList.get(0) + cell_formu + cell_formu_view;
                sameFormuId = allDataMap.get(tempStr);

                //System.out.println("sameFormuId : "+(null==sameFormuId?cellForId:sameFormuId)+"   "+tempStr);
                // if formu_excel has record
                List<String> excelValList = excelMap.get(formulaId);
                if (null != excelValList)
                {

                    if (null == sameFormuId)
                    {

                        for (String excelVal : excelValList)
                        {                          
                            System.out.println(formulaId + "-->" + cellForId);
                            excelVal = excelVal.replace(formulaId, cellForId);
                            String[] excel = excelVal.split("\\,");
                            psToFormuExcel.setObject(1, excel[0]);
                            psToFormuExcel.setObject(2, excel[1]);
                            psToFormuExcel.setObject(3, excel[2]);
                            psToFormuExcel.setObject(4, excel[3]);
                            psToFormuExcel.setObject(5, excel[4]);
                            psToFormuExcel.setObject(6, bankType);
                            psToFormuExcel.addBatch();
                            psToFormuExcel.executeBatch();
                            connection.commit();
                        }

                    }
                    else
                    {

                        for (String excelVal : excelValList)
                        {
                            System.out.println(formulaId + "-->" + sameFormuId);
                            excelVal = excelVal.replace(formulaId, sameFormuId);
                            String[] excel = excelVal.split("\\,");
                            psToFormuExcel.setObject(1, excel[0]);
                            psToFormuExcel.setObject(2, excel[1]);
                            psToFormuExcel.setObject(3, excel[2]);
                            psToFormuExcel.setObject(4, excel[3]);
                            psToFormuExcel.setObject(5, excel[4]);
                            psToFormuExcel.setObject(6, bankType);
                            psToFormuExcel.addBatch();
                            psToFormuExcel.executeBatch();
                            connection.commit();
                        }
                    }

                }
                //
                num++;
                // System.out.println(insertCellFormuSql);
                // 如果没有重复记录，则插入旧表
                if (null == sameFormuId)
                {
                    psFormula.setObject(1, cellForId);
                    psFormula.setObject(2, cell_formu);
                    psFormula.setObject(3, formulaType);
                    psFormula.setObject(4, cell_formu_view);
                    psFormula.addBatch();
                }
                else
                {
                    psReback.setObject(1, modelArrayList.get(1));
                    psReback.setObject(2, sameFormuId);
                    psReback.execute();
                    connection.commit();
                }

                psFormulaStd.setObject(1, null == sameFormuId ? cellForId : sameFormuId);
                psFormulaStd.setObject(2, cell_formu);
                psFormulaStd.setObject(3, formulaType);
                psFormulaStd.setObject(4, cell_formu_view);
                psFormulaStd.setObject(5, bankType);
                psFormulaStd.addBatch();

                if (num >= batchAddSize)
                {
                    psFormula.executeBatch();
                    num = 0;
                    // connection.commit();
                    System.out.println("psFormula commit");

                    psFormulaStd.executeBatch();
                    connection.commit();
                    System.out.println("psFormulaTest commit");
                }

                if (null != modelArrayList && modelArrayList.size() > 0)
                {
                    toNum++;
                    // 如果没有重复记录，则插入旧表
                    if (null == sameFormuId)
                    {
                        psToFormula.setObject(1, cellForId);
                        psToFormula.setObject(2, cellForId);
                        psToFormula.setObject(3, modelArrayList.get(0));
                        psToFormula.setObject(4, modelArrayList.get(1));
                        psToFormula.addBatch();
                    }

                    psToFormulaStd.setObject(1, null == sameFormuId ? cellForId : sameFormuId);
                    psToFormulaStd.setObject(2, null == sameFormuId ? cellForId : sameFormuId);
                    psToFormulaStd.setObject(3, modelArrayList.get(0));
                    psToFormulaStd.setObject(4, modelArrayList.get(1));
                    psToFormulaStd.setObject(5, bankType);
                    psToFormulaStd.addBatch();

                    if (toNum >= batchAddSize)
                    {
                        toNum = 0;
                        psToFormula.executeBatch();
                        // connection.commit();
                        System.out.println("psToFormula commit");
                        psToFormulaStd.executeBatch();
                        connection.commit();
                        System.out.println("psToFormulaStd commit");
                    }

                }

            }
            psFormula.executeBatch();
            // connection.commit();
            psFormulaStd.executeBatch();
            // connection.commit();
            psToFormula.executeBatch();
            // connection.commit();
            psToFormulaStd.executeBatch();
            connection.commit();
            psFormulaStd.close();
            psFormula.close();
            psToFormula.close();
            psToFormulaStd.close();
            psToFormuExcel.close();

            //针对例外表更新m_cell_to_formu和m_cell_to_formu_standard表
            psToUpdateToFormu.execute();
            psToUpdateToFormu.close();
            connection.commit();
            psTodelFormuToSt.execute();
            psTodelFormuToSt.close();
            connection.commit();
            //
            System.out.println("total time : " + (System.currentTimeMillis() - startTime));

            Set<Entry<String, List<String>>> funWebSetLost = funModelFunWebMapCopy.entrySet();
            Iterator<Entry<String, List<String>>> itLost = funWebSetLost.iterator();
            while (itLost.hasNext())
            {
                logger.info("Lost formu_id : " + itLost.next().getKey());
            }
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
            e.printStackTrace();
        }
        finally
        {

            if (null != conn)
            {
                conn.closeSession();
            }

        }

    }

    @Override
    public String getTolerances()
    {
        DBConn conn = null;
        Session session = null;
        Connection connection = null;
        PreparedStatement ps = null;
        String tolerance = "";
        try
        {

            conn = new DBConn();
            session = conn.openSession();
            connection = session.connection();

            String querySysParamSql = "select DESCRIPTION from SYS_PARAMETER where par_name='TOLERANCE'";
            ps = connection.prepareStatement(querySysParamSql);
            ResultSet addBatch = ps.executeQuery();

            while (addBatch.next())
            {
                tolerance = addBatch.getString(1);
            }
            ps.close();
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
            e.printStackTrace();
        }
        finally
        {

            if (null != conn)
            {
                conn.closeSession();
            }

        }
        return tolerance;
    }
}
