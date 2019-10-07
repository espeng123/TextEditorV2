
//Import a whole bunch of random shit
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FrameTest {

	// JFrame for the GUI
	static JFrame frame = new JFrame("GUI");
	// Main label for the text box
	static JLabel label = new JLabel("<html></html>", JLabel.LEFT);
	// Boolean for if shift is pressed
	static boolean shift = false;
	//Boolean for if command is pressed
	static boolean command = false;

	public static void main(String[] args) {

		// Setup the frame with size, background color, and close button
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.getContentPane().setBackground(Color.black);
		frame.setLayout(new BorderLayout());

		// Setup a JPanel for key typing
		JPanel panel = new JPanel();
		panel.setFocusTraversalKeysEnabled(false);
		frame.getContentPane().add(panel);
		panel.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//If the shift key has been released, set shift to false
				if (e.getKeyCode() == 16) {
					shift = false;
				}
				//If the command key has been released, set command to false
				if (e.getKeyCode() == 157) {
					command = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				// If the shift key has been pressed, set shift to true
				if (e.getKeyCode() == 16) {
					shift = true;
				}
				//If the command key has been pressed, set command to true
				if (e.getKeyCode() == 157) {
					command = true;
				}
				// Call the typeText function on the key that was just pressed
				typeText(e);
			}
		});
		panel.setFocusable(true);

		// Intelligent way to do borders
		JLabel label1 = new JLabel("<html> <br> <br></html>");
		JLabel label2 = new JLabel(" ");
		JLabel label3 = new JLabel("             ");
		JLabel label4 = new JLabel("             ");

		// Set up the location, size, allignment, font, and font color of the text box
		label.setVerticalAlignment(JLabel.TOP);
		label.setBounds(21, 21, 758, 758);
		label.setForeground(Color.white);
		label.setFont(new Font("Courier", Font.PLAIN, 14));

		// Add all content to the frame
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
		// Implements backspace
		if (keyTyped == 8) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			//Implements space			
			if (previousText.contains("&nbsp;")
					&& (previousText.substring(previousText.length() - 6).equals("&nbsp;"))) {
				// System.out.println(previousText.substring(previousText.length()-6));
				label.setText(previousText.substring(0, previousText.length() - 6) + "</html>");
			}

			else if (previousText.substring(previousText.length() - 6).equals("<html>")) {

			}
			//Adding non-breaking spaces
			else if (previousText.substring(previousText.length() - 6).equals("&emsp;")) {
				label.setText(previousText.substring(0, previousText.length() - 24) + "</html>");
			}

			//Implementing less than sign
			else if (previousText.substring(previousText.length() - 4).equals("&lt;")) {
				label.setText(previousText.substring(0, previousText.length() - 4) + "</html>");
			}

			//Implementing greater than sign
			else if (previousText.substring(previousText.length() - 4).equals("&gt;")) {
				label.setText(previousText.substring(0, previousText.length() - 4) + "</html>");
			}

			else if (previousText.substring(previousText.length() - 5).equals("&amp;")) {
				label.setText(previousText.substring(0, previousText.length() - 5) + "</html>");
			}

			else {
				label.setText(previousText.substring(0, previousText.length() - 1) + "</html>");
			}

			frame.repaint();
			// If the space key has been pressed
		} else if (keyTyped == 32) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			label.setText(previousText + "&nbsp;" + "</html>");
			frame.repaint();
			// If TAB was entered
		} else if (keyCode == 9) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&emsp;&emsp;&emsp;&emsp;";
			label.setText(previousText + "</html>");
			frame.repaint();
			// IF they tried to type “<”
		} else if (keyCode == 44 && shift) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&lt;";
			label.setText(previousText + "</html>");
			frame.repaint();
			// IF they tried to type “>”
		} else if (keyCode == 46 && shift) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&gt;";
			label.setText(previousText + "</html>");
			frame.repaint();
			// IF they tried to type “&”
		} else if (keyCode == 55 && shift) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			previousText += "&amp;";
			label.setText(previousText + "</html>");
			frame.repaint();
			// IF they pressed enter or return
		} else if (keyTyped == 10) {
			String previousText = label.getText();
			previousText = previousText.substring(0, previousText.length() - 7);
			label.setText(previousText + "<br>" + "</html>");
			frame.repaint();
			// Implements saving through command + s
		} else if (keyCode == 83 && command) {
			save();
			command = false;
			// Implements opening through command + o
		} else if (keyCode == 79 && command) {
			open();
			command = false;
		}

		//No ? box when hitting shift, caps lock, command, fn, control, alt, all the arrow keys, and right option keys 
		else if (keyCode != 16 && keyCode != 20 && keyCode != 157 && keyCode != 0 && keyCode != 17 && keyCode != 18
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

	// Save Function
	public static void save() {

		// Default file name
		String filename = "myText.txt";

		// File location chooser
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int r = j.showSaveDialog(null);
		// If the user chooses to save:
		if (r == JFileChooser.APPROVE_OPTION) {
			// Set the filename to the path of the selected file
			filename = j.getSelectedFile().getAbsolutePath();
		}
		// If the user forgets .txt add it automatically
		if (!filename.contains(".txt")) {
			filename += ".txt";
		}

		// Prepare the string to be saved
		String toSave = label.getText();
		// Remove the html tags
		toSave = toSave.substring(6, toSave.length() - 7);
		// Replace “&nbsp;” with a “ “
		if (toSave.contains("&nbsp;")) {
			toSave = toSave.replaceAll("&nbsp;", " ");
		}
		//Replace “&emsp;&emsp;&emsp;&emsp;” with a tab
		if (toSave.contains("&emsp;&emsp;&emsp;&emsp;")) {
			toSave = toSave.replaceAll("&emsp;&emsp;&emsp;&emsp;", "\t");
		}
		// Replace “&lt;” with a “<”
		if (toSave.contains("&lt;")) {
			toSave = toSave.replaceAll("&lt;", "<");
		}
		// Replace “&gt;” with a “>”
		if (toSave.contains("&gt;")) {
			toSave = toSave.replaceAll("&gt;", ">");
		}
		//Replace “&amp;” with a “&”
		if (toSave.contains("&amp;")) {
			toSave = toSave.replaceAll("&amp;", "&");
		}
		//Replace “<br>” with a enter
		if (toSave.contains("<br>")) {
			toSave = toSave.replaceAll("<br>", "\n");
		}

		// Create a buffered writer to save the file
		BufferedWriter bw = null;
		try {
			// Create a file at the designated filename
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			// Write the string to the file
			bw.write(toSave);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// Close the writer
				if (bw != null) {
					bw.close();
				}
			} catch (Exception ex) {
				System.out.println("Error in closing the BufferedWriter" + ex);
			}
		}
	}

	// Open Function
	public static void open() {
		// Initiate filename, currentLine, and text variables
		String filename = null;
		String currentLine;
		String text = "<html>";

		//Create an open dialog box
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		j.setFileFilter(filter);
		int r = j.showOpenDialog(null);
		// Get the filepath to the file the user wants open
		if (r == JFileChooser.APPROVE_OPTION) {
			filename = j.getSelectedFile().getAbsolutePath();
		}

		BufferedReader br = null;
		try {
			// Open the file and add line by line to the text
			FileReader fr = new FileReader(filename);
			br = new BufferedReader(fr);
			while ((currentLine = br.readLine()) != null) {

				text += currentLine + "<br>";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// Close the writer
				if (br != null) {
					br.close();
				}
			} catch (Exception ex) {
				System.out.println("Error in closing the BufferedWriter" + ex);
			}
		}
		// Adds the ending /html thingee so it work properlee
		text += "</html>";
	}

}
