package cn.com.do1.component.contact.tag.util;

import cn.com.do1.component.sms.sendsms.util.MD5Util;

import java.io.UnsupportedEncodingException;

/**
 * Created by sunqinghai on 17/3/6.
 */
public class TagUtil {


    /**
     * 获取标签成员关联表的id md5加密
     * @param tagId 标签id
     * @param menberId 成员id
     * @return 返回数据
     * @throws UnsupportedEncodingException 这是一个异常
     * @author sunqinghai
     * @date 2017 -3-13
     */
    public static String getTagRefMD5Id (String tagId, String menberId) throws UnsupportedEncodingException {
        return MD5Util.encrypt(tagId + "_" +menberId);
    }
}
