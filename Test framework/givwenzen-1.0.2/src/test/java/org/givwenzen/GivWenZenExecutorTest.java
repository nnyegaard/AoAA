package org.givwenzen;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.fail;

public class GivWenZenExecutorTest implements GivWenZen {

   public static final String METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE = "method without parameters and with return value";
   public static final String METHOD_WITHOUT_PARAMETERS_AND_WITHOUT_RETURN_VALUE = "method without parameters and without return value";
   public static final String METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE = "method taking int of 1 parameter and with return value";
   public static final String METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE = "method with MySimpleCass parameter and with return value";
   public static final String METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE = "method with MySimpleCass3 parameter using object parse and with return value";

   private GivWenZenExecutor executor;
   boolean methodWithOutParametersAndWithoutReturnValueCalled;
   int intParamValue;
   private static final String GIVEN = "given";
   private static final String WHEN = "when";
   private static final String THEN = "then";
   private static final String AND = "and";
   private static final String[] bddMethods = new String[4];
   MySimpleClass mySimpleClass;
   MySimpleClass3 mySimpleClass3;

   static {
      bddMethods[0] = GIVEN;
      bddMethods[1] = WHEN;
      bddMethods[2] = THEN;
      bddMethods[3] = AND;
   }

   @Before
   public void setUp() throws Exception {
      executor = GivWenZenExecutorCreator.instance()
         .customStepState(this)
         .stepClassBasePackage("org.givwenzen.")
         .create();
   }

   @Test
   public void testFindAppropriateStepDefinitionWhenDuplicationIsNeeded() throws Exception {
      assertCorrectStepDefinitionIsUsed("org.givwenzen.steps.gui.", "gui");
      assertCorrectStepDefinitionIsUsed("org.givwenzen.steps.businesslogic.", "business");
   }

   @Test
   public void testDuplicateStepDefinitionCauseException() throws Exception {
      ArrayList<String> list = new ArrayList<String>();
      executor = GivWenZenExecutorCreator.instance()
         .customStepState(list)
         .stepClassBasePackage("steps.gui.,steps.businesslogic.")
         .create();
      try {
         executor.given("a step can be handled either through the gui or through the business layer");
         fail("should throw exception");
      } catch (GivWenZenException e) {
      }

   }

   private void assertCorrectStepDefinitionIsUsed(String basePackageForSteps, String expected) throws Exception {
      ArrayList<String> list = new ArrayList<String>();
      executor = GivWenZenExecutorCreator.instance()
         .customStepState(list)
         .stepClassBasePackage(basePackageForSteps)
         .create();
      executor.given("a step can be handled either through the gui or through the business layer");

      assertThat(list.size()).isEqualTo(1);
      assertThat(list.get(0)).isEqualTo(expected);
   }

   @Test
   public void testShouldScreamWhenAnnotationContainsInvalidParameters() throws Exception {
      try {
         executor.given("method taking int of " + Integer.MAX_VALUE + "0 parameter and with return value");
         fail("Should scream when annotaion contains invalid parameters");
      } catch (InvalidDomainStepParameterException e) {
      }
   }

   @Test
   public void testInvalidStepParameterExceptionShouldDisplayExpectedParameters() throws Exception {
      try {
         executor.given("method taking int of " + Integer.MAX_VALUE + "0 parameter and with return value");
      } catch (InvalidDomainStepParameterException e) {

      }
   }

   @Test
   public void testShouldScreamWhenAnnotatedMethodIsNotFound() throws Exception {
      String method = "method taking int of " + Integer.MAX_VALUE + "0 parameter and with return value";
      try {
         executor.given(method);
         fail("Should Scream When Annotated Method Is Not Found");
      } catch (InvalidDomainStepParameterException e) {
         assertThat(e.getMessage().split("\\n")[1]).isEqualTo("Invalid step parameters in method pattern: " + method);
         assertThat(e.getMessage().split("\\n")[2]).isEqualTo(
            "  found matching method annotated with: method taking int of (\\d+) parameter and with return value");
         assertThat(e.getMessage().split("\\n")[3]).isEqualTo(
            "  method signature is: public java.lang.String org.givwenzen.GivWenZenExecutorTest$FakeSteps2.methodWithIntParameterAndWithReturnValue(int)");
      }
   }

   @Test
   public void testStepNotFoundExceptionShouldHaveAClearStackTrace() throws Exception {
      try {
         executor.given("not found");
      } catch (DomainStepNotFoundException e) {
         assertThat(e.getStackTrace().length).isEqualTo(0);
      }
   }

   @Test
   public void testStepNotFoundExceptionShouldGiveAnExampleMethodSignature() throws Exception {
      try {
         executor.given("not found");
      } catch (DomainStepNotFoundException e) {
         assertThat(e.getMessage().contains("@DomainStep(\"not found\")")).isTrue();
         assertThat(e.getMessage().toLowerCase()).contains("typical causes of this error are:");
         assertThat(e.getMessage().toLowerCase()).contains("step class ");
      } catch (Exception e) {
         System.out.println("error " + e);
      }
   }

   @Test
   public void testMethodWithOutParametersAndWithReturnValueIsCalled() throws Exception {
      for (String bddMethod : bddMethods) {
         assertThat(executeStringMethodOnAdapter(METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE, bddMethod))
            .isEqualTo(METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE);
      }
   }

   @Test
   public void testMethodWithOutParametersAndWithoutReturnValueIsCalled() throws Exception {
      for (String bddMethod : bddMethods) {
         methodWithOutParametersAndWithoutReturnValueCalled = false;
         assertThat(executeStringMethodOnAdapter(METHOD_WITHOUT_PARAMETERS_AND_WITHOUT_RETURN_VALUE, bddMethod))
             .isEqualTo(this);
         assertThat(methodWithOutParametersAndWithoutReturnValueCalled).isTrue();
      }
   }

   @Test
   public void testMethodWithIntegerParameterAndReturnValueIsCalled() throws Exception {
      for (String bddMethod : bddMethods) {
         intParamValue = 0;
         assertThat(executeStringMethodOnAdapter(METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE, bddMethod))
            .isEqualTo(METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE);
         assertThat(intParamValue).isEqualTo(1);
      }
   }

   @Test
   public void testMethodWithMySimpleClassParameterAndReturnValueIsCalled() throws Exception {
      for (String bddMethod : bddMethods) {
         mySimpleClass = null;
         assertThat(executeStringMethodOnAdapter(
            METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE, bddMethod))
               .isEqualTo(METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE);
         assertThat(mySimpleClass.getValue()).isEqualTo("MySimpleCass");
      }
   }

   @Test
   public void testMethodWithMySimpleClass3ParameterWithAObjectParseAndReturnValueIsCalled() throws Exception {
      for (String bddMethod : bddMethods) {
         mySimpleClass3 = null;
         assertThat(executeStringMethodOnAdapter(
               METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE, bddMethod))
            .isEqualTo(METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE);
         assertThat(mySimpleClass3.getValue()).isEqualTo("MySimpleCass3");
      }
   }

   @Test
   public void testAnnotatedClassesAreFoundAndStepsAreAvailable() throws Exception {
      // this method is available in FakeSteps.java
      assertThat(executor.given("my step")).isEqualTo(true);
      assertThat(executor.given("my step in class requiring access to the adapter")).isEqualTo(true);
   }

   @Test(expected = GivWenZenExecutionException.class)
   public void whenStepMethodThrowsAnExceptionThenAGivWenZenExcecutionExceptionShouldBeThrown() throws Exception {
      executor.given("this method throws an exception every time");
   }

   private Object executeStringMethodOnAdapter(String methodPatten, String givenWhenOrThen) throws Exception {
      try {
         Method method = executor.getClass().getMethod(givenWhenOrThen, String.class);
         return method.invoke(executor, methodPatten);
      } catch (Exception e) {
         throw new RuntimeException("Error executing '" + methodPatten + "' using '" + givenWhenOrThen + "': "
            + e.getMessage());
      }
   }

   public Object given(String methodString) throws Exception {
      return null;
   }

   public Object when(String methodString) throws Exception {
      return null;
   }

   public Object then(String methodString) throws Exception {
      return null;
   }

   public Object and(String methodString) throws Exception {
      return null;
   }

   @DomainSteps
   public static class FakeSteps {

      @DomainStep("my step")
      public boolean myStep() {
         return true;
      }

      @DomainStep("this method throws an exception every time")
      public void throwAnException() {
         throw new RuntimeException("exception in step method");
      }
   }

   @DomainSteps
   public static class FakeSteps2 {

      private GivWenZen adapter;

      public FakeSteps2(GivWenZen adapter) {
         this.adapter = adapter;
      }

      @DomainStep("my step in class requiring access to the adapter")
      public boolean myStep2() {
         return true;
      }

      @DomainStep(METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE)
      public String methodWithOutParametersAndWithReturnValue() {
         return METHOD_WITHOUT_PARAMETERS_AND_WITH_RETURN_VALUE;
      }

      @DomainStep(METHOD_WITHOUT_PARAMETERS_AND_WITHOUT_RETURN_VALUE)
      public void methodWithOutParametersAndWithoutReturnValue() {
         getExecutor().methodWithOutParametersAndWithoutReturnValueCalled = true;
      }

      @DomainStep("method taking int of (\\d+) parameter and with return value")
      public String methodWithIntParameterAndWithReturnValue(int param) {
         getExecutor().intParamValue = param;
         return METHOD_WITH_INTEGER_PARAMETER_AND_WITH_RETURN_VALUE;
      }

      @DomainStep("method with (.*) parameter and with return value")
      public String methodWithMySimpleClassParameterUsingPropertyEditorAndWithReturnValue(MySimpleClass param) {
         getExecutor().mySimpleClass = param;
         return METHOD_WITH_MYSIMPLECLASS_PARAMETER_AND_WITH_RETURN_VALUE;
      }

      @DomainStep("method with (.*) parameter using object parse and with return value")
      public String methodWithMySimpleClass3ParameterWithObjectParseAndWithReturnValue(MySimpleClass3 param) {
         getExecutor().mySimpleClass3 = param;
         return METHOD_WITH_MY_SIMPLE_CLASS3_PARAMETER_USING_OBJECT_PARSE_AND_WITH_RETURN_VALUE;
      }

      private GivWenZenExecutorTest getExecutor() {
         return (GivWenZenExecutorTest) adapter;
      }
   }

   public static class MySimpleClass3 extends MySimpleClass {
      public MySimpleClass3(String value) {
         super(value);
      }

      public static MySimpleClass3 parse(String value) {
         return new MySimpleClass3(value);
      }
   }

   public static class MySimpleClass {
      private String value;

      public String getValue() {
         return value;
      }

      public MySimpleClass(String value) {
         this.value = value;
      }

      public String toString() {
         return this.value;
      }
   }

   public static class MySimpleClassEditor extends PropertyEditorSupport {
      public String getAsText() {
         return getValue().toString();
      }

      public void setAsText(String text) throws IllegalArgumentException {
         setValue(new MySimpleClass(text));
      }
   }
}