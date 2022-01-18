# Exchange Rates API

The application provide functionalities to users such as getting rate between currencies and amount conversion between currencies.<br>

### API Documentation
You can reach to the API documentation (swagger-ui) by visiting localhost:8080/swagger-ui.html URL.<br>

### Running the app
The application can be run on docker by running build-and-run-docker.sh after cloning the repository on your local.<br>
The application will be run on 8080.<br>

If you have docker already installed you can run the application by running the following command below from your terminal<br>
docker run --name=exchange-rates -d -p 8080:8080 --restart=unless-stopped asetad/exchange-rates:1.0

If you don't have docker you can run the application by running the following below from your terminal<br>
mvn spring-boot:run