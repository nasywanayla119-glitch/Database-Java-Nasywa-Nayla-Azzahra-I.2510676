import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AppGUI extends JFrame {
    private JTextField txtNIM, txtNama, txtProdi, txtAngkatan;
    private JComboBox<String> cbGender;
    private JTable tabelMahasiswa;
    private DefaultTableModel model;

    public AppGUI() {
        setTitle("Sistem Akademik Terpadu");
        setSize(700, 550); // Ukuran diperbesar karena kolom nambah
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. PANEL INPUT (Update jadi 5 Baris) ---
        JPanel pnlInput = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlInput.add(new JLabel("NIM:"));
        txtNIM = new JTextField(); pnlInput.add(txtNIM);

        pnlInput.add(new JLabel("Nama:"));
        txtNama = new JTextField(); pnlInput.add(txtNama);

        pnlInput.add(new JLabel("Program Studi:"));
        txtProdi = new JTextField(); pnlInput.add(txtProdi);

        pnlInput.add(new JLabel("Angkatan:"));
        txtAngkatan = new JTextField(); pnlInput.add(txtAngkatan);

        pnlInput.add(new JLabel("Jenis Kelamin:"));
        cbGender = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        pnlInput.add(cbGender);

        // --- 2. PANEL TOMBOL ---
        JPanel pnlTombol = new JPanel();
        JButton btnSimpan = new JButton("Simpan Data");
        JButton btnHapus = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");
        pnlTombol.add(btnSimpan);
        pnlTombol.add(btnHapus);
        pnlTombol.add(btnRefresh);

        // --- 3. TABEL (Update Header) ---
        model = new DefaultTableModel(new String[]{"NIM", "Nama", "Prodi", "Angkatan", "Gender"}, 0);
        tabelMahasiswa = new JTable(model);
        
        // Atur lebar kolom NIM agar lebih rapi
        tabelMahasiswa.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabelMahasiswa.getColumnModel().getColumn(0).setMaxWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(tabelMahasiswa);

        // Pasang semua ke Frame
        add(pnlInput, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlTombol, BorderLayout.SOUTH);

        // --- LOGIKA TOMBOL ---

        tampilData();

        btnSimpan.addActionListener(e -> {
            if (txtNIM.getText().isEmpty() || txtNama.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "NIM dan Nama wajib diisi!");
                return;
            }
            try {
                Connection conn = Koneksi.getKoneksi();
                // Query Update: Menambah 3 kolom baru
                String sql = "INSERT INTO mahasiswa (nim, nama, prodi, angkatan, gender) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, txtNIM.getText());
                ps.setString(2, txtNama.getText());
                ps.setString(3, txtProdi.getText());
                ps.setString(4, txtAngkatan.getText());
                ps.setString(5, cbGender.getSelectedItem().toString());
                ps.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Data Mahasiswa Berhasil Disimpan!");
                tampilData();
                resetInput();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error Simpan: " + ex.getMessage());
            }
        });

        btnHapus.addActionListener(e -> {
            int baris = tabelMahasiswa.getSelectedRow();
            if (baris != -1) {
                String nim = model.getValueAt(baris, 0).toString();
                int konfirmasi = JOptionPane.showConfirmDialog(this, "Hapus data NIM " + nim + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    try {
                        Connection conn = Koneksi.getKoneksi();
                        String sql = "DELETE FROM mahasiswa WHERE nim = ?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, nim);
                        ps.executeUpdate();
                        tampilData();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal Hapus!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu!");
            }
        });

        btnRefresh.addActionListener(e -> tampilData());
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mahasiswa");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nim"), 
                    rs.getString("nama"),
                    rs.getString("prodi"),
                    rs.getString("angkatan"),
                    rs.getString("gender")
                });
            }
        } catch (SQLException e) {
            System.out.println("Gagal load data: " + e.getMessage());
        }
    }

    private void resetInput() {
        txtNIM.setText("");
        txtNama.setText("");
        txtProdi.setText("");
        txtAngkatan.setText("");
        txtNIM.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
    }
}