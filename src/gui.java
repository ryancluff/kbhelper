import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import components.queue.Queue;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

//Created by Clinton Stamper and finished by Ryan Cluff

public class gui extends JFrame implements ActionListener {
	
	private final boolean debug = false;
	public static boolean dispose = false;
	private final String version = "2.6.2";

	private static final long serialVersionUID = 4043046839186108654L;
	private final JPanel contentPane;
	
	private final asteaDownGui adGui;
	private final JTextField titleMicrocenter;
	private final JTextField customerName;
	private final JFormattedTextField primaryPhone;
	private final JFormattedTextField secondaryPhoneOrEmail;
	private final JTextField usernameField;
	private final JTextField passwordField;
	private final JTextField altPickupName;
	private final JFormattedTextField driveCost;
	private final JLabel lblCost;
	private final JFormattedTextField driveSku;
	private final JLabel lblSku;
	private final JTextArea reportedIssuesField;
	private final JLabel diagReceiptLabel;
	private final JTextField diagReceiptField;
	private final JTextField avBundleNameBox;
	private final JLabel avCostLabel;
	private final JLabel warrantyReceiptLabel;
	private final JTextField warrantyReceiptField;
	private final JTable extraCostsTable;
	private final JTextField daysToBenchField;
	private final JTextField userIDField;
	private final JTextField statusDisplay;
	private final JCheckBox dataBackupCheckBox = new JCheckBox("Backup");
	private final JCheckBox avBundleCheckBox = new JCheckBox("Antivirus");
	private final JCheckBox reimageCheckBox = new JCheckBox("Reimage");
	private final JCheckBox dataPolicyCheckbox = new JCheckBox("Data loss Policy");
	private final JCheckBox originallyPurchasedCheckBox = new JCheckBox("Purchased Originally at Microcenter?");
	private final JCheckBox freeOfChargeCheckBox = new JCheckBox("Free of Charge");
	private final JCheckBox asteaDown = new JCheckBox("Astea down");

	private final JComboBox<String> osComboBox = new JComboBox<String>();
	private final JComboBox<String> dataBackupComboBox = new JComboBox<String>();
	private final JComboBox<String> diagComboBox = new JComboBox<String>();
	private final JComboBox<String> warrantyComboBox = new JComboBox<String>();
	private final JComboBox<String> quoteComboBox = new JComboBox<String>();
	private final JComboBox<String> serviceComboBox = new JComboBox<String>();

	private final JButton copyToClipboardButton = new JButton("Copy to Clipboard");
	private final JButton clearForm = new JButton("Clear Form");
	private JFormattedTextField avBundleCostField;
	private final JLabel daysToBenchLabel = new JLabel("Days to Contact:");
	
	private final Register register = new Register();

	private final String daysToBenchDefaultText = "Days to Contact:";
	private String lastDaysToBenchValue = "";
	
	// config variables
	private final XMLTree configInputFile = new XMLTree1("Config/config.xml");
	private List<Service> services = new ArrayList<Service>();
	private List<String> osList = new ArrayList<String>();
	private List<Service> backupList = new ArrayList<Service>();
	private List<String> backupSizesList = new ArrayList<String>();
	private Service defaultAVBundle = new Service("", 0.0);
	private double reimageWinPrice = 0.0;
	private double reimageMacPrice = 0.0;
	private double backupLabor = 0.0;
	private double hddDiscount = 0.0;
		
	private List<String> blackList = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui frame = new gui();
					frame.setVisible(true);
					asteaDownGui adGui = new asteaDownGui();
					adGui.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
	}
	
	/**
	 * Create the frame.
	 */
	public gui() {
		importConfig(configInputFile);
		setTitle("Knowledge Bar Assistant | Version " + version);
		setIconImage(Toolkit.getDefaultToolkit().getImage(gui.class.getResource("/images/logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 10, 350, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		adGui = new asteaDownGui();

		titleMicrocenter = new JTextField();
		titleMicrocenter.setEditable(false);
		titleMicrocenter.setHorizontalAlignment(SwingConstants.CENTER);
		titleMicrocenter.setText("Knowledge Bar Helper | Version " + version);
		titleMicrocenter.setBounds(10, 0, this.getWidth()-6, 20);
		contentPane.add(titleMicrocenter);
		titleMicrocenter.setColumns(10);

		JLabel customerNameLabel = new JLabel("Customer Name:");
		customerNameLabel.setBounds(10, 40, 122, 14);
		customerNameLabel.setForeground(requiredColor);
		contentPane.add(customerNameLabel);
		
		customerName = new JTextField();
		customerName.setToolTipText("Customer's full name");
		customerName.setBounds(164, 37, 170, 20);
		contentPane.add(customerName);
		customerName.setColumns(10);

		JLabel phoneNumberLabel = new JLabel("Primary Phone:");
		phoneNumberLabel.setBounds(10, 65, 94, 14);
		phoneNumberLabel.setForeground(requiredColor);
		contentPane.add(phoneNumberLabel);

		primaryPhone = new JFormattedTextField();
		primaryPhone.setFocusLostBehavior(JFormattedTextField.COMMIT);
		primaryPhone.setToolTipText("Primary Contact Number");
		primaryPhone.setColumns(10);
		primaryPhone.setBounds(164, 62, 170, 20);
		contentPane.add(primaryPhone);
		
		JLabel secondaryPhoneOrEmailLabel = new JLabel("Secondary Contact:");
		secondaryPhoneOrEmailLabel.setBounds(10, 90, 120, 14);
		contentPane.add(secondaryPhoneOrEmailLabel);
		
		secondaryPhoneOrEmail = new JFormattedTextField();
		secondaryPhoneOrEmail.setFocusLostBehavior(JFormattedTextField.COMMIT);
		secondaryPhoneOrEmail.setToolTipText("Secondary Phone Number or Email");
		secondaryPhoneOrEmail.setColumns(10);
		secondaryPhoneOrEmail.setBounds(164, 87, 170, 20);
		contentPane.add(secondaryPhoneOrEmail);

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(10, 115, 84, 14);
		contentPane.add(usernameLabel);

		usernameField = new JTextField();
		usernameField.setToolTipText("Username for device");
		usernameField.setColumns(10);
		usernameField.setBounds(164, 112, 170, 20);
		contentPane.add(usernameField);

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 140, 127, 14);
		contentPane.add(passwordLabel);

		passwordField = new JTextField();
		passwordField.setToolTipText("Password for device");
		passwordField.setColumns(10);
		passwordField.setBounds(164, 137, 170, 20);
		contentPane.add(passwordField);

		JLabel altPickupLabel = new JLabel("Alt. Pickup Names:");
		altPickupLabel.setBounds(10, 175, 192, 14);
		contentPane.add(altPickupLabel);

		altPickupName = new JTextField();
		altPickupName.setToolTipText("Alternate pickup names");
		altPickupName.setColumns(10);
		altPickupName.setBounds(164, 172, 170, 20);
		contentPane.add(altPickupName);

		dataBackupCheckBox.setBounds(10, 210, 103, 14);
		contentPane.add(dataBackupCheckBox);
		
		dataPolicyCheckbox.setBackground(Color.LIGHT_GRAY);
		dataPolicyCheckbox.setForeground(requiredColor);
		dataPolicyCheckbox.setBounds(10, 232, 150, 24);
		contentPane.add(dataPolicyCheckbox);
		
		dataBackupComboBox.setModel(getModelOfBackupBox());
		dataBackupComboBox.setEnabled(false);
		dataBackupComboBox.setToolTipText("Destination Drive");
		dataBackupComboBox.setBounds(164, 207, 170, 20);
		dataBackupComboBox.setSelectedIndex(0);
		dataBackupComboBox.setForeground(Color.LIGHT_GRAY);
		contentPane.add(dataBackupComboBox);
		
		lblSku = new JLabel("SKU:");
		lblSku.setBounds(164, 237, 48, 14);
		lblSku.setForeground(Color.LIGHT_GRAY);
		contentPane.add(lblSku);
		
		driveSku = new JFormattedTextField();
		driveSku.setToolTipText("SKU of Non-Service Drive");
		driveSku.setText("");
		driveSku.setEnabled(false);
		driveSku.setBackground(Color.WHITE);
		driveSku.setColumns(10);
		driveSku.setBounds(164, 259, 80, 20);
		contentPane.add(driveSku);
		
		lblCost = new JLabel("Cost:");
		lblCost.setBounds(254, 237, 48, 14);
		lblCost.setForeground(Color.LIGHT_GRAY);
		contentPane.add(lblCost);
		
		driveCost = new JFormattedTextField();
		driveCost.setToolTipText("Cost of Non-Service Drive");
		driveCost.setText("");
		driveCost.setEnabled(false);
		driveCost.setBackground(Color.WHITE);
		driveCost.setColumns(10);
		driveCost.setBounds(254, 259, 80, 20);
		contentPane.add(driveCost);
		
		JLabel osLabel = new JLabel("OS:");
		osLabel.setBounds(10, 292, 103, 14);
		contentPane.add(osLabel);
		
		reimageCheckBox.setBounds(10, 317, 103, 14);
		contentPane.add(reimageCheckBox);
		reimageCheckBox.setToolTipText("Should we reimage the device?");
		
		
		osComboBox.setModel(getModelOfOsBox());
		osComboBox.setEnabled(true);
		osComboBox.setToolTipText("OS to reimage device to");
		osComboBox.setBounds(164, 292, 170, 20);
		osComboBox.setBackground(Color.WHITE);
		contentPane.add(osComboBox);
		
		freeOfChargeCheckBox.setFont(new Font("Dialog", Font.PLAIN, 12));
		freeOfChargeCheckBox.setBounds(164, 317, 170, 23);
		freeOfChargeCheckBox.setEnabled(false);
		freeOfChargeCheckBox.setForeground(Color.LIGHT_GRAY);
		contentPane.add(freeOfChargeCheckBox);

		avBundleCheckBox.setBounds(10, 355, 103, 14);
		contentPane.add(avBundleCheckBox);

		avBundleNameBox = new JTextField();
		avBundleNameBox.setText(defaultAVBundle.getName());
		avBundleNameBox.setToolTipText("AV Bundle to Install");
		avBundleNameBox.setEnabled(false);
		avBundleNameBox.setColumns(10);
		avBundleNameBox.setBounds(164, 352, 170, 20);
		avBundleNameBox.setForeground(Color.LIGHT_GRAY);
		contentPane.add(avBundleNameBox);
		
		avCostLabel = new JLabel("Cost:");
		avCostLabel.setBounds(114, 380, 48, 14);
		avCostLabel.setForeground(Color.LIGHT_GRAY);
		contentPane.add(avCostLabel);

		avBundleCostField = new JFormattedTextField();
		avBundleCostField.setText(String.format("%.2f", defaultAVBundle.getPrice()));
		avBundleCostField.setEnabled(false);
		avBundleCostField.setToolTipText("Cost of Antivirus");
		avBundleCostField.setColumns(10);
		avBundleCostField.setBounds(164, 377, 170, 20);
		avBundleCostField.setForeground(Color.LIGHT_GRAY);
		contentPane.add(avBundleCostField);
		
		JLabel reportedIssueLabel = new JLabel("Reported Issues:");
		reportedIssueLabel.setBounds(10, 415, 148, 14);
		contentPane.add(reportedIssueLabel);
		
		JScrollPane scrollPaneReportedIssues = new JScrollPane();
		scrollPaneReportedIssues.setBounds(164, 412, 168, 38);
		contentPane.add(scrollPaneReportedIssues);
		
		reportedIssuesField = new JTextArea();
		reportedIssuesField.setForeground(Color.BLACK);
		scrollPaneReportedIssues.setViewportView(reportedIssuesField);
				
		reportedIssuesField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		reportedIssuesField.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		reportedIssuesField.setLineWrap(true);
		reportedIssuesField.setToolTipText("Customer has reported the following issues:");
		reportedIssuesField.setColumns(10);
		
		JLabel lblVerifiedAtKb = new JLabel("Verified at KB:");
		lblVerifiedAtKb.setBounds(10, 465, 142, 14);
		contentPane.add(lblVerifiedAtKb);
		
		JScrollPane scrollPaneVerifiedIssues = new JScrollPane();
		scrollPaneVerifiedIssues.setBounds(164, 462, 168, 38);
		contentPane.add(scrollPaneVerifiedIssues);
		
		verifiedIssuesField = new JTextArea();
		verifiedIssuesField.setForeground(Color.BLACK);
		scrollPaneVerifiedIssues.setViewportView(verifiedIssuesField);
		
		verifiedIssuesField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		verifiedIssuesField.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		verifiedIssuesField.setLineWrap(true);
		verifiedIssuesField.setToolTipText("Issues verified at Knowledge Bar:");
		verifiedIssuesField.setColumns(10);

		JLabel requestedActionsLabel = new JLabel("Requested Actions:");
		requestedActionsLabel.setForeground(requiredColor);
		requestedActionsLabel.setBounds(10, 515, 142, 14);
		contentPane.add(requestedActionsLabel);
		
		requestedActionsField = new JTextField();
		requestedActionsField.setToolTipText("Customer has requested the following actions:");
		requestedActionsField.setColumns(10);
		requestedActionsField.setBounds(164, 512, 170, 20);
		contentPane.add(requestedActionsField);
		
		JLabel lblDiag = new JLabel("Diagnostics:");
		lblDiag.setForeground(requiredColor);
		lblDiag.setBounds(10, 550, 127, 14);
		contentPane.add(lblDiag);

		diagComboBox.setToolTipText("Please select which diag option we are doing");
		diagComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"", "No Diagnostic Tests", "Diagnostics ($39.99) [X]", "Free Diagnostics [W, P, R]", "Free Diagnostics [C]"}));
		diagComboBox.setSelectedIndex(0);
		diagComboBox.setBounds(10, 575, 148, 20);
		diagComboBox.setBackground(Color.WHITE);
		contentPane.add(diagComboBox);

		diagReceiptLabel = new JLabel("Paid Receipt:");
		diagReceiptLabel.setBounds(164, 550, 127, 14);
		diagReceiptLabel.setForeground(Color.LIGHT_GRAY);
		contentPane.add(diagReceiptLabel);

		diagReceiptField = new JTextField();  
		diagReceiptField.setEnabled(false);
		diagReceiptField.setToolTipText("What receipt did the customer pay for the diag on?");
		diagReceiptField.setColumns(10);
		diagReceiptField.setBounds(164, 575, 170, 20);
		contentPane.add(diagReceiptField);
		
		originallyPurchasedCheckBox.setBackground(Color.LIGHT_GRAY);
		originallyPurchasedCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		originallyPurchasedCheckBox.setBounds(10, 610, 324, 24);
		contentPane.add(originallyPurchasedCheckBox);

		JLabel warrantySectionTitle = new JLabel("Warranty:");
		warrantySectionTitle.setHorizontalAlignment(SwingConstants.LEFT);
		warrantySectionTitle.setBounds(10, 640, 148, 14);
		warrantySectionTitle.setForeground(requiredColor);
		contentPane.add(warrantySectionTitle);

		warrantyComboBox.setToolTipText("Please select which Warranty applies");
		warrantyComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"", "No Warranty", "Accidental Plan", "Extended Warranty", "Manufacturer\'s"}));
		warrantyComboBox.setSelectedIndex(0);
		warrantyComboBox.setBounds(10, 665, 148, 20);
		warrantyComboBox.setBackground(Color.WHITE);
		contentPane.add(warrantyComboBox);

		warrantyReceiptLabel = new JLabel("Purchase Receipt:");
		warrantyReceiptLabel.setBounds(166, 640, 130, 14);
		warrantyReceiptLabel.setForeground(Color.LIGHT_GRAY);
		contentPane.add(warrantyReceiptLabel);

		warrantyReceiptField = new JTextField();
		warrantyReceiptField.setToolTipText("What receipt was the warranty purchased on?");
		warrantyReceiptField.setColumns(10);
		warrantyReceiptField.setBounds(166, 665, 170, 20);
		warrantyReceiptField.setEnabled(false);
		contentPane.add(warrantyReceiptField);

		JScrollPane extraCostsTableScrollPane = new JScrollPane();
		extraCostsTableScrollPane.setBounds(10, 700, 324, 75);
		contentPane.add(extraCostsTableScrollPane);

		extraCostsTable = new JTable();
		extraCostsTableScrollPane.setViewportView(extraCostsTable);
		extraCostsTable.setToolTipText("All items that are quoted go on left with price on right");
		extraCostsTable.setBorder(UIManager.getBorder("PopupMenu.border"));
		extraCostsTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null},
					{null, null, null}, 
				},
				new String[] {
						"Task/Item", "Price", "Paid Receipt"
				}
				));
		extraCostsTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		
		serviceComboBox.setModel(getModelOfServiceBox());
		serviceComboBox.setBackground(Color.WHITE);
		serviceComboBox.setBounds(10, 785, 324, 25);
		contentPane.add(serviceComboBox);

		daysToBenchLabel.setForeground(Color.LIGHT_GRAY);
		daysToBenchLabel.setBounds(10, 855, 94, 14);
		contentPane.add(daysToBenchLabel);

		daysToBenchField = new JTextField();
		daysToBenchField.setBackground(Color.WHITE);
		daysToBenchField.setForeground(Color.LIGHT_GRAY);
		daysToBenchField.setEnabled(false);
		daysToBenchField.setToolTipText("How many days are we to bench?");
		daysToBenchField.setColumns(10);
		daysToBenchField.setBounds(112, 852, 48, 20);
		contentPane.add(daysToBenchField);

		JLabel userIDLabel = new JLabel("USER ID:");
		userIDLabel.setForeground(requiredColor);
		userIDLabel.setBounds(186, 855, 67, 14);
		contentPane.add(userIDLabel);

		userIDField = new JTextField();
		userIDField.setToolTipText("What is your user id?");
		userIDField.setColumns(10);
		userIDField.setBackground(Color.WHITE);
		userIDField.setBounds(256, 852, 78, 20);
		contentPane.add(userIDField);
		
		quoteComboBox.setForeground(Color.BLACK);
		quoteComboBox.setBackground(Color.WHITE);
		quoteComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Normal Quote Time", "Front of Line", "Straight to Tech", "Shipout"}));
		quoteComboBox.setBounds(10, 820, 225, 25);
		quoteComboBox.addActionListener(this);
		contentPane.add(quoteComboBox);

		copyToClipboardButton.setBackground(new Color(204, 255, 204));
		copyToClipboardButton.setBounds(10, 880, 190, 23);
		contentPane.add(copyToClipboardButton);

		clearForm.setBackground(new Color(255, 204, 204));
		clearForm.setBounds(200, 880, 130, 23);
		contentPane.add(clearForm);

		statusDisplay = new JTextField();
		statusDisplay.setToolTipText("The status of the Helper");
		statusDisplay.setBackground(Color.DARK_GRAY);
		statusDisplay.setEditable(false);
		statusDisplay.setText("Status");
		statusDisplay.setForeground(Color.WHITE);
		statusDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		statusDisplay.setBounds(10, 910, 324, 23);
		contentPane.add(statusDisplay);
		statusDisplay.setColumns(10);
		
		asteaDown.setBounds(240, 820, 100, 25);	
		contentPane.add(asteaDown);

		JSeparator separator_0 = new JSeparator();
		separator_0.setBounds(10, 600, 324, 5);
		contentPane.add(separator_0);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 540, 324, 5);
		contentPane.add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(9, 405, 324, 5);
		contentPane.add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(10, 345, 324, 5);
		contentPane.add(separator_3);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(10, 200, 324, 5);
		contentPane.add(separator_4);

		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(10, 165, 324, 5);
		contentPane.add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(10, 690, 324, 5);
		contentPane.add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(10, 285, 324, 5);
		contentPane.add(separator_7);

		this.dataBackupCheckBox.addActionListener(this);
		this.dataBackupComboBox.addActionListener(this);
		this.reimageCheckBox.addActionListener(this);
		this.freeOfChargeCheckBox.addActionListener(this);
		this.avBundleCheckBox.addActionListener(this);
		this.diagComboBox.addActionListener(this);
		this.warrantyComboBox.addActionListener(this);
		this.clearForm.addActionListener(this);
		this.copyToClipboardButton.addActionListener(this);
		this.serviceComboBox.addActionListener(this);
		this.originallyPurchasedCheckBox.addActionListener(this);
		this.asteaDown.addActionListener(this);
		this.adGui.confirm.addActionListener(this);
		this.adGui.cancel.addActionListener(this);
		statusNeutral("");
	}

	private ComboBoxModel<String> getModelOfServiceBox() {
		String[] items = new String[this.services.size()+1];
		items[0] = "Quick add...";
		for(int i = 0 ; i < services.size(); i++){
			items[i+1] = services.get(i).toString();
		}
		ComboBoxModel<String> model = new DefaultComboBoxModel<String>(items);
		return model;
	}
	
	private ComboBoxModel<String> getModelOfOsBox() {
		String[] items = new String[this.osList.size()+1];
		items[0] = "";
		for(int i = 0 ; i < osList.size(); i++){
			items[i+1] = osList.get(i).toString();
		}
		ComboBoxModel<String> model = new DefaultComboBoxModel<String>(items);
		return model;
	}
	
	private ComboBoxModel<String> getModelOfBackupBox() {
		String[] items = new String[this.backupList.size()+2];
		items[0] = "Supplied Drive";
		items[1] = "Non-service Drive";
		for(int i = 0 ; i < backupList.size(); i++){
			items[i+2] = backupSizesList.get(i);
		}
		ComboBoxModel<String> model = new DefaultComboBoxModel<String>(items);
		return model;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent event) {
		statusNeutral("");
		Object source = event.getSource();
		if(source == this.dataBackupCheckBox){
			boolean selected = this.dataBackupCheckBox.isSelected();
			this.dataBackupComboBox.setEnabled(selected);
			this.dataBackupComboBox.setForeground(selected ? Color.BLACK : Color.LIGHT_GRAY);
			this.dataBackupComboBox.setBackground(selected ? Color.WHITE : null);
		}else if (source == this.dataBackupComboBox) {
			if (this.dataBackupComboBox.getSelectedIndex() == 1) {
				this.driveCost.setEnabled(true);
				this.lblCost.setForeground(Color.BLACK);
				this.driveSku.setEnabled(true);
				this.lblSku.setForeground(Color.BLACK);
			}
			else {
				this.driveCost.setText("");
				this.driveSku.setText("");
				this.driveCost.setEnabled(false);
				this.driveCost.setBackground(Color.WHITE);
				this.lblCost.setForeground(Color.LIGHT_GRAY);
				this.driveSku.setEnabled(false);
				this.driveSku.setBackground(Color.WHITE);
				this.lblSku.setForeground(Color.LIGHT_GRAY);
			}
		}else if(source == this.avBundleCheckBox){
			boolean selected = this.avBundleCheckBox.isSelected();
			this.avBundleCostField.setEnabled(selected);
			this.avBundleNameBox.setEnabled(selected);
			this.avCostLabel.setForeground(selected ? Color.BLACK : Color.LIGHT_GRAY);
			this.avBundleNameBox.setForeground(selected ? Color.BLACK : Color.LIGHT_GRAY);
			this.avBundleCostField.setForeground(selected ? Color.BLACK : Color.LIGHT_GRAY);
		}else if(source == this.diagComboBox){
			int selected = this.diagComboBox.getSelectedIndex();
			this.diagReceiptField.setText("");
			switch (selected) {
				case 2:
					this.diagReceiptLabel.setText("Paid Receipt:");
					this.diagReceiptLabel.setForeground(requiredColor);
					this.diagReceiptField.setEnabled(true);
					break;
				case 4:
					this.diagReceiptLabel.setText("Approved by:");
					this.diagReceiptLabel.setForeground(requiredColor);
					this.diagReceiptField.setEnabled(true);
					break;
				default: 
					this.diagReceiptLabel.setText("Paid Receipt:");
					this.diagReceiptLabel.setForeground(Color.LIGHT_GRAY);
					this.diagReceiptField.setEnabled(false);
					break;
			}
		}else if(source == this.clearForm){
			stopTableEdits();
			clearForm();
		}else if(source == this.copyToClipboardButton){
			stopTableEdits();
			copyToClipBoard();
			if(this.statusDisplay.getText().trim().isEmpty()){
				statusGood("Successfully completed the copy to your clipboard.");
			}
		}else if(source == this.reimageCheckBox){
			boolean selected = this.reimageCheckBox.isSelected();
			this.freeOfChargeCheckBox.setEnabled(selected);
			this.freeOfChargeCheckBox.setForeground(selected ? Color.BLACK : Color.LIGHT_GRAY);
			this.freeOfChargeCheckBox.setSelected(false);
		}else if(source == this.serviceComboBox){
			quickAddServiceBox();
		}else if(source == this.quoteComboBox){
			int indexSelected = this.quoteComboBox.getSelectedIndex();
			this.daysToBenchField.setText("");
			switch (indexSelected){
			case 0:
				this.daysToBenchField.setForeground(Color.LIGHT_GRAY);
				this.daysToBenchLabel.setForeground(Color.LIGHT_GRAY);
				this.daysToBenchField.setEnabled(false);
				break;
			case 1: 
				this.daysToBenchField.setForeground(Color.BLACK);
				this.daysToBenchLabel.setForeground(requiredColor);
				this.daysToBenchField.setEnabled(true);
				break;
			case 2: 
				this.daysToBenchField.setForeground(Color.LIGHT_GRAY);
				this.daysToBenchLabel.setForeground(Color.LIGHT_GRAY);
				this.daysToBenchField.setEnabled(false);
				break;
			case 3: 
				this.daysToBenchField.setForeground(Color.BLACK);
				this.daysToBenchLabel.setForeground(requiredColor);
				this.daysToBenchField.setEnabled(true);
				this.daysToBenchField.setText("2");
				break;
			case 4:
				this.daysToBenchField.setForeground(Color.LIGHT_GRAY);
				this.daysToBenchLabel.setForeground(Color.LIGHT_GRAY);
				this.daysToBenchField.setEnabled(false);
				break;
			}
		}
		else if(source == this.originallyPurchasedCheckBox) {
			boolean selected = this.originallyPurchasedCheckBox.isSelected();
			this.warrantyReceiptField.setEnabled(selected);
			this.warrantyReceiptLabel.setForeground(selected ? Color.BLACK : Color.LIGHT_GRAY);
			this.warrantyReceiptField.setText("");
		}
		
		if (source == this.asteaDown && this.asteaDown.isSelected()) {
			adGui.setVisible(true);
		}
		else if (source == this.asteaDown && !this.asteaDown.isSelected()){
			adGui.setVisible(false);
		}
		
		if (source == this.adGui.cancel) {
			adGui.setVisible(false);
			this.asteaDown.setSelected(false);
		}
	}

	private void quickAddServiceBox() {
		int selectedIndex = this.serviceComboBox.getSelectedIndex();
		this.serviceComboBox.setSelectedIndex(0);

		if(selectedIndex > 0){
			addToTable(services.get(selectedIndex-1));
		}

	}
	
	private Boolean isBlacklisted(String id) {
		Boolean result = false;
		String idLower = id.toLowerCase();
		for (int i = 0; i < blackList.size(); i++) {
			if (idLower.equals(blackList.get(i))) {
				result = true;
			}
		}
		return result;
	}

	private void addToTable(Service service) {
		stopTableEdits();
		this.extraCostsTable.clearSelection();
		TableModel model = this.extraCostsTable.getModel();
		int rowCount = model.getRowCount();
		boolean added = false;
		for(int r = 0 ; r < rowCount  && !added; r++){
			boolean empty = true;
			Object input = model.getValueAt(r, 0);
			if(input != null){
				if(input instanceof String){
					String inputString = (String) input;
					if(!inputString.trim().isEmpty()){
						empty = false;
					}
				}
			}
			if(empty){
				model.setValueAt(service.getName(), r, 0);
				model.setValueAt(service.getPrice(), r, 1);
				added = true;
			}
		}
		this.extraCostsTable.setModel(model);

	}

	private void stopTableEdits() {
		try{
			this.extraCostsTable.getCellEditor().stopCellEditing();
		}catch(Exception e){

		}
	}

	private void copyToClipBoard() {
		if(allFieldsDone()){
			String result = generateReport();
			if (this.asteaDown.isSelected()) {
				try {
					generateTextFile(result);
				}
				catch (Exception E) {
					
				}
			}
			StringSelection stringSelection = new StringSelection(result);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
		}
	}

	private boolean allFieldsDone() {
		//Clear "no services selected error"
		this.diagComboBox.setBackground(Color.WHITE);
		this.extraCostsTable.setBackground(Color.WHITE);
		this.dataBackupCheckBox.setBackground(null);
		this.reimageCheckBox.setBackground(null);
		
		boolean result = true;
		String name = this.customerName.getText().trim();
		if(name.isEmpty()){
			statusError("Please enter customer's name.");
			this.customerName.setBackground(badFormColor);
			result = false;
		}else{
			this.customerName.setBackground(Color.WHITE);
		}

		String primaryPhone = this.primaryPhone.getText().trim();
		if(primaryPhone.isEmpty() || primaryPhone.length() < 6 ||( primaryPhone.length() != 7 && primaryPhone.length() < 10)){
			statusError("Please enter customer's primary phone number correctly.");
			this.primaryPhone.setBackground(badFormColor);
			result = false;
		}else{
			this.primaryPhone.setBackground(Color.WHITE);
		}
		
		String secondaryPhoneOrEmail = this.secondaryPhoneOrEmail.getText().trim();
		if (!secondaryPhoneOrEmail.isEmpty() && secondaryPhoneOrEmail != null) {		
			Boolean isPhoneNumber = true;
			for (int i = 0; i < secondaryPhoneOrEmail.length(); i++) {
				if (!Character.isDigit(secondaryPhoneOrEmail.charAt(i))) {
					isPhoneNumber = false;
					break;
				}
			}
			
			if (isPhoneNumber == true) {
				if(secondaryPhoneOrEmail.isEmpty() || secondaryPhoneOrEmail.length() < 6 ||( secondaryPhoneOrEmail.length() != 7 && secondaryPhoneOrEmail.length() < 10)){
					statusError("Please enter customer's sedondary form of contact correctly.");
					this.secondaryPhoneOrEmail.setBackground(badFormColor);
					result = false;
				}else{
					this.secondaryPhoneOrEmail.setBackground(Color.WHITE);
				}
			}
			else {
				if (secondaryPhoneOrEmail.contains("@") && (secondaryPhoneOrEmail.contains(".com") || secondaryPhoneOrEmail.contains(".edu") || secondaryPhoneOrEmail.contains(".org") || secondaryPhoneOrEmail.contains(".gov") || secondaryPhoneOrEmail.contains(".net"))) {
					this.secondaryPhoneOrEmail.setBackground(Color.WHITE);
				}
				else {
					statusError("Please enter customer's secondary form of contact correctly.");
					this.secondaryPhoneOrEmail.setBackground(badFormColor);
					result = false;
				}
			}
		}

		if(this.avBundleCheckBox.isSelected()){
			String bundle = this.avBundleNameBox.getText().trim();
			String cost = this.avBundleCostField.getText().trim();
			if(bundle.isEmpty()){
				statusError("Please enter the name of the av bundle.");
				this.avBundleNameBox.setBackground(badFormColor);
				result = false;	
			}else{
				this.avBundleNameBox.setBackground(Color.WHITE);
			}
			if(cost.isEmpty()){
				statusError("Please enter the cost for the av bundle.");
				this.avBundleCostField.setBackground(badFormColor);
				result = false;	
			}else{
				this.avBundleCostField.setBackground(Color.WHITE);
			}
		}

		String daysToBench = this.daysToBenchField.getText().trim();

		try{
			daysToBench.charAt(0);
			Integer.parseInt(daysToBench);
			this.daysToBenchField.setBackground(Color.WHITE);
		}catch(Exception e){
			int index = this.quoteComboBox.getSelectedIndex();
			if(index != 2 && index != 4){
				this.daysToBenchField.setBackground(badFormColor);
				statusError("Please enter the days to contact correctly.");
				result = false;
			}
		}

		String user = this.userIDField.getText().trim();
		if(user.isEmpty()){
			this.userIDField.setBackground(badFormColor);
			statusError("Please enter your userID above.");
			result = false;
		}else{
			Boolean blackListed = isBlacklisted(user);
			if (blackListed) {
				this.userIDField.setBackground(badFormColor);
				result = false;
				statusError("You are not ready to use this tool yet.");
			}
			else {
				this.userIDField.setBackground(Color.WHITE);
			}
		}

		String actionsNeeded = this.requestedActionsField.getText().trim();
		if(actionsNeeded.isEmpty()){
			this.requestedActionsField.setBackground(badFormColor);
			statusError("Enter the actions needed to be taken!");
			result = false;
		}else{
			this.requestedActionsField.setBackground(Color.WHITE);
		}

		int diagIndex = this.diagComboBox.getSelectedIndex();
		if(diagIndex == 0){
			this.diagComboBox.setBackground(badFormColor);
			result = false;
			statusError("Please indicate if we are doing diag tests.");

		}else{
			this.diagComboBox.setBackground(Color.WHITE);
		}

		int warrantyIndex = this.warrantyComboBox.getSelectedIndex();
		if(warrantyIndex == 0){
			this.warrantyComboBox.setBackground(badFormColor);
			result = false;
			statusError("Please indicate if there is a warranty.");

		}else{
			this.warrantyComboBox.setBackground(Color.WHITE);
		}

		int quoteIndex = this.quoteComboBox.getSelectedIndex();
		if(quoteIndex == 0){
			this.quoteComboBox.setBackground(badFormColor);
			result = false;
			statusError("Please indicate the quoted time to contact.");

		}else{
			this.quoteComboBox.setBackground(Color.WHITE);
		}

		if(this.reimageCheckBox.isSelected()){
			if(this.osComboBox.getSelectedIndex() == 0){
				this.osComboBox.setBackground(badFormColor);
				result = false;
				statusError("Indicate the operating system for reimage.");
			}else{
				this.osComboBox.setBackground(Color.WHITE);
			}
		}else{
			this.osComboBox.setBackground(Color.WHITE);
		}


		if(!this.dataPolicyCheckbox.isSelected()){
			this.dataPolicyCheckbox.setBackground(badFormColor);
			result = false;
			statusError("Inform customer of data loss policy!");
		}else{
			this.dataPolicyCheckbox.setBackground(Color.LIGHT_GRAY);
		}
		
		if(this.dataBackupComboBox.getSelectedIndex() == 1) {
			try {
				Double.parseDouble(this.driveCost.getText());
				this.driveCost.setBackground(Color.WHITE);
			}
			catch (Exception e){
				this.driveCost.setBackground(badFormColor);
				result = false;
				statusError("Enter the correct cost of the non-service drive!");
			}
			try {
				Integer.parseInt(this.driveSku.getText());
				this.driveSku.setBackground(Color.WHITE);
			}
			catch (Exception e){
				this.driveSku.setBackground(badFormColor);
				result = false;
				statusError("Enter the correct SKU of the non-service drive!");
			}
		}
		else {
			this.driveCost.setBackground(Color.WHITE);
			this.driveSku.setBackground(Color.WHITE);
		}

		if(this.originallyPurchasedCheckBox.isSelected()){
			String receipt = this.warrantyReceiptField.getText();
			
			if(receipt != null){
				receipt = receipt.trim();
				if(receipt.isEmpty()){
					this.warrantyReceiptField.setBackground(badFormColor);
					result = false;
					statusError("Indicated it was purchased here, add the receipt.");
				}else if (!checkReceipt(receipt)) {
					this.warrantyReceiptField.setBackground(badFormColor);
					result = false;
					statusError("Please input the receipt correctly.");
				}
				else{
					this.warrantyReceiptField.setBackground(Color.WHITE);
				}
			}else{
				this.warrantyReceiptField.setBackground(badFormColor);
				result = false;
				statusError("Indicated it was purchased here, add the receipt.");
			}
		}else{
			this.warrantyReceiptField.setBackground(Color.WHITE);
		}

		int diagSelection = this.diagComboBox.getSelectedIndex();
		if(diagSelection == 2){
			String receipt = this.diagReceiptField.getText().trim();
			if(receipt.isEmpty()){
				this.diagReceiptField.setBackground(badFormColor);
				statusError("Please input a receipt for the diag.");
				result = false;
			}else{
				if (!checkReceipt(receipt)) {
					this.diagReceiptField.setBackground(badFormColor);
					statusError("Enter the diagnostic receipt properly!");
					result = false;
				}
				else {
					this.diagReceiptField.setBackground(Color.WHITE);
				}
				this.diagComboBox.setBackground(Color.WHITE);
			}
		}
		else if(diagSelection == 4) {
			String approvedBy = this.diagReceiptField.getText().trim();
			if(approvedBy.isEmpty()){
				this.diagReceiptField.setBackground(badFormColor);
				statusError("Please input who authorized the 1C free diagnostic.");
				result = false;
			}else{
				this.diagReceiptField.setBackground(Color.WHITE);
			}
		}
		
		if (result) {
			register.Reset();
			collectNonTablePrices();
			collectTablePrice();
			if (register.numOfServices() < 1) {
				this.diagComboBox.setBackground(badFormColor);
				this.extraCostsTable.setBackground(badFormColor);
				this.dataBackupCheckBox.setBackground(badFormColor);
				this.reimageCheckBox.setBackground(badFormColor);
				statusError("Please indicate one or more services to be performed.");
				
				result = false;
			}
		}
		
		if (this.asteaDown.isSelected()) {
			if (this.adGui.isVisible()) {
				result = false;
				statusError("Please fill out the additional info due to Astea being down");
				this.adGui.setVisible(true);
			}
		}
		return result;
	}

	private String generateConfirmationMessage() {
		String result = "";
		result = "Please confirm the following information with the customer:\n\n";

		String primaryNumber = formatPhoneNumber(primaryPhone.getText().trim());
		if(primaryNumber != null && !primaryNumber.isEmpty()){
			result += "Primary Number: " + primaryNumber + "\n";
		}

		String secondaryNumber = formatPhoneNumber(secondaryPhoneOrEmail.getText().trim());
		if(secondaryNumber != null && !secondaryNumber.isEmpty()){
			result += "Secondary Form of Contact: " + secondaryNumber + "\n";
		}
		
		String password = this.passwordField.getText();
		if(password != null && !password.isEmpty()){
			result += "Password (Capitalization): \"" + password + "\"";
		}
		
		if (this.asteaDown.isSelected()) {
			result += "\n\n **Please verify that the customer has a complete account present in the POS.**";
		}

		String costReport = register.priceReport();
		result += "\n\n\n" + costReport;

		return result;
	}

	//BUILDING METHODS
	/*
	 * Customer's Name --- Phone Number | Secondary Number
	 * User:
	 * Pass:
	 * Customer authorizes only himself for pickup.
	 * Customer understands data loss policy, and requests/declines backup.
	 * Customer requests AV bundle.
	 * Customer reports ...
	 * Customer requests ...
	 * We are/aren't running diagnostic tests
	 * Diag tests were paid on:
	 * Quoted.... blank blank blank
	 * The following was verified at the Knowledge Bar:
	 * Warranty purchased on:
	 * Technician will call to introduce themselves by 8PM on 09/15
	 * CStamper
	 */
	private String generateReport() {
		String result = "";
		
		if (this.asteaDown.isSelected()) {
			result += "Request type: " + this.adGui.getRequestType();
			if (this.originallyPurchasedCheckBox.isSelected()) {
				result += " | Tag: " + this.warrantyReceiptField.getText();
			}
			result += "\n\n";
		}

		result += customerContactInformation();
		result += loginInformation();
		result += pickupAuthorizationInformation();
		result += "\n";
		
		result += issuesReportedInformation();
		result += issuesVerifiedInformation();
		result += customerRequestsInformation();
		result += diagnosticTestInformation();
		result += warrantyInformation();
		result += "\n";

		result += dataBackupInformation();
		result += antivirusInformation();
		result += "\n";
		
		result += "Total cost: ";
		result += quotedTable();

		result += technicianContactInformation();

		result += "This repair order was originally created by: ";
		result += knowledgeExpertInformation();
		result += "\n";
		
		if (this.asteaDown.isSelected()) {
			result += "\nRecieved items: \n";
			Queue<String> recievedList = this.adGui.getRecievedList();
			for (int i = 0; i < recievedList.length(); i++) {
				result += recievedList.dequeue() + "\n";
			}
			result += "\n\n\n\n\n\n";
			result += "Signature:__________________________________________________________\n";
		}
		else {
			result += "_";
		}

		boolean confirmed = confirmWithKE();

		return confirmed ? result : "";
	}

	private boolean confirmWithKE() {
		String message = generateConfirmationMessage();
		int popUpResult = JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION);
		if(popUpResult != JOptionPane.YES_OPTION){
			statusError("You Indicated a mismatch in the confirmation, please fix it");
			return false;
		}
		return true;
	}

	private String issuesVerifiedInformation() {
		String result = "";
		String verifiedIssues = this.verifiedIssuesField.getText().trim();
		if(verifiedIssues.isEmpty()){
			result += "There were no issues verified at Knowledge Bar.";
		}else{
			result += "Issues Verified at Knowledge Bar: ";
			result += verifiedIssues;
		}
		result += "\n";
		return result;
	}
	
	private Boolean importConfig(XMLTree inputFile) {
		Boolean serviceBool = false;
		Boolean defaultBool = false;
		Boolean osBool = false;
		for (int i = 0; i < inputFile.numberOfChildren(); i++) {
			XMLTree child = inputFile.child(i);
			String childLabel = child.label();
			if (childLabel.equals("defaults")) {
				defaultBool = importDefaults(child);
			}
			else if (childLabel.equals("osList")) {
				osList = importOsList(child);
				if (osList.size() > 0) {
					osBool = true;
				}
			}
			else if (childLabel.equals("serviceList")) {
				services = importServiceList(child);
				if (services.size() > 0) {
					serviceBool = true;
				}
			}
			else if (childLabel.equals("blackList")) {
				blackList = importBlackList(child);
			}
		}
		return (serviceBool && defaultBool && osBool);
	}
	
	private List<Service> importServiceList(XMLTree serviceXML) {
		List<Service> list = new ArrayList<Service>();
		for (int i = 0; i < serviceXML.numberOfChildren(); i++) {
			Service item;
			XMLTree child = serviceXML.child(i);
			String name = child.attributeValue("name");
			Double price = Double.parseDouble(child.attributeValue("price"));
			if (child.hasAttribute("short")) {
				String shortName = child.attributeValue("short");
				int sku = Integer.parseInt(child.attributeValue("sku"));
				item = new Service(name, price, shortName, sku);
			}
			else {
				item = new Service(name, price);
			}
			list.add(item);
		}
		return list;
	}
	
	private List<String> importOsList(XMLTree osXML) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < osXML.numberOfChildren(); i++) {
			list.add(osXML.child(i).attributeValue("name"));
		}
		return list;
	}

	private Boolean importDefaults(XMLTree defaultXML) {
		for (int i = 0; i < defaultXML.numberOfChildren(); i++) {
			XMLTree child = defaultXML.child(i);
			String label = child.label();
			if (label.equals("os")) {
				if (child.attributeValue("name").equals("windows")) {
					reimageWinPrice = Double.parseDouble(child.attributeValue("price"));
				}
				else if (child.attributeValue("name").equals("os x")) {
					reimageMacPrice = Double.parseDouble(child.attributeValue("price"));
				}
			}
			else if (label.equals("backupLabor")) {
				backupLabor = Double.parseDouble(child.attributeValue("price"));
			}
			else if (label.equals("hddDiscount")) {
				hddDiscount = Double.parseDouble(child.attributeValue("price"));
			}
			else if (label.equals("drive")) {
				String size = child.attributeValue("size");
				Double price = Double.parseDouble(child.attributeValue("price"));
				backupList.add(new Service("Successful data backup to a " + size + " service drive", backupLabor + price, -1, "", "or $9.99 for a failed backup "));
				backupSizesList.add(size + " service drive (+$" + price + ")");
			}
		}
		return true;
	}
	
	private List<String> importBlackList(XMLTree blackListXML) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < blackListXML.numberOfChildren(); i++) {
			XMLTree child = blackListXML.child(i);
			String name = child.attributeValue("name");
			list.add(name);
		}
		return list;
	}
	
	private String quotedTable() {
		String result = "\n";
		double subTotal = register.total();
		String subTotalString = String.format("$%.2f", subTotal);
		double tax = subTotal * 0.075;
		String taxString = String.format("$%.2f", tax);
		double total = subTotal + tax;
		String totalString = String.format("$%.2f", total);
		result += register.condensedReport();
		result += "Subtotal: " + subTotalString + " | Tax: " + taxString + " | Total: " + totalString;
		result += "\n\n";
		return result;
	}

	private void collectTablePrice() {
		TableModel model = this.extraCostsTable.getModel();
		int rows = model.getRowCount();
		for(int i = 0 ; i < rows ; i++){
			try{
				String itemName = (model.getValueAt(i, 0).toString()).trim();
				if(!itemName.isEmpty()){
					double price = getPrice(model.getValueAt(i, 1).toString().trim());
					Object receiptObj = model.getValueAt(i, 2);
					String receipt = null;
					if(receiptObj != null && receiptObj instanceof String){
						 receipt = (String) model.getValueAt(i, 2);
						if(receipt != null){
							receipt = receipt.trim();
						}
					}
					Service currentService = new Service(itemName, price, -1, receipt == null ? "" : receipt);
					register.addService(currentService);
				}
			}catch(Exception e){
			}
		}
	}
	
	private final Service dataBackup = new Service("Successful data backup", 99.99, -1, "", "or $9.99 for a failed backup ");
	
	private void collectNonTablePrices() {

		//DATA BACKUP
		if(this.dataBackupCheckBox.isSelected()){
			//Supplied Drive
			int index = this.dataBackupComboBox.getSelectedIndex();
			if (index == 0) {
				register.addService(dataBackup);
			}
			//Non-service Drive
			else if (index == 1) {
				register.addService(dataBackup);
				int sku = Integer.parseInt(this.driveSku.getText());
				String deviceName = new String("Non-Service Drive (" + sku + ")");
				double deviceCost = Double.parseDouble(this.driveCost.getText());
				Service nonServiceDrive = new Service (deviceName, deviceCost, "Non-Service Drive", sku);
				register.addService(nonServiceDrive);				
			}	
			//Service Drive
			else if (index > 1 && index < backupList.size() + 1) {
				register.addService(backupList.get(index - 2));
			}
			
		}

		//REIMAGE
		if (this.reimageCheckBox.isSelected()) {
			String operatingSystem = (String) this.osComboBox.getSelectedItem();
			operatingSystem = operatingSystem.trim();

			if (this.freeOfChargeCheckBox.isSelected()){
				Service reimageFree = new Service("Free Reimage", 0.00);
				register.addService(reimageFree);
			} else if (operatingSystem.contains("OSX") || operatingSystem.contains("macOS")){ 
				Service reimageOSX = new Service("Reimage", reimageMacPrice, -1, "", " to " + operatingSystem);
				register.addService(reimageOSX);
			} else if (tableContains("HDD/SSD Upgrade")) {
				Service reimageWithHDD = new Service("Reimage", reimageWinPrice - hddDiscount, -1, "", " to " + operatingSystem + " *-$10.00 discount with HDD/SSD install* ");
				register.addService(reimageWithHDD);
			} else {
				Service reimage = new Service("Reimage", reimageWinPrice, -1, "", "to " + operatingSystem + " ");
				register.addService(reimage);
			}
		}

		//AV BUNDLE
		if(this.avBundleCheckBox.isSelected()){
			double price = 0;
			try{
				price = getPrice(this.avBundleCostField.getText());
			}catch(Exception e){
				statusError("Please enter a cost for the AV bundle.");
			}
			Service avBundle = new Service(avBundleNameBox.getText().trim(), price);
			register.addService(avBundle);
		}


		int diagIndex = this.diagComboBox.getSelectedIndex();
		if(diagIndex > 1){
			Service result;
			if(diagIndex > 2){
				String extraInfo = "";
				int selectedIndex = this.warrantyComboBox.getSelectedIndex();
				switch(selectedIndex){
				case 2:
					extraInfo += " per accidental warranty";
					break;
				case 3:
					extraInfo += " per extended warranty";
					break;
				case 4:
					extraInfo += " per manufacturer's warranty";
					break;
				}
				if (diagIndex == 4) {
					extraInfo = "(Approved by " + this.diagReceiptField.getText() + ")";
				}
				result = new Service("Diagnostic tests", 0.00, -1, "", extraInfo);
			}else{
				result = new Service("Diagnostic tests", 39.99, -1, this.diagReceiptField.getText().trim(), "");
			}
			register.addService(result);
		}
	}

	private boolean tableContains(String string) {
		boolean result = false;
		TableModel model = this.extraCostsTable.getModel();
		int rows = model.getRowCount();
		for(int i = 0 ; i < rows ; i++){
			try{
				String itemName = (model.getValueAt(i, 0).toString()).trim();
				if(itemName.contains(string)){
					result = true;
				}
			}catch(Exception e){
			}
		}
		return result;
	}

	private double getPrice(String text) throws Exception{
		double result = 0;
		result = Double.parseDouble(text);
		return result;
	}

	private String knowledgeExpertInformation() {
		String result = "";
		result += this.userIDField.getText();
		return result;
	}

	private String technicianContactInformation() {
		String result = "";
		int selectedIndex = this.quoteComboBox.getSelectedIndex();
		switch(selectedIndex){
		case 1:
			int daysDelay1 = getDaysToBench();
			result += "Technician will contact customer no later than ";
			result += generateDate(daysDelay1);
			result += ".";
			break;
		case 2:
			result += "Quoted front of line, technician will contact customer no later than ";
			result += generateDate(2);
			result += ".";
			break;
		case 3:
			int daysDelay2 = getDaysToBench();
			result += "Quoted straight to technician, customer will receive further contact by ";
			result += generateDate(daysDelay2);
			result += ".";
			break;
		case 4:
			result += "Quoted 1 to 2 weeks till customer will receive further contact in order to ship device out.";
			break;
		}
		result += "\n";
		return result;
	}

	private int getDaysToBench() {
		String field = this.daysToBenchField.getText();
		int result = 0;
		try{
			result = Integer.parseInt(field);
		}catch(Exception e){
			statusError("Please Enter Days To Contact");
		}
		return result;
	}

	private String generateDate(int daysDelay) {
		String result = new String();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, daysDelay);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, getLocale());
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, getLocale());
		int year = cal.get(Calendar.YEAR);
		String fullDate = String.format("%s %s %02d, %d", dayOfWeek, month, dayOfMonth, year);
		if (dayOfWeek.equals("Sunday")) {
			 result = "6PM on " + fullDate;
		}
		else {
			result = "8PM on " + fullDate;
		}
		return result;
	} 

	private String warrantyInformation() {
		String result = "";
		int index = this.warrantyComboBox.getSelectedIndex();
		String receipt = this.warrantyReceiptField.getText().trim();
		switch(index){
		case 1:
			result += "There is no eligible warranty, or no warranty applies to the requested repairs";
			result += receipt.isEmpty() ? "." : " for the machine was purchased on this receipt: " + receipt;
			break;
		case 2:
			result += "There is an accidental plan ";
			result += receipt.isEmpty() ? "which was purchased with the machine attached to this workorder." : "which was purchased on this receipt: " + receipt;
			break;
		case 3:
			result += "There is a non-accidental warranty ";
			result += receipt.isEmpty() ? "which was purchased with the machine attached to this workorder." : "which was purchased on this receipt: " + receipt;
			break;
		case 4:
			result += "There is a manufacturer's warranty ";
			result += receipt.isEmpty() ? "for the machine attached to this workorder." : "for the machine purchased on this receipt: " + receipt;
			break;
		}
		result += "\n";
		return result;
	}

	private String diagnosticTestInformation() {
		String result = "";
		int index = this.diagComboBox.getSelectedIndex();
		if(index > 1){
			result += "We are running diagnostic tests on the device";
			if(index == 2){
				String diagReceipt = this.diagReceiptField.getText().trim();
				result += diagReceipt.isEmpty() ? " which were not paid for." : " which were paid for on " + diagReceipt + ".";
			}else{
				result += " which were quoted free.";
			}
		}else{
			result += "We are not running diagnostic tests on the device, customer understands this work is not guaranteed to resolve any issues.";
		}
		result += "\n";
		return result;
	}

	private Object customerRequestsInformation() {
		String result = "";
		result += "Customer has requested the following actions: ";
		String actions = this.requestedActionsField.getText().trim();
		result += actions.isEmpty() ? "no requested actions" : actions;
		result += ".\n";
		return result;
	}

	private String issuesReportedInformation() {
		String result = "";
		result += "Customer has reported the following issues: ";
		String issues = this.reportedIssuesField.getText().trim();
		result += issues.isEmpty() ? "no known issues" : issues;
		result += ".\n";
		return result;
	}

	private String antivirusInformation() {
		String result = "";
		result += "Customer ";
		if(this.avBundleCheckBox.isSelected()){
			result += "requests Anti-Virus bundle [ ";
			result += this.avBundleNameBox.getText().trim();
			result += " ] for this device.";
		}else{
			result += "declines Anti-Virus bundle for this device.";
		}
		result += "\n";
		return result;
	}

	private String dataBackupInformation() {
		String result = "";
		result += "Customer understands our data loss policy, and ";
		if(this.dataBackupCheckBox.isSelected()){
			result += "requests data backup ($99.99) from the device ";
			result += dataBackupComboBox.getSelectedItem().toString();
			result += ".";
		}else{
			result += "declines data backup for this device.";
		}
		result += "\n";
		return result;
	}

	private String pickupAuthorizationInformation() {
		String result = "";
		String pickupNames = this.altPickupName.getText().trim();
		result += "Customer authorizes ";
		result += pickupNames.isEmpty() ? "only themselves"  : pickupNames + " and themselves";
		result += " for pickup of the device.";
		result += "\n";
		return result;
	}

	private String loginInformation() {
		String result = "";
		String user = this.usernameField.getText().trim();
		String pass = this.passwordField.getText().trim();
		result += "Username: ";
		result += user.isEmpty() ? "Default" : user;
		result += " | ";
		result += "Password: ";
		result += pass.isEmpty() ? "None or Not Specified" : pass;
		if (!osComboBox.getSelectedItem().equals("")) {
			result += " | ";
			result += "OS: ";
			result += osComboBox.getSelectedItem();
		}
		result += "\n";
		return result;
	}

	private String customerContactInformation() {
		String result = "";
		result += this.customerName.getText().trim();
		result += " --- ";
		result += formatPhoneNumber(this.primaryPhone.getText().trim());
		String secondary = this.secondaryPhoneOrEmail.getText().trim();
		if(!secondary.isEmpty() && !secondary.equals("") && secondary != null){
			result += " | ";
			result += formatPhoneNumber(secondary);
		}
		result += "\n";
		return result;
	}

	private final String NUMBERS = "1234567890";

	private String formatPhoneNumber(String originalInput) {
		String result = "";
		String justNumbers = "";

		if (originalInput.contains("@") && (originalInput.contains(".com") || originalInput.contains(".edu") || originalInput.contains(".org") || originalInput.contains(".gov"))) {
			result = originalInput;
		}
		
		else {
			for(int i = 0 ; i < originalInput.length() ; i++){
				char currentChar = originalInput.charAt(i);
				if(NUMBERS.indexOf(currentChar) != -1){
					justNumbers = justNumbers + currentChar;
				}
			}


			for(int i = 0 ; i < justNumbers.length() ; i++){
				result += justNumbers.charAt(i);
				if(i % 3 == 2){
					result += ".";
					if(justNumbers.length() - i <=5){
						i++;
						while(justNumbers.length() > i){
							result += justNumbers.charAt(i);
							i++;
						}
						break;
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
	private Boolean checkReceipt (String receipt) {
		Boolean isReceipt = true;
		int firstHyphen = receipt.indexOf('-');
		int secondHyphen = receipt.indexOf('-', firstHyphen + 1);
		
		String store = new String();
		String dept = new String();
		try {
			store = receipt.substring(0, firstHyphen);
			dept = receipt.substring(firstHyphen + 1, secondHyphen);
		}
		catch (Exception e) {
			isReceipt = false;
		}
		
		if (isReceipt) {
			String transaction = receipt.substring(secondHyphen + 1);
			
			try { 
				int storeCheck = Integer.parseInt(store);
				int transactionCheck = Integer.parseInt(transaction);
			}
			catch (Exception e) {
				isReceipt = false;
			}
			
			int storeLength = store.length();
			int transactionLength = transaction.length();
			if (!(storeLength == 3 || storeLength == 2)) {
				isReceipt = false;
			}
			if (!( transactionLength == 8 || transactionLength == 7)) {
				isReceipt = false;
			}
			
			dept = dept.toLowerCase();
			if (!(dept.equals("po") || dept.equals("ad") || dept.equals("se") || dept.equals("re") || dept.equals("wp") || dept.equals("wo") || dept.equals("ic"))) {
				isReceipt = false;
			}
		}		return isReceipt;
	}
	
	private void generateTextFile(String contents) {
		String customerName = this.customerName.getText();
		SimpleWriter out = new SimpleWriter1L("Checkins/" + customerName + ".txt");
		out.println(contents);
		out.close();
	}
	
	
	/*private void setChangingColors() {
		this.customerName.setBackground(Color.WHITE);
		this.primaryPhone.setBackground(Color.WHITE);
		this.secondaryPhoneOrEmail.setBackground(Color.WHITE);
		this.dataBackupComboBox.setBackground(null);
		this.driveCost.setBackground(Color.WHITE);
		this.lblCost.setForeground(Color.LIGHT_GRAY);
		this.driveSku.setBackground(Color.WHITE);
		this.lblSku.setForeground(Color.LIGHT_GRAY);
		this.reimageCheckBox.setBackground(null);
		this.osComboBox.setBackground(Color.WHITE);
		this.avBundleNameBox.setBackground(Color.WHITE);
		this.avBundleNameBox.setForeground(Color.LIGHT_GRAY);
		this.avBundleCostField.setBackground(Color.WHITE);
		this.avBundleCostField.setForeground(Color.LIGHT_GRAY);
		this.requestedActionsField.setBackground(Color.WHITE);
		this.warrantyComboBox.setBackground(Color.WHITE);
		this.diagReceiptField.setBackground(Color.WHITE);
		this.quoteComboBox.setBackground(Color.WHITE);

		this.daysToBenchField.setForeground(debug ? Color.BLACK : Color.LIGHT_GRAY);
		this.daysToBenchField.setBackground(Color.WHITE);
		this.daysToBenchLabel.setForeground(debug ? requiredColor : Color.LIGHT_GRAY);
		this.userIDField.setBackground(Color.WHITE);
		this.dataPolicyCheckbox.setBackground(Color.LIGHT_GRAY);
		this.diagComboBox.setBackground(Color.WHITE);
		this.extraCostsTable.setBackground(Color.WHITE);
		this.dataBackupCheckBox.setBackground(null);
		this.warrantyReceiptField.setEnabled(false);
		this.warrantyReceiptLabel.setForeground(Color.LIGHT_GRAY);
		this.diagReceiptLabel.setForeground(Color.LIGHT_GRAY);
		

	}
*/	
	//CLEARING METHODS
	private void clearForm() {
		this.customerName.setText(debug ? "Ryan Cluff" : "");
		this.primaryPhone.setText(debug ? "7403418456" : "");
		this.secondaryPhoneOrEmail.setText(debug ? "7403896125" : "");
		this.usernameField.setText(debug ? "Ryan" : "");
		this.passwordField.setText(debug ? "Pa5sW0rd" : "");
		this.altPickupName.setText(debug ? "Lisa Cluff" : "");
		this.dataBackupCheckBox.setSelected(false);
		this.dataBackupComboBox.setSelectedIndex(0);
		this.dataBackupComboBox.setEnabled(false);
		this.driveCost.setEnabled(false);
		this.driveCost.setText("");
		this.driveCost.setBackground(Color.WHITE);
		this.lblCost.setForeground(Color.LIGHT_GRAY);
		this.driveSku.setEnabled(false);
		this.driveSku.setText("");
		this.driveSku.setBackground(Color.WHITE);
		this.lblSku.setForeground(Color.LIGHT_GRAY);
		this.avBundleCheckBox.setSelected(false);
		this.avBundleNameBox.setText(defaultAVBundle.getName());
		this.avBundleNameBox.setEnabled(false);
		this.avBundleCostField.setText(String.format("%.2f", defaultAVBundle.getPrice()));
		this.avBundleCostField.setEnabled(false);
		this.reportedIssuesField.setText(debug ? "All the issues" : "");
		this.requestedActionsField.setText(debug ? "All the actions" : "");
		this.requestedActionsField.setBackground(Color.WHITE);
		this.diagComboBox.setSelectedIndex(debug ? 3 : 0);
		this.diagReceiptField.setBackground(Color.WHITE);
		this.diagReceiptField.setText("");
		this.warrantyComboBox.setSelectedIndex(debug ? 1 : 0);
		this.warrantyComboBox.setBackground(Color.WHITE);
		this.warrantyReceiptField.setText("");
		this.quoteComboBox.setSelectedIndex(debug ? 1 : 0);
		this.quoteComboBox.setBackground(Color.WHITE);
		this.customerName.setBackground(Color.WHITE);
		this.primaryPhone.setBackground(Color.WHITE);
		this.dataBackupComboBox.setBackground(null);
		this.avBundleNameBox.setBackground(Color.WHITE);
		this.avBundleCostField.setBackground(Color.WHITE);
		this.daysToBenchField.setForeground(debug ? Color.BLACK : Color.LIGHT_GRAY);
		this.daysToBenchField.setBackground(Color.WHITE);
		this.daysToBenchField.setEnabled(debug);
		this.daysToBenchLabel.setForeground(debug ? requiredColor : Color.LIGHT_GRAY);
		this.userIDField.setBackground(Color.WHITE);
		this.userIDField.setText(debug ? "RCluff" : "");
		this.reimageCheckBox.setSelected(false);
		this.osComboBox.setSelectedIndex(0);
		this.verifiedIssuesField.setText(debug ? "All the verified" : "");
		this.serviceComboBox.setSelectedIndex(0);
		this.originallyPurchasedCheckBox.setSelected(false);
		this.dataPolicyCheckbox.setSelected(debug ? true : false);
		this.dataPolicyCheckbox.setBackground(Color.LIGHT_GRAY);
		this.daysToBenchLabel.setText(daysToBenchDefaultText);
		this.daysToBenchField.setText(debug ? "2" : lastDaysToBenchValue);
		this.freeOfChargeCheckBox.setEnabled(false);
		this.freeOfChargeCheckBox.setSelected(false);
		this.diagComboBox.setBackground(Color.WHITE);
		this.extraCostsTable.setBackground(Color.WHITE);
		this.dataBackupCheckBox.setBackground(null);
		this.reimageCheckBox.setBackground(null);
		this.warrantyReceiptField.setEnabled(false);
		this.warrantyReceiptLabel.setForeground(Color.LIGHT_GRAY);
		this.diagReceiptLabel.setForeground(Color.LIGHT_GRAY);
		this.adGui.recievedList.setBackground(Color.WHITE);
		this.adGui.requestTypeBox.setBackground(Color.WHITE);
		this.adGui.requestTypeBox.setSelectedIndex(0);
		this.asteaDown.setSelected(false);
		this.adGui.setVisible(false);
		
		clearTable();
	}

	private void clearTable() {
		this.extraCostsTable.clearSelection();
		TableModel model = this.extraCostsTable.getModel();
		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();
		for(int c = 0 ; c < columnCount ; c++){
			for(int r = 0 ; r < rowCount ; r++){
				model.setValueAt("", r, c);
			}
		}

	}

	private final Color badFormColor = new Color(255, 178, 178);
	private final Color requiredColor = new Color(0, 0, 180);
	private final Color goodColor = new Color(0, 130, 0);
	private final Color badColor = new Color(103, 0, 0);

	private void statusError(String error){
		this.statusDisplay.setBackground(badColor);
		error = "ERROR: " + error;
		this.statusDisplay.setText(error);
	}
	
	private JTextField requestedActionsField;
	private JTextArea verifiedIssuesField;

	private void statusGood(String message){
		this.statusDisplay.setBackground(goodColor);
		this.statusDisplay.setText(message);
	}

	private void statusNeutral(String message){
		this.statusDisplay.setBackground(Color.WHITE);
		this.statusDisplay.setText(message);
	}
}
