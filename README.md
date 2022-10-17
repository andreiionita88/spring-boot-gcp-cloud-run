# Sample Spring Boot app on GCP Cloud Run

The application creates users and posts and retrieves the latest post for a given user.

## Prerequisites
APIs
 - Container Registry - our build pushes containers. 
 - Cloud Run - this is where we'll run our app
 - Datastore - for storing and reading our application's data
 - Pub / Sub - for publishing messages upon post creation

For caching you need to create a small Redis Memorystore instance, then enable the Serverless VPC Access API and create a connector.
This will allow you to connect to various services within your VPC (like Memorystore or Cloud SQL instances)

For publishing via Pub / Sub you need to create a topic named `posts`. 
If you want to consume messages you will also need to create a subscription

Change the GCP project ID in the pom file (`gcp.project.name`)

## Running

When running the application locally, if you want to enable caching, remember to set the use the `caching` profile and have a redis instance running:
```
mvn spring-boot:run -Dspring-boot.run.profiles=caching
```

Windows: https://redis.io/docs/getting-started/installation/install-redis-on-windows/
Docker: https://redis.io/docs/stack/get-started/install/docker/

## Deployment
The application can be deployed to Cloud Run by:
* using a build trigger configured in GCP Cloud Build pointing to your GitHub repo
* running the following in your local project root folder:
   ```sh
   gcloud builds submit
     ```
  
## Cleanup
Remember to delete all unused resources once you are done with them, so that you do not wake up to huge unexpected costs


### TODOs
 - Add Cloud SQL integration. 
 - Add Terraform / deployment manager config