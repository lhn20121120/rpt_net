package com.fitech.gznx.form;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

//import com.fitech.hssmis.adapter.StrutsTemplateInfoDelegate;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.entity.Ccy;
import com.fitech.gznx.entity.GraphBean;

/**
 * �����װ���쵼��ҳ������ͼ��Ĳ�����ѯ��Ϣ
 * 
 * @author jack
 * 
 */
public final class LendingQueryForm extends ActionForm {



	private Map hm = new HashMap();

	/**
	 * ͳ��ͼ�����
	 */
	private String title;

	/**
	 * ͳ��ͼBEAN�б� �б�������:GraphBean
	 */
	private List gbs;

	/**
	 * ���ʵ�����ͼ
	 */
	private String naviGraph;

	/**
	 * ����ID
	 */
	private String orgId;
	


	/**
	 * ����ID����Ӧ�Ļ�������
	 */
	private String orgName;

	/**
	 * ����
	 */
	private String ccyId;

	/**
	 * ����γ���б�
	 */
//	private Ccy[] ccys = StrutsTemplateInfoDelegate.getAllCurr();

	/**
	 * �Ƿ��ǵ��һ�������
	 */
	private Integer isSigleOrg;

	/**
	 * �Ƿ��ǵ�һʱ���
	 */
	private Integer isOneTime = new Integer(1);

	/**
	 * ����ʱ��
	 */
	private String repDate;
	/**
	 * ����ʱ���б�
	 */
	private List repDateList;

	/** ********************������һҳѭ������(begin)****************************** */

	/**
	 * �Ƿ������һҳ ����Ϊy ������Ϊn
	 */
	private String existsNext;

	/**
	 * ��һ���ļ�������
	 */
	private String nextName;

	/**
	 * ��һҳRAQ�Ƿ��ǵ�ʱ���
	 */
	private String isOneTimeNext;

	/**
	 * ��һҳ�Ƿ��ǵ��һ���
	 */
	private String isSigleOrgNext;
	
	/**
	 * ͼ�μ��ϵ�����
	 */
	public String fitechGraphName = "";

	/** ********************������һҳѭ������(end)****************************** */
	
	private String reptYear = "";
	
	private String reptMonth = "";

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		return errors;
	}

	public LendingQueryForm() {

	}

	public String getCcyId() {
		return ccyId;
	}

	public void setCcyId(String ccyId) {
		this.ccyId = ccyId;
	}

//	public Ccy[] getCcys() {
//		return ccys;
//	}
//
//	public void setCcys(Ccy[] ccys) {
//		this.ccys = ccys;
//	}

	public String getExistsNext() {
		return existsNext;
	}

	public void setExistsNext(String existsNext) {
		this.existsNext = existsNext;
	}

	public Integer getIsOneTime() {
		return isOneTime;
	}

	public void setIsOneTime(Integer isOneTime) {
		this.isOneTime = isOneTime;
	}

	public String getIsOneTimeNext() {
		return isOneTimeNext;
	}

	public void setIsOneTimeNext(String isOneTimeNext) {
		this.isOneTimeNext = isOneTimeNext;
	}

	public Integer getIsSigleOrg() {
		return isSigleOrg;
	}

	public void setIsSigleOrg(Integer isSigleOrg) {
		this.isSigleOrg = isSigleOrg;
	}

	public String getIsSigleOrgNext() {
		return isSigleOrgNext;
	}

	public void setIsSigleOrgNext(String isSigleOrgNext) {
		this.isSigleOrgNext = isSigleOrgNext;
	}

	public String getNextName() {
		return nextName;
	}

	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRepDate() {
		return repDate;
	}

	public void setRepDate(String repDate) {
		this.repDate = repDate;
	}

	public String getNaviGraph() {
		return naviGraph;
	}

	public void setNaviGraph(String naviGraph, String orgId,String orgName) {

		this.naviGraph = naviGraph;
		
		this.orgName = orgName;

		String filePath = com.cbrc.smis.common.Config.WEBROOTPATH + Config.FILESEPARATOR
				+ "leadingXml" + Config.FILESEPARATOR + naviGraph + ".xml";

		this.conductInit(filePath, orgId); // ����XMLװ��ͼ��BEAN�б�

		// this.setGb(new GraphBean(naviGraph, orgId, filePath));

	}

	/**
	 * 
	 * ����˵��:�÷������ڽ���XMLװ��ͼ��BEAN�б�
	 * 
	 * @author jack
	 * @date 2009-12-12
	 */

	public void conductInit(String filePath, String orgId) {
		
		this.setOrgId(orgId);

		SAXReader saxReader = new SAXReader();

		Document document = null;

		try {

			document = saxReader.read(new File(filePath));

		} catch (DocumentException e) {

			e.printStackTrace();
		}
		Element root = (Element) document.selectNodes("fitechGraph").get(0);
		
		this.setFitechGraphName(root.attributeValue("title"));

		this.setTitle(root.attributeValue("title"));

		List list = root.selectNodes("graph");

	//	if (list != null && list.size() != 0)

			this.gbs = new ArrayList();

		for (int i = 0; i < list.size(); i++) {

			GraphBean graphBean = new GraphBean((Element) list.get(i), this.orgId,this.orgName,
					this.ccyId,this.repDate);

			this.gbs.add(graphBean);
		}
	}

	/**
	 * 
	 * ����˵��:�÷������ڴ����ѯ
	 * @author jack
	 * @date 2009-12-12
	 */
    public void conductQuery(){
    	
    	for(int i=0;i<this.gbs.size();i++){
    		
    		GraphBean gb = (GraphBean)gbs.get(i);
    		
    		gb.conductGraphBean(this.orgId,this.ccyId,this.repDate);
    	}
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setNaviGraph(String naviGraph) {
		this.naviGraph = naviGraph;
	}

	public List getGbs() {
		return gbs;
	}

	public void setGbs(List gbs) {
		this.gbs = gbs;
	}

	public Map getHm() {
		return hm;
	}

	public void setHm(Map hm) {
		this.hm = hm;
	}

	public List getRepDateList() {
		return repDateList;
	}

	public void setRepDateList(List repDateList) {
		this.repDateList = repDateList;
	}

	public String getFitechGraphName() {
		return fitechGraphName;
	}

	public void setFitechGraphName(String fitechGraphName) {
		this.fitechGraphName = fitechGraphName;
	}

	public String getReptMonth()
	{
		return reptMonth;
	}

	public void setReptMonth(String reptMonth)
	{
		this.reptMonth = reptMonth;
	}

	public String getReptYear()
	{
		return reptYear;
	}

	public void setReptYear(String reptYear)
	{
		this.reptYear = reptYear;
	}
}
