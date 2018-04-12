package core;
import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//test123

public class Main {
	

	
	public static void main(String[] args)  throws Exception{
		
		
		Database d = new Database();//in Database class a list of trials in this review will be created
		d.makeList();
		
	try {
		JAXBContext jaxbContext = JAXBContext.newInstance(
				
				Database.class,
				
				references.JournalReferenceObject.class,
				references.BookReferenceObject.class,
				references.BookSectionReferenceObject.class,
				references.OtherReferenceObject.class,
				references.ConferenceReferenceObject.class,
				references.CorrespondenceReferenceObject.class,
				references.CochraneProtocolReferenceObject.class,
				references.CochraneReviewReferenceObject.class,
				references.SoftwareReferenceObject.class,
				references.UnpublishedReferenceObject.class,
				
				trialAndOutcome.ContinuousOutcomeObject.class,
				trialAndOutcome.DichotomousOutcomeObject.class
				
				
				
				);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		String path = "haloperidol.xml";
		jaxbMarshaller.marshal(d, new File(path));	//puts database into a xml file that is saved according to path String
		
		
		
		
	} catch (JAXBException e) {
		
		e.printStackTrace();
	}
		

	}
	
	
	
	
}
