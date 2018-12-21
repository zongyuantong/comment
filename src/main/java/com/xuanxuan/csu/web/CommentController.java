package com.xuanxuan.csu.web;

import com.xuanxuan.csu.announce.LoginRequired;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dto.CommentDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuanxuan.csu.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @PutMapping("/{commentId}")
    @LoginRequired
    public Result updateComment(@RequestBody CommentDTO commentDTO, @PathVariable String commentId) {
        commentService.updateComment(commentDTO, commentId);
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
    @ApiOperation(nickname = "请求得到评论的详情数据", value = "查看评论论详情信息")
    @GetMapping("/{commentId}/replys")
    @LoginRequired
    public Result CommentDetail(@PathVariable String commentId) {
        return ResultGenerator.genSuccessResult(commentService.getCommentDetail(commentId));
    }

}
