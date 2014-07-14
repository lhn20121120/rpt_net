package com.cbrc.smis.proc.util;

/**
 * �Զ��幤����
 * 
 * @author rds
 * @date 2006-06-02
 */
public class FitechUtil {
	/**
	 * �±��·�
	 */
	private static final int[] MONTHREPORTTERMS={1,2,3,4,5,6,7,8,9,10,11,12};
	/**
	 * �����·�
	 */
	private static final int[] QUARTERREPORTTERMS={3,6,9,12};
	/**
	 * ���걨�·�
	 */
	private static final int[] HALFYEARREPORTTERMS={6,12};
	/**
	 * �걨�·�
	 */
	private static final int[] YEARREPORTTERMS={12};
	
	/**
	 * �жϱ����Ƿ���Ҫ����
	 *  1	'��'
	 *	2	'��'
	 *	3	'����'
	 *	4	'��'
	 * 
	 * @param repFreqId int Ƶ��ID
	 * @param term Integer �����·�
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
	 * �ж�ָ������ֵ�Ƿ���ָ����������
	 * 
	 * @param val int ����ҵ���ֵ
	 * @param vals int[] ���ҵ�����Դ
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
	 * ��  100000-sum(120000,120001)
	 * 
	 * ��ʽ�ַ����������ַ�����
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
				throw new Exception("��ʽ��ʽ����ȷ.......");
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
