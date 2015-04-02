package com.fitech.institution.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFCollectRelationDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;

public class ReadFormulaFileDelegate {
	private static FitechException log = new FitechException(
			AFCollectRelationDelegate.class);

	public Map<String, List<String>> readFormulaFile(File files[]) {
		Map<String, List<String>> formulasMap = null;
		List<String> formulas = null;
		BufferedReader br = null;
		formulasMap = new HashMap<String, List<String>>();
		List<String> binbiaoList = DBFomulaDelegate.getAllStandardTemplateId();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if ((file.isFile()) && (!(file.getName().endsWith("zip")))
					&& (!(file.getName().endsWith("rar")))
					&& (file.getName().startsWith("A")) && (file.isFile())) {
				String templateId = file.getName().substring(0,
						file.getName().indexOf("."));
				br = null;
				try {
					br = new BufferedReader(new FileReader(file));
					String str = null;
					if (binbiaoList.contains(templateId)) {
						while ((str = br.readLine()) != null) {
							if (str.contains("null")||str.toLowerCase().contains("if")) {
								log.println(templateId + "无效公式：" + str);
							} else {
								List<String> items = ConverFormulaDelegate
										.splitFormula(str);
								if (items != null && items.size() > 0) {
									String firstStr = items.get(0);
									String curr = firstStr.split(",")[2];
									String reportName = DBFomulaDelegate
											.getTemplateId(curr, templateId);
									if (!StringUtil.isEmpty(reportName)) {
										String versionId  = DBFomulaDelegate.queryVersion(reportName);
										AfTemplate af = null;
										if(versionId!=null ){
											 af = AFTemplateDelegate.getTemplate(reportName, versionId);
										}
										if (af!=null&& af.getUsingFlag()==1){
											if(!formulasMap.containsKey(reportName)) {
												formulasMap.put(reportName,
														new ArrayList<String>());
											}
											formulasMap.get(reportName).add(str);
										}
									}
								}

							}
						}
					} else {
						formulas = new ArrayList<String>();
						while ((str = br.readLine()) != null) {
							if (str.contains("null")||str.toLowerCase().contains("if")) { 
								log.println(templateId + "无效公式：" + str);
							}else{
								formulas.add(str);
							}
						}
						formulasMap.put(templateId, formulas);
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						if (br != null)
							br.close();
						br = null;
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} finally {
					try {
						if (br != null)
							br.close();
						br = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return formulasMap;
	}
}