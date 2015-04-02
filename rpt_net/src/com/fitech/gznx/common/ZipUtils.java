/*
 * 创建日期 2005-7-5
 *
 */
package com.fitech.gznx.common;

/**
 * @author cb
 *
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ZipUtils {
    private boolean overwrite = true;
    
    private static final String NATIVE_ENCODING = "native-encoding";
    
    private String encoding = "GB2312";
    private static FileUtils fileUtils =FileUtils.newFileUtils();
    
    /**
     * 解压缩文件srcF到dir目录下
     * @param srcF
     * @param dir
     * @return
     */
    public File[] expandFile(File srcF, File dir) {
    	
    	// System.out.println("------------------555555555555555555-------------------");
    	
        ZipFile zf = null;
        ArrayList fileArray = new ArrayList();
        File[] files =null;
        
        try {
            zf = new ZipFile(srcF,encoding);
            Enumeration e = zf.getEntries();
            
            while (e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) e.nextElement();
                
                extractFile(srcF, dir, zf.getInputStream(ze),
                        ze.getName(), new Date(ze.getTime()),
                        ze.isDirectory());
                File f = new File(dir,ze.getName());
                fileArray.add(f);
            }            
            
            files = new File[fileArray.size()];
            for(int i=0;i<fileArray.size();i++){
                files[i] = (File) fileArray.get(i);
            }
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return files;
    }
    
    public static File zipFiles(File[] files,String dir,String zipName) throws IOException{
        File zipFile = new File(dir,zipName);
        ZipOutputStream zipos = new ZipOutputStream(new FileOutputStream(zipFile));
        
        for(int i=0;i<files.length;i++){
            FileInputStream fis = new FileInputStream (files[i]);
            ZipEntry entry  = new ZipEntry(files[i].getName());
            zipos.putNextEntry(entry);
            byte[] buffer = new byte[1024];
            int size= 0;
            while((size=fis.read(buffer))!=-1){
                zipos.write(buffer,0,size);
            }
            fis.close();            
        }
        zipos.close();
        return zipFile;
    }
    /**
     * 
     * @param srcF
     * @param dir
     * @param compressedInputStream
     * @param entryName
     * @param entryDate
     * @param isDirectory
     * @throws IOException
     */
    protected void extractFile(File srcF, File dir,
            InputStream compressedInputStream,
            String entryName,
            Date entryDate, boolean isDirectory)
    throws IOException {
        
        File f = fileUtils.resolveFile(dir, entryName);
        try {
            if (!overwrite && f.exists()
                    && f.lastModified() >= entryDate.getTime()) {
                
                return;
            }
            
            
            // create intermediary directories - sometimes zip don't add them
            File dirF = fileUtils.getParentFile(f);
            if (dirF != null) {
                dirF.mkdirs();
            }
            
            if (isDirectory) {
                f.mkdirs();
            } else {
                byte[] buffer = new byte[1024];
                int length = 0;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    
                    while ((length =
                        compressedInputStream.read(buffer)) >= 0) {
                        fos.write(buffer, 0, length);
                    }
                    
                    fos.close();
                    fos = null;
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
            
            fileUtils.setFileLastModified(f, entryDate.getTime());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    /**
     * Should we overwrite files in dest, even if they are newer than
     * the corresponding entries in the archive?
     */
    public void setOverwrite(boolean b) {
        overwrite = b;
    }
    
    /**
     * Sets the encoding to assume for file names and comments.
     *
     * <p>Set to <code>native-encoding</code> if you want your
     * platform's native encoding, defaults to UTF8.</p>
     *
     * @since Ant 1.6
     */
    public void setEncoding(String encoding) {
        if (NATIVE_ENCODING.equals(encoding)) {
            encoding = null;
        }
        this.encoding = encoding;
    }
    

}
