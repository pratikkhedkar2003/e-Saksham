package com.sarthi.e_Saksham.constant.query;

public final class UserMstQuery {

    public static final String UPDATE_RESET_LOGIN_ATTEMPT_BY_USERNAME =
            """
            UPDATE esaksham_users_mst
            SET login_attempts = 0,
                updated_at = :updatedAt,
                updated_by = :updatedBy
            WHERE user_name = :username
            """;

    private UserMstQuery() {
        throw new AssertionError("No com.sarthi.e_Saksham.constant.query.UserMstQuery instances for you!");
    }
}
