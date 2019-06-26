package com.example.demo.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.elasticsearch.modals.ESBaseModal;
import com.example.demo.elasticsearch.returnObj.ESReturnObj;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public abstract class IESBaseService<T extends ESBaseModal> {

    public abstract String getType();

    @Autowired
    private RestClient client;

    protected String getEndPoint(){
        String endpoint =  getType() ;
        return endpoint;
    }

    /**
     * 异步执行HTTP请求
     * @return
     */
    protected Object asynchronous() {
        Request request = new Request(
                "GET",
                "/");
        client.performRequestAsync(request, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                System.out.println("异步执行HTTP请求并成功");
            }

            @Override
            public void onFailure(Exception exception) {
                System.out.println("异步执行HTTP请求并失败");
            }
        });
        return null;
    }

    protected Object add(String id,T t) throws IOException {
        // endpoint直接指定为index/type的形式
        String endpoint =  getEndPoint();
        String url = new StringBuilder(endpoint).
                append(id).toString();
        Request request = new Request("PUT", url);
        request.addParameter("pretty", "true");

        System.out.println(JSONObject.toJSON(t).toString());
        request.setEntity(new NStringEntity(JSONObject.toJSON(t).toString(),ContentType.APPLICATION_JSON));

        Response response = client.performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());
        return responseBody;
    }

    protected Object add(T t) throws IOException {
        // endpoint直接指定为index/type的形式
        String endpoint =  getEndPoint();
        String url = new StringBuilder(endpoint).
                append(t.getId()).toString();
        Request request = new Request("PUT", url);
        request.addParameter("pretty", "true");

        request.setEntity(new NStringEntity(JSONObject.toJSON(t).toString(),ContentType.APPLICATION_JSON));

        Response response = client.performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());
        return responseBody;
    }

    protected ESReturnObj getById(String id) {
        String endpoint =  getEndPoint();

        String searchString = new StringBuilder(endpoint).append(id).toString();

        Request request = new Request("GET", searchString);

        // 添加json返回优化
        request.addParameter("pretty", "true");
        Response response = null;
        String responseBody = null;

        ESReturnObj esReturnObj = null;

        try {
            // 执行HHTP请求
            response = client.performRequest(request);
            responseBody = EntityUtils.toString(response.getEntity());

            esReturnObj = JSONObject.parseObject(responseBody,ESReturnObj.class);
        } catch (IOException e) {
            return null;
        }
        return esReturnObj;
    }

    protected Object deteteById(String id) {
        String endpoint =  getEndPoint();

        String deleteString = new StringBuilder(endpoint).append(id).toString();

        Request request = new Request("DELETE", deleteString);

        // 添加json返回优化
        request.addParameter("pretty", "true");
        Response response = null;
        String responseBody = null;
        try {
            // 执行HHTP请求
            response = client.performRequest(request);

            responseBody = EntityUtils.toString(response.getEntity());

            JSONObject.parse(responseBody);
        } catch (IOException e) {
            return null;
        }
        return responseBody;
    }


    protected Object update(String id,T t) throws IOException {
        String endpoint =  getEndPoint();
        // 构造HTTP请求
        Request request = new Request("POST", new StringBuilder(endpoint).
                append(id).append("/_update").toString());
        request.addParameter("pretty", "true");

        // 将数据丢进去，这里一定要外包一层“doc”，否则内部不能识别
//        JSONObject.toJSON(t).toString()
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("doc", JSONObject.toJSON(t));
        request.setEntity(new NStringEntity(jsonObject.toString()));

        // 执行HTTP请求
        Response response = client.performRequest(request);

        // 获取返回的内容
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }

    // 搜索
    protected ESReturnObj query(String queryString) throws IOException {
        String endpoint =  getEndPoint();
        // 构造HTTP请求
        Request request = new Request("GET", new StringBuilder(endpoint).append("_search").toString());
        request.addParameter("pretty", "true");

        HttpEntity entity = new NStringEntity(queryString,
                ContentType.APPLICATION_JSON);

        request.setEntity(entity);

        // 执行HTTP请求
        Response response = client.performRequest(request);

        // 获取返回的内容
        String responseBody = EntityUtils.toString(response.getEntity());

        ESReturnObj esReturnObj =  JSON.parseObject(responseBody, ESReturnObj.class);

        return esReturnObj;
    }

    // 批量插入
    protected Object bulk(List<T> list) throws IOException {
        String endpoint =  getEndPoint();
        // 构造HTTP请求
        Request request = new Request("POST", new StringBuilder(endpoint).append("_bulk").toString());
        request.addParameter("pretty", "true");
        String tString = "";
        for (T t : list) {
            tString = tString + "{ \"index\": { \"_id\": \""+t.getId()+"\" }}" + "\n";
            tString = tString + JSONObject.toJSON(t).toString() + "\n";
        }

        HttpEntity entity = new NStringEntity(tString,
                ContentType.APPLICATION_JSON);

        request.setEntity(entity);

        // 执行HTTP请求
        Response response = client.performRequest(request);

        // 获取返回的内容
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }

}
