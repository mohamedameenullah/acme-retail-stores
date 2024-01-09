package com.acme.retail.acmeretailstores.processor;

import com.acme.retail.acmeretailstores.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ProductProcessor implements ItemProcessor<Product, Product> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

    @Override
    public Product process(Product item) throws Exception {
        item.setDate(new Date());
        return item;
    }
}
