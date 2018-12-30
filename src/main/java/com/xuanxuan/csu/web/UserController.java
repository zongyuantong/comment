package com.xuanxuan.csu.web;

import com.xuanxuan.csu.configurer.announce.LoginRequired;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultGenerator;
import com.xuanxuan.csu.dto.UserDTO;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by PualrDwade on 2018/12/03.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserInfoService userInfoService;


    /**
     * 小程序请求登陆
     *
     * @param userDTO
     * @return
     */
    @ApiOperation(value = "登陆请求api,存储sessionId并返回openId")
    @GetMapping("/login")
    public Result login(@RequestParam String code) {
        Map<String, String> resultMap = userInfoService.login(code);
        return ResultGenerator.genSuccessResult(resultMap);
    }


    /**
     * 用户数据授权同步接口
     *
     * @param userDTO
     * @return
     */
    @ApiOperation(value = "同步用户数据api,每次重新登陆都需要同步")
    @PostMapping("/auth")
    @LoginRequired
    public Result auth(@Valid @RequestBody UserDTO userDTO) {
        userInfoService.auth(userDTO);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 查看用户详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "得到用户详细信息")
    public Result detail(@PathVariable String id) {
        UserInfo userInfo = userInfoService.findById(id);
        return ResultGenerator.genSuccessResult(userInfo);
    }


    /**
     * 得到所有用户
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "得到所有用户信息")
    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<UserInfo> list = userInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
