package com.fitech.gznx.graph;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public class PieStyle {
	
	private GraphDataSource gs;
	
	private Element graph;
	
/**********饼图的样式（start）*******************/
	
	private   List sectionColorList = new ArrayList();
	
	private   String unit = "万元";
	
	/***图片的宽度****/
	private   int width = 450;
	
	/***图片的高度***/
	private   int height = 500;
	
	/***图片的背景色（R,G,B）***/
	private   String bgColorRGB = "255,255,255";
	
	/**图像标题的字体**/
	
	private   String ttFont = "黑体";
	
	/**图像标题字的大小*****/
	
	private   int ttFontSize = 25;
	
	/***标题的字的颜色（R,G,B）***/
	private   String ttColorRGB = "0,0,0";
	
	/***是否3D***/
	private   boolean is3DPie =  true;
	
	/***饼图的背景色(R,G,B)**/
	private   String pieBgColorRGB = "255,255,255";
	
	/***饼图标签的背景色(R,G,B)***/
	private   String pieLebleBgColorRGB = "255,255,255";
	
	/***饼图连线的颜色（R,G,B）***/
	private   String pieLebleLinkColorRGB = "255,255,255";
	
	/**饼图的标签的字体**/
	private   String pieLebleFont = "宋体";
	
	/**饼图的标签字的大小**/
	private   int pieLebleFontSize = 10;
	
	/**饼图的标签字的显示模式**/
	private   String pieLebleGenerator = "{0}: ({1}M, {2})";
	
	/***饼图的标签字的颜色(R,G,B)****/
	private   String pieLebleFontColorRGB = "0,0,0";
	
	/***饼图的标签阴影的颜色(R,G,B)***/
	private   String pieLebleShadowColorRGB = "255,255,255";
	
	/***饼图的图标的显示模式***/
	private   String pieLegendLabelGenerator = "{0}: ({1}M)";
	
	/***饼图的上左下右的边距**/
	private   String pieRange = "20,20,20,20";
	
	/***饼图是否是正圆***/
	private   boolean isCircular = false;
	
	/***3D饼图的高度***/
	private   String piedept = "0.2";
	
	/***3D饼图的透明度**/
	private   String pieForegroundAlpha = "0.1";
	
	/***起始的度数*****/
	private   String pieStartAngle = "30";
	
	/***图的边线的颜色****/
	private   String pieOutlineColorRGB = "246,248,247";
	
	public PieStyle(Element root , GraphDataSource gs){
		
		this.graph = root;
		
		this.gs = gs;
		
		PieGraph.resolveXml(this);
		
		GraphDataSource.setPieData(gs);
		
	}

	public String getBgColorRGB() {
		return bgColorRGB;
	}

	public void setBgColorRGB(String bgColorRGB) {
		this.bgColorRGB = bgColorRGB;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isIs3DPie() {
		return is3DPie;
	}

	public void setIs3DPie(boolean is3DPie) {
		this.is3DPie = is3DPie;
	}

	public boolean isCircular() {
		return isCircular;
	}

	public void setCircular(boolean isCircular) {
		this.isCircular = isCircular;
	}

	public String getPieBgColorRGB() {
		return pieBgColorRGB;
	}

	public void setPieBgColorRGB(String pieBgColorRGB) {
		this.pieBgColorRGB = pieBgColorRGB;
	}

	public String getPiedept() {
		return piedept;
	}

	public void setPiedept(String piedept) {
		this.piedept = piedept;
	}

	public String getPieForegroundAlpha() {
		return pieForegroundAlpha;
	}

	public void setPieForegroundAlpha(String pieForegroundAlpha) {
		this.pieForegroundAlpha = pieForegroundAlpha;
	}

	public String getPieLebleBgColorRGB() {
		return pieLebleBgColorRGB;
	}

	public void setPieLebleBgColorRGB(String pieLebleBgColorRGB) {
		this.pieLebleBgColorRGB = pieLebleBgColorRGB;
	}

	public String getPieLebleFont() {
		return pieLebleFont;
	}

	public void setPieLebleFont(String pieLebleFont) {
		this.pieLebleFont = pieLebleFont;
	}

	public String getPieLebleFontColorRGB() {
		return pieLebleFontColorRGB;
	}

	public void setPieLebleFontColorRGB(String pieLebleFontColorRGB) {
		this.pieLebleFontColorRGB = pieLebleFontColorRGB;
	}

	public int getPieLebleFontSize() {
		return pieLebleFontSize;
	}

	public void setPieLebleFontSize(int pieLebleFontSize) {
		this.pieLebleFontSize = pieLebleFontSize;
	}

	public String getPieLebleGenerator() {
		return pieLebleGenerator;
	}

	public void setPieLebleGenerator(String pieLebleGenerator) {
		this.pieLebleGenerator = pieLebleGenerator;
	}

	public String getPieLebleLinkColorRGB() {
		return pieLebleLinkColorRGB;
	}

	public void setPieLebleLinkColorRGB(String pieLebleLinkColorRGB) {
		this.pieLebleLinkColorRGB = pieLebleLinkColorRGB;
	}

	public String getPieLebleShadowColorRGB() {
		return pieLebleShadowColorRGB;
	}

	public void setPieLebleShadowColorRGB(String pieLebleShadowColorRGB) {
		this.pieLebleShadowColorRGB = pieLebleShadowColorRGB;
	}

	public String getPieLegendLabelGenerator() {
		return pieLegendLabelGenerator;
	}

	public void setPieLegendLabelGenerator(String pieLegendLabelGenerator) {
		this.pieLegendLabelGenerator = pieLegendLabelGenerator;
	}

	public String getPieOutlineColorRGB() {
		return pieOutlineColorRGB;
	}

	public void setPieOutlineColorRGB(String pieOutlineColorRGB) {
		this.pieOutlineColorRGB = pieOutlineColorRGB;
	}

	public String getPieRange() {
		return pieRange;
	}

	public void setPieRange(String pieRange) {
		this.pieRange = pieRange;
	}

	public String getPieStartAngle() {
		return pieStartAngle;
	}

	public void setPieStartAngle(String pieStartAngle) {
		this.pieStartAngle = pieStartAngle;
	}

	public List getSectionColorList() {
		return sectionColorList;
	}

	public void setSectionColorList(List sectionColorList) {
		this.sectionColorList = sectionColorList;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

}
