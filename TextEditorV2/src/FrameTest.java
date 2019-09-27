import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FrameTest{

	public static void main(String[] args) {
		
		 JFrame frame = new JFrame("GUI");
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setSize(800,800);
		 frame.getContentPane().setBackground(Color.black);
		 frame.setLayout(new BorderLayout());
		 
		 String words = "<html><p>This is a long paragraph and I want it to break on its own.  " + 
				    "This is a long paragraph and I want it to break on its own.  " +
				    "This is a long paragraph and I want it to break on its own.  " +
				    "This is a long paragraph and I want it to break on its own.</p></html>";
		 
		 
		 JLabel label = new JLabel(words,JLabel.LEFT);
		 JLabel label1 = new JLabel("<html> <br> <br></html>");
		 JLabel label2 = new JLabel(" ");
		 JLabel label3 = new JLabel("             ");
		 JLabel label4 = new JLabel("             ");
		 label.setVerticalAlignment(JLabel.TOP);
		 Dimension size = label.getPreferredSize();
		 System.out.println(size);
		 label.setBounds(21,21,758,758);
		 label.setForeground(Color.white);
		 label.setFont(new Font("Courier", Font.PLAIN, 14));
		 
		 frame.add(label,BorderLayout.CENTER);
		 frame.add(label1,BorderLayout.NORTH);
		 frame.add(label2,BorderLayout.SOUTH);
		 frame.add(label3,BorderLayout.EAST);
		 frame.add(label4,BorderLayout.WEST);
		 frame.setVisible(true);

	}
}
