package com.example.demo.elasticsearch.returnObj;

import lombok.Data;

import java.util.List;

@Data
public class ESReturnHits {

    private List<Object> hits;
    private Integer total;
    private Integer max_score;
}
