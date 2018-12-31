package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.PassageMapper;
import com.xuanxuan.csu.dao.ReplyMapper;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.model.*;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.util.VoConvertor;
import com.xuanxuan.csu.vo.CommentVO;
import com.xuanxuan.csu.vo.ReplyVO;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.core.AbstractService;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.util.HotCommentStrategy;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    private CommentMapper commentMapper;

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PassageMapper passageMapper;


    @Override
    public CommentVO getCommentDetail(String commentId) {
        CommentDetail commentDetail = commentMapper.selectCommentDetailById(commentId);
        if (commentDetail == null) throw new ServiceException("评论id不存在");
        CommentVO commentVO = convertor.conver2Vo(commentDetail);
        return commentVO;
    }


    @Override
    public void addNewComment(CommentDTO commentDTO) {
        //首先转化为model对象
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        //得到评论的楼层数
        Condition condition = new Condition(Comment.class);
        condition.createCriteria().andCondition("passage_id=", commentDTO.getPassageId());
        condition.orderBy("floor").desc();//最大楼层排序在最上面
        List<Comment> comments = commentMapper.selectByCondition(condition);

        if (comments.size() != 0) {
            comment.setFloor(comments.get(0).getFloor() + 1);
        } else {
            //从1楼开始
            comment.setFloor(1);
        }
        commentMapper.insert(comment);

    }


    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if (comment == null) throw new ServiceException("评论id错误");

        Condition condition = new Condition(Reply.class);
        condition.createCriteria().andCondition("comment_id=", commentId);
        replyMapper.deleteByCondition(condition);
        commentMapper.deleteByPrimaryKey(commentId);

    }


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
}
