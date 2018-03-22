
public class CochraneReviewReferenceObject extends ReferenceObject{
	protected String type = "CochraneReview";
	protected String allAuthors = "";
	protected String title = "";
	protected String originalTitle = "";
	protected String source = "";
	protected String date = "";
	protected String volume = "";
	
	public CochraneReviewReferenceObject(String[] referencesArray, int i){
		allAuthors = referencesArray[i + 2];
		title = referencesArray [i + 4];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		source = referencesArray [i + 4];
		volume = referencesArray[i + 6];
	}
	
}
