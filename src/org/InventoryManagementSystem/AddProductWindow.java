package org.InventoryManagementSystem;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AddProductWindow extends JFrame{
	DatabaseConnection connection;
	private JTextField nameField;
	private JTextField quantityField;
	private JTextField priceField;
	
	public AddProductWindow(DatabaseConnection connection) {
		setTitle("Add Product");
		setBounds(100, 100, 440, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.connection = connection;
		getContentPane().setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(0, 1, 220, 67);
		getContentPane().add(lblName);
		
		nameField = new JTextField();
		nameField.setBounds(220, 1, 220, 67);
		nameField.setColumns(10);
		getContentPane().add(nameField);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(118, 203, 220, 67);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				connection.insertProduct(nameField.getText(),Integer.valueOf(quantityField.getText()),Double.valueOf(priceField.getText()));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(btnAdd, "Please Enter the numbers only");
					e1.printStackTrace();
				}
			}
		});
		
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(0, 68, 220, 67);
		getContentPane().add(lblQuantity);
		
		quantityField = new JTextField();
		quantityField.setBounds(220, 68, 220, 67);
		quantityField.setColumns(10);
		getContentPane().add(quantityField);
		
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(0, 135, 220, 67);
		getContentPane().add(lblPrice);
		
		priceField = new JTextField();
		priceField.setBounds(220, 135, 220, 67);
		priceField.setColumns(10);
		getContentPane().add(priceField);
		getContentPane().add(btnAdd);
		
	}
	
}
