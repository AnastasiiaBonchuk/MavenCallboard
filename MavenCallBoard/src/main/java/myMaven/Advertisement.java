package myMaven;

public class Advertisement {
	
	private String authorsName;
	private long publicationDate;
	private String category;
	private String header;
	private String text;
	private Integer uid;
	

	public Advertisement(String authorsName,
						 long publicationDate,
						 String section, 
						 String header, 
						 String text,
						 Integer uid) 
	{
		this.authorsName = authorsName;
		this.publicationDate = publicationDate;
		this.category = section; 
		this.header = header;
		this.text = text;
		this.uid = uid;
	}
	
	public Advertisement() {
		// TODO Auto-generated constructor stub
	}


	public String getAuthorsName() {
		return authorsName;
	}
	public void setAuthorsName(String authorsName) {
		this.authorsName = authorsName;
	}
	public long getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(long publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getSection() {
		return category;
	}
	public void setSection(String section) {
		this.category = section;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public Integer getId() {
		return uid;
	}

	public void setId(Integer id) {
		this.uid = id;
	}
	
	
	

}
