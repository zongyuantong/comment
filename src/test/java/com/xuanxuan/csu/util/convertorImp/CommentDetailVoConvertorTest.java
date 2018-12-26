package com.xuanxuan.csu.util.convertorImp;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.CommentDetail;
import com.xuanxuan.csu.model.Passage;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.util.VoConvertor;
import com.xuanxuan.csu.vo.CommentVO;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;


/**
 * DO->VO转换器测试类
 */
public class CommentDetailVoConvertorTest extends Tester {

    @Resource
    private VoConvertor<CommentDetail, CommentVO> convertor;

    @Resource
    private CommentService commentService;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PassageService passageService;

    @Resource
    private ReplyService replyService;

    @Test
    public void testNull() {
        assertNotNull(convertor);
        assertNotNull(commentMapper);
    }

    @Test
    public void conver2Vo() {
        /**
         * 进行测试
         */
        //得到评论详情
        List<Comment> comments = commentMapper.selectAll();
        for (Comment comment : comments) {
            CommentDetail commentDetail = commentMapper.selectCommentDetailById(comment.getId());
            CommentVO commentVO = convertor.conver2Vo(commentDetail);
            System.out.println(commentVO);
        }
    }
}