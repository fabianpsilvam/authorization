## Authorization

Manage the authorization profiles

### Install dependencies

* Install docker services

`docker-compose up -d`

### Run locally

* Run on server port 8081

`./mvnw spring-boot:run`

### Profile type by user

`curl --location --request GET 'localhost:8081/profile/type/zebrands'`
