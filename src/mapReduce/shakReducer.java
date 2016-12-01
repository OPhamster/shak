import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class shakReducer extends Reducer<Text, Text, Text, Text> {
    	//Reduce method for just outputting the key from mapper as the value from mapper is just an empty string
                                                                               
    	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        	int sum = 0,count=0;
		StringBuffer minVarTweet = new StringBuffer(" ");
        	/*iterates through all the values available with a key and add them together and give the final result as the key and sum of its values*/
		//for (IntWritable value : values) {
		for (Text value : values) {
            	//sum += value.get();
			String userTweetandScore = value.toString();
			String data[] = userTweetandScore.split("score =");				
	        	int score = Integer.parseInt(data[1]);
			sum = sum+score;
			count = count+1;
		}
		double mean = sum/count,variance,minVar=100.0;
		for (Text value : values) {
			String userTweetandScore = value.toString();
			String data[] = userTweetandScore.split("score =");				
	        	double score = Double.parseDouble(data[1]);
			double stddev = mean-score;
			variance = stddev*stddev;			
			if(variance<minVar) {
				minVar = variance;
				minVarTweet.replace(0,minVarTweet.length(),data[0]);			
			}		
		}
        	context.write(key, new Text(minVarTweet.toString()));
    	}
}
