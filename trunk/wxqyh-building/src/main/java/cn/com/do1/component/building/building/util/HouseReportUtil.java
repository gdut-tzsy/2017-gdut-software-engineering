package cn.com.do1.component.building.building.util;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.reflation.ConvertUtil;
import cn.com.do1.component.addressbook.contact.service.IContactService;
import cn.com.do1.component.addressbook.contact.vo.TbQyUserCustomOptionVO;
import cn.com.do1.component.addressbook.contact.vo.UserInfoVO;
import cn.com.do1.component.addressbook.contact.vo.UserOrgVO;
import cn.com.do1.component.building.building.model.TbYsjdBanPo;
import cn.com.do1.component.building.building.model.TbYsjdHousePo;
import cn.com.do1.component.building.building.service.IBuildingService;
import cn.com.do1.component.building.building.vo.TbYsjdHouseImportVo;

/**
 * Created by apple on 2017/5/8.
 */
public class HouseReportUtil {
    public HouseReportUtil() {
    }
    public static void setFirstHead(Map<String, Integer> titleMap, Row writeRow, boolean isDimission, List<TbQyUserCustomOptionVO> optionVOs, boolean needStatus) throws BaseException, Exception {
        int titleIndex = 0;
        titleMap.put("houseNo", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("*房屋编号");
        ++titleIndex;
        titleMap.put("houseAddress", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("*房屋地址");
        ++titleIndex;
        titleMap.put("banNo", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("*楼栋编号");
        ++titleIndex;
        titleMap.put("banName", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("楼栋名称");
        ++titleIndex;
        titleMap.put("houseNumber", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("房号");
        ++titleIndex;
        titleMap.put("propertyOwner", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("产权人");
        ++titleIndex;
        titleMap.put("houseArea", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("房屋面积");
        ++titleIndex;
        titleMap.put("purpose", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("使用用途");
        ++titleIndex;
        titleMap.put("situation", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("使用情况");
        ++titleIndex;
        titleMap.put("structure", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("房屋结构");
        ++titleIndex;
        titleMap.put("banAddress", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("所属楼栋");
        ++titleIndex;

        if(optionVOs.size() > 0) {
            Iterator i$ = optionVOs.iterator();

            while(i$.hasNext()) {
                TbQyUserCustomOptionVO optionVO = (TbQyUserCustomOptionVO)i$.next();
                ++titleIndex;
                titleMap.put(optionVO.getId(), Integer.valueOf(titleIndex));
                writeRow.createCell(titleIndex).setCellValue(optionVO.getOptionName());
            }
        }
    }

    /**
     * <p>Description: 验证导入房屋</p>
     * @param contactService 通讯录Service
     * @param buildingService 房屋Service
     * @param list 导入信息
     * @param errlist 错误列表
     * @param errorlist 错误列表
     * @param poList 房屋po列表
     * @param type 修改或新增
     * @param creator 创建人
     * @return boolean
     * @throws Exception异常
     * @throws BaseException异常
     */
    public static boolean checkLegalPost(IContactService contactService, IBuildingService buildingService, List<TbYsjdHouseImportVo> list, List<TbYsjdHouseImportVo> errlist, List<String> errorlist, List<TbYsjdHousePo> poList, String type, String creator) throws Exception, BaseException {
        boolean isTrue = false;
        if (AssertUtil.isEmpty(list)) {
            return true;
        }
        String errInfo;
        TbYsjdHousePo tbYsjdHousePo;
        UserInfoVO userInfoVO;
        String userId;
        Double cheackD;
        int cheackI;
        
        for (TbYsjdHouseImportVo vo : list) {
            errInfo = "";
            if (AssertUtil.isEmpty(vo.getHouseNo())) {
                errInfo += "房屋编码不能为空；";
                isTrue = true;
            }
            if(!AssertUtil.isEmpty(vo.getHouseNo())){
                Pattern pattern = Pattern.compile("[0-9]*"); 
                Matcher isNum = pattern.matcher(vo.getHouseNo());
                if (!isNum.matches()) {
                    errInfo += "房屋编码格式不对，格式如[1591981984188]，请检查；";
                    isTrue = true;
                }
            }
            if (AssertUtil.isEmpty(vo.getHouseAddress())) {
                errInfo += "房屋地址不能为空；";
                isTrue = true;
            }
            if (AssertUtil.isEmpty(vo.getBanNo())) {
                errInfo += "楼栋编号不能为空；";
                isTrue = true;
            }
            if(!AssertUtil.isEmpty(vo.getBanNo())){
                Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$"); 
                Matcher isNum = pattern.matcher(vo.getBanNo());
                if (!isNum.matches()) {
                    errInfo += "楼栋编号格式不对，格式如[asd1591981984188]，请检查；";
                    isTrue = true;
                }
            }
            TbYsjdBanPo xxPo = buildingService.getbanByarchitectureNo(vo.getBanNo());
            if (AssertUtil.isEmpty(xxPo)) {
                errInfo += "楼栋编号不存在；";
                isTrue = true;
            }
            if(!AssertUtil.isEmpty(vo.getHouseNumber())){
                Pattern pattern = Pattern.compile("[0-9]*"); 
                Matcher isNum = pattern.matcher(vo.getHouseNumber());
                if (!isNum.matches()) {
                    errInfo += "房号格式不对，格式如[103]，请检查；";
                    isTrue = true;
                }
            }
            if(!AssertUtil.isEmpty(vo.getHouseArea())) {
                cheackD = ConvertUtil.Str2Double(vo.getHouseArea());
                if (AssertUtil.isEmpty(cheackD)) {
                    errInfo += "房屋面积格式不对，格式如[30.33]，请检查；";
                    isTrue = true;
                }
            }
            if(!AssertUtil.isEmpty(vo.getStructure())) {
                String cheackAt = "单房,一房一厅,两房一厅,两房两厅,三房以上,其他";
                if (cheackAt.indexOf(vo.getStructure())==-1) {
                    errInfo += "房屋结构类型不对，只能选择单房，一房一厅，两房一厅 ，两房两厅，三房以上，其他；";
                    isTrue = true;
                }
            }
            if(!AssertUtil.isEmpty(vo.getPurpose())) {
                String cheackP = "住宅 ,厂房 ,商业 ,商住 ,办公 ,公共设施 ,仓库 ,综合 ,其他";
                if (cheackP.indexOf(vo.getPurpose())==-1) {
                    errInfo += "使用用途不对，只能选择住宅 ，厂房 ，商业 ，商住 ，办公，公共设施 ，仓库 ，综合 ，其他；";
                    isTrue = true;
                }
            }

            if(!AssertUtil.isEmpty(vo.getSituation())) {
                String cheackS = "待租 ,空置 ,出租 ,部分出租 ,自用 ,其他";
                if (cheackS.indexOf(vo.getSituation())==-1) {
                    errInfo += "使用情况不对，只能选择待租 ，空置 ，出租，部分出租 ，自用 ，其他；";
                    isTrue = true;
                }
            }
            
            if (!AssertUtil.isEmpty(errInfo)) {
                vo.setError(errInfo);
                errlist.add(vo);
                errorlist.add(errInfo);
                continue;
            }
            
            tbYsjdHousePo = buildingService.getbanByHouseNo(vo.getHouseNo());
                if (AssertUtil.isEmpty(tbYsjdHousePo)) {
                    type = "0";
                } else {
                    type = "1";
                }
                if("1".equals(type)) {
                    //房屋编号
                    if (!AssertUtil.isEmpty(vo.getHouseNo())) {
                        tbYsjdHousePo.setHouseNo(vo.getHouseNo());
                    }
                    //房屋地址
                    if (!AssertUtil.isEmpty(vo.getHouseAddress())) {
                        tbYsjdHousePo.setHouseAddress(vo.getHouseAddress());
                    }
                    //楼栋编号
                    if (!AssertUtil.isEmpty(vo.getBanNo())) {
                        tbYsjdHousePo.setBanNo(vo.getBanNo());
                    }
                    //楼栋名称
                    if (!AssertUtil.isEmpty(vo.getBanName())) {
                        tbYsjdHousePo.setBanName(vo.getBanName());
                    }
                    //楼栋地址
                    if (!AssertUtil.isEmpty(vo.getBanAddress())) {
                        tbYsjdHousePo.setBanAddress(vo.getBanAddress());
                    }
                    //房号
                    if (!AssertUtil.isEmpty(vo.getHouseNumber())) {
                        tbYsjdHousePo.setHouseNumber(vo.getHouseNumber());
                    }
                    //产权人
                    if (!AssertUtil.isEmpty(vo.getPropertyOwner())) {
                        tbYsjdHousePo.setPropertyOwner(vo.getPropertyOwner());
                    }
                    //房屋面积
                    if (!AssertUtil.isEmpty(vo.getHouseArea())) {
                        tbYsjdHousePo.setHouseArea(vo.getHouseArea());
                    }
                    //使用用途 0:住宅 1:厂房 2:商业 3:商住 4:办公 5:公共设施 6:仓库 7:综合 8:其他
                    if (!AssertUtil.isEmpty(vo.getPurpose())) {
                        if ("住宅".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("0");
                        } else if ("厂房".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("1");
                        } else if ("商业".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("2");
                        } else if ("商住".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("3");
                        } else if ("办公".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("4");
                        } else if ("公共设施".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("5");
                        } else if ("仓库".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("6");
                        } else if ("综合".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("7");
                        } else if ("其他".equals(vo.getPurpose())) {
                            tbYsjdHousePo.setPurpose("8");
                        }
                    }
                    //使用情况 0:待租 1:空置 2:出租 3:部分出租 4:自用 5:其他
                    if (!AssertUtil.isEmpty(vo.getSituation())) {
                        if ("待租".equals(vo.getSituation())) {
                            tbYsjdHousePo.setSituation("0");
                        } else if ("空置".equals(vo.getSituation())) {
                            tbYsjdHousePo.setSituation("1");
                        } else if ("出租".equals(vo.getSituation())) {
                            tbYsjdHousePo.setSituation("2");
                        } else if ("部分出租".equals(vo.getSituation())) {
                            tbYsjdHousePo.setSituation("3");
                        } else if ("自用".equals(vo.getSituation())) {
                            tbYsjdHousePo.setSituation("4");
                        } else if ("其他".equals(vo.getSituation())) {
                            tbYsjdHousePo.setSituation("5");
                        }
                    }
                    //房屋结构 0:单房 1:一房一厅 2:两房一厅 3:两房两厅 4:三房以上 5:其他
                    if (!AssertUtil.isEmpty(vo.getStructure())) {
                        if ("单房".equals(vo.getStructure())) {
                            tbYsjdHousePo.setStructure("0");
                        } else if ("一房一厅".equals(vo.getStructure())) {
                            tbYsjdHousePo.setStructure("1");
                        } else if ("两房一厅".equals(vo.getStructure())) {
                            tbYsjdHousePo.setStructure("2");
                        } else if ("两房两厅".equals(vo.getStructure())) {
                            tbYsjdHousePo.setStructure("3");
                        } else if ("三房以上".equals(vo.getStructure())) {
                            tbYsjdHousePo.setStructure("4");
                        } else if ("其他".equals(vo.getStructure())) {
                            tbYsjdHousePo.setStructure("5");
                        }
                    }
                    //所属楼栋
                    if (!AssertUtil.isEmpty(vo.getBanName())) {
                        tbYsjdHousePo.setBanName(vo.getBanName());
                    }
                    poList.add(tbYsjdHousePo);
                    buildingService.delPoByHouseNo(vo.getHouseNo());
                    continue;
                }
                tbYsjdHousePo = new TbYsjdHousePo();
                tbYsjdHousePo.setId(UUID.randomUUID().toString());
                tbYsjdHousePo.setHouseNo(vo.getHouseNo());
                tbYsjdHousePo.setHouseAddress(vo.getHouseAddress());
                tbYsjdHousePo.setBanNo(vo.getBanNo());
                tbYsjdHousePo.setBanName(vo.getBanName());
                tbYsjdHousePo.setBanAddress(vo.getBanAddress());
                tbYsjdHousePo.setHouseNumber(vo.getHouseNumber());
                tbYsjdHousePo.setPropertyOwner(vo.getPropertyOwner());
                tbYsjdHousePo.setHouseArea(vo.getHouseArea());
                if ("住宅".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("0");
                } else if ("厂房".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("1");
                } else if ("商业".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("2");
                } else if ("商住".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("3");
                } else if ("办公".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("4");
                } else if ("公共设施".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("5");
                } else if ("仓库".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("6");
                } else if ("综合".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("7");
                } else if ("其他".equals(vo.getPurpose())) {
                    tbYsjdHousePo.setPurpose("8");
                }
                if ("待租".equals(vo.getSituation())) {
                    tbYsjdHousePo.setSituation("0");
                } else if ("空置".equals(vo.getSituation())) {
                    tbYsjdHousePo.setSituation("1");
                } else if ("出租".equals(vo.getSituation())) {
                    tbYsjdHousePo.setSituation("2");
                } else if ("部分出租".equals(vo.getSituation())) {
                    tbYsjdHousePo.setSituation("3");
                } else if ("自用".equals(vo.getSituation())) {
                    tbYsjdHousePo.setSituation("4");
                } else if ("其他".equals(vo.getSituation())) {
                    tbYsjdHousePo.setSituation("5");
                }
                if ("单房".equals(vo.getStructure())) {
                    tbYsjdHousePo.setStructure("0");
                } else if ("一房一厅".equals(vo.getStructure())) {
                    tbYsjdHousePo.setStructure("1");
                } else if ("两房一厅".equals(vo.getStructure())) {
                    tbYsjdHousePo.setStructure("2");
                } else if ("两房两厅".equals(vo.getStructure())) {
                    tbYsjdHousePo.setStructure("3");
                } else if ("三房以上".equals(vo.getStructure())) {
                    tbYsjdHousePo.setStructure("4");
                } else if ("其他".equals(vo.getStructure())) {
                    tbYsjdHousePo.setStructure("5");
                }
                tbYsjdHousePo.setCreatorTime(new Date());
                poList.add(tbYsjdHousePo);

        }

        return isTrue;
    }

    public static void seCondUtil(HSSFWorkbook writeWB, HSSFSheet writeSheet, Row writeRow, Object optionVOs, UserOrgVO userOrgVO) {
        writeSheet = writeWB.createSheet("房屋信息导入模板填写说明");
        //合並單元格
        writeSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        writeSheet.addMergedRegion(new CellRangeAddress(4, 6, 3, 3));
        //设置列宽
        writeSheet.setColumnWidth(1, 25000);
        writeSheet.setColumnWidth(3, 8000);
        //第一行
        writeRow = writeSheet.createRow(0);
        writeRow.setHeight((short)500);
        writeRow.createCell(0).setCellValue("请先阅读此说明，然后再【房屋信息导入模板】表中录入数据");

        writeRow = writeSheet.createRow(1);
        writeRow.createCell(0).setCellValue("字段");
        writeRow.createCell(1).setCellValue("说明");
        writeRow.createCell(2).setCellValue("是否必填");
        writeRow = writeSheet.createRow(2);
        writeRow.createCell(0).setCellValue("房屋编码");
        writeRow.createCell(1).setCellValue("只能填入数字，如：1591981984188");
        writeRow.createCell(2).setCellValue("是");
        writeRow = writeSheet.createRow(3);
        writeRow.createCell(0).setCellValue("房屋地址");
        writeRow.createCell(1).setCellValue("填入房屋地址");
        writeRow.createCell(2).setCellValue("是");
        writeRow = writeSheet.createRow(4);
        writeRow.createCell(0).setCellValue("楼栋编号");
        writeRow.createCell(1).setCellValue("只能填入数字，如：1591981984188");
        writeRow.createCell(2).setCellValue("是");
        writeRow = writeSheet.createRow(5);
        writeRow.createCell(0).setCellValue("楼栋名称");
        writeRow.createCell(1).setCellValue("可以为空。楼栋名称即楼栋信息中的建筑物名称");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(6);
        writeRow.createCell(0).setCellValue("房号");
        writeRow.createCell(1).setCellValue("可以为空。只能填入整数，如：201");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(7);
        writeRow.createCell(0).setCellValue("产权人");
        writeRow.createCell(1).setCellValue("可以为空。填入产权人");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(8);
        writeRow.createCell(0).setCellValue("房屋面积");
        writeRow.createCell(1).setCellValue("可以为空。只能填入房屋面积，如：60.22");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(9);
        writeRow.createCell(0).setCellValue("使用用途");
        writeRow.createCell(1).setCellValue("可以为空。只能选择使用用途");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(10);
        writeRow.createCell(0).setCellValue("使用情况");
        writeRow.createCell(1).setCellValue("可以为空。只能选择使用情况");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(11);
        writeRow.createCell(0).setCellValue("房屋结构");
        writeRow.createCell(1).setCellValue("可以为空。只能选择房屋结构");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(12);
        writeRow.createCell(0).setCellValue("所属楼栋");
        writeRow.createCell(1).setCellValue("可以为空。所属楼栋即楼栋信息中的楼栋地址");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(13);
    }
    
    /** 
     * 设置某些列的值只能输入预制的数据,显示下拉框. 
     * @param sheet 要设置的sheet. 
     * @param textlist 下拉框显示的内容 
     * @param firstRow 开始行 
     * @param endRow 结束行 
     * @param firstCol   开始列 
     * @param endCol  结束列 
     * @return 设置好的sheet. 
     */  
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet,  
            String[] textlist, int firstRow, int endRow, int firstCol,  
            int endCol) {  
        // 加载下拉列表内容  
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);  
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);  
        // 数据有效性对象  
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);  
        sheet.addValidationData(data_validation_list);  
        return sheet;  
    }  
}
