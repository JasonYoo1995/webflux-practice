package dummy.server.product_server.db;

import dummy.server.product_server.model.biz.Product;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
public class ProductDatabase implements InitializingBean {
    private List<Product> productTable;

    public ProductDatabase() {
        this.productTable = new LinkedList<>();
    }

    @Override
    public void afterPropertiesSet() {
        this.productTable.add(new Product(10L, "Desktop PC", 3_000_000L));
        this.productTable.add(new Product(11L, "iPad", 1_000_000L));
        this.productTable.add(new Product(12L, "Smart Watch", 300_000L));
        this.productTable.add(new Product(13L, "Laptop", 2_000_000L));
        this.productTable.add(new Product(14L, "Keyboard", 200_000L));
        this.productTable.add(new Product(15L, "Mouse", 50_000L));
    }
}
