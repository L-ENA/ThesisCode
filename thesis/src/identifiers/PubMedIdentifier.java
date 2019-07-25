package identifiers;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PubMedIdentifier extends Identifier {

	protected String type = "";
	protected String value = "";
	protected String link = "";
	protected String review = "";
	protected String studyLevelLink="";




	public PubMedIdentifier(Element identifierElement, String link, String review, String sll) {
		super();
		this.studyLevelLink= sll;
		this.link = link;
		this.review = review;
		type = "PubMed";
		try {
			value = identifierElement.getAttribute("VALUE");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
	}
	
	public String getStudyLevelLink() {
		return studyLevelLink;
	}

	public void setStudyLevelLink(String studyLevelLink) {
		this.studyLevelLink = studyLevelLink;
	}
	
	
public PubMedIdentifier() {
		
	}


public String getReview() {
	return review;
}




public void setReview(String review) {
	this.review = review;
}




public String getLink() {
	return link;
}




public void setLink(String link) {
	this.link = link;
}




public String getType() {
	return type;
}




public void setType(String type) {
	this.type = type;
}








public String getValue() {
	return value;
}




public void setValue(String value) {
	this.value = value;
}




}
