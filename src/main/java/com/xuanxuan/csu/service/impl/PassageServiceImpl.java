package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.PassageMapper;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.RefreshDTO;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.CommentDetail;
import com.xuanxuan.csu.model.Passage;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.core.AbstractService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.util.VoConvertor;
import com.xuanxuan.csu.vo.CommentRefreshVO;
import com.xuanxuan.csu.vo.CommentVO;
import com.xuanxuan.csu.vo.PassageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.*;


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

    @Resource
    private VoConvertor<CommentDetail, CommentVO> convertor;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserInfoService userInfoService;

    @Override
    public List<CommentVO> getComments(String passageId) {
        List<CommentDetail> commentDetailList = commentMapper.selectCommentListByPassageId(passageId);
        List<CommentVO> commentVOList = new ArrayList<>();
        for (CommentDetail commentDetail : commentDetailList) {
            CommentVO commentVO = convertor.conver2Vo(commentDetail);
            commentVOList.add(commentVO);
        }
        return commentVOList;
    }


    @Override
    public String genOpenId(String url) {
        //放入文章url,得到UUID
        Condition condition = new Condition(Passage.class);
        condition.createCriteria().andCondition("url=", url);
        List<Passage> list = passageMapper.selectByCondition(condition);
        if (list.size() == 0) {
            Passage passage = new Passage();
            passage.setUrl(url);
            passage.setId(UUID.randomUUID().toString());
            passageMapper.insert(passage);
            String id = passageMapper.select(passage).get(0).getId();
            return id;
        } else {
            return list.get(0).getId();
        }
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

}