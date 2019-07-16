# Calories Management REST API


##### GET /rest/meals/{id}
*Get meal*
+ Response 200

```
{
    "id": 100002,
    "dateTime": "2015-05-30T10:00:00",
    "description": "Завтрак",
    "calories": 500,
    "user": null
}
 ```
 
##### DELETE /rest/meals/{id}
*Delete meal*
+ Response 204
 
##### GET /rest/meals
*Get all meals*
+ Response 200
```
[
    {
        "id": 100007,
        "dateTime": "2015-05-31T20:00:00",
        "description": "Ужин",
        "calories": 510,
        "excess": true
    },
    {
        "id": 100003,
        "dateTime": "2015-05-30T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": false
    }
]
```
 
##### POST /rest/meals
*Create meal*
+ Request

```
{    
    "dateTime": "2015-05-30T10:00:00",
    "description": "Завтрак",
    "calories": 500
}
```

+ Response 201
```
{
    "id": 100010,
    "dateTime": "2015-05-30T10:00:00",
    "description": "Завтрак",
    "calories": 500,
    "user": null
}
```
 
##### PUT /rest/meals/{id}
*Update meal*
+ Request

```
{
    "dateTime": "2015-05-30T10:00:00",
    "description": "Завтрак",
    "calories": 1000
}
```

+ Response 204

##### GET /rest/meals/filter?startDate={date}&startTime={time}&endDate={date}&endTime={time}
*Filter meals by date/time*

Date format - **2015-12-12**

Time format - **10:00**

+ Response 200
```
[
    {
        "id": 100007,
        "dateTime": "2015-05-31T20:00:00",
        "description": "Ужин",
        "calories": 510,
        "excess": true
    },
    {
        "id": 100003,
        "dateTime": "2015-05-30T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": false
    }
]
```