package trialAndOutcome;

public enum BLINDNESS {
	
	SINGLEBLIND("Single"), DOUBLEBLIND("Double"), TRIPLEBLIND("Triple"),QUADRUPLEBLIND("Quadruple"), OPENTRIAL("Open trial"), OTHER("Please see prose for more info"), NOTAVAILABLE("Information on blindness was not extracted");
	
	private final String description;
	
	private BLINDNESS(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }

}
