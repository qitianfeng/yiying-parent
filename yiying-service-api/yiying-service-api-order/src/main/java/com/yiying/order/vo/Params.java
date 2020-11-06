package com.yiying.order.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class Params implements Serializable {
    private  Integer length;
    private int[][] list;
    private int[][] msg;
}
