# Please follow the below instructions before work with Kafka message streams

1. Please refer below Kafka documentation to install and setup Kafka on your machine. https://kafka.apache.org/quickstart
2. Please run the below command in the terminal to start zookeeper. `bin/zookeeper-server-start.sh config/zookeeper.properties`
2. Please run the below command in the terminal to start kafka server. `bin/kafka-server-start.sh config/server.properties`
3. Please run the below command in the terminal to work with multiple instances re-balancing feature with kafka.
   `bin/kafka-topics.sh --bootstrap-server <host>:<port> --alter --partitions <no-of-partitions-defined-in-property-file> --topic <topic-name>`
4. Please refer below Redis documentation to install and setup Redis on your machine. https://redis.io/docs/getting-started/
5. Please run the below command in the terminal to start redis. `redis-server`
   ##### Note:-
   >  Please run `redis-cli flushall` command if you need to clear the redis cache
6. Please refer the below google account credentials to log in the firebase account.
   * UserName: `cogversion55@gmail.com`
   * Password: `cog@COG1`

email-scheduling

{
"dayOfMonth": 24,
"dayOfTheWeek": 6,
"emailRequestDtoList": [
{
"mail": {
"content": { "<receiver-name>":"Saman",
"<order-no>":"90000",
"<delivery-date>":"06/30/2022",
"<delivery-location-address>":"San Francisco, CA, USA",
"<pick-up-date>":"07/02/2022",
"<total-rental-amount>":"$779.00",
"<damage-waiver-amount>":"$30.00",
"<total-paid-amount>":"$809.00",
"<mail-send-by>":"The Cloud of Goods Team",
"templateName":"order_confirmation_email_to_customer"},
"ordersList": [
{
"productName": "string",
"productQuantity": 0,
"productUrl": "string"
}
],
"receiverEmail": "kavihansi98@gmail.com",
"topBannerAdUrl": "string"
},
"mobileNo": "+94763456784",
"notification": {
"content": {}
},
"userId": 100
}
],
"hour": 10,
"minute": 30,
"month": 2,
"templateName": "order_confirmation_email_to_customer",
"year": 2023
}


kafka-massage 

{
"base64file":"string",
"filename":"file.pdf",
"mail":{
"content":{
"<receiver-name>":"Saman",
"<order-no>":"39709",
"<delivery-date>":"06/30/2022",
"<delivery-location-address>":"San Francisco, CA, USA",
"<pick-up-date>":"07/02/2022",
"<total-rental-amount>":"$779.00",
"<damage-waiver-amount>":"$30.00",
"<total-paid-amount>":"$809.00",
"<mail-send-by>":"The Cloud of Goods Team",
"templateName":"order_confirmation_email_to_customer"
},
"ordersList":[
{
"productName":"Bariatric Electric Chair",
"productQuantity":1,
"productUrl":"https://www.lifeserv.lk/wp-content/uploads/2019/10/LifeServ-Steel-Wheelchair-SMW-09.jpg?x32993"
}
],
"receiverEmail":[
"kavihansi98@gmail.com"

      ],



      "topBannerAdUrl":"https://www.plerdy.com/wp-content/uploads/2020/01/3.jpg"
},
"notification":{
"content":{
"<user-name>":"Saman",
"<vendor-name>":"Lakmal"
}
},
"templateName":"order_confirmation_email_to_customer",
"userId":100,
"mobileNo":"+94762445655"
}