package com.yeafel.priceget.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeafel.priceget.entity.TransactRecord;
import com.yeafel.priceget.repository.TransactRecordRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@ComponentScan(value = "com.yeafel.priceget.repository")
public class spiderUtils {

   @Value(value = "${yeafel.cookie}")
   private String cookie;
   @Autowired
   private TransactRecordRepository transactRecordRepository;


   public HashMap<String, String> getBUffCollection() {
      String url = "https://buff.163.com/user-center/bookmark/goods?game=csgo";

     try {

        RestTemplate restTemplate = new RestTemplate();

        long time = System.currentTimeMillis();
        String cookieValue = "_ntes_nnid=330e001a0bc1e2b81ebaf2992302a68a,"+time+"; _ntes_nuid=330e001a0bc1e2b81ebaf2992302a68a; Device-Id=PMT1ZU91DupswMenUPY2; timing_user_id=time_aRyzg7nihr; NTES_P_UTID=Ml0JrCLFXI4e8rSgKM1z0s58IluEPc8E|1713924914; NTES_PASSPORT=qsibW1zYtshxudRa0KiKEshqAcIddDg9dB0rudK1bhoZEox_E6mDytrVuQxwB4wZC4t2Oxv.TSZ7xT07HNIdhJn2XFK47oGR0POPoVfwqJQg3GvHs8adtzCA4L7zMlBa6VF8yPLLQED56c3AMUEm7QIIK3g6U7HMNuOB2BjfFuTjIVQ6mbAkQuz4CG_qCi.Wy; P_INFO=m15083727072@163.com|1713924914|1|mail163|00&99|jis&1713411707&unireg#jis&321100#10#0#0|150072&1|unireg|15083727072@163.com; nts_mail_user=15083727072@163.com:-1:1; Locale-Supported=zh-Hans; game=csgo; qr_code_verify_ticket=5b4lGHA2defb60e3b1219effaca6e4776228; remember_me=U1074831596|lAi8Ri4tynWqM0zSjxKLdZrA32TSN0AY; session=1-9kYIFMsutYOauOS6UT_5LmKtlwxxhOuDh-__ocVinZkJ2027831220; csrf_token=ImJiNTkyZDY0N2QwNmU3NTAzNzRkZTJlMjcyOTUxNzhiMGI2ZmRiYzci.GRsYCw.y_WNa-CujvCaSwk7MPaEJSvNy34";
//        String cookieValue = "_ntes_nnid=330e001a0bc1e2b81ebaf2992302a68a,"+time+"; _ntes_nuid=330e001a0bc1e2b81ebaf2992302a68a; Device-Id=PMT1ZU91DupswMenUPY2; nts_mail_user=kfy166666@163.com:-1:1; NTES_P_UTID=Ml0JrCLFXI4e8rSgKM1z0s58IluEPc8E|1713411484; P_INFO=m15083727072@163.com|1713411707|0|unireg|00&99|null&null&null#jis&321100#10#0#0|150072&1||15083727072@163.com; timing_user_id=time_aRyzg7nihr; remember_me=U1074831596|1lwisqRDRMA0lH2KElZAvyInmRjWPN3U; session=1-slxo0kXvp2wnP9fcQHcB8Pezz7lANl1Bn-G1ukWWOGpY2027831220; Locale-Supported=zh-Hans; " +
//                "game=csgo; csrf_token=IjA1MTRkZTUyNzFiMTNmMDRlNzQ2YTc3NWFlMzEyOGVhNGM5MGY4NTci.GQbWOg.fxdmYncGIi9qXyQFKB_Ox_ntGII"; // 替换成实际的Cookie值
        String filePath = "D:/Demo/price-get/src/main/java/com/yeafel/priceget/utils/utilsCollection.txt";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookieValue);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        HttpStatus statusCode = response.getStatusCode();

        boolean isSuccess = statusCode.is2xxSuccessful();

        if (!isSuccess){
//                推送获取失败信息
           MailUtil mailUtil = new MailUtil();
           mailUtil.sendGroupMailGo("获取收藏武器失败","buff获取武器收藏列表失败");
           return null;
        }

         String responseBody = response.getBody();

//        <h3><a href="/goods/769563">锯齿爪刀（★） | 虎牙 (崭新出厂)</a></h3>
        String goodsStartTag = "<h3><a href=\"/goods/";
        String goodsEndTag = "</a></h3>";

        HashMap<String, String> goodsHashMap = new HashMap<>();

        //循环查找
        int goodsStartIndex = 0;
        while ((goodsStartIndex = responseBody.indexOf(goodsStartTag, goodsStartIndex)) != -1) {
           int goodsEndIndex = responseBody.indexOf(goodsEndTag, goodsStartIndex);
           if (goodsEndIndex != -1) {
              String desiredSubstring = responseBody.substring(goodsStartIndex, goodsEndIndex + goodsEndTag.length());
              System.out.println("截取结果: " + desiredSubstring);

              // 提取数字部分
              int idStartIndex = desiredSubstring.indexOf("/goods/") + "/goods/".length();
              int idEndIndex = desiredSubstring.indexOf('"', idStartIndex);
              String itemId = desiredSubstring.substring(idStartIndex, idEndIndex);


              // 提取文字部分
              int secondTitleStartIndex = desiredSubstring.indexOf('>', desiredSubstring.indexOf('>') + 1) + 1;
              int titleEndIndexs = desiredSubstring.indexOf('<', secondTitleStartIndex);
              String itemName = desiredSubstring.substring(secondTitleStartIndex, titleEndIndexs).trim();

              goodsHashMap.put(itemId, itemName);

              // 更新索引，以便查找下一个商品信息
              goodsStartIndex = goodsEndIndex + goodsEndTag.length();
           } else {
              System.out.println("响应体中未找到闭合标签 '</a></h3>'");
              break; // 遇到不完整商品信息时停止循环
           }
        }
        if (goodsStartIndex == 0) {
           System.out.println("响应体中未找到任何起始标签 '<h3><a href=\"/goods/\">'");
        }

     return goodsHashMap;

     }catch (Exception e){
        e.printStackTrace();
     }

      return null;
   }



   public static void GetCSOBSpider(){
      String url = "https://csgoob.onet4p.net/obindex/knives?platform=0";


      RestTemplate restTemplate = new RestTemplate();

      ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,null, String.class);

      String responseBody = response.getBody();
      HttpStatus statusCode = response.getStatusCode();
      boolean isSuccess = statusCode.is2xxSuccessful();

      if (!isSuccess){
         System.out.println("爬取信息csob失败");
      }


//      <a href="/obindex/weapon_knife_butterfly?platform=0">蝴蝶刀</a>

      Document doc = Jsoup.parse(responseBody);
      Elements loadingDivs = doc.select(".ant-table-row ant-table-row-level-0");

      for (Element loadingDiv : loadingDivs) {
         System.out.println(loadingDiv.outerHtml()); // 打印截取到的div元素的完整HTML

         // 或者进行其他所需的操作
      }





   }



   public int setGoodsPrice(JSONObject body,String goodsName){

      JSONObject data = body.getJSONObject("data");
      JSONArray items = data.getJSONArray("items");
      int mark = 0;
      for (Object o : items) {
         JSONObject item = (JSONObject) o;
         Long goodsId = item.getLong("goods_id");
         BigDecimal price = item.getBigDecimal("price");
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         //交易时间 ，但小时数一直是00,
         JSONObject assetInfo = item.getJSONObject("asset_info");
         //我需要从商品解冻时间 -7天获得真正交易时间
         Long time = 0L;
         Long transact_time = item.getLong("transact_time");
         Long created_time = item.getLong("created_at");
         if (transact_time != null){
            time = transact_time;
         }else if (created_time != null){
            time = created_time;
         }

         String transactTimeStr = sdf.format(new Date(Long.parseLong( time+ "000")));
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
            System.out.println("存储成功----------------------------------------\r\n \r\n");
            mark ++;
         } catch (Exception e) {
            e.printStackTrace();
            System.out.println("该记录已经存在，不予存储");
            System.out.println("-----------------------------------------------\r\n \r\n");
         }

      }
      return mark;
   }

}
