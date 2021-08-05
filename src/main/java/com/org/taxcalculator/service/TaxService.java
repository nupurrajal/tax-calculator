package com.org.taxcalculator.service;

import com.org.taxcalculator.model.Product;
import com.org.taxcalculator.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class TaxService {

    private static final Logger logger = LoggerFactory.getLogger(TaxService.class);

    private static Set<String> exemptedProductTypeList;

    @PostConstruct
    private void initProductTypeList() {
        exemptedProductTypeList = new HashSet<>();
        String[] exemptedProducts = Constants.EXEMPTED_GOODS_LIST.split(",");
        for (String exemptedProduct : exemptedProducts) {
            exemptedProductTypeList.add(exemptedProduct);
        }
    }

    public HashMap processListOfGoods(List<Product> listOfGoods) {
        double totalTax = 0;
        double totalCost = 0;
        HashMap<String, Double> response = new LinkedHashMap<>();
        try {
            for (Product good : listOfGoods) {

                int quantity = good.getQuantity();
                String goodName = good.getName();
                double price = good.getPrice();

                int taxCutoff = 0;
                String category = good.getCategory();
                boolean isExempted = exemptedProductTypeList.contains(category);
                double exemptedPrice = price;
                if (!isExempted) {
                    taxCutoff = Constants.TAX_CUT_OFF;
                    exemptedPrice = price * 1.1;
                }
                if (goodName.contains(Constants.IMPORTED)) {
                    taxCutoff = Constants.TAX_CUT_OFF_IMPORTED;
                    if (isExempted)
                        exemptedPrice = price * 1.05;
                    else
                        exemptedPrice = price * 1.15;
                }
                double currTax = exemptedPrice - price;
                totalTax += (double) Math.round(currTax * 100.0) / 100.0;
                response.put(String.valueOf(quantity) + " " + goodName, (double) Math.round(exemptedPrice * 100.0) / 100.0);
                totalCost += (double) Math.round(exemptedPrice * 100.0) / 100.0;
            }
            response.put("Tax", totalTax);
            response.put("Total", totalCost);
            return response;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid Quantity / Price: " + e.getMessage());
        }

    }

}
