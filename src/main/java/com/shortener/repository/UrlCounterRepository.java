package com.shortener.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shortener.entity.UrlCounter;

public interface UrlCounterRepository extends MongoRepository<UrlCounter,Integer> {
    
}
