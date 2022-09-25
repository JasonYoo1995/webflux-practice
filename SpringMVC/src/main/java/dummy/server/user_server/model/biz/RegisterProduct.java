package dummy.server.user_server.model.biz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterProduct {
    private Long id;
    private Long userId; // FK
    private Long productId; // FK
}
