package org.example.userauthenticationservice.client;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerClient {

    private KafkaTemplate<String,String> kafkaTemplate;

    public KafkaProducerClient(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void SendMessage(String topic, String message){
        kafkaTemplate.send(topic,message);
    }

}
//Command to run
//cd C:\kafka\bin\windows
//zookeeper-server-start.bat ..\..\config\zookeeper.properties
//
//cd C:\kafka\bin\windows
//kafka-server-start.bat ..\..\config\server.properties


// Starting commands for kafka and zookeeper
// Error was
//2024-10-30T22:45:44.530+05:30  INFO 20184 --- [EmailService] [  restartedMain] o.a.k.c.t.i.KafkaMetricsCollector        : initializing Kafka metrics collector
//2024-10-30T22:45:44.666+05:30  INFO 20184 --- [EmailService] [  restartedMain] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 3.7.1
//        2024-10-30T22:45:44.668+05:30  INFO 20184 --- [EmailService] [  restartedMain] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: e2494e6ffb89f828
//2024-10-30T22:45:44.669+05:30  INFO 20184 --- [EmailService] [  restartedMain] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1730308544665
//        2024-10-30T22:45:44.675+05:30  INFO 20184 --- [EmailService] [  restartedMain] o.a.k.c.c.internals.LegacyKafkaConsumer  : [Consumer clientId=consumer-emailService-1, groupId=emailService] Subscribed to topic(s): signup
//2024-10-30T22:45:44.696+05:30  INFO 20184 --- [EmailService] [  restartedMain] o.e.e.EmailServiceApplication            : Started EmailServiceApplication in 2.223 seconds (process running for 2.927)
//2024-10-30T22:45:45.134+05:30  INFO 20184 --- [EmailService] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-emailService-1, groupId=emailService] Node -1 disconnected.
//2024-10-30T22:45:45.136+05:30  WARN 20184 --- [EmailService] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-emailService-1, groupId=emailService] Connection to node -1 (localhost/127.0.0.1:9092) could not be established. Node may not be available.
//2024-10-30T22:45:45.136+05:30  WARN 20184 --- [EmailService] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-emailService-1, groupId=emailService] Bootstrap broker localhost:9092 (id: -1 rack: null) disconnected
//2024-10-30T22:45:45.228+05:30  INFO 20184 --- [EmailService] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-emailService-1, groupId=emailService] Node -1 disconnected.
//2024-10-30T22:45:45.228+05:30  WARN 20184 --- [EmailService] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-emailService-1, groupId=emailService] Connection to node -1 (localhost/127.0.0.1:9092) could not be established. Node may not be available.
//2024-10-30T22:45:45.228+05:30  WARN 20184 --- [EmailService] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-emailService-1, groupId=emailService] Bootstrap broker localhost:9092 (id: -1 rack: null) disconnected
//2024-10-30T22:45:45.368+05:30  INFO 20184 --- [EmailService] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-emailService-1, groupId=emailService] Node -1 disconnected.
//kafka issue was fixed - thanks to chatgpt
//keeping notes here so that it may help others
//
//netstat -an | grep 9092
//tcp6       0      0 :::9092                 :::*                    LISTEN
//tcp6       1      0 127.0.0.1:43638         127.0.1.1:9092          CLOSE_WAIT
//ChatGPT said:
//ChatGPT
//Your output shows:
//
//tcp6 :::9092 LISTEN: Kafka is listening on port 9092 for IPv6 connections, which should typically allow connections from localhost as well.
//tcp6 127.0.0.1:43638 127.0.1.1:9092 CLOSE_WAIT: There’s a closed connection attempt on 127.0.1.1:9092, which may indicate that your application is unable to maintain a stable connection.
//
//sudo nano ~/kafka_2.13-3.0.0/config/server.properties
//        listeners=PLAINTEXT://0.0.0.0:9092
//advertised.listeners=PLAINTEXT://<your-local-ip>:9092
//spring.kafka.bootstrap-servers=172.19.22.234:9092 - add this in  application properties in both producer and consumer
// ~/kafka_2.13-3.0.0/bin/zookeeper-server-start.sh ~/kafka_2.13-3.0.0/config/zookeeper.properties
// ~/kafka_2.13-3.0.0/bin/kafka-server-start.sh ~/kafka_2.13-3.0.0/config/server.properties