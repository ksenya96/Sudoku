import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class Sudoku extends JComponent {
   
  private static JPanel panel;
  
  private static JFrame frame;
  private static Generation gen = new Generation();

  public static void main (String[] args) {
    frame = new JFrame ("Судоку");
    frame.setSize (600, 600);
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    frame.setResizable (false);
        panel = new JPanel () {
      
      public void paintComponent (Graphics g) {
         Image background = Toolkit.getDefaultToolkit().getImage("sudoku.jpg");
         g.drawImage (background, 0, 0, 600, 600, this);
         gen.paint(g);

      }
    };
    panel.setLayout(null);
    gen.TextFields(frame, panel);
    
        

    MyActionListener button_listener = new MyActionListener ();
    JButton NewGame = new JButton ("Новая игра");
    NewGame.setBounds (450, 50, 100, 60);
    NewGame.setActionCommand ("NewGame");
    NewGame.addActionListener (button_listener);
    panel.add (NewGame);

    JButton Clear = new JButton ("Сброс");
    Clear.setBounds (450, 120, 100, 60);
    Clear.setActionCommand ("Clear");
    Clear.addActionListener (button_listener);
    panel.add (Clear);

    frame.add (panel);
    frame.setVisible (true);
    frame.repaint ();
    /*try {Thread.sleep (1000);} catch (Exception e) {};
    gen.RemoveTextFields(panel);
    frame.repaint();*/
  }
  
  

  private static class MyActionListener implements ActionListener {
     public void actionPerformed (ActionEvent ae) {
        String command = ae.getActionCommand();
        switch (command) {
           case "NewGame": {
              gen.RemoveTextFields(panel); 
              gen = new Generation();  
                        
              frame.repaint();
              gen.TextFields(frame, panel);
           }
           case "Clear": {
              gen.ClearTextFields();
           } 
        }
     }
  }
  
  
  
  

  
}