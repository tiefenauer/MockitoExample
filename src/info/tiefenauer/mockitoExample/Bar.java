package info.tiefenauer.mockitoExample;

import org.apache.commons.lang3.StringUtils;

public class Bar {
	
	private int _val;

	public void doSomething(){
		System.out.println(toString());
	}
	
	public void doSomethingElse(){
		System.out.println(StringUtils.reverse(toString()));
	}

	public int getValue() {
		return _val;
	}
	public void setValue(Integer num) {
		_val = num;
	}
}
