import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DichOutcomeObject extends OutcomeObject{
	
	protected String comparisonName = ""; 	// e.g. VITAMIN E versus PLACEBO
	protected String outcomeName = ""; 	// e.g. Tardive dyskinesia: 1. Not improved to a clinically important extent
	protected String group1Name = ""; 	// e.g. Vitamin E -> Group 1. Check if intervention is always group 1!!!!!!!!!!! Also: Vitamin e combination instead of drug name -> verify with prose
	protected String group1NameProse = ""; // if additional prose cleaning happens this String contains the prose to double check cleaning manually
	protected String group2Name = "";	//e.g. Placebo
	protected String group2NameProse = "";
	protected String graphLabel1 = "";
	protected String graphLabel2 = "";
	protected boolean isGroup1Intervention = true; 	// if group 1 is not the intervention group this boolean will turn to false;
	protected String subgroupName ="";	//e.g. short-term
	protected int group1Events;
	protected int group2Events;
	protected int group1Total;
	protected int group2Total;
	
	private Pattern invalidIC = Pattern.compile("(\\b[Ee]xperiment(al)?)|(\\b[Cc]ontrol(s)?)|(\\b[Cc]ombination(s)?|([Cc]omparison(s)?))");//checks the first string extracted for I/C 
	private Pattern addedInfoCombinationsIC = Pattern.compile("(\\b[Cc]ombination(s)?)[:-]\\s([\\w\\d+][.])?"); //if String includes "combination" in different variations, then either : or - followed by white space followed by either single letter or one or more digits followed by full stop
	private Pattern addedInfoOtherAntipsyIC = Pattern.compile("(\\b[Oo]ther\\s((a)?typic(al)?\\s)?(antipsychotic)(s)?)[:-]\\s([\\w\\d+][.])?");
	private Pattern splitPattern = Pattern.compile("(\\b[Vv]ersus)+|(\\b[Vv]s[.]?)+|(\\b[Cc]ompared\\s(with|to)+)");	//splits comparison name at word boundary plus Versus, versus, Vs., Vs, ...
	
	private Matcher m;
	private String[] splitComparison = new String[2];
	private String[] splitIC = new String[2];
	
	//What does "ORDER" attribute mean?
	//extract nothing to do with CI, weight, fixed/ random effects, 
	
	DichOutcomeObject(Element dichDataElement, Element comparisonNameElement, Element dichOutcomeNameElement, Element dichOutcomeElement, Element dichSubgroupElement){
		
		NodeList groupLabel1List = dichOutcomeElement.getElementsByTagName("GROUP_LABEL_1");	//for intervention name
		Node groupLabel1Node = groupLabel1List.item(0);
		Element groupLabel1Element = (Element) groupLabel1Node;
		group1Name = groupLabel1Element.getTextContent().toLowerCase();	//gets intervention name and puts all in lower case
		//group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);	//optional: capitalises first letter of intervention name
		
		NodeList groupLabel2List = dichOutcomeElement.getElementsByTagName("GROUP_LABEL_2");	//for control name
		Node groupLabel2Node = groupLabel2List.item(0);
		Element groupLabel2Element = (Element) groupLabel2Node;
		group2Name = groupLabel2Element.getTextContent().toLowerCase();
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
		
		
		
		group1Events = Integer.parseInt(dichDataElement.getAttribute("EVENTS_1"));
		group2Events = Integer.parseInt(dichDataElement.getAttribute("EVENTS_2"));
		group1Total = Integer.parseInt(dichDataElement.getAttribute("TOTAL_1"));
		group2Total = Integer.parseInt(dichDataElement.getAttribute("TOTAL_2"));
		comparisonName = comparisonNameElement.getTextContent();
		outcomeName = dichOutcomeNameElement.getTextContent();
		
		System.out.println(comparisonName);
//		System.out.println(outcomeName );
//		System.out.println(subgroupName);

		m = invalidIC.matcher(group1Name);	//to see if intervention has invalid name, see pattern for more info
		if (m.find()){
			interventionCleaner();	//intervention has to be filled and cleaned via comparison name
		}
		group1Name = group1Name.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");//easier to make a list later
		System.out.println("Intervention: " + group1Name); //+ " Total: " + group1Total + ". Events: " + group1Events );
	
		m = invalidIC.matcher(group2Name);
		if (m.find()){
			controlCleaner();
		}
		group2Name = group2Name.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");
		System.out.println("Control: " +group2Name); //+ " Total: " + group2Total + ". Events: " + group2Events );
		
//		System.out.println(graphLabel1);
//		System.out.println(graphLabel2);
		
//		System.out.println(group1Events);
//		System.out.println(group2Events);
//		System.out.println(group1Total);
//		System.out.println(group2Total);
	}
	
	private void interventionCleaner(){
		
		splitComparison = splitPattern.split(comparisonName);	//splits comparison name so that its relevant part can be cleaned
		group1Name = splitComparison[0];	//first String in this array describes intervention
		//group1Name= group1Name.substring(0, 1).toUpperCase() + group1Name.substring(1);//optional capitalisation of first character
		group1NameProse = group1Name;	//in case the cleaning below produces sketchy output. This string is the prose
		
		m = addedInfoCombinationsIC.matcher(group1Name); 	//if the intervention was labeled "combination" or similar . 
		if (m.find()){
			splitIC = addedInfoCombinationsIC.split(group1Name);
			group1Name = splitIC[1].trim();	//irrelevant info according to the pattern is removed by taking the second String in this array
			group1Name = group1Name.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");	//makes a standardised list with "//" separating interventions
		} else {
			m = addedInfoOtherAntipsyIC.matcher(group1Name); // if some prose about "other antipsychotic" in various ways comes up it is cleaned away similar to above
			if (m.find()){
				splitIC = addedInfoOtherAntipsyIC.split(group1Name);
				group1Name = splitIC[1].trim();
				group1Name = group1Name.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto))", "//");
			}
		}
		
	}
	private void controlCleaner(){	//cleans names of control strings of pattern above matched. Very similar to interventionCleaner
		m = splitPattern.matcher(comparisonName);
		splitComparison[0] = "";
		splitComparison[1] = "";
		splitComparison = splitPattern.split(comparisonName);
		
		group2Name = splitComparison[1];
		group2Name = group2Name.toLowerCase();
		//group2Name= group2Name.substring(0, 1).toUpperCase() + group2Name.substring(1);
		group2NameProse= group2Name; // in case the String becomes sketchy during further cleaning this saves the prose
		
		m = addedInfoCombinationsIC.matcher(group2Name);
		if (m.find()){
			splitIC = addedInfoCombinationsIC.split(group2Name);
			group2Name = splitIC[1].trim();
			group2Name = group2Name.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto\\s))", "//");
		} else {
			m = addedInfoOtherAntipsyIC.matcher(group2Name);
			if (m.find()){
				splitIC = addedInfoOtherAntipsyIC.split(group2Name);
				group2Name = splitIC[1].trim();
				group2Name = group2Name.replaceAll("(\\s[+]\\s)|((\\b)plus(\\b)|(\\b)and(\\b)|(\\s[Aa]dded\\sto))", "//");
			}
		}
		
		
				
	}

}
