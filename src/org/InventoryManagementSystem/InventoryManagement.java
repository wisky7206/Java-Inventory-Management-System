package org.InventoryManagementSystem;

import java.awt.EventQueue;
import javax.swing.SwingWorker;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

/**
 * This class represents the Inventory Management system.
 */
public class InventoryManagement {
	DatabaseConnection connection = new DatabaseConnection("InventoryDB");
	private JFrame frame;
	private JTable purchaseTable;
	private DefaultTableModel producTableModel = new DefaultTableModel(new Object[] {"ID","Name","Quantity","Price","Created","Updated"}, 0);
	private DefaultTableModel categoriesTableModel = new DefaultTableModel(new Object[] {"ID","Name"}, 0);
	private DefaultTableModel inventotyTableModel = new DefaultTableModel(
			new Object[] {
				"ID", "Name", "Quantity","price", "Created", "Updated"
			},0
		);
	private JTextField idField;
	private JTextField quantityField;
	private JTable invntoryTable;
	private JTextField productIDField;
	private JTextField categIDField;
	private JTextField pidField;
	private JTextField quantitiesField;
	

	/**
	 * The main method of the application.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InventoryManagement window = new InventoryManagement();
					window.frame.setVisible(true);
					
	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * The constructor of the class.
	 * Initializes the database connection and the GUI.
	 */
	public InventoryManagement() {
		connection.createSchema();
		initialize();
	}

	/**
	 * Initializes the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 550);
		frame.setTitle("Inventory Management");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 700, 520);
		frame.getContentPane().add(tabbedPane);
		
		JPanel purchasePanel = new JPanel();
		tabbedPane.addTab("Purchase", null, purchasePanel, null);
		purchasePanel.setLayout(null);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(new Color(143, 240, 164));
		buttonsPanel.setBounds(0, 425, 700, 70);
		purchasePanel.add(buttonsPanel);
		buttonsPanel.setLayout(new GridLayout(1, 0, 5, 5));
		
		JButton btnDeleteCategory = new JButton("Delete Category");
		btnDeleteCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id  = (String)JOptionPane.showInputDialog(btnDeleteCategory, "Enter the category Id to Delete:");
				try {
					connection.deleteCategory(Integer.valueOf(id));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(btnDeleteCategory, "Invalid ID");
					e1.printStackTrace();
				}
			}
		});
		buttonsPanel.add(btnDeleteCategory);
		
		JButton btnDeleteProduct = new JButton("Delete Product");
		btnDeleteProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id  = (String)JOptionPane.showInputDialog(btnDeleteProduct, "Enter the Product Id to Delete:");
				try {
					connection.deleteProduct(Integer.valueOf(id));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(btnDeleteCategory, "Invalid ID");
					e1.printStackTrace();
				}
			}
		});
		buttonsPanel.add(btnDeleteProduct);
		
		JButton btnAddNewCategory = new JButton("Add New Category");
		btnAddNewCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name  = (String)JOptionPane.showInputDialog(btnAddNewCategory, "Enter the category name to add:");
				connection.insertCategory(name);
			}
		});
		buttonsPanel.add(btnAddNewCategory);
		
		JButton btnAddNewProduct = new JButton("Add New Product");
		btnAddNewProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddProductWindow window = new AddProductWindow(connection);
				window.setVisible(true);
			}
		});
		buttonsPanel.add(btnAddNewProduct);
		
		JToggleButton tglbtn = new JToggleButton("Products");
		tglbtn.setBounds(0, 0, 167, 25);
		tglbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tglbtn.getText() == "Products") {
					tglbtn.setText("Categories");
					purchaseTable.setModel(categoriesTableModel);
					connection.getAllCategories(categoriesTableModel);
				}else {
					tglbtn.setText("Products");
					purchaseTable.setModel(producTableModel);
					connection.getAllProducts(producTableModel);
				}
				
			}
		});
		purchasePanel.add(tglbtn);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 25, 700, 300);
		purchasePanel.add(scrollPane);
		
		purchaseTable = new JTable(producTableModel);
		scrollPane.setViewportView(purchaseTable);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(10, 393, 70, 25);
		purchasePanel.add(lblId);
		
		idField = new JTextField();
		idField.setBounds(78, 393, 114, 25);
		purchasePanel.add(idField);
		idField.setColumns(10);
		
		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setBounds(210, 393, 70, 25);
		purchasePanel.add(lblQuantity);
		
		quantityField = new JTextField();
		quantityField.setColumns(10);
		quantityField.setBounds(298, 393, 114, 25);
		purchasePanel.add(quantityField);
		
		JButton btnPurchase = new JButton("Purchase");
		btnPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					connection.purchaseProduct(Integer.valueOf(idField.getText()), Integer.valueOf(quantityField.getText()));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(btnPurchase, "Please enter Valid values");
					e1.printStackTrace();
				}
			}
		});
		btnPurchase.setBounds(438, 393, 117, 25);
		purchasePanel.add(btnPurchase);
		
		JLabel lblProduc = new JLabel("Product Id:");
		lblProduc.setBounds(12, 337, 100, 25);
		purchasePanel.add(lblProduc);
		
		productIDField = new JTextField();
		productIDField.setColumns(10);
		productIDField.setBounds(107, 337, 114, 25);
		purchasePanel.add(productIDField);
		
		JLabel lblCategoryId = new JLabel("Category Id:");
		lblCategoryId.setBounds(222, 337, 100, 25);
		purchasePanel.add(lblCategoryId);
		
		categIDField = new JTextField();
		categIDField.setColumns(10);
		categIDField.setBounds(323, 337, 114, 25);
		purchasePanel.add(categIDField);
		
		JButton btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					connection.assignCategoryToProduct(Integer.valueOf(productIDField.getText()),Integer.valueOf(categIDField.getText()));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(btnSet, "Invalid ID");
					e1.printStackTrace();
				}
			}
		});
		btnSet.setBounds(453, 337, 70, 25);
		purchasePanel.add(btnSet);
		
		JPanel inventoryPanel = new JPanel();
		tabbedPane.addTab("Inventory & Sales", null, inventoryPanel, null);
		inventoryPanel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setLocation(0, 25);
		scrollPane_1.setSize(700, 350);
		inventoryPanel.add(scrollPane_1);
		
		invntoryTable = new JTable(inventotyTableModel);
		scrollPane_1.setViewportView(invntoryTable);
		
		JLabel lblSelectCategory = new JLabel("Select Category");
		lblSelectCategory.setBounds(0, 0, 125, 15);
		inventoryPanel.add(lblSelectCategory);
		
		JComboBox<String> categoryComboBox = new JComboBox<String>();
		categoryComboBox.setBounds(125, 0, 125, 24);
		inventoryPanel.add(categoryComboBox);
		connection.updateComboBox(categoryComboBox);
		
		JLabel lblEnterPidSeparated = new JLabel("Enter PIDs separated by \",\"");
		lblEnterPidSeparated.setBounds(10, 387, 200, 15);
		inventoryPanel.add(lblEnterPidSeparated);
		
		pidField = new JTextField();
		pidField.setBounds(263, 387, 420, 25);
		inventoryPanel.add(pidField);
		pidField.setColumns(10);
		
		JLabel lblEnterQuantitiesSeparated = new JLabel("Enter Quantities separated by \",\"");
		lblEnterQuantitiesSeparated.setBounds(0, 414, 240, 15);
		inventoryPanel.add(lblEnterQuantitiesSeparated);
		
		quantitiesField = new JTextField();
		quantitiesField.setColumns(10);
		quantitiesField.setBounds(263, 414, 420, 25);
		inventoryPanel.add(quantitiesField);
		
		JButton btnSellProducts = new JButton("Sell Products");
		btnSellProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> pidArrayList = new ArrayList<>(Arrays.asList(pidField.getText().split(",")));
				ArrayList<String> quantArrayList = new ArrayList<>(Arrays.asList(quantitiesField.getText().split(",")));
		
				// Ask the user if they want to generate a bill
				int choice = JOptionPane.showConfirmDialog(btnSellProducts, "Generate Bill?", "Bill Confirmation", JOptionPane.YES_NO_OPTION);
		
				if (choice == JOptionPane.YES_OPTION) {
					// Proceed with selling the products and generating a bill
					String result = connection.sellMultipleProducts(pidArrayList, quantArrayList);
		
					// Calculate the total bill amount
					double totalAmount = connection.calculateTotalBillAmount(pidArrayList, quantArrayList);
		
					// Build the bill message
					StringBuilder billMessage = new StringBuilder("Bill:\n");
					for (int i = 0; i < pidArrayList.size(); i++) {
						// Get the product name and quantity
						String productName = connection.getProductNameById(Integer.parseInt(pidArrayList.get(i)));
						int quantity = Integer.parseInt(quantArrayList.get(i));
						double productPrice = connection.getProductPriceById(Integer.parseInt(pidArrayList.get(i)));
		
						// Calculate the line total (quantity * price)
						double lineTotal = quantity * productPrice;
		
						// Append product information to the bill message
						billMessage.append(productName).append(" x").append(quantity).append(" = $").append(lineTotal).append("\n");
					}
		
					// Append the total bill amount
					billMessage.append("Total Amount: $").append(totalAmount);
		
					// Display the bill using a JOptionPane
					JOptionPane.showMessageDialog(btnSellProducts, billMessage.toString(), "Bill", JOptionPane.INFORMATION_MESSAGE);
		
					// Check if the quantity of any item falls below 50
					boolean quantityBelow50 = false;
					StringBuilder lowQuantityMessage = new StringBuilder("Low Quantity Alert:\n");
		
					for (int i = 0; i < pidArrayList.size(); i++) {
						int productId = Integer.parseInt(pidArrayList.get(i));
						int quantity = connection.getProductQuantityById(productId); // Use the new method to get quantity
						if (quantity < 50) {
							quantityBelow50 = true;
		
							// Get the product name
							String productName = connection.getProductNameById(productId);
		
							// Append low quantity product information to the message
							lowQuantityMessage.append(productName).append(" (ID: ").append(productId).append(") - Quantity: ").append(quantity).append("\n");
						}
					}
		
					// If any item has quantity below 50, show a warning alert
					if (quantityBelow50) {
						JOptionPane.showMessageDialog(btnSellProducts, lowQuantityMessage.toString(), "Low Quantity Alert", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		btnSellProducts.setBounds(254, 451, 130, 25);
		inventoryPanel.add(btnSellProducts);
		

		
		

		
		
		

		
		categoryComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = (String)categoryComboBox.getSelectedItem();
				id = id.substring(0, id.lastIndexOf("|"));
				connection.getProductsByCategory(Integer.valueOf(id),inventotyTableModel);
			}
		});

	}
}
