package info.tiefenauer.mockitoExample;


public class Bar {
	
	private int _val;
	private String _name = "";
	private FooBar _fooBar = null;
	
	public Bar(){
	}
	public Bar(String name){
		_name = name;
	}
	
	public void doSomething(){
		System.out.println(toString());
	}
	
	public int getValue() {
		return _val;
	}
	public void setValue(int num) {
		_val = num;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
	public FooBar getFooBar() {
		return _fooBar;
	}
	public void setFooBar(FooBar value) {
		_fooBar = value;
	}
}
