package com.home.api;

/**
 * 用户对外的接口
 */
public interface UserService {

    /**
     * 通过ID查询用户
     *
     * @param id
     * @return
     */
    User findById(int id);

}
