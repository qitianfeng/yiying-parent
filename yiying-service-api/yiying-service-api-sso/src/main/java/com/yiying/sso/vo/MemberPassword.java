package com.yiying.sso.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberPassword implements Serializable {
    private String oldSecret;
    private String newSecret;
}
