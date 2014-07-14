package com.fitech.net.obtain.text;


	import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.common.CommMethod;
import com.fitech.net.config.Config;
	
	public class manage {
		/*  计算公式  */
	private String formula="sum(0)+sum(1)+sum(3)";
	private String pasedformula=null;
	private String pasedformula1=null;
	/*   分隔符  */
	private char fenge=',';
	/*  txt文件位置加名字 */
	private 	 String filename="D://test//satmx.txt";
	
	/* 机构的列*/
	private int gigoucolumn=3;
	/**行列号**/
	private String rowcolumn="";
	//模板名字
	private String reportname=null;
	/* 机构表 */
	private  List orgList=new ArrayList();
	/*机构对应的数据表*/
	private  List tempshuju=new ArrayList();
	
	private List formulaList=new ArrayList();
	private List dataList=new ArrayList();
	
	File file =null;
	
	BufferedReader in = null;
public boolean GetAll()
{
	parse();
	if(this.openfile()==false)return false;
	calculate();
     this.closefile();
	for (int i=0;i<orgList.size();i++)
	{
	Formula f=new Formula(replace(i));
	((orgdatabean)orgList.get(i)).setResult(f.getResult());
	}
	//插入数据
	for(int i=0;i<orgList.size();i++)
	{
		String filepath=Config.getReleaseTemplatePath() +File.separator +((orgdatabean)orgList.get(i)).getOrgId()+File.separator +reportname;
		// System.out.println(filepath);
		
		ImportDataToExcel importexcel=new ImportDataToExcel();
		if (!importexcel.FileExit(filepath))
		{
			String filep=Config.getReleaseTemplatePath ()+File.separator +((orgdatabean)orgList.get(i)).getOrgId();
			
			File fi=new File(Config.getTemplateFolderRealPath()+File.separator +reportname);
			try 
			{
			if(copyFile(filep,fi)==false)
			continue;
			}
			catch(Exception ex)
			{
				continue;
			}
		}
		try
		{
		
		importexcel.openWorkBook(filepath);
		importexcel.selectSheetByIndex(0);
		importexcel.setCellValueAsString(rowcolumn,String.valueOf(((orgdatabean)orgList.get(i)).getResult()));
		importexcel.save(filepath);
		}
		catch(Exception e)
		{
			e.printStackTrace();
	
			return false;
		}
		finally
		{
		importexcel.close();	
		}
		
	}
	return true;
	
}


		
		/**
		 * 拷贝文件
		 * 
		 * @author 
		 * @param file1文件夹目的地
		 *            
		 * @param file源文件 
		 *           
		 */
		public boolean copyFile(String file1, File file) throws IOException {
			// File file = new File(file2);
			(new File(file1)).mkdirs();
			if (file.isFile()) {
				FileInputStream input = new FileInputStream(file);
				FileOutputStream output = new FileOutputStream(file1
						+ "\\" + reportname);
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
			else return false;
			return true;

		}
	public static void main(String[] args) 
	{
		
		
		 manage abc=new manage();
		 abc.openfile();
		 try
		 {
			 while(true)
			 {
		String temp= abc.in.readLine();
		// System.out.println(temp);
		if(temp==null||temp.equals(""))
			return;
			 }
		 }
		 catch(Exception e)
		 {
			 
		 }
		 abc.closefile();
	//	 abc.calculate();
	//	Formula f=new Formula( abc.replace(0));
	//	// System.out.println(f.getResult());
		// abc.display();
		 
		 
//            Formula f=new Formula("((1+2+5)(*5)+1");
//            if (f.checkValid()==true)
//            	// System.out.println("ok");
//            else // System.out.println("false");
            
//           
//		// System.out.print("sfdfsd");
//		s abc=new s();
//		abc.calculate();
//		abc.display();
//		
//		String temss="A34";
//		// System.out.print(temss.trim().substring(0,1));
//		// System.out.print(temss.substring(1,temss.length()));
		
	}
	/*初试化*/
	public void init()
	{
		
	}
	private void getformuladata()
	{
		for(int i=0;i<orgList.size();i++)
		{
			
		}
	}
	//校验
	public int  check()
	{
		
		boolean result=false;
		
		this.parse();
		//校验行
		for(int i=0;i<this.dataList.size();i++)
		{
			result=CommMethod.IsNumber(dataList.get(i).toString());
			if(result==false)
				return 2;
			if( dataList.get(i).toString().trim().equals(String.valueOf(gigoucolumn)))
			{
				return 3;
			}
		}
		
		if(dataList.size()!=formulaList.size()||dataList.size()<1)return 4;
		Formula f=new Formula(this.pasedformula1);
		if(!f.checkValid())
			return 4;
		
		
		return 0;
	}
	
	/**
	 * 把[0]+[1]+[2]里面的0，1，2替换成机构数据
	 * @param org
	 * @return
	 */
	private String replace(int org)
	{
		String tempf=pasedformula;
		int begin=0;int end=0;
		while(true)
		{
			
			 begin =tempf.indexOf("[");
			 end =tempf.indexOf("]");
			// System.out.println(tempf+String.valueOf(begin)+end);
			if (begin!=-1&&end!=-1)
			{
				int column=Integer.parseInt(tempf.substring(begin+1,end));
				tempf=tempf.substring(0,begin )+((orgdatabean)orgList.get(org)).getDatalist().get(column).toString()+tempf.substring(end+1);
				// System.out.println(tempf);
			}
			else
			{
				return tempf;
			}
		}
	}
	/*打开文件*/
	  private boolean openfile()
	  {
		  try
		  {
			  file= new File(filename);
			  if (!file.exists())return false;
		  in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GB2312"));
	
		  }
		  catch(IOException e)
		    {
			  
		     e.printStackTrace();
		     return false;
		    }
	return true;
	
	  }
	  /*关闭文件*/
	  private void  closefile()
	  {
		  try
		    {
		     in.close();
		    }
		    catch(IOException e)
		    {
		     e.printStackTrace();
		    }
	
	  }
	  /* 
	   * 对取的一行数据解析
	   * 得到它的各个字段放在List里
	   * 
	   * */
	 public  List GetLineList (  )
	  {
		 List StringList=new ArrayList();
		 try
		    {
			 String strTemp =in.readLine();
			 
			 if(strTemp==null||strTemp.trim()=="")
				 return null;
			 int length=strTemp.length();
			 String tempList="";
			 int i=-1;
			 while(length!=0)
			 {		i++;
				 char ctemp=strTemp.charAt(i);
				 if(ctemp!=fenge)
					 tempList+=ctemp;
				 else
				 {
					 
					 StringList.add(tempList.trim());
					 tempList="";
				 }
				 length--;
			 }
		    }
		    catch(IOException e)
		    {
		     e.printStackTrace();
		    }
		 
		 
		  return StringList;
	  }
	 
	 /*
	  * 具体实现
	  * 
	  * */
	 public void calculate()
	 {
		 int tempshu=0;
		
		 
		List tempList=this.GetLineList();
		// System.out.println(gigoucolumn);
		int templength=tempList.size();
		int lastlength=templength;
		while(true)
		{
			
			if(templength!=lastlength)
				break;
			
				
			
			if(tempList!=null&&tempList.size()!=0)
			{
				templength=tempList.size();
				String org=tempList.get(gigoucolumn-1).toString();
				int orgAt=getOrg(org);
				if(orgAt==-1)
				{
					tempList=this.GetLineList();
					lastlength=templength;
					continue;
				}
				int orgcount=((orgdatabean)orgList.get(orgAt)).getCount();
				((orgdatabean)orgList.get(orgAt)).setCount(orgcount+1);
			//	// System.out.println(orgAt);
				orgdatabean on=(orgdatabean)orgList.get(orgAt);
				List thisorgList=on.getDatalist();
				if (thisorgList==null)
					thisorgList=new ArrayList();
				
				for(int i=0;i<formulaList.size();i++)
				{
					String formula=formulaList.get(i).toString().trim();
					int temp=Integer.parseInt( dataList.get(i).toString())-1;
					double data=0;
					if (thisorgList.size()<i+1)
					{
						
					}
					else 
					{
						data=Double.parseDouble(thisorgList.get(i).toString());
						thisorgList.remove(i);
						
					}
					if(formula.equals("sum"))
					{
						
						data+=Double.parseDouble(tempList.get(temp).toString());
					}
					else if (formula.equals("ave"))
					{
						
						data+=Double.parseDouble(tempList.get(temp).toString());
					}
					else if(formula.equals("max"))
					{
						double tempdata=Double.parseDouble(tempList.get(temp).toString());
						if (tempdata>data)
							data=tempdata;
					}
					else if(formula.equals("min"))
					{
						double tempdata=Double.parseDouble(tempList.get(temp).toString());
						if (tempdata<data)
							data=tempdata;
					}
					else{
						
					}
					thisorgList.add(i,String.valueOf(data));
					// System.out.println(thisorgList.size());
				}
				on.setDatalist(thisorgList);
				orgList.remove(orgAt);
				orgList.add(orgAt,on);
				
			}
			else
			{
				break;
			}
			tempList=this.GetLineList();
			lastlength=templength;
			
		}
		this.closefile();
		for(int i=0;i<formulaList.size();i++)
		{
			if(formulaList.get(i).toString().trim().equals("ave"))
			{
				for(int j=0;j<orgList.size();j++)
				{
					int orgtemp=Integer.parseInt(((orgdatabean)orgList.get(j)).getDatalist().get(i).toString());
					orgtemp=orgtemp/(((orgdatabean)orgList.get(j)).getCount());
					((orgdatabean)orgList.get(j)).getDatalist().remove(i);
					((orgdatabean)orgList.get(j)).getDatalist().add(i,String.valueOf(orgtemp));
					
				}
			}
		}
	  
	 }
	 private int getOrg(String org)
	 {
		org=org.trim();
		int i=0;
		for(;i<orgList.size();i++)
		{
			orgdatabean o=(orgdatabean)orgList.get(i);
			if(o.getOrgId().equals(org))
				return i;
		}
		//list里没有机构加入机构
		//判断机构是否存在
		boolean result=StrutsOrgNetDelegate.OrgExist(org);
		if (result!=false)
		{
		orgdatabean onew=new orgdatabean();
		onew.setOrgId(org);
		orgList.add(onew);
		return i;
		}
		else return -1;
	 }
	 /*
	  * 机构放在tempjigou的哪一列
	  * */
	 private int getjigouid(List temp)
	 {
//		String tempstring=(String)temp.get(gigoucolumn);
//		for(int i=0;i<tempjigou.size();i++)
//		{		
//				if((tempjigou.get(i))!=tempstring.trim())
//				{
//				return i;
//				}
//		}
//			
		 return -1;
	 }
	 
	 public void display()
	 {
		 
		// // System.out.print((String)tempjigou.get(0)+(String)tempshuju.get(0));
		
		 // System.out.println(((orgdatabean)orgList.get(0)).getDatalist().get(1));
		 // System.out.println(((orgdatabean)orgList.get(0)).getOrgId());
		 // System.out.println(orgList.size());
		 // System.out.println(((orgdatabean)orgList.get(0)).getDatalist().size());
		
	 }
	 
	 /*
	  * 把sum(1)+sum(2)变成1+2的形式
	  * 
	  * */
	private	 String  parse()
		 {
		formulaList.clear();
		dataList.clear();
			 int length=formula.length();
			 String tempstr=new String();
			 String tempstr1=new String();
			 String tempFor=new String();
			 int arrayId=0;
			 int temp=0;
			 while(temp<length)
			 {
				
				 char ctemp=formula.charAt(temp);
				 if(ctemp>='a'&&ctemp<='z') 
					 {
					 
					 while(ctemp!='(')
					 {
						 
					 tempFor+=ctemp;
					 temp++;
					 ctemp=formula.charAt(temp);
					 
					 }
					 if(!tempFor.equals(""))
					 formulaList.add(tempFor);
					 tempFor="";
					 temp++;
					 ctemp=formula.charAt(temp);
					 while(ctemp!=')')
					 {
						 tempFor+=ctemp;
						 temp++;
						 ctemp=formula.charAt(temp);
					 }
					 if(!tempFor.equals(""))
					 dataList.add(tempFor);
					 tempFor="";
					 
					 tempstr=tempstr+"["+arrayId+"]";
					 tempstr1=tempstr1+arrayId;
					 arrayId++;
					 }
				 else
				 {
					 tempstr+=ctemp;
					 tempstr1+=ctemp;
				 }
			 temp++;
		 }
			 
			 pasedformula=tempstr;
			 pasedformula1=tempstr1;
			 
		return tempstr;
	 }
		 
	
		 /*
	private	String  parse ()
		 {
			char forword=null;
			int length=formula.length();
			String tempStr="";
			while(length!=0)
			{	
				length--;
				char ctemp=formula.charAt(length);
				if(ctemp==' ')
					{continue;}
				if((ctemp>='a'&&ctemp<='z')||(ctemp>='A'&&ctemp<='Z'))
				{
					if(forword!=null)
						
				}
				else if(ctemp>='0'&&ctemp<='9')
				{
					
				}
				else 
				{
					
					switch (ctemp)
					{
						case '+':   return "+";
						case '-':   return "-";
						case '*':   return "*";
						case '/':    return "/";
						default :break;
					}
				}
				
			}
		 }*/
	private String getColumn(String tempString)
	{
		String temp=new String();
		int i=0;
		boolean ok=false;
		while(true)
		{
			char ctemp=tempString.charAt(i);
			if(ctemp==',')
				break;
			if(ok==true)
				temp+=ctemp;
			if(ctemp=='(')
				ok=true;
			
			
		}
		return temp.trim();
	}
	private String getrow(String tempString)
	{
		String temp=new String();
		int i=0;
		boolean ok=false;
		while(true)
		{
			char ctemp=tempString.charAt(i);
			if(ctemp==')')
				break;
			if(ok==true)
				temp+=ctemp;
			if(ctemp==',')
				ok=true;
			
			
		}
		return temp.trim();
	}
	private String getformula(String tempString)
	{
		String temp=new String();
		int i=0;
		boolean ok=false;
		while(i<tempString.length())
		{
			char ctemp=tempString.charAt(i);
			
			if(ok==true)
				temp+=ctemp;
			if(ctemp=='=')
				ok=true;
			
			
		}
		return temp;
	}
	public char getFenge() {
		return fenge;
	}
	public void setFenge(char fenge) {
		this.fenge = fenge;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public int getGigoucolumn() {
		return gigoucolumn;
	}
	public void setGigoucolumn(int gigoucolumn) {
		this.gigoucolumn = gigoucolumn;
	}
	public String getReportname() {
		return reportname;
	}
	public void setReportname(String reportname) {
		this.reportname = reportname;
	}
	public String getRowcolumn() {
		return rowcolumn;
	}
	public void setRowcolumn(String rowcolumn) {
		this.rowcolumn = rowcolumn;
	}
	}
