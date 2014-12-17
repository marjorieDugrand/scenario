import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import model.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import scenarioParser.SParser;
import scenarioParser.ScenarioException;


public class ScenarioParserTest {

	private SParser scenarioParser;
	
	@Before
	public void setUp() {
		scenarioParser = new SParser();
	}
	
	@Test(expected=ScenarioException.class)
	public void fileDoesNotExist() {
		Scenario scenario = null;
		String scenarioName = "res/notExistingFile.txt";
		try {
			scenario = scenarioParser.parseScenario(scenarioName);
		} catch(RuntimeException exception) {
			assertEquals("ERROR - " + scenarioName + " does not exist", exception.getMessage());
			assertNull(scenario);
			throw new ScenarioException(exception);
		}
	}
	
	@Test(expected=ScenarioException.class)
	public void inputIsNotAnXMLFile() {
		Scenario scenario = null;
		String scenarioName = "res/notXMLInput.txt";
		try {
			scenario = scenarioParser.parseScenario(scenarioName);
		} catch(RuntimeException exception) {
			assertEquals("ERROR - " + scenarioName + " is not an XML file", exception.getMessage());
			assertNull(scenario);
			throw new ScenarioException(exception);
		}
	}
	
	@Test
	public void inputIsValid() {
		String scenarioName = "res/xml/scenario1.xml";
		Scenario scenario = scenarioParser.parseScenario(scenarioName);
		assertNotNull(scenario);
		Scenario expectedScenario = buildExpectedScenario1();
		assertTrue(expectedScenario.equals(scenario));
	}
	
	@Test(expected=ScenarioException.class)
	public void scenarioIsIllFormated() {
		Scenario scenario = null;
		try {
			String scenarioName = "res/xml/notValidScenario.xml";
			scenario = scenarioParser.parseScenario(scenarioName);
		} catch(ScenarioException exception) {
			assertNull(scenario);
			throw new ScenarioException(exception);
		}
	}

	private Scenario buildExpectedScenario1() {
		Scenario scenario = new Scenario();
		TimeT duration = new TimeT();
		duration.setValue(BigInteger.valueOf(155));
		duration.setTimeUnit(TimeUnitType.MS);
		scenario.setDuration(duration);
		
		Producers producers = new Producers();
		List<ProducerT> producersList = new ArrayList<ProducerT>();
		ProducerT producer1 = createProducer("p1", "producer1", SizeUnitType.BYTES, 40);
		ProducerT producer2 = createProducer("p2", "producer2", SizeUnitType.BYTES, 50);
		producersList.add(producer1);
		producersList.add(producer2);
		
		producers.setProducers(producersList);
		scenario.setProducers(producers);
		
		List<BehaviourT> behaviours1 = new ArrayList<BehaviourT>();
		BehaviourT behaviour1 = createBehaviour(1, 10, 45, 125, 1);
		behaviours1.add(behaviour1);
		
		List<BehaviourT> behaviours2 = new ArrayList<BehaviourT>();
		BehaviourT behaviour2 = createBehaviour(1, 10, 12, 20, 5);
		behaviours2.add(behaviour2);
		
		Consumers consumers = new Consumers();
		List<ConsumerT> consumersList = new ArrayList<ConsumerT>();
		ConsumerT consumer1 = createConsumer("c1", "consumer1", producer1, behaviours1);
		ConsumerT consumer2 = createConsumer("c2", "consumer2", producer2, behaviours2);

		consumersList.add(consumer1);
		consumersList.add(consumer2);
		
		consumers.setConsumers(consumersList);
		scenario.setConsumers(consumers);

		return scenario;
	}
	
	private ProducerT createProducer(String id, String name, SizeUnitType type, long datasizeValue) {
		ProducerT producer = new ProducerT();
		producer.setId(id);
		producer.setName(name);
		DatasizeT datasize = new DatasizeT();
		datasize.setSizeUnit(type);
		datasize.setValue(BigInteger.valueOf(datasizeValue));
		producer.setDatasize(datasize);
		return producer;
	}
	
	private BehaviourT createBehaviour(long begin, long end, long frequency, long dValue, long pTime) {
		BehaviourT behaviour = new BehaviourT();
		behaviour.setTimeUnit(TimeUnitType.MS);
		behaviour.setBegin(BigInteger.valueOf(begin));
		behaviour.setEnd(BigInteger.valueOf(end));
		behaviour.setFrequency(BigInteger.valueOf(frequency));
		DatasizeT datasize = new DatasizeT();
		datasize.setValue(BigInteger.valueOf(dValue));
		datasize.setSizeUnit(SizeUnitType.BYTES);
		behaviour.setDatasize(datasize);
		TimeT processingTime = new TimeT();
		processingTime.setTimeUnit(TimeUnitType.MS);
		processingTime.setValue(BigInteger.valueOf(pTime));
		behaviour.setProcessingTime(processingTime);
		return behaviour;
	}
	
	private ConsumerT createConsumer(String id, String name, ProducerT producer, List<BehaviourT> behaviours) {
		ConsumerT consumer = new ConsumerT();
		consumer.setId(id);
		consumer.setProducerId(producer);
		consumer.setName(name);
		consumer.setBehaviours(behaviours);
		return consumer;
	}
}
