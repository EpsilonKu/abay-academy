package kz.bitter.project.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    ADMIN("ROLE_ADMIN"),
    TEACHER("ROLE_TEACHER"),
    USER("ROLE_USER");

    private String authority;

    Roles(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}
