package com.xuanxuan.csu.util.impl;


import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.ReplyMapper;
import com.xuanxuan.csu.dao.UserInfoMapper;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.CommentDetail;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.util.CommonUtil;
import com.xuanxuan.csu.util.DateTranStrategy;
import com.xuanxuan.csu.util.VoConvertor;
import com.xuanxuan.csu.vo.CommentVO;
import com.xuanxuan.csu.vo.ReplyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论详情信息转化为commentVO
 */

@Component
public class CommentDetailVoConvertor implements VoConvertor<CommentDetail, CommentVO> {

    //注入日志转化策略
    @Resource
    private DateTranStrategy dateTranStrategy;

    //注入用户服务模块
    @Resource
    private UserInfoMapper userInfoMapper;

    //注入回复服务
    @Resource
    private ReplyMapper replyMapper;

    //注入评论
    @Resource
    private CommentMapper commentMapper;

    /**
     * 将评论详情的DO转化为视图对象
     *
     * @param model
     * @return
     */
    @Override
    public CommentVO converToVo(CommentDetail commentDetail) {
        //首先将属性进行拷贝
        if (commentDetail == null) return null;
        CommentVO commentVO = new CommentVO();
        commentVO.setContent(CommonUtil.decodeUnicode(commentDetail.getContent()));
        BeanUtils.copyProperties(commentDetail, commentVO);
        commentVO.setCreateTime(dateTranStrategy.converToShow(commentDetail.getCreateTime()));
        //设置用户信息
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(commentDetail.getFromUid());
        if (userInfo == null) {
            userInfo = UserInfo.getDefaultUserInfo();//不存在用户(脏数据),构造默认用户信息
        }
        commentVO.setUsername(userInfo.getNickName());
        commentVO.setAvatar(userInfo.getAvatarUrl());
        //转化回复内容进行转化
        List<ReplyVO> replyVOList = new ArrayList<>();
        //列表不为空，只可能size为0
        for (Reply reply : commentDetail.getReplyList()) {
            //首先简单拷贝属性
            ReplyVO replyVO = new ReplyVO();
            BeanUtils.copyProperties(reply, replyVO);
            //解码content
            replyVO.setContent(CommonUtil.decodeUnicode(replyVO.getContent()));
            //设置时间
            replyVO.setCreateTime(dateTranStrategy.converToShow(reply.getCreateTime()));
            //设置用户信息
            UserInfo fromUser = userInfoMapper.selectByPrimaryKey(replyVO.getFromUid());
            fromUser = fromUser == null ? UserInfo.getDefaultUserInfo() : fromUser;
            replyVO.setFromUname(fromUser.getNickName());
            replyVO.setAvatar(fromUser.getAvatarUrl());
            //判断到底是回复评论还是回复
            if (replyVO.getReplyType() == 1) {
                Comment target = commentMapper.selectByPrimaryKey(replyVO.getReplyId());
                UserInfo temp = userInfoMapper.selectByPrimaryKey(target.getFromUid());
                replyVO.setToUname(temp != null ? temp.getNickName() : "云麓学子");
                replyVO.setToUid(target.getFromUid());
            } else {
                Reply target = replyMapper.selectByPrimaryKey(replyVO.getReplyId());
                UserInfo temp = userInfoMapper.selectByPrimaryKey(target.getFromUid());
                replyVO.setToUname(temp != null ? temp.getNickName() : "云麓学子");
                replyVO.setToUid(target.getFromUid());
            }
            replyVOList.add(replyVO);
        }
        //添加评论列表
        commentVO.setReplyList(replyVOList);
        //设置用户信息，不适用联表查询是因为后面会考虑单独拆分用户模块
        return commentVO;
    }
}
