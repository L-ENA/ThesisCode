
public class CorrespondenceReferenceObject extends ReferenceObject{
	protected String type = "Correspondence";
	protected String allAuthors = "";
	protected String title = "";
	protected String originalTitle = "";
	protected String source = "";
	protected String date = "";
	
	public CorrespondenceReferenceObject(String[] referencesArray, int i){
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		originalTitle = referencesArray[i + 9];
		source = referencesArray [i + 4];
		date = referencesArray[i + 5];
	}

}
