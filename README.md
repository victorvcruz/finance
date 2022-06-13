# This project is a finance control api

To run project you need Java, [Docker](https://docs.docker.com/engine/install/) and [Docker-compose](https://docs.docker.com/compose/install/),  installed in your pc

## How to run project

1. run `sudo docker-compose up -d` in root directory
3. run Main.java 
4. in http://localhost:4567/ insert your request

To stop execution run `sudo docker-compose down`

### Application of the requisition


* CreateAccount

`POST in http://localhost:4567/accounts`

```
{
	"username": "julio",
	"password": "2222"
} 
```
<br />
<br />

* CreateToken

`POST in http://localhost:4567/accounts/auth`

```
{
	"username": "julio",
	"password": "2222"
} 
```
<br />
then will return:

```
{
	"token": "eyJ0eXAiOiJKV1QiLCJ_example"
}
```
<br />
<br />

* CreateTransactions

`POST in http://localhost:4567/transactions`

Headers: "token": "eyJ0eXAiOiJKV1QiLCJ_example"

```
{
	"description": "First salary",
	"category_name": "Salary",
	"date": "2022/02/01",
	"value": 1100
}
```
<br />
<br />

* GetAccount

`GET in http://localhost:4567/accounts`

Headers: "token": "eyJ0eXAiOiJKV1QiLCJ_example"
<br />
<br />
<br />

* GetTransactions

`GET in http://localhost:4567/accounts/transactions`

Headers: "token": "eyJ0eXAiOiJKV1QiLCJ_example"

query parameters:

`http://localhost:4567/accounts/transactions?type=incomes&category=Salary&date_start=2022/02/01&date_end=2022/04/01`
<br />
<br />
<br />

* ChangeAccount

`PUT in http://localhost:4567/accounts`

Headers: "token": "eyJ0eXAiOiJKV1QiLCJ_example"

```
{
  "username": "Júlio",
  "password": "1234"
}
```

the changes in json params is optional
<br />
<br />
<br />

* ChangeTransactions

`PUT in http://localhost:4567/accounts`

Headers: "token": "eyJ0eXAiOiJKV1QiLCJ_example"

```
{
	"id":"8c07dfc5-6e62-4ab9-a6ea-4c7b19bb5ba8",
	"description": "Sunday lunch",
	"category_name": "Food",
	"date": "2022/02/07",
	"value": -139
}
```
<br />
<br />

* CancelTransactions

`DELETE in http://localhost:4567/accounts`

Headers: "token": "eyJ0eXAiOiJKV1QiLCJ_example"

```
{
	"id":"8c07dfc5-6e62-4ab9-a6ea-4c7b19bb5ba8"
}
```





