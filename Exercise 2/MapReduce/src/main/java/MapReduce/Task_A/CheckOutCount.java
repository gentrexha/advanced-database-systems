package MapReduce.Task_A;

import MapReduce.CSVSplitter;
import MapReduce.TextPair;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CheckOutCount {

    public static class MyMapper extends Mapper<LongWritable, Text, TextPair, Text> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // UsageClass,CheckoutType,MaterialType,CheckoutYear,CheckoutMonth,Checkouts,Title,Creator,Subjects,Publisher,PublicationYear
            // Split value into array
            String[] line = CSVSplitter.split(value.toString());
            // Check for header
            if (!line[0].equals("UsageClass")) {
                String checkOuts = line[5];
                // Check if checkOuts == NA || checkOuts == 0 then skip this line
                if (!checkOuts.equals("") && !checkOuts.equals("0")) {
                    context.write(new TextPair(line[7], line[6]), new Text(value));
                }
            }
        }
    }

    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {


        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        }
    }


    public static void main(String[] args) {

    }

}
