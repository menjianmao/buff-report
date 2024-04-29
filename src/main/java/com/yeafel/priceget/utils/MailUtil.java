package com.yeafel.priceget.utils;

import com.yeafel.priceget.entity.EmailProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

    /**
     * 发送邮件工具类
     */

    @Component
    @Slf4j
    @ComponentScan
    public class MailUtil {

        @Value(value = "${email.host}")
        private  String host;

        @Value(value = "${email.userName}")
        private  String  username;

        @Value(value = "${email.passWord}")
        private  String password;

        @Value(value = "${email.toEmail}")
        private  String toEmail;

        @Value(value = "${email.toName}")
        private  String toName;


        private final static Logger logger = LoggerFactory.getLogger(MailUtil.class);

        public static String buildContent(List<String[]> goodsList)  {


            //  读取模板文件
            String templateContent = "";
            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/yeafel/priceget/utils/模板.html"))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(System.lineSeparator());
                }
                templateContent = sb.toString();
            } catch (IOException e) {
                System.err.println("Error reading template file: " + e.getMessage());
                // 处理异常或返回默认内容/抛出异常...
            }


            //设置模板
//            String[] goodsItem1 = new String[]{"【1】", "AWP | 黑色魅影 (略有磨损)", "PriceMAX：375.0; PriceMin：186.0"};
//            String[] goodsItem2 = new String[]{"【2】", "AWP | 黑色魅影 (略有磨损)", "PriceMAX：375.0; PriceMin：186.0"};
//            goodsList.add(goodsItem1);
//            goodsList.add(goodsItem2);

            StringBuilder dynamicTableBodyBuilder = new StringBuilder();
            for (String[] goodsItem : goodsList) {
                dynamicTableBodyBuilder.append("<tr>\n");
                for (int i = 0; i < goodsItem.length; i++) {
                    dynamicTableBodyBuilder.append("    <td>").append(goodsItem[i]).append("</td>\n");
                }
                dynamicTableBodyBuilder.append("</tr>\n");
            }
            String string = dynamicTableBodyBuilder.toString();
            String htmlContent = templateContent.replace("{{tbody}}", string);

            return htmlContent;
        }

        /**
         * 邮件发送
         * @param mailHost 邮件服务地址
         * @param fromMail 发件人
         * @param fromName 收件人名
         * @param fromMailPwd 发件人密码
         * @param toMails 收件人，多个用英文逗号分隔
         * @param mailTitle 邮件标题
         * @param mailContent 邮件内容
         * @throws Exception
         */
        public static void sendMail(String mailHost, String fromMail, String fromName, String fromMailPwd,
                                    String toMails, String mailTitle, String mailContent) throws Exception {
            String[] toMailArr = null;
            if (toMails != null && !toMails.equals("")) {
                toMailArr = toMails.split(",");
            } else {
                throw new Exception("邮件发送人不能为空");
            }

            // 邮件属性信息
            Properties props = new Properties();
            props.put("mail.host", mailHost);
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(props); // 根据属性新建一个邮件会话
            //session.setDebug(true); // 是否打印调试信息
            toMailArr = toMails.split(",");
            for (String to : toMailArr) {
                MimeMessage message = new MimeMessage(session); // 由邮件会话新建一个消息对象
                message.setFrom(new InternetAddress(fromMail));// 设置发件人的地址
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(to, fromName));// 设置收件人,并设置其接收类型为TO
                message.setSubject(mailTitle);// 设置标题
                message.setContent(mailContent, "text/html;charset=UTF-8"); // 设置邮件内容类型为html
                message.setSentDate(new Date());// 设置发信时间
                message.saveChanges();// 存储邮件信息

                // 发送邮件
                Transport transport = session.getTransport();
                transport.connect(fromMail, fromMailPwd);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
            }
        }



        public void sendGroupMailGo(String mailTitle, String mailContent){
            try {
                //发送邮件
                MailUtil.sendGroupMail("smtp.163.com",
                        "15083727072@163.com",
                        "King",
                        "TDVLRHZLTMWELKIP",
                        "1849904470@qq.com",
                         mailTitle,
                         mailContent);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        /**
         * 邮件发送（群发）
         * @param mailHost 邮件服务地址
         * @param fromMail 发件人
         * @param fromName 收件人名
         * @param fromMailPwd 发件人密码
         * @param toMails 收件人，多个用英文逗号分隔
         * @param mailTitle 邮件标题
         * @param mailContent 邮件内容
         * @throws Exception
         */
        public static void sendGroupMail(String mailHost, String fromMail, String fromName, String fromMailPwd,
                                         String toMails, String mailTitle, String mailContent) throws Exception {
            String[] toMailArr = null;
            if (toMails != null && !toMails.equals("")) {
                toMailArr = toMails.split(",");
            } else {
                throw new Exception("邮件发送人不能为空");
            }

            // 邮件属性信息
            Properties props = new Properties();
            props.put("mail.host", mailHost);
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");


            Session session = Session.getInstance(props); // 根据属性新建一个邮件会话
            //session.setDebug(true); // 是否打印调试信息
            MimeMessage message = new MimeMessage(session); // 由邮件会话新建一个消息对象
            message.setFrom(new InternetAddress(fromMail)); // 设置发件人的地址
            InternetAddress[] sendTo = new InternetAddress[toMailArr.length];
            for (int i = 0; i < toMailArr.length; i++) {
                sendTo[i] = new InternetAddress(toMailArr[i], fromName);
            }
            message.setRecipients(Message.RecipientType.TO, sendTo); // 设置收件人,并设置其接收类型为TO
            message.setSubject(mailTitle); // 设置标题
            message.setContent(mailContent, "text/html;charset=UTF-8"); // 设置邮件内容类型为html
            message.setSentDate(new Date()); // 设置发信时间
            message.saveChanges(); // 存储邮件信息

            // 发送邮件
            Transport transport = session.getTransport();
            transport.connect(fromMail, fromMailPwd);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }

        /**
         * 读取html文件为String
         * @param htmlFileName
         * @return
         * @throws Exception
         */
        public static String readHtmlToString(String htmlFileName) throws Exception{
            InputStream is = null;
            Reader reader = null;
            try {
                is = MailUtil.class.getClassLoader().getResourceAsStream(htmlFileName);
                if (is ==  null) {
                    throw new Exception("未找到模板文件");
                }
                reader = new InputStreamReader(is, "UTF-8");
                StringBuilder sb = new StringBuilder();
                int bufferSize = 1024;
                char[] buffer = new char[bufferSize];
                int length = 0;
                while ((length = reader.read(buffer, 0, bufferSize)) != -1){
                    sb.append(buffer, 0, length);
                }
                return sb.toString();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    logger.error("关闭io流异常", e);
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch ( IOException e) {
                    logger.error("关闭io流异常", e);
                }
            }
        }

    }

