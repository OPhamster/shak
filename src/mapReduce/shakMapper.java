import java.io.IOException;
//import java.util.StringTokenizer;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class shakMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	//hadoop supported data types
    	//private final static IntWritable one = new IntWritable(1);
    	private Text userTweetandScore = new Text();
	private Text topic = new Text();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	//taking one line at a time and tokenizing the same
    		String line = value.toString();
		String kafkaOutput[] = line.split("->"); 
	       	//StringTokenizer tokenizer = new StringTokenizer(line);
		
        	//iterating through all the words available in that line and forming the key value pair
        	/*while (tokenizer.hasMoreTokens()) {
        	    word.set(tokenizer.nextToken());

            	//sending to output collector which inturn passes the same to reducer
            	context.write(word, one);
        	}*/
		//while (tokenizer.hasMoreTokens()) {
		//	word.set(tokenizer.nextToken());
		userTweetandScore.set(kafkaOutput[1]);
		topic.set(kafkaOutput[0]);
	    	//sending to output collector which inturn passes the same to reducer
		context.write(topic, userTweetandScore);
		//}
    
	}
}
