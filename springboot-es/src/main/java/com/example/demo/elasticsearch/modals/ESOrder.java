package com.example.demo.elasticsearch.modals;

import lombok.Data;

@Data
public class ESOrder extends ESBaseModal
{
    String orderId;
    String orderNo;

    @Override
    public String getId(){
        return  orderId;
    }
}
