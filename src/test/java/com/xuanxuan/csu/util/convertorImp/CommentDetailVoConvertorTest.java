package com.xuanxuan.csu.util.convertorImp;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.model.CommentDetail;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.util.VoConvertor;
import com.xuanxuan.csu.vo.CommentVO;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;


/**
 * DO->VO转换器测试类
 */
public class CommentDetailVoConvertorTest extends Tester {

    @Resource
    private VoConvertor<CommentDetail, CommentVO> convertor;

    @Resource
    private CommentMapper commentMapper;

    @Test
    public void testNull() {
        assertNotNull(convertor);
        assertNotNull(commentMapper);
    }

    @Test
    public void conver2Vo() {
        //得到评论详情
        CommentDetail commentDetail = commentMapper.selectCommentDetailById("aaaaaa");
        //进行转换测试
        CommentVO commentVO = convertor.conver2Vo(commentDetail);
        //打印显示
        System.out.println(commentVO);
    }
}