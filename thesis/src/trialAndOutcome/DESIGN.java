package trialAndOutcome;

public enum DESIGN {
	PARALLEL("Design: parallel"), CROSSOVER("Design: crossover"), FACTORIAL("Design: factorial"), OTHER("Design: see prose for trial design"), NOTAVAILABLE("Design: trial design not extracted");
	
	private final String description;

	private DESIGN(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }

}
