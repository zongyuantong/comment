package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.PassageMapper;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.RefreshDTO;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.Passage;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.core.AbstractService;
import com.xuanxuan.csu.util.VoConvertor;
import com.xuanxuan.csu.vo.CommentRefreshVO;
import com.xuanxuan.csu.vo.CommentVO;
import com.xuanxuan.csu.vo.PassageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author PualrDwade
 * Created by PualrDwade on 2018/12/03.
 */
@Service
@Transactional
public class PassageServiceImpl extends AbstractService<Passage> implements PassageService {
    @Resource
    private PassageMapper passageMapper;

    @Resource
    private CommentService commentService;

    @Resource
    private VoConvertor<Comment, CommentVO> commentVoConvertor;


    @Override
    public PassageVO getPassageDetailById(String passageId) {
        Passage passage = passageMapper.selectByPrimaryKey(passageId);
        if (passage == null) throw new RuntimeException("文章id错误");
        //文章存在,首先copy必要数据
        PassageVO passageVO = new PassageVO(passage);
        //构造评论list
        List<CommentVO> commentVOList = new ArrayList<>();
        Condition condition = new Condition(Comment.class);
        condition.createCriteria().andCondition("passage_id=", passageId);
        condition.orderBy("floor").desc();
        List<Comment> commentList = commentService.findByCondition(condition);
        System.out.println(commentList.size());
        //评论为空
        if (commentList.size() == 0 || commentList == null) {
            passageVO.setCommentVOList(new ArrayList<>());
            passageVO.setCommentNum(0);
            return passageVO;
        }
        //评论不为空,构造评论数据
        for (Comment item : commentList) {
            commentVOList.add(commentVoConvertor.conver2Vo(item));
        }
        passageVO.setCommentVOList(commentVOList);
        passageVO.setCommentNum(commentList.size());
        return passageVO;
    }


    @Override
    public List<CommentVO> getComments(String passageId) {
        List<CommentVO> commentVOList = new ArrayList<>();
        Condition condition = new Condition(Comment.class);
        condition.createCriteria().andCondition("passage_id=", passageId);
        condition.orderBy("floor").desc();
        List<Comment> commentList = commentService.findByCondition(condition);
        System.out.println("查询的大小:" + commentList.size());
        if (commentList.size() == 0 || commentList == null) return commentVOList;
        for (Comment item : commentList) {
            commentVOList.add(commentVoConvertor.conver2Vo(item));
        }
        return commentVOList;
    }


    @Override
    public String genOpenId(String url) {
        Condition condition = new Condition(Passage.class);
        condition.createCriteria().andCondition("url=", url);
        List list = passageMapper.selectByCondition(condition);
        if (list == null || list.size() == 0) {
            Passage passage = new Passage();
            passage.setUrl(url);
            passageMapper.insert(passage);
            String id = passageMapper.select(passage).get(0).getId();
            return id;
        } else throw new ServiceException("此文章已存在,无法生成openId");
    }

    @Override
    public CommentRefreshVO getRefreshComments(RefreshDTO refreshDTO) {
        int startFloor = refreshDTO.getStartFloor();
        int endFloor = refreshDTO.getEndFloor();
        CommentRefreshVO commentRefreshVO = new CommentRefreshVO();

        //1. 首先创建容器存放最新的评论内容
        List<CommentVO> newComments = new ArrayList<>();
        //查询楼层数比endFloor更大的
        Condition condition = new Condition(Comment.class);
        condition.createCriteria().andCondition("floor>", endFloor);
        condition.orderBy("floor").desc();//按照楼层数反序排序
        List<Comment> commentList = commentService.findByCondition(condition);
        //转化为VO
        for (Comment comment : commentList) {
            newComments.add(commentVoConvertor.conver2Vo(comment));
        }
        commentRefreshVO.setAddNum(commentList.size());
        commentRefreshVO.setNewComments(newComments);

        //2. 刷新已经加载过的评论(start to end)
        List<CommentVO> refreshComments = new ArrayList<>();
        Condition condition1 = new Condition(Comment.class);
        condition1.createCriteria().andCondition("floor>=", startFloor).andCondition("floor<=", endFloor);
        condition.orderBy("floor").desc();
        List<Comment> commentList2 = commentService.findByCondition(condition1);
        for (Comment comment : commentList2) {
            refreshComments.add(commentVoConvertor.conver2Vo(comment));
        }
        commentRefreshVO.setRefreshComments(refreshComments);

        return commentRefreshVO;

    }


    //    /**
//     * 得到热门评论list
//     *
//     * @param id 文章id
//     * @return
//     */
//    @Override
//    public List findHotByPassageId(String passageId) {
//        List<CommentVO> commentVOList = new ArrayList<>();
//        Condition condition = new Condition(Comment.class);
//        condition.createCriteria().andCondition("passage_id=", passageId);
//        //使用热门评论策略进行选择
//        List<Comment> commentList = hotCommentStrategy.getHotCommentList(commentMapper.selectByCondition(condition));
//        //拦截判断,如果没有任何评论,直接返回空list
//        if (commentList == null || commentList.size() == 0) return commentVOList;
//        //开始构造返回数据
//        //循环计算
//        for (Comment comment : commentList) {
//            //循环完,构建一个完整的评论回复实体
//            commentVOList.add(commentVoConvertor.conver2Vo(comment));
//        }
//        return commentVOList;
//    }
}
