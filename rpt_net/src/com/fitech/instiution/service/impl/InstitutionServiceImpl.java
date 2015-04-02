package com.fitech.instiution.service.impl;

import java.util.List;
import java.util.Map;

import com.fitech.instiution.dao.InstitutionDao;
import com.fitech.instiution.dao.impl.InstitutionDaoImpl;
import com.fitech.instiution.service.InstitutionService;

public class InstitutionServiceImpl implements InstitutionService
{
    InstitutionDao institutionDao = InstitutionDaoImpl.getInstance();

    public static InstitutionServiceImpl instance = new InstitutionServiceImpl();

    private InstitutionServiceImpl()
    {
    }

    public static InstitutionServiceImpl getInstance()
    {
        return instance;
    }

    @Override
    public Map<String, String> deletemCellFormulaStandard(String bankFlag)
    {
        return institutionDao.deletemCellFormulaStandard(bankFlag);

    }

    @Override
    public void saveCellFormulaStandard(Map<String, List<String>> funWebMap,
            Map<String, List<String>> funModelFunWebMap, String bankType, Map<String, String> allDataMap,
            Map<String, List<String>> excelMap)
    {
        institutionDao.saveCellFormulaStandard(funWebMap, funModelFunWebMap, bankType, allDataMap, excelMap);

    }

    public InstitutionDao getInstitutionDao()
    {
        return institutionDao;
    }

    public void setInstitutionDao(InstitutionDao institutionDao)
    {
        this.institutionDao = institutionDao;
    }

    @Override
    public String getTolerances()
    {

        return institutionDao.getTolerances();
    }

}
