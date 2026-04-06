import javax.persistence.*;

@Entity
@Table(name = "mahasiswa")
public class Mahasiswa {
    @Id
    private String nim;
    
    @Column(name = "nama")
    private String nama;
    
    @Column(name = "prodi")
    private String prodi;
    
    @Column(name = "angkatan")
    private String angkatan;
    
    @Column(name = "gender")
    private String gender;

    // Getter dan Setter (Wajib ada biar Hibernate bisa baca datanya)
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getProdi() { return prodi; }
    public void setProdi(String prodi) { this.prodi = prodi; }
    public String getAngkatan() { return angkatan; }
    public void setAngkatan(String angkatan) { this.angkatan = angkatan; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}