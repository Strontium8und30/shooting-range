package utilities.swing;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import net.miginfocom.swing.*;

public class FileSearchPanel extends JPanel {
		
	JTextField fileField = null;
	
	JButton search = null;
	
	FileFilter fileFilter = null;
	
	File file = null;

	
	public FileSearchPanel() {
		this(null, "");
	}
	
	public FileSearchPanel(FileFilter fileFilter, String currentDir) {
		this.fileFilter = fileFilter;
		setLayout(new MigLayout());
		setBorder(new EtchedBorder());
		add(fileField = new JTextField(currentDir), "grow");
		add(search = new JButton("Durchsuchen"));
		init();
	}
	
	
	private void init() {
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilteredFileChooser fileChooser = new FilteredFileChooser(FilteredFileChooser.OPEN);
				fileChooser.setCurrentDirectory(checkFile());
				fileChooser.setSelectedFile(checkFile());
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if (fileFilter != null) {
					fileChooser.setFileFilter(fileFilter);
				}
				fileChooser.showOpenDialog(FileSearchPanel.this);
				file = fileChooser.getSelectedFile();
				fileField.setText(file.toString());
			}
		});
		
		fileField.setPreferredSize(new Dimension(250,20));
		fileField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkFile();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkFile();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		checkFile();
	}
	
	private File checkFile() {
		file = new File(fileField.getText());
		if (file.exists() && !file.canWrite() || (!file.isDirectory() && !fileFilter.accept(file))) {
			fileField.setBackground(new Color(1.0f, 0.7f, 0.7f));
			return null;
		} else if (file.isFile() && file.exists() && file.canWrite()) {
			fileField.setBackground(new Color(1.0f, 1.0f, 0.7f));
		} else {
			fileField.setBackground(new Color(0.7f, 1.0f, 0.7f));
		}
		return file;
	}
	
	public File getFile() {
		return checkFile();
	}
}
