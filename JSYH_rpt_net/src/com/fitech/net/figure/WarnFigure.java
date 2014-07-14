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
   //3D��ͼ
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
	   //���ó�ʼ�Ƕ�
	  
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
	        	   //ǰ�벿��
	        	   if(currentValue-value1>0){	        	      
	        		   piedata.setValue(sectionNum+"."+temp[2],currentValue-value1); 	        	      
	        		   plot.setSectionPaint(sectionNum++,getColor(temp[2]));
	        	   }
	        	   //��ǰֵ
	        	   current=sectionNum;
	        	   piedata.setValue(sectionNum+".��ǰֵ"+cValue+"%",1); 
	        	   plot.setSectionPaint(sectionNum++,Color.white);
	        	   //��벿��
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
	   //�����Ѷ�������
	   if(current==-1){//С��100		   
		   if(currentValue<=100&&currentValue>=0){
			   //ǰ�벿��		    
			   if(currentValue-min>0){
				   piedata.setValue(sectionNum+".red",currentValue-min); 
				   plot.setSectionPaint(sectionNum++,Color.red);
			   }
			   //��ǰֵ
			   current=sectionNum;
			   piedata.setValue(sectionNum+".��ǰֵ"+cValue+"%",1); 
			   plot.setSectionPaint(sectionNum++,Color.white); 
			   min=currentValue;
		   }		 
	   }
	   piedata.setValue(sectionNum+".red",100.0-min+beginarc); 
	   plot.setSectionPaint(sectionNum++,Color.red);
	   outPrintInf+= " <TR align=\"left\">"+
	   				 " <TH >"+            
	   				 " <label style=\"background:"+"red"+"  \" >"+"&nbsp;&nbsp;</label>"+
	   				 " ����"+										
	   				 " </TH>"+
	   				 " <TR>";
	   if(currentValue>100)		  
		   outPrintInf+=  " <TR align=\"left\">"+
		 				 " <TH >"+            
		 				 " ����100%�쳣"+		 													
		 				 " </TH>"+
		 				 " <TR>";
	   //��ȡ���ǿ飨1ά���ݱ�ķ����±꣩�Լ���ȡ�����ľ��루0.0��1.0����3D��ͼ��Ч	   
	   if(current!=-1) plot.setExplodePercent(current,0.2); 
	  
   }
   public JFreeChart initial(){	   
	     //JFreeChart chart = new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT, plot, false); 	     
	     //����ͼƬ����ɫ
	     chart.setBackgroundPaint(java.awt.Color.white); 
	     //����ͼƬ��������
	     Font font = new Font("����",Font.CENTER_BASELINE,20);
	     TextTitle _title = new TextTitle(title);
	     _title.setFont(font);
	     chart.setTitle(_title); 	       
	     //��ͼ	     
	     PiePlot plot = (PiePlot)chart.getPlot();
	     //��ͳ��ͼƬ�Ͻ�����
	     plot.setURLGenerator(new StandardPieURLGenerator("lisk.jsp","section"));      
	     //Label�ĸ�ʽ
	     NumberFormat nfnumber = NumberFormat.getNumberInstance(Locale.CHINA);
	     NumberFormat nfpercent = NumberFormat.getPercentInstance(Locale.CHINA);
	     //������ʾ��ǩ
	     //��һ��������ʽ
         //	     {0} = {1} ��ʾʾ�� �׷���λ = 4
         //	     {0} : {2} ��ʾʾ�� �׷���λ : 4 %
         //	     {0} : ({1},{2}) ��ʾʾ�� �׷���λ = (4,4%)
	     StandardPieItemLabelGenerator spilg = new StandardPieItemLabelGenerator("{0} ��� {1}",nfnumber,nfpercent);
	     plot.setToolTipGenerator(spilg);
	 
//	     ������ǩ
	     StandardPieSectionLabelGenerator splg = new StandardPieSectionLabelGenerator("{0}",nfnumber,nfpercent);
	     plot.setLabelGenerator(splg);
	     
//	     �趨����͸���ȣ�0-1.0֮�䣩
	     plot.setBackgroundAlpha(0.6f);
//	     �趨ǰ��͸���ȣ�0-1.0֮�䣩
	     plot.setForegroundAlpha(0.90f);
//	     ָ�� section ��˳ʱ�뷽��������ʾ��Ĭ����˳ʱ�뷽��
	     plot.setDirection(Rotation.CLOCKWISE);
//	     ָ����ʾ�ı�ͼ��Բ�λ�����Բ��
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

