package scenarioParser;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import model.Scenario;

public class SParser {

	private static String scenarioXSD = "res/xsd/scenario.xsd";
	private static String model = "model";
	
	private Scenario scenarioUnmarshall(File scenarioFile) {
		Scenario scenario = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(model);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(scenarioXSD));
			unmarshaller.setSchema(schema);
			scenario = (Scenario) unmarshaller.unmarshal(scenarioFile);
		} catch (Exception e) {
			throw new ScenarioException(e.getMessage());
		}
		return scenario;
	}
	
	public void printScenario(Scenario scenario) {
		System.out.println(scenario.toString());
	}
	
	public Scenario parseScenario(String scenarioName) {
		File scenarioFile = new File(scenarioName);
		Scenario scenario = null;
		if(scenarioFile != null && !scenarioFile.isDirectory()) {		
			if(isXMLFile(scenarioFile)) {
				scenario = scenarioUnmarshall(scenarioFile);
				printScenario(scenario);
			} else {
				throw new ScenarioException("ERROR - " + scenarioName + " is not an XML file");
			}
		} else {
			throw new ScenarioException("ERROR - file " + scenarioName + " does not exist");
		}
		return scenario;
	}
	
	private boolean isXMLFile(File scenarioFile) {
		String name = scenarioFile.getName();
		return name.matches("([A-Za-z0-9])+.xml");
	}
}
