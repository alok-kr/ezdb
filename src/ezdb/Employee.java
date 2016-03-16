package ezdb;

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
