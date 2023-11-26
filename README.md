# Vicarius

Test task for Vicarius

## Getting Started

The project is located at https://github.com/BliznyukAlex/vicarius.git

## Application access

Application is deployed to AWS EC2
public API: ec2-51-20-85-15.eu-north-1.compute.amazonaws.com
port: 8080
Elasticsearch is deployed to elastic-cloud.com:
url: https://3e4034e89a05419ebd25f2de30060ac8.us-east-2.aws.elastic-cloud.com

## Running the application on local machine

to run the project on your local machine - go to project directory and run from command line

    mvn spring-boot:run

## Endpoints

    POST request to /index/{indexName} --> creates elasticsearch index and returns index
    
    POST request to /index/{indexName}/document --> create a document in the index
    
    GET request to /index/{indexName}/document/{documentId} --> return the Elasticsearch document by id from index
    
  
## Request examples
### Create index

    POST /index/index_name
    RESPONSE: HTTP 200 (OK)

### Create document

    POST /index/index_name/document
    Accept: application/json
    Content-Type: application/json
    {
        "name": "index_name_doc",
        "age": 99,
        "email": "test@example.com"
    }
    RESPONSE: HTTP 200 (OK)

### Get document

    GET /index/index_name/document/CZooDIwB8yzWm1CqYaTw
    RESPONSE: HTTP 200 (OK)