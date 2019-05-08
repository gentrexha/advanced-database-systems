package MapReduce;// Created as a template for Advanced Database Systems 2019

import java.util.ArrayList;

public class CSVSplitter {
	public static String[] split(String input){
	    ArrayList<String> output = new ArrayList<String>();
	    int start = 0;
	    boolean insideQuote = false;
	    
	    for (int current = 0; current < input.length();current++){
	    	char c = input.charAt(current);
	    	switch (c){
	    	case ',': 
	    		if (!insideQuote) {
	    			output.add(input.substring(start,current));
	    			start = current +1;
	    		}
	    		break;
	    	case '"':
	    		if (current == start) { 
	    			insideQuote = true;		
	    		} else if ( (current < input.length() - 1) && input.charAt(current+1) == '"'){ // escaped quote
	    			current = current + 1; // skip ahead
	    			continue;
	    		} else if ( (current < input.length() - 1) && input.charAt(current+1) == ',') {
	    			insideQuote = false;
	    		}    		   		
	    		break;
	    	}	    	
	    }	
	    output.add(input.substring(start));
	    return output.toArray( new String[output.size()]);
	}
	
	//This version does not consider quote when deciding splits
	public static String[] splitNoQuote(String input){
		  ArrayList<String> output = new ArrayList<String>();
		    int start = 0;
		    boolean insideQuote = false;
		    
		    for (int current = 0; current < input.length();current++){
		    	char c = input.charAt(current);
		    	switch (c){
		    	case ',': 
		    		if (!insideQuote) {
		    			output.add(input.substring(start,current));
		    			start = current +1;
		    		}
		    		break;
		    	
		    	}	    	
		    }	
		    output.add(input.substring(start));
		    return output.toArray( new String[output.size()]);
	}
}
