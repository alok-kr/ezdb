package ezdb;

import java.lang.reflect.Field;
import java.sql.*;

public class __DBTable__ {
	
	boolean autoCommit;
	Connection conn;
	
	@SuppressWarnings("rawtypes")
	Class original;
	
	@SuppressWarnings("rawtypes")
	public __DBTable__(Connection conn, Class ori) {
		this.autoCommit = true;
		this.conn = conn;
		this.original = ori;
	}
	
	public void append(Object obj) throws Exception { }
	
	public void update(Object oobj, Object nobj) throws Exception {
		// TODO
	}
	/* Format <string>FieldName, <Object>Value, <Enum>AND/OR */
	@SuppressWarnings("rawtypes")
	public Iterable selectE(Object... obj) throws Exception {
		// TODO
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Iterable selectCond(String cond)  throws Exception { return null; }
	@SuppressWarnings("rawtypes")
	public Iterable select(Object obj)  throws Exception { return null; }
	/* Select with IS NULL */
	@SuppressWarnings("rawtypes")
	public Iterable selectNULL(Object obj) throws Exception { return null; 	}
	
	public void delete(Object obj)  throws Exception { }
	public void deleteCond(String cond) throws Exception { }
	public void deleteAll()  throws Exception { }
	
	public int count() throws Exception {
		Statement stmt = conn.createStatement();
		stmt = conn.createStatement();
	    String sql = "SELECT count(*) FROM `__EZDB_" + original.getSimpleName() + "__`";
	    ResultSet rs = stmt.executeQuery(sql);
	    rs.next();
	    int c = rs.getInt(1); 
	    rs.close();
	    stmt.close();
		return c;
	}
	
	public int countCond(String cond)  throws Exception {
		Statement stmt = conn.createStatement();
		stmt = conn.createStatement();
	    String sql = "SELECT count(*) FROM `__EZDB_" + original.getSimpleName() + "__` where " + cond;
	    ResultSet rs = stmt.executeQuery(sql);
	    rs.next();
	    int c = rs.getInt(1); 
	    rs.close();
	    stmt.close();
		return c;
	}
	
	public void commit() throws Exception {
		conn.commit();
	}
	
	public void revoke()  throws Exception {
		conn.rollback();
	}
	
	public void setAutoCommit(boolean val) {
		autoCommit = val;
	}
	
	public boolean getAutoCommit() {
		return autoCommit;
	}
	
	public String selectList(Object obj) throws IllegalArgumentException, IllegalAccessException {
		String str = "";
		boolean flag = false;
		for(Field f : original.getFields()) {
			
			if(f.get(obj)!=null) {
				if(flag)
					str = str + " and ";
				flag = true;
				str = str + " " + f.getName() + " = '" + f.get(obj) + "' ";
			}
		}
		return " where " + str;
	}
	
	public String selectListNULL(Object obj) throws IllegalArgumentException, IllegalAccessException {
		String str = "";
		boolean flag = false;
		for(Field f : original.getFields()) {
			if(flag)
				str = str + " and ";
			flag = true;
			if(f.get(obj)!=null) {
				str = str + " " + f.getName() + " = '" + f.get(obj) + "' ";
			} else {
				str = str + " " + f.getName() + " is NULL ";
			}
		}
		return str;
	}
}
