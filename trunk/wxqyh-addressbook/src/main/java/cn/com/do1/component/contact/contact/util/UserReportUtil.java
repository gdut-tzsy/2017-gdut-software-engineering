package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserOptionDesVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.managesetting.managesetting.util.IndustryUtil;
import cn.com.do1.component.trade.model.VipUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;

/**
 * Created by liyixin on 2016/12/8.
 */
public class UserReportUtil {

    public static final String NO_FIND_DEPART = "该部门不存在，请检查是否存在该部门";

    public static final String ERROR_REPORT_STATE = "-1";

    /**
     * 设置excel表格的第二页
     * @param writeWB
     * @param writeSheet
     * @param writeRow
     * @param optionVOs
     * @param org
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-8
     * @version 1.0
     */
    public static void seCondUtil(HSSFWorkbook writeWB, Sheet writeSheet, Row writeRow, List<TbQyUserCustomOptionVO> optionVOs, UserOrgVO org) throws BaseException, Exception{
        writeSheet = writeWB.createSheet("通讯录模板填写说明");
        //合並單元格
        writeSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        writeSheet.addMergedRegion(new CellRangeAddress(4, 6, 3, 3));
        //设置列宽
        writeSheet.setColumnWidth(1, 25000);
        writeSheet.setColumnWidth(3, 8000);
        //第一行
        writeRow = writeSheet.createRow(0);
        writeRow.setHeight((short)500);

        writeRow.createCell(0).setCellValue("请先阅读此说明，然后在【通讯录】表中录入数据");

        writeRow = writeSheet.createRow(1);
        writeRow.createCell(0).setCellValue("字段");
        writeRow.createCell(1).setCellValue("说明");
        writeRow.createCell(2).setCellValue("是否必填");
        int rowIndex = 0;
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //教育版
            writeRow = writeSheet.createRow(2 + rowIndex);
            writeRow.createCell(0).setCellValue("成员类型");
            writeRow.createCell(1).setCellValue("可填写‘家长’或‘教师/职工’，不填则默认为空");
            writeRow.createCell(2).setCellValue("否");
            rowIndex = 1;
        }
        writeRow = writeSheet.createRow(2 + rowIndex);
        writeRow.createCell(0).setCellValue("姓名");
        writeRow.createCell(1).setCellValue("员工姓名");
        writeRow.createCell(2).setCellValue("是");

        writeRow = writeSheet.createRow(3 + rowIndex);
        writeRow.createCell(0).setCellValue("账号");
        writeRow.createCell(1).setCellValue("不能重复，联系人唯一标识，提交后不可更改，只能填写字母、数字、_或-，长度小于64个字符。如果没有账号，可以以企业名称简写+下划线+手机号码，如do1_13800138000；或者使用自增长的数字");
        writeRow.createCell(2).setCellValue("是");

        writeRow = writeSheet.createRow(4 + rowIndex);
        writeRow.createCell(0).setCellValue("微信号");
        writeRow.createCell(1).setCellValue("企业内必须唯一 ，微信号/手机号码/邮箱 三者不能同时为空");
        writeRow.createCell(2).setCellValue("否");
        writeRow.createCell(3).setCellValue("身份验证信息(这三个信息不可同时为空)");

        writeRow = writeSheet.createRow(5 + rowIndex);
        writeRow.createCell(0).setCellValue("手机号码");
        writeRow.createCell(1).setCellValue("手机号码。企业内必须唯一，微信号/手机号码/邮箱 三者不能同时为空");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(6 + rowIndex);
        writeRow.createCell(0).setCellValue("邮箱");
        writeRow.createCell(1).setCellValue("电子邮箱。企业内必须唯一，微信号/手机号码/邮箱 三者不能同时为空");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(7 + rowIndex);
        writeRow.createCell(0).setCellValue("电话");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(8 + rowIndex);
        writeRow.createCell(0).setCellValue("工作部门");
        if (IndustryUtil.isEduVersion(org.getOrgId())) { //教育版
            writeRow.createCell(1).setCellValue("支持多层导入：只填工作部门，各层间用“->”分隔；如果一个人属于多个部门各部门间以英文“;”隔开 (部门名称中不能包含中文“；”)；\n" +
                    "教育行业版中最低层部门格式为“xxxx级xx班(X年级xx班)”，则默认为班级部门（其中“()”为英文括号）");
        } else {
            writeRow.createCell(1).setCellValue("支持多层导入：只填工作部门，各层间用“->”分隔；如果一个人属于多个部门各部门间以英文“;”隔开 (部门名称中不能包含中文“；”)");
        }
        writeRow.createCell(2).setCellValue("是");

        writeRow = writeSheet.createRow(9 + rowIndex);
        writeRow.createCell(0).setCellValue("性别");
        writeRow.createCell(1).setCellValue("男或者女，为空表示未知");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(10 + rowIndex);
        writeRow.createCell(0).setCellValue("工作职位");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(11 + rowIndex);
        writeRow.createCell(0).setCellValue("阳历生日");
        writeRow.createCell(1).setCellValue("格式为：yyyy/MM/dd 或者为：yyyy-MM-dd，此单元格必须为文本类型，否则导入失败");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(12 + rowIndex);
        writeRow.createCell(0).setCellValue("地址");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(13 + rowIndex);
        writeRow.createCell(0).setCellValue("QQ号");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(14 + rowIndex);
        writeRow.createCell(0).setCellValue("备注");
        writeRow.createCell(1).setCellValue("备注信息");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(15 + rowIndex);
        writeRow.createCell(0).setCellValue("农历生日");
        writeRow.createCell(1).setCellValue("格式为：MM-dd此单元格必须为文本类型，否则导入失败");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(16 + rowIndex);
        writeRow.createCell(0).setCellValue("昵称");
        writeRow.createCell(1).setCellValue("用户昵称");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(17 + rowIndex);
        writeRow.createCell(0).setCellValue("电话2");
        writeRow.createCell(1).setCellValue("电话/手机号码2");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(18 + rowIndex);
        writeRow.createCell(0).setCellValue("入职时间");
        writeRow.createCell(1).setCellValue("格式为：yyyy/MM/dd 或者为：yyyy-MM-dd，此单元格必须为文本类型，否则导入失败");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(19 + rowIndex);
        writeRow.createCell(0).setCellValue("生日提醒");
        writeRow.createCell(1).setCellValue("只能填：“按阳历”或“按农历”其中一个，为空或者填其他一律默认按阳历发送生日提醒");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(20 + rowIndex);
        writeRow.createCell(0).setCellValue("身份证");
        writeRow.createCell(1).setCellValue("必须少于25位");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(21 + rowIndex);
        writeRow.createCell(0).setCellValue("证件类型");
        writeRow.createCell(1).setCellValue("证件类型标题。如果不对应，会关联不上，但不会报错。");
        writeRow.createCell(2).setCellValue("否");

        writeRow = writeSheet.createRow(22 + rowIndex);
        writeRow.createCell(0).setCellValue("证件内容");
        writeRow.createCell(1).setCellValue("证件号码");
        writeRow.createCell(2).setCellValue("否");
        if(VipUtil.isQwVip(org.getOrgId())) {
            writeRow = writeSheet.createRow(23 + rowIndex);
            writeRow.createCell(0).setCellValue("排序");
            writeRow.createCell(1).setCellValue("可用于将领导排在通讯录最前面，数字越小排序越前");
            writeRow.createCell(2).setCellValue("否");
            writeRow = writeSheet.createRow(24 + rowIndex);
            writeRow.createCell(0).setCellValue("保密");
            writeRow.createCell(1).setCellValue("填：是/否。 用于隐藏该成员的微信号、手机号、邮箱，隐藏后微信端不显示该成员的微信号、手机号、邮箱");
            writeRow.createCell(2).setCellValue("否");
        }else{
            writeRow = writeSheet.createRow(23 + rowIndex);
            writeRow.createCell(0).setCellValue("排序");
            writeRow.createCell(1).setCellValue("可用于将领导排在通讯录最前面，数字越小排序越前,仅VIP用户使用");
            writeRow.createCell(2).setCellValue("否");
            writeRow = writeSheet.createRow(24 + rowIndex);
            writeRow.createCell(0).setCellValue("保密");
            writeRow.createCell(1).setCellValue("填：是/否。 用于隐藏该成员的微信号、手机号、邮箱，隐藏后微信端不显示该成员的微信号、手机号、邮箱,仅VIP用户使用");
            writeRow.createCell(2).setCellValue("否");
        }
        if(optionVOs.size() > 0){
            for(int i = 0; i < optionVOs.size(); i ++){
                writeRow = writeSheet.createRow(25 + rowIndex + i);
                writeRow.createCell(0).setCellValue(optionVOs.get(i).getOptionName());
                if(null != optionVOs.get(i).getDesVos()){
                    if(ContactCustomUtil.TYPE_DROP_DOWN.equals(optionVOs.get(i).getType())){//下拉
                        String downContent = "下拉框选择:" + getoptionNames(optionVOs.get(i).getDesVos());
                        writeRow.createCell(1).setCellValue(downContent);
                    } else if (ContactCustomUtil.TYPE_MULTISELECT.equals(optionVOs.get(i).getType())) {//多选
                        String multSelectContent = "多选框选择:" + getoptionNames(optionVOs.get(i).getDesVos());
                        writeRow.createCell(1).setCellValue(multSelectContent);
                    } else {
                        writeRow.createCell(1).setCellValue(optionVOs.get(i).getDesVos().get(0).getName());
                    }
                }
                else{
                    writeRow.createCell(1).setCellValue("");
                }
                if("0".equals(optionVOs.get(i).getIsMust())) {
                    writeRow.createCell(2).setCellValue("否");
                }
                else {
                    writeRow.createCell(2).setCellValue("是");
                }
            }
        }
    }

    /**
     * 返回下拉选项
     *
     * @param desVos
     * @return
     */
    public static String getoptionNames(List<TbQyUserOptionDesVO> desVos) {
        StringBuffer names = new StringBuffer();
        for (TbQyUserOptionDesVO desVO : desVos) {
            names.append("," + desVO.getName());
        }
        if (names.length() > 0) {
            names.deleteCharAt(0);
        }
        return names.toString();
    }

    /**
     * 设置excel表格第一页的表头
     * @param titleMap
     * @param writeRow
     * @param isDimission
     * @param optionVOs
     * @param needStatus
     * @param orgId
     * @throws BaseException 这是一个异常
     * @throws Exception 这是一个异常
     * @author liyixin
     * @2016-12-8
     * @version 1.0
     */
    public static void setFirstHead(Map<String, Integer> titleMap, Row writeRow, boolean isDimission, List<TbQyUserCustomOptionVO> optionVOs, boolean needStatus, String orgId) throws BaseException, Exception {
        int titleIndex = 0;
        if (IndustryUtil.isEduVersion(orgId)) { //教育版模板
            titleMap.put("attribute", titleIndex);
            writeRow.createCell(titleIndex).setCellValue("成员类型");
            titleIndex++;
        }
        titleMap.put("persoonName", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("姓名");
        titleIndex++;
        titleMap.put("wxUserId", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("账号");
        titleIndex++;
        titleMap.put("weixinNum", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("微信号");
        titleIndex++;
        titleMap.put("mobile", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("手机号码");
        titleIndex++;
        titleMap.put("email", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("邮箱");
        titleIndex++;
        titleMap.put("shorMobile", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("电话");
        titleIndex++;
        titleMap.put("deptFullName", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("工作部门");
        titleIndex++;
        titleMap.put("sex", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("性别");
        titleIndex++;
        titleMap.put("position", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("工作职位");
        titleIndex++;
        titleMap.put("birthday", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("阳历生日");
        titleIndex++;
        titleMap.put("address", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("地址");
        titleIndex++;
        titleMap.put("qqNum", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("QQ号");
        titleIndex++;
        titleMap.put("identity", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("身份证");
        titleIndex++;
        titleMap.put("certificateTypeTitle", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("证件类型");
        titleIndex++;
        titleMap.put("certificateContent", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("证件内容");
        titleIndex++;
        titleMap.put("mark", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("备注");
        titleIndex++;
        titleMap.put("lunarCalendar", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("农历生日");
        titleIndex++;
        titleMap.put("nickName", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("昵称");
        titleIndex++;
        titleMap.put("phone", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("电话2");
        titleIndex++;
        titleMap.put("entryTime", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("入职时间");
        titleIndex++;
        titleMap.put("remindType", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("生日提醒");
        if(needStatus) {
            titleIndex++;
            titleMap.put("userStatus", titleIndex);
            writeRow.createCell(titleIndex).setCellValue("状态");
        }
        titleIndex++;
        titleMap.put("isTop", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("排序");
        titleIndex++;
        titleMap.put("secrecy", titleIndex);
        writeRow.createCell(titleIndex).setCellValue("保密");
        // 搜索离职
        if (isDimission) {
            titleIndex++;
            titleMap.put("leaveTime", titleIndex);
            writeRow.createCell(titleIndex).setCellValue("离职时间");
            titleIndex++;
            titleMap.put("leaveRemark", titleIndex);
            writeRow.createCell(titleIndex).setCellValue("离职原因");
        }
        if(optionVOs.size() > 0){
            for(TbQyUserCustomOptionVO optionVO : optionVOs){
                titleIndex++;
                titleMap.put(optionVO.getId(), titleIndex);
                writeRow.createCell(titleIndex).setCellValue(optionVO.getOptionName());
            }
        }
    }


}
