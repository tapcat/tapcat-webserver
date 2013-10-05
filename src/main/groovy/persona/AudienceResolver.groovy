package persona

public interface AudienceResolver {
    String resolve(String url) throws AudienceResolveException
}

public class AudienceResolveException extends Exception {
    AudienceResolveException() {
    }

    AudienceResolveException(String s) {
        super(s)
    }

    AudienceResolveException(String s, Throwable throwable) {
        super(s, throwable)
    }

    AudienceResolveException(Throwable throwable) {
        super(throwable)
    }

    AudienceResolveException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1)
    }
}
