package ezdb;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class EZDBRowIterator implements Iterable, Iterator {

	private ResultSet rs;
	private Class ori;
	
	EZDBRowIterator( ResultSet rs, Class ori) {
		this.rs = rs;
		this.ori = ori;
	}
	
	@Override
	public boolean hasNext() {
		try {
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Object next() {
		int i = 0;
		Object obj = null;
		try {
			obj = ori.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Field f : ori.getFields()) {
			i = i + 1;
			try {
				f.set(obj, rs.getObject(i));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return obj;
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return this;
	}
	
}
