package cn.com.do1.component.building.building.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.com.do1.component.common.annotation.TitleAnnotation;
import cn.com.do1.component.common.service.IExcelService;
import cn.com.do1.component.common.vo.ResultVO;
import cn.com.do1.component.util.ImportResultUtil;

/**
 * Created by admin on 2017/2/15.
 */
public class CommonExcelUtil {
    /**
     * 文件大小限制为10M
     */
    public static long fileMaxSize=1024*1024*10;

    /**
     * 校验日期yyyy/MM/dd
     */
    public final static String checkDate1 = "^((([0-9]{4}|[1-9][0-9]{3})/(((0[13578]|1[02])/(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)/(0[1-9]|[12][0-9]|30))|(02/(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))/02/29))$";
    /**
     * 校验日期yyyy-MM-dd
     */
    public final static String checkDate2 = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$";

    /**
	 * Excel导入方法
	 * @param file 导入的文件
	 * @param fileName 文件名
	 * @param Class
	 * @param serv
	 * @return list
	 */
    public static List importForExcel(File file, String fileName, Class<?> clazz, IExcelService serv, String id) throws Exception{
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
        //获取目标类所有可导入的字段列表
        Field[] field = clazz.getDeclaredFields();
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
                importExcel2003(targetList,fis,fieldMap,clazz,serv,id);
            }
            //处理excel2007
            if(fileName.endsWith(".xlsx")){
                importExcel2007(targetList,fis,fieldMap,clazz,serv,id);
            }
        } finally {
            if(fis !=null){
                fis.close();
            }
        }
        return targetList;
    }


    /**
     * 读取xcel2003
     * @param targetList 将要导入的数据list
     * @param fis Excel文件
     * @param fieldMap Excel的标题文本
     * @param clazz 装载Excel数据的类对象
     * @param serv service实现类，需要继承IExcelService接口，并且实现类中需实现valid()方法
     * @param id
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    public static void importExcel2003(List<Object> targetList,FileInputStream fis,Map<String,Method> fieldMap,Class<?> clazz,
    		IExcelService serv,String id)throws IOException, InstantiationException, IllegalAccessException,
    		IllegalArgumentException, InvocationTargetException, ParseException {
        Map<Short,String> titleMap=new HashMap<Short ,String>();//Excel标题map
        //把一张xls的数据表读到wb里
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        //读取第一页,一般一个excel文件会有三个工作表，这里获取第一个工作表来进行操作
        HSSFSheet sheet = wb.getSheetAt(0);

        //需要读取的列数
        int col= 0;
        //遍历所有行
        for(int i=0;i<sheet.getLastRowNum()+1;i++) {
            //取第i行数据
            HSSFRow row=sheet.getRow(i);
            if(row!=null){
                if(i==0){ //Excel标题行
                    col = row.getPhysicalNumberOfCells();
                    for(short j=0;j<col;j++){
                        HSSFCell titleCell=row.getCell(j);
                        titleMap.put(j, titleCell.getStringCellValue());
                    }
                    continue;
                }
                Object tObject=clazz.newInstance(); //新建clazz的实例，将每一行的数据写入此实例
                for(short j=0;j<col;j++){//遍历第i行的列
                    String titleString = (String)titleMap.get(j); //得到第j列标题
                    boolean boo = true;
                    if(!fieldMap.containsKey(titleString)){
                        boo = false;//如果该列不是默认模板的，就看该列是不是属于自定义字段的
                    }
                    //如果此列是需要的列，则继续操作
                    if(boo){
                        HSSFCell cell=row.getCell(j);//读取cell内容转换为字符串value
                        if(cell!=null){
                            String value= "";
                            switch(cell.getCellType()){
                                case HSSFCell.CELL_TYPE_STRING: //cell类型为字符串
                                    value=cell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC: //cell类型为数字（包含时间类型）
                                    if(!DateUtil.isCellDateFormatted(cell)){
                                        if("阳历生日".equals(titleString) || "入职时间".equals(titleString)){ //带中文的时间无法被识别为时间，特殊处理
                                            double d = cell.getNumericCellValue();
                                            Date date = DateUtil.getJavaDate(d);
                                            value = date.toString();
                                        }else{
                                            cell.setCellType(cell.CELL_TYPE_STRING);
                                            String temp = cell.getStringCellValue();
                                            if(temp.indexOf(".")>-1){ //判断是否包含小数点，如果不含小数点，则已字符串读取；如果含小数点，则转换为Double类型字符串
                                                value = String.valueOf(new Double(temp)).trim();
                                            }else{
                                                value = temp.trim();
                                            }
                                        }
                                    }else{
                                        value = String.valueOf(cell.getDateCellValue());
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_BOOLEAN: //cell类型为boolean型
                                    value=(cell.getBooleanCellValue()==true?"Y":"N");
                                    break;
                                case HSSFCell.CELL_TYPE_FORMULA: // 导入时如果为公式生成的数据
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
                            if("".equals(value))
                                continue;
                            //获取setter方法的参数类型，选择对应的方法将value放入tObject
                            Method setMethod;
                            String xclass;
                            Type[] ts;
                            setMethod=(Method)fieldMap.get(titleString);
                            ts = setMethod.getGenericParameterTypes();
                            xclass =ts[0].toString();
                            if(xclass.equals("class java.lang.String")){
                                setMethod.invoke(tObject, value);
                            }else if(xclass. equals("class java.util.Date")){
                                try{
                                    Date date = parseGreen(value);
                                    setMethod.invoke(tObject,date);
                                }catch(ParseException e){
                                    //maquanyang 2015-7-16 修改不是正确的时间格式时，没有错误提示
                                    //日期格式严格要求符合yyyy-MM-dd或yyyy/MMM/dd
                                    Date date = null;
                                    try{
                                        if(value.matches(checkDate2)){//符合yyyy-MM-dd
                                            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            date = format.parse(value + " 00:00:00");
                                            setMethod.invoke(tObject,date);
                                        }else if(value.matches(checkDate1)){
                                            DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            date = format.parse(value + " 00:00:00");
                                            setMethod.invoke(tObject,date);
                                        }else{
                                            setMethod=(Method)fieldMap.get(titleString+"(导入的数据)");
                                            setMethod.invoke(tObject,value);
                                        }
                                    }catch(ParseException e1){
                                        setMethod=(Method)fieldMap.get(titleString+"(导入的数据)");
                                        setMethod.invoke(tObject,value);
                                    }catch(IllegalArgumentException e2){
                                        setMethod=(Method)fieldMap.get(titleString+"(导入的数据)");
                                        setMethod.invoke(tObject,value);
                                    }
                                }
                            }else if(xclass. equals("class java.lang.Boolean")){
                                Boolean boolname=true;
                                if(value.equals("N")){
                                    boolname=false;
                                }
                                setMethod.invoke(tObject,boolname);
                            }else if(xclass. equals("class java.lang.Integer")){
                                setMethod.invoke(tObject,new Integer( value));
                            }else if(xclass. equals("class java.lang.Long")){
                                setMethod.invoke(tObject,new Long( value));
                            }else if(xclass.equals("class java.lang.Float")){
                                Float d= Float.parseFloat(value);
                                setMethod.invoke(tObject, d);
                            }else if(xclass.equals("class java.lang.Double")){
                                double d= Double.parseDouble(value);
                                setMethod.invoke(tObject, d);
                            }
                        }
                    }
                }
                targetList.add(tObject);
            }
        }
    }

    /**
     * 读取Excel2007
     * @param targetList
     * @param fis
     * @param fieldMap
     * @param clazz
     * @param serv
     * @param id
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    public static void importExcel2007(List<Object> targetList,FileInputStream fis,Map<String, Method> fieldMap,Class<?> clazz,IExcelService serv,String id)
                                throws IOException, InstantiationException, IllegalAccessException,IllegalArgumentException, InvocationTargetException, ParseException{
        Map<Short,String> titleMap=new HashMap<Short ,String>();
        //把一张xlsx的数据表读到wb里
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        //读取第一页,一般一个excel文件会有三个工作表，这里获取第一个工作表来进行操作
        XSSFSheet sheet = wb.getSheetAt(0);
        //需要处理的列数
        int col= 0;
        //遍历所有行
        for(int i=0;i<sheet.getLastRowNum()+1;i++) {
            //取第i行数据
            XSSFRow row=sheet.getRow(i);
            if(row!=null){
                //该行包含的单元格数既为列数
                if(i==0){
                    col=row.getPhysicalNumberOfCells();
                    for(short j=0;j<col;j++){
                        XSSFCell titleCell=row.getCell(j);
                        titleMap.put(j, titleCell.getStringCellValue());
                    }
                    continue;
                }
                //新建clazz的实例，将每一行的数据写入此实例
                Object tObject=clazz.newInstance();
                //遍历第i行的列
                for(short j=0;j<col;j++){
                    //得到第j列标题
                    String titleString = (String)titleMap.get(j);
                    boolean boo = true;
                    if(!fieldMap.containsKey(titleString)){
                        boo = false;//如果该列不是默认模板的，就看该列是不是属于自定义字段的
                    }
                    //如果此列是需要的列，则继续操作
                    if(boo){
                        //读取cell内容转换为字符串value
                        XSSFCell cell=row.getCell(j);
                        if(cell!=null){
                            String value= "";
                            switch(cell.getCellType()){
                                //cell类型为字符串
                                case  XSSFCell.CELL_TYPE_STRING:
                                    value=cell.getStringCellValue();
                                    break;
                                //cell类型为数字
                                case  XSSFCell.CELL_TYPE_NUMERIC:    //数值
                                    if(!DateUtil.isCellDateFormatted(cell)){
                                        //带中文的时间无法被识别为时间，特殊处理
                                        if("阳历生日".equals(titleString) || "入职时间".equals(titleString)){
                                            double d = cell.getNumericCellValue();
                                            Date date = DateUtil.getJavaDate(d);
                                            value = date.toString();
                                        }else{
                                            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                                            String temp = cell.getStringCellValue();
                                            //判断是否包含小数点，如果不含小数点，则已字符串读取；如果含小数点，则转换为Double类型字符串
                                            if(temp.indexOf(".")>-1){
                                                value = String.valueOf(new Double(temp)).trim();
                                            }else{
                                                value = temp.trim();
                                            }
                                        }
                                    }else{
                                        value = String.valueOf(cell.getDateCellValue());
                                    }
                                    break;
                                //cell类型为boolean型
                                case  XSSFCell.CELL_TYPE_BOOLEAN:
                                    value=(cell.getBooleanCellValue()==true?"Y":"N");
                                    break;
                                // 导入时如果为公式生成的数据
                                case  XSSFCell.CELL_TYPE_FORMULA:
                                    try {
                                        value = String.valueOf(cell.getNumericCellValue());
                                    } catch (IllegalStateException e) {
                                        value = String.valueOf(cell.getRichStringCellValue());
                                    }
                                    break;
                                case  XSSFCell.CELL_TYPE_BLANK:
                                    value = "";
                                    break;
                                case  XSSFCell.CELL_TYPE_ERROR:
                                    value = "";
                                    break;
                                default:
                                    value = "";
                            }
                            if("".equals(value))
                                continue ;
                            //获取setter方法的参数类型，选择对应的方法将value放入tObject
                            Method setMethod;
                            String xclass;
                            Type[] ts;
                            setMethod=(Method)fieldMap.get(titleString);
                            ts = setMethod.getGenericParameterTypes();
                            xclass =ts[0].toString();
                            if(xclass.equals("class java.lang.String")){
                                setMethod.invoke(tObject, value);
                            }else if(xclass.equals("class java.util.Date")){
                                try{
                                    Date date = parseGreen(value);
                                    setMethod.invoke(tObject,date);
                                }catch(ParseException e){
                                    //maquanyang 2015-7-16 修改不是正确的时间格式时，没有错误提示
                                    //日期格式严格要求符合yyyy-MM-dd或yyyy/MMM/dd
                                    Date date = null;
                                    try{
                                        if(value.matches(checkDate2)){//符合yyyy-MM-dd
                                            DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            date = format.parse(value + " 00:00:00");
                                            setMethod.invoke(tObject,date);
                                        }else if(value.matches(checkDate1)){
                                            DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            date = format.parse(value + " 00:00:00");
                                            setMethod.invoke(tObject,date);
                                        }else{
                                            setMethod=(Method)fieldMap.get(titleString+"(导入的数据)");
                                            setMethod.invoke(tObject,value);
                                        }
                                    }catch(ParseException e1){
                                        setMethod=(Method)fieldMap.get(titleString+"(导入的数据)");
                                        setMethod.invoke(tObject,value);
                                    }catch(IllegalArgumentException e2){
                                        setMethod=(Method)fieldMap.get(titleString+"(导入的数据)");
                                        setMethod.invoke(tObject,value);
                                    }
                                }
                            }else if(xclass. equals("class java.lang.Boolean")){
                                Boolean boolname=true;
                                if(value.equals("N")){
                                    boolname=false;
                                }
                                setMethod.invoke(tObject,boolname );
                            }else if(xclass. equals("class java.lang.Integer")){
                                double d= Double.parseDouble(value);
                                setMethod.invoke(tObject,(int)d);
                            }else if(xclass. equals("class java.lang.Long")){
                                setMethod.invoke(tObject,new Long( value));
                            }else if(xclass.equals("class java.lang.Float")){
                                Float d= Float.parseFloat(value);
                                setMethod.invoke(tObject, d);
                            }else if(xclass.equals("class java.lang.Double")){
                                double d= Double.parseDouble(value);
                                setMethod.invoke(tObject, d);
                            }
                        }
                    }
                }
                boolean flag = true;
                if(serv != null){
                    flag=serv.valid(tObject);
                }
                if(flag){
                    targetList.add(tObject);
                }else{
//                    recordError(id,i);
                }
            }
        }
    }

    /**
     * 记录错误
     * @param id
     * @param i
     */
    private static void recordError(String id,int i) {
        ResultVO resultVO = ImportResultUtil.getResultObject(id);
        if(resultVO == null){
            resultVO = new ResultVO();
        }
        List<String> errorlist = resultVO.getErrorlist();
        if(errorlist == null){
            errorlist = new ArrayList<String>();
        }
        errorlist.add("第"+(i+1)+"行读取失败!");
        resultVO.setErrorlist(errorlist);
        ImportResultUtil.putResultObject(id, resultVO);
    }


    public static Date parseGreen(String date) throws ParseException{
        SimpleDateFormat f=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);
        return f.parse(date);
    }
    
    public static String [] getExcelTitle(File file,String fileName) throws Exception{
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
					HSSFCell titleCell=row.getCell(j);
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
					XSSFCell titleCell=row.getCell(j);
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

    /**
     * 设置Excel模版标题
     * @param writeRow
     */
	public static void setFirstHead(Row writeRow,String[] titleTemplate) {
		for (int i = 0; i < titleTemplate.length; i++) {
            writeRow.createCell(i).setCellValue(titleTemplate[i]);
        }
	}
}
