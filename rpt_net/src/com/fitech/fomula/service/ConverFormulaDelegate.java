package com.fitech.fomula.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.AFCollectRelationDelegate;

public class ConverFormulaDelegate {
	private static FitechException log = new FitechException(
			AFCollectRelationDelegate.class);
	
	private ReadFormulaFileDelegate rfd;

	public ReadFormulaFileDelegate getRfd() {
		return rfd;
	}

	public void setRfd(ReadFormulaFileDelegate rfd) {
		this.rfd = rfd;
	}
	
	public void validationFormula(){
		if(rfd!=null){
			DBFomulaDelegate.dropTempTable();
			DBFomulaDelegate.createTempTable();
			Map<String,List> new_formulasMap = new HashMap<String,List>();
			Map<String,List> formulasMap = rfd.getFormulasMap();
			for (String key : formulasMap.keySet()) {
				List<String> formulas = formulasMap.get(key);
				List<String> formulas_ = new ArrayList<String>();
				for (int i = 0; i < formulas.size(); i++) {
					String formula = formulas.get(i);
					if(formula.contains("null")){
						formulas.remove(i);
						i--;
						continue;
					}
					formula = parseFormula(formula,key);
					if(formula==null){
						formulas.remove(i);
						i--;
						continue;
					}
					if(!formulas_.contains(formula))
						formulas_.add(formula);
				}
				
				DBFomulaDelegate.saveFormulaInfoToTemTable(key, formulas_);
				
				new_formulasMap.put(key, formulas_);
			}
			rfd.setFormulasMap(new_formulasMap);
		}
	}
	
	public String parseFormula(String srcStr,String templateId){
		String formula = null;
		if(srcStr==null || srcStr.equals(""))
			return formula;
		String beginStr = "[";
		String endStr = "]";
	//	String versionId = null;
		int startIndex = srcStr.indexOf(beginStr);
		if(startIndex==-1)return formula;
		List<String> list = new ArrayList<String>();
		for (int i = startIndex; i < srcStr.length(); i++) {
			char str = srcStr.charAt(i);

			if(String.valueOf(str).equals(endStr)){
				list.add(srcStr.substring(startIndex, i+1));
			}
			
			if(String.valueOf(str).equals(beginStr)){
				startIndex = i;
			}
		}
		for (String item : list) {
			log.println(item);
			item = item.substring(item.indexOf(beginStr)+1, item.indexOf(endStr));
			String[] params = item.split(",");
			String[] templates = DBFomulaDelegate.queryTemplate(params);
			
			if(templates==null)
				return null;
			
			boolean cellPidisValid = DBFomulaDelegate.queryTemplateIsValid(templates[0], templates[1]);
			if(!cellPidisValid)
				return null;
			
			String colNum = DBFomulaDelegate.queryColNum(params, templates[0], templates[1]);
			log.println("colNum:"+colNum);
			if(colNum==null)
				return null;
			String str = colNum+"."+params[0];
			if(params[3].equals("上期")){
				String versionId = DBFomulaDelegate.queryVersion(templateId);
				String freqId = DBFomulaDelegate.queryFreqId(params[4]);
				str = templateId+"_"+versionId+"_"+str+"_"+freqId+"_1";
			}
			if(!templateId.equals(templates[0])){
				str = templates[0]+"_"+templates[1]+"_"+str;
			}
			
			srcStr = srcStr.replace(beginStr+item+endStr, beginStr+str+endStr);
		}
		return srcStr;
	}
	
	public boolean compareFormula(){
		boolean result =false;
		try {
			//将现场库中标准产品的校验公式删除
			DBFomulaDelegate.delStanardProductFormulas("2");
			
			Map<String,List> new_formulas = DBFomulaDelegate.queryAllTemplateToSql(rfd.getFormulasMap());
			Map<String,List> old_formulas = DBFomulaDelegate.queryAllTemplateToHql(rfd.getFormulasMap());
			for (String key : old_formulas.keySet()) {
				List<String> oldFs = old_formulas.get(key);
				List<String> newFs = new_formulas.get(key);
				for(String formula : newFs){
					if(!oldFs.contains(formula)){
						AfValidateformula afFormula = new AfValidateformula();
						afFormula.setCellId(new Long(1));
						afFormula.setFormulaName(formula);
						afFormula.setFormulaValue(formula);
						String[] t_v = key.split("_");
						afFormula.setTemplateId(t_v[0]);
						afFormula.setValidateTypeId(formula.indexOf("_")==-1?new Long(1):new Long(2));
						afFormula.setVersionId(t_v[1]);
						DBFomulaDelegate.saveAfValidateFormula(afFormula);
						
						DBFomulaDelegate.saveAfPreFormula("2", String.valueOf(afFormula.getFormulaId()));
					}
				}
			}
			result=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result =false;
		}
		return result;
	}
	
}
