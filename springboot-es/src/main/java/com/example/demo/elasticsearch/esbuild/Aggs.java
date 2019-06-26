package com.example.demo.elasticsearch.esbuild;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Aggs {

    private Map<String,Object> aggs;

    private Aggs(Builder builder){
        if (builder.type == Builder.DIRECT){
            aggs = builder.subAggMap;
        }else{
            if (builder.aggsKey != null){
                aggs = new HashMap<>();
                aggs.put(builder.aggsKey,builder.aggsValue);
            }
        }
    }

    public static class Builder {
        private Map<String,Object> aggsValue;
        private String aggsKey;
        private Map<String,Object> subAggMap = new HashMap<>();

        public final static String SUM = "sum";
        public final static String AVG = "avg";
        public final static String MAX = "max";
        public final static String MIN = "min";

        public final static String DIRECT = "direct";
        public final static String GROUP = "group";

        private String type;

        public Builder(){
            aggsValue = new HashMap<>();
        }

        public Builder setType(String type){
            this.type = type;
            return this;
        }

        public Builder setAggsKey(String key){
            this.aggsKey = key;
            return this;
        }

        public Builder addGroupField(String field){

            Map<String,Object> fieldMap = new HashMap<>();
            fieldMap.put("field",field);
            aggsValue.put("terms",fieldMap);

            return this;
        }

        public Builder addAggs(String keyName,String fieldName,String type){

           if (subAggMap ==  null){
               subAggMap = new HashMap<>();
           }

           Map<String,Object> searchMap = new HashMap<>();

           if (type == SUM){
               Map<String,Object> fieldMap = new HashMap<>();
               fieldMap.put("field",fieldName);
               searchMap.put("sum",fieldMap);
           }

           subAggMap.put(keyName,searchMap);

           return this;
        }

        public Aggs build() {

            if (subAggMap != null){
                aggsValue.put("aggs",subAggMap);
            }

            return new Aggs(this);
        }
    }

}
