package com.cbrc.smis.util;

import java.io.File;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;

import DBstep.iMsgServer2000;

public class WebOffice {
    private int mFileSize;
    private byte[] mFileBody;
    private String mFileName;
    private String mFileType;
    private String mFileDate;
    private String mFileID;
    private String mRecordID;
    private String mOption;
    private String mFullPath;
    private String fFilePath;
    private String mSubject;
    private String mTemplate;

    private String mFilePath;

    private String mUserName;

    private iMsgServer2000 MsgObj;
    

   

    /**
     * ���ܻ�����  �����ĵ������ĵ����ݱ�����mFileBody��Ա���д��
     * @return
     */
     private boolean LoadFile_FilePath()
     {
      System.out.println("private boolean LoadFile_FilePath()");
      boolean mResult=false;
      if ( mRecordID.equalsIgnoreCase(""))
      {
         mRecordID="0"; //���Ϊ��
      }    
        try
        {
            mFullPath = Config.TEMP_DIR + mFileName;
            mResult=MsgObj.MsgFileLoad(mFullPath);  //��Ŀ¼������ļ�
System.out.println("mFullPath -- > " +mFullPath);            
            
        }
        catch(Exception e)
        {
          System.out.println(e.toString());
          mResult=false;
        }
     
      return (mResult);
     }

     /**
      * ���ܻ����ã������ĵ�������ĵ����ڣ��򸲸ǣ������ڣ������
      * @return
      */
     private boolean SaveFile_FilePath()
    {
        System.out.println("private  boolean SaveFile_FilePath()");
        boolean mResult = false;

        if (mRecordID != null && !"".equals(mRecordID))
        {
            ReportInForm reportIn = StrutsReportInDelegate
                    .getReportIn(new Integer(mRecordID));
            if (reportIn != null)
            {
                String orgId = reportIn.getOrgId();
                Integer year = reportIn.getYear();
                Integer term = reportIn.getTerm();
                String versionId = reportIn.getVersionId();
                String childRepId = reportIn.getChildRepId();
                Integer curId = reportIn.getCurId();
                Integer dataRangeId = reportIn.getDataRangeId();

                // Excel�ļ�����Ŀ¼
                String excelFilePath = com.fitech.net.config.Config
                        .getCollectExcelFolder()
                        + File.separator
                        + year
                        + "_"
                        + term
                        + File.separator
                        + orgId;

                File excelFile = new File(excelFilePath);

                if (!excelFile.exists()) excelFile.mkdirs();

                String excelFileName = childRepId + "_" + versionId + "_"
                        + dataRangeId + "_" + curId + ".xls";
                excelFilePath = excelFilePath + File.separator + excelFileName;
                mResult = MsgObj.MsgFileSave(excelFilePath); //�����ļ���Ŀ¼��
                /* ��Excel�ļ�������� */
                
                try
                {
                    ReportExcelHandler excelHandler = new ReportExcelHandler(new Integer(mRecordID), excelFilePath);
                    boolean result = excelHandler.copyExcelToDB(false);
                    if (result)
                        result = StrutsReportInDelegate.updateReportInCheckFlag(new Integer(mRecordID),
                                com.fitech.net.config.Config.CHECK_FLAG_AFTERSAVE);
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
              
            }
        }

        return (mResult);
    }



//   ȡ�ÿͻ��˷��������ݰ�
   private byte[] ReadPackage(HttpServletRequest request)
   {
   System.out.println("private byte[] ReadPackage(HttpServletRequest request)");
     byte mStream[]=null;
     int totalRead = 0;
     int readBytes = 0;
     int totalBytes = 0;
     try
     {
       totalBytes = request.getContentLength();
       mStream = new byte[totalBytes];
       while(totalRead < totalBytes)
       {
         request.getInputStream();
         readBytes = request.getInputStream().read(mStream, totalRead, totalBytes - totalRead);
         totalRead += readBytes;
         continue;
       }
     }
     catch (Exception e)
     {
       System.out.println(e.toString());
     }
     return (mStream);
   }

//   ���ʹ��������ݰ�
   private boolean SendPackage(HttpServletResponse response)
   {
   System.out.println("private boolean SendPackage(HttpServletResponse response)");
     boolean mresult=false ;
     try
     {
       ServletOutputStream OutBinarry=response.getOutputStream() ;
       OutBinarry.write(MsgObj.MsgVariant()) ;
       OutBinarry.flush();
       OutBinarry.close();
       mresult = true ;
     }
     catch(Exception e)
     {
       System.out.println(e.toString());
     }
     return (mresult);
   }

   public void ExecuteRun(HttpServletRequest request,HttpServletResponse response){

   System.out.println("public void ExecuteRun(HttpServletRequest request,HttpServletResponse response)");
      
     MsgObj=new DBstep.iMsgServer2000();      //������Ϣ������

     mOption="";
     mRecordID="";
     mFileBody=null;
     mFileName="";
     mFileType="";
     mFileSize=0;

     fFilePath = request.getSession().getServletContext().getRealPath("") ;  //ȡ�÷�����·��

     System.out.println("ReadPackage") ;
     
       try
       {
         if (request.getMethod().equalsIgnoreCase("POST"))
         {
   System.out.println("Post");
           MsgObj.MsgVariant(ReadPackage(request));
           if (MsgObj.GetMsgByName("DBSTEP").equalsIgnoreCase("DBSTEP"))
           {
             mOption=MsgObj.GetMsgByName("OPTION") ;
             if(mOption.equalsIgnoreCase("LOADFILE"))                //��������ĵ�
             {
               mRecordID=MsgObj.GetMsgByName("RECORDID");       //ȡ���ĵ����
               mUserName=MsgObj.GetMsgByName("USERNAME");       //ȡ���ĵ����
               mFileName=MsgObj.GetMsgByName("FILENAME");       //ȡ���ĵ����
               mFileType=MsgObj.GetMsgByName("FILETYPE");       //ȡ���ĵ����
               MsgObj.MsgTextClear();
               if (LoadFile_FilePath())                 //�����ĵ�
               {
                 MsgObj.SetMsgByName("STATUS","�򿪳ɹ�!");          //����״̬��Ϣ
                 MsgObj.MsgError("");                       //���������Ϣ
               }
               else
               {
                 MsgObj.MsgError("��ʧ��!");          //���ô�����Ϣ
               }
             }
             else if(mOption.equalsIgnoreCase("LOADTEMPLATE"))           //���󱣴��ĵ�
             {
               mTemplate=MsgObj.GetMsgByName("TEMPLATE");       //ȡ���ĵ����
               MsgObj.MsgTextClear();
               //mFullPath = fFilePath+"\\Document\\"+mTemplate;
               if (MsgObj.MsgFileLoad(mFullPath))                   //�����ĵ�����
               {
                 MsgObj.SetMsgByName("STATUS", "�򿪳ɹ�!");            //����״̬��Ϣ
                 MsgObj.MsgError("");               //���������Ϣ
               }
               else
               {
                 MsgObj.MsgError("����ʧ��!");          //���ô�����Ϣ
               }
             }
             else if(mOption.equalsIgnoreCase("SAVEFILE"))           //���󱣴��ĵ�
             {
               mRecordID=MsgObj.GetMsgByName("RECORDID");       //ȡ���ĵ����
               mUserName=MsgObj.GetMsgByName("USERNAME");       //ȡ���û�����
               mSubject=MsgObj.GetMsgByName("SUBJECT");     //ȡ���ĵ�����
               mFileName=MsgObj.GetMsgByName("FILENAME");       //ȡ���ĵ�����
               mFileSize=MsgObj.MsgFileSize();          //ȡ���ĵ���С
               mFileBody=MsgObj.MsgFileBody();          //ȡ���ĵ�����
               mFileType=MsgObj.GetMsgByName("FILETYPE");       //ȡ���ĵ�����
               MsgObj.MsgTextClear();
               if (SaveFile_FilePath())                     //�����ĵ�����
               {
                 MsgObj.SetMsgByName("STATUS", "����ɹ�!");            //����״̬��Ϣ
                 MsgObj.MsgError("");               //���������Ϣ
               }
               else
               {
                 MsgObj.MsgError("����ʧ��!");          //���ô�����Ϣ
               }
               MsgObj.MsgFileClear();
             }
           }
           else
           {
             MsgObj.MsgError("�ͻ��˷������ݰ�����!");
             MsgObj.MsgTextClear();
             MsgObj.MsgFileClear();
           }
         }
         else
         {
           MsgObj.MsgError("��ʹ��Post����");
           MsgObj.MsgTextClear();
           MsgObj.MsgFileClear();
         }
         if(SendPackage(response))
           System.out.println("OK!");
         else
           System.out.println("Error!");
       }
       catch(Exception e)
       {
         System.out.println(e.toString()) ;
       }
     }
   }
