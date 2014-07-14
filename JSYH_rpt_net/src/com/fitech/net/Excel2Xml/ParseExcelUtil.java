package com.fitech.net.Excel2Xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ParseExcelUtil extends Util{

    private HSSFWorkbook workbook = null;
    private HSSFSheet sheet = null;

    /**
     *  @@override getSubtitle()
     *  ��дԭ����ʹ��POI���� �õ�excelС����
     */
    public String getSubtitle(String path){
        String subTitle = "";
        try{
            if (workbook == null){
                workbook = new HSSFWorkbook(new FileInputStream(path));
                if (sheet == null) sheet = workbook.getSheetAt(0);
            }
            if (sheet != null){
                int subTitleCol = 0;
                int subTitleRow = 2;
                HSSFRow row1 = sheet.getRow(subTitleRow);

                if (null != row1.getCell((short) subTitleCol)
                        && row1.getCell((short) subTitleCol).getCellType() == HSSFCell.CELL_TYPE_STRING){
                    subTitle = row1.getCell((short) subTitleCol)
                            .getStringCellValue().trim();
                }
            }
        }catch (FileNotFoundException e){
        	subTitle = "";
            e.printStackTrace();
        }catch (IOException e){
        	subTitle = "";
            e.printStackTrace();
        }
        return subTitle;
    }

    /**
     *  @@override getTitle()
     *  ��дԭ����ʹ��POI���� �õ�excel�Ĵ����
     */
    public String getTitle(String path){
        String title = "";
        try{
            if (workbook == null){
                workbook = new HSSFWorkbook(new FileInputStream(path));
                if (sheet == null){
                    sheet = workbook.getSheetAt(0);
                }
            }
            if (sheet != null){
                HSSFRow rowTitle = sheet.getRow(0);
                if (rowTitle != null
                        && rowTitle.cellIterator().hasNext()
                        && null != rowTitle.getCell((short) 0)
                                .getStringCellValue()){
                    title = rowTitle.getCell((short) 0).getStringCellValue()
                            .replaceAll(" ", "");
                    if (title.indexOf("GF04�����") > -1){
                        rowTitle = sheet.getRow(2);
                        if (rowTitle != null
                                && rowTitle.cellIterator().hasNext()
                                && null != rowTitle.getCell((short) 0)
                                        .getStringCellValue()){
                            if (rowTitle.getCell((short) 0)
                                    .getStringCellValue().indexOf("��ע��Ŀ") > -1){
                                title = "GF04�����ע";
                            }
                        }
                    }
                    if (title == null || title.equals("")){
                        rowTitle = sheet.getRow(1);
                        title = rowTitle.getCell((short) 1)
                                .getStringCellValue().replaceAll(" ", "");
                    }
                }else{
                    rowTitle = sheet.getRow(1);
                    if (rowTitle != null
                            && rowTitle.cellIterator().hasNext()
                            && null != rowTitle.getCell((short) 1)
                                    .getStringCellValue()){
                        title = rowTitle.getCell((short) 1)
                                .getStringCellValue().replaceAll(" ", "");
                    }

                }
            }
        }catch (FileNotFoundException e){
        	title = "";
            e.printStackTrace();
        }catch (IOException e){
        	title = "";
            e.printStackTrace();
        }
        return title;
    }
}