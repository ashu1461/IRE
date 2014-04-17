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
import java.io.FileWriter;
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

class normalizelinks
{
	public static void main(String []args)
	{
		try
		{
			RandomAccessFile file = new RandomAccessFile("full.csv","r");
			FileWriter f1,f2,f3;
			f1=new FileWriter("users",true);
			String line=file.readLine();
			long count=1;
			while(line!=null)
			{
				f2=new FileWriter("temp/"+String.valueOf(count),true);
				String []arr=line.split("\t");
				System.out.println(arr.length);
				if(arr.length>=2)
				{	
					f1.write(arr[1]+"\n");
					if(arr.length>=1)
						f2.write(arr[0]);
					f2.write("\n");
					if(arr.length>=3)
						f2.write(arr[2]);
					f2.write("\n");
				}
				line=file.readLine();
				f2.close();
			}
			file.close();
			f1.close();
		}catch(IOException e){}
	}
}