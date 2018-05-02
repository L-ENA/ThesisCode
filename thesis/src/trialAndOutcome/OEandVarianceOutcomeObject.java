package trialAndOutcome;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
//@XmlRootElement(name = "OEandVarianceOutcome")
public class OEandVarianceOutcomeObject extends OutcomeObject {
	@XmlAttribute
	protected final String outcomeType ="O-E and Variance";
	
	protected String outcomeName = "";
	protected String subgroupName = "";
	
	
	@XmlElement(name = "Subgroup")
	public String getSubgroupName() {
		return subgroupName;
	}

	public void setSubgroupName(String subgroupName) {
		this.subgroupName = subgroupName;
	}



	@XmlElement(name = "Outcome")
	public String getOutcomeName() {
		return outcomeName;
	}



	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}


	
	public String getOutcomeType() {
		return outcomeType;
	}



	public OEandVarianceOutcomeObject() {
		super();
	}



	public OEandVarianceOutcomeObject(Element oeDataElement, Element comparisonNameElement, Element oeOutcomeNameElement, Element oeOutcomeElement, Element oeSubgroupElement) {
		
		outcomeName = oeOutcomeNameElement.getTextContent();
		
		if (oeSubgroupElement != null) {//because it is possible that the outcome is stored directly under the outcome node and not in a subgroup
			NodeList oeNameOfSubgroupList = oeSubgroupElement.getElementsByTagName("NAME");
			Element oeNameOfSubgroup = (Element) oeNameOfSubgroupList.item(0);//for subgroup name
			subgroupName = oeNameOfSubgroup.getTextContent();
			//System.out.println(outcomeName);
			//System.out.println(subgroupName);
		}
		
	}
}
