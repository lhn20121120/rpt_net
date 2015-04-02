package com.fitech.gznx.graph;

import org.dom4j.Element;

public class ColumnStyle {
	
	private GraphDataSource gs;
	
	private Element graph;
	
	private String vlabel = "单位：万元";

	private String isContailMe = "y";

	private int typeCount;

	/**********柱状图的样式（start）******************/

	/***图片的宽度****/
	private int width = 450;

	/***图片的高度***/
	private int height = 350;

	/***图片的背景色（R,G,B）***/
	private String bgColorRGB = "255,255,255";

	/**图像标题的字体**/

	private String ttFont = "黑体";

	/**图像标题字的大小*****/

	private int ttFontSize = 25;

	/***标题的字的颜色（R,G,B）***/
	private String ttColorRGB = "0,0,0";

	/***柱状图的背景色****/
	private String cBgColorRGB = "255,255,255";

	/***柱状图的边距***/
	private String cInsets = "5, 10, 5, 50";

	/***柱状图的横向网格的颜色**/
	private String cRangeGridlineColorRGB = "0,0,0";

	/***柱状图的柱子的透明度****/
	private String cForegroundAlpha = "1.0";

	/***柱状图的横轴的颜色***/
	private String cDomainAxisColor = "0,0,0";

	/***柱状图的横轴的Label字体***/
	private String cDomainAxisLabelFont = "宋体";

	/***柱状图的横轴的Label字的大小***/
	private int cDomainAxisLabelFontSize = 12;

	/***柱状图的横轴的Label字的颜色***/
	private String cDomainAxisLabelFontColor = "0,0,0";

	/***柱状图的横轴的TickLabel的字体***/
	private String cDomainAxisTickLabelFont = "宋体";

	/***柱状图的横轴的TickLabel的字的大小**/
	private int cDomainAxisTickLabelFontSize = 12;

	/***柱状图的横轴的TickLabel的字的颜色****/
	private String cDomainAxisTickLabelFontColor = "0,0,0";

	/***柱状图的纵轴的颜色***/
	private String cRangeAxisColor = "0,0,0";

	/***柱状图的纵轴的Label字的字体***/
	private String cRangeAxisLabelFont = "宋体";

	/**柱状图的纵轴的Label字的大小***/
	private int cRangeAxisLabelFontSize = 12;

	/**柱状图的纵轴的Label字的颜色**/
	private String cRangeAxisLabelFontColor = "0,0,0";

	/**柱状图的纵轴的TickLabel字的字体****/
	private String cRangeAxisTickLabelFont = "宋体";

	/**柱状图的纵轴的TickLabel字的大小****/
	private int cRangeAxisTickLabelFontSize = 12;

	/**柱状图的纵轴的TickLabel字的颜色****/
	private String cRangeAxisTickLabelFontColor = "0,0,0";

	/**柱状图是不是3D的****/
	private boolean isC3D = true;

	/***3D柱状图的背景墙的颜色***/
	private String cWallPaint = "244,241,236";

	/***柱状图的柱间距**/
	private String cItemMargin = "0.0";

	/*** 横轴TickLabel的字的角度**/
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

	/**********柱状图的样式（end）******************/

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
