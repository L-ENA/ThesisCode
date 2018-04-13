package core;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import trialAndOutcome.TrialObject;
@XmlRootElement
public class Database {
	public Database() {
		super();
	}

	List<TrialObject> trialList = new ArrayList<>();
	protected String reviewName = "";
	
	@XmlElement(name = "TRIAL")
	public List<TrialObject> getTrialList() {
		return trialList;
	}
	public void setTrialList(List<TrialObject> trialList) {
		this.trialList = trialList;
	}
	
	public void makeList() throws ParserConfigurationException{
		
		
		ParseReview parser = new ParseReview();
		Document review = parser.chooseAndParse(); //contains reviews specified by user via pop up browsing window
		int numberOfStudies = parser.numberOfIncludedStudies(review);//looks at how many trials there are to extract
		
		TrialObject a = new TrialObject(); //makes TrialObject with default empty constructor. If there are trials included in this review, the proper constructer is called in the for-loop.
		
	for (int i = 0; i < numberOfStudies; i++){
			
			a = new TrialObject(review, i);//extracts all data of the specified trial and dumps it into the trial list
			trialList.add(a);
			//System.out.println("Trial " + a.aauthorYearLetter + " added!");
			
		}
	
		reviewName = a.getReviewTitle();
	}
	

}
