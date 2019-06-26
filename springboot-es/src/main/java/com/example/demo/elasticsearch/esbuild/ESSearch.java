package com.example.demo.elasticsearch.esbuild;

import lombok.Data;

import java.util.Map;

@Data
public class ESSearch {
    private Map<String,Object> query;
    private Map<String,Object> sort;

    private Integer from;
    private Integer size;

    private Map<String,Object> aggs;

}
