---
title:  SOLID Principles
tags: ['Design Principle']
---

SOLID là viết tắt của 5 chữ cái đầu trong 5 nguyên tắc thiết kế hướng đối tượng. Giúp cho lập trình viên viết ra những đoạn code dễ đọc, dễ hiểu, dễ bảo trì. 5 nguyên tắc đó gồm:

- S: Single Responsibility Principle (SRP)
- O: Open-closed Principle (OCP)
- L: Liskov substitution Principle (LSP)
- I: Interface Segregation Principle (ISP)
- D: Dependency Inversion Principle (DIP)

## Single Responsibility Principle (SRP)


Một class chỉ nên giữ 1 trách nhiệm duy nhất, chỉ có thể sửa đổi class với 1 lý do duy nhất.


Sau đây là 1 ví dụ cho nguyên tắc này:
```csharp
public class Employee
{
    public int Employee_Id { get; set; }
    public string Employee_Name { get; set; }

    public bool InsertIntoEmployeeTable(Employee em)
    {
        // Insert into employee table
        return true;
    }

    public void GenerateReport(Employee em)
    {
        // Report generation with employee data using crystal report
    }
}
```

Đoạn code trên không tuân thủ SRP vì Employee có 2 trách nhiệm, một là thao tác với cơ sở dữ liệu và hai là tạo báo cáo. Employee không nên đảm nhận việc tạo báo cáo vì giả sử đến một ngày nào đó khách hàng yêu cầu phải tạo ra báo cáo định dạng xml hoặc bất cứ định dạng nào khác thì phải chỉnh sửa class này.

Cần tách phương thức GenerateReport ra khỏi Employee như sau:

```csharp
public class ReportGeneration
{
    public void GenerateReport(Employee em)
    {
        // Report reneration with employee data.
    }
}
```

## Open/Closed Principle (OCP)


Có thể thoải mái mở rộng 1 class (bằng cách kế thừa), nhưng không được sửa đổi bên trong class đó.


Nguyên tắc này nói rằng chúng ta nên thiết kế class sao cho có thể dễ dàng thêm chức năng mới (bằng inheritance), và không cần phải sửa class đã hoạt động tốt

Giả sử chúng ta có Class hình chữ nhật và Class hình tròn
```csharp
public class Rectangle 
{
    public double Height { get;set; }
    public double Wight { get;set; }
}
public class Circle 
{
    public double Radius { get;set; }
}
```

Chúng ta muốn tính diện tích của 1 tập hợp hình chữ nhật
```csharp
public class AreaCalculator 
{
    public double TotalArea(Rectangle[] arrRectangles)
    {
        double area;
        foreach(var objRectangle in arrRectangles)
        {
             area += objRectangle.Height * objRectangle.Width;
        }
        return area;
    }
}
```

Bây giờ chúng ta muốn tính diện tích của các hình tròn nữa chúng ta có thể làm như sau
```csharp
public class AreaCalculator
{
    public double TotalArea(object[] arrObjects)
    {
        double area = 0;
        Rectangle objRectangle;
        Circle objCircle;
        foreach(var obj in arrObjects)
        {
            if (obj is Rectangle)
            {
                area += obj.Height * obj.Width;
            }
            else
            {
                objCircle = (Circle)obj;
                area += objCircle.Radius * objCircle.Radius * Math.PI;
            } 
        }
        return area;
    }
}
```

Giờ nếu muốn tính diện tích của hình tam giác, chúng ta phải thêm vào 1 câu lệnh if nữa. Do đó vi phạm nguyên tắc OCP (không nên sửa class). Để khắc phục điều này chúng ta sử dụng interface hoặc abstract class thay vì sử dụng concrete class:

```csharp
public abstract class Shape
{
    public abstract double Area();
}

public class Rectangle : Shape
{
    public double Height { get;set; }
    public double Wight { get;set; }
    public override double Area() 
    {
        return Height * Width;
    }
}
public class Circle : Shape
{
    public double Radius { get;set; }
    public override double Area() 
    {
        return Radius * Radus * Math.PI;
    }
}
```

Và việc tính diện tích bây giờ khá đơn giản nhờ vào tính đa hình

```csharp
public class AreaCalculator
{
    public double TotalArea(Shape[] arrShapes)
    {
        double area = 0;
        foreach(var objShape in arrShapes)
        {
            area += objShape.Area();
        }
        return area;
    }
}
```

Và bây giờ khi cần tính diện tích của hình khác không cần phải sửa đổi Class AreaCalculator nữa

Nguyên lí này cũng có thể đạt được bằng cách áp dụng Dependency Injection, Strategy Pattern, Factory Pattern

## Liskov Substitution Principle (LSP)


Các object của class con có thể thay thế object của class cha mà không làm thay đổi tính đúng đắn của chương trình


Chúng ta có 2 Class là FemaleStudent và MaleStudent kế thừa từ Class Student. Trường quy định chỉ có sinh viên nam mới được học môn đá bóng. Chúng ta có thể ném Exception trong method JoinSoccerCourse của class FemaleStudent

```csharp
public class Student
{
    public string Name { get; set; }
    public abstract void JoinSoccerCourse();
}

public class FemaleStudent : Student
{
    public override void JoinSoccerCourse()
    {
        throw new Exception("Nữ không được học đá bóng");
    }
}

public class MaleStudent : Student
{
    public override void JoinSoccerCourse()
    {
        // Đăng kí môn học
    }
}
```

Đoạn code sau không gây ra lỗi biên dịch nhưng sẽ có lỗi runtime:

```csharp
List<Student> studentsJoinSoccerCourse = new List<Student>();
studentsJoinSoccerCourse.Add(new MaleStudent());
studentsJoinSoccerCourse.Add(new FemaleStudent());
foreach (Student s in studentsJoinSoccerCourse)
{
   s.JoinSoccerCourse();
}
```

Cách thiết kế trên đã không tuân thủ LSP, chúng ta cần thiết kế sao cho chương trình báo lỗi ngay khi biên dịch, tránh xảy ra lỗi runtime

Cần có 1 interface cho việc đăng kí khóa học đá bóng

```csharp
public class Student
{
    public string Name { get; set; }
}

public interface ISoccerCourse
{
    void JoinSoccerCourse();
} 

public class FemaleStudent : Student
{
}

public class MaleStudent : Student, ISoccerCourse
{
    public override void JoinSoccerCourse()
    {
        // Đăng kí môn học
    }
}
```

```csharp
List<ISoccerCourse> studentsJoinSoccerCourse = new List<ISoccerCourse>();
studentsJoinSoccerCourse.Add(new MaleStudent());
studentsJoinSoccerCourse.Add(new FemaleStudent()); // dòng này sẽ báo lỗi biên dịch
foreach (var s in studentsJoinSoccerCourse)
{
   s.JoinSoccerCourse();
}
```



Một số dấu hiệu điển hình có thể chỉ ra rằng LSP đã bị vi phạm:
- Các lớp con có các phương thức ghi đè phương thức của lớp cha nhưng với chức năng hoàn toàn khác.
- Các lớp con có phương thức ghi đè phương thức của lớp cha là một phương thức rỗng.
- Các phương thức bắt buộc kế thừa từ lớp cha ở lớp con nhưng không được sử dụng.
- Phát sinh Exception trong phương thức của lớp con.



Trong thực tế, A là B (hình vuông là hình chữ nhật) không có nghĩa là class A nên kế thừa class B. Chỉ cho class A kế thừa class B khi class A thay thế được cho class B.


## Interface Segregation Principle (ISP)


Thay vì dùng 1 interface lớn, nên tách thành nhiều interface nhỏ, với nhiều mục đích cụ thể


Giả sử chúng ta cần xây dựng một ứng dụng quản lí cho một công ty CNTT có các vai trò như TeamLead và Programmer, trong đó TeamLead chia một công việc lớn thành các công việc nhỏ hơn và giao chúng cho các Programmer hoặc có thể tự làm chúng.

```csharp
public interface ILead
{
    void CreateSubTask();
    void AssginTask();
    void WorkOnTask();
}
public class TeamLead : ILead
{
    public void AssignTask()
    {
        // Code to assign a task.
    }
    public void CreateSubTask()
    {
        // Code to create a sub task
    }
    public void WorkOnTask()
    {
        // Code to implement perform assigned task.
    }
}
```

Giờ chúng ta cần thêm Manager. Manager chỉ chia và giao công việc nhưng không làm chúng

```csharp
public class Manager : ILead
{
    public void AssignTask()
    {
        // Code to assign a task.
    }
    public void CreateSubTask()
    {
        // Code to create a sub task
    }
    public void WorkOnTask()
    {
        throw new Exception("Manager can't work on Task");
    }
}
```

Vì Manager triển khai ILead nên nó buộc phải implement WorkOnTask(), vì Manager không làm công việc nên chúng ta ném ra Exception. Việc này vi phạm ISP. Chúng ta sẽ thiết kế lại như sau

Vì chúng ta có ba vai trò, Manager chỉ có thể phân chia và giao công việc, TeamLead có thể phân chia, giao công việc và thực hiện chúng. Chúng ta cần phân chia trách nhiệm bằng cách tách interface ILead dành cho Programmer chỉ có thể làm công việc (WorkOnTask)

```csharp
public interface IProgrammer
{
    void WorkOnTask();
}
```

```csharp
public interface ILead
{
    void AssignTask();
    void CreateSubTask();
}
```

```csharp
public class Programmer: IProgrammer
{
    public void WorkOnTask()
    {
        // code to implement to work on the Task.
    }
}

public class Manager : ILead
{
    public void AssignTask()
    {
        // Code to assign a task.
    }
    public void CreateSubTask()
    {
        // Code to create a sub task
    }
}

public class TeamLead : ILead, IProgrammer
{
    public void AssignTask()
    {
        // Code to assign a task.
    }
    public void CreateSubTask()
    {
        // Code to create a sub task
    }
    public void WorkOnTask()
    {
        // Code to implement perform assigned task.
    }
}
```

## Dependency Inversion Principle (DIP)


1. Các module/class cấp cao không nên phụ thuộc vào các module/class cấp thấp. Cả 2 nên phụ thuộc vào abstraction.
2. Abstraction không nên phụ thuộc vào Detail. Detail nên phụ thuộc vào Abstraction
(Các class giao tiếp với nhau thông qua interface, không phải thông qua Implementation)


Chúng ta cần hiểu rõ một vài thứ

Module cấp cao là module mà phụ thuộc vào (dependency) module khác. Để hiểu dependency đọc [Denpendency Injection]( https://chicuongk3.github.io/2023/09/14/dependency-injection/)
Abstraction là interface hoặc abstract class


Chúng ta đi đến 1 ví dụ

```csharp
public class Employee
{
    public int ID { get; set; }
    public string Name { get; set; }
    public string Department { get; set; }
    public int Salary { get; set; }
}
```

```csharp
public class EmployeeDataAccessLogic
{
    public Employee GetEmployeeDetails(int id)
    {
        // In real time get the employee details from database
        // but here we have hard coded the employee details
        Employee emp = new Employee()
        {
            ID = id,
            Name = "Pranaya",
            Department = "IT",
            Salary = 10000
        };
        return emp;
    }
}

public class DataAccessFactory
{
    public static EmployeeDataAccessLogic GetEmployeeDataAccessObj()
    {
        return new EmployeeDataAccessLogic();
    }
}

public class EmployeeBusinessLogic
{
    EmployeeDataAccessLogic _EmployeeDataAccessLogic;
    public EmployeeBusinessLogic()
    {
        _EmployeeDataAccessLogic = DataAccessFactory.GetEmployeeDataAccessObj();
    }
    public Employee GetEmployeeDetails(int id)
    {
        return _EmployeeDataAccessLogic.GetEmployeeDetails(id);
    }
}
```



Trong ví dụ này, EmployeeBusinessLogic phụ thuộc vào EmployeeDataAccessLogic nên EmployeeBusinessLogic là module cấp cao và EmployeeDataAccessLogic là module cấp thấp

Đoạn code trên đã vi phạm DIP vì EmployeeBusinessLogic và EmployeeDataAccessLogic không phụ thuộc vào abstraction (cả 2 không implement từ 1 interface hay kế thừa từ abstract class nào)

Chúng ta sửa lại đoạn code trên để nó tuân theo DIP

```csharp
public interface IEmployeeDataAccessLogic
{
    Employee GetEmployeeDetails(int id);
    //Any Other Employee Related Method Declarations
}

// EmployeeDataAccessLogic implement IEmployeeDataAccessLogic
public class EmployeeDataAccessLogic : IEmployeeDataAccessLogic
{
    public Employee GetEmployeeDetails(int id)
    {
        // In real time get the employee details from database
        // but here we have hard coded the employee details
        Employee emp = new Employee()
        {
            ID = id,
            Name = "Pranaya",
            Department = "IT",
            Salary = 10000
        };
        return emp;
    }
}

// chuyển kiểu trả về của GetEmployeeDataAccessObj thành IEmployeeDataAccessLogic
public class DataAccessFactory
{
    public static IEmployeeDataAccessLogic GetEmployeeDataAccessObj()
    {
        return new EmployeeDataAccessLogic();
    }
}

// chuyển kiểu của _EmployeeDataAccessLogic thành IEmployeeDataAccessLogic
public class EmployeeBusinessLogic
{
    IEmployeeDataAccessLogic _EmployeeDataAccessLogic;
    public EmployeeBusinessLogic()
    {
        _EmployeeDataAccessLogic = DataAccessFactory.GetEmployeeDataAccessObj();
    }
    public Employee GetEmployeeDetails(int id)
    {
        return _EmployeeDataAccessLogic.GetEmployeeDetails(id);
    }
}
```

Bây giờ EmployeeBusinessLogic(module cấp cao) và EmployeeDataAccessLogic(module cấp thấp) đều phụ thuộc vào IEmployeeDataAccessLogic(Abstraction). Thỏa điều kiện 1
Và IEmployeeDataAccessLogic(Abstraction) không phụ thuộc vào EmployeeDataAccessLogic(Detail), nhưng EmployeeDataAccessLogic(Detail) phụ thuộc vào IEmployeeDataAccessLogic(Abstraction). Thỏa điều kiện 2

Khi đã áp dụng DIP, nếu muốn thay đổi EmployeeDataAccessLogic bằng 1 class khác, chỉ cần tạo 1 class implement interface IEmployeeDataAccessLogic mà không cần thay đổi EmployeeBusinessLogic.

