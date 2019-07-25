package identifiers;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OtherIdentifier extends Identifier {

	protected String type = "";
	protected String exactType = "";
	protected String value = "";
	protected String link = "";
	protected String review = "";
	protected String studyLevelLink="";
	


	public OtherIdentifier(Element identifierElement, String link, String review, String sll) {
		super();
		type = "Other";
		this.link = link;
		this.review = review;
		this.studyLevelLink= sll;
		try {
			
			try {
				exactType = identifierElement.getAttribute("OTHERTYPE");//this variable only exists if the field "Other" is altered by the author
			} catch (Exception e) {
				
			}
			
			
			value = identifierElement.getAttribute("VALUE");
			//System.out.println("Other value is : " + value);
			
			
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


	public OtherIdentifier() {
		
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



	@Override
	public String getExactType() {
		return exactType;
	}




	public void setExactType(String exactType) {
		this.exactType = exactType;
	}




	
	
	
	
	public String getValue() {
		return value;
	}




	public void setValue(String value) {
		this.value = value;
	}




	



	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}


	
	

}
