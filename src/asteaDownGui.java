import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import components.queue.Queue;
import components.queue.Queue1L;

public class asteaDownGui extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 8863507308713007595L;
	private final JPanel contentPane; 
	private final JLabel requestTypeLabel;
	public final JComboBox<String> requestTypeBox = new JComboBox<String>();
	private final JLabel recievedListLabel;
	public final JTable recievedList;
	public final JButton cancel = new JButton("Cancel");
	public final JButton confirm = new JButton("Confirm");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					asteaDownGui frame = new asteaDownGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});

	}
	
	public asteaDownGui() {
		setResizable(false);
		setTitle("Astea is down!?");
		setIconImage(Toolkit.getDefaultToolkit().getImage(gui.class.getResource("/images/logo.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 10, 350, 290);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		requestTypeLabel = new JLabel("Request type:");
		requestTypeLabel.setBounds(10, 10, 122, 14);
		contentPane.add(requestTypeLabel);
		
		requestTypeBox.setModel(new DefaultComboBoxModel<String>(new String[] {"", "1X", "1W", "1P", "1N", "1C", "1PR", "1WS"}));
		requestTypeBox.setToolTipText("Request type of the work order");
		requestTypeBox.setBackground(Color.WHITE);
		requestTypeBox.setBounds(174, 7, 160, 20);
		requestTypeBox.setSelectedIndex(0);
		contentPane.add(requestTypeBox);
		
		recievedListLabel = new JLabel("Recieved items: ");
		recievedListLabel.setBounds(10, 35, 122, 14);
		contentPane.add(recievedListLabel);
		
		JScrollPane recievedListScrollPane = new JScrollPane();
		recievedListScrollPane.setBounds(10, 60, 324, 135);
		contentPane.add(recievedListScrollPane);
		
		recievedList = new JTable();
		recievedListScrollPane.setViewportView(recievedList);
		recievedList.setToolTipText("All items that are quoted go on left with price on right");
		recievedList.setBorder(UIManager.getBorder("PopupMenu.border"));
		recievedList.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
				},
				new String[] {
						"Item Name", "Serial Number"
				}
				));
		recievedList.getColumnModel().getColumn(1).setPreferredWidth(45);
		
		confirm.setBackground(new Color(204, 255, 204));
		confirm.setBounds(10, 205, 165, 23);
		contentPane.add(confirm);
		
		cancel.setBackground(new Color(255, 204, 204));
		cancel.setBounds(175, 205, 160, 23);
		contentPane.add(cancel);
		
		this.confirm.addActionListener(this);
		this.cancel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == this.confirm) {
			stopTableEdits();
			if (allFieldsDone()) {
				this.setVisible(false);
			}
		}
	}
	
	private boolean allFieldsDone() {
		boolean result = true;
		if (this.requestTypeBox.getSelectedIndex() == 0) {
			this.requestTypeBox.setBackground(badFormColor);
			result = false;
		}
		else {
			this.requestTypeBox.setBackground(Color.WHITE);
		}
		if (tableIsEmpty(recievedList)) {
			recievedList.setBackground(badFormColor);
			result = false;
		}
		else {
			this.recievedList.setBackground(Color.WHITE);
		}
		
		return result;
	}
	
	private boolean tableIsEmpty(JTable table) {
		boolean result = true;
		TableModel model = table.getModel();
		try {
			String itemName = (model.getValueAt(0, 0)).toString().trim();
			if (!itemName.equals("")) {
				result = false;
			}
		}
		catch(Exception e) {
		}
		return result; 
	}
	
	public String getRequestType() {
		return (String) this.requestTypeBox.getSelectedItem();
	}
	
	public Queue<String> getRecievedList() {
		
		Queue<String> recievedList = new Queue1L<String>();
		TableModel model = this.recievedList.getModel();
		int rows = model.getRowCount();
		for (int i = 0; i < rows; i++) {
			try {
				String itemName = (model.getValueAt(i, 0)).toString().trim();
				try {
					String serialNumber = (model.getValueAt(i, 1)).toString().trim();
					itemName += " -- Serial Number: " + serialNumber;
				}
				catch (Exception e) { 
				}
				recievedList.enqueue(itemName);
			}
			catch(Exception e) {
			}
		}
		
		return recievedList;
	}
	
	private void stopTableEdits() {
		try{
			this.recievedList.getCellEditor().stopCellEditing();
		}catch(Exception e){

		}
	}
	
	private final Color badFormColor = new Color(255, 178, 178);

}
