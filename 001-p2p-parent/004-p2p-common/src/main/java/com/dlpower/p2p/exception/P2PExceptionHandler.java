package com.dlpower.p2p.exception;

import com.dlpower.p2p.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * description: 全局异常处理器
 *
 * @author chenlanjiang
 * @date 2021/6/29
 */
@ControllerAdvice
public class P2PExceptionHandler {

    /**
     * 用户操作异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UserOperationException.class)
    public Map<Object, Object> UserException(Exception e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    /**
     * 运行时异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Map<Object, Object> runTimeException(Exception e) {
        e.printStackTrace();
        return Result.error("系统繁忙，请稍后重试" + e.getMessage());
    }
}
