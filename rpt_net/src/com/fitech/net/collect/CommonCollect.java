package com.fitech.net.collect;

import java.util.ArrayList;

import org.dom4j.Document;

import com.fitech.net.collect.util.CollectUtil;

/**
 * 
 * @author wng.wl ��ͨ�ۼӱ��������
 * 
 */
public class CommonCollect {

	private int startRow = 0;

	private int endRow = 0;

	private char startCol;

	private char endCol;

	/**
	 * ��ʼ�����в���
	 */
	public CommonCollect(int startRow, int endRow, char startCol, char endCol) {

		this.startRow = startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
	}

	/**
	 * �����ۼӱ���
	 * 
	 * @return
	 */
	public Document start(ArrayList arrayList) {
		/** ��Ҫ���ܵ�Document */
		Document tDoc = CollectUtil.initDoc(arrayList);

		/** �������п����ۼӵĵ�Ԫ�� */
		Document totalDoc = CollectUtil.collectUnits(tDoc, arrayList, String
				.valueOf(startCol), startRow, String.valueOf(endCol), endRow,
				null);

		return totalDoc;
	}
}
