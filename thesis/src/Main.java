import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;


public class Main {

	
	public static void main(String[] args) throws ParserConfigurationException {
		ParseReview parserObject = new ParseReview();
		Document[] reviewArray = parserObject.chooseAndParse(); //contains reviews specified by user
		

	}

}
