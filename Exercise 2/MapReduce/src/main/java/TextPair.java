// Originally based on an example from "Hadoop: The Definitive Guide" by Tom White.
// https://github.com/tomwhite/hadoop-book/blob/master/ch05-io/src/main/java/oldapi/TextPair.java
// Copyright (C) 2014 Tom White
//
// Adapted by Filip Darmanovic and Cem Okulmus
// Created as a template for  Advanced Database Systems 2019

import java.io.*;
import org.apache.hadoop.io.*;

public class TextPair implements WritableComparable<TextPair> {

    private Text first;
    private Text second;

    public TextPair() {
        set(new Text(), new Text());
    }    
    
    public TextPair(TextPair copy) {
        set(new Text(copy.getFirst().toString()), 
        		new Text(copy.getSecond().toString()));
    }

    public TextPair(String first, String second) {
        set(new Text(first), new Text(second));
    }

    public TextPair(Text first, Text second) {
        set(first, second);
    }

    public void set(Text first, Text second) {
        this.first = first;
        this.second = second;
    }

    public void set(String first,String second){
        set(new Text(first),new Text(second));
    }

    public Text getFirst() {
        return first;
    }

    public Text getSecond() {
        return second;
    }
 

    @Override
    public void write(DataOutput out) throws IOException {
        first.write(out);
        second.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        first.readFields(in);
        second.readFields(in);
    }

    @Override
    public int hashCode() {
        return (1013 * first.hashCode()) ^ (1009 * second.hashCode()); // any large prime numbers should do
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TextPair) {
            TextPair tp = (TextPair) o;
            return first.equals(tp.first) && second.equals(tp.second);
        }
        return false;
    }

    @Override
    public String toString() {
        return first + "," + second;
    }

    @Override
    public int compareTo(TextPair tp) {
        int cmp = first.compareTo(tp.first);
        if (cmp != 0) {
            return cmp;
        }
        return second.compareTo(tp.second);
    }
}
