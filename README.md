# My cookery book
My cookery book is simple webapp for storing my personal recipes.
Available in English and Slovak localisation.

Avalaible as docker container:
https://hub.docker.com/r/tfilo/recipes

* default admin: admin/admin
* default user: user/user

## Run in docker standalone or docker-compose:

### standalone
* require running postgres instance
* `docker run -p 8080:8080 -e CB_DATASOURCE=jdbc:postgresql://localhost:5432/recipes -e CB_DATASOURCE_USER=username -e CB_DATASOURCE_PASS=password tfilo/recipes:latest`

### docker-compose
* `cd my-cookery-book && docker-compose up`

Language can be configured from default Slovak to English by environmental property `CB_LOCALE=en`.
Also setting custom CB_REMEMBER_ME_KEY is recomended for security reasons.
However init script creates some basic categories and units in database in Slovak language.
You will need to modify this category and unit names to English after login in to app as admin.

## Main technologies:

### required for build and run:
* [OpenJDK Temurin 17](https://adoptium.net/)
* [Apache Maven 3.8.4](https://maven.apache.org/)
* [Docker](https://www.docker.com/)
* [Postgres 13.5-alpine](https://hub.docker.com/_/postgres)
  
### Libraries, Frameworks, etc. used in this project:
* [cookieconsent](https://github.com/osano/cookieconsent)
* [css-loader](https://projects.lukehaas.me/css-loaders/)
* [dockerfile-maven-plugin](https://github.com/spotify/dockerfile-maven)
* [flying-saucer-pdf](https://github.com/flyingsaucerproject/flyingsaucer)
* [flyway-core](https://github.com/flyway/flyway)
* [Fontawesome](https://fontawesome.com/)
* [hibernate-types-52](https://github.com/vladmihalcea/hibernate-types)
* [jQuery](https://jquery.com/)
* [Log4j](https://logging.apache.org/log4j/2.x/)
* [lombok](https://projectlombok.org/)
* [mapstruct](https://mapstruct.org/)
* [postgresql-jdbc](https://jdbc.postgresql.org/)
* [SpringBoot](https://spring.io/projects/spring-boot)
* [Thymeleaf](https://www.thymeleaf.org/)
* [W3.CSS](https://www.w3schools.com/w3css/defaulT.asp)

## Build and run instructions for local development:
* `mvn clean install`
* `cd my-cookery-book && docker-compose up`

## Main features:
* User accounts with admin/editor/viewer roles
* User defined categories
* User defined tags
* User defined units
* Search by name
* Filter by category
* Filter by multiple tags
* Divide recipe into sections
* Recalculate ingredients by number of portions
* Print to PDF
* and more ...
