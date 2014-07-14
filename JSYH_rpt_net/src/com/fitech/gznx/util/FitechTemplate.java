package com.fitech.gznx.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import net.sf.hibernate.HibernateException;

import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsListingColsDelegate;
import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.other.Expression;
import com.cbrc.smis.util.FitechPDF;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.QdCellinfo;
import com.fitech.gznx.service.AFQDCellInfoDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.service.StrutsAFCellDelegate;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;

/**
 * 模板设定操作工具类
 * 
 * @author rds
 * @serialData 2005-12-9
 */
public class FitechTemplate {
	/**
	 * 错误信息
	 */
	private String error = null;

	/**
	 * 需要入库的表达式的列表
	 */
	private List mCellToFormuList = null;

	/**
	 * 表间校验公式
	 */
	private static final String FLAG_BJ = "BJ:";

	/**
	 * 表内校验公式
	 */
	private static final String FLAG_BL = "BN:";

	/**
	 * 跨频度校验公式
	 */
	private static final String FLAG_KP = "KP:";

	/**
	 * 计算类型公式
	 */
	private static final String FLAG_JS = "JS:";

	/**
	 * 前缀的长度
	 */
	private static final int FLAG_LEN = 3;

	/**
	 * 绝对公式和相对公式的分隔符
	 */
	private static final String SPLIT = "※";

	/**
	 * 读取关系表达式定义文件中的公式
	 * 
	 * @param in
	 *            InputStream 关系表达式定义文件流
	 * @return Collection 表达式列表
	 * @exception Exception
	 */
	public static Collection getExpessions(InputStream in) throws Exception {
		ArrayList resList = null;

		if (in == null)
			return null;

		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));

		if (reader != null) {
			resList = new ArrayList();
			String line = "";
			try{				
				while (!StringUtil.isEmpty(line = reader.readLine())) {
					Expression expression = new Expression();
					expression.setContent(getContent(line));
					expression.setType(getFlag(line));
	
					resList.add(expression);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return resList;
	}

	/**
	 * 读取关系表达式定义文件中的公式
	 * 
	 * @param in
	 *            InputStream 关系表达式定义文件流
	 * @return Collection 表达式列表
	 * @param reportStyle
	 *            Integer 报表类型
	 * @exception Exception
	 */
	public static Collection getExpessions(InputStream in, Integer reportStyle)
			throws Exception {
		ArrayList resList = null;

		if (in == null)
			return null;

		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));

		if (reader != null) {
			resList = new ArrayList();
			String line = "";
			try{
				
				while (!StringUtil.isEmpty(line = reader.readLine())) {
					Expression expression = new Expression();
					expression.setContent(getContent(line, reportStyle));
					expression.setType(getFlag(line));
	
					resList.add(expression);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return resList;
	}

	/**
	 * 获取表达式的内容
	 * 
	 * @param line
	 *            String 行内容
	 * @return String 表达式内容
	 */
	public static String getContent(String line) {
		if (line == null)
			return null;
		if (line.length() < FLAG_LEN)
			return null;

		if (!line.startsWith(FLAG_BJ) && !line.startsWith(FLAG_BL) 
				&& !line.startsWith(FLAG_KP)  && !line.startsWith(FLAG_JS))
			return line;

		return line.substring(FLAG_LEN);
	}

	/**
	 * 获取表达式的内容<br>
	 * 当报表类型为清单元式时，返回原值
	 * 
	 * @param line
	 *            String 行内容
	 * @param reportStyle
	 *            Integer 报表类型
	 * @return String 表达式内容
	 */
	public static String getContent(String line, Integer reportStyle) {
		if (line == null || reportStyle == null)
			return null;
		if(reportStyle.compareTo(Config.REPORT_STYLE_QD)==0) return line;
		if (!line.startsWith(FLAG_BJ)  && !line.startsWith(FLAG_BL) 
				&& !line.startsWith(FLAG_KP)  && !line.startsWith(FLAG_JS))
			return line;

		if (line.length() < FLAG_LEN)
			return null;

		return line.substring(FLAG_LEN);
	}

	/**
	 * 获取表达式的类型
	 * 
	 * @param line
	 *            String 行内容
	 * @return Integer 表达式类型
	 */
	private static Integer getFlag(String line) {
		if (line == null)
			return null;

		if (line.length() < FLAG_LEN)
			return null;

		if (!line.startsWith(FLAG_BJ)  && !line.startsWith(FLAG_BL) 
				&& !line.startsWith(FLAG_KP) && !line.startsWith(FLAG_JS))
			return Expression.FLAG_BL;

		String head = line.substring(0, FLAG_LEN);

		Integer style = null;

		if (head.toUpperCase().equals(FLAG_BL) == true)
			style = Expression.FLAG_BL;

		if (head.toUpperCase().equals(FLAG_BJ) == true)
			style = Expression.FLAG_BJ;

		if (head.toUpperCase().equals(FLAG_KP) == true)
			style = Expression.FLAG_KP;

		if (head.toUpperCase().equals(FLAG_JS) == true)
			style = Expression.FLAG_JS;

		return style;
	}

	/**
	 * 清单式报表的表内表间表达式检证
	 * 
	 * @param mCellForumForm
	 *            MCellFormuForm
	 * @param expression
	 *            String 关系表达式
	 * @return void 表达式校验正确，返回true;否则返回false
	 * @exception Exception
	 */
	public boolean validateEsp(MCellFormuForm mCellForumForm,
			String expression, Locale locale, MessageResources resources)
			throws Exception {
		boolean result = false;

		// // System.out.println("[FitechTemplate-ValidateEsp]Esp expression:" +
		// expression);
		if (mCellForumForm == null || expression == null)
			return result;

		this.mCellToFormuList = new ArrayList();

		String[] exps = expression.split(Config.SPLIT_SYMBOL_ESP);

		if (exps == null)
			return result;

		for (int i = 0; i < exps.length; i++) {
			String cellFormuView = "";
			
			String[] exp = exps[i].split(Config.SPLIT_SYMBOL_COMMA);
			String[] formu = exp[0].trim().split(SPLIT);
			if (formu != null && formu.length == 2)
			{
				exp[0] = formu[0];
				cellFormuView = formu[1];
			}
			else
			{
				this.error = resources.getMessage(locale, "express.cellFormuView.error", exp[0]);
				result = false;
				break;
			}

			if (isValidExpression(exp[0].trim()) == false) {
				this.error = resources.getMessage(locale,
						"express.defined.error", exp[0]);
				result = false;
				break;
			}

			MCellFormuForm form = new MCellFormuForm();

			/** 判断当前的表达式是否已存在* */
			if (StrutsAFCellFormuDelegate.isCellFormuExists(getExpression(exp[0]
					.trim()), mCellForumForm.getChildRepId(), mCellForumForm
					.getVersionId()) == true) {
				this.error = resources.getMessage(locale,
						"bjgx.expression.exists", getExpression(exp[0].trim()));
				result = false;
				return false;
			}
			/** 判断表达式的目标单元格是否存在* */
			List cols = getColOrCellList(exp[0].trim());
			if (isColNameExist(mCellForumForm
					.getChildRepId(), mCellForumForm.getVersionId(),
					cols,locale,resources) == false) {				
				return false;
			}


			List childReps = null; // 关连的报表列表
			List sameCols = null; // 表达式相似的单元格列表

			form.setChildRepId(mCellForumForm.getChildRepId());
			form.setVersionId(mCellForumForm.getVersionId());
			form.setCellFormu(exp[0].trim());
			form.setCellFormuView(cellFormuView);
			form.setFormuType(Config.CELL_CHECK_INNER);
			form.setSameCells(sameCols);
			form.setRelationTables(childReps);
			this.mCellToFormuList.add(form);

			result = true;
		}

		return result;
	}

	private boolean isColNameExist(String childRepId, String versionId,
			List cols,Locale locale, MessageResources resources) {
		QdCellinfo qdCellInfo = AFQDCellInfoDelegate.getCellInfo(childRepId,versionId);
		int startCol = convertColStringToNum(qdCellInfo.getStartCol())+1;
		int endCol = convertColStringToNum(qdCellInfo.getEndCol())+1;
		if (cols != null && cols.size() > 0)
		{
		for(int i=0;i<cols.size();i++){
			String colName = (String) cols.get(i);
			int colnum = Integer.valueOf(colName.replaceAll("T[.]", "").substring(3));
			if(colnum<startCol || startCol>endCol){
				this.error = resources.getMessage(locale, "qd.colName.not.exists", colName);
				return false;
			}
		}
		}
		
		return true;
	}
	
	/**
	 * 将列号转换为数字
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

	/**
	 * 根据关系表达式获取公式中的单元格或列名<br>
	 * 获取的单元格或报表列都是当前报表的元素
	 * 
	 * @param expression
	 *            String 关系表达式
	 * @return List
	 */
	private List getColOrCellList(String expression) {
		if (expression == null)
			return null;

		List cells = null;

		try {
			if (expression.indexOf(Config.SPLIT_SYMBOL_PIPE) > 0)
				expression = expression.substring(0, expression
						.indexOf(Config.SPLIT_SYMBOL_PIPE));

			// 正则表达式公式处理
			if (expression.indexOf("reg") > 0)
				expression = expression.substring(0, expression.indexOf("reg"));

			expression = format(expression);

			if (expression != null && expression.length() > 0) {
				FitechPDF fitechPDF = new FitechPDF();
				String[] arr = expression.split(Config.SPLIT_SYMBOL_COMMA);
				if (arr != null && arr.length > 0) {
					cells = new ArrayList();
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] != null
								&& !arr[i].trim().equals("")
								&& arr[i].length() > 0
								&& fitechPDF.CHARACTERS.indexOf(arr[i]
										.substring(0, 1)) >= 0
								&& arr[i].indexOf("_") < 0)
							cells.add(arr[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			cells = null;
		}

		return cells;
	}

	/**
	 * 表内表间表达式检证
	 * 
	 * @param mCellForumForm
	 *            MCellFormuForm
	 * @param expression
	 *            String 关系表达式
	 * @return void 表达式校验正确，返回true;否则返回false
	 * @exception Exception
	 */
	public boolean validateAF(MCellFormuForm mCellForumForm, String expression,
			Locale locale, MessageResources resources) throws Exception {
		boolean result = true;

		// System.out.println("[FitechTemplate-validate]expression:"
		// +expression);
		if (mCellForumForm == null || expression == null)
			return false;

		this.mCellToFormuList = new ArrayList();
		
		String[] exps = expression.split(Config.SPLIT_SYMBOL_ESP);

		if (exps == null)
			return false;

		String[] exp = null, formu = null;
		String cellFormuView = "";
		for (int i = 0; i < exps.length; i++) {
			
			exp = null;
			formu = null;
			// exp = exps[i].split(Config.SPLIT_SYMBOL_COMMA);

			/* 逗号于校验公式中重复，所以采用双管道符进行分割，如再有问题，可改用其他符号 @author Yao */
			exp = exps[i].split(Config.SPLIT_SYMBOL_SPECIAL);

			if (exp == null)
				continue;
			if (exp.length <= 0)
				continue;

//			if (exp[1].equals(String.valueOf(Config.CELL_CHECK_BETWEEN))) {
//				exp[0] = addVersionIdToFormul(exp[0]);
//			}
			formu = exp[0].trim().split(SPLIT);
			if (formu != null && formu.length == 2) {
				exp[0] = formu[0];
				cellFormuView = formu[1];
			} else {
				this.error = resources.getMessage(locale,
						"express.cellFormuView.error", exp[0]);
				return false;
				
			}


			/* 校验公式的有效性，目前无法判断正则表达式的校验公式，所以先注释掉 @author Yao */
			/*
			 * if (isValidExpression(exp[0].trim()) == false) { this.error =
			 * resources.getMessage(locale, "express.defined.error", exp[0]);
			 * result = false; break; }
			 */
			MCellFormuForm form = new MCellFormuForm();

			/** 判断当前的表达式是否已存在* */
			if (StrutsAFCellFormuDelegate.isCellFormuExists(
					getExpression(exp[0].trim()), mCellForumForm
							.getChildRepId(), mCellForumForm.getVersionId()) == true) {
				this.error = resources.getMessage(locale,
						"bjgx.expression.exists", getExpression(exp[0].trim()));
				
				return false;
			}
	
			String reportFlg = mCellForumForm.getReportFlg();
			/** 判断表达式的目标单元格是否存在* */
			List cells = getColOrCellList(exp[0].trim());
			if (cells != null && cells.size() > 0) {
				String cellName = "";

				// 追加人行校验
				if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
					for (int j = 0; j < cells.size(); j++) {
						cellName = (String) cells.get(j);
						String pid[] = cellName.split(Config.SPLIT_SYMBOL_DIAN);
						if (pid!= null && pid.length>1 && !StrutsAFCellDelegate.getCellId(mCellForumForm
								.getChildRepId(), mCellForumForm
								.getVersionId(), pid)) {
							this.error = resources.getMessage(locale,
									"cell.not.exists", cellName);
							return false;
							
						}
					}
				} else {
					for (int j = 0; j < cells.size(); j++) {
						cellName = (String) cells.get(j);
						if (StrutsAFCellDelegate.getCellId(mCellForumForm
								.getChildRepId(), mCellForumForm.getVersionId(),
								cellName.replaceAll("T[.]", "")) == null) {
							// System.out.println(cellName);
							this.error = resources.getMessage(locale,
									"cell.not.exists", cellName);
							return false;
							
						}
					}
				}
				
			}

			String colName = null;

			List childReps = null; // 关连的报表列表
			List sameCells = null; // 表达式相似的单元格列表

			Integer flag = Integer.valueOf(exp[1]);
			
			if (flag.compareTo(Expression.FLAG_BL) == 0) { // 表内校验表达式验证
				if (exp[0].indexOf(Config.SPLIT_SYMBOL_PIPE) > 0) {
					String tailer = exp[0].substring(exp[0]
							.indexOf(Config.SPLIT_SYMBOL_PIPE));
					String arr[] = tailer == null ? null : tailer.trim().split(
							Config.SPLIT_SYMBOL_COMMA);
					if (arr != null && arr.length > 0) {
						// 追加人行校验
						if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
							for (int j = 0; j < arr.length; j++) { 
								colName = arr[j].trim();
								String pid[] = colName.split(Config.SPLIT_SYMBOL_DIAN);
								if (pid!= null && pid.length>1 && !StrutsAFCellDelegate.getCellId(mCellForumForm
										.getChildRepId(), mCellForumForm
										.getVersionId(), pid)) {
									this.error = resources.getMessage(locale,
											"cell.not.exists", colName);
									return false;
									
								}
							}
						} else {
							for (int j = 0; j < arr.length; j++) { // 这样的验证还是有点问题，没做到验证所有管道符后的需转换成的单元格的验证
								colName = arr[j].trim();
								if (StrutsAFCellDelegate.getCellId(mCellForumForm
										.getChildRepId(), mCellForumForm
										.getVersionId(), colName) == null) {
									this.error = resources.getMessage(locale,
											"cell.not.exists", colName);
									return false;
									
								}
							}
						}
						
					}
				}
			} else if (flag.compareTo(Expression.FLAG_BJ) == 0) { // 表间校验表达式验证
				List bjList = getForumalList(exp[0].trim());
				if (bjList == null) { // 获取表间校验关链的报表列表失败!
					this.error = resources.getMessage(locale,
							"relation.report.get.error");
					return false;
					
				}

				childReps = new ArrayList();

				for (int j = 0; j < bjList.size(); j++) {
					if (bjList.get(j) != null) {
						String arr[] = ((String) bjList.get(j))
								.split(Config.SPLIT_SYMBOL_OUTLINE);
						if(arr != null && arr.length<3){
							this.error = "报表单元格未定义";
							return false;
							
						}
						if (AFTemplateDelegate.isTemplateExists(arr[0], arr[1]) == true) {
							// 追加人行校验
							if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
					
								String pid[] = arr[2].split(Config.SPLIT_SYMBOL_DIAN);
								if (pid!= null && pid.length>1 && !StrutsAFCellDelegate.getCellId(arr[0], arr[1], pid)) {
									this.error = FitechResource.getMsg(locale,
											resources,
											"relation.report.cell.not.exists",
											arr[0], arr[1], arr[0]);
									return false;
									
								}
							
							} else {
								if (StrutsAFCellDelegate.getCellId(arr[0], arr[1],
										arr[2]) == null) { // 判断关链的单元格是否存在
									
									this.error = FitechResource.getMsg(locale,
											resources,
											"relation.report.cell.not.exists",
											arr[0], arr[1], arr[0]);
									return false;
									
								}
							}
							
						} else { // 表间校验关链的报表不存在!
							this.error = FitechResource.getMessage(locale,
									resources, "relation.report.not.exists",
									arr[0], arr[1]);
							return false;
							
						}
					}
				}
			}

			form.setChildRepId(mCellForumForm.getChildRepId());
			form.setVersionId(mCellForumForm.getVersionId());
			form.setCellFormu(exp[0].trim());
			form.setCellFormuView(cellFormuView);
			form.setFormuType(Integer.valueOf(exp[1]));
			form.setSameCells(sameCells);
			form.setRelationTables(childReps);
			this.mCellToFormuList.add(form);
			
		}

		return result;
	}

	private String addVersionIdToFormul(String exp) {
		String[] formu = null;
		formu = exp.trim().split(SPLIT);
		if (formu == null || formu.length < 2) {
			return null;
		}
		String bjformul = formu[0];
		try {
			if (bjformul.indexOf(Config.SPLIT_SYMBOL_PIPE) > 0)
				bjformul = bjformul.substring(0, bjformul
						.indexOf(Config.SPLIT_SYMBOL_PIPE));

			// 正则表达式公式处理
			if (bjformul.indexOf("reg") > 0)
				bjformul = bjformul.substring(0, bjformul.indexOf("reg"));

			bjformul = format(bjformul);

			if (bjformul != null && bjformul.length() > 0) {
				FitechPDF fitechPDF = new FitechPDF();
				String[] arr = bjformul.split(Config.SPLIT_SYMBOL_COMMA);
				if (arr != null && arr.length > 0) {
					for (int i = 0; i < arr.length; i++) {
						if (!StringUtil.isEmpty(arr[i])
								&& fitechPDF.CHARACTERS.indexOf(arr[i].substring(0, 1)) >= 0
								&& arr[i].indexOf("_") > 0) {
							String[] bjtemplateformul = arr[i]
									.split(Config.SPLIT_SYMBOL_OUTLINE);
							if (bjtemplateformul != null
									&& bjtemplateformul.length >= 2) {
								String versionId = AFTemplateDelegate
										.getTemplateVersionId(bjtemplateformul[0]);
								if (!StringUtil.isEmpty(versionId)) {
									int arridex = arr[i].indexOf(Config.SPLIT_SYMBOL_OUTLINE);
									String updateFormul = bjtemplateformul[0]
											+ Config.SPLIT_SYMBOL_OUTLINE
											+ versionId
											+ Config.SPLIT_SYMBOL_OUTLINE
											+ arr[i].substring(arridex+1);
									exp=exp.replaceAll(arr[i], updateFormul);
								}
							}
						}
					}
				}
			}
			return exp;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public boolean validateAFO(MCellFormuForm mCellForumForm,
			String expression, Locale locale, MessageResources resources,
			String reportFlg, String versionId) throws Exception {
		boolean result = false;

		// System.out.println("[FitechTemplate-validate]expression:"
		// +expression);
		if (mCellForumForm == null || expression == null)
			return result;

		this.mCellToFormuList = new ArrayList();

		String[] exps = expression.split(Config.SPLIT_SYMBOL_ESP);

		if (exps == null)
			return result;

		String[] exp = null, formu = null;
		String cellFormuView = "";
		for (int i = 0; i < exps.length; i++) {
			exp = null;
			formu = null;
			// exp = exps[i].split(Config.SPLIT_SYMBOL_COMMA);

			/* 逗号于校验公式中重复，所以采用双管道符进行分割，如再有问题，可改用其他符号 @author Yao */
			exp = exps[i].split(Config.SPLIT_SYMBOL_SPECIAL);

			if (exp == null)
				continue;
			if (exp.length <= 0)
				continue;

			formu = exp[0].trim().split(SPLIT);
			if (formu != null && formu.length == 2) {
				exp[0] = formu[0];
				cellFormuView = formu[1];
			} else {
				this.error = resources.getMessage(locale,
						"express.cellFormuView.error", exp[0]);
				result = false;
				break;
			}

			/* 校验公式的有效性，目前无法判断正则表达式的校验公式，所以先注释掉 @author Yao */
			/*
			 * if (isValidExpression(exp[0].trim()) == false) { this.error =
			 * resources.getMessage(locale, "express.defined.error", exp[0]);
			 * result = false; break; }
			 */
			MCellFormuForm form = new MCellFormuForm();

			/** 判断当前的表达式是否已存在* */
			if (StrutsAFCellFormuDelegate.isCellFormuExists(
					getExpression(exp[0].trim()), mCellForumForm
							.getChildRepId(), mCellForumForm.getVersionId()) == true) {
				this.error = resources.getMessage(locale,
						"bjgx.expression.exists", getExpression(exp[0].trim()));
				result = false;
				return false;
			}
			/** 判断表达式的目标单元格是否存在* */
			List cells = getColOrCellList(exp[0].trim());
			if (cells != null && cells.size() > 0) {
				String cellName = "";
				for (int j = 0; j < cells.size(); j++) {
					cellName = (String) cells.get(j);
					if (StrutsAFCellDelegate.getCellId(mCellForumForm
							.getChildRepId(), mCellForumForm.getVersionId(),
							cellName.replaceAll("T[.]", "")) == null) {
						// System.out.println(cellName);
						this.error = resources.getMessage(locale,
								"cell.not.exists", cellName);
						result = false;
						return false;
					}
				}
			} else {
				this.error = resources.getMessage(locale, "cell.not.exists");
				result = false;
				return false;
			}

			String colName = null;

			List childReps = null; // 关连的报表列表
			List sameCells = null; // 表达式相似的单元格列表

			Integer flag = Integer.valueOf(exp[1]);

			if (flag.compareTo(Expression.FLAG_BL) == 0) { // 表内校验表达式验证
				if (exp[0].indexOf(Config.SPLIT_SYMBOL_PIPE) > 0) {
					String tailer = exp[0].substring(exp[0]
							.indexOf(Config.SPLIT_SYMBOL_PIPE));
					String arr[] = tailer == null ? null : tailer.trim().split(
							Config.SPLIT_SYMBOL_COMMA);
					if (arr != null && arr.length > 0) {
						for (int j = 0; j < arr.length; j++) { // 这样的验证还是有点问题，没做到验证所有管道符后的需转换成的单元格的验证
							colName = arr[j].trim();
							if (StrutsAFCellDelegate.getCellId(mCellForumForm
									.getChildRepId(), mCellForumForm
									.getVersionId(), colName) == null) {
								this.error = resources.getMessage(locale,
										"cell.not.exists", colName);
								result = false;
								break;
							}
						}
					}
				}
			} else if (flag.compareTo(Expression.FLAG_BJ) == 0) { // 表间校验表达式验证
				List bjList = getForumalList(exp[0].trim());
				if (bjList == null) { // 获取表间校验关链的报表列表失败!
					this.error = resources.getMessage(locale,
							"relation.report.get.error");
					result = false;
					break;
				}

				childReps = new ArrayList();

				for (int j = 0; j < bjList.size(); j++) {
					if (bjList.get(j) != null) {
						String arr[] = ((String) bjList.get(j))
								.split(Config.SPLIT_SYMBOL_OUTLINE);

						if (!reportFlg.endsWith("1") && arr.length == 2) {

							if (AFTemplateDelegate.isTemplateExists(arr[0],
									versionId) == true) {
								if (StrutsAFCellDelegate.getCellId(arr[0],
										versionId, arr[1]) != null) { // 判断关链的单元格是否存在
									// childReps.add(arr[0] +
									// Config.SPLIT_SYMBOL_COMMA + arr[1]);
								} else {
									this.error = FitechResource.getMsg(locale,
											resources,
											"relation.report.cell.not.exists",
											arr[0], arr[1], arr[0]);
									result = false;
									break;
								}
							} else { // 表间校验关链的报表不存在!
								this.error = FitechResource.getMessage(locale,
										resources,
										"relation.report.not.exists", arr[0],
										arr[1]);
								result = false;
								break;
							}
						} else {
							if (AFTemplateDelegate.isTemplateExists(arr[0],
									arr[1]) == true) {
								if (StrutsAFCellDelegate.getCellId(arr[0],
										arr[1], arr[2]) != null) { // 判断关链的单元格是否存在
									// childReps.add(arr[0] +
									// Config.SPLIT_SYMBOL_COMMA + arr[1]);
								} else {
									this.error = FitechResource.getMsg(locale,
											resources,
											"relation.report.cell.not.exists",
											arr[0], arr[1], arr[0]);
									result = false;
									break;
								}
							} else { // 表间校验关链的报表不存在!
								this.error = FitechResource.getMessage(locale,
										resources,
										"relation.report.not.exists", arr[0],
										arr[1]);
								result = false;
								break;
							}
						}

					}
				}
			}

			form.setChildRepId(mCellForumForm.getChildRepId());
			form.setVersionId(mCellForumForm.getVersionId());
			form.setCellFormu(exp[0].trim());
			form.setCellFormuView(cellFormuView);
			form.setFormuType(Integer.valueOf(exp[1]));
			form.setSameCells(sameCells);
			form.setRelationTables(childReps);
			form.setCellId(Integer.valueOf(String
					.valueOf((StrutsAFCellFormuDelegate.getCellid(
							mCellForumForm.getChildRepId(), mCellForumForm
									.getVersionId())))));
			this.mCellToFormuList.add(form);
			result = true;
		}

		return result;
	}

	public boolean validateAFF(MCellFormuForm mCellForumForm, Locale locale,
			MessageResources resources, String reportFlg, String versionId,
			Integer type, String childRepId, String _cellFormu)
			throws Exception {
		boolean result = false;

		if (mCellForumForm == null)
			return result;

		this.mCellToFormuList = new ArrayList();

		// exp = exps[i].split(Config.SPLIT_SYMBOL_COMMA);

		/* 逗号于校验公式中重复，所以采用双管道符进行分割，如再有问题，可改用其他符号 @author Yao */

		/* 校验公式的有效性，目前无法判断正则表达式的校验公式，所以先注释掉 @author Yao */
		/*
		 * if (isValidExpression(exp[0].trim()) == false) { this.error =
		 * resources.getMessage(locale, "express.defined.error", exp[0]); result =
		 * false; break; }
		 */
		MCellFormuForm form = new MCellFormuForm();

		/** 判断当前的表达式是否已存在* */
		if (StrutsAFCellFormuDelegate.isCellFormuExists(_cellFormu,
				mCellForumForm.getChildRepId(), mCellForumForm.getVersionId()) == true) {
			this.error = resources.getMessage(locale, "bjgx.expression.exists",
					mCellForumForm.getCellFormu());
			result = false;
			return false;
		}
		/** 判断表达式的目标单元格是否存在* */
		List cells = getColOrCellList(_cellFormu);
		if (cells != null && cells.size() > 0) {
			String cellName = "";
			for (int j = 0; j < cells.size(); j++) {
				cellName = (String) cells.get(j);
				if (StrutsAFCellDelegate.getCellId(mCellForumForm
						.getChildRepId(), mCellForumForm.getVersionId(),
						cellName.replaceAll("T[.]", "")) == null) {
					// System.out.println(cellName);
					this.error = resources.getMessage(locale,
							"cell.not.exists", cellName);
					result = false;
					return false;
				}
			}
		} else {
			String cellName = null;
			this.error = resources.getMessage(locale, "cell.not.exists",
					cellName);
			result = false;
			return false;
		}

		String colName = null;

		List childReps = null; // 关连的报表列表
		List sameCells = null; // 表达式相似的单元格列表

		Integer flag = Integer.valueOf(type);

		if (flag.compareTo(type) == 0) { // 表内校验表达式验证
			if (_cellFormu.indexOf(Config.SPLIT_SYMBOL_PIPE) > 0) {
				String tailer = _cellFormu.substring(_cellFormu
						.indexOf(Config.SPLIT_SYMBOL_PIPE));
				String arr[] = tailer == null ? null : tailer.trim().split(
						Config.SPLIT_SYMBOL_COMMA);
				if (arr != null && arr.length > 0) {
					for (int j = 0; j < arr.length; j++) { // 这样的验证还是有点问题，没做到验证所有管道符后的需转换成的单元格的验证
						colName = arr[j].trim();
						if (StrutsAFCellDelegate.getCellId(mCellForumForm
								.getChildRepId(),
								mCellForumForm.getVersionId(), colName) == null) {
							this.error = resources.getMessage(locale,
									"cell.not.exists", colName);
							result = false;
							break;
						}
					}
				}
			}
		} else if (flag.compareTo(type) == 0) { // 表间校验表达式验证
			List bjList = getForumalList(_cellFormu);
			if (bjList == null) { // 获取表间校验关链的报表列表失败!
				this.error = resources.getMessage(locale,
						"relation.report.get.error");
				result = false;
			}

			childReps = new ArrayList();

			for (int j = 0; j < bjList.size(); j++) {
				if (bjList.get(j) != null) {
					String arr[] = ((String) bjList.get(j))
							.split(Config.SPLIT_SYMBOL_OUTLINE);

					if (!reportFlg.endsWith("1") && arr.length == 2) {

						if (AFTemplateDelegate.isTemplateExists(childRepId,
								versionId) == true) {
							if (StrutsAFCellDelegate.getCellId(childRepId,
									versionId, arr[1]) != null) { // 判断关链的单元格是否存在
								// childReps.add(arr[0] +
								// Config.SPLIT_SYMBOL_COMMA + arr[1]);
							} else {
								this.error = FitechResource.getMsg(locale,
										resources,
										"relation.report.cell.not.exists",
										arr[0], arr[1], arr[0]);
								result = false;
								break;
							}
						} else { // 表间校验关链的报表不存在!
							this.error = FitechResource.getMessage(locale,
									resources, "relation.report.not.exists",
									arr[0], arr[1]);
							result = false;
							break;
						}
					} else {
						if (AFTemplateDelegate.isTemplateExists(childRepId,
								versionId) == true) {
							if (StrutsAFCellDelegate.getCellId(childRepId,
									versionId, arr[2]) != null) { // 判断关链的单元格是否存在
								// childReps.add(arr[0] +
								// Config.SPLIT_SYMBOL_COMMA + arr[1]);
							} else {
								this.error = FitechResource.getMsg(locale,
										resources,
										"relation.report.cell.not.exists",
										arr[0], arr[1], arr[0]);
								result = false;
								break;
							}
						} else { // 表间校验关链的报表不存在!
							this.error = FitechResource.getMessage(locale,
									resources, "relation.report.not.exists",
									arr[0], arr[1]);
							result = false;
							break;
						}
					}

				}
			}
		}

		form.setChildRepId(childRepId);
		form.setVersionId(versionId);
		form.setCellFormu(_cellFormu);
		form.setCellFormuView(mCellForumForm.getCellFormuView());
		form.setFormuType(type);
		form.setSameCells(sameCells);
		form.setRelationTables(childReps);
		form.setCellId(Integer.valueOf(String
				.valueOf((StrutsAFCellFormuDelegate.getCellid(mCellForumForm
						.getChildRepId(), mCellForumForm.getVersionId())))));
		this.mCellToFormuList.add(form);
		result = true;

		return result;
	}

	public boolean validateAFU(MCellFormuForm mCellForumForm, Locale locale,
			MessageResources resources, String reportFlg, String versionId,
			Integer type, String childRepId, String _cellFormu, String formuId)
			throws Exception {
		boolean result = false;

		if (mCellForumForm == null)
			return result;

		this.mCellToFormuList = new ArrayList();

		// exp = exps[i].split(Config.SPLIT_SYMBOL_COMMA);

		/* 逗号于校验公式中重复，所以采用双管道符进行分割，如再有问题，可改用其他符号 @author Yao */

		/* 校验公式的有效性，目前无法判断正则表达式的校验公式，所以先注释掉 @author Yao */
		/*
		 * if (isValidExpression(exp[0].trim()) == false) { this.error =
		 * resources.getMessage(locale, "express.defined.error", exp[0]); result =
		 * false; break; }
		 */
		MCellFormuForm form = new MCellFormuForm();

		/** 判断当前的表达式是否已存在* */
		if (StrutsAFCellFormuDelegate.isCellFormuExists(_cellFormu,
				mCellForumForm.getChildRepId(), mCellForumForm.getVersionId()) == false) {
			this.error = resources.getMessage(locale, "bjgx.expression.exists",
					mCellForumForm.getCellFormu());
			result = false;
			return false;
		}
		/** 判断表达式的目标单元格是否存在* */
		List cells = getColOrCellList(_cellFormu);
		if (cells != null && cells.size() > 0) {
			String cellName = "";
			for (int j = 0; j < cells.size(); j++) {
				cellName = (String) cells.get(j);
				if (StrutsAFCellDelegate.getCellId(mCellForumForm
						.getChildRepId(), mCellForumForm.getVersionId(),
						cellName.replaceAll("T[.]", "")) == null) {
					// System.out.println(cellName);
					this.error = resources.getMessage(locale,
							"cell.not.exists", cellName);
					result = false;
					return false;
				}
			}
		} else {
			String cellName = null;
			this.error = resources.getMessage(locale, "cell.not.exists",
					cellName);
			result = false;
			return false;
		}

		String colName = null;

		List childReps = null; // 关连的报表列表
		List sameCells = null; // 表达式相似的单元格列表

		Integer flag = Integer.valueOf(type);

		if (flag.compareTo(type) == 0) { // 表内校验表达式验证
			if (_cellFormu.indexOf(Config.SPLIT_SYMBOL_PIPE) > 0) {
				String tailer = _cellFormu.substring(_cellFormu
						.indexOf(Config.SPLIT_SYMBOL_PIPE));
				String arr[] = tailer == null ? null : tailer.trim().split(
						Config.SPLIT_SYMBOL_COMMA);
				if (arr != null && arr.length > 0) {
					for (int j = 0; j < arr.length; j++) { // 这样的验证还是有点问题，没做到验证所有管道符后的需转换成的单元格的验证
						colName = arr[j].trim();
						if (StrutsAFCellDelegate.getCellId(mCellForumForm
								.getChildRepId(),
								mCellForumForm.getVersionId(), colName) == null) {
							this.error = resources.getMessage(locale,
									"cell.not.exists", colName);
							result = false;
							break;
						}
					}
				}
			}
		} else if (flag.compareTo(type) == 0) { // 表间校验表达式验证
			List bjList = getForumalList(_cellFormu);
			if (bjList == null) { // 获取表间校验关链的报表列表失败!
				this.error = resources.getMessage(locale,
						"relation.report.get.error");
				result = false;
			}

			childReps = new ArrayList();

			for (int j = 0; j < bjList.size(); j++) {
				if (bjList.get(j) != null) {
					String arr[] = ((String) bjList.get(j))
							.split(Config.SPLIT_SYMBOL_OUTLINE);

					if (!reportFlg.endsWith("1") && arr.length == 2) {

						if (AFTemplateDelegate.isTemplateExists(childRepId,
								versionId) == true) {
							if (StrutsAFCellDelegate.getCellId(childRepId,
									versionId, arr[1]) != null) { // 判断关链的单元格是否存在
								// childReps.add(arr[0] +
								// Config.SPLIT_SYMBOL_COMMA + arr[1]);
							} else {
								this.error = FitechResource.getMsg(locale,
										resources,
										"relation.report.cell.not.exists",
										arr[0], arr[1], arr[0]);
								result = false;
								break;
							}
						} else { // 表间校验关链的报表不存在!
							this.error = FitechResource.getMessage(locale,
									resources, "relation.report.not.exists",
									arr[0], arr[1]);
							result = false;
							break;
						}
					} else {
						if (AFTemplateDelegate.isTemplateExists(childRepId,
								versionId) == true) {
							if (StrutsAFCellDelegate.getCellId(childRepId,
									versionId, arr[2]) != null) { // 判断关链的单元格是否存在
								// childReps.add(arr[0] +
								// Config.SPLIT_SYMBOL_COMMA + arr[1]);
							} else {
								this.error = FitechResource.getMsg(locale,
										resources,
										"relation.report.cell.not.exists",
										arr[0], arr[1], arr[0]);
								result = false;
								break;
							}
						} else { // 表间校验关链的报表不存在!
							this.error = FitechResource.getMessage(locale,
									resources, "relation.report.not.exists",
									arr[0], arr[1]);
							result = false;
							break;
						}
					}

				}
			}
		}
		form.setCellFormuId(Integer.valueOf(formuId));
		form.setChildRepId(childRepId);
		form.setVersionId(versionId);
		form.setCellFormu(_cellFormu);
		form.setCellFormuView(mCellForumForm.getCellFormuView());
		form.setFormuType(type);
		form.setSameCells(sameCells);
		form.setRelationTables(childReps);
		form.setCellId(Integer.valueOf(String
				.valueOf((StrutsAFCellFormuDelegate.getCellid(mCellForumForm
						.getChildRepId(), mCellForumForm.getVersionId())))));
		this.mCellToFormuList.add(form);
		result = true;

		return result;
	}

	/**
	 * 获取关系表达式,将表达式后的管道符后部的内容去除
	 * 
	 * @param content
	 *            String
	 * @return String
	 */
	private String getExpression(String content) {
		if (content == null)
			return null;
		if (content.equals(""))
			return null;

		if (content.indexOf(Config.SPLIT_SYMBOL_PIPE) > 0)
			content = content.substring(0, content
					.indexOf(Config.SPLIT_SYMBOL_PIPE));

		return content;
	}

	/**
	 * 获得关系表达式中的关连报表模板列表
	 * 
	 * @param forumla
	 *            String 表达式
	 * @return List
	 */
	private List getForumalList(String forumla) {
		List resList = null;

		if (forumla == null)
			return resList;

		String tmp = format(forumla);

		if (tmp == null)
			return resList;

		resList = new ArrayList();
		String arr[] = tmp.split(Config.SPLIT_SYMBOL_COMMA);

		for (int i = 0; i < arr.length; i++) {
			if (!arr[i].trim().equals("")
					&& arr[i].indexOf(Config.SPLIT_SYMBOL_OUTLINE) > 0) {
				resList.add(arr[i]);
			}
		}

		return resList;
	}

	/**
	 * 判断关系公式是否正确
	 * 
	 * @param expression
	 *            关系公式
	 * @return boolean 判断正确，返回true;否则，返回false
	 */
	private boolean isValidExpression(String expression) {
		boolean result = false;
		String number = "0123456789.";

		if (expression == null)
			return false;

		int pos1 = expression.indexOf(Config.SPLIT_SYMBOL_LEFT_MID_KUOHU);
		int pos2 = expression.indexOf(Config.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		expression = expression.substring(pos2 + 1);

		while (pos2 >= 0) {
			pos1 = expression.indexOf(Config.SPLIT_SYMBOL_LEFT_MID_KUOHU);
			pos2 = expression.indexOf(Config.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
			if (pos1 >= 0 && pos2 >= 0)
				expression = expression.substring(0, pos1)
						+ expression.substring(pos2 + 1);
		}

		// if(expression.substring(0,1).equals(Config.SPLIT_SYMBOL_EQUAL)==true){
		if (expression.length() > 1) {
			result = true;
			String str = getValidSymbolString();

			for (int i = 1; i < expression.length() - 1; i++) {
				if (str.indexOf(expression.substring(i, i + 1)) < 0
						&& number.indexOf(expression.substring(i, i + 1)) < 0) {
					result = false;
					break;
				}
			}
		} else {
			if (expression.length() == 1 && expression.equals("="))
				result = true;
		}
		// }

		return result;
	}

	/**
	 * 获得合法运算符的字符串
	 * 
	 * @return String
	 */
	private String getValidSymbolString() {
		if (Config.OPERATOR_SYMBOL == null)
			return "";
		if (Config.OPERATOR_SYMBOL.length == 0)
			return "";

		String str = "";
		for (int i = 0; i < Config.OPERATOR_SYMBOL.length; i++) {
			str += Config.OPERATOR_SYMBOL[i];
		}

		return str;
	}

	/**
	 * 格式化关系表达式<br>
	 * 将关系表达式中的运算符和[]符号全部替换成逗号
	 * 
	 * @param expression
	 *            String 表达式
	 * @return String 格式化后的表达式
	 */
	public String format(String expression) {
		if (expression == null)
			return expression;

		String res = expression.replaceAll("=", Config.SPLIT_SYMBOL_COMMA);
		
		if(res.indexOf(Config.SPLIT_SYMBOL_IF)>-1){
			res = res.replaceAll(Config.SPLIT_SYMBOL_IF, "");
			res = res.replaceAll(Config.SPLIT_SYMBOL_CURRMONTH, "");
		}
		
		for (int i = 0; i < Config.OPERATOR_SYMBOL.length; i++) {
			if (Config.OPERATOR_SYMBOL[i].length() == 1) {
				res = res.replace(Config.OPERATOR_SYMBOL[i].toCharArray()[0],
						Config.SPLIT_SYMBOL_COMMA.toCharArray()[0]);
			} else {
				res = res.replaceAll(Config.OPERATOR_SYMBOL[i],
						Config.SPLIT_SYMBOL_COMMA);
			}
		}

		return res.replace(Config.SPLIT_SYMBOL_LEFT_MID_KUOHU.toCharArray()[0],
				Config.SPLIT_SYMBOL_COMMA.toCharArray()[0]).replace(
				Config.SPLIT_SYMBOL_RIGHT_MID_HUOHU.toCharArray()[0],
				Config.SPLIT_SYMBOL_COMMA.toCharArray()[0]);
	}

	/**
	 * 获取需要入库的表达式的列表
	 * 
	 * @return List
	 */
	public List getMCellToFormuList() {
		return this.mCellToFormuList;
	}

	/**
	 * 获取错误信息键
	 * 
	 * @return String
	 */
	public String getError() {
		return this.error;
	}

}
