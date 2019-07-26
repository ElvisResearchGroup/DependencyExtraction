package general;

import static org.junit.Assert.assertTrue;
import java.util.stream.Stream;
import static general.General.*;

public class AMeaningFullName
extends AtomicTest.Tester{public static Stream<AtomicTest>test(){return Stream.of(a(()->
      pass(null)
      ),a(()->
      pass("")
      ),a(()->
      pass("  ")
      ));}
  public static void pass(String s1) {
   assertTrue(s1==null);
  }
}


/*@RunWith(Parameterized.class)
class TestRunner<T>{
  public static int lineNumber() {
    return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }
  @Parameter(0) public T testCase;
  @Parameter(1) public String _cb1;
  @Parameter(2) public String _path;
  @Parameter(3) public String _expected;
  @Parameter(4) public boolean isError;
  @Parameters(name = "{index}: line {0}")
  public static List<Object[]> createData() {
    return Arrays.asList(new Object[][] {
    {lineNumber(), "{}","This",
    "is.L42.connected.withSafeOperators.pluginWrapper.RefactorErrors$ClassUnfit"
    ,true
  },{lineNumber()
    
  }}
    @Test  public void test(){}
  }*/