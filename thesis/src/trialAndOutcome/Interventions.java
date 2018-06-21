package trialAndOutcome;

import java.util.ArrayList;
import java.util.List;

public class Interventions {
	
	public Interventions() {
		
	}
	
	
	
	protected List<Interventions> interventionList = new ArrayList<>();
	
	private ProbablyIntervention probObj;
	private UnlikelyIntervention unlikelyObj;
	private UnclearIntervention unclearObj;
	
	public Interventions(String revManID,String reviewTitle, List <String> interventionsList) {
		
		
		
		
		for(String thisItem : interventionsList) {
			if (thisItem.matches("(([1-9])|([ABCDE]\\s?))[.:]\\s?.*")) {//if String starts with a number, followed by . or :, or if it is lettered
				probObj = new ProbablyIntervention(thisItem, revManID, reviewTitle);
				interventionList.add(probObj);
			} else if (thisItem.matches("^([a-zA-Z]+\\s?){1,4}:.*")) { //If this String starts with up to 4 words that are followed by a :
				unclearObj = new UnclearIntervention(thisItem, revManID, reviewTitle);
				interventionList.add(unclearObj);
				
			} else {//most likely this String is a comment or extremely unpredictable.
				unlikelyObj = new UnlikelyIntervention(thisItem, revManID, reviewTitle);
				interventionList.add(unlikelyObj);
			}
		}
		
	}
	
	

	public List<Interventions> getInterventionList() {
		return interventionList;
	}



	public void setInterventionList(List<Interventions> interventionList) {
		this.interventionList = interventionList;
	}



	

	
	
	

}
