package com.xuanxuan.csu.web;

import com.xuanxuan.csu.announce.LoginRequired;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.dto.ReplyDTO;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.service.ReplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by PualrDwade on 2018/12/03.
 */
@RestController
@RequestMapping("/api/reply")
public class ReplyController {
    @Resource
    private ReplyService replyService;


    /**
     * 新增用户回复内容
     *
     * @param reply
     * @return
     */
    @ApiOperation(value = "添加回复内容")
    @PostMapping
    @LoginRequired
    public Result add(@Valid @RequestBody ReplyDTO replyDTO) {
        replyService.addNewReply(replyDTO);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 删除用户的回复内容
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除回复内容")
    @DeleteMapping("/{id}")
    @LoginRequired
    public Result delete(@PathVariable String id) {
        replyService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
}
