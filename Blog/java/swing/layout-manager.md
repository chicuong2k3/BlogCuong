# Layout Manager

Layout is the way components are arranged within a container, making them appear in desired positions. When the size of the container changes, the size and relative position of the components within the container also change accordingly.

## Flow Layout

The Flow Layout arranges components from left to right. When space runs out, it wraps components to the next line and starts again from left to right.

[!code-java[](code/FlowLayoutDemo.java)]

## Border Layout

Border Layout places components in the following areas of the container: top, bottom, left, right, center.

[!code-java[](code/BorderLayoutDemo.java)]

## CardLayout

Card Layout allows different components to be placed on the same space at different times.

[!code-java[](code/CardLayoutDemo.java)]

## Grid Layout

Grid Layout displays components on the container in a grid format. The container is divided into a grid of cells in multiple rows and columns.
- Cells have equal size.
- Each component is displayed in one cell of the grid.

[!code-java[](code/GridLayoutDemo.java)]

## GridBag Layout

GridBagLayout places components within a grid of rows and columns, allowing components to span multiple rows and columns. Rows and columns in the grid of cells can have different sizes.

Define the size and properties of the component by providing constraints for each component.

[!code-java[](code/GridBagLayoutDemo.java)]

## Box Layout

BoxLayout arranges components sequentially from left to right on a single row or from top to bottom in a single column.

[!code-java[](code/BoxLayoutDemo.java)]

## Spring Layout

SpringLayout works based on defining relationships between the edges of components. 

Set constraints for a component through the SpringLayout.Constraints object.

[!code-java[](code/SpringLayoutDemo.java)]

## Group Layout

- Sequential: Components are arranged sequentially in one direction.
- Parallel: Components are arranged within the same space (of the working direction):
    - Vertical: baseline, top, bottom
    - Horizontal: left, right, center

> [!NOTE]
> A group can be nested inside another group.


