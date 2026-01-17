# URL Shortener Service

Spring Boot application that generates short URLs using Base62 encoding with MongoDB storage and automatic expiration.

## Tech Stack

- Java 17
- Spring Boot 3.5.9
- MongoDB
- Maven

## Features

- Generate 7-character Base62 short codes
- Auto-expire URLs after 1 hour
- Global URL counter with atomic operations
- Collision detection for unique codes
- URL validation (http/https only)
- CORS enabled
- Global exception handling

## Project Structure

```
src/main/java/com/shortener/
├── controller/ShortnerController.java
├── dto/
│   ├── UrlRequest.java
│   └── UrlResponse.java
├── entity/
│   ├── UrlEntity.java
│   └── UrlCounter.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── UrlNotFoundException.java
├── repository/
│   ├── UrlRepository.java
│   └── UrlCounterRepository.java
├── service/
│   ├── TinyUrlService.java
│   └── serviceImpl/TinyUrlServiceImpl.java
├── utils/Base62Converter.java
└── ShortenerUrlApplication.java
```

## API Endpoints

### Create Short URL
```http
POST /tiny-url
Content-Type: application/json

{
  "url": "https://example.com"
}

Response:
{
  "shortUrl": "http://localhost:8080/abc1234",
  "count": 1
}
```

### Redirect to Original URL
```http
GET /{shortCode}

Response: 302 Found (redirects to original URL)
```

### Get Total Count
```http
GET /count

Response: 42
```

## Configuration

**application.properties:**
```properties
server.port=${PORT:8080}
spring.data.mongodb.uri=${DB_URL:mongodb://localhost:27017/tinyurl}
spring.data.mongodb.database=tiny_url
spring.data.mongodb.auto-index-creation=true
```

## Database Schema

### url Collection
- `_id`: Auto-generated
- `originalUrl`: Original long URL
- `shortCode`: 7-character unique code (indexed)
- `createdAt`: Timestamp with TTL index (expires after 1 hour)

### url_counter Collection
- `_id`: Always 1 (singleton)
- `count`: Total URLs generated

## Running the Application

### Local Development
```bash
mvn spring-boot:run
```

### Build
```bash
mvn clean package
```

### Docker
```bash
docker build -t url-shortener .
docker run -p 8080:8080 -e DB_URL=mongodb://host:27017/tinyurl url-shortener
```

## Prerequisites

- Java 17+
- MongoDB running on localhost:27017
- Maven 3.9+

## Dependencies

- spring-boot-starter-web
- spring-boot-starter-data-mongodb
- spring-boot-starter-validation
- jakarta.validation-api

## How It Works

1. **URL Shortening**: Generates random 7-character Base62 code, checks for collisions, stores in MongoDB
2. **Counter Management**: Uses atomic findAndModify with upsert for thread-safe counter increment
3. **Redirection**: Looks up short code, validates URL scheme, returns 302 redirect
4. **Auto-Expiration**: MongoDB TTL index automatically deletes URLs after 1 hour

## Error Handling

- **400 Bad Request**: Invalid URL format or validation errors
- **404 Not Found**: Short code not found or URL expired
- **302 Found**: Successful redirect to original URL

## Live Production

🚀 **Deployed Backend:** Render.
🚀 **Deployed Ui:** https://shorten-url-ui-tau.vercel.app/.
    

**Environment Variables:**
- `PORT=8080`
- `DB_URL=mongodb://localhost:27017/tinyurl`
- `base.url=http://localhost:8080`
- `base.cors=http://localhost:3000/`

## Future Improvements

- [ ] Rate limiting per IP/user
- [ ] QR code generation for short URLs
- [ ] Bulk URL shortening API
- [ ] Redis caching for frequently accessed URLs
- [ ] URL preview before redirect
- [ ] Soft delete with URL history
