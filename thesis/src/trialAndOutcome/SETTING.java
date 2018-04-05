package trialAndOutcome;

public enum SETTING {
	
	INPATIENT("inpatients"), OUTPATIENT("outpatients"), INANDOUT("inpatients and outpatients"), EMERGENCY("emergency room setting"), COMMUNITY("community setting"), HOSPITAL("hospital setting"), NOTAVAILABLE("setting not extracted");
	
	private final String description;

	private SETTING(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }
}
