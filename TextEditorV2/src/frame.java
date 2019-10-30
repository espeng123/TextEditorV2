
//Import a whole bunch of random stuff
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Stack;

public class frame {
	// Variables
	// Filename
	String filename = "/Untitled.txt";
	// JFrame for the GUI
	JFrame frame = new JFrame("");
	// Main label for the text box
	JLabel label = new JLabel("<html></html>", JLabel.LEFT);
	// Boolean for if shift is pressed
	static boolean shift = false;
	// Boolean for if command is pressed
	static boolean command = false;
	// String of the previous save
	String prevSave = "";
	// Number of windows
	static int windows = 0;
	// ArrayList of strings to be redone
	Stack<String> undone = new Stack<String>();
	int cursorIndex = 0;
	String copied = "";
	String text;
	String cursorText;

	// Constructor
	public frame(int xSize, int ySize, String newText, String filename) {
		Menu();
		frame.setTitle(
				"Text Editor - " + filename.substring((filename.lastIndexOf("/") + 1), filename.indexOf(".txt")));
		windows++;
		text = label.getText();
		if (newText != null) {
			text += newText;
		}
		update();
		prevSave = toPlainText(label.getText());
		this.filename = filename;

		listeners();

		formatStuff(xSize, ySize);
	}

	public frame(int xSize, int ySize) {
		Menu();
		frame.setTitle(
				"Text Editor - " + filename.substring((filename.lastIndexOf("/") + 1), filename.indexOf(".txt")));
		windows++;
		update();
		prevSave = toPlainText(label.getText());

		listeners();

		formatStuff(xSize, ySize);
	}

	// Methods
	
	void update() {
		if (text == null) {
			text = "";
			cursorText = "|";
		}
		else
			cursorText = text.substring(0, cursorIndex) + "|"+ text.substring(cursorIndex);
		label.setText("<html>"+cursorText+"</html>");
	}
	
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

	public void Menu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newFrame();
			}
		});
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				open();
			}
		});
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		JMenuItem closeMenuItem = new JMenuItem("Close");
		closeMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				windows--;
				frame.dispose();
			}
		});

		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(closeMenuItem);
		fileMenu.add(saveMenuItem);

		JMenu editMenu = new JMenu("Edit");
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				undo();
				update();
			}
		});
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				redo();
				update();
			}
		});

		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		frame.setJMenuBar(menuBar);
	}
	

	private void typeText(KeyEvent e) {

		int keyCode = e.getKeyCode();
		char keyTyped = e.getKeyChar();
		String textToAdd = "";
		
		
		// Implements backspace
		if (keyTyped == 8) {
			delete();
			// If the space key has been pressed
		} else if (keyTyped == 32) {
			textToAdd = "&nbsp;";
			// If TAB was entered
		} else if (keyCode == 9) {
			textToAdd = "&emsp;&emsp;&emsp;&emsp;";
			// IF they tried to type “<”
		} else if (keyCode == 44 && shift) {
			textToAdd = "&lt;";
			// IF they tried to type “>”
		} else if (keyCode == 46 && shift) {
			textToAdd = "&gt;";
			// IF they tried to type “&”
		} else if (keyCode == 55 && shift) {
			textToAdd = "&amp;";
			// IF they pressed enter or return
		} else if (keyTyped == 10) {
			textToAdd = "<br>";
			// Implements saving through command + s
		} else if (keyCode == 83 && command) {
			save();
			// Implements opening through command + o
		} else if (keyCode == 79 && command) {
			open();
		} else if (keyCode == 78 && command) {
			newFrame();
		}

		else if (keyCode == 90 && command && shift) {
			redo();
		} else if (keyCode == 90 && command) {
			undo();
		} else if (keyCode == 67 && command) {
			copy(label.getText());
			System.out.println(copied);
		} else if (keyCode == 86 && command) {
			text = paste(label.getText());
		}
		else if ((keyCode == 39 && command) || (keyCode == 37 && command)) {
			if( !text.contains("<u><font color=\"#00ffff\">"))
			{
				textToAdd = "<u><font color=\"#00ffff\">";
			}
		}

		else if (keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40) {
			moveCursor(text, keyCode);
		}

		// No ? box when hitting shift, caps lock, command, fn, control, alt, all the
		// arrow keys, and right option keys
		else if (keyCode != 16 && keyCode != 20 && keyCode != 157 && keyCode != 0 && keyCode != 17 && keyCode != 18
				&& keyCode != 65406 && keyCode != 27) {
			undone.clear();
			if (keyCode == 44 || keyCode == 46 || keyCode == 55) {
				if (!shift && !command) {
					textToAdd += keyTyped;

				}
			} else {
				textToAdd += keyTyped;
			}
		}
		
		if(!textToAdd.equals(""))
		{
			text = text.substring(0,cursorIndex) + textToAdd + text.substring(cursorIndex,text.length());
		}
		
		if(text.contains("<u><font color=\"#00ffff\">"))
		{
			if(!text.contains("</font></u>"))
			{
				int indexOfEndMark = text.indexOf("<u><font color=\"#00ffff\">") + 26;
				text = text.substring(0,indexOfEndMark) + "</font></u>" + text.substring(indexOfEndMark,text.length());
				cursorIndex += 1;
			}
		}

		moveCursor();
		if(text.contains("<u><font color=\"#00ffff\">"))
		{
			text = moveHighlight(text);
		}
		update();
		setSaveStatus();
	}

	// Save To File Function
	private void save() {

		boolean moveOn = false;
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
				moveOn = true;
			} else {
				command = false;
			}
		} else {
			moveOn = true;
		}
		String toSave = toPlainText(label.getText());

		if (moveOn) {
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
			frame.setTitle(
					"Text Editor - " + filename.substring((filename.lastIndexOf("/") + 1), filename.indexOf(".txt")));
		}
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

	private void newFrame() {
		new frame(800, 800);
	}

	private String toPlainText(String text) {
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

	private void delete() {
		// Deleting spaces
		if (text.contains("&nbsp;") && (text.substring(text.length() - 6).equals("&nbsp;"))) {
			text = text.substring(0, text.length() - 6);
		}
		// Deleting tabs
		else if (text.substring(text.length() - 6).equals("&emsp;")) {
			text = text.substring(0, text.length() - 24);
		}

		// Deleting enters
		else if (text.substring(text.length() - 4).equals("<br>")) {
			text = text.substring(0, text.length() - 4);
		}
		// Deleting less than sign
		else if (text.substring(text.length() - 4).equals("&lt;")) {
			text = text.substring(0, text.length() - 4);
		}
		// Deleting greater than sign
		else if (text.substring(text.length() - 4).equals("&gt;")) {
			text = text.substring(0, text.length() - 4);
		}

		// Deleting &
		else if (text.substring(text.length() - 5).equals("&amp;")) {
			text = text.substring(0, text.length() - 5);
		}

		// Deleting all other characters
		else {
			text = text.substring(0, text.length() - 1);
		}

	}

	private boolean isSaved(String text) {
		if (toPlainText(text).equals(prevSave))
			return true;
		else
			return false;
	}

	private void setSaveStatus() {
		if (!isSaved(label.getText()) && !(frame.getTitle().contains(" *"))) {
			frame.setTitle(frame.getTitle() + " *");
		}
		if (isSaved(label.getText()) && frame.getTitle().equals(frame.getTitle() + " *")) {
			frame.setTitle(frame.getTitle().substring(0, frame.getTitle().length() - 2));
		}
	}

	private void undo() {


			if (text.contains("&nbsp;")) {
				undone.push(text.substring(text.lastIndexOf("&nbsp;"), text.length()));
				text = text.substring(0, text.lastIndexOf("&nbsp;"));
			} else if (text.contains("&emsp;")) {
				undone.push(text.substring(text.lastIndexOf("&emsp;&emsp;&emsp;&emsp;"), text.length()));
				text = text.substring(0, text.lastIndexOf("&emsp;&emsp;&emsp;&emsp;"));
			} else if (text.contains("<br>")) {
				undone.push(text.substring(text.lastIndexOf("<br>"), text.length()));
				text = text.substring(0, text.lastIndexOf("<br>"));
			} else {
				undone.push(text);
			
		}
	}

	private void redo() {
		if (undone.size() >= 1) {
			text = text.substring(0, text.length() - 8);
			text += undone.pop();
		}
	}

	public void moveCursor() {
		cursorIndex += 1;
	}

	public void moveCursor(String text, int keyCode) {
		int num = 1;
		//Moving Right
		if (keyCode == 39 && cursorIndex != text.length()) {
			//if the next character after the cursor is ASCII
			if( text.substring(cursorIndex,text.length()).indexOf("&") == 0 )
			{
				//if the ASCII after the cursor is a tab set the num to move cursor to 24
				if( text.substring(cursorIndex,text.length()).substring(0,text.substring(cursorIndex,text.length()).indexOf(";")+1).equals("&emsp;"))
				{
					num = 24;
				}
				//otherwise set the num equal to the length of the ASCII string
				else
				{
					num = text.substring(cursorIndex,text.length()).indexOf(";")+1;
				}
			}
			else if( text.substring(cursorIndex,text.length()).indexOf("<br>") == 0)
			{
				num = 4;
			}
			cursorIndex += num;
			//Moving Left
		} else if (keyCode == 37 && cursorIndex != 6) {
			//if the character before the cursor is a ;
			if( text.substring(0,cursorIndex).lastIndexOf(";") == text.substring(0,cursorIndex).length()-1 )
			{
				//if it is a ; then make sure it is actually ASCII
				if(text.substring(0,cursorIndex).contains("&"))
				{
					if(!text.substring(text.substring(0,cursorIndex).lastIndexOf("&"),text.substring(0,cursorIndex).length()-1).contains(";"))
					{
						//if the ASCII after the cursor is a tab set the num to move cursor to 24
						if(text.substring(text.substring(0,cursorIndex).lastIndexOf("&"),text.substring(0,cursorIndex).length()).equals("&emsp;"))
						{
							num = 24;
						}
						//otherwise set the num equal to the length of the ASCII string
						else
						{
							num = text.substring(text.substring(0,cursorIndex).lastIndexOf("&"),text.substring(0,cursorIndex).length()-1).length()+1;
						}
					}
				}
			}
			else if( text.substring(cursorIndex-4,cursorIndex).equals("<br>"))
			{
				num = 4;
			}
			cursorIndex -= num;
		}
	}


	public void copy(String text) {
		copied = text.substring(cursorIndex+1, text.length() - 7);
	}

	public String paste(String text) {
		return text.substring(0, cursorIndex) + copied + text.substring(cursorIndex, text.length());
	}
	
	public String moveHighlight(String text)
	{
		text.replaceAll("</font></u>","");
		text = text.substring(0,cursorIndex) + "</font></u>" + text.substring(cursorIndex,text.length());
		return text;
	}

}
