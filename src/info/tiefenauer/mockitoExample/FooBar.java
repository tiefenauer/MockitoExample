package info.tiefenauer.mockitoExample;

public class FooBar {

	private Foo _foo;
	private Bar _bar;
	
	public FooBar(){
	}
	
	public FooBar(Foo foo, Bar bar){
		_foo = foo;
		_bar = bar;
	}
	
	public static FooBar create(){
		return new FooBar();
	}
	
	public Foo getFoo() {
		return _foo;
	}
	public void setFoo(Foo _foo) {
		this._foo = _foo;
	}
	public Bar getBar() {
		return _bar;
	}
	public void setBar(Bar _bar) {
		this._bar = _bar;
	}
}
