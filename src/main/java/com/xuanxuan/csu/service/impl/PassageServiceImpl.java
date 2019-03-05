package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.configurer.AppConfigurer;
import com.xuanxuan.csu.core.ConditionMap;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.PassageMapper;
import com.xuanxuan.csu.dto.RefreshDTO;
import com.xuanxuan.csu.model.CommentDetail;
import com.xuanxuan.csu.model.Passage;
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

    @Resource
    private VoConvertor<CommentDetail, CommentVO> convertor;


    /**
     * 根据文章id得到文章的回复内容
     *
     * @param passageId
     * @param page      分页的页数
     * @return
     */
    @Override
    public List<CommentVO> getComments(String passageId, int page) {
        List<CommentDetail> commentDetailList = commentMapper.selectCommentListByPassageId(passageId, page, AppConfigurer.COMMENT_PAGE_SIZE);
        List<CommentVO> commentVOList = new ArrayList<>();
        commentDetailList.forEach(commentDetail -> {
            commentDetail.setReplyList(commentDetail.getReplyList().
                    subList(0, Math.min(AppConfigurer.COMMENT_REPLAY_NUMBER, commentDetail.getReplyList().size())));
            commentVOList.add(convertor.converToVo(commentDetail));
        });
        return commentVOList;
    }

    /**
     * 生成文章id(后期可以考虑开发后台管理,用来自动生成文章ID,暂时不用)
     *
     * @param url
     * @return
     */
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


    /**
     * 刷新起始楼层到终止楼层的所有评论内容,同时查询终止楼层以上的最新楼层
     *
     * @param refreshDTO
     * @return
     */
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
        newCommentDetailList.forEach(commentDetail -> newCommentVOList.add(convertor.converToVo(commentDetail)));
        commentRefreshVO.setAddNum(newCommentVOList.size());
        commentRefreshVO.setNewComments(newCommentVOList);

        /**2. 重新加载刷新已经加载过的评论(start to end)*/
        conditionMap.addCondition("startFloor", refreshDTO.getStartFloor());
        List<CommentDetail> refreshCommentDetailList = commentMapper.selectCommentListByCondition(conditionMap.getConditionMap());
        refreshCommentDetailList.forEach(refreshCommentDetail -> refreshCommentVOList.add(convertor.converToVo(refreshCommentDetail)));
        commentRefreshVO.setRefreshComments(refreshCommentVOList);

        //返回值
        return commentRefreshVO;
    }


    /**
     * 检查文章id是否存在于数据库,若没有则直接创建
     *
     * @param passageId
     */
    @Override
    public void checkPassageExist(String passageId) {
        if (passageMapper.selectByPrimaryKey(passageId) == null) {
            Passage passage = new Passage();
            passage.setId(passageId);
            passage.setUrl(AppConfigurer.DEFAULT_PASSAGE_URL);
            passage.setPlatformId(AppConfigurer.DEFAULT_PLATFORM);
            passageMapper.insert(passage);
        }
    }

}