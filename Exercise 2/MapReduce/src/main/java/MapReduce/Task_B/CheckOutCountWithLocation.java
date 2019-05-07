package MapReduce.Task_B;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CheckOutCountWithLocation {
    public static class CheckOutsMapper extends Mapper<> {
        @Override
        protected void map(Object key, Object value, Context context) throws IOException, InterruptedException {

        }
    }

    public static class InventoryMapper extends Mapper<> {
        @Override
        protected void map(Object key, Object value, Context context) throws IOException, InterruptedException {

        }
    }

    public static class MyReducer extends Reducer<> {
        @Override
        protected void reduce(Object key, Iterable values, Context context) throws IOException, InterruptedException {

        }
    }

}
