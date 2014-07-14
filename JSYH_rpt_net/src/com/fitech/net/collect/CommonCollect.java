package com.fitech.net.collect;

import java.util.ArrayList;

import org.dom4j.Document;

import com.fitech.net.collect.util.CollectUtil;

/**
 * 
 * @author wng.wl 普通累加报表汇总类
 * 
 */
public class CommonCollect {

	private int startRow = 0;

	private int endRow = 0;

	private char startCol;

	private char endCol;

	/**
	 * 初始化所有参数
	 */
	public CommonCollect(int startRow, int endRow, char startCol, char endCol) {

		this.startRow = startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
	}

	/**
	 * 汇总累加报表
	 * 
	 * @return
	 */
	public Document start(ArrayList arrayList) {
		/** 需要汇总的Document */
		Document tDoc = CollectUtil.initDoc(arrayList);

		/** 汇总所有可以累加的单元格 */
		Document totalDoc = CollectUtil.collectUnits(tDoc, arrayList, String
				.valueOf(startCol), startRow, String.valueOf(endCol), endRow,
				null);

		return totalDoc;
	}
}
