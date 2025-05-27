package com.example.demo.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {
	private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5";
    
	private float getRate() {
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(API_URL, String.class);

		JSONArray jsonArray = new JSONArray(response);

		return jsonArray.toList().stream()
		        .map(obj -> new JSONObject((Map<?, ?>) obj))
		        .filter(currency -> "USD".equals(currency.getString("ccy")))
		        .findFirst()
		        .map(currency -> currency.getFloat("buy"))
		        .orElse(-1f);
	}
	
	public float getCurrency(float price) {
		return price / getRate();
	}
			
}

