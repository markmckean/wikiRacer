/* 
 * AUTHOR: Mark McKean
 * FILE: WikiRacer.java
 * ASSIGNMENT: Programming Assignment 10 - WikiRacer
 * COURSE: CSc 210; Spring 2022
 * PURPOSE: This file contains the main function for 
 * MaxPQ and WikiScraper. The method findWikiLadder will
 * take in a start and end word from args[0] and args[1]
 * respectfully. It then searches calls WikiScraper and MaxPQ
 * to find the wikipedia pages associated with the words.
 * Then will proceed to find a path of links between the 
 * two words.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WikiRacer {

	/*
	 * Do not edit this main function
	 */
	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	/*
	 * This functions calls upon the MaxPQ class to create and store "ladders"
	 * of links from the starting page to the final page.
	 */
	private static List<String> findWikiLadder(String start, String end) {
		Set<List<String>> setList = new HashSet<>();
		Set<String> endLinks = WikiScraper.findWikiLinks(end);
		List<String> finalList = new ArrayList<>();
		MaxPQ test = new MaxPQ(start, end);

		Set<String> startLinks = WikiScraper.findWikiLinks(start);
		if (test.size() == 1) {
			if (endLinks.contains(start) | startLinks.contains(end)) {
				finalList.add(start);
				finalList.add(end);
			} else {
				// Go through links on first page
				startLinks.parallelStream().forEach(link -> {
					Set<String> common = WikiScraper.findWikiLinks(link);
					// This finds any similar links
					common.retainAll(endLinks);
					// Amount of similar links
					int prio = common.size();
					List<String> ladder = new ArrayList<>();
					ladder.add(start);
					String strPrio = Integer.toString(prio);					
					strPrio += "^";
					ladder.add(strPrio + link);
					setList.add(ladder);
				});
			}
			for (List<String> x : setList)
				test.enqueue(x);
		}
		while (!test.isEmpty()) {
			finalList = test.search();
		}
		return finalList;
	}
}