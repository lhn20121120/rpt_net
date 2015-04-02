package com.fitech.gznx.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fitech.gznx.common.Config;
import com.fitech.gznx.security.OperatorLead;

/**
 * 该类用于创建对应的XML文件中的所有的图形
 * @author jack
 *
 */
public class GraphFactory
{

	public static final String TABLE = "0"; //表

	public static final String COLUMN = "1"; //柱状图

	public static final String PIE = "2"; //饼图

	public static final String BREAKLINE = "3"; //折线图

	public static final String PlanCOLUMN = "4"; //平面柱状图

	public String graphFactoryString = ""; //图库的名称（Xml文件的名称）

	public String graphString = ""; //xml文件中的id

	public String graphType = ""; //图的类型

	public String graphName = ""; //图的名称

	public List graphList = new ArrayList(); //图库里面包含的图片（其中的元素是Element类型）

	/**
	 * 存放临时生成的图片文件夹的绝对路径
	 */
	public String TEMPFILE = null;
	
	public GraphFactory(GraphDataSource gs)
	{

		this.graphFactoryString = gs.getNaviGraph();
		this.TEMPFILE = gs.getContextP() + File.separator + "temp" +File.separator +  "leading" + Config.FILESEPARATOR;
		String contextPath = gs.getContextP()+File.separator + "leadingXml";
		String xmlPath = contextPath+ File.separator + this.graphFactoryString + ".xml";

		SAXReader saxReader = new SAXReader();

		Document document = null;

		try 
		{
			
			File outfile = new File(TEMPFILE);
		    		
    		if(outfile.exists())
    			deleteFolder(outfile);
    		outfile.mkdir();
			
			document = saxReader.read(new File(xmlPath));

			Element root = (Element) document.selectNodes("fitechGraph").get(0);
			
			this.graphList = root.selectNodes("graph");

			//createGraph(operator , this , gs);

		}
		catch (Exception e)
		{

			e.printStackTrace();

		}

	}
	
	/**
	 * 删除指定文件夹及文件夹下的所以内容
	 * @param delFolder
	 * @return
	 */
	public boolean deleteFolder(File delFolder) { 
        //目录是否已删除 
        boolean hasDeleted = true; 
        //得到该文件夹下的所有文件夹和文件数组 
        File[] allFiles = delFolder.listFiles(); 
        
        if(allFiles != null){
        	for (int i = 0; i < allFiles.length; i++) { 
                //为true时操作 
                if (hasDeleted) { 
              	  if (allFiles[i].isDirectory()) { 
                        //如果为文件夹,则递归调用删除文件夹的方法 
              		  hasDeleted = deleteFolder(allFiles[i]); 
              	  }else if (allFiles[i].isFile()){ 
              		  try{
              			  if (!allFiles[i].delete()){  
              				  hasDeleted = false;   //删除失败,返回false 
              			  } 
              		  }catch (Exception e){ 
              			  hasDeleted = false; 
              		  } 
              	  } 
                }else{
              	  break;
                } 
           } 
        }         
        if (hasDeleted) {          
        	delFolder.delete();    //该文件夹已为空文件夹,删除它         
        }         
        return hasDeleted; 
    } 

	
	/**
	 * 生成对应的XML文件中的所有的图形
	 * @param operator
	 */
	public void createGraph(OperatorLead operator, GraphDataSource gs)
	{

		String sessionId = operator.getSessionId();

		if (getGraphList() != null && getGraphList().size() != 0)
		{

			List graphs = getGraphList();

			for (int i = 0; i < graphs.size(); i++)
			{

				Element graph = (Element) graphs.get(i);

				String GraphPath = TEMPFILE + sessionId + "_" + graph.attributeValue("id") + ".jpg";

				String BigGraphPath = TEMPFILE + sessionId + "_" + graph.attributeValue("id") + "Big.jpg";

				if (!operator.getTempFileList().contains(GraphPath))
				{

					operator.addTempFile(GraphPath);

					operator.addTempFile(BigGraphPath);

				}

				//如果是柱状图
				if (graph.attributeValue("type").equals(COLUMN))
				{

					ColumnStyle cs = new ColumnStyle(graph, gs);

					ColumnGraph.createGraph(cs, GraphPath, BigGraphPath);

				}

				//如果是饼图
				else if (graph.attributeValue("type").equals(PIE))
				{

					PieStyle ps = new PieStyle(graph, gs);

					PieGraph.createGraph(ps, GraphPath, BigGraphPath);

				}

				//如果是折线图				
				else if (graph.attributeValue("type").equals(BREAKLINE))
				{
					
					BreakLineStyle bls = new BreakLineStyle(graph, gs);
					if(gs.getNaviGraph().equals("liudongxingqingkuang"))
						bls.setFreq(Config.FREQ_DAY.intValue());
					else
						System.out.println("********"+gs.getNaviGraph());
					BreakLineGraph.createGraph(bls, GraphPath, BigGraphPath);

				}

				//如果是平面柱状图			
				else if (graph.attributeValue("type").equals(PlanCOLUMN))
				{

					ColumnStyle cs = new ColumnStyle(graph, gs);

					PlanColumnGraph.createGraph(cs, GraphPath, BigGraphPath);

				}

			}

		}

	}

	public String getGraphFactoryString()
	{
		return graphFactoryString;
	}

	public void setGraphFactoryString(String graphFactoryString)
	{
		this.graphFactoryString = graphFactoryString;
	}

	public List getGraphList()
	{
		return graphList;
	}

	public void setGraphList(List graphList)
	{
		this.graphList = graphList;
	}

	public String getGraphName()
	{
		return graphName;
	}

	public void setGraphName(String graphName)
	{
		this.graphName = graphName;
	}

	public String getGraphString()
	{
		return graphString;
	}

	public void setGraphString(String graphString)
	{
		this.graphString = graphString;
	}

	public String getGraphType()
	{
		return graphType;
	}

	public void setGraphType(String graphType)
	{
		this.graphType = graphType;
	}

}
