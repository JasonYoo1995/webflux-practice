package webflux.practice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.practice.model.biz.Product;
import webflux.practice.model.request.ProductOfUserRequest;
import webflux.practice.model.request.ProductRequest;
import webflux.practice.model.response.ProductIdList;

@Repository
@Slf4j
public class WebClientAPICall implements InitializingBean {
    /**
     * [Webflux에서 RestTemplate를 쓰면 안 되는 이유]
     * Webflux는 싱글 스레드이기 때문에
     * RestTemplate을 통해 외부 API에 요청하고 응답이 올때까지의 시간 동안 스레드가 Blocking되면
     * 우리 서버 내부의 다른 동작을 전혀 수행하지 못하고 오랫동안 자원을 낭비하게 된다
     * 즉, Spring MVC의 '멀티 스레드 + Blocking' 방식보다도 훨씬 느린 성능을 초래한다
     * WebClient의 Non-blocking Call 방식은
     * 외부 API의 요청 직후, 우리 서버 내부의 다른 동작을 수행하다가
     * 외부 API의 응답 시 Event Loop에 의해 Callback을 수행함으로써
     * 자원 이용률을 최대한 끌어올릴 수 있게 되고
     * 따라서 이러한 '싱글 스레드 + Non-blocking' 방식은 '멀리 스레드 + Blocking' 방식보다 높은 성능을 보여준다
     */

    WebClient webClient;

    public Flux<Product> getProductList() {
        String url = "localhost:81/product/list";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnComplete(() -> {
                    log.info("Succeed to call {}", url);
                })
                .doOnError(throwable -> {
                    log.error("Failed to call {} / {}", url, throwable.getStackTrace());
                });
    }

    public Flux<Long> getProductListOfUser(ProductOfUserRequest request) {
        String url = "localhost:81/user/registered-product";
        Mono<ProductIdList> productIdListMono = webClient.post()
                .uri(url)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ProductIdList.class)
                .doOnSuccess(productIdList -> {
                    log.info("Succeed to call {}", url);
                })
                .doOnError(throwable -> {
                    log.error("Failed to call {} / {}", url, throwable.getStackTrace());
                });
        return productIdListMono.flatMapIterable(productIdList -> productIdList.getProductIdList()); // flatMapIterable 참고 : https://tedblob.com/convert-mono-list-into-flux/
    }

    public Mono<Product> getProductById(ProductRequest request) {
        String url = "localhost:81/product/id";
        return webClient.post()
                .uri(url)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(product -> {
                    log.info("Succeed to call {}", url);
                })
                .doOnError(throwable -> {
                    log.error("Failed to call {} / {}", url, throwable.getStackTrace());
                });
    }

    @Override
    public void afterPropertiesSet() {
        this.webClient = WebClient.builder()
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(clientDefaultCodecsConfigurer -> {
                                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(
                                            new Jackson2JsonEncoder(
                                                    new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE),
                                                    MediaType.APPLICATION_JSON)
                                    );
                                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(
                                            new Jackson2JsonDecoder(
                                                    new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE),
                                                    MediaType.APPLICATION_JSON)
                                    );
                                })
                                .build()
                )
                .build();
    }

}