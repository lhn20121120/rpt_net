package com.fitech.specval.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fitech.gznx.common.StringUtil;


public class ValMethod {
	/**
	 * 字符长度校验
	 * @param formula 校验公式
	 * @param value	校验值
	 * @return
	 */
	public static boolean CharLength(String formula,String value){
		//boolean result = false;
		String str_0_0 = null;
		String str_0_1 = null;
		
		if(formula.indexOf(",")==-1){
			return false;
		}else{
			String str = fetchValue(formula);
			String str_0 = Spilt_0(str);
			String str_1 = Spilt_1(str); 
System.out.println(str_0+","+str_1);
			if(value==null || value.trim().equals("")){
				if(str_1.equals("Y")){//如果value值为空，且str1是Y
					return true;
				}else{
					return false;
				}
			}else{
				int len = value.length();//value不为空
System.out.println(len);
				if(str_0.indexOf("-")!=-1){//如果值是区间值
					//System.out.println(str.indexOf("-"));
					str_0_0 = Spilt_0_0(str_0);
					str_0_1 = Spilt_0_1(str_0);
System.out.println(str_0_0+","+str_0_1);
					if((len>=Integer.parseInt(str_0_0))&&(len<=Integer.parseInt(str_0_1))&&str_1.equals("N")){
						return true;
					}else{
						return false;
					}
				}else{//若果是单值
					if(len==Integer.parseInt(str_0)&&str_1.equals("N")){
						return true;
					}else{
						return false;
					}
					
				}
			}
		
		}
	}

	/**
	 * 字符串包含过滤校验
	 * @param formula	校验公式
	 * @param value		校验值
	 * @return
	 */
	public static boolean CharContain(String formula,String value){
		boolean result = false;
		String str =null;
		String str_0 = null;
		String str_1 = null;
		
		if(formula.indexOf(",")==-1){
			result = false;
		}else{
			str = fetchValue(formula);
			str_0 = Spilt_0(str);
			str_1 = Spilt_1(str); 
System.out.println(str_0+","+str_1);
			if(value==null || value.trim().equals("")){//如果value值为空,不存在包含
				result = false;
			}else{
				if(str_0.indexOf("|")!=-1){//如果值有分隔符
					String[] arr = str_0.split("\\|");
					for(int i=0;i<arr.length;i++){
						if((value.contains(arr[i])&&str_1.equals("Y"))||(!value.contains(arr[i])&&str_1.equals("N"))){
							return true;
						}else{
							result = false;
						}
						
					}	
				}else{//若果是单值
					if((value.contains(str_0)&&str_1.equals("Y"))||(!value.contains(str_0)&&str_1.equals("N"))){
						result = true;
					}else{
						result = false;
					}
					
				}
			}
		
		}
		return result;
	}

	/**
	 * 字符过滤校验
	 * @param formula
	 * @param value
	 * @return
	 */
	public static boolean charFilter(String formula,String value){
		boolean result = false;
		String str =null;
		String str_0 = null;
		String str_1 = null;
		String[] arr = null;
		if(formula.indexOf(",")==-1){
			result = false;
		}else{
			str = fetchValue(formula);
		    str_0 = Spilt_0(str);
			str_1 = Spilt_1(str); 
System.out.println(str_0+","+str_1);
			int index = Integer.parseInt(str_0)-1;//表示字串第几位
			char indexstr = value.charAt(index);//得到某一位置的字符
System.out.println(indexstr);
			if(str_1.indexOf("-")!=-1){//公式是含-字符
				arr = str_1.split("-");								
				if((indexstr>=arr[0].trim().toCharArray()[0])&&(indexstr<=arr[1].trim().toCharArray()[0])){
					result = true;
				}else{
					result = false;
				}
			}else if(str_1.indexOf("|")!=-1){//公式是含|字符
				arr = str_1.split("\\|");

				if((arr[0].trim().equals(String.valueOf(indexstr).trim()))||(arr[1].trim().equals(String.valueOf(indexstr).trim()))){
					result = true;
				}else{
					result = false;
				}
			}else{//公式是只是单个字符
				if(str_1.equals(String.valueOf(indexstr).trim())){
					result = true;
				}else{
					result = false;
				}
			}
		}
		return result;
	}
	
	/**
	 * 子符过滤
	 * @param value
	 * @return
	 */
	public static boolean charFormat(String value){
		boolean result = false;
		char[] strtoch = value.toCharArray();
		for(int i=0;i<strtoch.length;i++){
			char c = strtoch[i];
			if((c>='A'&&c<='Z')||(c>='0'&&c<='9')){
				result = true;
				//break;
			}else{
				result = false;
				break;
			}
		}
		
		return result;
	}

	/**
	 * 数值型校验
	 * @param formula
	 * @param value
	 * @return
	 */
	public static boolean numLength(String formula,String value){
		boolean result = false;
		String str =null;
		String str_0 = null;
		String str_1 = null;
		String str_2 = null;
		
		if(formula.split(",").length!=3){
			result = false;
		}else{
			str = fetchValue(formula);
			str_0 = Spilt_0(str);//整数长度
			str_1 = Spilt_1(str); //小数长度
			str_2 = Spilt_2(str);//Y/N
System.out.println(str_0+","+str_1+","+str_2);
			if(value==null || value.trim().equals("")){
				if(str_2.equals("Y")){//如果value值为空，且str2是Y
					result = true;
				}else{
					result = false;
				}
			}else{
				try{
					Double.valueOf(value);// 若传来的值不是数值型
				}catch(Exception e){
					return false;
				}
				if(value.indexOf(".")!=-1){//数值带小数
					int value_0 = value.split("\\.")[0].length();//整数部分长度
					int value_1 = value.split("\\.")[1].length();//小数部分长度
System.out.println(value_0+","+value_1);
					if((value_0<=Integer.parseInt(str_0))&&(value_1<=Integer.parseInt(str_1))&&(str_2.equals("N"))){
						result = true;
					}else{
						result = false;
					}
				}else{//数值只有整数部分
					int value_0 = value.split("\\.")[0].length();//整数部分长度
					if((value_0<=Integer.parseInt(str_0))&&(str_2.equals("N"))){
						result = true;
					}else{
						result = false;
					}
				}
				
			}
		
		}
		return result;
	}
	
	
	/**
	 * 数值大小校验
	 * @param formula
	 * @param value
	 * @return
	 */
	public static  boolean numSize(String formula,String value){
		boolean result = false;
		String str =null;
		String str_0 = null;
		String str_1 = null;
		
		if(formula.indexOf(",")==-1){
			result = false;
		}else{
			str = fetchValue(formula);
			str_0 = Spilt_0(str);
			str_1 = Spilt_1(str); //Y/N
			
System.out.println(str_0+","+str_1);
			if(value==null || value.trim().equals("")){//如果value值为空,无长度
				result = false;
	
			}else{
				char[] c = str_0.toCharArray();
				StringBuffer sc_0 = new StringBuffer(c.length);
				StringBuffer sc_1 = new StringBuffer(c.length);
				for(int i=0;i<c.length;i++){//把数字和运算符分开
					if(c[i]>='0'&& c[i]<='9'){
						try{
							sc_1.append(c[i]);//数字
System.out.println("---"+sc_1);
						}catch(Exception e){
							return false;
						}
					}else{
						try{
							sc_0.append(c[i]);//运算符
System.out.println("==="+sc_0);
						}catch(Exception e){
							return false;
						}
					}
				}
				if(sc_0.toString().equals(">")){
					if(Integer.parseInt(value)>Integer.parseInt(sc_1.toString())){
						if(str_1.equals("Y")){
							result = true;
						}else{
							result = false;
						}
					}else{
						if(str_1.equals("N")){
							result = true;
						}else{
							result = false;
						}
					}
				}else if(sc_0.toString().equals(">=")){
					System.out.println("+++"+Integer.parseInt(value));
					System.out.println("+=+"+Integer.parseInt(sc_1.toString()));
					if(Integer.parseInt(value)>=Integer.parseInt(sc_1.toString())){
						if(str_1.equals("Y")){
							result = true;
						}else{
							result = false;
						}
					}else{
						if(str_1.equals("N")){
							result = true;
						}else{
							result = false;
						}
					}
				}else if(sc_0.toString().equals("<")){
					if(Integer.parseInt(value)<Integer.parseInt(sc_1.toString())){
						if(str_1.equals("Y")){
							result = true;
						}else{
							result = false;
						}
					}else{
						if(str_1.equals("N")){
							result = true;
						}else{
							result = false;
						}
					}
				}else{
					if(Integer.parseInt(value)<=Integer.parseInt(sc_1.toString())){
						if(str_1.equals("Y")){
							result = true;
						}else{
							result = false;
						}
					}else{
						if(str_1.equals("N")){
							result = true;
						}else{
							result = false;
						}
					}
				}		
			}
		
		}
		return result;
	}

	/**
	 * 字符重复性校验
	 * @param value
	 * @return
	 */
	public static boolean charRepeat(String value){
		boolean result = false;
		String str =null;
		
		if(value==null || value.trim().equals("")){//如果value值为空
				return false;
		}else{
			char[] c = value.toCharArray();
			if(c.length<4){
				return true;
			}else{
				char temp = ' ';
				int count = 0;
				for(int i=1;i<c.length;i++){
					 temp = c[i-1];
					 if(temp==c[i]){//n个重复的比了n-i次
						 count = count + 1;
					 }else{
						 count = 0;
					 }
				}
System.out.println(count);
				if((count+1)>=4){
					return false;
				}else{
					return true;
				}
			}
		}	
	}

	/**
	 * 日期格式校验
	 * @param formula
	 * @param value
	 * @return
	 */
	public static boolean dateFormat(String formula,String value){
		boolean result = false;
		String str =null;
		str = fetchValue(formula);	
System.out.println(str);
		if(value==null || value.trim().equals("")){//如果value值为空
			return false;
		}else{
			 try {
				 SimpleDateFormat dateFormat = new SimpleDateFormat(str);//自定义的格式
		         dateFormat.setLenient(false);
		         dateFormat.parse(value);
		         return true;
		    } catch (Exception e) {
		    	return false;
		    }
		}	
	}
	/**
	 * 两位数值过滤校验
	 * @param formula
	 * @param value
	 * @return
	 */
	public static boolean numFilter(String formula,String value){

		String str =null;
		String str_0 = null;
		String str_1 = null;
		
		if(formula.indexOf(",")==-1){
			return false;
		}else{
			str = fetchValue(formula);
			str_0 = Spilt_0(str);
			str_1 = Spilt_1(str); //Y/N
			if(value==null || value.trim().equals("")){//如果value值为空,无长度
				return false;
			}else{
				try{
					int len = Integer.valueOf(str_0);
					if(value.length()<len || str_1.indexOf("-")<0){
						return false;
					}
					value = value.trim();
					len = value.length()-len;
					int minNum = Integer.valueOf(str_1.split("-")[0]);
					int maxNum = Integer.valueOf(str_1.split("-")[1]);
					int curNum = Integer.valueOf(value.substring(len));
					if(curNum >= minNum && curNum <= maxNum){
						return true;
					} else {
						return false;
					}
				} catch (Exception e){
					return false;
				}				
			}
		}
	}
	
	public static boolean regionId(String value,Map regionMap){
		// 行政区划校验
		if(StringUtil.isEmpty(value) && !regionMap.containsKey(value)){
			return false;
		}
		return true;
	}
	
	 /**
	  * 
	  * @param value 身份证值
	  * @param type 身份证类型
	  * @param regionid 行政区号
	  * @return
	  */
	public static boolean creditval(String value,char type,List regionidlist){
		boolean result = false;
		if(type=='A'){
			if(value==null || value.trim().equals("")){//如果value值为空
				return false;
			}else{
				char[] c = value.toCharArray();
				if(value.length()==10 || value.length()==11){//长度为10位或11位
					//为判断是否包含字符
					for(int i=0;i<value.length();i++){
						char t = c[i];
						if((t>='a'&& t<='z')||(t>='A'&&t<='Z')||(t>='0'&&t<='9')||
							t=='('||t==')'||t=='#'||t=='%'||t=='*'||t=='-'||t=='_'||
							t=='<'||t=='>'||t=='&'||t=='.'||t=='/'||t=='\\'){		
							result = true;
						}else{
							return false;
						}
					}
					//判断是否完全重复
					char temp = ' ';
					int count = 0;
					for(int i=1;i<c.length;i++){
						 temp = c[i-1];
						 if(temp==c[i]){//n个重复的比了n-i次
							 count++;
							 
						 }
					}
					count = count + 1;//表示有多少个元素重复
System.out.println("count"+count);
					if(count==value.length()){//全部元素重复
						return false;	
					}else{
						result = true;
					}
				}else if(value.length()==15){//长度为15位
					//为判断是否只是数字
					for(int i=0;i<value.length();i++){
						char t = c[i];
						if(t>='0'&&t<='9'){		
							result = true;
						}else{
							return false;
						}
					}
					//判断前2位是否是行政区号
					String v = value.substring(0, 2);
					for(Iterator it = regionidlist.iterator();it.hasNext();){
						String r = (String)it.next();
System.out.println(r);
						if(v.equals(r)){
							result = true;
						}else{
							return false;
						}
					}
				}else if(value.length()==18){//长度为18位
					//为判断前17位是否只是数字
					for(int i=0;i<17;i++){
						char t = c[i];
						if(t>='0'&&t<='9'){		
							result = true;
						}else{
							return false;
						}
					}
					//判断最后一位
					if((value.charAt(17)>='0'&&value.charAt(17)<='9')||value.charAt(17)=='X'){
						result = true;
					}else{
						return false;
					}
					//判断年月规范
					String date = value.substring(6, 14);
					boolean isFomat = dateFormat(date);
					if(!isFomat){
						return false;
					}else{
						result = true;
					}
					//判断前2位是否是行政区号
					String v = value.substring(0, 2);
					
					for(Iterator it = regionidlist.iterator();it.hasNext();){
						String r = (String)it.next();
System.out.println(r);
						if(v.equals(r)){
							result = true;
							break;
						}else{
							result =  false;
						}
					}
					
					//第18位校验
					boolean resultvalid = valid18Bit(value);
					if(!resultvalid){
						return false;
					}else{
						result= true;
					}
				}else{//其它长度不允许
					return false;
				}
			}
		}else if(type=='K'){
			if(value==null || value.trim().equals("")){//K类型为空,
				result= true;
			}else{
				return false;
			}
		}else if(type>='B'&& type<='I'){
			if(value==null || value.trim().equals("")){//如果value值为空
				return false;
			}else{
				//判断是否完全重复
				char[] c = value.toCharArray();
				char temp = ' ';
				int count = 0;
				for(int i=1;i<c.length;i++){
					 temp = c[i-1];
					 if(temp==c[i]){//n个重复的比了n-i次
						 count++;
						 
					 }
				}
				count = count + 1;//表示有多少个元素重复
System.out.println("count"+count);
				if(count==value.length()){//全部元素重复
					return false;	
				}else{
					result = true;
				}
			}
		}else{
			return false;
		}
	
			
		return result;
	}
	/**
	 * 日期格式校验
	 */
	private static boolean dateFormat(String date){
		 try {
			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");//自定义的格式
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
		 } catch (Exception e) {
       	return false;
		 }
	}
	
	
	/**
	 * 第18位校验
	 */
	private static boolean valid18Bit(String value){
		char[] ch = value.toCharArray();
		int[] in =new int[18];
		for(int i=0;i<17;i++){
			 in[i] =Integer.parseInt(String.valueOf(ch[i])) ;
		}
System.out.println(in[1]);
		//17位数分别乘以不同的系数,相加 |7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
		int sum = in[0]*7 + in[1]*9 + in[2]*10 + in[3]*5 +in[4]*8 +
					in[5]*4 + in[6]*2+ in[7]*1 + in[8]*6 + in[9]*3 +
					in[10]*7 + in[11]*9 + in[12]*10 + in[13]*5 +in[14]*8 +
					in[15]*4 + in[16]*2;
System.out.println("sum---"+sum);

		//用加出来和除以11，看余数是多少 |1 0 X 9 8 7 6 5 4 3 2
		int yu = sum % 11;
System.out.println("yu---"+yu);
		Map map = new HashMap();
		map.put(0, '1');
		map.put(1, '0');
		map.put(2, 'X');
		map.put(3, '9');
		map.put(4, '8');
		map.put(5, '7');
		map.put(6, '6');
		map.put(7, '5');
		map.put(8, '4');
		map.put(9, '3');
		map.put(10,'2');
		
		if(map.get(yu).toString().charAt(0)==value.charAt(17)){
			return true;
			
		}else{
			return false;
			
		}
		
	}
	
	

	private static String fetchValue(String str){
		//String str = "charlen(3-23,Y)";
		String str1 = null;
		int startIndex = str.indexOf("(");
		int endIndex = str.lastIndexOf(")");
		str1 = str.substring(startIndex+1, endIndex);
		return str1;
	}
	
	private static String Spilt_2(String formuItem) {
		String[] arr = null;
		String str = null;
		try {
			arr = formuItem.split(",");
			str = arr[2];
		} catch (Exception e) {
			str = null;
			e.printStackTrace();
		}
		return str;
	}
	
	private static String Spilt_1(String formuItem) {
		String[] arr = null;
		String str = null;
		try {
			arr = formuItem.split(",");
			str = arr[1];
		} catch (Exception e) {
			str = null;
			e.printStackTrace();
		}
		return str;
	}
	
	private static String Spilt_0(String formuItem) {
		String[] arr = null;
		String str = null;
		try {
			arr = formuItem.split(",");
			str = arr[0];
		} catch (Exception e) {
			str = null;
			e.printStackTrace();
		}
		return str;
	}
	
	private static String Spilt_0_1(String formuItem) {
		String[] arr = null;
		String str = null;
		try {
			arr = formuItem.split("-");
			str = arr[1];
		} catch (Exception e) {
			str = null;
			e.printStackTrace();
		}
		return str;
	}
	
	private static String Spilt_0_0(String formuItem) {
		String[] arr = null;
		String str = null;
		try {
			arr = formuItem.split("-");
			str = arr[0];
		} catch (Exception e) {
			str = null;
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
