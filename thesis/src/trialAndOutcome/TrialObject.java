package trialAndOutcome;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import references.BookSectionReferenceObject;

import references.BookReferenceObject;
import references.CochraneProtocolReferenceObject;
import references.CochraneReviewReferenceObject;
import references.ConferenceReferenceObject;
import references.CorrespondenceReferenceObject;
import references.JournalReferenceObject;
import references.OtherReferenceObject;
import references.ReferenceObject;
import references.SoftwareReferenceObject;
import references.UnpublishedReferenceObject;


public class TrialObject{
	///////////////////////////////////////////////////empty constructor for xml creation only
	public TrialObject() {
		super();
	}

//////////////////////////////////////////////////////////////////attributes. the protected ones have getters and setters because they will end up in the finished XML
	//protected static int counter = 1;
	protected String mainAuthor; //check
	protected int year;//check
	protected String aauthorYearLetter = ""; //for comparison with MeerKatBE
	
	protected String reviewTitle = "";
	

	protected String doi = "";
	
	protected String revManID;//check
	protected String[] references; //check

	protected DESIGN trialDesign = DESIGN.NOTAVAILABLE;////variables to do with trial design info
	private boolean crossoverTrial = false;
	private boolean paralellTrial = false;
	private boolean factorialTrial = false;
	private boolean otherDesign = false;
	protected String designProse = "";
	protected String designAddedInfo = "";
	
	protected String countries = "";
	protected String meerKatCountry = "";
	protected String countryProse = ""; // this string is going to be big. but in case a country is not added in the list or a city is added instead of a country we need to screen these one by one
	
	
	

	protected SETTING trialSetting = SETTING.NOTAVAILABLE;	//will hold the final value for setting in which this trial was conducted
	private boolean outP = false;	//Setting info can appear in various fields and be easily falsified by the occurrence of simple words such as "hospital". These boolean turn true if a very clear indication of the setting appears. If the final setting variable later differs from these clear indications, the setting variable will be adjusted.
	private boolean inP = false;
	private boolean bothP = false;
	private boolean emergencyR = false;
	private boolean psychiatricH = false;
	private boolean hospitalSetting = true; 
	
	protected String settingProse = "";
	
	
	

	private BLINDNESS blindingMethod = BLINDNESS.NOTAVAILABLE;//all about blinding
	protected String blindnessCleaned = BLINDNESS.NOTAVAILABLE.getDescription();
	

	protected String blindingProse = "";
	
	
	

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
	
	
	
	protected DichotomousOutcomeObject dobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list and re-filled with the next outcome
	protected ContinuousOutcomeObject cobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list and re-filled with the next outcome
	protected ReferenceObject refObject;/// for one reference, can be of different types. Procedure similar to outcomeObject. 
	protected List<ReferenceObject> referenceList = new ArrayList<>(); ///////List that will contain all referenceObjects


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////all the patterns for cleaning and extraction
	private int breakBiasVerification;
	private Matcher m;
	private Pattern design = Pattern.compile("([Dd]esign:)+");
	//parallel
	private Pattern parallelDesign = Pattern.compile("([pP]arallel)+|([Bb]etween\\s[Pp]atient(s)?)+|([Nn]on(-|\\s)[Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern parallelDesignCleaner = Pattern.compile("([Dd]esign)+|([pP]arallel)+|([Gg]roup(s)?)+|([Ss]tud(y|ies))?+|[:(),.]+");
	//cross over, crossed over, crossover, cross-over, crossed-over + caps variations
	private Pattern crossoverDesign = Pattern.compile("([Cc]ross(ed)?-?\\s?[Oo](ver))+");
	private Pattern crossoverDesignCleaner = Pattern.compile("([Dd]esign)+|([Cc]ross(ed)?-?\\s?[Oo](ver))+|[:(),.]+");
	//factorial, fully crossed + caps variation
	private Pattern factorialDesign = Pattern.compile("(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+");
	private Pattern factorialDesignCleaner = Pattern.compile("([Dd]esign)+|(([Ff]actorial)|([Ff]ully\\s[Cc]rossed))+|[:(),.]+");
	
	private Pattern inPatient = Pattern.compile("([Ii]n[-\\s]?\\s?((and\\s|or\\s|\\s?/\\s?)out)?patient(s)?)");//Setting patterns
	private Pattern outPatient = Pattern.compile("([Oo]ut[-\\s]?\\s?((and\\s|or\\s|\\s?/\\s?)in)?patient(s)?)");
	private Pattern emergencyRoom = Pattern.compile("([Ee]mergency\\s(rooms?|departments?))");
	private Pattern community = Pattern.compile("([Cc]ommunity|[Cc]ommunities)");
	private Pattern hospital = Pattern.compile("([Ii]n\\s)?([Hh]ospital(i[sz]ed)?)|([Mm]ental\\shealth\\scente?re?)|([Ii]ntensive\\scare)");
	private Pattern psychiatricHospital = Pattern.compile("([Pp]sychiatr(y|ic))");
	
	private Pattern blindness = Pattern.compile("([Bb]linding:)|([Bb]lindness:)|(([Dd]ouble|[Ss]ingle|[Tt]riple)\\s[Bb]lind:)|([Bb]lind:)");
	private Pattern doubleBlind = Pattern.compile("[Dd]ouble");
	private Pattern singleBlind = Pattern.compile("([Ss]ingle)");
	private Pattern tripleBlind = Pattern.compile("[Tt]riple");
	private Pattern quadrupleBlind = Pattern.compile("[Qq]uadruple");
	private Pattern openLabel = Pattern.compile("([Oo]pen)|([Nn]ot\\sblind(ed)?)|([Nn]on\\s?-?[Bb]lind(ing)?)|(:\\s?(no|none)\\Z)");
	private Pattern unclearBlinding = Pattern.compile("([Nn]ot\\sclear)|([Uu]nclear)");
	private Pattern notReported = Pattern.compile("([Nn]ot?\\s(specified|stated|report|reported|indicated|indication|described|description|available|information))");
	
	private Pattern countryPattern = Pattern.compile("(Locations?:)|([Cc]ountr(y|ies):)|([Ss]etting:)");
	
	//patterns for beginning/end of strings
	private Pattern beginningEndArray = Pattern.compile("^\\W+|[^\\w)]$"); //All special characters at beginning plus all special characters without closing brackets at end, because of factorial design
	private Pattern beginningEnd = Pattern.compile("^\\W+|\\W+$");	//All special chars at beginning and end
	private Pattern endPunctuationCleanerA = Pattern.compile("([^.!?\"']$)");	//matches when neither .!?"' at end of sentence. Needs 
	private Pattern endPunctuationCleanerB = Pattern.compile("([^!?.][\"'])$");	//to be applied after other punctuation regex. matches when there is no !?. before "'
	
	/////////////////////////////////////////////////////////////xml related attributes that are used between different methods in the big constructor
	private Element qualityItemsElement;
	Element studyElement;
	private NodeList qualityItemList;
	private NodeList referencesList;
	
	public TrialObject(Document review, int studyNumber){//////////the constructor that fills all attributes
					
					//Creates rootElement
					
					Element rootElement = null;
					try {
						NodeList rootList = review.getElementsByTagName("COCHRANE_REVIEW");
						Node rootNode = rootList.item(0);
						rootElement = (Element) rootNode;
						doi = rootElement.getAttribute("DOI");
						
					} catch (Exception e11) {
						e11.printStackTrace();
					}
					
					try {
						NodeList coverSheetList = rootElement.getElementsByTagName("COVER_SHEET");
						Element coverSheetElement = (Element) coverSheetList.item(0);
						
						NodeList titleList = coverSheetElement.getElementsByTagName("TITLE");
						Element titleElement = (Element) titleList.item(0);
						reviewTitle = titleElement.getTextContent().trim();
						System.out.println(reviewTitle);
					} catch (DOMException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
					///////extracts all info on biases that the review author added. The Risk is a three field option (High, low or unclear Risk), judgement of bias includes : justification or quotes from the trials
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
					
					String methodString = charMethodsElement.getTextContent().trim();
					
					System.out.println("BIG STRING: " + methodString);
					
					String[] newArray = splitMethods(methodString);
					
					String[] methodStringArray = methodString.split("(?=(\\.[A-Za-z*]))|\\.\\n"); // splits text at every full stop that is followed by a linebreak or directly by word character
					
					for (int k = 0; k<methodStringArray.length; k++){
						m = beginningEndArray.matcher(methodStringArray[k]);	//To make Strings neat. Sometimes they start with special chars, e.g. "."
						methodStringArray[k] = m.replaceAll("");
						//System.out.println(methodStringArray[k] );
						
						m = design.matcher(methodStringArray[k]);	//trial design is extracted in the following lines
						if (m.find()){	//Uses regex pattern to identify if this line is about design of trial
							designVerifyer(methodStringArray[k]);	
						}
						
						cleanSetting(methodStringArray[k]);
						cleanBlindness(methodStringArray[k]);
						getCountryProse(methodStringArray[k]);
						

						}
					
					
					
					//Extracts prose from participant section and tries to match
					NodeList charParticipantsList = studyToExtractElement.getElementsByTagName("CHAR_PARTICIPANTS");
					Node charParticipantsNode = charParticipantsList.item(0);
					Element charParticipantsElement = (Element) charParticipantsNode; 
					
					String participantString = charParticipantsElement.getTextContent();
					String[] participantStringArray = participantString.split("(?=(\\.[A-Za-z]))|\\.\\n");// splits text at every full stop that is followed by a linebreak or directly by word character
					
					for (int i = 0; i < participantStringArray.length; i++){
						m = beginningEndArray.matcher(participantStringArray[i]);	//To make Strings neat. Sometimes they start with special chars, e.g. "."
						participantStringArray[i] = m.replaceAll("");
						//System.out.println(participantStringArray[i] );
						cleanSetting(participantStringArray[i]);
						getCountryProse(participantStringArray[i]);
					}
					
					if (countryProse != null)
					countryProse = countryProse.trim(); //in case 2 prose strings are appended to each other there will be an unnecessary whitespace in the end. this one is trimmed away
					if (settingProse != null)
					settingProse = settingProse.trim();
					
					String[] extractedCountries;
					
					//Searches the big method String for country names. Writes them to String in alphabetical order and 
					//also creates output in MeerKatBE syntax
					String[] countryList = new String[] {"Afghanistan","Albania","Algeria","Andorra","Angola","Antigua and Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Basutoland","Belarus","Belgium","Belize","Benin","Bhutan","Bolivia","Bosnia and Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina Faso","Burma", "Burundi","Cambodia","Cameroon","Canada","Cabo Verde","China", "Central African Republic","Ceylon","Chad","Chile","Colombia","Comoros","Congo","Costa Rica","countries","Cote d'Ivoire","Croatia","Cuba","Curacao","Cyprus","Czechia","Czech Republic","Czechoslovakia","Denmark","Djibouti","Dominica","Dominican Republic","East Germany","East Pakistan","East Timor","Ecuador", "Egypt","El Salvador","Equatorial Guinea","Eritrea","Estonia","Ethiopia", "Europe", "Fiji","Finland","France","Gabon","Gambia","Georgia","Germany","Ghana","Greece","Grenada","Guatemala"," Guinea","Guinea-Bissau","Guyana","Haiti","Holy See","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran"," Iraq","Ireland","Israel","Italy","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Kiribati", "Kosovo","Korea","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia", "Madagascar","Malawi","Malaysia","Maldives","Mali", "Malta","Marshall Islands","Mauritania","Mauritius","Mexico","Micronesia","Moldova", "Monaco","Mongolia","Montenegro","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","New Zealand","Nicaragua", "Niger","Nigeria","Norway","Oman","other countries","Pakistan","Palau","Palestinian Territories","Panama","Papua New Guinea","Paraguay","Peru", "Puerto Rico","Philippines","Poland","Portugal","Qatar","Rhodesia","Romania","Rwanda","Russia","Saint Kitts and Nevis","Saint Lucia","Saint Vincent and the Grenadines","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Sikkim","Singapore","Sint Maarten","Slovakia","Slovenia","Solomon Islands","Somalia","South Africa","South Sudan","South Vietnam","Southwest Africa","Spain","Sri Lanka","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Tanganyika","Taiwan","Tajikistan","Tanzania"," Thailand"," Timor-Leste"," Togo"," Tonga","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Tuvalu","Uganda","UK", "Ukraine","UAE","Union of Soviet Socialist Republics","United Arab Emirates","United Arab Republic","United Kingdom","United States","Uruguay","USA","USSR","Uzbekistan","Vanuatu","Venezuela","Vietnam", "Western Samoa","West Germany","Yemen","Yugoslavia","Zaire","Zambia","Zanzibar","Zimbabwe"};
					
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
					//Extracts countries from "Participants" section of table. This info should be in Methods section above but we never know :) Sometimes it pops up here	
					
					if(countries.equals("")){
						
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
						e9.printStackTrace();
					}
					
					
//test
					
					
referenceExtracting(); //Extracts all information on references of this trial. See method referenceExtracting() below for more details. Puts info into array of strings that will be further analysed below
					
					for (int i = 0; i < references.length; i++){
						if (references[i].equals("JOURNAL_ARTICLE")){
							refObject = new JournalReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14; 	//its plus 14 because +1 is added at the end of the loop and this array contains new info to check on every 15th index
						} else if (references[i].equals("CONFERENCE_PROC")){
							refObject = new ConferenceReferenceObject(references, i);
							referenceList.add(refObject);
							
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("UNPUBLISHED")){
							refObject = new UnpublishedReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("OTHER")){
							refObject = new OtherReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("BOOK_SECTION")){
							refObject = new BookSectionReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("CORRESPONDENCE")){
							refObject = new CorrespondenceReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("BOOK")){
							refObject = new BookReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("COCHRANE_REVIEW")){
							refObject = new CochraneReviewReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						} else if (references[i].equals("COCHRANE_PROTOCOL")){
							refObject = new CochraneProtocolReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						}else if (references[i].equals("COMPUTER_PROGRAM")){
							refObject = new SoftwareReferenceObject(references, i);
							referenceList.add(refObject);
							//System.out.println(refObject.getClass());
							i = i + 14;
						}
					}
					
					//tries to extract year when study was conducted
					try {
						cache = studyElement.getAttribute("YEAR");
						year = Integer.parseInt(cache); //converts String to int
					} catch (NumberFormatException e8) {
						try {
							// if year can't be extracted as attribute it is tried via revman-ID. In case there is a hyphen or..., the characters resulting from this are eliminated and the String is restored to normal
							cache = revManID.replaceAll("(_x002d_)", "-").replace("(_x0026_)", "&").replaceAll("[^\\d.]", "");
							year = Integer.parseInt(cache); //Takes year of publication from ID
						} catch (NumberFormatException e) {
							try {
								//tries to take year of publication from additional references, e.g.intensive case management review, when there is absolutely no date entered because this trial is just the description of one centre in the study, or in in vocational training, where authors deleted the years from RevMan ID or possibly forgot to enter it
								cache = refObject.getDate().replaceAll("[^\\d.]", "");
								year = Integer.parseInt(cache);
							} catch (NumberFormatException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								// last resort
								e.printStackTrace();
								year = 0000;
							}
							
							
							
						}
					}
					
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Extracts OutcomeObjects for this trial
					
						System.out.println("Main author of Study: " + mainAuthor);
						//System.out.println(trialSetting.getContent());
						//System.out.println(countries);
						//System.out.println(blindingMethod.getContent());
						//System.out.println(countries);
					
					//treverses xml to find data section
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
						
						
						try {////////////looks for dichotomous outcomes by their tag name. Elements are created and used as parameters for object creation. All
							//dichotomous outcome objects are traversed and it is checked if their ID equals the revman ID of the trial which outcomes 
							//are supposed to extracted in this call. All outcomes are added to the object list
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
											
											dobj = new DichotomousOutcomeObject(dichDataElement, comparisonNameElement, dichOutcomeNameElement, dichOutcomeElement, dichSubgroupElement);
											outcomeList.add(dobj);
											//System.out.println("Outcome added to list");
										}
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							NodeList contOutcomeList = comparisonElement.getElementsByTagName("CONT_OUTCOME");
							for (int l = 0; l < contOutcomeList.getLength(); l++){
								Node contOutcomeNode = contOutcomeList.item(l);
								Element contOutcomeElement = (Element) contOutcomeNode;
								
								NodeList contOutcomeNameList = contOutcomeElement.getElementsByTagName("NAME");
								Node contOutcomeNameNode = contOutcomeNameList.item(0);
								Element contOutcomeNameElement = (Element) contOutcomeNameNode;
								
								
								NodeList contSubgroupList = contOutcomeElement.getElementsByTagName("CONT_SUBGROUP");
								for (int j = 0; j <contSubgroupList.getLength(); j++){
									Node contSubgroupNode = contSubgroupList.item(j);
									Element contSubgroupElement = (Element) contSubgroupNode;
									
									NodeList contDataList = contSubgroupElement.getElementsByTagName("CONT_DATA");
									for (int k = 0; k < contDataList.getLength(); k++){
										Node contDataNode = contDataList.item(k);
										Element contDataElement = (Element) contDataNode;
										
										if (contDataElement.getAttribute("STUDY_ID").equals(revManID)){
											
											cobj = new ContinuousOutcomeObject(contDataElement, comparisonNameElement, contOutcomeNameElement, contOutcomeElement, contSubgroupElement);
											outcomeList.add(cobj);
											//System.out.println("Outcome added to list");
										}
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
					//aauthorYearLetter = mainAuthor+year+yearLetter;
					//System.out.println("Setting");
					//System.out.println(settingProse);
					System.out.println();
					
					
					
					
	}
	
	private String[] splitMethods (String str) {
		String[] forReturn = str.split("(?=([Bb]linding:)|([Bb]lindness:)|(([Dd]ouble|[Ss]ingle|[Tt]riple)\\s[Bb]lind:)|([Bb]lind:))|(?=(Duration:))|(?=(Design:))", 4);
		
		for (int i = 0; i<forReturn.length;i++) {
			System.out.println(forReturn[i]);
		}
			
		
		return forReturn;
	}
	
	 private void cleanSetting(String prose){
		 
		 if (prose.contains("Setting:")| prose.contains("Location:")| prose.contains("Design:")){
			 
			 settingProse = settingProse + prose + ". ";
			 
			 m = emergencyRoom.matcher(prose);//looks for emergency room
			 if (m.find() && outP == false && inP == false && bothP == false){//only allows to take the value emergency room setting if it was not previously specified that patients are in or outpatients
				 trialSetting = SETTING.EMERGENCY;
				 emergencyR = true;
			 }
			 
			 m = psychiatricHospital.matcher(prose); //looks for psychiatric hospital type of trial setting
			 if (m.find() && outP == false && inP == false && bothP == false){
				 trialSetting = SETTING.PSYCHIATRY;
				 psychiatricH = true;
			 }
			 
			 m = hospital.matcher(prose);//looks for hospital setting. If conflicting info came up before (eg if it said inpatient setting before and this string just states hospital name) this pure hospital setting gets lower priority than eg. psychiatric hospital. Otherwise, the mention of "psychiatric hospital" would end up being classified as hospital only
			 if (m.find() && emergencyR  == false && psychiatricH == false && outP == false && inP == false && bothP == false){	//to avoid emergency room information to be overwritten when hospital name appears - emergency rooms are naturally located in hospitals anyway
				 trialSetting = SETTING.HOSPITAL;
				 hospitalSetting = true;
			 }
			 
			 m = community.matcher(prose);//looks for community setting. has lower priority that hospital because hospitals can be named as community hospitals. Community settting therefore applies only as long as hospital boolean is false
			 if (m.find()&& hospitalSetting == false && outP == false && inP == false && bothP == false){
				 trialSetting = SETTING.COMMUNITY;
			 }
			 
			 m = outPatient.matcher(prose);//looks for outpatients
			 if (m.find() || outP == true){	// if a clear reference to in, out, or mixed setting was found before, and this is the nth time this method was called, or because it said things like "hospital inpatients, admitted through emergency room", the following booleans make sure that the right value is re-written to the setting variable.
				 trialSetting = SETTING.OUTPATIENT;
				 outP = true;
			 }
			 
			 m = inPatient.matcher(prose);//looks if this prose is about inpatiens
			 if (m.find() || inP == true ){
				 if (outP == true || bothP == true){	//outP was set true before if outpatients came up
					 trialSetting = SETTING.INANDOUT;
					 bothP = true;
				 } else {
					 trialSetting = SETTING.INPATIENT;//it did not find outpatients, so the population consists only of inpatients
					 inP = true;
				 }
			 }
			 
			 
		 }
	 }
	
	 
	private void getCountryProse(String str){
		m = countryPattern.matcher(str);
		if (m.find()){
			countryProse = countryProse + str + ". ";
			
		}
	}
	 
	private void referenceExtracting(){
		//This following for-loop runs through all references for the included study and saves relevant fields into an array that is created to hold information in the following positions:
		//0th,  ..index -> Type of publication, eg. Journal ->"TYPE"
		//1st..., ..index -> Primary attribute: Yes or No ->"PRIMARY"
		//2nd... ->Names of all authors of this publication ->"AU"
		//3rd...->Title of publication->"TI"
		//4th...->Name of Journal->Journal->"SO"
		//5...-> Year published->"YR"
		//6...->Volume ->"VL" 
		//7...-> Issue ->"NO"
		//8...-> Pages ->"PG"
		
		//9: TO original name
		//10: EN: edition
		//11: ED Editor
		//12: PB Publisher
		//13: CY City
		//14: MD medium
		
		//if a field is not available, eg. if the reference refers to a conference protocol that lacks page numbers, empty "" space is inserted and next position of array is tried to be filled
	
		
		referencesList = studyElement.getElementsByTagName("REFERENCE");
		int numberReferences = referencesList.getLength();
		references = new String[numberReferences * 15];
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
				e4.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element tiElement = null;
			try {
				NodeList tiList = referenceElement.getElementsByTagName("TI");
				Node tiNode = tiList.item(0);
				tiElement = (Element) tiNode;
			} catch (Exception e4) {
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
				e3.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element soElement = null;
			try {
				NodeList soList = referenceElement.getElementsByTagName("SO");
				Node soNode = soList.item(0);
				soElement = (Element) soNode;
			} catch (Exception e3) {
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
				e2.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element yrElement = null;
			try {
				NodeList yrList = referenceElement.getElementsByTagName("YR");
				Node yrNode = yrList.item(0);
				yrElement = (Element) yrNode;
			} catch (Exception e2) {
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
				e1.printStackTrace();
			}
			
			arrayCounter++;
			
			
			Element vlElement = null;
			try {
				NodeList vlList = referenceElement.getElementsByTagName("VL");
				Node vlNode = vlList.item(0);
				vlElement = (Element) vlNode;
			} catch (Exception e1) {
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
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element noElement = null;
			try {
				NodeList noList = referenceElement.getElementsByTagName("NO");
				Node noNode = noList.item(0);
				noElement = (Element) noNode;
			} catch (Exception e1) {
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
				e.printStackTrace();
			}
			
			arrayCounter++;
			
			Element pgElement = null;
			try {
				NodeList pgList = referenceElement.getElementsByTagName("PG");
				Node pgNode = pgList.item(0);
				pgElement = (Element) pgNode;
			} catch (Exception e1) {
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
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element toElement = null;
			try {
				NodeList toList = referenceElement.getElementsByTagName("TO");
				Node toNode = toList.item(0);
				toElement = (Element) toNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (toElement != null){
				references[arrayCounter] = toElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element enElement = null;
			try {
				NodeList enList = referenceElement.getElementsByTagName("EN");
				Node enNode = enList.item(0);
				enElement = (Element) enNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (enElement != null){
				references[arrayCounter] = enElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element edElement = null;
			try {
				NodeList edList = referenceElement.getElementsByTagName("ED");
				Node edNode = edList.item(0);
				edElement = (Element) edNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (edElement != null){
				references[arrayCounter] = edElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element pbElement = null;
			try {
				NodeList pbList = referenceElement.getElementsByTagName("PB");
				Node pbNode = pbList.item(0);
				pbElement = (Element) pbNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (pbElement != null){
				references[arrayCounter] = pbElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element cyElement = null;
			try {
				NodeList cyList = referenceElement.getElementsByTagName("CY");
				Node cyNode = cyList.item(0);
				cyElement = (Element) cyNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (cyElement != null){
				references[arrayCounter] = cyElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
			Element mdElement = null;
			try {
				NodeList mdList = referenceElement.getElementsByTagName("MD");
				Node mdNode = mdList.item(0);
				mdElement = (Element) mdNode;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				if (mdElement != null){
				references[arrayCounter] = mdElement.getTextContent().replaceAll("\n", "").trim();
				//System.out.println(pgElement.getTextContent().replaceAll("\n", "").trim());
				} else {
					references[arrayCounter] = "";
				}
			} catch (DOMException e) {
				e.printStackTrace();
			}
			arrayCounter++;
			
		}
	}
	
private void cleanBlindness(String str){//looks which kind of blinding methods were extracted for this trial
		
	
		m = blindness.matcher(str);//checks if this String is about blindness at all
		
		
		boolean doubt = false;
		if (performanceBiasRisk.contains("High")) {//indicates, that authors had doubts about trial methodology
			doubt = true;
		}
		
		
		if (m.find()){//this big if-construct attempts to determine which kind of blindness is present
			blindingProse = str;
			
			
			
			
			m = doubleBlind.matcher(str);//looks for indication that the trial is double-blind
			if (m.find()){
				if (doubt) {
					blindingMethod = BLINDNESS.BIASEDDOUBLE;//review authors had significant doubts about this trial, therefore the value becomes unclear
					blindnessCleaned = blindingMethod.getDescription(); //this recurring line fills the cleaned String that is written into the database. Content of this String is defined in the BLINDNESS enum.
				} else {
					blindingMethod = BLINDNESS.DOUBLE;//the pattern matched so this trial is classified as double-blind
					blindnessCleaned = blindingMethod.getDescription(); //this recurring line fills the cleaned String that is written into the database. Content of this String is defined in the BLINDNESS enum.
				}
				
			} else {//the pattern did not match so other options are explored. Blindness options are checked in order of decreasing likelihood.
				m = singleBlind.matcher(str);
				if (m.find()){
					if (doubt) {
						blindingMethod = BLINDNESS.BIASEDSINGLE;
						blindnessCleaned = blindingMethod.getDescription(); 
					} else {
						blindingMethod = BLINDNESS.SINGLE;
						blindnessCleaned = blindingMethod.getDescription(); 
					}
					
					} else {
						m = openLabel.matcher(str);
						if (m.find()){
							if (doubt) {
								blindingMethod = BLINDNESS.BIASEDOPEN;
								blindnessCleaned = blindingMethod.getDescription(); 
							} else {
								blindingMethod = BLINDNESS.OPENTRIAL;
								blindnessCleaned = blindingMethod.getDescription(); 
							}
							
						} else {
							m = tripleBlind.matcher(str);
							if (m.find()){
								if (doubt) {
									blindingMethod = BLINDNESS.BIASEDTRIPLE;
									blindnessCleaned = blindingMethod.getDescription(); 
								} else {
									blindingMethod = BLINDNESS.TRIPLE;
									blindnessCleaned = blindingMethod.getDescription(); 
								}
								
							} else {
								m = quadrupleBlind.matcher(str);
								if (m.find()){
									if (doubt) {
										blindingMethod = BLINDNESS.BIASEDQUADRUPLE;
										blindnessCleaned = blindingMethod.getDescription(); 
									} else {
										blindingMethod = BLINDNESS.QUADRUPLE;
										blindnessCleaned = blindingMethod.getDescription(); 
									}
									
								} else {
									m = notReported.matcher(str);
									if (m.find()) {
										blindingMethod = BLINDNESS.NOTREPORTED;
										blindnessCleaned = blindingMethod.getDescription(); 
									} else {
										m = unclearBlinding.matcher(str);
										if (m.find()) {
											blindingMethod = BLINDNESS.UNCLEAR;
											blindnessCleaned = blindingMethod.getDescription();
										} else {
											blindingMethod = BLINDNESS.OTHER;
											blindnessCleaned = blindingMethod.getDescription(); 
										}
										
									} 
									
								}
							}
						}
					}
				}
			}
		}
		

		
	private void designVerifyer(String str){
		
		//To see if this String contains info on trial design
		m = parallelDesign.matcher(str); 	//Uses regex pattern for identifying parallel trials
		if (m.find()){	//To see if this trial is a parallel trial. Returns true if the trial is parallel
			paralellTrial = true;	//Boolean to store that this trial is parallel
			trialDesign = DESIGN.PARALLEL;
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
				trialDesign = DESIGN.CROSSOVER;
				designProse = str.trim();
				//System.out.println("DesignProse: " + designProse);
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
					trialDesign = DESIGN.FACTORIAL;
					designProse = str.trim();
					//System.out.println("DesignProse: " + designProse);
					m = factorialDesignCleaner.matcher(str);
					designAddedInfo = m.replaceAll("").trim();
					m = beginningEnd.matcher(designAddedInfo);
				
					designAddedInfo = m.replaceAll("");
					if (designAddedInfo.length() != 0)
							designAddedInfo= designAddedInfo.substring(0, 1).toUpperCase() + designAddedInfo.substring(1);
					
					
	//			System.out.println("Design: factorial" + ". PROSE DESIGN: " + designProse);
				} else {
					otherDesign = true;
					trialDesign = DESIGN.OTHER;
					designProse = str.trim();
					//System.out.println("DesignProse: " + designProse);
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
	
	public String getMainAuthor() {
		return mainAuthor;
	}



	public void setMainAuthor(String mainAuthor) {
		this.mainAuthor = mainAuthor;
	}



	public int getYear() {
		return year;
	}



	public void setYear(int year) {
		this.year = year;
	}



	public String getAuthorYearLetter() {
		return aauthorYearLetter;
	}



	public void setAuthorYearLetter(String authorYearLetter) {
		this.aauthorYearLetter = authorYearLetter;
	}



	

	public String getRevManID() {
		return revManID;
	}



	public void setRevManID(String revManID) {
		this.revManID = revManID;
	}


@XmlElement
	public String[] getReferences() {
		return references;
	}



	public void setReferences(String[] references) {
		this.references = references;
	}



	



	public String getDesignProse() {
		return designProse;
	}



	public void setDesignProse(String designProse) {
		this.designProse = designProse;
	}



	public String getDesignAddedInfo() {
		return designAddedInfo;
	}



	public void setDesignAddedInfo(String designAddedInfo) {
		this.designAddedInfo = designAddedInfo;
	}



	public String getCountries() {
		return countries;
	}



	public void setCountries(String countries) {
		this.countries = countries;
	}



	public String getSelectionBiasRandomSequenceJudgement() {
		return selectionBiasRandomSequenceJudgement;
	}



	public void setSelectionBiasRandomSequenceJudgement(String selectionBiasRandomSequenceJudgement) {
		this.selectionBiasRandomSequenceJudgement = selectionBiasRandomSequenceJudgement;
	}



	public String getSelectionBiasRandomSequenceBiasRisk() {
		return selectionBiasRandomSequenceBiasRisk;
	}



	public void setSelectionBiasRandomSequenceBiasRisk(String selectionBiasRandomSequenceBiasRisk) {
		this.selectionBiasRandomSequenceBiasRisk = selectionBiasRandomSequenceBiasRisk;
	}



	public String getSelectionBiasAllocationConcealmentBiasRisk() {
		return selectionBiasAllocationConcealmentBiasRisk;
	}



	public void setSelectionBiasAllocationConcealmentBiasRisk(String selectionBiasAllocationConcealmentBiasRisk) {
		this.selectionBiasAllocationConcealmentBiasRisk = selectionBiasAllocationConcealmentBiasRisk;
	}



	public String getSelectionBiasAllocationConcealmentJudgement() {
		return selectionBiasAllocationConcealmentJudgement;
	}



	public void setSelectionBiasAllocationConcealmentJudgement(String selectionBiasAllocationConcealmentJudgement) {
		this.selectionBiasAllocationConcealmentJudgement = selectionBiasAllocationConcealmentJudgement;
	}



	public String getPerformanceBiasRisk() {
		return performanceBiasRisk;
	}



	public void setPerformanceBiasRisk(String performanceBiasRisk) {
		this.performanceBiasRisk = performanceBiasRisk;
	}



	public String getPerformanceBiasJudgement() {
		return performanceBiasJudgement;
	}



	public void setPerformanceBiasJudgement(String performanceBiasJudgement) {
		this.performanceBiasJudgement = performanceBiasJudgement;
	}



	public String getDetectionBiasRisk() {
		return detectionBiasRisk;
	}



	public void setDetectionBiasRisk(String detectionBiasRisk) {
		this.detectionBiasRisk = detectionBiasRisk;
	}



	public String getDetectionBiasJudgement() {
		return detectionBiasJudgement;
	}



	public void setDetectionBiasJudgement(String detectionBiasJudgement) {
		this.detectionBiasJudgement = detectionBiasJudgement;
	}



	public String getAttritionBiasRisk() {
		return attritionBiasRisk;
	}



	public void setAttritionBiasRisk(String attritionBiasRisk) {
		this.attritionBiasRisk = attritionBiasRisk;
	}



	public String getAttritionBiasJudgement() {
		return attritionBiasJudgement;
	}



	public void setAttritionBiasJudgement(String attritionBiasJudgement) {
		this.attritionBiasJudgement = attritionBiasJudgement;
	}



	public String getReportingBiasRisk() {
		return reportingBiasRisk;
	}



	public void setReportingBiasRisk(String reportingBiasRisk) {
		this.reportingBiasRisk = reportingBiasRisk;
	}



	public String getReportingBiasJudgement() {
		return reportingBiasJudgement;
	}



	public void setReportingBiasJudgement(String reportingBiasJudgement) {
		this.reportingBiasJudgement = reportingBiasJudgement;
	}



	public String getOtherBiasRisk() {
		return otherBiasRisk;
	}



	public void setOtherBiasRisk(String otherBiasRisk) {
		this.otherBiasRisk = otherBiasRisk;
	}



	public String getOtherBiasJudgement() {
		return otherBiasJudgement;
	}



	public void setOtherBiasJudgement(String otherBiasJudgement) {
		this.otherBiasJudgement = otherBiasJudgement;
	}



	



	public String getMeerKatCountry() {
		return meerKatCountry;
	}



	public void setMeerKatCountry(String meerKatCountry) {
		this.meerKatCountry = meerKatCountry;
	}


	public void setOutcomeList(List<OutcomeObject> outcomeList) {
		this.outcomeList = outcomeList;
	}
/////////////////////////////////complicated ones

	public ReferenceObject getRefObject() {
		return refObject;
	}


	public void setRefObject(ReferenceObject refObject) {
		this.refObject = refObject;
	}
	
	
	public List<ReferenceObject> getReferenceList() {
		return referenceList;
	}


	public void setReferenceList(List<ReferenceObject> referenceList) {
		this.referenceList = referenceList;
	}


	public ContinuousOutcomeObject getCobj() {
		return cobj;
	}


	public void setCobj(ContinuousOutcomeObject cobj) {
		this.cobj = cobj;
	}


	public DichotomousOutcomeObject getDobj() {
		return dobj;
	}

	public void setDobj(DichotomousOutcomeObject dobj) {
		this.dobj = dobj;
	}

	protected List<OutcomeObject> outcomeList = new ArrayList<>();///array list that will contain all outcomes and their data
	
	@XmlElement(name = "OUTCOME")
	public List<OutcomeObject> getOutcomeList() {
		return outcomeList;
	}
	public SETTING getSetting() {
		return trialSetting;
	}


	public void setSetting(SETTING setting) {
		this.trialSetting = setting;
	}
	public DESIGN getTrialDesign() {
		return trialDesign;
	}


	public void setTrialDesign(DESIGN trialDesign) {
		this.trialDesign = trialDesign;
	}
	public BLINDNESS getBlindingMethod() {
		return blindingMethod;
	}


	public void setBlindingMethod(BLINDNESS blindingMethod) {
		this.blindingMethod = blindingMethod;
	}
	public String getCountryProse() {
		return countryProse;
	}

	public void setCountryProse(String countryProse) {
		this.countryProse = countryProse;
	}
	
	public String getSettingProse() {
		return settingProse;
	}

	public void setSettingProse(String settingProse) {
		this.settingProse = settingProse;
	}
	public String getBlindingProse() {
		return blindingProse;
	}

	public void setBlindingProse(String blindingProse) {
		this.blindingProse = blindingProse;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getBlindnessCleaned() {
		return blindnessCleaned;
	}

	public void setBlindnessCleaned(String blindnessCleaned) {
		this.blindnessCleaned = blindnessCleaned;
	}
}
