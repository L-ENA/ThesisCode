package trialAndOutcome;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import core.ParseReview;

public class writeBack {
	
	public void cleanReview(TrialObject a) throws ParserConfigurationException {
		ParseReview parser = new ParseReview();
		Document[] reviewsToChange = parser.chooseAndParse();
		Document rm5 = reviewsToChange[0];
		
			
			//Element newChar = rm5.createElement("INCLUDED_CHAR");
			//a.characteristicsOfIncludedStudiesElement.appendChild(newChar);
			
			Element root = rm5.getDocumentElement();
		    Element rootElement = rm5.getDocumentElement();
		    
		    //String newCharOutcomesContent = 
		    //a.charObject.charOutcomesElement.setTextContent(textContent);     
			
			 
		
	}
	
}
