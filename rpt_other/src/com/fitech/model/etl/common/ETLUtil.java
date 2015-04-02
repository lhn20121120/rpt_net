package com.fitech.model.etl.common;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ETLUtil {

	/**
	 *  解析存储过程表达式返回包含两个object的数组
	  * @Title: getProBody 
	  * @author: xfc100
	  * @date: Feb 29, 2012  4:52:24 PM
	  * @param procedureExpress
	  * @return Object[]
	 */
	public static Object[] parseProBody(String procedureExpress){
		Object[] os = new Object[2];
		String[] strs = procedureExpress.split("\\(");
		String proName = strs[0];
		String procedure = strs[1].replaceAll("[()]", "");
		String[] params = procedure.split(",");
		int paramCount = params.length;
		StringBuffer sql = new StringBuffer("");
		for(int i=0;i<paramCount;i++)
			sql.append("?,");
		os[0] = "{call " + proName + "(" + sql.substring(0, sql.length()-1) + ")}"; //{call pro_load_test(?,?)}
		List paramList = new ArrayList();
		for(int i=0;i<params.length;i++){
			String[] ps = params[i].split(" ");
			String[] _ps = {ps[0].trim(),ps[1].trim()};
			paramList.add(_ps);
		}
		os[1] = paramList;
		return os;
	}
	public static Object[] parseScriptBody(String procedureExpress){
		Object[] os = new Object[2];
		String[] strs = procedureExpress.split("\\(");
		String proName = strs[0];
		String procedure = strs[1].replaceAll("[()]", "");
		String[] params = procedure.split(",");
		int paramCount = params.length;
		StringBuffer sql = new StringBuffer("");
		for(int i=0;i<paramCount;i++)
			sql.append("?,");
		os[0] = "{call " + proName + "(" + sql.substring(0, sql.length()-1) + ")}"; //{call pro_load_test(?,?)}
		List paramList = new ArrayList();
		for(int i=0;i<params.length;i++){
			String[] ps = params[i].split(" ");
			String[] _ps = {ps[0].trim(),ps[1].trim()};
			paramList.add(_ps);
		}
		os[1] = paramList;
		return os;
	}
	/**
	 *  执行windows脚本
	  * @Title: RunScript 
	  * @author: xfc100
	  * @date: Apr 12, 2012  2:10:11 PM
	  * @param scriptPath
	  * @throws Exception void
	 */
	public static void runBatScript(String scriptPath)throws Exception{
    	Process process = null;
		InputStreamReader ir = null;
		LineNumberReader input = null;
		// runTime 启动规则
		try {
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(scriptPath);
			ir = new InputStreamReader(process.getInputStream());
			input = new LineNumberReader(ir);
			String line;
//			process.waitFor();
			while ((line = input.readLine()) != null)
				System.out.println(line);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				ir.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	public static void runSHScript(String scriptPath)throws Exception{
    	Process process = null;
		InputStreamReader ir = null;
		LineNumberReader input = null;
		// runTime 启动规则
		try {
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(scriptPath);
	//		process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",scriptPath},null,null);
			ir = new InputStreamReader(process.getInputStream());
			input = new LineNumberReader(ir);
			String line;
			while ((line = input.readLine()) != null)
				System.out.println(line);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				ir.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	public static String getProcOverFileName(Integer taskMoniId,Integer procId)throws Exception{
		String overFileName = "ok_task_" + taskMoniId + "_" + procId + ".txt";
		return overFileName;
	}
	public static String getProcScriptFileName(Integer taskMoniId,Integer procId)throws Exception{
		String fileName = "script_" + taskMoniId + "_" + procId;
		return fileName;
	}
	public static String getScriptInitSuccFileName(Integer taskMoniId,Integer procId)throws Exception{
		String overFileName = "initsucc_task_" + taskMoniId + "_" + procId + ".txt";
		return overFileName;
	}
	/**
	 * 根据正则式提取指定字符串中的字符
	 * @param srcStr
	 * @param reg
	 * @return
	 * @throws Exception
	 */
	public static  List<String> findMatcList(String srcStr,String reg)throws Exception{
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(srcStr);
		while(m.find())
			result.add(srcStr.substring(m.start(), m.end()));
		return result;
	}
	public static void main(String[] args) throws Exception{
//		String str = "pro_dsfads(date ddd,orgId ddd)";
//		String[] strs = str.replaceAll("[()]", "").split("\\(");
//		System.out.println(strs.length);
		String path = "E:\\test.bat E:\\综合报送平台\\武汉农商行\\设计文档\\脚本实例\\20120331";
		Runtime.getRuntime().exec("cmd /c mkdir e:\\FITETL");
//		runScript(path);
	}
}
