package com.fruitshop.util;

import org.apache.commons.mail.HtmlEmail;

public class SendEmail {

    private  SendEmail(){

    }

    public static boolean sendEmail(String emailAddress,String code) {
        try {
            HtmlEmail email = new HtmlEmail();//不用更改
            email.setHostName("smtp.qq.com");//需要修改，126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com
            email.setCharset("UTF-8");
            email.addTo(emailAddress);// 收件地址

            email.setFrom("1030725545@qq.com", "1030725545@qq.com");//此处填邮箱地址和用户名,用户名可以任意填写
            email.setAuthentication("1030725545@qq.com", "qimygubmlwerbfch");//此处填写邮箱地址和客户端授权码
            email.setSubject("二手交易平台");//此处填写邮件名，邮件名可任意填写
            email.setMsg("尊敬的二手交易平台用户您好,您的验证码为:" + code+",请及时注册");//此处填写邮件内容

            email.send();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
