package com.fitech.gznx.graph;

import org.dom4j.Element;

import com.fitech.gznx.common.Config;

public class BreakLineStyle {
	
	private GraphDataSource gs;
	
	private Element graph;
	
	/*********时序图的数据*******/
	private   String alabel = "时间";
	
	private   String vlabel = "单位：万元";
    
    private   int freq = Config.FREQ_MONTH.intValue();
	
	/**********时序图的样式（start）********************/
    
    /***图片的宽度****/
	private   int width = 450;
	
	/***图片的高度***/
	private   int height = 350;
	
	/***图片的背景色（R,G,B）***/
	private   String bgColorRGB = "255,255,255";
	
	/**图像标题的字体**/
	
	private   String ttFont = "黑体";
	
	/**图像标题字的大小*****/
	
	private   int ttFontSize = 25;
	
	/***标题的字的颜色（R,G,B）***/
	private   String ttColorRGB = "0,0,0";
	
	/***折线图的背景色***/
	private   String blBgColorRGB = "255,255,255";
	
	/***折线图的边距**/
	private   String blInsets = "5,5,5,5";
	
	/***折线图的纵向网格的颜色**/
	private   String blDomainGridlineColorRGB = "0,0,0";
	
	/***折线图的横向网格的颜色****/
	private   String blRangeGridlineColorRGB = "0,0,0";
	
	/***折线图的纵向间隔带的颜色***/
	private   String blDomainTickBandColorRGB = "";
	
	/***折线图的横向间隔带的颜色**/
	private   String blRangeTickBandColorRGB = "";
	
	/***折线图的折点是否可见***/
	private   boolean isBaseShapesVisible = true;
	
	/***折线图的折点数据是否可见***/
	private   boolean isItemLabelVisible = false;
	
	/***折线图的横轴的颜色**/
	private   String blDomainAxisColor = "0,0,0";
	
	/***折线图的横轴的Label字的字体***/
	private   String blDomainAxisLabelFont = "宋体";
	
	/***折线图的横轴的Label字的大小***/
	private   int  blDomainAxisLabelFontSize = 12;
	
	/***折线图的横轴的Label字的颜色***/
	private   String blDomainAxisLabelFontColor = "0,0,0";
	
	/***折线图的横轴的TickLabel字的字体***/
	private   String blDomainAxisTickLabelFont = "宋体";
	
	/***折线图的横轴的TickLabel字的大小***/
	private   int blDomainAxisTickLabelFontSize = 12;
	
	/***折线图的横轴的TickLabel字的颜色***/
	private   String blDomainAxisTickLabelFontColor = "0,0,0";
		
	/***折线图的纵轴的颜色***/
    private   String blRangeAxisColor = "0,0,0";
	
    /***折线图的纵轴的Label字的字体***/
	private   String blRangeAxisLabelFont = "宋体";
	
	/***折线图的纵轴的Label字的大小***/
	private   int  blRangeAxisLabelFontSize = 12;
	
	/***折线图的纵轴的Label字的颜色***/
	private   String blRangeAxisLabelFontColor = "0,0,0";
	
	/***折线图的纵轴的TickLabel字的字体***/
	private   String blRangeAxisTickLabelFont = "宋体";
	
	/***折线图的纵轴的TickLabel字的大小***/
	private   int blRangeAxisTickLabelFontSize = 12;
	
	/***折线图的纵轴的TickLabel字的颜色***/
	private   String blRangeAxisTickLabelFontColor = "0,0,0";
	
	
	public BreakLineStyle(Element root , GraphDataSource gs){
		
		this.graph = root;
		
		this.gs = gs;
		
		BreakLineGraph.resolveXml(this);
		
		GraphDataSource.setBreakLineData(gs);
		
	}
	

	public String getAlabel() {
		return alabel;
	}

	public void setAlabel(String alabel) {
		this.alabel = alabel;
	}

	public String getBgColorRGB() {
		return bgColorRGB;
	}

	public void setBgColorRGB(String bgColorRGB) {
		this.bgColorRGB = bgColorRGB;
	}

	public String getBlBgColorRGB() {
		return blBgColorRGB;
	}

	public void setBlBgColorRGB(String blBgColorRGB) {
		this.blBgColorRGB = blBgColorRGB;
	}

	public String getBlDomainAxisColor() {
		return blDomainAxisColor;
	}

	public void setBlDomainAxisColor(String blDomainAxisColor) {
		this.blDomainAxisColor = blDomainAxisColor;
	}

	public String getBlDomainAxisLabelFont() {
		return blDomainAxisLabelFont;
	}

	public void setBlDomainAxisLabelFont(String blDomainAxisLabelFont) {
		this.blDomainAxisLabelFont = blDomainAxisLabelFont;
	}

	public String getBlDomainAxisLabelFontColor() {
		return blDomainAxisLabelFontColor;
	}

	public void setBlDomainAxisLabelFontColor(String blDomainAxisLabelFontColor) {
		this.blDomainAxisLabelFontColor = blDomainAxisLabelFontColor;
	}

	public int getBlDomainAxisLabelFontSize() {
		return blDomainAxisLabelFontSize;
	}

	public void setBlDomainAxisLabelFontSize(int blDomainAxisLabelFontSize) {
		this.blDomainAxisLabelFontSize = blDomainAxisLabelFontSize;
	}

	public String getBlDomainAxisTickLabelFont() {
		return blDomainAxisTickLabelFont;
	}

	public void setBlDomainAxisTickLabelFont(String blDomainAxisTickLabelFont) {
		this.blDomainAxisTickLabelFont = blDomainAxisTickLabelFont;
	}

	public String getBlDomainAxisTickLabelFontColor() {
		return blDomainAxisTickLabelFontColor;
	}

	public void setBlDomainAxisTickLabelFontColor(
			String blDomainAxisTickLabelFontColor) {
		this.blDomainAxisTickLabelFontColor = blDomainAxisTickLabelFontColor;
	}

	public int getBlDomainAxisTickLabelFontSize() {
		return blDomainAxisTickLabelFontSize;
	}

	public void setBlDomainAxisTickLabelFontSize(int blDomainAxisTickLabelFontSize) {
		this.blDomainAxisTickLabelFontSize = blDomainAxisTickLabelFontSize;
	}

	public String getBlDomainGridlineColorRGB() {
		return blDomainGridlineColorRGB;
	}

	public void setBlDomainGridlineColorRGB(String blDomainGridlineColorRGB) {
		this.blDomainGridlineColorRGB = blDomainGridlineColorRGB;
	}

	public String getBlDomainTickBandColorRGB() {
		return blDomainTickBandColorRGB;
	}

	public void setBlDomainTickBandColorRGB(String blDomainTickBandColorRGB) {
		this.blDomainTickBandColorRGB = blDomainTickBandColorRGB;
	}

	public String getBlInsets() {
		return blInsets;
	}

	public void setBlInsets(String blInsets) {
		this.blInsets = blInsets;
	}

	public String getBlRangeAxisColor() {
		return blRangeAxisColor;
	}

	public void setBlRangeAxisColor(String blRangeAxisColor) {
		this.blRangeAxisColor = blRangeAxisColor;
	}

	public String getBlRangeAxisLabelFont() {
		return blRangeAxisLabelFont;
	}

	public void setBlRangeAxisLabelFont(String blRangeAxisLabelFont) {
		this.blRangeAxisLabelFont = blRangeAxisLabelFont;
	}

	public String getBlRangeAxisLabelFontColor() {
		return blRangeAxisLabelFontColor;
	}

	public void setBlRangeAxisLabelFontColor(String blRangeAxisLabelFontColor) {
		this.blRangeAxisLabelFontColor = blRangeAxisLabelFontColor;
	}

	public int getBlRangeAxisLabelFontSize() {
		return blRangeAxisLabelFontSize;
	}

	public void setBlRangeAxisLabelFontSize(int blRangeAxisLabelFontSize) {
		this.blRangeAxisLabelFontSize = blRangeAxisLabelFontSize;
	}

	public String getBlRangeAxisTickLabelFont() {
		return blRangeAxisTickLabelFont;
	}

	public void setBlRangeAxisTickLabelFont(String blRangeAxisTickLabelFont) {
		this.blRangeAxisTickLabelFont = blRangeAxisTickLabelFont;
	}

	public String getBlRangeAxisTickLabelFontColor() {
		return blRangeAxisTickLabelFontColor;
	}

	public void setBlRangeAxisTickLabelFontColor(
			String blRangeAxisTickLabelFontColor) {
		this.blRangeAxisTickLabelFontColor = blRangeAxisTickLabelFontColor;
	}

	public int getBlRangeAxisTickLabelFontSize() {
		return blRangeAxisTickLabelFontSize;
	}

	public void setBlRangeAxisTickLabelFontSize(int blRangeAxisTickLabelFontSize) {
		this.blRangeAxisTickLabelFontSize = blRangeAxisTickLabelFontSize;
	}

	public String getBlRangeGridlineColorRGB() {
		return blRangeGridlineColorRGB;
	}

	public void setBlRangeGridlineColorRGB(String blRangeGridlineColorRGB) {
		this.blRangeGridlineColorRGB = blRangeGridlineColorRGB;
	}

	public String getBlRangeTickBandColorRGB() {
		return blRangeTickBandColorRGB;
	}

	public void setBlRangeTickBandColorRGB(String blRangeTickBandColorRGB) {
		this.blRangeTickBandColorRGB = blRangeTickBandColorRGB;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isBaseShapesVisible() {
		return isBaseShapesVisible;
	}

	public void setBaseShapesVisible(boolean isBaseShapesVisible) {
		this.isBaseShapesVisible = isBaseShapesVisible;
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

	public Element getGraph() {
		return graph;
	}

	public void setGraph(Element graph) {
		this.graph = graph;
	}

	public GraphDataSource getGs() {
		return gs;
	}

	public void setGs(GraphDataSource gs) {
		this.gs = gs;
	}


	public boolean isItemLabelVisible()
	{
		return isItemLabelVisible;
	}


	public void setItemLabelVisible(boolean isItemLabelVisible)
	{
		this.isItemLabelVisible = isItemLabelVisible;
	}
	
	/**********时序图的样式（end）********************/
	

}
