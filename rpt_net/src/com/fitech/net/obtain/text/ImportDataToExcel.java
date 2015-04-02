package com.fitech.net.obtain.text;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ImportDataToExcel {

	private POIFSFileSystem fileSystem = null; 
    private HSSFWorkbook workBook = null; 
    private HSSFSheet sheet = null;
    private FileInputStream fileStream=null;
    /**
     * 打开工作部
     * 
     * @param filePath    文件路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void openWorkBook(String filePath) throws FileNotFoundException, IOException{
        try
        {
        	fileStream=new  FileInputStream(filePath);
    	fileSystem = new POIFSFileSystem(fileStream);
    
        workBook = new HSSFWorkbook(fileSystem);
        }
        catch(FileNotFoundException e)
        {
        	e.printStackTrace();
        }
    }
    public void close()
    {
    	try
    	{
    	   	fileStream.close();
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    }
    public boolean FileExit(String path)
    {
    File file=new File(path);
    return (file.exists());
    
    }
    /**
     * 选择工作表
     * 
     * @param iSheet 工作表的索引
     * @throws Exception
     */
    public void selectSheetByIndex(int iSheet) throws Exception{
        if ( this.workBook == null ){
            throw new Exception("there is not any workBook is valuble");    
        }
        this.sheet = this.workBook.getSheetAt(iSheet);
    }
    public boolean setCellValueAsString(String position,String value) throws Exception{
        int iCol = 0;
        int iRow = 0;
        // System.out.println(position);
        //取得单元格的行数，列数
        iCol = position.toUpperCase().charAt(0) - 'A';
        if (position.toUpperCase().charAt(1) >= 'A' 
            && position.toUpperCase().charAt(1) <= 'Z'){
            iCol = iCol * 10 + position.toUpperCase().charAt(1) - 'A';
            iRow = Integer.parseInt(position.substring(2)) - 1;            
        } else {
            iRow = Integer.parseInt(position.substring(1)) - 1;    
        }
        return this.setCellValueAsString(iRow,iCol,value) ;
    }
    public boolean setCellValueAsString(int iRow,int iCol,String data) throws Exception{
        boolean result=true;
    	try
        {
    		
    		
    	HSSFCell cell = getCell(iRow, iCol);
    // System.out.println(""+iRow+iCol+data);
    	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    	   cell.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
    	 

        cell.setCellValue(data);
        
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	result=false;
        }
        return result ;      
    }
    public void save(String path )
    
    {
    	FileOutputStream fileOut=null;
    	try
    	{
    	 fileOut = new FileOutputStream(path);
    	this.workBook.write(fileOut);
    	 fileOut.flush();
    	 fileOut.close();
    	}
    	catch(Exception e)
    	{
    	e.printStackTrace();	
    	}
    	finally
    	{
    		
    	}

    }
    
    /**
     * 根据行号和列号选择单元
     * 
     * @param iRow
     * @param iCol
     * @return
     * @throws Exception
     */
    private HSSFCell getCell(int iRow, int iCol) throws Exception {
        if (null == this.sheet){
        	this.sheet=this.workBook.createSheet();
           // throw new Exception("can't read any data:sheet is not exist!!!");    
        }
        HSSFRow row = this.sheet.getRow(iRow);
        if (null == row){
        	row=this.sheet.createRow(iRow);
          //  throw new Exception("can't read any data:row index is not wrong!!!");    
        }
        HSSFCell cell = row.getCell((short)iCol);
        if (null == cell){
        	cell=row.createCell((short)iCol);
          //  throw new Exception("can't read any data:col index is not wrong!!!");    
        }
        return cell;
    }



}
