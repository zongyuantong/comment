package com.xuanxuan.csu.dao;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.core.ConditionMap;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.RefreshDTO;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.*;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.vo.CommentVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

/**
 * 高级sql查询的单元测试类
 */
public class CommentMapperTest extends Tester {

    @Resource
    CommentService commentService;
    @Resource
    PassageService passageService;
    @Resource
    UserInfoService userInfoService;
    @Resource
    ReplyService replyService;
    @Resource
    CommentMapper commentMapper;

    /**
     * 查询评论详情
     */
    @Test
    public void test1() {
        String commentId = "test1";
        CommentDetail commentDetail = commentMapper.selectCommentDetailById(commentId);
        System.out.println(commentDetail);
        for (Reply reply : commentDetail.getReplyList()) {
            System.out.println("回复信息:" + reply);
        }
        //测试为空的情况
        //随便增加一条评论
        Comment comment = new Comment();
        comment.setId("chenzhixuan");
        comment.setContent("test empty list");
        comment.setFloor(0);
        comment.setFromUid("test2");
        comment.setPassageId("test1");
        commentMapper.insert(comment);


        CommentDetail commentDetail1 = commentMapper.selectCommentDetailById("chenzhixuan");
        System.out.println(commentDetail1);
        //评论是否为空？
        assertNotNull(commentDetail1.getReplyList());
        //不为空，size = 0
        assertEquals(commentDetail1.getReplyList().size(), 0);
    }


    /**
     * 测试文章的评论回复查询
     */
    @Test
    public void test2() {
        List<CommentDetail> commentDetailList = commentMapper.selectCommentListByPassageId("null", 0, 10);
        assertEquals(0, commentDetailList.size());
    }

    @Test
    public void test3() {
        List<CommentDetail> commentDetailList = commentMapper.selectCommentListByPassageId("test1", 2, 0);
        for (CommentDetail commentDetail : commentDetailList) {
            System.out.println(commentDetail);
        }
    }

    @Test
    public void test4() {
        for (int i = 0; i < 10; ++i) {
            List<CommentDetail> commentDetailList = commentMapper.selectCommentListByPassageId("test1", i, 1);
            for (CommentDetail commentDetail : commentDetailList) {
                System.out.println("第" + i + "页数据：" + commentDetail);
            }
        }

    }


    /**
     * 测试条件查询评论
     */
    @Test
    public void test5() {
        //构造条件
        RefreshDTO refreshDTO = new RefreshDTO();
        refreshDTO.setPassageId("test1");
        refreshDTO.setEndFloor(4);
        refreshDTO.setStartFloor(1);

        ConditionMap conditionMap = new ConditionMap(refreshDTO);
        System.out.println("条件为" + conditionMap);

        //进行条件查询
        conditionMap.removeAll();
        conditionMap.addCondition("passageId", "test1");
        conditionMap.addCondition("endFloor", 4);

        List<CommentDetail> commentDetailList = commentMapper.selectCommentListByCondition(conditionMap.getConditionMap());

        System.out.println(commentDetailList);

    }
}