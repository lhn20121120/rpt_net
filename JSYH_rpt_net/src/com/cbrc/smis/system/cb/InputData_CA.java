/*
 * Created on 2005-12-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.system.cb;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
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
import com.cbrc.smis.common.DateUtil;
import com.cbrc.smis.common.XmlUtils;
import com.cbrc.smis.common.ZipUtils;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechPDF;

/**
 * @author cb
 * 
 * �����ܹ��Ա���XML�����ļ����д���, ����ǵ�Ե�ʽ�Ľ�ֱ�ӽ��д��� ������嵥ʽ�Ľ����û����InputDataByListing����ͳһ����
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InputData_CA {

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

		listing = new File(ConfigOncb.TEMP_DIR + Config.FILESEPARATOR
				+ ConfigOncb.EXPLAINFILENAME); // ����һ��˵���ļ�����

		if (!(listing.exists())) { // �ж��Ƿ����˵��XML(listing.xml)�ļ� ���������׳��쳣

			errorMessage = "";

			errorMessage = "��ѹ" + file.getName() + "���ļ�ʱ"
					+ "���ֲ�����listing.xml�ļ�";

			throw new Exception(errorMessage);

		}

		String dataFileName = this.getZipName(listing); // ͨ����ȡlisting.xml�ļ����fileNameԪ�صõ������ļ���

		String zipFileName = ConfigOncb.TEMP_DIR + Config.FILESEPARATOR
				+ dataFileName; // �õ�ZIP�ļ���

		File zipFile = new File(zipFileName);

		if (!zipFile.exists()) {

			errorMessage = "";

			errorMessage = file.getName() + "���" + zipFile.getName()
					+ "�����ļ�������";

			throw new Exception(errorMessage);
		}

		try {

			zipUtils.expandFile(zipFile, dir); // �������ZIP�ļ������ZIP�ļ����н�ѹ��

			 System.out.println("----------------COUNT="
					+ dir.listFiles().length + "���ļ�------------------");

			/*
			 * File[] files2 = dir.listFiles();
			 * 
			 * for(int count=files2.length;count)
			 */

		} catch (Exception e) {

			errorMessage = "";

			errorMessage = "��ѹ" + file.getName() + "�����" + dataFileName
					+ "�����ļ�ʱ��������";

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

				xmlFile = new File(ConfigOncb.TEMP_DIR + Config.FILESEPARATOR
						+ xmlName); // ����XML�����ļ���
				/** **********************CA����****************** */
				System.out
						.println("-------------------begin openAndCheckData----------------------");

				String bankCerNamePath = ConfigOncb.TEMP_DIR
						+ Config.FILESEPARATOR + xmlName + "InnerSign.cer";

				File bankCer = new File(bankCerNamePath); // �����г�֤���ļ�����

				String operatorCerNamePath = ConfigOncb.TEMP_DIR
						+ Config.FILESEPARATOR + xmlName + "OuterSign.cer";

				File operatorCer = new File(operatorCerNamePath); // ���ɲ���Ա֤���ļ�����

				String xmlFileNameByDark = ConfigOncb.TEMP_DIR
						+ Config.FILESEPARATOR + xmlName + "Enc.txt";

				File xmlFileByDark = new File(xmlFileNameByDark); // ����ǩ�����ܺ���ļ�����

				if (!bankCer.exists()) {

					errorList.add("�г�ǩ��");

				}

				if (!operatorCer.exists()) {

					errorList.add("����Աǩ��");

				}

				if (!xmlFileByDark.exists()) {

					errorList.add("����");

				}

				if (bankCer.exists() && operatorCer.exists()
						&& xmlFileByDark.exists()) { // �ж���Ҫ����������ļ��Ƿ񶼴���

					try {

						// ������һ����н�����ǩ,Ŀǰ�������ȶԲ���Ա��ǩ���ٶ��г���ǩ
						xmlFile = OpenAndCheckData.getXmlFileByBright(xmlName,
								xmlFileByDark, operatorCer, bankCer); // ��������н�����ǩ�õ������ļ�����

					} catch (Exception e) {

						errorMessage = e.getMessage();

						FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA,
								ConfigOncb.HANDLER, errorMessage, "OVER");

						e.printStackTrace();

						// System.out.println("��ӡCA������Ϣ" + errorMessage);

						continue;
					}

					System.out
							.println("--------------------------------------------end openAndCheckData----------------------");
					// �޸ĵ��յ�
					/** ******************************************** */

					/*
					 * if (xmlFile.exists()) { // �ж�ƴ�������ļ�����ָ���ļ��Ƿ����
					 * // System.out.println(xmlFile);
					 */
					/** ********************************************** */

					/*
					 * File testXmlFile = new File("C:/test/" +
					 * xmlFile.getName());
					 * 
					 * InputStream input = new FileInputStream(xmlFile);
					 * 
					 * byte[] buffer = new byte[input.available()];
					 * 
					 * input.read(buffer);
					 * 
					 * input.close();
					 * 
					 * OutputStream output = new FileOutputStream(testXmlFile);
					 * 
					 * output.write(buffer);
					 * 
					 * output.flush();
					 * 
					 * output.close();
					 */

					/** ********************************************** */

					reportIn = this.createReportIn(listingE, xmlFile, file
							.getName()); // ����洢ʵ�ʱ���

					if (reportIn == null) { // ����洢����ʱʧ��������ȥ
						continue;
					}

					StrutsReportInDataDelegate.insertReortInData(xmlFile,
							reportIn.getRepInId()); // ��XML�ļ����

					if (this.isPop(xmlFile)) { // �ж��Ƿ��ǵ�Ե�ʽ�ı���

						Element popRoot = XmlUtils.getRootElement(xmlFile); // ��Ե�ʽ����XML�ļ��ĸ�

						for (Iterator ii = popRoot.elementIterator(); ii
								.hasNext();) {

							Element ee = (Element) ii.next();

							if (ee.getName().toUpperCase().equals("P1")) { // �ж��Ƿ���P1Ԫ��

								for (Iterator iii = ee.elementIterator(); iii
										.hasNext();) { // �������XML�����ļ�����ѭ������

									Element eee = (Element) iii.next();

									String elementName = eee.getName();

									if (this.isMCellElementName(elementName)) { // �ж��Ƿ��ǵ�Ԫ��Ԫ��

										String row_String = this
												.getRow(elementName);

										Integer rowId = new Integer(Integer
												.parseInt(row_String)); // �õ���Ԫ���к�

										String colId = this.getCol(elementName,
												row_String); // �õ���Ԫ���к�

										String reportValue = eee
												.getStringValue(); // �õ�����ֵ

										reportValue = this
												.dataPoor(reportValue); // ȥ������ֵ�еĶ����ִ�

										String childRepId = this
												.getElementValue(listingE,
														ConfigOncb.REPORTID);

										String versionId = this
												.getElementValue(listingE,
														ConfigOncb.VERSION);

										MCell mCell = StrutsMCellDelegate
												.getMCell(childRepId,
														versionId, rowId, colId); // �����ݿ��еõ�һ����Ԫ�����

										if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��

											continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��
										}

										StrutsReportInInfoDelegate
												.insertReportInInfo(mCell
														.getCellId(), reportIn
														.getRepInId(),
														reportValue, file
																.getName(),
														xmlFile.getName());
									}
								}

							}
						}

					} else { // ���ｫ���嵥ʽ��������Ӧ����

						if (this.isG5312(xmlFile)) { // �����G5301�������������⴦��

							this
									.conductG5301(xmlFile, listingE, file,
											reportIn);

							continue;
						}

						String tableName = this.getTableName(listingE);

						if (tableName.equals("")) {

							errorMessage = "";

							errorMessage = file.getName() + "���ļ��е�"
									+ xmlFile.getName() + "�е�XML�����ļ���ʽ����ȷ";

							FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA,
									ConfigOncb.HANDLER, errorMessage, "OVER");

							continue;

						}

						else { // ������ת����InputDataByListing���������嵥ʽ�ı���
							InputDataByListing dbl = new InputDataByListing();

							dbl.controler(xmlFile, tableName, reportIn
									.getRepInId(), reportIn.getMChildReport()
									.getComp_id().getChildRepId(), file
									.getName(), xmlFile.getName());

						}

					}

					xmlFile.delete();

				} else {

					errorMessage = "�ڽ�����ǩʱ����" + file.getName() + "��������"
							+ xmlName + ".xml�����ļ��е�";

					for (int m = 0; m < errorList.size(); m++) {

						errorMessage = errorMessage + errorList.get(m);

					}

					errorMessage = errorMessage + "��" + errorList.size()
							+ "��������";

					FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA,
							ConfigOncb.HANDLER, errorMessage, "OVER");
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

	public void conductG5301(File xmlFile, Element listingE, File file,
			ReportIn reportIn) throws Exception {

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
			String childRepId = this.getElementValue(listingE,
					ConfigOncb.REPORTID);
			String versionId = this.getElementValue(listingE,
					ConfigOncb.VERSION);

			// MCell mCell = StrutsMCellDelegate.getMCell(childRepId,
			// versionId,rowId, colId); // �����ݿ��еõ�һ����Ԫ�����
			MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId,
					elementName);
			// // System.out.println(childRepId + "\t" + versionId + "\t===" +
			// elementName + " is not found!");
			if (mCell == null) { // �����Ӧ�ĵ�Ԫ�񲻴�����ֱ��������һ����Ԫ��
				// // System.out.println(childRepId + "\t" + versionId + "\t" +
				// elementName + "is not found!");
				/*
				 * errorMessage = "";
				 * 
				 * errorMessage = file.getName() + "���ļ����";
				 * 
				 * errorMessage = errorMessage + xmlFile.getName();
				 * 
				 * errorMessage = errorMessage + "�е�" + elementName + "��Ԫ�񲻴���";
				 * 
				 * FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA,
				 * ConfigOncb.HANDLER, errorMessage, "OVER");
				 * 
				 * e.printStackTrace();
				 */

				continue; // �����Ԫ�����Ϊ��,�򷵻�for������������һ����Ԫ��

			}

			StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(),
					reportIn.getRepInId(), reportValue, file.getName(), xmlFile
							.getName());

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

		xmlName = xmlName
				+ (!this.getElementValue(e, ConfigOncb.FITECHCURR).equals(
						ConfigOncb.COMMONCURRNAME) ? this.getElementValue(e,
						ConfigOncb.FITECHCURR) : "");

		/* xmlName = xmlName + ".xml"; */

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
	public ReportIn createReportIn(Element element, File fileXml,
			String zipFileName) throws Exception {

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

		String submitDateString = this.getElementValue(root, Config.SUBMITDATE);

		Date date = null;

		date = DateUtil.getFormatDate(submitDateString, DateUtil.NORMALDATE);

		/*
		 * Date submitDate =
		 * DateUtil.getFormatDate(submitDateString,DateUtil.NORMALDATE);
		 */

		Integer repOutId = null;

		String repName = null;

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

			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName()
					+ "�ļ���ı��������ݿ��в�����";

			throw new Exception(errorMessage);
		}

		String dataRange_String = this.getElementValue(root,
				ConfigOncb.DATARANGEID);

		Integer dataRange = null;

		try {
			int dataRange_int = Integer.parseInt(dataRange_String); // �õ����ݷ�ΧID
			dataRange = new Integer(dataRange_int);
			mrt = StrutsMDataRgTypeDelegate.getMDataRgTypeOncb(new Integer(
					dataRange_int)); // �õ�һ�����ݷ�Χ����
		} catch (Exception e) {
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName()
					+ "�ļ�������ݷ�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		if (mrt == null) { // ���ݷ�ΧΪ�����׳��쳣
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName()
					+ "�ļ�������ݷ�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		mChildReport = StrutsMChildReportDelegate.getMChileReport(childRepId,
				versionId);

		if (mChildReport == null) { // ����ӱ����������׳��쳣
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName()
					+ "�ļ����ӱ��������ݿ��в�����";
			throw new Exception(errorMessage);
		}

		mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(orgId, childRepId,
				versionId); // �õ�һ��������Χ���ö���

		if (mrr == null) { // ������������Χ���÷�Χ���������׳��쳣
			errorMessage = "";
			errorMessage = "����" + zipFileName + "���ļ����" + fileXml.getName()
					+ "�ļ��ﱨ��������÷�Χ�����ݿ��в�����";
			throw new Exception(errorMessage);
		}

		try {
			year = new Integer(Integer.parseInt(this.getElementValue(root,
					ConfigOncb.YEAR)));
		} catch (Exception e) {
			year = null;
		}

		Integer term = null;

		try {
			// term = new
			// Integer(Integer.parseInt(this.getElementValue(root,ConfigOncb.TERM)));
			term = new Integer(Integer.parseInt(this.getElementValue(root,
					ConfigOncb.MONTH)));
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
			for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator
					.hasNext();) {

				// fileXmlRoot_iterator = fileXmlRoot.elementIterator(); //
				// ����ǵ�Ե�ʽ������￪ʼ

				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();

				if (fileXmlRoot_1.getName().toUpperCase().equals(
						ConfigOncb.SECONDUPPER)) { // �ж��Ƿ���Ԫ��P1
					repName = this.getElementValue(fileXmlRoot_1,
							ConfigOncb.FITECHTITLE);
					// String dateString = this.getElementValue(fileXmlRoot_1,
					// ConfigOncb.FITECHDATE);
					fitechSubmitYear = this.getElementValue(fileXmlRoot_1,
							ConfigOncb.FITECHSUBMITYEAR);
					fitechSubmitMonth = this.getElementValue(fileXmlRoot_1,
							ConfigOncb.FITECHSUBMITMONTH);
					/*
					 * try { date=DateUtil.getDateByString(dateString); } catch
					 * (Exception ee) { date = null; }
					 */
				}
			}

		} else { // ������嵥ʽ������￪ʼ
			fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // ������嵥ʽ������￪ʼ
			fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
			for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1
					.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
				e = (Element) fileXmlRoot_iterator_1.next();
				if (this.isDetailHeader(e.getName())) { // �ҵ�detailHeaderԪ��
					repName = this.getElementValue(e, ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����
					// String dateString =
					// this.getElementValue(e,ConfigOncb.FITECHDATE);
					fitechSubmitYear = this.getElementValue(e,
							ConfigOncb.FITECHSUBMITYEAR); // �����ϱ������
					fitechSubmitMonth = this.getElementValue(e,
							ConfigOncb.FITECHSUBMITMONTH);
					/*
					 * try { date=DateUtil.getDateByString(dateString); } catch
					 * (Exception eee) { date = null; }
					 */
					break;
				}
			}
			if (fileXmlRootName.equals(ConfigOncb.G5301)) { // �����G5301ʱ
				repName = this.getElementValue(fileXmlRoot,
						ConfigOncb.FITECHTITLE); // �õ�ʵ�ʱ�����
				// String dateString =
				// this.getElementValue(fileXmlRoot,ConfigOncb.FITECHDATE);
				fitechSubmitYear = this.getElementValue(fileXmlRoot,
						ConfigOncb.FITECHSUBMITYEAR);
				fitechSubmitMonth = this.getElementValue(fileXmlRoot,
						ConfigOncb.FITECHSUBMITMONTH);
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

		ReportIn reportIn = StrutsReportInDelegate.insertReportIn(mChildReport,
				mCurr, mrt, mrr, year, term, writer, checker, principal, times,
				repName, date, zipFileName, fileXml.getName(), repOutId); // ������ʵ�ʱ��������ݿ�

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
			col = cellName.endsWith(rowId) ? cellName.replaceAll(rowId, "")
					: "";
		} catch (Exception e) {
			col = "";
		}

		return col;
	}

	public static void main(String[] args) {
		// System.out.println(new InputData_CA().getCol("A70", "70"));
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

}