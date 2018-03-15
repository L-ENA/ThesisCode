import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DichOutcomeObject extends OutcomeObject{
	
	protected String comparisonName = ""; 	// e.g. VITAMIN E versus PLACEBO
	protected String outcomeName = ""; 	// e.g. Tardive dyskinesia: 1. Not improved to a clinically important extent
	protected String group1Name = ""; 	// e.g. Vitamin E -> Group 1. Check if intervention is always group 1!!!!!!!!!!! Also: Vitamin e combination instead of drug name -> verify with prose
	protected String group2Name = "";	//e.g. Placebo
	protected String graphLabel1 = "";
	protected String graphLabel2 = "";
	protected boolean isGroup1Intervention = true; 	// if group 1 is not the intervention group this boolean will turn to false;
	protected String subgroupName ="";	//e.g. short-term
	protected int group1Events;
	protected int group2Events;
	protected int group1Total;
	protected int group2Total;
	
	private Pattern invalidIC = Pattern.compile("(\\b[Ee]xperiment(al)?)|(\\b[Cc]ontrol)|(\\b[Cc]ombination(s)?)");
	private Pattern splitPattern = Pattern.compile("(\\b[Vv]ersus)+|(\\b[Vv]s[.]?)+|(\\b[Cc]ompared\\s(with|to)+)");	//splits comparison name at word boundary plus Versus, versus, Vs., Vs, ...
	private Matcher m;
	private String[] invalidNames = {"Experimental", "experimental", "Control", "control", "Controls", "controls", "Combination", "combination", "combinations", "Combinations"};
	private String[] splitComparison = new String[2];
	//Combination, combinations, experimental, control
	//What does "ORDER" attribute mean?
	//extract nothing to do with CI, weight, fixed/ random effects, 
	
	DichOutcomeObject(Element dichDataElement, Element comparisonNameElement, Element dichOutcomeNameElement, Element dichOutcomeElement, Element dichSubgroupElement){
		
		NodeList groupLabel1List = dichOutcomeElement.getElementsByTagName("GROUP_LABEL_1");
		Node groupLabel1Node = groupLabel1List.item(0);
		Element groupLabel1Element = (Element) groupLabel1Node;
		group1Name = groupLabel1Element.getTextContent();	//gets intervention name
		group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);	//capitalises first letter of intervention name
		
		
		
		
		NodeList groupLabel2List = dichOutcomeElement.getElementsByTagName("GROUP_LABEL_2");
		Node groupLabel2Node = groupLabel2List.item(0);
		Element groupLabel2Element = (Element) groupLabel2Node;
		group2Name = groupLabel2Element.getTextContent();
		
		NodeList graphLabel1List = dichOutcomeElement.getElementsByTagName("GRAPH_LABEL_1");
		Element graphLabel1Element = (Element) graphLabel1List.item(0);
		graphLabel1 = graphLabel1Element.getTextContent();
		
		NodeList graphLabel2List = dichOutcomeElement.getElementsByTagName("GRAPH_LABEL_2");
		Element graphLabel2Element = (Element) graphLabel2List.item(0);
		graphLabel2 = graphLabel2Element.getTextContent();
		
		NodeList dichNameOfSubgroupList = dichSubgroupElement.getElementsByTagName("NAME");
		Element dichNameOfSubgroup = (Element) dichNameOfSubgroupList.item(0);
		subgroupName = dichNameOfSubgroup.getTextContent();
		
		
		
		group1Events = Integer.parseInt(dichDataElement.getAttribute("EVENTS_1"));
		group2Events = Integer.parseInt(dichDataElement.getAttribute("EVENTS_2"));
		group1Total = Integer.parseInt(dichDataElement.getAttribute("TOTAL_1"));
		group2Total = Integer.parseInt(dichDataElement.getAttribute("TOTAL_2"));
		comparisonName = comparisonNameElement.getTextContent();
		outcomeName = dichOutcomeNameElement.getTextContent();
		
		
		
		System.out.println("-----------" +comparisonName);
//		System.out.println(outcomeName );
//		System.out.println(subgroupName);
		System.out.println("Intervention: " + group1Name); //+ " Total: " + group1Total + ". Events: " + group1Events );
		m = invalidIC.matcher(group1Name);
		if (m.find()){
			interventionCleaner();
		}
		System.out.println("Intervention: " + group1Name); //+ " Total: " + group1Total + ". Events: " + group1Events );
		System.out.println("Control: " +group2Name); //+ " Total: " + group2Total + ". Events: " + group2Events );
		
		m = invalidIC.matcher(group2Name);
		if (m.find()){
			controlCleaner();
		}
		System.out.println("Control: " +group2Name); //+ " Total: " + group2Total + ". Events: " + group2Events );
		
//		System.out.println(graphLabel1);
//		System.out.println(graphLabel2);
		
//		System.out.println(group1Events);
//		System.out.println(group2Events);
//		System.out.println(group1Total);
//		System.out.println(group2Total);
	}
	
	private void interventionCleaner(){
		
		m = splitPattern.matcher(comparisonName);
		splitComparison = splitPattern.split(comparisonName);
		
		group1Name = splitComparison[0];
		group1Name = group1Name.toLowerCase();
		group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);
		
	}
	private void controlCleaner(){
		m = splitPattern.matcher(comparisonName);
		splitComparison = splitPattern.split(comparisonName);
		
		group2Name = splitComparison[1];
		group2Name = group2Name.toLowerCase();
		group2Name= group2Name.substring(0, 1).toUpperCase() + group2Name.substring(1);
	}

}
