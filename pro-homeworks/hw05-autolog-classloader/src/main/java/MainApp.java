import classes.Ioc;
import classes.TestLogging;


public class MainApp {
    public static void main(String[] args) {
      TestLogging testClass = Ioc.getLoggingProxy();
      testClass.calculation(1);
      testClass.calculation(2,3);
      testClass.calculation(3,4,5);
    }
}
