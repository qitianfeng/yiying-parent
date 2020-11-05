package com.yiying.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QiException extends RuntimeException{
    @ApiModelProperty("状态码")
    private Integer code;

    private String msg;
}
