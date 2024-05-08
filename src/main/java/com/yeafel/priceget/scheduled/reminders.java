package com.yeafel.priceget.scheduled;

import com.yeafel.priceget.entity.NeedGetGoods;
import com.yeafel.priceget.entity.TransactRecord;
import com.yeafel.priceget.repository.NeedGetGoodsRepository;
import com.yeafel.priceget.repository.TransactRecordRepository;
import com.yeafel.priceget.utils.MailUtil;
import com.yeafel.priceget.utils.priceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 根据数据库内获取的价格判断是否达到预购买标准
 */
@Component
@Slf4j
public class reminders {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactRecordRepository transactRecordRepository;

    @Autowired
    private NeedGetGoodsRepository needGetGoodsRepository;



//    @Scheduled(cron = "0 0/2 * * * ? ")
    @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(cron = "0 30 19 * * ?")
    public void getReminders() {
//          查所有关注的武器
        List<TransactRecord> list = transactRecordRepository.findAll();
        List<NeedGetGoods> idList = needGetGoodsRepository.findAll();

        List<TransactRecord> reportList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();

        if (list.isEmpty()) {
            System.out.println("获取需要提醒的武器为空");
            return;
        }
        HashMap<Integer, Double> priceHashMap = new HashMap<>();
        HashMap<Long,String> hashMap = new HashMap<>();
        for (TransactRecord record : list) {
            hashMap.put(record.getGoodsId(),record.getGoodsName());
            if (record.getTransactTime().after(startTime)) {
                reportList.add(record);
            }
        }

        for (NeedGetGoods needGetGoods : idList) {
            priceHashMap.put(needGetGoods.getGoodsId(), needGetGoods.getBasePrice());
        }
        int i = 1;
        String mailContent = "";
        List<String[]> arrayList = new ArrayList<>();
        for (Map.Entry<Long, String> entry : hashMap.entrySet()) {
            Integer k = Math.toIntExact(entry.getKey());
            String v = entry.getValue();
            String goodsName = v;

            double priceMax = priceUtils.getPriceMax(reportList, k);
            double priceMin = priceUtils.getPriceMin(reportList, k);

            if (priceHashMap.containsKey(k)) {
                double basePrice = priceHashMap.get(k);
                if (basePrice > priceMin && priceMin > 0) {
                  String goodsNames = goodsName + "：设置底价："+basePrice+";  当前底价："+ priceMin;
                    MailUtil mailUtil = new MailUtil();
                    mailUtil.sendGroupMailGo("***  **---buff收藏武器底价提醒--- *****\t\n", goodsNames);
                }
                String[] goodsItem = new String[]{"【"+i+"】", goodsName, "Max："+priceMax+"; Min: "+priceMin,"Base: "+basePrice };
                arrayList.add(goodsItem);
            }
            mailContent = goodsName.toString();
            System.out.println("------------------------***-------------------------------");
            System.out.println(mailContent);
            System.out.println("-------------***-------------------***------------------------");
            i++;
//     MailUtil.sendGroupMailGo("buff武器降价提醒---" + goodsName, mailContent);
        }
        MailUtil mailUtil = new MailUtil();
        mailUtil.sendGroupMailGo("---buff武器每日提醒---", MailUtil.buildContent(arrayList));

    }

    public void getReminders(Long goodsId) {
        //有id情况下
        List<TransactRecord> list = transactRecordRepository.findAll();
        List<TransactRecord> reportList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();

        String goodsName = "";
        for (TransactRecord transactRecord : list) {
            if (transactRecord.getTransactTime().after(startTime)) {
                reportList.add(transactRecord);
                Long goodId = transactRecord.getGoodsId();
                if (goodId.equals(goodsId)) {
                    goodsName = transactRecord.getGoodsName();
                }
            }
        }

        if (list.isEmpty() || goodsName.equals("")) {
            System.out.println("获取需要提醒的武器为空");
            return;
        }


//      763400
        double priceMax = priceUtils.getPriceMax(reportList, goodsId);
        double priceMin = priceUtils.getPriceMin(reportList, goodsId);
        String mailContent = goodsName + "：PriceMAX：" + priceMax + "; \t\n\t PriceMin：" + priceMin
                            +"\t\n\t -----------------------------" ;

        System.out.println("-------------------------------------------------------");
        System.out.println(mailContent);
        System.out.println("--------------------------------------------------------");

        MailUtil mailUtil = new MailUtil();
        mailUtil.sendGroupMailGo(goodsName +": 降价提醒", mailContent);
    }





}
