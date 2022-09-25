package dummy.server.product_server.model.biz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product {
    private Long id;
    private String name;
    private Long price;
}
