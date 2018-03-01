
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ParseReview {
	
	protected Document[] chooseAndParse() throws ParserConfigurationException{

		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		
		int returnCode = chooser.showOpenDialog(null);
		if(returnCode == JFileChooser.CANCEL_OPTION){// things to do when cancel
			return null;
		} 
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		File[] chosenFiles = chooser.getSelectedFiles(); //get absolute file?
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		Document rm5 = null;
		DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
		
		int length = chosenFiles.length;
		
		if (length  == 1){ // if only 1 review was chosen
			Document[] rm5Array = new Document[1];
			try {
				
				rm5 = dBuilder.parse(chosenFiles[0]);
				rm5.normalize();
				rm5Array[0] = rm5;
			} catch (SAXException e) {
			} catch (IOException e) {
			}
			
			return rm5Array;
			
		} else { // more than 1 was chosen
			
			Document[] rm5Array = new Document[length];
			
			
			
			for (int i = 0; i < chosenFiles.length; i++){
				try {
					
					rm5 = dBuilder.parse(chosenFiles[i]);
					rm5.normalize();
					rm5Array[i] = rm5;
				} catch (SAXException e) {
				} catch (IOException e) {
				}
			}

				return rm5Array;
		}
		
			
		 
		
		
	}

}
