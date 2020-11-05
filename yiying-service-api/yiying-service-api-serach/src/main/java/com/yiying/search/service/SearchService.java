package com.yiying.search.service;

import org.springframework.stereotype.Repository;

import java.util.Map;

public interface SearchService {
    /**
     * 导入索引
     */
    void importEs();

    /**
     * 搜索索引
     * @param searchMap
     * @return
     */
    Map search(Map<String,String> searchMap);

}
