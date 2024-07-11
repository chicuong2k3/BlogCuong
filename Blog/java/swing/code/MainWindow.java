import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;

class StudentTableModel extends AbstractTableModel {
    String[] colNames = { "Ma", "Ho Ten", "Ngay Sinh", "Hinh anh" };
    ArrayList<Student> dshs;
    public StudentTableModel(ArrayList<Student> dshs) {
        this.dshs = dshs;
        
    }
    @Override
    public int getRowCount() {
        return dshs.size(); 
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student hs = dshs.get(rowIndex);
        Object value = null;
        
        switch (columnIndex) {
            case 0:
                value = hs.getMa();
                break;
            case 1:
                value = hs.getHoTen();
                break;
            case 2:
                value = hs.getNgaySinh();
                break;    
            case 3:
                value = hs.getImageUrl();
                break;
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
        return value;
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Student hs = dshs.get(rowIndex);
        if (columnIndex == 0) {
            hs.setMa((int)value);
        }      
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return colNames[columnIndex];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (dshs.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }
    
    public void addRow(Student newHocSinh) {
        dshs.add(newHocSinh);
        fireTableRowsInserted(dshs.size() - 1, dshs.size() - 1);
    }
    
    
    
    public void updateRow(int rowIndex, int ma, String hoTen, LocalDate ngaySinh, String imageUrl) {
        if (rowIndex >= 0 && rowIndex < dshs.size()) {
            Student hs = dshs.get(rowIndex);
            hs.setMa(ma);
            hs.setHoTen(hoTen);
            hs.setNgaySinh(ngaySinh);
            hs.setImageUrl(imageUrl);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

}


public class MainWindow {
    JTable table;
    StudentTableModel model;
    
    public MainWindow() {

    }


    private void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Quản Lý Học Sinh");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setPreferredSize(new Dimension(800, 600));

        addComponentsToPane(frame.getContentPane());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void addComponentsToPane(Container pane) {
        pane.setLayout(new BorderLayout());

        JPanel childPanel1 = new JPanel();
        JPanel childPanel2 = new JPanel();
        childPanel1.setLayout(new GridBagLayout());
        childPanel2.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        pane.add(childPanel1, BorderLayout.NORTH);
        // BorderLayout.PAGE_START
        // BorderLayout.CENTER
        // BorderLayout.EAST, BorderLayout.NORTH, BorderLayout.WEST, BorderLayout.SOUTH
        pane.add(childPanel2, BorderLayout.SOUTH);


        JLabel label = new JLabel("Nhập tên học sinh: ");
        JTextField textField = new JTextField(30);
        JButton button = new JButton("Tìm kiếm");
        // button.setPreferredSize(new Dimension(100, 20));

        c.gridx = 0;
        c.gridy = 0;
        childPanel1.add(label, c);

        c.gridx = 1;
        c.gridy = 0;
        childPanel1.add(textField, c);

        c.gridx = 2;
        c.gridy = 0;
        childPanel1.add(button, c);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        JLabel label2 = new JLabel("Nhập mã học sinh: ");
        JTextField textField2 = new JTextField(30);
        JButton button2 = new JButton("Tìm kiếm");
        c.gridx = 0;
        c.gridy = 1;
        childPanel1.add(label2, c);

        c.gridx = 1;
        c.gridy = 1;
        childPanel1.add(textField2, c);

        c.gridx = 2;
        c.gridy = 1;
        childPanel1.add(button2, c);



        // Add Button
        JButton addBtn = new JButton("Them hoc sinh");

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // int ma = Integer.parseInt(textField1.getText());
                // String ten = textField2.getText();
                // double diem = Double.parseDouble(textField3.getText());
                // String hinhAnh = "";
                // String diaChi = textField4.getText();
                // String ghiChu = textField5.getText();
                // HocSinhFileOperation op = new HocSinhFileOperation();
                
                // if (img != null) {
                //     BufferedImage bufferedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                //     Graphics2D g2d = bufferedImage.createGraphics();
                //     g2d.drawImage(img, 0, 0, 80, 80, null);
                //     g2d.dispose();
                //     try {
                //         hinhAnh = textField1.getText() + "_" + textField2.getText() + ".png";
                //         File outputImage = new File(hinhAnh);
                //         ImageIO.write(bufferedImage, "png", outputImage);
                //     } catch (IOException ex) {
                //         System.err.println("Error while saving image: " + ex.getMessage());
                //     }
                // }
                
                
                // HocSinh hs = new HocSinh(ma, ten, diem, hinhAnh, diaChi, ghiChu);
                // op.themHocSinh(hs);
                // model.addRow(hs);
                
                // textField1.setText("");
                // textField2.setText("");
                // textField3.setText("");
                // textField4.setText("");
                // textField5.setText("");
                
            }
        });

        c.gridx = 0;
        c.gridy = 0;
        childPanel2.add(addBtn, c);


        //model = new StudentTableMode(dshs);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setAutoCreateRowSorter(true);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        JScrollPane scrollPane = new JScrollPane(
                table, 
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        childPanel2.add(scrollPane, c);
        
        
         
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                // int i = table.getSelectedRow();
                // int ma = Integer.parseInt(model.getValueAt(i, 0).toString());
                // textField1.setText(model.getValueAt(i, 0).toString());
                // textField2.setText(model.getValueAt(i, 1).toString());
                // textField3.setText(model.getValueAt(i, 2).toString());
                // textField4.setText(model.getValueAt(i, 3).toString());
                // textField5.setText(model.getValueAt(i, 4).toString());
                // HocSinh hocsinh = op.docHocSinh(ma);
                // String path = hocsinh.getHinhAnh();
                // img = new ImageIcon(path).getImage();
                // Image newImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    
                // imageLabel.setIcon(new ImageIcon(newImg));
            }
            
        });

    }

    public static void main(String[] args) {
        // DataStorage storage = new DataStorage("student-list.txt");
        // ArrayList<Student> students = storage.readStudents();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().createAndShowGUI();
            }
        });
    }

}

class Student {
    private int ma;
    private String hoTen;
    private LocalDate ngaySinh;
    private String imageUrl;

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Student(int ma, String hoTen, LocalDate ngaySinh, String imageUrl) {
        this.ma = ma;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Ma: " + ma + ". Ho Ten: " + hoTen + ". Ngay sinh: " + ngaySinh + ". image: " + imageUrl;
    }
}

class DataStorage {
    private String fileUrl;

    public DataStorage(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public ArrayList<Student> readStudents() {
        ArrayList<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileUrl))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Assuming DateHelper.convertToLocalDate is a valid method that converts a
                // string to LocalDate
                Student student = new Student(Integer.parseInt(parts[0]), parts[1],
                        DateHelper.convertToLocalDate(parts[2]), parts[3]);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return students;
    }
}

class DateHelper {
    public static LocalDate convertToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            return date;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
            return null;
        }
    }
}