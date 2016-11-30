import java.util.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitProducer {
	private static TwitterFactory twitterConfig(){
		//Twitter configurations		
		ConfigurationBuilder twitterProps = new ConfigurationBuilder();
		twitterProps.setDebugEnabled(true);
		twitterProps.setOAuthConsumerKey("GJlV5s971WmG7l3mFNt5Rh3hH");
		twitterProps.setOAuthConsumerSecret("a4zpmhwveHMoYcE6zGddDaTGwFeUcGOmpEMm5RrYt7pAp4KnuG");
		twitterProps.setOAuthAccessToken("801879069809778689-Q0be0zvk5qDeT4wg7VrtYNPeeCbSpoe");
		twitterProps.setOAuthAccessTokenSecret("n7fWhU9QDpcAvLbZKeoyQ0dQmUG4AtB3HQ0LJgcFXnbrW");
		return new TwitterFactory(twitterProps.build());
	}
	private static Properties producerConfig(){
		//Kafka Properties
		Properties kafkaProps = new Properties();
	      	kafkaProps.put("bootstrap.servers", "localhost:9092");
     		kafkaProps.put("acks", "all");
      		kafkaProps.put("retries", 0);
      		kafkaProps.put("batch.size", 16384);
	      	kafkaProps.put("linger.ms", 1);
	   	kafkaProps.put("buffer.memory", 33554432);
           	kafkaProps.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
         	kafkaProps.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		return kafkaProps;	
	}
	private static void getTweets(String topicName,Producer<String, String> producer,Twitter twitter,String trendTopic) throws Exception {
		// Return 50 tweets for each trending Topic 		
		Query query = new Query(trendTopic.toString());
		// set number of tweets retrieved to 50		
		query.setCount(50);
		QueryResult result;
		result = twitter.search(query);
		List<Status> tweets = result.getTweets();
		for (Status tweet : tweets) {
			// This format will ease the processing down the pipeline
			// format-> #TOPIC:USER_NAME-TEXT;
			String userTweet = tweet.getUser().getScreenName()+"-"+tweet.getText();
			producer.send(new ProducerRecord<String, String>(topicName,trendTopic,userTweet));	
			// set topics as key values		
		}
	}
	public static void main(String[] args) throws Exception{   
      		// Producer code	
	      	String topicName = "Hello-Kafka";
		Properties kafkaProps = producerConfig();
      		Producer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);
            	
		//Twitter oAuth
		TwitterFactory factory = twitterConfig();
		Twitter twitter = factory.getInstance();
		//Twitter : get top global trends
		Trends globTrends = twitter.getPlaceTrends(1);
		//For each topic retrieve tweets		
		for (int i =0; i < globTrends.getTrends().length; i++) {
			getTweets(topicName,producer,twitter,globTrends.getTrends()[i].getName());			
		}
       		producer.close();
   	}
}
