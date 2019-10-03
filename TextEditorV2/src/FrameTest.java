import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FrameTest {

	static JFrame frame = new JFrame("GUI");
	static JLabel label = new JLabel("<html></html>", JLabel.LEFT);
	static boolean shift = false;

	public static void main(String[] args) {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.getContentPane().setBackground(Color.black);
		frame.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setFocusTraversalKeysEnabled(false);
		frame.getContentPane().add(panel);
		panel.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 16) {
					shift = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				if (e.getKeyCode() == 16) {
					shift = true;
				}
				typeText(e);
			}
		});
		panel.setFocusable(true);

		JLabel label1 = new JLabel("<html> <br> <br></html>");
		JLabel label2 = new JLabel(" ");
		JLabel label3 = new JLabel("             ");
		JLabel label4 = new JLabel("             ");
		label.setVerticalAlignment(JLabel.TOP);
		Dimension size = label.getPreferredSize();
		// System.out.println(size);
		label.setBounds(21, 21, 758, 758);
		label.setForeground(Color.white);
		label.setFont(new Font("Courier", Font.PLAIN, 14));

		frame.add(label, BorderLayout.CENTER);
		frame.add(label1, BorderLayout.NORTH);
		frame.add(label2, BorderLayout.SOUTH);
		frame.add(label3, BorderLayout.EAST);
		frame.add(label4, BorderLayout.WEST);
		frame.setVisible(true);

	}

	public static void typeText(KeyEvent e) {
		int keyCode = e.getKeyCode();
		char keyTyped = e.getKeyChar();
		if (keyTyped == 8) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			if (previousText.contains("&nbsp;")
					&& (previousText.substring(previousText.length() - 6).equals("&nbsp;"))) {
				// System.out.println(previousText.substring(previousText.length()-6));
				label.setText(previousText.substring(0, previousText.length() - 6) + "</html>");
			} else if (previousText.substring(previousText.length() - 6).equals("<html>")) {

			} else if (previousText.substring(previousText.length() - 6).equals("&emsp;")) {
				label.setText(previousText.substring(0, previousText.length() - 24) + "</html>");
			} else if (previousText.substring(previousText.length() - 4).equals("&lt;")) {
				label.setText(previousText.substring(0, previousText.length() - 4) + "</html>");
			} else if (previousText.substring(previousText.length() - 4).equals("&gt;")) {
				label.setText(previousText.substring(0, previousText.length() - 4) + "</html>");
			} else if (previousText.substring(previousText.length() - 5).equals("&amp;")) {
				label.setText(previousText.substring(0, previousText.length() - 5) + "</html>");
			} else {
				label.setText(previousText.substring(0, previousText.length() - 1) + "</html>");
			}

			frame.repaint();
		} else if (keyTyped == 32) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			label.setText(previousText + "&nbsp;" + "</html>");
			frame.repaint();
		} else if (keyCode == 9) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&emsp;&emsp;&emsp;&emsp;";
			label.setText(previousText + "</html>");
			frame.repaint();
		} else if (keyCode == 44 && shift) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&lt;";
			label.setText(previousText + "</html>");
			frame.repaint();
		} else if (keyCode == 46 && shift) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&gt;";
			label.setText(previousText + "</html>");
			frame.repaint();
		} else if (keyCode == 55 && shift) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&amp;";
			label.setText(previousText + "</html>");
			frame.repaint();
		} else if (keyTyped == 10) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			label.setText(previousText + "<br>" + "</html>");
			frame.repaint();
		} else if (keyCode != 16 && keyCode != 20 && keyCode != 157 && keyCode != 0 && keyCode != 17 && keyCode != 18
				&& keyCode != 37 && keyCode != 38 && keyCode != 39 && keyCode != 40) {
			if (keyCode == 44 || keyCode == 46 || keyCode == 55) {
				if (!shift) {
					String previousText = label.getText();
					previousText = previousText.substring(0, previousText.length() - 7);
					label.setText(previousText + keyTyped + "</html>");
					frame.repaint();
				}
			} else {
				String previousText = label.getText();
				previousText = previousText.substring(0, previousText.length() - 7);
				label.setText(previousText + keyTyped + "</html>");
				frame.repaint();
			}
		}
	}

}
