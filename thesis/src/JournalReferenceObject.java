
public class JournalReferenceObject extends ReferenceObject {
	
	//0th, 9th ..index -> Type of publication, eg. Journal ->"TYPE"
	//1st..., 10th..index -> Primary attribute: Yes or No ->"PRIMARY"
	//2nd... ->Names of all authors of this publication ->"AU"
	//3rd...->Title of publication->"TI"
	//4th...->Name of Journal->Journal->"SO"
	//5...-> Year published->"YR"
	//6...->Volume ->"VL" 
	//7...-> Issue ->"NO"
	//8...-> Pages ->"PG"
	//empty fields will just stay empty
	
	protected String type = "";
	protected String allAuthors = "";
	protected String title = "";
	protected String journalName = "";
	protected String date = "";
	protected String volume = "";
	protected String issue = "";
	protected String pages = "";
	
	public JournalReferenceObject(String[] referencesArray, int i){
		type = "Journal article";
		allAuthors = referencesArray[i + 2];
		title = referencesArray[i + 3];
		journalName = referencesArray [i + 4];
		date = referencesArray[i + 5];
		volume = referencesArray[i + 6];
		issue = referencesArray[i + 7];
		pages = referencesArray[i + 8];
	}

}
