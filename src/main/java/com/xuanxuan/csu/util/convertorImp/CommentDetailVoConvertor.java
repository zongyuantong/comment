package com.xuanxuan.csu.util.convertorImp;


import com.xuanxuan.csu.model.CommentDetail;
import com.xuanxuan.csu.model.Reply;
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

    /**
     * 将评论详情的DO转化为视图对象
     *
     * @param model
     * @return
     */
    @Override
    public CommentVO conver2Vo(CommentDetail commentDetail) {
        System.out.println("评论详情:" + commentDetail);
        //首先将属性进行拷贝
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(commentDetail, commentVO);
        commentVO.setCreateTime(dateTranStrategy.conver2Show(commentDetail.getCreateTime()));
        //转化回复内容进行转化
        List<ReplyVO> replyVOList = new ArrayList<>();
        //列表不为空，只可能size为0
        for (Reply reply : commentDetail.getReplyList()) {
            //对评论进行转换
            //首先简单拷贝属性
            ReplyVO replyVO = new ReplyVO();
            BeanUtils.copyProperties(reply, replyVO);
            //设置时间
            replyVO.setCreateTime(dateTranStrategy.conver2Show(reply.getCreateTime()));
            replyVOList.add(replyVO);
        }
        //添加评论列表
        commentVO.setReplyList(replyVOList);
        return commentVO;
    }
}
