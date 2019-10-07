import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FrameTest {

	static JFrame frame = new JFrame("GUI");
	static JLabel label = new JLabel("<html></html>", JLabel.LEFT);
	static boolean shift = false;
	static boolean command = false;

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
				if (e.getKeyCode() == 157) {
					command = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				if (e.getKeyCode() == 16) {
					shift = true;
				}
				if (e.getKeyCode() == 157) {
					command = true;
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
		} else if (keyCode == 83 && command) {
			save();
		} else if (keyCode == 79 && command) {
			open();
		} else if (keyCode != 16 && keyCode != 20 && keyCode != 157 && keyCode != 0 && keyCode != 17 && keyCode != 18
				&& keyCode != 37 && keyCode != 38 && keyCode != 39 && keyCode != 40 && keyCode != 65406
				&& keyCode != 27) {
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

	public static void save() {

		String filename = "myText.txt";

		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int r = j.showSaveDialog(null);
		if (r == JFileChooser.APPROVE_OPTION)

		{
			// set the label to the path of the selected file
			filename = j.getSelectedFile().getAbsolutePath();
		}
		if (!filename.contains(".txt")) {
			filename += ".txt";
		}

		String toSave = label.getText();
		toSave = toSave.substring(6, toSave.length() - 7);
		if (toSave.contains("&nbsp;")) {
			toSave = toSave.replaceAll("&nbsp;", " ");
		}
		if (toSave.contains("&emsp;&emsp;&emsp;&emsp;")) {
			toSave = toSave.replaceAll("&emsp;&emsp;&emsp;&emsp;", "\t");
		}
		if (toSave.contains("&lt;")) {
			toSave = toSave.replaceAll("&lt;", "<");
		}
		if (toSave.contains("&gt;")) {
			toSave = toSave.replaceAll("&gt;", ">");
		}
		if (toSave.contains("&amp;")) {
			toSave = toSave.replaceAll("&amp;", "&");
		}
		if (toSave.contains("<br>")) {
			toSave = toSave.replaceAll("<br>", "\n");
		}
		System.out.println(toSave);

		BufferedWriter bw = null;
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(toSave);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println("Error in closing the BufferedWriter" + ex);
			}
		}
	}

	public static void open() {
		String filename = null;
		String currentLine;
		String text = "<html>";

		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		j.setFileFilter(filter);
		int r = j.showOpenDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			filename = j.getSelectedFile().getAbsolutePath();
		}

		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			while ((currentLine = br.readLine()) != null) {

				text += currentLine + "<br>";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text+="</html>";
		
		if(text.contains("</html>"))
		{
			label.setText(text);
		}
	}

}
