package com.example.demo3.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RolePermissionDTO {

        @JsonProperty("role")
        private String role;

        @JsonProperty("permissions")
        private Long permissions;

        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }

        public Long getPermissions() {
                return permissions;
        }

        public void setPermissions(Long permissions) {
                this.permissions = permissions;
        }

}
