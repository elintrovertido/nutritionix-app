package com.nutritionix.authentication_service.utils;

import java.util.Set;

public enum Roles {
    ADMIN(Set.of(
            Permissions.NUTRITION_ADD,
            Permissions.NUTRITION_VIEW,
            Permissions.NUTRITION_DELETE,
            Permissions.WISHLIST_ADD,
            Permissions.WISHLIST_VIEW,
            Permissions.WISHLIST_DELETE,
            Permissions.USER_ADD,
            Permissions.USER_VIEW,
            Permissions.USER_DELETE
    )),
    USER(Set.of(
            Permissions.NUTRITION_VIEW,
            Permissions.WISHLIST_ADD,
            Permissions.WISHLIST_VIEW,
            Permissions.WISHLIST_DELETE
    )),
    PREMIUM_USER(Set.of(
            Permissions.NUTRITION_ADD,
            Permissions.NUTRITION_VIEW,
            Permissions.NUTRITION_DELETE,
            Permissions.WISHLIST_ADD,
            Permissions.WISHLIST_VIEW,
            Permissions.WISHLIST_DELETE
    )),
    NUTRITIONIST(Set.of(
            Permissions.NUTRITION_ADD,
            Permissions.NUTRITION_VIEW,
            Permissions.NUTRITION_DELETE
    ));

   private final Set<Permissions> permissions;

   Roles(Set<Permissions> permissions){
       this.permissions = permissions;
   }

   public Set<Permissions> getPermissions(){
       return this.permissions;
   }
}
