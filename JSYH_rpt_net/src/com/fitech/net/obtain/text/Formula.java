package com.fitech.net.obtain.text;


/**
 * <p>Title: ��ʽ������</p>
 * <p>Description: ʵ�ֶ԰�����()���ı��ʽ���м򵥵��������㡣</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author leasion
 * @version 1.0
 */
/**
* ԭ����:http://dev2dev.bea.com.cn/bbs/thread.jspa?forumID=124&threadID=14861&tstart=90
* �޸���:lkz
* �޸�˵����
*     1.���ӶԸ�������֧�֣�
*     2.���ӶԹ�ʽ�ļ�飻
*     3.���Ӷ����ƹ�ʽ(A+B)+(C-D)��֧�֡�
*     4.����һЩС�ĸĶ���
*
*/

import java.util.Vector;

public class Formula {
	
    private int leftBracket = 0;//�����Ÿ���
    private int rightBracket = 0;//�����Ÿ���
    private int startL = 0;
    private int startR = 0;
    private double answer = 0;
    private String strValue="";
    private String leftNumber = "0";
    private String rightNumber = "0";
    public String Msg = "";
    private String formula="";
    private int[] sym = new int[4];
    private Vector list = new Vector();

    public Formula(String calRule){
      this.setFormula(calRule);
    }
    
    private int getLeftBracket(String calRule) {
        leftBracket = 0;
        startL = calRule.indexOf("(");
        if (startL != -1) {
            calRule = calRule.substring(startL + 1, calRule.length());
        }
        while (startL != -1) {
            leftBracket++;
            startL = calRule.indexOf("(");
            calRule = calRule.substring(startL + 1, calRule.length());
        }
        return leftBracket;
    }

    private void setLeftBracket(int leftBracket) {
        this.leftBracket = leftBracket;
    }
    public void setFormula(String calRule){
      formula=replaceSubtration(calRule.trim());
      formula="("+formula+")";
    }
    /*
    /*Ϊ��ʹ��ʽ��֧�ָ�����ʹ�á�`����ʾ���ţ�ʹ�á�-����ʾ����
    */
    private String replaceSubtration(String vstr){
      String tmp="";
      String result="";
      int startS = vstr.indexOf("-");
      if (startS !=-1) {
        if (startS > 0) {
          tmp = vstr.substring(startS - 1, startS);
          if (!"+".equals(tmp) && !"-".equals(tmp) && !"*".equals(tmp) &&!"/".equals(tmp) &&
             !"(".equals(tmp)){
            result = result + vstr.substring(0, startS) + "`";
          }
          else
            result = result + vstr.substring(0, startS + 1);
        }
        else
          result = result + vstr.substring(0, startS + 1);
       vstr = vstr.substring(startS + 1);
      }
      while (startS != -1) {
        startS = vstr.indexOf("-");
        if (startS > 0) {
          tmp = vstr.substring(startS - 1, startS);
          if (!"+".equals(tmp) && !"-".equals(tmp) && !"*".equals(tmp) &&!"/".equals(tmp) &&
             !"(".equals(tmp))
            result = result + vstr.substring(0, startS) + "`";
          else
            result = result + vstr.substring(0, startS + 1);
        }
        else
          result = result + vstr.substring(0, startS + 1);
          vstr = vstr.substring(startS + 1);
      }
      result+=vstr;
      return result;
    }

    public String getFormula(){
      return formula.replace('`','-').substring(1,formula.length()-1);
    }

    private int getRightBracket(String calRule) {
        rightBracket = 0;
        startR = calRule.indexOf(")");
        if (startR != -1) {
            calRule = calRule.substring(startR + 1, calRule.length());
        }
        while (startR != -1) {
            rightBracket++;
            startR = calRule.indexOf(")");
            calRule = calRule.substring(startR + 1, calRule.length());
        }
        return rightBracket;
    }

    private void setRightBracket(int rightBracket) {
        this.rightBracket = rightBracket;
    }

    /*
    /*�Ա��������Ÿ���
    */
    private boolean compareToLR() {
        int lb = getLeftBracket(formula);
        int rb = getRightBracket(formula);
        boolean CTLR = false;
        if (lb == rb) {
            Msg = "";
            CTLR = true;
        } else if (lb > rb) {
            Msg = "�������ĸ������������������飡";
            CTLR = false;
        } else {
            Msg = "�������ĸ������������������飡";
            CTLR = false;
        }
        return CTLR;
    }
   /*
   /*��鹫ʽ���Ƿ���ڷǷ��ַ���(+��-)��
   */
   private boolean checkFormula(){
      boolean isOk=true;
      String[] bracket =new String[2];
      String[] sign=new String[6];
      bracket[0]="(";
      bracket[1]=")";
      sign[0]="+";
      sign[1]="`";
      sign[2]="*";
      sign[3]="/";
      sign[4]="?";
      sign[5]=":";
      String vstr="";
      for(int i=0;i<bracket.length;i++){
        for(int j=0;j<sign.length;j++){
          if (i==0)
          {        
        	  vstr=bracket[i]+sign[j];
          }            
          else
          {        	 
        	  vstr=sign[j]+bracket[i];
          }
            
          if (formula.indexOf(vstr)>0){
        	 
            Msg="��ʽ�д��ڷǷ��ַ�"+vstr;
            isOk=false;
            return isOk;
          }
        }
      }
      for(int i=0;i<sign.length;i++){
        for(int j=0;j<sign.length;j++){
          vstr=sign[i]+sign[j];
          if (formula.indexOf(vstr)>0){
        	
              Msg="��ʽ�д��ڷǷ��ַ�"+vstr;
              isOk=false;
              return isOk;
          }
        }
      }
      if (formula.indexOf("()")>0){
        Msg="��ʽ�д��ڷǷ��ַ�()";
        isOk=false;
      }
      return isOk;
    }
   public boolean checkValid(){
	   
     if ((formula==null) || (formula.trim().length()<=0) ) {
       Msg="����������calRule!";
       return false;
     }
    
     return (compareToLR()&&checkFormula());
   }

 
  
  public double getResult(){
	    String formulaStr = "", calRule = "";
	    double value = 0.0;
	    calRule = this.formula;

	    String s1=null;
	    String s2=null;
	    String s3=null;
	    if (checkValid()) {
	      for (int i = 0; i < leftBracket; i++) {
	        int iStart=calRule.lastIndexOf("(") + 1;
	        formulaStr = calRule.substring(iStart,
	                                       iStart+calRule.substring(iStart).indexOf(")")).trim();    
	        if(formulaStr.indexOf("?")>-1)
	        {
	        	 s1=formulaStr.substring(0,formulaStr.indexOf("?"));
	        	 s2=formulaStr.substring(formulaStr.indexOf("?")+1,formulaStr.indexOf(":"));
	        	s3=formulaStr.substring(formulaStr.indexOf(":")+1,formulaStr.length());
	        	if(s1.indexOf(">")>-1)
	        	{        		
	        		String s11=s1.substring(0,s1.indexOf(">"));
	        		String s12=s1.substring(s1.indexOf(">")+1,s1.length());        		        		
	        		//value=(3>2)? Double.parseDouble(s1) : Double.parseDouble(s2);    
	        		if(Double.parseDouble(s11)>Double.parseDouble(s12))        			
	        		{
	        			value=Double.parseDouble(s2);
	        		}else
	        		{
	        			value=Double.parseDouble(s3);
	        		}        		
	        	}
	        	if(s1.indexOf("<")>-1)
	        	{        		
	        		String s11=s1.substring(0,s1.indexOf("<"));
	        		String s12=s1.substring(s1.indexOf("<")+1,s1.length());        		        		
	        		//value=(3>2)? Double.parseDouble(s1) : Double.parseDouble(s2);    
	        		if(Double.parseDouble(s11)<Double.parseDouble(s12))        			
	        		{
	        			value=Double.parseDouble(s2);
	        		}else
	        		{
	        			value=Double.parseDouble(s3);
	        		}        		
	        	}
	        	if(s1.indexOf("=")>-1)
	        	{        		
	        		String s11=s1.substring(0,s1.indexOf("="));
	        		String s12=s1.substring(s1.indexOf("=")+1,s1.length());        		        		
	        		//value=(3>2)? Double.parseDouble(s1) : Double.parseDouble(s2);    
	        		if(Double.parseDouble(s11)==Double.parseDouble(s12))        			
	        		{
	        			value=Double.parseDouble(s2);
	        		}else
	        		{
	        			value=Double.parseDouble(s3);
	        		}        		
	        	}
	        }else{
	        	  symbolParse(formulaStr);
	              value = parseString();
	        }
	 
	        iStart=calRule.lastIndexOf("(");
	        int iEnd=calRule.substring(iStart).indexOf(")")+1;
	     
	        calRule = calRule.substring(0,iStart).trim() +
	            value +
	            calRule.substring(iStart+iEnd, calRule.length()).trim();
	     
	      }
	    }
	    double tmp = Math.pow(10, 10);
	    value = Math.round(value * tmp) / tmp;
	    
	    return value;
	   }
  
    public void FormulaStr(String calRule) {
        String formulaStr = "";
        if (checkValid()) {
            for (int i = 0; i < leftBracket; i++) {
                formulaStr = calRule.substring(calRule.lastIndexOf("(") + 1, calRule.indexOf(")")).trim();
                symbolParse(formulaStr);
                double value = parseString();
                strValue=String.valueOf(value);
               
                //formulaVal = Double.parseDouble(formulaStr);
           //     // System.out.println("formulaVal=" + value);
                calRule = calRule.substring(0, calRule.lastIndexOf("(")).trim() + value + calRule.substring(calRule.indexOf(")") + 1, calRule.length()).trim();
             //   // System.out.println("calRule=" + calRule);
            }
        }
    }

    /*
    /*��ȡ�������������ݵ�List
    */
    private void symbolParse(String str) {
        list.clear();
        for (int i = 0; i < 4; i++) {
            compareMin(str);
            while (sym[i] != -1) {
                String insStr = str.substring(0, sym[i]).trim();
                list.add(insStr);
                insStr = str.substring(sym[i], sym[i] + 1).trim();
                list.add(insStr);
                str = str.substring(sym[i] + 1, str.length()).trim();
                compareMin(str);
            }
        }
        if (sym[0] == -1 && sym[1] == -1 && sym[2] == -1 & sym[3] == -1) {
            list.add(str);
        }
    }
     /*
    /*ѭ���Ƚϸ�SubString��ʼֵ
    */
    private void compareMin(String str) {
        int sps = str.indexOf("`");//����subtration
        sym[0] = sps;
        int spa = str.indexOf("+");//�ӷ�addition
        sym[1] = spa;
        int spd = str.indexOf("/");//����division
        sym[2] = spd;
        int spm = str.indexOf("*");//�˷�multiplication
        sym[3] = spm;
        for (int i = 1; i < sym.length; i++) {
            for (int j = 0; j < sym.length - i; j++)
                if (sym[j] > sym[j + 1]) {
                    int temp = sym[j];
                    sym[j] = sym[j + 1];
                    sym[j + 1] = temp;
                }
        }
    }

    private double parseString()
            throws NumberFormatException, StringIndexOutOfBoundsException {
        try{
          calculate();
          return answer;
        }catch(Exception e){
          Msg="����" + e.getMessage();
          return 0.0;
        }
    }

    private void calculate() {
        /*
        /*�������
        */
        int spd = list.indexOf("/");
        while (spd != -1) {
            leftNumber = list.get(spd - 1).toString();
            rightNumber = list.get(spd + 1).toString();
            list.remove(spd - 1);
            list.remove(spd - 1);
            list.remove(spd - 1);
            double ln = Double.valueOf(leftNumber).doubleValue();
            double rn = Double.valueOf(rightNumber).doubleValue();
            double answer;
            if(rn==0.00)
            {
            	answer=0.00;
            }else
            {
            	answer = ln / rn;
            }
           
            list.add(spd - 1, String.valueOf(answer));
            spd = list.indexOf("/");
        }
        /*
        /*����˷�
        */
        int spm = list.indexOf("*");
        while (spm != -1) {
            leftNumber = list.get(spm - 1).toString();
            rightNumber = list.get(spm + 1).toString();
            list.remove(spm - 1);
            list.remove(spm - 1);
            list.remove(spm - 1);
            double ln = Double.valueOf(leftNumber).doubleValue();
            double rn = Double.valueOf(rightNumber).doubleValue();
            double answer = ln * rn;
            list.add(spm - 1, String.valueOf(answer));
            spm = list.indexOf("*");
        }
        /*
        /*�������
        */
        int sps = list.indexOf("`");
        while (sps != -1) {
            leftNumber = list.get(sps - 1).toString();
            rightNumber = list.get(sps + 1).toString();
            list.remove(sps - 1);
            list.remove(sps - 1);
            list.remove(sps - 1);
            double ln = Double.valueOf(leftNumber).doubleValue();
            double rn = Double.valueOf(rightNumber).doubleValue();
            double answer = ln - rn;
            list.add(sps - 1, String.valueOf(answer));
            sps = list.indexOf("`");
        }
        /*
        /*����ӷ�
        */
        int spa = list.indexOf("+");
        while (spa != -1) {
            leftNumber = list.get(spa - 1).toString();
            rightNumber = list.get(spa + 1).toString();
            list.remove(spa - 1);
            list.remove(spa - 1);
            list.remove(spa - 1);
 
            double ln = Double.valueOf(leftNumber).doubleValue();
            double rn = Double.valueOf(rightNumber).doubleValue();
            double answer = ln + rn;
            list.add(spa - 1, String.valueOf(answer));
            spa = list.indexOf("+");
        }
        if (list.size() != 0) {
            String result = list.get(0).toString();
            if (result == null || result.length() == 0) result = "0";
            answer = Double.parseDouble(list.get(0).toString());
        }
    }
}


