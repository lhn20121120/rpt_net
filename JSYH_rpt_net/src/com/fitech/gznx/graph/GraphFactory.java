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
 * �������ڴ�����Ӧ��XML�ļ��е����е�ͼ��
 * @author jack
 *
 */
public class GraphFactory
{

	public static final String TABLE = "0"; //��

	public static final String COLUMN = "1"; //��״ͼ

	public static final String PIE = "2"; //��ͼ

	public static final String BREAKLINE = "3"; //����ͼ

	public static final String PlanCOLUMN = "4"; //ƽ����״ͼ

	public String graphFactoryString = ""; //ͼ������ƣ�Xml�ļ������ƣ�

	public String graphString = ""; //xml�ļ��е�id

	public String graphType = ""; //ͼ������

	public String graphName = ""; //ͼ������

	public List graphList = new ArrayList(); //ͼ�����������ͼƬ�����е�Ԫ����Element���ͣ�

	/**
	 * �����ʱ���ɵ�ͼƬ�ļ��еľ���·��
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
	 * ɾ��ָ���ļ��м��ļ����µ���������
	 * @param delFolder
	 * @return
	 */
	public boolean deleteFolder(File delFolder) { 
        //Ŀ¼�Ƿ���ɾ�� 
        boolean hasDeleted = true; 
        //�õ����ļ����µ������ļ��к��ļ����� 
        File[] allFiles = delFolder.listFiles(); 
        
        if(allFiles != null){
        	for (int i = 0; i < allFiles.length; i++) { 
                //Ϊtrueʱ���� 
                if (hasDeleted) { 
              	  if (allFiles[i].isDirectory()) { 
                        //���Ϊ�ļ���,��ݹ����ɾ���ļ��еķ��� 
              		  hasDeleted = deleteFolder(allFiles[i]); 
              	  }else if (allFiles[i].isFile()){ 
              		  try{
              			  if (!allFiles[i].delete()){  
              				  hasDeleted = false;   //ɾ��ʧ��,����false 
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
        	delFolder.delete();    //���ļ�����Ϊ���ļ���,ɾ����         
        }         
        return hasDeleted; 
    } 

	
	/**
	 * ���ɶ�Ӧ��XML�ļ��е����е�ͼ��
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

				//�������״ͼ
				if (graph.attributeValue("type").equals(COLUMN))
				{

					ColumnStyle cs = new ColumnStyle(graph, gs);

					ColumnGraph.createGraph(cs, GraphPath, BigGraphPath);

				}

				//����Ǳ�ͼ
				else if (graph.attributeValue("type").equals(PIE))
				{

					PieStyle ps = new PieStyle(graph, gs);

					PieGraph.createGraph(ps, GraphPath, BigGraphPath);

				}

				//���������ͼ				
				else if (graph.attributeValue("type").equals(BREAKLINE))
				{
					
					BreakLineStyle bls = new BreakLineStyle(graph, gs);
					if(gs.getNaviGraph().equals("liudongxingqingkuang"))
						bls.setFreq(Config.FREQ_DAY.intValue());
					else
						System.out.println("********"+gs.getNaviGraph());
					BreakLineGraph.createGraph(bls, GraphPath, BigGraphPath);

				}

				//�����ƽ����״ͼ			
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
