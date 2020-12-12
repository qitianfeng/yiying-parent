package com.yiying.search.service;

import com.alibaba.fastjson.JSON;
import com.yiying.search.vo.SearchInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResultMapperImpl implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

        //1.创建一个当前页的记录集合对象
        List<T> content = new ArrayList<>();

        if(response.getHits()==null || response.getHits().getTotalHits()<=0){
            return new AggregatedPageImpl<T>(content);
        }
        //搜索到的结果集
        for (SearchHit searchHit : response.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();//每一个行的数据 json的 数据
            SearchInfo skuInfo = JSON.parseObject(sourceAsString, SearchInfo.class);

            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();//key :高亮的字段名  value 就是该字段的高亮的数据集合

            HighlightField highlightField = highlightFields.get("title");
            //有高亮的数据
            if(highlightField!=null){
                StringBuffer buffer = new StringBuffer();//有高亮的数据
                //取高亮的数据
                for (Text text : highlightField.getFragments()) {
                    String string = text.string();
                    buffer.append(string);
                }
                skuInfo.setTitle(buffer.toString());//有高亮的数据
            }
            content.add((T)skuInfo);
        }


        //2.创建分页的对象 已有

        //3.获取总个记录数
        long totalHits = response.getHits().getTotalHits();

        //4.获取所有聚合函数的结果
        Aggregations aggregations = response.getAggregations();

        //5.深度分页的ID
        String scrollId = response.getScrollId();


        return new AggregatedPageImpl<T>(content,pageable,totalHits,aggregations,scrollId);
    }

    @Override
    public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
        return null;
    }
}
