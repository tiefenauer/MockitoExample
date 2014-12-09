package test.info.tiefenauer.mockitoExample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import info.tiefenauer.mockitoExample.Bar;
import info.tiefenauer.mockitoExample.Foo;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class FooTest {
	
	private Bar bar = new Bar();
	
	@Mock Foo foo;

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

	@Test
	public void simpleStub() {
		// stub method
		when(foo.bar()).thenReturn(bar);
		assertEquals("should return mocked value", bar, foo.bar());
		
		// change stub
		Bar otherBar = mock(Bar.class);
		when(foo.bar()).thenReturn(otherBar);
		assertEquals("should return new mocked value", otherBar, foo.bar());
	}
	
	@Test
	public void stubWithParameters() {
		Date now = new Date();
		when(foo.bar(now)).thenReturn(bar);
		assertEquals("should return mocked value", bar, foo.bar(now));
		assertNull("not stubbed call should return null", foo.bar(new Date()));
	}
	
	@Test
	public void stubWithMatchers(){
		Date date1 = new Date();
		Date date2 = new Date();
		
		when(foo.bar(any(Date.class))).thenReturn(bar);
		assertEquals("should return mocked value", bar, foo.bar(date1));
		assertEquals("should also return mocked value", bar, foo.bar(date2));
	}
	
	@Test
	public void stubMultipleReturnValues(){
		when(foo.bar(anyString())).thenReturn("fooBar1", "fooBar2", "fooBar3");
		assertEquals("should return first stubbed value", "fooBar1", foo.bar("bla1"));
		assertEquals("should return second stubbed value", "fooBar2", foo.bar("bla2"));
		assertEquals("should return third stubbed value", "fooBar3", foo.bar("bla3"));
		assertEquals("should return last stubbed value", "fooBar3", foo.bar("bla4"));
	}
	
	@Test(expected=RuntimeException.class)
	public void stubException(){
		when(foo.bar()).thenThrow(new RuntimeException());
		foo.bar();
	}
	
	@Test(expected=RuntimeException.class)
	public void stubExceptionAlternativeSyntax(){
		doThrow(RuntimeException.class).when(foo).bar();
		foo.bar();
	}
	
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
	
	@Test(expected=InvalidUseOfMatchersException.class)
	public void stubMultipleParametersWrong(){
		when(foo.fooBar(any(Foo.class), bar, "fooBar")).thenReturn("fooBar123");
		assertEquals("this should throw an exception", "fooBar123", foo.fooBar(new Foo(), new Bar(), "fooBar"));
	}
	
	@Test
	public void stubMultipleParametersCorrect(){
		when(foo.fooBar(any(Foo.class), any(Bar.class), eq("fooBar"))).thenReturn("fooBar123");
		assertEquals("this should NOT throw an exception", "fooBar123", foo.fooBar(new Foo(), new Bar(), "fooBar"));
	}
	
	@Test
	public void verifyMethodCall(){
		Bar barMock0 = mock(Bar.class);
		Bar barMock1 = mock(Bar.class);
		Bar barMock2 = mock(Bar.class);
		Bar barMock3 = mock(Bar.class);

		new Foo().addBars(Arrays.asList(barMock0, barMock1, barMock2, barMock3));
		
		// verify bar() was called
		verify(barMock1).doSomething();
		
		// matchers are supported
		verify(barMock0, never()).doSomething();
		verify(barMock1, atLeastOnce()).doSomething();
		verify(barMock2, times(2)).doSomething();
		verify(barMock3, atLeast(2)).doSomething();
		verify(barMock3, atMost(3)).doSomething();
		verify(barMock1, only()).doSomething();
	}
	
	@Test
	public void verifyWithArgumentCapture(){
		Bar barMock0 = mock(Bar.class);
		Bar barMock1 = mock(Bar.class);
		Bar barMock2 = mock(Bar.class);
		Bar barMock3 = mock(Bar.class);

		new Foo().addBars(Arrays.asList(barMock0, barMock1, barMock2, barMock3));
		
		ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
		
		verify(barMock0).setValue(captor.capture());
		assertEquals("value should be set to index in list", 10, captor.getValue().intValue());
		
		verify(barMock2).setValue(captor.capture());
		assertEquals("value should be set to index in list", 12, captor.getValue().intValue());
	}
	
}
