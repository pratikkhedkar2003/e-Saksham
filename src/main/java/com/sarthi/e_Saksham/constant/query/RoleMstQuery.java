package com.sarthi.e_Saksham.constant.query;

public final class RoleMstQuery {

    public static final String SELECT_ROLE_BY_USER_ID_AND_CLIENT_ID =
            """
            SELECT
                erm.role_id,
                erm.role_name,
                erm.authorities,
                erm.client_id,
                erm.created_by,
                erm.updated_by,
                erm.created_at,
                erm.updated_at,
                erm.is_default
            FROM esaksham_roles_mst erm
            JOIN esaksham_user_role_mappings eurm
              ON eurm.role_id = erm.role_id
            WHERE erm.client_id = :clientId
              AND eurm.client_id = :clientId
              AND eurm.user_id = :userId
            """;

    public static final String SELECT_DEFAULT_ROLE_BY_CLIENT_ID =
            """
            SELECT
                erm.role_id,
                erm.role_name,
                erm.authorities,
                erm.client_id,
                erm.created_by,
                erm.updated_by,
                erm.created_at,
                erm.updated_at,
                erm.is_default
            FROM esaksham_roles_mst erm
            WHERE erm.client_id = :clientId
                AND erm.is_default = true
            """;

    private RoleMstQuery() {
        throw new AssertionError("No com.sarthi.e_Saksham.constant.query.RoleMstQuery instances for you!");
    }
}
