package streamapicasestudy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeService {
	static EmployeeService empService = new EmployeeService();
	static EmployeeRepository empr = new EmployeeRepository();
	public static void main(String[] args) {
		empr.dataCreator();
		//find sum of salaries of all employees
		System.out.println("Sum of Salaries of all Employees is "+empService.sumSalaries(empr.getEmpList()));
		
		
		//find out senior most employee of company
		System.out.println("Senior Most Employee is "+empService.seniorMost(empr.getEmpList()));
		
		
		/*Sort employees by their
		Employee id
	    Department id
	    First name*/
		System.out.println("Sorted List of Employees is "+empService.sortedList(empr.getEmpList()));
		
		
		//List employee name, salary and salary increased by 15%.
		System.out.println("Employee Name         Salary After Increase");
		List<Employee> newList = empService.getAfterIncrease(empr.getEmpList());
		for(Employee e:newList)
		{
			System.out.printf("%s %s       %.3f\n",e.getFirstName(),e.getLastName(),e.getSalary());
		}
		
		
		//List out department names and count of employees in each
		System.out.println("Department Name         Count of Employees");
		Map<String,Long> mapCount = empService.getCountofEmps(empr.getEmpList());
		mapCount.keySet().stream().forEach(i->System.out.println(i+"                "+mapCount.get(i)));
		
		
		//List employee name and duration of their service in months and days.
		Map<String, Period> ans = empService.getDuration(empr.getEmpList());
		ans.keySet().stream().forEach(o->{
			System.out.println(o+" "+(ans.get(o).getYears()*12+ans.get(o).getMonths())+" Months "+ans.get(o).getDays()+" Days ");
		});
		
		
		//Find departments with highest count of employees.
		System.out.println("Department with Highest number of Employees is "+empService.getHighestDept(empr.getEmpList()));
		
		
		//Create a method to accept first name and last name of manager 
		//to print name of all his/her subordinates.
		System.out.println("\nTo Print Subordinates of a Manager : \n");
		empService.getEmpsUnderMgr(empr.getEmpList());
		}
	
	
	  private void getEmpsUnderMgr(List<Employee> empList) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter First Name of Manager : ");
		try {
			String fName = br.readLine();
			System.out.print("\nEnter Last Name of Manager : ");
			String lName = br.readLine();
			String name = fName+" "+lName;
			Optional<Integer> mgrId = empList.stream().filter(i->
			{
				String name1 = i.getFirstName()+" "+i.getLastName();
				Integer Id = null;
				if((name1.equals(name)))
				{
					Id = i.getEmpId();
				}
				return Id != null;
			}
			).map(i->i.getEmpId()).findFirst();
			if(mgrId.isPresent()) {
			List<String> subList = empList.stream().filter(i->i.getManagerId().equals(mgrId)).map(i->i.getFirstName()+" "+i.getLastName()).collect(Collectors.toList()).stream().sorted().collect(Collectors.toList());
			System.out.println("Subordinates of "+name+" :");
			subList.stream().forEach(System.out::println);
			}
			else {
				System.out.println("There is no Employee with name "+name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	  
	  
	String getHighestDept(List<Employee> empList) {
		Map<String,Long> mapCount = empList.stream().collect(Collectors.groupingBy((Employee e)->e.getDepartment().
				getDepartmentName(),Collectors.counting()));
		return mapCount.keySet().stream().sorted((i,j)->mapCount.get(j).compareTo(mapCount.get(i))).
				collect(Collectors.toList()).get(0);
		
	}
	
	
	Map<String, Period> getDuration(List<Employee> empList) {
		 Map<String, Period> vs = empList.stream().collect(Collectors.toMap(i->i.getFirstName()+
				 " "+i.getLastName(),i->Period.between(i.getHireDate(), LocalDate.now())));
				 return vs;
		
	}
	
	
	private Map<String,Long> getCountofEmps(List<Employee> empList) {
		return (empList.stream().collect(Collectors.groupingBy((Employee e)->e.getDepartment().
				getDepartmentName(), Collectors.counting())));
	}
	
	
	private List<Employee> getAfterIncrease(List<Employee> empList) {
		return empList.stream().map(i -> {
									double newsal = i.getSalary()*1.15;
									 i.setSalary(newsal);
									 return i;
				}).collect(Collectors.toList());
		}
	
	private List<String> sortedList(List<Employee> empList) {
		List<Employee> flist = empList.stream().sorted((o1,o2)->o1.getEmpId().
				compareTo(o2.getEmpId())).sorted((o1,o2)->o1.getDepartment().
						getDepartmentId().compareTo(o2.getDepartment().getDepartmentId())).
				sorted((o1,o2)->o1.getFirstName().compareTo(o2.getFirstName())).collect(Collectors.toList());
		List<String> names = flist.stream().map(Employee::getFirstName).collect(Collectors.toList());
		return names;
	}
	
	
	private String seniorMost(List<Employee> empList) {
		List<Employee> sortedEmp = empList.stream().sorted((o1,o2)->o1.getHireDate().
				compareTo(o2.getHireDate())).collect(Collectors.toList());
		return (sortedEmp.get(0).getFirstName() +" "+sortedEmp.get(0).getLastName());
	}
	
	
	private double sumSalaries(List<Employee> empList) {
		return empList.stream().mapToDouble(emp->emp.getSalary()).sum();
		
	}

}
