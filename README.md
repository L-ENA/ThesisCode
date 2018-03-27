# ThesisCode
Parses XML, extracts and cleans data on trials (all included trials with all their outcomes and references), creates new, accessible XML to be used for Excel/Access/other database

Classes that contain data are:

TrialObject
-contains info of a single trial, all its outcomes as objects in an outcome list and all references as objects in reference list 

attributes:

  protected String mainAuthor; Name of main author
	protected int year; year the study was published
	protected String aauthorYearLetter = ""; for comparison with MeerKatBE
	
	protected String revManID;//ID that ReviewManager uses to refer to the trial
	protected String[] references; Array that contains all references as single Strings, does probably not need to be protected

	protected boolean crossoverTrial = false; Is it a crossover trial? Info on trial design
	protected boolean paralellTrial = false; info on trial design
	protected boolean factorialTrial = false; see above
	protected boolean otherDesign = false; see above
	protected String designProse; this String contains extracted information on trial design before it was analysed and written to cleaned    variables
	protected String designAddedInfo;sometimes, the prose design String contains additional info. this will be, if sucessful, stored in     here
	protected String countries = ""; sll countries or the country where this trial was conducted, in alphabetic order
	
	protected String selectionBiasRandomSequenceJudgement; Usually contains quotes that the review author extracted from the trial to       justify their judgement of bias on this item
	protected String selectionBiasRandomSequenceBiasRisk; Can be High, Low or Unclear Risk
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
	
	protected int nrOfParticipants;Total number of participants in this trial. Numbers of analysed participants for each outcome can vary
	protected String meerKatCountry; This displays country list in a diferent format so that its comparable to the MeerKatDB
  
  protected DichotomousOutcomeObject dobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list   and re-filled with the next outcome
  protected ContinuousOutcomeObject cobj;//object that contains data of one outcome. It will be immediately dumped in the outcome list     and re-filled with the next outcome
  protected Object refObject;/// for one reference, can be of different types. Procedure similar to outcomeObject. g and s
	
	protected List<Object> referenceList = new ArrayList<>(); ///////List that will contain all referenceObjects , 
	protected List<Object> outcomeList = new ArrayList<>();///array list that will contain all outcome objects and their data
  
 
