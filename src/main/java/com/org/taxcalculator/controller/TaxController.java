package com.org.taxcalculator.controller;

/*
    Author: nprj
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.org.taxcalculator.model.Product;
import com.org.taxcalculator.service.TaxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/calculate")
public class TaxController {

    private static final Logger logger = LoggerFactory.getLogger(TaxController.class);

    @Autowired
    TaxService taxService;

    @GetMapping(value = "/health")
    public ResponseEntity<Map> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Server is running!");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/tax-on-goods", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map> eventpublisher(@RequestBody String productListString) {
        Map<String, String> response = new HashMap<>();
        Map<String, Double> successResponse;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Product> productList = objectMapper.readValue(productListString, new TypeReference<List<Product>>(){});
            if (productList.size() < 1) {
                response.put("status", "failed");
                response.put("statusCode", String.valueOf(HttpStatus.NO_CONTENT));
                response.put("Message", "Received an empty list of products.");
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
            HashMap<String, Double> taxCalculated = taxService.processListOfGoods(productList);
            if (taxCalculated == null || taxCalculated.size() == 0) {
                response.put("status", "failed");
                response.put("statusCode", String.valueOf(HttpStatus.BAD_REQUEST));
                response.put("Message", "Error in calculation");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            successResponse = taxCalculated;
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            response.put("status", "failed");
            response.put("statusCode", String.valueOf(HttpStatus.BAD_REQUEST));
            response.put("Message", "Exception occurred while processing: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
