package com.fitech.fomula.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.FileUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.service.AFCollectRelationDelegate;

public class ReadFormulaFileDelegate {
	
	private static FitechException log = new FitechException(
			AFCollectRelationDelegate.class);
	
	private String tempCatalog;
	
	private Map<String,List> formulasMap = new HashMap<String,List>();

	public String getTempCatalog() {
		return tempCatalog;
	}

	public void setTempCatalog(String tempCatalog) {
		this.tempCatalog = tempCatalog;
	}

	public Map<String, List> getFormulasMap() {
		return formulasMap;
	}

	public void setFormulasMap(Map<String, List> formulasMap) {
		this.formulasMap = formulasMap;
	}
	
	/**
	 * 去指定的解压目录读取公式文件
	 * 以模板编号=所有公式的形式保存映射关系
	 * @param filePath
	 * @return
	 */
	public void readFormulaFile(File file){
		if(file.isFile() && !file.getName().endsWith("zip")
				&& !file.getName().endsWith("rar")
				&& file.getName().startsWith("A")){
			List<String> formulas = new ArrayList<String>();
			BufferedReader br = null;
			
			try {
				br = new BufferedReader(new FileReader(file));

				String str = null;
				while ((str = br.readLine()) != null) {
					formulas.add(str);
				}

				formulasMap.put(file.getName().substring(0, file.getName().indexOf(".")), formulas);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
					br = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(file.isDirectory()){
			for(File f : file.listFiles()){
				readFormulaFile(f);
			}
		}
	}
	
	public void distory(){
		if(!StringUtil.isEmpty(tempCatalog)){
			FileUtil.deleteFile(tempCatalog);
		}
	}
	
}
