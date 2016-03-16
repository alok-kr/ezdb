package ezdb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import javassist.*;

public class DB {

	String driver;
	String db, user, pass;
	
	public DB(String driver, String db, String user, String pass) {
		this.driver = driver;
		this.db = db;
		this.user = user;
		this.pass = pass;
	}
	
	@SuppressWarnings("rawtypes")
	public Object getDBTable(Class original) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String cName = original.getName();
		String sName = original.getSimpleName();
		
		/* Creating a new __DBTable__ Class */
		
		CtClass cc = pool.get("ezdb.__DBTable__");
		cc.setName("ezdb.__DBTable_"+original.getSimpleName()+"__");
		cc.setInterfaces(new CtClass[] { pool.makeClass("ezdb.DBTable") });
		
		/* Connect DB and create table if not already */
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(db,user,pass);
		
		Statement stmt = conn.createStatement();
		String sql = "SELECT count(*) FROM information_schema.tables WHERE table_name = '__EZDB_"+sName+"__'";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int c = rs.getInt(1);
		rs.close();
		stmt.close();
		if(c==0) {
			/* Create table */
			boolean flag = false;
			String csql = "create table `__EZDB_"+sName+"__` (\n";
			for(Field f:original.getDeclaredFields()) {
				EZDB e = f.getAnnotation(EZDB.class);
				if(flag)
					csql = csql + ",\n";
				csql = csql + f.getName()+ " " + e.colDef();
				flag = true;
			}
			csql = csql + "\n)";
			stmt = conn.createStatement();
			stmt.executeUpdate(csql);
			stmt.close();
			//conn.commit();
		}
		
		
		/* Modify Methods */
		
		CtMethod appendM = cc.getDeclaredMethod("append");	
		appendM.setBody( "{" +
			Statement.class.getName() +" stmt = conn.createStatement();\n" +
			cName + " dbobj = (" + cName + ") $1;"+
			"String sql = \"insert into `__EZDB_" + sName + "__` values(" + 
				fieldList(original) + ")\";" +
			"stmt.executeUpdate(sql);"+
			"stmt.close();" +
			"}"
		);
			
		CtMethod updateM = cc.getDeclaredMethod("update");
		updateM.setBody("{" +
			
			"}"
		);
		
		CtMethod selectCondM = cc.getDeclaredMethod("selectCond");
		selectCondM.setBody("{" +
				Statement.class.getName() +" stmt = conn.createStatement(); " +
				"String sql = \"select * from `__EZDB_" + sName + "__` \";" +
				"if($1!=null)" +
					"sql = sql + \" where \" + $1;" +
				ResultSet.class.getName() + " rs = stmt.executeQuery(sql);" +	
				"return new " + EZDBRowIterator.class.getName() + "(rs, original);" +
				"}"
			);
		
		CtMethod selectM = cc.getDeclaredMethod("select");
		selectM.setBody("{" + 
				Statement.class.getName() + " stmt = conn.createStatement(); " +
				"String sql = \"Select * from `__EZDB_Employee__` \" + selectList($1);" +
				ResultSet.class.getName() + " rs = stmt.executeQuery(sql);"+
				"return new " + EZDBRowIterator.class.getName() + "(rs, original);"+
				"}"
			);
		
		CtMethod selectNULLM = cc.getDeclaredMethod("selectNULL");
		selectNULLM.setBody("{" + 
				Statement.class.getName() + " stmt = conn.createStatement(); " +
				"String sql = \"Select * from `__EZDB_Employee__` where \" + selectListNULL($1);" +
				ResultSet.class.getName() + " rs = stmt.executeQuery(sql);"+
				"return new " + EZDBRowIterator.class.getName() + "(rs, original);"+
				"}"
		);
		CtMethod deleteM = cc.getDeclaredMethod("delete");
		deleteM.setBody("{" +
			Statement.class.getName() + " stmt = conn.createStatement();\n" +
			"String sql = \"delete from `__EZDB_"+ sName + "__` where \" + "+DB.class.getName()+".fieldValList(original,$1) ;" +
			"stmt.executeUpdate(sql);" +
			"stmt.close();" +
			"}"
		);
		
		CtMethod deleteCondM = cc.getDeclaredMethod("deleteCond");
		deleteCondM.setBody("{" +
				Statement.class.getName() + " stmt = conn.createStatement();\n" +
				"String sql = \"delete from `__EZDB_"+ sName + "__` where \" + $1 ;" +
				"stmt.executeUpdate(sql);" +
				"stmt.close();" +
				"}"
			);
		CtMethod deleteAllM = cc.getDeclaredMethod("deleteAll");
		deleteAllM.setBody("{" +
				Statement.class.getName() + " stmt = conn.createStatement();\n" +
				"String sql = \"delete from `__EZDB_"+ sName + "__`\";" +
				"stmt.executeUpdate(sql);" +
				"stmt.close();" +
				"}"
			);

		
		/* Call constructor and set conn and Original class */
		
		Class clazz = cc.toClass();
		@SuppressWarnings("unchecked")
		Constructor<?> ctor = clazz.getConstructor(Connection.class, Class.class);
		Object obj = ctor.newInstance(new Object[] { conn, original });
		
		return obj;
	}
	
	@SuppressWarnings("rawtypes")
	public String fieldList(Class original) {
		boolean flag = false;
		String flist = "";
		for(Field f : original.getFields()) {
			if(flag)
				flist = flist + ", ";
			flag = true;
			if(f.getType()==Integer.class || f.getType()==int.class)
				flist = flist + "  \" + dbobj." + f.getName() + " + \"  ";
			if(f.getType()==String.class)
				flist = flist + " '\" + dbobj." + f.getName() + " + \"' ";
		}
		return flist;
	}
	
	@SuppressWarnings("rawtypes")
	public static String fieldValList(Class original, Object obj) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		
		boolean flag = false;
		String flist = "";
		for(Field f : original.getFields()) {
			if(flag)
				flist = flist + "and ";
			flag = true;
			if(f.getType()==Integer.class || f.getType()==int.class)
				flist = flist + " " + f.getName() + " = " + original.getField(f.getName()).get(obj) +  " ";
			if(f.getType()==String.class)
				flist = flist + " " + f.getName() + " = '" + original.getField(f.getName()).get(obj) + "' ";
		}
		return flist;
	}
}
