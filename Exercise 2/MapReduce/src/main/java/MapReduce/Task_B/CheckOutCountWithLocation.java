package MapReduce.Task_B;

import MapReduce.CSVSplitter;
import MapReduce.Task_A.CheckOutCount;
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
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import static MapReduce.Task_A.CheckOutCount.getKeyFromValue;

public class CheckOutCountWithLocation {
    public static class CheckOutsMapper extends Mapper<LongWritable, Text, Text, Text> {
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

    public static class InventoryMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //BibNum,Title,Author,ISBN,PublicationYear,Publisher,Subjects,ItemType,ItemCollection,FloatingItem,ItemLocation,ReportDate,ItemCount
            String[] line = CSVSplitter.split(value.toString());
            // Check for header
            if (!line[0].equals("BibNum")) {
                context.write(new Text(line[2]), new Text(value));
            }
        }
    }

    public static class MyReducer extends Reducer<Text, Text, TextPair, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            HashMap<String, Integer> checkOutsMap = new HashMap<String, Integer>();
            HashMap<String, String> inventoryMap = new HashMap<String, String>();

            //Group checkOuts by books and get checkout sums
            for (Text value : values) {
                String[] line = CSVSplitter.split(value.toString());
                if (line.length == 11) {
                    String title = line[6];
                    Integer checkOut = Integer.parseInt(line[5]);
                    // if title does not exist in treemap add title, else update its value adding current checkout
                    if (!checkOutsMap.containsKey(title)) {
                        checkOutsMap.put(title, checkOut);
                    } else {
                        checkOutsMap.put(title, checkOutsMap.get(title) + checkOut);
                    }
                } else {
                    //BibNum,Title,Author,ISBN,PublicationYear,Publisher,Subjects,ItemType,ItemCollection,FloatingItem,ItemLocation,ReportDate,ItemCount
                    String title = line[1];
                    //publication Year
                    String inventoryValues = line[4] + "," +
                            //subjects
                            line[6] + "," +
                            //item Location
                            line[10];
                    inventoryMap.put(title, inventoryValues);
                }
            }

            if (checkOutsMap.size() != 0) {
                // Get highest key value (checkCount)
                int max = Collections.max(checkOutsMap.values());
                // Dirty method to get book based on key value.
                Text book = new Text(getKeyFromValue(checkOutsMap, max).toString());
                if (inventoryMap.containsKey(book.toString())) {
                    Text inventory = new Text(inventoryMap.get(book.toString()));
                    context.write(new TextPair(key, book), inventory);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.output.textoutputformat.separator",","); // output a CSV
        Job job = Job.getInstance(conf);
        job.setJarByClass(CheckOutCountWithLocation.class);
        //Setting first argument for checkout.csv and second for inventory.csv
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, CheckOutsMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, InventoryMapper.class);

        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(MyReducer.class);
        //Third argument for output Path
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        if(job.waitForCompletion(true)){
            System.exit(0);
        } else{
            System.exit(1);
        }
    }

}
