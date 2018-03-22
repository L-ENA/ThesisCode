package references;

public class OtherReferenceObject extends ReferenceObject{
	
	//if the information in the references section points to a reference of type "other". 
			protected String type = "Other reference";
			protected String allAuthors = "";
			protected String title = "";
			protected String source = "";
			protected String date = "";
			protected String volume = "";
			protected String issue = "";
			protected String pages = "";
			protected String originalTitle = "";
			
			public OtherReferenceObject(String[] referencesArray, int i){

				
				allAuthors = referencesArray[i + 2];
				title = referencesArray[i + 3];
				source = referencesArray [i + 4];
				date = referencesArray[i + 5];
				volume = referencesArray[i + 6];
				issue = referencesArray[i + 7];
				pages = referencesArray[i + 8];
				originalTitle = referencesArray[i + 9];
				
				
			
			}

}
