1. About
	The script create_java.py creates a java class using parametered sparql queries files.

2. Usage
	python create_java.py <ontology_name> <ontology_path> <requests_path>

	Where:
		<ontology_name>: name of the ontology to which the requests make reference
		<ontology_path>: path to said ontology
		<requests_path>: folder in which the requests are stored 

	Example:
		python create_java.py IoTF2B506 ../Ontology/UV506Gr2.owl ./SparqlQuerys


3. Requirements
	Each request file must follow the convention stated below to descrive its contents:
		
		3.1 Statement for the file:
			Each file must contain the #File statement descriving the file in the following format 

				#File: <name_of_the_file> -- <description>
			
			Example:
				#File: SensorReference_Create -- Creates a sensor reference in the database


		3.2 Statement for the parameters:
			For each different parameter of the file a #Parameter statement is required
				
				#Parameter: <name_of_the_parameter> -- <description of the parameter> -- <type> -- <optionallity> -- <default_value>
			
				<type>: M for MANDATORY anything else for OPTIONNAL
				<default_value>: when type is OPTIONNAL contains the defailt value, can be ommited if <type> == M

			Example:
				#Parameter: DataSheet -- Reference value of the sensor -- String -- O -- default
				#Parameter: DataSheet -- Reference value of the sensor -- String -- M

	Notes:
		Each the statements must be written in a separate line, without empty lines between them.
		The first statement must be in the first line of the document.
		There should not be any spaces between the begining of the line and the # character.
		The good functioning of the program is not guaranteed if the numer of whitespaces between each element of a statement is not respected.
		Do not create a file or parameter starting with a non-alphabetic

4. Example
	This section contains a full sparql query file with the annotations according to this conventions

	SensorReference_Create.sparql:

		#File: SensorReference_Create -- Creates a datasheet
		#Parameter: DataSheet -- Reference value of the sensor -- String -- M
		#Parameter: DataSheetReference -- Reference Code of the sensor -- String -- M

		PREFIX db:<http://projet404.edu/data_base#>
		PREFIX ds:<http://www.semanticweb.org/samya/ontologies/DataSheet.owl#>
		PREFIX dul:<http://www.loa.istc.cnr.it/ontologies/DUL.owl#>
		PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
		PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>

		INSERT {
		  #Instantiations
		  db:%%DataSheet%% rdf:type ds:SensorDataSheet.
		  db:%%DataSheet%%Name rdf:type ds:NameOfReference. #can ommit this
		  
		  #Set the name to the datasheet
		  db:%%DataSheet%% ds:hasNameOfReference db:%%DataSheet%%Name.
		  db:%%DataSheet%%Name dul:hasDataValue  "%%DataSheetReference%%"^^xsd:string #can omit this
		}
		WHERE {
		}
