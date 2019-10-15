
//Import a whole bunch of random stuff
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class frame {
	// Variables
	// Filename
	String filename = "/Untitled.txt";
	// JFrame for the GUI
	JFrame frame = new JFrame("");
	// Main label for the text box
	JLabel label = new JLabel("<html></html>", JLabel.LEFT);
	// Boolean for if shift is pressed
	boolean shift = false;
	// Boolean for if command is pressed
	boolean command = false;
	// String of the previous save
	String prevSave = "";
	// Number of windows
	static int windows = 0;

	// Constructor
	public frame(int xSize, int ySize, String text, String filename) {
		frame.setTitle("Text Editor - " + filename.substring((filename.lastIndexOf("/")+1),filename.indexOf(".txt")));
		windows++;
		String previousText = label.getText();
		previousText = previousText.substring(0, previousText.length() - 7);
		if (text != null) {
			previousText += text;
		}
		label.setText(previousText + "|</html>");
		prevSave = toPlainText(label.getText());
		this.filename = filename;

		listeners();

		formatStuff(xSize, ySize);
	}

	public frame(int xSize, int ySize) {
		frame.setTitle("Text Editor - " + filename.substring((filename.lastIndexOf("/")+1),filename.indexOf(".txt")));
		windows++;
		String previousText = label.getText();
		previousText = previousText.substring(0, previousText.length() - 7);
		label.setText(previousText + "|</html>");
		prevSave = toPlainText(label.getText());

		listeners();

		formatStuff(xSize, ySize);
	}

	// Methods
	public void formatStuff(int xSize, int ySize) {
		// Setup the frame with size, background color, and close button
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(xSize, ySize);
		frame.getContentPane().setBackground(Color.black);
		frame.setLayout(new BorderLayout());

		// Intelligent way to do borders
		JLabel label1 = new JLabel("<html> <br> <br></html>");
		JLabel label2 = new JLabel(" ");
		JLabel label3 = new JLabel("             ");
		JLabel label4 = new JLabel("             ");

		// Set up the location, size, alignment, font, and font color of the text box
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

	public void listeners() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!isSaved(label.getText())) {
					int result = JOptionPane.showConfirmDialog(null,
							"Your work is not saved. Do you wish to save it now?", "Confirm", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (result == JOptionPane.NO_OPTION) {
						windows--;
						frame.dispose();
					} else {
						save();
					}
				} else {
					windows--;
					frame.dispose();
				}

				if (windows <= 0) {
					System.exit(0);
				}
			}
		});

		// Setup a JPanel for key typing
		JPanel panel = new JPanel();
		panel.setFocusTraversalKeysEnabled(false);
		frame.getContentPane().add(panel);
		panel.setFocusable(true);

		panel.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// If the shift key has been released, set shift to false
				if (e.getKeyCode() == 16) {
					shift = false;
				}
				// If the command key has been released, set command to false
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
				// If the command key has been pressed, set command to true
				if (e.getKeyCode() == 157) {
					command = true;
				}
				// Call the typeText function on the key that was just pressed
				typeText(e);
			}
		});
	}

	private void typeText(KeyEvent e) {

		int keyCode = e.getKeyCode();
		char keyTyped = e.getKeyChar();
		String previousText = label.getText();
		if (previousText.equals("<html></html>")) {
			previousText = previousText.substring(0, previousText.length() - 7);
		} else {
			previousText = previousText.substring(0, previousText.length() - 8);
		}
		// Implements backspace
		if (keyTyped == 8) {
			previousText = delete(previousText);
			// If the space key has been pressed
		} else if (keyTyped == 32) {
			previousText += "&nbsp;";
			// If TAB was entered
		} else if (keyCode == 9) {
			previousText += "&emsp;&emsp;&emsp;&emsp;";
			// IF they tried to type “<”
		} else if (keyCode == 44 && shift) {
			previousText += "&lt;";
			// IF they tried to type “>”
		} else if (keyCode == 46 && shift) {
			previousText += "&gt;";
			// IF they tried to type “&”
		} else if (keyCode == 55 && shift) {
			previousText += "&amp;";
			// IF they pressed enter or return
		} else if (keyTyped == 10) {
			previousText += "<br>";
			// Implements saving through command + s
		} else if (keyCode == 83 && command) {
			save();
			command = false;
			// Implements opening through command + o
		} else if (keyCode == 79 && command) {
			open();
			command = false;
		} else if (keyCode == 78 && command) {
			new frame(800, 800);
			command = false;
		}
		// No ? box when hitting shift, caps lock, command, fn, control, alt, all the
		// arrow keys, and right option keys
		else if (keyCode != 16 && keyCode != 20 && keyCode != 157 && keyCode != 0 && keyCode != 17 && keyCode != 18
				&& keyCode != 37 && keyCode != 38 && keyCode != 39 && keyCode != 40 && keyCode != 65406
				&& keyCode != 27) {
			if (keyCode == 44 || keyCode == 46 || keyCode == 55 || keyCode == 83 || keyCode == 79 || keyCode == 78) {
				if (!shift && !command) {
					previousText += keyTyped;

				}
			} else {
				previousText += keyTyped;

			}
		}
		previousText += "|</html>";
		label.setText(previousText);
		setSaveStatus();
		frame.repaint();
	}

	// Save To File Function
	private void save() {

		// Default file name
		if (filename.equals("/Untitled.txt")) {
			// File location chooser
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			// j.setDefaultCloseOperation(JFileChooser.EXIT_ON_CLOSE);
			int r = j.showSaveDialog(null);
			// If the user chooses to save:
			if (r == JFileChooser.APPROVE_OPTION) {
				// Set the filename to the path of the selected file
				filename = j.getSelectedFile().getAbsolutePath();

				// If the user forgets .txt add it automatically
				if (!filename.contains(".txt")) {
					filename += ".txt";
				}
			} else {
				command = false;
			}
		}
		String toSave = toPlainText(label.getText());

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
			prevSave = toSave;
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
		setSaveStatus();
		frame.setTitle("Text Editor - " + filename.substring((filename.lastIndexOf("/")+1),filename.indexOf(".txt")));
	}

	// Open Function
	private void open() {

		// Initiate currentLine, and text variables
		String openFilename;
		String currentLine;
		String text = "";

		// Create an open dialog box
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		j.setFileFilter(filter);
		int r = j.showOpenDialog(null);
		// Get the filepath to the file the user wants open
		if (r == JFileChooser.APPROVE_OPTION) {
			openFilename = j.getSelectedFile().getAbsolutePath();

			BufferedReader br = null;
			try {
				// Open the file and add line by line to the text
				FileReader fr = new FileReader(openFilename);
				br = new BufferedReader(fr);
				currentLine = br.readLine();
				while (currentLine != null) {
					text += currentLine + "<br>";
					currentLine = br.readLine();
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

			text = toHtml(text);
			
			new frame(800, 800, text, openFilename);
		} else {
			command = false;
		}

	}

	private String toPlainText(String text) {
		// Remove the html tags
		text = text.substring(6, text.length() - 8);
		// Replace “&nbsp;” with a “ “
		if (text.contains("&nbsp;")) {
			text = text.replaceAll("&nbsp;", " ");
		}
		// Replace “&emsp;&emsp;&emsp;&emsp;” with a tab
		if (text.contains("&emsp;&emsp;&emsp;&emsp;")) {
			text = text.replaceAll("&emsp;&emsp;&emsp;&emsp;", "\t");
		}
		// Replace “&lt;” with a “<”
		if (text.contains("&lt;")) {
			text = text.replaceAll("&lt;", "<");
		}
		// Replace “&gt;” with a “>”
		if (text.contains("&gt;")) {
			text = text.replaceAll("&gt;", ">");
		}
		// Replace “&amp;” with a “&”
		if (text.contains("&amp;")) {
			text = text.replaceAll("&amp;", "&");
		}
		// Replace “<br>” with a enter
		if (text.contains("<br>")) {
			text = text.replaceAll("<br>", "\n");
		}

		return text;
	}

	private String toHtml(String text) {
		// Replace “&” with a “&amp;”
		if (text.contains("&")) {
			text = text.replaceAll("&", "&amp;");
		}
		// Replace " " with a “&nbsp;”
		if (text.contains(" ")) {
			text = text.replaceAll(" ", "&nbsp;");
		}
		// Replace tab with “&emsp;&emsp;&emsp;&emsp;”
		if (text.contains("\t")) {
			text = text.replaceAll("\t", "&emsp;&emsp;&emsp;&emsp;");
		}
		// Replace "<" with a "&lt;"
		if (text.contains("<")) {
			text = text.replaceAll("<", "&lt;");
		}
		// Replace “>” with a “&gt;”
		if (text.contains(">")) {
			text = text.replaceAll(">", "&gt;");
		}
		// Replace ascii html with real html
		if (text.contains("&lt;br&gt;")) {
			text = text.replaceAll("&lt;br&gt;", "<br>");
		}

		// Adds the ending /html thingee so it work properlee

		return text;
	}

	private String delete(String previousText) {
		// Deleting spaces
		if (previousText.contains("&nbsp;") && (previousText.substring(previousText.length() - 6).equals("&nbsp;"))) {
			previousText = previousText.substring(0, previousText.length() - 6);
		}
		// Don't delete the HTML tag
		else if (previousText.equals("<html>")) {
			previousText = previousText;
		}
		// Deleting tabs
		else if (previousText.substring(previousText.length() - 6).equals("&emsp;")) {
			previousText = previousText.substring(0, previousText.length() - 24);
		}

		// Deleting enters
		else if (previousText.substring(previousText.length() - 4).equals("<br>")) {
			previousText = previousText.substring(0, previousText.length() - 4);
		}
		// Deleting less than sign
		else if (previousText.substring(previousText.length() - 4).equals("&lt;")) {
			previousText = previousText.substring(0, previousText.length() - 4);
		}
		// Deleting greater than sign
		else if (previousText.substring(previousText.length() - 4).equals("&gt;")) {
			previousText = previousText.substring(0, previousText.length() - 4);
		}

		// Deleting &
		else if (previousText.substring(previousText.length() - 5).equals("&amp;")) {
			previousText = previousText.substring(0, previousText.length() - 5);
		}

		// Deleting all other characters
		else {
			previousText = previousText.substring(0, previousText.length() - 1);
		}

		return previousText;
	}

	private boolean isSaved(String text) {
		if (toPlainText(text).equals(prevSave))
			return true;
		else
			return false;
	}
	
	private void setSaveStatus()
	{
		if(!isSaved(label.getText()) && !(frame.getTitle().contains(" *")))
		{
			frame.setTitle(frame.getTitle() + " *");
		}
		if(isSaved(label.getText()) && frame.getTitle().equals(frame.getTitle() + " *"))
		{
			frame.setTitle(frame.getTitle().substring(0,frame.getTitle().length()-2));
		}
	}

}
