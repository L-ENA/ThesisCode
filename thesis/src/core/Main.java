package core;
import java.io.File;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


//test123

public class Main {
	

	
	public static void main(String[] args)  throws Exception{
		
		
		Database d = new Database();
		d.makeList();//in Database class a list of trials in this review will be created
		
		if (d.reviewName == "") {//if reviewName is empty it indicates that only the default constructor for trial objects was called in the database class. Therefore, there are no trials included in the review that was analysed.
			System.out.println("No trials included");
		} else {
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
				
				
				
				String path = "Z:\\git\\ThesisCode\\thesis\\" + d.reviewName + ".xml";
				jaxbMarshaller.marshal(d, new File(path));	//puts database into a xml file that is saved according to path String
				
				
				
				
			} catch (JAXBException e) {
				
				e.printStackTrace();
			}
		}
		
	
		

	}
	
	
	
	
}

