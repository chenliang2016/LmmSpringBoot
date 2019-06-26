package com.example.demo.elasticsearch.service;

import com.example.demo.elasticsearch.esbuild.Aggs;
import com.example.demo.elasticsearch.esbuild.ESQueryBuilder;
import com.example.demo.elasticsearch.modals.ESOrder;
import com.example.demo.elasticsearch.returnObj.ESReturnObj;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ESebService extends IESBaseService<ESOrder>{

    @Override
    public String getType() {
        return "/kibana_sample_data_ecommerce/_doc/";
    }

    public ESReturnObj queryOrderList(Integer page,Integer size) {

        ESQueryBuilder esQueryBuilder = new ESQueryBuilder();

        int from = 0;

        if (page != null){
            from = (page - 1) * size;
        }

        esQueryBuilder
                .addSort("customer_birth_date","desc")
                .setFrom(from)
                .setSize(size)
                .addMatch("customer_full_name","Eddie")
                .addTerm("currency","EUR");

        String[] type  = new String[1];
        type[0] = "order";
        esQueryBuilder.addTerms("type",type);

        String queryString = esQueryBuilder.build();

        ESReturnObj responseEntity = null;
        try{
            responseEntity =  query(queryString);
        }catch (Exception e){
            e.printStackTrace();
        }

        return responseEntity;
    }

    public Map count() {

        ESQueryBuilder esQueryBuilder = new ESQueryBuilder();

        esQueryBuilder
                .setSize(0);

        Aggs aggs = new Aggs.Builder()
                .setAggsKey("group_by_device")
                .addGroupField("customer_full_name.keyword")
                .addAggs("taxful_total_priceSum","taxful_total_price", Aggs.Builder.SUM)
                .build();

        esQueryBuilder.addAggs(aggs);

        String queryString = esQueryBuilder.build();

        ESReturnObj returnObj = null;

        try{
            returnObj =  query(queryString);
        }catch (Exception e){
            e.printStackTrace();
        }

        Map returnAggs = returnObj.getAggregations();

        return returnAggs;
    }

}
