
public class ConferenceReferenceObject extends ReferenceObject{
	//if the information in the references section points to conference proceedings
	protected String type = "Conference proceedings";
	protected String allAuthors = "";
	protected String title = "";
	protected String conferenceName = "";
	protected String date = "";
	protected String potentialFurtherInfo = "";
	
	
	public ConferenceReferenceObject(String[] referencesArray, int i){
		
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		conferenceName = referencesArray [i + 4];
		date = referencesArray[i + 5];
		i = i + 6;
		for (int j = 0; j < 3; j++){	//in case some extra info comes up it will be added in this String. If no info comes up the white space will be trimmed away and the String stays empty.
			potentialFurtherInfo = potentialFurtherInfo + " " + referencesArray[i + j];
			potentialFurtherInfo = potentialFurtherInfo.trim();
			
		}
		
		
	}

}
