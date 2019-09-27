import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FrameTest{
	
	static JFrame frame = new JFrame("GUI");
	static JLabel label = new JLabel("<html></html>",JLabel.LEFT);

	public static void main(String[] args) {
		
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setSize(800,800);
		 frame.getContentPane().setBackground(Color.black);
		 frame.setLayout(new BorderLayout());
		 
		 JPanel panel = new JPanel();
		 frame.getContentPane().add(panel);
		 panel.addKeyListener(new KeyListener() {
			 @Override
			 public void keyTyped(KeyEvent e) {}

			 @Override
			 public void keyReleased(KeyEvent e) {}

			 @Override
			 public void keyPressed(KeyEvent e) {
				 typeText(e.getKeyChar());
				 System.out.println("Pressed " + e.getKeyChar());
			 }
		 });
		 panel.setFocusable(true);

		 
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
	
	public static void typeText(char keyTyped)
	{
		String previousText = label.getText();
		previousText = previousText.substring(0,previousText.length()-7);
		label.setText(previousText+keyTyped+"</html>");
		frame.repaint();
	}
	
	
	
}
