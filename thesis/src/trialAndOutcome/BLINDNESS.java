package trialAndOutcome;

public enum BLINDNESS {
	
	SINGLE("Single"), DOUBLE("Double"), TRIPLE("Triple"), QUADRUPLE("Quadruple"), OPENTRIAL("Open trial"), OTHER("Please see prose for more info"), NOTAVAILABLE("Information on blindness was not extracted"),
	UNCLEARSINGLE("Unclear if single"), UNCLEARDOUBLE("Unclear if double"), UNCLEARTRIPLE("Unclear if triple"), UNCLEARQUADRUPLE("Unclear if quadruple"), UNCLEAROPEN("Unclear if open")
	;
	
	private final String description;
	
	private BLINDNESS(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }

}

