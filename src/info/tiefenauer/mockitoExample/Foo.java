package info.tiefenauer.mockitoExample;

import java.util.Date;
import java.util.List;

public class Foo {
	
	private Bar _bar;

	public Bar bar(){
		return new Bar();
	}
	
	public Bar bar(Date date){
		return new Bar();
	}
	
	public String bar(String str){
		return str;
	}
	
	public String fooBar(Foo foo, Bar bar, String str){
		return this.toString() + bar.toString();
	}
	
	public void addBars(List<Bar> bars){
		for (Bar bar : bars){
			int index = bars.indexOf(bar);
			if(index%2 == 0){
				bar.setValue(new Integer(index + 10));
			}
			for (int i=0;i<index;i++){
				bar.doSomething();
			}
		}
	}
	
	
	
	public void setBar(Bar bar){
		_bar = bar;
	}
	public Bar getBar(){
		return _bar;
	}
}
