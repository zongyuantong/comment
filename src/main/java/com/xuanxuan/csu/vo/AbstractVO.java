package com.xuanxuan.csu.vo;

import com.xuanxuan.csu.util.DateTranStrategy;
import com.xuanxuan.csu.util.impl.OrdinalDateTranStrategyImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 数据传输对象的公共抽象基类,提供默认的工具方法
 */

public abstract class AbstractVO {

    private final DateTranStrategy dateTranStrategy = new OrdinalDateTranStrategyImpl();

    //使用指定的格式将日期转化为字符串
    public String date2String(String pattem, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattem);
        return dateFormat.format(date);
    }

    //使用默认的格式将日期转化为字符串
    public String date2String(Date date) {
        //根据时间生成策略
        return dateTranStrategy.conver2Show(date);
    }
}
