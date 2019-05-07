package MapReduce.Task_A;

import MapReduce.CSVSplitter;
import MapReduce.TextPair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;
import java.util.*;

public class CheckOutCount {
    @SuppressWarnings("Duplicates")
    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
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
                    //Pass author as key row as value
                    context.write(new Text(line[7]), new Text(value));
                }
            }
        }
    }

    public static class MyReducer extends Reducer<Text, Text, TextPair, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            HashMap<String, Integer> map = new HashMap<>();

            for (Text value : values) {
                String[] line = CSVSplitter.split(value.toString());
                String title = line[6];
                Integer checkOut = Integer.parseInt(line[5]);
                // if title does not exist in treemap add title, else update its value adding current checkout
                if(!map.containsKey(title)){
                   map.put(title, checkOut);
                } else {
                    map.put(title, map.get(title) + checkOut);
                }
            }
            // Get highest key value (checkCount)
            int max = Collections.max(map.values());
            // Dirty method to get book based on key value.
            Text book = new Text(getKeyFromValue(map, max).toString());
            context.write(new TextPair(key, book), NullWritable.get());
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.output.textoutputformat.separator", ","); // output a CSV
        Job job = Job.getInstance(conf);
        job.setJarByClass(CheckOutCount.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        if(job.waitForCompletion(true)){
            System.exit(0);
        } else{
            System.exit(1);
        }
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
