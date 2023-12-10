# Photo-Server API

## Swagger UI
Swagger documentation exposed on http://localhost:8080/swagger-ui/index.html

To list the available paths, use the following command:
~~~
cat openapi.json | jq '.paths | keys[]'
~~~
Currently, the following paths are available:
~~~
"/api/v1/"
"/api/v1/accounts"
"/api/v1/accounts/adminProfile"
"/api/v1/accounts/userProfile"
"/api/v1/accounts/{id}"
"/api/v1/auth/authenticate"
"/api/v1/folders"
"/api/v1/welcome"
~~~

## Generate a client

See https://openapi-generator.tech/docs/installation

Shell script using npx
~~~
#!/bin/bash

cd photo-ui
rm -rf src/api 
npx @openapitools/openapi-generator-cli generate \
   -i ../docs/openapi.json \
   -g typescript-fetch \
   -o src/api
~~~

or via docker
~~~
cd photo-ui
rm -rf src/api
docker run --rm -v "${PWD}:/local" openapitools/openapi-generator-cli generate \
    -i ../docs/openapi.json \
    -g typescript-fetch \
    -o src/api
~~~