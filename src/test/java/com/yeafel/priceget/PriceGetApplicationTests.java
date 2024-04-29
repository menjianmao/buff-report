package com.yeafel.priceget;

import com.yeafel.priceget.repository.NeedGetGoodsRepository;
import com.yeafel.priceget.scheduled.GetC5CSGOPriceDataScheduled;
import com.yeafel.priceget.scheduled.GetCSGOPriceDataScheduled;
import com.yeafel.priceget.scheduled.GetIgxeCSGOPriceDataScheduled;
import com.yeafel.priceget.scheduled.reminders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SpringBootTest
class PriceGetApplicationTests {

    @Autowired
    private GetC5CSGOPriceDataScheduled getC5CSGOPriceDataScheduled;
    @Autowired
    private GetCSGOPriceDataScheduled getCSGOPriceDataScheduled;
    @Autowired
    private NeedGetGoodsRepository needGetGoodsRepository;

    @Autowired
    private GetIgxeCSGOPriceDataScheduled GetIgxeCSGOPriceDataScheduled;
    @Autowired
    private reminders reminders;
    @Test
    void contextLoads() {
//        getCSGOPriceDataScheduled.getPricesData();
//        reminders.getReminders();
//        GetIgxeCSGOPriceDataScheduled.getPricesData();
//        GetIgxeCSGOPriceDataScheduled.getPricesData();
//        spiderUtils.GetCSOBSpider();


//        try {
//            String s = MailUtil.buildContent(new ArrayList<>());
//            MailUtil.sendGroupMailGo("测试邮件", s);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }






//        String productName = "M9 刺刀（★） | 自由之手（略有磨损）";
        String productName = "沙漠之鹰 | 印花集 (崭新出厂)";
                // Step 1: 使用URLEncoder.encode()进行URL编码
        String encodedProductName = null;
        try {
            encodedProductName = URLEncoder.encode(productName, String.valueOf(StandardCharsets.UTF_8));

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // Step 2: 构造完整的URL，假设目标域名是 https://csgoob.com/goods
        String baseUrl = "https://csgoob.com/goods";
        String finalUrl = baseUrl + "?name=" + encodedProductName;

        System.out.println(finalUrl);



    }

}
