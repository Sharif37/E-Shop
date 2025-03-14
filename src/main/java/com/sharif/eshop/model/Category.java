package com.sharif.eshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @NaturalId
    private String name ;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products ;




    public Category(String name) {
        this.name =name ;
    }
}
