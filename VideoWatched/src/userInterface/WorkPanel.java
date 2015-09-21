package userInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WorkPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	private JFrame wFrame= null;
	private JButton add = new JButton("Add Video");
	private JButton del = new JButton("Delete Video");
	private	static ImagePanel myImagePanel = null;
    private JList list;
    private DefaultListModel listModel;
	private JScrollPane scrollPane = null;
	private static File file = null;
	private static String line;
	private static Properties prop = new Properties();
	private static InputStream input = null;

	public WorkPanel(JFrame pFrame, DefaultListModel pListModel){	
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add.addActionListener(this);
		this.add(add);
		del.addActionListener(this);
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			if (prop.getProperty("options").equals("disabled") || prop.getProperty("imgOptions").equals("disabled")) {
				add.setEnabled(false);
				del.setEnabled(false);
			}
			else {
				add.setEnabled(true);
				del.setEnabled(true);
			}
		}
		catch (IOException io) {
			io.printStackTrace();
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
 
		this.add(del);
		wFrame = pFrame;
		listModel = pListModel;

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
		
        JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension (250, 500));
		
        
        this.add(scrollPane);
        
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			
		if (e.getSource() == add) {
				
			JFileChooser chooser = new JFileChooser(prop.getProperty("imgDirectory"));
			
			int returnVal = chooser.showOpenDialog(add);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File aFile = chooser.getSelectedFile();
				
					
					OutputStream output = new FileOutputStream("config.properties");
					prop.setProperty("imgDirectory", chooser.getSelectedFile().getParent());
					prop.store(output,null);
				
				if(!listModel.contains(aFile.getName())) {
					try{
						wFrame.remove(myImagePanel);
					}
					catch(NullPointerException error){}
					finally{
						myImagePanel = new ImagePanel(aFile);
					}
					
					wFrame.add(myImagePanel, BorderLayout.CENTER);
		            
		            int index = list.getSelectedIndex();
		            int size = listModel.getSize();
		            
		            //If no selection or if item in last position is selected,
		            //add the new one to end of list, and select new one.
		            if (index == -1 || (index+1 == size)) {
		                listModel.addElement(aFile.getName());
		                try {
		                	file = new File(prop.getProperty("location"));
		            		//if file doesnt exists, then create it
		            		if(!file.exists()){
		            			file.createNewFile();
		            		}
		            		System.out.println(file.getName());
							FileWriter fstream = new FileWriter(file.getAbsolutePath(), true);
							BufferedWriter out = new BufferedWriter(fstream);
							out.write(aFile.getName().toString());
							out.newLine();
							//close buffer writer
							out.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		                
		                list.setSelectedIndex(size);

		            //Otherwise insert the new one after the current selection,
		            //and select new one.
		            } else {
		                listModel.insertElementAt(aFile.getName(), index+1);
		                try {
		                	file = new File(prop.getProperty("location"));
		            		//if file doesnt exists, then create it
		            		if(!file.exists()){
		            			file.createNewFile();
		            		}
		            		System.out.println(file.getName());
							FileWriter fstream = new FileWriter(file.getAbsolutePath(), true);
							BufferedWriter out = new BufferedWriter(fstream);
							out.write(aFile.getName().toString());
							out.newLine();
							//close buffer writer
							out.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		                list.setSelectedIndex(index+1);
		            }
				}
				else {
					System.out.println("Filename already exists!!!");
				}
			}
			else{
				System.out.println("Canceled Operation");
			}
		}
		else if (e.getSource() == del) {
			try {
				
	            ListSelectionModel lsm = list.getSelectionModel();
	            int firstSelected = lsm.getMinSelectionIndex();
	            int lastSelected = lsm.getMaxSelectionIndex();
	            
	            String removeVideo = listModel.get(firstSelected).toString();
	            removeLine(removeVideo, file);
	            listModel.removeRange(firstSelected, lastSelected);
	            int size = listModel.size();

	            if (size == 0) {
	            //List is empty: disable delete, up, and down buttons.
	                del.setEnabled(false);
	            } 
	            else {
	            //Adjust the selection.
	                if (firstSelected == listModel.getSize()) {
	                //Removed item in last position.
	                    firstSelected--;
	                }
	                list.setSelectedIndex(firstSelected);
	            }
			}
			catch (NullPointerException er){}
		}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		wFrame.repaint();
		wFrame.revalidate();
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1) {
            //No selection: disable delete, up, and down buttons.
                del.setEnabled(false);
				try{
					wFrame.remove(myImagePanel);
				}
				catch(NullPointerException error){}
            } 
            else {
            //Single selection: permit all operations.
                del.setEnabled(true);
				try{
					wFrame.remove(myImagePanel);
				}
				catch(NullPointerException error){}
				finally{
					String filename = listModel.get(list.getSelectedIndex()).toString();
					try {
						input = new FileInputStream("config.properties");
						// load a properties file
						prop.load(input);
						File theFile = new File(prop.getProperty("imgDirectory"), filename);
						myImagePanel = new ImagePanel(theFile);
					}
					catch (IOException ex) {
						ex.printStackTrace();
					}
						finally {
					
						if (input != null) {
							try {
								input.close();
							} catch (IOException io) {
								io.printStackTrace();
							}
						}
					}
				}
				wFrame.add(myImagePanel, BorderLayout.CENTER);
            }
    		wFrame.repaint();
    		wFrame.revalidate();
		}
	}
	
	public void removeLine(String name, File pFile ) {
		 File tempFile = new File(System.getProperty("user.home") + File.separator + "temp.txt");
		 BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(pFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				if ((currentLine != null) && !currentLine.equalsIgnoreCase(name)) {
					writer.write(currentLine + System.getProperty("line.separator"));
				}
			}

			writer.flush();
			writer.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 boolean success = tempFile.renameTo(pFile);
		 System.out.println(success);
	}
	
	public DefaultListModel getListModel() {
		return listModel;
	}
		
	public JList getList() {
		return list;
	}
	
	public ImagePanel getImagePanel() {
		return myImagePanel;
	}
	
	public JButton getAddButton() {
		return add;
	}
	
	public JButton getDelButton() {
		return del;
	}
}
