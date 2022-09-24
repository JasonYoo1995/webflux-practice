package webflux.practice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WebfluxController {
    /**
     * Controller의 리턴값을 스프링이 알아서 구독한다
     * 즉, 스프링이 알아서 Publisher.subscribe()를 호출하므로
     * subscribe()을 호출해줄 필요가 없다
     */
    @GetMapping("1")
    Mono<Void> practice1(){
        System.out.println("yes");
        return null;
    }
}
