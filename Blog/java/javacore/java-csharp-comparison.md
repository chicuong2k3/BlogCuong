# Learning Java for C# Developer

## Overview

| Feature                  | C#                             | Java                         |
|--------------------------|--------------------------------|------------------------------|
| Operator Overloading     | Yes                            | No                           |
| Pointers                 | Yes                            | No                           |
| Runtime Environment      | Common Language Runtime (CLR)  | Java Virtual Machine (JVM)   |
| Cross-Platform Support   | Yes                            | Yes                          |
| Garbage Collection       | Yes                            | Yes                          |
| Strongly-Typed           | Yes                            | Yes                          |
| Properties               | Yes                            | No                           |
| Event                    | Yes                            | No                           |
| Indexer                  | Yes                            | No                           |
| Preprocessor Directives  | Yes                            | No                           |
| Aliases (using/import)   | Yes                            | No                           |
| Pass by Reference        | Yes                            | No                           |
| Verbatim Strings         | Yes                            | Yes (Raw String Literals)    |
| String Interpolation     | Yes                            | Yes                          |
| Explicit Interface Impl  | Yes                            | No                           |
| Partial Types            | Yes                            | No                           |
| Extension Methods        | Yes                            | Yes (via Manifold or Lombok) |
| Nullable Types           | Yes                            | No                           |
| Anonymous Methods        | Yes                            | Yes (from Java 8)            |
| Anonymous Inner Classes  | No                             | Yes                          |

## Syntax

| C# Keyword  | Java Keyword |
|-------------|--------------|
| extern      | native       |
| operator    | N/A          |
| as          | N/A          |
| out         | N/A          |
| base        | super        |
| override    | N/A          |
| bool        | boolean      |
| fixed       | N/A          |
| params      | ...          |
| typeof      | N/A          |
| partial     | N/A          |
| uint        | N/A          |
| byte        | N/A          |
| ulong       | N/A          |
| unchecked   | N/A          |
| get         | N/A          |
| unsafe      | N/A          |
| readonly    | N/A          |
| ushort      | N/A          |
| checked     | N/A          |
| ref         | N/A          |
| using       | import       |
| implicit    | N/A          |
| value       | N/A          |
| in          | N/A          |
| sbyte       | byte         |
| virtual     | N/A          |
| sealed      | final        |
| decimal     | N/A          |
| set         | N/A          |
| where       | extends      |
| delegate    | N/A          |
| is          | instanceof   |
| sizeof      | N/A          |
| lock        | synchronized |
| stackalloc  | N/A          |
| yield       | N/A          |
| :           | extends      |
| namespace   | package      |
| string      | N/A          |
| :           | implements   |
| struct      | N/A          |
| N/A         | strictfp     |
| event       | N/A          |
| N/A         | throws       |
| explicit    | N/A          |
| object      | N/A          |
| N/A         | transient    |


> [!NOTE]
> The **[NonSerialized] attribute** in C# is equivalent to the **transient** keyword in Java.


## Equivalent Technologies


| Technology | C# | Java |
|---|---|---|
| ORM | Entity Framework | Hibernate |
| Database Access | ADO.NET | JDBC |
| ORM | Entity Framework | Hibernate |


## The same but different

### Packages vs. Namespaces

In Java, the package names dictate the directory structure of source files in an application whereas in C# namespaces do not dictate the physical layout of source files in directories only their logical structure.

### Primitive Types

The **byte** type in Java is analogous to **sbyte** type in C#.

In Java, the **decimal** type doesn't exist.

### Array Declarations


```java
// Java
int[] arr = new int[100]; // valid
int arr[] = new int[100]; // valid
```

```csharp
// C#
int[] arr = new int[100]; // valid
int arr[] = new int[100]; // ERROR: Won't compile
```

### switch Statement

Unlike Java, in C#, switch statements support the use of string literals and do not allow fall-through unless the label contains no statements.

### Base Class Constructors

```java
// Java 
class B extends A {
    public B() {
        super();
    }
}
```

```csharp
// C# 
class B : A 
{
    public B() : base() 
    {
        
    }
}
```

### Parameter Lists and ForEach loop

```java
// Java
public static void printInts(Integer... args) {
    for (int num: args) {

    }
}
```

```csharp
// C#
public static void PrintInts(params int[] args) 
{
    foreach (int num in args)
    {

    }
}
```

### Access Modifiers

| C# Access Modifier   | Java Access Modifier |
|----------------------|----------------------|
| private              | private              |
| public               | public               |
| internal             | protected            |
| protected            | N/A                  |
| internal protected   | N/A                  |

> [!NOTE]
> The default accessibility of a C# field or method when no access modifier is specified is **private** while in Java it is ***protected*** (it means that the derived classes can inherit the field by default in Java).

### Importing Libraries

- First the needed libraries must be referenced somewhere in the source file which is done via the **import** keyword in Java. 
- There must be a way to tell the compiler where to find the location of the needed library. Specifying the location of libraries that will be used by a Java program is done using the CLASSPATH environment variable or the -classpath compiler option.

### Generics

In Java, the generic functionality is implemented using type erasure. Specifically the generic type information is present only at compile time, after which it is erased by the compiler and all the type declarations are replaced with **Object**. The compiler then automatically inserts casts in the right places. The reason for this approach is that it provides total interoperability between generic code and legacy code that doesn't support generics.

> [!NOTE]
> The main problem with type erasure is that the generic type information is not available at run time via reflection or run time type identification.

> [!CAUTION]
> Generic data structures types must always be declared using objects and not primitive types.

In C#, when the generic type is compiled, the generated IL contains placeholders for specific types. At runtime, when an initial reference is made to a generic type (e.g. List<int>) the system looks to see if anyone already asked for the type or not. If the type has been previously requested, then the previously generated specific type is returned. If not, the JIT compiler instantiates a new type by replacing the generic type parameters in the IL with the specific type (e.g. replacing List<T> with List<int>). It should be noted that if the requested type is a reference type as opposed to a value type then the generic type parameter is replaced with **Object**. However, **there is no casting done internally by the .NET runtime when accessing the type**.

The mechanism for specifying a method that can operate on data structures containing any type in C# is via a feature called *generic type inferencing* while in Java this is done using *wildcard types*.

```java
// Java
public static void doSomething(Stack<?> stack) {

}
```

```csharp
// C#
public static void DoSomething<T>(Stack<T> stack) {
    
}
```

In C# there are three types of constraints that can be applied to generic types:
- A derivation constraint indicates to the compiler that the generic type parameter derives from a base type such an interface or a particular base class.
- A default constructor constraint indicates to the compiler that the generic type parameter exposes a public default constructor.
- A reference/value type constraint constrains the generic type parameter to be a reference or a value type.

In Java, only the derivation constraint is supported.

```java
public static void doSomething(Stack<? extends BaseClass> stack) {

}
```

### Constants

In Java, to declare constants the **final** keyword is used. Final variables can be set either at compile time or run time. Final members can be left uninitialized when declared but then must be defined in the constructor.

In C# to declare constants the **const** keyword is used for compile time constants while the **readonly** keyword is used for runtime constants.

> [!NOTE]
> Java also supports having final parameters to a method. It allow arguments to a method to be accessible from within inner classes declared in the method body. This functionality is non-existent in C#.

## Nested Classes

In Java there are 2 kinds of nested classes: non-static nested classes also known as inner classes and static nested classes. 

The nested classes in C# are equivalent to Java's static nested classes. C# has nothing analogous to Java's inner classes. 

> [!NOTE]
> In Java a nested class can be declared in any block of code including methods, this is not the case in C#.

### Metadata Annotations

A key difference between C# attributes and Java annotations is that one can create meta-annotations (i.e. annotations on annotations) in Java but can not do the same in C#.

```java
import java.lang.annotation.*;
import java.lang.reflect.*;

@Documented 
@Retention(RetentionPolicy.RUNTIME)
@interface AuthorInfo{
    String author();
}

@AuthorInfo(author="Author")
class HelloWorld {

}

public class Test {

    public static void main(String[] args) throws Exception {
     
        Class c = Class.forName("HelloWorld");
        AuthorInfo a = (AuthorInfo) c.getAnnotation(AuthorInfo.class); 
        
        System.out.println("Author Information for " + c);
        System.out.println("Author: " + a.author());

    }
}
```

```csharp
// C#
using System; 
using System.Reflection;

[AttributeUsage(AttributeTargets.Class)]
public class AuthorInfoAttribute : Attribute
{
    string author; 
    public AuthorInfoAttribute(string author)
    {
        this.author = author;
    }
    public string Author { get; private set; }

}

[AuthorInfo("Author")]
class HelloWorld
{

}

class Test
{
    public static void Main(string[] args)
    {   
        Type t = typeof(HelloWorld); 

        Console.WriteLine("Author Information for " + t);
        foreach(AuthorInfoAttribute att in t.GetCustomAttributes(typeof(AuthorInfoAttribute), false))
        {
            Console.WriteLine("Author: " + att.Author); 
        }
    }
}
```

### Enumerations

In Java, enumerated types are a full fledged class which means they are typesafe and can be extended by adding methods, fields or even implementing interfaces. Whereas in C#, an enumerated type is simply syntactic sugar around an integral type (typically an int) meaning they cannot be extended and are not typesafe.


### Finalizers vs. Deconstructors

Java finalizers has the concept of finalizers which have the same semantics as C# destructors.
In C#, destructors automatically call the base class finalizer after executing which is not the case in Java.


```java
// Java
public class A {
    public void finalize() {

    }
}
```

```csharp
// C#
public class A
{
    ~A()
    {

    }
}
```

### Synchronizing Methods

Both C# and Java support the concept of synchronized methods. Synchronized methods are marked in Java by using the **synchronized** keyword while in C# it is done by annotating the method with the **[MethodImpl(MethodImplOptions.Synchronized)]** attribute.

### Main Method

Main method in C# can have a *void* parameter while it is not the case in Java.


```java
// Java
using System; 
class A {
    public static void main(String[] args) {
        System.out.println("Hello World"); 
    }
} 
```

In Java, a class is the unit of compilation so all one has to do is invoke the specific class one wants run via the command line to run its main method. In C# one can get the same effect by compiling the application with the /main switch to specify which main should be used as the starting point of the application when the executable is created.

In Java's favor, one doesn't have to recompile to change which main is used by the application while a recompile is needed in a C# application. However, Java doesn't support conditional compilation, so the main method will be part of your released classes.

```powershell
csc /main:A /out:example.exe A.cs B.cs 
example.exe
Hello World from class A
csc /main:B /out:example.exe A.cs B.cs 
example.exe
Hello World from class B
```

```powershell
javac A.java B.java
java A
Hello World from class A
java B
Hello World from class B
```

### Reflection

Reflection in C# is done at the assembly level while reflection in Java is done at the class level. 

Since assemblies are typically stored in DLLs, one needs the DLL containing the targeted class to be available in C# while in Java one needs to be able to load the class file for the targeted class.

### Exception Handling

Only Java exceptions have methods that allow one to alter the stack trace.

### Multithreads and Volatile --NOT COMPLETED

Java threads are created by subclassing the **java.lang.Thread** class and overriding its **run()** method or by implementing the **java.lang.Runnable** interface and implementing the **run()** method.

In Java, every class inherits the wait(), notify() and notifyAll() from **java.lang.Object** which are used for thread operations.

> [!IMPORTANT]
> There are major differences in the semantics of ***volatile*** in Java and C#.

### Collections

The Java collections framework consists of a large number of the classes and interfaces in the java.util package. Instead of having a separate namespace for generic collections, the collections in the **java.util** package have been retrofitted to support generics.

### Virtual Methods

In Java all methods are virtual methods while in C#, one must explicitly state which methods one wants to be virtual.

In Java, mark methods as **final** to make it cannot be overridden by derived classes. Java disallows the derived class from containing a method that has the same signature as the **final** base class method.

### Object Serialization

In Java, serializable objects are those that implement the **Serializable** interface while the **transient** keyword is used to mark members of a Java class as ones not to be serialized.

```java
import java.io.*; 

class SerializeTest implements Serializable {

    transient int x; 
    private int y; 
    public SerializeTest(int a, int b) {
        x = a; 
        y = b; 
    }

    public String toString() {
        return "{ x=" + x + ", y=" + y + " }"; 
    }

    public static void main(String[] args) throws Exception {

        SerializeTest st = new SerializeTest(66, 61); 
        System.out.println("Before Write := " + st);

        System.out.println("\n Writing SerializeTest object to disk");
        FileOutputStream out  = new FileOutputStream("serialized.txt");
        ObjectOutputStream so = new ObjectOutputStream(out);    
        so.writeObject(st);
        so.flush();

        System.out.println("\n Reading SerializeTest object from disk\n");
        FileInputStream in     = new FileInputStream("serialized.txt");
        ObjectInputStream si   = new ObjectInputStream(in); 
        SerializeTest fromdisk = (SerializeTest)si.readObject();

        /* x will be 0 because it won't be read from disk since transient */ 
        System.out.println("After Read := " + fromdisk);
    }
}
```

### Events

There is no general mechanism for event handling in Java.

An event is typically a subclass of the **java.util.EventObject** class, which has methods that enable setting or getting of the object that was the source of the event. A subscriber in the Java model usually implements an interface that ends with the word Listener (e.g. MouseListener, ActionListener, KeyListener, etc) which should contain a callback method that would be called by the publisher on the occurrence of the event. The publisher typically has a method that begins with add and ends with Listener (e.g. addMouseListener, addActionListener, addKeyListener, etc) which is used to register subscribers.



