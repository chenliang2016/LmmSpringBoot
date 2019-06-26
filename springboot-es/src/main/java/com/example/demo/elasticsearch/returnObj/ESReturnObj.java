package com.example.demo.elasticsearch.returnObj;

import lombok.Data;

import java.util.Map;

@Data
public class ESReturnObj {
    private Object _shards;
    private ESReturnHits hits;
    private Map<String,Object> aggregations;

}
