package com.yiying.movie.dto.excell;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelSubjectData {
    @ExcelProperty(index = 0)
    private String firstSubjectName;
    @ExcelProperty(index = 1)
    private String secondSubjectName;
}
