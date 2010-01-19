import java.util.Date;


public class Deal implements Comparable {

	private Date d;
	private int numReplies;
	private int numViews;
	private String category;
	private String title;
	private String url;
	
	public Deal(String title, String url, String category, Date d, int numReplies, int numViews){
		this.title = title;
		this.url = url;
		this.category = category;
		this.d = d;
		this.numReplies = numReplies;
		this.numViews = numViews;
	}
	
	public Date getDate(){
		return d;
	}
	
	public int getNumReplies(){
		return numReplies;
	}
	
	public int getNumViews(){
		return numViews;
	}
	
	public String getCategory(){
		return category;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getURL(){
		return url;
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null) return false;
		if(getClass() != o.getClass()) return false;
		
		Deal other = (Deal)o;
		return this.title.equals(other.title);
	}
	
	public int hashCode(){
		return title.hashCode();
	}

	@Override
	public int compareTo(Object o) {
		if(this == o) return 0;
		if(o == null) return 0;
		if(getClass() != o.getClass()) return 0;
		
		Deal other = (Deal)o;
		return this.d.compareTo(other.d);
	}
}
