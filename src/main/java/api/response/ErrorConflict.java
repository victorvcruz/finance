package api.response;

public class ErrorConflict {

    private String message;
    public ErrorConflict(){
        this.message = "This user already exists";
    }

}

