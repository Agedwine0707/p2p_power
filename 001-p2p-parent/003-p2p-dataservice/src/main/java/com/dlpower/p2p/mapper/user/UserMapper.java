package com.dlpower.p2p.mapper.user;


import com.dlpower.p2p.model.user.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    /**
     * 查询平台总用户数
     * @return
     */
    Long selectAllUserCount();

    User selectUserByPhone(String phone);
}