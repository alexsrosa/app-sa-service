# Application SA. Service

## Application context
This application aims to load store and product information and as such provide a Restful API to filter 
information and store name manipulation.

### System architectural approach

This application follows a layered architecture that resembles the clean architecture.
A more detailed explanation of the layers follows:

- **Model**: Layer that has mapping of database objects and their relationships.
- **Repository**: In this layer, [Spring Data](https://spring.io/projects/spring-data) 
is used to represent operations performed on the database, such as saving and searching the saved data.
- **Service**: This layer represents the business part linked to database objects 
and contains fundamental rules of use.
- **UseCase**: This layer represents the features of the system and can have links with one or more services.
- **Controller** This layer holds the system entry points for data exposure.

Other features of the system:

- **Scheduler**: For the data load, the [Spring SchedulingTasks](https://spring.io/guides/gs/scheduling-tasks/) was used, which uses cron to schedule data reading in the configured time interval.
  

### Explanation of data loading

- Data loading happens periodically, fetching information from four external endpoints. 
- Once loaded they can be updated in a new future check. 
- The routine is always run at application startup and cron is scheduled to run every hour.
- To optimize the processing in the reading of records from the external API, 
the multiple treads approach was used, thus achieving a significant improvement in the time of 
the first load and also in the updates.

### Technologies used in this project

- [Kotlin](https://kotlinlang.org/)
- [Gradle Build Tool](https://docs.gradle.org)
- [Stack Spring](https://spring.io/)
- [Docker, Docker-Compose](https://www.docker.com/)
- [Database Postgres](https://www.postgresql.org/)

## Database details

This application the following database structures:

- **clusters**: Lists all existing clusters.
  - id, name, created_at, update_at
- **regions**: Lists all existing regions.
  - id, name, type, clusters_id, created_at, updated_at
- **stores**: Lists all existing stores.
  - id, name, name_alias, theme, region_id, created_at, updated_at
- **store_products**: Lists all existing store products per each season.
  - id, product, season, store_id, hash_id, created_at, updated_at
- **products**: Containing all the available sizes for each product, per season.
  - id, description, ean, model, season, size, sku, created_at, updated_at

#### Query to see the total records in the database
````sql
select
(select count(*) from clusters ) as clusters,
(select count(*) from regions ) as regions,
(select count(*) from stores ) as stores,
(select count(*) from store_products ) as store_products,
(select count(*) from products ) as products;
````

## API - Exposed resources 

The endpoints detailed below are also documented in the swagger via the link:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### `GET /filters` 

Lists all the available fields for which users can use to filter for Stores

**Response example:**
````json
[
  "season",
  "cluster",
  "region",
  "regionType",
  "productModel",
  "productSize",
  "sku",
  "storeName",
  "storeTheme"
]
````

### `POST /filters/{filter_id}?page=X` 

Lists all possible remaining values that a user can select of a given filter, after using other filters.

**Request example:**
`http://localhost:8080/filters/REGION?page=0`
````json
{
  "filters": [
    {
      "id": "Cluster",
      "values": [ "Europe", "Asia"]
    },
    {
      "id": "Region_Type",
      "values": [ "XY"]
    },
    {
      "id": "Season",
      "values": ["S17"]
    }
  ]
}
````
**Response example:**
````json
[
  "North EU",
  "South EU"
]
````

### `POST /stores?page=X`

Filters all stores matching the given filters

**Request example:**
`http://localhost:8080/stores?page=0`
````json
{
  "filters": [
    {
      "id": "Season",
      "values": ["S20"]
    },
    {
      "id": "Region",
      "values": ["North EU"]
    }
  ]
}
````
**Response example:**
````json
[
  {
    "name": "A Sunday store",
    "theme": "What an a shaky Store!",
    "region": "North EU"
  },
  {
    "name": "A color store",
    "theme": "What an a nightly Store!",
    "region": "North EU"
  }
]
````

### `PATCH /stores/{store_name}`

Updates the name of a given Store:

**Request example:**
`http://localhost:8080/stores/store1
````json
{
  "name": "store1 - changed"
}
````
This request returns 200 Ok.

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

**Note:** To improve the speed of the image build, a cached image was made with a pre-build of the project dependencies, 
this significantly decreased the build time. The cache image can be found in _/config/docker/DockerGradleCache_

## Future improvements

- Refactoring code for working with gradle modules for better encapsulation and to ensure correct usage of dependencies;
- Creation of control tables for better management of the load schedule;
- Separation of load schedules in order to guarantee processing and find problems more easily.
- Use better threads resources of Kotlin language to gain performance in the load schedule.
- Use some database migration tool like Liquibase. 


Enjoy ;)