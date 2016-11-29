#!/bin/bash

#start kafka server and consumer script at the same time
(sudo gnome-terminal --window-with-profile=shak -e "$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_CONFIG"&) && (gnome-terminal --window-with-profile=shak -e "$KAFKA_HOME/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic Hello-Kafka")

#load and run shak
#gnome-terminal -e java TwitProducer Hello-Kafka -cp $CLASSPATH:.
