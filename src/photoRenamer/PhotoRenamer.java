package photoRenamer;

import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import managers.ImageManager;
import managers.TagManager;
import managers.MyLogingSystem;;

/**
 * Create the window for photo renamer.
 */
public class PhotoRenamer {
	// Singletons
	private ImageManager im = ImageManager.getInstance();
	private TagManager tm = TagManager.getInstance();
	private MyLogingSystem mls = MyLogingSystem.getInstance();

	// First hierarchy
	private JFrame jf;
	private JTabbedPane tabPane;
	private JPanel panelMain, panelLeft, imagePanel;
	private JPanel panelTab1, panelTab2, panelTab3;
	private JLabel labelPicName, imageLabel;
	private JButton buttonChooseDir;
	private JScrollPane scrollPanePic;
	private JFileChooser fileChooser;

	// Second hierarchy
	// Things under panelTab1
	private JLabel labelTab1;
	private JList<String> listTab1;
	private DefaultListModel<String> listTab1Model = new DefaultListModel<>();
	private JScrollPane scrollPaneTab1;

	// Things under panelTab2
	private JLabel labelTab2;
	private JButton buttonTab2;
	private JList<String> listTab2;
	private DefaultListModel<String> listTab2Model = new DefaultListModel<>();
	private JScrollPane scrollPaneTab2;

	// Things under panelTab3
	private JLabel labelTab31, labelTab32;
	private JButton buttonTab31, buttonTab32;
	private JList<String> listTab31, listTab32;
	private DefaultListModel<String> listTab31Model = new DefaultListModel<>();
	private DefaultListModel<String> listTab32Model = new DefaultListModel<>();
	private JScrollPane scrollPaneTab31, scrollPaneTab32;
	private JPanel panelTab3Left, panelTab3Mid, panelTab3Right;
	private JTextField jtf;

	// Constructor to build the window
	public PhotoRenamer() {
		// Main frame
		jf = new JFrame("My Photo Renamer");

		// Main JPanel
		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		jf.setContentPane(panelMain);

		// File chooser
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Choose a path that contains pictures...");

		// South
		buttonChooseDir = new JButton("Choose Directory");
		ButtonListener bl = new ButtonListener();
		buttonChooseDir.addActionListener(bl);
		buttonChooseDir.setActionCommand("chooseDirectory");
		panelMain.add(buttonChooseDir, BorderLayout.SOUTH);
		buttonChooseDir.setMnemonic(KeyEvent.VK_C);

		// Center (or left of the frame)
		panelLeft = new JPanel();
		panelLeft.setLayout(new BorderLayout());
		imageLabel = new JLabel(null, null, JLabel.CENTER);
		imagePanel = new JPanel();
		imagePanel.add(imageLabel);
		scrollPanePic = new JScrollPane(imagePanel);
		scrollPanePic.setPreferredSize(new Dimension(400, 200));
		panelLeft.add(scrollPanePic, BorderLayout.CENTER);
		labelPicName = new JLabel("picName", SwingConstants.CENTER);
		panelLeft.add(labelPicName, BorderLayout.NORTH);
		panelMain.add(panelLeft, BorderLayout.CENTER);

		// East
		tabPane = new JTabbedPane();
		panelTab1 = new JPanel(new BorderLayout());
		panelTab2 = new JPanel(new BorderLayout());
		panelTab3 = new JPanel(new BorderLayout());
		panelTab2.setPreferredSize(new Dimension(610, 300));

		// panelTab1
		labelTab1 = new JLabel("All the pictures under that directory:", SwingConstants.CENTER);
		listTab1 = new JList<String>(listTab1Model);
		listTab1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneTab1 = new JScrollPane(listTab1);
		panelTab1.add(labelTab1, BorderLayout.NORTH);
		panelTab1.add(scrollPaneTab1, BorderLayout.CENTER);
		JListListener jll = new JListListener();
		keyListener kl = new keyListener();
		listTab1.addListSelectionListener(jll);
		listTab1.addKeyListener(kl);
		tabPane.addTab("Pictures", panelTab1);
		tabPane.setMnemonicAt(0, KeyEvent.VK_P);

		// panelTab2
		labelTab2 = new JLabel("All names that this picture has had: (Sorted by timestamp)", SwingConstants.LEFT);
		buttonTab2 = new JButton("<~ oT emaN treveR");
		buttonTab2.setMnemonic(KeyEvent.VK_E);
		buttonTab2.addActionListener(bl);
		buttonTab2.setActionCommand("revert");
		listTab2 = new JList<String>(listTab2Model);
		listTab2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneTab2 = new JScrollPane(listTab2);
		panelTab2.add(labelTab2, BorderLayout.NORTH);
		panelTab2.add(scrollPaneTab2, BorderLayout.WEST);
		panelTab2.add(buttonTab2, BorderLayout.CENTER);
		tabPane.addTab("Names", panelTab2);
		tabPane.setMnemonicAt(1, KeyEvent.VK_N);

		// panelTab3
		panelTab3Left = new JPanel(new BorderLayout());
		labelTab31 = new JLabel("Tags: (Ctrl[+A]: Select[ALL])");
		listTab31 = new JList<String>(listTab31Model);
		scrollPaneTab31 = new JScrollPane(listTab31);
		panelTab3Left.add(labelTab31, BorderLayout.NORTH);
		panelTab3Left.add(scrollPaneTab31, BorderLayout.CENTER);
		panelTab3.add(panelTab3Left, BorderLayout.WEST);

		panelTab3Mid = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(panelTab3Mid, BoxLayout.Y_AXIS);
		panelTab3Mid.setLayout(boxLayout1);
		buttonTab31 = new JButton("<~ add");
		buttonTab31.setMnemonic(KeyEvent.VK_A);
		buttonTab31.addActionListener(bl);
		buttonTab31.setActionCommand("add");
		buttonTab32 = new JButton("remove ~>");
		buttonTab32.setMnemonic(KeyEvent.VK_R);
		buttonTab32.addActionListener(bl);
		buttonTab32.setActionCommand("remove");
		panelTab3Mid.add(Box.createRigidArea(new Dimension(0, 20)));
		panelTab3Mid.add(buttonTab31);
		panelTab3Mid.add(Box.createRigidArea(new Dimension(0, 15)));
		panelTab3Mid.add(buttonTab32);
		buttonTab31.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonTab32.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelTab3.add(panelTab3Mid, BorderLayout.CENTER);

		panelTab3Right = new JPanel();
		BoxLayout boxLayout2 = new BoxLayout(panelTab3Right, BoxLayout.Y_AXIS);
		panelTab3Right.setLayout(boxLayout2);
		labelTab32 = new JLabel("All Tags Available: (Enter/Delete Tags)");
		jtf = new JTextField(10);
		jtf.setMaximumSize(new Dimension(200, 15));
		jtf.addActionListener(bl);
		jtf.setActionCommand("JText");
		listTab32 = new JList<String>(listTab32Model);
		for (String s : TagManager.getAllTags()) {
			listTab32Model.addElement(s);
		}
		listTab32.addKeyListener(kl);
		scrollPaneTab32 = new JScrollPane(listTab32);
		panelTab3Right.add(labelTab32);
		labelTab32.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelTab3Right.add(jtf);
		panelTab3Right.add(scrollPaneTab32);
		panelTab3Right.setPreferredSize(new Dimension(256, 0));
		panelTab3.add(panelTab3Right, BorderLayout.EAST);

		tabPane.addTab("Tags", panelTab3);
		tabPane.setMnemonicAt(2, KeyEvent.VK_T);
		panelMain.add(tabPane, BorderLayout.EAST);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setLocationRelativeTo(null);

		try {
			tm.saveToFile("tm.bin");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			im.saveToFile("im.bin");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create and show a photo renamer.
	 *
	 * @param args
	 *            the command-line arguments.
	 */
	public static void main(String[] args) {
		PhotoRenamer pr = new PhotoRenamer();
		pr.jf.setVisible(true);
	}

	/**
	 * Adjust each panel's size to fit the frame.
	 * 
	 */
	private void adjustWindow() {
		panelTab2.setPreferredSize(new Dimension(630, 300));
		scrollPaneTab2.setPreferredSize(new Dimension(256, 0));
		scrollPaneTab31.setPreferredSize(new Dimension(256, 0));
		panelTab3.setPreferredSize(new Dimension(630, 300));
		panelTab1.setPreferredSize(new Dimension(630, 300));
	}

	/**
	 * An action listener that performs different actions for different action
	 * commands, which also uses the built-in observer pattern.
	 */
	private class ButtonListener implements ActionListener {

		/**
		 * Perform different actions for different action commands.
		 * 
		 * @param ae
		 *            the ActionEvent generated by the component of JFrame.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getActionCommand() == "chooseDirectory") {
				int returnVal = fileChooser.showOpenDialog(jf);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					ArrayList<String> al = new ArrayList<String>();
					im.showAllImageFiles(file, al);

					// First remove all the elements before adding to it.
					listTab1Model.removeAllElements();
					for (String s : al) {
						listTab1Model.addElement(s);
					}
					listTab2Model.removeAllElements();
					listTab31Model.removeAllElements();
					labelPicName.setText("picName");
					imageLabel.setIcon(null);
					tabPane.setSelectedIndex(0);
				}
			} else if (ae.getActionCommand() == "JText") {
				String s = jtf.getText();
				if (TagManager.getAllTags().contains(s)) {
				} else {
					TagManager.getAllTags().add(s);
					listTab32Model.addElement(s);
				}
				jtf.setText("");

				try {
					tm.saveToFile("tm.bin");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (ae.getActionCommand() == "add") {
				if ((listTab1.getSelectedValue() != null) && (listTab32.getSelectedValue() != null)) {
					List<String> l = listTab32.getSelectedValuesList();
					Image i = ImageManager.getImages().get(listTab1.getSelectedValue());
					if (l.size() == 1) {
						if (i.getTags().contains(listTab32.getSelectedValue())) {
						} else {
							i.addTag(listTab32.getSelectedValue(), true);
							// Goes after the add action to update new things.
							listTab2Model.addElement(i.getFileName());
							listTab31Model.addElement(listTab32.getSelectedValue());
							labelPicName.setText(i.getFileName());
							listTab1Model.setElementAt(i.getPath(), listTab1.getSelectedIndex());
							im.addImage(i);
						}
					} else {
						if (i.addTags(l)) {
							// Goes after the add action to update new things.
							listTab2Model.addElement(i.getFileName());
							listTab31Model.removeAllElements();
							for (String s : i.getTags()) {
								listTab31Model.addElement(s);
							}
							labelPicName.setText(i.getFileName());
							listTab1Model.setElementAt(i.getPath(), listTab1.getSelectedIndex());
							im.addImage(i);
						}
					}

					try {
						im.saveToFile("im.bin");
					} catch (IOException e) {
						e.printStackTrace();
					}
					adjustWindow();
				}

			} else if (ae.getActionCommand() == "remove") {
				if ((listTab1.getSelectedValue() != null) && (listTab31.getSelectedValue() != null)) {
					List<String> l = listTab31.getSelectedValuesList();
					Image i = ImageManager.getImages().get(listTab1.getSelectedValue());
					if (l.size() == 1) {
						i.removeTag(listTab31.getSelectedValue(), true);
						// Goes after the remove action to update new things.
						listTab2Model.addElement(i.getFileName());
						listTab31Model.removeElement(listTab31.getSelectedValue());
						labelPicName.setText(i.getFileName());
						listTab1Model.setElementAt(i.getPath(), listTab1.getSelectedIndex());
						im.addImage(i);
					} else {
						i.removeTags(l);
						// Goes after the remove action to update new things.
						listTab2Model.addElement(i.getFileName());
						for (String s : l) {
							listTab31Model.removeElement(s);
						}
						labelPicName.setText(i.getFileName());
						listTab1Model.setElementAt(i.getPath(), listTab1.getSelectedIndex());
						im.addImage(i);
					}

					try {
						im.saveToFile("im.bin");
					} catch (IOException e) {
						e.printStackTrace();
					}
					adjustWindow();
				}
			} else if (ae.getActionCommand() == "revert") {
				if ((listTab1.getSelectedValue() != null) && (listTab2.getSelectedValue() != null)) {
					Image i = ImageManager.getImages().get(listTab1.getSelectedValue());
					if (listTab2.getSelectedValue().equals(i.getFileName())) {
					} else {
						i.revertName(listTab2.getSelectedValue());
						listTab31Model.removeAllElements();
						for (String s : i.getTags()) {
							listTab31Model.addElement(s);
						}
						labelPicName.setText(i.getFileName());
						listTab1Model.setElementAt(i.getPath(), listTab1.getSelectedIndex());
						im.addImage(i);
						try {
							im.saveToFile("im.bin");
						} catch (IOException e) {
							e.printStackTrace();
						}
						adjustWindow();
					}
				}
			}
		}
	}

	/**
	 * A ListSelectionListener that updates the JList according to the
	 * requirement, which also uses the built-in observer pattern.
	 */
	private class JListListener implements ListSelectionListener {

		/**
		 * Updates the JList according to the requirement.
		 * 
		 * @param e
		 *            the ListSelectionEvent generated by the JList.
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				if (e.getSource() == listTab1) {
					if (listTab1.getSelectedValue() == null) {
						listTab2Model.removeAllElements();
						listTab31Model.removeAllElements();
						imageLabel.setIcon(null);
						labelPicName.setText("picName");
					} else {
						// Draw the icon and update the label.
						BufferedImage img = null;
						File f = new File(listTab1.getSelectedValue());
						labelPicName.setText(f.getName());
						try {
							img = ImageIO.read(f);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						java.awt.Image i = img.getScaledInstance(300, 290, java.awt.Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(i);
						imageLabel.setIcon(icon);

						listTab2Model.removeAllElements();
						listTab31Model.removeAllElements();
						// update the Original name list and current tags list.
						if (ImageManager.getImages().containsKey(listTab1.getSelectedValue())) {
							// add names.
							ArrayList<String> al = new ArrayList<String>();
							al = ImageManager.getImages().get(listTab1.getSelectedValue()).getNames();
							for (String s : al) {
								listTab2Model.addElement(s);
							}
							// add current tags.
							HashSet<String> hs = new HashSet<String>();
							hs = ImageManager.getImages().get(listTab1.getSelectedValue()).getTags();
							for (String m : hs) {
								listTab31Model.addElement(m);
							}
						} else {
							Image image = new Image(listTab1.getSelectedValue());
							listTab2Model.addElement(image.getFileName());
							im.addImage(image);
							try {
								tm.saveToFile("tm.bin");
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}

						// Make sure the panels and JLists will not expand or
						// become smaller.
						adjustWindow();
					}
				}
			}
		}
	}

	/**
	 * A KeyListener that performs different actions according to pressing
	 * different keys, which also uses the built-in observer pattern.
	 */
	private class keyListener implements KeyListener {

		/**
		 * Performs different actions according to pressing different keys.
		 * 
		 * @param ke
		 *            the KeyEvent generated by the keyboard.
		 */
		@Override
		public void keyPressed(KeyEvent ke) {
			if (ke.getSource() == listTab32) {
				if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
					List<String> l = listTab32.getSelectedValuesList();
					if (l.size() != 0) {
						for (String s : l) {
							listTab32Model.removeElement(s);
							TagManager.getAllTags().remove(s);
						}
						try {
							tm.saveToFile("tm.bin");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		public void keyReleased(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent arg0) {
		}
	}
}
