package com.fitech.gznx.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Rui.Feng
 * Date: 2008-5-6
 * Time: 15:45:45
 */
public class StringUtil {

    public static boolean isEmpty(String input){
        if(input==null || input.trim().length()==0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isInt(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch(Exception exception){
            return false;
        }
    }

    public static boolean isLong(String input){
        try{
            Long.parseLong(input);
            return true;
        }catch(Exception exception){
            return false;
        }
    }

    public static boolean isFloat(String input){
        try{
            Float.parseFloat(input);
            return true;
        }catch(Exception exception){
            return false;
        }
    }
    
    public static boolean isDouble(String input){
        try{
            Double.parseDouble(input);
            return true;
        }catch(Exception exception){
            return false;
        }
    }

    public static String uninitCapital(String input){
        return input.substring(0,1).toLowerCase()+ input.substring(1);
    }
    
    /**
	 * String转换成Map
	 * @param content
	 * @returndingli
	 * 2008-6-5
	 */
	public static Map getMapbyString (String content) {
		Map map = new HashMap();
		String temp[] = content.split(",");
		for (int i=0; i<temp.length; i++){
			map.put(temp[i].trim(), null);
		}
		return map;
	}
	/**
	 * String[]转换成Map
	 * @param content
	 */
	public static Map getMapbyString (String[] content) {
		Map map = new HashMap();
		for (int i=0; i<content.length; i++){
			map.put(content[i].trim(), null);
		}
		return map;
	}
	/**
	 * 
	 * title:该方法用于将指定的对象类型将其用指定的界限符和分隔符将对象中的字符串加工成指定的字符串 author:chenbing
	 * author:chenbing date:2008-2-26
	 * 
	 * @param obj
	 *            指定的对象
	 * @param splitStr
	 *            分隔符
	 * @param bracketStr
	 *            界限符 如果为空则没有界限符
	 * @return
	 */
	public static String getStrsBySplitStr(Object obj, String splitStr,
			String bracketStr) {

		if (obj == null)
			return null;

		bracketStr = bracketStr == null ? "" : bracketStr;

		String result = null;

		StringBuffer sb = new StringBuffer("");

		if (obj instanceof ArrayList) { // 字符串列表

			List list = (List) obj;

			for (int i = 0; i < list.size(); i++)

				sb.append(splitStr + bracketStr + ((String) list.get(i)).trim()
						+ bracketStr);

		} else if (obj instanceof String) {// 字符串

			String str = (String) obj;

			String[] strs = str.split(",");

			for (int i = 0; i < strs.length; i++)

				sb.append(splitStr + bracketStr + strs[i].trim() + bracketStr);

		} else if (obj instanceof HashSet) {// Set

			Set set = (HashSet) obj;

			List list = new ArrayList();

			list.addAll(set);

			for (int i = 0; i < list.size(); i++)

				sb.append(splitStr + bracketStr + ((String) list.get(i)).trim()
						+ bracketStr);
		}
		if (sb.length() > 1)

			result = sb.substring(1);

		return result;
	}
	 // 删除上传的ZIP文件及解压文件夹
    public static void deleteUploadFile(String filePath) {

		File f = new File(filePath);
		if (f.isDirectory()) {
		    File[] fileList = f.listFiles();
		    if(fileList != null && fileList.length>0){
			    for (int i = 0; i < fileList.length; i++) {
					File excelFile = fileList[i];			
					excelFile.delete();
			    }
		    }
		    f.delete();
		}			
    }
}
