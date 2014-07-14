package com.cbrc.smis.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import com.cbrc.smis.common.Config;

/**
*
* <p>标题: ClearTempFileTask</p>
*
* <p>描述: 定时清理临时文件夹下临时文件类 </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2008-02-20
* @version 1.0
*/
public class ClearTempFileTask extends TimerTask
{

    /**
     * 时间间隔
     */
    private long interval;
    
    /**
     * 得到时间间隔
     * @return
     */
    public long getInterval()
    {
        return interval;
    }

    /**
     * 设置时间间隔
     * @param interval
     */
    public void setInterval(long interval)
    {
        this.interval = interval;
    }

    /**
     * 清除临时文件夹下临时文件主运行程序
     */
    public void run()
    {
        
        String path     = Config.TEMP_DIR;             //临时文件存放目录
//System.out.println("path-->" + path);
        File dir        = new File(path);
        
        List fileLst    = new ArrayList();             //存放临时文件名的集合
        
        if(dir.isDirectory())                          //如果是文件夹
        {
            File file[] = dir.listFiles();             //获取文件夹下的文件
            if(file != null)
            {
                int length = file.length;
                for (int i = 0; i < length; i++)
                {                    
                    String name = file[i].getName();  //文件名称                   
                    if(name.indexOf(".xls") != -1)    //如果是excel文件
                    {
                                                       //将文件名中的".xls"替换
                        fileLst.add(name.replaceAll(".xls",""));
                    }                    
                }
            }           
        }
        clear(fileLst);
    }

    /**
     * 清楚临时文件
     * @param fileLst   临时文件加下excel文件名集合(去后缀".xls")
     */
    private void clear(List fileLst)
    {
        if(fileLst != null && !fileLst.isEmpty())
        {
            Iterator itr = fileLst.iterator();           //迭代文件名
           
            long l1  = System.currentTimeMillis();       //系统时间long 值
           
            List tempLst = new ArrayList();              //需要删除的excel文件的文件名集合
            while(itr.hasNext())
            {
                String name = (String) itr.next();           //文件名        
                try
                {
                    //临时文件夹下的文件名是由系统时间的long 值生成,在这里再转换成long值
                    Long l2 = new Long(name);
                    //判断当前系统时间long值和文件名转换的long值是否大于设定的时间间隔
                    //大于则放入temoLst集合中
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
            //删除可删除的临时文件加下的临时文件
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
