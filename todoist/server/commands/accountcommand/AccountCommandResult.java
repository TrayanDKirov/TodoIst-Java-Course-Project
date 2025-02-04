package bg.sofia.uni.fmi.mjt.project.todoist.server.commands.accountcommand;

import bg.sofia.uni.fmi.mjt.project.todoist.server.user.User;

public class AccountCommandResult {
    private String output;
    private User user;
    private String passwordOfUser;

    private AccountCommandResult(AccountCommandResult.Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("Null reference in AccountCommandResult constructor. ");
        }

        this.output = builder.output;
        this.user = builder.user;
        this.passwordOfUser = builder.passwordOfUser;
    }

    public String getOutput() {
        return this.output;
    }

    public boolean isUserAndPasswordSet() {
        return user != null && passwordOfUser != null;
    }

    public User getUser() {
        if (!isUserAndPasswordSet()) {
            throw new RuntimeException("Parameter in AccountCommandResult is not set. ");
        }

        return this.user;
    }

    public String getPasswordOfUser() {
        if (!isUserAndPasswordSet()) {
            throw new RuntimeException("Parameter in AccountCommandResult is not set. ");
        }

        return this.passwordOfUser;
    }

    public static class Builder {
        private String output;
        private User user;
        private String passwordOfUser;

        public Builder(String output) {
            if (output == null) {
                throw new IllegalArgumentException("Null reference in AccountCommandResult::Builder constructor. ");
            }

            this.output = output;
        }

        public Builder setUserAndPassword(User user, String passwordOfUser) {
            if (user == null || passwordOfUser == null) {
                throw new IllegalArgumentException("Null reference in AccountCommandResult::Builder::setUserAndPassword. ");
            }

            this.user = user;
            this.passwordOfUser = passwordOfUser;

            return this;
        }

        public AccountCommandResult build() {
            return new AccountCommandResult(this);
        }
    }
}
