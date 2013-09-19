package persona
import org.json.JSONException
import org.json.JSONObject

class BrowserIdResponse {

    private BrowserIdResponseStatus status
    private String email
    private String audience
    private long expires
    private String issuer
    private String reason

    private JSONObject jsonResponse

    /**
     *
     * @return status of the verification. Will be {@link BrowserIdResponseStatus#OK} if the assertion is valid.
     */
    public BrowserIdResponseStatus getStatus() {
        return status
    }

    /**
     *
     * @return email address (the identity) that was verified
     */
    public String getEmail() {
        return email
    }

    /**
     *
     * @return domain for which the assertion is valid
     */
    public String getAudience() {
        return audience
    }


    /**
     *
     * @return expiration date for the assertion
     */
    public Date getExpires() {
        return new Date(expires)
    }


    /**
     *
     * @return domain of the certifying authority
     */
    public String getIssuer() {
        return issuer
    }

    /**
     *
     * @return reason for verification failure
     */
    public String getReason() {
        return reason
    }

    private static class ResponseFields {
        static final String STATUS='status'
        static final String EMAIL='email'
        static final String AUDIENCE='audience'
        static final String EXPIRES='expires'
        static final String ISSUER='issuer'
        static final String REASON='reason'
    }

    /**
     *
     * @param response result of a call to a BrowserID verify service
     * @throws JSONException if the response cannot be parsed as JSON markup.
     */
    public BrowserIdResponse(String response) throws JSONException{
        jsonResponse = new JSONObject(response)
        status = BrowserIdResponseStatus.parse((String) jsonResponse.get(ResponseFields.STATUS))

        switch(status){
            case OK:
                email = jsonResponse.getString(ResponseFields.EMAIL)
                audience = jsonResponse.getString(ResponseFields.AUDIENCE)
                expires = jsonResponse.getLong(ResponseFields.EXPIRES)
                if(jsonResponse.has(ResponseFields.ISSUER)) issuer = jsonResponse.getString(ResponseFields.ISSUER)
                break
            case FAILURE:
                if(jsonResponse.has(ResponseFields.REASON)) reason = jsonResponse.getString(ResponseFields.REASON)
                break
        }

    }

    /**
     * BrowserID response status
     */
    public static enum BrowserIdResponseStatus {
        /**
         * Verification was successful. ('okay')
         */
        OK('okay'),
        /**
         * Verification failed. ('failure')
         */
        FAILURE('failure')

        private String value

        BrowserIdResponseStatus(String value){
            this.value = value
        }

        public String toString(){
            return value
        }

        public static BrowserIdResponseStatus parse(String value){
            OK.value.equals(value) ? OK : FAILURE
        }
    }

    public String toString(){
        return jsonResponse.toString()
    }
}
