## Application
Product price calculator.

Application implemented by Impact Zbigniew Bockowski for InPost recruitment process

## Building & Running

### Running from jar
```
mvn clean package
java -jar target/product-price-calculator-*.jar
```
### Running from docker image (Docker installation required)
```
mvn compile com.google.cloud.tools:jib-maven-plugin:3.3.1:dockerBuild
docker run -p 8080:8080 -t product-price-calculator:0.0.1-SNAPSHOT
```

### Port
8080

### Open Api Documentation
```
/v3/api-docs (json)
/v3/api-docs.yaml (yaml)
```

### SwaggerUI endpoint
```
/swagger-ui.html
```

### Postman collection
For your convinience there is a Postman collection created in postman folder.
It contains example setup for all exposed endpoints. You will find there as well one of the preconfigured product id required for all requests.

## How to play?
Application stores 5 example products, listing their ids and prices in application log.
Those are constant, very same entries, persisted in H2 db any time application starts.
Please follow Swagger UI spec or use attached Postman collection to work with service.

## Kubernetes hints
### Liveness probe
```
/actuator/health/liveness
```

### Readiness probe
```
/actuator/health/readiness
```

### Documentation
Service is calculating total price for given product amount and requested type of discount.
Application provides ability to store two types of discounts for predefined, example set of products.
Percentage based discount created for product will have influence on product price for any number of products. There can be only one that type of discount created for product.
Amount based discount will have influence on product price calculation only for given amount of products. You can create any number of discount of that type, but only one will be taken into consideration for given number of products.
For example having 3 discounts of that type, for 10, 20 and 30 items and asking for price for 21 items, only second one discount will be taken into account for price calculation.
There is no way to add duplicated discount of any type for given product.
Price will be calculated without discount if there is no discount created for given product. Same situation if you do not ask for discount directly.
You need to pass discount type if you would like to apply a discount to calculation. Discount type is as well helpful to figure out what discount should be used in case there are different type of discounts created for product.
