package com.intuit.comparisonapi.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.comparisonapi.models.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class ElasticCarRepo implements CarRepo {

    private static final Logger logger = LoggerFactory.getLogger(ElasticCarRepo.class);

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Value("${elasticsearch.index}")
    private String index;

    @Value("${elasticsearch.type}")
    private String docType;

    @Value("${elasticsearch.count}")
    private int resultCount;

    @Value("${elasticsearch.similafields}")
    private String[] fields;


    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String saveCar(Car car) {
        UUID uuid = UUID.randomUUID();
        car.setId(uuid.toString());
        IndexRequest request = new IndexRequest.Builder<Car>().index(index).type(docType).id(uuid.toString()).document(car).build();

        IndexResponse indexResponse = null;
        try {
            indexResponse = elasticsearchClient.index(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return indexResponse.id();
    }

    @Override
    public Car searchCar(String id) {
        GetRequest getRequest = new GetRequest.Builder().index(index).type(docType).id(id).build();
        try {
            return elasticsearchClient.get(getRequest, Car.class).source();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getSimilarCars(Car car) {
        LikeDocument likeDocument = LikeBuilders.document().index(index).type(docType).id(car.getId()).build();
        Like like = new Like.Builder().document(likeDocument).build();
        MoreLikeThisQuery moreLikeThisQuery = new MoreLikeThisQuery.Builder().fields(Arrays.asList(fields)).maxQueryTerms(12).minTermFreq(1).like(like).build();
        Query query = new Query.Builder().moreLikeThis(moreLikeThisQuery).build();
        SearchRequest searchRequest = new SearchRequest.Builder().index(index).type(docType).query(query).size(resultCount).build();
        SearchResponse searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(searchRequest, Car.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HitsMetadata<Car> carsMeta = searchResponse.hits();
        return convertResponseToActual(searchResponse, Car.class);
    }


    private String convertCarToJSONString(Car car) {
        try {
            return objectMapper.writeValueAsString(car);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> convertResponseToActual(SearchResponse searchResponse, Class<?> clazz) {
        HitsMetadata hits = null;
        List<T> response = new ArrayList<>();
        if (null != searchResponse) {
            hits = searchResponse.hits();
            try {
                if (hits != null) {
                    List<Hit> searchHits = hits.hits();
                    if (searchHits != null && searchHits.size() >= 1) {
                        for (Hit hit : searchHits) {
                            T data = (T) hit.source();
                            response.add(data);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }

}
