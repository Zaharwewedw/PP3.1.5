package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table
public class  User {
    @NotEmpty(message = "The name cannot be empty")
    @Size(min = 2, max = 15, message = "The name must consist of less than 2 and no more than 15 characters")
    private String name;
    @Min(value = 14, message = "Access to persons under 14 years of age is prohibited")
    @Max(value = 150, message = "The age is too great")
    private int age;
    @Email(message = "The email does not meet the requirements")
    @NotEmpty(message = "The email cannot be empty")
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-z0-9_-]{3,15}$", message = "Invalid username")
    private String usNa;


    @Size(min = 4, message = "The allowed password characters are at least 4")
    private String pass;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    private Set<Role> roleSet = new HashSet<>();

    public User() {

    }

    public User(String name, int age, String email, Set<Role> roleSet) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.roleSet = roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Role roleSet) {
        this.roleSet.add(roleSet);
    }

    public String getUsNa() {
        return usNa;
    }

    public void setUsNa(String usNa) {
        this.usNa = usNa;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String toStringHeader() {
        List<String> roleNames = roleSet.stream().map(a -> {
            if (a.getRoleUser().equals("ROLE_ADMIN")) {
                return "ADMIN";
            }  else {
                return "USER";
            }
        }
        ).sorted().collect(Collectors.toList());
        String rolesString = String.join(", ", roleNames);
        return String.format("%s.", rolesString);
    }

    public String getAdmin() {
        String dost = "NO ACTIVE";
        for (Role role : getRoleSet()) {
            if (role.getRoleUser().equals("ROLE_ADMIN")) {
                return "ACTIVE";
            }
        }
        return dost;
    }

    @Override
    public String toString() {
        return "   name = " + name +
                "  age = " + age +
                "  email = " + email + "   ";
    }
}
