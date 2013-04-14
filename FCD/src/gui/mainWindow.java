package gui;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.AbstractListModel;
import cdengine.FileCompressor;


public class mainWindow {

	private JFrame frmFileCompressionV;
	private JTextField textField;
	private JTextField txtPartNum;
	private JTextField txtPartSize;
	private JRadioButton rdbtnOfParts;
	private JRadioButton rdbtnSizekb;
	private JCheckBox chckbxBreakIntoParts;
	private JFileChooser fc = new JFileChooser();
	private JList fileList;
	private JLabel lbl_dest;
	private static JProgressBar progressBar;
	
	private FileCompressor fileCompressor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow window = new mainWindow();
					window.frmFileCompressionV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public mainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFileCompressionV = new JFrame();
		frmFileCompressionV.setTitle("File Compression V 0.00001");
		frmFileCompressionV.setResizable(false);
		frmFileCompressionV.setBounds(100, 100, 705, 340);
		frmFileCompressionV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFileCompressionV.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 279, 699, 33);
		frmFileCompressionV.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(10, 11, 46, 14);
		panel.add(lblStatus);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(237, 11, 452, 14);
		panel.add(progressBar);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setToolTipText("File Selection");
		panel_1.setBounds(10, 11, 329, 259);
		frmFileCompressionV.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		
		DefaultListModel alm = new DefaultListModel();
		fileList = new JList(alm);	
		
		
		fileList.setBounds(10, 23, 249, 225);
		panel_1.add(fileList);
		
		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				 fc.setAcceptAllFileFilterUsed(true);
				 int result = fc.showOpenDialog(frmFileCompressionV);
				 if (result == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            DefaultListModel alm = (DefaultListModel) fileList.getModel();
		            alm.addElement(file);
		            
		           
		            //fileList.
				 }
			}
		});
		btnAdd.setIcon(new ImageIcon(mainWindow.class.getResource("/gui/add-icon.png")));
		btnAdd.setBounds(269, 23, 50, 50);
		panel_1.add(btnAdd);
		
		JButton btnRemove = new JButton("");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel alm = (DefaultListModel) fileList.getModel();
	            int[] ind = fileList.getSelectedIndices();
	            for (int i = 0; i < ind.length; i++)
	            {
	            	int[] index = fileList.getSelectedIndices();
	            	alm.remove(index[0]);
	            }
			}
		});
		btnRemove.setIcon(new ImageIcon(mainWindow.class.getResource("/gui/Actions-remove-icon.png")));
		btnRemove.setBounds(269, 84, 50, 50);
		panel_1.add(btnRemove);
		
		JRadioButton rdbtnGvgg = new JRadioButton("gvgg");
		rdbtnGvgg.setBounds(77, 150, 109, 23);
		panel_1.add(rdbtnGvgg);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(349, 11, 340, 113);
		frmFileCompressionV.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblOutputFileName = new JLabel("Output File Name");
		lblOutputFileName.setBounds(10, 15, 91, 14);
		panel_2.add(lblOutputFileName);
		
		textField = new JTextField();
		textField.setBounds(120, 12, 210, 20);
		panel_2.add(textField);
		textField.setColumns(10);
		
		JLabel lblDestination = new JLabel("Destination");
		lblDestination.setBounds(10, 40, 78, 14);
		panel_2.add(lblDestination);
		
		lbl_dest = new JLabel("...");
		lbl_dest.setBounds(100, 40, 230, 14);
		panel_2.add(lbl_dest);
		
		JButton btnSselect = new JButton("Select");
		btnSselect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				 fc.setAcceptAllFileFilterUsed(false);
				 int result = fc.showOpenDialog(frmFileCompressionV);
				 if (result == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            
		            lbl_dest.setText(file.getAbsolutePath());
		            
		           
		            //fileList.
				 }
			}
		});
		btnSselect.setBounds(10, 66, 320, 36);
		panel_2.add(btnSselect);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Options & Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(349, 131, 340, 137);
		frmFileCompressionV.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Archive Only (No Compression)");
		chckbxNewCheckBox.setBounds(16, 20, 273, 23);
		panel_3.add(chckbxNewCheckBox);
		
		
		
		JButton btnExtract = new JButton("Extract");
		btnExtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO:  File Encryption
			}
		});
		btnExtract.setBounds(16, 99, 150, 32);
		panel_3.add(btnExtract);
		
		JButton btnCompress = new JButton("Compress");
		btnCompress.setBounds(176, 99, 154, 32);
		btnCompress.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//TODO: Modify for archiving
				
				//Grab destination folder from lbl_dest
				String outFileString = lbl_dest.getText() + "\\";
				
				//Append filename to folder
				outFileString += textField.getText() + ".fcd";
				
				//Grab the file list as a DefaultListModel
				DefaultListModel alm = (DefaultListModel) fileList.getModel();
				
				byte[] output = null;
				//read byte array from file by file name
				try{
					output = Files.readAllBytes(Paths.get(alm.get(0).toString()));
				} catch(IOException e){
					JOptionPane.showMessageDialog(null,
							"Failed to read input file.",  
							"Results", JOptionPane.ERROR_MESSAGE);
				}
				fileCompressor = new FileCompressor(output);
				
				//do file compression; set output to return
				output = fileCompressor.compressFile();
						
				//Create output file path
				Path outFile = Paths.get(outFileString);
				try {
					//Write the output file to disk
					Files.write(outFile, output);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null,
							"Failed to write output file.",  
							"Results", JOptionPane.ERROR_MESSAGE);
				}	
			}
		});
		panel_3.add(btnCompress);
		
		rdbtnOfParts = new JRadioButton("# of parts");
		rdbtnOfParts.setEnabled(false);
		rdbtnOfParts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnOfParts.isSelected())
				{
					txtPartNum.setEnabled(true);
					txtPartSize.setEnabled(false);
				}
				else
				{
					txtPartNum.setEnabled(false);
					txtPartSize.setEnabled(true);
				}
			}
		});
		rdbtnOfParts.setBounds(140, 46, 86, 23);
		panel_3.add(rdbtnOfParts);
		
		rdbtnSizekb = new JRadioButton("Size (KB)");
		rdbtnSizekb.setEnabled(false);
		rdbtnSizekb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnSizekb.isSelected())
				{
					txtPartNum.setEnabled(false);
					txtPartSize.setEnabled(true);
				}
				else
				{
					txtPartNum.setEnabled(true);
					txtPartSize.setEnabled(false);
				}
			}
		});
		rdbtnSizekb.setBounds(140, 69, 86, 23);
		panel_3.add(rdbtnSizekb);
		
		ButtonGroup group = new ButtonGroup();
		    group.add(rdbtnOfParts);
		    group.add(rdbtnSizekb);
		   
		
		chckbxBreakIntoParts = new JCheckBox("Break Into Parts");
		chckbxBreakIntoParts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxBreakIntoParts.isSelected())
				{
					rdbtnOfParts.setEnabled(true);
					rdbtnSizekb.setEnabled(true);
				}
				else
				{
					rdbtnOfParts.setEnabled(false);
					rdbtnSizekb.setEnabled(false);
				}
			}
		});
		chckbxBreakIntoParts.setBounds(16, 46, 124, 23);
		panel_3.add(chckbxBreakIntoParts);		
		
		txtPartNum = new JTextField();
		txtPartNum.setEnabled(false);
		txtPartNum.setBounds(229, 47, 101, 20);
		panel_3.add(txtPartNum);
		txtPartNum.setColumns(10);
		
		txtPartSize = new JTextField();
		txtPartSize.setEnabled(false);
		txtPartSize.setColumns(10);
		txtPartSize.setBounds(229, 70, 101, 20);
		panel_3.add(txtPartSize);
				
	}
	
	public static void setProgress(int n)
	{
		progressBar.setValue(n);
	}
}
