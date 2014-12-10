package info.tiefenauer.mockitoExample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class Foo {
	
	private List<Bar> _barList = new ArrayList<Bar>();
	
	public void addBar(Bar bar){
		_barList.add(bar);
		FooBar.create(this, bar);
	}
	
	public Bar getBar(){
		if (_barList.size() > 0)
			return _barList.remove(0);
		return null;
	}
	public Bar getBar(String name){
		for (Bar bar : _barList){
			if (bar.getName().equals(name))
				return bar;
		}
		return null;
	}
	public Bar getBar(String name, int value){
		for (Bar bar : _barList){
			if (bar.getName().equals(name) && bar.getValue() == value)
				return bar;
		}
		return null;
	}
	
	public String bar(String str){
		return privateBar(str);
	}
	
	private String privateBar(String str){
		return StringUtils.reverse(str);
	}
	
	public void addBars(List<Bar> bars){
		for (Bar bar : bars){
			int index = bars.indexOf(bar);
			
			for (int i=0;i<index;i++){
				bar.doSomething();
			}
			if(index%2 == 0){
				bar.setValue(new Integer(index + 10));
				if (index > 0){
					bar.setFooBar(new FooBar(this, bar));
				}
			}
			_barList.add(bar);
		}
	}
	
	public int getInt(){
		return new Random().nextInt();
	}
	public long getLong(){
		return new Random().nextLong();
	}
	public boolean getBoolean(){
		return new Random().nextBoolean();
	}
	public float getFloat(){
		return new Random().nextFloat();
	}
	public Object getObject(){
		return new Object();
	}
}
