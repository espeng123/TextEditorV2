
//Import a whole bunch of random stuff
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Stack;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

public class frame {
	String filename = "/Untitled.txt";
	JFrame frame = new JFrame("");
	JLabel label = new JLabel("<html></html>", JLabel.LEFT);
	// if shift is pressed
	static boolean shift = false;
	// if command is pressed
	static boolean command = false;
	// String of the previous save
	String prevSave = "";
	// Number of windows
	static int windows = 0;
	boolean selection;
	boolean inFrame;
	int numOfLines = 1;
	int mouseXStart;
	int mouseYStart;
	boolean mousePressed = false;
	boolean mouseSelect = false;

	static String[] specialChars = { "&nbsp;", "&emsp;&emsp;&emsp;&emsp;", "&lt;", "&gt;", "&amp;", "<br>" };
	static String[] specialCharsV2 = { "&nbsp;", "&emsp;", "&lt;", "&gt;", "&amp;", "<u><font color=\"#00ffff\">" };
	static String[] undoSpecialChars = { "&nbsp;", "&emsp;&emsp;&emsp;&emsp;", "<br>" };

	// ArrayList of strings to be redone
	Stack<String> undone = new Stack<String>();
	int cursorIndex = 0;

	String text;

	// Constructor
	public frame(int xSize, int ySize, String newText, String filename) {
		Menu();
		frame.setTitle(
				"Text Editor - " + filename.substring((filename.lastIndexOf("/") + 1), filename.indexOf(".txt")));
		windows++;
		text = "";
		if (newText != null) {
			text += newText;
		}
		cursorIndex = text.length();
		update();
		prevSave = toPlainText(text);
		this.filename = filename;

		listeners();

		formatStuff(xSize, ySize);

		numOfLines = countNumOfLines();
	}

	public frame(int xSize, int ySize) {
		Menu();
		frame.setTitle(
				"Text Editor - " + filename.substring((filename.lastIndexOf("/") + 1), filename.indexOf(".txt")));
		windows++;
		text = "";
		update();
		prevSave = toPlainText(text);

		listeners();

		formatStuff(xSize, ySize);
	}

	// Methods

	void update() {
		String displayText = text.substring(0, cursorIndex);
		displayText += "|";
		displayText += text.substring(cursorIndex);

		label.setText("<html>" + displayText + "</html>");
	}

	private void formatStuff(int xSize, int ySize) {
		// Setup the frame with size, background color, and close button
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(xSize, ySize);
		frame.getContentPane().setBackground(Color.black);
		frame.setLayout(new BorderLayout());

		// Intelligent way to do borders
		JLabel label1 = new JLabel("<html> <br> <br></html>");
		JLabel label2 = new JLabel(" ");
		JLabel label3 = new JLabel("             ");
		JLabel label4 = new JLabel("             ");

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

	private void listeners() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!isSaved(text)) {
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
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!text.contains("<u><font color=\"#00ffff\">")) {
						setCursorOnClick(getLineNumber(e), getCharNumber(e));
					} else {
						text = text.replaceAll("<u><font color=\"#00ffff\">", "");
						text = text.replaceAll("</u></font>", "");
						setCursorOnClick(getLineNumber(e), getCharNumber(e));
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// mousePressed = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				inFrame = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				inFrame = false;
			}
		});

		frame.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				mouseSelect = true;
				mouseSelection(e);
			}
		});

	}

	private void Menu() {
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
		JMenuItem copyMenuItem = new JMenuItem("Copy");
		copyMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				copy();
				update();
			}
		});
		JMenuItem pasteMenuItem = new JMenuItem("Paste");
		pasteMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paste();
				update();
			}
		});
		JMenuItem cutMenuItem = new JMenuItem("Cut");
		cutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cut();
				update();
			}
		});

		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);
		editMenu.add(cutMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		frame.setJMenuBar(menuBar);
	}

	private void typeText(KeyEvent e) {

		int keyCode = e.getKeyCode();
		char keyTyped = e.getKeyChar();
		String textToAdd = "";
		if (text.contains("<u><font color=\"#00ffff\">") && !mouseSelect) {
			selection = true;
		} else {
			selection = false;
		}

		// Implements backspace
		if (keyTyped == 8) {
			delete();
			// If the space key has been pressed
		} else if (keyTyped == 32) {
			textToAdd = "&nbsp;";
			// If TAB was entered
		} else if (keyCode == 9) {
			textToAdd = "&emsp;&emsp;&emsp;&emsp;";
			// IF they tried to type â€œ<â€�
		} else if (keyCode == 44 && shift) {
			textToAdd = "&lt;";
			// IF they tried to type â€œ>â€�
		} else if (keyCode == 46 && shift) {
			textToAdd = "&gt;";
			// IF they tried to type â€œ&â€�
		} else if (keyCode == 55 && shift) {
			textToAdd = "&amp;";
			// IF they pressed enter or return
		} else if (keyTyped == 10) {
			textToAdd = "<br>";
			numOfLines++;
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
			copy();
		} else if (keyCode == 86 && command) {
			paste();
		} else if (keyCode == 88 && command) {
			cut();
		} else if (keyCode == 192 && command) {
			if (!selection) {
				textToAdd = "<u><font color=\"#00ffff\">";
				selection = true;
			} else {
				text = text.replaceAll("<u><font color=\"#00ffff\">", "");
				cursorIndex = text.indexOf("</font></u>");
				text = text.replaceAll("</font></u>", "");
				selection = false;
			}
		}

		else if (keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40) {
			if (selection && !mouseSelect) {
				moveHighlight(keyCode);
			} else if (mouseSelect) {
				// DO NOTHING
			} else {
				moveCursor(keyCode);
			}
		}

		// No ? box when hitting shift, caps lock, command, fn, control, alt, all the
		// arrow keys, and right option keys
		else if (keyCode != 16 && keyCode != 20 && keyCode != 157 && keyCode != 0 && keyCode != 17 && keyCode != 18
				&& keyCode != 65406 && keyCode != 27) {
			undone.clear();
			if (keyCode == 44 || keyCode == 46 || keyCode == 55 || keyCode == 192) {
				if (!shift && !command) {
					textToAdd += keyTyped;

				}
			} else {
				textToAdd += keyTyped;
			}
		}

		if (text.contains("<u><font color=\"#00ffff\">") && !textToAdd.equals("")) {
			replace(textToAdd);
			mouseSelect = false;
		} else {
			add(textToAdd);
			cursorIndex += textToAdd.length();
		}


		if (selection) {
			if (!text.contains("</font></u>")) {
				int indexOfEndMark = text.indexOf("<u><font color=\"#00ffff\">") + 26;
				text = text.substring(0, indexOfEndMark) + "</font></u>"
						+ text.substring(indexOfEndMark, text.length());
				cursorIndex += 12;
			}
		}

		update();
		setSaveStatus();
	}

	void add(String s) {
		text = text.substring(0, cursorIndex) + s + text.substring(cursorIndex);
	}

	void replace(String s) {
		String toReplace = text.substring(text.indexOf("<u><font color=\"#00ffff\">") + 25,text.indexOf("</font></u>"));
		text = text.replace(toReplace, s);
		text = text.replace("<u><font color=\"#00ffff\">", "");
		cursorIndex = text.indexOf("</font></u>");
		text = text.replace("</font></u>", "");
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
		String toSave = toPlainText(text);

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
					numOfLines++;
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

		// Replace â€œ&nbsp;â€� with a â€œ â€œ
		if (text.contains("&nbsp;")) {
			text = text.replaceAll("&nbsp;", " ");
		}
		// Replace â€œ&emsp;&emsp;&emsp;&emsp;â€� with a tab
		if (text.contains("&emsp;&emsp;&emsp;&emsp;")) {
			text = text.replaceAll("&emsp;&emsp;&emsp;&emsp;", "\t");
		}
		// Replace â€œ&lt;â€� with a â€œ<â€�
		if (text.contains("&lt;")) {
			text = text.replaceAll("&lt;", "<");
		}
		// Replace â€œ&gt;â€� with a â€œ>â€�
		if (text.contains("&gt;")) {
			text = text.replaceAll("&gt;", ">");
		}
		// Replace â€œ&amp;â€� with a â€œ&â€�
		if (text.contains("&amp;")) {
			text = text.replaceAll("&amp;", "&");
		}
		// Replace â€œ<br>â€� with a enter
		if (text.contains("<br>")) {
			text = text.replaceAll("<br>", "\n");
		}

		if (text.contains("<u><font color=\"#00ffff\">")) {
			text = text.replace("<u><font color=\"#00ffff\">", "");
			text = text.replace("</font></u>", "");
		}

		return text;
	}

	private String toHtml(String text) {
		// Replace â€œ&â€� with a â€œ&amp;â€�
		if (text.contains("&")) {
			text = text.replaceAll("&", "&amp;");
		}
		// Replace " " with a â€œ&nbsp;â€�
		if (text.contains(" ")) {
			text = text.replaceAll(" ", "&nbsp;");
		}
		// Replace tab with â€œ&emsp;&emsp;&emsp;&emsp;â€�
		if (text.contains("\t")) {
			text = text.replaceAll("\t", "&emsp;&emsp;&emsp;&emsp;");
		}
		// Replace "<" with a "&lt;"
		if (text.contains("<")) {
			text = text.replaceAll("<", "&lt;");
		}
		// Replace â€œ>â€� with a â€œ&gt;â€�
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
		if (!selection) {
			if (text != "") {
				String toRight = text.substring(cursorIndex);
				cursorIndex -= leftCharLen();
				text = text.substring(0, cursorIndex) + toRight;
			}
		} else {
			int start = text.indexOf("<u><font color=\"#00ffff\">");
			int end = text.indexOf("</font></u>") + 11;
			text = text.substring(0, start) + text.substring(end, text.length());
			cursorIndex = start;
			selection = false;
		}
	}

	private boolean isSaved(String text) {
		if (toPlainText(text).equals(prevSave))
			return true;
		else
			return false;
	}

	private void setSaveStatus() {
		if (isSaved(text)) {
			if (frame.getTitle().contains(" *")) {
				frame.setTitle(frame.getTitle().substring(0, frame.getTitle().length() - 2));
			}
		} else {
			if (!frame.getTitle().contains(" *")) {
				frame.setTitle(frame.getTitle() + " *");
			}
		}
	}

	private void undo() {
		if (!text.equals("")) {
			int index = 0;
			String textBeforeCursor = text.substring(0, cursorIndex);
			for (String s : undoSpecialChars) {
				if (textBeforeCursor.contains(s)) {
					if (textBeforeCursor.lastIndexOf(s) > index) {
						index = textBeforeCursor.lastIndexOf(s);
					}
				}
			}
			int before = text.length();
			undone.push(textBeforeCursor.substring(index, textBeforeCursor.length()));
			textBeforeCursor = textBeforeCursor.substring(0, index);
			text = (textBeforeCursor + text.substring(cursorIndex, text.length()));
			cursorIndex -= (before - text.length());
		}
	}

	private void redo() {
		if (undone.size() > 0) {
			String toAdd = undone.pop();
			text += toAdd;
			cursorIndex += toAdd.length();
		}

	}

	private void moveCursor(int keyCode) {
		// move right
		if (keyCode == 39) {
			cursorIndex += rightCharLen();
			// Moving Left
		} else if (keyCode == 37) {
			cursorIndex -= leftCharLen();
		}
	}

	private int rightCharLen() {
		int num = 1;
		if (cursorIndex == text.length()) {
			num = 0;
		}

		String rightOfCursor = text.substring(cursorIndex);

		for (String s : specialChars) {
			if (rightOfCursor.length() >= s.length()) {
				if (rightOfCursor.substring(0, s.length()).equals(s)) {
					num = s.length();
				}

			}
		}

		return num;

	}

	private int nextCharLen() {
		int num = 1;
		if (cursorIndex == text.length()) {
			num = 0;
		}

		String rightOfCursor = text.substring(cursorIndex);

		for (String s : specialCharsV2) {
			if (rightOfCursor.length() >= s.length()) {
				if (rightOfCursor.substring(0, s.length()).equals(s)) {
					num = s.length();
				}

			}
		}

		return num;

	}

	private int leftCharLen() {
		int num = 1;
		if (cursorIndex == 0) {
			num = 0;
		}

		String leftOfCursor = text.substring(0, cursorIndex);

		for (String s : specialChars) {
			if (leftOfCursor.length() >= s.length()) {
				if (leftOfCursor.substring(cursorIndex - s.length(), cursorIndex).equals(s)) {
					num = s.length();
				}

			}
		}

		return num;
	}

	private void copy() {
		if (text.contains("<u><font color=\"#00ffff\">")) {
			String copied = text.substring(text.indexOf("<u><font color=\"#00ffff\">") + 25,
					text.indexOf("</font></u>"));
			StringSelection stringSelection = new StringSelection(toPlainText(copied));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
	}

	private void paste() {
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		String copied = "";
		try {
			copied = (String) c.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		copied = toHtml(copied);
		text = text.substring(0, cursorIndex) + copied + text.substring(cursorIndex, text.length());
		cursorIndex += copied.length();
	}

	private void cut() {
		if (text.contains("<u><font color=\"#00ffff\">")) {
			String copied = text.substring(text.indexOf("<u><font color=\"#00ffff\">") + 25,
					text.indexOf("</font></u>"));
			StringSelection stringSelection = new StringSelection(toPlainText(copied));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
			text = text.replace(copied, "");
			text = text.replace(copied, "");
			text = text.replace("<u><font color=\"#00ffff\">", "");
			cursorIndex = text.indexOf("</font></u>");
			text = text.replace("</font></u>", "");
		}
	}

	private void moveHighlight(int keyCode) {
		if (keyCode == 39) {
			if (cursorIndex != text.length()) {
				int index = text.indexOf("</font></u>");
				text = text.substring(0, index) + text.substring(index + 11, text.length());
				cursorIndex -= 11;
				cursorIndex += rightCharLen();
				text = text.substring(0, cursorIndex) + "</font></u>" + text.substring(cursorIndex, text.length());
				cursorIndex += 11;
			}
		} else if (keyCode == 37) {
			if (cursorIndex >= text.indexOf("<u><font color=\"#00ffff\">") + 37) {
				int index = text.indexOf("</font></u>");
				text = text.substring(0, index) + text.substring(index + 11, text.length());
				cursorIndex -= 11;
				cursorIndex -= leftCharLen();
				text = text.substring(0, cursorIndex) + "</font></u>" + text.substring(cursorIndex, text.length());
				cursorIndex += 11;
			}
		}
	}

	private int getLineNumber(MouseEvent e) {
		if (inFrame) {
			int y = e.getY();
			int mousLinePos = y - 78;
			int line;
			if (mousLinePos > 0) {
				line = 1;
			} else {
				return -1;
			}
			while (mousLinePos > line * 15) {
				line++;
			}
			return line - 1;
		}
		return -1;
	}

	private int getCharNumber(MouseEvent e) {
		if (inFrame) {
			int x = e.getX();
			int mousLinePos = x - 50;
			int charNum;
			if (mousLinePos > 0) {
				charNum = 1;
			} else {
				return -1;
			}
			while (mousLinePos > charNum * 8) {
				charNum++;
			}
			return charNum;
		}
		return -1;
	}

	private void setCursorOnClick(int line, int charNum) {
		cursorIndex = getLocation(line, charNum);
		update();
	}

	private int getLocation(int line, int charNum) {
		int previousCursorPos = cursorIndex;
		int num = 0;
		if (line >= 0) {
			if (line < numOfLines) {
				String lineText = getLine(line);
				if (charNum <= lineText.length() + 1 && charNum > 0) {
					cursorIndex = getLengthTo(line);
					for (int i = 0; i < charNum - 1; i++) {
						cursorIndex += nextCharLen();
					}
					num = cursorIndex;
					cursorIndex = previousCursorPos;
				} else if (charNum == -1) {
					num = getLengthTo(line);
				} else {
					num = getLengthTo(line);
					num += lineText.length();
				}
			}
		}
		return num;
	}

	private void mouseSelection(MouseEvent e) {
		int location = getLocation(getLineNumber(e), getCharNumber(e));
		if (!text.contains("<u><font color=\"#00ffff\">")) {
			setCursorOnClick(getLineNumber(e), getCharNumber(e));
			text = text.substring(0, cursorIndex) + "<u><font color=\"#00ffff\">"
					+ text.substring(cursorIndex, text.length());
			cursorIndex += 25;
			text = text.substring(0, cursorIndex) + "</font></u>" + text.substring(cursorIndex, text.length());
		} else if (text.contains("<u><font color=\"#00ffff\">") && location > cursorIndex) {
			text = text.replace("</font></u>", "");
			setCursorOnClick(getLineNumber(e), getCharNumber(e));
			text = text.substring(0, cursorIndex) + "</font></u>" + text.substring(cursorIndex, text.length());
		}
		update();
	}

	private int countNumOfLines() {
		String toDestroy = text;
		int count = 0;
		while (toDestroy.contains("<br>")) {
			toDestroy = toDestroy.substring(toDestroy.indexOf("<br>") + 4, toDestroy.length());
			count++;
		}

		return count;
	}

	private String getLine(int num) {
		String line = text;
		while (num > 0) {
			line = line.substring(line.indexOf("<br>") + 4, line.length());
			num--;
		}
		if (line.contains("<br>")) {
			line = line.substring(0, line.indexOf("<br>"));
		}
		return line;
	}

	private int getLengthTo(int num) {
		int length = 0;
		String toDestroy = text;
		for (int i = 0; i < num; i++) {
			length += toDestroy.substring(0, toDestroy.indexOf("<br>") + 4).length();
			toDestroy = toDestroy.substring(toDestroy.indexOf("<br>") + 4, toDestroy.length());
		}
		return length;
	}

}
