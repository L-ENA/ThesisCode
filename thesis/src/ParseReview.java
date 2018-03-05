
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseReview {
	
	protected Document chooseAndParse() throws ParserConfigurationException{
		
JFileChooser chooser = new JFileChooser();
		
		int returnCode = chooser.showOpenDialog(null);
		if(returnCode == JFileChooser.CANCEL_OPTION){// things to do when cancel
			return null;
		} 
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		File review = chooser.getSelectedFile().getAbsoluteFile();

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		

			Document rm5 = null;
			try {
				DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
				rm5 = dBuilder.parse(review);
				rm5.normalize();
			} catch (ParserConfigurationException e) {
			} catch (SAXException e) {
			} catch (IOException e) {
			}
			return rm5;
	}
	
	protected int numberOfIncludedStudies(Document rm5){
		
		// Gets root element of parsed XML document, moves to included studies section and returns length of included studies list.
		NodeList rootList = rm5.getElementsByTagName("COCHRANE_REVIEW");
		Node rootNode = rootList.item(0);
		Element rootElement = (Element) rootNode;
		
		NodeList characteristicsOfStudiesList = rootElement.getElementsByTagName("CHARACTERISTICS_OF_STUDIES");
		Node characteristicsOfStudiesNode = characteristicsOfStudiesList.item(0);
		Element characteristicsOfStudiesElement = (Element) characteristicsOfStudiesNode;
		
		NodeList characteristicsOfIncludedStudiesList = 
				characteristicsOfStudiesElement.getElementsByTagName("CHARACTERISTICS_OF_INCLUDED_STUDIES");
		Node characteristicsOfIncludedStudiesNode = characteristicsOfIncludedStudiesList.item(0);
		Element characteristicsOfIncludedStudiesElement = (Element) characteristicsOfIncludedStudiesNode;
		
		NodeList includedStudiesList = characteristicsOfIncludedStudiesElement.getElementsByTagName("INCLUDED_CHAR");
		
		return includedStudiesList.getLength();
	}

}
