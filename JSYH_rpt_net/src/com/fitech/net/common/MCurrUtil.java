package com.fitech.net.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCurr;

/**
 * 处理多币种的情况
 * @author jcm
 * 2006-10-29
 */
public class MCurrUtil {
		
	private static MCurrUtil mCurrUtil = null;
	
    /**
     * 是否已获取货币配置
     */
    private boolean initConfig = false;
	private MCurrUtil(){}

	public static MCurrUtil newInstance(){
		if(mCurrUtil == null) mCurrUtil = new MCurrUtil();
		
		return mCurrUtil;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 初始化多币种配置文件
	 * @return
	 */
	public List getMCurrReportInForm(){
		String filePath = Config.WEBROOTPATH +"xml"+File.separator + "currTypeConfig.xml";
		
		List list = null;		
		try {
			list = new ArrayList();
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(filePath));
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("childRepId"); i.hasNext();) {
				Element e= (Element) i.next();
				
				ReportInForm reportInForm = new ReportInForm();
				reportInForm.setChildRepId(e.attributeValue("id"));
				reportInForm.setRepName(e.attributeValue("repName"));				
				List currList = new ArrayList();
				for(Iterator iter=e.elementIterator();iter.hasNext();){					
					Element childE = (Element)iter.next();
					MCurrForm mCurrForm = new MCurrForm();	
					/**已使用hibernate 卞以刚 2011-12-21**/
					MCurr mCurr = StrutsMCurrDelegate.getMCurr(childE.getText());
					if(mCurr != null && mCurr.getCurId() != null) mCurrForm.setCurId(mCurr.getCurId());
					mCurrForm.setCurName(childE.attributeValue("id")+"_"+childE.getText());					
					currList.add(mCurrForm);
				}
				reportInForm.setMCurr(currList);
				list.add(reportInForm);
			}
            initConfig = true;//龚明2007-09-18修改
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-21
	 * 初始化多币种配置文件
	 * @return
	 */
	public Map getMutiMCurrReportInForm(){
		String filePath = Config.WEBROOTPATH  +"xml"+ File.separator + "currTypeConfig.xml";
		
		Map map = new HashMap();	
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(filePath));
			Element root = document.getRootElement();
			for (Iterator i = root.elementIterator("childRepId"); i.hasNext();) {
				Element e= (Element) i.next();
				
				ReportInForm reportInForm = new ReportInForm();
				reportInForm.setChildRepId(e.attributeValue("id"));
				reportInForm.setRepName(e.attributeValue("repName"));				
				List currList = new ArrayList();
				for(Iterator iter=e.elementIterator();iter.hasNext();){					
					Element childE = (Element)iter.next();
					MCurrForm mCurrForm = new MCurrForm();
					/**已使用hibernate 卞以刚 2011-12-21**/
					MCurr mCurr = StrutsMCurrDelegate.getMCurr(childE.getText());
					if(mCurr != null && mCurr.getCurId() != null) mCurrForm.setCurId(mCurr.getCurId());
					mCurrForm.setCurName(childE.attributeValue("id")+"_"+childE.getText());					
					currList.add(mCurrForm);
				}
				reportInForm.setMCurr(currList);
				map.put(reportInForm.getChildRepId(),reportInForm);
			}
            initConfig = true;//龚明2007-09-18修改
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 根据报表id判断该报表是否是多币种报表
	 * @param childRepId
	 * @return
	 */
	public List isExist(String childRepId){
		
		List mCurrFormList = null;		
		//this.getMCurrReportInForm();
        //龚明2007-09-18修改            2007-12-9 吴昊修改
        List currReportInFormList = null;
        /**已使用hibernate 卞以刚 2011-12-22**/
        currReportInFormList = getMCurrReportInForm();

		if(currReportInFormList == null || currReportInFormList.size() <= 0) return mCurrFormList;
		
		ReportInForm resultForm = null;		
		for(int i=0;i<currReportInFormList.size();i++){
			ReportInForm currReportInForm = (ReportInForm)currReportInFormList.get(i);
			
			if(currReportInForm.getChildRepId().equals(childRepId)){
				resultForm = currReportInForm;
				break;
			}
		}
		
		if(resultForm != null && resultForm.getMCurr() != null && resultForm.getMCurr().size() > 0)
			return resultForm.getMCurr();
		
		return null;
	}
}
