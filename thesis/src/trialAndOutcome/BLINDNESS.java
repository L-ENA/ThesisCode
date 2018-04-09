package trialAndOutcome;

public enum BLINDNESS {
	
	SINGLEBLIND("Single-blind"), DOUBLEBLIND("Double-blind"), TRIPLEBLIND("Triple-blind"), OPENTRIAL("Open trial"), NOTAVAILABLE("Information on blindness was not extracted");
	
	private final String description;
	
	private BLINDNESS(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }

}
