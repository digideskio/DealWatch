import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;

public class DealParser {
	
	private final static String FORUM_URL = "http://www.redflagdeals.com/forums/hot-deals-f9/";
	private final static String RAW_START = "<http://www.redflagdeals.com/forums/hot-deals-f9/?prefixid=";
	private final static String RAW_END = "<http://www.redflagdeals.com/forums/newthread.php?do=newthread&amp;f=9>";
	private final static String NEWLINE_REGEX = "\\r?\\n";
	private final static String STICKY_TAG = "Sticky:";
	private final static int NUM_STICKY_LINES = 7;
	
	private final static String URL_TITLE_REGEX = "<(.+?)(new-post/)?>(.+)( \\(<http.+)?";
	private DealParser(){}
	
	public static TreeSet<Deal> parse() throws ParserException, ParseException{
		TreeSet<Deal> deals = new TreeSet<Deal>();
		LinkedList<String> rawDeals = getRawDealData();
		while(rawDeals.size() > 0)
		{
			String type = parseType(rawDeals.remove(0));
			String[] urlTitle = parseURLAndTitle(rawDeals.remove(0));
			String author = rawDeals.remove(0);
			Date d = parseDate(rawDeals.remove(0));
			rawDeals.remove(0);
			int numReplies = parseNumReplies(rawDeals.remove(0));
			int numViews = parseNumViews(rawDeals.remove(0));
			
			Deal deal = new Deal(urlTitle[1], urlTitle[0], type, d, numReplies, numViews);
			deals.add(deal);
		}
		return deals;
	}
	
	/**
	 * Parses the deal type
	 * @param line The line containing the type
	 * @return the type
	 */
	private static String parseType(String line){
		return line.substring(line.indexOf(">")+1);
	}
	
	/**
	 * Parses the URL and title
	 * @param line The line containing the URL and title
	 * @return an array containing the URL and title
	 */
	private static String[] parseURLAndTitle(String line){
		String title = "";
		String url = line.split("<")[1].substring(0, line.indexOf(">")-1);
	
		if(url.endsWith("new-post/")){
			url = url.substring(0, url.length() - 10);
			title = line.split("<.+?>")[2];
		}
		else{
			title = line.split("<.+?>")[1];
		}
		if(title.endsWith("(")){
			title = title.substring(0, title.length() - 2);
		}
		
		return new String[]{url, title};
	}
	
	private static Date parseDate(String line) throws ParseException{
		line = line.replace("th", "");
		line = line.replace("nd", "");
		line = line.replace("st", "");
		line = line.replace("rd", "");
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm aaa");
		return df.parse(line);
	}
	
	private static int parseNumReplies(String line){
		String numReplies = line.substring(line.indexOf(">")+1);
		return Integer.parseInt(numReplies.replaceAll(",", ""));
	}
	
	private static int parseNumViews(String line){
		return Integer.parseInt(line.replaceAll(",", ""));
	}
	
	private static LinkedList<String> getRawDealData() throws ParserException{
		String rawData = parseHTML();
	    LinkedList<String> lines = getDealTable(rawData);
	    return removeStickyPosts(lines);
	}

	private static String parseHTML() throws ParserException{
		Parser p = new Parser(FORUM_URL);
		StringBean sb = new StringBean ();
	    sb.setLinks(true);
	    p.visitAllNodesWith(sb);
	    return sb.getStrings();
	}
	
	private static LinkedList<String> getDealTable(String rawNodes) {
		String raw = rawNodes.substring(rawNodes.indexOf(RAW_START));
	    raw = raw.substring(0, raw.indexOf(RAW_END));
	    
	    List<String> l = Arrays.asList(raw.split(NEWLINE_REGEX));
	    return new LinkedList<String>(l);
	}

	private static LinkedList<String> removeStickyPosts(LinkedList<String> lines) {
		while(lines.get(1).contains(STICKY_TAG)){
	    	int numLinesToRemove = NUM_STICKY_LINES;
	    	while(numLinesToRemove > 0){
	    		lines.remove(0);
	    		numLinesToRemove--;
	    	}
	    }
		    
		return lines;
	}
	
	/*
	public static void main(String[] args){
		String test1 = "<http://www.redflagdeals.com/forums/logitech-harmony-remote-adapter-ps3-59-99-bb-831017/>Logitech Harmony Remote adapter for PS3 - $59.99 at BB (<http://www.redflagdeals.com/forums/logitech-harmony-remote-adapter-ps3-59-99-bb-831017/> 1<http://www.redflagdeals.com/forums/logitech-harmony-remote-adapter-ps3-59-99-bb-831017/2/> 2<http://www.redflagdeals.com/forums/logitech-harmony-remote-adapter-ps3-59-99-bb-831017/3/> 3)";
		String test2 = "<http://www.redflagdeals.com/forums/lobster-tails-4-5oz-5-99-ea-metro-saved-4-a-834565/new-post/><http://www.redflagdeals.com/forums/lobster-tails-4-5oz-5-99-ea-metro-saved-4-a-834565/>Lobster Tails 4-5oz $5.99 ea at Metro saved $4";
		String test3 = "<http://www.redflagdeals.com/forums/cheap-hdmi-cables-main-thread-818863/new-post/><http://www.redflagdeals.com/forums/cheap-hdmi-cables-main-thread-818863/>CHEAP HDMI cables - The main thread (<http://www.redflagdeals.com/forums/cheap-hdmi-cables-main-thread-818863/> 1<http://www.redflagdeals.com/forums/cheap-hdmi-cables-main-thread-818863/2/> 2<http://www.redflagdeals.com/forums/cheap-hdmi-cables-main-thread-818863/3/> 3 ...<http://www.redflagdeals.com/forums/cheap-hdmi-cables-main-thread-818863/19/> Last Page)";
		parseURLAndTitle(test1);
		parseURLAndTitle(test2);
	}
	*/
}
