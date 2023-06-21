/* 
 * AUTHOR: Mark McKean
 * FILE: MaxPQ.java
 * ASSIGNMENT: Programming Assignment 10 - WikiRacer
 * COURSE: CSc 210; Spring 2022
 * PURPOSE: This class is a Priority Queue 
 * binary Max-Heap backed by an ArrayList<String>.
 * It is used with the class WikiScraper to order links
 * of wikipedia pages by their highest priority 
 */


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaxPQ {
	private ArrayList<List<String>> PQ;
	public String start;
	public Set<String> endLinks;
	public String end;

	// The constructor, initiates queue with a null
	// pointer at index 0
	public MaxPQ(String start, String end) {
		this.end = end;
		this.start = start;
		this.endLinks = WikiScraper.findWikiLinks(end);
		this.PQ = new ArrayList<>();
		PQ.add(0, null);
	}

	// Adds a List<String> to the back of the queue
	// and calls bubbleUp to find the correct positioning
	public void enqueue(List<String> ladder) {
		PQ.add(PQ.size(), ladder);
		int index = PQ.size() - 1;
		bubbleUp(index);
	}

	// Removes and returns the highest priority element
	// and replaces it with the least priority element
	// and calls bubbleDown to find the correct position
	public List<String> dequeue() {
		List<String> deqLadder = PQ.get(1);
		List<String> newRoot = PQ.get(PQ.size() - 1);
		PQ.set(1, newRoot);
		bubbleDown(1);
		return deqLadder;
	}
	
	// Uses the key char ^ to split a string
	// and the first half is the prio and the 
	// second half is the link
	public int getPrio(List<String> ladder) {
		String linkAndPrio = ladder.get(ladder.size() - 1);
		String[] strArray = linkAndPrio.split(("\\^"));
		int prio = Integer.valueOf(strArray[0]);
		return prio;
	}

	// Checks if a List<String> has any children
	// returns 1 for 1 child, 2 for 2, and 0 for none
	public int hasChildren(int index) {
		if (index * 2 + 1 < PQ.size()) {
			return 2;
		} else if (index * 2 < PQ.size()) {
			return 1;
		}
		return 0;
	}
	
	// First this function makes sure there are possible
	// indexes to switch with. Then finds the highest prio
	// child node and swaps place until it is in the correct 
	// position
	public ArrayList<List<String>> bubbleDown(int index) {
		if (hasChildren(index) != 0) {
			while (hasChildren(index) == 2) {
				if (getPrio(PQ.get(index * 2)) >= getPrio(PQ.get(index * 2 + 1))) {
					if (getPrio(PQ.get(index * 2)) > getPrio(PQ.get(index))) {
						List<String> temp = PQ.get(index * 2);
						PQ.set(index * 2, PQ.get(index));
						PQ.set(index, temp);
						index = index * 2;
					} else
						return PQ;
				} else if (getPrio(PQ.get(index * 2)) < getPrio(PQ.get(index * 2 + 1))) {
					if (getPrio(PQ.get(index * 2 + 1)) > getPrio(PQ.get(index))) {
						List<String> temp = PQ.get(index * 2 + 1);
						PQ.set(index * 2 + 1, PQ.get(index));
						PQ.set(index, temp);
						index = index * 2 + 1;
					} else
						return PQ;
				}
			}
			while (hasChildren(index) == 1) {
				if (getPrio(PQ.get(index * 2)) > getPrio(PQ.get(index))) {
					List<String> temp = PQ.get(index * 2);
					PQ.set(index * 2, PQ.get(index));
					PQ.set(index, temp);
					index = index * 2;
				} else
					return PQ;
			}
		}
		return PQ;
	}
	
	//Checks to see if the node is at the root, if not
	// switches places with any parent node that has a lower
	// priority then it
	public ArrayList<List<String>> bubbleUp(int index) {
		List<String> curr = PQ.get(index);
		int prio = getPrio(curr);
		while (index != 1) {
			if (getPrio(PQ.get(index / 2)) < prio) {
				List<String> temp = PQ.get(index / 2);
				PQ.set(index / 2, curr);
				PQ.set(index, temp);
				index = index / 2;
			} else {
				index = 1;
			}
		}
		return PQ;
	}

	// Returns true if the queue is empty
	public boolean isEmpty() {
		if (PQ.size() == 1 | PQ.size() == 0)
			return true;
		return false;
	}
	
	// Clears the queue
	public void clear() {
		PQ.clear();
	}
	
	// Returns the size of the queue
	public int size() {
		return PQ.size();
	}

	// Searches through each link and determines its 
	// priority by how many links it shares with
	// the end page
	public List<String> search() {
		Set<List<String>> setList = new HashSet<>();
		List<String> finalList = new ArrayList<>();
		List<String> topLink = dequeue();
		String linkAndPrio = topLink.get(topLink.size() - 1);
		String[] strArray = linkAndPrio.split("\\^");
		String page = strArray[1];
		topLink.remove(topLink.size() - 1);

		Set<String> pageLinks = WikiScraper.findWikiLinks(page);
		
		if (endLinks.contains(start) || pageLinks.contains(end)) {
			for (String x : topLink) {
				finalList.add(x);
			}
			finalList.add(page);
			finalList.add(end);
			PQ.clear();
		} else {
			// Go through links on page
			pageLinks.parallelStream().forEach(link -> {
				System.out.println(link);
				Set<String> common = WikiScraper.findWikiLinks(link);
				// This finds any similar links
				common.retainAll(endLinks);
				List<String> ladder = new ArrayList<>();
				for (String x : topLink) {
					ladder.add(x);
				}

				// Amount of similar links
				int prio = common.size();
				ladder.add(page);
				String strPrio = Integer.toString(prio);
				strPrio += "^";
				ladder.add(strPrio + link);
				setList.add(ladder);				
			});
		}
		for (List<String> x : setList)
			enqueue(x);
		return finalList;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 1; i < PQ.size(); i++) {
			for (int x = 0; x < PQ.get(i).size(); x++) {
				str += PQ.get(i).get(x);
			}
			str += "\n";
		}
		return str;
	}
}
