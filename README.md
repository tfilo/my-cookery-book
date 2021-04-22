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

### required for build:
* [AdoptOpenJDK 16 OpenJ9](https://adoptopenjdk.net/?variant=openjdk16&jvmVariant=openj9)
* [Apache Maven 3.8.1](https://maven.apache.org/)
* [Docker](https://www.docker.com/)
  
### used in project:
* [SpringBoot](https://spring.io/projects/spring-boot)
* [Thymeleaf](https://www.thymeleaf.org/)
* [jQuery](https://jquery.com/)
* [W3.CSS](https://www.w3schools.com/w3css/defaulT.asp)
* [Fontawesome](https://fontawesome.com/)
* [Postgres 13.2-alpine](https://hub.docker.com/_/postgres)

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

## Screenshots:
![Login screen](https://github.com/tfilo/my-cookery-book/blob/assets/login.png?raw=true)

![Recipes in soups category](https://github.com/tfilo/my-cookery-book/blob/assets/soups.png?raw=true)

![Categories administration](https://github.com/tfilo/my-cookery-book/blob/assets/categories.png?raw=true)

![Edit one category](https://github.com/tfilo/my-cookery-book/blob/assets/category.png?raw=true)

![Main menu](https://github.com/tfilo/my-cookery-book/blob/assets/menu.png?raw=true)

![Tags](https://github.com/tfilo/my-cookery-book/blob/assets/tags.png?raw=true)
