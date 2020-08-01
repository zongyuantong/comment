package com.xuanxuan.csu.util.impl;


import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.util.HotCommentStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotCommentByStarNum implements HotCommentStrategy {
    @Override
    public List<Comment> getHotCommentList(List<Comment> commentList) {
        //重写比较方法以按照starnum排序
        commentList.sort((o1, o2) -> {
            if (o1.getZanNum() > o2.getZanNum()) return 1;
            else if (o1.getZanNum() == o2.getZanNum()) return 0;
            else return -1;
        });
        return commentList;
    }

}
