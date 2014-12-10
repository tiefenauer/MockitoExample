package test.info.tiefenauer.mockitoExample;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import info.tiefenauer.mockitoExample.Bar;
import info.tiefenauer.mockitoExample.Foo;
import info.tiefenauer.mockitoExample.FooBar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FooBar.class, Foo.class})
public class PowerMockitoTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Static methods must be mocked using mockStatic()
	 */
	@Test
	public void mockStaticMethod() {
		mockStatic(FooBar.class);
		FooBar myFooBar = new FooBar();
		when(FooBar.create()).thenReturn(myFooBar);
		FooBar myOtherFooBar = new FooBar(new Foo(), new Bar());
		when(FooBar.create(any(Foo.class), any(Bar.class))).thenReturn(myOtherFooBar);
		assertEquals("should return the stubbed instance", myFooBar, FooBar.create());
		assertEquals("should return the other stubbed instance", myOtherFooBar, FooBar.create(new Foo(), new Bar()));
	}
	
	@Test
	public void mockPrivateMethod() throws Exception{
		Foo foo = PowerMockito.spy(new Foo());
		PowerMockito.when(foo, "privateBar", anyString()).thenReturn("someString");
		assertEquals("should call the mocked private method stub", "someString", foo.bar("BooFar"));
	}
	
	/**
	 * Static Methods must be mocked using verifyStatic
	 */
	@Test
	public void verifyStaticMethod(){
		mockStatic(FooBar.class);
		new Foo().addBar(new Bar());
		verifyStatic(times(1));
		FooBar.create(any(Foo.class), any(Bar.class));
		verifyStatic(never());
		FooBar.create();
	}

}
