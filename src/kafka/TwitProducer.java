import java.util.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
//import twitter4j.TwitterFactory;
//import twitter4j.Twitter;
//import twitter4j.Trends;
//import twitter4j.Status;
//import twitter4j.Query;
//import twitter4j.QueryResult;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import java.io.*;
import java.lang.*;
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
		Query query = new Query(trendTopic.toString());
		query.count(50);
		QueryResult result;
		do {
			result = twitter.search(query);
		        List<Status> tweets = result.getTweets();
		        for (Status tweet : tweets) {
		            	//System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
				String userTweet = trendTopic.toString()+":"+tweet.getUser().getScreenName()+"-"+tweet.getText()+";";
				producer.send(new ProducerRecord<String, String>(topicName,trendTopic,userTweet));	
		        }
		} while ((query = result.nextQuery()) != null);
	}
	public static void main(String[] args) throws Exception{   
      		// Producer code	
      		if(args.length == 0){
         		System.out.println("Enter topic name");
         		return;
      		}
	      	String topicName = args[0].toString();
		Properties kafkaProps = producerConfig();
      		Producer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);
            	
		//Twitter oAuth
		TwitterFactory factory = twitterConfig();
		//Twitter : get top global trends - and send their tweets
		Twitter twitter = factory.getInstance();
		Trends globTrends = twitter.getPlaceTrends(1);
		for (int i =0; i < globTrends.getTrends().length; i++) {
			//producer.send(new ProducerRecord<String, String>(topicName,Integer.toString(i),globTrends.getTrends()[i].getName()));
			getTweets(topicName,producer,twitter,globTrends.getTrends()[i].getName());			
			//System.out.println(globTrends.getTrends()[i].getName());	
		}
		//		
		//System.out.println("Message sent successfully");
       		producer.close();
   	}
}
