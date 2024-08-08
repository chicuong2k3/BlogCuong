---
title: Tổng hợp C#
tags: ['C#']
---

# String

```cs
string str1 = "Hello";
string str2 = "Hello";
string str3 = new string(str1);
fixed (char* p1 = str1)
{
    System.Console.WriteLine((ulong)p1);
}

fixed (char* p2 = str2)
{
    System.Console.WriteLine((ulong)p2);
}

fixed (char* p3 = str3)
{
    System.Console.WriteLine((ulong)p3);
}


string verbatimString = @"C:\Home";

// compare 2 strings
string s1 = "hello";
char[] characters = { 'h', 'e', 'l', 'l', 'o' };
string s2 = "hello";
string s3 = new string(characters);

// == operator and Equals method is the same
// https://www.tutorialsteacher.com/articles/compare-strings-in-csharp
System.Console.WriteLine($"s1.Equals(s2): {s1.Equals(s2)}");
System.Console.WriteLine($"s1 == s2: {s1 == s2}");
System.Console.WriteLine($"{string.Equals(s1, s2)}");
System.Console.WriteLine($"{s1.CompareTo(s2)}");

// String Builder is in the System.Text namespace
System.Console.WriteLine("String Builder");
StringBuilder buffer1 = new StringBuilder(); // default capacity
StringBuilder buffer2 = new StringBuilder(12); // specified capacity
StringBuilder buffer3 = new StringBuilder("StringBuilder");
buffer3.Append(characters, 2, 2);

string myString = "This {0} costs {1:C}\n";
object[] parameters = new object[] { "car", 1000.5 };
buffer1.AppendFormat(myString, parameters);
System.Console.WriteLine(buffer1);

// Insert, Remove, Replace
buffer1.Insert(15, new char[] { 'e', 'x', 'p', 'e', 'n', 's', 'i', 'v', 'e' });
buffer1.Remove(1, 4);

StringBuilder replaceStr = new StringBuilder("this is a test");
replaceStr.Replace("t", "T", 0, 5);
System.Console.WriteLine(replaceStr);


// Regular Expression
string test = "Regular expresssions are sometimes called regex or regexp";
Regex expression = new Regex("e");
System.Console.WriteLine($"Match \'e\' in the testString: {expression.Match(test)}");

System.Console.WriteLine("Match \"regex\" in the testString");
foreach (Match match in Regex.Matches(test, "regexp?"))
{
    System.Console.Write($"{match} ");
}

// Match either cat or hat: (c|h)at

string testString = "abc, DEF, 123";

// Quantifiers are greedy—they match the longest possible occurrence of the pattern
// follow a quantifier with a question mark (?) to make it lazy
foreach (var match in Regex.Matches(testString, @"\w+"))
{
    System.Console.WriteLine($"{match}");
}
foreach (var match in Regex.Matches(testString, @"\w+?"))
{
    System.Console.WriteLine($"{match}");
}



// Quantifier
// * : 0 or more | colorpanel: escape * with \
// + : 1 or more
// ? : 0 or 1
// {n} : exactly n
// {n,} : at least n
// {n,m} : between n and m (inclusive)


// Some patterns
// . : any single character except newline
// \w : any word character = [a-zA-Z_0-9]
// \d : any digit = [0-9]
// \D : [^0-9]
// \s : any whitespace = [\t\n\x0B\f\r]
// ^ : mark the beginning of a line
// $ : mark the ending of a line
// | : equivalent to OR
// \b"word" : specify word boundary. Ex: never matches er\b but verb don't 


// Character class
// [afd] : any of a, f, or d
// [a-z] : any lower character from a to z
// [^a-z] : any character isn't in the range a-z
// character-class subtraction [\d-[4]]


// (?=) : lookahead. Ex: The(?=\sfat)   lấy ra The mà theo sau là " fat"
// (?!) : !lookahead. Ex: The(?!\sfat)  lấy ra The mà theo sau không là " fat"
// (?<=) : lookbehind. Ex: (?<=The\s)(fat)   lấy ra fat mà phía trước là "The "
// (?<!) : !lookbehind. Ex: (?<!The\s)(fat)   lấy ra fat mà phía trước không là "The "


// Replace and Split static method
string testStr = "This sentence ends in 5 stars *****";
string digitString = "1, 2, 3, 4, 5, 6, 7, 8,     9";
string testStr1 = Regex.Replace(testStr, @"\*", "^");
System.Console.WriteLine(testStr1);
testStr1 = Regex.Replace(testStr, @"\w+", "cuong");
System.Console.WriteLine(testStr1);

Regex pattern = new Regex(@"\d");
string replacedString = pattern.Replace(digitString, "digit", 3);
System.Console.WriteLine(replacedString);

string[] result = Regex.Split(digitString, @",\s+");
foreach (var item in result)
{
    System.Console.WriteLine(item);
}

// (?<group name>subexpression) : Capture group
// \number : Backreference to group #number
// (?:subexpression) : Noncapturing group
string phoneNumber = "This is my info: Chi Cuong: 0974413825 Email";
Regex regex = new Regex(@"\s*(?<name>[\w\s]+):\s(?<phone>\d{10})");
var matches = regex.Matches(phoneNumber);
foreach (Match match in matches)
{
    System.Console.WriteLine($"Name: {match.Groups[1].Value}");
    System.Console.WriteLine($"Phone number: {match.Groups[2].Value}");
}
foreach (Match match in matches)
{
    System.Console.WriteLine($"Name: {match.Groups["name"].Value}");
    System.Console.WriteLine($"Phone number: {match.Groups["phone"].Value}");
}

// Common regex
// Email: ^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6})*$
// Whole + Decimal + Fraction: [-]?[0-9]+[,.]?[0-9]*([\/][0-9]+[,.]?[0-9]*)*
// Password:
// Should have 1 lowercase letter, 1 uppercase letter, 1 number, 1 special character and be at least 8 characters long
// (?=(.*[0-9]))(?=.*[\!@#$%^&*()\\[\]{}\-_+=~`|:;"'<>,./?])(?=.*[a-z])(?=(.*[A-Z]))(?=(.*)).{8,}
// Should have 1 lowercase letter, 1 uppercase letter, 1 number, and be at least 8 characters long
// (?=(.*[0-9]))((?=.*[A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z]))^.{8,}$
// URL: https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#()?&//=]*)
```

# Selenium

```cs
// dùng để ẩn cửa sổ console
ChromeDriverService service = ChromeDriverService.CreateDefaultService();
service.HideCommandPromptWindow = true; 

// dùng để ẩn cửa sổ Chrome
ChromeOptions chromeOptions = new ChromeOptions();
chromeOptions.AddArgument("--headless");

IWebDriver driver = new ChromeDriver(service, chromeOptions);
driver.Navigate().GoToUrl(url);

IWebElement element = driver.FindElement(By.ClassName(""));
element.SendKeys("");

// Xóa giá trị của phần tử chỉ định , nếu phần tử là văn bản
element.Clear();

element.Click();

// Xác định xem một phần tử hiện đang được hiển thị hay không
var displayed = element.Displayed;

// Xác định xem phần tử này có được chọn hay không
var selected = element.Selected;

// xác định vị trí của phần tử trên trang
var point = element.Location;

// trả về giá trị thuộc tính của phần tử
element.GetAttribute("src");

// Đóng cửa sổ hiện tại mà IWebDriver đang kiểm soát
driver.Close();

// Đóng tất cả các cửa sổ do IWebDriver mở
driver.Quit();
```

# Networking

## IP Address
```cs
// IPAddress (nằm trong System.Net namespace) đại diện cho IP Address trong IPv4 hoặc IPv6
IPAddress a1 = new IPAddress(new byte[] { 101, 102, 103, 104 });
IPAddress a2 = IPAddress.Parse("101.102.103.104");
IPAddress a3 = IPAddress.Parse("[3EA0:FFFF:198A:E4A3:4FF2:54fA:41BC:8D31]");

// IPEndPoint đại diện cho tổ hợp IP Address và Port 
IPAddress a = IPAddress.Parse("101.102.103.104");
IPEndPoint ep = new IPEndPoint(a, 2048);
```

## Uri
```cs
Uri info = new Uri ("http://www.domain.com:80/info/");
Uri page = new Uri ("http://www.domain.com/info/page.html");
Console.WriteLine(info.Host);
Console.WriteLine(info.Port); 
Console.WriteLine(page.Port); 
Console.WriteLine(info.IsBaseOf(page)); // True

Uri relative = info.MakeRelativeUri(page);
Console.WriteLine(relative.IsAbsoluteUri); // False
Console.WriteLine(relative.ToString()); // page.html

// Khởi tạo relative Uri
Uri u = new Uri ("page.html", UriKind.Relative);
```
## Dns
```cs
// Lấy hostname của máy local
var localHostname = Dns.GetHostName();
var uri = new Uri("https://www.bootstrapcdn.com/");
// Phân giải host hoặc IP thành đối tượng IPHostEntry
var hostEntry = Dns.GetHostEntry(uri.Host); 
IPAddress[] ipaddresses = hostEntry.AddressList;
```

## Ping
```cs
var ping = new Ping();
var pingReply = ping.Send("google.com.vn");
Console.WriteLine(pingReply.Status);
if (pingReply.Status == IPStatus.Success)
{
    Console.WriteLine(pingReply.RoundtripTime);
    Console.WriteLine(pingReply.Address);
}
```

## Http Client


Sử dụng cùng 1 instance để đạt hiệu suất tốt nhất

```cs
var client = new HttpClient();
var task1 = client.GetStringAsync("http://www.linqpad.net");
var task2 = client.GetStringAsync("http://www.albahari.com");
Console.WriteLine(await task1);
Console.WriteLine(await task2);

// truy cập các Property của HttpClient thông qua HttpClientHandler
var handler = new HttpClientHandler { UseProxy = false };
var client = new HttpClient(handler);
```

## Gửi GET Request với GetAsync


GetStringAsync, GetByteArrayAsync, và GetStreamAsync là những shortcut cho GetAsync

```cs
HttpResponseMessage response = await client.GetAsync("http://...");

// Ném Exception nếu truy vấn có mã trả về không thành công
response.EnsureSuccessStatusCode();

var statusCode = response.StatusCode;
// Đoạn text, mô tả thông tin cho mã trạng thái
var reasonPhrase = response.ReasonPhrase;

// Content: chứa content và các header liên quan đến content nếu có
string html = await response.Content.ReadAsStringAsync(); 
// method khác: ReadAsStreamAsync, ReadAsByteArrayAsync  

// Ghi vào file
using (var fileStream = new FileStream(filepath, FileMode.Create, FileAccess.Write, FileShare.None))
{
    await response.Content.CopyToAsync(fileStream);
}
```

## Gửi Request với SendAsync


GetAsync, PostAsync, PutAsync, and DeleteAsync là shortcuts cho SendAsync

```cs
var client = new HttpClient(new HttpClientHandler { UseProxy = false });
var request = new HttpRequestMessage(HttpMethod.Post, "http://...");
// httpRequestMessage.RequestUri = new Uri("http://...");


// sử dụng StringContent
string json = "{\"value1\": \"giatri1\", \"value2\": \"giatri2\"}";
request.Content = new StringContent(json, Encoding.UTF8, "application/json");

// sử dụng FormUrlEncodedContent
var parameters = new List<KeyValuePair<string, string>>();
parameters.Add(new KeyValuePair<string, string>("name1", "value1"));
parameters.Add(new KeyValuePair<string, string>("name2", "value2"));
request.Content = new FormUrlEncodedContent(parameters);

// sử dụng MultipartFormDataContent
var content = new MultipartFormDataContent();
Stream fileStream = System.IO.File.OpenRead("Program.cs");
content.Add(new StreamContent(fileStream), "fileupload", "abc.xyz");
content.Add(new StringContent("value1"), "key1");
httpRequestMessage.Content = content;


HttpResponseMessage response = await client.SendAsync(request);
response.EnsureSuccessStatusCode();
var responseContent = await response.Content.ReadAsStringAsync();
```


[Multipart Form Data](https://www.sobyte.net/post/2021-12/learn-about-http-multipart-form-data/)


## Header và Query String

```cs
var client = new HttpClient();
// DefaultRequestHeaders property dành cho header của mọi request
// Headers property của HttpRequestMessage dành cho header của 1 request cụ thể
client.DefaultRequestHeaders.UserAgent.Add(new ProductInfoHeaderValue("VisualStudio", "2022"));
client.DefaultRequestHeaders.Add("CustomHeader", "VisualStudio/2022");

// Query String
// tạo query string có kí tự đặc biệt hoặc khoảng trắng
string search = Uri.EscapeDataString("(HttpClient or HttpRequestMessage)"); 
string language = Uri.EscapeDataString("fr");
string requestURI = "http://www.google.com/search?q=" + search + "&hl=" + language;
```

## HttpClientHandler và Cookies

```cs
// AllowAutoRedirect: mặc định là true
// AutomaticDecompression: handler tự động giải nén/nén nội dung HTTP
// UseCookies: mặc định là true, cho phép sử dụng thuộc tính CookieContainer 
// để lưu các Cookie của server khi respone trả về, cũng như tự động gửi Cookie khi gửi truy vấn
HttpClientHandler handler = new HttpClientHandler();
CookieContainer cookies = new CookieContainer();
// cookies.Add(new Uri(url), new Cookie("name", "value"));
handler.CookieContainer = cookies;
var httpClient = new HttpClient(handler);

var request = new HttpRequestMessage(HttpMethod.Post, url);
request.Headers.Add("User-Agent", "Mozilla/5.0");
var parameters = new List<KeyValuePair<string, string>>()
{
    new KeyValuePair<string, string>("key1", "value1"),
    new KeyValuePair<string, string>("key2", "value2")
};
request.Content = new FormUrlEncodedContent(parameters);

var response = await httpClient.SendAsync(request);
```


[Vấn đề với CookieContainer](http://thomaskrehbiel.com/post/1690-cookiecontainer_httpwebrequest_and_secure_cookies/)


## Proxies

```cs
WebProxy p = new WebProxy("192.178.10.49", 808);
// nếu có cung cấp domain thì Windows-based authentication được sử dụng
// sử dụng CredentialCache.DefaultNetworkCredentials để sử dụng user hiện tại
p.Credentials = new NetworkCredential("username", "password", "domain");
var handler = new HttpClientHandler { Proxy = p };
var client = new HttpClient(handler);
```

## DelegatingHandler 

# Concurrency

## Thread

```cs
Thread t = new Thread(WriteY);
t.Start();
// Lấy Thread hiện tại
Thread currentThread = Thread.CurrentThread;
// Chờ 1 Thread kết thúc, trả về false nếu hết timeout mà chưa kết thúc
var result = t.Join(1000);

// Kiểm tra 1 Thread có đang bị block không
bool blocked = (someThread.ThreadState & ThreadState.WaitSleepJoin) != 0;
```

Thread.Sleep(0) nhường quyền sử dụng CPU cho Thread khác. Thread.Yield() giống vậy ngoại trừ việc chỉ nhường cho Thread chạy trên cùng CPU
Nếu thêm Thread.Yield() ở bất kì đâu mà làm chương trình lỗi thì chắc chắn code có bug


Một I/O-bound operation làm việc theo một trong 2 cách: 
- Chờ Thread hiện tại cho đến khi thao tác hoàn tất (như Console.ReadLine, Thread.Sleep hoặc ThreadJoin) 
- Thực thi bất đồng bộ, thực hiện callback khi operation kết thúc một thời gian sau đó


```cs
// Mỗi Thread có memory stack riêng nên có các local variable riêng biệt
new Thread(Go).Start();
Go();
void Go()
{
    for (int cycles = 0; cycles < 5; cycles++) 
        Console.Write ('A');
}

// output: AAAAAAAAAA

// Thread chia sẻ data nếu cùng reference tới 1 object hoặc 1 variable
bool done = false;
new Thread(Go).Start();
Go();
void Go()
{
    if (!done) 
    {
        done = true;
        Console.Write('A');
    }       
}
// output: A

// 2 Thread dùng chung field _done
var tt = new ThreadTest();
new Thread(tt.Go).Start();
tt.Go();
class ThreadTest
{
    bool _done;
    public void Go()
    {
        if (!_done) { _done = true; Console.WriteLine("Done"); }
    }
}

// static field dùng chung cho mọi Thread trong cùng CPU
class ThreadTest
{
    static bool _done;
    static void Main()
    {
        new Thread(Go).Start();
        Go();
    }
    static void Go()
    {
        if (!_done) { _done = true; Console.WriteLine("Done"); }
    }
}
```

## Locking và Thread Safety

```cs
class ThreadSafe
{
    static bool _done;
    static readonly object _locker = new object();
    static void Main()
    {
        new Thread(Go).Start();
        Go();
    }
    static void Go()
    {
        lock (_locker)
        {
            if (!_done) { Console.WriteLine("Done"); _done = true; }
        }
    }
}
```

## Truyền data cho Thread

```cs
void Print(string message) => Console.WriteLine(message);

Thread t = new Thread(() => {
    Print("Hello");
    Console.WriteLine ("Hi");
});
t.Start();

// cách khác
Thread t = new Thread(Print);
t.Start("Hello");
void Print(object message)
{
    string msg = (string)message;
    Console.WriteLine(msg);
}
```

## Lỗi Catured Variable

```cs
// 10 Thread cùng truy cập biến i đồng thời ?
for (int i = 0; i < 10; i++)
{
    new Thread(() => Console.Write(i)).Start();
}

// sửa
for (int i = 0; i < 10; i++)
{
    int threadId = i;
    new Thread(() => Console.Write(threadId)).Start();
}
```

## Exception Handling

```cs
// Exception được ném ra trong 1 Thread không liên quan đến Thread khởi động nó
try
{
    new Thread(Go).Start();
}
catch (Exception ex)
{
    Console.WriteLine("Exception caught: " + ex.Message);
}

void Go()
{
    throw null; // Ném null Exception
}

// Sửa
new Thread(Go).Start();

void Go()
{
    try
    {
        throw null; // Ném null Exception
    }
    catch (Exception ex)
    {
        Console.WriteLine("Exception caught: " + ex.Message);
    }
}
```

## Foreground và Background Thread


- Application chờ cho tất cả Foreground Thread kết thúc mới kết thúc
- Application kết thúc thì tất cả Background Thread đang chạy bị bắt kết thúc theo
- Mặc định 1 Thread là Foreground Thread


## Signaling

```cs
// false: bất kì thread nào gọi WaitOne với ManualResetEvent sẽ bị block cho đến khi nhận được signal
// true: bất kì thread nào gọi WaitOne với ManualResetEvent sẽ không bị block
var signal = new ManualResetEvent(false);
new Thread(() =>
{
    Console.WriteLine("Waiting for signal...");
    signal.WaitOne(); // chờ signal
    signal.Dispose();
    Console.WriteLine("Got signal!");
}).Start();

Thread.Sleep(2000);
signal.Set(); // gửi signal cho thread khác thực thi
```

## Thread Pool


- Khi khởi động 1 Thread luôn tốn 1 lượng thời gian nhỏ để tổ chức 1 vài thứ, Thread Pool giải quyết vấn đề này
- Thread Pool còn đảm bảo 1 công việc CPU-bound không gây ra oversubscription (số Thread > số CPU core => đòi hỏi context switch)
- Pool Thread luôn là Background Thread


```cs
Task.Run(() => Console.WriteLine("Hello"));

// Trước .NET Framework 4.0
ThreadPool.QueueUserWorkItem(notUsed => Console.WriteLine("Hello"));
```

## Synchronization Context


Marshal: Việc chuyển request cho UI Thread khi muốn cập nhật UI từ 1 Worker Thread (Thread chạy một operation tốn nhiều thời gian)


```cs
// SynchronizationContext cho phép thực hiện marshal
partial class MyWindow : Window
{
    SynchronizationContext _uiSyncContext;
    public MyWindow()
    {
        InitializeComponent();
        _uiSyncContext = SynchronizationContext.Current;
        new Thread(Work).Start();
    }
    void Work()
    {
        Thread.Sleep(5000);
        UpdateMessage("Message");
    }
    void UpdateMessage(string message)
    {
        _uiSyncContext.Post(_ => txtMessage.Text = message, null);
    }
}
```

# Task

## Starting a Task

```cs
// Mặc định Task sử dụng Pool Thread
// tương đương với new Thread(() => Console.WriteLine("Hello")).Start();
Task.Run(() => Console.WriteLine("Hello"));

// Wait giống với Join
Task task = Task.Run(() =>
{
    Thread.Sleep(2000);
    Console.WriteLine("Hello");
});

Console.WriteLine(task.IsCompleted); // False
task.Wait();

// thực hiện long-running CPU-bound work
Task task = new Task.Factory.StartNew(() => 
{
    Thread.Sleep(10000);
    Console.WriteLine("Hello");
}, TaskCreationOptions.LongRunning);

// return value
Task<int> task = Task.Run(() =>
{
    return 10;
});
int result = task.Result; // block thread hiện tại cho đến khi Task kết thúc
```

## Exception Handling


Nếu Task ném 1 unhandled Exception thì nó sẽ được tự động được ném tới bất kì Thread nào gọi Wait() hoặc Result property  


```cs
Task task = Task.Run(() => { throw null; });
try
{
    task.Wait();
}
catch (AggregateException aex)
{
    if (aex.InnerException is NullReferenceException)
        Console.WriteLine("Null!");
    else
        throw;
}
```

## Continuation


Continuation là việc tiếp tục thực hiện gì đó sau khi 1 công việc hoàn thành
Continuation thường được implement bằng callback (hàm mà được thực thi khi 1 công việc nào đó hoàn thành)


```cs
Task<int> primeNumberTask = Task.Run(() =>
{
    int count = Enumerable.Range(2, 3000000).Count(n => Enumerable.Range(2, (int)Math.Sqrt(n) - 1).All(i => n % i > 0));
    return count;
});

// awaiter là object mà có 2 method OnCompleted và GetResult và 1 propery là IsCompleted
var awaiter = primeNumberTask.GetAwaiter();

// hủy việc tự động marshal 
var awaiter = primeNumberTask.ConfigureAwait(false).GetAwaiter();


awaiter.OnCompleted(() =>
{
    // ném lại Exception nếu task lỗi
    int result = awaiter.GetResult();
    Console.WriteLine(result);
});

// cách khác
primeNumberTask.ContinueWith(antecedent =>
{
    int result = antecedent.Result;
    Console.WriteLine(result); 
});

// Task.Delay() là phương thức bất đồng bộ tương đương với Thread.Sleep()
Task.Delay(5000).GetAwaiter().OnCompleted(() => Console.WriteLine(42));
Task.Delay(5000).ContinueWith(ant => Console.WriteLine(42));
```

## TaskCompletionSource

TaskCompletionSource cho phép tạo một tác vụ và quyết định khi nào tác vụ sẽ hoàn thành hoặc bị lỗi.
```cs
public class TaskCompletionSource<TResult>
{
    // nếu gọi 2 lần sẽ ném Exception
    public void SetResult(TResult result);
    public void SetException(Exception exception);
    public void SetCanceled();

    // nếu gọi 2 lần sẽ trả về false
    public bool TrySetResult(TResult result);
    public bool TrySetException(Exception exception);
    public bool TrySetCanceled();
    public bool TrySetCanceled(CancellationToken cancellationToken);
    //...
}
```

```cs
TaskCompletionSource<int> tcs = new TaskCompletionSource<int>();

Task<int> t = new Thread(() =>
{
    Thread.Sleep(5000); 
    tcs.SetResult(42);
});
t.IsBackground = true;
t.Start();

Task<int> task = tcs.Task; 
Console.WriteLine(task.Result); 
```

# Asynchronous Programming


Có thể sử dụng Asynchronous Programming mà không cần dùng Multithreading khi xử lí I/O-bound (bằng TaskCompletionSource)


```cs
int GetPrimesCount(int start, int count)
{
    return ParallelEnumerable.Range(start, count).Count(n => Enumerable.Range(2, (int)Math.Sqrt(n) - 1).All(i => n % i > 0));
}

void DisplayPrimeCounts()
{
    for (int i = 0; i < 10; i++)
    {
        Console.WriteLine(GetPrimesCount(i * 1000 + 2, 1000) + " primes between " + (i * 10000) + " and " + ((i + 1) * 1000 - 1));
    }

    Console.WriteLine("Done!");
}
```


# File

## Excel

```cs
// DocumentFormat.OpenXml
// WindowsBase
using DocumentFormat.OpenXml;
using DocumentFormat.OpenXml.Packaging;
using DocumentFormat.OpenXml.Spreadsheet;
```

```cs
// tạo file Excel mới
using (SpreadsheetDocument document = SpreadsheetDocument.Create(fileName, SpreadsheetDocumentType.Workbook))
{
    // WorkbookPart chứa danh sách các sheet
    WorkbookPart workbookPart = document.AddWorkbookPart();
    workbookPart.Workbook = new Workbook(); 

    // WorksheetPart chứa các Data và Column
    WorksheetPart worksheetPart = workbookPart.AddNewPart<WorksheetPart>();
    worksheetPart.Worksheet = new Worksheet(); 

    // Sheets chứa danh sách các sheet
    Sheets sheets = workbookPart.Workbook.AppendChild(new Sheets());
    Sheet sheet = new Sheet() 
    {   
        Id = workbookPart.GetIdOfPart(worksheetPart), 
        SheetId = 1, 
        Name = "Employees" 
    };
    sheets.Append(sheet);

    workbookPart.Workbook.Save();


    // thêm dữ liệu
    SheetData sheetData = worksheetPart.Worksheet.AppendChild(new SheetData());
    Row row = new Row();
    row.Append(
        CreateCell("Id", CellValues.String),
        CreateCell("Name", CellValues.String),
        CreateCell("Birth Date", CellValues.String),
        CreateCell("Salary", CellValues.String)
    );
    sheetData.AppendChild(row);

    foreach (var employee in employees)
    {
        Row row = new Row();
        row.Append(
            CreateCell(employee.Id.ToString(), CellValues.Number),
            CreateCell(employee.Name, CellValues.String),
            CreateCell(employee.DoB.ToString("dd/MM/yyyy"), CellValues.String),
            CreateCell(employee.Salary.ToString(), CellValues.Number)
        );
        sheetData.AppendChild(row);
    }

    worksheetPart.Worksheet.Save();
}

private Cell CreateCell(string value, CellValues dataType)
{
    return new Cell()
    {
        CellValue = new CellValue(value),
        DataType = new EnumValue<CellValues>(dataType)
    };
}
```

```cs
// đọc dữ liệu
SpreadsheetDocument document = SpreadsheetDocument.Open(fileName, false);
WorkbookPart workbookPart = document.WorkbookPart;
Sheets sheets = workbookPart.Workbook.Descendants<Sheet>();
Sheet sheet = sheets.FirstOrDefault(s => s.Name == "Products"); 
WorksheetPart worksheetPart = (WorksheetPart)(workbookPart.GetPartById(sheet.Id)); 
IEnumerable<Cell> cells = worksheetPart.Worksheet.Descendants<Cell>();

int row = 3;
do
{
    Cell idCell = cells.FirstOrDefault(c => c?.CellReference == $"B{row}");

    if (idCell?.InnerText.Length > 0)
    {
        string id = idCell.InnerText;
    }
    row++;
} while (idCell?.InnerText.Length > 0);


Cell nameCell = cells.FirstOrDefault(c => c?.CellReference == $"C{row}")!;
string stringId = nameCell!.InnerText;
var stringTable = wbPart.GetPartsOfType<SharedStringTablePart>().FirstOrDefault()!;
string name = stringTable.SharedStringTable.ElementAt(int.Parse(stringId)).InnerText;

```
