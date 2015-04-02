package com.cbrc.smis.common;

import java.io.Serializable;

/**
 * 
 * @author chenbing
 * 
 * 该类用于分页操作 该类中的属性用于记录用户访问的页面状态 同时该类提供"首页","前一页","后一页","尾页"和"第x页"的一些页面操作的方法
 */
public class ApartPageState implements Serializable {

	/**
	 * 为了便于Struts标签控制并符合常理这里,下面这两句将定义两个整形常量
	 */
	private final int TRUE = 1;

	private final int FLASE = 0;

	/**
	 * 代表访问首页的字符串常量
	 */
	private final String FIRSTPAGE = "t";

	/**
	 * 代表访问前一页的字符串常量
	 */
	private final String FRONTPAGE = "f";

	/**
	 * 代表访问下一页的字符串常量
	 */
	private final String NEXTPAGE = "n";

	/**
	 * 代表访问尾页的字符串常量
	 */
	private final String ENDPAGE = "e";

	/**
	 * 代表访问绝对页的字符串常量
	 */
	private final String ABSOLUTEPAGE = "a";

	/**
	 * 显示记录的起点
	 */
	private int begin;

	/**
	 * 每页显示的记录数,该属性与"begin"属性相配合使用
	 */
	private int limit;

	/**
	 * 记录的总数量
	 */
	private int recordCount;

	/**
	 * 显示记录的总页数
	 */
	private int pageCount;

	/**
	 * 正在访问的页面号
	 */
	private int pageVisiting;

	/**
	 * 是否是首页
	 */
	private int isFirstPage;

	/**
	 * 是否是尾页
	 */
	private int isEndPage;

	/**
	 * 该构造函数对所有的属性进行初始化操作
	 * 
	 * @param recordCount
	 *            记录总数
	 * @param limit
	 *            每页显示的记录数
	 */
	public ApartPageState(int recordCount, int limit) {

		super();

		this.begin = 0; // 初始化显示起点

		this.limit = limit; // 初始化显示的长度

		this.recordCount = recordCount; // 初始化总记录数

		int m = (this.recordCount) / (this.limit);

		int leavings = (this.recordCount) % (this.limit); // 得到余数

		if (leavings != 0)

			m++;

		this.pageCount = m; // 初始化总页数

		this.pageVisiting = 1; // 初始化正在访问的页号

		this.conductPageState();

	}

	/**
	 * 该方法用于处理isFirstPage,isEndPage两个属性
	 * 
	 */
	public void conductPageState() {

		if (this.recordCount != 0) {

			if ((this.pageVisiting) == 1)

				this.isFirstPage = this.TRUE;

			else

				this.isFirstPage = this.FLASE;

			if ((this.pageVisiting) == (this.pageCount))

				this.isEndPage = this.TRUE;

			else

				this.isEndPage = this.FLASE;

		} else {     //如果查询记录为零则执行下面的语句

			this.isFirstPage = this.TRUE;

			this.isEndPage = this.TRUE;
		}

	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getIsEndPage() {
		return isEndPage;
	}

	public void setIsEndPage(int isEndPage) {
		this.isEndPage = isEndPage;
	}

	public int getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(int isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageVisiting() {
		return pageVisiting;
	}

	public void setPageVisiting(int pageVisiting) {
		this.pageVisiting = pageVisiting;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	/**
	 * 该方法用于处理访问首页时的操作
	 * 
	 */
	public void visitFirstPage() {

		this.pageVisiting = 1;

		this.begin = 0;

		this.conductPageState();

	}

	/**
	 * 该方法用于处理访问尾页时的操作
	 * 
	 */
	public void visitEndPage() {

		this.pageVisiting = this.pageCount;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();

	}

	/**
	 * 该方法用于处理访问下一页时的操作
	 * 
	 */
	public void visitNextPage() {

		this.pageVisiting = this.pageVisiting + 1;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();
	}

	/**
	 * 该方法用于处理访问前一页时的操作
	 * 
	 */
	public void visitFrontPage() {

		this.pageVisiting = this.pageVisiting - 1;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();
	}

	/**
	 * 该方法用于处理访问绝对页面时的操作,绝对页面是从第一页开始显示的
	 * 
	 * @param pageNumber
	 */
	public void visitAbsolutePage(int pageNumber) {

		if ((pageNumber < 1) || (pageNumber > (this.pageCount))){
			if(pageNumber < 1)
				this.pageVisiting = 1;        // 如果输入的页码小于1这个页码值的范围则让其等于首页
			else
				this.pageVisiting = this.pageCount; // 如果输入的页码大于这个页码值的范围则让其等于总页数
		}
		else

			this.pageVisiting = pageNumber;

		this.begin = ((this.pageVisiting - 1) * (this.limit));

		this.conductPageState();

	}

	/**
	 * 根据输入的分页类型来统一处理分页操作 注意该方法用于判断首页,前一页,后一页,尾页,绝对页的字符串分别是t,f,n,e,a和数字
	 * 
	 * @param apartType
	 *            type:String 形如t,f,n,e,a和数字的首页,前一页,后一页,尾页,绝对页的字符串
	 * @throws Exception
	 */
	public void conduct(String apartType) throws Exception {

		if (apartType.equals(this.FRONTPAGE)) {

			this.visitFrontPage();

			return;

		}

		if (apartType.equals(this.NEXTPAGE)) {

			this.visitNextPage();

			return;

		}

		if (apartType.equals(this.FIRSTPAGE)) {

			this.visitFirstPage();

			return;

		}

		if (apartType.equals(this.ENDPAGE)) {

			this.visitEndPage();

			return;

		}

		int pageNumber;

		try {

			pageNumber = Integer.parseInt(apartType);

		} catch (Exception e) {

			pageNumber = 1;
		}

		this.visitAbsolutePage(pageNumber);
	}
}
