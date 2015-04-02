package com.fitech.gznx.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.SearchPbocDataDelegate;
import com.fitech.gznx.vo.SearchVo;

public class SearchPBOCDataTestAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			SearchVo vo = new SearchVo();
			Map<String, String> itemMap = new LinkedHashMap<String,String>();
			// ����<2014-09��2014-09>
			Map<String ,String> dateMap = new  TreeMap<String ,String>();
			// ����<0900,����>
			Map<String ,String> orgMap = new TreeMap<String ,String>();
			// 数据
			Map<String,String> dataMap = new HashMap<String,String>();
			
			
//			dataMap :{331010000_cellindex_G13040690K8_2012-09_1=0, 331010000_cellindex_G13040690K8_2012-06_1=0}
//
//			dateMap {2012-06=2012-06, 2012-07=2012-07, 2012-08=2012-08, 2012-09=2012-09, 2012-10=2012-10}
//
//			itemMap {cellindex_G13040690K8=3 | ������ĩ�������Ų��ɳ����ĳ�ŵ�����и�ծ}
//
//			orgIds [331010000, 001, 002];
//
//			orgMap {001=�Ϻ�����, 002=��Ͻ����, 331010000=��������}
			dataMap.put("331010000_cellindex_G13040690K8_2012-09_1", "99");
			dataMap.put("331010000_cellindex_G13040690K8_2012-06_1", "66");
			dataMap.put("331010000_cellindex_G13040690K8_2012-08_1", "88");
			
			orgMap.put("001", "�Ϻ�����");
			orgMap.put("002", "��������");
			orgMap.put("331010000", "�Ϻ�������");
			
			itemMap.put("cellindex_G13040690K8", "3 | ������ĩ�������Ų��ɳ����ĳ�ŵ�����и�ծ");
			
			dateMap.put("2012-06","2012-06");
			dateMap.put("2012-07","2012-07");
			dateMap.put("2012-08","2012-08");
			dateMap.put("2012-09","2012-09");
			vo.setDateMap(dateMap);
			vo.setItemMap(itemMap);
			vo.setDataMap(dataMap);
			vo.setOrgMap(orgMap);
			request.setAttribute("vo", vo);	
			request.setAttribute("dataMap", dataMap);	
			return mapping.findForward("index");
	}
}
