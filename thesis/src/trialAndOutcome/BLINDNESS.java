package trialAndOutcome;

public enum BLINDNESS {
	
	SINGLE("Blinding: single"), DOUBLE("Blinding: double"), TRIPLE("Blinding: triple"), QUADRUPLE("Blinding: quadruple"), OPENTRIAL("Blinding: open trial"), NOTREPORTED("Blinding: not reported"), OTHER("Please see prose for more info"), NOTAVAILABLE("Blinding: information on blindness was not extracted"),
	BIASEDSINGLE("Blinding: single, with high risk of bias"), BIASEDDOUBLE("Blinding: double, with high risk of bias"), BIASEDTRIPLE("Blinding: triple, with high risk of bias"), BIASEDQUADRUPLE("Blinding: quadruple, with high risk of bias"), BIASEDOPEN("Blinding: open, with high risk of bias"), UNCLEAR("Blinding: unclear")
	;
	
	private final String description;
	
	public String getDescription() {
		return description;
	}

	private BLINDNESS(String description) {
		this.description = description;
	}
	
	public String getContent() {
        return description;
    }

}

