package org.halkneistiyor.datamodel;


import java.io.Serializable;
import java.util.Set;

public class SocialUser implements Serializable
{
    public static final String KIND = "User";

    private static final long serialVersionUID = 1L;

    String userId;
    String email;
    String nickname;
    String firstName;
    String lastName;
    Set<UserRole> roles;
    boolean enabled;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Set<UserRole> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<UserRole> roles)
    {
        this.roles = roles;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("User [userId=").append(userId).append(", email=")
            .append(email).append(", enabled=").append(enabled).append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        SocialUser user = (SocialUser) o;

        return !(userId != null ? !userId.equals(user.userId) : user.userId != null);
    }

    @Override
    public int hashCode()
    {
        return userId != null ? userId.hashCode() : 0;
    }
}
