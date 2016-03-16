# ezdb
This project makes database operation (currently MySQL) more easier. Currently in Java.

### How to use it

It is very simple to manipulate table using **ezdb**. Just have to follow following steps...

1. First import ezdb library

`import ezdb.*;`

2. Create a class which will be used to interact with table. For example, create class Employee which will be used to create table in database for Employee.

```
import ezdb.*;

public class Employee {

   @EZDB(colDef = "INTEGER", autoIncrement = false, isPrimary = false)
   public int id;
   
   @EZDB(colDef = "VARCHAR(25)")
   public String first_name;
   
   @EZDB(colDef = "VARCHAR(25)")
   public String last_name;
   
   @EZDB(colDef = "INTEGER")
   public int salary;  

   public Employee() {
	   
   }
   public Employee(int id, String fname, String lname, int salary) {
	   this.id = id;
	   this.first_name = fname;
	   this.last_name = lname;
	   this.salary = salary;
	}
}

```

3. In main() create an object of **ezdb.DB**

```
   DB MySQL_DB = new DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/test", "root", "password");
```

4. Create an object of **ezdb.DBTable**

```
   DBTable dbt = (DBTable)MySQL_DB.getDBTable(Employee.class);
```

5. Now use it to insert, select, delete rows 

#####Insert
```
   Employee emp1 = new Employee(1, "Abc", "Kum", 100000);
   Employee emp2 = new Employee(1, "Def", "Mnj", 200000);
   Employee emp3 = new Employee(1, "Abc", "Mno", 300000);

   dbt.append(emp1);
   dbt.append(emp2);
   dbt.append(emp3);
```	

#####Delete

```
   dbt.deleteCond("first_name = 'Def'");
```

#####Select
```
   Employee e;
   emp2.first_name = "Abc";
   Iterator i = dbt.selectNULL(emp2).iterator();

   while(i.hasNext()) {
      e = (Employee)i.next();
      System.out.println("Id : "+e.id+" Name : " + e.first_name + " " + e.last_name + " Salary : " + e.salary);
   }
```

#####Count

```
   System.out.println("Entries : " + dbt.count());
```

###Current Status

Currently, project is in alpha stage.

###Future plans

1. Different databases will be added
2. More annotation to control create of table
3. More Programming Language support
4. Proper documentation
