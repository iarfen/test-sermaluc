package com.sermalucTest.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;
    
    @Column
    @NotNull
    @NotEmpty
    public String name;

    @Column
    @NotNull
    @NotEmpty
    @Pattern(regexp="[A-Za-z0-9.-]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+", message="Please provide a valid email address")
    public String email;

    @Column
    @NotNull
    @NotEmpty
    @Pattern(regexp="[A-Za-z0-9]+[0-9][0-9]", message="Please provide a valid password")
    public String password;

    @Column
    @OneToMany
    public List<Phone> phones;

    @Column
    @NotNull
    @NotEmpty
    public Date createdAt;

    @Column
    @NotNull
    @NotEmpty
    public Date modifiedAt;

    @Column
    @NotNull
    @NotEmpty
    public Date lastLogin;

    @Column
    @NotNull
    @NotEmpty
    public String token;

    @Column
    @NotNull
    @NotEmpty
    public Boolean isActive;
}
