package references;

public class BookReferenceObject{
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

}
