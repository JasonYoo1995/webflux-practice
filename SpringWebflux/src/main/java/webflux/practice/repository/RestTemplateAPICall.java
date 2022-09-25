package webflux.practice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.practice.model.biz.Product;
import webflux.practice.model.request.ProductOfUserRequest;
import webflux.practice.model.request.ProductRequest;
import webflux.practice.model.response.ProductIdList;

@Repository
public class RestTemplateAPICall implements InitializingBean {
    RestTemplate restTemplate;

    public Flux<Long> getProductListOfUser(ProductOfUserRequest request) {
        String url = "http://localhost:81/user/registered-product";
        ProductIdList productIdList = restTemplate.postForObject(url, request, ProductIdList.class);
        Flux<Long> idFlux = Flux.fromIterable(productIdList.getProductIdList());
        return idFlux;
    }

    public Mono<Product> getProductById(ProductRequest request) {
        String url = "http://localhost:81/product/id";
        Product productArray = restTemplate.postForObject(url, request, Product.class);
        return Mono.just(productArray);
    }

    @Override
    public void afterPropertiesSet() {
        this.restTemplate = new RestTemplateBuilder()
                .messageConverters(
                        new MappingJackson2HttpMessageConverter(
                                new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                        )
                ).build();
    }
}