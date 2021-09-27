# Application SA. Service



## Technologies

- Kotlin
- Stack Spring
- Docker, Docker-Compose
- Database Postgres

## Docker Commands

To run the project just run the following command:
````
docker-compose up
````
After the first run and if you allow a new build to be done, run the command below:

Tip: Use the detach _(-d)_ parameter to not lock the terminal
````
docker-compose up --build
docker-compose up --build -d
````
To stop all containers use _stop_ and to stop and kill all containers use _down_:
````
docker-compose stop
docker-compose down
````

## Database
This application the following database structures:

- **clusters**: Lists all existing clusters.
- **regions**: Lists all existing regions.
- **stores**: Lists all existing stores.
- **store_products**: Lists all existing store products per each season.
- **products**: Containing all the available sizes for each product, per season.

#### Query to see the total records in the database
````sql
select
(select count(*) from clusters ) as clusters,
(select count(*) from regions ) as regions,
(select count(*) from stores ) as stores,
(select count(*) from store_products ) as store_products,
(select count(*) from products ) as products;
````