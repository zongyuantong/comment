package com.xuanxuan.csu.util;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.UserInfoMapper;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.util.impl.CommentVoConvertor;
import com.xuanxuan.csu.vo.CommentVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class VoConvertorTest extends Tester {

    @Resource
    private VoConvertor<Comment, CommentVO> commentVOVoConvertor = new CommentVoConvertor();

    @Autowired
    private VoConvertor<Comment, CommentVO> commentCommentVOVoConvertor;


    @Resource
    private UserInfoMapper userInfoMapper;


    @Test
    public void test1() {
        assertNotNull(commentVOVoConvertor);
    }

    @Test
    public void test2() {
        assertNotNull(commentCommentVOVoConvertor);
    }

    @Test
    public void test3() {
        assertNotNull(userInfoMapper);
    }

    @Test
    public void test4() {
        assertNotNull(userInfoMapper);
    }
}