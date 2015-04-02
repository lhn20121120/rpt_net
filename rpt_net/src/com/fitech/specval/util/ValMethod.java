package com.fitech.specval.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fitech.gznx.common.StringUtil;


public class ValMethod {
	/**
	 * �ַ�����У��
	 * @param formula У�鹫ʽ
	 * @param value	У��ֵ
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
				if(str_1.equals("Y")){//���valueֵΪ�գ���str1��Y
					return true;
				}else{
					return false;
				}
			}else{
				int len = value.length();//value��Ϊ��
System.out.println(len);
				if(str_0.indexOf("-")!=-1){//���ֵ������ֵ
					//System.out.println(str.indexOf("-"));
					str_0_0 = Spilt_0_0(str_0);
					str_0_1 = Spilt_0_1(str_0);
System.out.println(str_0_0+","+str_0_1);
					if((len>=Integer.parseInt(str_0_0))&&(len<=Integer.parseInt(str_0_1))&&str_1.equals("N")){
						return true;
					}else{
						return false;
					}
				}else{//�����ǵ�ֵ
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
	 * �ַ�����������У��
	 * @param formula	У�鹫ʽ
	 * @param value		У��ֵ
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
			if(value==null || value.trim().equals("")){//���valueֵΪ��,�����ڰ���
				result = false;
			}else{
				if(str_0.indexOf("|")!=-1){//���ֵ�зָ���
					String[] arr = str_0.split("\\|");
					for(int i=0;i<arr.length;i++){
						if((value.contains(arr[i])&&str_1.equals("Y"))||(!value.contains(arr[i])&&str_1.equals("N"))){
							return true;
						}else{
							result = false;
						}
						
					}	
				}else{//�����ǵ�ֵ
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
	 * �ַ�����У��
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
			int index = Integer.parseInt(str_0)-1;//��ʾ�ִ��ڼ�λ
			char indexstr = value.charAt(index);//�õ�ĳһλ�õ��ַ�
System.out.println(indexstr);
			if(str_1.indexOf("-")!=-1){//��ʽ�Ǻ�-�ַ�
				arr = str_1.split("-");								
				if((indexstr>=arr[0].trim().toCharArray()[0])&&(indexstr<=arr[1].trim().toCharArray()[0])){
					result = true;
				}else{
					result = false;
				}
			}else if(str_1.indexOf("|")!=-1){//��ʽ�Ǻ�|�ַ�
				arr = str_1.split("\\|");

				if((arr[0].trim().equals(String.valueOf(indexstr).trim()))||(arr[1].trim().equals(String.valueOf(indexstr).trim()))){
					result = true;
				}else{
					result = false;
				}
			}else{//��ʽ��ֻ�ǵ����ַ�
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
	 * �ӷ�����
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
	 * ��ֵ��У��
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
			str_0 = Spilt_0(str);//��������
			str_1 = Spilt_1(str); //С������
			str_2 = Spilt_2(str);//Y/N
System.out.println(str_0+","+str_1+","+str_2);
			if(value==null || value.trim().equals("")){
				if(str_2.equals("Y")){//���valueֵΪ�գ���str2��Y
					result = true;
				}else{
					result = false;
				}
			}else{
				try{
					Double.valueOf(value);// ��������ֵ������ֵ��
				}catch(Exception e){
					return false;
				}
				if(value.indexOf(".")!=-1){//��ֵ��С��
					int value_0 = value.split("\\.")[0].length();//�������ֳ���
					int value_1 = value.split("\\.")[1].length();//С�����ֳ���
System.out.println(value_0+","+value_1);
					if((value_0<=Integer.parseInt(str_0))&&(value_1<=Integer.parseInt(str_1))&&(str_2.equals("N"))){
						result = true;
					}else{
						result = false;
					}
				}else{//��ֵֻ����������
					int value_0 = value.split("\\.")[0].length();//�������ֳ���
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
	 * ��ֵ��СУ��
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
			if(value==null || value.trim().equals("")){//���valueֵΪ��,�޳���
				result = false;
	
			}else{
				char[] c = str_0.toCharArray();
				StringBuffer sc_0 = new StringBuffer(c.length);
				StringBuffer sc_1 = new StringBuffer(c.length);
				for(int i=0;i<c.length;i++){//�����ֺ�������ֿ�
					if(c[i]>='0'&& c[i]<='9'){
						try{
							sc_1.append(c[i]);//����
System.out.println("---"+sc_1);
						}catch(Exception e){
							return false;
						}
					}else{
						try{
							sc_0.append(c[i]);//�����
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
	 * �ַ��ظ���У��
	 * @param value
	 * @return
	 */
	public static boolean charRepeat(String value){
		boolean result = false;
		String str =null;
		
		if(value==null || value.trim().equals("")){//���valueֵΪ��
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
					 if(temp==c[i]){//n���ظ��ı���n-i��
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
	 * ���ڸ�ʽУ��
	 * @param formula
	 * @param value
	 * @return
	 */
	public static boolean dateFormat(String formula,String value){
		boolean result = false;
		String str =null;
		str = fetchValue(formula);	
System.out.println(str);
		if(value==null || value.trim().equals("")){//���valueֵΪ��
			return false;
		}else{
			 try {
				 SimpleDateFormat dateFormat = new SimpleDateFormat(str);//�Զ���ĸ�ʽ
		         dateFormat.setLenient(false);
		         dateFormat.parse(value);
		         return true;
		    } catch (Exception e) {
		    	return false;
		    }
		}	
	}
	/**
	 * ��λ��ֵ����У��
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
			if(value==null || value.trim().equals("")){//���valueֵΪ��,�޳���
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
		// ��������У��
		if(StringUtil.isEmpty(value) && !regionMap.containsKey(value)){
			return false;
		}
		return true;
	}
	
	 /**
	  * 
	  * @param value ���ֵ֤
	  * @param type ���֤����
	  * @param regionid ��������
	  * @return
	  */
	public static boolean creditval(String value,char type,List regionidlist){
		boolean result = false;
		if(type=='A'){
			if(value==null || value.trim().equals("")){//���valueֵΪ��
				return false;
			}else{
				char[] c = value.toCharArray();
				if(value.length()==10 || value.length()==11){//����Ϊ10λ��11λ
					//Ϊ�ж��Ƿ�����ַ�
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
					//�ж��Ƿ���ȫ�ظ�
					char temp = ' ';
					int count = 0;
					for(int i=1;i<c.length;i++){
						 temp = c[i-1];
						 if(temp==c[i]){//n���ظ��ı���n-i��
							 count++;
							 
						 }
					}
					count = count + 1;//��ʾ�ж��ٸ�Ԫ���ظ�
System.out.println("count"+count);
					if(count==value.length()){//ȫ��Ԫ���ظ�
						return false;	
					}else{
						result = true;
					}
				}else if(value.length()==15){//����Ϊ15λ
					//Ϊ�ж��Ƿ�ֻ������
					for(int i=0;i<value.length();i++){
						char t = c[i];
						if(t>='0'&&t<='9'){		
							result = true;
						}else{
							return false;
						}
					}
					//�ж�ǰ2λ�Ƿ�����������
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
				}else if(value.length()==18){//����Ϊ18λ
					//Ϊ�ж�ǰ17λ�Ƿ�ֻ������
					for(int i=0;i<17;i++){
						char t = c[i];
						if(t>='0'&&t<='9'){		
							result = true;
						}else{
							return false;
						}
					}
					//�ж����һλ
					if((value.charAt(17)>='0'&&value.charAt(17)<='9')||value.charAt(17)=='X'){
						result = true;
					}else{
						return false;
					}
					//�ж����¹淶
					String date = value.substring(6, 14);
					boolean isFomat = dateFormat(date);
					if(!isFomat){
						return false;
					}else{
						result = true;
					}
					//�ж�ǰ2λ�Ƿ�����������
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
					
					//��18λУ��
					boolean resultvalid = valid18Bit(value);
					if(!resultvalid){
						return false;
					}else{
						result= true;
					}
				}else{//�������Ȳ�����
					return false;
				}
			}
		}else if(type=='K'){
			if(value==null || value.trim().equals("")){//K����Ϊ��,
				result= true;
			}else{
				return false;
			}
		}else if(type>='B'&& type<='I'){
			if(value==null || value.trim().equals("")){//���valueֵΪ��
				return false;
			}else{
				//�ж��Ƿ���ȫ�ظ�
				char[] c = value.toCharArray();
				char temp = ' ';
				int count = 0;
				for(int i=1;i<c.length;i++){
					 temp = c[i-1];
					 if(temp==c[i]){//n���ظ��ı���n-i��
						 count++;
						 
					 }
				}
				count = count + 1;//��ʾ�ж��ٸ�Ԫ���ظ�
System.out.println("count"+count);
				if(count==value.length()){//ȫ��Ԫ���ظ�
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
	 * ���ڸ�ʽУ��
	 */
	private static boolean dateFormat(String date){
		 try {
			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");//�Զ���ĸ�ʽ
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
		 } catch (Exception e) {
       	return false;
		 }
	}
	
	
	/**
	 * ��18λУ��
	 */
	private static boolean valid18Bit(String value){
		char[] ch = value.toCharArray();
		int[] in =new int[18];
		for(int i=0;i<17;i++){
			 in[i] =Integer.parseInt(String.valueOf(ch[i])) ;
		}
System.out.println(in[1]);
		//17λ���ֱ���Բ�ͬ��ϵ��,��� |7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
		int sum = in[0]*7 + in[1]*9 + in[2]*10 + in[3]*5 +in[4]*8 +
					in[5]*4 + in[6]*2+ in[7]*1 + in[8]*6 + in[9]*3 +
					in[10]*7 + in[11]*9 + in[12]*10 + in[13]*5 +in[14]*8 +
					in[15]*4 + in[16]*2;
System.out.println("sum---"+sum);

		//�üӳ����ͳ���11���������Ƕ��� |1 0 X 9 8 7 6 5 4 3 2
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
