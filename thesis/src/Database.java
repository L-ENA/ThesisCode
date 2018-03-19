import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class Database implements java.io.Serializable{
	public Database() {
		super();
	}

	List<TrialObject> trialList = new ArrayList<>();
	public List<TrialObject> getTrialList() {
		return trialList;
	}
	public void setTrialList(List<TrialObject> trialList) {
		this.trialList = trialList;
	}
	
	public void makeList() throws ParserConfigurationException{
		
		
		ParseReview parser = new ParseReview();
		Document review = parser.chooseAndParse(); //contains reviews specified by user
		int numberOfStudies = parser.numberOfIncludedStudies(review);
		
	for (int i = 0; i < numberOfStudies; i++){
			
			TrialObject a = new TrialObject(review, i);
			trialList.add(a);
			System.out.println("Trial " + a.aauthorYearLetter + " added!");
		}
	}
	

}
