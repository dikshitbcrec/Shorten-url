package com.shortener.service.serviceImpl;

import com.shortener.dto.UrlRequest;
import com.shortener.dto.UrlResponse;
import com.shortener.entity.UrlCounter;
import com.shortener.entity.UrlEntity;
import com.shortener.exception.UrlNotFoundException;
import com.shortener.repository.UrlCounterRepository;
import com.shortener.repository.UrlRepository;
import com.shortener.service.TinyUrlService;
import com.shortener.utils.Base62Converter;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class TinyUrlServiceImpl implements TinyUrlService {
    
    private final Logger logger = LoggerFactory.getLogger(TinyUrlServiceImpl.class);

    @Value("${base.url:http://localhost:8080}")
    private String BASE_URL;
    private final UrlRepository urlRepository;
    private final UrlCounterRepository counterRepository;
    private final Base62Converter base62Converter;
    private final MongoTemplate mongoTemplate;

    public TinyUrlServiceImpl(UrlRepository urlRepository, Base62Converter base62Converter,UrlCounterRepository counterRepository,MongoTemplate mongoTemplate) {
        this.urlRepository = urlRepository;
        this.base62Converter = base62Converter;
        this.counterRepository = counterRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UrlResponse generateShortUrl(UrlRequest urlRequest) {
        String shortCode = "";
        // Keep generating until a unique code is found
        do {
            shortCode = this.base62Converter.base62Encoder(7); // Use your random Base62 generator
        } while (urlRepository.existsByShortCode(shortCode));

        // Set the short code in the entity
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortCode(shortCode);
        urlEntity.setCreatedAT();
        urlEntity.setOriginalUrl(urlRequest.url());
        this.urlRepository.save(urlEntity);
        this.incrementGlobalCounter();
        return new UrlResponse( BASE_URL+"/"+shortCode);
    }

    @Override
    public String getUrl(String shortCode) {
        Optional<UrlEntity> urlDetails= this.urlRepository.findByShortCode(shortCode);
        logger.info("getUrl() method invoked", shortCode);

        if(urlDetails.isPresent()){
            UrlEntity urlEntity = urlDetails.get();
            return urlEntity.getOriginalUrl();
        }else{
            throw new UrlNotFoundException("URL not found");
        }
    }

    @Override
    public Long getTotalCount() {
         logger.info("getUrl() method invoked");
        Optional<UrlCounter> counterDetails= this.counterRepository.findById(1);
       

        if(counterDetails.isPresent()){
            return counterDetails.get().getCount();
        }else{
            throw new UrlNotFoundException("URL not found");
        }
    }

    public void incrementGlobalCounter() {
    Query query = new Query(Criteria.where("_id").is(1));
    Update update = new Update().inc("count", 1L);
    mongoTemplate.upsert(query, update, UrlCounter.class);
}
}
