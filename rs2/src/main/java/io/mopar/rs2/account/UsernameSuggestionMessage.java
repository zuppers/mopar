package io.mopar.rs2.account;

import io.mopar.core.msg.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class UsernameSuggestionMessage extends Message {

    /**
     *
     */
    private List<String> suggestedUsernames = new ArrayList<>();

    /**
     *
     */
    public UsernameSuggestionMessage() {}

    /**
     *
     * @param username
     */
    public void addUsername(String username) {
        suggestedUsernames.add(username);
    }

    /**
     *
     * @return
     */
    public List<String> getSuggestedUsernames() {
        return suggestedUsernames;
    }
}
