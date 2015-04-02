package com.cbrc.exesqlcbrc.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.Session;

import com.cbrc.exesqlcbrc.service.ExecuteSqlService;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

public class ExecuteSqlServiceImpl implements ExecuteSqlService
{

    private FitechException log = new FitechException(ExecuteSqlServiceImpl.class);

    @Override
    public boolean execSQL(Map<String, String> sqlMap, FitechMessages messages) throws Exception
    {
        DBConn conn = null;
        Session session = null;
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psBatch = null;
        // PreparedStatement psCreateTab = null;
        // PreparedStatement psIntoRecord = null;
        int batchAddSize = 0;

        try
        {

            conn = new DBConn();
            session = conn.openSession();
            connection = session.connection();
            connection.setAutoCommit(false);

            String querySysParamSql = "select par_value from SYS_PARAMETER where par_name='BATCHSIZE'";
            psBatch = connection.prepareStatement(querySysParamSql);
            ResultSet addBatch = psBatch.executeQuery();
            while (addBatch.next())
            {
                batchAddSize = addBatch.getInt(1);
            }
            psBatch.close();

            Set<String> sets = sqlMap.keySet();
            Iterator<String> its = sets.iterator();

            int count = 0;
            while (its.hasNext())
            {
                count++;
                String sql = its.next();
                System.out.println("exec : " + sql);

                ps = connection.prepareStatement(sql);
                ps.execute();
                ps.close();
                if (count % batchAddSize == 0)
                {
                    connection.commit();
                }

            }

            // ps.close();
            connection.commit();
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
            e.printStackTrace();
            messages.add("sql执行失败，请删除已导入数据重新导入");
        }
        finally
        {

            if (null != conn)
            {
                conn.closeSession();
            }

        }
        return false;
    }

    @Override
    public void bakData(Map<String, String> mCellMap, Map<String, String> mCellToMap)
    {
        DBConn conn = null;
        Session session = null;
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psTo = null;

        StringBuilder mCellPartStr = new StringBuilder(
                "insert into M_CELL_FORMU(CELL_FORMU_ID,CELL_FORMU,FORMU_TYPE,CELL_FORMU_VIEW) values(");
        StringBuilder mCellToPartStr = new StringBuilder(
                "insert into M_CELL_TO_FORMU(CELL_TO_FORMU_ID,CELL_FORMU_ID,CHILD_REP_ID,VERSION_ID) values(");
        try
        {

            conn = new DBConn();
            session = conn.openSession();
            connection = session.connection();
            connection.setAutoCommit(false);

            String queryMCellSql = "select CELL_FORMU_ID,CELL_FORMU,FORMU_TYPE,CELL_FORMU_VIEW from M_CELL_FORMU";
            StringBuilder wholeMCellStr = new StringBuilder();
            ps = connection.prepareStatement(queryMCellSql);
            ResultSet mcellSet = ps.executeQuery();
            while (mcellSet.next())
            {
                wholeMCellStr = mCellPartStr.append(mcellSet.getString(1)).append(",");
                wholeMCellStr = wholeMCellStr.append("'" + mcellSet.getString(2) + "'").append(",");
                wholeMCellStr = wholeMCellStr.append(mcellSet.getString(3)).append(",");
                wholeMCellStr = wholeMCellStr.append("'" + mcellSet.getString(4) + "'").append(")\n");

                mCellMap.put(mCellPartStr.toString(), null);
                wholeMCellStr = new StringBuilder();
                mCellPartStr = new StringBuilder(
                        "insert into M_CELL_FORMU(CELL_FORMU_ID,CELL_FORMU,FORMU_TYPE,CELL_FORMU_VIEW) values(");

            }

            ps.close();
            connection.commit();

            String queryMCellToSql = "select CELL_TO_FORMU_ID,CELL_FORMU_ID,CHILD_REP_ID,VERSION_ID from M_CELL_TO_FORMU";
            StringBuilder wholeMCellToStr = new StringBuilder();
            psTo = connection.prepareStatement(queryMCellToSql);
            ResultSet mcellToSet = psTo.executeQuery();
            while (mcellToSet.next())
            {
                wholeMCellToStr = mCellToPartStr.append(mcellToSet.getString(1)).append(",");
                wholeMCellToStr = wholeMCellToStr.append(mcellToSet.getString(2)).append(",");
                wholeMCellToStr = wholeMCellToStr.append("'" + mcellToSet.getString(3) + "'").append(",");
                wholeMCellToStr = wholeMCellToStr.append(mcellToSet.getString(4)).append(")\n");

                mCellToMap.put(mCellToPartStr.toString(), null);
                wholeMCellToStr = new StringBuilder();
                mCellToPartStr = new StringBuilder(
                        "insert into M_CELL_TO_FORMU(CELL_TO_FORMU_ID,CELL_FORMU_ID,CHILD_REP_ID,VERSION_ID) values(");

            }
            psTo.close();
            connection.commit();

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
    public List<String> getAllList()
    {
        DBConn conn = null;
        Session session = null;
        Connection connection = null;
        PreparedStatement ps = null;
        List<String> bakTime = new ArrayList<String>();

        String queryBakCellList = "select M_CELL_NAME from BAK_CELL_FORMU_RECORD";
        try
        {
            conn = new DBConn();
            session = conn.openSession();
            connection = session.connection();
            ps = connection.prepareStatement(queryBakCellList);
            ResultSet rs = ps.executeQuery(queryBakCellList);
            while (rs.next())
            {
                String mCellName = rs.getString(1);
                String[] cellName = mCellName.split("\\_");
                bakTime.add(cellName[cellName.length - 1]);
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

        return bakTime;
    }

    @Override
    public void bakUp(String bakTime)
    {
        DBConn conn = null;
        Session session = null;
        Connection connection = null;
        PreparedStatement ps = null;

        String truncateMCellSql = "truncate table m_cell_formu";
        String truncateMCellToSql = "truncate table m_cell_to_formu";
        String truncateMCellSTSql = "truncate table m_cell_formu_standard";
        String truncateMCellSTToSql = "truncate table m_cell_to_formu_standard";

        String queryBakCellList = "insert into m_cell_formu select * from cell_" + bakTime;
        String queryBakCellToList = "insert into m_cell_to_formu select * from cell_to_" + bakTime;
        String queryBakCellSTList = "insert into m_cell_formu_standard select * from cell_st_" + bakTime;
        String queryBakCellToSTList = "insert into m_cell_to_formu_standard select * from cell_st_to_" + bakTime;
        try
        {
            conn = new DBConn();
            session = conn.openSession();
            connection = session.connection();
            ps = connection.prepareStatement(truncateMCellSql);
            ps.execute();

            ps = connection.prepareStatement(truncateMCellToSql);
            ps.execute();

            ps = connection.prepareStatement(truncateMCellSTSql);
            ps.execute();

            ps = connection.prepareStatement(truncateMCellSTToSql);
            ps.execute();

            ps = connection.prepareStatement(queryBakCellList);
            ps.execute();
            connection.commit();

            ps = connection.prepareStatement(queryBakCellToList);
            ps.execute();
            connection.commit();

            ps = connection.prepareStatement(queryBakCellSTList);
            ps.execute();
            connection.commit();

            ps = connection.prepareStatement(queryBakCellToSTList);
            ps.execute();
            connection.commit();

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

    }
}
