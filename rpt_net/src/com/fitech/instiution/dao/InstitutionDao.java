package com.fitech.instiution.dao;

import java.util.List;
import java.util.Map;

public interface InstitutionDao
{
    public Map<String, String> deletemCellFormulaStandard(String bankFlag);

    public void saveCellFormulaStandard(Map<String, List<String>> funWebMap,
            Map<String, List<String>> funModelFunWebMap, String bankType, Map<String, String> allDataMap,
            Map<String, List<String>> excelMap);

    public String getTolerances();
}
