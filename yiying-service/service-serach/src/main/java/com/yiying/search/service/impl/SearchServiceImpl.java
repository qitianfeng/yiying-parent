package com.yiying.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.yiying.movie.entity.MMovie;
import com.yiying.movie.entity.MSubject;
import com.yiying.movie.service.MMovieService;
import com.yiying.movie.service.MSubjectService;
import com.yiying.search.mapper.SearchMapper;
import com.yiying.search.service.SearchResultMapperImpl;
import com.yiying.search.service.SearchService;
import com.yiying.search.vo.SearchInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jboss.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = Logger.getLogger(SearchServiceImpl.class);
    @Reference
    private MMovieService movieService;

    @Autowired
    private SearchMapper searchMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Reference
    private MSubjectService subjectService;


    @Override
    public void importEs() {
        //获取所有的电影信息
        List<MMovie> movieList = movieService.list();
        //将获取到的数据转化为searchInfo列表
        List<SearchInfo> searchInfos = new ArrayList<>();

        for (MMovie movie : movieList) {
            Map<String, Object> specMap = new HashMap<String, Object>();

            SearchInfo searchInfo = new SearchInfo();
            BeanUtils.copyProperties(movie, searchInfo);

            //拼接全部地区规格参数
            MSubject subject = subjectService.getById(movie.getSubjectId());
            MSubject subjectParent = subjectService.getById(movie.getSubjectParentId());
            specMap.put(subjectParent.getGenres(), subject.getGenres());

            //拼接全部年份规格参数
            String yearParent = subjectService.getById(movie.getSubjectYearParentId()).getGenres();
            String year = subjectService.getById(movie.getSubjectYearId()).getGenres();
            specMap.put(yearParent, year);

            //拼接全部类型规格参数
            String genres = subjectService.getById(movie.getSubjectGeneresParentId()).getGenres();
            String genres1 = subjectService.getById(movie.getSubjectGeneresId()).getGenres();
            specMap.put(genres, genres1);

            searchInfo.setSpecMap(specMap);
            String s = JSON.toJSONString(specMap);
            searchInfo.setSubjectSpec(s);
            searchInfos.add(searchInfo);
        }

        //调用spring data elasticsearch的API导入到es中
        searchMapper.saveAll(searchInfos);
    }


    /**
     * 根据查询条件查询数据并返回相应信息
     *
     * @param searchMap
     * @return
     */
    @Override
    public Map search(Map<String, String> searchMap) {


        //搜索条件构建对象，用于封装各种搜索条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        String keyWord = "";
        if (searchMap != null && searchMap.size() > 0) {
            //根据关键字搜索
            keyWord = searchMap.get("keywords");
            if (StringUtils.isEmpty(keyWord)) {
                keyWord = "";
                builder.withFilter(QueryBuilders.matchAllQuery()); //查询所有
            }
            if (!StringUtils.isEmpty(keyWord)) {

//                builder.withQuery(QueryBuilders.queryStringQuery(keyWord).field("title"));
                builder.withQuery(QueryBuilders.wildcardQuery("title","*"+keyWord+"*"));
            }
        }


        /**
         * 执行搜索，响应结果
         * 搜索条件封装
         * 搜索的结果集 需要转化的类型
         *
         */

        builder.addAggregation(AggregationBuilders.terms("subjectSpec").field("subjectSpec.keyword").size(500));


        //4.4 设置高亮的字段 设置前缀 和 后缀

        //设置高亮的字段 针对 商品的名称进行高亮
        if (!StringUtils.isEmpty(keyWord)) {
            HighlightBuilder.Field field = new HighlightBuilder.Field("title");
            //设置前缀 和 后缀
            field.preTags("<em style=\"color:red\">").postTags("</em>");
            field.fragmentSize(100);
            builder.withHighlightFields(field);
        }
//        builder.withQuery(QueryBuilders.multiMatchQuery(keyWord));


        //========================过滤查询 开始=====================================

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //4.4 过滤查询的条件设置   商品分类的条件
      /*  String subject = searchMap.get("subject");

        if (!StringUtils.isEmpty(subject)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("subject", subject));
        }*/

        //4.6 过滤查询的条件设置   规格条件

        if (searchMap != null) {
            for (String key : searchMap.keySet()) {//{ brand:"",category:"",spec_后端开发:"Java"}
                if (key.startsWith("spec_")) {
                    //截取规格的名称
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
                }
            }
        }
        //4.7 过滤查询的条件设置   价格区间的过滤查询
        String price = searchMap.get("price");// 0-500  3000-*
        if (!StringUtils.isEmpty(price)) {
            //获取值 按照- 切割
            String[] split = price.split("-");
            //过滤范围查询
            //0<=price<=500
            if (!split[1].equals("*")) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0], true).to(split[1], true));
            } else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }

        }
        //过滤查询
        builder.withFilter(boolQueryBuilder);

        //========================过滤查询 结束=====================================
        //分页查询

        //第一个参数:指定当前的页码  注意: 如果是第一页 数值为0
        //第二个参数:指定当前的页的显示的行
        String pageNum1 = searchMap.get("pageNum");
        Integer pageNum = Integer.valueOf(pageNum1);

        Integer pageSize = 30;

        builder.withPageable(PageRequest.of(pageNum - 1, pageSize));

        //排序操作
        //获取排序的字段 和要排序的规则
        //评分排序
        String sortField = searchMap.get("sortField");//score
        String sortRule = searchMap.get("sortRule");//DESC ASC
        if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
            //执行排序
            builder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equalsIgnoreCase("ASC") ? SortOrder.ASC : SortOrder.DESC));
            //nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }
        //价格排序
        String sortPriceField = searchMap.get("sortPriceField");//score
        String sortPriceRule = searchMap.get("sortPriceRule");//DESC ASC
        if (!StringUtils.isEmpty(sortPriceField) && !StringUtils.isEmpty(sortPriceRule)) {
            //执行排序
            builder.withSort(SortBuilders.fieldSort(sortPriceField).order(sortPriceRule.equalsIgnoreCase("ASC") ? SortOrder.ASC : SortOrder.DESC));
            //nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
        }
        //5.构建查询对象(封装了查询的语法)
        NativeSearchQuery nativeSearchQuery = builder.build();



        //6.执行查询
        AggregatedPage<SearchInfo> page = elasticsearchTemplate.queryForPage(
                nativeSearchQuery,
                SearchInfo.class,
                new SearchResultMapperImpl());

      /*  // 6.2 获取聚合分组结果
        StringTerms stringTermsCategory = (StringTerms) page.getAggregation("title");
        List<String> categoryList = getStringsCategoryList(stringTermsCategory);
*/
//6.4 获取 规格的分组结果 列表数据map
        StringTerms stringTermsSpec = (StringTerms) page.getAggregation("subjectSpec");

        Map<String, Set<String>> specMap = getStringSetMap(stringTermsSpec);
        logger.info(searchMap);
        //分页参数
        long totalElements = page.getTotalElements();

        //总页数
        int totalPages = page.getTotalPages();

        //获取数据结构集
        List<SearchInfo> content = page.getContent();
        logger.error(builder.toString());
        logger.info(keyWord);
        logger.error(content);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", content);
        resultMap.put("total", totalElements);
        resultMap.put("totalPage", totalPages);
        resultMap.put("specMap", specMap);
        logger.info(specMap);

        return resultMap;
    }


    private Map<String, Set<String>> getStringSetMap(StringTerms stringTermsSpec) {
        //key :规格的名称
        //value :规格名称对应的选项的多个值集合set
        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();
        Set<String> specValues = new HashSet<String>();
        if (stringTermsSpec != null) {
            //1. 获取分组的结果集
            for (StringTerms.Bucket bucket : stringTermsSpec.getBuckets()) {
                //2.去除结果集的每一行数据()
                String keyAsString = bucket.getKeyAsString();

                //3.转成JSON 对象  map  key :规格的名称  value:规格名对应的选项的单个值
                Map<String, String> map = JSON.parseObject(keyAsString, Map.class);
                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                    String key = stringStringEntry.getKey();//规格名称
                    String value = stringStringEntry.getValue();//规格的名称对应的单个选项值

                    //先从原来的specMap中 获取 某一个规格名称 对应的规格的选项值集合
                    specValues = specMap.get(key);
                    if (specValues == null) {
                        specValues = new HashSet<>();
                    }
                    specValues.add(value);
                    //4.提取map中的值放入到返回的map中
                    specMap.put(key, specValues);
                }
            }
        }
        return specMap;
    }
}
