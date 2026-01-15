package com.shortener.service.serviceImpl;

import com.shortener.dto.UrlRequest;
import com.shortener.entity.UrlEntity;
import com.shortener.exception.UrlNotFoundException;
import com.shortener.repository.UrlRepository;
import com.shortener.service.TinyUrlService;
import com.shortener.utils.Base62Converter;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TinyUrlServiceImpl implements TinyUrlService {
    
    private final Logger logger = LoggerFactory.getLogger(TinyUrlServiceImpl.class);

    @Value("${base.url:http://localhost:8080}")
    private String BASE_URL;
    private final UrlRepository urlRepository;
    private final Base62Converter base62Converter;

    public TinyUrlServiceImpl(UrlRepository urlRepository, Base62Converter base62Converter) {
        this.urlRepository = urlRepository;
        this.base62Converter = base62Converter;
    }

    @Override
    public String generateShortUrl(UrlRequest urlRequest) {
        String shortCode = "";
        // Keep generating until a unique code is found
        do {
            shortCode = this.base62Converter.base62Encoder(7); // Use your random Base62 generator
        } while (urlRepository.existsByShortCode(shortCode));

        // Set the short code in the entity
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortCode(shortCode);
        urlEntity.setOriginalUrl(urlRequest.url());
        this.urlRepository.save(urlEntity);
        return BASE_URL+"/"+shortCode;
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
}
