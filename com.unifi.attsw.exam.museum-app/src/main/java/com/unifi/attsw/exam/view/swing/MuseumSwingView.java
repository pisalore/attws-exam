package com.unifi.attsw.exam.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.view.MuseumView;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MuseumSwingView extends JFrame implements MuseumView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMuseum;
	private JTextField txtRooms;

	/**
	 * Create the frame.
	 */
	public MuseumSwingView() {
		setTitle("Museum Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 100, 100, 152, 100 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblMuseum = new JLabel("Museum");
		GridBagConstraints gbc_lblMuseum = new GridBagConstraints();
		gbc_lblMuseum.anchor = GridBagConstraints.EAST;
		gbc_lblMuseum.insets = new Insets(0, 0, 5, 5);
		gbc_lblMuseum.gridx = 0;
		gbc_lblMuseum.gridy = 0;
		contentPane.add(lblMuseum, gbc_lblMuseum);

		txtMuseum = new JTextField();
		txtMuseum.setName("museum");
		GridBagConstraints gbc_txtMuseum = new GridBagConstraints();
		gbc_txtMuseum.insets = new Insets(0, 0, 5, 5);
		gbc_txtMuseum.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMuseum.gridx = 1;
		gbc_txtMuseum.gridy = 0;
		contentPane.add(txtMuseum, gbc_txtMuseum);
		txtMuseum.setColumns(10);

		JButton btnFindAll = new JButton("Find all");
		GridBagConstraints gbc_btnFindAll = new GridBagConstraints();
		gbc_btnFindAll.insets = new Insets(0, 0, 5, 0);
		gbc_btnFindAll.gridx = 3;
		gbc_btnFindAll.gridy = 0;
		contentPane.add(btnFindAll, gbc_btnFindAll);

		JLabel lblRooms = new JLabel("Rooms");
		GridBagConstraints gbc_lblRooms = new GridBagConstraints();
		gbc_lblRooms.anchor = GridBagConstraints.EAST;
		gbc_lblRooms.insets = new Insets(0, 0, 5, 5);
		gbc_lblRooms.gridx = 0;
		gbc_lblRooms.gridy = 1;
		contentPane.add(lblRooms, gbc_lblRooms);

		txtRooms = new JTextField();
		txtRooms.setName("rooms");
		GridBagConstraints gbc_txtRooms = new GridBagConstraints();
		gbc_txtRooms.insets = new Insets(0, 0, 5, 5);
		gbc_txtRooms.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRooms.gridx = 1;
		gbc_txtRooms.gridy = 1;
		contentPane.add(txtRooms, gbc_txtRooms);
		txtRooms.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		
		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				btnAdd.setEnabled(!txtMuseum.getText().trim().isEmpty() && !txtRooms.getText().trim().isEmpty());
			}
		};
		

		txtRooms.addKeyListener(btnAddEnabler);
		btnAdd.setEnabled(false);

		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 2;
		contentPane.add(btnAdd, gbc_btnAdd);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);

		JList museumList = new JList();
		museumList.setName("museumList");
		scrollPane.setViewportView(museumList);

		JLabel errorMessageLabel = new JLabel(" ");
		errorMessageLabel.setForeground(Color.RED);
		errorMessageLabel.setName("errorMessageLabel");
		GridBagConstraints gbc_errorMessageLabel = new GridBagConstraints();
		gbc_errorMessageLabel.gridwidth = 3;
		gbc_errorMessageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_errorMessageLabel.gridx = 0;
		gbc_errorMessageLabel.gridy = 5;
		contentPane.add(errorMessageLabel, gbc_errorMessageLabel);

		JButton btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
		gbc_btnDeleteSelected.anchor = GridBagConstraints.EAST;
		gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteSelected.gridx = 3;
		gbc_btnDeleteSelected.gridy = 5;
		contentPane.add(btnDeleteSelected, gbc_btnDeleteSelected);
	}

	@Override
	public void showError(String message, Museum museum) {
		// TODO Auto-generated method stub

	}

}
