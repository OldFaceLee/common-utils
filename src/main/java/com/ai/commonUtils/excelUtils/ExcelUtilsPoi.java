package com.ai.commonUtils.excelUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilsPoi {
	/**
	 * 根据路径获取Excel的结果集合
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getExcelData(String filePath) throws Exception {
		return getExcelData(filePath, 0, 0);
	}

	/**
	 * 根据路径获取Excel的结果集合
	 * 
	 * @param filePath
	 * @param sheetNum
	 * @param startRow
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getExcelData(String filePath, int sheetNum, int startRow) throws Exception {
		return getExcelData2Map(new File(filePath), 0, 0);
	}

	/**
	 * 根据File获取Excel的结果集合
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getExcelData(File file) throws Exception {
		return getExcelData(file, 0, 0);
	}

	/**
	 * 根据File获取Excel的结果集合
	 * 
	 * @param
	 * @param sheetNum
	 * @param startRow
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getExcelData(File file, int sheetNum, int startRow) throws Exception {
		return getExcelData2Map(file, sheetNum, startRow);
	}

	/**
	 * 根据输入流获取Excel的结果集合
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getExcelData(FileInputStream input, boolean isXls) throws Exception {
		return getExcelData(input, isXls, 0, 0);
	}

	/**
	 * 根据输入流获取Excel的结果集合
	 * 
	 * @param
	 * @param sheetNum
	 * @param startRow
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getExcelData(FileInputStream input, boolean isXls, int sheetNum,
			int startRow) throws Exception {
		return getExcelData2Map(input, isXls, sheetNum, startRow);
	}

	/**
	 * 获取Excel固定单元格的值
	 * 
	 * @param input
	 * @param isXls
	 * @param cellNos
	 * @return
	 */
	private static Map<String, String> getCellValues(FileInputStream input, boolean isXls, List<String> cellNos) {
		Map<String, String> map = new HashMap<String, String>();
		Workbook wb = getWorkbook(input, isXls);
		if (!wb.isSheetHidden(0)) {
			Sheet sheet = wb.getSheetAt(0);
			for (String cn : cellNos) {
				System.out.println(cn);
				String[] cells = cn.split("_");
				if (cells.length > 1) {
					Row row = sheet.getRow(Integer.valueOf(cells[1]) - 1);
					map.put(cn, getContent(row, getColumnNum(cells[0]) - 1));
				}
			}
		}
		return map;
	}

	/**
	 * 从startRow开始读取Excel数据
	 * 
	 * @param file
	 * @param sheetNum
	 * @param startRow
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getExcelData2Map(File file, int sheetNum, int startRow) throws Exception {
			FileInputStream input = new FileInputStream(file);
			return getExcelData2Map(input, file.getName().toLowerCase().endsWith("xls"), sheetNum, startRow);
	}

	/**
	 * 从startRow开始读取Excel数据
	 * 
	 * @param
	 * @param sheetNum
	 * @param startRow
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, String>> getExcelData2Map(FileInputStream input, boolean isXls, int sheetNum,
			int startRow) throws Exception {
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Workbook wb = null;
		wb = getWorkbook(input, isXls);
		int sheetNums = wb.getNumberOfSheets();
		if (sheetNum < sheetNums) {
			Sheet sheet = wb.getSheetAt(sheetNum);
			if (!wb.isSheetHidden(sheetNum)) {
				int rows = sheet.getLastRowNum() + 1;
				System.out.println("行数："+rows);
				// 循环遍历每一行的数据
				for (int j = startRow; j < rows; j++) {
					Row r = sheet.getRow(j);
					if (null != r) {
						// 获得总列数
						int columns = r.getPhysicalNumberOfCells();
						Map<String, String> excelMap = new HashMap<String, String>();
						// 遍历该行，所有的列
						for (int k = 0; k < columns; k++) {
							excelMap.put(findCurrentColumnChar(k).toUpperCase(), StringUtils.trim(getContent(r, k)));
						}
						listMap.add(excelMap);
					}
				}
			}
		} else {
			throw new Exception("您输入的sheet序号有误！请确认该sheet是否存在！");
		}
		return listMap;
	}

	/**
	 * 根据文件输入流获取excel的Workbook对象
	 * 
	 * @param input
	 * @param isXls
	 * @return
	 * @throws IOException
	 */
	private static Workbook getWorkbook(FileInputStream input, boolean isXls) {
		Workbook wb = null;
		try {
			// 根据文件格式(2003或者2007)来初始化
			if (isXls) {
				wb = new HSSFWorkbook(input);
			} else {
				wb = new XSSFWorkbook(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 获得某一数字的char值
	 * 
	 * @param column
	 * @return
	 */
	private static String findCurrentColumnChar(int column) {
		return (char) (column + 65) + "";
	}

	/**
	 * 获取Excel的列序号
	 * 
	 * @param columnName
	 * @return
	 */
	private static int getColumnNum(String columnName) {
		int columnNum = 0;
		char[] columns = columnName.toCharArray();
		for (int index = 0; index < columns.length; index++) {
			int ch = columnName.charAt(columns.length - index - 1);
			columnNum += (int) (ch - 'A' + 1) * Math.pow(26, index);
		}
		return columnNum;
	}

	/**
	 * 获取单元格内容
	 * 
	 * @param row
	 * @param cellNo
	 * @return
	 */
	private static String getContent(Row row, int cellNo) {
		Cell cell = row.getCell(cellNo);
		String cellContent = "";
		if (null != cell) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				cellContent = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				cellContent = String.valueOf(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				cellContent = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				try {
					cellContent = cell.getStringCellValue();
				} catch (IllegalStateException e) {
					try {
						cellContent = String.valueOf(cell.getNumericCellValue());
					} catch (IllegalStateException le) {
						cellContent = "";
					}
				}
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				cellContent = "";
				break;
			default:
				cellContent = "";
				break;
			}
		}
		return cellContent;
	}



	/**
	 * 获取excel， 李学军，文件的所有sheet名称
	 * @param filePath
	 * @return
	 */
	public static List<String> getSheetName(String filePath){
		InputStream inputStream = null;
		List<String> list = new ArrayList<>();
		try {
			inputStream = new FileInputStream(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook book = null;
		try {
			book = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int sheetTotals = book.getNumberOfSheets();
		for (int i = 0; i < sheetTotals; i++) {
			list.add(book.getSheetName(i));
		}
		return list;
	}



	/**
	 * 李学军，rowNo是从exce哪一行开始读取， columnNo是从最右侧数不读取多少列
	 * @param fileAbsolutePath
	 * @param sheetName
	 * @param rowNo
	 * @param columnNo
	 * @return
	 */
	public static String[][] getArrayFromXLSX(String fileAbsolutePath, String sheetName,int rowNo,int columnNo) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(fileAbsolutePath));
			XSSFWorkbook book = new XSSFWorkbook(inputStream);
			if (StringUtils.isBlank(sheetName)) {
				System.out.println("sheetName为空");
				return null;
			}
			XSSFSheet sheet = book.getSheet(sheetName);
			int rowNum = sheet.getLastRowNum()+1;
			int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
			if(rowNo < 0 || rowNo >rowNum){
				throw new Exception();
			}
			if(columnNo < 0 || columnNo > coloumNum){
				throw new Exception();
			}
			String[][] contents = new String[rowNum-rowNo][coloumNum-columnNo];
			for (int j = 0; j < rowNum; j++) {
				XSSFRow row = sheet.getRow(j+rowNo);
				if (row != null) {
					for (int k = 0; k <row.getLastCellNum()-columnNo; k++) {
						contents[j][k] = getXCellFormatValue(row.getCell(k));
					}
				}
			}
			return contents;
		} catch (FileNotFoundException fe) {
			fe.getStackTrace();
		} catch (IOException ie) {
			ie.getStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 李学军， 获取单元格格式化后的值
	 * @param cell
	 * @return
	 */
	private static String getXCellFormatValue(XSSFCell cell) {
		String cellValue = "";
		if (null != cell) {
			switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					cellValue = cell.getRichStringCellValue().getString();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					cellValue = (new Double(cell.getNumericCellValue())).intValue() + "";
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					cellValue = "";
					break;
				default:
					cellValue = " ";
			}
		}
		return cellValue;
	}

	/**
	 * 李学军， 获取指定的cell内容
	 * @param filePath
	 * @param sheetName
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getCellValue(String filePath,String sheetName,int row,int column){
		String st = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(new File(filePath));
			XSSFWorkbook book = null;
			book = new XSSFWorkbook(fs);

			if (StringUtils.isBlank(sheetName)) {
				System.out.println("sheetName为空");
				return null;
			}
			XSSFSheet sheet = book.getSheet(sheetName);
			Cell cell = sheet.getRow(row).getCell(column);
			st = cell.getStringCellValue();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return st;

	}


}
