package cn.com.do1.component.contact.student.util;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.component.addressbook.contact.vo.ImportVO;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.contact.contact.util.ContactCustomUtil;
import cn.com.do1.component.contact.contact.util.ExcelUtil;
import cn.com.do1.component.contact.contact.util.UserReportUtil;
import cn.com.do1.component.contact.student.vo.ExportStudentVO;
import cn.com.do1.component.contact.student.vo.ImportErrorStudentVO;
import cn.com.do1.component.contact.student.vo.ImportStudentVO;
import cn.com.do1.component.trade.model.VipUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hejinjiao on 2017/1/19.
 */
public class StudentReportUtil {
    /**
     * 学生信息-父母信息导入模板
     *
     * @param writeSheet the write sheet
     * @param writeRow   the write row
     * @param hasParent  是否存在父母信息
     * @param allFiled   通讯录所有字段
     * @param isError    是否存在错误提示
     * @param optionVOs  通讯录自定义字段
     * @param titleMap   字段排序号
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    public static void setFiledHead(Sheet writeSheet, Row writeRow, boolean hasParent, boolean allFiled, boolean isError,
                                    List<TbQyUserCustomOptionVO> optionVOs, Map<String, Integer> titleMap)
            throws BaseException, Exception {
        //合並單元格
        writeSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));
        if (hasParent) { //存在父母信息
            writeSheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 18));
        }
        //第一行
        writeRow = writeSheet.createRow(0);
        writeRow.createCell(0).setCellValue("学生信息");
        if (hasParent) { //存在父母信息
            writeRow.createCell(12).setCellValue("家长信息");
        }

        writeRow = writeSheet.createRow(1);// 第二行字段
        int titleIndex = 0;
        writeRow.createCell(titleIndex).setCellValue("学生姓名");
        titleMap.put("学生姓名", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("班级");
        titleMap.put("班级", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("入学登记手机号");
        titleMap.put("入学登记手机号", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("本校教师子女");
        titleMap.put("本校教师子女", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("学生性别");
        titleMap.put("学生性别", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("身份证号码");
        titleMap.put("身份证号码", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("出生年月");
        titleMap.put("出生年月", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("学生微信号");
        titleMap.put("学生微信号", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("学生手机号");
        titleMap.put("学生手机号", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("学生邮箱");
        titleMap.put("学生邮箱", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("家长/监护人");
        titleMap.put("家长/监护人", titleIndex);

        titleIndex++;
        writeRow.createCell(titleIndex).setCellValue("备注");
        titleMap.put("备注", titleIndex);

        if (hasParent) { //存在父母信息
            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("成员类型");
            titleMap.put("attribute", titleIndex);

            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("姓名");
            titleMap.put("personName", titleIndex);

            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("账号");
            titleMap.put("wxUserId", titleIndex);

            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("微信号");
            titleMap.put("weixinNum", titleIndex);

            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("手机号码");
            titleMap.put("mobile", titleIndex);

            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("邮箱");
            titleMap.put("email", titleIndex);

            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("工作部门");
            titleMap.put("deptFullName", titleIndex);
            // 通讯录中所有字段
            if (allFiled) {
                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("电话");
                titleMap.put("shorMobile", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("性别");
                titleMap.put("sex", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("工作职位");
                titleMap.put("position", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("阳历生日");
                titleMap.put("birthday", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("地址");
                titleMap.put("address", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("QQ号");
                titleMap.put("qqNum", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("身份证");
                titleMap.put("identity", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("证件类型");
                titleMap.put("certificateTypeTitle", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("证件内容");
                titleMap.put("certificateContent", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("备注");
                titleMap.put("mark", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("农历生日");
                titleMap.put("lunarCalendar", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("昵称");
                titleMap.put("nickName", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("电话2");
                titleMap.put("phone", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("入职时间");
                titleMap.put("entryTime", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("生日提醒");
                titleMap.put("remindType", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("排序");
                titleMap.put("isTop", titleIndex);

                titleIndex++;
                writeRow.createCell(titleIndex).setCellValue("保密");
                titleMap.put("secrecy", titleIndex);

                if (optionVOs != null && optionVOs.size() > 0) {
                    for (TbQyUserCustomOptionVO optionVO : optionVOs) {
                        titleIndex++;
                        writeRow.createCell(titleIndex).setCellValue(optionVO.getOptionName());
                        titleMap.put(optionVO.getId(), titleIndex);
                    }
                }
            }
        }

        if (isError) {
            titleIndex++;
            writeRow.createCell(titleIndex).setCellValue("错误提示");
            titleMap.put("错误提示", titleIndex);
        }
    }

    /**
     * 模板中字段说明
     *
     * @param writeWB   the write wb
     * @param optionVOs the option v os
     * @param orgId     the org id
     * @param hasParent the has parent
     * @param allFiled  the all filed
     * @throws BaseException the base exception
     * @throws Exception     the exception
     */
    public static void setFieldMark(HSSFWorkbook writeWB, List<TbQyUserCustomOptionVO> optionVOs, String orgId,
                                    boolean hasParent, boolean allFiled)
            throws BaseException, Exception {
        Sheet writeSheet = writeWB.createSheet("学生导入模板填写说明");
        //设置列宽
        writeSheet.setColumnWidth(1, 25000);
        writeSheet.setColumnWidth(3, 8000);
        int rowIndex = 0;
        Row writeRow = writeSheet.createRow(rowIndex);// 第一行
        writeRow.createCell(0).setCellValue("字段");
        writeRow.createCell(1).setCellValue("说明");
        writeRow.createCell(2).setCellValue("是否必填");

        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("学生姓名");
        writeRow.createCell(1).setCellValue("学生姓名");
        writeRow.createCell(2).setCellValue("是");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("班级");
        writeRow.createCell(1).setCellValue("学生所在班级部门的全路径，多级部门各层间用“->”分隔；\n" +
                "最低级部门必须为班级部门,其他层部门默认为行政部门；\n" +
                "班级部门格式为“xxxx级xx班(X年级xx班)”，其中“()”是英文符号");
        writeRow.createCell(2).setCellValue("是");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("入学登记手机号");
        writeRow.createCell(1).setCellValue("非常重要，用于验证学生是否重名");
        writeRow.createCell(2).setCellValue("是");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("本校教师子女");
        writeRow.createCell(1).setCellValue("选择“是”或“否”，默认为“否”");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("学生性别");
        writeRow.createCell(1).setCellValue("男/女，二选一，默认为空");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("身份证号码");
        writeRow.createCell(1).setCellValue("学生身份证号码，可查看户口簿，必须少于25位");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("出生年月");
        writeRow.createCell(1).setCellValue("格式为：YYYY-MM此单元格必须为文本类型，否则导入失败");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("学生微信号");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("学生手机号");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("学生邮箱");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("家长/监护人");
        writeRow.createCell(1).setCellValue("支持多个导入；\n" +
                "账号需唯一，用于匹配家长与孩子关系（必须先在通讯录管理导入家长信息）；\n" +
                "格式如父亲,账号1;母亲,账号2\n" +
                "\",\"“;”必须英文格式；\n" +
                "身份可选父亲、母亲、爷爷、奶奶、外公、外婆、其他监护人");
        writeRow.createCell(2).setCellValue("否");
        rowIndex++;
        writeRow = writeSheet.createRow(rowIndex);
        writeRow.createCell(0).setCellValue("学生备注");
        writeRow.createCell(1).setCellValue("");
        writeRow.createCell(2).setCellValue("否");
        if (hasParent) {
            rowIndex++;
            writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("成员类型");
            writeRow.createCell(1).setCellValue("可填写‘家长’或‘教师/职工’，不填则默认为空");
            writeRow.createCell(2).setCellValue("否");
            rowIndex++;
            writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("姓名");
            writeRow.createCell(1).setCellValue("员工姓名");
            writeRow.createCell(2).setCellValue("是");
            rowIndex++;
            writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("账号");
            writeRow.createCell(1).setCellValue("不能重复，联系人唯一标识，提交后不可更改，只能填写字母、数字、_或-，长度小于64个字符。如果没有账号，可以以企业名称简写+下划线+手机号码，如do1_13800138000；或者使用自增长的数字");
            writeRow.createCell(2).setCellValue("是");
            rowIndex++;
            writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("微信号");
            writeRow.createCell(1).setCellValue("企业内必须唯一 ，微信号/手机号码/邮箱 三者不能同时为空");
            writeRow.createCell(2).setCellValue("否");
            writeRow.createCell(3).setCellValue("身份验证信息(这三个信息不可同时为空)");
            rowIndex++;
            writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("手机号码");
            writeRow.createCell(1).setCellValue("手机号码。企业内必须唯一，微信号/手机号码/邮箱 三者不能同时为空");
            writeRow.createCell(2).setCellValue("否");
            rowIndex++;
            writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("邮箱");
            writeRow.createCell(1).setCellValue("电子邮箱。企业内必须唯一，微信号/手机号码/邮箱 三者不能同时为空");
            writeRow.createCell(2).setCellValue("否");
            rowIndex++;
            writeRow = writeSheet.createRow(rowIndex);
            writeRow.createCell(0).setCellValue("工作部门");
            writeRow.createCell(1).setCellValue("支持多层导入：只填工作部门，各层间用“->”分隔；如果一个人属于多个部门各部门间以英文“;”隔开 (部门名称中不能包含中文“；”)；\n" +
                    "教育行业版中最低层部门格式为“xxxx级xx班(X年级xx班)”，则默认为班级部门（其中“()”为英文括号）");
            writeRow.createCell(2).setCellValue("是");
            if (allFiled) {
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("电话");
                writeRow.createCell(1).setCellValue("");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("性别");
                writeRow.createCell(1).setCellValue("男或者女，为空表示未知");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("工作职位");
                writeRow.createCell(1).setCellValue("");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("阳历生日");
                writeRow.createCell(1).setCellValue("格式为：yyyy/MM/dd 或者为：yyyy-MM-dd，此单元格必须为文本类型，否则导入失败");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("地址");
                writeRow.createCell(1).setCellValue("");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("QQ号");
                writeRow.createCell(1).setCellValue("");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("备注");
                writeRow.createCell(1).setCellValue("备注信息");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("农历生日");
                writeRow.createCell(1).setCellValue("格式为：MM-dd此单元格必须为文本类型，否则导入失败");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("昵称");
                writeRow.createCell(1).setCellValue("用户昵称");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("电话2");
                writeRow.createCell(1).setCellValue("电话/手机号码2");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("入职时间");
                writeRow.createCell(1).setCellValue("格式为：yyyy/MM/dd 或者为：yyyy-MM-dd，此单元格必须为文本类型，否则导入失败");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("生日提醒");
                writeRow.createCell(1).setCellValue("只能填：“按阳历”或“按农历”其中一个，为空或者填其他一律默认按阳历发送生日提醒");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("身份证");
                writeRow.createCell(1).setCellValue("必须少于25位");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("证件类型");
                writeRow.createCell(1).setCellValue("证件类型标题。如果不对应，会关联不上，但不会报错。");
                writeRow.createCell(2).setCellValue("否");
                rowIndex++;
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(0).setCellValue("证件内容");
                writeRow.createCell(1).setCellValue("证件号码");
                writeRow.createCell(2).setCellValue("否");

                if (VipUtil.isQwVip(orgId)) {
                    rowIndex++;
                    writeRow = writeSheet.createRow(rowIndex);
                    writeRow.createCell(0).setCellValue("排序");
                    writeRow.createCell(1).setCellValue("可用于将领导排在通讯录最前面，数字越小排序越前");
                    writeRow.createCell(2).setCellValue("否");
                    rowIndex++;
                    writeRow = writeSheet.createRow(rowIndex);
                    writeRow.createCell(0).setCellValue("保密");
                    writeRow.createCell(1).setCellValue("填：是/否。 用于隐藏该成员的微信号、手机号、邮箱，隐藏后微信端不显示该成员的微信号、手机号、邮箱");
                    writeRow.createCell(2).setCellValue("否");
                } else {
                    rowIndex++;
                    writeRow = writeSheet.createRow(rowIndex);
                    writeRow.createCell(0).setCellValue("排序");
                    writeRow.createCell(1).setCellValue("可用于将领导排在通讯录最前面，数字越小排序越前,仅VIP用户使用");
                    writeRow.createCell(2).setCellValue("否");
                    rowIndex++;
                    writeRow = writeSheet.createRow(rowIndex);
                    writeRow.createCell(0).setCellValue("保密");
                    writeRow.createCell(1).setCellValue("填：是/否。 用于隐藏该成员的微信号、手机号、邮箱，隐藏后微信端不显示该成员的微信号、手机号、邮箱,仅VIP用户使用");
                    writeRow.createCell(2).setCellValue("否");
                }
                if (optionVOs != null && optionVOs.size() > 0) {
                    for (int i = 0; i < optionVOs.size(); i++) {
                        rowIndex++;
                        writeRow = writeSheet.createRow(rowIndex);
                        writeRow.createCell(0).setCellValue(optionVOs.get(i).getOptionName());
                        if (null != optionVOs.get(i).getDesVos()) {
                            if (ContactCustomUtil.TYPE_DROP_DOWN.equals(optionVOs.get(i).getType())) {//下拉
                                String downContent = "下拉框选择:" + UserReportUtil.getoptionNames(optionVOs.get(i).getDesVos());
                                writeRow.createCell(1).setCellValue(downContent);
                            }else if (ContactCustomUtil.TYPE_MULTISELECT.equals(optionVOs.get(i).getType())) {//多选
                                String multSelectContent = "多选框选择:" + UserReportUtil.getoptionNames(optionVOs.get(i).getDesVos());
                                writeRow.createCell(1).setCellValue(multSelectContent);
                            }else {
                                writeRow.createCell(1).setCellValue(optionVOs.get(i).getDesVos().get(0).getName());
                            }
                        } else {
                            writeRow.createCell(1).setCellValue("");
                        }
                        if ("0".equals(optionVOs.get(i).getIsMust())) {
                            writeRow.createCell(2).setCellValue("否");
                        } else {
                            writeRow.createCell(2).setCellValue("是");
                        }
                    }
                }
            }
        }
    }

    /**
     * 返回通讯录用户字段
     *
     * @param optionVOs the option v os
     * @return contact field
     */
    public static Map<String, String> getContactField(List<TbQyUserCustomOptionVO> optionVOs) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("成员类型", "attribute");
        map.put("姓名", "personName");
        map.put("账号", "wxUserId");
        map.put("微信号", "weixinNum");
        map.put("手机号码", "mobile");
        map.put("邮箱", "email");
        map.put("工作部门", "deptFullName");
        map.put("电话", "shorMobile");
        map.put("性别", "sex");
        map.put("工作职位", "position");
        map.put("阳历生日", "birthday");
        map.put("地址", "address");
        map.put("QQ号", "qqNum");
        map.put("身份证", "identity");
        map.put("证件类型", "certificateTypeTitle");
        map.put("证件内容", "certificateContent");
        map.put("备注", "mark");
        map.put("农历生日", "lunarCalendar");
        map.put("昵称", "nickName");
        map.put("电话2", "phone");
        map.put("入职时间", "entryTime");
        map.put("生日提醒", "remindType");
        map.put("排序", "isTop");
        map.put("保密", "secrecy");
        if (optionVOs != null && optionVOs.size() > 0) {
            for (TbQyUserCustomOptionVO optionVO : optionVOs) {
                map.put(optionVO.getOptionName(), optionVO.getId());
            }
        }
        return map;
    }

    /**
     * 组装导入学生父母信息
     *
     * @param stuVO the stu vo
     * @return import student vo
     */
    public static ImportStudentVO assembleStudentVO(ImportStudentVO stuVO) {
        if (!AssertUtil.isEmpty(stuVO.getItemVOs())) {
            ImportVO parent = new ImportVO();
            Map<String, String> itemMap = stuVO.getItemVOs();
            Iterator it = itemMap.entrySet().iterator();
            String key = "";
            String value = "";
            Map.Entry e;
            while (it.hasNext()) {
                e = (Map.Entry) it.next();
                key = (String) e.getKey();
                value = (String) e.getValue();
                if ("attribute".equals(key)) {
                    parent.setAttribute(value);
                    it.remove();
                } else if ("personName".equals(key)) {
                    parent.setPersonName(value);
                    it.remove();
                } else if ("wxUserId".equals(key)) {
                    parent.setWxUserId(value);
                    it.remove();
                } else if ("weixinNum".equals(key)) {
                    parent.setWeixinNum(value);
                    it.remove();
                } else if ("mobile".equals(key)) {
                    parent.setMobile(value);
                    it.remove();
                } else if ("email".equals(key)) {
                    parent.setEmail(value);
                    it.remove();
                } else if ("shorMobile".equals(key)) {
                    parent.setShorMobile(value);
                    it.remove();
                } else if ("deptFullName".equals(key)) {
                    parent.setDeptFullName(value);
                    it.remove();
                } else if ("sex".equals(key)) {
                    parent.setSex(itemMap.get(key));
                    it.remove();
                } else if ("position".equals(key)) {
                    parent.setPosition(value);
                    it.remove();
                } else if ("birthday".equals(key)) {
                    if (value.matches(ExcelUtil.checkDate2)) {
                        parent.setBirthday(DateUtil.parse(value, "yyyy-MM-dd"));
                    } else if (value.matches(ExcelUtil.checkDate1)) {
                        parent.setBirthday(DateUtil.parse(value, "yyyy-MM-dd"));
                    }
                    it.remove();
                } else if ("address".equals(key)) {
                    parent.setAddress(value);
                    it.remove();
                } else if ("qqNum".equals(key)) {
                    parent.setQqNum(value);
                    it.remove();
                } else if ("certificateTypeTitle".equals(key)) {
                    parent.setCertificateTypeTitle(value);
                    it.remove();
                } else if ("certificateContent".equals(key)) {
                    parent.setCertificateContent(value);
                    it.remove();
                } else if ("mark".equals(key)) {
                    parent.setMark(value);
                    it.remove();
                } else if ("lunarCalendar".equals(key)) {
                    parent.setLunarCalendar(value);
                    it.remove();
                } else if ("nickName".equals(key)) {
                    parent.setNickName(value);
                    it.remove();
                } else if ("phone".equals(key)) {
                    parent.setPhone(value);
                    it.remove();
                } else if ("entryTime".equals(key)) {
                    if (value.matches(ExcelUtil.checkDate2)) {
                        parent.setEntryTime(DateUtil.parse(value, "yyyy-MM-dd"));
                    } else if (value.matches(ExcelUtil.checkDate1)) {
                        parent.setEntryTime(DateUtil.parse(value, "yyyy-MM-dd"));
                    }
                    it.remove();
                } else if ("remindType".equals(key)) {
                    parent.setRemindType(value);
                    it.remove();
                } else if ("isTop".equals(key)) {
                    parent.setIsTop(Integer.parseInt(value));
                    it.remove();
                } else if ("secrecy".equals(key)) {
                    parent.setSecrecy(value);
                    it.remove();
                }
            }
            if (!AssertUtil.isEmpty(itemMap)) {
                parent.setItemVOs(itemMap);
            }
            stuVO.setParentVO(parent);
        }
        return stuVO;
    }

    /**
     * 生成错误信息Excel表
     *
     * @param writeSheet the write sheet
     * @param writeRow   the write row
     * @param list       the list
     * @param rowIndex   the row index
     * @param titleMap   the title map
     */
    public static void setErrorExcel(Sheet writeSheet, Row writeRow, List<ImportErrorStudentVO> list, int rowIndex, Map<String, Integer> titleMap) {
        if (!AssertUtil.isEmpty(list)) {
            for (ImportErrorStudentVO error : list) {
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(titleMap.get("学生姓名")).setCellValue(error.getPersonName());
                writeRow.createCell(titleMap.get("班级")).setCellValue(error.getClassName());
                writeRow.createCell(titleMap.get("入学登记手机号")).setCellValue(error.getRegisterPhone());
                writeRow.createCell(titleMap.get("本校教师子女")).setCellValue(error.getHasTeacher());
                writeRow.createCell(titleMap.get("学生性别")).setCellValue(error.getSexDesc());
                writeRow.createCell(titleMap.get("身份证号码")).setCellValue(error.getIdentity());
                writeRow.createCell(titleMap.get("出生年月")).setCellValue(error.getBirthday());
                writeRow.createCell(titleMap.get("学生微信号")).setCellValue(error.getWeixinNum());
                writeRow.createCell(titleMap.get("学生手机号")).setCellValue(error.getMobile());
                writeRow.createCell(titleMap.get("学生邮箱")).setCellValue(error.getEmail());
                writeRow.createCell(titleMap.get("家长/监护人")).setCellValue(error.getParents());
                writeRow.createCell(titleMap.get("备注")).setCellValue(error.getMark());
                writeRow.createCell(titleMap.get("错误提示")).setCellValue(error.getErrMsg());
                if (!AssertUtil.isEmpty(error.getParentVO())) {
                    writeRow.createCell(titleMap.get("attribute")).setCellValue(error.getParentVO().getAttribute());
                    writeRow.createCell(titleMap.get("personName")).setCellValue(error.getParentVO().getPersonName());
                    writeRow.createCell(titleMap.get("wxUserId")).setCellValue(error.getParentVO().getWxUserId());
                    writeRow.createCell(titleMap.get("weixinNum")).setCellValue(error.getParentVO().getWeixinNum());
                    writeRow.createCell(titleMap.get("mobile")).setCellValue(error.getParentVO().getMobile());
                    writeRow.createCell(titleMap.get("email")).setCellValue(error.getParentVO().getEmail());
                    writeRow.createCell(titleMap.get("deptFullName")).setCellValue(error.getParentVO().getDeptFullName());
                }
                rowIndex++;
            }
        }
    }

    /**
     * 生成学生信息导出Excel表
     *
     * @param writeSheet the write sheet
     * @param writeRow   the write row
     * @param rowIndex   the row index
     * @param list       the list
     * @param titleMap   the title map
     */
    public static void setStudentExcel(Sheet writeSheet, Row writeRow, int rowIndex, List<ExportStudentVO> list, Map<String, Integer> titleMap) {
        if (!AssertUtil.isEmpty(list)) {
            for (ExportStudentVO student : list) {
                writeRow = writeSheet.createRow(rowIndex);
                writeRow.createCell(titleMap.get("学生姓名")).setCellValue(student.getPersonName());
                writeRow.createCell(titleMap.get("班级")).setCellValue(student.getClassFullName());
                writeRow.createCell(titleMap.get("入学登记手机号")).setCellValue(student.getRegisterPhone());
                writeRow.createCell(titleMap.get("本校教师子女")).setCellValue(student.getHasTeacher());
                writeRow.createCell(titleMap.get("学生性别")).setCellValue(student.getSexDesc());
                writeRow.createCell(titleMap.get("身份证号码")).setCellValue(student.getIdentity());
                writeRow.createCell(titleMap.get("出生年月")).setCellValue(student.getBirthday());
                writeRow.createCell(titleMap.get("学生微信号")).setCellValue(student.getWeixinNum());
                writeRow.createCell(titleMap.get("学生手机号")).setCellValue(student.getMobile());
                writeRow.createCell(titleMap.get("学生邮箱")).setCellValue(student.getEmail());
                writeRow.createCell(titleMap.get("家长/监护人")).setCellValue(student.getParents());
                writeRow.createCell(titleMap.get("备注")).setCellValue(student.getMark());
                rowIndex++;
            }
        }
    }
}
