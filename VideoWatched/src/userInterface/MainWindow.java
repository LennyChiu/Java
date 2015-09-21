package userInterface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainWindow {

	private static WorkPanel myWorkPanel = null;
	private static JFrame  frame = new JFrame("Video List");
	private static JMenuBar menuBar = null;
	private static JMenu menu = null;
	private static JMenuItem reset = null;
	private static JMenuItem chooseDir = null;
	private static JMenuItem save = null;
	private static Properties prop = new Properties();
	private static OutputStream output = null;
	private static InputStream input = null;
    private static DefaultListModel listModel;
    private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);	
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty("location"));
			 BufferedReader reader;
				try {					
					File file = new File(prop.getProperty("location"));
					if(!file.exists()){
            			file.createNewFile();
            		}
					reader = new BufferedReader(new FileReader(prop.getProperty("location")));
					
					listModel = new DefaultListModel();
					String currentLine;
					
					while ((currentLine = reader.readLine()) != null) {
						if (currentLine != null) {
							listModel.addElement(currentLine);
						}
					}
					reader.close();
				
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				
		} 
		catch (FileNotFoundException ex) {
			try {
			
				output = new FileOutputStream("config.properties");
				prop.setProperty("location", System.getProperty( "user.home" ) + File.separator);
				prop.setProperty("options", "disabled");
				prop.setProperty("imgOptions", "disabled");
			
				// save properties to project root folder
				prop.store(output, null);
				listModel = new DefaultListModel();
			} 
			catch (IOException io) {
				io.printStackTrace();
			} 
			finally {
				if (output != null) {
					try {
						output.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}	
		catch (IOException ex) {
			ex.printStackTrace();
		}
			finally {
		
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		myWorkPanel = new WorkPanel(frame, listModel);

		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		reset = new JMenuItem("Reset");
		chooseDir = new JMenuItem("Choose Image Directory ...");
		save = new JMenuItem("Create a New File");
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all video from list. (This option cannot be undone.)","", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION)
				{
				    JOptionPane.showMessageDialog(null, "Deleted all videos");
					DefaultListModel removeList = myWorkPanel.getListModel();
					removeList.removeAllElements();
					try {
						input = new FileInputStream("config.properties");
						
						// load a properties file
						prop.load(input);
						
						output = new FileOutputStream("config.properties");
						prop.setProperty("options", "disabled");
						prop.setProperty("imgOptions", "disabled");
						prop.store(output, null);
						File file = new File(prop.getProperty("location"));
						if(file.exists()){
							
	            			file.delete();
	            			file.createNewFile();
	            		}
					}
					catch (IOException io) {
						io.printStackTrace();
					} 
					finally {
						if (input != null) {
							try {
								input.close();
							}
							catch (IOException error) {
								error.printStackTrace();
							}
						}
					}
				}
					
		
			}
		});
		chooseDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("user.home"));
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			    // disable the "All files" option.
			    chooser.setAcceptAllFileFilterUsed(false);

			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					try {
						output = new FileOutputStream("config.properties");

						// set the properties value
					      prop.setProperty("imgDirectory", chooser.getSelectedFile().toString());
					      prop.setProperty("imgOptions", "enabled");
					      if (prop.getProperty("options").equals("enabled") && !myWorkPanel.getListModel().isEmpty()) {
					    	  myWorkPanel.getAddButton().setEnabled(true);
						      myWorkPanel.getDelButton().setEnabled(true);
					      }
					      else if (prop.getProperty("options").equals("enabled") && myWorkPanel.getListModel().isEmpty()){
					    	  
					    	  myWorkPanel.getAddButton().setEnabled(true);
					    	  myWorkPanel.getDelButton().setEnabled(false);
					      }
						// save properties to project root folder
						prop.store(output, null);

					} catch (IOException io) {
						io.printStackTrace();
					} finally {
						if (output != null) {
							try {
								output.close();
							} catch (IOException er) {
								er.printStackTrace();
							}
						}

					}
			 
			      }
			    else {
			      System.out.println("No Folder Selection ");
			      }
			}
		});
		
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 JFileChooser chooser = new JFileChooser();
				    int retrival = chooser.showSaveDialog(null);
				    if (retrival == JFileChooser.APPROVE_OPTION) {
				    	
				        try {
				            FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt");
				            output = new FileOutputStream("config.properties");

							// set the properties value
						      prop.setProperty("location", chooser.getSelectedFile()+".txt");
						      prop.setProperty("options", "enabled");
						      if (prop.getProperty("imgOptions").equals("enabled") && !myWorkPanel.getListModel().isEmpty()) {
						    	  myWorkPanel.getAddButton().setEnabled(true);
							      myWorkPanel.getDelButton().setEnabled(true);
						      }
						      else if (prop.getProperty("imgOptions").equals("enabled") && myWorkPanel.getListModel().isEmpty()){
						    	  
						    	  myWorkPanel.getAddButton().setEnabled(true);
						    	  myWorkPanel.getDelButton().setEnabled(false);
						      }
//						      myWorkPanel.getAddButton().setEnabled(true);
//						      myWorkPanel.getDelButton().setEnabled(true);

							// save properties to project root folder
							prop.store(output, null);
					        fw.flush();
					        fw.close();
				        } catch (Exception ex) {
				            ex.printStackTrace();				            
				        }
				        finally {
							if (output != null) {
								try {
									output.close();
								} catch (IOException er) {
									er.printStackTrace();
								}
							}
				        }
				    }
			}
		});
		menu.add(reset);
		menu.add(chooseDir);
		menu.add(save);
		menuBar.add(menu);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(myWorkPanel, BorderLayout.LINE_START);
		frame.setJMenuBar(menuBar);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
