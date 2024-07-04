import java.awt.event.*;

import java.awt.*;
import javax.swing.*;
 
public class ActionEventDemo implements ActionListener {
    private void createAndShowGUI() {

        JFrame frame = new JFrame("ActionEventDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JLabel yellowLabel = new JLabel();
        yellowLabel.setOpaque(true);
        yellowLabel.setBackground(new Color(248, 213, 131));
        yellowLabel.setPreferredSize(new Dimension(200, 180));

        JButton button = new JButton("Text");
        button.setBounds(50, 100, 95, 30); 
         
        // button.addActionListener(this);
        // or
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });


 
        frame.getContentPane().add(yellowLabel, BorderLayout.CENTER);
        frame.getContentPane().add(button);
        
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ActionEventDemo().createAndShowGUI();
            }
        });
    }

   
}
