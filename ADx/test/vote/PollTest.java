package vote;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import shell.AdaptationOptionsFrame;

public class PollTest {
	
	// system under test
	static Poll sut = new Poll();  // creates an empty vector of votes
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// arrange poll
		// load options
		
		try{
			File choice = new File("related/Greater_Banjul_Area.xml");
			// This builds a document of whatever's in the given resource
			SAXBuilder builder = new SAXBuilder();
			// the argument to the builder is any url. Here it is passed the file instance
			Document doc = builder.build(choice);
			//AdaptationOptionsFrame aof = new AdaptationOptionsFrame(doc);
			sut.setAdaptationOptionsModel(doc);
		}
		catch (IOException ioe) {
		      System.err.println(ioe);
		}
		catch (JDOMException jde) {
		      System.err.println(jde);
		}
		
		sut.setNb_ranks(3);  // number to be ranked - param 2
		
		for (int i=0;i<4;i++)  // assuming 4 voters - param 0
		{
			Vote<String> tv = new Vote<String>(5,""); //assuming 5 options - param 1
			tv.setNameString("test-voter-" + i);
			
			String [] votes = {"1","2","3","",""};  // corresponding votes
			Collections.shuffle(Arrays.asList(votes));
			for (int j=0;j<5;j++) {
				tv.set(j, votes[j]);	
			}
			Vote<String> finalVote = new Vote (tv, ""); // use copy constructor
			finalVote.setNameString(tv.getNameString()); // for some reason it doesnt copy the name
			sut.addVote(finalVote);
		}
		
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		sut = null;
	}

	@Before
	public void setUp() throws Exception {
		// test fixture - all things that must be in place to run a test
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	@Ignore
	public void testAddVote() {
		
		// set up new vote
		int nbr = sut.getNb_ranks(); 
		String [] votes = new String [5];
		Vote<String> tv = new Vote<String>(5,"");
		
		for (int j=0;j<5;j++)
		{
			if (j<nbr)votes[j] = String.valueOf(j);
			else votes[j] = "";
			
		}
		Collections.shuffle(Arrays.asList(votes));
		for (int k=0; k<5; k++){
			tv.set(k, votes[k]);
			//Vote<String> v : votes[]) 
		}
		
		Vote<String> finalVote = new Vote (tv, ""); // use copy constructor
		sut.addVote(finalVote);
		assertEquals("5 votes counted in poll",5,sut.getNb_votes());
		
		
		//fail("Not yet implemented");
	}

	
	public void testRemoveVote() {
		fail("Not yet implemented");
	}

	
	public void testPoll() {
		fail("Not yet implemented");
	}

	
	public void testGetVotes() {
		fail("Not yet implemented");
	}

	
	public void testGetVoteByNumber() {
		fail("Not yet implemented");
	}

	
	public void testSetVotes() {
		fail("Not yet implemented");
	}

	//@Test
	public void testGetNb_votes() {
		assertEquals("4 votes counted in poll",4,sut.getNb_votes());
		//fail("Not yet implemented");
	}

	
	public void testSetNb_votes() {
		fail("Not yet implemented");
	}

	
	public void testIsNew() {
		// set up default vote not shuffled
		int nbr = sut.getNb_ranks(); 
		String [] votes = new String [5];
		Vote<String> tv = new Vote<String>(5,"");
		tv.setNameString("Richard");
		for (int j=0;j<5;j++)
		{
			if (j<nbr)votes[j] = String.valueOf(j);
			else votes[j] = "";
		}
		for (int j=0;j<5;j++) {
			tv.set(j, votes[j]);	
		}
		Vote<String> finalVote = new Vote (tv, ""); // use copy constructor
		// add it once
		sut.addVote(finalVote);
		
		// the shuffle is required to pass this test
		Collections.shuffle(Arrays.asList(votes));
		for (int j=0;j<5;j++) {
			tv.set(j, votes[j]);	
		}
		Vote<String> finalVoteAgain = new Vote (tv, ""); // use copy constructor
		// test if it is new (unique)
		assertTrue("This is new", sut.isNew(finalVoteAgain));
		
		
		//fail("Not yet implemented");
	}

	@Test
	//@Ignore  // this is currently executed from ResultsFrame not from Poll
	public void testComputeVoting() {
		//create a ranking engine
		sut.setAwardsByCombo();
		assertNotNull("awards has not been initiated properly", sut.awards);
		
		Vector<Vote> vv = sut.getVotes();
		for (Vote<String> v: vv){
			sut.printResultOfVoter(v);
		}
		//adxFrame.votingResult(poll);
		//sut.getAdaptationOptionsModel().
		//rankingEngine.FINALIZE()
		sut.computeVoting();
		
		assertNotNull("aoModel has not been initiated properly", sut.getAdaptationOptionsModel());
		assertEquals("aoModel does not have expected number of columns (10)", 10, sut.getAdaptationOptionsModel().getColumnCount());
		//fail("Not yet implemented");
	}
}
