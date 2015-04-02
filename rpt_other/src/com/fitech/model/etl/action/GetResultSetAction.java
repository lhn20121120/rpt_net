package com.fitech.model.etl.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fitech.framework.core.web.action.DefaultBaseAction;
import  com.fitech.framework.core.common.Config;
import com.fitech.model.etl.common.ETLConfig;
import com.fitech.model.etl.service.IETLLoadFileInfoService;
import com.fitech.model.etl.service.IGetResultSetBySQLService;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.opensymphony.xwork2.ActionContext;

public class GetResultSetAction extends DefaultBaseAction {
	public Log log = LogFactory.getLog(GetResultSetAction.class);
	private IGetResultSetBySQLService resultSetBySQLService;
	private IETLLoadFileInfoService loadFIleInfoService;
	private String searchSql;// 查询SQL
	private List<Object[]> objList;// 结果集合
	private String searchType;// 查询类型
	private String filePath;
	private List<List<Object[]>> objArrayList;
	private Boolean isSuccess;
	private String path;
	private String fileName;
	private String jspPath;
	private List<String> objStrList;
	private String tablename;
	
	
	private File uploadFileNow;
	private String uploadFileNowFileName;
	public File getUploadFileNow() {
		return uploadFileNow;
	}

	public void setUploadFileNow(File uploadFileNow) {
		this.uploadFileNow = uploadFileNow;
	}

	public String getUploadFileNowFileName() {
		return uploadFileNowFileName;
	}

	public void setUploadFileNowFileName(String uploadFileNowFileName) {
		this.uploadFileNowFileName = uploadFileNowFileName;
	}

	/***
	 * 根据传入的查询类型执行(searchType)SQL语句 searchType值： "sql":代表执行SQL语句，可执行INSERT
	 * UPDATE DELETE语句 "proc":代表执行存储过程
	 * "createProc":代表创建存储过程，由于创建存储过程语句有可能过于复杂，顾此处将创建存储过程和执行存储过程区分执行
	 * 
	 * @return
	 */
	public String findListBySql() {
		Connection conn = null;
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(ServletActionContext.HTTP_REQUEST);
		PrintWriter writer = null;
		System.out.println("searchSql =" + searchSql);
		try {
			conn = resultSetBySQLService.getConnection();
			writer = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage(), e1);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}

		boolean isFirst = true;
		// 写入样式
		writer.write("<link href=\"" + Config.WEBROOTPATH
				+ "/css/index.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		writer.write("<link href=\"" + Config.WEBROOTPATH
				+ "/css/table.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		writer
				.write("<link href=\""
						+ Config.WEBROOTPATH
						+ "/css/globalStyle.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		writer
				.write("<link href=\""
						+ request.getContextPath()
						+ "/js/themes/default/easyui.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		writer
				.write("<link href=\""
						+ request.getContextPath()
						+ "/js/themes/icon.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		writer.write("<link href=\"" + request.getContextPath()
				+ "/js/demo.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		writer.write("<script type=\"text/javascript\" src=\""
				+ request.getContextPath()
				+ "/js/jquery-1.4.min.js\"></script>\n");
		writer.write("<script type=\"text/javascript\" src=\""
				+ request.getContextPath()
				+ "/js/jquery.easyui.min.js\"></script>\n");
		writer.write("<script type=\"text/javascript\" src=\""
				+ request.getContextPath()
				+ "/js/easyui-lang-zh_CN.js\"></script>\n");
		// 写入样式结束
		if (isFirst) {// 写入返回按钮
			writer
					.write("<a href=\""
							+ request.getContextPath()
							+ "/getsql/rightindex.jsp\" class=\"easyui-linkbutton\"   plain=\"true\" iconCls=\"icon-back\" style=\"border:1px solid\">返回</a><br>\n");
			isFirst = false;
		}

		/***
		 * 执行SQL语句
		 */
		if (searchType.equals("sql")) {
			objArrayList = new ArrayList<List<Object[]>>();
			if (searchSql.contains(";")) {
				String[] searchSqls = searchSql.split(";");
				for (int i = 0; i < searchSqls.length; i++) {// 执行增删改语句
					String sql = searchSqls[i];// 获取分割后的SQL
					if (sql.trim().startsWith("-")) {// 清除换行符
						sql = sql.substring(sql.indexOf("\r\n", 2) + 2);
					}
					if (sql.contains("\r\n"))
						sql = sql.replaceAll("\r\n", " ");
					System.out.println("sql=" + sql);
					if (sql != null && !sql.trim().equals("")
							&& !sql.startsWith("-")) {
						try {
							if (!sql.toUpperCase().startsWith("SELECT")) {// 增，删，改语句
								// ||sql.toUpperCase().startsWith("UPDATE") ||
								// sql.toUpperCase().startsWith("DELETE")||
								// sql.toUpperCase().startsWith("ALTER")

								isSuccess = resultSetBySQLService.execSQL(sql,
										null, conn);// 执行SQL
								writer.write("<br>");
								writer
										.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:green;font-size:14px\">SQL语句&nbsp;&nbsp;&nbsp;"
												+ sql
												+ "&nbsp;&nbsp;&nbsp;执行成功!</span><br>\n");
							}
							if (sql.toUpperCase().startsWith("SELECT")) {// 执行查询SQL
								objList = resultSetBySQLService
										.getListBySQL(sql);
								objArrayList.add(objList);
							}
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getMessage(), e);// 写入失败信息
							writer.write("<br>");
							writer
									.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:red;font-size:14px\">SQL语句&nbsp;&nbsp;&nbsp;"
											+ sql
											+ "&nbsp;&nbsp;&nbsp;执行失败，"
											+ e.getMessage() + "</span><br>\n");
						}
						if (objArrayList != null && objArrayList.size() > 0) {// 查询SQL执行结果
							for (List<Object[]> objs : objArrayList) {// 遍历结果，生成表格
								writer
										.write("<table style=\"width:100%;border:1px ;border-style: ridge;\" cellpadding=\"0\" cellspacing=\"0\">\n");
								if (objs != null && objs.size() > 0) {
									for (int j = 0; j < objs.size(); j++) {
										Object[] obj = objs.get(i);
										String color = "";
										if (j != 0 && j % 2 == 0)// 斑马线样式
											color = "#EBF3FA";
										else
											color = "white";
										writer.write("<tr>\n");
										for (int k = 0; k < obj.length; k++) {
											writer
													.write("<td style=\"background-color:"
															+ color
															+ ";padding:4px;font-size:13px;border:1px ;border-style: ridge;\">"
															+ obj[k]
															+ "</td>\n");
										}
										writer.write("</tr>\n");
									}
								}
								writer.write("</table>\n");
							}
						}
					}
				}
				// 处理有分号结尾SQL结束
			} else {// 无分号
				if (!searchSql.toUpperCase().startsWith("SELECT")
				// ||searchSql.toUpperCase().startsWith("UPDATE") ||
				// searchSql.toUpperCase().startsWith("DELETE")||
				// searchSql.toUpperCase().startsWith("ALTER")
				) {// 执行增删改SQL语句

					try {
						isSuccess = resultSetBySQLService.execSQL(searchSql,
								null, conn);// 执行
						writer.write("<br>");
						writer
								.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:green;font-size:14px\">SQL语句&nbsp;&nbsp;&nbsp;"
										+ searchSql
										+ "&nbsp;&nbsp;&nbsp;执行成功!</span><br>\n");
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage(), e);// 写入异常信息
						writer.write("<br>");
						writer
								.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:red;font-size:14px\">SQL语句&nbsp;&nbsp;&nbsp;"
										+ searchSql
										+ "&nbsp;&nbsp;&nbsp;执行失败，"
										+ e.getMessage() + "</span><br>\n");
					}
				}

				if (searchSql.toUpperCase().startsWith("SELECT")) {// 执行查询SQL
					try {
						objList = resultSetBySQLService.getListBySQL(searchSql);// 执行
						objArrayList.add(objList);
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage(), e);// 写入异常信息
						writer.write("<br>");
						writer
								.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:red;font-size:14px\">SQL语句&nbsp;&nbsp;&nbsp;"
										+ searchSql
										+ "&nbsp;&nbsp;&nbsp;执行失败，"
										+ e.getMessage() + "</span><br>\n");
					}
				}
				if (objArrayList != null && objArrayList.size() > 0) {
					for (List<Object[]> objs : objArrayList) {// 遍历集合，生成表格
						writer
								.write("<table style=\"width:100%;border:solid 1px #bdd5e0;\" cellpadding=\"0\" align=\"center\" cellspacing=\"0\">\n");
						if (objs != null && objs.size() > 0) {
							for (int i = 0; i < objs.size(); i++) {
								Object[] obj = objs.get(i);
								String color = "";
								if (i != 0 && i % 2 == 0)
									color = "#EBF3FA";
								else
									color = "white";
								writer.write("<tr>\n");
								for (int j = 0; j < obj.length; j++) {
									writer
											.write("<td style=\"background-color:"
													+ color
													+ ";padding:4px;font-size:13px;border:1px ;border-style: ridge;text-align:center\">"
													+ obj[j] + "</td>\n");
								}
								writer.write("</tr>\n");
							}
						}
						writer.write("</table>\n");
					}
				}
			}
		}

		else if (searchType.equals("proc")) {// 存储过程

			// 写入样式结束
			if (searchSql.indexOf(";") > 0) {// 以分号结尾SQL
				String[] searchSqls = searchSql.split(";");
				for (int i = 0; i < searchSqls.length; i++) {
					String sql = searchSqls[i];// 分割SQL
					if (sql != null && !sql.equals("")) {
						try {
							objList = resultSetBySQLService.getListByProc(sql);// 执行存储过程
							if (objList != null && objList.size() > 0) {// 若有返回，遍历集合，生成表格
								writer
										.write("<table style=\"width:100%;border:1px ;border-style: ridge;\" cellpadding=\"0\" cellspacing=\"0\">\n");
								for (int j = 0; i < objList.size(); j++) {
									Object[] obj = objList.get(j);
									String color = "";
									if (j != 0 && j % 2 == 0)
										color = "#EBF3FA";
									else
										color = "white";
									writer.write("<tr>\n");
									for (int k = 0; k < obj.length; j++) {
										writer
												.write("<td style=\"background-color:"
														+ color
														+ ";padding:4px;font-size:13px;border:1px ;border-style: ridge;\">"
														+ obj[k] + "</td>\n");
									}
									writer.write("</tr>\n");
								}

								writer.write("</table>\n");
							} else {// 写入成功信息
								writer.write("<br>");
								writer
										.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:green;font-size:14px\">存储过程&nbsp;&nbsp;&nbsp;"
												+ sql
												+ "&nbsp;&nbsp;&nbsp;执行成功!</span><br>\n");
							}
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getMessage(), e);// 写入异常信息
							writer.write("<br>");
							writer
									.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:green;font-size:14px\">存储过程&nbsp;&nbsp;&nbsp;"
											+ sql
											+ "&nbsp;&nbsp;&nbsp;执行失败，"
											+ e.getMessage() + "</span><br>\n");
						}
					}
				}
			} else {
				if (searchSql != null && !searchSql.equals("")) {
					try {
						objList = resultSetBySQLService
								.getListByProc(searchSql);// 执行存储过程
						if (objList != null && objList.size() > 0) {
							writer
									.write("<table style=\"width:100%;border:1px ;border-style: ridge;\" cellpadding=\"0\" cellspacing=\"0\">\n");
							for (int j = 0; j < objList.size(); j++) {// 遍历集合
								Object[] obj = objList.get(j);
								String color = "";
								if (j != 0 && j % 2 == 0)
									color = "#EBF3FA";
								else
									color = "white";
								writer.write("<tr>\n");
								for (int k = 0; k < obj.length; j++) {
									writer
											.write("<td style=\"background-color:"
													+ color
													+ ";padding:4px;font-size:13px;border:1px ;border-style: ridge;\">"
													+ obj[k] + "</td>\n");
								}
								writer.write("</tr>\n");
							}
							writer.write("</table>\n");
						} else {
							writer.write("<br>");
							writer
									.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:green;font-size:14px\">存储过程&nbsp;&nbsp;&nbsp;"
											+ searchSql
											+ "&nbsp;&nbsp;&nbsp;执行成功!</span><br>\n");
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage(), e);// 写入异常信息
						writer.write("<br>");
						writer
								.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:red;font-size:14px\">存储过程&nbsp;&nbsp;&nbsp;"
										+ searchSql
										+ "&nbsp;&nbsp;&nbsp;执行失败，"
										+ e.getMessage() + "</span><br>\n");
					}
				}
			}

		} else if (searchType.equals("createProc")) {
			try {
				isSuccess = resultSetBySQLService.execSQL(searchSql, conn);// 执行SQL
				writer.write("<br>");
				writer
						.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:green;font-size:14px\">存储过程创建成功!</span><br>\n");

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);// 写入失败信息
				writer.write("<br>");
				writer
						.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:red;font-size:14px\">存储过程创建失败，"
								+ e.getMessage() + "</span><br>\n");
			}
		}
		if (writer != null) {
			writer.close();
			writer = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
		return null;
	}

	/***
	 * 根据传入路径(filePath)查看选中的文件
	 * 
	 * @return
	 */
	public String catFile() {
		File file = new File(filePath);
		BufferedReader reader = null;
		PrintWriter writer = null;
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(ServletActionContext.HTTP_REQUEST);
		try {
			reader = new BufferedReader(new FileReader(file));
			writer = response.getWriter();
			String data = "";
			// 写入样式
			writer
					.write("<link href=\""
							+ request.getContextPath()
							+ "/css/index.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
			writer
					.write("<link href=\""
							+ request.getContextPath()
							+ "/css/table.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
			writer
					.write("<link href=\""
							+ request.getContextPath()
							+ "/css/globalStyle.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
			writer
					.write("<link href=\""
							+ request.getContextPath()
							+ "/js/themes/default/easyui.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
			writer
					.write("<link href=\""
							+ request.getContextPath()
							+ "/js/themes/icon.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
			writer
					.write("<link href=\""
							+ request.getContextPath()
							+ "/js/demo.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
			writer.write("<script type=\"text/javascript\" src=\""
					+ request.getContextPath()
					+ "/js/jquery-1.4.min.js\"></script>");
			writer.write("<script type=\"text/javascript\" src=\""
					+ request.getContextPath()
					+ "/js/jquery.easyui.min.js\"></script>");
			writer.write("<script type=\"text/javascript\" src=\""
					+ request.getContextPath()
					+ "/js/easyui-lang-zh_CN.js\"></script>");
			// 写入样式结束

			writer
					.write("<form action=\""
							+ request.getContextPath()
							+ "/getResultSetAction!getfilePath.action\" method=\"post\">\n"
							+ "<span style=\"font-size:14px;margin:14px 4px 14px 4px\">当前文件："
							+ filePath
							+ "</span><br><br>\n"
							+ "<a href=\"javascript:document.forms[0].action='getResultSetAction!getfilePath.action';document.forms[0].submit();\" class=\"easyui-linkbutton\"   plain=\"true\" iconCls=\"icon-back\" style=\"border:1px solid;margin-bottom:4px\">返回</a>&nbsp;"
							+ "<a href=\"javascript:document.forms[0].action='getResultSetAction!downLoadFile.action';document.forms[0].submit()\" class=\"easyui-linkbutton\" plain=\"true\" style=\"border:1px solid;margin-bottom:4px\">下载</a>\n"
							+ "<input type=\"hidden\" name=\"path\"	value=\""
							+ path
							+ "\"/>\n"
							+ "<input type=\"hidden\" name=\"filePath\" value=\""
							+ filePath
							+ "\"/>\n"
							+ "<input type=\"hidden\" name=\"fileName\" value=\""
							+ fileName + "\">\n"
							+ "<textarea style='width:99%;height:90%;'>\n");
			while ((data = reader.readLine()) != null) {
				writer.write(data);
				writer.write("\n");
			}
			writer
					.write("</textarea>"
							+ "<a href=\"javascript:document.forms[0].action='getResultSetAction!getfilePath.action';document.forms[0].submit();\" class=\"easyui-linkbutton\"  plain=\"true\" iconCls=\"icon-back\" style=\"border:1px solid;margin-top:4px\">返回</a>&nbsp;"
							+ "<a href=\"javascript:document.forms[0].action='getResultSetAction!downLoadFile.action';document.forms[0].submit()\" class=\"easyui-linkbutton\" plain=\"true\" style=\"border:1px solid;margin-top:4px\">下载</a>\n"
							+ "</form>");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} catch (IOException e) {

			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
					writer = null;
				}
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}

		return null;
	}

	/***
	 * 传入文件夹路径(path),以表格形式展示该路径文件夹下所有文件及文件夹
	 * 
	 * @return
	 */
	public String getfilePath() {
		PrintWriter writer = null;
		File file = new File(path);
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(ServletActionContext.HTTP_REQUEST);
		try {
			writer = response.getWriter();
			if (file.isDirectory()) {
				// 写入样式
				writer
						.write("<link href=\""
								+ request.getContextPath()
								+ "/css/index.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
				writer
						.write("<link href=\""
								+ request.getContextPath()
								+ "/css/table.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
				writer
						.write("<link href=\""
								+ request.getContextPath()
								+ "/css/globalStyle.css\" rel=\"stylesheet\" type=\"text/css\" />\n");

				writer
						.write("<link href=\""
								+ request.getContextPath()
								+ "/js/themes/default/easyui.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
				writer
						.write("<link href=\""
								+ request.getContextPath()
								+ "/js/themes/icon.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
				writer
						.write("<link href=\""
								+ request.getContextPath()
								+ "/js/demo.css\" rel=\"stylesheet\" type=\"text/css\" />\n");

				writer.write("<script type=\"text/javascript\" src=\""
						+ request.getContextPath()
						+ "/js/jquery-1.4.min.js\"></script>\n");
				writer.write("<script type=\"text/javascript\" src=\""
						+ request.getContextPath()
						+ "/js/jquery.easyui.min.js\"></script>\n");
				writer.write("<script type=\"text/javascript\" src=\""
						+ request.getContextPath()
						+ "/js/easyui-lang-zh_CN.js\"></script>\n");
				// 写入样式结束

				String levelPath = "";// 上一层路径
				if (path.endsWith(File.separator)) {
					String subPath = path.substring(0, path
							.lastIndexOf(File.separator));
					if (subPath.lastIndexOf(File.separator) >= 0)
						levelPath = subPath.substring(0, subPath
								.lastIndexOf(File.separator));
				} else {
					if (path.lastIndexOf(File.separator) >= 0)
						levelPath = path.substring(0, path
								.lastIndexOf(File.separator));
				}
				levelPath += File.separator;

				writer
						.write("<table  cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;border:1px solid\">\n<tr>\n"
								+ "<td style=\"border-bottom:1px solid;font-size:18px;padding-top:10px;padding-bottom:5px;padding-left:5px\">\n<img src=\"images/fe.gif\" style=\"border:none;vertical-align: middle;\"/>"
								+ "<input style=\"width:500px;border:none;font-size:16px;\" value=\""
								+ path
								+ "\" readonly=\"readonly\">"
								+ "\n</td>\n"
								+ "<td style=\"width:150px;border-bottom:1px solid;font-size:18px;padding-top:10px;padding-bottom:5px;\">"
								+ "<a href=\""
								+ request.getContextPath()
								+ "/getResultSetAction!getfilePath.action?path="
								+ levelPath
								+ "\"  class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-back\" style=\"border:1px solid;margin-bottom:4px\">上层目录</a></td></tr>");

				for (File f : file.listFiles()) {
					if (f.isFile()) {// 文件
						writer
								.write("<tr>\n<td style=\"border-bottom:1px solid;padding-left:25px;padding-top:4px\">"
										+ f.getName()
										+ "</td>"
										+ "<td style=\"border-bottom:1px solid;;padding-top:4px\">\n"
										+ "<form action=\""
										+ request.getContextPath()
										+ "/getResultSetAction!catFile.action\" method=\"post\" style=\"display:inline\" id=\""
										+ f.getName()
										+ "\">\n"
										+ "<input type=\"hidden\" name=\"filePath\" value=\""
										+ path
										+ f.getName()
										+ "\"/>\n"
										+ "<input type=\"hidden\" name=\"fileName\" value=\""
										+ f.getName()
										+ "\">"
										+ "<input type=\"hidden\" name=\"path\" value=\""
										+ path
										+ "\">"
										+ "<a href=\"javascript:document.getElementById('"
										+ f.getName()
										+ "').action='"
										+ request.getContextPath()
										+ "/getResultSetAction!catFile.action';document.getElementById('"
										+ f.getName()
										+ "').submit();\"  class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-search\" style=\"border:1px solid;margin-bottom:4px\">查看</a>\n"
										+ "<a href=\"javascript:document.getElementById('"
										+ f.getName()
										+ "').action='"
										+ request.getContextPath()
										+ "/getResultSetAction!downLoadFile.action';document.getElementById('"
										+ f.getName()
										+ "').submit();\" class=\"easyui-linkbutton\" plain=\"true\" style=\"border:1px solid;margin-bottom:4px\">下载</a>\n"
										+ "</form>\n" + "</td>\n</tr>\n");
					}
				}
				for (File f : file.listFiles()) {
					if (f.isDirectory()) {// 文件夹
						writer
								.write("<tr>\n<td colspan=\"2\" style=\"border-bottom:1px solid;padding-left:25px;padding-top:4px\">\n"
										+ "<form action=\"getResultSetAction!getfilePath.action\" method=\"post\" style=\"display:inline\">\n"
										+ "<img src=\"images/fc.gif\" style=\"border:none;vertical-align: middle;\" />\n"
										+ "<span onclick=\"this.parentNode.submit()\" style=\"cursor:hand;color:blue\">"
										+ f.getName()
										+ "</span>\n"
										+ "<input type=\"hidden\" name=\"path\" value=\""
										+ path
										+ f.getName()
										+ "\"></form></td>\n</tr>\n");
					}
				}
				writer.write("</table>\n");
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {

			if (writer != null) {
				writer.close();
				writer = null;
			}

		}
		return null;
	}

	/***
	 * 根据传入文件路径(filePath),下载该文件
	 * 
	 * @return
	 */
	public String downLoadFile() {
		HttpServletResponse response = null;
		try {
			response = (HttpServletResponse) ActionContext.getContext().get(
					ServletActionContext.HTTP_RESPONSE);
			// 下载
			response.reset();
			response.setContentType("application/octet-stream;charset=gb2312");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "gb2312"));
			response.setHeader("Accept-ranges", "bytes");

			FileInputStream inStream = new FileInputStream(filePath);
			int len;
			byte[] buffer = new byte[100];

			while ((len = inStream.read(buffer)) > 0) {
				response.getOutputStream().write(buffer, 0, len);
			}
			inStream.close();
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 查询数据库所有数据表，并创建JQUERY-EASY-UI所支持的JSON文件格式 页面已树形展示数据库所有数据表
	 * 
	 * @return
	 */
	public String findTableName() {
		try {
			objStrList = loadFIleInfoService.findAllTableNameByUser();
			// 生成表名JSON文件
			String treeData = createTreeJSON(objStrList);
			System.out.println("----生成的JSON文件为:" + treeData + "----");
			String filePath = Config.WEBROOTPATH + File.separator + "js"
					+ File.separator + "tablename" + ".json";
			File file = new File(filePath);
			if (file.exists())
				file.delete();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			writer.write(treeData);
			writer.flush();
			writer.close();
			System.out.println("----数据表JSON文件写入成功----");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

	/***
	 * 根据传入表名(tablename),查询该表所有数据
	 * 
	 * @return
	 */
	public String getListBySQL() {
		if (tablename == null || tablename.equals(""))
			return "OBJVIEW";
		String sql = "select * from " + tablename;
		try {
			objArrayList = resultSetBySQLService.getListBySQL(sql);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "OBJVIEW";
	}

	/***
	 * 根据数据集合，创建JQUERY-EASY-UI所支持的JSON文件格式
	 * 
	 * @param tableList
	 *            传入的数据集合
	 * @return
	 */
	private String createTreeJSON(List<String> tableList) {
		StringBuffer treeJSON = new StringBuffer("[");
		treeJSON.append("{\"id\":\"tablename\",");
		treeJSON.append("\"text\":" + "\"数据库表\"");
		treeJSON.append(",\"children\":[");
		for (int i = 0; i < tableList.size(); i++) {
			String s = tableList.get(i);
			treeJSON.append("{\"id\":\"" + s + "\",\"text\":\"" + s + "\"");

			treeJSON.append("}");

			if (i != tableList.size() - 1)
				treeJSON.append(",");
		}
		treeJSON.append("]}]");
		return treeJSON.toString();
	}

	/***
	 * 根据传入的表名，查询该表所有数据，根据该表数据动态生成INSERT语句
	 * 
	 * @return
	 */
	public String getInsertSQL() {
		if (tablename == null || tablename.equals(""))
			return "INSERTSQLVIEW";
		try {
			objStrList = resultSetBySQLService.getInsertSQL(tablename);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return "INSERTSQLVIEW";
	}

	/***
	 * 根据传入的表名(tablename),查询该表所有数据，动态生成该表INSERT语句并已"表名.sql"格式生成.sql文件 并下载该文件
	 * 
	 * @return
	 */
	public String downLoadInsertSQL() {
		HttpServletResponse response = null;
		PrintWriter writer = null;
		try {
			if (tablename == null || tablename.equals(""))
				return null;

			response = (HttpServletResponse) ActionContext.getContext().get(
					ServletActionContext.HTTP_RESPONSE);
			writer = response.getWriter();
			objStrList = resultSetBySQLService.getInsertSQL(tablename);
			if (objStrList == null || objStrList.size() == 0) {
				writer
						.write("<span style=\"margin-left:10px\">>></span>&nbsp;&nbsp;<span style=\"color:red;font-size:14px\">"
								+ tablename + "表无数据，无法生成SQL文件</span><br>\n");
			}
			String fileName = Config.WEBROOTPATH + File.separator + "js"
					+ File.separator + tablename + ".sql";
			File file = new File(fileName);
			if (file.exists())
				file.delete();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			for (String s : objStrList) {
				bw.write(s + "\n");
				bw.write("\n");
			}
			bw.close();
			// 下载
			response.reset();
			response.setContentType("application/octet-stream;charset=gb2312");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(tablename + ".sql", "gb2312"));
			response.setHeader("Accept-ranges", "bytes");

			FileInputStream inStream = new FileInputStream(fileName);
			int len;
			byte[] buffer = new byte[100];

			while ((len = inStream.read(buffer)) > 0) {
				response.getOutputStream().write(buffer, 0, len);
			}
			inStream.close();

			file.delete();
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用户执行页面传入的脚本命令
	 * 
	 * @return
	 */
	public String runShell() {
		HttpServletResponse response = null;
		PrintWriter out = null;
		HttpServletRequest request = null;
		InputStream in = null;
		BufferedReader br = null;
		try {
			response = (HttpServletResponse) ActionContext.getContext().get(
					ServletActionContext.HTTP_RESPONSE);
			request = (HttpServletRequest) ActionContext.getContext().get(
					ServletActionContext.HTTP_REQUEST);
			out = response.getWriter();
			// 获取页面传过来的执行命令。
			String shellCode = request.getParameter("shellCode").trim();
			// System.out.println("处理前"+shellCode);
			// String separator = System.getProperty("file.separator");
			// String path =
			// request.getSession().getServletContext().getRealPath(
			// "");
			// // 拼接完整命令
			// shellCode = path + "etlScript" + separator + "tmp" + separator
			// + shellCode;

			System.out.println("处理后" + shellCode);
			Process p = Runtime.getRuntime().exec(shellCode);
			in = p.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			String tmp = null;
			StringBuffer result = new StringBuffer();
			while ((tmp = br.readLine()) != null) {
				if (tmp != null) {
					result.append(tmp);
					result.append("\r\n");
				}
			}
			if (!result.equals("")) {
				// out.write("success<br/>");
				out.write(result.toString());
				System.out.println(result.toString());
			} else {
				out.write("命令已经执行");
				System.out.println("命令已经执行");
			}
			br.close();
			br = null;
			in.close();
			in = null;
		} catch (Exception e) {
			out.write(e.getMessage());
		} finally {
			try {
				if (br != null) {
					br.close();
					br = null;
				}
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;

	}
	/**
	 * 上传文件
	 * @return
	 */
	public String uploadFileNow(){
		if(uploadFileNow!=null && uploadFileNowFileName!=null 
				&& !"".equals(uploadFileNowFileName)){
			File outFile = null;
			BufferedOutputStream writer = null;
			BufferedInputStream reader = null;
			
			try {
				String path = Config.WEBROOTPATH+File.separator+uploadFileNowFileName;//拼接文件写入路径
				System.out.println("------开始上传文件...------");
				System.out.println("--文件名为："+uploadFileNowFileName+",上传的文件路径为："+path+"--");
				outFile = new File(path);
				if(outFile.exists())//文件存在，则先删除该文件
					outFile.delete();
				reader = new BufferedInputStream(new FileInputStream(uploadFileNow));
				writer = new BufferedOutputStream(new FileOutputStream(outFile));
				//String readerS = "";
				byte[] buffer =new byte[reader.available()];
				reader.read(buffer);
				writer.write(buffer);
				writer.flush();
				System.out.println("------文件上传成功------");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{//释放资源
				try {
					if(writer!=null){
						writer.close();
						writer = null;
					}
					if(reader!=null){
						reader.close();
						reader = null;
					}
					if(outFile!=null){
						outFile  = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
		return "uploadSuccess";
	}

	/***
	 * 所有变量 GETTER AND SETTER 语句
	 * 
	 * @return
	 */
	public String getSearchSql() {
		return searchSql;
	}

	public void setSearchSql(String searchSql) {
		this.searchSql = searchSql;
	}

	public IGetResultSetBySQLService getResultSetBySQLService() {
		return resultSetBySQLService;
	}

	public void setResultSetBySQLService(
			IGetResultSetBySQLService resultSetBySQLService) {
		this.resultSetBySQLService = resultSetBySQLService;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public List<Object[]> getObjList() {
		return objList;
	}

	public void setObjList(List<Object[]> objList) {
		this.objList = objList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<List<Object[]>> getObjArrayList() {
		return objArrayList;
	}

	public void setObjArrayList(List<List<Object[]>> objArrayList) {
		this.objArrayList = objArrayList;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if (path != null && !path.equals("") && !path.endsWith(File.separator))
			path += File.separator;
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getJspPath() {
		return jspPath;
	}

	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}

	public IETLLoadFileInfoService getLoadFIleInfoService() {
		return loadFIleInfoService;
	}

	public void setLoadFIleInfoService(
			IETLLoadFileInfoService loadFIleInfoService) {
		this.loadFIleInfoService = loadFIleInfoService;
	}

	public List<String> getObjStrList() {
		return objStrList;
	}

	public void setObjStrList(List<String> objStrList) {
		this.objStrList = objStrList;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

}
