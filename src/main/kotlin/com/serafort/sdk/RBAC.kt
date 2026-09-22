package com.serafort.sdk

object RBAC {
    fun hasPermission(user: UserContext, required: String): Boolean {
        return hasPermission(user.permissions, required)
    }

    fun hasPermission(userPermissions: List<String>, required: String): Boolean {
        for (perm in userPermissions) {
            if (perm == "*") return true
            if (perm == required) return true
            if (perm.endsWith(":*")) {
                val prefix = perm.removeSuffix(":*")
                if (required.startsWith("$prefix:") || required == prefix) {
                    return true
                }
            }
        }
        return false
    }

    fun hasRole(user: UserContext, role: String): Boolean {
        return user.roles.contains(role)
    }

    fun hasTenant(user: UserContext, tenantId: String): Boolean {
        return user.tenantId == tenantId
    }
}
