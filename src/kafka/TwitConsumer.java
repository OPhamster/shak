import java.util.Properties;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class TwitConsumer {
	private static Properties consumerConfig() {
		Properties consumerProps = new Properties();
      
      		consumerProps.put("bootstrap.servers", "localhost:9092");
      		consumerProps.put("group.id", "twitterFeed");
      		consumerProps.put("enable.auto.commit", "true");
      		consumerProps.put("auto.commit.interval.ms", "1000");
      		consumerProps.put("session.timeout.ms", "30000");
      		consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      		consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		return consumerProps;
	}
	public static void main(String[] args) throws Exception {
      		//Kafka consumer configuration settings
		
		String topicName = "Hello-Kafka";      
      		//Kafka Consumer subscribes list of topics here.
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(consumerConfig());
      		consumer.subscribe(Arrays.asList(topicName));
      
      		//print the topic name
      		System.out.println("Subscribed to topic " + topicName);
      		int i = 0;
      
      		while (true) {
         		ConsumerRecords<String, String> records = consumer.poll(100);
         		for (ConsumerRecord<String, String> record : records) {
		         	// print the offset,key and value for the consumer records.
	       			System.out.printf("key = %s, value = %s\n", record.key(), record.value());
			}
      		}
   	}
}
