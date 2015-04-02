package com.fitech.specval.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fitech.specval.adapter.Region;

import net.sourceforge.jeval.Evaluator;

public class ValRole {
	
	/**
	 * 解析formula，选择
	 * @param formula 校验公式
	 * @param value	校验值
	 * @param type 证件类型
	 * @param regionidlist 行政区号
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
	 * 同列重复校验	SameColrepeat([str])
	 *
	 * @param formula 公式
	 * @param cellMap 单元格map
	 * @param cowCount 列数量 (table af_qd_cellinfo中维护)
	 * @return
	 */
	public boolean sameColrepeat(String formula,Map cellMap,int startRow ,int colCount){
		
		boolean result = false;
		
		if(formula==null || formula.trim().equals("")
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		//取出公式校验列
		formula = this.fetchFormula(formula,"(",")");
		
		if(formula.trim().equals(""))
			return result;
		
		String value = "";  //单元格值
		String values = ""; //
		
		int rows = cellMap.size()/colCount;
		
		for(int i = startRow ; i<rows+startRow ; i++){
			
			value = (String) cellMap.get(formula + i);
			
			//如果有相同的则返回false
			if (values.contains(value)) return result;
			
			values += value + ",";
			
		}

		result = true;
		
		return result;
	}


	/**
	 * 	同行重复校验	SameRowrepeat（[str],[str],<str>, ...）<br>
	 * 	[str] 列号，如“A”
	 * @param formula 公式
	 * @param cellMap 单元格map
	 * @param cowCount 列数量 (table af_qd_cellinfo中维护)
	 * @return
	 */
	public boolean SameRowrepeat(String formula,Map cellMap,int startRow ,int colCount){
		
		boolean result = false;
		String res = ""; //记录第几行不符合
		
		if(formula == null || formula.trim().equals("")
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		
		//取出公式校验列
		formula = this.fetchFormula(formula,"(",")");
		
		if(formula.trim().equals(""))
			return result;
		
		String formus[] = formula.split(",");
		
		//取得报表行数
		int rows = cellMap.size()/colCount;
		
		String value = "";  //单元格值
		String values = ""; //
		
		int matched=0; //找到匹配的数量
		
		//遍历行
		for(int i = startRow ; i< rows+startRow; i++){

			for (int j=0;j<formus.length;j++){
				
				value = (String) cellMap.get(formus[j] + i);
				
				//如果有相同的则返回false
				if (values.contains(value)) 
					matched++;
				
				values += value + ",";
			}
			
			if(matched>0)
				res += "第" + i + "行,";

		}
		
		if(res.trim().equals("")) 
			result = true;
		else
			result = false;
		
		return result;
	}
	
	/**
	 * 	同行多列数值大小校验	MoreColNumSize([str],[str],<str>,…,[con]) <br>
	 * 	[str] 列号，如“A”
	 *  [con] 比较值，如“>=3000”
	 * @param formula 公式
	 * @param cellMap 单元格map
	 * @param cowCount 列数量 (table af_qd_cellinfo中维护)
	 * @return
	 */
	public boolean moreColNumSize(String formula,Map cellMap,int startRow,int colCount){
		
		boolean result = false;
		String res = ""; //记录第几行不符合
		
		if(formula == null || formula.trim().equals("")	
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		
		//取出公式校验列
		formula = this.fetchFormula(formula,"(",")");
		
		if(formula.trim().equals(""))
			return result;
		
		String formus[] = formula.split(",");
		
		//取得报表行数
		int rows = cellMap.size()/colCount;
		
		String value = "";  //单元格值
		
		String role = formus[formus.length-1];
		
		Evaluator evaluator = new Evaluator();		
		
		int notMatch = 0; //记录不符合条数
		
		//遍历行
		for(int i = startRow ; i<rows+startRow ; i++){

			notMatch = 0;
			
			for (int j=0;j<formus.length-1;j++){
				
				value = (String) cellMap.get(formus[j] + i);
				
				try{
					//如果不符合则+1
					if (evaluator.evaluate(value+role).equals("0.0")) 
						notMatch++;
				}catch(Exception e){
					notMatch++;
				}
			}
			
			if(notMatch==(formus.length-1))
				res += "第" + i + "行,";
		}
		
		if(res.trim().equals("")) 
			result = true;
		else
			result = false;
		
		return result;
	}
	
	/**
	 * 	同行多列逻辑判断校验	IF([str],[con])THEN {[fun]}<br>
	 * 	[str] 列号，如“A”
	 *  [con] 比较字符，如“B”
	 *  <br>例：当A列等于“A”时，B列按身份证规则校验 IF{A,"A"} THEN{B,creditval()}
	 * @param formula 公式
	 * @param cellMap 单元格map
	 * @param cowCount 列数量 (table af_qd_cellinfo中维护)
	 * @return
	 */
	public boolean ifThen(String formula,Map cellMap,int startRow,int colCount){
		
		boolean result = false;
		String res = ""; //记录第几行不符合
		
		if(formula == null || formula.trim().equals("") 
				|| cellMap == null || cellMap.isEmpty() 
				|| colCount<1)
			return result;
		
		//取得报表行数
		int rows = cellMap.size()/colCount;
		
		String formus[] = formula.split("IF");
		
		//公式条件要>=1
		if(formus.length-1<1) return false;

		//if-then组合队列
		String formuFormat[][]=new String[formus.length-1][4];
		
		int notMatch = 0; //记录不符合条数
		
		//条件循环，看有多少个if-then，拆出的第一个不要，因为是""
		for(int cond=0;cond<formus.length-1;cond++){
			
			String spCond[] = formus[cond+1].trim().split("THEN");
			
			//没有条件直接返回
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

		String value1 = "";  //单元格值1
		String value2 = "";	 //单元格值2
		
		
		//遍历行
		for(int i = startRow ; i<rows+startRow ; i++){

			notMatch = 0;
			
			for(int fTimes = 0 ; fTimes<formuFormat.length ; fTimes++){
				
				value1 = (String) cellMap.get(formuFormat[fTimes][0] + i);
				value2 = (String) cellMap.get(formuFormat[fTimes][2] + i);
				
				//是比较则
				if(this.isCompare(formuFormat[fTimes][0])){
					
					
					try{
						//如果不符合则+1
						if (evaluator.evaluate(value1+formuFormat[fTimes][1]).equals("0.0")) 
							notMatch++;
						else{
							//调用匹配函数(只允许调单一函数校验)
						}
							
						
					}catch(Exception e){
						notMatch++;
					}
					
					
				}else{
					
					if (!value1.contains(formuFormat[fTimes][1]))
						notMatch++;
					else{
						//调用匹配函数(只允许调单一函数校验)
						List regionidlist = Region.getRegionid();
						char val = value1.charAt(0);
						this.chooseVal(formuFormat[fTimes][3], value2,val, regionidlist);
					}
					
				}
				
			}
			
			
			if(notMatch==(formus.length-1))
				res += "第" + i + "行,";
		}
		
		if(res.trim().equals("")) 
			result = true;
		else
			result = false;
		
		return result;
	}
	
	/**
	 * 是否是比较
	 * @param str 
	 * @return 是为true
	 */
	private boolean isCompare(String str){

		if(str==null || str.trim().equals(""))
			return false;
			
		if(str.contains(">") || str.contains(">") || str.contains("="))
			return true;
		
		
		return false;
	}
	
	/**
	 * 用於解析公式刮號中的字符串
	 * @param formula 公式
	 * @param leftAmbit 左界
	 * @param rightAmbit 右界
	 * @return
	 */
	private String fetchFormula(String formula,String leftAmbit,String rightAmbit){

		int startIndex = formula.indexOf(leftAmbit);
		int endIndex = formula.lastIndexOf(rightAmbit);
		formula = formula.substring(startIndex+1, endIndex);
		return formula;
	}
	
}
