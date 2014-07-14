package com.fitech.specval.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fitech.specval.adapter.Region;

import net.sourceforge.jeval.Evaluator;

public class ValRole {
	
	/**
	 * ����formula��ѡ��
	 * @param formula У�鹫ʽ
	 * @param value	У��ֵ
	 * @param type ֤������
	 * @param regionidlist ��������
	 */
	public boolean chooseVal(String formula,String value,char type,List regionidlist){
		
		boolean result = false;
		
		if(formula == null || formula.trim().equals(""))
			return result;
		else{
			if(formula.contains("charlength")){
System.out.println(formula+","+value);
				result = ValMethod.CharLength(formula, value);
			}else if(formula.contains("charcontain")){
				result = ValMethod.CharContain(formula, value);
			}else if(formula.contains("charfilter")){
				result = ValMethod.charFilter(formula, value);
			}else if(formula.contains("charformat")){
				result = ValMethod.charFormat(value);
			}else if(formula.contains("numlength")){
				result = ValMethod.numLength(formula, value);
			}else if(formula.contains("numsize")){
				result = ValMethod.numSize(formula, value);
			}else if(formula.contains("charrepeat")){
				result = ValMethod.charRepeat(value);
			}else if(formula.contains("dateformat")){
				result = ValMethod.dateFormat(formula, value);
			}else if(formula.contains("creditval")){
				result = ValMethod.creditval(value, type, regionidlist);
			}else{
				result = false;
			}
		}
		
		
		return result;
	}
	
	

	
	/**
	 * ͬ���ظ�У��	SameColrepeat([str])
	 *
	 * @param formula ��ʽ
	 * @param cellMap ��Ԫ��map
	 * @param cowCount ������ (table af_qd_cellinfo��ά��)
	 * @return
	 */
	public boolean sameColrepeat(String formula,Map cellMap,int startRow ,int colCount){
		
		boolean result = false;
		
		if(formula==null || formula.trim().equals("")
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		//ȡ����ʽУ����
		formula = this.fetchFormula(formula,"(",")");
		
		if(formula.trim().equals(""))
			return result;
		
		String value = "";  //��Ԫ��ֵ
		String values = ""; //
		
		int rows = cellMap.size()/colCount;
		
		for(int i = startRow ; i<rows+startRow ; i++){
			
			value = (String) cellMap.get(formula + i);
			
			//�������ͬ���򷵻�false
			if (values.contains(value)) return result;
			
			values += value + ",";
			
		}

		result = true;
		
		return result;
	}


	/**
	 * 	ͬ���ظ�У��	SameRowrepeat��[str],[str],<str>, ...��<br>
	 * 	[str] �кţ��硰A��
	 * @param formula ��ʽ
	 * @param cellMap ��Ԫ��map
	 * @param cowCount ������ (table af_qd_cellinfo��ά��)
	 * @return
	 */
	public boolean SameRowrepeat(String formula,Map cellMap,int startRow ,int colCount){
		
		boolean result = false;
		String res = ""; //��¼�ڼ��в�����
		
		if(formula == null || formula.trim().equals("")
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		
		//ȡ����ʽУ����
		formula = this.fetchFormula(formula,"(",")");
		
		if(formula.trim().equals(""))
			return result;
		
		String formus[] = formula.split(",");
		
		//ȡ�ñ�������
		int rows = cellMap.size()/colCount;
		
		String value = "";  //��Ԫ��ֵ
		String values = ""; //
		
		int matched=0; //�ҵ�ƥ�������
		
		//������
		for(int i = startRow ; i< rows+startRow; i++){

			for (int j=0;j<formus.length;j++){
				
				value = (String) cellMap.get(formus[j] + i);
				
				//�������ͬ���򷵻�false
				if (values.contains(value)) 
					matched++;
				
				values += value + ",";
			}
			
			if(matched>0)
				res += "��" + i + "��,";

		}
		
		if(res.trim().equals("")) 
			result = true;
		else
			result = false;
		
		return result;
	}
	
	/**
	 * 	ͬ�ж�����ֵ��СУ��	MoreColNumSize([str],[str],<str>,��,[con]) <br>
	 * 	[str] �кţ��硰A��
	 *  [con] �Ƚ�ֵ���硰>=3000��
	 * @param formula ��ʽ
	 * @param cellMap ��Ԫ��map
	 * @param cowCount ������ (table af_qd_cellinfo��ά��)
	 * @return
	 */
	public boolean moreColNumSize(String formula,Map cellMap,int startRow,int colCount){
		
		boolean result = false;
		String res = ""; //��¼�ڼ��в�����
		
		if(formula == null || formula.trim().equals("")	
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		
		//ȡ����ʽУ����
		formula = this.fetchFormula(formula,"(",")");
		
		if(formula.trim().equals(""))
			return result;
		
		String formus[] = formula.split(",");
		
		//ȡ�ñ�������
		int rows = cellMap.size()/colCount;
		
		String value = "";  //��Ԫ��ֵ
		
		String role = formus[formus.length-1];
		
		Evaluator evaluator = new Evaluator();		
		
		int notMatch = 0; //��¼����������
		
		//������
		for(int i = startRow ; i<rows+startRow ; i++){

			notMatch = 0;
			
			for (int j=0;j<formus.length-1;j++){
				
				value = (String) cellMap.get(formus[j] + i);
				
				try{
					//�����������+1
					if (evaluator.evaluate(value+role).equals("0.0")) 
						notMatch++;
				}catch(Exception e){
					notMatch++;
				}
			}
			
			if(notMatch==(formus.length-1))
				res += "��" + i + "��,";
		}
		
		if(res.trim().equals("")) 
			result = true;
		else
			result = false;
		
		return result;
	}
	
	/**
	 * 	ͬ�ж����߼��ж�У��	IF([str],[con])THEN {[fun]}<br>
	 * 	[str] �кţ��硰A��
	 *  [con] �Ƚ��ַ����硰B��
	 *  <br>������A�е��ڡ�A��ʱ��B�а����֤����У�� IF{A,"A"} THEN{B,creditval()}
	 * @param formula ��ʽ
	 * @param cellMap ��Ԫ��map
	 * @param cowCount ������ (table af_qd_cellinfo��ά��)
	 * @return
	 */
	public boolean ifThen(String formula,Map cellMap,int startRow,int colCount){
		
		boolean result = false;
		String res = ""; //��¼�ڼ��в�����
		
		if(formula == null || formula.trim().equals("") 
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		
		//ȡ�ñ�������
		int rows = cellMap.size()/colCount;
		
		String formus[] = formula.split("IF");
		
		//��ʽ����Ҫ>=1
		if(formus.length-1<1) return false;

		//if-then��϶���
		String formuFormat[][]=new String[formus.length-1][4];
		
		int notMatch = 0; //��¼����������
		
		//����ѭ�������ж��ٸ�if-then������ĵ�һ����Ҫ����Ϊ��""
		for(int cond=0;cond<formus.length-1;cond++){
			
			String spCond[] = formus[cond+1].trim().split("THEN");
			
			//û������ֱ�ӷ���
			if(spCond.length!=2) return false;
			spCond[0] = this.fetchFormula(spCond[0], "{", "}");
			spCond[1] = this.fetchFormula(spCond[1], "{", "}");
			
			String ifs[] = spCond[0].split(",");
			if(ifs.length!=2) return false;
			
			String thens[] = spCond[1].split(",");
			if(ifs.length!=2) return false;
			
			
			formuFormat[cond][0] = ifs[0];
			formuFormat[cond][1] = ifs[1];
			formuFormat[cond][2] = thens[0];
			formuFormat[cond][3] = thens[1];
			
		}
				
		Evaluator evaluator = new Evaluator();		

		String value1 = "";  //��Ԫ��ֵ1
		String value2 = "";	 //��Ԫ��ֵ2
		
		
		//������
		for(int i = startRow ; i<rows+startRow ; i++){

			notMatch = 0;
			
			for(int fTimes = 0 ; fTimes<formuFormat.length ; fTimes++){
				
				value1 = (String) cellMap.get(formuFormat[fTimes][0] + i);
				value2 = (String) cellMap.get(formuFormat[fTimes][2] + i);
				
				//�ǱȽ���
				if(this.isCompare(formuFormat[fTimes][0])){
					
					
					try{
						//�����������+1
						if (evaluator.evaluate(value1+formuFormat[fTimes][1]).equals("0.0")) 
							notMatch++;
						else{
							//����ƥ�亯��(ֻ�������һ����У��)
						}
							
						
					}catch(Exception e){
						notMatch++;
					}
					
					
				}else{
					
					if (!value1.contains(formuFormat[fTimes][1]))
						notMatch++;
					else{
						//����ƥ�亯��(ֻ�������һ����У��)
						List regionidlist = Region.getRegionid();
						char val = value1.charAt(0);
						this.chooseVal(formuFormat[fTimes][3], value2,val, regionidlist);
					}
					
				}
				
			}
			
			
			if(notMatch==(formus.length-1))
				res += "��" + i + "��,";
		}
		
		if(res.trim().equals("")) 
			result = true;
		else
			result = false;
		
		return result;
	}
	
	/**
	 * �Ƿ��ǱȽ�
	 * @param str 
	 * @return ��Ϊtrue
	 */
	private boolean isCompare(String str){

		if(str==null || str.trim().equals(""))
			return false;
			
		if(str.contains(">") || str.contains(">") || str.contains("="))
			return true;
		
		
		return false;
	}
	
	/**
	 * ��춽�����ʽ��̖�е��ַ���
	 * @param formula ��ʽ
	 * @param leftAmbit ���
	 * @param rightAmbit �ҽ�
	 * @return
	 */
	private String fetchFormula(String formula,String leftAmbit,String rightAmbit){

		int startIndex = formula.indexOf(leftAmbit);
		int endIndex = formula.lastIndexOf(rightAmbit);
		formula = formula.substring(startIndex+1, endIndex);
		return formula;
	}
	
}
