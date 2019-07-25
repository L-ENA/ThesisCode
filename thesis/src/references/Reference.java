package references;

public class Reference {
	protected String date = "";
	protected boolean primaryReference = false;
	protected String studyLevelLink="";
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isPrimaryReference() {
		return primaryReference;
	}

	public void setPrimaryReference(boolean primaryReference) {
		this.primaryReference = primaryReference;
	}
	
	public String getStudyLevelLink() {
		return studyLevelLink;
	}

	public void setStudyLevelLink(String studyLevelLink) {
		this.studyLevelLink = studyLevelLink;
	}

}
