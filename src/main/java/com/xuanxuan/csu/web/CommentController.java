package com.xuanxuan.csu.web;

import com.xuanxuan.csu.configurer.announce.LoginRequired;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.vo.CommentVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PualrDwade on 2018/12/03.
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Resource
    private CommentService commentService;


    /**
     * 添加新评论
     *
     * @param commentDTO
     * @return
     * @apiNote 此方法需要login状态检测
     */
    @ApiOperation(value = "添加文章新评论")
    @PostMapping
    @LoginRequired
    public Result add(@Valid @RequestBody CommentDTO commentDTO) {
        commentService.addNewComment(commentDTO);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 更新评论内容,需要登陆状态验证
     *
     * @param commentDTO 评论的数据传输对象
     * @param id         评论对应id,
     * @return
     */

    @ApiOperation(value = "更新评论内容")
    @PutMapping
    @LoginRequired
    public Result updateComment(@Valid @RequestBody CommentDTO commentDTO) {
        commentService.updateComment(commentDTO);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 删除评论内容的api,需要登陆状态
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除评论内容")
    @DeleteMapping("/{commentId}")
    @LoginRequired
    public Result deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * @param id
     * @return
     * @apiNote 查看评论的详情信息
     */
    @ApiOperation(value = "查看评论详情信息")
    @GetMapping("/{commentId}/replys")
    public Result CommentDetail(@PathVariable String commentId, HttpServletRequest request) {
        CommentVO commentVO = commentService.getCommentDetail(commentId);
        commentService.commentsFilter(commentVO, request.getHeader("sessionId"));
        return ResultGenerator.genSuccessResult(commentVO);
    }

}
