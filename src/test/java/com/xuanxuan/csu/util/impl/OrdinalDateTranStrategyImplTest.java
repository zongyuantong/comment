package com.xuanxuan.csu.util.impl;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.util.DateTranStrategy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class OrdinalDateTranStrategyImplTest extends Tester {

    @Autowired
    private DateTranStrategy dateTranStrategy;

    @Test
    public void testConverSToshow1() {
        System.out.println(dateTranStrategy.converToShow(new Date()));
    }


    @Test
    public void testConverToshow2() {
        Comment comment = new Comment();
        System.out.println(dateTranStrategy.converToShow(comment.getCreateTime()));
    }
}