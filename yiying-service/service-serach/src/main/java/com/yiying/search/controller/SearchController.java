package com.yiying.search.controller;

import com.yiying.common.Result;
import com.yiying.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/import")
    public Result importEs() {

        searchService.importEs();
        return Result.ok();
    }

    @PostMapping
    public Result search(@RequestParam(required = false) Map<String, String> searchMap) {
        Object pageNum = searchMap.get("pageNum");
        if (pageNum == null) {
            searchMap.put("pageNum", "1");
        }
        if (pageNum instanceof Integer) {
            searchMap.put("pageNum", pageNum.toString());
        }

        //拼装url
        String urlString=null;
        StringBuilder url = new StringBuilder("/movie");
        if (searchMap != null && searchMap.size() > 0) {
            //是由查询条件
            url.append("?");
            for (String paramKey : searchMap.keySet()) {
                if (!"sortRule".equals(paramKey) && !"sortField".equals(paramKey) && !"pageNum".equals(paramKey)) {
                    if (searchMap.get(paramKey) != null&&searchMap.get(paramKey).length()>0) {
                        url.append(paramKey).append("=").append(searchMap.get(paramKey)).append("&");
                    }
                }
            }
            //http://localhost:9009/search/list?keywords=手机&spec_网络制式=4G&
            urlString = url.toString();
            //去除路径上的最后一个&
            urlString = urlString.substring(0, urlString.length() - 1);
//            model.addAttribute("url",urlString);
        } else {
//            model.addAttribute("url",url);
        }
        Map search = searchService.search(searchMap);
        return Result.ok().data(search).data("url", urlString);
    }
}
