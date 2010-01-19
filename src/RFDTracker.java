import java.util.Set;
import java.util.TreeSet;





public class RFDTracker {
	private static final int FIVE_MIN_IN_MILLI = 300000;
	
public static void main(String [] args){
	GrowlNotifier growl = new GrowlNotifier("DealWatch", System.getProperty("user.dir")+"/MAPLE_LEAF.gif",new String[]{"New Deal", "Hot Deal"});
	TreeSet<Deal> prevDeals = new TreeSet<Deal>();
	
	try {
		while(true){
			TreeSet<Deal> deals = DealParser.parse();
			int ct = 0;
			for(Deal d : deals){
				if(ct > 5)
					break;
				growl.sendNotification("New Deal", d.getTitle(), "");
				ct++;
			}

			Thread.sleep(FIVE_MIN_IN_MILLI);
		}
	} 
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
