package com.cbrc.smis.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import com.cbrc.smis.common.Config;

/**
*
* <p>����: ClearTempFileTask</p>
*
* <p>����: ��ʱ������ʱ�ļ�������ʱ�ļ��� </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2008-02-20
* @version 1.0
*/
public class ClearTempFileTask extends TimerTask
{

    /**
     * ʱ����
     */
    private long interval;
    
    /**
     * �õ�ʱ����
     * @return
     */
    public long getInterval()
    {
        return interval;
    }

    /**
     * ����ʱ����
     * @param interval
     */
    public void setInterval(long interval)
    {
        this.interval = interval;
    }

    /**
     * �����ʱ�ļ�������ʱ�ļ������г���
     */
    public void run()
    {
        
        String path     = Config.TEMP_DIR;             //��ʱ�ļ����Ŀ¼
//System.out.println("path-->" + path);
        File dir        = new File(path);
        
        List fileLst    = new ArrayList();             //�����ʱ�ļ����ļ���
        
        if(dir.isDirectory())                          //������ļ���
        {
            File file[] = dir.listFiles();             //��ȡ�ļ����µ��ļ�
            if(file != null)
            {
                int length = file.length;
                for (int i = 0; i < length; i++)
                {                    
                    String name = file[i].getName();  //�ļ�����                   
                    if(name.indexOf(".xls") != -1)    //�����excel�ļ�
                    {
                                                       //���ļ����е�".xls"�滻
                        fileLst.add(name.replaceAll(".xls",""));
                    }                    
                }
            }           
        }
        clear(fileLst);
    }

    /**
     * �����ʱ�ļ�
     * @param fileLst   ��ʱ�ļ�����excel�ļ�������(ȥ��׺".xls")
     */
    private void clear(List fileLst)
    {
        if(fileLst != null && !fileLst.isEmpty())
        {
            Iterator itr = fileLst.iterator();           //�����ļ���
           
            long l1  = System.currentTimeMillis();       //ϵͳʱ��long ֵ
           
            List tempLst = new ArrayList();              //��Ҫɾ����excel�ļ����ļ�������
            while(itr.hasNext())
            {
                String name = (String) itr.next();           //�ļ���        
                try
                {
                    //��ʱ�ļ����µ��ļ�������ϵͳʱ���long ֵ����,��������ת����longֵ
                    Long l2 = new Long(name);
                    //�жϵ�ǰϵͳʱ��longֵ���ļ���ת����longֵ�Ƿ�����趨��ʱ����
                    //���������temoLst������
                    if((l1 - l2.longValue()) > interval)
                    {                        
                        tempLst.add(name);
                    }
                }
                catch (Exception e)
                {
                   continue;
                }
            }
            //ɾ����ɾ������ʱ�ļ����µ���ʱ�ļ�
            if(!tempLst.isEmpty())
            {
                Iterator iter = tempLst.iterator();
                while(iter.hasNext())
                {
                    File f = new File(Config.TEMP_DIR + (String)iter.next() + ".xls");
                    if(f.exists())
                        f.delete();
                }
            }
        }
    }
}
