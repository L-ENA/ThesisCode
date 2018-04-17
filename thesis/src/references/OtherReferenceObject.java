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
			protected boolean primaryReference = false;
			
			public boolean isPrimaryReference() {
				return primaryReference;
			}

			public void setPrimaryReference(boolean primaryReference) {
				this.primaryReference = primaryReference;
			}

			public OtherReferenceObject(String[] referencesArray, int i){

				if (referencesArray[i + 1].equals("YES")) {
					primaryReference = true;
				}
				allAuthors = referencesArray[i + 2];
				title = referencesArray[i + 3];
				source = referencesArray [i + 4];
				date = referencesArray[i + 5];
				volume = referencesArray[i + 6];
				issue = referencesArray[i + 7];
				pages = referencesArray[i + 8];
				originalTitle = referencesArray[i + 9];
				
				
			
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

			public String getSource() {
				return source;
			}

			public void setSource(String source) {
				this.source = source;
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

			public String getIssue() {
				return issue;
			}

			public void setIssue(String issue) {
				this.issue = issue;
			}

			public String getPages() {
				return pages;
			}

			public void setPages(String pages) {
				this.pages = pages;
			}

			public String getOriginalTitle() {
				return originalTitle;
			}

			public void setOriginalTitle(String originalTitle) {
				this.originalTitle = originalTitle;
			}

}
