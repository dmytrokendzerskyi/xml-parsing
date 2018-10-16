package com.parsing;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class User implements Serializable {

    @XmlElement
    private Long id;
    @XmlElement
    private String name;
    @XmlElement
    private Integer age;
    @XmlElement
    private Gender gender;
    @XmlElement
    private String active;
    @XmlElement(name = "role")
    @XmlElementWrapper(name = "roles")
    private List<Role> roles;

    public User() {
    }

    public User(Long id, String name, Integer age, Gender gender, String active, List<Role> roles) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.active = active;
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", active='" + active + '\'' +
                ", roles=" + roles +
                '}';
    }

    public enum Gender{
        MALE,FEMALE
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Role{
        private Long id;
        private String name;

        public Role() {
        }

        public Role(Long id, String name) {
            this.id = id;
            this.name = name;
           }

        @Override
        public String toString() {
            return "Role{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
