import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
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
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;
class grouper
{
		public static void similarize()
		{
			//Scanner user_input=new Scanner(System.in);
			RandomAccessFile file;
			//BufferedReader file;
			BufferedWriter fwrite;
			FileWriter fw;
			//System.out.println("Enter the path");
			//String path=user_input.nextLine();
			String path="crawler/";
			long nFiles = new File(path).listFiles().length;
			search searchit=new search();
			int hitsPerPage=750;
			String index = "index";
			String field = "contents";
			System.out.println(nFiles);
				for(int i=9280;i<=nFiles;i++)
				{
					System.out.println("File no.+"+i);
					try
					{
					
						//file = new BufferedReader(new FileReader("crawler/"+String.valueOf(i)));
						file= new RandomAccessFile("crawler/"+String.valueOf(i),"r");
					   String line=file.readLine();
					   HashMap<String,Double> hm =new HashMap();
					   ArrayList<String> doc_list=new ArrayList();
					   ArrayList<Double> score_list=new ArrayList();
					   ArrayList<obj> flist=new ArrayList();
					   TreeMap tm=new TreeMap();
					   IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
					   IndexSearcher searcher = new IndexSearcher(reader);
					   Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
					   QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
					   long lcount=0;
					   while(line!=null)
					   {
						  // System.out.println(line);
						   if(lcount>500)
							   break;
						   lcount++;
						   String arr[]=line.split("[ ,.]");
						   line = line.replaceAll("[^\\w_]", "");
						   if(!line.equalsIgnoreCase(""))
						   {
							   Query query = parser.parse(line);
							   searchit.doPagingSearch(null, searcher, query, hitsPerPage, false,false,doc_list,score_list);
							   for(int k=0;k<doc_list.size();k++)
							   {
								   if(hm.containsKey(doc_list.get(k)))
									   hm.put(doc_list.get(k),hm.get(doc_list.get(k))+score_list.get(k));
								   else
									   hm.put(doc_list.get(k),score_list.get(k));
							   }
						   }
						   line=file.readLine();
					   }
					   Set set = hm.entrySet();
						Iterator it = set.iterator();
					   while(it.hasNext()) 
					   {
					         Map.Entry me = (Map.Entry)it.next();
					         obj temp =new obj();
					         temp.doc=(String)me.getKey();
					         temp.score=hm.get(temp.doc);
					         flist.add(temp);
					   }
					
					   Collections.sort(flist,new Comparator<obj>() {
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
					   //System.out.println(doc_list);
					   try{
					   fw=new FileWriter("similars/"+String.valueOf(i));
					   for(int k=flist.size()-1;k>=0;k--)
					   {
						   if(!String.valueOf(i).equalsIgnoreCase(flist.get(k).doc))
						   fw.append(flist.get(k).doc+" ");
					   }
					   fw.close();
					   file.close();}catch(IOException e){System.out.println(e+"fw");}
					}catch(IOException e){System.out.println(e+"file");}
					catch(ParseException e){System.out.println(e);}
					finally{}
				}
			
		}
		
		
		public static void groupon()
		{
			try
			{
			long nFiles = new File("similars/").listFiles().length;
			RandomAccessFile file,ft;
			FileWriter fw;
			fw= new FileWriter("groupsall");
			long ngroups=0;
			for(int i=1;i<9280;i++)
			{
				System.out.println(i);
				try
				{
					file = new RandomAccessFile("similars/"+String.valueOf(i),"r");
					String line=file.readLine();
					String arr[]=line.split(" ");
					HashMap<String,Set> hm=new HashMap();
					int flag=0;
					long tempn=0;
					for(int j=0;j<arr.length;j++)
					{
						if(Integer.parseInt(arr[j])<i)
							continue;
						else
						{
							for(long k=1;k<=hm.size();k++)
							{
								Set<String> set = new HashSet();
								set=hm.get(String.valueOf(k));
								Iterator it = set.iterator();
								//Map.Entry me = (Map.Entry)it.next();
								int tempc=0;
								while(it.hasNext()) 
								{
									try{
									ft = new RandomAccessFile("similars/"+String.valueOf(it.next()),"r");
									String temps=ft.readLine();
									String tarr[]=temps.split(" ");
									int ttempc=tempc;
									for(int l=0;l<tarr.length;l++)
									{
										if(tarr[l].equalsIgnoreCase(arr[j]))
										{
											tempc++;
											break;
										}
									}
									ft.close();
									if(ttempc==tempc)
										break;}catch(IOException e){}
								}
								if(tempc==set.size())
								{
									set.add(arr[j]);
									flag=1;
									hm.put(String.valueOf(k), set);
								}
									
							}
							if(flag==0)
							{
								Set temset = new HashSet();
								temset.add(arr[j]);
								tempn+=1;
								hm.put(String.valueOf(tempn), temset);
							}
						}
					}
					
					 
					 for(long k=1;k<=hm.size();k++)
					 {
						 try{
						 ngroups++;
						 fw.append(String.valueOf(ngroups)+"#");
						 Set set=hm.get(String.valueOf(k));
						 Iterator it = set.iterator();
						 fw.append(String.valueOf(i)+" ");
						 while(it.hasNext())
							 fw.append(String.valueOf(it.next()+" ")); 
						 fw.append("\n");}catch(IOException e){}
						 
					 }
					 
					 file.close();
				}catch(IOException e){System.out.println(e);}
				catch(NumberFormatException e){System.out.println(e);}
				catch(NullPointerException e){System.out.println(e);}
			}
			fw.close();}catch(IOException e){}
		}
		
		
		public static void distribute_groups()
		{
			try
			{
				RandomAccessFile file = new RandomAccessFile("similars/groups","r");
				FileWriter fw;
				String line=file.readLine();
				String arr[],arr2[],gr;
				while(line!=null)
				{
					arr=line.split("#");
					gr=arr[0];
					arr2=arr[1].split(" ");
					System.out.println(arr[0]);
					System.out.println(arr[1]);
					for(int i=0;i<arr2.length;i++)
					{
						//System.out.println(arr2[i]);
						fw=new FileWriter("groups/"+arr2[i],true);
						fw.write(gr+" ");
						fw.close();
					}
					line=file.readLine();
				}
				file.close();
			}catch(IOException e){}
		}
		
		public static void finalize_groups()//find tags for each group.
		{
			try
			{
				RandomAccessFile file = new RandomAccessFile("similars/groups","r");
				FileWriter fw;
				String line=file.readLine();
				while(line!=null)
				{
					String arr[]=line.split("#");
					String gr=arr[0];
					String arr2[]=arr[1].split(" ");
					HashMap<String,Long> hm=new HashMap();
					ArrayList<obj> lis=new ArrayList();
					for(int i=0;i<arr2.length;i++)
					{
						RandomAccessFile file2 = new RandomAccessFile("crawler/"+arr2[i],"r");
						String lin=file2.readLine();
						//System.out.println(lin);
						lin=file2.readLine();
						//System.out.println(lin);
						String temps[]=lin.split("[,|]");
						for(int j=0;j<temps.length;j++)
						{
							if(hm.containsKey(temps[j]))
								hm.put(temps[j], hm.get(temps[j])+1);
							else
								hm.put(temps[j], (long)1);
						}
						file2.close();
					}
					//System.out.println(hm);
						Set set = hm.entrySet();
						Iterator it = set.iterator();
					   while(it.hasNext()) 
					   {
					         Map.Entry me = (Map.Entry)it.next();
					         obj temp =new obj();
					         temp.doc=(String)me.getKey();
					         temp.score=hm.get(temp.doc);
					         lis.add(temp);
					   }
					//System.out.println("p2");
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
					fw= new FileWriter("grouptags/"+gr);
					for(int j=lis.size()-1;j>=0;j--)
						fw.append(lis.get(j).doc+",");
					fw.append(lis.get(0).doc);
					hm.clear();
					lis.clear();
					fw.close();
					line=file.readLine();
				}
				file.close();
				
			}catch(IOException e){}
		}
		
		
		public static void main(String args[])
		{
			//similarize();
			System.out.println("Similarize done");
			groupon();
			System.out.println("grouping done");
			/*distribute_groups();
			System.out.println("distribution done");
			finalize_groups();
			System.out.println("finalized");*/
		}
}