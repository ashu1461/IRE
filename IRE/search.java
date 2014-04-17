import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
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


class MyComp implements Comparator<obj>
{
	 
    @Override
    public int compare(obj o1, obj o2) {
    	if(o1.score<o2.score)
    		return 1;
    	else
    		return 0;
    }
     
}
public class search 
{
	
	public static void gosearch(String link, String desc, String user, ArrayList<obj>lis2)
	{
		Scanner user_input=new Scanner(System.in);
		Date start = new Date();
		RandomAccessFile file;
		  /* while(true)
		   {*/	
			ArrayList links_list= new ArrayList();
			ArrayList<Double> score_list= new ArrayList();
			HashMap<String, Double>hm= new HashMap();
			TreeMap<obj, Double>tm= new TreeMap(new MyComp());
			TreeMap<obj,Double> tm2=new TreeMap(new MyComp());
			Set<String> tag_set=new HashSet<String>();;
			String skipwords[]={"http","co","com","uk","in","www"};
			List<obj> lis=new ArrayList();
			//List<obj> lis2=new ArrayList();
			lis.clear();
			lis2.clear();
		    String index = "index";
		    String field = "contents";
		    int repeat = 0,flag=0;
		    boolean raw = false;
		    /*String link,desc,user;
		    System.out.println("Enter link url");
		    	link=user_input.nextLine();
		    System.out.println("Enter desc");
		    	desc=user_input.nextLine();
		    System.out.println("Enter user");
		    	user=user_input.nextLine();*/
		    String queryString;
		    	hm.clear();
		    String link_arr[]=link.split("[ :./#\\?=]"),desc_arr[]=desc.split(" "),user_arr[]=user.split(" "),link_arr2[]=link.split("[:./\\#]");
		    /*for(int j=0;j<link_arr.length;j++)
		    	System.out.println(link_arr[j]);*/
		   int hitsPerPage = 750;
		   for(int j=0;j<link_arr.length;j++)
		   {	 
			   //System.out.println("searching for:"+link_arr[j]);
			   queryString=link_arr[j];
			   for(int k=0;k<skipwords.length;k++)
			   {
				   if(queryString.equalsIgnoreCase(skipwords[k]))
				   {
					   flag=1;
					   break;
				   }
			   }
			   if(flag==1)
			   {
				   flag=0;
				   continue;
			   }
		    try{
				    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
				    IndexSearcher searcher = new IndexSearcher(reader);
				       // :Post-Release-Update-Version.LUCENE_XY:
				        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
				    
				        BufferedReader in = null;
				      
				        // :Post-Release-Update-Version.LUCENE_XY:
				      QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
				     
				        //  in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
				          String line = queryString != null ? queryString : in.readLine();
				          
				          if (line == null || line.length() == -1) 
				        	  break;
				    
				          line = line.trim();
				          if (line.length() == 0) 
				            break;
				          
				          Query query = parser.parse(line);
				          //System.out.println(query);
				          if (repeat > 0) 
				          {                           // repeat & time as benchmark
				           start = new Date();
				            for (int i = 0; i < repeat; i++) ;
				              searcher.search(query, null, 100);
				            
				           Date end = new Date();
				            System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
				          }
				    
				         doPagingSearch(in, searcher, query, hitsPerPage, raw,queryString == null,links_list,score_list);
				       
				         for(int i=0;i<links_list.size();i+=1)
				         {
				        	 if(hm.containsKey((String) links_list.get((int)i)))
				        	 {
				        		 double temp=hm.get((String) links_list.get((int)i));
				        		 temp=temp+score_list.get(i);
				        		 hm.put((String) links_list.get((int)i),temp);
				        	 }
				        	 else
				        		 hm.put((String) links_list.get((int)i),score_list.get(i));
				         }	
				         links_list.clear();
				         score_list.clear();
		        reader.close();}
		    catch (IOException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		    catch (ParseException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		   }
		   //System.out.println(hm);
		   for(int j=0;j<desc_arr.length;j++)
		   {	 
			  // System.out.println("searching for:"+link_arr[j]);
			   queryString=desc_arr[j];
		    try{
				    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
				    IndexSearcher searcher = new IndexSearcher(reader);
				       // :Post-Release-Update-Version.LUCENE_XY:
				        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
				    
				        BufferedReader in = null;
				      
				        // :Post-Release-Update-Version.LUCENE_XY:
				      QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
				     
				        //  in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
				          String line = queryString != null ? queryString : in.readLine();
				          
				          if (line == null || line.length() == -1) 
				        	  break;
				    
				          line = line.trim();
				          if (line.length() == 0) 
				            break;
				          
				          Query query = parser.parse(line);
				          //System.out.println(query);
				          if (repeat > 0) 
				          {                           // repeat & time as benchmark
				           start = new Date();
				            for (int i = 0; i < repeat; i++) ;
				              searcher.search(query, null, 100);
				            
				           Date end = new Date();
				            System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
				          }
				    
				         doPagingSearch(in, searcher, query, hitsPerPage, raw,queryString == null,links_list,score_list);
				         for(int i=0;i<links_list.size();i+=1)
				         {
				        	 if(hm.containsKey((String) links_list.get((int)i)))
				        	 {
				        		 double temp=hm.get((String) links_list.get((int)i));
				        		 temp+=score_list.get(i);
				        		 hm.put((String) links_list.get((int)i),temp);
				        	 }
				        	 else
				        		 hm.put((String) links_list.get((int)i),score_list.get(i));
				         }	
				         links_list.clear();
				         score_list.clear();
		        reader.close();}
		    catch (IOException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		    catch (ParseException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		   }
		   
		   for(int j=0;j<user_arr.length;j++)
		   {	 
			   //System.out.println("searching for:"+link_arr[j]);
			   queryString=user_arr[j];
		    try{
				    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
				    IndexSearcher searcher = new IndexSearcher(reader);
				       // :Post-Release-Update-Version.LUCENE_XY:
				        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
				    
				        BufferedReader in = null;
				      
				        // :Post-Release-Update-Version.LUCENE_XY:
				      QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
				     
				        //  in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
				          String line = queryString != null ? queryString : in.readLine();
				          
				          if (line == null || line.length() == -1) 
				        	  break;
				    
				          line = line.trim();
				          if (line.length() == 0) 
				            break;
				          
				          Query query = parser.parse(line);
				          //System.out.println(query);
				          if (repeat > 0) 
				          {                           // repeat & time as benchmark
				           start = new Date();
				            for (int i = 0; i < repeat; i++) ;
				              searcher.search(query, null, 100);
				            
				           Date end = new Date();
				            System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
				          }
				    
				         doPagingSearch(in, searcher, query, hitsPerPage, raw,queryString == null,links_list,score_list);
				         for(int i=0;i<links_list.size();i+=1)
				         {
				        	 if(hm.containsKey((String) links_list.get((int)i)))
				        	 {
				        		 double temp=hm.get((String) links_list.get((int)i));
				        		 temp+=score_list.get(i);
				        		 hm.put((String) links_list.get((int)i),temp);
				        	 }
				        	 else
				        		 hm.put((String) links_list.get((int)i),score_list.get(i));
				         }	
				         links_list.clear();
				         score_list.clear();
		        reader.close();}
		    catch (IOException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		    catch (ParseException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		    
		   }
		  
		   for(int j=0;j<link_arr2.length;j++)
		   {	 
			   //System.out.println("searching for:"+link_arr2[j]);
			   queryString=link_arr2[j];
		    try{
				    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
				    IndexSearcher searcher = new IndexSearcher(reader);
				       // :Post-Release-Update-Version.LUCENE_XY:
				        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
				    
				        BufferedReader in = null;
				      
				        // :Post-Release-Update-Version.LUCENE_XY:
				      QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
				     
				        //  in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
				          String line = queryString != null ? queryString : in.readLine();
				          
				          if (line == null || line.length() == -1) 
				        	  break;
				    
				          line = line.trim();
				          if (line.length() == 0) 
				            break;
				          
				          Query query = parser.parse(line);
				          //System.out.println(query);
				          if (repeat > 0) 
				          {                           // repeat & time as benchmark
				           start = new Date();
				            for (int i = 0; i < repeat; i++) ;
				              searcher.search(query, null, 100);
				            
				           Date end = new Date();
				            System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
				          }
				    
				         doPagingSearch(in, searcher, query, hitsPerPage, raw,queryString == null,links_list,score_list);
				         for(int i=0;i<links_list.size();i+=1)
				         {
				        	 if(hm.containsKey((String) links_list.get((int)i)))
				        	 {
				        		 double temp=hm.get((String) links_list.get((int)i));
				        		 temp+=score_list.get(i);
				        		 hm.put((String) links_list.get((int)i),temp);
				        	 }
				        	 else
				        		 hm.put((String) links_list.get((int)i),score_list.get(i));
				         }
				         links_list.clear();
				         score_list.clear();
		        reader.close();}
		    catch (IOException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		    catch (ParseException e) {
			      System.out.println(" caught a " + e.getClass() +
			       "\n with message: " + e.getMessage());
			    }
		   }
		   //System.out.println(hm);
		   Set set = hm.entrySet();
		   Iterator i = set.iterator();
		   obj temp_obj;
		   while(i.hasNext()) 
		   {
		         Map.Entry me = (Map.Entry)i.next();
		         
		         temp_obj=new obj();
		         temp_obj.doc=(String)me.getKey();
		         temp_obj.score=hm.get(temp_obj.doc);
		        // System.out.println(temp_obj.doc+" "+temp_obj.score);
		         tm.put(temp_obj, temp_obj.score);
		         lis.add(temp_obj);
		   }
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
		 //  System.out.println(tm.size()+" "+hm.size()+" "+lis.size());
		   //System.out.println(tm);
		   
		   while(tm.size()>100)
			   tm.remove(tm.firstKey());
		   for(int j=0;lis.size()>100;j++)
			   lis.remove(0);
		   try{
			   
				   for(int j=0;j<lis.size();j++)
				   {   
					   obj temp=lis.get(j);
					   file = new RandomAccessFile("crawler/"+temp.doc,"r");
					   String tempstr=file.readLine();
					   if(tempstr.equalsIgnoreCase(user))
						   temp.score+=1000;
					   lis2.add(temp);
					   file.close();
				   }
				   
				   Collections.sort(lis2,new Comparator<obj>() {
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
				 /*  
				   for(int j=lis2.size()-1;j>=0 && tag_set.size()<=20;j--)
				   {
					   //System.out.println("here");   
					   String temp_arr[];
					   obj temp=lis2.get(j);
					   file = new RandomAccessFile("crawler/"+temp.doc,"r");
					   String tempstr=file.readLine();
					   tempstr=file.readLine();
					   
					   temp_arr=tempstr.split("[,|]");
					   
					   for(int k=0;k<temp_arr.length;k++)
					   {
						   if(k>=3)
							   break;
						   tag_set.add(temp_arr[k]);
					   }
					   file.close();
				   }*/
		   		}catch(Exception e) {System.out.println(e);}
	        /*System.out.println(tag_set);
	        tag_set.clear();*/
		 // }
	 }
	
	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean interactive,ArrayList list,ArrayList list2) throws IOException 
	{
			// Collect enough docs to show 5 pages
			TopDocs results = searcher.search(query, 5 * hitsPerPage);
			ScoreDoc[] hits = results.scoreDocs;
			
			int numTotalHits = results.totalHits;
			//System.out.println(numTotalHits + " total matching documents");
			
			int start = 0;
			int end = Math.min(numTotalHits, hitsPerPage);
			
			while (true) 
			{
			
				if (end > hits.length) 
				{
					System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
					System.out.println("Collect more (y/n) ?");
					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0) == 'n')
						break;
					hits = searcher.search(query, numTotalHits).scoreDocs;
				}
			
				end = Math.min(hits.length, start + hitsPerPage);
			
				for (int i = start; i < end; i++) 
				{
					if (raw) 
					{                              // output raw format
						System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
						continue;
					}
			
					Document doc = searcher.doc(hits[i].doc);
					String path = doc.get("path");
					if (path != null) 
					{
							list.add(path.substring(path.indexOf('/')+1));
							list2.add((double)hits[i].score);
						String title = doc.get("title");
						
					}
					else 
						System.out.println((i+1) + ". " + "No path for this document");
				}
			
				if (!interactive || end == 0) 
					break;
			
			
				if (numTotalHits >= end) 
				{
					boolean quit = false;
					while (true) 
					{
						System.out.print("Press ");
							if (start - hitsPerPage >= 0) 
								System.out.print("(p)revious page, ");  
							
							if (start + hitsPerPage < numTotalHits) 
								System.out.print("(n)ext page, ");
			
							System.out.println("(q)uit or enter number to jump to a page.");
			
							String line = in.readLine();
							if (line.length() == 0 || line.charAt(0)=='q') 
							{
								quit = true;
								break;
							}
							if (line.charAt(0) == 'p') 
							{
								start = Math.max(0, start - hitsPerPage);
								break;
							} 
							else if (line.charAt(0) == 'n') 
							{
								if (start + hitsPerPage < numTotalHits) 
									start+=hitsPerPage;
								break;
							}
							else 
							{
								int page = Integer.parseInt(line);
								if ((page - 1) * hitsPerPage < numTotalHits) 
								{
									start = (page - 1) * hitsPerPage;
									break;
								}
								else 
									System.out.println("No such page");
							}
					}
					if (quit) break;
					end = Math.min(numTotalHits, start + hitsPerPage);
				}
			}
		}
	
	}
