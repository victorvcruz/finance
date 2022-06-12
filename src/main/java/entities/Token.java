package entities;


public class Token {

    private String token;


    public Token(String tokenString){
        token = tokenString;
    }

    public String token() {
        return token;
    }

    public void setTokenString(String token) {
        this.token = token;
    }
}
