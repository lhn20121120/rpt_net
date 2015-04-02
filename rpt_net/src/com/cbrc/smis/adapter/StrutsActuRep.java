package com.cbrc.smis.adapter;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.util.FitechException;

public class StrutsActuRep
{
    private static FitechException log = new FitechException(StrutsActuRep.class);

    /**
     * 获得报表的报送频度和报送时间
     * 
     * @author rds
     * @serialData 2005-12-22 13:37
     * 
     * @param childRepId String 子报表ID
     * @param versionId String 版本号
     * @return List
     */
    public static List getMActuRep(String childRepId, String versionId, Integer dataRangeId)
    {
        List resList = null;

        if (childRepId == null || versionId == null)
            return null;

        DBConn conn = null;
        try
        {
            String hql = "from MActuRep mcr where mcr.comp_id.childRepId='" + childRepId + "'"
                    + " and mcr.comp_id.versionId='" + versionId + "' and mcr.comp_id.dataRangeId=" + dataRangeId;
            conn = new DBConn();

            List list = conn.openSession().find(hql);

            if (list != null && list.size() > 0)
            {
                resList = new ArrayList();
                for (int i = 0; i < list.size(); i++)
                {
                    MActuRep mActuRep = (MActuRep) list.get(i);
                    MActuRepForm mActuRepForm = new MActuRepForm();
                    TranslatorUtil.copyPersistenceToVo(mActuRep, mActuRepForm);
                    /*
                     * // System.out.println("childRepId:" +
                     * mActuRepForm.getChildRepId()); //
                     * System.out.println("versionId:" +
                     * mActuRepForm.getVersionId()); //
                     * System.out.println("DataRangeId:" +
                     * mActuRepForm.getDataRangeId()); //
                     * System.out.println("RepFreqId:" +
                     * mActuRepForm.getRepFreqId()); //
                     * System.out.println("OrgTypeId:" +
                     * mActuRepForm.getOATId());
                     */
                    resList.add(mActuRepForm);
                }
            }
        }
        catch (HibernateException he)
        {
            log.printStackTrace(he);
        }
        catch (Exception e)
        {
            log.printStackTrace(e);
        }
        finally
        {
            if (conn != null)
                conn.closeSession();
        }

        return resList;
    }
}
