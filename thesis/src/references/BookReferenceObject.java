package references;

public class BookReferenceObject extends ReferenceObject{
	protected String type = "Book";
	protected String allAuthors = "";
	protected String title = "";
	protected String date = "";
	protected String volume = "";
	protected String edition = "";
	protected String publisher = "";
	protected String city = "";
	
	public BookReferenceObject(String[] referencesArray, int i){
		allAuthors = referencesArray[i + 2];
		title = referencesArray [i + 4];
		date = referencesArray[i + 5];
		volume = referencesArray[i + 6];
		edition = referencesArray[i + 10];
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
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
