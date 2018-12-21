package com.xuanxuan.csu.util;


import java.util.Date;

/**
 * @author PualrDwade
 * @note 日期转化为显示时间的策略接口
 */
public interface DateTranStrategy {

    //将日期转化为前端的显示内容
    public String conver2Show(Date date);
}
