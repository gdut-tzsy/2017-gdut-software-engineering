package cn.com.do1.component.contact.contact.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.com.do1.common.exception.NonePrintException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.util.*;
import cn.com.do1.component.util.ErrorCodeDesc;
import cn.com.do1.component.util.memcached.CacheWxqyhObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.com.do1.component.common.annotation.TitleAnnotation;
import cn.com.do1.component.common.service.IExcelService;
import cn.com.do1.component.common.vo.ResultVO;



public class ExcelUtil{
	
	public static long fileMaxSize=1024*1024*2;  //文件大小限制为2M
	
	/**
	 * 校验日期yyyy/MM/dd
	 */
	public final static String checkDate1 = "^((([0-9]{4}|[1-9][0-9]{3})/(((0[13578]|1[02])/(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)/(0[1-9]|[12][0-9]|30))|(02/(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))/02/29))$";
	/**
	 * 校验日期yyyy-MM-dd
	 */
	public final static String checkDate2 = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$";
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
	public static List importForExcel(File file,String fileName,Class<?> clazz,IExcelService serv,String id,  List<TbQyUserCustomOptionVO> optionVOs) throws Exception, NonePrintException {
		List<Object> targetList=new ArrayList<Object>();

		//判断是否传入了文件
		if(file==null){
			throw new Exception("传入了一个空文件");
		}
		//判断文件格式
		if(!fileName.endsWith(".xls")&&!fileName.endsWith(".xlsx")){
			throw new Exception("文件格式不对");
		}
		//判断文件大小
		if(file.length()>fileMaxSize){
			throw new Exception("文件太大");
		}
		Map<String,TbQyUserCustomOptionVO > optionVOMap = new HashMap<String, TbQyUserCustomOptionVO>();
		for(int i = 0; i < optionVOs.size(); i ++){//把自定义字段的放进一个map里面去
			optionVOMap.put(optionVOs.get(i).getOptionName(),optionVOs.get(i));
		}
		//获取目标类所有可导入的字段列表
		Field[] field=clazz.getDeclaredFields();
		Map<String, Method> fieldMap=new HashMap<String, Method>();
		for(int i=0;i<field.length;i++){
			Field f=field[i];
			TitleAnnotation title=f.getAnnotation(TitleAnnotation.class);
			if(title!=null){
				String fieldname=f.getName();
				String setMethodName = "set"  
                    + fieldname.substring(0, 1).toUpperCase()  
                    + fieldname.substring(1); 
				//构造setter方法
				Method setMethod=clazz.getMethod(setMethodName, new Class[]{f.getType()});
				fieldMap.put(title.titleName(), setMethod);
			}
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			//处理excel2003
			if(fileName.endsWith(".xls")){
				importExcel2003(targetList,fis,fieldMap,clazz,serv,id, optionVOMap);
			}
			//处理excel2007
			if(fileName.endsWith(".xlsx")){
				importExcel2007(targetList,fis,fieldMap,clazz,serv,id, optionVOMap);
			}
		} finally {
			if(fis !=null){
				fis.close();
			}
		}
		return targetList;
	}
	
	public static String [] getExcelTitle(File file,String fileName,Class<?> clazz,IExcelService serv,String id) throws Exception{
		//判断是否传入了文件
		if(file==null){
			throw new Exception("传入了一个空文件");
		}
		//判断文件格式
		if(!fileName.endsWith(".xls")&&!fileName.endsWith(".xlsx")){
			throw new Exception("文件格式不对");
		}
		//判断文件大小
		if(file.length()>fileMaxSize){
			throw new Exception("文件太大");
		}
		String [] titleArry = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			//处理excel2003
			if(fileName.endsWith(".xls")){
				//把一张xls的数据表读到wb里
				HSSFWorkbook wb = new HSSFWorkbook(fis);
				//读取第一页,一般一个excel文件会有三个工作表，这里获取第一个工作表来进行操作
				HSSFSheet sheet = wb.getSheetAt(0);

				//需要读取的列数
				int col= 0;
				//取标题行数据
				HSSFRow row=sheet.getRow(0);
				col = row.getPhysicalNumberOfCells();
				titleArry = new String[col];
				for(short j=0;j<col;j++){
					if (AssertUtil.isEmpty(row.getCell(j))) break; //空单元格，截止扫描
					HSSFCell titleCell=row.getCell(j);
					if (AssertUtil.isEmpty(titleCell) || AssertUtil.isEmpty(titleCell.getStringCellValue())) break; //单元格为空字符，截止扫描
					titleArry[j] = titleCell.getStringCellValue();
				}
			}
			//处理excel2007
			if(fileName.endsWith(".xlsx")){
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				//读取第一页,一般一个excel文件会有三个工作表，这里获取第一个工作表来进行操作
				XSSFSheet sheet = wb.getSheetAt(0);
				//需要读取的列数
				int col= 0;
				//取标题行数据
				XSSFRow row=sheet.getRow(0);
				col = row.getPhysicalNumberOfCells();
				titleArry = new String[col];
				for(short j=0;j<col;j++){
					if (AssertUtil.isEmpty(row.getCell(j))) break; //空单元格，截止扫描
					XSSFCell titleCell=row.getCell(j);
					if (AssertUtil.isEmpty(titleCell) || AssertUtil.isEmpty(titleCell.getStringCellValue())) break; //单元格为空字符，截止扫描
					titleArry[j] = titleCell.getStringCellValue();
				}
			}
		} finally {
			if(fis !=null){
				fis.close();
			}
		}
		
		return titleArry;
	}
	
	public static void importExcel2003(List<Object> targetList,FileInputStream fis,Map<String, 
			Method> fieldMap,Class<?> clazz,IExcelService serv,String id,  Map<String,TbQyUserCustomOptionVO > optionVOMap)
			throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ParseException{
		Map<Short,String> titleMap=new HashMap<Short ,String>();
		//把一张xls的数据表读到wb里
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		//读取第一页,一般一个excel文件会有三个工作表，这里获取第一个工作表来进行操作
		HSSFSheet sheet = wb.getSheetAt(0);

		//需要读取的列数
		int col= 0;
		int entryCol = 0; //行内空列数
		HSSFRow row;
		//遍历所有行
		for(int i=0;i<sheet.getLastRowNum()+1;i++) {
			Map<String, String> itemVOMap = new HashMap<String, String>();
			//取第i行数据
			row = sheet.getRow(i);
			if (row == null) {
				break;
			}
			if (i == 0) {
				col = row.getPhysicalNumberOfCells();
				HSSFCell titleCell;
				for (short j = 0; j < col; j++) {
					titleCell = row.getCell(j);
					//title为空 则结束读取
					if (AssertUtil.isEmpty(titleCell) || AssertUtil.isEmpty(titleCell.getStringCellValue()) || j == maxCol) { //title为空往后的列不处理
						col = j;
						break;
					}
					titleMap.put(j, titleCell.getStringCellValue());
				}
				continue;
			}
			//新建clazz的实例，将每一行的数据写入此实例
			Object tObject = clazz.newInstance();
			entryCol = 0; //空列
			HSSFCell cell;
			//遍历第i行的列
			for (short j = 0; j < col; j++) {
				//得到第j列标题
				String titleString = titleMap.get(j);
				boolean boo = true;
				if (!fieldMap.containsKey(titleString)) {
					boo = false;//如果该列不是默认模板的，就看该列是不是属于自定义字段的
					if (optionVOMap.containsKey(titleString)) {
						boo = true;//如果该列属于自定义字段的
					}
				}
				//如果此列是需要的列，则继续操作
				if (boo) {
					//读取cell内容转换为字符串value
					cell = row.getCell(j);
					if (cell != null) {
						//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						String value = "";

						switch (cell.getCellType()) {
							//cell类型为字符串
							case HSSFCell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							//cell类型为数字（包含时间类型）
							case HSSFCell.CELL_TYPE_NUMERIC:
								if (!DateUtil.isCellDateFormatted(cell)) {
									//带中文的时间无法被识别为时间，特殊处理
									if ("阳历生日".equals(titleString) || "入职时间".equals(titleString)) {
										double d = cell.getNumericCellValue();
										Date date = DateUtil.getJavaDate(d);
										value = date.toString();
									} else {
										cell.setCellType(cell.CELL_TYPE_STRING);
										String temp = cell.getStringCellValue();
										//判断是否包含小数点，如果不含小数点，则已字符串读取；如果含小数点，则转换为Double类型字符串
										if (temp.indexOf(".") > -1) {
											value = String.valueOf(new Double(temp)).trim();
										} else {
											value = temp.trim();
										}
									}
								} else {
									value = String.valueOf(cell.getDateCellValue());
								}
								break;
							//cell类型为boolean型
							case HSSFCell.CELL_TYPE_BOOLEAN:
								value = (cell.getBooleanCellValue() == true ? "Y" : "N");
								break;
							// 导入时如果为公式生成的数据
							case HSSFCell.CELL_TYPE_FORMULA:
								try {
									value = String.valueOf(cell.getNumericCellValue());
								} catch (IllegalStateException e) {
									value = String.valueOf(cell.getRichStringCellValue());
								}
								break;
							case HSSFCell.CELL_TYPE_BLANK:
								value = "";
								break;
							case HSSFCell.CELL_TYPE_ERROR:
								value = "";
								break;
							default:
								value = "";
						}
						if ("".equals(value)) {
							entryCol++;
							continue;
						}
						//获取setter方法的参数类型，选择对应的方法将value放入tObject
						Method setMethod;
						String xclass;
						Type[] ts;
						if (optionVOMap.containsKey(titleString)) {//如果是自定义字段
							setMethod = (Method) fieldMap.get("自定义");
							xclass = "class java.lang.String";
						} else {
							setMethod = (Method) fieldMap.get(titleString);
							ts = setMethod.getGenericParameterTypes();
							xclass = ts[0].toString();
						}
						if (xclass.equals("class java.lang.String")) {
							if (optionVOMap.containsKey(titleString)) {//如果是自定义字段
								itemVOMap.put(optionVOMap.get(titleString).getId(), value);
								setMethod.invoke(tObject, itemVOMap);
							} else {//如果不是自定义字段
								setMethod.invoke(tObject, value);
							}
						} else if (xclass.equals("class java.util.Date")) {
							try {
								Date date = parseGreen(value);
								setMethod.invoke(tObject, date);
							} catch (ParseException e) {
								//maquanyang 2015-7-16 修改不是正确的时间格式时，没有错误提示
								//日期格式严格要求符合yyyy-MM-dd或yyyy/MMM/dd
								Date date = null;
								try {
									if (value.matches(checkDate2)) {//符合yyyy-MM-dd
										DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										date = format.parse(value + " 00:00:00");
										setMethod.invoke(tObject, date);
									} else if (value.matches(checkDate1)) {
										DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
										date = format.parse(value + " 00:00:00");
										setMethod.invoke(tObject, date);
									} else {
										setMethod = (Method) fieldMap.get(titleString + "(导入的数据)");
										setMethod.invoke(tObject, value);
									}
								} catch (ParseException e1) {
									setMethod = (Method) fieldMap.get(titleString + "(导入的数据)");
									setMethod.invoke(tObject, value);
								} catch (IllegalArgumentException e2) {
									setMethod = (Method) fieldMap.get(titleString + "(导入的数据)");
									setMethod.invoke(tObject, value);
								}
							}
						} else if (xclass.equals("class java.lang.Boolean")) {
							Boolean boolname = true;
							if (value.equals("N")) {
								boolname = false;
							}
							setMethod.invoke(tObject, boolname);
						} else if (xclass.equals("class java.lang.Integer")) {
							setMethod.invoke(tObject, new Integer(value));
						} else if (xclass.equals("class java.lang.Long")) {
							setMethod.invoke(tObject, new Long(value));
						} else if (xclass.equals("class java.lang.Float")) {
							Float d = Float.parseFloat(value);
							setMethod.invoke(tObject, d);
						} else if (xclass.equals("class java.lang.Double")) {
							double d = Double.parseDouble(value);
							setMethod.invoke(tObject, d);
						}
					} else {
						entryCol++;
					}
				}
			}
			if (entryCol == col)
				break;
			boolean flag = false;
			if (serv != null) {
				flag = serv.valid(tObject);
			} else {
				flag = true;
			}
			if (flag) {
				targetList.add(tObject);
			} else {
				//检测行内数据是否为空
				String s0 = (String) titleMap.get(0);
				String s1 = (String) titleMap.get(1);
				String s3 = (String) titleMap.get(3);
				//BatchImportThread.errorlist.add("第"+i+"行读取失败，请检查手机号或姓名是否为空<br />");
				ResultVO resultVO = ImportResultUtil.getResultObject(id);
				if (resultVO == null) {
					resultVO = new ResultVO();
				}
				List<String> errorlist = resultVO.getErrorlist();
				if (errorlist == null) {
					errorlist = new ArrayList<String>();
					if (s0 != null || s1 != null || s3 != null) {
						errorlist.add("第" + (i + 1) + "行读取失败，请检查手机号或姓名是否为空<br />");
					}
					resultVO.setErrorlist(errorlist);
				} else {
					if (s0 != null || s1 != null || s3 != null) {
						errorlist.add("第" + (i + 1) + "行读取失败，请检查手机号或姓名是否为空<br />");
					}
				}

				ImportResultUtil.putResultObject(id, resultVO);
			}

		}
	}
	
	public static void importExcel2007(List<Object> targetList,FileInputStream fis,
			Map<String, Method> fieldMap,Class<?> clazz,IExcelService serv,String id,  Map<String,TbQyUserCustomOptionVO > optionVOMap)
			throws IOException, InstantiationException, IllegalAccessException,
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
		for(int i=0;i<sheet.getLastRowNum()+1;i++) {
			Map<String,String> itemVOMap = new HashMap<String, String>();
			//取第i行数据
			XSSFRow row=sheet.getRow(i);
			if(row==null) {
				break;
			}
			//该行包含的单元格数既为列数
			if (i == 0) {
				col = row.getPhysicalNumberOfCells();
				XSSFCell titleCell;
				for (short j = 0; j < col; j++) {
					titleCell = row.getCell(j);
					//title为空 则结束读取
					if (AssertUtil.isEmpty(titleCell) || AssertUtil.isEmpty(titleCell.getStringCellValue()) || j == maxCol) { //title为空往后的列不处理
						col = j;
						break;
					}
					titleMap.put(j, titleCell.getStringCellValue());
				}
				continue;
			}
			//新建clazz的实例，将每一行的数据写入此实例
			Object tObject = clazz.newInstance();
			entryCol = 0;
			XSSFCell cell;
			//遍历第i行的列
			for (short j = 0; j < col; j++) {
				//得到第j列标题
				String titleString = (String) titleMap.get(j);
				boolean boo = true;
				if (!fieldMap.containsKey(titleString)) {
					boo = false;//如果该列不是默认模板的，就看该列是不是属于自定义字段的
					if (optionVOMap.containsKey(titleString)) {
						boo = true;//如果该列属于自定义字段的
					}
				}
				//如果此列是需要的列，则继续操作
				if (boo) {
					//读取cell内容转换为字符串value
					cell = row.getCell(j);
					if (cell != null) {
						//cell.setEncoding( Cell.ENCODING_UTF_16);
						String value = "";

						switch (cell.getCellType()) {
							//cell类型为字符串
							case XSSFCell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							//cell类型为数字
							case XSSFCell.CELL_TYPE_NUMERIC:    //数值
								if (!DateUtil.isCellDateFormatted(cell)) {
									//带中文的时间无法被识别为时间，特殊处理
									if ("阳历生日".equals(titleString) || "入职时间".equals(titleString)) {
										double d = cell.getNumericCellValue();
										Date date = DateUtil.getJavaDate(d);
										value = date.toString();
									} else {
										cell.setCellType(XSSFCell.CELL_TYPE_STRING);
										String temp = cell.getStringCellValue();
										//判断是否包含小数点，如果不含小数点，则已字符串读取；如果含小数点，则转换为Double类型字符串
										if (temp.indexOf(".") > -1) {
											value = String.valueOf(new Double(temp)).trim();
										} else {
											value = temp.trim();
										}
									}
								} else {
									value = String.valueOf(cell.getDateCellValue());
								}
								break;
							//cell类型为boolean型
							case XSSFCell.CELL_TYPE_BOOLEAN:
								value = (cell.getBooleanCellValue() == true ? "Y" : "N");
								break;
							// 导入时如果为公式生成的数据
							case XSSFCell.CELL_TYPE_FORMULA:
								try {
									value = String.valueOf(cell.getNumericCellValue());
								} catch (IllegalStateException e) {
									value = String.valueOf(cell.getRichStringCellValue());
								}
								break;
							case XSSFCell.CELL_TYPE_BLANK:
								value = "";
								break;
							case XSSFCell.CELL_TYPE_ERROR:
								value = "";
								break;
							default:
								value = "";
						}
						if ("".equals(value)) {
							entryCol++;
							continue;
						}

						//获取setter方法的参数类型，选择对应的方法将value放入tObject
						Method setMethod;
						String xclass;
						Type[] ts;
						if (optionVOMap.containsKey(titleString)) {//如果是自定义字段
							setMethod = (Method) fieldMap.get("自定义");
							xclass = "class java.lang.String";
						} else {
							setMethod = (Method) fieldMap.get(titleString);
							ts = setMethod.getGenericParameterTypes();
							xclass = ts[0].toString();
						}
						if (xclass.equals("class java.lang.String")) {
							if (optionVOMap.containsKey(titleString)) {//如果是自定义字段
								itemVOMap.put(optionVOMap.get(titleString).getId(), value);
								setMethod.invoke(tObject, itemVOMap);
							} else {//如果不是自定义字段
								setMethod.invoke(tObject, value);
							}
						} else if (xclass.equals("class java.util.Date")) {
							try {
								Date date = parseGreen(value);
								setMethod.invoke(tObject, date);
							} catch (ParseException e) {
								//maquanyang 2015-7-16 修改不是正确的时间格式时，没有错误提示
								//日期格式严格要求符合yyyy-MM-dd或yyyy/MMM/dd
								Date date = null;
								try {
									if (value.matches(checkDate2)) {//符合yyyy-MM-dd
										DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										date = format.parse(value + " 00:00:00");
										setMethod.invoke(tObject, date);
									} else if (value.matches(checkDate1)) {
										DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
										date = format.parse(value + " 00:00:00");
										setMethod.invoke(tObject, date);
									} else {
										setMethod = (Method) fieldMap.get(titleString + "(导入的数据)");
										setMethod.invoke(tObject, value);
									}
								} catch (ParseException e1) {
									setMethod = (Method) fieldMap.get(titleString + "(导入的数据)");
									setMethod.invoke(tObject, value);
								} catch (IllegalArgumentException e2) {
									setMethod = (Method) fieldMap.get(titleString + "(导入的数据)");
									setMethod.invoke(tObject, value);
								}
							}
						} else if (xclass.equals("class java.lang.Boolean")) {
							Boolean boolname = true;
							if (value.equals("N")) {
								boolname = false;
							}
							setMethod.invoke(tObject, boolname);
						} else if (xclass.equals("class java.lang.Integer")) {
							double d = Double.parseDouble(value);
							setMethod.invoke(tObject, (int) d);
						} else if (xclass.equals("class java.lang.Long")) {
							setMethod.invoke(tObject, new Long(value));
						} else if (xclass.equals("class java.lang.Float")) {
							Float d = Float.parseFloat(value);
							setMethod.invoke(tObject, d);
						} else if (xclass.equals("class java.lang.Double")) {
							double d = Double.parseDouble(value);
							setMethod.invoke(tObject, d);
						}
					} else {
						entryCol++;
					}
				}
			}
			if (entryCol == col) //空列等于总列数
				break;
			boolean flag = true;
			if (serv != null) {
				flag = serv.valid(tObject);
			}
			if (flag) {
				targetList.add(tObject);
			} else {
				//检测行内数据是否为空
				String s0 = (String) titleMap.get(0);
				String s1 = (String) titleMap.get(1);
				String s3 = (String) titleMap.get(3);

				//BatchImportThread.errorlist.add("第"+i+"行读取失败，请检查手机号或姓名是否为空<br />");
				ResultVO resultVO = ImportResultUtil.getResultObject(id);
				if (resultVO == null) {
					resultVO = new ResultVO();
				}
				List<String> errorlist = resultVO.getErrorlist();
				if (errorlist == null) {
					errorlist = new ArrayList<String>();
					if (s0 != null || s1 != null || s3 != null) {
						errorlist.add("第" + (i + 1) + "行读取失败，请检查手机号或姓名是否为空<br />");
					}
					resultVO.setErrorlist(errorlist);
				} else {
					if (s0 != null || s1 != null || s3 != null) {
						errorlist.add("第" + (i + 1) + "行读取失败，请检查手机号或姓名是否为空<br />");
					}
				}
				ImportResultUtil.putResultObject(id, resultVO);
			}
		}
	}
	
	/**
	 * Excel导出方法
	 * @param header
	 * @param list
	 * @param clazz
	 * @param exportFileName
	 */
	public static File exportForExcel(String[] header,List list,Class<?> clazz,String exportFileName) throws Exception{
		if(exportFileName==null||"".equals(exportFileName)){
			exportFileName = "exportForExcel.xls";
		}
		File file=new File(exportFileName);
		FileOutputStream out= null;
		
		out=new FileOutputStream(file);
		
		HSSFWorkbook wb = createWB(header, list, clazz);
		wb.write(out);
		out.close();

		return file;
		
	}
	
	public static HSSFWorkbook createWB(String[] header, List list, Class<?> clazz)
			throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, ParseException {
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("Sheet1");
		HSSFRow row=sheet.createRow(0);
		HSSFCell cell=null;
		//设置单元格类型
		HSSFCellStyle style=wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置内容居左
		Map<Short, String> titleMap=new HashMap<Short, String>();
		//设置头信息
		for(short i=0;i<header.length;i++){
			cell=row.createCell(i);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(header[i]);
			titleMap.put(i, header[i]);
		}
		
		//得到目标类的所有可导入的字段列表  
        Field field[] = clazz.getDeclaredFields();  
        //将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中  
        Map<String, Method> fieldMap = new HashMap<String, Method>();  
        // 循环读取所有字段  
        for (int i = 0; i < field.length; i++) {  
            Field f = field[i];  
            // 得到单个字段上的Annotation  
            TitleAnnotation title = f.getAnnotation(TitleAnnotation.class);  
            // 如果标识了Annotation
            if (title != null) {  
                // 构造设置了Annotation的字段的Getter方法  
                String fieldname = f.getName();  
                String getMethodName = "get"  
                        + fieldname.substring(0, 1).toUpperCase()  
                        + fieldname.substring(1);  
                // 构造调用的method，  
                Method getMethod = clazz.getMethod(getMethodName);  
                // 将这个method以Annotation的名字为key来存入。  
                fieldMap.put(title.titleName(), getMethod);  
            }  
        }
		
		Object record=clazz.newInstance();
		//遍历list
		for(int i=0;i<list.size();i++){
			record=list.get(i);
			//第一行为标题，从第二行开始写入数据
			row=sheet.createRow(i+1);
			for(short j=0;j<header.length;j++){
				cell=row.createCell(j);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);//设置编码
				cell.setCellStyle(style);	//设置style
				//sheet.autoSizeColumn(j);  //调整第j列宽度
				String titleString=(String)titleMap.get(j);
				if(fieldMap.containsKey(titleString)){
					Method getMethod=(Method)fieldMap.get(titleString);
					Object value=getMethod.invoke(record);
					//根据value类型选择不同的类型转换方法
					if(value instanceof String){
						cell.setCellValue(value.toString());
					}else if(value instanceof Double){
						cell.setCellValue((Double)value);
					}else if(value instanceof BigDecimal){
						cell.setCellValue(value.toString());
					}else if(value instanceof Integer){
						cell.setCellValue((Integer)value);
					}else if(value instanceof Float){
						cell.setCellValue((Float)value);
					}else if(value instanceof Long){
						cell.setCellValue((Long)value);
					}else if(value instanceof Boolean){
						cell.setCellValue((Boolean)value);
					}else if(value instanceof java.util.Date|value instanceof java.sql.Date){
						cell.setCellValue(parseGreen(value.toString()).toLocaleString());
					}
				}
			}
		}
		return wb;
	}
	/**
     * 将未指定格式的日期字符串转化成java.util.Date类型日期对象 <br>
     *
     * @param date,待转换的日期字符串
     * @return
     * @throws ParseException
     */
    public static Date parseStringToDate(String date) throws ParseException{
        Date result=null;
        String parse=date;
        parse=parse.replaceFirst("^[0-9]{4}([^0-9])", "yyyy$1");
        parse=parse.replaceFirst("^[0-9]{2}([^0-9])", "yy$1");
        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1MM$2");
        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}( ?)", "$1dd$2");
        parse=parse.replaceFirst("( )[0-9]{1,2}([^0-9])", "$1HH$2");
        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1mm$2");
        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1ss$2");
                
        DateFormat format=new SimpleDateFormat(parse);

        result=format.parse(date);
        return result;
    }
    public static Date parseGreen(String date) throws ParseException{
    	SimpleDateFormat f=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);
    	return f.parse(date);
    }
}