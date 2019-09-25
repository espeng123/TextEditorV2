import javax.swing.*;
import java.awt.*;

public class FrameTest {

	public static void main(String[] args) {
		 JFrame frame = new JFrame("GUI");
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setSize(800,800);
		 frame.getContentPane().setBackground(Color.black);
		 
		 String words = "<html><p>This is a long paragraph and I want it to break on its own.  " + 
				    "This is a long paragraph and I want it to break on its own.  " +
				    "This is a long paragraph and I want it to break on its own.  " +
				    "This is a long paragraph and I want it to break on its own.</p></html>";
		 
		 
		 JLabel label = new JLabel(words,JLabel.LEFT);
		 label.setVerticalAlignment(JLabel.TOP);
		 Dimension size = label.getPreferredSize();
		 System.out.println(size);
		 label.setBounds(21,21,758,758);
		 label.setForeground(Color.white);
		 label.setFont(new Font("Courier", Font.PLAIN, 14));
		 
		 frame.add(label);
		 frame.setLayout(null);
		 frame.setVisible(true);

	}

}
