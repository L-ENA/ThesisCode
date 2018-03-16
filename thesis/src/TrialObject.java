import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TrialObject {
	protected static int counter = 1;
	protected String mainAuthor; //check
	protected int year;//check
	protected String authorYearLetter = ""; //for comparison with MeerKatBE
	protected String doi;
	protected int crgID;
	protected String revManID;//check
	protected String[] references; //check

	protected boolean crossoverTrial = false;
	protected boolean paralellTrial = false;
	protected boolean factorialTrial = false;
	protected boolean otherDesign = false;
	protected String designProse;
	protected String designAddedInfo;
	protected String countries = "";
	//protected RANDOMIZATIONTYPE randomisation:
	//protected ALLOCATIONTYPE allocation;
	//protected BLINDINGTYPE blinding;
	//protected ATTRITIONTYPE attrition;
	//protected REPORTINGTYPE reporting;
	
	protected String selectionBiasRandomSequenceJudgement;
	protected String selectionBiasRandomSequenceBiasRisk;
	protected String selectionBiasAllocationConcealmentBiasRisk;
	protected String selectionBiasAllocationConcealmentJudgement;
	protected String performanceBiasRisk;
	protected String performanceBiasJudgement;
	protected String detectionBiasRisk;
	protected String detectionBiasJudgement;
	protected String attritionBiasRisk;
	protected String attritionBiasJudgement;
	protected String reportingBiasRisk;
	protected String reportingBiasJudgement;
	protected String otherBiasRisk;
	protected String otherBiasJudgement;
	
	protected int nrOfParticipants;
	protected String interventionGroup;
	protected String controlGroup;

	protected int nrIntervention;
	protected int nrControl;
	//protected SETTING setting;
	//protected outcomeList : OutcomeObjects
	protected String meerKatCountry;
	
	private int breakBiasVerification;
	
	private Matcher m;
	private Pattern design = Pattern.compile("([Dd]esign)+");
	//parallel
	private Pattern parallelDesign = Pattern.compile("([pP]arallel)+|([Bb]etween\\s[Pp]atient(s)?)+|([Nn]on(-|\\s)[Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern parallelDesignCleaner = Pattern.compile("([Dd]esign)+|([pP]arallel)+|([Gg]roup(s)?)+|([Ss]tud(y|ies))?+|[:(),.]+");
	//cross over, crossed over, crossover, cross-over, crossed-over + caps variations
	private Pattern crossoverDesign = Pattern.compile("([Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern crossoverDesignCleaner = Pattern.compile("([Dd]esign)+|([Cc]ross(ed)?-?\\s?[Oo](ver))+|[:(),.]+");
	//factorial, fully crossed + caps variation
	private Pattern factorialDesign = Pattern.compile("(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+");
	private Pattern factorialDesignCleaner = Pattern.compile("([Dd]esign)+|(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+|[:(),.]+");
	
	//patterns for beginning/end of strings
	private Pattern beginningEndArray = Pattern.compile("^\\W+|[^\\w)]$"); //All special characters at beginning plus all special characters without closing brackets at end, because of factorial design
	private Pattern beginningEnd = Pattern.compile("^\\W+|\\W+$");	//All special chars at beginning and end
	private Pattern endPunctuationCleanerA = Pattern.compile("([^.!?\"']$)");	//matches when neither .!?"' at end of sentence. Needs 
	private Pattern endPunctuationCleanerB = Pattern.compile("([^!?.][\"'])$");	//to be applied after other punctuation regex. matches when there is no !?. before "'
	private Element qualityItemsElement;
	Element studyElement;
	private NodeList qualityItemList;
	private NodeList referencesList;
	
	public TrialObject(Document review, int studyNumber){
					
					//Creates rootElement
					
					Element rootElement = null;
					try {
						NodeList rootList = review.getElementsByTagName("COCHRANE_REVIEW");
						Node rootNode = rootList.item(0);
						rootElement = (Element) rootNode;
					} catch (Exception e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();
					}
					
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Traverses the characteristics of included studies part of the revman file. This is where bias tables and prose information are located
					Element studyToExtractElement = null;
					Element characteristicsOfStudiesElement = null;
					try {
						NodeList characteristicsOfStudiesList = rootElement.getElementsByTagName("CHARACTERISTICS_OF_STUDIES");
						Node characteristicsOfStudiesNode = characteristicsOfStudiesList.item(0);
						characteristicsOfStudiesElement = (Element) characteristicsOfStudiesNode;
						
						NodeList characteristicsOfIncludedStudiesList = characteristicsOfStudiesElement.getElementsByTagName("CHARACTERISTICS_OF_INCLUDED_STUDIES");
						Node characteristicsOfIncludedStudiesNode = characteristicsOfIncludedStudiesList.item(0);
						Element characteristicsOfIncludedStudiesElement = (Element) characteristicsOfIncludedStudiesNode;
						
						NodeList includedStudiesList = characteristicsOfIncludedStudiesElement.getElementsByTagName("INCLUDED_CHAR");
						Node studyToExtractNode = includedStudiesList.item(studyNumber);
						studyToExtractElement = (Element) studyToExtractNode;
					} catch (Exception e10) {
						// TODO Auto-generated catch block
						e10.printStackTrace();
					}
					
					//Extracts information into variables
					//Gets revman ID for study
					revManID = studyToExtractElement.getAttribute("STUDY_ID"); 
					String cache = revManID.replaceAll("STD-", "").replaceAll("(_x002d_)", "-").replaceAll("(_x0026_)", "&");// replaces space with hyphen and at first puts & back 
					String[] cacheArray = cache.split("-\\d+");	//Splits at "-"+ digit
					mainAuthor = cacheArray[0]; //Take name of author from ID
					
					String yearLetter;
					try {
						yearLetter = cacheArray[1];
					} catch (Exception e) {
						// if this trialname/year is unique in the review
						yearLetter = "";
					}
					
					NodeList qualityItemsList = rootElement.getElementsByTagName("QUALITY_ITEMS");
					Node qualityItemsNode = qualityItemsList.item(0);
					qualityItemsElement = (Element) qualityItemsNode;
					
					qualityItemList = qualityItemsElement.getElementsByTagName("QUALITY_ITEM");
					breakBiasVerification = qualityItemList.getLength();
					String[] biasArray;
					
					biasArray = biasAnalyser(0, "random sequence generation");
					selectionBiasRandomSequenceBiasRisk = biasArray[0];
					selectionBiasRandomSequenceJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "allocation concealment");
					selectionBiasAllocationConcealmentBiasRisk = biasArray[0];
					selectionBiasAllocationConcealmentJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "performance bias");
					performanceBiasRisk = biasArray[0];
					performanceBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "detection bias");
					detectionBiasRisk = biasArray[0];
					detectionBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "incomplete outcome data");
					attritionBiasRisk = biasArray[0];
					attritionBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "selective reporting");
					reportingBiasRisk = biasArray[0];
					reportingBiasJudgement = biasArray[1];
					
					biasArray = biasAnalyser(0, "other bias");
					otherBiasRisk = biasArray[0];
					otherBiasJudgement = biasArray[1];
					

					
					
					//////////////////////////////////////////////////////////////////////////////////////////////////
					//extracts prose about methods and tries to match this prose to create a standardised output
					NodeList charMethodsList = studyToExtractElement.getElementsByTagName("CHAR_METHODS");
					Node charMethodsNode = charMethodsList.item(0);
					Element charMethodsElement = (Element) charMethodsNode;
					
					
					
					//NodeList methodParagraphList = charMethodsElement.getElementsByTagName("P");
					//Node methodNode = methodParagraphList.item(0);
					//Element methodElement = (Element) methodNode;
						
					String methodString = charMethodsElement.getTextContent();

					
				
			
					
					String[] methodStringArray = methodString.split("\\."); // splits text at every full stop
					
					for (int k = 0; k<methodStringArray.length; k++){
						//System.out.println(methodStringArray[k]);
						m = beginningEndArray.matcher(methodStringArray[k]);	//Factorial design with brackets causes problems otherwise
						methodStringArray[k] = m.replaceAll("");
						
						m = design.matcher(methodStringArray[k]);	
						if (m.find()){	//Uses regex pattern to identify if this line is about design of trial
							designVerifyer(methodStringArray[k]);
						}

						}
					
					
					//Extracts prose from participant section and tries to match
					NodeList charParticipantsList = studyToExtractElement.getElementsByTagName("CHAR_PARTICIPANTS");
					Node charParticipantsNode = charParticipantsList.item(0);
					Element charParticipantsElement = (Element) charParticipantsNode; 
					String[] extractedCountries;
					
					//Searches the big method String for country names. Writes them to String in alphabetical order and 
					//also creates output in MeerKatBE syntax
					String[] countryList = new String[] {"Afghanistan","Albania","Algeria","Andorra","Angola","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Basutoland","Belarus","Belgium","Belize","Benin","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina Faso","Burma", "Burundi","Cambodia","Cameroon","Canada","Cabo Verde","China", "Central African Republic","Ceylon","Chad","Chile","Colombia","Comoros","Congo","Costa Rica","Cote d'Ivoire","Croatia","Cuba","Curacao","Cyprus","Czechia","Czech Republic","Czechoslovakia","Denmark","Djibouti","Dominica","Dominican Republic","East Germany","East Pakistan","East Timor","Ecuador", "Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia","Fiji","Finland","France","Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada","Guatemala"," Guinea","Guinea-Bissau","Guyana","Haiti","Holy See","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran"," Iraq","Ireland","Israel","Italy","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati", "Kosovo","Korea","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia", "Madagascar","Malawi","Malaysia","Maldives","Mali", "Malta","Marshall Islands","Mauritania","Mauritius","Mexico","Micronesia","Moldova", "Monaco","Mongolia","Montenegro","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","New Zealand","Nicaragua", "Niger","Nigeria","Norway","Oman","Pakistan","Palau","Palestinian Territories","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Qatar","Rhodesia","Romania","Rwanda","Russia","Saint Kitts and Nevis","Saint Lucia","Saint Vincent and the Grenadines","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Sikkim","Singapore","Sint Maarten","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Sudan","South Vietnam","Southwest Africa","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Tanganyika","Taiwan","Tajikistan","Tanzania"," Thailand"," Timor-Leste"," Togo"," Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","UK", "Ukraine","UAE","Union of Soviet Socialist Republics","United Arab Emirates","United Arab Republic","United Kingdom","Uruguay","USA","USSR","Uzbekistan","Vanuatu","Venezuela","Vietnam", "Western Samoa","West Germany","Yemen","Yugoslavia","Zaire","Zambia","Zanzibar","Zimbabwe"};
					
					for (int i = 0; i < countryList.length; i++){
							if (methodString.contains(countryList[i])){
								countries = countries + countryList[i] + ", ";
							}
						}
					countries = countries.trim().replaceAll(",$", "");
					extractedCountries = countries.split("(,\\s)");
					if (extractedCountries.length > 1){
						meerKatCountry = "Multi-Center";
						for (int i = 0; i < extractedCountries.length; i++){
							meerKatCountry = meerKatCountry + "//" + extractedCountries[i];
						}
					} else {
						meerKatCountry = countries;
					}
					//Extracts countries from "Participants" section of table	
					
					if(countries.equals("")){
						String participantString = charParticipantsElement.getTextContent();
						for (int i = 0; i < countryList.length; i++){
							if (participantString.contains(countryList[i])){
								countries = countries + countryList[i] + ", ";
							}
						}
						countries = countries.trim().replaceAll(",$", "");
						extractedCountries = countries.split(",\\s");
						if (extractedCountries.length > 1){
							meerKatCountry = meerKatCountry + "Multi-Center";
							for (int i = 0; i < extractedCountries.length; i++){
								meerKatCountry = meerKatCountry + "//" + extractedCountries[i];
							}
						} else {
							meerKatCountry = countries;
						}
					}
					

					
					////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Traverses the revman file's Studies and references section
					
					try {
						NodeList studiesAndReferencesList = rootElement.getElementsByTagName("STUDIES_AND_REFERENCES");
						Node studiesAndReferencesNode = studiesAndReferencesList.item(0);
						Element studiesAndReferencesElement = (Element) studiesAndReferencesNode;
						
						NodeList studiesList = studiesAndReferencesElement.getElementsByTagName("STUDIES");
						Node studiesNode = studiesList.item(0);
						Element studiesElement = (Element) studiesNode;
						
						
						NodeList includedStudiesBList = studiesElement.getElementsByTagName("INCLUDED_STUDIES");
						Node includedStudiesNode = includedStudiesBList.item(0);
						Element includedStudiesElement = (Element) includedStudiesNode;
						
						
						NodeList studyList = includedStudiesElement.getElementsByTagName("STUDY");
						Node studyNode = studyList.item(studyNumber);
						studyElement = (Element) studyNode;
					} catch (Exception e9) {
						// TODO Auto-generated catch block
						e9.printStackTrace();
					}
					
					try {
						cache = studyElement.getAttribute("YEAR");
						year = Integer.parseInt(cache); //converts String to int
					} catch (NumberFormatException e8) {
						// if year can't be extracted as attribute it is tried via revman-ID. In case there is a hyphen or..., the characters resulting from this are eliminated and the String is restored to normal
						cache = revManID.replaceAll("(_x002d_)", "-").replace("(_x0026_)", "&").replaceAll("[^\\d.]", "");
						year = Integer.parseInt(cache); //Takes year of publication from ID
					}

					
					referenceExtracting(); //Extracts all information on references of this trial. See method below for more details
					
			
					
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Extracts OutcomeObjects for this trial
					
						System.out.println("Main author of Study: " + mainAuthor);
					
					
					NodeList analysesAndDataList = rootElement.getElementsByTagName("ANALYSES_AND_DATA");
					Node analysesAndDataNode = analysesAndDataList.item(0);
					Element analysesAndDataElement = (Element) analysesAndDataNode;
					
					NodeList comparisonList = analysesAndDataElement.getElementsByTagName("COMPARISON");
					for (int i = 0; i < comparisonList.getLength(); i++){
						Node comparisonNode = comparisonList.item(i);
						Element comparisonElement = (Element) comparisonNode;
						
						NodeList comparisonNameList = comparisonElement.getElementsByTagName("NAME");
						Node comparisonNameNode = comparisonNameList.item(0);
						Element comparisonNameElement = (Element) comparisonNameNode; //takeOver for comparisonname and groupnames
						
						
						NodeList dichOutcomeList = comparisonElement.getElementsByTagName("DICH_OUTCOME");
						for (int l = 0; l < dichOutcomeList.getLength(); l++){
							Node dichOutcomeNode = dichOutcomeList.item(l);
							Element dichOutcomeElement = (Element) dichOutcomeNode;
							
							NodeList dichOutcomeNameList = dichOutcomeElement.getElementsByTagName("NAME");
							Node dichOutcomeNameNode = dichOutcomeNameList.item(0);
							Element dichOutcomeNameElement = (Element) dichOutcomeNameNode;
							
							
							NodeList dichSubgroupList = dichOutcomeElement.getElementsByTagName("DICH_SUBGROUP");
							for (int j = 0; j <dichSubgroupList.getLength(); j++){
								Node dichSubgroupNode = dichSubgroupList.item(j);
								Element dichSubgroupElement = (Element) dichSubgroupNode;
								
								NodeList dichDataList = dichSubgroupElement.getElementsByTagName("DICH_DATA");
								for (int k = 0; k < dichDataList.getLength(); k++){
									Node dichDataNode = dichDataList.item(k);
									Element dichDataElement = (Element) dichDataNode;
									
									if (dichDataElement.getAttribute("STUDY_ID").equals(revManID)){
										
										OutcomeObject dOBJ = new DichOutcomeObject(dichDataElement, comparisonNameElement, dichOutcomeNameElement, dichOutcomeElement, dichSubgroupElement);
									}
								}
							}
						}
						
						
						
						
//						NodeList comparisonNameList = comparisonElement.getElementsByTagName("NAME");
//						Node comparisonNameNode = comparisonNameList.item(0);
//						Element comparisonNameElement = (Element) comparisonNameNode;
//						
//						comparisonName = comparisonNameElement.getTextContent();
//						System.out.println(comparisonName);
					}
					
					
//					System.out.println(mainAuthor);
//					
//					System.out.println("RandomSequenceBias: " + selectionBiasRandomSequenceBiasRisk + ". " + selectionBiasRandomSequenceJudgement 
//							+ "\n" + "AllocationBias: " + selectionBiasAllocationConcealmentBiasRisk + ". " + selectionBiasAllocationConcealmentJudgement
//							+ "\n" + "PerformanceBias: " + performanceBiasRisk + ". " + performanceBiasJudgement
//							+ "\n" + "DetectionBias: " + detectionBiasRisk + ". " + detectionBiasJudgement
//							+ "\n" + "AttritionBias: " + attritionBiasRisk + ". " + attritionBiasJudgement
//							+ "\n" + "ReportingBias: " + reportingBiasRisk + ". " + reportingBiasJudgement
//							+ "\n" + "OtherBias: " + otherBiasRisk + ". " + otherBiasJudgement
//							);
//				
					
//					for (int j = 0; j<references.length; j++){
//						System.out.println(references[j]);
//					}
					
//								System.out.println(year);
//					
//								System.out.println(revManID);
//								authorYearLetter = mainAuthor + year + yearLetter;
//								System.out.println(authorYearLetter);
//					
					//				
//					System.out.println("Country or countries : " + countries);
//					System.out.println(meerKatCountry);
					
					System.out.println();
					
					
					
					
	}
	
	 
	
	private void referenceExtracting(){
		//This following for-loop runs through all references for the included study and saves relevant fields into an array that is created to hold information in the following positions:
		//0th, 9th ..index -> Type of publication, eg. Journal ->"TYPE"
		//1st..., 10th..index -> Primary attribute: Yes or No ->"PRIMARY"
		//2nd... ->Names of all authors of this publication ->"AU"
		//3rd...->Title of publication->"TI"
		//4th...->Name of Journal->Journal->"SO"
		//5...-> Year published->"YR"
		//6...->Volume ->"VL" 
		//7...-> Issue ->"NO"
		//8...-> Pages ->"PG"
		
		//if a filed is not available, eg. if the reference refers to a conference protocol that lacks page numbers, empty "" space is inserted and next position of array is tried to be filled
	
		
		referencesList = studyElement.getElementsByTagName("REFERENCE");
		int numberReferences = referencesList.getLength();
		references = new String[numberReferences * 9];
		int arrayCounter = 0;
		for (int i = 0; i < numberReferences; i++){
			Element referenceElement = null;
			try {
				Node referenceNode = referencesList.item(i);
				referenceElement = (Element) referenceNode;
			} catch (Exception e7) {
			}
		
			try {
				if (referenceElement != null){
				references[arrayCounter] = referenceElement.getAttribute("TYPE"); 
				//System.out.println(referenceElement.getAttribute("TYPE"));
				} else {
					references[arrayCounter] = "";
				}
			} catch (Exception e6) {
			}
			arrayCounter++;
			
			try {
				if (referenceElement != null){
				references[arrayCounter] = referenceElement.getAttribute("PRIMARY");
				//System.out.println(referenceElement.getAttribute("PRIMARY"));
				} else {
					references[arrayCounter] = "";
				}
			} catch (Exception e5) {
			}
			arrayCounter++;
			
			
			Element auElement = null;
			try {
				NodeList auList = referenceElement.getElementsByTagName("AU");
				Node auNode = auList.item(0);
				auElement = (Element) auNode;
			} catch (Exception e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
			
			try {
				if (auElement != null){
				references[arrayCounter] = auElement.getTextContent().replaceAll("\n", "").trim(); //2nd, 11th... Authors names for this reference
				//System.out.println(auElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element tiElement = null;
			try {
				NodeList tiList = referenceElement.getElementsByTagName("TI");
				Node tiNode = tiList.item(0);
				tiElement = (Element) tiNode;
			} catch (Exception e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			
			try {
				if (tiElement != null){
				references[arrayCounter] = tiElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(tiElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element soElement = null;
			try {
				NodeList soList = referenceElement.getElementsByTagName("SO");
				Node soNode = soList.item(0);
				soElement = (Element) soNode;
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			try {
				if (soElement != null){
				references[arrayCounter] = soElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(soElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element yrElement = null;
			try {
				NodeList yrList = referenceElement.getElementsByTagName("YR");
				Node yrNode = yrList.item(0);
				yrElement = (Element) yrNode;
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			try {
				if (yrElement != null){
				references[arrayCounter] = yrElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(yrElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element vlElement = null;
			try {
				NodeList vlList = referenceElement.getElementsByTagName("VL");
				Node vlNode = vlList.item(0);
				vlElement = (Element) vlNode;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				if (vlElement != null){
				references[arrayCounter] = vlElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(vlElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element noElement = null;
			try {
				NodeList noList = referenceElement.getElementsByTagName("NO");
				Node noNode = noList.item(0);
				noElement = (Element) noNode;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				if (noElement != null){
				references[arrayCounter] = noElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(noElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			arrayCounter++;
			
			Element pgElement = null;
			try {
				NodeList pgList = referenceElement.getElementsByTagName("PG");
				Node pgNode = pgList.item(0);
				pgElement = (Element) pgNode;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				if (pgElement != null){
				references[arrayCounter] = pgElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arrayCounter++;
			
			
		
			
			
		}
	}
	

	
	private void designVerifyer(String str){
		
		//To see if this String contains info on trial design
		m = parallelDesign.matcher(str); 	//Uses regex pattern for identifying parallel trials
		if (m.find()){	//To see if this trial is a parallel trial. Returns true if the trial is parallel
			paralellTrial = true;	//Boolean to store that this trial is parallel
			designProse = str.trim();	//stores intact description of the trial
			m = parallelDesignCleaner.matcher(str);	//uses regex pattern for cleaning parellel trial
			designAddedInfo = m.replaceAll("").trim();	//cleans additional info from prose
			m = beginningEnd.matcher(designAddedInfo);	//regex pattern for cleaning beginning/end from non-word characters
			designAddedInfo = m.replaceAll("");	//cleans additional info from prose
			
			if (designAddedInfo.length() != 0)	//capitalises first letter
				designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
			
	//		System.out.println("Design: parallel" + ". PROSE DESIGN: " + designProse);
//			
		} else {
			m = crossoverDesign.matcher(str); 
			if (m.find()){
				crossoverTrial = true;
				designProse = str.trim();
				m = crossoverDesignCleaner.matcher(str);
				designAddedInfo = m.replaceAll("").trim();
				m = beginningEnd.matcher(designAddedInfo);
			
				designAddedInfo = m.replaceAll("");
				if (designAddedInfo.length() != 0)
					designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
				
	//		System.out.println("Design: Crossover" + ". PROSE DESIGN: " + designProse);
			} else {
				m = factorialDesign.matcher(str);
				if (m.find()){
					factorialTrial = true;
					designProse = str.trim();
					m = factorialDesignCleaner.matcher(str);
					designAddedInfo = m.replaceAll("").trim();
					m = beginningEnd.matcher(designAddedInfo);
				
					designAddedInfo = m.replaceAll("");
					if (designAddedInfo.length() != 0)
							designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
					
					
	//			System.out.println("Design: factorial" + ". PROSE DESIGN: " + designProse);
				} else {
					otherDesign = true;
					designProse = str.trim();
					designAddedInfo = "";
				}
			}
		}
		
	}
	
	private String[] biasAnalyser(int index, String description){
		String[] forReturn = {"", ""}; //index 0 will hold choice of bias risk, index 1 will hold judgement/quotes
		
		Node specificItemNode = qualityItemList.item(index);	//Index navigates to the desired bias item
		Element specificItemElement = (Element) specificItemNode;
		
		
		
		NodeList nameList = specificItemElement.getElementsByTagName("NAME");
		Node nameNode = nameList.item(0);
		Element nameElement = (Element) nameNode;
		
		NodeList qualityItemDataList = specificItemElement.getElementsByTagName("QUALITY_ITEM_DATA");
		Node qualityItemDataNode = qualityItemDataList.item(0);
		Element qualityItemDataElement = (Element) qualityItemDataNode;
		
		NodeList specificItemDataEntryList = qualityItemDataElement.getElementsByTagName("QUALITY_ITEM_DATA_ENTRY");
		int entryLength = specificItemDataEntryList.getLength();
		
		String biasVerification = nameElement.getTextContent().toLowerCase();
		if (entryLength != 0 && biasVerification.contains(description) ) {
			for (int l = 0; l < entryLength; l++) {
				Node specificDataEntryNode = specificItemDataEntryList.item(l);
				Element specificDataEntryElement = (Element) specificDataEntryNode;
				if (specificDataEntryElement.getAttribute("STUDY_ID").equals(revManID)) { //searches the desired trial for this object
					if (specificDataEntryElement.getAttribute("RESULT").equals("UNKNOWN")) { //These are the options from RevMans dropdown menu in the bias table
						forReturn[0] = "Unclear Risk";
					} else if (specificDataEntryElement.getAttribute("RESULT").equals("YES")) {
						forReturn[0] = "Low risk";
					} else if (specificDataEntryElement.getAttribute("RESULT").equals("NO")) {
						forReturn[0] = "High risk";
					} else {
						forReturn[0] = "Information on " + description + " could not be extracted";	//if anything unexpected happens
					}
					forReturn[1] = specificDataEntryElement.getTextContent().trim().replaceAll("\n", ", ");
					
					m = endPunctuationCleanerA.matcher(forReturn[1]);
					if (m.find())										//Cleaning data: Patterns match when there is no either .!?"' at the very end 
						forReturn[1] = forReturn[1] + ".";				//and while there is also no .!? before the last "'  -> in this case a full stop will be inserted
					m = endPunctuationCleanerB.matcher(forReturn[1]);
					if (m.find())
						forReturn[1] = forReturn[1] + ".";
				}
			} 
		} else {
			if (breakBiasVerification > (index + 1)){ 	// breakBiasVerification contains amount of nodes that can be analysed
				return biasAnalyser((index + 1), description); 	// as long as there are more nodes to be analysed the recursion is active
			} else {
				return new String[] {"Information could not be retrieved", "Information could not be retrieved"};	//no more nodes, possibly the description String has to be changed or the bias item is not there
			}
		}
		return forReturn;
	}
	


}
