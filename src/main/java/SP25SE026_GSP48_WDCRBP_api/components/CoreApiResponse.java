package SP25SE026_GSP48_WDCRBP_api.components;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class CoreApiResponse<T> extends ResponseEntity<CoreResponse<T>> {
    public CoreApiResponse(HttpStatusCode status) {
        super(status);
    }

    public CoreApiResponse(CoreResponse<T> body, HttpStatusCode status) {
        super(body, status);
    }

    public CoreApiResponse(MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(headers, status);
    }

    public CoreApiResponse(CoreResponse<T> body, MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(body, headers, status);
    }

    public CoreApiResponse(CoreResponse<T> body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public static <T> CoreApiResponse<T> success(T data) {
        CoreResponse<T> coreResponse= new CoreResponse<>(
                HttpStatus.OK.value(),
                "Success",
                System.currentTimeMillis(),
                data
        );
        return new CoreApiResponse<>(coreResponse, HttpStatus.OK);
    }

    public static <T> CoreApiResponse<T> success(String message) {
        CoreResponse<T> coreResponse= new CoreResponse<>(
                HttpStatus.OK.value(),
                message,
                System.currentTimeMillis(),
                null
        );
        return new CoreApiResponse<>(coreResponse, HttpStatus.OK);
    }

    public static <T> CoreApiResponse<T> success(T data,String message) {
        CoreResponse<T> coreResponse= new CoreResponse<>(
                HttpStatus.OK.value(),
                message,
                System.currentTimeMillis(),
                data
        );
        return new CoreApiResponse<>(coreResponse, HttpStatus.OK);
    }

    public static <T> CoreApiResponse<T> error(HttpStatus status,String message,T data) {
        CoreResponse<T> coreResponse = new CoreResponse<>(
                status.value(),
                message,
                System.currentTimeMillis(),
                data
        );
        return new CoreApiResponse<>(coreResponse, status);
    }
    public static <T> CoreApiResponse<T> error(String message,T data) {
        CoreResponse<T> coreResponse = new CoreResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message,
                System.currentTimeMillis(),
                data
        );
        return new CoreApiResponse<>(coreResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public static <T> CoreApiResponse<T> error(HttpStatus status,T data) {
        CoreResponse<T> coreResponse = new CoreResponse<>(
                status.value(),
                status.getReasonPhrase(),
                System.currentTimeMillis(),
                data
        );
        return new CoreApiResponse<>(coreResponse, status);
    }
    public static <T> CoreApiResponse<T> error(HttpStatus status,String message) {
        CoreResponse<T> coreResponse = new CoreResponse<>(
                status.value(),
                message,
                System.currentTimeMillis(),
                null
        );
        return new CoreApiResponse<>(coreResponse,status);
    }
    public static <T> CoreApiResponse<T> error(T data) {
        CoreResponse<T> coreResponse = new CoreResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                System.currentTimeMillis(),
                data
        );
        return new CoreApiResponse<>(coreResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public T getData() {
        CoreResponse<T> coreResponse = this.getBody();
        return (coreResponse != null) ? coreResponse.getData() : null;
    }
}
