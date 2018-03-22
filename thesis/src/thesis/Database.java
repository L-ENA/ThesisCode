package thesis;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
@XmlRootElement
public class Database {
	public Database() {
		super();
	}

	List<TrialObject> trialList = new ArrayList<>();
	
	@XmlElement
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
		
	for (int i = 0; i < numberOfStudies; i++){
			
			TrialObject a = new TrialObject(review, i);//extracts all data of the specified trial and dumps it into the trial list
			trialList.add(a);
			//System.out.println("Trial " + a.aauthorYearLetter + " added!");
		}
	}
	

}
