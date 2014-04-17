import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;

public class tagger
{
	public static void main(String args[])
	{
		Scanner user_input=new Scanner(System.in);
		RandomAccessFile file;
		Date start = new Date();
		//System.out.println("Enter link url");
    		String link=args[0];
	    //System.out.println("Enter desc");
	    	String desc=args[1];
	    //System.out.println("Enter user");
	    	String user=args[2];
	    search searcher = new search();
	    ArrayList<obj> lis2=new ArrayList();
	    ArrayList<obj> lis=new ArrayList();
	    searcher.gosearch(link, desc, user, lis2);
	    
	    HashMap<String,Double>hgroups=new HashMap();
	    Set tagset=new HashSet();
	    for(int i=0;i<lis2.size();i++)
	    {
	    	//System.out.println(lis2.get(i).doc);
	    	try
	    	{
	    		file = new RandomAccessFile("groups/"+lis2.get(i).doc,"r");
	    		String line=file.readLine();
	    		String temps[]=line.split(" ");
	    		//System.out.println(line);
	    		for(int j=0;j<temps.length;j++)
	    		{
	    			if(hgroups.containsKey(temps[j]))
	    				hgroups.put(temps[j], hgroups.get(temps[j])+(i+1)/lis2.size()+(temps.length-j)/(lis2.size()+temps.length));
	    			else
	    				hgroups.put(temps[j], (double)(i+1)/lis2.size());
	    		}
	    		file.close();
	    	}catch(IOException e){}
	    }
	  
	    
			Set set = hgroups.entrySet();
			Iterator it = set.iterator();
		   while(it.hasNext()) 
		   {
		         Map.Entry me = (Map.Entry)it.next();
		         obj temp =new obj();
		         temp.doc=(String)me.getKey();
		         temp.score=hgroups.get(temp.doc);
		         lis.add(temp);
		   }
		   //System.out.println(hgroups);
	    Collections.sort(lis,new Comparator<obj>() {
	        @Override
	        public int compare(obj  o1, obj  o2)
	        {

	        	if(o1.score<o2.score)
	        		return  -1;
	        	else if (o1.score>o2.score)
	        		return 1;
	        	else
	        		return 0;
	        }
	    });
	    
	    try
    	{
	    	file=null;
	    	if(lis.size()>=1)
    		file = new RandomAccessFile("grouptags/"+lis.get(lis.size()-1).doc,"r");
	    	String line="";
	    	try{
    		line=file.readLine();}catch(NullPointerException e){}
    		String temps[]=line.split(",");
    		int i=0;
    		while(tagset.size()<=6 && i<temps.length&&temps.length>0)
    		{
    			if(!tagset.contains(temps[i]))
    				tagset.add(temps[i]);
    			i++;
    		}
    		if(lis.size()>=2)
    		file = new RandomAccessFile("grouptags/"+lis.get(lis.size()-2),"r");
    		try{
        		line=file.readLine();}catch(NullPointerException e){}
    		temps=line.split(",");
    		i=0;
    		while(tagset.size()<=8 && i<temps.length&&temps.length>0)
    		{
    			if(!tagset.contains(temps[i]))
    				tagset.add(temps[i]);
    			i++;
    		}	
    		if(lis.size()>=3)
    		file = new RandomAccessFile("grouptags/"+lis.get(lis.size()-3),"r");
    		try{
        		line=file.readLine();}catch(NullPointerException e){}
    		temps=line.split(",");
    		i=0;
    		//System.out.println("here");
    		while(tagset.size()<=9 && i<temps.length &&temps.length>0)
    		{
    			if(!tagset.contains(temps[i]))
    				tagset.add(temps[i]);
    			i++;
    		}
    		//System.out.println("here");
    		for(i=lis.size()-4;i>=0 && tagset.size()<=9;i--)
    	    {
    	    	try
    	    	{
    	    		file = new RandomAccessFile("grouptags/"+lis.get(i),"r");
    	    		line=file.readLine();
    	    		temps=line.split(",");
    	    		int j=0;
    	    		while(tagset.size()<=9 && j<temps.length)
    	    		{
    	    			if(!tagset.contains(temps[j]))
    	    				tagset.add(temps[j++]);
    	    		}
    	    	}catch(IOException e){}
    	    }
    		//System.out.println(lis2.size());
    		for(i=lis2.size()-1;i>=0 && tagset.size()<=10;i--)
    	    {
    	    	try
    	    	{
    	    		file = new RandomAccessFile("/home/dj/temps/temp/"+lis2.get(i).doc,"r");
    	    		line=file.readLine();
    	    		line=file.readLine();
    	    		//System.out.println(line);
    	    		temps=line.split(",");
    	    		int j=0;
    	    		while(tagset.size()<=5 && j<temps.length)
    	    		{
    	    			if(!tagset.contains(temps[j]))
    	    				tagset.add(temps[j]);
    	    			j++;
    	    		}
    	    	}catch(IOException e){System.out.println(e);}
    	    }
    	}catch(IOException e){}
	    System.out.println(tagset);
	}
}