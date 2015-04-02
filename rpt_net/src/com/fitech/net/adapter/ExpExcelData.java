package com.fitech.net.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hsqldb.lib.StringUtil;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.util.UtilForExcelAndRaq;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfOtherreportdata;
import com.fitech.gznx.po.AfPbocreportdata;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.util.ExcelUtil;

public class ExpExcelData {
	/**
	 * 根据repInId查询数据,调用写入excel数据的方法
	 * 
	 * @param map
	 * @param response
	 */
	public static void expData(Map map, HttpServletResponse response,String operatorId) throws Exception {
		Map datas = new HashMap();
		String str = "";
		String fileName = null;
		String templateId= "";
		String versionId = "";
		if (map != null && map.size() > 0) {
			if (map.containsKey("repInId")) {
				String reportFlg = (String)map.get("reportFlg");
				//取监管单元格数据
				if(reportFlg!=null && reportFlg.equals("1")){
					List<ReportInInfo> ris = StrutsExcelData.getData1(map);
					if (ris != null && ris.size() > 0) {
						for (int i = 0; i < ris.size(); i++) {
							ReportInInfo ri = ris.get(i);
							str = ri.getMCell().getMChildReport().getComp_id()
									.getChildRepId()
									+ ri.getMCell().getMChildReport().getComp_id()
									.getVersionId() + ".xls";
							if(StringUtil.isEmpty(templateId)){
								templateId = ri.getMCell().getMChildReport().getComp_id()
								.getChildRepId();
								versionId= ri.getMCell().getMChildReport().getComp_id()
								.getVersionId();
							}
							//int dateType  = ri.getMCell().getDataType() ;
							if(ri.getMCell().getDataType()!=null){
								int dateType  = ri.getMCell().getDataType() ;
								datas.put(ri.getMCell().getCellName(), ri.getReportValue()+"@@"+dateType);
							}else{
								datas.put(ri.getMCell().getCellName(), ri.getReportValue());
							}
						}
					}
					fileName=Config.WEBROOTPATH+File.separator+"report_mgr"+File.separator+"excel"+File.separator+str;
				}//取人行单元格数据
				else if(reportFlg!=null && reportFlg.equals("2")){
					List<AfPbocreportdata> aets = StrutsExcelData.getData2(map);
					if (aets != null && aets.size() > 0) {
						for (int i = 0; i < aets.size(); i++) {
							AfPbocreportdata aet = aets.get(i);
							AfCellinfo af = StrutsExcelData.getCellInfo(aet.getId().getCellId());
							str = af.getTemplateId()+"_"+af.getVersionId()+ ".xls";
							datas.put(af.getCellName(),aet.getCellData());
							if(StringUtil.isEmpty(templateId)){
								templateId=af.getTemplateId();
								versionId= af.getVersionId();
							}
						}
					}
					fileName = Config.RAQ_TEMPLATE_PATH+"templateFiles" + File.separator +"excel" + File.separator +str;
				}//取其他报表单元格数据
				else if(reportFlg!=null && reportFlg.equals("3")){
					List<AfOtherreportdata> aos = StrutsExcelData.getData3(map);
					if (aos != null && aos.size() > 0) {
						for (int i = 0; i < aos.size(); i++) {
							AfOtherreportdata ao = aos.get(i);
							AfCellinfo af = StrutsExcelData.getCellInfo(ao.getId().getCellId());
							str = af.getTemplateId()+"_"+af.getVersionId()+ ".xls";
							datas.put(af.getCellName(),ao.getCellData());
							if(StringUtil.isEmpty(templateId)){
								templateId = af.getTemplateId();
								versionId= af.getVersionId();
							}
						}
					}else{
						if(map.get("repInId")!=null){
							AfReport af = StrutsExcelData.getAfReport(map.get("repInId").toString());
							str =af.getTemplateId()+"_"+af.getVersionId()+".xls";
							if(StringUtil.isEmpty(templateId)){
								templateId = af.getTemplateId();
								versionId= af.getVersionId();
							}
						}
					}
					fileName = Config.RAQ_TEMPLATE_PATH+"templateFiles" + File.separator +"excel" + File.separator +str;
				}
				//如果系统中不存在1104excel模板
				if(!new File(fileName).exists()){
					String raqFilePath = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH+File.separator+"templateFiles"
						+ File.separator + "Raq"+ File.separator+templateId + "_" + versionId + ".raq";
					String produceExcelPath = fileName;
					if(!StringUtil.isEmpty(templateId)){
						//调用润乾API生成excel模板
						UtilForExcelAndRaq.createExcelForRaq(raqFilePath, produceExcelPath, templateId);
					}
				}
				datas.put("path", fileName);
				datas.put("repInId", map.get("repInId").toString());
				datas.put("reportFlg", map.get("reportFlg").toString());
				datas.put("templateId", templateId);
				datas.put("versionId", versionId);
				ExcelUtil.writeDataExcel(datas, response,operatorId);
			}
		}
	}
}
