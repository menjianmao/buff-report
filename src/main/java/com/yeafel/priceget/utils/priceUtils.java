package com.yeafel.priceget.utils;

import com.yeafel.priceget.entity.TransactRecord;

import java.util.List;

public class priceUtils {

    public static double getPriceMax(List<TransactRecord> list, long goodsId) {
        double max = 0;
        for (TransactRecord record : list) {
            if (record.getGoodsId().equals(goodsId)){
                double price = record.getPrice().doubleValue();
                if (price > max){
                    max = price;
                }

            }
        }
        return max;
    }


    public static double getPriceMin(List<TransactRecord> list,long goodsId) {
        double min = 0;
        for (TransactRecord record : list) {
            if (record.getGoodsId().equals(goodsId)){
                double price = record.getPrice().doubleValue();
                if (min == 0){
                    min = price;
                }

                if (price < min){
                    min = price;
                }
            }
        }
        return min;
    }


}
