package org.gobiiproject.gobiimodel.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.CoderMalfunctionError;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HelperIterators {

	/**
	 * Returns an iterator that filters each line from a file based on 'from' and 'to'.
	 * @param file
	 * @param from
	 * @param to
	 * @return
	 */
	public static Iterator<String> getFilteredIterator(String file, String from, String to){
		try {
			return new SingleLineFilteredIterator(file,from,to);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns an iterator that takes a mapping from string to string, and takes a file which is filtered by 'from' and 'to', and returns each line's index, sequentially.
	 * @param file
	 * @param from
	 * @param to
	 * @param index
	 * @return Iterator that returns the index output of filtered string
	 */
	public static Iterator<String> getIndexedIterator(String file,String from, String to,Map<String,String> index){
		try {
			return new SingleLineIndexedIterator(file,from,to,index);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns an iterator that always has the constant value as its output
	 * @param constant
	 * @return iterator that always returns a constant.
	 */
	public static Iterator<String> getConstantIterator(String constant){
		return new Idioterator(constant);
	}
}
//PS, this is terrible. But it works. -Josh L.S.
/**
* SingleLineFilteredIterator takes a string representing a file containing a single 'column' of data over several lines.
* It iterates over every line of the file, returning them when 'next' is called.
* Note: Reads a line ahead in the file of where it's returning, in order to correctly process hasNext(). 
* @author Josh L.S.
* For example: The file
*---------------------------------
*I
*like
*cheese
* 
* ------------------------
* Will return in successive calls to next:
* "I","like","cheese",""
* and then hasNext will be false, and the BufferedReader will close.
* Note: File handle will be open for the duration of the Iterator, so two iterators on the same file will fail, badly.
*/
class SingleLineFilteredIterator implements Iterator<String>{
	BufferedReader reader;
	String next,from,to;
	SingleLineFilteredIterator(String file, String from, String to) throws FileNotFoundException{
		reader = new BufferedReader(new FileReader(file));
		getNext();
		this.from=from;
		this.to=to;
	}
	@Override
	public boolean hasNext() {
		return next!=null;
	}
	@Override
	public String next() {
		String next=this.next;
		getNext();
		return next;
	}
	private void getNext(){
		next=null;
		try{
			next=HelperFunctions.filter(reader.readLine(),from,to,null,null);
		}catch(Exception e){//on exception, next is null, close reader to be polite
			try{
				next=null;
				reader.close();
				}catch(Exception f){/*meh*/}
			}
	}
}
/**
* Exactly like SingleLineFilteredIterator, but takes a map of string -> string, or a filename containing one line of response, then string -> string.
* 
* @author jdl232 Josh L.S.
*
*/
class SingleLineIndexedIterator extends SingleLineFilteredIterator{
	private Map<String,String> index;
	SingleLineIndexedIterator(String file, String from, String to,Map<String,String> index) throws FileNotFoundException {
		super(file, from, to);
		this.index=index;
	}
	SingleLineIndexedIterator(String file, String from, String to, String indexFile) throws FileNotFoundException{
		this(file,from,to,indexFileMap(indexFile));
	}
	//Note: Not used? Still works, 5 second code rule.
	private static Map<String,String> indexFileMap(String indexFilename) throws FileNotFoundException{
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader reader = new BufferedReader(new FileReader(indexFilename));
		try{
			reader.readLine();
			while(reader.ready()){
				String line = reader.readLine();
				if(line==null)continue;
				String[]keyvalue=line.split("\t");
				map.put(keyvalue[0], keyvalue[1]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public String next(){
		String input=super.next();
		if(index.containsKey(input)){
			return index.get(input);
		}
		else{
			return input;
		}
	}
}	

//PS, this is <i>more</i> terrible. But it works. -Josh L.S.
/**
* Idioterator returns the string it was given, forever.
* @author Josh L.S.
* @reason because I needed one
*/
class Idioterator implements Iterator<String>{
	String iLikeSwords;private static boolean yeah=true;
	Idioterator(String welcomeToCorneria) throws CoderMalfunctionError{iLikeSwords=welcomeToCorneria;}
	public boolean hasNext() {	return yeah;}
	public String next() {return iLikeSwords;}	
}