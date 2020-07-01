package com.example.spring06.endpoint;

import com.example.spring06.entity.CustomErrorType;
import com.example.spring06.entity.Product;
import com.example.spring06.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;

@RestController
public class ProductEndpoint {

    @Autowired
    private ProductModel productModel;

    @RequestMapping(path = "/endpoint/product/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable int id) {
        Optional<Product> optionalProduct = productModel.findById(id);
        if (((Optional) optionalProduct).isPresent()) {
            Product product = optionalProduct.get();
            product.setStatus(0);
            productModel.save(product);
            return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(new CustomErrorType("Unable to delete. Product with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/endpoint/product/delete-many", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestBody String ids) throws UnsupportedEncodingException {
        String[] splittedIds = java.net.URLDecoder.decode(ids, "UTF-8").split(",");
        Integer[] arrayIds = new Integer[splittedIds.length];
        for (int i = 0; i < splittedIds.length; i++) {
            arrayIds[i] = new Integer(splittedIds[i]);
        }
        Iterable<Product> list = productModel.findAllById(Arrays.asList(arrayIds));
        for (Product p :
                list) {
            p.setStatus(0);
        }
        productModel.saveAll(list);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }
}
