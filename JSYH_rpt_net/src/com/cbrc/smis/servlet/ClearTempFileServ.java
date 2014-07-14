package com.cbrc.smis.servlet;

import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cbrc.smis.util.ClearTempFileTask;
/**
*
* <p>����: ClearTempFileServ</p>
*
* <p>����: �����������ʱ,���ö�ʱ������ʱ
*       �ļ�������ʱ�ļ������Servlet�� </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
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
        //��web.xml�ļ��л�ȡ����ʱ�����ĺ�����
        String interval = config.getInitParameter("interval");      
        if(interval != null && !"".equals(interval))
        {
            //��ʱ��
            Timer timer = new Timer();
            //������ʱ�ļ�������ʱ�ļ�������ִ����
            ClearTempFileTask task = new ClearTempFileTask();
            try
            {
                //ʱ�����ַ���ת����longֵ
                long l = Long.parseLong(interval);
                //�趨���
                task.setInterval(l);
                //�趨�����ʼִ������,ÿ��l����ִ��һ������
                timer.schedule(task,5000,l);
                System.out.println("����ɾ����ʱ�ļ����ʱ��������óɹ���ÿ��"+ (l/(1000)) + "��ִ��һ�Σ�");
            }
            catch (Exception e)
            {
               System.out.println("����ɾ����ʱ�ļ����ʱ���������: interval =" + interval );
            }
        }
    }

}
