package com.dlpower.p2p.exception;

/**
 * description: 用户操作相关异常
 *
 * @author chenlanjiang
 * @date 2021/6/29
 */
public class UserOperationException extends RuntimeException{

    public UserOperationException(String message) {
        super(message);
    }
}
