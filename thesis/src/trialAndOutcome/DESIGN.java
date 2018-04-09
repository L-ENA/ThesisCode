package trialAndOutcome;

public enum DESIGN {
	PARALLEL("Parallel trial design"), CROSSOVER("Crossover trial design"), FACTORIAL("Factorial trial design"), NOTAVAILABLE("Trial design not extracted");
	
	private final String description;

	private DESIGN(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }

}
