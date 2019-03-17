package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.AbstractService;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.PassageMapper;
import com.xuanxuan.csu.dao.ReplyMapper;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.CommentDetail;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.util.CommonUtil;
import com.xuanxuan.csu.util.VoConvertor;
import com.xuanxuan.csu.vo.CommentVO;
import com.xuanxuan.csu.vo.UserStateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author PualrDwade
 * Created by PualrDwade on 2018/12/03.
 * @note :有重复代码,考虑用建造者模式,简化CommentDTO对象的构建,提高代码简洁度
 */
//@Service
@Component
@Transactional
public class CommentServiceImpl extends AbstractService<Comment> implements CommentService {

    /***
     * 注入do到vo的转换器
     */
    @Resource
    VoConvertor<CommentDetail, CommentVO> convertor;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PassageMapper passageMapper;


    /**
     * 根据评论id得到评论信息以及所有的复回复信息详情
     *
     * @param commentId 评论id
     */
    @Override
    public CommentVO getCommentDetail(String commentId) {
        CommentDetail commentDetail = commentMapper.selectCommentDetailById(commentId);
        if (commentDetail == null) throw new ServiceException("评论id不存在");
        CommentVO commentVO = convertor.converToVo(commentDetail);
        return commentVO;
    }

    /**
     * 增加一条新的评论
     *
     * @param commentDTO 评论数据出传输对象
     */
    @Override
    public void addNewComment(CommentDTO commentDTO) {
        commentDTO.setContent(CommonUtil.unicode(commentDTO.getContent()));
        //首先转化为model对象
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        //得到评论的楼层数
        Condition condition = new Condition(Comment.class);
        condition.createCriteria().andCondition("passage_id=", commentDTO.getPassageId());
        condition.orderBy("floor").desc();//最大楼层排序在最上面
        List<Comment> comments = commentMapper.selectByCondition(condition);
        //判断楼层数
        if (comments.size() != 0) {
            comment.setFloor(comments.get(0).getFloor() + 1);
        } else {
            //从1楼开始
            comment.setFloor(1);
        }
        System.out.println("待添加评论的时间:" + comment.getCreateTime());
        commentMapper.insert(comment);
    }

    /**
     * 更新一条评论信息
     *
     * @param commentDTO
     */
    @Override
    public void updateComment(CommentDTO commentDTO) {
        //查询是否存在此评论
        Comment comment = commentMapper.selectByPrimaryKey(commentDTO.getCommentId());
        if (comment == null) throw new ServiceException("评论id错误");

        if (!comment.getPassageId().equals(commentDTO.getPassageId())) throw new ServiceException("文章id错误");

        if (!comment.getFromUid().equals(commentDTO.getFromUid())) throw new ServiceException("所属用户id错误");

        //进行保存
        comment.setContent(commentDTO.getContent());
        commentMapper.updateByPrimaryKey(comment);

    }


    /**
     * 重载方法,对单个进行点赞过滤
     *
     * @param commentVO
     * @param sessionId
     */
    @Override
    public void commentsFilter(CommentVO commentVO, String sessionId) {
        List<CommentVO> commentVOList = new ArrayList<>();
        commentVOList.add(commentVO);
        commentsFilter(commentVOList, sessionId);
    }


    /**
     * 删除一条评论
     *
     * @param commentId
     */
    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) throw new ServiceException("评论id错误");

        Condition condition = new Condition(Reply.class);
        condition.createCriteria().andCondition("comment_id=", commentId);
        replyMapper.deleteByCondition(condition);
        commentMapper.deleteByPrimaryKey(commentId);
    }

    /**
     * 通过用户id进行点赞过滤
     *
     * @param commentVOList
     * @param sessionId
     */
    @Override
    public void commentsFilter(List<CommentVO> commentVOList, String sessionId) {
        //判断session是否有效
        if (StringUtils.isEmpty(sessionId)) {
            return;
        }
        String openId = (String) redisTemplate.opsForValue().get(sessionId);
        if (!StringUtils.isEmpty(openId)) {
            UserStateVO userStateVO = userInfoService.getUserState(openId);
            //根据点赞信息,设置对应字段
            commentVOList.forEach(commentVO -> {
                commentVO.setIsZan(userStateVO.getCommentStarList().contains(commentVO.getId()));
                //对评论的回复同时进行判断
                commentVO.getReplyList().forEach(replyVO -> replyVO.setIsZan(userStateVO.getReplyStarList().contains(replyVO.getId())));
            });
        }
    }
}
