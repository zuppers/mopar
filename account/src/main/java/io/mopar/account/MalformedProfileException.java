package io.mopar.account;

import java.io.IOException;

/**
 * Created by hadyn on 7/27/2015.
 */
public class MalformedProfileException extends IOException {
    public MalformedProfileException(String s) {
        super(s);
    }

    public MalformedProfileException(Exception ex) {
        super(ex);
    }
}
