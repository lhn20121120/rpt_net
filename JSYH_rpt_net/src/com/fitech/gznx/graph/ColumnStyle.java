package com.fitech.gznx.graph;

import org.dom4j.Element;

public class ColumnStyle {
	
	private GraphDataSource gs;
	
	private Element graph;
	
	private String vlabel = "��λ����Ԫ";

	private String isContailMe = "y";

	private int typeCount;

	/**********��״ͼ����ʽ��start��******************/

	/***ͼƬ�Ŀ��****/
	private int width = 450;

	/***ͼƬ�ĸ߶�***/
	private int height = 350;

	/***ͼƬ�ı���ɫ��R,G,B��***/
	private String bgColorRGB = "255,255,255";

	/**ͼ����������**/

	private String ttFont = "����";

	/**ͼ������ֵĴ�С*****/

	private int ttFontSize = 25;

	/***������ֵ���ɫ��R,G,B��***/
	private String ttColorRGB = "0,0,0";

	/***��״ͼ�ı���ɫ****/
	private String cBgColorRGB = "255,255,255";

	/***��״ͼ�ı߾�***/
	private String cInsets = "5, 10, 5, 50";

	/***��״ͼ�ĺ����������ɫ**/
	private String cRangeGridlineColorRGB = "0,0,0";

	/***��״ͼ�����ӵ�͸����****/
	private String cForegroundAlpha = "1.0";

	/***��״ͼ�ĺ������ɫ***/
	private String cDomainAxisColor = "0,0,0";

	/***��״ͼ�ĺ����Label����***/
	private String cDomainAxisLabelFont = "����";

	/***��״ͼ�ĺ����Label�ֵĴ�С***/
	private int cDomainAxisLabelFontSize = 12;

	/***��״ͼ�ĺ����Label�ֵ���ɫ***/
	private String cDomainAxisLabelFontColor = "0,0,0";

	/***��״ͼ�ĺ����TickLabel������***/
	private String cDomainAxisTickLabelFont = "����";

	/***��״ͼ�ĺ����TickLabel���ֵĴ�С**/
	private int cDomainAxisTickLabelFontSize = 12;

	/***��״ͼ�ĺ����TickLabel���ֵ���ɫ****/
	private String cDomainAxisTickLabelFontColor = "0,0,0";

	/***��״ͼ���������ɫ***/
	private String cRangeAxisColor = "0,0,0";

	/***��״ͼ�������Label�ֵ�����***/
	private String cRangeAxisLabelFont = "����";

	/**��״ͼ�������Label�ֵĴ�С***/
	private int cRangeAxisLabelFontSize = 12;

	/**��״ͼ�������Label�ֵ���ɫ**/
	private String cRangeAxisLabelFontColor = "0,0,0";

	/**��״ͼ�������TickLabel�ֵ�����****/
	private String cRangeAxisTickLabelFont = "����";

	/**��״ͼ�������TickLabel�ֵĴ�С****/
	private int cRangeAxisTickLabelFontSize = 12;

	/**��״ͼ�������TickLabel�ֵ���ɫ****/
	private String cRangeAxisTickLabelFontColor = "0,0,0";

	/**��״ͼ�ǲ���3D��****/
	private boolean isC3D = true;

	/***3D��״ͼ�ı���ǽ����ɫ***/
	private String cWallPaint = "244,241,236";

	/***��״ͼ�������**/
	private String cItemMargin = "0.0";

	/*** ����TickLabel���ֵĽǶ�**/
	private String cLabelPositions = "";
	
	
	
	public ColumnStyle(Element root, GraphDataSource gs) {

		this.graph = root;

		this.gs = gs;

		if (graph.attributeValue("type").equals(GraphFactory.PlanCOLUMN)) {

			PlanColumnGraph.resolveXml(this);

			GraphDataSource.setPieData(gs);

		}

		else if (graph.attributeValue("type").equals(GraphFactory.COLUMN)) {

			ColumnGraph.resolveXml(this);

			GraphDataSource.setColumnData(gs);
		}

    }

	

	public String getBgColorRGB() {
		return bgColorRGB;
	}

	public void setBgColorRGB(String bgColorRGB) {
		this.bgColorRGB = bgColorRGB;
	}

	public String getCBgColorRGB() {
		return cBgColorRGB;
	}

	public void setCBgColorRGB(String bgColorRGB) {
		cBgColorRGB = bgColorRGB;
	}

	public String getCDomainAxisColor() {
		return cDomainAxisColor;
	}

	public void setCDomainAxisColor(String domainAxisColor) {
		cDomainAxisColor = domainAxisColor;
	}

	public String getCDomainAxisLabelFont() {
		return cDomainAxisLabelFont;
	}

	public void setCDomainAxisLabelFont(String domainAxisLabelFont) {
		cDomainAxisLabelFont = domainAxisLabelFont;
	}

	public String getCDomainAxisLabelFontColor() {
		return cDomainAxisLabelFontColor;
	}

	public void setCDomainAxisLabelFontColor(String domainAxisLabelFontColor) {
		cDomainAxisLabelFontColor = domainAxisLabelFontColor;
	}

	public int getCDomainAxisLabelFontSize() {
		return cDomainAxisLabelFontSize;
	}

	public void setCDomainAxisLabelFontSize(int domainAxisLabelFontSize) {
		cDomainAxisLabelFontSize = domainAxisLabelFontSize;
	}

	public String getCDomainAxisTickLabelFont() {
		return cDomainAxisTickLabelFont;
	}

	public void setCDomainAxisTickLabelFont(String domainAxisTickLabelFont) {
		cDomainAxisTickLabelFont = domainAxisTickLabelFont;
	}

	public String getCDomainAxisTickLabelFontColor() {
		return cDomainAxisTickLabelFontColor;
	}

	public void setCDomainAxisTickLabelFontColor(String domainAxisTickLabelFontColor) {
		cDomainAxisTickLabelFontColor = domainAxisTickLabelFontColor;
	}

	public int getCDomainAxisTickLabelFontSize() {
		return cDomainAxisTickLabelFontSize;
	}

	public void setCDomainAxisTickLabelFontSize(int domainAxisTickLabelFontSize) {
		cDomainAxisTickLabelFontSize = domainAxisTickLabelFontSize;
	}

	public String getCForegroundAlpha() {
		return cForegroundAlpha;
	}

	public void setCForegroundAlpha(String foregroundAlpha) {
		cForegroundAlpha = foregroundAlpha;
	}

	public String getCInsets() {
		return cInsets;
	}

	public void setCInsets(String insets) {
		cInsets = insets;
	}

	public String getCItemMargin() {
		return cItemMargin;
	}

	public void setCItemMargin(String itemMargin) {
		cItemMargin = itemMargin;
	}

	public String getCLabelPositions() {
		return cLabelPositions;
	}

	public void setCLabelPositions(String labelPositions) {
		cLabelPositions = labelPositions;
	}

	public String getCRangeAxisColor() {
		return cRangeAxisColor;
	}

	public void setCRangeAxisColor(String rangeAxisColor) {
		cRangeAxisColor = rangeAxisColor;
	}

	public String getCRangeAxisLabelFont() {
		return cRangeAxisLabelFont;
	}

	public void setCRangeAxisLabelFont(String rangeAxisLabelFont) {
		cRangeAxisLabelFont = rangeAxisLabelFont;
	}

	public String getCRangeAxisLabelFontColor() {
		return cRangeAxisLabelFontColor;
	}

	public void setCRangeAxisLabelFontColor(String rangeAxisLabelFontColor) {
		cRangeAxisLabelFontColor = rangeAxisLabelFontColor;
	}

	public int getCRangeAxisLabelFontSize() {
		return cRangeAxisLabelFontSize;
	}

	public void setCRangeAxisLabelFontSize(int rangeAxisLabelFontSize) {
		cRangeAxisLabelFontSize = rangeAxisLabelFontSize;
	}

	public String getCRangeAxisTickLabelFont() {
		return cRangeAxisTickLabelFont;
	}

	public void setCRangeAxisTickLabelFont(String rangeAxisTickLabelFont) {
		cRangeAxisTickLabelFont = rangeAxisTickLabelFont;
	}

	public String getCRangeAxisTickLabelFontColor() {
		return cRangeAxisTickLabelFontColor;
	}

	public void setCRangeAxisTickLabelFontColor(String rangeAxisTickLabelFontColor) {
		cRangeAxisTickLabelFontColor = rangeAxisTickLabelFontColor;
	}

	public int getCRangeAxisTickLabelFontSize() {
		return cRangeAxisTickLabelFontSize;
	}

	public void setCRangeAxisTickLabelFontSize(int rangeAxisTickLabelFontSize) {
		cRangeAxisTickLabelFontSize = rangeAxisTickLabelFontSize;
	}

	public String getCRangeGridlineColorRGB() {
		return cRangeGridlineColorRGB;
	}

	public void setCRangeGridlineColorRGB(String rangeGridlineColorRGB) {
		cRangeGridlineColorRGB = rangeGridlineColorRGB;
	}

	public String getCWallPaint() {
		return cWallPaint;
	}

	public void setCWallPaint(String wallPaint) {
		cWallPaint = wallPaint;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isC3D() {
		return isC3D;
	}

	public void setC3D(boolean isC3D) {
		this.isC3D = isC3D;
	}

	public String getIsContailMe() {
		return isContailMe;
	}

	public void setIsContailMe(String isContailMe) {
		this.isContailMe = isContailMe;
	}

	public String getTtColorRGB() {
		return ttColorRGB;
	}

	public void setTtColorRGB(String ttColorRGB) {
		this.ttColorRGB = ttColorRGB;
	}

	public String getTtFont() {
		return ttFont;
	}

	public void setTtFont(String ttFont) {
		this.ttFont = ttFont;
	}

	public int getTtFontSize() {
		return ttFontSize;
	}

	public void setTtFontSize(int ttFontSize) {
		this.ttFontSize = ttFontSize;
	}

	public int getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}

	public String getVlabel() {
		return vlabel;
	}

	public void setVlabel(String vlabel) {
		this.vlabel = vlabel;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**********��״ͼ����ʽ��end��******************/

	public GraphDataSource getGs() {
		return gs;
	}

	public void setGs(GraphDataSource gs) {
		this.gs = gs;
	}
	
	public Element getGraph() {
		return graph;
	}

	public void setGraph(Element graph) {
		this.graph = graph;
	}
	
	

}
