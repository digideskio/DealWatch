import java.util.HashMap;
import java.util.Map;

import org.htmlparser.tags.BodyTag;

import net.sf.libgrowl.Application;
import net.sf.libgrowl.GrowlConnector;
import net.sf.libgrowl.Notification;
import net.sf.libgrowl.NotificationType;


public class GrowlNotifier {
	private GrowlConnector growl;
	private Application app;
	private NotificationType[] types;
	
	public GrowlNotifier(String appName, String iconUrl, String[] types){
		growl = new GrowlConnector();
		app = new Application(appName, iconUrl);
		this.types = new NotificationType[types.length];
		for(int i = 0; i < types.length; i++){
			this.types[i] = new NotificationType(types[i]); 
		}
		
		growl.register(app, this.types);
	}
	
	public void sendNotification(String type, String title, String msg){
		NotificationType t = getType(type);
		Notification n = new Notification(app, t, title, msg);
		growl.notify(n);
	}
	
	private NotificationType getType(String typeName){
		for(NotificationType type : types){
			if(type.getDisplayName().equals(typeName)){
				return type;
			}
		}
		return null;
	}
}
