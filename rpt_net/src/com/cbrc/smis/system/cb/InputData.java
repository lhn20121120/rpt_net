/*
 * Created on 2005-12-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.system.cb;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.cbrc.smis.adapter.Procedure;
import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDataDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.common.ConductString;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.common.XmlUtils;
import com.cbrc.smis.common.ZipUtils;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechPDF;
import com.fitech.net.Excel2Xml.Excel2XML;

/**
 * @author cb
 * 
 * �����ܹ��Ա���XML�����ļ����д���, ����ǵ�Ե�ʽ�Ľ�ֱ�ӽ��д��� ������嵥ʽ�Ľ����û����InputDataByListing����ͳһ����
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InputData {

	public static String isOnLine = null;

	public static String messageInfo = "";

	/**
	 * ������ZIP�ļ�
	 * 
	 * @param file
	 *            ZIP���ļ�����
	 * @throws Exception
	 */
	public void conductZip(File file) throws Exception {

		ReportIn reportIn = null;

		ZipUtils zipUtils = new ZipUtils();

		String errorMessage = "";

		File dir = new File(ConfigOncb.TEMP_DIR); // dir�ǽ�ѹ���ļ���ŵĵ�ַ(�õ�ַ��һ����ʱ��ַ,����ͻ�ɾ�����е��ļ�)

		File[] files = null;

		File listing = null;

		try {

			files = zipUtils.expandFile(file, dir); // �������ZIP�ļ����н�ѹ��

		} catch (Exception e) {

			errorMessage = "";

			errorMessage = "��ѹ" + file.getName() + "���ļ�ʱʧ��";

			throw new Exception(errorMessage);
		}

		listing = new File(ConfigOncb.TEMP_DIR + Config.FILESEPARATOR + ConfigOncb.EXPLAINFILENAME); // ����һ��˵���ļ�����

		if (!(listing.exists())) { // �ж��Ƿ����˵��XML(listing.xml)�ļ� ���������׳��쳣

			errorMessage = "";

			errorMessage = "��ѹ" + file.getName() + "���ļ�ʱ" + "���ֲ�����listing.xml�ļ�";

			throw new Exception(errorMessage);

		}

		String dataFileName = this.getZipName(listing); // ͨ����ȡlisting.xml�ļ����fileNameԪ�صõ������ļ���

		String zipFileName = ConfigOncb.TEMP_DIR + Config.FILESEPARATOR + dataFileName; // �õ�ZIP�ļ���

		File zipFile = new File(zipFileName);

		if (!zipFile.exists()) {

			errorMessage = "";

			errorMessage = file.getName() + "���" + zipFile.getName() + "�����ļ�������";

			throw new Exception(errorMessage);
		}

		try {

			zipUtils.expandFile(zipFile, dir); // �������ZIP�ļ������ZIP�ļ����н�ѹ��

			/*
			 * // System.out.println("----------------COUNT=" +
			 * dir.listFiles().length + "���ļ�------------------");
			 */

		} catch (Exception e) {

			errorMessage = "";

			errorMessage = "��ѹ" + file.getName() + "�����" + dataFileName + "�����ļ�ʱ��������";

			throw new Exception(errorMessage);
		}

		Element listingRoot = XmlUtils.getRootElement(listing); // ˵��XML�ļ��ĸ�Ԫ��

		File xmlFile = null;

		for (Iterator i = listingRoot.elementIterator(); i.hasNext();) { // �����￪ʼ��ZIP���е�XML�����ļ�����ѭ������

			List errorList = new ArrayList();

			Element listingE = (Element) i.next();

			if (listingE.getName().equals(ConfigOncb.REPORT)) { // �ж�Ԫ�����Ƿ���report

				String xmlName = this.createXmlName(listingE);

				xmlName = ConductString.getStringNotSpace(xmlName); // ȥǰ��ո�

				xmlFile = new File(ConfigOncb.TEMP_DIR + Config.FILESEPARATOR + xmlName); // ����XML�����ļ���

				if (xmlFile.exists()) { // �ж�ƴ�������ļ�����ָ���ļ��Ƿ����

					// System.out.println(xmlFile);

					reportIn = this.createReportIn(listingE, xmlFile, file.getName()); // ����洢ʵ�ʱ���

					if (reportIn == null) { // ����洢����ʱʧ��������ȥ
						continue;
					}

					StrutsReportInDataDelegate.insertReortInData(xmlFile, reportIn.getRepInId()); // ��XML�ļ����

					if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

						Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

						for (Iterator ii = popRoot.elementIterator(); ii.hasNext();) {

							Element ee = (Element) ii.next();

							if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

								for (Iterator iii = ee.elementIterator(); iii.hasNext();) { // �������XML�����ļ�����ѭ������

									Element eee = (Element) iii.next();

									String elementName = eee.getName();

									if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��

										String row_String = this.getRow(elementName);

										Integer rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�

										String colId = this.getCol(elementName, row_String); // �õ���Ԫ���к�

										String reportValue = eee.getStringValue(); // �õ�����ֵ

										reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�

										String childRepId = this.getElementValue(listingE, ConfigOncb.REPORTID);

										String versionId = this.getElementValue(listingE, ConfigOncb.VERSION);

										MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����

										if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��

											continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
										}

										StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, file.getName(), xmlFile
												.getName());
									}
								}

							}
						}

					} else { // ���ｫ���嵥ʽ��������Ӧ����

						if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��

							this.conductG5301(xmlFile, listingE, file, reportIn);

							continue;
						}

						String tableName = this.getTableName(listingE);

						if (tableName.equals("")) {

							errorMessage = "";

							errorMessage = file.getName() + "���ļ��е�" + xmlFile.getName() + "�е�XML�����ļ���ʽ����ȷ";

							FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");

							continue;

						}

						else { // ������ת����InputDataByListing���������嵥ʽ�ı���
							InputDataByListing dbl = new InputDataByListing();

							dbl.controler(xmlFile, tableName, reportIn.getRepInId(), reportIn.getMChildReport().getComp_id().getChildRepId(), file.getName(), xmlFile
									.getName());

						}

					}

					xmlFile.delete();

				} else {

					errorMessage = "�ڽ�����ǩʱ����" + file.getName() + "��������" + xmlName + ".xml�����ļ��е�";

					for (int m = 0; m < errorList.size(); m++) {

						errorMessage = errorMessage + errorList.get(m);

					}

					errorMessage = errorMessage + "��" + errorList.size() + "��������";

					FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
				}

				/*
				 * if (xmlFile.exists())
				 * 
				 * xmlFile.delete();
				 * 
				 * if (bankCer.exists())
				 * 
				 * bankCer.delete();
				 * 
				 * if (operatorCer.exists())
				 * 
				 * operatorCer.delete();
				 * 
				 * if (xmlFileByDark.exists())
				 * 
				 * xmlFileByDark.delete();
				 */

			}
		}
		/* this.deleteAllFile(); */// ɾ�����е���ʱ�ļ�
	}

	public boolean isG5312(File xmlFiel) throws Exception {

		Element root = XmlUtils.getRootElement(xmlFiel);

		String name = root.getName();

		if (!name.equals(ConfigOncb.G5301))

			return false;

		return true;
	}

	public void conductG5301(File xmlFile, Element listingE, File file, ReportIn reportIn) throws Exception {

		String errorMessage = "";

		Element root = XmlUtils.getRootElement(xmlFile);

		Element element = null;

		String elementName = "";

		for (Iterator i = root.elementIterator(); i.hasNext();) {

			element = (Element) i.next();

			elementName = element.getName();

			/*
			 * if (!this.isMCellElementName(elementName)) // �ж��Ƿ�����ȷ�ĵ�Ԫ��Ԫ��
			 * continue; String row_String = this.getRow(elementName); Integer
			 * rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�
			 * String colId = this.getCol(elementName); // �õ���Ԫ���к�
			 */

			String reportValue = element.getStringValue(); // �õ�����ֵ
			reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
			String childRepId = this.getElementValue(listingE, ConfigOncb.REPORTID);
			String versionId = this.getElementValue(listingE, ConfigOncb.VERSION);

			// MCell mCell = StrutsMCellDelegate.getMCell(childRepId,
			// versionId,rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
			MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, elementName);
			if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
				continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��

			}

			StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, file.getName(), xmlFile.getName());

		}

	}

	/**
	 * ��������Ľ��ƴ��һ��XML�ļ���
	 * 
	 * @param e
	 * @return String XML�ļ���
	 * @throws Exception
	 */
	public String createXmlName(Element e) throws Exception {

		String xmlName = this.getElementValue(e, ConfigOncb.ORGID);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.REPORTID);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.VERSION);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.DATARANGEID);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.YEAR);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.MONTH);

		// xmlName = xmlName + this.getElementValue(e, ConfigOncb.TERM);

		xmlName = xmlName + (!this.getElementValue(e, ConfigOncb.FITECHCURR).equals(ConfigOncb.COMMONCURRNAME) ? this.getElementValue(e, ConfigOncb.FITECHCURR) : "");

		xmlName = xmlName + ".xml";

		/* xmlName = xmlName; */

		return xmlName;

	}

	/**
	 * �÷������ڸ���listing�е�report�ڵ�� XML�����ļ�������һ��ReportIn(ʵ�ʱ�����) ��������
	 * 
	 * @param element
	 *            listing�е�report�ڵ�
	 * @param fileXml
	 *            XML�����ļ�����
	 * @param zipFile
	 *            ע��������ZIP���ļ�������,�����ǳ��ڴ�����־��ѯ�Ŀ���
	 * @return ʵ�ʱ�����
	 * @throws Exception
	 */
	public ReportIn createReportIn(Element element, File fileXml, String zipFileName) throws Exception {

		Element root = element;

		String childRepId = this.getElementValue(root, ConfigOncb.REPORTID); // ��listingXml���ӱ���ID

		String versionId = this.getElementValue(root, ConfigOncb.VERSION); // ��listingXml�汾ID

		String orgId = this.getElementValue(root, ConfigOncb.ORGID); // ��listingXml����ID

		String mCurrName = this.getElementValue(root, ConfigOncb.FITECHCURR); // ��listingXml����ID

		String writer = this.getElementValue(root, ConfigOncb.WRITER); // ��listingXml�õ�����

		String checker = getElementValue(root, ConfigOncb.CHECKER);

		String principal = getElementValue(root, ConfigOncb.PRINCIPAL);

		String times_String = this.getElementValue(root, ConfigOncb.TIMES); // ��listingXml�õ�����

		String repOutIdString = this.getElementValue(root, ConfigOncb.REPOUTID); // ��listingXml�еõ��������ݼ�¼ID

		Integer repOutId = null;

		String repName = null;

		Date date = null;

		MCurr mCurr = null;

		MDataRgType mrt = null;

		MRepRange mrr = null;

		MChildReport mChildReport = null;

		Integer times = null;

		Integer year = null;

		String errorMessage = "";

		mCurr = StrutsMCurrDelegate.getMCurr(mCurrName); // ͨ��������ȥ�õ�һ�����ֶ���

		if (mCurr == null) // �����жϵõ��ı��ֶ����Ƿ�Ϊ�� ���Ϊ�������һ������ҵĶ���

			mCurr = StrutsMCurrDelegate.getMCurr("�����");

		if (mCurr == null) { // �������Ϊ�����׳��쳣

			errorMessage = "";

			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName() + "�ļ���ı��������ݿ��в�����";

			throw new Exception(errorMessage);
		}

		String dataRange_String = this.getElementValue(root, ConfigOncb.DATARANGEID);

		Integer dataRange = null;

		try {
			int dataRange_int = Integer.parseInt(dataRange_String); // �õ����ݷ�ΧID
			dataRange = new Integer(dataRange_int);
			mrt = StrutsMDataRgTypeDelegate.getMDataRgTypeOncb(new Integer(dataRange_int)); // �õ�һ�����ݷ�Χ����
		} catch (Exception e) {
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName() + "�ļ�������ݷ�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		if (mrt == null) { // ���ݷ�ΧΪ�����׳��쳣
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName() + "�ļ�������ݷ�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		mChildReport = StrutsMChildReportDelegate.getMChileReport(childRepId, versionId);

		if (mChildReport == null) { // ����ӱ����������׳��쳣
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName() + "�ļ����ӱ��������ݿ��в�����";
			throw new Exception(errorMessage);
		}

		mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(orgId, childRepId, versionId); // �õ�һ��������Χ���ö���

		if (mrr == null) { // ������������Χ���÷�Χ���������׳��쳣
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName() + "�ļ��ﱨ��������÷�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		try {
			year = new Integer(Integer.parseInt(this.getElementValue(root, ConfigOncb.YEAR)));
		} catch (Exception e) {
			year = null;
		}

		Integer term = null;

		try {
			// term = new
			// Integer(Integer.parseInt(this.getElementValue(root,ConfigOncb.TERM)));
			term = new Integer(Integer.parseInt(this.getElementValue(root, ConfigOncb.MONTH)));
		} catch (Exception e) {
			term = null;
		}

		try {
			times = new Integer(Integer.parseInt(times_String));
		} catch (Exception e) {
			times = null;
		}

		try {
			repOutId = new Integer(Integer.parseInt(repOutIdString)); // �����õ��������ݼ�¼ID
		} catch (Exception e) {
			repOutId = null; // ���û������Ϊ��
		}

		Element fileXmlRoot = XmlUtils.getRootElement(fileXml); // XML�����ļ��ĸ�Ԫ��

		String fileXmlRootName = fileXmlRoot.getName().toUpperCase();

		Element fileXmlRoot_1 = null; // ����һЩ����ʹ��

		Iterator fileXmlRoot_iterator = null;

		Element e = null;

		String fitechSubmitYear = null; // ������ϱ����
		String fitechSubmitMonth = null; // ������ϱ��·�

		// ������ݸ�Ԫ���Ƿ���F���ж��ǵ�Ե�ʽ�����嵥ʽ
		if (fileXmlRootName.equals(ConfigOncb.UPPERELEMENT)) {
			for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator.hasNext();) {

				// fileXmlRoot_iterator = fileXmlRoot.elementIterator(); //
				// ����ǵ�Ե�ʽ������￪ʼ

				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();

				if (fileXmlRoot_1.getName().toUpperCase().equals(ConfigOncb.SECONDUPPER)) { // �ж��Ƿ���Ԫ��P1
					repName = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHTITLE);
					// String dateString = this.getElementValue(fileXmlRoot_1,
					// ConfigOncb.FITECHDATE);
					fitechSubmitYear = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITYEAR);
					fitechSubmitMonth = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITMONTH);
					/*
					 * try { date=DateUtil.getDateByString(dateString); } catch
					 * (Exception ee) { date = null; }
					 */
				}
			}

		} else { // ������嵥ʽ������￪ʼ
			fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // ������嵥ʽ������￪ʼ
			fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
			for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
				e = (Element) fileXmlRoot_iterator_1.next();
				if (this.isDetailHeader(e.getName())) { // �ҵ�detailHeaderԪ��
					repName = this.getElementValue(e, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����
					// String dateString =
					// this.getElementValue(e,ConfigOncb.FITECHDATE);
					fitechSubmitYear = this.getElementValue(e, ConfigOncb.FITECHSUBMITYEAR); // �����ϱ������
					fitechSubmitMonth = this.getElementValue(e, ConfigOncb.FITECHSUBMITMONTH);
					/*
					 * try { date=DateUtil.getDateByString(dateString); } catch
					 * (Exception eee) { date = null; }
					 */
					break;
				}
			}
			if (fileXmlRootName.equals(ConfigOncb.G5301)) { // �����G5301ʱ
				repName = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����
				// String dateString =
				// this.getElementValue(fileXmlRoot,ConfigOncb.FITECHDATE);
				fitechSubmitYear = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITYEAR);
				fitechSubmitMonth = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITMONTH);
				/*
				 * try { date =
				 * DateUtil.getDateByString(dateString,DateUtil.NORMALDATE); //
				 * �õ��ϱ����� } catch (Exception eee) { date = null; }
				 */
			}
		}

		/*
		 * if (StrutsReportInDelegate.isRepeat(childRepId, versionId, orgId,
		 * mCurrName, dataRange, year, term, times))
		 * 
		 * throw new Exception("error_1");
		 */

		/**
		 * ����Ϊ����ʱ�������������Ҫ
		 */
		/*
		 * SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		 * if(date==null) // System.out.println("date is null"); else
		 * // System.out.println("date:" +date.toLocaleString()); String
		 * _date=dateFormat.format(date);
		 * year=Integer.valueOf(_date.substring(0,4));
		 * term=Integer.valueOf(_date.substring(5,7));
		 */
		/*
		 * if(fitechSubmitMonth!=null){ try{
		 * term=Integer.valueOf(fitechSubmitMonth); }catch(NumberFormatException
		 * ne){ ne.printStackTrace(); term=null; } } if(fitechSubmitYear!=null){
		 * try{ year=Integer.valueOf(fitechSubmitYear);
		 * }catch(NumberFormatException ne){ ne.printStackTrace(); year=null; } }
		 */
		/** **************************************************** */

		ReportIn reportIn = StrutsReportInDelegate.insertReportIn(mChildReport, mCurr, mrt, mrr, year, term, writer, checker, principal, times, repName, date,
				zipFileName, fileXml.getName(), repOutId); // ������ʵ�ʱ��������ݿ�

		return reportIn;
	}

	/**
	 * �ж��Ƿ���detailHeaderԪ��
	 * 
	 * @param detailHeader
	 * @return
	 */
	public boolean isDetailHeader(String detailHeader) {

		boolean is = true;

		int length = detailHeader.length();

		if (length < ConfigOncb.DETAILHEADER.length())

			return false;

		String sub = detailHeader.substring(0, 12);

		if (!sub.equals(ConfigOncb.DETAILHEADER))

			is = false;

		return is;
	}

	/**
	 * �ж��Ƿ�
	 */
	public boolean isTotal(String total) {

		boolean is = true;

		int length = total.length();

		if (length < ConfigOncb.TOTAL.length())

			return false;

		if (total.indexOf(ConfigOncb.TOTAL) <= -1)

			is = false;

		return is;
	}

	/**
	 * ����Ԫ��������ݿ�
	 * 
	 * @param reportIn
	 * @throws Exception
	 */
	public void saveCell(ReportIn reportIn) throws Exception {

	}

	/**
	 * �÷������ڷ��صڶ����ZIP�����ļ���
	 * 
	 * @param file
	 *            File ����ZIP�����ļ�����XML�ļ�
	 * @return String
	 * @throws Exception
	 */
	public String getZipName(File file) throws Exception {

		String zipName = "";

		Element root = XmlUtils.getRootElement(file);

		for (Iterator i = root.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (e.getName().equals("fileName")) {

				zipName = e.getStringValue();

				zipName = ConductString.getStringNotSpace(zipName);

				break;
			}
		}

		return zipName;
	}

	/**
	 * ��������Ľ��Ԫ���ҳ��ý��Ԫ�ص���Ԫ�ض�Ӧ��ֵ ���û�и�Ԫ�ػ��Ԫ�ص�ֵΪ���ַ�����������ַ���
	 * 
	 * @param element
	 *            ����Ľڵ�
	 * @param elementName
	 *            �������Ԫ����
	 * @return �ַ���
	 */
	public String getElementValue(Element element, String elementName) {

		String elementValue = "";

		Element e = null;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			e = (Element) i.next();

			if (e.getName().equals(elementName)) {

				elementValue = e.getStringValue();

				elementValue = ConductString.getStringNotSpace(elementValue); // ȥǰ��ո�

				break;

			}
		}
		return elementValue;
	}

	/**
	 * ȥ���ַ����е���Ч�ַ� ����������ַ�����ȥ��������� ����ǲ��������ַ��������κδ���
	 * 
	 * @param oldString
	 * @return
	 */
	public String dataPoor(String oldString) {

		String newString = "";

		float f = 0;

		try {
			return oldString.trim();
			// f = Float.parseFloat(oldString); // ת��float ����ɹ���˵���������ַ���

			// newString = String.valueOf(f); // ������ɹ���˵������ͨ�ַ���

		} catch (Exception e) {

			newString = oldString;
		}

		return newString;
	}

	/**
	 * ��������һ��ZIP�ļ���XML�ļ�����
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public int getXmlCount(File file) throws Exception {

		Element root = XmlUtils.getRootElement(file);

		Element element = null;

		int size = 0;

		for (Iterator i = root.elementIterator(); i.hasNext();) {

			element = (Element) i.next();

			if (element.getName().equals(ConfigOncb.REPORT))

				size++;
		}

		return size;
	}

	/**
	 * �ж��Ƿ��ǵ�Ե�
	 * 
	 * @param file
	 * @return boolean
	 */
	public boolean isPop(File file) {

		Element root = XmlUtils.getRootElement(file);

		String name = root.getName(); // �õ���Ԫ�ص�����

		name = ConductString.getStringNotSpace(name); // ȥǰ��ո�

		if (!name.equals(ConfigOncb.UPPERELEMENT))

			return false;

		return true;
	}

	/**
	 * ����������ַ��������к�
	 * 
	 * @param s
	 *            �ַ���
	 * @return �ַ������͵��к�
	 */
	public String getCol(String s) {

		String col = "";

		try {

			col = s.substring(0, 1);

		} catch (Exception e) {

			col = "";
		}

		return col;
	}

	/**
	 * ���ݵ�Ԫ���ƻ�ȡ��Ԫ������
	 * 
	 * @param cellName
	 *            String ��Ԫ����
	 * @param rowId
	 *            String ��Ԫ����к�
	 * @return String ��ȡʧ�ܣ�����""
	 */
	public String getCol(String cellName, String rowId) {
		if (cellName == null || rowId == null)
			return "";

		String col = "";

		try {
			col = cellName.endsWith(rowId) ? cellName.replaceAll(rowId, "") : "";
		} catch (Exception e) {
			col = "";
		}

		return col;
	}

	public static void main(String[] args) {
		// System.out.println(new InputData().getCol("A70", "70"));
	}

	/**
	 * ����������ַ��������к�
	 * 
	 * @param s
	 * @return
	 */
	public String getRow(String s) {

		String row = "";

		int m = 1;

		try {
			// row = s.substring(m); //����д����û���˵�Ԫ������Ϊ�����ĸ��ϵ��������:AA112��

			Integer _row = new FitechPDF().getRowNo(s);
			row = _row != null ? _row.toString() : "";
		} catch (Exception e) {

			row = "";
		}

		return row;
	}

	/**
	 * �ж�һ����Ԫ��Ԫ�����Ƿ�����Ч��
	 * 
	 * @param elementName
	 *            String Ԫ��ֵ
	 * @return true ����Ч�� false����Ч��
	 */
	public boolean isMCellElementName(String elementName) {

		boolean is = true;

		String s = this.getRow(elementName);

		try {

			int i = Integer.parseInt(s);

		} catch (Exception e) {

			is = false;
		}

		return is;
	}

	public void deleteAllFile() {

		File file = new File(ConfigOncb.TEMP_DIR);

		if (file.exists()) {

			File[] files = file.listFiles();

			int size = files.length;

			for (int i = 0; i < size; i++)

				files[i].delete();
		}
	}

	/**
	 * �÷������ڷ���һ��XML�ļ���PԪ��
	 * 
	 * @param file
	 *            XML�ļ���
	 * @return Element
	 */
	public Element getXml_P(File file) {

		Element root = XmlUtils.getRootElement(file);

		Element e = null;

		for (Iterator i = root.elementIterator(); i.hasNext();) {

			e = (Element) i.next();

			if (e.getText().equals("P"))

				break;
		}

		return e;
	}

	/**
	 * �÷������ڸ���listing.xml������嵥ʽ�б�ı���
	 * 
	 * @param e
	 *            listing.xml�ļ���reportԪ��
	 * @return �� ��
	 */
	public String getTableName(Element e) throws Exception {

		String ta = "";

		String reportId = this.getElementValue(e, ConfigOncb.REPORTID);

		reportId = ConductString.getStringNotSpace(reportId);

		String version = this.getElementValue(e, ConfigOncb.VERSION);

		version = ConductString.getStringNotSpace(version);

		ta = StrutsListingTableDelegate.getTalbeName(reportId, version);

		ta = ConductString.getStringNotSpace(ta);

		return ta;
	}

	public boolean conductXML(File xmlFile, ReportInForm reportInForm,String userName) throws Exception {
		boolean bool = false;
		ReportIn reportIn = null;
		String errorMessage = "";

		if (xmlFile == null)
			return bool;
		reportIn = this.createReportIn(xmlFile, reportInForm); // ����洢ʵ�ʱ���

		if (reportIn == null) { // ����洢����ʱʧ��������ȥ
			return bool;
		}

		StrutsReportInDataDelegate.insertReortInData(xmlFile, reportIn.getRepInId()); // ��XML�ļ����

		if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

			Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

			for (Iterator ii = popRoot.elementIterator(); ii.hasNext();) {
				Element ee = (Element) ii.next();
				if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

					for (Iterator iii = ee.elementIterator(); iii.hasNext();) { // �������XML�����ļ�����ѭ������
						Element eee = (Element) iii.next();
						String elementName = eee.getName();

						if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��
							String row_String = this.getRow(elementName);
							Integer rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�
							String colId = this.getCol(elementName, row_String); // �õ���Ԫ���к�
							String reportValue = eee.getStringValue(); // �õ�����ֵ
							reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
							String childRepId = reportInForm.getChildRepId();
							String versionId = reportInForm.getVersionId();

							MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
							if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
								continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
							}
							StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, xmlFile.getName(), xmlFile.getName());
						}
					}

				}
			}

		} else { // ���ｫ���嵥ʽ��������Ӧ����
			if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��
				this.conductG5301_2(xmlFile, reportIn, reportInForm);
				return true;
			}
			String tableName = this.getTableName(reportInForm.getChildRepId(), reportInForm.getVersionId());
			if (tableName.equals("")) {
				errorMessage = "XML�����ļ���ʽ����ȷ";

				FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
				return false;
			} else { // ������ת����InputDataByListing���������嵥ʽ�ı���
				try {
					InputDataByListing dbl = new InputDataByListing();
					dbl.controler(xmlFile, tableName, reportIn.getRepInId(), reportIn.getMChildReport().getComp_id().getChildRepId(), "", "");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		xmlFile.delete();
		bnValidate(reportIn.getRepInId(), userName);
		return true;
	}

	public boolean conductXML(File xmlFile, ReportInForm reportInForm, String year, String term,String userName) throws Exception {
		boolean bool = false;
		ReportIn reportIn = null;
		String errorMessage = "";

		if (xmlFile == null)
			return bool;
		reportIn = this.createReportIn(xmlFile, reportInForm, year, term); // ����洢ʵ�ʱ���

		if (reportIn == null) { // ����洢����ʱʧ��������ȥ
			return bool;
		}

		StrutsReportInDataDelegate.insertReortInData(xmlFile, reportIn.getRepInId()); // ��XML�ļ����

		if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

			Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

			for (Iterator ii = popRoot.elementIterator(); ii.hasNext();) {
				Element ee = (Element) ii.next();
				if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

					for (Iterator iii = ee.elementIterator(); iii.hasNext();) { // �������XML�����ļ�����ѭ������
						Element eee = (Element) iii.next();
						String elementName = eee.getName();

						if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��
							String row_String = this.getRow(elementName);
							Integer rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�
							String colId = this.getCol(elementName, row_String); // �õ���Ԫ���к�
							String reportValue = eee.getStringValue(); // �õ�����ֵ
							reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
							String childRepId = reportInForm.getChildRepId();
							String versionId = reportInForm.getVersionId();

							MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
							if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
								continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
							}
							StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, xmlFile.getName(), xmlFile.getName());
						}
					}

				}
			}

		} else { // ���ｫ���嵥ʽ��������Ӧ����
			if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��
				this.conductG5301_2(xmlFile, reportIn, reportInForm);
				return true;
			}
			String tableName = this.getTableName(reportInForm.getChildRepId(), reportInForm.getVersionId());
			if (tableName.equals("")) {
				errorMessage = "XML�����ļ���ʽ����ȷ";

				FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
				return false;
			} else { // ������ת����InputDataByListing���������嵥ʽ�ı���
				try {
					InputDataByListing dbl = new InputDataByListing();
					dbl.controler(xmlFile, tableName, reportIn.getRepInId(), reportIn.getMChildReport().getComp_id().getChildRepId(), "", "");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		// xmlFile.delete();
		bnValidate(reportIn.getRepInId(),userName);
		return true;
	}

	public boolean conductXML2(File xmlFile, ReportInForm reportInForm) throws Exception {
		boolean bool = false;
		ReportIn reportIn = null;
		String errorMessage = "";

		if (xmlFile == null)
			return bool;
		reportIn = this.createReportIn(xmlFile, reportInForm); // ����洢ʵ�ʱ���

		StrutsReportInDelegate.updateReportInCheckFlag(reportIn.getRepInId());

		if (reportIn == null) { // ����洢����ʱʧ��������ȥ
			return bool;
		}

		StrutsReportInDataDelegate.insertReortInData(xmlFile, reportIn.getRepInId()); // ��XML�ļ����

		if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

			Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

			for (Iterator ii = popRoot.elementIterator(); ii.hasNext();) {
				Element ee = (Element) ii.next();
				if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

					for (Iterator iii = ee.elementIterator(); iii.hasNext();) { // �������XML�����ļ�����ѭ������
						Element eee = (Element) iii.next();
						String elementName = eee.getName();

						if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��
							String row_String = this.getRow(elementName);
							Integer rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�
							String colId = this.getCol(elementName, row_String); // �õ���Ԫ���к�
							String reportValue = eee.getStringValue(); // �õ�����ֵ
							reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
							String childRepId = reportInForm.getChildRepId();
							String versionId = reportInForm.getVersionId();

							MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
							if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
								continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
							}
							StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, xmlFile.getName(), xmlFile.getName());
						}
					}

				}
			}

		} else { // ���ｫ���嵥ʽ��������Ӧ����
			if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��
				this.conductG5301_2(xmlFile, reportIn, reportInForm);
				return true;
			}
			String tableName = this.getTableName(reportInForm.getChildRepId(), reportInForm.getVersionId());
			if (tableName.equals("")) {
				errorMessage = "XML�����ļ���ʽ����ȷ";

				FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
				return false;
			} else { // ������ת����InputDataByListing���������嵥ʽ�ı���
				try {
					InputDataByListing dbl = new InputDataByListing();
					dbl.controler(xmlFile, tableName, reportIn.getRepInId(), reportIn.getMChildReport().getComp_id().getChildRepId(), "", "");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		// xmlFile.delete();
		// bnValidate(reportIn.getRepInId());
		return true;
	}

	public boolean conductXML3(File xmlFile, ReportInForm reportInForm) throws Exception {
		boolean bool = false;
		ReportIn reportIn = null;
		String errorMessage = "";
		Integer handFlag = new Integer(1); // �Ƿ��ֹ���ƽ��
		if (xmlFile == null)
			return bool;
		reportIn = this.createReportIn(xmlFile, reportInForm); // ����洢ʵ�ʱ���
		StrutsReportInDelegate.updateReportInHandworkFlag(reportIn.getRepInId(), handFlag); // ���±�־(�Ƿ��ֹ���ƽ��)
		StrutsReportInDelegate.updateReportInCheckFlag(reportIn.getRepInId());

		if (reportIn == null) { // ����洢����ʱʧ��������ȥ
			return bool;
		}

		StrutsReportInDataDelegate.insertReortInData(xmlFile, reportIn.getRepInId()); // ��XML�ļ����

		if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

			Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

			for (Iterator ii = popRoot.elementIterator(); ii.hasNext();) {
				Element ee = (Element) ii.next();
				if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

					for (Iterator iii = ee.elementIterator(); iii.hasNext();) { // �������XML�����ļ�����ѭ������
						Element eee = (Element) iii.next();
						String elementName = eee.getName();

						if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��
							String row_String = this.getRow(elementName);
							Integer rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�
							String colId = this.getCol(elementName, row_String); // �õ���Ԫ���к�
							String reportValue = eee.getStringValue(); // �õ�����ֵ
							reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
							String childRepId = reportInForm.getChildRepId();
							String versionId = reportInForm.getVersionId();

							MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
							if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
								continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
							}
							StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, xmlFile.getName(), xmlFile.getName());
						}
					}

				}
			}

		} else { // ���ｫ���嵥ʽ��������Ӧ����
			if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��
				this.conductG5301_2(xmlFile, reportIn, reportInForm);
				return true;
			}
			String tableName = this.getTableName(reportInForm.getChildRepId(), reportInForm.getVersionId());
			if (tableName.equals("")) {
				errorMessage = "XML�����ļ���ʽ����ȷ";

				FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
				return false;
			} else { // ������ת����InputDataByListing���������嵥ʽ�ı���
				try {
					InputDataByListing dbl = new InputDataByListing();
					dbl.controler(xmlFile, tableName, reportIn.getRepInId(), reportIn.getMChildReport().getComp_id().getChildRepId(), "", "");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		// xmlFile.delete();
		// bnValidate(reportIn.getRepInId());
		return true;
	}

	public ReportIn createReportIn(File xmlFile, ReportInForm reportInForm) throws Exception {

		if (xmlFile == null)
			return null;
		String repName = null;
		String mCurrName = null;
		String writer = null;
		String checker = null;
		String principal = null;
		Date date = Calendar.getInstance().getTime();
		String errorMessage = "";

		MCurr mCurr = null;
		MDataRgType mrt = null;
		MRepRange mrr = null;
		MChildReport mChildReport = null;
		String dataRangeDes = null;

		Element fileXmlRoot = XmlUtils.getRootElement(xmlFile); // XML�����ļ��ĸ�Ԫ��
		String fileXmlRootName = fileXmlRoot.getName().toUpperCase();
		Element fileXmlRoot_1 = null; // ����һЩ����ʹ��
		Iterator fileXmlRoot_iterator = null;
		Element e = null;

		String fitechSubmitYear = null; // ������ϱ����
		String fitechSubmitMonth = null; // ������ϱ��·�

		// ������ݸ�Ԫ���Ƿ���F���ж��ǵ�Ե�ʽ�����嵥ʽ
		if (fileXmlRootName.equals(ConfigOncb.UPPERELEMENT)) {
			for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator.hasNext();) {

				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
				if (fileXmlRoot_1.getName().toUpperCase().equals(ConfigOncb.SECONDUPPER)) { // �ж��Ƿ���Ԫ��P1
					repName = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHTITLE);

					fitechSubmitYear = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITYEAR);
					fitechSubmitMonth = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITMONTH);
					mCurrName = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCURR);
					writer = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHFILLER);
					checker = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCHECKER);
					principal = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHPRINCIPAL);
					dataRangeDes = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHRANGE);
				}
			}
		} else { // ������嵥ʽ������￪ʼ
			fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // ������嵥ʽ������￪ʼ
			fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
			for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
				e = (Element) fileXmlRoot_iterator_1.next();
				if (this.isDetailHeader(e.getName())) { // �ҵ�detailHeaderԪ��
					repName = this.getElementValue(e, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����

					fitechSubmitYear = this.getElementValue(e, ConfigOncb.FITECHSUBMITYEAR); // �����ϱ������
					fitechSubmitMonth = this.getElementValue(e, ConfigOncb.FITECHSUBMITMONTH);
					dataRangeDes = this.getElementValue(e, ConfigOncb.FITECHRANGE);
					break;
				}
			}
			if (reportInForm.getChildRepId().equals("S3209")) {
				for (; fileXmlRoot_iterator.hasNext();) {
					Element subForm = (Element) fileXmlRoot_iterator.next();
					subForm.getName();
					if (writer != null || checker != null || principal != null)
						break;
					for (Iterator fileXmlRoot_iterator_1 = subForm.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
						e = (Element) fileXmlRoot_iterator_1.next();
						if (e.getName().equals("total3")) {
							if (writer == null && checker == null && principal == null) {
								writer = this.getElementValue(e, ConfigOncb.FITECHFILLER);
								checker = getElementValue(e, ConfigOncb.FITECHCHECKER);
								principal = getElementValue(e, ConfigOncb.FITECHPRINCIPAL);
							}
							break;
						}
					}
				}
			} else {
				for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
					e = (Element) fileXmlRoot_iterator_1.next();
					if (this.isTotal(e.getName())) {
						if (writer == null && checker == null && principal == null) {
							writer = this.getElementValue(e, ConfigOncb.FITECHFILLER);
							checker = getElementValue(e, ConfigOncb.FITECHCHECKER);
							principal = getElementValue(e, ConfigOncb.FITECHPRINCIPAL);
						}
						break;
					}
				}
			}
			if (fileXmlRootName.equals(ConfigOncb.G5301)) { // �����G5301ʱ
				repName = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����

				fitechSubmitYear = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITYEAR);
				fitechSubmitMonth = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITMONTH);

			}
		}

		mCurr = StrutsMCurrDelegate.getMCurr(mCurrName);
		if (mCurr == null) // �����жϵõ��ı��ֶ����Ƿ�Ϊ�� ���Ϊ�������һ������ҵĶ���
			mCurr = StrutsMCurrDelegate.getMCurr("�����");
		if (mCurr == null) { // �������Ϊ�����׳��쳣
			this.messageInfo = "������ʧ�ܣ����ֲ����ڣ�";
			errorMessage = "";
			errorMessage = "�ļ���ı��������ݿ��в�����";
			throw new Exception(errorMessage);
		}

		mrt =new  StrutsMDataRgTypeDelegate().selectOneByName(dataRangeDes);
		if (mrt == null) { // ���ݷ�ΧΪ�����׳��쳣
			this.messageInfo = "������ʧ�ܣ����Ϳھ��������";
			errorMessage = "";
			errorMessage = "�ļ�������ݷ�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		mChildReport = StrutsMChildReportDelegate.getMChileReport(reportInForm.getChildRepId(), reportInForm.getVersionId());
		if (mChildReport == null) { // ����ӱ����������׳��쳣
			this.messageInfo = "������ʧ�ܣ��������ڣ�";
			errorMessage = "";
			errorMessage = "�ļ����ӱ��������ݿ��в�����";
			throw new Exception(errorMessage);
		}

		if (mChildReport.getIsPublic() == new Integer(0)) {
			this.messageInfo = "������ʧ�ܣ��ñ���ģ�廹δ������";
			errorMessage = "";
			errorMessage = "������ʧ�ܣ��ñ���ģ�廹δ������";
			throw new Exception(errorMessage);
		}

		boolean bool = StrutsReportInDelegate.isExistDataRange(dataRangeDes, mChildReport.getComp_id().getChildRepId(), mChildReport.getComp_id().getVersionId());

		if (bool == false) {
			this.messageInfo = "������ʧ�ܣ�û�иñ���ı��Ϳھ���";
			errorMessage = "";
			errorMessage = "������ʧ�ܣ�û�иñ���ı��Ϳھ�";
			throw new Exception(errorMessage);
		}
		mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(reportInForm.getOrgId(), reportInForm.getChildRepId(), reportInForm.getVersionId()); // �õ�һ��������Χ���ö���
		if (mrr == null) { // ������������Χ���÷�Χ���������׳��쳣
			this.messageInfo = "������ʧ�ܣ�û�иñ���ı��ͷ�Χ��";
			errorMessage = "";
			errorMessage = "�ļ��ﱨ��������÷�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		fitechSubmitYear = fitechSubmitYear.replaceAll("��", "");
		fitechSubmitMonth = fitechSubmitMonth.replaceAll("��", "");

		bool = StrutsMActuRepDelegate.getfreq(mChildReport.getComp_id().getChildRepId(), mChildReport.getComp_id().getVersionId(), mrt.getDataRangeId(),
				fitechSubmitMonth.trim());

		if (bool == false) {
			this.messageInfo = "������ʧ�ܣ�û�иñ���ı���Ƶ�ȣ�";
			errorMessage = "";
			errorMessage = "������ʧ�ܣ�û�иñ���ı���Ƶ��";
			throw new Exception(errorMessage);
		}

		ReportIn reportIn = null;
		try {
			reportIn = StrutsReportInDelegate.insertReportIn(mChildReport, mCurr, mrt, mrr, new Integer(fitechSubmitYear.trim()), new Integer(fitechSubmitMonth.trim()),
					writer, checker, principal, new Integer(0), repName, date, "", "", null); // ������ʵ�ʱ��������ݿ�
		} catch (Exception ex) {
			ex.printStackTrace();
			reportIn = null;
		}

		return reportIn;
	}

	public ReportIn createReportIn(File xmlFile, ReportInForm reportInForm, String year, String term) throws Exception {

		if (xmlFile == null)
			return null;
		String repName = null;
		String mCurrName = null;
		String writer = null;
		String checker = null;
		String principal = null;
		Date date = Calendar.getInstance().getTime();
		String errorMessage = "";

		MCurr mCurr = null;
		MDataRgType mrt = null;
		MRepRange mrr = null;
		MChildReport mChildReport = null;
		String dataRangeDes = null;

		Element fileXmlRoot = XmlUtils.getRootElement(xmlFile); // XML�����ļ��ĸ�Ԫ��
		String fileXmlRootName = fileXmlRoot.getName().toUpperCase();
		Element fileXmlRoot_1 = null; // ����һЩ����ʹ��
		Iterator fileXmlRoot_iterator = null;
		Element e = null;

		String fitechSubmitYear = null; // ������ϱ����
		String fitechSubmitMonth = null; // ������ϱ��·�

		// ������ݸ�Ԫ���Ƿ���F���ж��ǵ�Ե�ʽ�����嵥ʽ
		if (fileXmlRootName.equals(ConfigOncb.UPPERELEMENT)) {
			for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator.hasNext();) {

				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
				if (fileXmlRoot_1.getName().toUpperCase().equals(ConfigOncb.SECONDUPPER)) { // �ж��Ƿ���Ԫ��P1
					repName = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHTITLE);

					fitechSubmitYear = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITYEAR);
					fitechSubmitMonth = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITMONTH);
					mCurrName = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCURR);
					writer = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHFILLER);
					checker = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCHECKER);
					principal = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHPRINCIPAL);
					dataRangeDes = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHRANGE);
				}
			}
		} else { // ������嵥ʽ������￪ʼ
			fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // ������嵥ʽ������￪ʼ
			fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
			for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
				e = (Element) fileXmlRoot_iterator_1.next();
				if (this.isDetailHeader(e.getName())) { // �ҵ�detailHeaderԪ��
					repName = this.getElementValue(e, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����

					fitechSubmitYear = this.getElementValue(e, ConfigOncb.FITECHSUBMITYEAR); // �����ϱ������
					fitechSubmitMonth = this.getElementValue(e, ConfigOncb.FITECHSUBMITMONTH);
					dataRangeDes = this.getElementValue(e, ConfigOncb.FITECHRANGE);
					break;
				}
			}
			if (reportInForm.getChildRepId().equals("S3209")) {
				for (; fileXmlRoot_iterator.hasNext();) {
					Element subForm = (Element) fileXmlRoot_iterator.next();
					subForm.getName();
					if (writer != null || checker != null || principal != null)
						break;
					for (Iterator fileXmlRoot_iterator_1 = subForm.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
						e = (Element) fileXmlRoot_iterator_1.next();
						if (e.getName().equals("total3")) {
							if (writer == null && checker == null && principal == null) {
								writer = this.getElementValue(e, ConfigOncb.FITECHFILLER);
								checker = getElementValue(e, ConfigOncb.FITECHCHECKER);
								principal = getElementValue(e, ConfigOncb.FITECHPRINCIPAL);
							}
							break;
						}
					}
				}
			} else {
				for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
					e = (Element) fileXmlRoot_iterator_1.next();
					if (this.isTotal(e.getName())) {
						if (writer == null && checker == null && principal == null) {
							writer = this.getElementValue(e, ConfigOncb.FITECHFILLER);
							checker = getElementValue(e, ConfigOncb.FITECHCHECKER);
							principal = getElementValue(e, ConfigOncb.FITECHPRINCIPAL);
						}
						break;
					}
				}
			}
			if (fileXmlRootName.equals(ConfigOncb.G5301)) { // �����G5301ʱ
				repName = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����

				fitechSubmitYear = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITYEAR);
				fitechSubmitMonth = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITMONTH);

			}
		}

		if (fitechSubmitYear == null || fitechSubmitMonth == null) {
			this.messageInfo = "������ʧ�ܣ������뱨��ʱ�䣡";
			errorMessage = "";
			errorMessage = "����ʱ��δ��";
			throw new Exception(errorMessage);
		}

		fitechSubmitYear = fitechSubmitYear.replaceAll("��", "");
		fitechSubmitMonth = fitechSubmitMonth.replaceAll("��", "");

		if (!fitechSubmitYear.equals(year) || !fitechSubmitMonth.equals(term)) {
			this.messageInfo = "������ʧ�ܣ������������ѯ�ı������ڲ�һ�£�";
			errorMessage = "";
			errorMessage = "������ʧ�ܣ������������ѯ�ı������ڲ�һ��";
			throw new Exception(errorMessage);
		}
	
		mCurr = StrutsMCurrDelegate.getMCurr(mCurrName);
		if (mCurr == null) // �����жϵõ��ı��ֶ����Ƿ�Ϊ�� ���Ϊ�������һ������ҵĶ���
			mCurr = StrutsMCurrDelegate.getMCurr("�����");
		if (mCurr == null) { // �������Ϊ�����׳��쳣
			this.messageInfo = "������ʧ�ܣ����ֲ����ڣ�";
			errorMessage = "";
			errorMessage = "�ļ���ı��������ݿ��в�����";
			throw new Exception(errorMessage);
		}
		
			mrt =new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeDes);
			if (mrt == null) { // ���ݷ�ΧΪ�����׳��쳣
				this.messageInfo = "������ʧ�ܣ����Ϳھ��������";
				errorMessage = "";
				errorMessage = "�ļ�������ݷ�Χ�����ݿ��в�����";
				throw new Exception(errorMessage);
			}
		

		mChildReport = StrutsMChildReportDelegate.getMChileReport(reportInForm.getChildRepId(), reportInForm.getVersionId());
		if (mChildReport == null) { // ����ӱ����������׳��쳣
			this.messageInfo = "������ʧ�ܣ��������ڣ�";
			errorMessage = "";
			errorMessage = "�ļ����ӱ��������ݿ��в�����";
			throw new Exception(errorMessage);
		}

		if (mChildReport.getIsPublic() == new Integer(0)) {
			this.messageInfo = "������ʧ�ܣ��ñ���ģ�廹δ������";
			errorMessage = "";
			errorMessage = "������ʧ�ܣ��ñ���ģ�廹δ������";
			throw new Exception(errorMessage);
		}

		boolean bool = StrutsReportInDelegate.isExistDataRange(dataRangeDes, mChildReport.getComp_id().getChildRepId(), mChildReport.getComp_id().getVersionId());
		if (bool == false) {
			this.messageInfo = "������ʧ�ܣ�û�иñ���ı��Ϳھ���";
			errorMessage = "";
			errorMessage = "������ʧ�ܣ�û�иñ���ı��Ϳھ�";
			throw new Exception(errorMessage);
		}

		mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(reportInForm.getOrgId(), reportInForm.getChildRepId(), reportInForm.getVersionId()); // �õ�һ��������Χ���ö���
		if (mrr == null) { // ������������Χ���÷�Χ���������׳��쳣
			this.messageInfo = "������ʧ�ܣ�û�иñ���ı��ͷ�Χ��";
			errorMessage = "";
			errorMessage = "�ļ��ﱨ��������÷�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		bool = StrutsMActuRepDelegate.getfreq(mChildReport.getComp_id().getChildRepId(), mChildReport.getComp_id().getVersionId(), mrt.getDataRangeId(),
				fitechSubmitMonth.trim());

		if (bool == false) {
			this.messageInfo = "������ʧ�ܣ�û�иñ���ı���Ƶ�ȣ�";
			errorMessage = "";
			errorMessage = "������ʧ�ܣ�û�иñ���ı���Ƶ��";
			throw new Exception(errorMessage);
		}

		ReportIn reportIn = null;
		try {
			reportIn = StrutsReportInDelegate.insertReportIn(mChildReport, mCurr, mrt, mrr, new Integer(fitechSubmitYear.trim()), new Integer(fitechSubmitMonth.trim()),
					writer, checker, principal, new Integer(0), repName, date, "", "", null); // ������ʵ�ʱ��������ݿ�
		} catch (Exception ex) {
			ex.printStackTrace();
			reportIn = null;
		}

		return reportIn;
	}

	public File getXMLFile(String childRepId, String versionId, String orgId) {
		
//		/** �������� (��֧1 ����0) */
//		Integer template_type = 0;
//		/** ȡ�ñ������� */
//		MChildReportForm tempForm = StrutsMChildReportDelegate.getMChildReport(childRepId, versionId);
//		String reportType = tempForm.getTemplateType();
//		
//		if(reportType != null && !reportType.equals("") && reportType.trim().equals("excel")){
//			template_type = 1;
//		}
		
		
	
		String fileName = com.fitech.net.config.Config.getCollectExcelFolder() + File.separator + orgId + File.separator + childRepId + "_" + versionId
				+ ".xls";
		// ����excelת��xml�ļ�Ŀ¼
		StringBuffer string = new StringBuffer(Config.WEBROOTPATH + com.fitech.net.config.Config.REPORT_NAME + File.separator
				+ com.fitech.net.config.Config.SERVICE_UP_TEMP);

		File upFile = new File(string.toString());
		if (!upFile.exists())
			upFile.mkdir();

		string.append(File.separator).append(com.fitech.net.config.Config.SERVICE_UP_XML);
		File xmlFileDir = new File(string.toString());
		if (!xmlFileDir.exists())
			xmlFileDir.mkdir();

		string.append(orgId);

		File xmlFileFolder = new File(string.toString());
		if (!xmlFileFolder.exists())
			xmlFileFolder.mkdir();

		Calendar calendar = Calendar.getInstance();
		StringBuffer buffer = new StringBuffer();
		buffer.append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH) + 1).append(calendar.get(Calendar.DATE))
				.append(calendar.get(Calendar.HOUR_OF_DAY)).append(calendar.get(Calendar.MINUTE)).append(calendar.get(Calendar.SECOND)).append(
						calendar.get(Calendar.MILLISECOND));

		// ��ȡxml�ļ�
		Excel2XML.excel2xml(fileName, orgId, versionId, buffer.toString() );
		File xmlFiles[] = xmlFileFolder.listFiles();
		File xmlFile = null;
		if (xmlFiles.length > 0) {
			for (int i = 0; i < xmlFiles.length; i++) {
				String fName = xmlFiles[i].getName();
				if (fName.equals(buffer.toString() + ".xml")) {
					xmlFile = xmlFiles[i];
					break;
				}
			}
		}
		com.fitech.net.config.Config.FILENAME = null;
		return xmlFile;
	}

	public void conductG5301_2(File xmlFile, ReportIn reportIn, ReportInForm reportInForm) throws Exception {

		Element root = XmlUtils.getRootElement(xmlFile);

		Element element = null;

		String elementName = "";

		for (Iterator i = root.elementIterator(); i.hasNext();) {
			element = (Element) i.next();
			elementName = element.getName();

			String reportValue = element.getStringValue(); // �õ�����ֵ
			reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
			String childRepId = reportInForm.getChildRepId();
			String versionId = reportInForm.getVersionId();

			MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, elementName);

			if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
				continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
			}

			StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, "", "");
		}

	}

	public String getTableName(String childRepId, String versionId) throws Exception {

		String ta = "";

		ta = StrutsListingTableDelegate.getTalbeName(childRepId, versionId);

		ta = ConductString.getStringNotSpace(ta);

		return ta;
	}

	/**
	 * <b>ִ�б���У��</b> ������������������󣬶Ըñ���ִ�б���У�顣<br>
	 * ����У��ͨ���ı�����������״̬������У��δͨ���ı�����Ϊ�ر�״̬��ͬ����ͬ<br>
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return void
	 */
	public void bnValidate(Integer repInId,String userName) {
		/** ��ȡ����У����Ϣ֮ǰ,ִ�б���У��洢����* */
		boolean bool = Procedure.runBNJY(repInId,userName);
		ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(repInId);

		if (reportInForm != null) {
			int tblIVF = reportInForm.getTblInnerValidateFlag() != null ? reportInForm.getTblInnerValidateFlag().intValue() : 0;
			if (tblIVF == Config.TBL_INNER_VALIDATE_NO_FLAG.intValue() || bool == false) {
				StrutsReportInDelegate.updateReportIn(reportInForm.getRepInId());
			}
		}
	}

	/**
	 * �˴�ΪУ����õı����ͷ������ڲ����ٵ���У�飬�ⲿ����У�鷽��
	 * 
	 * @param xmlFile
	 * @param reportInForm
	 * @return
	 * @throws Exception
	 */
	public boolean conductJYXML(File xmlFile, ReportInForm reportInForm) throws Exception {
		boolean bool = false;
		ReportIn reportIn = null;
		String errorMessage = "";

		if (xmlFile == null)
			return bool;
		reportIn = this.createReportIn(xmlFile, reportInForm); // ����洢ʵ�ʱ���

		if (reportIn == null) { // ����洢����ʱʧ��������ȥ
			return bool;
		}

		StrutsReportInDataDelegate.insertReortInData(xmlFile, reportIn.getRepInId()); // ��XML�ļ����

		if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

			Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

			for (Iterator ii = popRoot.elementIterator(); ii.hasNext();) {
				Element ee = (Element) ii.next();
				if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

					for (Iterator iii = ee.elementIterator(); iii.hasNext();) { // �������XML�����ļ�����ѭ������
						Element eee = (Element) iii.next();
						String elementName = eee.getName();

						if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��
							String row_String = this.getRow(elementName);
							Integer rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�
							String colId = this.getCol(elementName, row_String); // �õ���Ԫ���к�
							String reportValue = eee.getStringValue(); // �õ�����ֵ
							reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
							String childRepId = reportInForm.getChildRepId();
							String versionId = reportInForm.getVersionId();

							MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
							if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
								continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
							}
							StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, xmlFile.getName(), xmlFile.getName());
						}
					}

				}
			}

		} else { // ���ｫ���嵥ʽ��������Ӧ����
			if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��
				this.conductG5301_2(xmlFile, reportIn, reportInForm);
				return true;
			}
			String tableName = this.getTableName(reportInForm.getChildRepId(), reportInForm.getVersionId());
			if (tableName.equals("")) {
				errorMessage = "XML�����ļ���ʽ����ȷ";

				FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
				return false;
			} else { // ������ת����InputDataByListing���������嵥ʽ�ı���
				try {
					InputDataByListing dbl = new InputDataByListing();
					dbl.controler(xmlFile, tableName, reportIn.getRepInId(), reportIn.getMChildReport().getComp_id().getChildRepId(), "", "");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		// xmlFile.delete();
		reportInForm.setRepInId(reportIn.getRepInId());
		reportInForm.setRepName(reportIn.getRepName());
		reportInForm.setReportDate(reportIn.getReportDate());
		return true;
	}

	public boolean conductOnLineJYXML(File xmlFile, ReportInForm reportInForm, String year, String term,String userName) throws Exception {
		boolean bool = false;
		ReportIn reportIn = null;
		String errorMessage = "";

		if (xmlFile == null)
			return bool;
		reportIn = this.createJYReportIn(xmlFile, reportInForm, year, term); // ����洢ʵ�ʱ���

		if (reportIn == null) { // ����洢����ʱʧ��������ȥ
			return bool;
		}

		StrutsReportInDataDelegate.insertReortInData(xmlFile, reportIn.getRepInId()); // ��XML�ļ����

		if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

			Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

			for (Iterator ii = popRoot.elementIterator(); ii.hasNext();) {
				Element ee = (Element) ii.next();
				if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

					for (Iterator iii = ee.elementIterator(); iii.hasNext();) { // �������XML�����ļ�����ѭ������
						Element eee = (Element) iii.next();
						String elementName = eee.getName();

						if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��
							String row_String = this.getRow(elementName);
							Integer rowId = new Integer(Integer.parseInt(row_String)); // �õ���Ԫ���к�
							String colId = this.getCol(elementName, row_String); // �õ���Ԫ���к�
							String reportValue = eee.getStringValue(); // �õ�����ֵ
							reportValue = this.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�
							String childRepId = reportInForm.getChildRepId();
							String versionId = reportInForm.getVersionId();

							MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
							if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
								continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
							}
							StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(), reportIn.getRepInId(), reportValue, xmlFile.getName(), xmlFile.getName());
						}
					}

				}
			}

		} else { // ���ｫ���嵥ʽ��������Ӧ����
			if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��
				this.conductG5301_2(xmlFile, reportIn, reportInForm);
				return true;
			}
			String tableName = this.getTableName(reportInForm.getChildRepId(), reportInForm.getVersionId());
			if (tableName.equals("")) {
				errorMessage = "XML�����ļ���ʽ����ȷ";

				FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA, ConfigOncb.HANDLER, errorMessage, "OVER");
				return false;
			} else { // ������ת����InputDataByListing���������嵥ʽ�ı���
				try {
					InputDataByListing dbl = new InputDataByListing();
					dbl.controler(xmlFile, tableName, reportIn.getRepInId(), reportIn.getMChildReport().getComp_id().getChildRepId(), "", "");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		// xmlFile.delete();
		reportInForm.setRepInId(reportIn.getRepInId());
		reportInForm.setRepName(reportIn.getRepName());
		reportInForm.setReportDate(reportIn.getReportDate());
		// System.out.println("***У�鿪ʼ****"+new Date());
		Procedure.runBNJY(reportIn.getRepInId(),userName);
		// System.out.println("***У�����****"+new Date());
		// Procedure.runKPDJY(reportIn.getRepInId());
		// Procedure.runBJJY(reportIn.getRepInId());

		StrutsReportInDelegate.updateReportInCheckFlag(reportInForm.getRepInId(), com.fitech.net.config.Config.CHECK_FLAG_AFTERJY);
		return true;
	}

	public ReportIn createJYReportIn(File xmlFile, ReportInForm reportInForm, String year, String term) throws Exception {

		if (xmlFile == null)
			return null;
		String repName = null;
		String mCurrName = null;
		String writer = null;
		String checker = null;
		String principal = null;
		Date date = Calendar.getInstance().getTime();
		String errorMessage = "";

		MCurr mCurr = null;
		MDataRgType mrt = null;
		MRepRange mrr = null;
		MChildReport mChildReport = null;
		String dataRangeDes = null;

		Element fileXmlRoot = XmlUtils.getRootElement(xmlFile); // XML�����ļ��ĸ�Ԫ��
		String fileXmlRootName = fileXmlRoot.getName().toUpperCase();
		Element fileXmlRoot_1 = null; // ����һЩ����ʹ��
		Iterator fileXmlRoot_iterator = null;
		Element e = null;

		String fitechSubmitYear = null; // ������ϱ����
		String fitechSubmitMonth = null; // ������ϱ��·�

		// ������ݸ�Ԫ���Ƿ���F���ж��ǵ�Ե�ʽ�����嵥ʽ
		if (fileXmlRootName.equals(ConfigOncb.UPPERELEMENT)) {
			for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator.hasNext();) {

				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
				if (fileXmlRoot_1.getName().toUpperCase().equals(ConfigOncb.SECONDUPPER)) { // �ж��Ƿ���Ԫ��P1
					repName = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHTITLE);

					fitechSubmitYear = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITYEAR);
					fitechSubmitMonth = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHSUBMITMONTH);
					mCurrName = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCURR);
					writer = this.getElementValue(fileXmlRoot_1, ConfigOncb.FITECHFILLER);
					checker = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHCHECKER);
					principal = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHPRINCIPAL);
					dataRangeDes = getElementValue(fileXmlRoot_1, ConfigOncb.FITECHRANGE);
				}
			}
		} else { // ������嵥ʽ������￪ʼ
			fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // ������嵥ʽ������￪ʼ
			fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
			for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
				e = (Element) fileXmlRoot_iterator_1.next();
				if (this.isDetailHeader(e.getName())) { // �ҵ�detailHeaderԪ��
					repName = this.getElementValue(e, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����

					fitechSubmitYear = this.getElementValue(e, ConfigOncb.FITECHSUBMITYEAR); // �����ϱ������
					fitechSubmitMonth = this.getElementValue(e, ConfigOncb.FITECHSUBMITMONTH);
					dataRangeDes = this.getElementValue(e, ConfigOncb.FITECHRANGE);
					break;
				}
			}
			if (reportInForm.getChildRepId().equals("S3209")) {
				for (; fileXmlRoot_iterator.hasNext();) {
					Element subForm = (Element) fileXmlRoot_iterator.next();
					subForm.getName();
					if (writer != null || checker != null || principal != null)
						break;
					for (Iterator fileXmlRoot_iterator_1 = subForm.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
						e = (Element) fileXmlRoot_iterator_1.next();
						if (e.getName().equals("total3")) {
							if (writer == null && checker == null && principal == null) {
								writer = this.getElementValue(e, ConfigOncb.FITECHFILLER);
								checker = getElementValue(e, ConfigOncb.FITECHCHECKER);
								principal = getElementValue(e, ConfigOncb.FITECHPRINCIPAL);
							}
							break;
						}
					}
				}
			} else {
				for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
					e = (Element) fileXmlRoot_iterator_1.next();
					if (this.isTotal(e.getName())) {
						if (writer == null && checker == null && principal == null) {
							writer = this.getElementValue(e, ConfigOncb.FITECHFILLER);
							checker = getElementValue(e, ConfigOncb.FITECHCHECKER);
							principal = getElementValue(e, ConfigOncb.FITECHPRINCIPAL);
						}
						break;
					}
				}
			}
			if (fileXmlRootName.equals(ConfigOncb.G5301)) { // �����G5301ʱ
				repName = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����

				fitechSubmitYear = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITYEAR);
				fitechSubmitMonth = this.getElementValue(fileXmlRoot, ConfigOncb.FITECHSUBMITMONTH);

			}
		}

		if (fitechSubmitYear == null || fitechSubmitMonth == null) {
			this.messageInfo = "�������ݲ���ȷ�������뱨��ʱ�䣡";
			errorMessage = "";
			errorMessage = "����ʱ��δ��";
			throw new Exception(errorMessage);
		}
	
		fitechSubmitYear = fitechSubmitYear.replaceAll("��", "");
		fitechSubmitMonth = fitechSubmitMonth.replaceAll("��", "");

		if (!fitechSubmitYear.equals(year) || !fitechSubmitMonth.equals(term)) {
			this.messageInfo = "�������ݲ���ȷ�������������ѯ�ı������ڲ�һ�£�";
			errorMessage = "";
			errorMessage = "�������ݲ���ȷ�������������ѯ�ı������ڲ�һ��";
			throw new Exception(errorMessage);
		}

		mCurr = StrutsMCurrDelegate.getMCurr(mCurrName);
		if (mCurr == null) // �����жϵõ��ı��ֶ����Ƿ�Ϊ�� ���Ϊ�������һ������ҵĶ���
			mCurr = StrutsMCurrDelegate.getMCurr("�����");
		if (mCurr == null) { // �������Ϊ�����׳��쳣
			this.messageInfo = "�������ݲ���ȷ�����ֲ����ڣ�";
			errorMessage = "";
			errorMessage = "�ļ���ı��������ݿ��в�����";
			throw new Exception(errorMessage);
		}
		/** �ж�ģ������ */
	
			mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeDes);
			if (mrt == null) { // ���ݷ�ΧΪ�����׳��쳣
				this.messageInfo = "�������ݲ���ȷ�����Ϳھ��������";
				errorMessage = "";
				errorMessage = "�ļ�������ݷ�Χ�����ݿ��в�����";
				throw new Exception(errorMessage);
			}
		

		mChildReport = StrutsMChildReportDelegate.getMChileReport(reportInForm.getChildRepId(), reportInForm.getVersionId());
		if (mChildReport == null) { // ����ӱ����������׳��쳣
			this.messageInfo = "�������ݲ���ȷ���������ڣ�";
			errorMessage = "";
			errorMessage = "�ļ����ӱ��������ݿ��в�����";
			throw new Exception(errorMessage);
		}

		if (mChildReport.getIsPublic() == new Integer(0)) {
			this.messageInfo = "�������ݲ���ȷ���ñ���ģ�廹δ������";
			errorMessage = "";
			errorMessage = "�������ݲ���ȷ���ñ���ģ�廹δ������";
			throw new Exception(errorMessage);
		}
		boolean bool  ;
		/** �ж�ģ������ */
		
			 bool = StrutsReportInDelegate.isExistDataRange(dataRangeDes, mChildReport.getComp_id().getChildRepId(), mChildReport.getComp_id().getVersionId());
			if (bool == false) {
				this.messageInfo = "�������ݲ���ȷ��û�иñ���ı��Ϳھ���";
				errorMessage = "";
				errorMessage = "�������ݲ���ȷ��û�иñ���ı��Ϳھ�";
				throw new Exception(errorMessage);
			}
		

		mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(reportInForm.getOrgId(), reportInForm.getChildRepId(), reportInForm.getVersionId()); // �õ�һ��������Χ���ö���
		if (mrr == null) { // ������������Χ���÷�Χ���������׳��쳣
			this.messageInfo = "�������ݲ���ȷ��û�иñ���ı��ͷ�Χ��";
			errorMessage = "";
			errorMessage = "�ļ��ﱨ��������÷�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		bool = StrutsMActuRepDelegate.getfreq(mChildReport.getComp_id().getChildRepId(), mChildReport.getComp_id().getVersionId(), mrt.getDataRangeId(),
				fitechSubmitMonth.trim());

		if (bool == false) {
			this.messageInfo = "�������ݲ���ȷ��û�иñ���ı���Ƶ�ȣ�";
			errorMessage = "";
			errorMessage = "�������ݲ���ȷ��û�иñ���ı���Ƶ��";
			throw new Exception(errorMessage);
		}

		ReportIn reportIn = null;
		try {
			reportIn = StrutsReportInDelegate.insertReportIn(mChildReport, mCurr, mrt, mrr, new Integer(fitechSubmitYear.trim()), new Integer(fitechSubmitMonth.trim()),
					writer, checker, principal, new Integer(0), repName, date, "", "", null); // ������ʵ�ʱ��������ݿ�
		} catch (Exception ex) {
			ex.printStackTrace();
			reportIn = null;
		}

		return reportIn;
	}
}