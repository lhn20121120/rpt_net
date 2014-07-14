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
     * 功能或作用  调出文档，将文档内容保存在mFileBody里，以便进行打包
     * @return
     */
     private boolean LoadFile_FilePath()
     {
      System.out.println("private boolean LoadFile_FilePath()");
      boolean mResult=false;
      if ( mRecordID.equalsIgnoreCase(""))
      {
         mRecordID="0"; //编号为空
      }    
        try
        {
            mFullPath = Config.TEMP_DIR + mFileName;
            mResult=MsgObj.MsgFileLoad(mFullPath);  //从目录里调出文件
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
      * 功能或作用：保存文档，如果文档存在，则覆盖，不存在，则添加
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

                // Excel文件保存目录
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
                mResult = MsgObj.MsgFileSave(excelFilePath); //保存文件到目录里
                /* 将Excel文件解析入库 */
                
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



//   取得客户端发来的数据包
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

//   发送处理后的数据包
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
      
     MsgObj=new DBstep.iMsgServer2000();      //创建信息包对象

     mOption="";
     mRecordID="";
     mFileBody=null;
     mFileName="";
     mFileType="";
     mFileSize=0;

     fFilePath = request.getSession().getServletContext().getRealPath("") ;  //取得服务器路径

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
             if(mOption.equalsIgnoreCase("LOADFILE"))                //请求调用文档
             {
               mRecordID=MsgObj.GetMsgByName("RECORDID");       //取得文档编号
               mUserName=MsgObj.GetMsgByName("USERNAME");       //取得文档编号
               mFileName=MsgObj.GetMsgByName("FILENAME");       //取得文档编号
               mFileType=MsgObj.GetMsgByName("FILETYPE");       //取得文档编号
               MsgObj.MsgTextClear();
               if (LoadFile_FilePath())                 //调入文档
               {
                 MsgObj.SetMsgByName("STATUS","打开成功!");          //设置状态信息
                 MsgObj.MsgError("");                       //清除错误信息
               }
               else
               {
                 MsgObj.MsgError("打开失败!");          //设置错误信息
               }
             }
             else if(mOption.equalsIgnoreCase("LOADTEMPLATE"))           //请求保存文档
             {
               mTemplate=MsgObj.GetMsgByName("TEMPLATE");       //取得文档编号
               MsgObj.MsgTextClear();
               //mFullPath = fFilePath+"\\Document\\"+mTemplate;
               if (MsgObj.MsgFileLoad(mFullPath))                   //保存文档内容
               {
                 MsgObj.SetMsgByName("STATUS", "打开成功!");            //设置状态信息
                 MsgObj.MsgError("");               //清除错误信息
               }
               else
               {
                 MsgObj.MsgError("保存失败!");          //设置错误信息
               }
             }
             else if(mOption.equalsIgnoreCase("SAVEFILE"))           //请求保存文档
             {
               mRecordID=MsgObj.GetMsgByName("RECORDID");       //取得文档编号
               mUserName=MsgObj.GetMsgByName("USERNAME");       //取得用户名称
               mSubject=MsgObj.GetMsgByName("SUBJECT");     //取得文档名称
               mFileName=MsgObj.GetMsgByName("FILENAME");       //取得文档类型
               mFileSize=MsgObj.MsgFileSize();          //取得文档大小
               mFileBody=MsgObj.MsgFileBody();          //取得文档内容
               mFileType=MsgObj.GetMsgByName("FILETYPE");       //取得文档类型
               MsgObj.MsgTextClear();
               if (SaveFile_FilePath())                     //保存文档内容
               {
                 MsgObj.SetMsgByName("STATUS", "保存成功!");            //设置状态信息
                 MsgObj.MsgError("");               //清除错误信息
               }
               else
               {
                 MsgObj.MsgError("保存失败!");          //设置错误信息
               }
               MsgObj.MsgFileClear();
             }
           }
           else
           {
             MsgObj.MsgError("客户端发送数据包错误!");
             MsgObj.MsgTextClear();
             MsgObj.MsgFileClear();
           }
         }
         else
         {
           MsgObj.MsgError("请使用Post方法");
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
