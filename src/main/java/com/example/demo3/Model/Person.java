package com.example.demo3.Model;

import java.util.List;

public class Person {

        private int id;
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private List<String> role;


        public Person() {}

        public Person(String firstName, String lastName,String username, String email,List<String> role) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username=username;
            this.email = email;
            this.role=role;
        }


        public int getId() {
            return id;
        }

        public void setId(int id){
            this.id=id;
        }


        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername(){
            return username;
        }
        public void setUsername(String username){
            this.username=username;
        }
        public List<String>getRole(){
            return role;
        }
        public void setRole(List<String> role){
            this.role=role;
        }

}
