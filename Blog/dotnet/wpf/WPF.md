
# 1. Creating custom type instances in XAML

```xml
<Window xmlns:local="clr-namespace:CH01.CustomTypes;assembly=MyAssembly">
</Window>
```      

Anything prefixed by the string local (we can select anything here), should be looked up in the CH01.CustomTypes namespace.

```xml
<local:Book Name="Windows Internals" Author="" Price="40" YearPublished="2009" />
```

> We can map a single XML namespace to multiple .NET namespaces by using the XmlnsDefinition attribute within the assembly where the exported types reside. This only works for referenced assemblies; that is, it's typically used in class library assemblies.
We can make all types within this assembly and the MyClassLibrary namespace part of an XML namespace by adding the following attribute:
```csharp
[assembly: XmlnsDefinition("http://mylibrary.com", "MyClassLibrary")]
```
The first argument is the XML namespace name. This can be anything, but is typically some form of fictitious URL, so as to lower chances of collisions. 
The second argument is the .NET namespace mapped by this XML namespace. 

We can add another attribute to map the same XML namespace to the new .NET namespace:
```csharp
[assembly: XmlnsDefinition("http://mylibrary.com", "MyClassLibrary.MyOtherTypes")]
```

This means that a single XML prefix can now map to multiple .NET namespaces: ```xmlns:mylib="http://mylibrary.com"```

# 2. Markup Extension:

### Access a markup extension:
```xml
<Ellipse Stroke="Black" Height="50" Fill="{x:Static SystemColors.DesktopBrush}" />
```

### Create Custom Markup Extension
```csharp
public class RandomExtension : MarkupExtension 
{
    readonly int _from, _to;
    public RandomExtension(int from, int to)
    {
        _from = from; _to = to;s
    }

    public RandomExtension(int to) : this(0, to) { }

    static readonly Random _rnd = new Random();
    public override object ProvideValue(IServiceProvider sp)
    {
        return (double)_rnd.Next(_from, _to);
    }

}
```

# 3. Routed Event

> Routed events are events which navigate up or down the visual tree acording to their RoutingStrategy. The routing strategy can be bubble, tunnel or direct.

> - Tunneling: The event is raised on the root element and navigates down to the visual tree until it reaches the source element or until the tunneling is stopped by marking the event as handled. Its name always starts with Preview.
> - Bubbling The event is raised on the source element and navigates up to the visual tree until it reaches the root element or until the bubbling is stopped by marking the event as handled. The bubbling event is raised after the tunneling event.
> - Direct The event is raised on the source element and must be handled on the source element itself.

> Stop bubbling by setting e.Handled = true

> Attached events can be handled by any element, even if that element's type does not define any such event. This is achieved through attached event syntax:
```xml
<Grid ButtonBase.Click="OnKeyPressed">
```

## Basic Drawing

```csharp
Point _pos;
bool _isDrawing;
Brush _stroke = Brushes.Black;
private void OnMouseDown(object sender, MouseButtonEventArgs e) {
	var rect = e.Source as Rectangle;
	if (rect != null) {
		_stroke = rect.Fill;
	} else {
		_isDrawing = true;
		_pos = e.GetPosition(root);
		root.CaptureMouse();
	}
}

private void OnMouseMove(object sender, MouseEventArgs e) {
	if (_isDrawing) {
		Line line = new Line();
		line.X1 = _pos.X;
		line.Y1 = _pos.Y;
		_pos = e.GetPosition(root);
		line.X2 = _pos.X;
		line.Y2 = _pos.Y;
		line.Stroke = _stroke;
		line.StrokeThickness = 1;
		root.Children.Add(line);
	}
}

```

# 4. Resource

## Logical Resource

Every element (deriving from FrameworkElement) has a Resources property of type  ResourceDictionary. This means that every element can have resources associated with it. In XAML, the x:Key attribute must be specified

```xml
<Window.Resources>
	<LinearGradientBrush x:Key="brush1">
		<GradientStop Offset="0" Color="Yellow" />
		<GradientStop Offset="1" Color="Brown" />
	</LinearGradientBrush>
</Window.Resources>
```
Using this resource in XAML requires the StaticResource markup extension
```xml
<Rectangle Fill="{StaticResource brush1}" />
```


> The same lookup effect can be achieved in code by using the FrameworkElement.
FindResource method
```csharp
Brush brush = (Brush)x.TryFindResource("brush1");
if (brush == null) { 
	// not found
}

// Add and remove a Resource dynamically:
this.Resources.Add("brush2", new SolidColorBrush(Color.FromRgb(200, 10, 150)));
this.Resources.Remove("brush1");

// Modify the Resource
var brush = (LinearGradientBrush)this.Resources["brush1"];
brush.GradientStops.Add(new GradientStop(Colors.Blue, .5));
```

### Dynamically binding to a logical resource
```csharp
var brush = new RadialGradientBrush();
brush.GradientStops.Add(new GradientStop(Colors.Blue, 0));
brush.GradientStops.Add(new GradientStop(Colors.White, 1));
this.Resources["brush1"] = brush;
```

### User-selected colors and fonts

```xml
<TextBlock Fill="{DynamicResource {x:Static SystemColors.DesktopBrushKey}}" />
```

## Binary Resource
### Build Action:
> - Resource: the resource is stored as a resource inside the compiled assembly. This makes the actual file unnecessary when deploying the application. These resources are part of the assembly and are stored in a resource named AssemblyName.g.resources
> - Content: the resource is not included in the assembly. This makes it more appropriate when the resource needs to change often and a rebuild would be undesirable. Also, if the resource is large, and not always needed, it's better to leave it off to the resulting assembly.

```xml
<Image Source="Images/apple.png" />
```
is equivalent to this markup
```xml
<Image Source="pack://application:,,,/Images/apple.png" />
```

### Save Image

- Nếu ảnh < 256KB: sử dụng cột kiểu VarBinary(max) thì hiệu năng tốt hơn 
- Nếu ảnh > 1MB: Lưu trên ổ đĩa thì hiệu năng tốt hơn 
- Ở giữa 256KB và 1MB: thích lưu đâu thì lưu

> Convert image to byte array

```cs
var filename = screen.FileName;
var image = new BitmapImage(new Uri(filename, UriKind.Absolute));
var encoder = new JpegBitmapEncoder();
encoder.Frames.Add(BitmapFrame.Create(image));
using (var stream = new MemoryStream())
{
	encoder.Save(stream);
	var db = new MyShop1Entities();
	var photo= new Photo() { data = stream.ToArray() };
	db.Photos.Add(photo);
	db.SaveChanges();
}
```

> Convert byte array to image
```cs
var array = value as byte[];
using (var stream = new MemoryStream (array))
{
	var image = new BitmapImage();
	image.BeginInit();
	image.CreateOptions = BitmapCreateOptions.PreservePixelFormat;
	image.CreateOptions = BitmapCacheOption.OnLoad;
	image.UriSource = null;
	image.StreamSource = stream;
	image.EndInit();
	image. Freeze();
	return image;
}
```

### Accessing binary resources in code
> Make sure Build Action is set to Resource

```xml
<!-- book.xml -->
<Books>
	<Book Name="Windows Internals" Author="Mark Russinovich" />
	<Book Name="Essential COM" Author="Don Box" />
	<Book Name="Programming Windows with MFC" Author="Jeff Prosise" />
</Books>
```
```csharp
StreamResourceInfo info = Application.GetResourceStream(new Uri("books.xml", UriKind.Relative));
var books = XElement.Load(info.Stream);
var bookList = from book in books.Elements("Book")
				orderby (string)book.Attribute("Author")
				select new {
					Name = (string)book.Attribute("Name"),
					Author = (string)book.Attribute("Author")
				};
foreach (var book in bookList) {
	_text.Text += book.ToString() + Environment.NewLine;
}	
```
> If the resource has been marked with a Build Action of Content, then the similar looking Application.GetContentStream method should be used instead.

### Accessing binary resources from another assembly:
```xml
<Image Source="/AssemblyReference;component/ResourceName"/>
```
> This scheme does not work with resources marked with a Build Action of Content. A way around this is to use the full pack URI with a siteOfOrigin base:
```xml
<Image Source="pack://siteOfOrigin:,,,/images/apple.png"/>
```

## Managing logical resources with Resource Dictionary

```xml
<ResourceDictionary>
	<ResourceDictionary.MergedDictionaries>
                <ResourceDictionary Source="/MyClassLibrary;component/Resources/
Brushes.xaml" />
	</ResourceDictionary.MergedDictionaries>
</ResourceDictionary>
```

Merging different resource dictionaries may cause an issue: two or more resources with the same keys that originate from different merged dictionaries. This is not an error and does not throw an exception. Instead, the selected object is the one from the last resource dictionary added.
If a resource in the current resource dictionary has the same key as the any of the resources in its merged dictionaries – it always wins out.

# 5. Application and Windows

## Selecting startup window dynamically

```csharp
protected override void OnStartup(StartupEventArgs e)
{
	Window mainWindow = null;
	// check some state or setting as appropriate
	if (ConfigurationManager.AppSettings["AdvancedMode"] == "1")
		mainWindow = new OtherWindow();
	else
	mainWindow = new MainWindow();
	mainWindow.Show();
}
```

## Open Image
```csharp
void OnOpenImage(object sender, RoutedEventArgs e)
{
	var dlg = new OpenFileDialog {
	Filter = "Image files|*.png;*.jpg;*.gif;*.bmp",
	Title = "Select image to open",
	InitialDirectory = Environment.GetFolderPath(Environment.SpecialFolder.MyPictures)
	};
       if (dlg.ShowDialog() == true)
       {
		var bmp = new BitmapImage(new Uri(dlg.FileName));
		_img.Source = bmp;
	}
}
```

## Color Dialog
You can use the ColorPicker or ColorCanvas provided with the Extended WPF toolkit library on [CodePlex](http://wpftoolkit.codeplex.com/)

[The Windows API Code Pack](http://archive.msdn.microsoft.com/WindowsAPICodePack) is a Microsoft project on CodePlex that provides many .NET wrappers to native Windows features

## Create ownership between windows

Every Window object has an Owner property. By default, it's null, meaning the Window is unowned, independent of other windows. If an owner is set, the Window now obeys ownership rules.

## Create Custom Shaped Window

```xml
<Window AllowsTransparency="True" 
		WindowStyle="None" 
		Background="Transparent">
</Window>
```
```csharp
// Click Event
private void OnClose(object sender, RoutedEventArgs e) {
	Close();
}
// MouseLeftButtonDown Event
private void OnMove(object sender, MouseButtonEventArgs e)
{
	DragMove();
}
```

## Creating a single instance application

We may sometimes want to limit the number of running instances of some application to just one.

```csharp
// App.xaml.cs
[DllImport("user32", CharSet = CharSet.Unicode)]
static extern IntPtr FindWindow(string cls, string win);
[DllImport("user32")]
static extern IntPtr SetForegroundWindow(IntPtr hWnd);
[DllImport("user32")]
static extern bool IsIconic(IntPtr hWnd);
[DllImport("user32")]
static extern bool OpenIcon(IntPtr hWnd);

private void Application_Startup(object sender, StartupEventArgs e)
{
	bool isNew; // returned value indicates whether this is a brand new kernel mutex or another handle to an existing one
	var mutex = new Mutex(true, "MySingleInstMutex", out isNew);
	if (!isNew) {
		ActivateOtherWindow();
		Shutdown();
	}
}

private static void ActivateOtherWindow()
{
	var other = FindWindow(null, "Single Instance");
	if (other != IntPtr.Zero)
	{
		SetForegroundWindow(other); 
		if (IsIconic(other))
		{
			OpenIcon(other);
		}
	}
}

```

# 6. Data Binding

```csharp
// Hook up the binding in code
var binding = new Binding("Value");
binding.ElementName = "_slider";
_text.SetBinding(TextBlock.FontSizeProperty, binding);
```

#### Binding Mode

- Default: whatever the target dependency property has been stated at registration time (DependencyProperty.Register) with a FrameworkPropertyMetadata
> By default, the Mode is set to BindingMode.Default. 
> For the TextBox.Text property the default is a two way binding; For the TextBlock.Text property it's one way
- OneTime: Indicates the binding should occur only when the properties are first connected. Any further changes to the source are not reflected on the target.
- OneWayToSource: Similar to one way, but works from the target to the source. 

#### Update Source Trigger

It's only relevant in a TwoWay or OneWayToSource binding mode, with the following possible values:
- LostFocus: The source is updated when the target element loses focus (which is exactly why the TextBox only updated the Slider after it had lost focus)
- PropertyChanged: The source is updated as soon as the target property changes. This is the default for most properties 
- Explicit: The source is updated if the UpdateSource method is called on the binding expression 
- Default: This is the default set by the target property at registration time with the FrameworkPropertyMetadata.DefaultUpdateSourceTrigger property

#### Update Source or Target manually

Sometimes, we may want to "force" data flow from source to target or vice versa. This is possible with the **BindingExpression.UpdateSource** and **BindingExpression.UpdateTarget** methods.
```csharp
var expr = BindingOperations.GetBindingExpression(_text, TextBox.TextProperty);
expr.UpdateSource();
```

#### Binding from a Source and RelativeSource property
```xml
<ControlName Source={StaticResource resourceName} />
```

The possible modes for RelativeSource property are:
- Self: Binding one property to another property on the same object. 
```xml
<Button Content="{Binding ActualWidth, RelativeSource={RelativeSource Self}}"/>
```
- FindAncestor: The source object is a parent of the target object. 
```xml
<TextBlock Text="{Binding ActualHeight, RelativeSource={RelativeSource FindAncestor, AncestorType=Window}}"/>
```
- PreviousData: Makes the source the previous data object when binding to a collection 
- TemplatedParent: Indicates the source object is the control whose template is being built.

> The object must somehow notify interested parties that the property changed. There are two common ways to do that. The first is to turn the property into a dependency property, which naturally supports change notifications. Although this seems attractive at first, it has two problems:
> - It requires the data object to inherit from DependencyObject, which may be inconvenient;
> - The data object must now incur a dependency on WPF
> The second option, which is commonly used, is to implement the INotifyPropertyChanged interface. This interface is not defined by WPF, but rather is part of System.Dll, one of the core .NET assemblies.

## Implementing INotifyPropertyChanged

```csharp
public abstract class ObservableObject : INotifyPropertyChanged {
	public event PropertyChangedEventHandler PropertyChanged;
	protected virtual void OnPropertyChanged(string propName) {
		PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propName));
	}
}

class Person : ObservableObject 
{
	public string Name { get; set; }
	int _age;
	public int Age 
	{
		get { return _age; }
		set 
		{
			if (_age != value) 
			{
				_age = value;
				OnPropertyChanged("Age");
			}
		}
	}
}
```

> The most severe problem is the use of a property name as a string, which is at least inelegant, but more importantly can lead to subtle errors if the property name is misspelled or later changed. One way to improve this is to check at runtime that the property with that name actually exists as the first line in OnPropertyChanged:
```csharp
Debug.Assert(GetType().GetProperty(propName) != null);
```

> Another way to improve the code is to do the inequality check within a common method. A further improvement would be not specifying the actual property name, but letting the code infer it automatically

```csharp
protected void SetProperty<T>(ref T field, T value, Expression<Func<T>> expr) 
{
	if (!EqualityComparer<T>.Default.Equals(field, value)) 
	{
		field = value;
		var lambda = (LambdaExpression)expr; MemberExpression memberExpr;
		if (lambda.Body is UnaryExpression) 
		{
			var unaryExpr = (UnaryExpression)lambda.Body;
			memberExpr = (MemberExpression)unaryExpr.Operand;
		}
		else 
		{
                        memberExpr = (MemberExpression)lambda.Body;
		}
        
		OnPropertyChanged(memberExpr.Member.Name);
	}
}
```
The first check uses EqualityComparer. Default object to check for equality. This property returns a default equality comparer that calls object.Equals if it has to, but if the target object implements IEquatable, it uses that implementation SetProperty<> accepts as the last argument, a lambda expression that the compiler provides for the method in the form of an Expression object, which is a way to look at code as data. This allows the extraction of the property name provided by the caller. Bottom line – using this method in a data object becomes safer if not simpler:

```csharp
int _age;
public int Age
{
	get { return _age; }
	set 
	{ 
		SetProperty(ref _age, value, () => Age); 
	}
}
```
> C# 5.0 feature provides the caller member name automatically:

```csharp
protected void SetProperty<T>(ref T field, T value, [CallerMemberName] string propName = null) 
{
	if (!EqualityComparer<T>.Default.Equals(field, value)) 
	{
		field = value;
		PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propName));
	}
}
```
The new CallerMemberName attribute makes the compiler provide the calling member name. This means that the property setter is now simplified:

```csharp
int _age;
public int Age
{
	get { return _age; }
	set 
	{ 
		SetProperty(ref _age, value); 
	}
}

```

## Binding to a Collection

```xml
<ComboBox ItemsSource="{Binding}" Grid.Row="2"/>
```

> Use ObservableCollection, that implements INotifyCollectionChanged interface to notify the UI when an object is added or removed

> When the selection is changed in the ListBox, it does not affect the selected item in the ComboBox and vice versa. If we wanted the selected item to be synchronized between Selector controls that bind to the same data, we could set the IsSynchronizedToCurrentItem property on the ComboBox and ListBox to true

## Binding to multiple properties

> We need a Converter Class that implements IMultiValueConverter 

```csharp
class RGBConverter : IMultiValueConverter {
	SolidColorBrush _brush = new SolidColorBrush();
	public object Convert(object[] values, Type targetType, object parameter, CultureInfo culture) {
		_brush.Color = Color.FromRgb(System.Convert.ToByte(
		values[0]), System.Convert.ToByte(values[1]),
		System.Convert.ToByte(values[2]));
		return _brush;
	}
	public object[] ConvertBack(object value, Type[] tTypes, object parameter, CultureInfo culture) {
		throw new NotImplementedException();
	}
}
```
```xml
<Window.Resources>
	<local:RGBConverter x:Key="rgbConverter" />
</Window.Resources>
<Rectangle.Fill>
	<MultiBinding Converter="{StaticResource rgbConverter}">
		<Binding Path="Value" ElementName="_red"/>
		<Binding Path="Value" ElementName="_green"/>
		<Binding Path="Value" ElementName="_blue"/>
	</MultiBinding>
</Rectangle.Fill>
```

## Validating Data

```csharp
class Person : INotifyPropertyChanged, IDataErrorInfo {
// never called by WPF
	public string Error {
		get {
			// return null to indicate no error, or some error string otherwise 
			return null; 
		}
	}
		
	public string this[string name] {
		get {
			switch(name) {
				case "Name":
					if (string.IsNullOrWhiteSpace(Name))
					return "Name cannot be empty";
					break;
				case "Age":
					if (Age < 1)
					return "Age must be a positive integer";
					break;
			}
			return null;
		}
	}
}
```

> To make WPF use this interface, the Binding.ValidatesOnDataErrors property must be set to true (this adds a DataErrorValidationRule to the ValidationRules collection)

> We can also use Data Annotation Attributes to validate data

## Custom Validation Rules


# 7. Styles, Triggers and Control Templates

## 7.1. Styles

> A Style is a container for a bunch of Setter objects and triggers. 

```xml
<Application.Resources>
	<!-- the property must be a dependency property -->
	<Style TargetType="Button" x:Key="numericStyle">
		<Setter Property="FontSize" Value="20" />
		<Setter Property="Effect">
			<Setter.Value>
				<DropShadowEffect Color="Blue"/>
			</Setter.Value>
		</Setter>
	</Style>

	<!-- A style can be inherited from another style -->
	<Style TargetType="Button" x:Key="operatorStyle" BasedOn="{StaticResource numericStyle}">
		<Setter Property="FontWeight" Value="ExtraBold" />
		<Setter Property="Effect">
			<Setter.Value>
				<DropShadowEffect Color="Red" />
			</Setter.Value>
		</Setter>
	</Style>

	<!-- This style will work on anything deriving from Control. Using this capability is not recommended -->
	<Style x:Key="numericStyle">
		<Setter Property="Button.MinWidth" Value="40" />
		<Setter Property="Control.Effect">
			<Setter.Value>
				<DropShadowEffect Color="Blue"/>
			</Setter.Value>
		</Setter>
	</Style>

	<!-- The Style is applied to all elements of the target type, but not derived types -->
	<Style TargetType="Button">
		<Setter Property="Button.Padding" Value="6" />
		<Setter Property="Button.MinWidth" Value="40" />
	</Style>
<Application.Resources>

<Window.Resources>
	<Style TargetType="Button" x:Key="operatorStyle" BasedOn="{StaticResource {x:Type Button}}">
</Window.Resources>
```

### Place to set styles:
- FrameworkElement.Style property
- FrameworkElement.FocusVisualStyle property: Accepts a Style that affects the way the focus indicator is rendered when that element has the keyboard focus.
- ItemsControl.ItemContainerStyle property: Accepts a Style that affects the container element of each data item.
- DataGrid.CellStyle: Accepts a Style that affects the way a cell is rendered. Similar properties exposed by the DataGrid include ColumnHeaderStyle, DragIndicatorStyle, DropLocationIndicatorStyle, RowHeaderStyle, and RowStyle.

## 7.2. Triggers

### 7.2.1. Property Triggers

> Property triggers work with dependency properties only

```xml
<Application.Resources>
	<Style TargetType="Button" x:Key="numericStyle">
		<Style.Triggers>
			<Trigger Property="IsPressed" Value="True">
				<Setter Property="Effect" Value="{x:Null}" />
				<Setter Property="RenderTransform">
					<Setter.Value>
						<TranslateTransform X="4" Y="4" />
					</Setter.Value>
				</Setter>
			</Trigger>
		</Style.Triggers>
	</Style>
<Application.Resources>
```

Trigger with animation:

```xml
<Application.Resources>
	<Style TargetType="Button" x:Key="numericStyle">
		<Style.Triggers>
			<Trigger Property="IsMouseOver" Value="True">
			<Setter Property="Panel.ZIndex" Value="100" />
			<Trigger.EnterActions>
				<BeginStoryboard>
					<Storyboard >
						<DoubleAnimation To="1.3" Duration="0:0:0.3" Storyboard.TargetProperty="RenderTransform.ScaleX" />
						<DoubleAnimation To="1.3" Duration="0:0:0.3" Storyboard.TargetProperty="RenderTransform.ScaleY" />
					</Storyboard>
				</BeginStoryboard>
			</Trigger.EnterActions>
			<Trigger.ExitActions>
				<BeginStoryboard>
					<Storyboard >
						<DoubleAnimation Duration="0:0:0.1" Storyboard.TargetProperty="RenderTransform.ScaleX" />
						<DoubleAnimation Duration="0:0:0.1" Storyboard.TargetProperty="RenderTransform.ScaleY" />
					</Storyboard>
				</BeginStoryboard>
			</Trigger.ExitActions>
		<Style.Triggers>
	</Style>
<Application.Resources>
```

> A Style is not the only object that contains a Triggers property. There are 3 other such properties found in WPF:
> - DataTemplate.Triggers
> - ControlTemplate.Triggers
> - FrameworkElement.Triggers: An element can host triggers as well, but these are limited to EventTrigger objects only. This limitation is a bit artificial, but in practice, using triggers on some specific element is rare. It's much more common to use triggers to tweak the behavior of many elements, through a Style, DataTemplate, or ControlTemplate

### 7.2.2. Data Triggers

> Data triggers are able to inspect any property, but their usefulness lies within data templates that naturally bind to data objects 

```xml
<DataTemplate>
	<Border Margin="2" BorderBrush="Blue" BorderThickness="1" Padding="2" x:Name="_border">
		<Grid>
			<Grid.RowDefinitions>
				<RowDefinition Height="Auto" />
				<RowDefinition Height="Auto" />
			</Grid.RowDefinitions>
			<TextBlock Text="{Binding BookName}" FontSize="20" FontWeight="Bold" />
			<TextBlock Grid.Row="1" Text="{Binding AuthorName}" FontSize="16" Foreground="Blue" />
			<TextBlock  FontWeight="Bold" Foreground="Red" Grid.RowSpan="2" 
						VerticalAlignment="Center" Visibility="Hidden"
						x:Name="_free" Text="Free!" Margin="4" FontSize="25"/>
		</Grid>
	</Border>

	<DataTemplate.Triggers>
		<DataTrigger Binding="{Binding IsFree}" Value="True">
			<Setter Property="Background" TargetName="_border" Value="Yellow" />
			<Setter Property="Visibility" TargetName="_free" Value="Visible" />
		</DataTrigger>
	</DataTemplate.Triggers>
</DataTemplate>
```

### 7.2.3. Event Triggers

> This trigger type fires when a routed event occurs, executing animation-related actions

```xml
<Grid Margin="8" RenderTransformOrigin=".5,.5">
	<Grid.RenderTransform>
		<ScaleTransform ScaleX="0" ScaleY="0" />
	</Grid.RenderTransform>

	<Grid.Triggers>
		<EventTrigger RoutedEvent="Loaded">
			<BeginStoryboard>
				<Storyboard>
					<DoubleAnimation To="1" Duration="0:0:.8" Storyboard.TargetProperty="RenderTransform.ScaleX" />
					<DoubleAnimation To="1" Duration="0:0:.8" Storyboard.TargetProperty="RenderTransform.ScaleY" />
				</Storyboard>
			</BeginStoryboard>
		</EventTrigger>
	</Grid.Triggers>
</Grid>
```

### 7.2.4. Multi Triggers

```xml
<Window>
	<Window.Resources>
		<Style>
			<MultiTrigger>
				<MultiTrigger.Conditions>
					<Condition Property="IsMouseOver" Value="True" />
					<Condition Property="IsDefault" Value="True" />
				</MultiTrigger.Conditions>
				<Setter Property="Background" Value="Cyan" />
				<Setter Property="Effect">
					<Setter.Value>
						<DropShadowEffect />
					</Setter.Value>
				</Setter>
			</MultiTrigger>
		</Style>
	</Window.Resources>
	<StackPanel>
		<Button Content="Move mouse over me" FontSize="20" HorizontalAlignment="Center" Margin="20" Padding="6" x:Name="theButton" />
		<CheckBox Content="Default button" Margin="10" IsChecked="{Binding IsDefault, ElementName=theButton, Mode=TwoWay}" FontSize="15"/>
	</StackPanel>
</Window>
```

### 7.2.5. Change Appearance of a Progress Bar

The secret lies in the name of the rectangle (PART_Indicator in this case). This "part" is looked up by the progress bar control, and if found, it changes its Width appropriately without us needing to intervene. This is published by attributes on the control's class

> TemplateBinding does a one way binding to a property on the templated control
> RelativeSource was used because StringFormat does not exist on a TemplateBinding
```xml
<Window>
    <Window.Resources>
        <ControlTemplate TargetType="ProgressBar" x:Key="pt1">
            <Grid>
                <Rectangle x:Name="PART_Track">
                    <Rectangle.Fill>
                        <LinearGradientBrush EndPoint="1,0">
                            <GradientStop Color="DarkBlue" Offset="0" />
                            <GradientStop Color="LightBlue" Offset="1" />
                        </LinearGradientBrush>
                    </Rectangle.Fill>
                </Rectangle>
                <Rectangle x:Name="PART_Indicator" Fill="Orange" Stroke="Black" StrokeThickness="1" HorizontalAlignment="Left" />
				<TextBlock Text="{Binding Value, RelativeSource={RelativeSource TemplatedParent}, StringFormat=\{0\}%}"
							Foreground="{TemplateBinding Foreground}" VerticalAlignment="Center" HorizontalAlignment="Center"/>
            </Grid>
            <ControlTemplate.Triggers>
                <Trigger Property="IsIndeterminate" Value="True">
                    <Setter Property="Fill" TargetName="PART_Indicator">
                        <Setter.Value>
                            <LinearGradientBrush EndPoint=".1,1" SpreadMethod="Repeat">
                                <GradientStop Offset="0" Color="Orange" />
                                <GradientStop Offset="1" Color="Red" />
                            </LinearGradientBrush>
                        </Setter.Value>
                    </Setter>
                </Trigger>
            </ControlTemplate.Triggers>
        </ControlTemplate>
    </Window.Resources>
    <StackPanel>
        <CheckBox Content="Indeterminate" FontSize="16" Margin="4" IsChecked="{Binding IsIndeterminate, ElementName=pb1, Mode=TwoWay}" />
        <ProgressBar Height="30" Value="60" Margin="20" Template="{StaticResource pt1}" x:Name="pb1" />
    </StackPanel>
</Window>
```

> A Control Template can combine with Style

```xml
<Style TargetType="ProgressBar">
	<Setter Property="Template">
		<Setter.Value>
			<ControlTemplate TargetType="ProgressBar" >
				...
			</ControlTemplate>
		</Setter.Value>
	</Setter>
	<Setter Property="Background">
		<Setter.Value>
			...
		</Setter.Value>
	</Setter>
</Style>
```
#### Extending a template with attached properties

```csharp
public static class ProgressBarAttributes {
	public static bool GetShowText(DependencyObject obj) {
		return (bool)obj.GetValue(ShowTextProperty);
	}
	public static void SetShowText(DependencyObject obj, bool value) {
		obj.SetValue(ShowTextProperty, value);
	}
	public static readonly DependencyProperty ShowTextProperty = DependencyProperty.RegisterAttached("ShowText", typeof(bool), typeof(ProgressBarAttributes), new UIPropertyMetadata(true));
}
```

```xml
<Window.Resources>
	<BooleanToVisibilityConverter x:Key="bool2vis" />
</Window.Resources>
<TextBlock Visibility="{TemplateBinding local:ProgressBarAttributes.ShowText, Converter={StaticResource bool2vis}}" />
```

### 7.2.6. Replacing the control template of a scroll bar

```xml
<Application.Resources>
	<ControlTemplate TargetType="RepeatButton" x:Key="repeatTransTemplate">
		<Rectangle Fill="Transparent" />
	</ControlTemplate>
	<ControlTemplate TargetType="RepeatButton" x:Key="plainTemplate">
		<Grid>
			<ContentPresenter Margin="{TemplateBinding Padding}" />
		</Grid>
	</ControlTemplate>
	<ControlTemplate TargetType="Thumb" x:Key="vthumbTemplate">
		<Rectangle RadiusX="5" RadiusY="10" Stroke="{TemplateBinding BorderBrush}" 
					StrokeThickness="{TemplateBinding BorderThickness}" 
					Fill="{TemplateBinding Background}" />
	</ControlTemplate>
</Application.Resources>
```

```xml
<ControlTemplate TargetType="ScrollBar" x:Key="verticalScrollBarTemplate">
	<Grid>
		<Grid.RowDefinitions>
			<RowDefinition Height="Auto" />
			<RowDefinition />
			<RowDefinition Height="Auto" />
		</Grid.RowDefinitions>
		<Border BorderBrush="DarkBlue" BorderThickness="1" Background="LightBlue" Grid.Row="1">
			<Track x:Name="PART_Track" IsDirectionReversed="True">
				<Track.DecreaseRepeatButton>
					<RepeatButton Command="ScrollBar.PageUpCommand" Template="{StaticResource repeatTransTemplate}" />
				</Track.DecreaseRepeatButton>
				<Track.IncreaseRepeatButton>
					<RepeatButton Command="ScrollBar.PageDownCommand" Template="{StaticResource repeatTransTemplate}" />
				</Track.IncreaseRepeatButton>
				<Track.Thumb>
					<Thumb Template="{StaticResource vthumbTemplate}" BorderBrush="Black" BorderThickness="1">
						<Thumb.Background>
							<LinearGradientBrush EndPoint="0,1">
								<GradientStop Offset="0" Color="DarkGreen" />
								<GradientStop Offset="1" Color="LightGreen" />
							</LinearGradientBrush>
						</Thumb.Background>
					</Thumb>
				</Track.Thumb>
			</Track>
		</Border>
		<Viewbox>
			<RepeatButton Command="{x:Static ScrollBar.LineUpCommand}" Template="{StaticResource plainTemplate}">
				<Path Data="M 25,0 L 50,50 L 0,50 Z" Fill="Blue" />
			</RepeatButton>
		</Viewbox>
		<Viewbox Grid.Row="2">
			<RepeatButton Command="{x:Static ScrollBar.LineDownCommand}" Template="{StaticResource plainTemplate}">
				<Path Data="M 25,50 L 0,0 L 50,0 Z" Fill="Blue" />
			</RepeatButton>
		</Viewbox>
	</Grid>
</ControlTemplate>

<ControlTemplate TargetType="ScrollBar" x:Key="horizontalScrollBarTemplate">
</ControlTemplate>

<Style TargetType="ScrollBar">
	<Style.Triggers>
		<Trigger Property="Orientation" Value="Vertical">
			<Setter Property="Template" Value="{StaticResource verticalScrollBarTemplate}" />
		</Trigger>
		<Trigger Property="Orientation" Value="Horizontal">
			<Setter Property="Template" Value="{StaticResource horizontalScrollBarTemplate}" />
		</Trigger>
	</Style.Triggers>
</Style>
```

### 7.2.7. Customizing selection in a Selector control

> ContentPresenter is an element used mostly in control templates, which renders the Content of the templated control based on the usual rules of the Content property (meaning it uses a DataTemplate if provided, and so on). It can technically bind to another property (of type Object) by setting the ContentSource property to the base property name; for example, for HeaderedContentControl template, a ContentPresenter would set ContentSource to "Header", so that it looks at Header and HeaderTemplate as appropriate.

```xml
<ListBox>
	<ListBox.ItemContainerStyle>
		<Style TargetType="ListBoxItem">
			<Setter Property="Template">
				<Setter.Value>
					<ControlTemplate TargetType="ListBoxItem">
						<Border BorderBrush="{TemplateBinding BorderBrush}" Background="{TemplateBinding Background}" Margin="{TemplateBinding Padding}" BorderThickness="{TemplateBinding BorderThickness}">
							<ContentPresenter HorizontalAlignment="{TemplateBinding HorizontalContentAlignment}" VerticalAlignment="{TemplateBinding VerticalContentAlignment}" />
						</Border>
					</ControlTemplate>
				</Setter.Value>
				
			</Setter>
			<Setter Property="Background" Value="Transparent" />
			<Setter Property="BorderThickness" Value="2" />
			<Setter Property="BorderBrush" Value="Transparent" />
			<Style.Triggers>
				<Trigger Property="IsSelected" Value="True">
					<Setter Property="BorderBrush" Value="Red" />
				</Trigger>
			</Style.Triggers>
		</Style>
	</ListBox.ItemContainerStyle>
</ListBox>
```

# 8. Graphics and Animation

## 8.1. Transform

```xml
<!-- RenderTransform makes the transformation after the layout pass is over -->
<Image Source="Penguins.jpg" Height="100" RenderTransformOrigin=".5,.5">
	<Image.RenderTransform>
		<RotateTransform Angle="45" />
	</Image.RenderTransform>
</Image>

<!-- LayoutTransform takes the transform changes into consideration as part of the layout phase -->
<Image Source="Penguins.jpg" Height="100">
	<Image.LayoutTransform>
		<RotateTransform Angle="45" />
	</Image.LayoutTransform>
</Image>
```

## 8.2. Adorners

```xml
<Canvas x:Name="_canvas" Background="White">
</Canvas>
```

```csharp
Point _current;
FrameworkElement _currentShape;
bool _moving;
Adorner _adorner;
public MainWindow() {
	InitializeComponent();
	Loaded += delegate {
		CreateCircles();
	};
}

void CreateCircles() {
	var rnd = new Random();
	int start = rnd.Next(30);
	for(int i = 0; i < 10; i++) 
	{
		var circle = new Ellipse 
		{
			Stroke = Brushes.Black,
			StrokeThickness = 1,
			Width = 50,
			Height = 50
		};
		var fill = typeof(Brushes).GetProperties(BindingFlags.Static | BindingFlags.Public)[start].GetValue(null, null) as Brush;
		circle.Fill = fill;
		Canvas.SetLeft(circle, rnd.NextDouble() * ActualWidth);
		Canvas.SetTop(circle, rnd.NextDouble() * ActualHeight);
		_canvas.Children.Add(circle);
		start += 2;
	}
}
```

```csharp
// SelectionAdorner.cs
class SelectionAdorner : Adorner {
	public SelectionAdorner(UIElement element) : base(element) {

	}
	static readonly Brush _rectFill = new SolidColorBrush(Color.FromArgb(30, 0, 0, 255));
    static readonly Brush _circleFill = new SolidColorBrush(Color.FromArgb(60, 255, 0, 0));
    const double _circleRadius = 6;
	protected override void OnRender(DrawingContext dc)
    {
        dc.DrawRectangle(_rectFill, _pen, new Rect(AdornedElement.DesiredSize));
        dc.DrawEllipse(_circleFill, null, new Point(0, 0), _circleRadius, _circleRadius);
        dc.DrawEllipse(_circleFill, null, new Point(AdornedElement.DesiredSize.Width, 0),_circleRadius, _circleRadius);
        dc.DrawEllipse(_circleFill, null, new Point(AdornedElement.DesiredSize.Width, AdornedElementDesiredSize.Height), _circleRadius, _circleRadius);
        dc.DrawEllipse(_circleFill, null, new Point(0, AdornedElement.DesiredSize.Height),_circleRadius, _circleRadius);
    }
}
```

## 8.3. Animation

# 9. Commands and MVVM Pattern

## 7.1. Commands

```xml
<Window.CommandBindings>
    <CommandBinding Command="Open" Executed="OnOpen" />
    <CommandBinding Command="IncreaseZoom" Executed="OnZoomIn" CanExecute="OnIsImageExist" />
    <CommandBinding Command="DecreaseZoom" Executed="OnZoomOut" CanExecute="OnIsImageExist" />
    <CommandBinding Command="local:Commands.ZoomNormalCommand" Executed="OnZoomNormal" CanExecute="OnIsImageExist" />
</Window.CommandBindings>

<ToolBar>
    <Button Content="Open..." Command="Open" Margin="6"/>
    <Button Content="Zoom In" Command="IncreaseZoom" Margin="6"/>
    <Button Content="Zoom Out" Command="DecreaseZoom" Margin="6"/>
    <Button Command="local:Commands.ZoomNormalCommand" Content="Normal" Margin="6"/>
</ToolBar>
```

> WPF provides a basic support for commands with the ICommand interface and two built-in implementations, RoutedCommand and RoutedUICommand.

```csharp
public interface ICommand {
	bool CanExecute(object parameter); // executes the command
	void Execute(object parameter); // indicates whether this command is available at this time
	event EventHandler CanExecuteChanged; // notify WPF when the CanExecute method should be called again
	// because something has changed that may make this method return a different result, 
	// and so the enabled/disabled state may need to update
}
```
The following line:
```xml
<CommandBinding Command="IncreaseZoom" Executed="OnZoomIn" CanExecute="OnIsImageExist" />
```
Causes the XAML parser to convert the string IncreaseZoom (using a Type Converter) to the static property NavigationCommands.IncreaseZoom (of type RoutedUICommand), the same one used in the button:
```xml
<Button Content="Zoom In" Command="IncreaseZoom" Margin="6"/>
```
When the button is clicked, Execute is called on the IncreaseZoom command instance. This explicit implementation inside RoutedUICommand looks for a corresponding CommandBinding, starting from the target element, which is by default the command source itself (the button) and moving up the visual tree until such a command and a handler is found. If not found, the command source is disabled.

Once the CommandBinding is found, its Executed event handler is called. Similarly, when WPF calls CanExecute on the RoutedUICommand, the CommandBinding is located in the same way, but this time the CanExecute event handler is called. If it does not exist, but an Executed event does exist, the command source is always enabled.

### Custom Command

```csharp
public static class Commands
{
    static readonly RoutedUICommand _zoomNormalCommand = new RoutedUICommand("Zoom Normal", "Normal", typeof(Commands), new InputGestureCollection(new [] { new KeyGesture(Key.N, ModifierKeys.Alt)}));
    public static RoutedUICommand ZoomNormalCommand
    {
        get 
		{ 
			return _zoomNormalCommand; 
		}
    }
}
```


> Buttons and menu items are capable of invoking commands. This is abstracted by the ICommandSource interface
> ```csharp
> public interface ICommandSource {
> 	ICommand Command { get; } // the command to invoke
>	object CommandParameter { get; } // optional parameter
>	IInputElement CommandTarget { get; } // element is the target from which the lookup for a CommandBinding begins
> }
> ```

## 7.2. MVVM

```csharp
// ImageData.cs
class ImageData : INotifyPropertyChanged
{
	ICommand _openImageFileCommand, _zoomCommand;
	public ImageData() {
		_openImageFileCommand = new OpenImageFileCommand(this);
		_zoomCommand = new ZoomCommand(this);
	}

	public ICommand OpenImageFileCommand {
		get { return _openImageFileCommand; }
	}
	public ICommand ZoomCommand {
		get { return _zoomCommand; }
	}

    string _imagePath;
    double _zoom = 1.0;
    public double Zoom
    {
        get { return _zoom; }
        set
        {
            _zoom = value;
            OnPropertyChanged("Zoom");
        }
    }
    public string ImagePath
    {
        get { return _imagePath; }
        set
        {
            _imagePath = value;
            OnPropertyChanged("ImagePath");
        }
    }

    protected virtual void OnPropertyChanged(string name)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
    public event PropertyChangedEventHandler PropertyChanged;
}
```
```csharp
// ./Commands/OpenImageFileCommand.cs
class OpenImageFileCommand : ICommand {
	private ImageData _data;
	publlic OpenImageFileCommand(ImageData data) {
		_data = data;
	}

	public bool CanExecute(object parameter) 
	{
		return true;
	}

	public event EventHandler CanExecuteChanged;

	public void Execute(object parameter) 
	{
		var dlg = new OpenFileDialog()
        {
            Filter = "Image Files|*.jpg;*.png;*.bmp;*.gif"
        };
        if (dlg.ShowDialog() == true)
        {
            _data.ImagePath = 	dlg.FileName;
        }
	}
}
```

```csharp
// ./Commands/ZoomCommand.cs
class ZoomCommand : ICommand {
	private ImageData _data;
	publlic OpenImageFileCommand(ImageData data) {
		_data = data;
	}

	public bool CanExecute(object parameter) 
	{
		return _data.ImagePath != null;
	}

	public event EventHandler CanExecuteChanged;

	public void Execute(object parameter) 
	{
		var zoomType = (ZoomType)Enum.Parse(typeof(ZoomType), (string)parameter, true);
		switch (zoomType) {
			case ZoomType.ZoomIn:
				_data.Zoom *= 1.2;
				break;
			case ZoomType.ZoomOut:
				_data.Zoom /= 1.2;
				break;
			case ZoomType.ZoomNormal:
				_data.Zoom = 1.0;
				break;
		}
	}

	public event EventHandler CanExecuteChanged {
		add { CommandManager.RequerySuggested += value; }
		remove { CommandManager.RequerySuggested -= value; }
	}
}



enum ZoomType {
	ZoomIn,
	ZoomOut,
	ZoomNormal
}
```

```csharp
// MainWindow.xaml.cs
public partial class MainWindow : Window {
	public MainWindow() {
		InitializeComponent();
		DataContext = new ImageData();
	}
}
```

```xml
<!-- MainWindow.xaml -->
<Window x:Class="WpfApp2.MainWindow"
        xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        xmlns:d="http://schemas.microsoft.com/expression/blend/2008"
        xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
        xmlns:local="clr-namespace:WpfApp2"
        mc:Ignorable="d"
        Title="MainWindow" Height="450" Width="800">
    <Window.CommandBindings>
        <CommandBinding Command="Open" Executed="OnOpen" />
        <CommandBinding Command="IncreaseZoom" Executed="OnZoomIn" CanExecute="OnIsImageExist" />
        <CommandBinding Command="DecreaseZoom" Executed="OnZoomOut" CanExecute="OnIsImageExist" />
        <CommandBinding Command="local:Commands.ZoomNormalCommand" Executed="OnZoomNormal" CanExecute="OnIsImageExist" />
    </Window.CommandBindings>
    <Grid>
        <Grid.RowDefinitions>
            <RowDefinition Height="Auto"/>
            <RowDefinition/>
        </Grid.RowDefinitions>
        <ToolBar>
            <Button Command="{Binding OpenImageFileCommand}" Margin="6" Content="Open..." />
            <Button Content="Zoom In" Command="{Binding ZoomCommand}" CommandParameter="ZoomIn" Margin="6"/>
            <Button Content="Zoom Out" Command="{Binding ZoomCommand}" CommandParameter="ZoomOut" Margin="6"/>
            <Button Content="Normal" Command="{Binding ZoomCommand}" CommandParameter="ZoomNormal" Margin="6"/>
        </ToolBar>
        <ScrollViewer HorizontalScrollBarVisibility="Auto"
                      VerticalScrollBarVisibility="Auto"
                      Grid.Row="1">
            <Image Stretch="None"
                   Source="{Binding ImagePath}">
                <Image.LayoutTransform>
                    <ScaleTransform ScaleX="{Binding Zoom}" ScaleY="{Binding Zoom}"/>
                </Image.LayoutTransform>
            </Image>
        </ScrollViewer>
    </Grid>
</Window>

```



# 2. Creating a dependency property


```csharp
// Code snippet: propdp
public partial class SimpleControl : UserControl {
	public SimpleControl() {
		InitializeComponent();
	}

	public int YearPublished {
		get { return (int)GetValue(YearPublishedProperty); }
		set { SetValue(YearPublishedProperty, value); }
	}

	public static readonly DependencyProperty YearPublishedProperty = DependencyProperty.Register("YearPublished", typeof(int), typeof(SimpleControl), new UIPropertyMetadata(2000, OnValueChanged));

	private static void OnValueChanged(DependencyObject obj, DependencyPropertyChangedEventArgs e) {
		// do something when property changes 
	}
}
```

# 3. Creating a Attached Property


```csharp
// Code snippet: propa
class RotationManager : DependencyObject {
	public static double GetAngle(DependencyObject obj) 
    {
		return (double)obj.GetValue(AngleProperty);
	}

	public static void SetAngle(DependencyObject obj, double value) 
    {
		obj.SetValue(AngleProperty, value);
	}

	public static readonly DependencyProperty AngleProperty = 	DependencyProperty.RegisterAttached("Angle", typeof(double), typeof(RotationManager), new UIPropertyMetadata(0.0, OnAngleChanged));

    private static void OnAngleChanged(DependencyObject obj, DependencyPropertyChangedEventArgs e) 
    {
        var element = obj as UIElement;
        if (element != null)
        {
            element.RenderTransformOrigin = new Point(.5, .5);
            element.RenderTransform = new RotateTransform((double)e.NewValue);
        }
    }

}
```

An exception will be thrown if the object was not UIElement
