package com.crististinca.CarRental.Utils;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class WClient {

    public final static String url = "http://localhost:8080/api/v1";

    public static String auth_token = "";

    public static Optional<String> getUsername() {
        if (auth_token.isEmpty()) {
            return Optional.empty();
        }

        Optional<JsonObject> jsonOptionalObject = getJsonObj();
        if (jsonOptionalObject.isEmpty()) {
            return Optional.empty();
        }

        JsonObject jsonObject = jsonOptionalObject.get();

        String email = jsonObject.get("e").getAsString();

        return Optional.of(email);
    }

    public static Optional<List<String>> getRoles() {
        if (auth_token.isEmpty()) {
            return Optional.empty();
        }

        Optional<JsonObject> jsonOptionalObject = getJsonObj();
        if (jsonOptionalObject.isEmpty()) {
            return Optional.empty();
        }

        JsonObject jsonObject = jsonOptionalObject.get();

        String roles = jsonObject.get("au").getAsString();

        List<String> rolesList = Arrays.asList(roles.split(","));

        return Optional.of(rolesList);
    }

    private static Optional<JsonObject> getJsonObj() {
        if (auth_token.isEmpty()) {
            return Optional.empty();
        }

        String[] chunks = auth_token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

        return Optional.ofNullable(jsonObject);
    }
}
