package ImageViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.GridBagLayout;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JScrollPane;
import java.awt.GridLayout;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.RowFilter;

import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.ComponentOrientation;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.CardLayout;
import javax.swing.SpringLayout;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.ScrollPane;
import javax.swing.DropMode;
import javax.swing.ImageIcon;

import java.awt.Button;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JList;
import java.awt.Insets;
import javax.swing.JSplitPane;

public class GUIProject extends JFrame {

	/**
	 * Launch the application.
	 */
	private List<ImageInfo> images;
	private String imageDatabasePath;
	private JTable table_1;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIProject frame = new GUIProject();
					frame.setVisible(true);
					frame.setTitle("GUI Project - Wiktoria Prusik s19058");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIProject() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 800);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnLoad = new JMenu("Image Database");
		menuBar.add(mnLoad);
		
		JMenuItem mntmImageDatabase = new JMenuItem("Load...");
		mnLoad.add(mntmImageDatabase);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Save As...");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
				{
					try {
						Files.copy(new File(imageDatabasePath).toPath(),
								new File(fileChooser.getSelectedFile().getPath()).toPath());
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		mnLoad.add(mntmNewMenuItem);
		
		JMenuItem mntmEdit = new JMenuItem("Edit");
		mnLoad.add(mntmEdit);
		
		JMenu mnDisplay = new JMenu("Display");
		menuBar.add(mnDisplay);
		
		JMenuItem mntmAllImages = new JMenuItem("All Images");
		mntmAllImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (images != null)
				{
					String[] headers = {"Picture", "Description"};
					Object[][] rows = new Object[images.size()][2];
					
					fillRowsWithImagesAndTheirDescriptions(rows, headers, images);
				}
			}
		});
		mnDisplay.add(mntmAllImages);
		
		JMenu mnNewMenu = new JMenu("All Images By Tag...");
		mnDisplay.add(mnNewMenu);
		
		JMenu mnImageByInfo = new JMenu("Image By Info");
		mnDisplay.add(mnImageByInfo);
		
		JMenuItem mntmImageDatabase_1 = new JMenuItem("Image Database");
		mntmImageDatabase_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				createAndShowImageDatabaseTable();
			}
		});
		mnDisplay.add(mntmImageDatabase_1);
		
		mntmImageDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION)
				{
					ImageInfoBuilder imageBuilder = new ImageInfoBuilder();
					images = new ArrayList<ImageInfo>();
					try {
						imageDatabasePath = fileChooser.getSelectedFile().getPath();
						Files.readAllLines(Paths.get(imageDatabasePath)).forEach(line -> 
						{
							ImageInfo image = imageBuilder.BuildImage(line);
							if (image != null)
							{
								images.add(image);
							}
						});
						
						if (images != null)
						{
							mnNewMenu.removeAll();
							mnImageByInfo.removeAll();
							createAndShowImageDatabaseTable();
						    
						    List<String> tags = new ArrayList<>();
						    
						    for (ImageInfo image : images)
						    {
						    	if (!tags.contains(image.tag))
						    	{
						    		tags.add(image.tag);
						    		
						    		JMenuItem tagMenuItem = new JMenuItem(image.tag);
						    		mnNewMenu.add(tagMenuItem);
						    		tagMenuItem.addActionListener(new ActionListener() {
						    			public void actionPerformed(ActionEvent e){
										    List<ImageInfo> imagesWithTag = new ArrayList<>();
						    				
						    				for (ImageInfo imageWithTag : images)
						    					if (tagMenuItem.getText().equals(imageWithTag.tag))
						    						imagesWithTag.add(imageWithTag);
						    				
						    				String[] imageHeaders = {"Picture", "Description"};
						    				Object[][] imageRows = new Object[imagesWithTag.size()][2];
						    				
						    				fillRowsWithImagesAndTheirDescriptions(imageRows, imageHeaders, imagesWithTag);
						    			}
						    		});
						    	}
						    	JMenuItem imageMenuItem = new JMenuItem(image.path);
						    	mnImageByInfo.add(imageMenuItem);
						    	imageMenuItem.addActionListener(new ActionListener() {
						    		public void actionPerformed(ActionEvent e)
						    		{
						    			List<ImageInfo> imagesWithPath = new ArrayList<>();
						    			
						    			String[] imageHeaders = {"Picture", "Description"};
						    			Object[][] imageRows = new Object[1][2];
						    			
						    			imagesWithPath.add(image);
						    			fillRowsWithImagesAndTheirDescriptions(imageRows, imageHeaders, imagesWithPath);
						    		}
						    	});
						    }
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		JMenu mnSearch = new JMenu("Search");
		menuBar.add(mnSearch);
		
		JMenu mnImagesTaken = new JMenu("Images Taken");
		mnSearch.add(mnImagesTaken);
		
		JSplitPane splitPane = new JSplitPane();
		mnImagesTaken.add(splitPane);
		
		JLabel lblEarlierThan = new JLabel("Earlier Than...");
		splitPane.setLeftComponent(lblEarlierThan);
		
		textField = new JTextField();
		splitPane.setRightComponent(textField);
		textField.setColumns(10);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try {
					List<ImageInfo> earlierImages = new ArrayList<>();
					
					Date textFieldDate = new SimpleDateFormat("yyyy-MM-dd").parse(textField.getText());
					
					for (ImageInfo image : images)
					{
						if (!image.date.equals(""))
						{
							Date imageDate = new SimpleDateFormat("yyyy-MM-dd").parse(image.date);
							
							if (textFieldDate.compareTo(textFieldDate) < 0)
								earlierImages.add(image);
						}
						
						if (earlierImages.size() > 0)
						{
							String[] headers = {"Picture", "Description"};
							Object[][] imagesRows = new Object[earlierImages.size()][2];
							fillRowsWithImagesAndTheirDescriptions(imagesRows, headers, earlierImages);
						}
					}
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JSplitPane splitPane_1 = new JSplitPane();
		mnImagesTaken.add(splitPane_1);
		
		JLabel lblLaterThan = new JLabel("Later Than...");
		splitPane_1.setLeftComponent(lblLaterThan);
		
		textField_1 = new JTextField();
		splitPane_1.setRightComponent(textField_1);
		textField_1.setColumns(10);
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try {
					List<ImageInfo> earlierImages = new ArrayList<>();
					
					Date textFieldDate = new SimpleDateFormat("yyyy-MM-dd").parse(textField.getText());
					
					for (ImageInfo image : images)
					{
						if (!image.date.equals(""))
						{
							Date imageDate = new SimpleDateFormat("yyyy-MM-dd").parse(image.date);
							
							if (textFieldDate.compareTo(textFieldDate) < 0)
								earlierImages.add(image);
						}
						
						if (earlierImages.size() < 0)
						{
							String[] headers = {"Picture", "Description"};
							Object[][] imagesRows = new Object[earlierImages.size()][2];
							fillRowsWithImagesAndTheirDescriptions(imagesRows, headers, earlierImages);
						}
					}
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JMenu mnByPath = new JMenu("By Path");
		mnSearch.add(mnByPath);
		
		JMenuItem mntmGreatestValue = new JMenuItem("Greatest Value");
		mnByPath.add(mntmGreatestValue);
		
		JMenuItem mntmLeastValue = new JMenuItem("Least Value");
		mnByPath.add(mntmLeastValue);
		
		JSplitPane splitPane_3 = new JSplitPane();
		mnByPath.add(splitPane_3);
		
		JLabel label = new JLabel("Provided Value");
		splitPane_3.setLeftComponent(label);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		splitPane_3.setRightComponent(textField_3);
		
		JMenu mnNewMenu_1 = new JMenu("By Author");
		mnSearch.add(mnNewMenu_1);
		
		JMenuItem menuItem = new JMenuItem("Greatest Value");
		mnNewMenu_1.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("Least Value");
		mnNewMenu_1.add(menuItem_1);
		
		JSplitPane splitPane_4 = new JSplitPane();
		mnNewMenu_1.add(splitPane_4);
		
		JLabel label_1 = new JLabel("Provided Value");
		splitPane_4.setLeftComponent(label_1);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		splitPane_4.setRightComponent(textField_4);
		
		JMenu mnByLocation = new JMenu("By Location");
		mnSearch.add(mnByLocation);
		
		JMenuItem menuItem_3 = new JMenuItem("Greatest Value");
		mnByLocation.add(menuItem_3);
		
		JMenuItem menuItem_4 = new JMenuItem("Least Value");
		mnByLocation.add(menuItem_4);
		
		JSplitPane splitPane_2 = new JSplitPane();
		mnByLocation.add(splitPane_2);
		
		JLabel lblProvidedValue = new JLabel("Provided Value");
		splitPane_2.setLeftComponent(lblProvidedValue);
		
		textField_2 = new JTextField();
		splitPane_2.setRightComponent(textField_2);
		textField_2.setColumns(10);
		
		JMenu mnByDate = new JMenu("By Date");
		mnSearch.add(mnByDate);
		
		JMenuItem menuItem_6 = new JMenuItem("Greatest Value");
		mnByDate.add(menuItem_6);
		
		JMenuItem menuItem_7 = new JMenuItem("Least Value");
		mnByDate.add(menuItem_7);
		
		JSplitPane splitPane_5 = new JSplitPane();
		mnByDate.add(splitPane_5);
		
		JLabel label_2 = new JLabel("Provided Value");
		splitPane_5.setLeftComponent(label_2);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		splitPane_5.setRightComponent(textField_5);
		
	}
	
	private void createAndShowImageDatabaseTable()
	{
		if (images != null)
		{
			String[] headers = {"Path", "Author", "Location", "Date", "Tag"};
		    int imagesNumber = images.size();
			Object[][] content = new Object[imagesNumber][headers.length];
		    
		    for (int i = 0; i < imagesNumber; i++)
		    {
		    	content[i][0] = images.get(i).path;
		    	content[i][1] = images.get(i).author;
		    	content[i][2] = images.get(i).date;
		    	content[i][3] = images.get(i).location;
		    	content[i][4] = images.get(i).tag;
		    }
		      
		    JTable table = new JTable(content, headers);
		    table.setEnabled(false);
		    
		    TableRowSorter<TableModel> sorter  = new TableRowSorter<TableModel>(table.getModel());
		    table.setRowSorter(sorter);
		    sorter.setSortable(0, false);
		    sorter.setSortable(4, false);
		    
		    JScrollPane scrollPane = new JScrollPane(table);
		    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		      
		    JPanel panel = new JPanel();
		    panel.setLayout(new BorderLayout());
		    panel.add(scrollPane, BorderLayout.CENTER);
		    
		    setContentPane(panel);
		    setVisible(true);
		}
	}
	
	private void fillRowsWithImagesAndTheirDescriptions(Object[][] rows, String[] headers, List<ImageInfo> images)
	{
		int i = 0;
		for (ImageInfo image : images)
		{
			rows[i][0] = new ImageIcon(image.path);
			rows[i][1] = image.path + ";\n"
						+ image.author + ";\n"
						+ image.location + ";\n"
						+ image.date + ";\n"
						+ image.tag;
			i++;
		}
		
		DefaultTableModel model = new DefaultTableModel(rows, headers)
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
		
		table_1 = new JTable(model);
		table_1.setShowHorizontalLines(true);
		table_1.setShowVerticalLines(true);
		table_1.setShowGrid(true);
		
		table_1.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
		
		for (int row = 0; row < table_1.getRowCount(); row++)
        {
            int rowHeight = table_1.getRowHeight();

            for (int column = 0; column < table_1.getColumnCount(); column++)
            {
                Component comp = table_1.prepareRenderer(table_1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            table_1.setRowHeight(row, rowHeight);
        }
		
		JScrollPane scrollPane = new JScrollPane(table_1);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    
	    JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    panel.add(scrollPane, BorderLayout.CENTER);
		setContentPane(scrollPane);
	    setVisible(true);
	}
}
