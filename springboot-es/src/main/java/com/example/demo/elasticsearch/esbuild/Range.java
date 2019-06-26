package com.example.demo.elasticsearch.esbuild;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Range {

    private Map<String,Object> range;

    private Range(Builder builder){
        if (builder.rangeKey != null){
            range = new HashMap<>();
            range.put(builder.rangeKey,builder.rangeValue);
        }
    }

    public static class Builder {
        private Map<String,Object> rangeValue;

        private String rangeKey;

        public final static String GT = "gt"; //大于
        public final static String LT = "lt"; //小于
        public final static String GTE = "gte"; //大于或等于
        public final static String LTE = "lte"; //大于

        public Builder(){
            rangeValue = new HashMap<>();
        }

        public Builder setRangeKey(String key){
            this.rangeKey = key;
            return this;
        }

        public Builder addRange(String key,Object value){
            rangeValue.put(key,value);
            return this;
        }

        public Range build() {
            return new Range(this);
        }
    }


}
