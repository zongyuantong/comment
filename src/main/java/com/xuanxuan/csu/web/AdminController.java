package com.xuanxuan.csu.web;

import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.model.Admin;
import com.xuanxuan.csu.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by PualrDwade on 2018/12/22.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    /**
     * @param admin
     * @return
     */

    @PostMapping
    @ApiOperation(value = "管理员登陆")
    public Result login(@Valid @RequestBody Admin admin) {
        String sessionId = adminService.login(admin);
        if (sessionId != null) {
            return ResultGenerator.genSuccessResult(sessionId);

        } else {
            return ResultGenerator.genFailResult(null);
        }
    }
}