package references;

public class CochraneReviewReferenceObject extends ReferenceObject{
	protected String type = "";
	protected String allAuthors = "";
	protected String title = "";
	protected String originalTitle = "";
	protected String source = "";
	protected String date = "";
	protected String volume = "";
	protected boolean primaryReference = false;
	
	public boolean isPrimaryReference() {
		return primaryReference;
	}

	public void setPrimaryReference(boolean primaryReference) {
		this.primaryReference = primaryReference;
	}

	public CochraneReviewReferenceObject(String[] referencesArray, int i){
		type = "Cochrane Review";
		if (referencesArray[i + 1].equals("YES")) {
			primaryReference = true;
		}
		allAuthors = referencesArray[i + 2];
		title = referencesArray [i + 3];
		date = referencesArray[i + 5];
		originalTitle = referencesArray[i + 9];
		source = referencesArray [i + 4];
		volume = referencesArray[i + 6];
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

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
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
	
}
