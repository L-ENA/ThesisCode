package references;

public class BookSectionReferenceObject extends ReferenceObject {
	
	protected String type = "Book section";
	protected String allAuthors = "";
	protected String title = "";
	protected String bookName = "";
	protected String date = "";
	protected String originalTitle = "";
	protected String volume = "";
	protected String pages = "";
	protected String edition = "";
	protected String editor = "";
	protected String publisher = "";
	protected String city = "";
	
	public BookSectionReferenceObject(String[] referencesArray, int i){
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		bookName = referencesArray [i + 4];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		volume = referencesArray[i + 6];
		pages = referencesArray[i + 8];
		edition = referencesArray[i + 10];
		editor= referencesArray[i + 11];
		publisher= referencesArray[i + 12];
		city= referencesArray[i + 13];
		
	}

}
