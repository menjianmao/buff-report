package com.yeafel.priceget.scheduled;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeafel.priceget.entity.NeedGetGoods;
import com.yeafel.priceget.entity.TransactRecord;
import com.yeafel.priceget.repository.NeedGetGoodsRepository;
import com.yeafel.priceget.repository.TransactRecordRepository;
import com.yeafel.priceget.utils.MailUtil;
import com.yeafel.priceget.utils.spiderUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Yeafel
 * 定时获取csgo商品价格以及部分属性
 * 2021/3/13 23:53
 * Do or Die,To be a better man!
 */
@Component
@Slf4j
public class GetCSGOPriceDataScheduled {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactRecordRepository transactRecordRepository;

    @Autowired
    private NeedGetGoodsRepository needGetGoodsRepository;

    @Value(value = "${yeafel.cookie}")
    private String cookie;

    /**
     * 每个小时第10分钟获取
     */
    @Scheduled(cron = "30 30 18 * * ?")
    @Scheduled(cron = "30 0 8 * * ?")
//    @Scheduled(cron = "0 0 17 ? * TUES,THUR,SAT")
//    @Scheduled(cron = "0 10 * * * ?")
//    @Scheduled(cron = "0/3 * * * * ?")
    public void getPricesData() {

        long inTime = System.currentTimeMillis();
        try {
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            int val = rand.nextInt(1000000 , 2000000);
            System.out.println("爬取记录启动：先睡眠："+val/1000/60+"分钟···············\r\n \r\n \r\n");
            Thread.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        spiderUtils spiderUtils = new spiderUtils();
        Map<String, String> bUffCollection = spiderUtils.getBUffCollection();
        List<NeedGetGoods> allList = needGetGoodsRepository.findAll();
        List<Integer> arrayList = new ArrayList<>();
        for (NeedGetGoods goods : allList) {
            Integer id = goods.getGoodsId();
            arrayList.add(id);
        }

        if (!bUffCollection.isEmpty()){

            for (Map.Entry<String, String> entry : bUffCollection.entrySet()) {
                boolean status = true;
                String k = entry.getKey();
                String v = entry.getValue();
                for (Integer id : arrayList) {
                    if (id.equals(Integer.valueOf(k))) {
                        status =false;
                        break;
                    }
                }
               if (status){
                   NeedGetGoods needGetGoods = new NeedGetGoods();
                   needGetGoods.setGoodsId(Integer.valueOf(k));
                   needGetGoods.setGoodsName(v);
                   needGetGoodsRepository.save(needGetGoods);
                   System.out.println("存入："+k + " " + v);

                   MailUtil mailUtil = new MailUtil();
                   mailUtil.sendGroupMailGo("---新增收藏武器提醒---", k+" "+v);
               }

            }
        }else {
           System.out.println("BUFF获取失败新增武器失败");
       }


        //https://buff.163.com/api/market/goods/bill_order?game=csgo&goods_id=42192&_=1615643464574
        //https://buff.163.com/api/market/goods/bill_order?game=csgo&goods_id=768729&_=1615651248793
//        String url = "https://buff.163.com/api/market/goods/bill_order";
        List<NeedGetGoods> needGetGoodsList = needGetGoodsRepository.findAll();
        String[] names = new String[needGetGoodsList.size()];
        int[] goodsIdList = new int[needGetGoodsList.size()];
        for (int i = 0; i < needGetGoodsList.size(); i++) {
            names[i] = needGetGoodsList.get(i).getGoodsName();
            goodsIdList[i] = needGetGoodsList.get(i).getGoodsId();
        }

        System.out.println("\r\n \r\n 本次BUFF获取任务启动.........................");
//        for (int i = 0; i < goodsIdList.length; i++) {
        int  mark = 0;
        for (int i = 0; i < names.length; i++) {
            String goodsName = names[i];
            long currentTimeMillis = System.currentTimeMillis();
            String url = "https://buff.163.com/api/market/goods/bill_order?game=csgo" +
                    "&goods_id=" + goodsIdList[i] + "&_=" + currentTimeMillis;

            HttpHeaders headers = new HttpHeaders();
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";
            List<String> cookies = new ArrayList<>();

            String cookie = "_ntes_nnid=330e001a0bc1e2b81ebaf2992302a68a,"+currentTimeMillis+"; _ntes_nuid=330e001a0bc1e2b81ebaf2992302a68a; Device-Id=PMT1ZU91DupswMenUPY2; nts_mail_user=kfy166666@163.com:-1:1; NTES_P_UTID=Ml0JrCLFXI4e8rSgKM1z0s58IluEPc8E|1713411484; P_INFO=m15083727072@163.com|1713411707|0|unireg|00&99|null&null&null#jis&321100#10#0#0|150072&1||15083727072@163.com; timing_user_id=time_aRyzg7nihr; remember_me=U1074831596|1lwisqRDRMA0lH2KElZAvyInmRjWPN3U; session=1-slxo0kXvp2wnP9fcQHcB8Pezz7lANl1Bn-G1ukWWOGpY2027831220; Locale-Supported=zh-Hans; game=csgo; csrf_token=IjA1MTRkZTUyNzFiMTNmMDRlNzQ2YTc3NWFlMzEyOGVhNGM5MGY4NTci.GQbR2A.7Qxq8wXt3Ju26MLnUs7utxjr0gk";
            cookies.add("_ntes_nnid=" + cookie);
            headers.set(HttpHeaders.USER_AGENT,userAgent);
            headers.put(HttpHeaders.COOKIE,cookies);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);

            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JSONObject body = JSONObject.parseObject(result.getBody());

            String error = body.getString("error");
            if ( error != null ){

                MailUtil mailUtil = new MailUtil();
                mailUtil.sendGroupMailGo("---BUFF信息系获取失败---", error );
                return;
            }

            JSONObject data = body.getJSONObject("data");
            JSONArray items = data.getJSONArray("items");
            for (Object o : items) {
                JSONObject item = (JSONObject) o;
                Long goodsId = item.getLong("goods_id");
                BigDecimal price = item.getBigDecimal("price");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //交易时间 ，但小时数一直是00,
                JSONObject assetInfo = item.getJSONObject("asset_info");
                //我需要从商品解冻时间 -7天获得真正交易时间
                String transactTimeStr = sdf.format(new Date(Long.parseLong(item.getLong("transact_time") + "000")));
                Date transactTime = null;
                try {
                    transactTime = sdf.parse(transactTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String paintwear = assetInfo.getString("paintwear");
                JSONObject info = assetInfo.getJSONObject("info");
                JSONArray stickers = info.getJSONArray("stickers");
                //最终入库的某武器的一条或多条贴纸情况
                List<String> stickerList = new ArrayList<>();
                //如果贴纸不为空那么将贴纸信息记录
                if (stickers.size() > 0) {
                    for (int j = 0; j < stickers.size(); j++) {
                        JSONObject sticker = (JSONObject) stickers.get(j);
                        String stickerName = sticker.getString("name");
                        BigDecimal stickerWear = sticker.getBigDecimal("wear");
                        String stickerItem = stickerName + "|" + stickerWear;
                        stickerList.add(stickerItem);
                    }
                }
                //获取图片
                String img = item.getString("img_src");

                //开始存入数据
                TransactRecord transactRecord = new TransactRecord();
                transactRecord.setPrice(price);
                transactRecord.setGoodsId(goodsId);
                transactRecord.setPaintwear(paintwear);
                transactRecord.setStickers(stickerList.toString());
                transactRecord.setStickerIsInfluence(0);
                transactRecord.setTransactTime(transactTime);
                transactRecord.setGoodsName(goodsName);
                transactRecord.setPlatform("网易BUFF");
                transactRecord.setSrcImg(img);
                try {
                    System.out.println("开始存储----------------------------------------");
                    String curTime = sdf.format(new Date());
                    System.out.println("当前时间" + curTime);
                    transactRecordRepository.save(transactRecord);
                    System.out.println(transactRecord.toString());
                    mark++;
                    System.out.println("存储成功----------------------------------------\r\n \r\n");

                } catch (Exception e) {
                    System.out.println("该记录已经存在，不予存储");
                    System.out.println("-----------------------------------------------\r\n \r\n");
                }
            }

            //存一组数据睡眠20-50秒
            try {
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                int val = rand.nextInt(20000 , 50000);
                System.out.println("已获取一条记录，睡眠"+val/1000+"···············\r\n \r\n \r\n");
                Thread.sleep(val);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        long endTime = System.currentTimeMillis();
        long date = endTime - inTime;
        System.out.println("本次获取任务完成.........................");
        MailUtil mailUtil = new MailUtil();
        mailUtil.sendGroupMailGo("---今日BUFF信息获取完成---", "本次共获取"+mark+"条数据,耗时："+date/1000+"s");
    }
}
