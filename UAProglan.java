import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class UAProglan {
    public static void main(String[] args) {
        // Membuat JFrame
        JFrame frame = new JFrame("Program Mengelola Daftar Tugas");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Membuat JLabel dan JTextField untuk input
        JLabel lblID = new JLabel("Nomor:");
        lblID.setBounds(20, 20, 100, 25);
        JTextField txtID = new JTextField();
        txtID.setBounds(130, 20, 150, 25);
        txtID.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Ignore non-digit characters
                }
            }
        });

        JLabel lblTugas = new JLabel("Tugas:");
        lblTugas.setBounds(20, 60, 100, 25);
        JTextField txtTugas = new JTextField();
        txtTugas.setBounds(130, 60, 150, 25);

        JLabel lblLastName = new JLabel("Deadline:");
        lblLastName.setBounds(20, 100, 100, 25);
        JTextField txtLastName = new JTextField();
        txtLastName.setBounds(130, 100, 150, 25);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(20, 140, 100, 25);
        String[] statuses = {"Belum Selesai", "Proses", "Selesai"};
        JComboBox<String> comboStatus = new JComboBox<>(statuses);
        comboStatus.setBounds(130, 140, 150, 25);

        JLabel lblImage = new JLabel("Image:");
        lblImage.setBounds(20, 180, 100, 25);
        JButton btnUpload = new JButton("Upload");
        btnUpload.setBounds(130, 180, 150, 25);
        JLabel imgPreview = new JLabel();
        imgPreview.setBounds(290, 20, 150, 150);
        imgPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Membuat JButton untuk menambahkan data
        JButton btnAdd = new JButton("Tambah Data");
        btnAdd.setBounds(20, 220, 120, 30);
        JButton btnUpdate = new JButton("Update Data");
        btnUpdate.setBounds(150, 220, 120, 30);
        JButton btnDelete = new JButton("Hapus Data");
        btnDelete.setBounds(280, 220, 120, 30);

        // Membuat JTable dan model tabel
        String[] columnNames = {"No", "Tugas", "Deadline", "Status", "Image"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 270, 750, 150);

        // Menambahkan komponen ke JFrame
        frame.add(lblID);
        frame.add(txtID);
        frame.add(lblTugas);
        frame.add(txtTugas);
        frame.add(lblLastName);
        frame.add(txtLastName);
        frame.add(lblStatus);
        frame.add(comboStatus);
        frame.add(lblImage);
        frame.add(btnUpload);
        frame.add(imgPreview);
        frame.add(btnAdd);
        frame.add(btnUpdate);
        frame.add(btnDelete);
        frame.add(scrollPane);

        // ActionListener untuk tombol Upload
        btnUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage img = ImageIO.read(selectedFile);
                        ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                        imgPreview.setIcon(imageIcon);
                        imgPreview.setText(selectedFile.getAbsolutePath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // ActionListener untuk tombol Tambah Data
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validasi input tidak boleh kosong
                    String id = txtID.getText().trim();
                    String firstName = txtTugas.getText().trim();
                    String lastName = txtLastName.getText().trim();
                    String status = comboStatus.getSelectedItem().toString();
                    String imgPath = imgPreview.getText().trim();

                    if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || status.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Semua kolom kecuali gambar harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Menambahkan data ke tabel
                        tableModel.addRow(new Object[]{id, firstName, lastName, status, imgPath.isEmpty() ? "N/A" : imgPath});

                        // Mengosongkan input setelah data ditambahkan
                        txtID.setText("");
                        txtTugas.setText("");
                        txtLastName.setText("");
                        comboStatus.setSelectedIndex(0);
                        imgPreview.setIcon(null);
                        imgPreview.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Masukkan format angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener untuk tombol Update Data
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Validasi input tidak boleh kosong
                    String id = txtID.getText().trim();
                    String firstName = txtTugas.getText().trim();
                    String lastName = txtLastName.getText().trim();
                    String status = comboStatus.getSelectedItem().toString();
                    String imgPath = imgPreview.getText().trim();

                    if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || status.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Semua kolom kecuali gambar harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Memperbarui data di tabel
                        tableModel.setValueAt(id, selectedRow, 0);
                        tableModel.setValueAt(firstName, selectedRow, 1);
                        tableModel.setValueAt(lastName, selectedRow, 2);
                        tableModel.setValueAt(status, selectedRow, 3);
                        tableModel.setValueAt(imgPath.isEmpty() ? "N/A" : imgPath, selectedRow, 4);

                        // Mengosongkan input setelah data diperbarui
                        txtID.setText("");
                        txtTugas.setText("");
                        txtLastName.setText("");
                        comboStatus.setSelectedIndex(0);
                        imgPreview.setIcon(null);
                        imgPreview.setText("");

                        JOptionPane.showMessageDialog(frame, "Data diperbarui!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris yang akan diperbarui!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener untuk tombol Hapus Data
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Data dihapus!");

                    // Mengosongkan input setelah data dihapus
                    txtID.setText("");
                    txtTugas.setText("");
                    txtLastName.setText("");
                    comboStatus.setSelectedIndex(0);
                    imgPreview.setIcon(null);
                    imgPreview.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tableModel.addTableModelListener(e -> { //untuk mengubah status dan gambar
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 3) {
                String updatedStatus = (String) tableModel.getValueAt(row, column);
                JOptionPane.showMessageDialog(frame, "Status diperbarui");
            } else if (column == 4) {
                String updatedImagePath = (String) tableModel.getValueAt(row, column);
                try {
                    BufferedImage img = ImageIO.read(new File(updatedImagePath));
                    ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    imgPreview.setIcon(imageIcon);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Menambahkan MouseListener untuk memilih data dari tabel
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    txtID.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtTugas.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtLastName.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    comboStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
                    imgPreview.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    try {
                        BufferedImage img = ImageIO.read(new File(tableModel.getValueAt(selectedRow, 4).toString()));
                        ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                        imgPreview.setIcon(imageIcon);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Menampilkan JFrame
        frame.setVisible(true);
    }
}