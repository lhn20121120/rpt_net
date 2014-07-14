package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfValidateformula;

public class AFValidateFormulaDelegate {
	
	private static FitechException log = new FitechException(
			AFValidateFormulaDelegate.class);
	
    /**
     * jdbc���� �������﷨ ����Ҫ�޸�
     * ���Ը� 2011-12-26
     * Ӱ���af_validateformula
     * ��ȡ�����[���ڱ��У��/���ڱ��ļ���]��ϵ���ʽ�б�
     * @param conn              Connection          ��������
     * @param reportIn          ReportIn            ��������
     * @return  List ���ʽ����
     * @throws Exception
     */
    public static List getCellFormus(Connection orclCon, AfReport reportIn,Integer formuType) throws Exception{
    	
        ArrayList resList = null;
        PreparedStatement pStmt = null;
        ResultSet plRS = null;
        
        try {
            if (reportIn.getTemplateId() != null && reportIn.getVersionId() != null) {
            	
                //��ѯĳһ�ű�������е�Ԫ����ʽ
                String plSql = "select a.formula_id,a.formula_value,a.formula_name from af_validateformula a "
                       + " where a.template_id = ? and a.version_id = ? and a.validate_type_id="
                       + formuType;
                
				//System.out.println(plSql +"="+reportIn.getTemplateId()+"-"+reportIn.getVersionId());
                pStmt = orclCon.prepareStatement(plSql.toUpperCase());
                pStmt.setString(1, reportIn.getTemplateId());
                pStmt.setString(2, reportIn.getVersionId());

                plRS = pStmt.executeQuery();

                resList = new ArrayList();
                while (plRS.next()) {
                    AfValidateformula cellFormu = new AfValidateformula();
                    cellFormu.setFormulaId(plRS.getLong("formula_id".toUpperCase()));
                    cellFormu.setFormulaValue(plRS.getString("formula_value".toUpperCase()));
                    cellFormu.setFormulaName(plRS.getString("formula_name".toUpperCase()));
                    cellFormu.setValidateTypeId(formuType.longValue());
                    resList.add(cellFormu);
                }
            } 
        } 
        catch (SQLException sqle) {
            throw new Exception(sqle.getMessage());
        } finally {
            if (plRS != null)   plRS.close();
            if (pStmt != null)  pStmt.close();
        }
        return resList;
    }
	

}
