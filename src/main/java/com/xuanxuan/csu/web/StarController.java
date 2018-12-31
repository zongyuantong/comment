package com.xuanxuan.csu.web;

import com.xuanxuan.csu.configurer.announce.LoginRequired;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.dto.StarDTO;
import com.xuanxuan.csu.service.StarService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    public Result add(@Valid @RequestBody StarDTO star) {
        starService.zan(star);
        return ResultGenerator.genSuccessResult();
    }

}
