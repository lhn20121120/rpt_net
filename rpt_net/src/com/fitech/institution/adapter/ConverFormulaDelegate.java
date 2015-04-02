package com.fitech.institution.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.util.FitechException;

public class ConverFormulaDelegate {
	private static FitechException log = new FitechException(
			ConverFormulaDelegate.class);

	public static List<String> splitFormula(String srcStr) {
		List <String > items = null;
		
		if (srcStr == null || srcStr.equals(""))
			return items;
		String beginStr = "[";
		String endStr = "]";
		// String versionId = null;
		int startIndex = srcStr.indexOf(beginStr);
		if (startIndex == -1)
			return items;
		items = new ArrayList<String>();
		for (int i = startIndex; i < srcStr.length(); i++) {
			char str = srcStr.charAt(i);

			if (String.valueOf(str).equals(endStr)) {
				items.add(srcStr.substring(startIndex, i + 1));
			}

			if (String.valueOf(str).equals(beginStr)) {
				startIndex = i;
			}
		}
		return items;
	}
	
	
	public static String parseFormula(String srcStr, String templateId) {
		String formula = null;
		if (srcStr == null || srcStr.equals(""))
			return formula;
		String beginStr = "[";
		String endStr = "]";
		// String versionId = null;
		int startIndex = srcStr.indexOf(beginStr);
		if (startIndex == -1)
			return formula;
		List<String> list = new ArrayList<String>();
		list = splitFormula(srcStr);
//		Map<String, String> relationMap = DBFomulaDelegate.getRelationMap();
//		List<String> bingbiaoList = DBFomulaDelegate.getAllBingbiaoId();
//		Map<String, String> relationMap = DBFomulaDelegate.getBingBiaoRelation();
//		List <String> templateIdsList = DBFomulaDelegate.getAllStandardTemplateId();
		for (String item : list) {
			log.println(item);
			item = item.substring(item.indexOf(beginStr) + 1,
					item.indexOf(endStr));
			String[] params = item.split(",");
			String[] templates  = DBFomulaDelegate.queryTemplate(params);
			if (templates == null) {
				return null;
			}
			if(templates[0].contains("请")){
				srcStr = templates[0];
			}else{
				boolean cellPidisValid = DBFomulaDelegate.queryTemplateIsValid(templates[0], templates[1]);
				if (!cellPidisValid)
					return null;
				String colNum = DBFomulaDelegate.queryColNum(params, templates[0],
						templates[1]);
				log.println("colNum:" + colNum);
				if (colNum == null)
					return null;
				if(colNum.contains("af_cellInfo")){
					srcStr = colNum;
					break;
				}else{
					String str = colNum + "." + params[0];
					if (params[3].equals("上期")) {
						String versionId = DBFomulaDelegate.queryVersion(templateId);
						String freqId = DBFomulaDelegate.queryFreqId(params[4]);
						str = templateId + "_" + versionId + "_" + str + "_" + freqId
								+ "_1";
					}
					if (!templateId.equals(templates[0])) {
						str = templates[0] + "_" + templates[1] + "_" + str;
					}
					srcStr = srcStr.replace(beginStr + item + endStr, beginStr + str
							+ endStr);
				}
			}
		}
		return srcStr;
	}

	public static List<String> validationFormula(
			Map<String, List<String>> formulasMap) {
		List<String> resultList = new ArrayList<String>();
		for (String key : formulasMap.keySet()) {
			List<String> formulas = formulasMap.get(key);
			List<String> formulas_ = new ArrayList<String>();
			Map<String, String> viewMap = new HashMap<String, String>();
			for (int i = 0; i < formulas.size(); i++) {
				String viewformula = formulas.get(i);
				String formula = parseFormula(viewformula, key);
				if (formula == null) {
					log.println(key + "无效公式：" + viewformula);
					formulas.remove(i);
					i--;
					continue;
				}
				if(formula.contains("请")){
					log.println(key + "更新失败!"+formula);
					resultList.add(key + "更新失败!"+formula);
					formulas.remove(i);
					i--;
					break;
				}
				if (!formulas_.contains(formula)) {
					formulas_.add(formula);
					viewMap.put(formula, viewformula);
				}
				
			}
			
			boolean f= DBFomulaDelegate.updateInstitution(key, formulas_,
					viewMap);
			if (!f) {
				log.println(key + "更新失败");
				resultList.add(key + "更新失败");
			}
		}
		return resultList;
	}
}
