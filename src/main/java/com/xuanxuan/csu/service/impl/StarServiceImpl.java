package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.UserStarMapper;
import com.xuanxuan.csu.dto.StarDTO;
import com.xuanxuan.csu.model.Comment;
import com.xuanxuan.csu.model.Reply;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.model.UserStar;
import com.xuanxuan.csu.service.CommentService;
import com.xuanxuan.csu.service.ReplyService;
import com.xuanxuan.csu.service.StarService;
import com.xuanxuan.csu.core.AbstractService;
import com.xuanxuan.csu.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by PualrDwade on 2018/12/03.
 */
@Service
@Transactional
public class StarServiceImpl extends AbstractService<UserStar> implements StarService {
    @Resource
    private UserStarMapper userStarMapper;

    @Resource
    private CommentService commentService;

    @Resource
    private ReplyService replyService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 用户点赞业务
     *
     * @param starDTO
     */
    @Override
    public void zan(StarDTO starDTO) {
        //判断词此条文章用户是否已经点赞
        Condition condition = new Condition(UserStar.class);
        condition.createCriteria().andCondition("to_id=", starDTO.getToId()).
                andCondition("user_id=", starDTO.getUserId());
        List<UserStar> userStarList = userStarMapper.selectByCondition(condition);
        //如果已经存在,则抛出异常
        System.out.println(userStarList);
        if (userStarList != null && userStarList.size() != 0) throw new RuntimeException("已经存在此用户的点赞记录");
        //得到点赞类型
        int type = 0;
        Comment temp = commentService.findById(starDTO.getToId());
        if (temp == null) type = 2;
        else type = 1;
        //不存在点赞.首先添加用户点赞记录
        if (type == 1) {
            Comment comment = commentService.findById(starDTO.getToId());
            if (comment == null) throw new RuntimeException("评论不存在");
            //添加一条用户点赞记录
            UserStar userStar = new UserStar();
            BeanUtils.copyProperties(starDTO, userStar);
            int num = userStarMapper.insert(userStar);
            System.out.println("插入成功,受影响条数:" + num);
            comment.setZanNum(comment.getZanNum() + 1);
            System.out.println("comment 更新成功");
            //然后判断用户是否存在,如果不存在,抛出异常(同时查看事务回滚的机制)
            UserInfo userInfo = userInfoService.findById(starDTO.getUserId());
            if (userInfo == null) throw new RuntimeException("用户不存在!");
            //的确发生了回滚
        } else if (type == 2) {
            Reply reply = replyService.findById(starDTO.getToId());
            if (reply == null) throw new RuntimeException("回复不存在");
            //由于数据库reply表没有点赞数(不需要经常展示)如果要,就使用条件查询从user_star中查询回复的点赞数
            //只需增加一条记录
            UserStar userStar = new UserStar();
            BeanUtils.copyProperties(starDTO, userStar);
            userStarMapper.insert(userStar);
        } else {
            throw new RuntimeException("对象类型错误");
        }
    }

    /**
     * 取消点赞业务
     *
     * @param starDTO
     */
    @Override
    public void cancelZan(StarDTO starDTO) {
        //首先判断是否已经赞了,如果没有赞,就谈不上点赞
        Condition condition = new Condition(UserStar.class);
        condition.createCriteria().andCondition("to_id", starDTO.getToId()).
                andCondition("user_id", starDTO.getUserId());
        List<UserStar> userStarList = userStarMapper.selectByCondition(condition);
        if (userStarList.size() == 0 || userStarList == null) throw new RuntimeException("不存在用户点赞信息,不能取消");
        //取消点赞
        System.out.println("userStarList:" + userStarList.size());
        userStarMapper.deleteByIds(userStarList.get(0).getId());
        //对应的点赞数量-1;
        Comment comment = commentService.findById(starDTO.getToId());
        if (comment != null) {
            comment.setZanNum(comment.getZanNum() - 1);
            if (comment.getZanNum() < 0) throw new ServiceException("点赞下溢");
            commentService.update(comment);
        }
    }
}
