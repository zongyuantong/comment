package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.CommentMapper;
import com.xuanxuan.csu.dao.ReplyMapper;
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
import org.springframework.data.util.CastUtils;
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
    private ReplyMapper replyMapper;

    @Resource
    private CommentMapper commentMapper;


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

        //如果已经存在,对前端抛出提示
        if (userStarList.size() != 0) {
            throw new ServiceException("你已经点过赞了");
        }

        //对目标进行点赞
        UserStar userStar = new UserStar();
        BeanUtils.copyProperties(starDTO, userStar);
        userStarMapper.insert(userStar);

        //将对应的点赞数+1
        Object target = starDTO.getToType().equals(1) ?
                commentMapper.selectByPrimaryKey(starDTO.getToId()) :
                replyMapper.selectByPrimaryKey(starDTO.getToId());
        if (target instanceof Comment) {
            ((Comment) target).setZanNum(((Comment) target).getZanNum() + 1);
            commentMapper.updateByPrimaryKeySelective((Comment) target);
        } else if (target instanceof Reply) {
            ((Reply) target).setZanNum(((Reply) target).getZanNum() + 1);
            replyMapper.updateByPrimaryKeySelective((Reply) target);
        }
    }
}
