package dummy.server.user_server.db;

import dummy.server.user_server.model.biz.RegisterProduct;
import dummy.server.user_server.model.biz.User;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Getter
public class UserDatabase implements InitializingBean {
    private List<User> userTable;
    private List<RegisterProduct> registerProductTable;

    public UserDatabase() {
        this.userTable = new LinkedList<>();
        this.registerProductTable = new LinkedList<>();
    }

    @Override
    public void afterPropertiesSet() {
        this.userTable.add(new User(0L, "Jason"));
        this.userTable.add(new User(1L, "Daniel"));
        this.userTable.add(new User(2L, "Paul"));

        this.registerProductTable.add(new RegisterProduct(20L, 0L, 10L));
        this.registerProductTable.add(new RegisterProduct(21L, 0L, 11L));
        this.registerProductTable.add(new RegisterProduct(22L, 1L, 12L));
        this.registerProductTable.add(new RegisterProduct(23L, 1L, 13L));
        this.registerProductTable.add(new RegisterProduct(24L, 2L, 14L));
        this.registerProductTable.add(new RegisterProduct(25L, 2L, 15L));
    }
}
