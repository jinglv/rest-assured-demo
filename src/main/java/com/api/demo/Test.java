package com.api.demo;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

/**
 * @author jingLv
 * @date 2021/01/04
 */
public class Test {

    public static void main(String[] args) {
        String userLoginBody = "{\n" +
                "    \"username\":\"xiaohong\",\n" +
                "    \"password\":\"123123\"\n" +
                "}";
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userLoginBody)
                .when()
                .get("http://localhost:8886/v1/user/login")
                .then()
                .log().all();
    }
}
