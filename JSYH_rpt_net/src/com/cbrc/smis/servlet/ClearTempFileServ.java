package com.cbrc.smis.servlet;

import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cbrc.smis.util.ClearTempFileTask;
/**
*
* <p>标题: ClearTempFileServ</p>
*
* <p>描述: 随服务器启动时,调用定时清理临时
*       文件夹下临时文件任务的Servlet类 </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2008-02-20
* @version 1.0
*/
public class ClearTempFileServ extends HttpServlet
{

    /**
     * Constructor of the object.
     */
    public ClearTempFileServ()
    {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy()
    {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

   
    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occure
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //从web.xml文件中获取清理时间间隔的毫秒数
        String interval = config.getInitParameter("interval");      
        if(interval != null && !"".equals(interval))
        {
            //定时器
            Timer timer = new Timer();
            //清理临时文件夹下临时文件的任务执行类
            ClearTempFileTask task = new ClearTempFileTask();
            try
            {
                //时间间隔字符串转换成long值
                long l = Long.parseLong(interval);
                //设定间隔
                task.setInterval(l);
                //设定五秒后开始执行任务,每隔l毫秒执行一次任务
                timer.schedule(task,5000,l);
                System.out.println("清理删除临时文件间隔时间参数设置成功：每隔"+ (l/(1000)) + "秒执行一次！");
            }
            catch (Exception e)
            {
               System.out.println("清理删除临时文件间隔时间参数有误: interval =" + interval );
            }
        }
    }

}
