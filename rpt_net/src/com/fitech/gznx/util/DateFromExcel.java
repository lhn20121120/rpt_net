package com.fitech.gznx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.filter.AdminLoginFilter;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFPbocReportDelegate;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsAFCellDelegate;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.Area;
import com.runqian.report4.usermodel.DataSetConfig;
import com.runqian.report4.usermodel.DataSetMetaData;
import com.runqian.report4.usermodel.IByteMap;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.usermodel.input.InputProperty;
import com.runqian.report4.usermodel.input.RelationParams;
import com.runqian.report4.usermodel.input.TableRelation;
import com.runqian.report4.usermodel.input.TableRelations;
import com.runqian.report4.usermodel.input.UpdateProperty;
import com.runqian.report4.util.ReportUtils;

public class DateFromExcel {

	private String printPath;
	
	public static final String WRITER = "�����";
	
	public static final String CHECKER = "������";
	
	public static final String PRINCIPAL = "������";
	
	private FitechMessages messages;

	public String saveFromExcel(String reportId, String templateId,
			String versionId, String reportFlg) {
		String filepath = null;
		try {

			// ģ��·��
			String reportFile = Config.RAQ_TEMPLATE_PATH + "templateFiles"
					+ File.separator + "Raq" + File.separator + templateId
					+ "_" + versionId + ".raq";
			File raqfile = new File(reportFile);
       		if(!raqfile.exists()){
       			messages.add(reportFile+"��������");
       			return null;
       		}
			ReportDefine rd = (ReportDefine) ReportUtils.read(reportFile);
			// ȡ�������������
			int maxrow = rd.getRowCount();
			int maxcol = rd.getColCount();
			AfTemplate template = AFTemplateDelegate.getTemplate(templateId,
					versionId);
			if (template != null
					&& template.getIsReport() != null
					&& template.getIsReport().intValue() == Integer.valueOf(
							com.fitech.gznx.common.Config.TEMPLATE_REPORT)
							.intValue()) {
				// ȡ�ñ�������
				List<ReportInInfoForm> reportData = AFReportDealDelegate.getReportData(Integer
						.valueOf(reportId), reportFlg);
				int colIndex = -1;

				for (ReportInInfoForm cell:reportData) {
					if (StringUtil.isEmpty(cell.getReportValue())) {
						continue;
					}
					if (cell.getColId() == null) {
						continue;
					}
					colIndex = convertColStringToNum(cell.getColId()) + 1;
					if (colIndex == -1)
						continue;
					int rownum = cell.getRowId().intValue();
					if(rownum>maxrow || colIndex>maxcol){
						continue;
					}

					INormalCell iExcelCell = rd.getCell(rownum,
							(short) colIndex);
					iExcelCell.setExpMap(null);
					iExcelCell.setValue(cell.getReportValue());

					rd.setCell(rownum, (short) colIndex, iExcelCell);
				}
			}

			// ��ӡ�ģ��·��
			filepath = Config.RAQ_TEMPLATE_PATH + "templateFiles" + File.separator
					+ "printRaq" + File.separator + printPath + File.separator
					+ reportId + ".raq";
			ReportUtils.write(filepath, rd);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return filepath;
	}
	
	/**
	 * ��һ�μ���raqģ��ʱֻ�����õ�Ԫ��������
	 * @param rd
	 * @param reportId
	 * @param reportFlg
	 */
	private void setUpdateProperty(ReportDefine rd,String reportId
			,String reportFlg,String templateId){
		if(rd!=null && reportId !=null && reportFlg!=null){
			int rowCount = rd.getRowCount();
			int colCount = rd.getColCount();
			for(int i = 1;i<=rowCount;i++){
				for(int j =1;j<=colCount;j++){
					InputProperty input = new InputProperty();
					input.setWritable(true);
					input.setEmptyIsNull(true);
					
					INormalCell inc = rd.getCell(i, (short)j);
					String colname = DateFromExcel.convertNumToCol(j-1)+i;
					String reportValue = (String)inc.getValue();
					if(reportValue!=null && !reportValue.equals("")){
						if(reportValue.indexOf(WRITER)>-1 ||
								reportValue.indexOf(CHECKER) >-1||
								reportValue.indexOf(PRINCIPAL) >-1){
							boolean result = StrutsAFCellDelegate.isUpPropertyName(templateId, reportValue);
							if(result)
								continue;
							Area area1= inc.getMergedArea();
							if(area1 != null){
								int begincol = area1.getBeginCol();
								int endcol = area1.getEndCol();
								if(j>begincol){
									continue;
								}
							}
							//���ñ���������
							
							InputProperty pro = inc.getInputProperty();
							if(pro==null){
								UpdateProperty updateProperty = new UpdateProperty();
								updateProperty.setName("update1");
								updateProperty.setMode((byte) 1);
								TableRelations tableRelations = new TableRelations();
								tableRelations.setDataSourceName(Config.DATASOURCE_NAME);
								//tableRelations.setSchema("FITECH");

								if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
									tableRelations.setTableName("REPORT_IN");
								} else if (reportFlg
										.equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
									tableRelations.setTableName("AF_REPORT");
								} else if (reportFlg
										.equals(com.fitech.gznx.common.Config.OTHER_REPORT)) {
									tableRelations.setTableName("AF_REPORT");
								}
								// û���Ҳ�������ݿ�
								tableRelations.setUpdateUnmodified(true);
								// �����ֶ�����
								TableRelation tableRelationrepId = new TableRelation();
								TableRelation tableRelationcell = new TableRelation();
								String fieldName  = "";
								if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
									// ����Id�ֶ���������
									fieldName = "REP_IN_ID";
								} else {
									// ����Id�ֶ���������
									fieldName = "REP_ID";
								}
								if(!fieldName.equals("")){
									tableRelationrepId.setFieldName(fieldName);
									tableRelationrepId.setPrimaryKey(true);
									RelationParams relationRepId = new RelationParams();
									relationRepId.addParam(reportId, (byte) 0);

									tableRelationrepId.setRelationParams(relationRepId);
									tableRelations.addRelation(tableRelationrepId);
									
									fieldName = "";
								}
								if(reportValue.indexOf(WRITER)>-1){
									// ��Ԫ�������ֶ���������
									fieldName = "writer";
								}else if(reportValue.indexOf(CHECKER)>-1){
									// ��Ԫ�������ֶ���������
									fieldName = "checker";
								}else if(reportValue.indexOf(PRINCIPAL)>-1){
									// ��Ԫ�������ֶ���������
									fieldName = "principal";
								}
								if(!fieldName.equals("")){
									tableRelationcell.setFieldName(fieldName);
									tableRelationcell.setPrimaryKey(false);
									RelationParams relationParams = new RelationParams();
							
									relationParams.addParam("="+colname, (byte) 0);

									tableRelationcell.setRelationParams(relationParams);
									
									tableRelations.addRelation(tableRelationcell);
								}
								updateProperty.setRelation(tableRelations);
								ArrayList uplist = new ArrayList();
								uplist.add(updateProperty);
								
								input.setUpdateList(uplist);
								inc.setInputProperty(input);
								
								rd.setCell(i, (short) j, inc);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * ������״̬Ϊ�޸�ʱ��������ȥ���ݿ��ȡ��¼�����������Ԫ��ֵ���ٴ����������
	 * @param rd
	 * @param reportId
	 * @param reportFlg
	 */
	private void resovleReportDefine(ReportDefine rd,String reportId
				,String reportFlg,String templateId){
		if(rd!=null && reportId !=null && reportFlg!=null){
			int rowCount = rd.getRowCount();
			int colCount = rd.getColCount();
			for(int i = 1;i<=rowCount;i++){
				for(int j =1;j<=colCount;j++){
					InputProperty input = new InputProperty();
					input.setWritable(true);
					input.setEmptyIsNull(true);
					
					INormalCell inc = rd.getCell(i, (short)j);
					String colname = DateFromExcel.convertNumToCol(j-1)+i;
					String reportValue = (String)inc.getValue();
					if(reportValue!=null && !reportValue.equals("")){
						if(reportValue.indexOf(WRITER)>-1 ||
								reportValue.indexOf(CHECKER) >-1||
								reportValue.indexOf(PRINCIPAL) >-1){
							boolean result = StrutsAFCellDelegate.isUpPropertyName(templateId, reportValue);
							if(result)
								continue;
							Area area1= inc.getMergedArea();
							if(area1 != null){//�ж��Ƿ��Ǻϲ���Ԫ��ĵ�һ��
								int begincol = area1.getBeginCol();
								int endcol = area1.getEndCol();
								if(j>begincol){
									continue;
								}
							}
							String value = "";
							try {
								if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
									ReportIn reportIn =AFPbocReportDelegate.getSimReportIn(Integer.parseInt(reportId));
									if(reportIn!=null){
										if(reportValue.indexOf(WRITER)>-1){
											if(reportIn.getWriter()!=null 
													&& !reportIn.getWriter().equals("")){
												value = reportIn.getWriter();
											}
											/*//��������Ϊ��,Ĭ��Ϊ��¼��
											else
											{
												value = WRITER + "��" + AdminLoginFilter.threadOpLocal.get().getOperatorName();
											}*/
										}else if(reportValue.indexOf(CHECKER)>-1){
											if(reportIn.getChecker()!=null 
													&& !reportIn.getChecker().equals("")){
												value = reportIn.getChecker();
											}
										}else if(reportValue.indexOf(PRINCIPAL)>-1){
											if(reportIn.getPrincipal()!=null 
													&& !reportIn.getPrincipal().equals("")){
												value = reportIn.getPrincipal();
											}
										}
									}
								} else if (reportFlg
										.equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
									AfReport afReport = AFReportDelegate.getReportIn(new Long(reportId));
									if(reportValue.indexOf(WRITER)>-1){
										if(afReport.getWriter()!=null && !afReport.getWriter().equals("")){
											value = afReport.getWriter();
										}
									}else if(reportValue.indexOf(CHECKER)>-1){
										if(afReport.getChecker()!=null && !afReport.getChecker().equals("")){
											value = afReport.getChecker();
										}
									}else if(reportValue.indexOf(PRINCIPAL)>-1){
										if(afReport.getPrincipal()!=null && !afReport.getPrincipal().equals("")){
											value = afReport.getPrincipal();
										}
									}
								} else if (reportFlg
										.equals(com.fitech.gznx.common.Config.OTHER_REPORT)) {
									AfReport afReport = AFReportDelegate.getReportIn(new Long(reportId));
									if(reportValue.indexOf(WRITER)>-1){
										if(afReport.getWriter()!=null 
												&& !afReport.getWriter().equals("")){
											value = afReport.getWriter();
										}
									}else if(reportValue.indexOf(CHECKER)>-1){
										if(afReport.getChecker()!=null 
												&& !afReport.getChecker().equals("")){
											value = afReport.getChecker();
										}
									}else if(reportValue.indexOf(PRINCIPAL)>-1){
										if(afReport.getPrincipal()!=null 
												&& !afReport.getPrincipal().equals("")){
											value = afReport.getPrincipal();
										}
									}
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(!value.equals(""))
								inc.setValue(value);
							//���ñ���������
							InputProperty pro = inc.getInputProperty();
							if(pro==null){
								UpdateProperty updateProperty = new UpdateProperty();
								updateProperty.setName("update1");
								updateProperty.setMode((byte) 1);
								TableRelations tableRelations = new TableRelations();
								tableRelations.setDataSourceName(Config.DATASOURCE_NAME);
								//tableRelations.setSchema("FITECH");

								if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
									tableRelations.setTableName("REPORT_IN");
								} else if (reportFlg
										.equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
									tableRelations.setTableName("AF_REPORT");
								} else if (reportFlg
										.equals(com.fitech.gznx.common.Config.OTHER_REPORT)) {
									tableRelations.setTableName("AF_REPORT");
								}
								// û���Ҳ�������ݿ�
								tableRelations.setUpdateUnmodified(true);
								// �����ֶ�����
								TableRelation tableRelationrepId = new TableRelation();
								TableRelation tableRelationcell = new TableRelation();
								String fieldName  = "";
								if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
									// ����Id�ֶ���������
									fieldName = "REP_IN_ID";
								} else {
									// ����Id�ֶ���������
									fieldName = "REP_ID";
								}
								if(!fieldName.equals("")){
									tableRelationrepId.setFieldName(fieldName);
									tableRelationrepId.setPrimaryKey(true);
									RelationParams relationRepId = new RelationParams();
									relationRepId.addParam(reportId, (byte) 0);

									tableRelationrepId.setRelationParams(relationRepId);
									tableRelations.addRelation(tableRelationrepId);
									
									fieldName = "";
								}
								if(reportValue.indexOf(WRITER)>-1){
									// ��Ԫ�������ֶ���������
									fieldName = "writer";
								}else if(reportValue.indexOf(CHECKER)>-1){
									// ��Ԫ�������ֶ���������
									fieldName = "checker";
								}else if(reportValue.indexOf(PRINCIPAL)>-1){
									// ��Ԫ�������ֶ���������
									fieldName = "principal";
								}
								if(!fieldName.equals("")){
									tableRelationcell.setFieldName(fieldName);
									tableRelationcell.setPrimaryKey(false);
									RelationParams relationParams = new RelationParams();
							
									relationParams.addParam("="+colname, (byte) 0);

									tableRelationcell.setRelationParams(relationParams);
									
									tableRelations.addRelation(tableRelationcell);
								}
								updateProperty.setRelation(tableRelations);
								ArrayList uplist = new ArrayList();
								uplist.add(updateProperty);
								
								input.setUpdateList(uplist);
								inc.setInputProperty(input);
								
								rd.setCell(i, (short) j, inc);
							}
						}
					}
				}
			}
		}
	}
	
	/***
	 * jdbc���� ������Ҫ�޸�  ���Ը�  2011-12-21
	 * @param reportId
	 * @param templateId
	 * @param versionId
	 * @param reportFlg
	 * @param isreport
	 * @return
	 */
	public String saveFromExcel(String reportId, String templateId,
			String versionId, String reportFlg, int isreport) {
		String filepath = null;
		try {

			// ģ��·��
			String reportFile = Config.RAQ_TEMPLATE_PATH + "templateFiles"
					+ File.separator + "Raq" + File.separator + templateId
					+ "_" + versionId + ".raq";
			
			File raqfile = new File(reportFile);
       		if(!raqfile.exists()){
       			messages.add(reportFile+"��������");
       			return null;
       		}
			ReportDefine rd = (ReportDefine) ReportUtils.read(reportFile);
			// ȡ�������������
			int maxrow = rd.getRowCount();
			int maxcol = rd.getColCount();
			
			//�������û������Եķ���
			resovleReportDefine(rd, reportId, reportFlg, templateId);
			
			// ȡ�ñ�������
			/**jdbc���� ������oracle�﷨ ����Ҫ�޸�  ���Ը�  2011-12-21**/
			List<ReportInInfoForm> reportData = AFReportDealDelegate.getReportData(Integer
					.valueOf(reportId), reportFlg);
			//��������������ɾ�����ݼ��������������������չ��Ч��
			if(reportData!=null && reportData.size()>0 && rd!=null && rd.getDataSetMetaData()!=null){
				DataSetMetaData dataSet = rd.getDataSetMetaData();
				if(dataSet!=null && dataSet.getDataSetConfigCount()>0){
					if(reportFlg.equals("1") || reportFlg.equals("3")){//��������౨����G14��G23���û���ֻ��дǰ5�ң������5�Ҳ���д����������ɺ���5�ҵĵ�Ԫ���������ݼ����ã�������������ɾ�����е�Ԫ������ݼ�������
						for(int i=1;i<=maxrow;i++){
							for(int j=1;j<=maxcol;j++){
								INormalCell icell = null;;
								try{
									icell = rd.getCell(i,(short) j);
								}catch(Exception e){
									continue;
								}
								if(icell.getExpMap()!=null){
									//�жϻ�ȡ��ֵ�Ƿ���ֻ��                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ���ʽ
										for(int m=0;m<dataSet.getDataSetConfigCount();m++){
											DataSetConfig df = dataSet.getDataSetConfig(m);
											if(icell.getExpMap().toString().toLowerCase().indexOf("ds_org")!=-1){//�ж��Ƿ��������ݼ�ds_org
												break;											}
											if(icell.getExpMap().toString().indexOf(df.getName().trim())!=-1){//�ж��Ƿ��������ݼ��ı��ʽ
												icell.setExpMap(null);
												break;
											}
										}
								}
							}
						}
					}
					int count = dataSet.getDataSetConfigCount();
					int j=0;
					int temp=0;
					while(j<count){
						DataSetConfig df = dataSet.getDataSetConfig(j);
						if(df!=null && df.getName()!=null){
							temp = j;
							j++;
							if(!df.getName().trim().toLowerCase().equals("ds_org") &&  df.getName().toUpperCase().trim().indexOf(templateId.toUpperCase().trim())==-1){
								dataSet.removeDataSetConfig(temp);
								j--;
								count = dataSet.getDataSetConfigCount();
							}else{
								System.out.println(df.getName());
							}
						}
						temp =0;
					}
				}
			}
			int colIndex = -1;
			for (ReportInInfoForm cell: reportData) {
				if (StringUtil.isEmpty(cell.getReportValue())) {
					continue;
				}
				if (cell.getColId() == null) {
					continue;
				}
				colIndex = convertColStringToNum(cell.getColId()) + 1;
				if (colIndex == -1)
					continue;
				int rownum = cell.getRowId().intValue();
				if(rownum>maxrow || colIndex>maxcol){
					continue;
				}
				INormalCell iExcelCell = rd.getCell(rownum,
						(short) colIndex);
				
//				InputProperty input = new InputProperty();
//				input.setWritable(true);
//				input.setEmptyIsNull(true);
//				String formulaValue = iExcelCell.getInputProperty().getAutoCalc();
//				formulaValue = formulaValue.replaceAll("IF", "EXCEL_IF");
//				formulaValue = formulaValue.replaceAll("if", "EXCEL_IF");
//				formulaValue = formulaValue.replaceAll("MAX", "EXCEL_MAX");
//				formulaValue = formulaValue.replaceAll("max", "EXCEL_MAX");
//				formulaValue = formulaValue.replaceAll("MIN", "EXCEL_MIN");
//				formulaValue = formulaValue.replaceAll("min", "EXCEL_MIN");
//				input.setAutoCalc(formulaValue);
//				iExcelCell.setInputProperty(input);			
							
				
				
				IByteMap map = iExcelCell.getExpMap(false);
				if(map != null && !map.isEmpty() && map.getValue(0) != null && iExcelCell.getBackColor() == Config.FORMULA_BGCOLOR){
					continue;
				}
				iExcelCell.setExpMap(null);
				iExcelCell.setValue(cell.getReportValue());

				rd.setCell(rownum, (short) colIndex, iExcelCell);
			}


			rd.setInput(IReport.INPUT_NORMAL);
			/**��ʹ��hibernate���� ���Ը� 2011-12-22
			 * Ӱ�����MCell || AfCellinfo**/
			List<Object[]> celllist = StrutsAFCellDelegate.getCellNoSList(templateId,
					versionId, reportFlg);

			for (Object[] cell:celllist) {

				int row = new Integer(String.valueOf(cell[0]));
				int col = convertColStringToNum((String) cell[1]) + 1;
				if(row>maxrow || col>maxcol){
					continue;
				}
				INormalCell iExcelCell = rd.getCell(row, (short) col);
				Area area1= iExcelCell.getMergedArea();
				if(area1 != null){
					int begincol = area1.getBeginCol();
					int endcol = area1.getEndCol();
					if(col>begincol){
						continue;
					}
				}
				 if (iExcelCell.getBackColor() == -4144960) {
					 continue;
			        }
				IByteMap map = iExcelCell.getExpMap(false);
				// ��Ԫ��ID
				String cellId = String.valueOf(cell[2]);
				String cellName = (String) cell[1]
						+ String.valueOf(cell[0]);
				String cellvna = (String) cell[1] + String.valueOf(cell[0]);
				
				// ���������
				InputProperty input = new InputProperty();
				input.setWritable(true);
				input.setEmptyIsNull(true);
				
				
				IByteMap propertyMap = iExcelCell.getPropertyMap();
				String propertvalue = (String) propertyMap.get(INormalCell.FORMAT);
				if(!StringUtil.isEmpty(propertvalue) ){
					
					
					String propertor = propertvalue.replaceAll("#", "");
					propertor = propertor.replaceAll("0", "");
					propertor = propertor.replaceAll(".", "");
					propertor = propertor.replaceAll(",", "");
					if(StringUtil.isEmpty(propertor)){
						
						int idexp = propertvalue.indexOf(".");
						if(idexp>-1){
							String xiaosu = propertvalue.substring(idexp+1);
							if(StringUtil.isEmpty(xiaosu.replaceAll("0", ""))){
								// ȡ��С��λ
								int sdex = propertvalue.substring(idexp+1).length();
								cellName = "if(isEmpty(trim("+cellvna+")) || !isnumber(rplc(trim(str("+cellvna+
									")),\",\",\"\")),0,if(number(rplc(trim(str("+cellvna+")),\",\",\"\"))<1 && number(rplc(trim(str("+
									cellvna+")),\",\",\"\"))>-1,str(round(number(rplc(trim(str("+cellvna+")),\",\",\"\")),"+sdex+
									"),'#0."+xiaosu+"'), str(round(number(rplc(trim(str("+cellvna+")),\",\",\"\")),"+sdex+"),'#0."+xiaosu+"')))";
							}
						} else {
							cellName = "if(isEmpty(trim("+cellvna+")) || !isnumber(rplc(trim(str("+cellvna+
								")),\",\",\"\")),0,if(number(rplc(trim(str("+cellvna+")),\",\",\"\"))<1 && number(rplc(trim(str("+cellvna+
								")),\",\",\"\"))>-1,str(round(number(rplc(trim(str("+cellvna+
								")),\",\",\"\")),0),'#0'), str(round(number(rplc(trim(str("+cellvna+")),\",\",\"\")),0),'#0')))";							

						}
//						ArrayList validateList = new ArrayList();
//						Validity vv = new Validity();
//						vv.setExp(cellvna+".match(/^([-]?\\d)+(\\.\\d+)?$/)");
//						vv.setPrompt(cellvna+"����ֵ");
//						validateList.add(vv);
//						input.setValidityList(validateList);
					//	input.setInputType(InputProperty.TYPE_NUMBER);
					} 
				}
				cellName = "=" + cellName;
				
				// ��Ӹ�������
				UpdateProperty updateProperty = new UpdateProperty();
				updateProperty.setName("update1");
				updateProperty.setMode((byte) 1);
				TableRelations tableRelations = new TableRelations();
				tableRelations.setDataSourceName(Config.DATASOURCE_NAME);
				//tableRelations.setSchema("FITECH");

				if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
					tableRelations.setTableName("REPORT_IN_INFO");
				} else if (reportFlg
						.equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
					tableRelations.setTableName("AF_PBOCREPORTDATA");
				} else if (reportFlg
						.equals(com.fitech.gznx.common.Config.OTHER_REPORT)) {
					tableRelations.setTableName("AF_OTHERREPORTDATA");
				}
				// û���Ҳ�������ݿ�
				tableRelations.setUpdateUnmodified(true);
				// �����ֶ�����
				TableRelation tableRelationrepId = new TableRelation();
				TableRelation tableRelationcellId = new TableRelation();
				TableRelation tableRelationcellData = new TableRelation();
				// ����ͬ����Ӧ�ֶ�Ҳ����ͬ
				if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
					// ����Id�ֶ���������
					tableRelationrepId.setFieldName("REP_IN_ID");
					tableRelationrepId.setPrimaryKey(true);
					RelationParams relationRepId = new RelationParams();
					relationRepId.addParam(reportId, (byte) 0);

					tableRelationrepId.setRelationParams(relationRepId);

					// ��Ԫ�������ֶ���������
					tableRelationcellData.setFieldName("REPORT_VALUE");
					tableRelationcellData.setPrimaryKey(false);
					RelationParams relationCellData = new RelationParams();
					relationCellData.addParam(cellName, (byte) 0);

					tableRelationcellData.setRelationParams(relationCellData);
				} else {
					// ����Id�ֶ���������
					tableRelationrepId.setFieldName("REP_ID");
					tableRelationrepId.setPrimaryKey(true);
					RelationParams relationRepId = new RelationParams();
					relationRepId.addParam(reportId, (byte) 0);

					tableRelationrepId.setRelationParams(relationRepId);

					// ��Ԫ�������ֶ���������
					tableRelationcellData.setFieldName("CELL_DATA");
					tableRelationcellData.setPrimaryKey(false);
					RelationParams relationCellData = new RelationParams();
					relationCellData.addParam(cellName, (byte) 0);

					tableRelationcellData.setRelationParams(relationCellData);
				}
				// ��Ԫ��Id�ֶ���������
				tableRelationcellId.setFieldName("CELL_ID");
				tableRelationcellId.setPrimaryKey(true);
				RelationParams relationCellId = new RelationParams();
				relationCellId.addParam(cellId, (byte) 0);
				tableRelationcellId.setRelationParams(relationCellId);

				tableRelations.addRelation(tableRelationrepId);
				tableRelations.addRelation(tableRelationcellId);
				tableRelations.addRelation(tableRelationcellData);
				updateProperty.setRelation(tableRelations);
				ArrayList uplist = new ArrayList();
				uplist.add(updateProperty);
				
				input.setUpdateList(uplist);
				if(map != null && !map.isEmpty() && map.getValue(0) != null && iExcelCell.getBackColor() == Config.FORMULA_BGCOLOR){
					String formulaValue = (String) map.getValue(0);
					if(!StringUtil.isEmpty(formulaValue)){
						String valformul = checkFormul(formulaValue.trim());
						input.setAutoCalc("="+valformul);
					}
				}
				iExcelCell.setInputProperty(input);
				rd.setCell(row, (short) col, iExcelCell);

			}
//			INormalCell iExcelCell=rd.getCell(33, (short) 3);
//			InputProperty input = new InputProperty();
//			input.setWritable(true);
//			input.setEmptyIsNull(true);
//			String formual = iExcelCell.getInputProperty().getAutoCalc();
//			formual = formual.replaceFirst("if", "CBRCIF");
//			input.setAutoCalc(formual);
//			iExcelCell.setInputProperty(input);
			// ��ӡ�ģ��·��
			filepath = Config.RAQ_TEMPLATE_PATH + "templateFiles" + File.separator
					+ "printRaq" + File.separator + printPath + File.separator
					+ reportId + ".raq";
			ReportUtils.write(filepath, rd);
			File file = new File(filepath);
			if (!file.exists()) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return printPath + "/" + reportId + ".raq";
	}



	/**
	 * ���к�ת��Ϊ����
	 * 
	 * @param ref
	 * @return
	 */
	private int convertColStringToNum(String ref) {
		int retval = 0;
		int pos = 0;
		for (int k = ref.length() - 1; k > -1; k--) {
			char thechar = ref.charAt(k);
			if (pos == 0)
				retval += Character.getNumericValue(thechar) - 9;
			else
				retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
			pos++;
		}
		return retval - 1;
	}

	public String viewFromExcel(String reportId, String templateId,
			String versionId, String reportFlg, int isreport) {
		String filepath = null;
		try {

			// ģ��·��
			String reportFile = Config.RAQ_TEMPLATE_PATH + "templateFiles"
					+ File.separator + "Raq" + File.separator + templateId
					+ "_" + versionId + ".raq";
			File raqfile = new File(reportFile);
       		if(!raqfile.exists()){
       			messages.add(reportFile+"��������");
       			return null;
       		}
			ReportDefine rd = (ReportDefine) ReportUtils.read(reportFile);
			// ȡ�������������
			int maxrow = rd.getRowCount();
			int maxcol = rd.getColCount();
			// ȡ�ñ�������
			if (isreport != Integer.valueOf(
					com.fitech.gznx.common.Config.TEMPLATE_VIEW).intValue()) {
				/**jdbc���� ������Ҫ�޸�  ���Ը�  2011-12-21**/
				List<ReportInInfoForm> reportData = AFReportDealDelegate.getReportData(Integer
						.valueOf(reportId), reportFlg);

				
				//��������������ɾ�����ݼ��������������������չ��Ч��
				if(reportData!=null && reportData.size()>0 && rd!=null && rd.getDataSetMetaData()!=null){
					DataSetMetaData dataSet = rd.getDataSetMetaData();
					if(dataSet!=null && dataSet.getDataSetConfigCount()>0){
						if(reportFlg.equals("1")){//��������౨����G14��G23���û���ֻ��дǰ5�ң������5�Ҳ���д����������ɺ���5�ҵĵ�Ԫ���������ݼ����ã�������������ɾ�����е�Ԫ������ݼ�������
							for(int i=1;i<=maxrow;i++){
								for(int j=1;j<=maxcol;j++){
									INormalCell icell = null;;
									try{
										icell = rd.getCell(i,(short) j);
									}catch(Exception e){
										continue;
									}
									if(icell.getExpMap()!=null){
										//�жϻ�ȡ��ֵ�Ƿ���ֻ��                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ���ʽ
											for(int m=0;m<dataSet.getDataSetConfigCount();m++){
												DataSetConfig df = dataSet.getDataSetConfig(m);
												if(icell.getExpMap().toString().toLowerCase().indexOf("ds_org")!=-1){//�ж��Ƿ��������ݼ�ds_org
													break;											}
												if(icell.getExpMap().toString().indexOf(df.getName().trim())!=-1){//�ж��Ƿ��������ݼ��ı��ʽ
													icell.setExpMap(null);
													break;
												}
											}
									}
								}
							}
						}
						int count = dataSet.getDataSetConfigCount();
						int j=0;
						int temp=0;
						while(j<count){
							DataSetConfig df = dataSet.getDataSetConfig(j);
							if(df!=null && df.getName()!=null){
								temp = j;
								j++;
								if(!df.getName().trim().toLowerCase().equals("ds_org") && df.getName().toUpperCase().trim().indexOf(templateId.toUpperCase().trim())==-1){
									dataSet.removeDataSetConfig(temp);
									j--;
									count = dataSet.getDataSetConfigCount();
								}else{
									System.out.println(df.getName());
								}
							}
							temp =0;
						}
					}
				}
				
				
				int colIndex = -1;
				// ������ӵ�����
				for (ReportInInfoForm cell: reportData) {

					if (StringUtil.isEmpty(cell.getReportValue())) {
						continue;
					}
					if (cell.getColId() == null) {
						continue;
					}
					colIndex = convertColStringToNum(cell.getColId()) + 1;
					if (colIndex == -1)
						continue;
					int rownum = cell.getRowId().intValue();
					if(rownum>maxrow || colIndex>maxcol){
						continue;
					}
					INormalCell iExcelCell = rd.getCell(rownum,
							(short) colIndex);
					IByteMap map = iExcelCell.getExpMap(false);
					if(map != null && !map.isEmpty() && map.getValue(0) != null && iExcelCell.getBackColor() == Config.FORMULA_BGCOLOR){
						continue;
					}
					iExcelCell.setExpMap(null);
					iExcelCell.setValue(("".equals(cell.getReportValue())||cell.getReportValue()==null||"null".equals(cell.getReportValue()))?0:cell.getReportValue());

					rd.setCell(rownum, (short) colIndex, iExcelCell);
				}
			}

			// ��ӡ�ģ��·��
			filepath = Config.RAQ_TEMPLATE_PATH + "templateFiles" + File.separator
					+ "printRaq" + File.separator + printPath + File.separator
					+ reportId + ".raq";
			ReportUtils.write(filepath, rd);
			File file = new File(filepath);
			if (!file.exists()) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return printPath + "/" + reportId + ".raq";
	}
	
	/****
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����MCell || AfCellinfo
	 * @param templateId
	 * @param versionId
	 * @param repId
	 * @param reportFlg
	 * @return
	 */
	public String saveFromRAQ(String templateId, String versionId,
			String repId, String reportFlg) {
		String filepath = null;
		try {
			// ģ��·��
			String reportFile = Config.RAQ_TEMPLATE_PATH + "templateFiles"
					+ File.separator + "Raq" + File.separator + templateId
					+ "_" + versionId + ".raq";
			File raqfile = new File(reportFile);
       		if(!raqfile.exists()){
       			messages.add(reportFile+"��������");
       			return null;
       		}
			ReportDefine rd = (ReportDefine) ReportUtils.read(reportFile);
			
			//���û�������
			setUpdateProperty(rd, repId, reportFlg, templateId);
			
			// ȡ�������������
			int maxrow = rd.getRowCount();
			int maxcol = rd.getColCount();
			rd.setInput(IReport.INPUT_NORMAL);
			/**��ʹ��hibernate���� ���Ը� 2011-12-22
			 * Ӱ�����MCell || AfCellinfo**/
			List<Object[]> celllist = StrutsAFCellDelegate.getCellNoSList(templateId,
					versionId, reportFlg);
			// ���������

			for (Object[] cell: celllist) {

				int row = new Integer(String.valueOf(cell[0]));
				int col = convertColStringToNum((String) cell[1]) + 1;
				if(row>maxrow || col>maxcol){
					continue;
				}
				INormalCell iExcelCell = rd.getCell(row, (short) col);
				Area area1= iExcelCell.getMergedArea();
				if(area1 != null){
					int begincol = area1.getBeginCol();
					int endcol = area1.getEndCol();
					if(col>begincol){
						continue;
					}
				}
				if (iExcelCell.getBackColor() == -4144960) {
					 continue;
			        }
				IByteMap map = iExcelCell.getExpMap(false);
				String cellId = String.valueOf(cell[2]);
				String cellName = (String) cell[1]
						+ String.valueOf(cell[0]);
				String cellvna= (String) cell[1]
				     						+ String.valueOf(cell[0]);
				// ���������
				InputProperty input = new InputProperty();
				input.setWritable(true);
				input.setEmptyIsNull(true);
				
				IByteMap propertyMap = iExcelCell.getPropertyMap();
				String propertvalue = (String) propertyMap.get(INormalCell.FORMAT);
				if(!StringUtil.isEmpty(propertvalue) ){
					String propertor = propertvalue.replaceAll("#", "");
					propertor = propertor.replaceAll("0", "");
					propertor = propertor.replaceAll(".", "");
					propertor = propertor.replaceAll(",", "");
					if(StringUtil.isEmpty(propertor)){
						
						int idexp = propertvalue.indexOf(".");
						if(idexp>-1){
							String xiaosu = propertvalue.substring(idexp+1);
							if(StringUtil.isEmpty(xiaosu.replaceAll("0", ""))){
								// ȡ��С��λ
								int sdex = propertvalue.substring(idexp+1).length();
								cellName = "if(isEmpty(trim("+cellvna+")) || !isnumber(rplc(trim(str("+cellvna+
									")),\",\",\"\")),0,if(number(rplc(trim(str("+cellvna+")),\",\",\"\"))<1 && number(rplc(trim(str("+
									cellvna+")),\",\",\"\"))>-1,str(round(number(rplc(trim(str("+cellvna+")),\",\",\"\")),"+sdex+
									"),'#0."+xiaosu+"'), str(round(number(rplc(trim(str("+cellvna+")),\",\",\"\")),"+sdex+"),'#0."+xiaosu+"')))";
							}
						} else {
							cellName = "if(isEmpty(trim("+cellvna+")) || !isnumber(rplc(trim(str("+cellvna+
								")),\",\",\"\")),0,if(number(rplc(trim(str("+cellvna+")),\",\",\"\"))<1 && number(rplc(trim(str("+
								cellvna+")),\",\",\"\"))>-1,str(round(number(rplc(trim(str("+cellvna+
								")),\",\",\"\")),0),'#0'), str(round(number(rplc(trim(str("+cellvna+")),\",\",\"\")),0),'#0')))";							
						}
//						// ����У�鹫ʽ
//						ArrayList validateList = new ArrayList();
//						Validity vv = new Validity();
//						vv.setExp(cellVn+".match(/^([-]?\\d)+(\\.\\d+)?$/)");
//						vv.setPrompt(cellVn+"����ֵ");
//						validateList.add(vv);
//						input.setValidityList(validateList);
						//input.setInputType(InputProperty.TYPE_NUMBER);
					}
				}
				cellName = "=" + cellName;
				// ��Ӹ�������
				UpdateProperty updateProperty = new UpdateProperty();
				updateProperty.setName("update1");
				updateProperty.setMode((byte) 1);
				TableRelations tableRelations = new TableRelations();
				tableRelations.setDataSourceName(Config.DATASOURCE_NAME);
				//tableRelations.setSchema("FITECH");

				if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
					tableRelations.setTableName("REPORT_IN_INFO");
				} else if (reportFlg
						.equals(com.fitech.gznx.common.Config.PBOC_REPORT)) {
					tableRelations.setTableName("AF_PBOCREPORTDATA");
				} else if (reportFlg
						.equals(com.fitech.gznx.common.Config.OTHER_REPORT)) {
					tableRelations.setTableName("AF_OTHERREPORTDATA");
				}
				// û���Ҳ�������ݿ�
				tableRelations.setUpdateUnmodified(true);
				// �����ֶ�����
				TableRelation tableRelationrepId = new TableRelation();
				TableRelation tableRelationcellId = new TableRelation();
				TableRelation tableRelationcellData = new TableRelation();
				// ����ͬ����Ӧ�ֶ�Ҳ����ͬ
				if (reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)) {
					// ����Id�ֶ���������
					tableRelationrepId.setFieldName("REP_IN_ID");
					tableRelationrepId.setPrimaryKey(true);
					RelationParams relationRepId = new RelationParams();
					relationRepId.addParam(repId, (byte) 0);

					tableRelationrepId.setRelationParams(relationRepId);

					// ��Ԫ�������ֶ���������
					tableRelationcellData.setFieldName("REPORT_VALUE");
					tableRelationcellData.setPrimaryKey(false);
					RelationParams relationCellData = new RelationParams();
					relationCellData.addParam(cellName, (byte) 0);

					tableRelationcellData.setRelationParams(relationCellData);
				} else {
					// ����Id�ֶ���������
					tableRelationrepId.setFieldName("REP_ID");
					tableRelationrepId.setPrimaryKey(true);
					RelationParams relationRepId = new RelationParams();
					relationRepId.addParam(repId, (byte) 0);

					tableRelationrepId.setRelationParams(relationRepId);

					// ��Ԫ�������ֶ���������
					tableRelationcellData.setFieldName("CELL_DATA");
					tableRelationcellData.setPrimaryKey(false);
					RelationParams relationCellData = new RelationParams();
					relationCellData.addParam(cellName, (byte) 0);

					tableRelationcellData.setRelationParams(relationCellData);
				}
				// ��Ԫ��Id�ֶ���������
				tableRelationcellId.setFieldName("CELL_ID");
				tableRelationcellId.setPrimaryKey(true);
				RelationParams relationCellId = new RelationParams();
				relationCellId.addParam(cellId, (byte) 0);
				tableRelationcellId.setRelationParams(relationCellId);

				tableRelations.addRelation(tableRelationrepId);
				tableRelations.addRelation(tableRelationcellId);
				tableRelations.addRelation(tableRelationcellData);
				updateProperty.setRelation(tableRelations);
				ArrayList uplist = new ArrayList();
				uplist.add(updateProperty);
				
				input.setUpdateList(uplist);
				// ���ü��㹫ʽ
				if(map != null && !map.isEmpty() && map.getValue(0) != null && iExcelCell.getBackColor() == Config.FORMULA_BGCOLOR){
					String formulaValue = (String) map.getValue(0);
					if(!StringUtil.isEmpty(formulaValue)){
						String valformul = checkFormul(formulaValue.trim());
						input.setAutoCalc("="+valformul);
					}
				}
				iExcelCell.setInputProperty(input);
				rd.setCell(row, (short) col, iExcelCell);
			}
			// ��ӡ�ģ��·��
			filepath = Config.RAQ_TEMPLATE_PATH + "templateFiles" + File.separator
					+ "printRaq" + File.separator + printPath + File.separator
					+ templateId + "_" + versionId + ".raq";
			ReportUtils.write(filepath, rd);
			File file = new File(filepath);
			if (!file.exists()) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return printPath + "/" + templateId + "_" + versionId
				+ ".raq";
	}
	/**
	 * ת�����㺯��
	 * @param formulaValue
	 * @return
	 */
	private String checkFormul(String formulaValue) {
//		if(formulaValue.indexOf("MAX")>=0||formulaValue.indexOf("MIN")>=0||formulaValue.indexOf("IF")>=0){
//			System.out.println(formulaValue);
//		}
		formulaValue = formulaValue.replaceAll("MAX", "EXCEL_MAX")
		.replaceAll("max", "EXCEL_MAX")
		.replaceAll("IF", "EXCEL_IF")
		.replaceAll("if", "EXCEL_IF")
		.replaceAll("MIN", "EXCEL_MIN")
		.replaceAll("min", "EXCEL_MIN")
		.replaceAll("ABS", "EXCEL_ABS")
		.replaceAll("abs", "EXCEL_ABS")
		.replaceAll("ROUND", "EXCEL_ROUND")
		.replaceAll("round", "EXCEL_ROUND");
	
		if(formulaValue.indexOf("SUM")>=0 || formulaValue.indexOf("sum")>=0){
			String convertFormul = "${" +formulaValue +"}";

			return convertFormul;
//		}else if(formulaValue.indexOf("round")>=0 || formulaValue.indexOf("ROUND")>=0){
//			return formulaValue;
		}else{
			return formulaValue;
		}
	}
	private static String[] subformul(String formul) {
		String[] formarr = new String[2];
		int len = formul.length();
		for(int i=0;i<len;i++){
			if(Character.isDigit(formul.charAt(i))){
				formarr[0] =  formul.substring(0, i);
				formarr[1] =  formul.substring(i);
				
				return formarr;
			}
		}
		return null;
	}
	
	/**
     * ���к�ת��Ϊ����
     * @param ref
     * @return
     */
    public static String convertNumToCol(int num) {
    	StringBuffer col= new StringBuffer() ;
    	if(num<26){
    		int acs = num + 'A';
    		col.append((char) acs);
    	}else{
    		int acs = num/26-1 + 'A';
    		int acs1 = num%26 + 'A';
    		col.append((char) acs).append((char) acs1);
    	}
    	return col.toString();
    }
	public String getPrintPath() {
		return printPath;
	}

	public void setPrintPath(String printPath) {
		this.printPath = printPath;
	}

	public FitechMessages getMessages() {
		return messages;
	}

	public void setMessages(FitechMessages messages) {
		this.messages = messages;
	}

}
