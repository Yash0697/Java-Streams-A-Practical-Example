package streamapicasestudy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeRepository {
	private static List<Employee> empList = new ArrayList<Employee>();
	public void dataCreator() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("EmpData.txt"));
			String line = reader.readLine();
			while (line != null) {
				String[] data = line.split(",");
				Employee e = new Employee();
				e.setEmpId(Integer.parseInt(data[0]));
				e.setFirstName(data[1]);
				e.setLastName(data[2]);
				e.setEmail(data[3]);
				e.setPhoneNumber(data[4]);
				Pattern p = Pattern.compile("([0-9]{4})-([0-9]{2})-([0-9]{2})");
				Matcher m  = p.matcher(data[5]);
				while(m.find())
				{
					int year = Integer.parseInt(m.group(1));
					int month = Integer.parseInt(m.group(2));
					int day = Integer.parseInt(m.group(3));
					LocalDate date = LocalDate.of(year, month, day);
					e.setHireDate(date);
				}
				e.setDesignation(data[6]);
				e.setSalary(Double.parseDouble(data[7]));
				e.setManagerId(Integer.parseInt(data[8]));
				Department dept = new Department();
				dept.setDepartmentId(Integer.parseInt(data[9]));
				dept.setDepartmentName(data[10]);
				dept.setManagerId(Integer.parseInt(data[11]));
				e.setDepartment(dept);
				empList.add(e);
				line = reader.readLine();
			}
			reader.close();
		}catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	public List<Employee> getEmpList() {
		return empList;
	}

}
