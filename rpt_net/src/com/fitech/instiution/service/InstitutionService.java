package com.fitech.instiution.service;

import java.util.List;
import java.util.Map;

public interface InstitutionService
{
    public Map<String, String> deletemCellFormulaStandard(String bankFlag);

    public void saveCellFormulaStandard(Map<String, List<String>> funWebMap,
            Map<String, List<String>> funModelFunWebMap, String bankType, Map<String, String> allDataMap,
            Map<String, List<String>> excelMap);

    public String getTolerances();
}
