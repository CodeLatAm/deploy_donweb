package com.abel.sistema_gestion.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50)
    private String name;
    @Column(length = 100)
    private String address;
    @Column(length = 50)
    private String phone;
    @Column(length = 50)
    private String cp;
    @Column(length = 50)
    private String email;
    @Column(length = 100)
    private String betweenStreets;
    @Column(length = 100)
    private String location;
    @Column(length = 100)
    private String province;
    @Column(length = 20)
    private String dni;
    private boolean activo = true;
    //private Integer vendorId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vendor_id")  // Define la clave foránea en la tabla de clients
    //@JsonIgnore // Evitar serialización recursiva
    private Vendor vendor;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Client{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", cp='").append(cp).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", betweenStreets='").append(betweenStreets).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
