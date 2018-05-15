package trialAndOutcome;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GenericInverseOutcomeObject extends OutcomeObject{
	@XmlAttribute
	protected final String outcomeType ="Generic Inverse Variance";
	
	protected String outcomeName = "";
	protected String reviewTitle = "";
	
protected String subgroupName = "";
	
	public String getReviewTitle() {
	return reviewTitle;
}

public void setReviewTitle(String reviewTitle) {
	this.reviewTitle = reviewTitle;
}
@XmlElement(name = "Outcome")
public String getOutcomeName() {
	return outcomeName;
}

	@XmlElement(name = "Subgroup")
	public String getSubgroupName() {
		return subgroupName;
	}

	public void setSubgroupName(String subgroupName) {
		this.subgroupName = subgroupName;
	}
	
	



	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}


	
	public String getOutcomeType() {
		return outcomeType;
	}



	public GenericInverseOutcomeObject() {
		super();
	}
	
public GenericInverseOutcomeObject(Element oeDataElement, Element comparisonNameElement, Element oeOutcomeNameElement, Element oeOutcomeElement, Element oeSubgroupElement, String review) {
	this.reviewTitle = review;
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
