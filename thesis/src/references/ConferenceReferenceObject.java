package references;

public class ConferenceReferenceObject extends ReferenceObject{
	//if the information in the references section points to conference proceedings
	protected String type = "Conference proceedings";
	protected String allAuthors = "";
	protected String title = "";
	protected String conferenceName = "";
	protected String date = "";
	protected String originalTitle = "";
	protected String volume = "";
	protected String pages = "";
	protected String edition = "";
	protected String editor = "";
	protected String publisher = "";
	protected String city = "";
	
	
	
	
	public ConferenceReferenceObject(String[] referencesArray, int i){
		
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		conferenceName = referencesArray [i + 4];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		volume = referencesArray[i + 6];
		pages = referencesArray[i + 8];
		edition = referencesArray[i + 10];
		editor= referencesArray[i + 11];
		publisher= referencesArray[i + 12];
		city= referencesArray[i + 13];
		
		
	}




	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}




	public String getAllAuthors() {
		return allAuthors;
	}




	public void setAllAuthors(String allAuthors) {
		this.allAuthors = allAuthors;
	}




	public String getTitle() {
		return title;
	}




	public void setTitle(String title) {
		this.title = title;
	}




	public String getConferenceName() {
		return conferenceName;
	}




	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}




	public String getDate() {
		return date;
	}




	public void setDate(String date) {
		this.date = date;
	}




	public String getOriginalTitle() {
		return originalTitle;
	}




	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}




	public String getVolume() {
		return volume;
	}




	public void setVolume(String volume) {
		this.volume = volume;
	}




	public String getPages() {
		return pages;
	}




	public void setPages(String pages) {
		this.pages = pages;
	}




	public String getEdition() {
		return edition;
	}




	public void setEdition(String edition) {
		this.edition = edition;
	}




	public String getEditor() {
		return editor;
	}




	public void setEditor(String editor) {
		this.editor = editor;
	}




	public String getPublisher() {
		return publisher;
	}




	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}




	public String getCity() {
		return city;
	}




	public void setCity(String city) {
		this.city = city;
	}

}
