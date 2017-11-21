package cn.com.do1.component.contact.contact.util;

import cn.com.do1.common.util.AssertUtil;
import cn.com.do1.common.util.DateUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LiYiXin on 2016/9/6.
 */
public class UserSeniorSearchUtil {
    /**后台高级高级搜索条件优化
     * @author 李一新
     * @return
     *  @time 2016年09月06日
     */
    public static Map<String, Object> manageSearchCondition(Map<String, Object> searchMap ){
        //精确搜索有值
        if(!AssertUtil.isEmpty(searchMap.get("exactName"))){
            String exactName = (String) searchMap.get("exactName");
            searchMap.put("exactName",exactName+"%");
        }
        //如果模糊搜索拼音有值
        if(!AssertUtil.isEmpty(searchMap.get("pinyin"))){
            String pinyin = (String) searchMap.get("pinyin");
            Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
            Matcher ma = patter.matcher(pinyin);
            StringBuilder sb = new StringBuilder("");
            // 在字母中间添加%
            if (ma.find()) {
                for (char iterable_element : pinyin.toCharArray()) {
                    sb.append(iterable_element).append("%");
                }
            }
            if (sb.length() > 0)
                sb = sb.replace(sb.length() - 1, sb.length(), "");

            if (sb.length() > 0)
                pinyin = sb.toString();
            if (!AssertUtil.isEmpty(pinyin)) {
                searchMap.put("pinyin", "%" + pinyin.toLowerCase() + "%");
            }
        }
        //如果联系人信息有值
        if(!AssertUtil.isEmpty(searchMap.get("personName"))){
            String personName = (String) searchMap.get("personName");
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(personName);
            //如果是纯数字
            if(isNum.matches()){
                searchMap.put("mobileInfo", "%" + personName + "%");
                searchMap.put("personName","%"+personName+"%");
            }
            else{
                pattern = Pattern.compile("[a-zA-Z]+");
                isNum = pattern.matcher(personName);
                //如果是纯英文
                if(isNum.matches()){
                    String pinyin = (String) searchMap.get("personName");
                    Pattern patter = Pattern.compile("^[a-zA-Z]{2,6}$");
                    Matcher ma = patter.matcher(pinyin);
                    StringBuilder sb = new StringBuilder("");
                    // 在字母中间添加%
                    if (ma.find()) {
                        for (char iterable_element : pinyin.toCharArray()) {
                            sb.append(iterable_element).append("%");
                        }
                    }
                    if (sb.length() > 0)
                        sb = sb.replace(sb.length() - 1, sb.length(), "");

                    if (sb.length() > 0)
                        pinyin = sb.toString();
                    if (!AssertUtil.isEmpty(pinyin)) {
                        searchMap.put("pinyinInfo", "%" + pinyin.toLowerCase() + "%");
                    }
                    searchMap.put("personName","%"+personName+"%");
                }
                //其它情况
                else{
                    searchMap.put("personName","%"+personName+"%");
                }
            }
        }
        return searchMap;
    }

    /**
     *修改前台的日期查询，增加开始时间和结束时间
     * @param searchMap 查询数据
     * @return
     * @author liyixin
     * @2017-6-5
     * @version 1.0
     */
    public static Map<String, Object> addStartAndEndTime(Map<String, Object> searchMap) {
        if(AssertUtil.isEmpty(searchMap)){
            return searchMap;
        }
        //如果入职时间不为空
        if(!AssertUtil.isEmpty(searchMap.get("startEntryTime"))){
            searchMap.put("startEntryTime", DateUtil.parse((String) searchMap.get("startEntryTime"), "yyyy-MM-dd :start"));
        }
        //如果入职时间不为空
        if(!AssertUtil.isEmpty(searchMap.get("endEntryTime"))){
            searchMap.put("endEntryTime", DateUtil.parse((String) searchMap.get("endEntryTime"), "yyyy-MM-dd :end"));
        }
        //如果创建时间不为空
        if(!AssertUtil.isEmpty(searchMap.get("startTimes"))){
            searchMap.put("startTimes", DateUtil.parse((String) searchMap.get("startTimes"), "yyyy-MM-dd :start"));
        }
        //如果创建时间不为空
        if(!AssertUtil.isEmpty(searchMap.get("endTime"))){
            searchMap.put("endTime", DateUtil.parse((String) searchMap.get("endTime"), "yyyy-MM-dd :end"));
        }
        //如果取消关注时间不为空
        if(!AssertUtil.isEmpty(searchMap.get("reStartFollowTimes"))){
            searchMap.put("reStartFollowTimes", DateUtil.parse((String) searchMap.get("reStartFollowTimes"), "yyyy-MM-dd :start"));
        }
        //如果入职时间不为空
        if(!AssertUtil.isEmpty(searchMap.get("reEndFollowTimes"))){
            searchMap.put("reEndFollowTimes", DateUtil.parse((String) searchMap.get("reEndFollowTimes"), "yyyy-MM-dd :end"));
        }
        return searchMap;
    }
}
