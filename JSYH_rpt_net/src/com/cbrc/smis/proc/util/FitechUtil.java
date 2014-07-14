package com.cbrc.smis.proc.util;

/**
 * 自定义工具类
 * 
 * @author rds
 * @date 2006-06-02
 */
public class FitechUtil {
	/**
	 * 月报月份
	 */
	private static final int[] MONTHREPORTTERMS={1,2,3,4,5,6,7,8,9,10,11,12};
	/**
	 * 季报月份
	 */
	private static final int[] QUARTERREPORTTERMS={3,6,9,12};
	/**
	 * 半年报月份
	 */
	private static final int[] HALFYEARREPORTTERMS={6,12};
	/**
	 * 年报月份
	 */
	private static final int[] YEARREPORTTERMS={12};
	
	/**
	 * 判断本月是否需要报送
	 *  1	'月'
	 *	2	'季'
	 *	3	'半年'
	 *	4	'年'
	 * 
	 * @param repFreqId int 频度ID
	 * @param term Integer 报表月份
	 * @return boolean
	 */
	public static boolean isNeedReport(int repFreqId,Integer term){
		boolean res=false;
		if(term==null) return res;
		
		switch(repFreqId){
			case 1:
				res=true;
				break;
			case 2:
				res=isContaint(term.intValue(),QUARTERREPORTTERMS);
				break;
			case 3:
				res=isContaint(term.intValue(),HALFYEARREPORTTERMS);
				break;
			case 4:
				res=isContaint(term.intValue(),YEARREPORTTERMS);
				break;
		}
		
		return res;
	}
	
	/**
	 * 判断指定的数值是否在指定的数组中
	 * 
	 * @param val int 需查找的数值
	 * @param vals int[] 查找的数据源
	 * @return boolean  
	 */
	private static boolean isContaint(int val,int[] vals){
		if(vals==null) return false;
		
		boolean res=false;
		
		for(int i=0;i<vals.length;i++){
			if(vals[i]==val){
				res=true;
				break;
			}
		}
		
		return res;
	}
	/**
	 * 将  100000-sum(120000,120001)
	 * 
	 * 格式字符串解析成字符数组
	 * @param fomual
	 * @return
	 * @throws Exception
	 */
	public static String[] parseFomual(String fomual)throws Exception{
		String[] strs = null;
		if(fomual!=null && !fomual.equals("")){
			strs = fomual.trim().split("-");
			if(strs!=null && strs.length>0){
				strs[0] =strs[0].trim();
				String s1 = strs[1].substring(strs[1].indexOf("(")+1,strs[1].lastIndexOf(")"));
				strs[1] = s1.trim();
 			}
			if(strs==null || strs.length==0)
				throw new Exception("公式格式不正确.......");
		}
		return strs;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(parseFomual("100000-sum(htjy)").length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
