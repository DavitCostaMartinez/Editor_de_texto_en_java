package es.aev;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

public class Vista extends JFrame {

	boolean editable = false;
	String selectedFile = "";
	private JPanel contentPane;
	private JTextField DirectoryText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vista frame = new Vista();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * 
	 */
	public Vista() {
		setTitle("Apruebanos, Roberto (Pablo el Rozalen heroe nacional)");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		DirectoryText = new JTextField();
		DirectoryText.setEditable(false);
		DirectoryText.setBounds(109, 10, 365, 23);
		contentPane.add(DirectoryText);
		DirectoryText.setColumns(10);

		JButton DirectoryButton = new JButton("Obrir...");
		DirectoryButton.setBounds(10, 10, 89, 23);
		contentPane.add(DirectoryButton);

		JList<File> FileList = new JList<File>();
		FileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		FileList.setBounds(10, 70, 101, 280);
		contentPane.add(FileList);

		JPopupMenu popupMenu = new JPopupMenu();
		FileList.add(popupMenu);

		JMenuItem VerInfoItem = new JMenuItem("Ver Informacio");
		popupMenu.add(VerInfoItem);

		JMenuItem canviarNomItem = new JMenuItem("Canviar Nom");
		popupMenu.add(canviarNomItem);

		JMenuItem copiarItem = new JMenuItem("Copiar Archiu");
		popupMenu.add(copiarItem);
		JMenuItem eliminarItem = new JMenuItem("Eliminar");
		popupMenu.add(eliminarItem);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(119, 44, 355, 306);
		contentPane.add(scrollPane);

		JTextArea FileContentArea = new JTextArea();
		FileContentArea.setText("Selecciona el boton \"Obrir\" \r\ni selecciona un directori per a continuar");
		scrollPane.setViewportView(FileContentArea);
		FileContentArea.setLineWrap(true);
		FileContentArea.setEditable(false);

		JPopupMenu popupMenu_1 = new JPopupMenu();
		FileContentArea.add(popupMenu_1);
		popupMenu_1.setEnabled(false);

		JMenuItem GuardarArchiu = new JMenuItem("Guardar");
		popupMenu_1.add(GuardarArchiu);

		JMenuItem GuardarNouArchiu = new JMenuItem("Guardar com");
		popupMenu_1.add(GuardarNouArchiu);

		JMenuItem Trobar = new JMenuItem("Trobar");
		popupMenu_1.add(Trobar);

		JMenuItem TrobarRemplazar = new JMenuItem("Trobar y Reemplaçar");
		popupMenu_1.add(TrobarRemplazar);

		JButton NouArchiuButton = new JButton("Nou Archiu");
		NouArchiuButton.setBounds(10, 46, 101, 23);
		contentPane.add(NouArchiuButton);
		NouArchiuButton.setEnabled(false);
		/**
		 * Mètode per a seleccionar els fixers del teu equip i mostrar una llista del fixers .txt
		 */
		DirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editable = false;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = fileChooser.showOpenDialog(DirectoryButton);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					DirectoryText.setText(file.getAbsolutePath());
					ResetFileList(FileList);
					NouArchiuButton.setEnabled(true);
					FileContentArea.setText("Fes click dret en un archiu per a veure la seua informacio "
							+ "o doble click per a obrirlo i veure el seu contingut");
				} else {
					DirectoryText.setText("Error al realizar la carga");
					NouArchiuButton.setEnabled(false);
				}
			}
		});

		FileList.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() && FileList.getSelectedIndex() != -1) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			/**
			 * Mètode que mostrara en el FileContentArea tot el contingut del archiu seleccionat en FileList amb doble click. 
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2 && FileList.getSelectedIndex() != -1) {
					try {
						editable = true;
						selectedFile = ""+FileList.getSelectedValue();
						popupMenu_1.setEnabled(true);
						FileContentArea.setEditable(true);
						FileContentArea.setText("");
						FileReader fReader = new FileReader(
								DirectoryText.getText() + "/" + FileList.getSelectedValue());
						BufferedReader bufferedReader = new BufferedReader(fReader);
						String line = bufferedReader.readLine();
						while (line != null) {
							FileContentArea.setText(FileContentArea.getText() + line + "\n");
							line = bufferedReader.readLine();
						}
						bufferedReader.close();
						fReader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		/**
		 * Metode del menu que eliminara amb el click dret del archiu seleccionat i tendras l'opció a aceptar o calcelar.
		 */
		eliminarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File eliminarFile = new File(DirectoryText.getText() + "/" + FileList.getSelectedValue());
				int input = JOptionPane.showConfirmDialog(null, "Estas segur de eliminar aquest archiu?",
						"Selecciona una opcio...", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if (input == 0) {
					eliminarFile.delete();
					ResetFileList(FileList);
				}
			}
		});
		/**
		 * Metode del menu que canviara el nom amb el click dret del archiu seleccionat.
		 */
		canviarNomItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File tempFile = new File(DirectoryText.getText() + "/" + FileList.getSelectedValue());
				String responseString = JOptionPane.showInputDialog(null, "Introdueix el nou nom del archiu");
				File newfile = new File(DirectoryText.getText() + "/" + responseString + ".txt");

				boolean state = tempFile.renameTo(newfile);
				if (!state) {
					JOptionPane.showMessageDialog(null, "Ya existeix un archiu amb el mateix nom en el directori",
							"Error", JOptionPane.DEFAULT_OPTION);
				}
				ResetFileList(FileList);

			}
		});
		/**
		 * Metode del menu que copiara amb el click dret del archiu seleccionat.
		 */
		copiarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = DirectoryText.getText() + "/" + FileList.getSelectedValue();
				name = name.substring(0, name.lastIndexOf("."));
				File file = new File(DirectoryText.getText() + "/" + FileList.getSelectedValue());
				File newFile = new File(name + "_copia.txt");

				try (BufferedReader br = new BufferedReader(new FileReader(file));
						BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));) {
					String linea = br.readLine();
					while (linea != null) {
						bw.write(linea);
						bw.newLine();
						linea = br.readLine();
					}
					bw.flush();
				} catch (IOException x) {
					System.out.println("Error E/S: " + x);
				}
				ResetFileList(FileList);
			}

		});
		/**
		 * Boton que podras crear un nou archiu .txt desde eixa ruta de la carpeta seleccionada.
		 */
		NouArchiuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String responseString = JOptionPane.showInputDialog(null, "Introdueix el nom del archiu");
				File newfiFile = new File(DirectoryText.getText() + "/" + responseString + ".txt");
				try {
					newfiFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ResetFileList(FileList);
			}
		});
		/**
		 * Metode del menu que podras ver la informació del archiu en FileContentArea amb el click dret del archiu seleccionat.
		 */
		VerInfoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editable = false;
				FileContentArea.setEditable(false);
				FileContentArea.setText("");
				File file = new File(DirectoryText.getText() + "/" + FileList.getSelectedValue());

				String pattern = "dd-MM-yyyy HH:mm:ss";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String formatted = simpleDateFormat.format(file.lastModified());

				FileContentArea
						.setText(FileContentArea.getText() + "Tamaño archivo: " + file.length() + " bytes" + "\n");
				FileContentArea.setText(FileContentArea.getText() + "Ultima Modificacion: " + formatted + "\n");
				FileContentArea.setText(FileContentArea.getText() + "Permiso de Escritura: " + file.canWrite() + "\n");
				FileContentArea.setText(FileContentArea.getText() + "Permiso de Lectura: " + file.canRead() + "\n");
				FileContentArea
						.setText(FileContentArea.getText() + "Permiso de Ejecucion: " + file.canExecute() + "\n");
			}
		});
		/**
		 * Menu per a FileContentArea.
		 */
		FileContentArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() && editable == true) {
					popupMenu_1.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		/**
		 * Metode del menu que podras guardar la informació del archiu en FileContentArea amb el click dret del FileContentArea.
		 */
		GuardarArchiu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File newfiFile = new File(DirectoryText.getText() + "/" + selectedFile);
				try {
					FileWriter pw = new FileWriter(newfiFile);
					FileContentArea.write(pw);
					pw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		/**
		 * Metode del menu que podras guardar en un nou archiu la informació del archiu en FileContentArea amb el click dret del FileContentArea.
		 */
		GuardarNouArchiu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String responseString = JOptionPane.showInputDialog(null, "Introdueix el nou nom del archiu");
				File newFile = new File(DirectoryText.getText() + "/" + responseString + ".txt");
				try {
					newFile.createNewFile();
					FileWriter pw = new FileWriter(newFile);
					FileContentArea.write(pw);
					pw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ResetFileList(FileList);
			}
		});
		/**
		 * Metode del menu que podras trobar la paraula escrita i destacarla amb el click dret del FileContentArea.
		 */
		Trobar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String responseString = JOptionPane.showInputDialog(null, "Introdueix la palabra a trobar");
				try {
					findOccurrences(FileContentArea, responseString);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		/**
		 * Metode del menu que podras trobar la paraula escrita del archiu i reemplaçar-li amb el click dret del FileContentArea.
		 */
		TrobarRemplazar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField xField = new JTextField(5);
				JTextField yField = new JTextField(5);
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Busqueda:"));
				myPanel.add(xField);
				myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				myPanel.add(new JLabel("Reemplaçament:"));
				myPanel.add(yField);

				int result = JOptionPane.showConfirmDialog(null, myPanel, "Introdueix la busqueda y el reemplaçament",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						ReplaceOccurrences(FileContentArea, xField.getText(), yField.getText());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
	}
	/**
	 * Metode per a resetear el contingut del FileContentArea
	 * @param Jlist on estan els archius
	 * @return void
	 */
	public void ResetFileList(JList FileList) {
		File file = new File(DirectoryText.getText());
		DefaultListModel modelo = new DefaultListModel();
		for (File files : file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		})) {
			if (files.isFile()) {
				modelo.addElement(files.getName());
			}
		}
		FileList.setModel(modelo);

	}
	/**
	 * Metode per a trobar la paraula i destacarla.
	 * @param JTextArea on es buscara
	 * @param String la paraula que es va a buscar
	 * @return void
	 */
	public void findOccurrences(JTextArea textArea, String keyWord) throws BadLocationException {
		Highlighter highlighter = textArea.getHighlighter();
		DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);

		highlighter.removeAllHighlights();

		if (keyWord.isEmpty() || keyWord == null)
			return;

		Pattern pattern = Pattern.compile("\\b" + keyWord + "\\b");
		Matcher matcher = pattern.matcher(textArea.getText());

		while (matcher.find()) {
			highlighter.addHighlight(matcher.start(), matcher.end(), painter);
		}
	}
	/**
	 * Metode per a trobar la paraula i reemplaçar-li.
	 * @param JTextArea on es va a reemplaçar
	 * @param String de la paraula a buscar
	 * @param String que va a reemplaçar les ocurrences
	 * @return void
	 */
	public void ReplaceOccurrences(JTextArea textArea, String keyWord, String newWord) throws BadLocationException {
		Highlighter highlighter = textArea.getHighlighter();

		highlighter.removeAllHighlights();

		if (keyWord.isEmpty() || keyWord == null || newWord.isEmpty() || newWord == null)
			return;

		Pattern pattern = Pattern.compile("\\b" + keyWord + "\\b");
		Matcher matcher = pattern.matcher(textArea.getText());
		
		textArea.setText(matcher.replaceAll(newWord));
		
		
	}
}
