
# Introduction to Java Swing

JFC (Java Foundation Class) includes a group of features for building graphical user interfaces (GUIs) and adding rich graphics functionality and interactivity to Java applications.

Swing has 18 packages, but the two most commonly used packages are **javax.swing** and **javax.swing.event**.

javax.swing contains the standard components of Swing.

javax.swing.event contains the classes and interfaces that support working with events and listeners.

## Swing Components

### Top-Level Containers

There are three types of top-level containers: JFrame, JDialog, and JApplet.

- GUI components must reside within a containment hierarchy.
- Each GUI component can only be contained within one container.
- Each top-level container has a content pane that holds other components.
- A menu bar can be added to a top-level container. The menu bar resides within the top-level container but outside the content pane.

> [!NOTE]
> Containment hierarchy is the component tree with a top-level container as the root.

> [!NOTE]
> A Swing application with one window and two dialogs has three containment hierarchies. One containment hierarchy has its root as JFrame (corresponding to the window), and two containment hierarchies have their roots as JDialog (corresponding to the dialogs).
 
> [!NOTE]
> The default content pane inherits from JComponent.
> Retrieve the content pane of a top-level container using the getContentPane method.

> [!WARNING]
> The getContentPane method returns a Container object, not a JComponent object.

[!code-java[](code/TopLevelDemo.java?highlight=10-13,15-16,21)]


### JPanels

JPanel is a container component where we can place other components inside it. When adding other components to a JPanel, we can also specify a particular layout.

```java
JPanel contentPane = new JPanel(new BorderLayout());
contentPane.setBorder(someBorder);
contentPane.add(someComponent, BorderLayout.CENTER);
contentPane.add(anotherComponent, BorderLayout.PAGE_END);

topLevelContainer.setContentPane(contentPane);
```

> [!TIP]
> Each top-level container depends on an intermediate container called the root pane. The root pane manages the content pane, menu bar, and other containers (such as layered pane and glass pane).
> The glass pane is often used to intercept events occurring on the top-level container or to draw over multiple components.

![Root Pane](https://docs.oracle.com/javase/tutorial/figures/ui/ui-rootPane.gif)


[!code-java[](code/MainWindow.java)]