package br.usp.iterador.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Guilherme Silveira
 */
public class ValueEditDialog extends JDialog {

	/**
	 * uid
	 */
	private static final long serialVersionUID = 3258417226746049080L;

	public static final int BUTTON_CANCEL = 0;

	public static final int BUTTON_OK = 1;

	private String name;

	private JTextField nameField;

	private int selectedButton;

	private String value;

	private JTextArea valueField;

	/**
	 * This is the default constructor
	 */
	public ValueEditDialog() {
		super((JFrame)null, "Value edit", true);
		this.setSize(500, 250);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		JButton jButton = new JButton();
		jButton.setText("Cancel");
		jButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				selectedButton = BUTTON_CANCEL;
				name = ValueEditDialog.this.nameField.getText();
				value = ValueEditDialog.this.valueField.getText();
				ValueEditDialog.this.dispose();
			}
		});
		return jButton;
	}

	private JButton getOk() {
		JButton jButton1 = new JButton();
		jButton1.setText("Ok");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				ValueEditDialog.this.selectedButton = BUTTON_OK;
				ValueEditDialog.this.name = ValueEditDialog.this.nameField
						.getText();
				ValueEditDialog.this.value = ValueEditDialog.this.valueField
						.getText();
				ValueEditDialog.this.dispose();
			}
		});
		return jButton1;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		JPanel jContentPane = new javax.swing.JPanel();
		jContentPane.setLayout(new java.awt.BorderLayout());
		jContentPane.add(getUpperPanel(), java.awt.BorderLayout.NORTH);
		jContentPane.add(getJPanel1(), java.awt.BorderLayout.CENTER);
		jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
		return jContentPane;
	}

	private JPanel getUpperPanel() {
		JPanel upper = new JPanel(new GridLayout(2, 1));
		JPanel jPanel3 = new JPanel();
		jPanel3.setLayout(new BorderLayout());
		jPanel3.add(new JLabel("Name: "), java.awt.BorderLayout.WEST);
		jPanel3.add(getNameField(), java.awt.BorderLayout.CENTER);
		upper.add(jPanel3);
		upper.add(new JLabel("---", JLabel.CENTER));
		return upper;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		JPanel jPanel = new JPanel(new GridLayout(1, 2));
		jPanel.add(getJButton(), null);
		jPanel.add(getOk(), null);
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		JPanel jPanel1 = new JPanel(new BorderLayout());
		jPanel1.add(getJPanel2());
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		JPanel jPanel2 = new JPanel();
		jPanel2.setLayout(new BorderLayout());
		jPanel2.add(new JLabel("Value: "), java.awt.BorderLayout.WEST);
		jPanel2.add(getValueField(), java.awt.BorderLayout.CENTER);
		return jPanel2;
	}

	/**
	 * Returns the new name
	 * 
	 * @see java.awt.Component#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextField getNameField() {
		return nameField = new JTextField();
	}

	public int getSelectedButton() {
		return selectedButton;
	}

	public String getValue() {
		return value;
	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getValueField() {
		if (valueField == null) {
			valueField = new JTextArea();
		}
		return valueField;
	}

	public void setName(String name) {
		this.name = name;
		this.nameField.setText(name);
	}

	public void setValue(String value) {
		this.value = value;
		this.valueField.setText(value);
	}
}
