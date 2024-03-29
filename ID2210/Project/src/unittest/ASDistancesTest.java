package unittest;

import org.junit.*;
import se.sics.asdistances.ASDistances;

/**
 *
 * @author Niklas Wahlén <nwahlen@kth.se>
 */
public class ASDistancesTest {

    ASDistances distances = null;

    public ASDistancesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        distances = ASDistances.getInstance();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetDistance() {
        System.out.println("Distance: " + distances.getDistance("193.10.67.148", "85.226.78.233"));
    }
}
