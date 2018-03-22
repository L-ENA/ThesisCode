
public class SoftwareReferenceObject extends ReferenceObject{
	protected String type = "Software";
	protected String allAuthors = "";
	protected String title = "";
	
	protected String date = "";
	protected String originalTitle = "";
	
	protected String edition = "";
	
	protected String publisher = "";
	protected String city = "";
	protected String medium = "";
	
	public SoftwareReferenceObject(String[] referencesArray, int i){
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		edition = referencesArray[i + 10];
		publisher= referencesArray[i + 12];
		city= referencesArray[i + 13];
		medium = referencesArray[i + 13];
		
	}

}
