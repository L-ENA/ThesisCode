import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DichOutcomeObject extends OutcomeObject{
	
	protected String comparisonName; 	// e.g. VITAMIN E versus PLACEBO
	protected String outcomeName; 	// e.g. Tardive dyskinesia: 1. Not improved to a clinically important extent
	protected String group1Name; 	// e.g. Vitamin E -> Group 1. Check if intervention is always group 1!!!!!!!!!!!
	protected String group2Name;	//e.g. Placebo
	protected boolean isGroup1Intervention = true; 	// if group 1 is not the intervention group this boolean will turn to false;
	protected String subgroupName;	//e.g. short-term
	protected int group1Events;
	protected int group2Events;
	protected int group1Total;
	protected int group2Total;
	
	//What does "ORDER" attribute mean?
	//extract nothing to do with CI, weight, fixed/ random effects, 
	
	DichOutcomeObject(Element dichDataElement, Element comparisonNameElement, Element dichOutcomeNameElement){
		
		group1Events = Integer.parseInt(dichDataElement.getAttribute("EVENTS_1"));
		group2Events = Integer.parseInt(dichDataElement.getAttribute("EVENTS_2"));
		group1Total = Integer.parseInt(dichDataElement.getAttribute("TOTAL_1"));
		group2Total = Integer.parseInt(dichDataElement.getAttribute("TOTAL_2"));
		comparisonName = comparisonNameElement.getTextContent(); //plus grouplabels
		outcomeName = dichOutcomeNameElement.getTextContent();
		
		
		System.out.println(comparisonName);
		System.out.println(outcomeName);
		System.out.println(group1Events);
		System.out.println(group2Events);
		System.out.println(group1Total);
		System.out.println(group2Total);
	}

}
