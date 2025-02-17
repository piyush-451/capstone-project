package com.Capstone.Ecommerce.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
public class UtilsFunctions {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> extractListFromClaims(Claims claims, String key){
        return objectMapper.convertValue(claims.get("roles"), new TypeReference<List<String>>() {});
    }

    public Long extractIdFromPrincipal(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.parseLong(objectMapper.convertValue(principal, new TypeReference<Map<String,String>>() {}).get("id")); ///specific type reference
    }

    public String extractEmailFromPrincipal(){
        System.out.println("Utils Function");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return objectMapper.convertValue(principal, new TypeReference<Map<String,String>>() {}).get("email");  /////generic type reference
    }

    public <T> void updateObject(Object source, T target) {
        Class<?> dtoClass = source.getClass();
        Class<?> entityClass = target.getClass();

        for (Field dtoField : dtoClass.getDeclaredFields()) {
            try {
                if ("id".equals(dtoField.getName())) {
                    continue;
                }

                Field entityField = entityClass.getDeclaredField(dtoField.getName());

                // Skip if types do not match
                System.out.println();
                System.out.println(dtoField.getName() + " " + dtoField.getType());
                if (!isTypeCompatible(dtoField, entityField)) {
                    System.out.println();
                    System.out.println(dtoField.getName());
                    continue;
                }

                dtoField.setAccessible(true);
                Object dtoValue = dtoField.get(source);

                if (dtoValue != null) {
                    entityField.setAccessible(true);
                    entityField.set(target, dtoValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // Field doesn't exist in entity or can't be accessed → Ignore
            }
        }
    }

    public static boolean isTypeCompatible(Field dtoField, Field entityField) {
        Type dtoType = dtoField.getGenericType();
        Type entityType = entityField.getGenericType();

        // ✅ Ensure both types are exactly the same (including generics)
        return dtoType.equals(entityType);
    }

    public String getJwtToken() {
        return SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
    }

}
