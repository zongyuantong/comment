package com.xuanxuan.csu.web;

import com.xuanxuan.csu.configurer.announce.LoginRequired;
import com.xuanxuan.csu.configurer.AppConfigurer;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.dto.RefreshDTO;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.PassageService;
import com.xuanxuan.csu.vo.CommentRefreshVO;
import com.xuanxuan.csu.vo.CommentVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by PualrDwade on 2018/12/03.
 */
@RestController
@RequestMapping("/api/passage")
public class PassageController {
    @Resource
    private PassageService passageService;

    @Resource
    private CommentService commentService;


    /**
     * @param url
     * @return
     * @apiNote 生成文章的id, 需要登陆
     */
    @ApiOperation(value = "申请文章接入Id")
    @PostMapping("/genOpenId")
    @LoginRequired
    public Result genOpenId(@RequestParam String url) {
        return ResultGenerator.genSuccessResult(passageService.genOpenId(url));
    }

    /**
     * @return
     * @apiNote 页面需要评论数据接口
     * @note 默认10条数据, 默认得到第一页数据
     * 请求此接口需要传入文章id,通过数据库查询得到评论与回复数据
     */
    @ApiOperation(value = "得到文章的详细评论信息")
    @GetMapping("/{passageId}/comments")
    public Result getComments(@RequestParam(defaultValue = "1") Integer page,
                              @PathVariable String passageId,
                              HttpServletRequest request) {
        if (page < 1) {
            //默认查询第一页
            page = 1;
        }
        page -= 1;
        page *= AppConfigurer.COMMENT_PAGE_SIZE;
        passageService.checkPassageExist(passageId);
        //查询评论详情数据
        List<CommentVO> list = passageService.getComments(passageId, page);
        //点赞过滤器
        commentService.commentsFilter(list, request.getHeader("sessionId"));
        return ResultGenerator.genSuccessResult(list);
    }


    /**
     *
     * @param refreshDTO
     * @return
     * @apiNote 得到刷新的数据
     */
    @ApiOperation(value = "刷新评论数据")
    @PostMapping("/comments/refresh")
    public Result getNewComments(@Valid @RequestBody RefreshDTO refreshDTO, HttpServletRequest request) {
        CommentRefreshVO commentRefreshVO = passageService.getRefreshComments(refreshDTO);
        commentService.commentsFilter(commentRefreshVO.getNewComments(), request.getHeader("sessionId"));
        commentService.commentsFilter(commentRefreshVO.getRefreshComments(), request.getHeader("sessionId"));
        return ResultGenerator.genSuccessResult(commentRefreshVO);
    }

    @ApiOperation(value = "删除该文章全部的评论")
    @DeleteMapping("/{passageId}/comments")
    public Result deleteComments(@PathVariable String passageId,
                                 HttpServletRequest request){
        passageService.deleteComments(passageId);
        return ResultGenerator.genSuccessResult();
    }

}
