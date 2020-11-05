package com.yiying.search.mapper;

import com.yiying.search.vo.SearchInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchMapper extends ElasticsearchRepository<SearchInfo, String> {
}
