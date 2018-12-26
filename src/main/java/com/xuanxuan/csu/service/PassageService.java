package com.xuanxuan.csu.service;

import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.dto.RefreshDTO;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Passage;
import com.xuanxuan.csu.core.Service;
import com.xuanxuan.csu.vo.CommentRefreshVO;
import com.xuanxuan.csu.vo.CommentVO;
import com.xuanxuan.csu.vo.PassageVO;

import java.util.List;


/**
 * Created by PualrDwade on 2018/12/03.
 * 文章业务类,处理文章模块的业务
 */
public interface PassageService extends Service<Passage> {

    /**
     * 根据文章id得到文章的回复内容
     *
     * @param passageId
     * @param page      分页的页数
     * @return
     */
    public List<CommentVO> getComments(String passageId, int page);


    /**
     * 生成文章id(后期可以考虑开发后台管理,用来自动生成文章ID,暂时不用)
     *
     * @param url
     * @return
     */
    public String genOpenId(String url);


    /**
     * 刷新起始楼层到终止楼层的所有评论内容,同时查询终止楼层以上的最新楼层
     *
     * @param refreshDTO
     * @return
     */
    public CommentRefreshVO getRefreshComments(RefreshDTO refreshDTO);

}
