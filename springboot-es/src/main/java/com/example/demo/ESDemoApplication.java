package com.example.demo;

import com.example.demo.elasticsearch.returnObj.ESReturnObj;
import com.example.demo.elasticsearch.service.ESOrderService;
import com.example.demo.elasticsearch.service.ESebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class ESDemoApplication implements ApplicationRunner {

	@Autowired
	private ESebService eSebService;

	@Autowired
	private ESOrderService esOrderService;

	public static void main(String[] args) {
		SpringApplication.run(ESDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		System.out.print("#################开始插入order ###############\n");
		esOrderService.pubObject();

		System.out.print("#################插入order 成功 ###############\n");
		System.out.print("#################在kibana使用 GET /testorder/_doc/_search 命令进行查询 ###############\n");


		System.out.print("#################查询 start ###############\n");
		ESReturnObj esReturnObj =  eSebService.queryOrderList(1,10);

		System.out.print("#################查询结果###############\n");

		System.out.print(esReturnObj);
		System.out.print("\n");

		System.out.print("#################统计 start ###############\n");
		Map map =  eSebService.count();

		System.out.print("#################统计结果###############\n");

		System.out.print(map);

	}
}
