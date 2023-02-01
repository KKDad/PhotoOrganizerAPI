# Photo-Server API

## Swagger UI
Swagger documentation exposed on http://localhost:8080/swagger-ui/index.html

## Generate a client

See https://openapi-generator.tech/docs/installation

Shell script using npx
~~~
#!/bin/bash
rm -rf src/api 
npx @openapitools/openapi-generator-cli generate \
   -i docs/openapi.json \
   -g typescript-fetch \
   -o src/api
~~~

or via docker
~~~
docker run --rm -v "${PWD}:/local" openapitools/openapi-generator-cli generate \
    -i docs/openapi.json \
    -g typescript-fetch \
    -o src/api
~~~