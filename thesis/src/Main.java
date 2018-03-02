import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Main {

	
	public static void main(String[] args) throws ParserConfigurationException {
		ParseReview parser = new ParseReview();
		Document review = parser.chooseAndParse(); //contains reviews specified by user
		int numberOfStudies = parser.numberOfIncludedStudies(review);
		
	for (int i = 0; i < numberOfStudies; i++){
			
			TrialObject a = new TrialObject(review, i);
			
			
			
		}
		

	}

}
