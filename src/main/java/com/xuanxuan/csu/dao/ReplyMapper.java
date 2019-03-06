package com.xuanxuan.csu.dao;

import com.xuanxuan.csu.core.Mapper;
import com.xuanxuan.csu.model.Reply;

import java.util.List;

public interface ReplyMapper extends Mapper<Reply> {

    /**
     * 批量删除replyList接口
     *
     * @param replyList
     */
    public void batchDeleteReplys(List<String> ids);
}