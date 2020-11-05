package com.yiying.movie.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectObjNestVo {
    private String id;
    private  String label;
    private List<SubjectObjVo> children = new ArrayList<>();
}
