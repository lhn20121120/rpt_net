package com.fitech.net.figure;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.text.NumberFormat;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;



public class WarnFigure 
{
   DefaultPieDataset piedata = null;
   //3D饼图
   PiePlot  plot = null; 
   String   title="";
   String   allWarnMessageColor="";
   String   outPrintInf="";
   JFreeChart  chart=null;
   public WarnFigure(String title,String WarnMessageColor,float cValue)
   {
	   float currentValue=Math.abs(cValue);
	   this.title=title;	   
	   this.allWarnMessageColor=WarnMessageColor;		   	   
	  	  
	   piedata = new DefaultPieDataset();	   	   
	   chart = ChartFactory.createPieChart3D(
	             title,  // chart title
	             piedata,                // data
	             false,                   // include legend
	             false,
	             false
	   );
	   plot = (PiePlot)chart.getPlot(); 
	  
	   if(allWarnMessageColor == null) allWarnMessageColor = "0.0,100.0,red";
	   String[] colors=allWarnMessageColor.split(Config.SPLIT_OUTER);
	   double  min=0;
	   int     current=-1;
	   int     sectionNum=0;
	   double  beginarc=0;
	   //设置初始角度
	  
       if(colors.length>0){
    	   try{    	    
    		   beginarc=Double.parseDouble(colors[0].split(Config.SPLIT_INNER)[0]);    	    
    		   //The initial default value is 90 degrees, which corresponds to 12 o'clock. A value of zero corresponds to 3 o'clock... this is the encoding used by Java's Arc2D class.     	    
    		   plot.setStartAngle(90-beginarc*3.6);    	    
    	   }catch(NumberFormatException e){}
       }
	   for(int i=0;i<colors.length;i++){	       
		   if(colors[i].length()>5){	       
			   String [] temp=colors[i].split(Config.SPLIT_INNER);
	           
	           double value1=Double.parseDouble(temp[0]);
	           double value2=Double.parseDouble(temp[1]);
	           min=value1;
	           if((currentValue>value1)&&(currentValue<=value2)){	             
	        	   //前半部分
	        	   if(currentValue-value1>0){	        	      
	        		   piedata.setValue(sectionNum+"."+temp[2],currentValue-value1); 	        	      
	        		   plot.setSectionPaint(sectionNum++,getColor(temp[2]));
	        	   }
	        	   //当前值
	        	   current=sectionNum;
	        	   piedata.setValue(sectionNum+".当前值"+cValue+"%",1); 
	        	   plot.setSectionPaint(sectionNum++,Color.white);
	        	   //后半部分
	        	   if(value2-currentValue<=0)	        		  
	        		   continue;
	        	   min=currentValue;
	           }	           	           
	           piedata.setValue(sectionNum+"."+temp[2],value2-min); 	          
	    	   plot.setSectionPaint(sectionNum++,getColor(temp[2]));	    	   
	    	   outPrintInf+= " <TR align=\"left\">"+
	    	                 "	<TH >"+            
	    	                 " <label style=\"background:"+temp[2]+"  \" >"+"&nbsp;&nbsp;</label>"+
	    	                 "("+value1+"%~"+value2+"%)"+										
	    				     " </TH>"+
	    				     " </TR>";
	    	  min=value2; 	       
		   }	   
	   }
	   //不在已定义区间
	   if(current==-1){//小于100		   
		   if(currentValue<=100&&currentValue>=0){
			   //前半部分		    
			   if(currentValue-min>0){
				   piedata.setValue(sectionNum+".red",currentValue-min); 
				   plot.setSectionPaint(sectionNum++,Color.red);
			   }
			   //当前值
			   current=sectionNum;
			   piedata.setValue(sectionNum+".当前值"+cValue+"%",1); 
			   plot.setSectionPaint(sectionNum++,Color.white); 
			   min=currentValue;
		   }		 
	   }
	   piedata.setValue(sectionNum+".red",100.0-min+beginarc); 
	   plot.setSectionPaint(sectionNum++,Color.red);
	   outPrintInf+= " <TR align=\"left\">"+
	   				 " <TH >"+            
	   				 " <label style=\"background:"+"red"+"  \" >"+"&nbsp;&nbsp;</label>"+
	   				 " 其它"+										
	   				 " </TH>"+
	   				 " <TR>";
	   if(currentValue>100)		  
		   outPrintInf+=  " <TR align=\"left\">"+
		 				 " <TH >"+            
		 				 " 超出100%异常"+		 													
		 				 " </TH>"+
		 				 " <TR>";
	   //抽取的那块（1维数据表的分类下标）以及抽取出来的距离（0.0～1.0），3D饼图无效	   
	   if(current!=-1) plot.setExplodePercent(current,0.2); 
	  
   }
   public JFreeChart initial(){	   
	     //JFreeChart chart = new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT, plot, false); 	     
	     //设置图片背景色
	     chart.setBackgroundPaint(java.awt.Color.white); 
	     //设置图片标题属性
	     Font font = new Font("黑体",Font.CENTER_BASELINE,20);
	     TextTitle _title = new TextTitle(title);
	     _title.setFont(font);
	     chart.setTitle(_title); 	       
	     //饼图	     
	     PiePlot plot = (PiePlot)chart.getPlot();
	     //在统计图片上建连接
	     plot.setURLGenerator(new StandardPieURLGenerator("lisk.jsp","section"));      
	     //Label的格式
	     NumberFormat nfnumber = NumberFormat.getNumberInstance(Locale.CHINA);
	     NumberFormat nfpercent = NumberFormat.getPercentInstance(Locale.CHINA);
	     //创建提示标签
	     //第一个参数格式
         //	     {0} = {1} 显示示例 甲方单位 = 4
         //	     {0} : {2} 显示示例 甲方单位 : 4 %
         //	     {0} : ({1},{2}) 显示示例 甲方单位 = (4,4%)
	     StandardPieItemLabelGenerator spilg = new StandardPieItemLabelGenerator("{0} 宽度 {1}",nfnumber,nfpercent);
	     plot.setToolTipGenerator(spilg);
	 
//	     创建标签
	     StandardPieSectionLabelGenerator splg = new StandardPieSectionLabelGenerator("{0}",nfnumber,nfpercent);
	     plot.setLabelGenerator(splg);
	     
//	     设定背景透明度（0-1.0之间）
	     plot.setBackgroundAlpha(0.6f);
//	     设定前景透明度（0-1.0之间）
	     plot.setForegroundAlpha(0.90f);
//	     指定 section 按顺时针方向依次显示，默认是顺时针方向
	     plot.setDirection(Rotation.CLOCKWISE);
//	     指定显示的饼图上圆形还是椭圆形
	     plot.setCircular(false);
	     
	     return chart;
	     
   }
   public   String getOutPrintInf()
   {
	   return outPrintInf;
   }
   private Paint getColor(String color){	       
	   if(color.equalsIgnoreCase("red"))    return Color.red;
	   else if(color.equalsIgnoreCase("green"))  return Color.GREEN; 
	   else if(color.equalsIgnoreCase("blue"))   return Color.blue;
	   else if(color.equalsIgnoreCase("yellow")) return Color.yellow;
	   else if(color.equalsIgnoreCase("black"))  return Color.black; 
	   else if(color.equalsIgnoreCase("white"))  return Color.white; 
	   return Color.red;
   }
}

