package cn.com.do1.component.contact.department.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.util.ErrorCodeDesc;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.com.do1.component.addressbook.department.vo.ImportDeptToUserVO;

public class DeptToUserExcelUtil{

	public static long fileMaxSize=1024*1024*2;  //文件大小限制为2M

	/**
	 * 最大列数 50
	 */
	public final static int maxCol = 50;
	/*
	 * Excel导入方法
	 * @param file 导入的文件
	 * @param fileName 文件名
	 * @param Class
	 * @param serv
	 * @return list
	 */
	public static List importForExcel(File file,String fileName,String id) throws Exception, NonePrintException {
		List<Object> targetList=new ArrayList<Object>();

		//判断是否传入了文件
		if (file == null) {
			throw new NonePrintException(ErrorCodeDesc.excel_no_exist.getCode(), ErrorCodeDesc.excel_no_exist.getDesc());
		}
		//判断文件格式
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			throw new NonePrintException(ErrorCodeDesc.excel_error_format.getCode(), ErrorCodeDesc.excel_error_format.getDesc());
		}
		//判断文件大小
		if (file.length() > fileMaxSize) {
			throw new NonePrintException(ErrorCodeDesc.excel_too_larger.getCode(), ErrorCodeDesc.excel_too_larger.getDesc());
		}
		FileInputStream fis = new FileInputStream(file);

		//处理excel2003
		if(fileName.toLowerCase().endsWith(".xls")){
			importExcel2003(targetList,fis,id);
		}
		//处理excel2007
		if(fileName.toLowerCase().endsWith(".xlsx")){
			importExcel2007(targetList,fis,id);
		}
		fis.close();
		return targetList;
	}

	public static void importExcel2003(List<Object> targetList,FileInputStream fis,String id)
			throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ParseException{
		Map<Short,String> titleMap=new HashMap<Short ,String>();
		//把一张xls的数据表读到wb里
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		//读取第一页,一般一个excel文件会有三个工作表，这里获取第一个工作表来进行操作
		HSSFSheet sheet = wb.getSheetAt(0);
		int entryCol = 0; //行内空列数
        //需要读取的列数
        int col = 0;
        //遍历所有行
        ImportDeptToUserVO deptToUserVO = null;
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            //取第i行数据
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				break;
			}
			if (i == 0) {//标题栏
				col = row.getPhysicalNumberOfCells();
				Map<String, String> titles = new HashMap<String, String>();
				String title = "";
				HSSFCell titleCell;
				for (short j = 0; j < col; j++) {
					titleCell = row.getCell(j);
					//title为空 则结束读取
					if (AssertUtil.isEmpty(titleCell) || AssertUtil.isEmpty(titleCell.getStringCellValue()) || j == maxCol) { //title为空往后的列不处理
						col = j;
						break;
					}
					title = getCellValue(title, titleCell);
					if (!"".equals(title)) {
						titleMap.put(j, titleCell.getStringCellValue());
						titles.put(String.valueOf(j), titleCell.getStringCellValue());
					}
				}
				continue;
			}

			deptToUserVO = new ImportDeptToUserVO();
			entryCol = 0; //空列
			HSSFCell cell;
			//数据值
			for (short j = 0; j < col; j++) {
				//得到第j列标题
				String titleString = (String) titleMap.get(j);
				//如果此列是需要的列，则继续操作
				cell = row.getCell(j);
				if (titleString != null && titleString != "") {
					if (cell != null) {
						String value = "";
						switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_NUMERIC:
								cell.setCellType(XSSFCell.CELL_TYPE_STRING);
								value = cell.getStringCellValue();
								value = String.valueOf(Math.round(Double.parseDouble(value) * 100) / 100.0);
								break;
							case HSSFCell.CELL_TYPE_STRING:
								value = cell.getRichStringCellValue().getString();
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								value = cell.getStringCellValue();
								break;
						}
						if ("".equals(value)) {
							entryCol++;
						}
						deptToUserVO = setDeptToUserVOExcel(deptToUserVO, j, value);
					} else {
						entryCol++;
					}
				}

			}
			if (entryCol == col)
				break;
			targetList.add(deptToUserVO);
		 }
	}

	/**
	 * 获取单元格的内容
	 * @param title
	 * @param titleCell
	 * @return
	 */
	private static String getCellValue(String title, HSSFCell titleCell) {
		switch (titleCell.getCellType()) {
		    case XSSFCell.CELL_TYPE_NUMERIC :
		    	titleCell.setCellType(XSSFCell.CELL_TYPE_STRING);
		    	title=titleCell.getStringCellValue();
		        break;
		    case XSSFCell.CELL_TYPE_STRING :
		    	title = titleCell.getRichStringCellValue().getString();
		        break;
		    case XSSFCell.CELL_TYPE_FORMULA :
		    	title = titleCell.getStringCellValue();
		        break;
		}
		return title;
	}

	public static void importExcel2007(List<Object> targetList,FileInputStream fis,String id)
			throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ParseException{
		Map<Short,String> titleMap=new HashMap<Short ,String>();
		//把一张xlsx的数据表读到wb里
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		//读取第一页,一般一个excel文件会有三个工作表，这里获取第一个工作表来进行操作
		XSSFSheet sheet = wb.getSheetAt(0);
		int entryCol = 0; //行内空列数
		//需要处理的列数
		int col= 0;
		//遍历所有行
		ImportDeptToUserVO deptToUserVO;
		XSSFRow row;
		for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
			//取第i行数据
			row = sheet.getRow(i);
			if (row == null) {
				break;
			}
			//该行包含的单元格数既为列数
			if (i == 0) {//i=0时为标题
				col = row.getPhysicalNumberOfCells();

				Map<String, String> titles = new HashMap<String, String>();
				XSSFCell titleCell;
				for (short j = 0; j < col; j++) {
					if (j > 5) {//最大只有5列,超过5列的数据不处理
						continue;
					}
					titleCell = row.getCell(j);
					if (AssertUtil.isEmpty(titleCell) || AssertUtil.isEmpty(titleCell.getStringCellValue()) || j == maxCol) { //title为空往后的列不处理
						col = j;
						break;
					}
					if (!"".equals(titleCell.getStringCellValue())) {
						titleMap.put(j, titleCell.getStringCellValue());
						titles.put(String.valueOf(j), titleCell.getStringCellValue());
					}
				}
				continue;
			}
			//i>0时为数据实例
			//新建clazz的实例，将每一行的数据写入此实例
			//遍历第i行的列
			deptToUserVO = new ImportDeptToUserVO();
			;
			XSSFCell cell;
			for (short j = 0; j < col; j++) {
				if (j > 5) {//最大只有5列,超过5列的数据不处理
					continue;
				}
				//得到第j列标题
				String titleString = titleMap.get(j);
				//如果此列是需要的列，则继续操作
				cell = row.getCell(j);
				if (titleString != null && titleString != "") {
					if (cell != null) {
						String value = changeStr(cell);
						if ("".equals(value)) {
							entryCol++;
						}
						deptToUserVO = setDeptToUserVOExcel(deptToUserVO, j, value);
					} else {
						entryCol++;
					}
				}
			}
			if (entryCol == col)
				break;
			targetList.add(deptToUserVO);
		}
	}

	private static ImportDeptToUserVO setDeptToUserVOExcel(ImportDeptToUserVO vo,int x,String value){
		switch(x){
			case 0:
				vo.setDepartmentId(value.trim());
				break;
			case 1:
				vo.setDepartmentName(value.trim());
				break;
			case 2:
				vo.setDeptFullName(value.trim());
				break;
			case 3:
				vo.setPersonNames(value.trim());
				break;
			case 4:
				vo.setWxUserIds(value.trim());
				break;
		}
		return vo;
	}

	private static String changeStr(XSSFCell cell){
		String value="";
		switch (cell.getCellType()) {
        case XSSFCell.CELL_TYPE_NUMERIC :
        	cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        	value=cell.getStringCellValue();
        	//处理小数点后问题，chenfeixiong
        	value=String.valueOf(Math.round(Double.parseDouble(value)*100)/100.0);
            break;
        case XSSFCell.CELL_TYPE_STRING :
        	value = cell.getRichStringCellValue().getString();
            break;
        case XSSFCell.CELL_TYPE_FORMULA :
        	value = cell.getCellFormula();
            break;
		}
		return value;
	}

}