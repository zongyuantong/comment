package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.configurer.AppConfigurer;
import com.xuanxuan.csu.core.ConditionMap;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.PassageMapper;
import com.xuanxuan.csu.dto.RefreshDTO;
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
    private CommentMapper commentMapper;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 注入do到vo的转换器
     */
    @Resource
    private VoConvertor<CommentDetail, CommentVO> convertor;

    @Override
    public List<CommentVO> getComments(String passageId, int page) {
        List<CommentDetail> commentDetailList = commentMapper.selectCommentListByPassageId(passageId, page, AppConfigurer.COMMENT_PAGE_SIZE);
        List<CommentVO> commentVOList = new ArrayList<>();
        commentDetailList.forEach(commentDetail -> {
            commentDetail.setReplyList(commentDetail.getReplyList().
                    subList(0, Math.min(AppConfigurer.COMMENT_REPLAY_NUMBER, commentDetail.getReplyList().size())));
            commentVOList.add(convertor.conver2Vo(commentDetail));
        });
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
        //判断对应文章是否存在
        Passage passage = passageMapper.selectByPrimaryKey(refreshDTO.getPassageId());

        if (passage == null) {
            throw new ServiceException("文章id不存在");
        }

        //存储最新评论
        List<CommentVO> newCommentVOList = new ArrayList<>();
        //存储刷新的评论
        List<CommentVO> refreshCommentVOList = new ArrayList<>();
        //存储刷新的响应实体
        CommentRefreshVO commentRefreshVO = new CommentRefreshVO();


        /**1. 刷新得到最新的数据(end->max)*/
        ConditionMap conditionMap = new ConditionMap(refreshDTO);
        conditionMap.removeCondition("startFloor");
        List<CommentDetail> newCommentDetailList = commentMapper.selectCommentListByCondition(conditionMap.getConditionMap());
        newCommentDetailList.forEach(commentDetail -> {
            newCommentVOList.add(convertor.conver2Vo(commentDetail));
        });
        commentRefreshVO.setAddNum(newCommentVOList.size());
        commentRefreshVO.setNewComments(newCommentVOList);

        /**2. 重新加载刷新已经加载过的评论(start to end)*/
        conditionMap.addCondition("startFloor", refreshDTO.getStartFloor());
        List<CommentDetail> refreshCommentDetailList = commentMapper.selectCommentListByCondition(conditionMap.getConditionMap());
        refreshCommentDetailList.forEach(refreshCommentDetail -> {
            refreshCommentVOList.add(convertor.conver2Vo(refreshCommentDetail));
        });
        commentRefreshVO.setRefreshComments(refreshCommentVOList);

        //返回值
        return commentRefreshVO;

    }

}