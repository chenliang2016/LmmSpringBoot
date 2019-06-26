package com.example.demo.elasticsearch.service;

import com.example.demo.elasticsearch.modals.ESOrder;
import com.example.demo.elasticsearch.returnObj.ESReturnObj;
import org.springframework.stereotype.Service;


@Service
public class ESOrderService extends IESBaseService<ESOrder> {

    @Override
    public String getType() {
        return "/testorder/_doc/";
    }

    public void pubObject() {
        ESOrder order = new ESOrder();
        order.setOrderId("111");
        order.setOrderNo("test");

        try{
            add(order);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ESReturnObj getOrder(String orderId){
        ESReturnObj eorder =  getById(orderId);
        return  eorder;
    }


}
