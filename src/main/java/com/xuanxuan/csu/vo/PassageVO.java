package com.xuanxuan.csu.vo;


import com.xuanxuan.csu.model.Passage;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;


/**
 * @note 文章内容数据传输对象, 传递给控制层返回数据进行封装
 */
@Data
public class PassageVO extends AbstractVO {
    //文章的id
    private String id;

    //文章url
    private String url;

    //文章平台Id
    private String platformId;

    //一个文章有多个评论回复
    private List<CommentVO> commentVOList;

    public PassageVO(Passage passage) {
        super();
        BeanUtils.copyProperties(passage, this);
    }

    private int CommentNum;
}
