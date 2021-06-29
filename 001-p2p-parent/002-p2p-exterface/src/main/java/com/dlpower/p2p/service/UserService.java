package com.dlpower.p2p.service;

import com.dlpower.p2p.model.user.User;

public interface UserService {

    /**
     * 获取平台用户数
     * @return
     */
    Long queryAllUserCount();

    User queryUserByPhone(String phone);

    User register(String phone, String loginPassword) throws Exception;
}
