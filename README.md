# wikiRacer

WikiRacer is a game where you choose two wikipedia pages, and try to navigate from one to the other by only clicking links inside the wiki pages.

The intent of this program is to find the least amount of links you must navigate through to get to the final wiki page.

WikiRacer.java uses the WikiScraper class inside the WikiScraper.java file to scrape the HTML of a wikipedia page and return the valid links.

Once the links of the page have been found the findWikiLadder function will determine the priority of each link.

The priority of each link is determined by the amount of similar links on the current page and on the final page. 

Once the priority of the link has been determined, they will be inserted into the Priority Queue and the queue will sort them accordingly. 

MaxPQ.java is my implementation of a priority queue binary max heap, built with an ArrayList

