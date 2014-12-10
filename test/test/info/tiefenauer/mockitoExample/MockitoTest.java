package test.info.tiefenauer.mockitoExample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import info.tiefenauer.mockitoExample.Bar;
import info.tiefenauer.mockitoExample.Foo;
import info.tiefenauer.mockitoExample.FooBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {
	
	@Rule public ExpectedException thrown = ExpectedException.none();
	@Mock Foo foo;
	
	private Bar bar = new Bar();
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * When not stubbed mocks will return these default values
	 */
	@Test
	public void defaultValues(){
		assertEquals("default int value should be zero", 0, foo.getInt());
		assertEquals("default long value should be zero", 0l, foo.getLong());
		assertEquals("default float value should be zero", 0f, foo.getFloat(), 0.00001);
		assertFalse("default boolean value should be false", foo.getBoolean());
		assertNull("default Object value should be null", foo.getObject());
	}
	
	/**
	 * Simple stub for a method without arguments
	 */
	@Test
	public void simpleStub() {
		// stub method
		when(foo.getBar()).thenReturn(bar);
		assertEquals("should return mocked value", bar, foo.getBar());
		
		// change stub
		Bar otherBar = mock(Bar.class);
		when(foo.getBar()).thenReturn(otherBar);
		assertEquals("should return new mocked value", otherBar, foo.getBar());
	}
	
	/**
	 * Stub a method with parameters
	 */
	@Test
	public void stubWithParameters() {
		when(foo.getBar("bar")).thenReturn(bar);
		assertEquals("should return mocked value", bar, foo.getBar("bar"));
		assertNull("not stubbed call should return null", foo.getBar("rab"));
	}
	
	/**
	 * Stub a method with parameteres using matchers
	 */
	@Test
	public void stubWithMatchers(){
		Bar bar1 = mock(Bar.class);
		Bar bar2 = mock(Bar.class);
		Bar bar3 = mock(Bar.class);
		
		// stub one argument
		when(foo.getBar(anyString())).thenReturn(bar1);
		when(foo.getBar(any(String.class))).thenReturn(bar1);
		when(foo.getBar(eq("bar"))).thenReturn(bar2);
		ArgumentMatcher<String> isStringOfLength2 = new ArgumentMatcher<String>() {
			@Override
			public boolean matches(Object argument) {
				return argument != null && argument instanceof String && ((String)argument).length() == 2;
			}
		};
		when(foo.getBar(argThat(isStringOfLength2))).thenReturn(bar3);
		
		assertEquals("check stub with any Bar", bar1, foo.getBar("fooBar"));
		assertEquals("check stub with any other Bar", bar1, foo.getBar("BarFoo"));
		assertEquals("check stub with mock", bar2, foo.getBar("bar"));
		assertEquals("check stub with a valid bar", bar3, foo.getBar("fb"));
	}
	
	
	/**
	 * A mock can be stubbed to return multiple values in order. The last value will be repeated
	 */
	@Test
	public void stubMultipleReturnValues(){
		when(foo.bar(anyString())).thenReturn("fooBar1", "fooBar2", "fooBar3");
		assertEquals("should return first stubbed value", "fooBar1", foo.bar("bla1"));
		assertEquals("should return second stubbed value", "fooBar2", foo.bar("bla2"));
		assertEquals("should return third stubbed value", "fooBar3", foo.bar("bla3"));
		assertEquals("should return last stubbed value", "fooBar3", foo.bar("bla4"));
	}	
	
	@Test
	public void stubMultipleParameters(){
		when(foo.getBar("bar", 42)).thenReturn(bar);
		assertEquals("check stub with values", bar, foo.getBar("bar", 42));
		assertNull("should return null if one parameter does not match", foo.getBar("foo", 42));
		assertNull("should return null if one parameter does not match", foo.getBar("bar", 43));
	}
	
	/**
	 * Stub a method with multiple arguments using matchers
	 */
	@Test
	public void stubMultipleMatchersCorrect(){
		when(foo.getBar(anyString(), eq(42))).thenReturn(bar);
		assertEquals("check stub with multiple matchers", bar, foo.getBar("any string", 42));		
	}
	
	/**
	 * When stubbing multiple arguments using matchers, all arguments must use matchers
	 */
	@Test(expected=InvalidUseOfMatchersException.class)
	public void stubMultipleMatchersInvalid(){
		when(foo.getBar(anyString(), 42)).thenReturn(bar);
		assertEquals("check stub with multiple matchers", bar, foo.getBar("any string", 42));		
	}
	
	/**
	 * A mock can be stubbed to throw an exception
	 */
	@Test
	public void stubException(){
		when(foo.getBar(null)).thenThrow(new IllegalArgumentException());
		thrown.expect(IllegalArgumentException.class);
		foo.getBar(null);
	}
	
	/**
	 * Methods with void return type must be stubbed using doXXX
	 */
	@Test
	public void stubVoidMethods(){
		doThrow(IllegalArgumentException.class).when(foo).addBar(null);
		thrown.expect(IllegalArgumentException.class);
		foo.addBar(null);
	}
	
	/**
	 * A mock can be stubbed using an answer, which will be executed every time.
	 */
	@Test
	public void stubAnswer(){
		when(foo.bar(any(String.class))).thenAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				String inputStr = (String) invocation.getArguments()[0];
				return StringUtils.reverse(inputStr);
			}
		});
		assertEquals("should return answer", "cba", foo.bar("abc"));
		assertEquals("should return answer", "FooBar", foo.bar("raBooF"));
	}
	
	/**
	 * A mock object can be verified to
	 */
	@Test
	public void verifyMethodCall(){
		Bar barMock0 = mock(Bar.class);
		Bar barMock1 = mock(Bar.class);
		Bar barMock2 = mock(Bar.class);
		Bar barMock3 = mock(Bar.class);

		new Foo().addBars(Arrays.asList(barMock0, barMock1, barMock2, barMock3));
		
		// verify doSomething() was called
		verify(barMock1).doSomething();
		
		// matchers are supported
		verify(barMock0, never()).doSomething();
		verify(barMock1, atLeastOnce()).doSomething();
		verify(barMock2, times(2)).doSomething();
		verify(barMock3, atLeast(2)).doSomething();
		verify(barMock3, atMost(3)).doSomething();
		verify(barMock1, only()).doSomething();
	}
	
	/**
	 * Arguments of mock object methods can be verified too
	 */
	@Test
	public void verifyMethodCallWithArguments(){
		Bar barMock0 = mock(Bar.class);
		Bar barMock1 = mock(Bar.class);
		Bar barMock2 = mock(Bar.class);
		Bar barMock3 = mock(Bar.class);

		new Foo().addBars(Arrays.asList(barMock0, barMock1, barMock2, barMock3));
		
		verify(barMock0).setValue(10);
		verify(barMock2).setValue(12);
	}
	
	/**
	 * Arguments can be captured, if further assertions must be made
	 */
	@Test
	public void verifyMethodCallWithMultipleArguments(){
		Bar barMock0 = mock(Bar.class);
		Bar barMock1 = mock(Bar.class);
		Bar barMock2 = mock(Bar.class);

		Foo foo = new Foo();
		foo.addBars(Arrays.asList(barMock0, barMock1, barMock2));
		
		ArgumentCaptor<FooBar> captor = ArgumentCaptor.forClass(FooBar.class);
		
		verify(barMock2).setFooBar(captor.capture());
		assertEquals("FooBar should contain correct Foo", foo, captor.getValue().getFoo());
		assertEquals("FooBar should contain correct Bar", barMock2, captor.getValue().getBar());
	}
	
	@Test
	public void spyOnRealObject(){
		List<Bar> spyOnList = spy(new ArrayList<Bar>());
		// won't work
		//when(spyOnList.get(0)).thenReturn(bar);
		
		doReturn(bar).when(spyOnList).get(0);
		doReturn(bar).when(spyOnList).get(1);
		doReturn(null).when(spyOnList).get(2);
		
		// verify stubbed methods
		assertEquals("should return the mocked value", bar, spyOnList.get(0));
		assertEquals("should return the mocked value", bar, spyOnList.get(1));
		assertNull("should return the mocked value (null)", spyOnList.get(2));
		
		// verify unstubbed methods
		spyOnList.add(bar);
		assertEquals("un-stubbed methods should still work", 1, spyOnList.size());
	}
	
}
