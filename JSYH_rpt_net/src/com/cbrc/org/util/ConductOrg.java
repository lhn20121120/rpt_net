/*
 * Created on 2005-12-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.org.util;

import net.sf.hibernate.Session;

import com.cbrc.org.hibernate.MOrg;
import com.cbrc.smis.common.Config;


/**
 * @author cb
 * 
 * ���������Ի��������һЩ��ز���
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductOrg {

    private Session session;

    /**
     * @param session
     */
    public ConductOrg(Session session) {
        super();
        this.session = session;
    }

    /**
     * �÷��������õ�һ��������¼
     * 
     * @param orgId
     *            String
     * @return ��������
     */
    public MOrg getOrg(String orgId) throws Exception {

        MOrg mOrg = null;

        try {

            mOrg = (MOrg) session.get(MOrg.class, orgId);

        } catch (Exception e) {
            
            throw  new Exception(Config.GETORGERROR);

        } finally {

            session.close();

        }

        return mOrg;
    }
}