package com.shortener.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class Base62Converter {

    private final Logger logger = LoggerFactory.getLogger(Base62Converter.class);

    // base 62 encoder + random
    private static final String  BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;
    private  SecureRandom random ;
    public Base62Converter() {
        this.random= new SecureRandom();
    }
   

    public String base62Encoder(int length){
        logger.info("base62Encoder() invoked ");
        StringBuilder shortCode = new StringBuilder();
        for(int i=0;i<length;i++)
        {
            int index = random.nextInt(BASE);
            shortCode.append(BASE62.charAt(index));
        }
        
        return shortCode.toString();
    }
}
