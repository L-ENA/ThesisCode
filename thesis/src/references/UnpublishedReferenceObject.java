package references;

public class UnpublishedReferenceObject extends ReferenceObject{

	//if the information in the references section points to an unpublished reference
		protected String type = "Unpublished reference";
		protected String allAuthors = "";
		protected String title = "";
		protected String source = "";
		protected String date = "";
		protected String originalTitle = "";
		
		public UnpublishedReferenceObject(String[] referencesArray, int i){

			
			allAuthors = referencesArray[i + 2];
			title = referencesArray[i + 3];
			source = referencesArray [i + 4];
			date = referencesArray[i + 5];
			originalTitle = referencesArray[i + 9];
			
			
			
		
		}
}
