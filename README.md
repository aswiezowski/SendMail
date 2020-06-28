# Getting Started

Application for sending predefined mails

## Running on local environment

```
docker-compose up
spring_profiles_active=dev ./mvnw clean spring-boot:run
```
Sent mails can be checked on http://127.0.0.1:8025

## Running on production environment
```
spring_profiles_active=prod SMTP_HOST=host SMTP_PORT=port SMTP_USER=user SMTP_PASSWORD=pass ./mvnw clean spring-boot:run
```

## Tests

```
./mvnw test
```

## API

Creating new mail
```
curl -X POST \
  http://127.0.0.1:8080/mails \
  -H 'content-type: application/json' \
  -d '{
	"sender": "some@mail.com", 
    "recipients": [
    	"some@recipent.com"
    	],
    "content":"content",
    "subject":"subject"
}'
```

Getting mail status
```
curl -X GET \
  http://127.0.0.1:8080/mails/8ba4b237-b060-49dc-bacd-32ec57bc2ff8/status \
  -H 'content-type: application/json'
 ```
 
 Getting mail
 ```curl -X GET \
      http://127.0.0.1:8080/mails/8ba4b237-b060-49dc-bacd-32ec57bc2ff8 \
      -H 'content-type: application/json'
 ```
 

Getting all mails
```
curl -X GET \
  http://127.0.0.1:8080/mails \
  -H 'content-type: application/json'
```

Sending pending mails
```
curl -X PATCH \
  'http://127.0.0.1:8080/mails?status=pending' \
  -H 'content-type: application/json' \
  -d '{
	"status": "sent"
}'
```