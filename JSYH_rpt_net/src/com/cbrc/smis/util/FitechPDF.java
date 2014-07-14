/**
 * ������Ŀ����:<br>
 * ����PDF����ģ��,���ģ���е������������������ص�ͷβ��Ϣ.<br>
 * �ڵ�ǰ�������,����������Ĳ���̫��:<br>
 *   1��Ĭ�ϰ�PDF�����е����������������ȫ��Ϊ����ֵ�ͣ�<br>
 *   2����ͨ����������ж����Ƿ������������������Ƶĵ�һ����ĸ�Ǵ�д��Ӣ����ĸ�������ַ��ǺϷ�����ֵ��<br>
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
 * ����PDF����ģ�������
 * 
 * @author rds
 * @date 2005-12-1
 */
public class FitechPDF {
	private FitechException log=new FitechException(FitechPDF.class);

	public String CHARACTERS="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public String CHARACTERS_MATH="0123456789";
	
	/**
	 * PDF����ģ���е�ȫ����
	 */
	private HashMap fields=null;
	
	/**
	 * PDF����ģ�������(��Ե�ʽ���嵥ʽ����)
	 */
	private Integer reportStyle=null;
	
	/**
	 * ���캯��
	 */
	public FitechPDF(){}
	
	/**
	 * ����PDF�ļ�
	 * 
	 * @param input InputStream PDF�ļ�������
	 * @return void �����ɹ�������true;���򣬷���false
	 */
	public boolean parse(InputStream input){
		boolean result=false;
		PDDocument document=null;
		
		String _flag="";  //��ʶ
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
										if(_flag!=null && _flag.trim().equalsIgnoreCase("P1[0]")){   //��ҳPDF����Ľ���
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
										}else{  //�嵥ʽPDF����ģ��Ĵ���
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
									}else{	//��Ե�ʽPDF����ģ��Ĵ���		
										this.reportStyle=Config.REPORT_STYLE_DD;
										
										FitechPDFField fitechPDFField=new FitechPDFField();
										fitechPDFField.setFieldName(field.getPartialName());
										fitechPDFField.setFieldType(field.getFieldType());
										fitechPDFField.setFieldValue(field.getValue());
										fitechPDFField.setReadOnly(field.isReadonly());
										/**
										 * ��ʱ�����������
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
				log.println("��ȡPDF�ļ��е�AcroFormʧ��!");
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
	 * ����������ƣ��ж�������Ƿ����<br>
	 * �����ǰ������ڣ�����true;���򣬷���false
	 * 
	 * @param fieldName String ������
	 * @return boolean
	 */
	public boolean isExists(String fieldName){
		return this.fields.containsKey(fieldName);
	}
	
	/**
	 * ����������Ʒ������ֵ<br>
	 * �����ǰ������ڣ�������ֵ�����ң������ֵΪnull,���ؿ��ַ���;<br>
	 * �����ǰ���򲻴��ڣ����ؿ��ַ���<br>
	 * 
	 * @param fieldName String ������
	 * @return String ���ֵ
	 */
	public String getValueByFieldName(String fieldName){
		String value="";
		
		if(!this.fields.containsKey(fieldName)) return value;
		
		Object object=this.fields.get(fieldName);
		
		if(object!=null) value=object==null?"":((FitechPDFField)object).getFieldValue();
			
		return value;
	}
	
	/**
	 * ��ȡ�嵥ʽ���������
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
						col=field.getFieldName().substring(0,2);    		 //��	
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
							tree.add("��" + (bs>0?this.CHARACTERS.subSequence(bs,bs+1):"") + 
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
					_lCol.setPdfColName("�����");
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
	 * ��ȡ���������б�
	 * 
	 * @return List �������򣬷���List���󣻷��򣬷���null
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
	 * �����������,�жϵ�ǰ�����Ƿ���������<br>
	 * �����Ƶĵ�һ���ַ���˳�Ǵ�д��Ӣ���ַ�,�����ַ���˳����ֵ
	 * 
	 * @param fieldName String ������
	 * @return boolean ��ǰ������������,����true;����,����false
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
	 * ��ȡ�ı������ڵ���<br>
	 * �����ı������ƣ�����ı������ڵ��кţ���ȡʧ�ܣ�����null���ɹ�������������ֵ
	 * 
	 * @param fieldName String �ı��������
	 * @return Integer �к� 
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
	 * ��ȡ�ı������ڵ���
	 * 
	 * @param fieldName String �ı��������
	 * @return String ����
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
	 * ����ģ����Ϣ<br>
	 * ������ģ���ļ��еı��������Ϣ�Ϳ���������Ϣ���
	 * 
	 * @param mChildReportForm MChildReportForm
	 * @return boolean ����ɹ�������true;���򣬷���false
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
			
			/**��ȡ���ҵ�λID**/
			Integer curUnitId=null;
		    try{
		    	curUnitId=mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD)==0?Integer.valueOf(curUnit):StrutsMCurUnitDelegate.getCurUnitID(curUnit);
		    }catch(Exception e){
		    	e.printStackTrace();
		    	return false;
		    }
		    if(curUnitId==null) return false;
		    
			/**ʵ��������Form���󲢶��丳ֵ**/
			MMainRepForm mMainRepForm=new MMainRepForm();
			mMainRepForm.setRepCnName(title!=null?title.replaceAll(" ",""):"");
			mMainRepForm.setCurUnit(curUnitId);
			mMainRepForm.setRepTypeId(repTypeId);
			
			/**���ӱ���Form���󲢶��丳ֵ**/
			mChildReportForm.setReportName(subTitle!=null && !subTitle.equals("")?subTitle.replaceAll(" ",""):(title!=null?title.replaceAll(" ",""):""));
			mChildReportForm.setVersionId(version);
			mChildReportForm.setCurUnit(curUnitId);
			mChildReportForm.setReportStyle(this.reportStyle);
				
			FitechPDFReport fitechPDFReport=new FitechPDFReport();	
			/**
			 * PDF����ģ��ı��洦��
			 */
			if(this.reportStyle.compareTo(Config.REPORT_STYLE_DD)==0){ //��Ե�ʽ
				List inputFields=this.getInputFields();		
				
				if(inputFields!=null){
					resSave=fitechPDFReport.saveBaseReportTemplate(mMainRepForm,mChildReportForm,inputFields);
				}
			}else if(this.reportStyle.compareTo(Config.REPORT_STYLE_QD)==0){ //�嵥ʽ
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
		
			/**��PDF�ļ����**/
			if(resSave==true){
				ReportDataForm reportDataForm=new ReportDataForm();
				reportDataForm.setChildRepId(mChildReportForm.getChildRepId());
				reportDataForm.setVersionId(mChildReportForm.getVersionId());
				reportDataForm.setPdfIN(FitechUtil.readFile(mChildReportForm.getTmpFileName()));
				if(StrutsReportDataDelegate.insert(reportDataForm)==false){
					log.println("��PDF�ļ�д�����ݿ����!");
				}
			}else{
				log.println("���汨��ģ����Ϣʧ��!");
			}
			
			if(FitechUtil.deleteFile(mChildReportForm.getTmpFileName())==false){
				log.println("��ʱ�ļ�[" + mChildReportForm.getTmpFileName() + "]ɾ��ʧ��!");
			}
		}
		
		return resSave;
	}
	
	/**
	 * ���ñ���ģ�������
	 * 
	 * @param reportStyle Integer
	 * @return void
	 */
	public void setReportStyle(Integer reportStyle){
		this.reportStyle=reportStyle;
	}
	/**
	 * ��ȡ����ģ�������
	 * 
	 * @return Integer
	 */
	public Integer getReportStyle(){
		return this.reportStyle;
	}
	
	/**
	 * main����
	 * 
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			File file=new File("E:\\G01_notActive.pdf");
			//File file=new File("C:\\Documents and Settings\\IBMUSER\\����\\SMIS\\����\\PDFģ�壨3��8�գ�\\��������\\G01.pdf");
			//File file=new File("C:\\Documents and Settings\\IBMUSER\\����\\SMIS\\����\\��̬����1211����\\��ɫ����\\S33.pdf");
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