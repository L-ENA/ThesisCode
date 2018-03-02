import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TrialObject {
	
	protected String author; //check
	protected int year;//check
	protected String doi;
	protected int crgID;
	protected String revManID;//check
	//protected RANDOMIZATIONTYPE randomisation:
	//protected ALLOCATIONTYPE allocation;
	//protected BLINDINGTYPE blinding;
	//protected ATTRITIONTYPE attrition;
	//protected REPORTINGTYPE reporting;
	protected int nrOfParticipants;
	protected String interventionGroup;
	protected String controlGroup;

	protected int nrIntervention;
	protected int nrControl;
	//protected SETTING setting;
	//protected outcomeList : OutcomeObjects
	
	public TrialObject(Document review, int studyNumber){
					
		//Traverses XML until the desired study is found
					NodeList rootList = review.getElementsByTagName("COCHRANE_REVIEW");
					Node rootNode = rootList.item(0);
					Element rootElement = (Element) rootNode;
					
					NodeList characteristicsOfStudiesList = rootElement.getElementsByTagName("CHARACTERISTICS_OF_STUDIES");
					Node characteristicsOfStudiesNode = characteristicsOfStudiesList.item(0);
					Element characteristicsOfStudiesElement = (Element) characteristicsOfStudiesNode;
					
					NodeList characteristicsOfIncludedStudiesList = characteristicsOfStudiesElement.getElementsByTagName("CHARACTERISTICS_OF_INCLUDED_STUDIES");
					Node characteristicsOfIncludedStudiesNode = characteristicsOfIncludedStudiesList.item(0);
					Element characteristicsOfIncludedStudiesElement = (Element) characteristicsOfIncludedStudiesNode;
					
					NodeList includedStudiesList = characteristicsOfIncludedStudiesElement.getElementsByTagName("INCLUDED_CHAR");
					Node studyToExtractNode = includedStudiesList.item(studyNumber);
					Element studyToExtractElement = (Element) studyToExtractNode;
					//Extracts information into variables
					revManID = studyToExtractElement.getAttribute("STUDY_ID"); //Gets revman ID for study
					String cache = revManID.replaceAll("[^\\d.]", "");
					year = Integer.parseInt(cache); //Takes year of publication from ID
					cache = revManID.replaceAll("STD-", "");
					String[] cacheArray = cache.split("-");
					author = cacheArray[0]; //Taken name of author from ID
							
					
					System.out.println(revManID);
					System.out.println(year);
					System.out.println(author);
					
					
					
					
	}
	


}
