package com.fitech.papp.webservice.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;
import com.fitech.papp.webservice.pojo.WebSysOrg;
import com.fitech.papp.webservice.service.OrgService;
import com.fitech.papp.webservice.util.Constant;

/**
 * http://localhost:8080/portal/services/OrgWebService
 */
public class OrgServiceImpl implements OrgService {
	public static Logger log = Logger.getLogger(OrgServiceImpl.class);

	/**
	 * 更新机构信息
	 * 
	 * @param org
	 * @return String "0": 成功 
	 * "1": 机构不存在
	 *  "2": 记录未通过客户风险校验 
	 *  "3": 系统错误 新增未完成     c
	 *  "-1": 上级机构不存在
	 * @throws Exception
	 */
	@Override
	public String updateOrg(WebSysOrg org) {
		String result = "";
		OrgInfoForm orgInfoForm = new OrgInfoForm();
		com.fitech.net.adapter.TranslatorUtil.copyProperties(orgInfoForm, org);
		AfOrg parentAfOrg = null;
		org.setOrgAreaId("0");
		// 0:修改机构失败；1：修改机构成功；2：需要修改汇总关系
		// int result = 1;
		// 是否为虚拟机构，0为真实机构，1为虚拟机构
//		int isCollect = 1;
		try {
			// 得到父节点的详细信息
			parentAfOrg = AFOrgDelegate.getOrgInfo(org.getHigherOrgId());
			if(parentAfOrg==null){
				return "-1";
			}
			// 改变机构代码
			if (StringUtil.isEmpty(org.getOrgId())) {
				result = "3";
				return result;
			} else {
				if (!AFOrgDelegate.isExistSameOrgId(orgInfoForm)) {//如果修改的机构在rpt_net库不存在
					String flag = insertOrg(org);//调用新增机构代码
					if(flag.equals("0"))
						result  = Constant.ORG_UPDATE_SUCCESS;
					else
						result = "3";
				}else{
					/** 1 判断是否有相同的机构名称(真实机构的名称禁止修改) */
					if (AFOrgDelegate.isExistSameOrgName(orgInfoForm)) {
						result = "3";
					}
					/** 2更新机构信息 */
					if (AFOrgDelegate.updateOrgInfo(orgInfoForm)) {
						OrgNetForm orgNetForm = new OrgNetForm();
						// 同步更新1104部分
						com.fitech.net.adapter.TranslatorUtil.copyProperties(
								orgNetForm, org);
						StrutsOrgNetDelegate.update(orgNetForm);
						result = "0";
					} else {
						result = "3";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "3";
		}
		return result;
	}

	/**
	 * 新增机构信息
	 * 
	 * @param org
	 * @return String "0" : 成功 "1" : 机构已经存在 "2" : 记录未通过客户风险校验 "3" : 系统错误 新增未完成
	 *         "4" : 新增的是总行，客户风险中已经有总行 不允许出现多个总行
	 * @throws Exception
	 */
	@Override
	public String insertOrg(WebSysOrg org) {
		String result = "";
		OrgNetForm orgNetForm = null;
		// BeanUtils.copyProperties(orgNetForm, org);
		// 得到父节点的详细信息
		AfOrg parentAfOrg = AFOrgDelegate.getOrgInfo(org.getHigherOrgId());
		// 0:增加机构失败；1：增加机构成功；2：需要增加汇总关系
		try {
			if (parentAfOrg != null && parentAfOrg.getIsCollect() != null
					&& parentAfOrg.getIsCollect().equals(Config.IS_COLLECT)) {
				// 虚拟机构不可添加子机构
				result = "3";
				return result;
			}
			/** 1 判断是否为空 */
			if (org.getOrgId() == null || org.getOrgName() == null
					|| org.getOrgId().trim().equals("")
					|| org.getOrgName().trim().equals("")) {
				// 加载错误信息
				result = "3";
				return result;
			}
			/** 1 判断是否有相同的机构代码 */
			if (AFOrgDelegate.isExistSameOrgId(org.getOrgId())) {
				// 加载错误信息
				return "1";
			}

			/** 2 判断是否有相同的机构名称 */
			if (AFOrgDelegate.isExistSameOrgName(org.getOrgName())) {
				// 加载错误信息
				return "1";
			}
			org.setOrgType(org.getOrgLevel());
			org.setOrgAreaId("0");
			System.out.println(org);
			if (AFOrgDelegate.add(org)) {
				orgNetForm = new OrgNetForm();
				com.fitech.net.adapter.TranslatorUtil.copyProperties(
						orgNetForm, org);

				if (orgNetForm.getPre_org_id() == null
						|| orgNetForm.getPre_org_id().equals("")) {
					orgNetForm.setPre_org_id(Config.VIRTUAL_TOPBANK);
					orgNetForm.setOrg_type_id(new Integer(1));
				}
				int i = StrutsOrgNetDelegate.create(orgNetForm);
				if(i==2){
					result = "1";
				}else if(i==1) {
					result = "0";
				}else{
					result = "3";
				}
				return result;
			} else {
				return "3";
			}
		} catch (Exception e) {
			/** 错误则返回原页面 */
			e.printStackTrace();
			AFOrgDelegate.delete(org.getOrgId());
			result = "3";

		}
		return result;
	}

	/**
	 * 删除机构信息
	 * 
	 * @param org
	 * @return String "0"： 成功 "1": 机构不存在 不允许删除 "2"： 机构存在 下级机构，不允许删除 "3": 系统错误
	 *         新增未完成
	 * @throws Exception
	 */
	@Override
	public String deleteOrg(WebSysOrg org) {
		String result = "";
		try {
			String orgId = org.getOrgId();
			if (orgId == null || orgId.equals("")) {
				return "3";
			}

			AfOrg oi = AFOrgDelegate.getOrgInfo(orgId);
			if (null == oi) {
				return "2";
			}
			/** 1如果该机构为根节点"总行"，则禁止删除 */
			if (oi.getPreOrgId() != null
					&& oi.getPreOrgId().equals(Config.TOPBANK)) {
				return "3";
			}
			/** 2 判断删除的机构是否有子机构，如果有，则必须先删除子机构 */
			List lstChildList = AFOrgDelegate.getChildList(orgId);
			if (lstChildList != null && lstChildList.size() > 0) {
				return "3";
			}
			/** 3 判断删除的机构是否有下属人员，如果有，则必须先删除人员 */
			if (AFOrgDelegate.hasUsers(orgId)) {
				return "3";
			}

			// /** 4 判断删除的[真实]机构是否有对应的指标集数据，如果有，则禁止删除，指标集数据需要到后台删除 */
			// if (!oi.getIsCollect().toString().equals(Config.IS_COLLECT)) {
			// if (AFOrgDelegate.hasMeasureAlert(orgId)) {
			// addMyMessage(request, "AfOrg.has.hasMeasureAlert");
			// return mapping.getInputForward();
			// }
			// }
			/** 5 删除机构信息 */
			boolean flag  = AFOrgDelegate.delete(oi.getOrgId());
			System.out.println(flag);
			if (flag) {
				// 同步更新1104部分
				OrgNetForm orgNetForm = new OrgNetForm();
				com.fitech.net.adapter.TranslatorUtil.copyProperties(
						orgNetForm, org);
				
				if( StrutsOrgNetDelegate.remove(orgNetForm)){
					return "0";
				}
				else
					return "3";
			} else {
				return "2";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "3";
		}
		return result;
	}

}
