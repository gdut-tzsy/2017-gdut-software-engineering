package cn.com.do1.component.contact.contact.event;
/**
 * Created by ken on 2017/3/20.
 */

import cn.com.do1.common.annotation.reger.ProcesserUnit;
import cn.com.do1.common.exception.BaseException;
import cn.com.do1.common.exception.ExceptionCenter;
import cn.com.do1.common.util.DateUtil;
import cn.com.do1.common.util.string.StringUtil;
import cn.com.do1.component.addressbook.contact.vo.ExtOrgVO;
import cn.com.do1.component.contact.contact.util.AddressbookSyncTask;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbDqdpOrganizationTempPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.model.TbQyExperienceApplicationPO;
import cn.com.do1.component.qiweipublicity.experienceapplication.service.IExperienceapplicationService;
import cn.com.do1.component.qiweipublicity.experienceapplication.util.ExperienceAgentStatusUtil;
import cn.com.do1.component.qwinterface.orginfo.IOrgRegist;
import cn.com.do1.component.uploadfile.imageupload.service.IFileMediaService;
import cn.com.do1.component.util.Configuration;
import cn.com.do1.component.util.EmailUtil;
import cn.com.do1.component.util.IpUtil;
import cn.com.do1.component.util.vo.IpInfo;
import cn.com.do1.dqdp.core.DqdpAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 注册成功后ip解析，发送邮箱等的回调方法
 *
 * @author ChenFeixiong 2017/3/20
 * @version 1.0
 */
@ProcesserUnit(name = "WxqyhRegisterHelper")
public class RegisterHelper implements IOrgRegist {
    private static final Logger logger = LoggerFactory.getLogger(RegisterHelper.class);
    private static IExperienceapplicationService experienceapplicationService = DqdpAppContext.getSpringContext().getBean("experienceapplicationService", IExperienceapplicationService.class);
    private static IFileMediaService fileMediaService = DqdpAppContext.getSpringContext().getBean("fileMediaService", IFileMediaService.class);

    @Override
    public void registSucceed(String userName,ExtOrgVO orgVO, TbDqdpOrganizationTempPO tempPO, TbQyExperienceApplicationPO tbQyExperienceApplicationPO, int type) {
        //解析ip
        analyticalIP(tbQyExperienceApplicationPO);
        //发送邮件
        sendEmail(tbQyExperienceApplicationPO,type,userName);
        //接入号特殊处理
        if (ExperienceAgentStatusUtil.REGISTER_TYPE_OWN == type) {
            //插入VIP空间统计
            try {
                fileMediaService.addFileStatisticsByOrgId(tempPO.getOrgId());
            } catch (Exception e) {
                logger.error("VIP空间统计失败", e);
                ExceptionCenter.addException(e, "VIP空间统计失败", tempPO.getOrgId());
            } catch (BaseException e) {
                logger.error("VIP空间统计失败", e);
                ExceptionCenter.addException(e, "VIP空间统计失败", tempPO.getOrgId());
            }
            //同步通讯录
            AddressbookSyncTask.insertTaskForFirstRegister(tempPO.getCorpId(), tempPO.getOrgId());
        }
    }

    /**
     * 发送邮件
     * @param tbQyExperienceApplicationPO
     * @param type
     * @param userName
     * @author ChenFeixiong
     * @date 2017 -3-22
     */
    public void sendEmail(TbQyExperienceApplicationPO tbQyExperienceApplicationPO, int type,String userName){
        String fromUser = Configuration.AUTO_TO_FROM_EMAIL_REGIS;
        String toUsers = tbQyExperienceApplicationPO.getEmail();
        String ccUsers = "";
        String bccUsers = Configuration.AUTO_TO_EMAIL;
        String titley = "";
        String contenty = "";
        try {
            if (ExperienceAgentStatusUtil.REGISTER_TYPE_OWN == type) {
                titley = "关于接入" + tbQyExperienceApplicationPO.getEnterpriseName() + "企业微信的邮件通知";
                contenty = getSendInsertEmallContent(tbQyExperienceApplicationPO.getEnterpriseName(),userName);
            } else if (ExperienceAgentStatusUtil.REGISTER_TYPE_EXP == type) {
                titley = tbQyExperienceApplicationPO.getEnterpriseName() + "，您提交的企微账号已经开通！";
                contenty = EmailUtil.getContent(tbQyExperienceApplicationPO.getEnterpriseName(),userName);
            } else {
                logger.error("发送邮件类型不匹配，接入类型为" + type);
                return;
            }
            EmailUtil.SendEmail(fromUser, toUsers, ccUsers, bccUsers, titley, contenty);
            tbQyExperienceApplicationPO.setSendemail(1);
            tbQyExperienceApplicationPO.setSuccessTime(new Date());
            experienceapplicationService.updatePO(tbQyExperienceApplicationPO, false);
        } catch (Exception e) {
            logger.debug("WxqyhRegisterHelper", e);
        }
    }

    /**
     * 解析ip
     * @param tbQyExperienceApplicationPO
     * @author ChenFeixiong
     * @date 2017 -3-22
     */
    public void analyticalIP(TbQyExperienceApplicationPO tbQyExperienceApplicationPO){
        if (StringUtil.isNullEmpty(tbQyExperienceApplicationPO.getEnterpriseIp())) {
            return;
        }
        try {
            IpInfo info = IpUtil.getAddresses(tbQyExperienceApplicationPO.getEnterpriseIp());
            tbQyExperienceApplicationPO.setCountry(info.getCountry());
            tbQyExperienceApplicationPO.setArea(info.getArea());
            tbQyExperienceApplicationPO.setProvince(info.getRegion());
            tbQyExperienceApplicationPO.setCity(info.getCity());
            tbQyExperienceApplicationPO.setCounty(info.getCounty());
            experienceapplicationService.updatePO(tbQyExperienceApplicationPO, false);
        } catch (BaseException e) {
            logger.error("接入套件自动对接处理,IpUtil失败" + tbQyExperienceApplicationPO.getEnterpriseIp(), e);
            ExceptionCenter.addException(e, "接入套件自动对接处理,IpUtil失败", tbQyExperienceApplicationPO.getEnterpriseIp());
        } catch (Exception e) {
            logger.error("接入套件自动对接处理,IpUtil失败" + tbQyExperienceApplicationPO.getEnterpriseIp(), e);
            ExceptionCenter.addException(e, "接入套件自动对接处理,IpUtil失败", tbQyExperienceApplicationPO.getEnterpriseIp());
        }
    }
    /**
     * 获取接口号发送邮件模版
     *
     * @param orgName
     * @param accountName
     * @return
     */
    private String getSendInsertEmallContent(String orgName, String accountName) {
        String curYear = DateUtil.format(new Date(), "yyyy");
        return "<div style=\" background:#eee; width:100%;padding: 30px 0;\"><div style=\"width:640px;margin:0 auto;background: #fff; border-top:4px solid #ff9600;font-family: '微软雅黑';\">" +
                "<table cellspacing=\"0\"cellpadding=\"0\"style=\"width:100%;\"><tbody><tr style=\"height: 70px; \"><td><img style=\"padding: 20px 0 30px 30px;\"src=\"http://wbg.do1.com.cn/templets/default/images/logo.png\">" +
                "</td><td style=\"text-align: right\"><img style=\"padding: 0 30px 0 0; width: 185px;\"src=\"http://wbg.do1.com.cn/templets/default/images/mail_text.png\"></td></tr></tbody>" +
                "</table><table style=\"width: 580px; margin: 0px auto;color:#333;font-size: 16px;line-height: 1.6;\"cellspacing=\"0\"cellpadding=\"0\"><tbody><tr><td style=\"\">" +
                "<p style=\"margin: 15px 0 15px 0;font-size: 16px;color:#333;\">" + orgName + "：<br>您好，欢迎使用企微云平台。</p><p style=\"margin: 10px 0;font-size: 16px;color:#666;\">企微云平台是基于企业微信的移动工作平台。企微致力于让企业文化建设、移动办公、销售管理、数据采集变得更简单，" +
                "真正帮助企业提升效率。平台上超过20个基础应用均为永久免费使用，您可逐一体验，按需使用。</p><p style=\"margin: 20px 0 60px 0;padding-bottom: 30px;font-size: 16px;line-height: 1.8;padding: 20px 15px;border-radius: 5px;background:#f5f5f5;border:1px solid #eee;\">" +
                "<span style=\"color: #FF8B00;font-size: 18px\">您的企微云平台管理账号为：" + accountName + "</span><br><span style=\"font-size: 16px;\"><a href=\"http://qy.do1.com.cn/\"style=\"text-decoration: none;color: #5A90AE;border-bottom:1px dashed #ADBFC8;\">点击这里登录管理平台</a>；如忘记密码，可通过" +
                "<a href=\"http://qy.do1.com.cn/qwy/qiweipublicity/experience2/Retrieve1.jsp\" style=\"text-decoration:none;color:#5A90AE;border-bottom:1px dashed#ADBFC8;\">本链接找回</a></span></p><p style=\"margin: 20px 0 ;color:#666;font-size: 16px;\">在开始使用企微前，我们建议您先通过下面链接快速了解如何使用：<br>" +
                "<a href=\"http://wbg.do1.com.cn/help/jinjiejiqiao/2015/1009/455.html?amail\"class=\"\"style=\"text-decoration: none; font-size: 16px; color: #5A90AE;\">[企微云平台快速入门指南]</a></p>" +
                "<p style=\"margin:20px 0; color: #666;font-size: 16px;\">我们还邀请您关注「企微云平台DO1」服务号<span style=\"font-size: 14px;color:#999;\">（ID:do1info）</span>，每周会发布企微的功能更新及使用指南；</p>" +
                "<p style=\"margin:20px 0; color: #666;font-size: 16px;\">同时，欢迎加入企微QQ交流群：<a href=\"" + Configuration.AUTO_TO_QQ_URL + "\"style=\"text-decoration: none; color: #5A90AE;border-bottom:1px dashed #ADBFC8;\">" + Configuration.AUTO_TO_QQ + "</a>" +
                "与其他用户共同交流<span style=\"font-size: 14px;color:#999;\">（ID:qinzhangbo01）</span></p><p style=\"text-align: center;padding:10px 0 20px 0 ;\">" +
                "<img border=\"0\"img=\"\"src=\"http://wbg.do1.com.cn/templets/default/images/qr_do1info.png?123\"style=\"margin: 0 10px 0 0;width: 140px;\">" +
                "</p><p style=\"margin:10px 0; padding:20px 0  20px 0; text-align: right; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(221, 221, 221);font-size: 16px;\">企微云平台CEO 覃章波</p></td></tr>" +
                "<tr><td style=\"padding: 0 0 30px 0\"><p style=\"margin: 0px; padding-top: 10px; line-height: 22px; font-size: 12px; color: rgb(102, 102, 102);\"><span style=\"font-size: 14px;\">广东道一信息技术股份有限公司</span><br>证劵代码：830972<br><span style=\"\">" +
                "客服电话：400-111-2626<br>企微云平台官网：<a href=\"http://wbg.do1.com.cn\"style=\"color:#ff9600;text-decoration: underline;\">http://wbg.do1.com.cn</td></tr></tbody></table><p style=\"padding: 10px; font-size: 12px; background-color: rgb(238, 238, 238); color: rgb(187, 187, 187); text-align: center; margin-top: 0px; margin-bottom: 0px;\">" +
                "Copyright?2014-" + curYear + " DO1.com.cn All Rights Reserved</p></div></div>";
    }
}
