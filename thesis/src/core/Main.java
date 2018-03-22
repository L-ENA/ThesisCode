package core;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;



public class Main {
	

	
	public static void main(String[] args)  throws ParserConfigurationException{
		
		
		Database d = new Database();//in Database class a list of trials in this review will be created
		d.makeList();
		
	try {
		JAXBContext jaxbContext = JAXBContext.newInstance(Database.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(d, new File("haloperidol1.xml"));	//puts database into a xml file
		
		
	} catch (JAXBException e) {
		
		e.printStackTrace();
	}
		

	}

}
