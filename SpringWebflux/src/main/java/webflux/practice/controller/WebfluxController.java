package webflux.practice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import webflux.practice.model.biz.Product;
import webflux.practice.model.request.ProductOfUserRequest;
import webflux.practice.service.EcommerceService;

/**
 * Controller의 리턴값을 스프링이 알아서 구독한다
 * 즉, 스프링이 알아서 Publisher.subscribe()를 호출하므로
 * subscribe()을 호출해줄 필요가 없다
 */

@RestController
@Slf4j
@AllArgsConstructor
public class WebfluxController {
    EcommerceService ecommerceService;

    /**
     * [Use Case 1]
     * 등록되어 있는 모든 상품 목록 조회
     */
    @GetMapping("/product/list")
    Flux<Product> getProductList(){
        return ecommerceService.getProductList();
    }

    /**
     * [Use Case 2]
     * 특정 판매자가 등록한 상품 목록 조회 (RestTemplate으로 통신)
     */
    @PostMapping("/user/registered-product/rest-template")
    Flux<Product> getProductListOfUserByRestTemplate(@RequestBody ProductOfUserRequest request){
        return ecommerceService.getProductListOfUserByRestTemplate(request);
    }

    /**
     * [Use Case 2]
     * 특정 판매자가 등록한 상품 목록 조회 (WebClient로 통신)
     */
    @PostMapping("/user/registered-product/web-client")
    Flux<Product> getProductListOfUserByWebClient(@RequestBody ProductOfUserRequest request){
        return ecommerceService.getProductListOfUserByWebClient(request);
    }

    /**
     * [Use Case 3]
     * 특정 유저가 특정 상품을 선택했다는 이벤트가 발생하고
     * 이 이벤트가 분석툴 서버로 전송된다
     */
    @PostMapping("/event")
    void sendEvent(){
        // TODO
    }
}
