# Camunda Rest API

The main purpuse of that application is to give an example that how the Camunda Rest API works. More about loot up at (https://docs.camunda.org/manual/7.14/reference/rest/) [https://docs.camunda.org/manual/7.14/reference/rest/]

## About

1. First you have to deploy your process.bpmn file. To do that i give an example below. For more about take look up at (https://docs.camunda.org/manual/7.14/reference/rest/deployment/post-deployment/) [https://docs.camunda.org/manual/7.14/reference/rest/deployment/post-deployment/]
  
   ![deployment](https://github.com/eyrafabdullayev/camunda-rest-api/blob/master/screenshots/deploy2.png)

   But as you know that we also have used JWT Token to access the resources, for that i have to create and send it with request header.
   
   ![authenticate](https://github.com/eyrafabdullayev/camunda-rest-api/blob/master/screenshots/authentication.png)
   
   then to do that select Bearer Token as Type and add the given token.
   
   ![access token](https://github.com/eyrafabdullayev/camunda-rest-api/blob/master/screenshots/deploy.png)

2. After that you have to send a request to start that process by id. I have already given an example below. For more about take look up at (https://docs.camunda.org/manual/7.14/reference/rest/process-definition/post-start-process-instance/) [https://docs.camunda.org/manual/7.14/reference/rest/process-definition/post-start-process-instance/]

   ![starting the process](https://github.com/eyrafabdullayev/camunda-rest-api/blob/master/screenshots/start.png)

   And also have to send access token.
   
   ![access token](https://github.com/eyrafabdullayev/camunda-rest-api/blob/master/screenshots/start2.png)

  Then Service tast will have been completed and a request will been sended to refresh the current token. If the auth_token variable is empty or not found the exception will be raise. Else the process will be complete successfully.
   
4. And i have also used Spring Cloud Vault to secure and store some of the properties or secrets. For that you have to start vault server and add that properties as i have already shown below.

  ![vault setting](https://github.com/eyrafabdullayev/camunda-rest-api/blob/master/screenshots/vault.png)

## Running

$ docker run -it -p 8080:8080 eyrafabdullayev/camunda-rest-api:latest
