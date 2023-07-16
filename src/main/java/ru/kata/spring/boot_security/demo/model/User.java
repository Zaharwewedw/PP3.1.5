package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class  User {
    @NotEmpty(message = "name:The name cannot be empty")
    @Size(min = 2, max = 15, message = "name:The name must consist of less than 2 and no more than 15 characters")
    private String name;
    @Min(value = 14, message = "age:Access to persons under 14 years of age is prohibited")
    @Max(value = 150, message = "age:The age is too great")
    private Integer age;
    @Email(message = "email:The email does not meet the requirements")
    @NotEmpty(message = "email:The email cannot be empty")
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-z0-9_-]{3,15}$", message = "username:Invalid username")
    private String username;


    @Size(min = 4, message = "password:The allowed password characters are at least 4")
    private String pass;

    @ManyToMany
    @JoinTable(name = "userss_roless")
    private Set<Role> roleSet = new HashSet<>();

    public User() {

    }

    public User(String name, int age, String email, Set<Role> roleSet, String pass, String username) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.roleSet = roleSet;
        this.pass = pass;
        this.username = username;
    }



    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public void addRole(Role role) {
        this.roleSet.add(role);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String usNa) {
        this.username = usNa;
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
