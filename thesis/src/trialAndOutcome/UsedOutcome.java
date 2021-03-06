package trialAndOutcome;

public class UsedOutcome extends OutcomeCharTable{
	protected String revManID;
	protected String reviewTitle;
	protected String value;//the String that represents the unused outcome
	protected String status = "Used";//indicated that this outcome was used in the review
	protected String studyLevelLink;
	

	public UsedOutcome() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UsedOutcome(String thisItem, String revManID, String reviewTitle, String sll) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
