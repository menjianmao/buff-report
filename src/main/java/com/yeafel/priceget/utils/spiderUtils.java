package com.yeafel.priceget.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


public class spiderUtils {

   @Value(value = "${yeafel.cookie}")
   private String cookie;
   public HashMap<String, String> getBUffCollection() {
      String url = "https://buff.163.com/user-center/bookmark/goods?game=csgo";

     try {

        RestTemplate restTemplate = new RestTemplate();

        long time = System.currentTimeMillis();
        String cookieValue = "_ntes_nnid=330e001a0bc1e2b81ebaf2992302a68a,"+time+"; _ntes_nuid=330e001a0bc1e2b81ebaf2992302a68a; Device-Id=PMT1ZU91DupswMenUPY2; nts_mail_user=kfy166666@163.com:-1:1; NTES_P_UTID=Ml0JrCLFXI4e8rSgKM1z0s58IluEPc8E|1713411484; P_INFO=m15083727072@163.com|1713411707|0|unireg|00&99|null&null&null#jis&321100#10#0#0|150072&1||15083727072@163.com; timing_user_id=time_aRyzg7nihr; remember_me=U1074831596|1lwisqRDRMA0lH2KElZAvyInmRjWPN3U; session=1-slxo0kXvp2wnP9fcQHcB8Pezz7lANl1Bn-G1ukWWOGpY2027831220; Locale-Supported=zh-Hans; " +
                "game=csgo; csrf_token=IjA1MTRkZTUyNzFiMTNmMDRlNzQ2YTc3NWFlMzEyOGVhNGM5MGY4NTci.GQbWOg.fxdmYncGIi9qXyQFKB_Ox_ntGII"; // 替换成实际的Cookie值
        String filePath = "D:/Demo/price-get/src/main/java/com/yeafel/priceget/utils/utilsCollection.txt";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookieValue);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        HttpStatus statusCode = response.getStatusCode();

        boolean isSuccess = statusCode.is2xxSuccessful();

        if (!isSuccess){
//                推送获取失败信息
           MailUtil.sendGroupMailGo("获取收藏武器失败","buff获取武器收藏列表失败");
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
}
