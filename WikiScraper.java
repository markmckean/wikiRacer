/* 
 * AUTHOR: Mark McKean
 * FILE: WikiScraper.java
 * ASSIGNMENT: Programming Assignment 10 - WikiRacer
 * COURSE: CSc 210; Spring 2022
 * PURPOSE: This is a utility class for WikiRacer and MaxPQ.
 * This class can access a wikipedia page and find all the links
 * on that page.
 */

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * TODO: You will have to implement memoization somewhere in this class.
 */
public class WikiScraper {
			
	/*
	 * This functions gets the HTML of a wikipedia page by
	 * calling fetchHTML. Then calls scrapeHTML to create a set
	 * of string only containing links from the page.
	 */
	public static Set<String> findWikiLinks(String link) {
		String html = fetchHTML(link);
		Set<String> links = scrapeHTML(html);
		return links;
	}
	
	/*
	 * This function opens a web page and takes the html of
	 * the page, and converts it into a string
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	/*
	 * This function takes a word and turns it into a valid
	 * link to a wikipedia page
	 */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	/*
	 * This function iterates through the string "html"
	 * and determines what is a link, by key strings, then
	 * strips the part around the basic name of the link
	 * and adds it to a set
	 */
	private static Set<String> scrapeHTML(String html) {
	    Set<String> set = new HashSet<>();  
	    String inputString = html;    
	    int index = inputString.indexOf("<a href=\"/wiki/");  
	    while(index != -1) {
	    	inputString = inputString.substring(index+15);  
	        String link = inputString.substring(0,inputString.indexOf('"'));  
	        if(link.indexOf('#') == -1 && link.indexOf(':') == -1)  
	            set.add(link);
	        index = inputString.indexOf("<a href=\"/wiki/");  
	    }
	    return set; 
	}
	
}
