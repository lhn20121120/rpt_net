/**
 * 这个类的目的是:<br>
 * 解析PDF报表模板,获得模板中的输入域和其他报表相关的头尾信息.<br>
 * 在当前这个类中,有两处处理的不是太好:<br>
 *   1、默认把PDF报表中的输入域的数据类型全设为了数值型；<br>
 *   2、是通过域的名称判断其是否是输入域；输入域名称的第一个字母是大写的英文字母，其它字符是合法的数值；<br>
 */
package com.cbrc.smis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.pdfbox.cos.COSName;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentCatalog;
import org.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.pdfbox.pdmodel.interactive.form.PDField;

import com.cbrc.smis.adapter.StrutsMCurUnitDelegate;
import com.cbrc.smis.adapter.StrutsReportDataDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ListingColsForm;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MMainRepForm;
import com.cbrc.smis.form.ReportDataForm;
/**
 * 解析PDF报表模板操作类
 * 
 * @author rds
 * @date 2005-12-1
 */
public class FitechPDF {
	private FitechException log=new FitechException(FitechPDF.class);

	public String CHARACTERS="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public String CHARACTERS_MATH="0123456789";
	
	/**
	 * PDF报表模板中的全部域
	 */
	private HashMap fields=null;
	
	/**
	 * PDF报表模板的类型(点对点式或清单式报表)
	 */
	private Integer reportStyle=null;
	
	/**
	 * 构造函数
	 */
	public FitechPDF(){}
	
	/**
	 * 解析PDF文件
	 * 
	 * @param input InputStream PDF文件二制流
	 * @return void 解析成功，返回true;否则，返回false
	 */
	public boolean parse(InputStream input){
		boolean result=false;
		PDDocument document=null;
		
		String _flag="";  //标识
		int i,j,k,g,h;
		List subFields=null,_subFields=null;
		PDField _field=null;
		try{
			//document=PDDocument.load(input);
			PDFParser pdfParser=new PDFParser(input);
			pdfParser.parse();
			document=pdfParser.getPDDocument();
						
			PDDocumentCatalog catalog=document.getDocumentCatalog();
			PDAcroForm acroForm=catalog.getAcroForm();
			
			PDAnnotation annotation=null;

			if(acroForm!=null){
				this.fields=new HashMap();
				
				List ancestorFields=acroForm.getFields();
				if(ancestorFields!=null){
					for(i=0;i<ancestorFields.size();i++){
						PDField field=(PDField)ancestorFields.get(i);

						List parentFields=field.getKids();

						if(parentFields!=null){
							for(j=0;j<parentFields.size();j++){
								field=(PDField)parentFields.get(j);
								_flag=field.getDictionary().getString(COSName.getPDFName("T"));
								List fields=field.getKids();
								if(fields==null) continue;
								for(g=0;g<fields.size();g++){
									subFields=null;
									_subFields=null;
									field=(PDField)fields.get(g);
									
									/************************************/
									if(field.getKids()!=null){ 
										if(_flag!=null && _flag.trim().equalsIgnoreCase("P1[0]")){   //分页PDF报表的解析
											this.reportStyle=Config.REPORT_STYLE_DD;
											
											subFields=field.getKids();
											
											if(subFields.size()>0){
												_field=null;
												for(h=0;h<subFields.size();h++){													
													_field=(PDField)subFields.get(h);
													FitechPDFField fitechPDFField=new FitechPDFField();
													fitechPDFField.setFieldName(_field.getPartialName());
													fitechPDFField.setFieldType(_field.getFieldType());
													fitechPDFField.setFieldValue(_field.getValue());
													fitechPDFField.setReadOnly(_field.isReadonly());
													
													fitechPDFField.setDataType(FitechPDFField.CELL_DATA_TYPE_INTEGER);
													
													annotation=(PDAnnotation)_field.getWidget();
													fitechPDFField.setVisibility(!annotation.isInvisible());
															
													/*// System.out.println(h + "\t" + 
															_field.getFieldType() + "\t" + 
															_field.getFullyQualifiedName() + "\t" +
															_field.getPartialName()+ "\t" +
															_field.getValue() + "\t" + 
															_field.isReadonly() + "\t" + 
															_field.getFieldFlags() + "\t" + 
															annotation.isInvisible());*/
															
													this.fields.put(fitechPDFField.getFieldName(),fitechPDFField);
													
												}
											}											
										}else{  //清单式PDF报表模板的处理
											this.reportStyle=Config.REPORT_STYLE_QD;
											
											subFields=field.getKids();
										
											if(subFields.size()>0){
												_subFields=((PDField)subFields.get(0)).getKids();
												
												if(_subFields!=null && _subFields.size()>0){
													_field=null;
													
													for(k=0;k<_subFields.size();k++){
														_field=(PDField)_subFields.get(k);
														FitechPDFField fitechPDFField=new FitechPDFField();
														fitechPDFField.setFieldName(_field.getPartialName());
														fitechPDFField.setFieldType(_field.getFieldType());
														fitechPDFField.setFieldValue(_field.getValue());
														fitechPDFField.setReadOnly(_field.isReadonly());
														
														annotation=(PDAnnotation)_field.getWidget();
														fitechPDFField.setVisibility(!annotation.isInvisible());
																											
														/*// System.out.println(k + "\t" + 
																_field.getFieldType() + "\t" + 
																_field.getFullyQualifiedName() + "\t" +
																_field.getPartialName()+ "\t" +
																_field.getValue() + "\t" + 
																_field.isReadonly() + "\t" + 
																_field.getFieldFlags() + "\t" + 
																annotation.getContents());*/
														
														this.fields.put(fitechPDFField.getFieldName(),fitechPDFField);
													}
												}
										    }
										}
									}else{	//点对点式PDF报表模板的处理		
										this.reportStyle=Config.REPORT_STYLE_DD;
										
										FitechPDFField fitechPDFField=new FitechPDFField();
										fitechPDFField.setFieldName(field.getPartialName());
										fitechPDFField.setFieldType(field.getFieldType());
										fitechPDFField.setFieldValue(field.getValue());
										fitechPDFField.setReadOnly(field.isReadonly());
										/**
										 * 临时解决问题设置
										 */
										fitechPDFField.setDataType(FitechPDFField.CELL_DATA_TYPE_INTEGER);
										
										annotation=(PDAnnotation)field.getWidget();
										fitechPDFField.setVisibility(!annotation.isInvisible());
										
										/*// System.out.println(g + "\t" + 
												field.getFieldType() + "\t" + 
												field.getFullyQualifiedName() + "\t" +
												field.getPartialName()+ "\t" +
												field.getValue() + "\t" + 
												field.isReadonly() + "\t" + 
												field.getFieldFlags() + "\t" + 
												annotation.isInvisible());*/
										
										this.fields.put(fitechPDFField.getFieldName(),fitechPDFField);
									}
								}
							}
						}
					}
				}
			}else{
				log.println("获取PDF文件中的AcroForm失败!");
			}
			
			result=true;
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			if(document!=null) 
				try{
					document.close();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
		}
		
		return result;
	}	
	
	/**
	 * 根据域的名称，判断这个域是否存在<br>
	 * 如果当前的域存在，返回true;否则，返回false
	 * 
	 * @param fieldName String 域名称
	 * @return boolean
	 */
	public boolean isExists(String fieldName){
		return this.fields.containsKey(fieldName);
	}
	
	/**
	 * 根据域的名称返回域的值<br>
	 * 如果当前的域存在，返回其值，并且，如果其值为null,返回空字符串;<br>
	 * 如果当前的域不存在，返回空字符串<br>
	 * 
	 * @param fieldName String 域名称
	 * @return String 域的值
	 */
	public String getValueByFieldName(String fieldName){
		String value="";
		
		if(!this.fields.containsKey(fieldName)) return value;
		
		Object object=this.fields.get(fieldName);
		
		if(object!=null) value=object==null?"":((FitechPDFField)object).getFieldValue();
			
		return value;
	}
	
	/**
	 * 获取清单式报表的列名
	 * 
	 * @return List
	 */
	public List getInputCols(){
		List inputCols=null;
		TreeSet tree=null;
		
		if(this.fields==null) return null;
		if(this.fields.isEmpty()==true) return null;
		
		try{
			Iterator it=this.fields.keySet().iterator();
			tree=new TreeSet();
			FitechPDFField field=null;
			while(it.hasNext()){
				field=(FitechPDFField)this.fields.get(it.next());
				if(field==null) break;
				if(isInputField(field.getFieldName(),2)==true && field.getFieldName().length()>=3){
					String col="";
					int row=0;
					try{
						col=field.getFieldName().substring(0,2);    		 //列	
						Integer _row=Integer.valueOf(field.getFieldName().substring(2));
						row=_row.intValue();
					}catch(Exception e){
						col="";
						row=0;
					}
					if(col.toUpperCase().equals("FH") && (row>0 && row<99)){
						//tree.add(field.getFieldName());
						int bs=row/26;
						int ys=row%26;
						if(ys>0){
							tree.add("列" + (bs>0?this.CHARACTERS.subSequence(bs,bs+1):"") + 
								this.CHARACTERS.substring(ys,ys+1));
						}
					}
				}
			}
			
			if(tree!=null){
				it=tree.iterator();
				if(it!=null){
					inputCols=new ArrayList();
					ListingColsForm _lCol=new ListingColsForm();
					_lCol.setPdfColName("序号列");
					_lCol.setDbColName("Col1");
					inputCols.add(_lCol);
					int i=2;
					while(it.hasNext()){
						ListingColsForm lCol=new ListingColsForm();
						lCol.setPdfColName((String)it.next());
						lCol.setDbColName("Col" + i);
						inputCols.add(lCol);
						i++;
					}
				}
			}
		}catch(Exception e){
			log.printStackTrace(e);
			tree=null;
		}
		
		return inputCols;
	}
	
	/**
	 * 获取可输入域列表
	 * 
	 * @return List 有输入域，返回List对象；否则，返回null
	 */
	public List getInputFields(){
		List resList=null;
		
		if(this.fields!=null && !fields.isEmpty()){
			resList=new ArrayList();
			Set keys=fields.keySet();
			Iterator iterator=keys.iterator();
			while(iterator.hasNext()){
				FitechPDFField field=(FitechPDFField)this.fields.get(iterator.next());
				if(field!=null){
					if(field.getFieldType().toUpperCase().equals(FitechPDFField.TYPE_TX) && 
						(field.isReadOnly()==false || (field.getFieldValue()!=null && field.getFieldValue().equals("0")))&& 
						field.isVisibility()==true && 
						isInputField(field.getFieldName())){
						MCellForm mCellForm=new MCellForm();
						mCellForm.setCellName(field.getFieldName());
						mCellForm.setColId(getColName(field.getFieldName()));
						mCellForm.setRowId(getRowNo(field.getFieldName()));
						mCellForm.setDataType(field.getDataType());
						resList.add(mCellForm);
					}						
				}
			}
		}
		
		return resList;
	}
	
	/**
	 * 根据域的名称,判断当前的域是否是输入域<br>
	 * 域名称的第一个字符必顺是大写的英文字符,其它字符必顺是数值
	 * 
	 * @param fieldName String 域名称
	 * @return boolean 当前的域是输入域,返回true;否则,返回false
	 */
	private boolean isInputField(String fieldName){
		boolean isInput=false;
		
		try{
			if(fieldName==null) return isInput;	
			if(fieldName.trim().equals("")==true) return isInput;
			
			if(fieldName.length()<2) return isInput;
			
			String f_char=fieldName.substring(0,1);
			String s_char=fieldName.substring(1,2);
			//String t_char=fieldName.substring(2,3);
			
			if(CHARACTERS.indexOf(f_char.toUpperCase())>=0){
				if(CHARACTERS.indexOf(s_char.toUpperCase())>=0){
					try{
						if(Integer.parseInt(fieldName.substring(2))<0) isInput=false;
						isInput=true;
					}catch(NumberFormatException nfe){
						isInput=false;
					}
				}else if(CHARACTERS_MATH.indexOf(s_char)>=0){
					try{
						if(Integer.parseInt(fieldName.substring(1))<0) isInput=false;
						isInput=true;
					}catch(NumberFormatException nfe){
						isInput=false;
					}
				}				
			}
			
			/*// System.out.println(f_char + "\t" + s_char + "\t" + t_char + "\t" );
			if(CHARACTERS.indexOf(f_char.toUpperCase())>=0 &&
					(CHARACTERS.indexOf(f_char.toUpperCase())>=0 || CHARACTERS_MATH.indexOf(t_char)>=0) &&
					CHARACTERS_MATH.indexOf(t_char)>=0){
				isInput=true;
			}
			if(fieldName.length()>3){
				try{
					if(Integer.parseInt(fieldName.substring(3))<0) isInput=false;
				}catch(NumberFormatException nfe){
					isInput=false;
				}
			}*/
			
			/*
			String colName=fieldName.substring(0,1);
			String row=fieldName.substring(1);
			
			int rowNo=0;
			try{
				rowNo=Integer.parseInt(row);
			}catch(NumberFormatException nfe){
				rowNo=-1;
			}
	
			isInput=CHARACTERS.indexOf(colName.toUpperCase())>=0 && rowNo>0?true:isInput;
			*/
			
		}catch(Exception e){
			log.printStackTrace(e);
			isInput=false;
		}
		
		return isInput;
	}
	
	private boolean isInputField(String fieldName,int len){
		boolean isInput=true;
		
		try{
			if(fieldName==null) return false;	
			if(fieldName.trim().equals("")==true) return false;
			
			String colName=fieldName.substring(0,len);
			String row=fieldName.substring(len);
			
			int rowNo=0;
			try{
				rowNo=Integer.parseInt(row);
			}catch(NumberFormatException nfe){
				rowNo=-1;
			}
			for(int i=0;i<colName.length();i++){
				if(CHARACTERS.indexOf(colName.substring(i,1).toUpperCase())<0){
					isInput=false;
					break;
				}
			}
			if(rowNo<0) isInput=false;
		}catch(Exception e){
			log.printStackTrace(e);
			isInput=false;
		}
		
		return isInput;
	}
	
	/**
	 * 获取文本域所在的行<br>
	 * 根据文本域名称，获得文本域所在的行号；获取失败，返回null；成功，返回正整数值
	 * 
	 * @param fieldName String 文本域的名称
	 * @return Integer 行号 
	 */
	public Integer getRowNo(String fieldName){
		Integer rowNo=null;
		
		if(fieldName==null) return rowNo;
		
		String _rowNo="";
		try{
			//String _rowNo=fieldName.substring(1);
			
			String _char="";
			for(int i=fieldName.length();i>0;i--){
				_char=fieldName.substring(i-1,i);
				if(CHARACTERS_MATH.indexOf(_char)>=0) _rowNo=_char + _rowNo;
			}

			rowNo=_rowNo.trim().equals("")?null:Integer.valueOf(_rowNo);
		}catch(Exception e){
			e.printStackTrace();
			rowNo=null;
		}
		
		return rowNo;
	}
	
	/**
	 * 获取文本域所在的列
	 * 
	 * @param fieldName String 文本域的名称
	 * @return String 列名
	 */
	public String getColName(String fieldName){
		//return fieldName==null?null:fieldName.substring(0,1);
		if(fieldName==null) return null;
		if(fieldName.length()>2){
			String s_char=fieldName.substring(1,2);
			if(CHARACTERS.indexOf(s_char)>=0){
				return fieldName.substring(0,2);
			}else{
				return fieldName.substring(0,1);
			}
		}else{
			return fieldName.substring(0,1);
		}
	}
	
	/**
	 * 保存模板信息<br>
	 * 将报表模板文件中的报表基本信息和可输入域信息入库
	 * 
	 * @param mChildReportForm MChildReportForm
	 * @return boolean 保存成功，返回true;否则，返回false
	 */
	public boolean save(MChildReportForm mChildReportForm){
		boolean resSave=false;
		
		if(mChildReportForm==null) return resSave;
		if(mChildReportForm.getTmpFileName()==null) return resSave;
		
		InputStream in=FitechUtil.readFile(mChildReportForm.getTmpFileName());
		
		if(in==null) return resSave;
		
		Integer repTypeId=mChildReportForm.getRepTypeId();

		if(parse(in)==true){
			String title="",subTitle="",version="",curUnit="";
			if(mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD)==0){
				String[] titles=new String[2];
				if(mChildReportForm.getReportTitle()!=null) 
					titles=mChildReportForm.getReportTitle().split("-");
				title=titles.length>=1?titles[0]:"";
				subTitle=titles.length>=2?titles[1]:title;
				version=mChildReportForm.getVersionId();
				curUnit=mChildReportForm.getReportCurUnit();
			}else{
				title=getValueByFieldName(FitechPDFReport.TITLE);
				subTitle=getValueByFieldName(FitechPDFReport.SUBTITLE);				
				version=getValueByFieldName(FitechPDFReport.version);
				curUnit=getValueByFieldName(FitechPDFReport.CURUNIT);
			}
			
			/**获取货币单位ID**/
			Integer curUnitId=null;
		    try{
		    	curUnitId=mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD)==0?Integer.valueOf(curUnit):StrutsMCurUnitDelegate.getCurUnitID(curUnit);
		    }catch(Exception e){
		    	e.printStackTrace();
		    	return false;
		    }
		    if(curUnitId==null) return false;
		    
			/**实例主报表Form对象并对其赋值**/
			MMainRepForm mMainRepForm=new MMainRepForm();
			mMainRepForm.setRepCnName(title!=null?title.replaceAll(" ",""):"");
			mMainRepForm.setCurUnit(curUnitId);
			mMainRepForm.setRepTypeId(repTypeId);
			
			/**对子报表Form对象并对其赋值**/
			mChildReportForm.setReportName(subTitle!=null && !subTitle.equals("")?subTitle.replaceAll(" ",""):(title!=null?title.replaceAll(" ",""):""));
			mChildReportForm.setVersionId(version);
			mChildReportForm.setCurUnit(curUnitId);
			mChildReportForm.setReportStyle(this.reportStyle);
				
			FitechPDFReport fitechPDFReport=new FitechPDFReport();	
			/**
			 * PDF报表模板的保存处理
			 */
			if(this.reportStyle.compareTo(Config.REPORT_STYLE_DD)==0){ //点对点式
				List inputFields=this.getInputFields();		
				
				if(inputFields!=null){
					resSave=fitechPDFReport.saveBaseReportTemplate(mMainRepForm,mChildReportForm,inputFields);
				}
			}else if(this.reportStyle.compareTo(Config.REPORT_STYLE_QD)==0){ //清单式
				List inputCols=this.getInputCols();
				
				if(inputCols!=null){
					for(int i=0;i<inputCols.size();i++){
						ListingColsForm lcForm=(ListingColsForm)inputCols.get(i);
						/*// System.out.println("DbColName:" + lcForm.getDbColName());
						// System.out.println("PdfColName:" + lcForm.getPdfColName());*/
					}
					resSave=fitechPDFReport.saveEspReportTemplate(mMainRepForm,mChildReportForm,inputCols);
				}
			}
		
			/**将PDF文件入库**/
			if(resSave==true){
				ReportDataForm reportDataForm=new ReportDataForm();
				reportDataForm.setChildRepId(mChildReportForm.getChildRepId());
				reportDataForm.setVersionId(mChildReportForm.getVersionId());
				reportDataForm.setPdfIN(FitechUtil.readFile(mChildReportForm.getTmpFileName()));
				if(StrutsReportDataDelegate.insert(reportDataForm)==false){
					log.println("将PDF文件写入数据库错误!");
				}
			}else{
				log.println("报存报表模板信息失败!");
			}
			
			if(FitechUtil.deleteFile(mChildReportForm.getTmpFileName())==false){
				log.println("临时文件[" + mChildReportForm.getTmpFileName() + "]删除失败!");
			}
		}
		
		return resSave;
	}
	
	/**
	 * 设置报表模板的类型
	 * 
	 * @param reportStyle Integer
	 * @return void
	 */
	public void setReportStyle(Integer reportStyle){
		this.reportStyle=reportStyle;
	}
	/**
	 * 获取报表模板的类型
	 * 
	 * @return Integer
	 */
	public Integer getReportStyle(){
		return this.reportStyle;
	}
	
	/**
	 * main方法
	 * 
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			File file=new File("E:\\G01_notActive.pdf");
			//File file=new File("C:\\Documents and Settings\\IBMUSER\\桌面\\SMIS\\报表\\PDF模板（3月8日）\\基础报表\\G01.pdf");
			//File file=new File("C:\\Documents and Settings\\IBMUSER\\桌面\\SMIS\\报表\\动态样例1211激活\\特色报表\\S33.pdf");
			FileInputStream fin=new FileInputStream(file);
			FitechPDF pdfUtil=new FitechPDF();
			pdfUtil.parse(fin);
			/*// System.out.println(pdfUtil.getInputFields().size());
			List list=pdfUtil.getInputFields();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					
				}
			}*/
			/*// System.out.println("");*/
			//FitechPDF fitechPDF=new FitechPDF();
			//// System.out.println(fitechPDF.isInputField("T5"));
			//// System.out.println(fitechPDF.getRowNo("ABB109"));
		}catch(Exception ioe){
			ioe.printStackTrace();
		}
	}

}