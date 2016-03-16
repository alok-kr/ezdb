package demo;

import java.util.Iterator;

import ezdb.*;

public class Main {

	public static DBTable dbt;
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		
		DB MySQL_DB = new DB("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.2.61/test", "root", "alpass");
	
		dbt = (DBTable)MySQL_DB.getDBTable(Employee.class);
		
		/* Add Data to table */
		
		Employee emp1 = new Employee(1, "Abc", "Kum", 100000);
		Employee emp2 = new Employee(1, "Def", "Mnj", 200000);
		Employee emp3 = new Employee(1, "Abc", "Mno", 300000);
		
		dbt.append(emp1);
		dbt.append(emp2);
		dbt.append(emp3);

		/* Delete Data from table */
		
		//dbt.deleteCond("first_name = 'Abc'");
		//dbt.deleteAll();
	
		
		/* Search Data */
		
		Employee e;
		emp2.first_name = "Abc";
		Iterator i = dbt.selectNULL(emp2).iterator();
		
		while(i.hasNext()) {
			e = (Employee)i.next();
			System.out.println("Id : "+e.id+" Name : " + e.first_name + " " + e.last_name + " Salary : " + e.salary);
		}
		System.out.println("---------");
		
		emp2.first_name = "Def";
		for(Object o:dbt.select(emp2)) {
			e = (Employee)o;
			System.out.println("Id : "+e.id+" Name : " + e.first_name + " " + e.last_name + " Salary : " + e.salary);
		}
		
		/* Count of Entries in Table */
		
		System.out.println("Entries : " + dbt.count());
	}

}
