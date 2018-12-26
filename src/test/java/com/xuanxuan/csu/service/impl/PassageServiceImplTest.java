package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.RefreshDTO;
import com.xuanxuan.csu.model.Passage;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.vo.CommentRefreshVO;
import com.xuanxuan.csu.vo.CommentVO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

public class PassageServiceImplTest extends Tester {

    /**
     * 通过文章Id得到文章详细信息
     */

    @Resource
    private PassageService passageService;

    @Resource
    private CommentService commentService;

    @Resource
    private UserInfoService userInfoService;


    /**
     * 通过文章Id得到文章评论(高楼层到低楼层)
     */
    @Test
    public void getComments() {
        //得到文章列表
        List<Passage> passages = passageService.findAll();
        //循环得到所有文章的所有评论
        for (Passage passage : passages) {
            List<CommentVO> commentVOList = passageService.getComments(passage.getId());
            //打印输出
            for (CommentVO commentVO : commentVOList) {
                System.out.println(commentVO);
            }
        }
    }

    /**
     * 测试生成openId
     */
    @Test
    public void genOpenId() {
    }

    /**
     * 测试得到刷新文章评论
     */
    @Test
    public void getRefreshComments() {
        //首先加载一页数据
        List<CommentVO> oldCommentVOList = passageService.getComments("test1");
        //打印结果显示
        for (CommentVO commentVO : oldCommentVOList) {
            System.out.println(commentVO);
        }

        //随后添加一条新的数据
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setFromUid("test2");
        commentDTO.setPassageId("test1");
        commentDTO.setContent("test update");
        commentService.addNewComment(commentDTO);


        //查看更新情况
        RefreshDTO refreshDTO = new RefreshDTO();
        refreshDTO.setEndFloor(oldCommentVOList.get(0).getFloor());
        refreshDTO.setStartFloor(oldCommentVOList.get(oldCommentVOList.size() - 1).getFloor());
        CommentRefreshVO commentRefreshVO = passageService.getRefreshComments(refreshDTO);
        System.out.println(commentRefreshVO);
    }
}