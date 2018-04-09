package trialAndOutcome;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//@XmlRootElement(name = "DichOutcome")
public class DichotomousOutcomeObject extends OutcomeObject{
	
	
	@XmlAttribute
    protected final String outcomeType ="Dichotomous";
	
	
	public String getOutcomeType() {
		return outcomeType;
	}
	public DichotomousOutcomeObject() {
		super();
	}
	public String toString() {
		return "DichOutcomeObject [comparisonName=" + comparisonName + ", outcomeName=" + outcomeName + ", group1Name="
				+ interventionGroupName + ", group1NameProse=" + interventionProse + ", group2Name=" + controlGroupName
				+ ", group2NameProse=" + controlProse + ", isGroup1Intervention=" 
				+ ", subgroupName=" + subgroupName + ", group1Events=" + interventionEvents + ", group2Events=" + controlEvents
				+ ", group1Total=" + interventionTotalN + ", group2Total=" + controlTotalN + "]";
	}
	
@XmlElement(name = "Comparison")
	public String getComparisonName() {
		return comparisonName;
	}
	//@XmlElement
	public void setComparisonName(String comparisonName) {
		this.comparisonName = comparisonName;
	}
	@XmlElement(name = "Outcome")
	public String getOutcomeName() {
		return outcomeName;
	}
	//@XmlElement
	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}
	@XmlElement(name = "Intervention")
	public String getGroup1Name() {
		return interventionGroupName;
	}

	public void setGroup1Name(String group1Name) {
		this.interventionGroupName = group1Name;
	}
	//@XmlElement
	public String getGroup1NameProse() {
		return interventionProse;
	}

	public void setGroup1NameProse(String group1NameProse) {
		this.interventionProse = group1NameProse;
	}
	@XmlElement(name = "Control")
	public String getGroup2Name() {
		return controlGroupName;
	}

	public void setGroup2Name(String group2Name) {
		this.controlGroupName = group2Name;
	}
	//@XmlElement
	public String getGroup2NameProse() {
		return controlProse;
	}

	public void setGroup2NameProse(String group2NameProse) {
		this.controlProse = group2NameProse;
	}

	
	@XmlElement(name = "Subgroup")
	public String getSubgroupName() {
		return subgroupName;
	}

	public void setSubgroupName(String subgroupName) {
		this.subgroupName = subgroupName;
	}
	@XmlElement(name = "Intervention_Events")
	public int getGroup1Events() {
		return interventionEvents;
	}

	public void setGroup1Events(int group1Events) {
		this.interventionEvents = group1Events;
	}
	@XmlElement(name = "Control_Events")
	public int getGroup2Events() {
		return controlEvents;
	}

	public void setGroup2Events(int group2Events) {
		this.controlEvents = group2Events;
	}
	@XmlElement(name = "Intervention_Total")
	public int getGroup1Total() {
		return interventionTotalN;
	}

	public void setGroup1Total(int group1Total) {
		this.interventionTotalN = group1Total;
	}
	@XmlElement(name = "Control_Total")
	public int getGroup2Total() {
		return controlTotalN;
	}

	public void setGroup2Total(int group2Total) {
		this.controlTotalN = group2Total;
	}
	protected String comparisonName = ""; 	// e.g. VITAMIN E versus PLACEBO
	protected String outcomeName = ""; 	// e.g. Tardive dyskinesia: 1. Not improved to a clinically important extent
	protected String interventionGroupName = ""; 	// e.g. Vitamin E -> Group 1. Check if intervention is always group 1!!!!!!!!!!! Also: Vitamin e combination instead of drug name -> verify with prose
	protected String interventionProse = ""; // if additional prose cleaning happens this String contains the prose to double check cleaning manually
	protected String controlGroupName = "";	//e.g. Placebo
	protected String controlProse = "";
	protected String graphLabel1 = "";
	protected String graphLabel2 = "";
	protected String subgroupName ="";	//e.g. short-term
	protected int interventionEvents;
	protected int controlEvents;
	protected int interventionTotalN;
	protected int controlTotalN;
	
	
	private Pattern invalidIC = Pattern.compile("(\\b[Ee]xperiment(al)?)|(\\b[Cc]ontrol(s)?)|(\\b[Cc]ombination(s)?|([Cc]omparison(s)?))");//checks the first string extracted for I/C 
	private Pattern addedInfoCombinationsIC = Pattern.compile("(\\b[Cc]ombination(s)?)[:-]\\s([\\w\\d+][.])?"); //if String includes "combination" in different variations, then either : or - followed by white space followed by either single letter or one or more digits followed by full stop
	private Pattern addedInfoOtherAntipsyIC = Pattern.compile("(\\b[Oo]ther\\s((a)?typic(al)?\\s)?(antipsychotic)(s)?)[:-]\\s([\\w\\d+][.])?");
	private Pattern splitPattern = Pattern.compile("(\\b[Vv]ersus)+|(\\b[Vv]s[.]?)+|(\\b[Cc]ompared\\s(with|to)+)");	//splits comparison name at word boundary plus Versus, versus, Vs., Vs, ...
	
	private Matcher m;
	private String[] splitComparison = new String[2];
	private String[] splitIC = new String[2];
	
	//What does "ORDER" attribute mean?
	//extract nothing to do with CI, weight, fixed/ random effects, 
	
	DichotomousOutcomeObject(Element dichDataElement, Element comparisonNameElement, Element dichOutcomeNameElement, Element dichOutcomeElement, Element dichSubgroupElement){
		
		NodeList groupLabel1List = dichOutcomeElement.getElementsByTagName("GROUP_LABEL_1");	//for intervention name
		Node groupLabel1Node = groupLabel1List.item(0);
		Element groupLabel1Element = (Element) groupLabel1Node;
		interventionGroupName = groupLabel1Element.getTextContent().toLowerCase();	//gets intervention name and puts all in lower case
		//group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);	//optional: capitalises first letter of intervention name
		
		NodeList groupLabel2List = dichOutcomeElement.getElementsByTagName("GROUP_LABEL_2");	//for control name
		Node groupLabel2Node = groupLabel2List.item(0);
		Element groupLabel2Element = (Element) groupLabel2Node;
		controlGroupName = groupLabel2Element.getTextContent().toLowerCase();
		//group2Name = group2Name.substring(0, 1).toUpperCase() + group2Name.substring(1);
		
		NodeList graphLabel1List = dichOutcomeElement.getElementsByTagName("GRAPH_LABEL_1");
		Element graphLabel1Element = (Element) graphLabel1List.item(0);
		graphLabel1 = graphLabel1Element.getTextContent();
		
		NodeList graphLabel2List = dichOutcomeElement.getElementsByTagName("GRAPH_LABEL_2");
		Element graphLabel2Element = (Element) graphLabel2List.item(0);
		graphLabel2 = graphLabel2Element.getTextContent();
		
		NodeList dichNameOfSubgroupList = dichSubgroupElement.getElementsByTagName("NAME");
		Element dichNameOfSubgroup = (Element) dichNameOfSubgroupList.item(0);
		subgroupName = dichNameOfSubgroup.getTextContent();
		
		
		
		interventionEvents = Integer.parseInt(dichDataElement.getAttribute("EVENTS_1"));
		controlEvents = Integer.parseInt(dichDataElement.getAttribute("EVENTS_2"));
		interventionTotalN = Integer.parseInt(dichDataElement.getAttribute("TOTAL_1"));
		controlTotalN = Integer.parseInt(dichDataElement.getAttribute("TOTAL_2"));
		comparisonName = comparisonNameElement.getTextContent();
		outcomeName = dichOutcomeNameElement.getTextContent();
		
//		System.out.println(comparisonName);
//		System.out.println(outcomeName );
//		System.out.println(subgroupName);

		m = invalidIC.matcher(interventionGroupName);	//to see if intervention has invalid name, see pattern for more info
		if (m.find()){
			interventionCleaner();	//intervention has to be filled and cleaned via comparison name
		}
		interventionGroupName = interventionGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");//easier to make a list later
//		System.out.println("Intervention: " + interventionGroupName); //+ " Total: " + group1Total + ". Events: " + group1Events );
	
		m = invalidIC.matcher(controlGroupName);
		if (m.find()){
			controlCleaner();
		}
		controlGroupName = controlGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");
//		System.out.println("Control: " +controlGroupName); //+ " Total: " + group2Total + ". Events: " + group2Events );
		controlGroupName = controlGroupName.toLowerCase();
		interventionGroupName = interventionGroupName.toLowerCase();
//		System.out.println(graphLabel1);
//		System.out.println(graphLabel2);
		
//		System.out.println(group1Events);
//		System.out.println(group2Events);
//		System.out.println(group1Total);
//		System.out.println(group2Total);
	}
	
	private void interventionCleaner(){
		
		splitComparison = splitPattern.split(comparisonName);	//splits comparison name so that its relevant part can be cleaned
		interventionGroupName = splitComparison[0];	//first String in this array describes intervention
		//group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);//optional capitalisation of first character
		interventionProse = interventionGroupName;	//in case the cleaning below produces sketchy output. This string is the prose
		
		m = addedInfoCombinationsIC.matcher(interventionGroupName); 	//if the intervention was labeled "combination" or similar . 
		if (m.find()){
			splitIC = addedInfoCombinationsIC.split(interventionGroupName);
			interventionGroupName = splitIC[1].trim();	//irrelevant info according to the pattern is removed by taking the second String in this array
			interventionGroupName = interventionGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");	//makes a standardised list with "//" separating interventions
		} else {
			m = addedInfoOtherAntipsyIC.matcher(interventionGroupName); // if some prose about "other antipsychotic" in various ways comes up it is cleaned away similar to above
			if (m.find()){
				splitIC = addedInfoOtherAntipsyIC.split(interventionGroupName);
				interventionGroupName = splitIC[1].trim();
				interventionGroupName = interventionGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto))", "//");
			}
		}
		
	}
	private void controlCleaner(){	//cleans names of control strings of pattern above matched. Very similar to interventionCleaner
		m = splitPattern.matcher(comparisonName);
		splitComparison[0] = "";
		splitComparison[1] = "";
		splitComparison = splitPattern.split(comparisonName);
		
		controlGroupName = splitComparison[1];
		controlGroupName = controlGroupName.toLowerCase();
		//group2Name= group2Name.substring(0, 1).toUpperCase() + group2Name.substring(1);
		controlProse= controlGroupName; // in case the String becomes sketchy during further cleaning this saves the prose
		
		m = addedInfoCombinationsIC.matcher(controlGroupName);
		if (m.find()){
			splitIC = addedInfoCombinationsIC.split(controlGroupName);
			controlGroupName = splitIC[1].trim();
			controlGroupName = controlGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");
		} else {
			m = addedInfoOtherAntipsyIC.matcher(controlGroupName);
			if (m.find()){
				splitIC = addedInfoOtherAntipsyIC.split(controlGroupName);
				controlGroupName = splitIC[1].trim();
				controlGroupName = controlGroupName.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto))", "//");
			}
		}
		
		
				
	}

}
