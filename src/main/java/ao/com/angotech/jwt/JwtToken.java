package ao.com.angotech.jwt;

public class JwtToken {

    String token;

    public JwtToken() {

    }

    public JwtToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
