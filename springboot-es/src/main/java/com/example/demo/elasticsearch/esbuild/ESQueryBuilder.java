package com.example.demo.elasticsearch.esbuild;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESQueryBuilder {

    private List mustArray;

    private Map<String,Object> must;

    private ESSearch esSearch;

    private Integer from;

    private Integer size;

    private Aggs aggs;

    Map<String,Object> sortMap ;

    public ESQueryBuilder(){
        mustArray = new ArrayList();
    }

    public ESQueryBuilder addTerm(String key ,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);

        Map<String,Object> term = new HashMap<>();
        term.put("term",map);

        mustArray.add(term);
        return this;
    }

    public ESQueryBuilder addMatch(String key ,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);

        Map<String,Object> match = new HashMap<>();
        match.put("match",map);

        mustArray.add(match);
        return this;
    }

    public ESQueryBuilder addTerms(String key ,Object value){

        Map<String, Object> map = new HashMap<>();
        map.put(key,value);

        Map<String,Object> terms = new HashMap<>();
        terms.put("terms",map);

        mustArray.add(terms);

        return this;
    }

    public ESQueryBuilder addRange(Range range){
        if (range != null && range.getRange() != null){
            mustArray.add(range);
        }
        return this;
    }

    public ESQueryBuilder addAggs(Aggs aggs){
        if (aggs != null && aggs.getAggs() != null){
            this.aggs = aggs;
        }
        return this;
    }

    public ESQueryBuilder addSort(String sortKey ,Object sortType){

        if (sortMap == null){
            sortMap = new HashMap<>();
        }

        Map<String,Object> order = new HashMap<>();
        order.put("order",sortType);
        sortMap.put(sortKey,order);
        return this;
    }

    public ESQueryBuilder setFrom(int from){
        this.from = from;
        return this;
    }

    public ESQueryBuilder setSize(int size){
        this.size = size;
        return this;
    }

    public String build(){
        must = new HashMap<>();
        must.put("must",mustArray);

        esSearch = new ESSearch();

        // 添加query start
        Map<String,Object> querymap = new HashMap<>();
        querymap.put("bool",must);

        esSearch.setQuery(querymap);
        // 添加query end

        if (sortMap != null){
            esSearch.setSort(sortMap);
        }

        if (from != null){
            esSearch.setFrom(from);
        }

        if (size != null){
            esSearch.setSize(size);
        }

        if (aggs != null){
            esSearch.setAggs(aggs.getAggs());
        }

        String searchString = JSONObject.toJSONString(esSearch);

        return searchString;

    }

}
