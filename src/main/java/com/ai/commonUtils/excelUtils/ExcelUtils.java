package com.ai.commonUtils.excelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.ai.commonUtils.collectionUtils.CollectionUtils;
import com.ai.commonUtils.fileUtils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 
 * @author lixuejun
 *
 */


public class ExcelUtils {
	
	private  static Logger logger = Logger.getLogger(ExcelUtils.class);
	private  static Workbook workBook = null;
	private  static OutputStream os = null;
	private  static WritableWorkbook wwb  = null;
	private  static WritableCellFormat wcf = null;
	private  static HSSFWorkbook workBookPoiXls= null;
	private  static XSSFWorkbook workBookPoiXlsx = null;
	private  static HSSFSheet sheet = null;
	private  static XSSFSheet sheetXssf = null;
	private  static XSSFRow row = null;
	private  static OutputStream out = null;
	private  static FileInputStream in =null;
	
	
	public static void main(String[] args) throws Exception {
		String path = "C:\\Users\\lixuejun\\Desktop\\lixj5.xls";
//		writeCellContent(path,path,1,1,1,"测试");


	}

	/**
	 * excel列名 对应 Integer的工具类
	 * @param mapKey
	 * @return
	 */
	private static int columnMap(String mapKey){
		int index = 0;
		List<String> listStrs = Arrays.asList(
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"AA","BB","CC","DD","EE","FF","GG","HH","II","JJ","KK","LL","MM","NN","OO","PP","QQ","RR","SS","TT","UU","VV","WW","XX","YY","ZZ");
		Map<String,Integer> map = new HashMap<String, Integer>();
		for (int i=0; i<listStrs.size();i++){
			map.put(listStrs.get(i),i);
		}
		for(Map.Entry<String,Integer> m : map.entrySet()){
			if(mapKey.equalsIgnoreCase(m.getKey())){
				index = m.getValue();
			}
		}
		return index;
	}

	/**
	 * 获取指定sheet、指定row、指定column的cell值，例如，A,B,C,等等，返回string类型的值
	 * @param sheetNumber
	 * @param columnName
	 * @param rowNumber
	 * @param filePathWithFileExtendsName
	 * @return
	 */
	public static String getCellContent(int sheetNumber, int rowNumber, String columnName , String filePathWithFileExtendsName){
		if(!FileUtils.getFileExtandsName(filePathWithFileExtendsName).equalsIgnoreCase("xls")){
			try {
				throw new Exception("excel extends must endsWith xls(excel97-2003)");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			workBook = Workbook.getWorkbook(new FileInputStream(filePathWithFileExtendsName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		//Get the workbook
		Sheet sheet [] = workBook.getSheets();
		Sheet s = null;
		if(rowNumber<=0 || sheetNumber<=0){
			try {
				throw new Exception("excel`s rowNumber || sheetNumber should not <= 0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		s = sheet[sheetNumber-1];
		Cell cell = s.getCell(columnMap(columnName), rowNumber-1);
		String cellValues = cell.getContents();
		logger.info("读取excel文件："+filePathWithFileExtendsName+",传入的参数为sheet ="+sheetNumber+", row="+rowNumber+", column="+columnMap(columnName)+", 其Cell的content："+cellValues);
		workBook.close();
		return cellValues;

	}

	
	/**
	 * 向一个excel里边指定了sheet、row、column的cell里写数据
	 * @param sourceFilePath
	 * @param sheetNumber
	 * @param row
	 * @param column
	 * @param inputContents
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws BiffException
	 */
	public  static void writeCellContent (String sourceFilePath,String filePath,int sheetNumber,int row,int column,String inputContents) {
		File sourceFile = new File(sourceFilePath);
		File targetFile = new File(filePath);
		if(!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			workBook = Workbook.getWorkbook(sourceFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		try {
			wwb = Workbook.createWorkbook(targetFile, workBook);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(sheetNumber <=0 || row<=0 || column<=0){
			try {
				throw new Exception("params sheetNumber || row|| column should not be <=0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		WritableSheet sheet = wwb.getSheet(sheetNumber-1);
		WritableFont wf = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD, false);
		wcf = new WritableCellFormat(wf);
		try {
			wcf.setAlignment(Alignment.CENTRE);
		} catch (WriteException e) {
			e.printStackTrace();
		}
//		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		if(inputContents.equalsIgnoreCase("pass")){
			try {
				wcf.setBackground(Colour.GREEN);
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}else if (inputContents.equalsIgnoreCase("fail")){
			try {
				wcf.setBackground(Colour.RED);
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		Label label = new Label(column-1,row-1,inputContents,wcf);
		try {
			sheet.addCell(label);
			logger.info("向cell中row:"+row+" column"+column+" 插入数据:"+inputContents);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		try {
			wwb.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			wwb.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		workBook.close();

	}



	public static void writeCellContent (String sourceFilePath,String filePath,int sheetNumber,int row,String columnName,String inputContents) {
		File sourceFile = new File(sourceFilePath);
		File targetFile = new File(filePath);
		if(!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			workBook = Workbook.getWorkbook(sourceFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		try {
			wwb = Workbook.createWorkbook(targetFile, workBook);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(sheetNumber <=0 || row<=0 ){
			try {
				throw new Exception("params sheetNumber || row should not be <=0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		WritableSheet sheet = wwb.getSheet(sheetNumber-1);
		WritableFont wf = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD, false);
		wcf = new WritableCellFormat(wf);
		try {
			wcf.setAlignment(Alignment.CENTRE);
		} catch (WriteException e) {
			e.printStackTrace();
		}
//		wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
		if(inputContents.equalsIgnoreCase("pass")){
			try {
				wcf.setBackground(Colour.GREEN);
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}else if (inputContents.equalsIgnoreCase("fail")){
			try {
				wcf.setBackground(Colour.RED);
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		Label label = new Label(columnMap(columnName)-1,row-1,inputContents,wcf);
		try {
			sheet.addCell(label);
			logger.info("向cell中row:"+row+" column"+columnMap(columnName)+" 插入数据:"+inputContents);
		} catch (WriteException e) {
			e.printStackTrace();
		}
		try {
			wwb.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			wwb.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		workBook.close();

	}
	
	/**
	 * 获取指定excel中sheet下的某一列下的所有cell的值，返回的是ArrayList<String>
	 * @param sheetNumber
	 * @param columnName
	 * @param filePathWithFileExtendsName
	 * @return
	 * @throws BiffException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<String> getAllDataUnderColumnName(int sheetNumber,String columnName,String filePathWithFileExtendsName) {
		if(!FileUtils.getFileExtandsName(filePathWithFileExtendsName).equalsIgnoreCase("xls")){
			try {
				throw new Exception("excel extends must endsWith xls(excel97-2003)");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			workBook = Workbook.getWorkbook(new FileInputStream(filePathWithFileExtendsName));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		if(sheetNumber <=0 ){
			try {
				throw new Exception("sheetNumber Param shoule be >0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Sheet sheet = workBook.getSheet(sheetNumber-1);
		int rows = sheet.getRows();
        int columns = sheet.getColumns();
        
        ArrayList<String> columnData = new ArrayList();

        	for(int rowNumber = 0; rowNumber< rows; rowNumber++){
        		String cellVaule = StringUtils.trim(sheet.getCell(columnMap(columnName),rowNumber).getContents());
//        		String key = sheet.getCell(columnNo,rowNumber).getContents();
        		if(!cellVaule.equals("")){
					columnData.add(cellVaule);
				}
        	}
        workBook.close();
        	logger.info(columnName+"列单元格下的全部数据有"+columnData.size()+"个，且数据为："+columnData);
        return columnData;
	}
	
	/**
	 * 指定两个column列，把他们对应的cell装入ArrayList，并且装入Map,通过key-value形式取值
	 * @param sheetNumber
	 * @param columnNameKey
	 * @param columnNameValue
	 * @param filePath
	 * @return
	 * @throws BiffException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static HashMap<String,String> get2ColumnMapValue(int sheetNumber,String columnNameKey,String columnNameValue, String filePath){
		try {
			workBook = Workbook.getWorkbook(new FileInputStream(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		if(sheetNumber <=0){
			try {
				throw new Exception("param sheetNumber should not <=0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Sheet sheet = workBook.getSheet(sheetNumber-1);
		int rows = sheet.getRows();
        int columns = sheet.getColumns();
        
        ArrayList<String> columnKeyData = new ArrayList();
        	for(int rowNumber = 0; rowNumber< rows; rowNumber++){
        		String cellVaule = sheet.getCell(columnMap(columnNameKey),rowNumber).getContents().trim();
        		columnKeyData.add(cellVaule);
        	}
        ArrayList<String> columnValueData = new ArrayList();
        	for(int rowNumber = 0; rowNumber< rows; rowNumber++){
        		String cellVaule = sheet.getCell(columnMap(columnNameValue),rowNumber).getContents().trim();
        		columnValueData.add(cellVaule);
        	}
        HashMap<String, String> map = new HashMap();
        for(int i=0; i<columnKeyData.size();i++){
        	map.put(columnKeyData.get(i), columnValueData.get(i));
        }
        logger.info(columnNameKey+"列与"+columnNameValue+"列的map映射为："+map);
        workBook.close();
		return map;
	}
	


	
}
