package trialAndOutcome;

public class UnlikelyIntervention extends Interventions{
	protected String revManID;
	protected String reviewTitle;
	protected String value;
	protected String isIntervention = "Unlikely";
	protected String studyLevelLink="";
	public UnlikelyIntervention() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnlikelyIntervention(String thisItem, String revManID, String reviewTitle, String sll) {
		this.reviewTitle = reviewTitle;
		this.revManID = revManID;
		this.value = thisItem;
		this.studyLevelLink = sll;
	}
	public String getStudyLevelLink() {
		return studyLevelLink;
	}

	public void setStudyLevelLink(String studyLevelLink) {
		this.studyLevelLink = studyLevelLink;
	}

	public String getRevManID() {
		return revManID;
	}

	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIsIntervention() {
		return isIntervention;
	}

	public void setIsIntervention(String isIntervention) {
		this.isIntervention = isIntervention;
	}

}
