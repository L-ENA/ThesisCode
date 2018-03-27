# ThesisCode
Parses XML, extracts and cleans data on trials (all included trials with all their outcomes and references), creates new, accessible XML to be used for Excel/Access/other database

Classes that contain data are:

TrialObject
-contains info of a single trial, all its outcomes as objects in an outcome list and all references as objects in reference list 

Outcome objects
-different classes for continuous, dichotomous, and other less frequent outcome types. Outcomes are stored on outcomes list of their trial object

Reference objects
- different classes that contain infos on type and reference data. Stored in the respective trial object.
