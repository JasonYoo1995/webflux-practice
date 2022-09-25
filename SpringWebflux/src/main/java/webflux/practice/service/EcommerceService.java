package webflux.practice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import webflux.practice.model.biz.Product;
import webflux.practice.model.request.ProductOfUserRequest;
import webflux.practice.model.request.ProductRequest;
import webflux.practice.repository.RestTemplateAPICall;
import webflux.practice.repository.WebClientAPICall;

@Service
@AllArgsConstructor
public class EcommerceService {
    WebClientAPICall webClientAPICall;
    RestTemplateAPICall restTemplateAPICall;

    public Flux<Product> getProductList() {
        return webClientAPICall.getProductList();
    }

    public Flux<Product> getProductListOfUserByWebClient(ProductOfUserRequest request) {
        Flux<Long> productIdFlux = webClientAPICall.getProductListOfUser(request); /** (1) */
        return productIdFlux.flatMap(productId -> webClientAPICall.getProductById(new ProductRequest(productId))); /** (2) */
        /** (1) -> (2)는 순차 수행되지만, (2)에서는 productIdFlux 내부의 모든 요소(=productId)들에 대하여 병렬로 외부 API를 호출한다 */
    }

    public Flux<Product> getProductListOfUserByRestTemplate(ProductOfUserRequest request) {
        Flux<Long> productIdFlux = restTemplateAPICall.getProductListOfUser(request); /** (1) */
        return productIdFlux.flatMap(productId -> restTemplateAPICall.getProductById(new ProductRequest(productId))); /** (2) */
        /** (2)에서 flatMap을 썼음에도 불구하고, restTemplate에 의해 Webflux의 싱글 스레드가 Block 되기 때문에
         * productIdFlux 내부의 모든 요소(=productId)들에 대하여 순차적으로 외부 API를 호출한다
         * 즉, getProductListOfUserByWebClient() 메서드보다 더 오래 소요된다
         */
    }
}