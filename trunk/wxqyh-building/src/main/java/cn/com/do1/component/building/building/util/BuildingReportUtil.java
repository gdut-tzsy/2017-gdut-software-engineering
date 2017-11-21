package cn.com.do1.component.building.building.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
import cn.com.do1.component.building.building.service.IBuildingService;
import cn.com.do1.component.building.building.vo.CommunityVo;
import cn.com.do1.component.building.building.vo.GridOperatorVo;
import cn.com.do1.component.building.building.vo.TbYsjdBanImportVo;

/**
 * Created by apple on 2017/5/8.
 */
public class BuildingReportUtil {
    public BuildingReportUtil() {
    }
    public static void setFirstHead(Map<String, Integer> titleMap, Row writeRow, boolean isDimission, List<TbQyUserCustomOptionVO> optionVOs, boolean needStatus) throws BaseException, Exception {
        int titleIndex = 0;
        titleMap.put("communityName", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("*社区");
        ++titleIndex;
        titleMap.put("gridName", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("*所属网格");
        ++titleIndex;
        titleMap.put("architectureNo", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("*建筑物编码");
        ++titleIndex;
        titleMap.put("banAddress", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("*楼栋地址");
        ++titleIndex;
        titleMap.put("architectureName", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("建筑物名称");
        ++titleIndex;
        titleMap.put("doorplateAddress", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("门牌地址");
        ++titleIndex;
        titleMap.put("builtupArea", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("建筑面积");
        ++titleIndex;
        titleMap.put("totalNum", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("总套数");
        ++titleIndex;
        titleMap.put("floorNumber", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("楼层数");
        ++titleIndex;
        titleMap.put("architectureType", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("建筑物类型");
        ++titleIndex;
        titleMap.put("purpose", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("使用用途");
        ++titleIndex;
        titleMap.put("situation", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("使用情况");
        ++titleIndex;
        titleMap.put("owner", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("业主");
        ++titleIndex;
        titleMap.put("ownerPhone", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("业主手机号");
        ++titleIndex;
        titleMap.put("banLong", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("楼栋长");
        ++titleIndex;
        titleMap.put("banLongPhone", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("楼栋长手机号");
        ++titleIndex;
        titleMap.put("ownerFixedPhone", Integer.valueOf(titleIndex));
        writeRow.createCell(titleIndex).setCellValue("业主固定电话");
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
     * <p>Description: 验证导入楼栋</p>
     * @param contactService 通讯录Service
     * @param buildingService 楼栋Service
     * @param list 导入信息
     * @param errlist 错误列表
     * @param errorlist 错误列表
     * @param poList 楼栋po列表
     * @param type 修改或新增
     * @param creator 创建人
     * @return boolean
     * @throws Exception异常
     * @throws BaseException异常
     */
    public static boolean checkLegalPost(IContactService contactService, IBuildingService buildingService, List<TbYsjdBanImportVo> list, List<TbYsjdBanImportVo> errlist, List<String> errorlist, List<TbYsjdBanPo> poList, String type, String creator) throws Exception, BaseException {
        boolean isTrue = false;
        if (AssertUtil.isEmpty(list)) {
            return true;
        }
        String errInfo;
        TbYsjdBanPo tbYsjdBanPo;
        UserInfoVO userInfoVO;
        String userId;
        Float cheackF;
        int cheackI;
        
        for (TbYsjdBanImportVo vo : list) {
            errInfo = "";
            if (AssertUtil.isEmpty(vo.getCommunityName())) {
                errInfo += "社区不能为空；";
                isTrue = true;
            }
            CommunityVo community = buildingService.getCommunityByCommunityName(vo.getCommunityName());
            if (AssertUtil.isEmpty(community)) {
                errInfo += "社区不存在；";
                isTrue = true;
            }
            if (AssertUtil.isEmpty(vo.getGridName())) {
                errInfo += "所属网格不能为空；";
                isTrue = true;
            }
            CommunityVo grid = new CommunityVo();
            List<GridOperatorVo> GridOperatorList = new ArrayList<GridOperatorVo>();
            if (!AssertUtil.isEmpty(community)) {
                grid = buildingService.getCommunityByGridName(vo.getGridName(),community.getId());
                if (AssertUtil.isEmpty(grid)) {
                    errInfo += "所属网格不存在；";
                    isTrue = true;
                }
                if (!AssertUtil.isEmpty(grid)) {
                    GridOperatorList = buildingService.getGridOperator(grid.getId());
                }
                if (AssertUtil.isEmpty(GridOperatorList)) {
                    errInfo += "所属网格的网格没有网格员，请在通讯录录入网格员；";
                    isTrue = true;
                }
            }
            if(AssertUtil.isEmpty(vo.getArchitectureNo())){
                errInfo += "建筑物编码不能为空；";
                isTrue = true;
            }
            if(!AssertUtil.isEmpty(vo.getArchitectureNo())){
                Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$"); 
                Matcher isNum = pattern.matcher(vo.getArchitectureNo());
                if (!isNum.matches()) {
                    errInfo += "建筑物编码格式不对，格式如[asd1591981984188]，请检查；";
                    isTrue = true;
                }
            }
            if(AssertUtil.isEmpty(vo.getBanAddress())){
                errInfo += "楼栋地址不能为空；";
                isTrue = true;
            }
            if(!AssertUtil.isEmpty(vo.getBuiltupArea())) {
                cheackF = ConvertUtil.Str2Float(vo.getBuiltupArea());
                if (AssertUtil.isEmpty(cheackF)) {
                    errInfo += "建筑面积格式不对，格式如[30.3]，请检查；";
                    isTrue = true;
                }
            }
            if(!AssertUtil.isEmpty(vo.getTotalNum())) {
                cheackI =  ConvertUtil.cvStIntg(vo.getTotalNum());
                if (AssertUtil.isEmpty(cheackI)) {
                    errInfo += "总套数格式不对，格式如[1]，请检查；";
                    isTrue = true;
                }
            }
            if(!AssertUtil.isEmpty(vo.getFloorNumber())) {
                cheackI =  ConvertUtil.cvStIntg(vo.getFloorNumber());
                if (AssertUtil.isEmpty(cheackI)) {
                    errInfo += "楼层数格式不对，格式如[1]，请检查；";
                    isTrue = true;
                }
            }
            if(!AssertUtil.isEmpty(vo.getArchitectureType())) {
                String cheackAt = "楼房,特殊类建筑 ,平房 ,别墅";
                if (cheackAt.indexOf(vo.getArchitectureType())==-1) {
                    errInfo += "建筑物类型不对，只能选择楼房，特殊类建筑 ，平房 ，别墅；";
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
           
            if(!AssertUtil.isEmpty(vo.getOwnerPhone())) {
                Pattern p = Pattern.compile("^(((1[0-9][0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))\\d{8})$");
                Matcher m = p.matcher(vo.getOwnerPhone());
                if (!m.matches()) {
                    errInfo += "业主手机号格式不对，请输入有效的手机号码；";
                    isTrue = true;
                }
            }

            if(!AssertUtil.isEmpty(vo.getBanLongPhone())) {
                Pattern p = Pattern.compile("^(((1[0-9][0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))\\d{8})$");
                Matcher m = p.matcher(vo.getBanLongPhone());
                if (!m.matches()) {
                    errInfo += "楼栋长手机号格式不对，请输入有效的手机号码；";
                    isTrue = true;
                }
            }
            
            if(!AssertUtil.isEmpty(vo.getOwnerFixedPhone())) {
                Pattern p = Pattern.compile("^(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}$");
                Matcher m = p.matcher(vo.getOwnerFixedPhone());
                if (!m.matches()) {
                    errInfo += "业主固定电话格式不对，请输入有效的固定电话；";
                    isTrue = true;
                }
            }
            
            if (!AssertUtil.isEmpty(errInfo)) {
                vo.setError(errInfo);
                errlist.add(vo);
                errorlist.add(errInfo);
                continue;
            }
            
            tbYsjdBanPo = buildingService.getbanByarchitectureNo(vo.getArchitectureNo());
                if (AssertUtil.isEmpty(tbYsjdBanPo)) {
                    type = "0";
                } else {
                    type = "1";
                }
                if("1".equals(type)) {
                    //社区名
                    if (!AssertUtil.isEmpty(vo.getCommunityName())) {
                        tbYsjdBanPo.setCommunityName(vo.getCommunityName());
                    }
                    //社区id
                    if (!AssertUtil.isEmpty(community)) {
                        tbYsjdBanPo.setCommunity(community.getId());
                    }
                    //网格名
                    if (!AssertUtil.isEmpty(vo.getGridName())) {
                        tbYsjdBanPo.setGridName(vo.getGridName());
                    }
                    //网格id
                    if (!AssertUtil.isEmpty(grid)) {
                        tbYsjdBanPo.setGrid(grid.getId());
                    }
                    //楼栋地址
                    if (!AssertUtil.isEmpty(vo.getBanAddress())) {
                        tbYsjdBanPo.setBanAddress(vo.getBanAddress());
                    }
                    //建筑物名称
                    if (!AssertUtil.isEmpty(vo.getArchitectureName())) {
                        tbYsjdBanPo.setArchitectureName(vo.getArchitectureName());
                    }
                    //门牌地址
                    if (!AssertUtil.isEmpty(vo.getDoorplateAddress())) {
                        tbYsjdBanPo.setDoorplateAddress(vo.getDoorplateAddress());
                    }
                    //建筑面积
                    if (!AssertUtil.isEmpty(vo.getBuiltupArea())) {
                        tbYsjdBanPo.setBuiltupArea(vo.getBuiltupArea());
                    }
                    //总套数
                    if (!AssertUtil.isEmpty(vo.getTotalNum())) {
                        tbYsjdBanPo.setTotalNum(vo.getTotalNum());
                    }
                    //楼层数
                    if (!AssertUtil.isEmpty(vo.getFloorNumber())) {
                        tbYsjdBanPo.setFloorNumber(vo.getFloorNumber());
                    }
                    //建筑物类型 0:楼房 1:特殊类建筑 2:平房 3:别墅
                    if (!AssertUtil.isEmpty(vo.getArchitectureType())) {
                        if ("楼房".equals(vo.getArchitectureType())) {
                            tbYsjdBanPo.setArchitectureType("0");
                        } else if ("特殊类建筑".equals(vo.getArchitectureType())) {
                            tbYsjdBanPo.setArchitectureType("1");
                        } else if ("平房".equals(vo.getArchitectureType())) {
                            tbYsjdBanPo.setArchitectureType("2");
                        } else if ("别墅".equals(vo.getArchitectureType())) {
                            tbYsjdBanPo.setArchitectureType("3");
                        }
                    }
                    //使用用途 0:住宅 1:厂房 2:商业 3:商住 4:办公 5:公共设施 6:仓库 7:综合 8:其他
                    if (!AssertUtil.isEmpty(vo.getPurpose())) {
                        if ("住宅".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("0");
                        } else if ("厂房".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("1");
                        } else if ("商业".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("2");
                        } else if ("商住".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("3");
                        } else if ("办公".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("4");
                        } else if ("公共设施".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("5");
                        } else if ("仓库".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("6");
                        } else if ("综合".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("7");
                        } else if ("其他".equals(vo.getPurpose())) {
                            tbYsjdBanPo.setPurpose("8");
                        }
                    }
                    //使用情况 0:待租 1:空置 2:出租 3:部分出租 4:自用 5:其他
                    if (!AssertUtil.isEmpty(vo.getSituation())) {
                        if ("待租".equals(vo.getSituation())) {
                            tbYsjdBanPo.setSituation("0");
                        } else if ("空置".equals(vo.getSituation())) {
                            tbYsjdBanPo.setSituation("1");
                        } else if ("出租".equals(vo.getSituation())) {
                            tbYsjdBanPo.setSituation("2");
                        } else if ("部分出租".equals(vo.getSituation())) {
                            tbYsjdBanPo.setSituation("3");
                        } else if ("自用".equals(vo.getSituation())) {
                            tbYsjdBanPo.setSituation("4");
                        } else if ("其他".equals(vo.getSituation())) {
                            tbYsjdBanPo.setSituation("5");
                        }
                    }
                    //业主
                    if (!AssertUtil.isEmpty(vo.getOwner())) {
                        tbYsjdBanPo.setOwner(vo.getOwner());
                    }
                    //业主手机号
                    if (!AssertUtil.isEmpty(vo.getOwnerPhone())) {
                        tbYsjdBanPo.setOwnerPhone(vo.getOwnerPhone());
                    }
                    //楼栋长
                    if (!AssertUtil.isEmpty(vo.getBanLong())) {
                        tbYsjdBanPo.setBanLong(vo.getBanLong());
                    }
                    //楼栋长手机号
                    if (!AssertUtil.isEmpty(vo.getBanLongPhone())) {
                        tbYsjdBanPo.setBanLongPhone(vo.getBanLongPhone());
                    }
                    //业主固定电话
                    if (!AssertUtil.isEmpty(vo.getOwnerFixedPhone())) {
                        tbYsjdBanPo.setOwnerFixedPhone(vo.getOwnerFixedPhone());
                    }
                    if (!AssertUtil.isEmpty(GridOperatorList)) {
                        if (GridOperatorList.size() > 1) {
                            String personNames = "";
                            String personIds = "";
                            for (int i = 0; i < GridOperatorList.size(); i++) {
                                if (i==0) {
                                    personNames = GridOperatorList.get(i).getPersonName();
                                    personIds = GridOperatorList.get(i).getUserId();
                                } else {
                                    personNames = personNames + "," + GridOperatorList.get(i).getPersonName();
                                    personIds = personIds + "," + GridOperatorList.get(i).getUserId();
                                }
                            }
                            tbYsjdBanPo.setGridOperatorName(personNames);
                            tbYsjdBanPo.setGridOperatorId(personIds);
                        } else {
                            tbYsjdBanPo.setGridOperatorName(GridOperatorList.get(0).getPersonName());
                            tbYsjdBanPo.setGridOperatorId(GridOperatorList.get(0).getUserId());
                        }
                    } else {
                        continue;
                    }
                    poList.add(tbYsjdBanPo);
                    buildingService.delPoByArchitectureNo(vo.getArchitectureNo());
                    continue;
                }
                tbYsjdBanPo = new TbYsjdBanPo();
                tbYsjdBanPo.setId(UUID.randomUUID().toString());
                tbYsjdBanPo.setCommunityName(vo.getCommunityName());
                tbYsjdBanPo.setCommunity(community.getId());
                tbYsjdBanPo.setGridName(vo.getGridName());
                tbYsjdBanPo.setGrid(grid.getId());
                tbYsjdBanPo.setArchitectureNo(vo.getArchitectureNo());
                tbYsjdBanPo.setBanAddress(vo.getBanAddress());
                tbYsjdBanPo.setArchitectureName(vo.getArchitectureName());
                tbYsjdBanPo.setDoorplateAddress(vo.getDoorplateAddress());
                tbYsjdBanPo.setBuiltupArea(vo.getBuiltupArea());
                tbYsjdBanPo.setTotalNum(vo.getTotalNum());
                tbYsjdBanPo.setFloorNumber(vo.getFloorNumber());
                if ("楼房".equals(vo.getArchitectureType())) {
                    tbYsjdBanPo.setArchitectureType("0");
                } else if ("特殊类建筑".equals(vo.getArchitectureType())) {
                    tbYsjdBanPo.setArchitectureType("1");
                } else if ("平房".equals(vo.getArchitectureType())) {
                    tbYsjdBanPo.setArchitectureType("2");
                } else if ("别墅".equals(vo.getArchitectureType())) {
                    tbYsjdBanPo.setArchitectureType("3");
                }
                if ("住宅".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("0");
                } else if ("厂房".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("1");
                } else if ("商业".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("2");
                } else if ("商住".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("3");
                } else if ("办公".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("4");
                } else if ("公共设施".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("5");
                } else if ("仓库".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("6");
                } else if ("综合".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("7");
                } else if ("其他".equals(vo.getPurpose())) {
                    tbYsjdBanPo.setPurpose("8");
                }
                if ("待租".equals(vo.getSituation())) {
                    tbYsjdBanPo.setSituation("0");
                } else if ("空置".equals(vo.getSituation())) {
                    tbYsjdBanPo.setSituation("1");
                } else if ("出租".equals(vo.getSituation())) {
                    tbYsjdBanPo.setSituation("2");
                } else if ("部分出租".equals(vo.getSituation())) {
                    tbYsjdBanPo.setSituation("3");
                } else if ("自用".equals(vo.getSituation())) {
                    tbYsjdBanPo.setSituation("4");
                } else if ("其他".equals(vo.getSituation())) {
                    tbYsjdBanPo.setSituation("5");
                }
                tbYsjdBanPo.setOwner(vo.getOwner());
                tbYsjdBanPo.setOwnerPhone(vo.getOwnerPhone());
                tbYsjdBanPo.setBanLong(vo.getBanLong());
                tbYsjdBanPo.setBanLongPhone(vo.getBanLongPhone());
                tbYsjdBanPo.setOwnerFixedPhone(vo.getOwnerFixedPhone());
                if (!AssertUtil.isEmpty(GridOperatorList)) {
                    if (GridOperatorList.size() > 1) {
                        String personNames = "";
                        String personIds = "";
                        for (int i = 0; i < GridOperatorList.size(); i++) {
                            if (i==0) {
                                personNames = GridOperatorList.get(i).getPersonName();
                                personIds = GridOperatorList.get(i).getUserId();
                            } else {
                                personNames = personNames + "," + GridOperatorList.get(i).getPersonName();
                                personIds = personIds + "," + GridOperatorList.get(i).getUserId();
                            }
                        }
                        tbYsjdBanPo.setGridOperatorName(personNames);
                        tbYsjdBanPo.setGridOperatorId(personIds);
                    } else {
                        tbYsjdBanPo.setGridOperatorName(GridOperatorList.get(0).getPersonName());
                        tbYsjdBanPo.setGridOperatorId(GridOperatorList.get(0).getUserId());
                    }
                } else {
                    continue;
                }
                tbYsjdBanPo.setCreatorTime(new Date());
                tbYsjdBanPo.setLight("0");
                poList.add(tbYsjdBanPo);

        }

        return isTrue;
    }

    public static void seCondUtil(HSSFWorkbook writeWB, HSSFSheet writeSheet, Row writeRow, Object optionVOs, UserOrgVO userOrgVO) {
        writeSheet = writeWB.createSheet("楼栋信息导入模板填写说明");
        //合並單元格
        writeSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        writeSheet.addMergedRegion(new CellRangeAddress(4, 6, 3, 3));
        //设置列宽
        writeSheet.setColumnWidth(1, 25000);
        writeSheet.setColumnWidth(3, 8000);
        //第一行
        writeRow = writeSheet.createRow(0);
        writeRow.setHeight((short)500);
        writeRow.createCell(0).setCellValue("请先阅读此说明，然后再【楼栋信息导入模板】表中录入数据");

        writeRow = writeSheet.createRow(1);
        writeRow.createCell(0).setCellValue("字段");
        writeRow.createCell(1).setCellValue("说明");
        writeRow.createCell(2).setCellValue("是否必填");
        writeRow = writeSheet.createRow(2);
        writeRow.createCell(0).setCellValue("社区");
        writeRow.createCell(1).setCellValue("通讯录中的社区名称");
        writeRow.createCell(2).setCellValue("是");
        writeRow = writeSheet.createRow(3);
        writeRow.createCell(0).setCellValue("所属网格");
        writeRow.createCell(1).setCellValue("通讯录中的社区对应的网格名称");
        writeRow.createCell(2).setCellValue("是");
        writeRow = writeSheet.createRow(4);
        writeRow.createCell(0).setCellValue("建筑物编码");
        writeRow.createCell(1).setCellValue("只能填入数字和英文，如：a1591981984188");
        writeRow.createCell(2).setCellValue("是");
        writeRow = writeSheet.createRow(5);
        writeRow.createCell(0).setCellValue("楼栋地址");
        writeRow.createCell(1).setCellValue("填入楼栋地址");
        writeRow.createCell(2).setCellValue("是");
        writeRow = writeSheet.createRow(6);
        writeRow.createCell(0).setCellValue("建筑物名称");
        writeRow.createCell(1).setCellValue("可以为空。填入建筑物名称");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(7);
        writeRow.createCell(0).setCellValue("门牌地址");
        writeRow.createCell(1).setCellValue("可以为空。填入门牌地址");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(8);
        writeRow.createCell(0).setCellValue("建筑面积");
        writeRow.createCell(1).setCellValue("可以为空。只能填入建筑面积，如：60.2");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(9);
        writeRow.createCell(0).setCellValue("总套数");
        writeRow.createCell(1).setCellValue("可以为空。只能填入整数，如：2");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(10);
        writeRow.createCell(0).setCellValue("楼层数");
        writeRow.createCell(1).setCellValue("可以为空。只能填入整数，如：2");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(11);
        writeRow.createCell(0).setCellValue("建筑物类型");
        writeRow.createCell(1).setCellValue("可以为空。只能选择建筑物类型");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(12);
        writeRow.createCell(0).setCellValue("使用用途");
        writeRow.createCell(1).setCellValue("可以为空。只能选择使用用途");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(13);
        writeRow.createCell(0).setCellValue("使用情况");
        writeRow.createCell(1).setCellValue("可以为空。只能选择使用情况");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(14);
        writeRow.createCell(0).setCellValue("业主");
        writeRow.createCell(1).setCellValue("可以为空。填入业主姓名");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(15);
        writeRow.createCell(0).setCellValue("业主手机号");
        writeRow.createCell(1).setCellValue("可以为空。只能填入业主手机号，如：13612345678");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(16);
        writeRow.createCell(0).setCellValue("楼栋长");
        writeRow.createCell(1).setCellValue("可以为空。填入楼栋长姓名");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(17);
        writeRow.createCell(0).setCellValue("楼栋长手机号");
        writeRow.createCell(1).setCellValue("可以为空。只能填入楼栋长手机号，如：13612345678");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(18);
        writeRow.createCell(0).setCellValue("楼栋长手机号");
        writeRow.createCell(1).setCellValue("可以为空。只能填入业主固定电话，如：89123457或020-89123457");
        writeRow.createCell(2).setCellValue("否");
        writeRow = writeSheet.createRow(19);
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
