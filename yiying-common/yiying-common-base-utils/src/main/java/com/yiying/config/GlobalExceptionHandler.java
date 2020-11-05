package com.yiying.config;


import com.yiying.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error();
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.error().message("执行了自定义异常");
    }

    @ExceptionHandler(QiException.class)
    @ResponseBody
    public Result error(QiException e){
        e.printStackTrace();
        return Result.ok().error().message(e.getMsg()).code(e.getCode());
    }
}
