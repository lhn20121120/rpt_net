package com.fitech.gznx.common;

import java.io.*;

/**
 * User: Rui.Feng
 * Date: 2008-5-30
 * Time: 11:13:24
 */
public class FileCopy {
    private static final  int BUFFER_SIZE = 5000;
    public static void copy(File source , File dest)throws Exception{
        InputStream in = null ;
        OutputStream out = null ;

        try  {
            in = new BufferedInputStream( new FileInputStream(source), BUFFER_SIZE);
            out = new BufferedOutputStream( new FileOutputStream(source), BUFFER_SIZE);
            byte [] buffer = new byte [BUFFER_SIZE];
            while (in.read(buffer) > 0 )  {
                out.write(buffer);
            }
        }finally  {
            try{
                if ( null != in)  {
                    in.close();
                }
                if ( null != out)  {
                    out.close();
                }
            }catch(Exception ee){
                ee.printStackTrace();
            }

        }



    }
}
