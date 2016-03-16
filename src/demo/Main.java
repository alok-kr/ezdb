package demo;

import java.util.Iterator;

import ezdb.*;

public class Main {

	public static DBTable dbt;
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {
		DB MySQL_DB = new DB("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.2.61/test", "root", "alpass");
	
		dbt = (DBTable)MySQL_DB.getDBTable(Employee.class);
		
		Employee emp = new Employee(1, "Alok", "Kumar", 100000);
		Employee emp2 = new Employee(1, "Rahul", "Kumar", 100000);
		Employee emp3 = new Employee(1, "Amol", "Kumar", 100000);
		dbt.append(emp);
		dbt.append(emp2);
		dbt.append(emp3);
		//dbt.deleteCond("first_name = 'Alok2'");
		//dbt.deleteAll();
		
	
		Employee e;
		emp2.first_name = "Alok";
		Iterator i = dbt.selectNULL(emp2).iterator();
		
		while(i.hasNext()) {
			e = (Employee)i.next();
			System.out.println("Id : "+e.id+" Name : " + e.first_name + " " + e.last_name + " Salary : " + e.salary);
		}
		System.out.println("---------");
		emp2.first_name = "Alok2";
		for(Object o:dbt.select(emp2)) {
			e = (Employee)o;
			System.out.println("Id : "+e.id+" Name : " + e.first_name + " " + e.last_name + " Salary : " + e.salary);
		}
		
		System.out.println("Entries : " + dbt.count());
	}

}
