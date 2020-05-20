#!/bin/sh
docker run -p 5432:5432 -e POSTGRES_DB=recipes -e POSTGRES_PASSWORD=User123 -e POSTGRES_USER=user -v postgre_recipe_data:/var/lib/postgresql/data postgres
