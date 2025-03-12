package com.sharif.eshop.model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id ;
    private String filename ;
    private String filetype ;
    @Lob
    private Blob image ;

    private String downloadUrl ;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product ;



}
