package com.fitech.gznx.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.DownLoadDataToZip;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFPBOCReportForm;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfPboccell;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFPbocUpCountDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AfPboccellDelegate;

public class ExportPbocAFReportAction extends Action {
	
	

	private static FitechException log = new FitechException(
			ExportPbocAFReportAction.class);
	
	private static String zeroS = "0";
	
	public static final String ZEROS;
	
	static{
		//根据配置的精度获取数值为0的数
		if(Integer.valueOf(Config.DOUBLEPERCISION)>0){
			zeroS += ".";
			for(int k=0;k<Integer.valueOf(Config.DOUBLEPERCISION);k++)
				zeroS +="0";
		}
		ZEROS = zeroS;
	}

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		FitechMessages messages = new FitechMessages();
		
		// 取得request范围内的请求参数，并存放在reportForm内
		AFPBOCReportForm pbocForm = (AFPBOCReportForm) form;
		RequestUtils.populate(pbocForm, request);
		
		// List对象的初始化
		List resList = new ArrayList();
		List adList = new ArrayList();
		
		// 取得当前用户的权限信息
		HttpSession session = request.getSession();
		Operator operator = null;
		
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
		//新老规范标志
		String versionFlag = "0";//默认新版 
		if (request.getParameter("versionFlg")!= null ){
			versionFlag = request.getParameter("versionFlg");
		}
		
		//文件规范类型名称
		String typeName = versionFlag.equals("1") ? "A" : "B";
		
		
		// 文件生成路径
		String srcFileName = Config.TEMP_DIR + File.separator
				+ operator.getOperatorId();
		
		DownLoadDataToZip dldtZip = DownLoadDataToZip.newInstance();
		
		File outfile = new File(srcFileName);
		
		if (outfile.exists())
			dldtZip.deleteFolder(outfile);
		
		outfile.mkdir();
		
		// 判断日期是否为空
		if (StringUtil.isEmpty(pbocForm.getDate())) {
			String yestoday = DateUtil.getToday();
			pbocForm.setDate(yestoday);
		}
		
		// 判断机构ID是否为空
		if (pbocForm.getOrgId() == null)
			pbocForm.setOrgId(operator.getOrgId());
		
		// 取得传递的参数
		String repInIdString = pbocForm.getRepInIds();
		
		// 全部下载时的参数
		String rep_ids = "";
		
		if (repInIdString == null || repInIdString.equals("")) {
			
			if (session.getAttribute("downLoadRh") != null) {
				
				List rsList = new ArrayList();
				
				rsList = (List) session.getAttribute("downLoadRh");
				
				for (int i = 0; i < rsList.size(); i++) {
					
					Aditing aditing = (Aditing) rsList.get(i);
					
					String obj = aditing.getOrgId() + ":"
							+ aditing.getRepInId() + ":"
							+ aditing.getActuFreqID() + ":"
							+ aditing.getTemplateId() + ":"
							+ aditing.getVersionId() + ":"
							+ aditing.getBatchId();
					
					rep_ids += (rep_ids == "" ? "" : ",") + obj;
				}
			}
			repInIdString = rep_ids;
		}
		
//		System.out.println(rep_ids);
		
		String reqorgId = pbocForm.getOrgId();
		String date = pbocForm.getDate();
		
		// 判断是否选择报表
		if (StringUtil.isEmpty(repInIdString)) {
			
			messages.add("没有选择报表");
			
		} else {
			
			String[] repFreqIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);
			
			HashMap mapb = new HashMap();
			
			// 批次 List
			List<String> batchList = new ArrayList();

			for (int i = 0; i < repFreqIds.length; i++) {
				
				String batchId = repFreqIds[i].split(":")[5];
				
				if (StringUtil.isEmpty(batchId)) {
					//如果是空，当第1批处理
					mapb.put("1", "1");
					batchList.add("1");
				}
				
				if (!mapb.containsKey(batchId)) {
					mapb.put(batchId, batchId);
					batchList.add(batchId);
				}
			}
			
			HashMap mapr = new HashMap();
			
			// 频度List
			List<String> repFreqIdList = new ArrayList();
			for (int i = 0; i < repFreqIds.length; i++) {
				String reqfreqId = repFreqIds[i].split(":")[2];
				if (StringUtil.isEmpty(reqfreqId)) {
					continue;
				}
				if (!mapr.containsKey(reqfreqId)) {
					mapr.put(reqfreqId, reqfreqId);
					repFreqIdList.add(reqfreqId);
				}
			}
			
			// 机构ID List
			List<String> orgIdList = new ArrayList();
			//机构Id和地区id之间的映射关系
			HashMap orgIdOfRegionIds = new HashMap();
			//机构Id和外报id之间的映射关系
			HashMap orgIdOfOuterIds = new HashMap();
			//机构Id和机构名称之间的映射关系
			HashMap orgIdOfOrgNames = new HashMap();
			
			
			
//			HashMap mapo = new HashMap();
//			//地区和机构的映射
//			HashMap mapor = new HashMap();
//			//机构和外报机构编号映射
//			HashMap mapoo = new HashMap();
//			
//			//保存机构id
//			HashMap mapOrgs = new HashMap();
//			
//			//add by chris 2014.3.24
//			//保存标准机构编码与地区编码对照关系
//			HashMap mapregs = new HashMap();

			//1为原有规则
			if(versionFlag.equals("1")){
				for (int i = 0; i < repFreqIds.length; i++) {
					String orgId = repFreqIds[i].split(":")[0];
					if (StringUtil.isEmpty(orgId)) {
						continue;
					}
					if(orgIdList.contains(orgId))
						continue;
					AfOrg aforginfo = AFOrgDelegate.getOrgInfo(orgId);
					
					//String regionId = String.valueOf(aforginfo.getRegionId());
					String regionId = String.format("%07d", aforginfo.getRegionId());
//					旧版报文 需要7位
					//regionId = ExportPbocAFReportAction.regionFermat(regionId, 7);
					
					orgIdOfRegionIds.put(aforginfo.getOrgId(), regionId);
					
					orgIdOfOrgNames.put(aforginfo.getOrgId(), aforginfo.getOrgName());
					
					orgIdList.add(aforginfo.getOrgId());
					
//					String outerId = "";
//					outerId = aforginfo.getRegionId().toString();//改为地区码
//					
//					//旧版报文 需要7位
//					outerId = ExportPbocAFReportAction.regionFermat(outerId, 7);
//					
//					if (!mapor.containsKey(orgId)) {
//						mapor.put(orgId, outerId);
//					}
//					if (!mapo.containsKey(outerId)) {
//						mapo.put(outerId, aforginfo.getOrgName());
//						//新增地区、外报机构映射
//						if(!mapoo.containsKey(outerId))
//							mapoo.put(outerId, getfullName(aforginfo.getRegionId().toString(),5));
//						
//						orgIdList.add(outerId);
//	
//					}
//					mapOrgs.put(outerId, aforginfo.getOrgId());
					
				}
			}else{
				//新机构按机构定
				for (int i = 0; i < repFreqIds.length; i++) {
					String orgId = repFreqIds[i].split(":")[0];
					if (StringUtil.isEmpty(orgId)) {
						continue;
					}
					if(orgIdList.contains(orgId))
						continue;
					AfOrg aforginfo = AFOrgDelegate.getOrgInfo(orgId);
					
					//String regionId = String.valueOf(aforginfo.getRegionId());
					
//					旧版报文 需要7位
					//regionId = ExportPbocAFReportAction.regionFermat(regionId, 7);
					
					String regionId = String.format("%07d", aforginfo.getRegionId());
					
					orgIdOfRegionIds.put(orgId, regionId);
					
					if(StringUtil.isEmpty(aforginfo.getOrgOuterId())){
						messages.add(aforginfo.getOrgName()+"[标准化机构编码为空,生成报文失败!]");
						request.setAttribute(Config.MESSAGES, messages);							
						return new ActionForward("/exportRhAFReport.do?styleFlg=new");
					}
					
					String orgOuterId = String.valueOf(aforginfo.getOrgOuterId());
					
//					新版报文 需要14位
					orgOuterId = ExportPbocAFReportAction.regionFermat(orgOuterId, 14);
					
					orgIdOfOuterIds.put(aforginfo.getOrgId(), orgOuterId);
					
					orgIdOfOrgNames.put(aforginfo.getOrgId(), aforginfo.getOrgName());
					
					orgIdList.add(aforginfo.getOrgId());
					
//					String regionID = aforginfo.getOrgOuterId();
//					if(StringUtil.isEmpty(regionID)){
//						messages.add(aforginfo.getOrgName()+"[标准化机构编码为空,生成报文失败!]");
//						request.setAttribute(Config.MESSAGES, messages);							
//						return new ActionForward("/exportRhAFReport.do?styleFlg=new");
//					}
//					//regionID = ExportPbocAFReportAction.regionFermat(regionID, 14);
//					if (!mapor.containsKey(orgId)) {
//						mapor.put(orgId, regionID);
//					}
//					if (!mapo.containsKey(regionID)) {
//						mapo.put(regionID, aforginfo.getOrgName());
//						//新增地区、外报机构映射
//						if(!mapoo.containsKey(regionID))
//							mapoo.put(regionID,regionID);
//						
//						orgIdList.add(regionID);
//	
//					}
//					mapOrgs.put(regionID, aforginfo.getOrgId());
//					//新版报文需要11位:3—6  机构类代码  7—13  地区代码  modify by chengang 2014.3.24
//					String orgregion = String.valueOf(aforginfo.getRegionId());
//					orgregion = ExportPbocAFReportAction.regionFermat(orgregion, 7);
//					if (!mapregs.containsKey(orgregion)) {
//						mapregs.put(regionID, orgregion);
//					}					
				}
			}
			
			// 创建人行机构代码
			//String rhOrgId = "";
			
			/* 
			 * 获得数据字典（总行人行编码及总行所在地区）
			 * 旧：数据字典类型编号183，共11位，
			 * 1-4位人行编码，5-11所在地区码
			 * 例：54011310001
			 * 新：数据字典类型编号185，共13位，
			 * 1位一级码，2位二级码，3-6位3级码（人行编码），7-13所在地区码
			 * 例：C154011310001
			 */
			/**2012-07-15修改 修改人行机构信息编码为application.properties配置文件中的code.lib(key) 
			 * 取消从数据库获取的过程*/
//			String codeTypeId = versionFlag.equals("1") ?
//								com.fitech.gznx.common.Config.RHORGIDTYPE :
//								com.fitech.gznx.common.Config.RHFORMATORGIDTYPE;
//			AfCodelib afcodelib = StrutsCodeLibDelegate.getCodeLib(codeTypeId,com.fitech.gznx.common.Config.RHORGIDID);
			
			
			if (!pbocForm.getOrgId().equals("'-999'")) {
				//不是生成全部机构
				String reqfreq=pbocForm.getRepFreqId();
				for (String orgId : orgIdList) {
					
					for (String reqfreqId : repFreqIdList) {
						
						for (String batchId : batchList) {
							/**2012-07-15修改 修改人行机构信息编码为application.properties配置文件中的code.lib(key) 
							 * 取消从数据库获取的过程*/
//							if (afcodelib != null && afcodelib.getCodeName() != null) {
//								
//								rhOrgId = afcodelib.getCodeName();
//							}
							
							// 下载文件名中的日期
							String reqfdate = DateUtil.getFreqDateLast(date,
									Integer.valueOf(reqfreqId.substring(0,1)));
							
							String repFreqName = "";
							String rhrepfreqid = "";
							
							if (reqfreqId.equals("1")) {
								rhrepfreqid = "4";
								repFreqName = "月报";
							} else if (reqfreqId.equals("2")) {
								rhrepfreqid = "3";
								repFreqName = "季报";
							} else if (reqfreqId.equals("3")) {
								rhrepfreqid = "1";
								repFreqName = "半年报";
							} else if (reqfreqId.equals("4")) {
								rhrepfreqid = "0";
								repFreqName = "年报";
							} else if (reqfreqId.equals("6")) {
								rhrepfreqid = "7";
								repFreqName = "日报";
							} else if (reqfreq.indexOf("9")>-1) {
								if(reqfreq.equals("91")){
									rhrepfreqid = "4";
									repFreqName = "年初结转（月）";
								}
								if(reqfreq.equals("92")){
									rhrepfreqid = "3";
									repFreqName = "年初结转（季）";
								}
								if(reqfreq.equals("93")){
									rhrepfreqid = "0";
									repFreqName = "年初结转（年）";
								}
							}else if(reqfreqId.equals("7")){
								rhrepfreqid = "6";
								repFreqName = "周(人行)";
							}else if(reqfreqId.equals("8")){
								rhrepfreqid = "5";
								repFreqName = "旬(人行)";
							}else if(reqfreqId.equals("10")){
								rhrepfreqid = "";
								repFreqName = "快(人行)";
							}
							
							//新旧机构格式 都以outerID命名
							//String formatOrgId = mapoo.get(outerId).toString();
								//getOrgNumber(rhOrgId,regionID,mapoo.get(regionID).toString());
							//新机构格式
							/**2012-07-15修改 修改人行机构信息编码为application.properties配置文件中的code.lib(key) 
							 * 取消从数据库获取的过程*/
							//2014-3-34 陈刚修改 formatOrgId=Config.CODE.LIB(前4位) + regionId
							String formatOrgId = Config.CODE_LIB.substring(0,4) + orgIdOfRegionIds.get(orgId);
								//是1则为原有规则
								//versionFlag.equals("1") ? rhOrgId.substring(0, 4) + outerId : mapoo.get(outerId).toString();
								//versionFlag.equals("1") ? Config.CODE_LIB.substring(0,4) + outerId : mapoo.get(outerId).toString(); 
								//versionFlag.equals("1") ? Config.CODE_LIB.substring(0,4) + outerId : Config.CODE_LIB.substring(0,4) + mapregs.get(outerId);
							
							// 创建文件名filename
							StringBuffer filename = new StringBuffer();
							
							filename.append(formatOrgId)
								.append(reqfdate.replaceAll("-", ""))
								.append(rhrepfreqid).append(batchId);
							
							// 创建报表报送次数time
							int time = AFPbocUpCountDelegate
									.saveorupdateAfPbocupcount(filename.toString());
							try{
								int num = Integer.parseInt(Config.RH_FORMAT_END);
								filename.append(num+"");
							}catch(Exception e){
								filename.append(time);
							}
							// 头文件输出流
							FileWriter bi = null;
							// 数据文件
							FileWriter bj = null;
							// 数据说明文件
							FileWriter bd = null;
							//增加缓冲池，提高速度
							BufferedWriter ai = null;
							BufferedWriter aj = null;
							BufferedWriter ad = null;
							
							String aifilename = "";
							String aifilepath = "";
							String ajfilename = "";
							String ajfilepath = "";
							String adfilename = "";
							String adfilepath = "";
							
							try {
								
								aifilename = typeName + "I" + filename + ".IDX";
								ajfilename = typeName + "J" + filename + ".DAT";
								adfilename = typeName + "D" + filename + ".TXT";
								
								aifilepath = srcFileName + File.separator
										+ aifilename;
								ajfilepath = srcFileName + File.separator
										+ ajfilename;
								adfilepath = srcFileName + File.separator
										+ adfilename;
								
								bi = new FileWriter(aifilepath);
								bj = new FileWriter(ajfilepath);
								bd = new FileWriter(adfilepath);
								//增加缓冲池，提高速度
								ai = new BufferedWriter(bi);
								aj = new BufferedWriter(bj);
								ad = new BufferedWriter(bd);
								
								ad.write(date + Config.BANK_NAME + "数据说明" + "\r\n");

								String str = ExportPbocAFReportAction.getFormatStr(orgId, orgIdOfRegionIds, orgIdOfOrgNames);
//								ad.write(//是1则为原有规则 modyfiy by chengang 2014/3/24
//										versionFlag.equals("1") ? str : "["+mapregs.get(outerId) 
//										+ " " + mapo.get(outerId)
//										+ "]\r\n");
								ad.write("["+str+"]\r\n");
								
								if (!StringUtil.isEmpty(pbocForm.getContents())) {
									ad.write(pbocForm.getContents());
								}

								String[] repInIds = repInIdString
										.split(Config.SPLIT_SYMBOL_COMMA);
								
								int inum = 1;
								for (int i = 0; i < repInIds.length; i++) {
									
									if(!repInIds[i].split(":")[5].equals(batchId))
										continue;
									if(!repInIds[i].split(":")[0].equals(orgId))
										continue;
									String templateId = null;
									String versionId = null;
									String rep_freq_id = null;
									String repInId = null;
									templateId = repInIds[i].split(":")[3];
									versionId = repInIds[i].split(":")[4];
									repInId = repInIds[i].split(":")[1];
									// repInId = AFReportDelegate.getReportId
									//			(pbocForm,templateId,versionId,reqfreqId);
									
									if (repInId == null)
										repInId = "0";
									
									ReportInForm reportInForm = AFReportDelegate
											.getReportIn(new Integer(repInId));

									StringBuffer aiindex = null;
									
									// 搜索报表定义中的报文定义
									List pboc = AfPboccellDelegate
											.getAFPbocCell(templateId, versionId);
									
									
									if (pboc != null && pboc.size() > 0) {
										
										for (int j = 0; j < pboc.size(); j++) {
											
											//报文关键字标号
											String imums = getfullName(
													String.valueOf(inum),
													//是1则为原有规则
													versionFlag.equals("1") ? 5:5);//modify by chengang 2014.3.24
											
											AfPboccell cell = (AfPboccell) pboc.get(j);
											
											aiindex = new StringBuffer();
											aiindex.append("I").append(imums)
												.append("|").append(templateId.substring(0, 5));
											
											//是1则为原有规则
											if(versionFlag.equals("1")){
												aiindex.append("|").append(formatOrgId.substring(0, 4))
														.append("|").append(orgIdOfRegionIds.get(orgId));
											}else{
												//modify by chengang2014/3/24
												aiindex.append("|").append(formatOrgId.substring(0, 4))
												.append("|").append(orgIdOfRegionIds.get(orgId));
											}
											
											aiindex.append("|").append(cell.getDataType())
												.append("|").append(cell.getCurId())
												.append("|").append(cell.getDanweiId())
												.append("|");
											
											boolean result = AfPboccellDelegate
													.isAFPbocCell(reportInForm, cell.getId().getColId());
											
											if (result) {
												aiindex.append("1");
											} else {
												aiindex.append("0");
											}
											
											aiindex.append("|").append(cell.getPsuziType());
											
											//修改新版系统，追加OuterId 2014.3.24 chengang
											if(versionFlag.equals("0")){
												aiindex.append("|").append(orgIdOfOuterIds.get(orgId));
											}
											
											ai.write(aiindex.toString()
													+ "\r\n");
											
											if (result) {
												List cellList = AfPboccellDelegate.getAFPbocCellList(reportInForm,cell.getId().getColId());
												//2012-02-22 LuYueFei新增：根据人行报表ID查询此报表所有单元格的数据，查询结果以键值对存放
												java.util.Map cellDataMap = AfPboccellDelegate.getAFPbocCellDataMap(reportInForm);
												//2012-02-22 LuYueFei
												if (cellList != null && cellList.size() > 0) {
													
													for (int cx = 0; cx < cellList.size(); cx++) {
														
														AfCellinfo cellinfo = (AfCellinfo) cellList.get(cx);
														//2012-02-22 LuYueFei注释
														//String cellData = AfPboccellDelegate.getAFPbocCellData(reportInForm,cellinfo.getCellId());
														//2012-02-22 LuYueFei注释
														String cellData = (String)cellDataMap.get(cellinfo.getCellId());
														
														
														/***
														 * 20120116修改，该处数据精度已配置文件
														 * application.properties中double.precision的值为准------------------开始
														 */
														if (!StringUtil.isEmpty(cellData)){
															if(Config.DOUBLEPERCISION_TEMPLATELIST.containsKey(templateId)){
																cellData = String.format("%1$."+Config.DOUBLEPERCISION_TEMPLATELIST.get(templateId)+"f", Double.valueOf(cellData));
															}else{
																cellData = String.format("%1$."+Config.DOUBLEPERCISION+"f", Double.valueOf(cellData));
															}
														}
														
														/***
														 * 2013年1月22日修改，河北行批量导出数据全为整数修改
														 * 精度为指定配置的精度-----------------------------------------------结束
														 */
														
														if (!StringUtil.isEmpty(cellData)
																&& !StringUtil.isEmpty(cellinfo.getCellPid())
																&& cellData != null) {
															if(!Config.DOUBLEPERCISION_TEMPLATELIST.containsKey(templateId)){
																if(cellData.equals(ZEROS))
																	continue;
															}else{
																String zeroStr = getDefaultZero(Integer.parseInt((String)Config.DOUBLEPERCISION_TEMPLATELIST.get(templateId)));
																if(cellData.equals(zeroStr))
																	continue;
															}
															StringBuffer ajstr = new StringBuffer();
															
															ajstr.append("I")
																.append(imums)
																.append("|").append(cellinfo.getCellPid())
																.append("|").append(cellData);
															
															aj.write(ajstr.toString() + "\r\n");
														}
													}
												}
											}
											inum++;
										}
									}else{
										messages.add("请在模版管理页面，修改报文定义！");
									}
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								if(ai!=null)
									ai.close();
								if(aj!=null)
									aj.close();
								if(ad!=null)
									ad.close();
								if(bi!=null)
									bi.close();
								if(bj!=null)
									bj.close();
								if(bd!=null)
									bd.close();
							}

							AFPBOCReportForm aipboc = new AFPBOCReportForm();
							aipboc.setFileName(aifilename);
							aipboc.setRepFreqName(repFreqName);
							aifilepath = aifilepath.replaceAll("\\\\","\\\\\\\\");
							
							aipboc.setFilePath(aifilepath);
							AFPBOCReportForm ajpboc = new AFPBOCReportForm();
							ajpboc.setFileName(ajfilename);
							ajpboc.setRepFreqName(repFreqName);
							ajfilepath = ajfilepath.replaceAll("\\\\","\\\\\\\\");
							
							ajpboc.setFilePath(ajfilepath);
							
							if (batchId.equals("0")) {
								aipboc.setBatchName("第零批");
								ajpboc.setBatchName("第零批");
							} else if (batchId.equals("1")) {
								aipboc.setBatchName("第一批");
								ajpboc.setBatchName("第一批");
							} else if (batchId.equals("2")) {
								aipboc.setBatchName("第二批");
								ajpboc.setBatchName("第二批");
							}
							
							resList.add(aipboc);
							resList.add(ajpboc);
							
							AFPBOCReportForm adpboc = new AFPBOCReportForm();
							adpboc.setFileName(adfilename);
							
							adfilepath = adfilepath.replaceAll("\\\\", "\\\\\\\\");
							
							adpboc.setFilePath(adfilepath);
							
							adList.add(adpboc);
						}
					}
				}
			}
			else {
				
				//全行代报
				if (Config.CODE_LIB != null) {
					
					//rhOrgId = afcodelib.getCodeName();
					
					// 下载文件名中的日期
					String reqfreq = "";
					reqfreq = pbocForm.getRepFreqId();
					
					String reqfdate = DateUtil.getFreqDateLast(date, Integer.valueOf(reqfreq.substring(0,1)));
					
					String repFreqName = "";
					String rhrepfreqid = "";
					
					for (String batchId : batchList) {

						for (String reqfreqId : repFreqIdList) {

					if (reqfreq.equals("1")) {
						rhrepfreqid = "4";
						repFreqName = "月报";
					} else if (reqfreq.equals("2")) {
						rhrepfreqid = "3";
						repFreqName = "季报";
					} else if (reqfreq.equals("4")) {
						rhrepfreqid = "0";
						repFreqName = "年报";
					}else if (reqfreqId.equals("6")) {
						rhrepfreqid = "7";
						repFreqName = "日报";
					}  else if (reqfreq.indexOf("9")>-1) {
						if(reqfreq.equals("91")){
							rhrepfreqid = "4";
							repFreqName = "年初结转（月）";
						}
						if(reqfreq.equals("92")){
							rhrepfreqid = "3";
							repFreqName = "年初结转（季）";
						}
						if(reqfreq.equals("93")){
							rhrepfreqid = "0";
							repFreqName = "年初结转（年）";
						}
					}else if(reqfreqId.equals("7")){
						rhrepfreqid = "6";
						repFreqName = "周(人行)";
					}else if(reqfreqId.equals("8")){
						rhrepfreqid = "5";
						repFreqName = "旬(人行)";
					}else if(reqfreqId.equals("10")){
						rhrepfreqid = "";
						repFreqName = "快(人行)";
					}
					
					
					// 创建文件名filename
					StringBuffer filename = new StringBuffer();
					String supplementFlag = "";
					
//					if (pbocForm.getSupplementFlag().equals("-999"))
//						supplementFlag = "2";
//					else
//						supplementFlag = pbocForm.getSupplementFlag();
					
					filename.append(Config.CODE_LIB);//机构类代码
					filename.append(reqfdate.replaceAll("-", ""))//日期
							.append(rhrepfreqid)//频度
							//.append(supplementFlag);
							.append(batchId);//批次
					
					// 创建报表报送次数time
					int time = AFPbocUpCountDelegate
							.saveorupdateAfPbocupcount(filename.toString());
					try{
						int num = Integer.parseInt(Config.RH_FORMAT_END);
						filename.append(num+"");//顺序号
					}catch(Exception e){
						filename.append(time);
					}
//					filename.append(time);
					
					// 头文件输出流
					FileWriter bi = null;
					// 数据文件
					FileWriter bj = null;
					// 数据说明文件
					FileWriter bd = null;
					//增加缓冲池，提高速度
					BufferedWriter ai = null;
					BufferedWriter aj = null;
					BufferedWriter ad = null;
					
					
					String aifilename = "";
					String aifilepath = "";
					String ajfilename = "";
					String ajfilepath = "";
					String adfilename = "";
					String adfilepath = "";
					
					aifilename = typeName + "I" + filename + ".IDX";
					ajfilename = typeName + "J" + filename + ".DAT";
					adfilename = typeName + "D" + filename + ".TXT";
					
					aifilepath = srcFileName + File.separator + aifilename;
					ajfilepath = srcFileName + File.separator + ajfilename;
					adfilepath = srcFileName + File.separator + adfilename;
					
					bi = new FileWriter(aifilepath);
					bj = new FileWriter(ajfilepath);
					bd = new FileWriter(adfilepath);
					//增加缓冲池，提高速度
					ai = new BufferedWriter(bi);
					aj = new BufferedWriter(bj);
					ad = new BufferedWriter(bd);
					
					ad.write(date + Config.BANK_NAME +"数据说明" + "\r\n");
					
					int inum = 1;
					
					for (String orgId : orgIdList) {
						
						//新机构格式
						String formatOrgId = 
						versionFlag.equals("1") ?
								Config.CODE_LIB.substring(0, 4) :
									Config.CODE_LIB.substring(0, 4);//modify by chengang 2014.2.24
									//getOrgNumber(rhOrgId,regionID,mapoo.get(regionID).toString());
					
						try {
							String str = ExportPbocAFReportAction.getFormatStr(orgId, orgIdOfRegionIds, orgIdOfOrgNames);
//							ad.write(//是1则为原有规则 modify by chengang 2014.2.24
//									versionFlag.equals("1") ? str : "["+mapregs.get(regionID)
//									+ " " + mapo.get(regionID)
//									+ "]\r\n");
							ad.write("["+str+ "]\r\n");
							
							if (!StringUtil.isEmpty(pbocForm.getContents())) {
								
								ad.write(pbocForm.getContents() + "\r\n");
							}
							
							String[] repInIds = repInIdString.split(Config.SPLIT_SYMBOL_COMMA);

							for (int i = 0; i < repInIds.length; i++) {
								
								if(!repInIds[i].split(":")[5].equals(batchId))
									continue;
								
//								if(!mapor.get(repInIds[i].split(":")[0]).equals(regionID))
//									continue;
								
								if(!repInIds[i].split(":")[0].equals(orgId))
									continue;
								
								String templateId = null;
								String versionId = null;
								String rep_freq_id = null;
								String repInId = null;
								
								templateId = repInIds[i].split(":")[3];
								versionId = repInIds[i].split(":")[4];
								repInId = repInIds[i].split(":")[1];
								
								// repInId = AFReportDelegate.getReportId
								//				(pbocForm,templateId,versionId,reqfreqId);
								
								if (repInId == null)
									repInId = "0";
								
								ReportInForm reportInForm = AFReportDelegate.getReportIn(
										new Integer(repInId));

								StringBuffer aiindex = null;
								
								// 搜索报表定义中的报文定义
								List pboc = AfPboccellDelegate.getAFPbocCell(templateId, versionId);
								
								if (pboc != null && pboc.size() > 0) {
									
									for (int j = 0; j < pboc.size(); j++) {
										
										//报文关键字标号
										String imums = getfullName(
												String.valueOf(inum),
												//是1则为原有规则
												versionFlag.equals("1") ? 5:5);//modify by chengang 2014.2.24
										
										AfPboccell cell = (AfPboccell) pboc.get(j);
										
										aiindex = new StringBuffer();
										
										aiindex.append("I")
												.append(imums)
												.append("|").append(templateId.substring(0, 5));
												
										if (versionFlag.equals("1")){
											aiindex.append("|").append(formatOrgId)
													.append("|").append(orgIdOfRegionIds.get(orgId));
										} else {
											aiindex.append("|").append(formatOrgId)
											.append("|").append(orgIdOfRegionIds.get(orgId));//modify by chengang
										}

										aiindex.append("|").append(cell.getDataType())
												.append("|").append(cell.getCurId())
												.append("|").append(cell.getDanweiId())
												.append("|");
										
										boolean result = AfPboccellDelegate
												.isAFPbocCell(reportInForm,
														cell.getId().getColId());
										
										if (result) {
											aiindex.append("1");
										} else {
											aiindex.append("0");
										}
										
										aiindex.append("|").append(cell.getPsuziType());
										//修改新版系统，追加OuterId 2014.3.24 chengang
										if(versionFlag.equals("0")){
											aiindex.append("|").append(orgIdOfOuterIds.get(orgId));
										}
										
										ai.write(aiindex.toString() + "\r\n");
										
										if (result) {
											List cellList = AfPboccellDelegate.getAFPbocCellList(reportInForm,cell.getId().getColId());
											//2012-02-22 LuYueFei新增：根据人行报表ID查询此报表所有单元格的数据，查询结果以键值对存放
											java.util.Map cellDataMap = AfPboccellDelegate.getAFPbocCellDataMap(reportInForm);
											//2012-02-22 LuYueFei
											
											if (cellList != null && cellList.size() > 0) {
												
												for (int cx = 0; cx < cellList.size(); cx++) {
													
													AfCellinfo cellinfo = (AfCellinfo) cellList.get(cx);
													
													//2012-02-22 LuYueFei注释
													//String cellData = AfPboccellDelegate.getAFPbocCellData(reportInForm,cellinfo.getCellId());
													//2012-02-22 LuYueFei注释
													String cellData = (String)cellDataMap.get(cellinfo.getCellId());
													
													/***
													 * 2013年1月22日修改，河北行批量导出数据全为整数修改
													 * 精度为指定配置的精度-----------------------------------------------开始
													 */
													if (!StringUtil.isEmpty(cellData)){
														if(Config.DOUBLEPERCISION_TEMPLATELIST.containsKey(templateId)){
															cellData = String.format("%1$."+Config.DOUBLEPERCISION_TEMPLATELIST.get(templateId)+"f", Double.valueOf(cellData));
														}else{
															cellData = String.format("%1$."+Config.DOUBLEPERCISION+"f", Double.valueOf(cellData));
														}
													}
													/***
													 * 2013年1月22日修改，河北行批量导出数据全为整数修改
													 * 精度为指定配置的精度-----------------------------------------------结束
													 */
													
													if (!StringUtil.isEmpty(cellData)
															&& !StringUtil.isEmpty(cellinfo
																	.getCellPid())
															&& cellData != null) {
														if(!Config.DOUBLEPERCISION_TEMPLATELIST.containsKey(templateId)){
															if(cellData.equals(ZEROS))
																continue;
														}else{
															String zeroStr = getDefaultZero(Integer.parseInt((String)Config.DOUBLEPERCISION_TEMPLATELIST.get(templateId)));
															if(cellData.equals(zeroStr))
																continue;
														}
														StringBuffer ajstr = new StringBuffer();
														
														ajstr.append("I")
															.append(imums)
															.append("|").append(cellinfo.getCellPid())
															.append("|").append(cellData);
														
														aj.write(ajstr.toString() + "\r\n");
													}
												}
											}
										}
										inum++;
									}
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							
						
					}
					
					ai.close();
					aj.close();
					ad.close();
					bi.close();
					bj.close();
					bd.close();
					
					AFPBOCReportForm aipboc = new AFPBOCReportForm();
					
					aipboc.setFileName(aifilename);
					aipboc.setRepFreqName(repFreqName);
					aifilepath = aifilepath.replaceAll("\\\\", "\\\\\\\\");
					aipboc.setFilePath(aifilepath);
					
					AFPBOCReportForm ajpboc = new AFPBOCReportForm();
					ajpboc.setFileName(ajfilename);
					ajpboc.setRepFreqName(repFreqName);
					ajfilepath = ajfilepath.replaceAll("\\\\", "\\\\\\\\");
					ajpboc.setFilePath(ajfilepath);
					
					if (batchId.equals("0")) {
						aipboc.setBatchName("第零批");
						ajpboc.setBatchName("第零批");
					} else if (batchId.equals("1")) {
						aipboc.setBatchName("第一批");
						ajpboc.setBatchName("第一批");
					} else {
						aipboc.setBatchName("第二批");
						ajpboc.setBatchName("第二批");
					}
					
					resList.add(aipboc);
					resList.add(ajpboc);
					
					AFPBOCReportForm adpboc = new AFPBOCReportForm();
					
					adpboc.setFileName(adfilename);
					adfilepath = adfilepath.replaceAll("\\\\", "\\\\\\\\");
					adpboc.setFilePath(adfilepath);
					adList.add(adpboc);
				}
			}
		}
		}
		}
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);

		if (resList != null && resList.size() > 0) {
			request.getSession().setAttribute(Config.RECORDS, resList);
		}
		String templateIds = request.getParameter("workTaskTemp");
		String workTaskTerm = request.getParameter("workTaskTerm");
		String workTaskOrgId = request.getParameter("workTaskOrgId");
		String urlParam = request.getParameter("urlParam");
		String parOrgId = request.getParameter("parOrgId");
		String systemSchemaFlag = request.getParameter("systemSchemaFlag");
		
		String lastParam = "";
		lastParam = lastParam + "date=" + date + "&orgId="
				+ pbocForm.getOrgId() + "&repFreqId=" + pbocForm.getRepFreqId()
				+ "&supplementFlag=" + pbocForm.getSupplementFlag()
				+"&workTaskOrgId="+workTaskOrgId
				+"&workTaskTemp="+templateIds
				+"&workTaskTerm="+workTaskTerm
				+"&urlParam="+urlParam
				+"&parOrgId="+parOrgId
				+"&systemSchemaFlag="+systemSchemaFlag;
		  
		request.setAttribute("lastParam", lastParam);
		request.setAttribute("versionFlag", versionFlag);
		if (adList != null && adList.size() > 0) {
			request.getSession().setAttribute("adList", adList);
		}
		return mapping.findForward("index");
	}

	private static String getfullName(String fname, int num) {
		StringBuffer ss = new StringBuffer();
		if (num > fname.length()) {

			for (int i = 0; i < num - fname.length(); i++) {
				ss.append("0");
			}

		}
		ss.append(fname);
		return ss.toString();
	}

	/**
	 * 获得新机构编号
	 * @param rhorgId 人行总行编号
	 * @param regionId 地区编号
	 * @param orgId 自编机构号
	 * @return
	 */
	private static String getOrgNumber(String rhorgId,String regionId,String orgId) {
		
		String newOrgId = "";
		
		//总行编号、地区码不能为空
		if(regionId.equals("") || regionId.equals("")){
			return newOrgId;
		}
		
		//当地区码为0时，即为总行，取总行所在区划
		if(regionId.equals("0000000"))
			regionId = rhorgId.substring(6,13);

		//编辑新机构号
		newOrgId = rhorgId.substring(0,6);
		
		//如果自定机构为空，暂取地区代码
		if(orgId.equals("")|| orgId==null)
			newOrgId += regionId;
		//新机构代码
		else
			newOrgId += regionId.substring(0,2)+orgId;
		
		//加入校验位
		newOrgId += getModNumber(newOrgId);
		
		return newOrgId;
		
	}
	
	
	/**
	 * 根据传入字串获得校验码（The Luhn Mod-10 Method）
	 * <br>
	 * Checks whether a string of digits is a valid credit card number according
	 * to the Luhn algorithm.
	 *
	 * 1. Starting with the second to last digit and moving left, double the
	 *    value of all the alternating digits. For any digits that thus become
	 *    10 or more, add their digits together. For example, 1111 becomes 2121,
	 *    while 8763 becomes 7733 (from (1+6)7(1+2)3).
	 *
	 * 2. Add all these digits together. For example, 1111 becomes 2121, then
	 *    2+1+2+1 is 6; while 8763 becomes 7733, then 7+7+3+3 is 20.
	 *
	 * 3. If the total ends in 0 (put another way, if the total modulus 10 is
	 *    0), then the number is valid according to the Luhn formula, else it is
	 *    not valid. So, 1111 is not valid (as shown above, it comes out to 6),
	 *    while 8763 is valid (as shown above, it comes out to 20).
	 * 
	 * @param number 机构编号13位
	 * @return 校验位值
	 */
	private static int getModNumber(String number) {
		int sum = 0;

		int n = 0;
		boolean alternate = true;
		
		for (int i = number.length() - 1; i >= 0; i--) {
			
			try{
				n = Integer.parseInt(number.substring(i, i + 1));
			}catch(Exception e){
				n = 0;
			}
			
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return sum % 10;
	}
	
	/***
	 * 报文的位数处理
	 * 新版报文需要14位 不足则自动补0，超过则截断
	 * 旧报文需要7位 不足则自动补0，超过则截断
	 * @param numCount
	 * @return
	 */
	private static String regionFermat(String region,int numCount){
		if(region==null || region.equals("")){
			String num = "";
			for(int j=0;j<numCount;j++)
				num+="0";
			return num;
		}
		if(region.length()<numCount){//
			int notNum = numCount-region.length();
			String num = "";
			for(int j=0;j<notNum;j++)
				num+="0";
			region+=num;
		}else
			region=region.substring(0,numCount);
		return region;
	}
	
	public static String getDefaultZero(int num){
		String zero = "0";
		if(num>0){
			zero+=".";
			for (int i = 0; i < num; i++) {
				zero+="0";
			}
		}
		return zero;
	}
	/**
	 * 
	 * @param outerId 标准机构编码
	 * @param mapOrgs 机构编码Map key=outerId
	 * @param mapo 机构名称Map key=outerId
	 * @return
	 */
	public static String getFormatStr(String orgId,Map orgIdOfRegionIds,Map orgIdOfOrgNames){
		StringBuffer sb = new StringBuffer();
		String[] strs = Config.RH_DESC_CONTE.split(",");
		if(strs!=null && strs.length>0){
			for(String str : strs){
				if(str.equalsIgnoreCase("orgId")){
					sb.append(orgId+"     ");
				}else if(str.equalsIgnoreCase("outerId")){
					sb.append(orgIdOfRegionIds.get(orgId)+"     ");
				}else if(str.equalsIgnoreCase("orgName")){
					sb.append(orgIdOfOrgNames.get(orgId));
				}
			}
		}
		return sb.toString();
	}
	
}
