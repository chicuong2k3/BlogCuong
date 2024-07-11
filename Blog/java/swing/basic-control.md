
# Common Controls

## JTabbedPane

```java
JTabbedPane mainJTabbedPane = new JTabbedPane();
mainJTabbedPane.addTab("Tài khoản", tkPanel);
mainJTabbedPane.addTab("Môn học", new MonHocPanel());
mainJTabbedPane.addTab("Học kì", new HocKiPanel());
mainJTabbedPane.addTab("Lớp học", new LopHocPanel());
mainJTabbedPane.addTab("Kỳ đăng ký học phần", new KyDKHPPanel());
mainJTabbedPane.addTab("Học phần", new HocPhanPanel());
```

## JOptionPane

```java
JOptionPane.showMessageDialog(chooseUserDialog, "Tên group không được trống", "Lỗi tạo group", JOptionPane.WARNING_MESSAGE);

String[] options = { "Thêm", "Ghi đè" };
int choice = JOptionPane.showOptionDialog(this,
		"Bạn muốn thêm các học sinh vào danh sách có sẵn hoặc ghi đè thành danh sách mới?", "Thêm hay ghi đè", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
if (choice == 0)
	Main.danhSachHocSinh.merge(FileManager.importFromCSV(fileName));
else
	Main.danhSachHocSinh = FileManager.importFromCSV(fileName);

int confirmDelete = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá những học sinh sau: a, b, c");
if (confirmDelete == JOptionPane.YES_OPTION) {
	
}
```

## JDialog

Giống JFrame nhưng có thêm 2 cái này:

```java
this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);	//tránh bấm mấy cái đằng sau
this.setLocationRelativeTo(null);
```

## ComboBox

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ComboBoxDemo extends JPanel implements ActionListener {
	JLabel picture;

	public ComboBoxDemo() {
		super(new BorderLayout());

		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };

		JComboBox<String> petList = new JComboBox<String>(petStrings);
		petList.setSelectedIndex(4);
		petList.addActionListener(this);

		picture = new JLabel();
		picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
		picture.setHorizontalAlignment(JLabel.CENTER);
		updateLabel(petStrings[petList.getSelectedIndex()]);
		picture.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		picture.setPreferredSize(new Dimension(177, 122 + 10));

		add(petList, BorderLayout.PAGE_START);
		add(picture, BorderLayout.PAGE_END);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	public void actionPerformed(ActionEvent e) {
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		String petName = (String) cb.getSelectedItem();
		updateLabel(petName);
	}

	protected void updateLabel(String name) {
		ImageIcon icon = createImageIcon("images/" + name + ".gif");
		picture.setIcon(icon);
		picture.setToolTipText("A drawing of a " + name.toLowerCase());
		if (icon != null) {
			picture.setText(null);
		} else {
			picture.setText("Image not found");
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ComboBoxDemo.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame("ComboBoxDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new ComboBoxDemo();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
```

## JTable

```java
public void updateTable(Object[][] objectMatrix) {
		theTable.setModel(new DefaultTableModel(objectMatrix, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
}


JTable theTable = new JTable();
theTable.setRowHeight(30);

JScrollPane theScrollPane = new JScrollPane();
theScrollPane.setViewportView(theTable);

theTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	public void valueChanged(ListSelectionEvent event) {
					
	}
});

theTable.getRowCount();
theTable.setRowSelectionInterval(0, 0);
theTable.getSelectedRow();
theTable.getValueAt(selectedRow, i).toString();
```

## Bonus JDBC

```java
import java.sql.*;

public class MyJDBC {
	public static void main(String args[]) {
		Connection con = null;

		try {

//			String driverName = "org.gjt.mm.mysql.Driver"; // MySQL MM JDBC driver
//	        Class.forName(driverName);
//	    
//	        String serverName = "localhost";
//	        String mydatabase = "mydatabase";
//	        String url = "jdbc:mysql://" + serverName +  "/" + mydatabase; // a JDBC url
//	        String username = "username";
//	        String password = "password";
//	        connection = DriverManager.getConnection(url, username, password);

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager
.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QLHS;user=hao;password=123");

//			Statement st = con.createStatement();
//			String strsql = "select * from HOCSINH";
//			ResultSet rs = st.executeQuery(strsql);
			PreparedStatement pst = con.prepareStatement("SELECT * FROM HOCSINH");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				System.out.print(rs.getString("MAHS") + " | ");
				System.out.print(rs.getNString("TENHS") + " | ");
				System.out.print(rs.getFloat("DIEM") + " | ");
				System.out.print(rs.getNString("HINHANH") + " | ");
				System.out.print(rs.getNString("DIACHI") + " | ");
				System.out.println(rs.getNString("GHICHU"));
			}

			String addStatement = "INSERT INTO HOCSINH "
					+ "(MAHS, TENHS, DIEM, HINHANH, DIACHI, GHICHU) VALUES ('218', N'Nguyễn Thị E', 9.3, 'C:\\Users\\Admin\\Desktop\\aa.png', N'địa chỉ bủh', N'no ghi chú')";

//			Statement st = con.createStatement();
//			st.executeUpdate(addStatement);
			pst = con.prepareStatement(addStatement);
			pst.execute();

			con.close();
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}
}

```

 



