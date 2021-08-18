# Cost management application

Created by Peter Fejes for ChemAxon interview.

### Available REST endpoints:

- POST localhost:8080/getCostsOfUser
  - Requires JSON request body with the type of UserCostRequestParamDTO 
  ```
    { 
        "userId" : "User1",
        "startDate" : "2018-04-23T09:20:13.354Z",
        "endDate" : "2020-05-23T09:20:13.354Z"
    }

- PUT localhost:8080/storeCost
  - Requires JSON request body with the type of UserCostDTO 
  ```
    { 
        "userId" : "User1",
        "cost" : 100.0,
        "date" : "2020-05-23T09:20:13.354Z"
    }

- POST localhost:8080/sumOfCosts
    - Requires JSON request body with the type of UserCostRequestParamDTO
  ```
    { 
        "userId" : "User1",
        "startDate" : "2018-04-23T09:20:13.354Z",
        "endDate" : "2020-05-23T09:20:13.354Z"
    }

- POST localhost:8080/avgOfCosts/{userId}
    - Requires an userId as path parameter


### How to run the application


