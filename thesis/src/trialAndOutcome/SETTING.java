package trialAndOutcome;

public enum SETTING {
	
	INPATIENT("inpatient setting"), OUTPATIENT("outpatient setting"), INANDOUT("inpatient and outpatient setting"), EMERGENCY("emergency room setting"), COMMUNITY("community setting"), HOSPITAL("hospital setting"), NOTAVAILABLE("setting not extracted");
	
	private final String description;

	private SETTING(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }
}
