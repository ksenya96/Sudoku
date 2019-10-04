import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.*;
import java.util.*;
import javax.swing.text.MaskFormatter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class Generation {
  private static int size;
  private static int x0, y0;
  private static int SquareSize;
  private static int[][] field;
  private static int[][] field0;  
  private static int read_num = 0;
  private static ArrayList<JFormattedTextField> list = new ArrayList<JFormattedTextField>();
  private static int FalseRow = -1, FalseCol = -1, FalseArea = -1;

  public Generation () {
    init();
  }

  private void init () {
    size = 9;
    x0 = 50;
    y0 = 50;
    SquareSize = 40;
    field = new int[size][size];
    field0 = new int[size][size];
    generation();
    for (int l = 0; l < size; l++)
          for (int m = 0; m < size; m++)
              field0[l][m] = field[l][m];

    DeleteCells ();
    FalseRow = -1;
    FalseCol = -1;
    FalseArea = -1;
     //addKeyListener (this);

  }
  void paint (Graphics g) {
     Graphics2D g2 = (Graphics2D)g;
           
     int font_size = 24;
     g2.setFont (new Font ("Serif", Font.BOLD, font_size));
     
     FontMetrics fm = g2.getFontMetrics ();
      
     g2.setColor (new Color (255, 255, 224));
     g2.fillRect (x0, y0, SquareSize * size, SquareSize * size);
       
     //рисуем игровое поле
     g2.setColor (Color.red);
     if (FalseRow != -1) {
        int x = x0, y = y0 + FalseRow * SquareSize;
        for (int j = 0; j < size; j++) {
          g2.fillRect (x, y, SquareSize, SquareSize);
          x += SquareSize;
        }
        FalseRow = -1;
     }

     g2.setColor (Color.red);
     if (FalseCol != -1) {
        int x = x0 + FalseCol * SquareSize, y = y0;
        for (int j = 0; j < size; j++) {
          g2.fillRect (x, y, SquareSize, SquareSize);
          y += SquareSize;
        }
        FalseCol = -1;
     }

     g2.setColor (Color.red);
     if (FalseArea != -1) {
         int k = (int)Math.sqrt (size);
         int x;
         int y = y0 + FalseArea / k * k * SquareSize;
         for (int i = 0; i < k; i++) {
            x = x0 + FalseArea % k * k * SquareSize;
            for (int j = 0; j < k; j++) {
              g2.fillRect (x, y, SquareSize, SquareSize);
              x += SquareSize; 
            }
            y += SquareSize;
         }
         FalseArea = -1;
     }


        
     g2.setColor (Color.black);
     int x = x0, y = y0;
     for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {                               
           g2.drawRect (x, y, SquareSize, SquareSize); 
           if (field[i][j] != 0)         
              g2.drawString ("" + field[i][j], x + SquareSize / 2 - fm.charWidth ('a') / 3, y + SquareSize / 2 + fm.getHeight () / 3);
           x += SquareSize;
        }
        x = x0;
        y += SquareSize;
     }
       
     g2.setStroke (new BasicStroke (4));   
         
     //жирные линии
     x = x0;
     y = y0;
     for (int i = 0; i <= size; i += (int)Math.sqrt (size)) {
        y = y0 + i * SquareSize; 
        g2.drawLine (x, y, x + size * SquareSize, y);       
     }
     
     x = x0;
     y = y0;
     for (int i = 0; i <= size; i += (int)Math.sqrt (size)) {
        x = x0 + i * SquareSize; 
        g2.drawLine (x, y, x, y + + size * SquareSize);       
     }

  }

  void TextFields (JFrame frame, JPanel panel) {
     list = new ArrayList<JFormattedTextField>();
    //слушатель текстовых полей
    DocumentListener listener = new DocumentListener() {
      public void insertUpdate(DocumentEvent event) {
          RecordInField(frame);
      }
      public void removeUpdate(DocumentEvent event) {
         
      }
      public void changedUpdate(DocumentEvent event) {  }
    };

    int x = x0, y = y0;
    for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
               if (field[i][j] == 0) {                     
                 MaskFormatter format = null;
                 try {
                   format = new MaskFormatter ("#");
                 }
                 catch (Exception e) {}
                 format.setValidCharacters ("123456789");
                 JFormattedTextField textfield = new JFormattedTextField(format);
                 list.add (textfield);
                 textfield.setBounds (x + 5, y + 5, SquareSize - 5 * 2, SquareSize - 5 * 2);
                 textfield.setHorizontalAlignment (JTextField.CENTER);
                 textfield.setFont (new Font ("Serif", Font.PLAIN, 24));
                 textfield.setForeground (Color.blue); 
                 //при редактировании не возвращать предыдущее значение    
                 textfield.setFocusLostBehavior(JFormattedTextField.PERSIST);
                 //textfield.addKeyListener (new MyKeyListener ());   
                 textfield.getDocument().addDocumentListener (listener);
                 panel.add (textfield);
               }
               x += SquareSize;
            }
            x = x0;
            y += SquareSize;
    }
    
  }

  void ClearTextFields () {
    for (int i = 0; i < list.size(); i++) 
      list.get(i).setText ("");

  }
  void RemoveTextFields (JPanel panel) {
    for (int i = 0; i < list.size(); i++)
      panel.remove (list.get(i));
      //panel.revalidate();
  }



  private void generation () {
    int value;
    Random r = new Random();
    int i = 0, prev_i = 0;
    int k_r = 0;
    //генерируем случайную матрицу
    while (i < 9) {
       int j = 0;
       
       while (j < 9) {
          int k_c = 0;
          value = r.nextInt(10 - 1) + 1;
          while (k_c < 100 && (!CheckRow (value, i, j) || !CheckCol (value, i, j) || !CheckArea(value, i, j))) {
            //System.out.println (value);
            value = r.nextInt(10 - 1) + 1;
            k_c++;
          }
          
          if (k_c < 100) {
            field[i][j] = value;
            //System.out.println("value " + value + i + j + " " + k_c);
            j++;
          }
          else {
            prev_i = i;
            i--; 
            k_r++;
            break;
            
          }
       }
       
       //если не получилось, повторяем заново
       i++;
       if (k_r >= 10) {
         i = 0;
         k_r = 0;
         for (int k = 0; k < 9; k++)
           for (int l = 0; l < 9; l++)
              field[k][l] = 0;
         }
       }

       
  }
  //удаление ячеек
  private void DeleteCells () {
      int k = 0;
      int row, col;
      Random r = new Random();
      while (k < 35) {
         row = r.nextInt (9);
         col = r.nextInt (9);
         if (field[row][col] != 0) {
             field[row][col] = 0;
             k++;
         }
      }

  }

  
  
  //записывать в поле
  private void RecordInField (JFrame frame) {
    //System.out.println ("Here");
    String s;
    int x, y;
    int value;
    int r, c;
    JFormattedTextField tf;
    for (int i = 0; i < list.size(); i++) {
       tf = list.get(i);
       s = tf.getText ();
       
       x = tf.getX() - x0;
       y = tf.getY() - y0;
       r = y / SquareSize;
       c = x / SquareSize;
       if (!(s.equals ("")) && !(s.equals (" "))) {
          value = Integer.parseInt (s);
             
          if (!CheckRow (value, r, c) | !CheckCol (value, r, c) | !CheckArea(value, r, c)) {
             tf.setForeground (Color.red); 
          }     
          else {
            field[r][c] = value;
            tf.setForeground (Color.blue);
          }
       }
       else {
          field[r][c] = 0;
       }

       
     
                    
    }
    //System.out.println (FalseRow);
    /*try {Thread.sleep (20);}
    catch (Exception e) {}*/
    //System.out.println (FalseRow);

    int all = 0;
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
         if (field[i][j] == field0[i][j])
             all++;
    if (all == size * size)
         frame.getGraphics().drawString("Молодец!", 50, 50);

    frame.repaint(); 
  }

  private void WriteField () {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++)
       System.out.print (field[i][j] + " ");
      System.out.println ();
    }
    System.out.println ();
  }

  //проверить совпадения в строке
  private boolean CheckRow (int value, int row, int col) {
     int j = 0;
     boolean found = true;
     while (j < size && found) {
          if (field[row][j] == value && j != col) {             
             found = false;             
             FalseRow = row;
          }
          j++;
     }
     return found;       
  }

  //проверить совпадение в столбце
  private boolean CheckCol (int value, int row, int col) {
     int i = 0;
     boolean found = true;
     while (i < size && found) {
          if (field[i][col] == value && i != row) {             
             found = false;
             FalseCol = col;
          }
          i++;
     }
     return found;       
  }

  //проверить совпадение в области
  private boolean CheckArea (int value, int row, int col) {
     int k = (int)Math.sqrt (size);
     int r = row - row % k;
     int c = col - col % k;
     boolean found = true;
     int i = r, j = c;
     while (found && i < r + k) {
          j = c;
          while (found && j < c + k) {
             if (field[i][j] == value && (i != row || j != col)) {
                found = false;
                FalseArea = (row / k) * k + col / k;
             }
             j++;
          }
          i++;
     }
     return found;
  }


  
}