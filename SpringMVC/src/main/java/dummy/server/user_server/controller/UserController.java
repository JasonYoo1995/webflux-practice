package dummy.server.user_server.controller;

import dummy.server.user_server.model.biz.RegisterProduct;
import dummy.server.user_server.model.request.ProductOfUserRequest;
import dummy.server.user_server.db.UserDatabase;
import dummy.server.user_server.model.response.ProductIdList;
import dummy.server.util.DelayForProcess;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserController {
    UserDatabase userDatabase;

    @PostMapping("/registered-product")
    ProductIdList getListOfProductByUser(@RequestBody ProductOfUserRequest request) {
        DelayForProcess.delay("/user/registered-product", 2);

        Long userId = request.getUserId();
        Iterator<RegisterProduct> iterator = userDatabase.getRegisterProductTable().iterator();
        ProductIdList productIdList = new ProductIdList();
        while (iterator.hasNext()) {
            RegisterProduct registerProduct = iterator.next();
            if (userId == registerProduct.getUserId()) {
                productIdList.getProductIdList().add(registerProduct.getProductId());
            }
        }
        return productIdList;
    }
}