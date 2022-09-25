package dummy.server.product_server.controller;

import dummy.server.product_server.db.ProductDatabase;
import dummy.server.product_server.model.biz.Product;
import dummy.server.product_server.model.request.ProductRequest;
import dummy.server.util.DelayForProcess;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
@Slf4j
public class ProductController {
    ProductDatabase productDatabase;

    @GetMapping("/list")
    List<Product> getProductList(){
        DelayForProcess.delay("/product/id", 2);

        return productDatabase.getProductTable();
    }

    @PostMapping("/id")
    Product getProductById(@RequestBody ProductRequest request){
        DelayForProcess.delay("/product/id", 2);

        Long productId = request.getProductId();
        Iterator<Product> iterator = productDatabase.getProductTable().iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (productId == product.getId()) {
                return product;
            }
        }
        return null;
    }
}