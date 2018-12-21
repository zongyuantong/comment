package com.xuanxuan.csu.web;

import com.xuanxuan.csu.announce.LoginRequired;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.dto.StarDTO;
import com.xuanxuan.csu.model.UserStar;
import com.xuanxuan.csu.service.StarService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by PualrDwade on 2018/12/03.
 */
@RestController
@RequestMapping("/api/star")
public class StarController {
    @Resource
    private StarService starService;

    @ApiOperation(value = "对评论进行点赞")
    @PostMapping
    @LoginRequired
    public Result add(@RequestBody StarDTO star, HttpServletRequest request) {
        String openId = (String) request.getAttribute("openId");
        star.setUserId(openId);
        starService.zan(star);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "对评论取消点赞")
    @DeleteMapping
    @LoginRequired
    public Result delete(@RequestBody StarDTO star, HttpServletRequest request) {
        String openId = (String) request.getAttribute("openId");
        star.setUserId(openId);
        starService.cancelZan(star);
        return ResultGenerator.genSuccessResult();
    }

}
