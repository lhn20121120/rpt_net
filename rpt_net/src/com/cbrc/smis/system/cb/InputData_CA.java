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
 * 该类能够对报表XML数据文件进行处理, 如果是点对点式的将直接进行处理 如果是清单式的将调用会调用InputDataByListing类来统一处理
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InputData_CA {

	/**
	 * 处理单个ZIP文件
	 * 
	 * @param file
	 *            ZIP包文件对象
	 * @throws Exception
	 */
	public void conductZip(File file) throws Exception {

		ReportIn reportIn = null;

		ZipUtils zipUtils = new ZipUtils();

		String errorMessage = "";

		File dir = new File(ConfigOncb.TEMP_DIR); // dir是解压后文件存放的地址(该地址是一个临时地址,用完就会删除所有的文件)

		File[] files = null;

		File listing = null;

		try {

			files = zipUtils.expandFile(file, dir); // 在这里对ZIP文件进行解压缩

		} catch (Exception e) {

			errorMessage = "";

			errorMessage = "解压" + file.getName() + "包文件时失败";

			throw new Exception(errorMessage);
		}

		listing = new File(ConfigOncb.TEMP_DIR + Config.FILESEPARATOR
				+ ConfigOncb.EXPLAINFILENAME); // 定义一个说明文件对象

		if (!(listing.exists())) { // 判断是否存在说明XML(listing.xml)文件 不存在则抛出异常

			errorMessage = "";

			errorMessage = "解压" + file.getName() + "包文件时"
					+ "发现不存在listing.xml文件";

			throw new Exception(errorMessage);

		}

		String dataFileName = this.getZipName(listing); // 通过读取listing.xml文件里的fileName元素得到数据文件名

		String zipFileName = ConfigOncb.TEMP_DIR + Config.FILESEPARATOR
				+ dataFileName; // 得到ZIP文件名

		File zipFile = new File(zipFileName);

		if (!zipFile.exists()) {

			errorMessage = "";

			errorMessage = file.getName() + "里的" + zipFile.getName()
					+ "数据文件不存在";

			throw new Exception(errorMessage);
		}

		try {

			zipUtils.expandFile(zipFile, dir); // 在这里对ZIP文件里面的ZIP文件进行解压缩

			 System.out.println("----------------COUNT="
					+ dir.listFiles().length + "个文件------------------");

			/*
			 * File[] files2 = dir.listFiles();
			 * 
			 * for(int count=files2.length;count)
			 */

		} catch (Exception e) {

			errorMessage = "";

			errorMessage = "解压" + file.getName() + "里面的" + dataFileName
					+ "数据文件时发生错误";

			throw new Exception(errorMessage);
		}

		Element listingRoot = XmlUtils.getRootElement(listing); // 说明XML文件的根元素

		File xmlFile = null;

		for (Iterator i = listingRoot.elementIterator(); i.hasNext();) { // 从这里开始对ZIP包中的XML数据文件进行循环处理

			List errorList = new ArrayList();

			Element listingE = (Element) i.next();

			if (listingE.getName().equals(ConfigOncb.REPORT)) { // 判断元素名是否是report

				String xmlName = this.createXmlName(listingE);

				xmlName = ConductString.getStringNotSpace(xmlName); // 去前后空格

				xmlFile = new File(ConfigOncb.TEMP_DIR + Config.FILESEPARATOR
						+ xmlName); // 生成XML数据文件名
				/** **********************CA部分****************** */
				System.out
						.println("-------------------begin openAndCheckData----------------------");

				String bankCerNamePath = ConfigOncb.TEMP_DIR
						+ Config.FILESEPARATOR + xmlName + "InnerSign.cer";

				File bankCer = new File(bankCerNamePath); // 生成行长证书文件对象

				String operatorCerNamePath = ConfigOncb.TEMP_DIR
						+ Config.FILESEPARATOR + xmlName + "OuterSign.cer";

				File operatorCer = new File(operatorCerNamePath); // 生成操作员证书文件对象

				String xmlFileNameByDark = ConfigOncb.TEMP_DIR
						+ Config.FILESEPARATOR + xmlName + "Enc.txt";

				File xmlFileByDark = new File(xmlFileNameByDark); // 生成签名加密后的文件对象

				if (!bankCer.exists()) {

					errorList.add("行长签名");

				}

				if (!operatorCer.exists()) {

					errorList.add("操作员签名");

				}

				if (!xmlFileByDark.exists()) {

					errorList.add("密文");

				}

				if (bankCer.exists() && operatorCer.exists()
						&& xmlFileByDark.exists()) { // 判断需要的这个三个文件是否都存在

					try {

						// 下面这一句进行解密验签,目前这里是先对操作员验签，再对行长验签
						xmlFile = OpenAndCheckData.getXmlFileByBright(xmlName,
								xmlFileByDark, operatorCer, bankCer); // 在这里进行解密验签得到数据文件对象

					} catch (Exception e) {

						errorMessage = e.getMessage();

						FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA,
								ConfigOncb.HANDLER, errorMessage, "OVER");

						e.printStackTrace();

						// System.out.println("打印CA错误信息" + errorMessage);

						continue;
					}

					System.out
							.println("--------------------------------------------end openAndCheckData----------------------");
					// 修改的终点
					/** ******************************************** */

					/*
					 * if (xmlFile.exists()) { // 判断拼出来的文件名所指的文件是否存在
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
							.getName()); // 这里存储实际表单表

					if (reportIn == null) { // 如果存储报表时失败则跳过去
						continue;
					}

					StrutsReportInDataDelegate.insertReortInData(xmlFile,
							reportIn.getRepInId()); // 将XML文件入库

					if (this.isPop(xmlFile)) { // 判断是否是点对点式的报表

						Element popRoot = XmlUtils.getRootElement(xmlFile); // 点对点式报表XML文件的根

						for (Iterator ii = popRoot.elementIterator(); ii
								.hasNext();) {

							Element ee = (Element) ii.next();

							if (ee.getName().toUpperCase().equals("P1")) { // 判断是否到了P1元素

								for (Iterator iii = ee.elementIterator(); iii
										.hasNext();) { // 从这里对XML数据文件进行循环处理

									Element eee = (Element) iii.next();

									String elementName = eee.getName();

									if (this.isMCellElementName(elementName)) { // 判断是否是单元格元素

										String row_String = this
												.getRow(elementName);

										Integer rowId = new Integer(Integer
												.parseInt(row_String)); // 得到单元格列号

										String colId = this.getCol(elementName,
												row_String); // 得到单元格行号

										String reportValue = eee
												.getStringValue(); // 得到报表值

										reportValue = this
												.dataPoor(reportValue); // 去处报表值中的多余字串

										String childRepId = this
												.getElementValue(listingE,
														ConfigOncb.REPORTID);

										String versionId = this
												.getElementValue(listingE,
														ConfigOncb.VERSION);

										MCell mCell = StrutsMCellDelegate
												.getMCell(childRepId,
														versionId, rowId, colId); // 从数据库中得到一个单元格对象

										if (mCell == null) { // 如果相应的单元格不存在则直接跳到下一个单元格

											continue; // 如果单元格对象为空,则返回for处继续处理下一个单元格
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

					} else { // 这里将对清单式报表作相应处理

						if (this.isG5312(xmlFile)) { // 如果是G5301将在这里作特殊处理

							this
									.conductG5301(xmlFile, listingE, file,
											reportIn);

							continue;
						}

						String tableName = this.getTableName(listingE);

						if (tableName.equals("")) {

							errorMessage = "";

							errorMessage = file.getName() + "包文件中的"
									+ xmlFile.getName() + "中的XML数据文件格式不正确";

							FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA,
									ConfigOncb.HANDLER, errorMessage, "OVER");

							continue;

						}

						else { // 从这里转向用InputDataByListing类来处理清单式的报表
							InputDataByListing dbl = new InputDataByListing();

							dbl.controler(xmlFile, tableName, reportIn
									.getRepInId(), reportIn.getMChildReport()
									.getComp_id().getChildRepId(), file
									.getName(), xmlFile.getName());

						}

					}

					xmlFile.delete();

				} else {

					errorMessage = "在解密验签时发现" + file.getName() + "包里属于"
							+ xmlName + ".xml数据文件中的";

					for (int m = 0; m < errorList.size(); m++) {

						errorMessage = errorMessage + errorList.get(m);

					}

					errorMessage = errorMessage + "这" + errorList.size()
							+ "处不存在";

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
		/* this.deleteAllFile(); */// 删除所有的临时文件
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
			 * if (!this.isMCellElementName(elementName)) // 判断是否是正确的单元格元素
			 * continue; String row_String = this.getRow(elementName); Integer
			 * rowId = new Integer(Integer.parseInt(row_String)); // 得到单元格列号
			 * String colId = this.getCol(elementName); // 得到单元格行号
			 */

			String reportValue = element.getStringValue(); // 得到报表值
			reportValue = this.dataPoor(reportValue); // 去除报表值中的多余字串
			String childRepId = this.getElementValue(listingE,
					ConfigOncb.REPORTID);
			String versionId = this.getElementValue(listingE,
					ConfigOncb.VERSION);

			// MCell mCell = StrutsMCellDelegate.getMCell(childRepId,
			// versionId,rowId, colId); // 从数据库中得到一个单元格对象
			MCell mCell = StrutsMCellDelegate.getMCell(childRepId, versionId,
					elementName);
			// // System.out.println(childRepId + "\t" + versionId + "\t===" +
			// elementName + " is not found!");
			if (mCell == null) { // 如果相应的单元格不存在则直接跳到下一个单元格
				// // System.out.println(childRepId + "\t" + versionId + "\t" +
				// elementName + "is not found!");
				/*
				 * errorMessage = "";
				 * 
				 * errorMessage = file.getName() + "包文件里的";
				 * 
				 * errorMessage = errorMessage + xmlFile.getName();
				 * 
				 * errorMessage = errorMessage + "中的" + elementName + "单元格不存在";
				 * 
				 * FitechLog.writeLog(Config.LOG_SYSTEM_SAVEDATA,
				 * ConfigOncb.HANDLER, errorMessage, "OVER");
				 * 
				 * e.printStackTrace();
				 */

				continue; // 如果单元格对象为空,则返回for处继续处理下一个单元格

			}

			StrutsReportInInfoDelegate.insertReportInInfo(mCell.getCellId(),
					reportIn.getRepInId(), reportValue, file.getName(), xmlFile
							.getName());

		}

	}

	/**
	 * 根据输入的结点拼出一个XML文件名
	 * 
	 * @param e
	 * @return String XML文件名
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
	 * 该方法用于根据listing中的report节点和 XML数据文件创建出一个ReportIn(实际表单对象) 并存入库表
	 * 
	 * @param element
	 *            listing中的report节点
	 * @param fileXml
	 *            XML数据文件对象
	 * @param zipFile
	 *            注意这里是ZIP包文件的名字,传它是出于错误日志查询的考虑
	 * @return 实际表单对象
	 * @throws Exception
	 */
	public ReportIn createReportIn(Element element, File fileXml,
			String zipFileName) throws Exception {

		Element root = element;

		String childRepId = this.getElementValue(root, ConfigOncb.REPORTID); // 从listingXml拿子报表ID

		String versionId = this.getElementValue(root, ConfigOncb.VERSION); // 从listingXml版本ID

		String orgId = this.getElementValue(root, ConfigOncb.ORGID); // 从listingXml机构ID

		String mCurrName = this.getElementValue(root, ConfigOncb.FITECHCURR); // 从listingXml币种ID

		String writer = this.getElementValue(root, ConfigOncb.WRITER); // 从listingXml得到作者

		String checker = getElementValue(root, ConfigOncb.CHECKER);

		String principal = getElementValue(root, ConfigOncb.PRINCIPAL);

		String times_String = this.getElementValue(root, ConfigOncb.TIMES); // 从listingXml得到次数

		String repOutIdString = this.getElementValue(root, ConfigOncb.REPOUTID); // 从listingXml中得到外网数据记录ID

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

		mCurr = StrutsMCurrDelegate.getMCurr(mCurrName); // 通过币种名去得到一个币种对象

		if (mCurr == null) // 这里判断得到的币种对象是否为空 如果为空则加载一个人民币的对象

			mCurr = StrutsMCurrDelegate.getMCurr("人民币");

		if (mCurr == null) { // 币种如果为空则抛出异常

			errorMessage = "";

			errorMessage = "发现" + zipFileName + "包文件里的" + fileXml.getName()
					+ "文件里的币种在数据库中不存在";

			throw new Exception(errorMessage);
		}

		String dataRange_String = this.getElementValue(root,
				ConfigOncb.DATARANGEID);

		Integer dataRange = null;

		try {
			int dataRange_int = Integer.parseInt(dataRange_String); // 得到数据范围ID
			dataRange = new Integer(dataRange_int);
			mrt = StrutsMDataRgTypeDelegate.getMDataRgTypeOncb(new Integer(
					dataRange_int)); // 得到一个数据范围对象
		} catch (Exception e) {
			errorMessage = "";
			errorMessage = "发现" + zipFileName + "包文件里的" + fileXml.getName()
					+ "文件里的数据范围在数据库中不存在";
			throw new Exception(errorMessage);
		}

		if (mrt == null) { // 数据范围为空则抛出异常
			errorMessage = "";
			errorMessage = "发现" + zipFileName + "包文件里的" + fileXml.getName()
					+ "文件里的数据范围在数据库中不存在";
			throw new Exception(errorMessage);
		}

		mChildReport = StrutsMChildReportDelegate.getMChileReport(childRepId,
				versionId);

		if (mChildReport == null) { // 如果子报表不存在则抛出异常
			errorMessage = "";
			errorMessage = "发现" + zipFileName + "包文件里的" + fileXml.getName()
					+ "文件里子报表在数据库中不存在";
			throw new Exception(errorMessage);
		}

		mrr = StrutsMRepRangeDelegate.getMRepRanageOncb(orgId, childRepId,
				versionId); // 得到一个机构范围适用对象

		if (mrr == null) { // 如果报表机构范围适用范围不存在则抛出异常
			errorMessage = "";
			errorMessage = "发现" + zipFileName + "包文件里的" + fileXml.getName()
					+ "文件里报表机构适用范围在数据库中不存在";
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
			repOutId = new Integer(Integer.parseInt(repOutIdString)); // 最终拿到外网数据记录ID
		} catch (Exception e) {
			repOutId = null; // 如果没有则至为空
		}

		Element fileXmlRoot = XmlUtils.getRootElement(fileXml); // XML数据文件的根元素

		String fileXmlRootName = fileXmlRoot.getName().toUpperCase();

		Element fileXmlRoot_1 = null; // 申明一些对象供使用

		Iterator fileXmlRoot_iterator = null;

		Element e = null;

		String fitechSubmitYear = null; // 报表的上报年份
		String fitechSubmitMonth = null; // 报表的上报月份

		// 下面根据根元素是否是F来判断是点对点式还是清单式
		if (fileXmlRootName.equals(ConfigOncb.UPPERELEMENT)) {
			for (fileXmlRoot_iterator = fileXmlRoot.elementIterator(); fileXmlRoot_iterator
					.hasNext();) {

				// fileXmlRoot_iterator = fileXmlRoot.elementIterator(); //
				// 如果是点对点式则从这里开始

				fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();

				if (fileXmlRoot_1.getName().toUpperCase().equals(
						ConfigOncb.SECONDUPPER)) { // 判断是否是元素P1
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

		} else { // 如果是清单式则从这里开始
			fileXmlRoot_iterator = fileXmlRoot.elementIterator(); // 如果是清单式则从这里开始
			fileXmlRoot_1 = (Element) fileXmlRoot_iterator.next();
			for (Iterator fileXmlRoot_iterator_1 = fileXmlRoot_1
					.elementIterator(); fileXmlRoot_iterator_1.hasNext();) {
				e = (Element) fileXmlRoot_iterator_1.next();
				if (this.isDetailHeader(e.getName())) { // 找到detailHeader元素
					repName = this.getElementValue(e, ConfigOncb.FITECHTITLE); // 得到实际报表名
					// String dateString =
					// this.getElementValue(e,ConfigOncb.FITECHDATE);
					fitechSubmitYear = this.getElementValue(e,
							ConfigOncb.FITECHSUBMITYEAR); // 报表上报的年份
					fitechSubmitMonth = this.getElementValue(e,
							ConfigOncb.FITECHSUBMITMONTH);
					/*
					 * try { date=DateUtil.getDateByString(dateString); } catch
					 * (Exception eee) { date = null; }
					 */
					break;
				}
			}
			if (fileXmlRootName.equals(ConfigOncb.G5301)) { // 如果是G5301时
				repName = this.getElementValue(fileXmlRoot,
						ConfigOncb.FITECHTITLE); // 得到实际报表名
				// String dateString =
				// this.getElementValue(fileXmlRoot,ConfigOncb.FITECHDATE);
				fitechSubmitYear = this.getElementValue(fileXmlRoot,
						ConfigOncb.FITECHSUBMITYEAR);
				fitechSubmitMonth = this.getElementValue(fileXmlRoot,
						ConfigOncb.FITECHSUBMITMONTH);
				/*
				 * try { date =
				 * DateUtil.getDateByString(dateString,DateUtil.NORMALDATE); //
				 * 得到上报日期 } catch (Exception eee) { date = null; }
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
		 * 此外为了临时解决导入数据需要
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
				repName, date, zipFileName, fileXml.getName(), repOutId); // 将内网实际表单存入数据库

		return reportIn;
	}

	/**
	 * 判断是否是detailHeader元素
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
	 * 将单元格存入数据库
	 * 
	 * @param reportIn
	 * @throws Exception
	 */
	public void saveCell(ReportIn reportIn) throws Exception {

	}

	/**
	 * 该方法用于返回第二层的ZIP数据文件名
	 * 
	 * @param file
	 *            File 放置ZIP数据文件名的XML文件
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
	 * 根据输入的结点元素找出该结点元素的子元素对应的值 如果没有该元素或该元素的值为空字符串则输出空字符串
	 * 
	 * @param element
	 *            输入的节点
	 * @param elementName
	 *            输入的子元素名
	 * @return 字符串
	 */
	public String getElementValue(Element element, String elementName) {

		String elementValue = "";

		Element e = null;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			e = (Element) i.next();

			if (e.getName().equals(elementName)) {

				elementValue = e.getStringValue();

				elementValue = ConductString.getStringNotSpace(elementValue); // 去前后空格

				break;

			}
		}
		return elementValue;
	}

	/**
	 * 去处字符串中的无效字符 如果是数字字符串则去处多余的零 如果是不是数字字符串则不作任何处理
	 * 
	 * @param oldString
	 * @return
	 */
	public String dataPoor(String oldString) {

		String newString = "";

		float f = 0;

		try {
			return oldString.trim();
			// f = Float.parseFloat(oldString); // 转成float 如果成功则说明是数字字符串

			// newString = String.valueOf(f); // 如果不成功则说明是普通字符串

		} catch (Exception e) {

			newString = oldString;
		}

		return newString;
	}

	/**
	 * 返回里面一层ZIP文件的XML文件数量
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
	 * 判断是否是点对点
	 * 
	 * @param file
	 * @return boolean
	 */
	public boolean isPop(File file) {

		Element root = XmlUtils.getRootElement(file);

		String name = root.getName(); // 得到根元素的名子

		name = ConductString.getStringNotSpace(name); // 去前后空格

		if (!name.equals(ConfigOncb.UPPERELEMENT))

			return false;

		return true;
	}

	/**
	 * 根据输入的字符串返回列号
	 * 
	 * @param s
	 *            字符串
	 * @return 字符串类型的行号
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
	 * 根据单元名称获取单元的列名
	 * 
	 * @param cellName
	 *            String 单元名称
	 * @param rowId
	 *            String 单元格的行号
	 * @return String 获取失败，返回""
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
	 * 根据输入的字符串返回行号
	 * 
	 * @param s
	 * @return
	 */
	public String getRow(String s) {

		String row = "";

		int m = 1;

		try {
			// row = s.substring(m); //这种写法，没老滤单元格名称为多个字母结合的情况，如:AA112等

			Integer _row = new FitechPDF().getRowNo(s);
			row = _row != null ? _row.toString() : "";
		} catch (Exception e) {

			row = "";
		}

		return row;
	}

	/**
	 * 判断一个单元格元素名是否是有效的
	 * 
	 * @param elementName
	 *            String 元素值
	 * @return true 是有效的 false是无效的
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
	 * 该方法用于返回一个XML文件的P元素
	 * 
	 * @param file
	 *            XML文件名
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
	 * 该方法用于根据listing.xml计算出清单式列表的表名
	 * 
	 * @param e
	 *            listing.xml文件的report元素
	 * @return 表 名
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