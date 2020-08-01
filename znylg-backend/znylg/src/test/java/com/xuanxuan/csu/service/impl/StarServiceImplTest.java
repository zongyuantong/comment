package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dto.StarDTO;
import com.xuanxuan.csu.dto.UserDTO;
import com.xuanxuan.csu.service.StarService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class StarServiceImplTest extends Tester {


    @Resource
    private StarService starService;

    /**
     * 评论点赞的测试
     */
    @Test
    public void test1() {
        StarDTO starDTO = new StarDTO();
        starDTO.setToId("test3");
        starDTO.setUserId("test6");
        starDTO.setToType(2);
        starService.zan(starDTO);

        try {
            starService.zan(starDTO);
            throw new RuntimeException("测试未通过,没有正确抛出异常");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }
}