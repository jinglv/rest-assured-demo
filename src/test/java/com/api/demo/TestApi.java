package com.api.demo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Rest-Assured请求处理，发送不同的Request
 *
 * @author jingLv
 * @date 2021/01/04
 */
public class TestApi {

    private static String token;
    private static final Integer USER_ID = 1;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8886/v1";
        String loginBody = "{\n" +
                "    \"username\":\"xiaohong\",\n" +
                "    \"password\":\"123123\"\n" +
                "}";
        token = given()
                .when()
                .contentType("application/json")
                .body(loginBody)
                .get("/user/login")
                .then()
                .log().body()
                .extract().response().path("data.token");
    }

    /**
     * 发送post请求 -- 创建用户信息
     */
    @Test
    @DisplayName("创建用户信息")
    void addUserInfo() {
        String userInfoBody = "{\n" +
                "    \"userId\":" + USER_ID + ",\n" +
                "    \"userName\":\"小红\",\n" +
                "    \"email\":\"xiaohong@qq.com\",\n" +
                "    \"phone\":\"18623456543\",\n" +
                "    \"friends\":[\n" +
                "        {\n" +
                "            \"userId\":11,\n" +
                "            \"userName\":\"小红喵\",\n" +
                "            \"email\":\"xiaohongmiao@qq.com\",\n" +
                "            \"phone\":\"18623456555\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"userId\":12,\n" +
                "            \"userName\":\"小红旺\",\n" +
                "            \"email\":\"xiaohongwang@qq.com\",\n" +
                "            \"phone\":\"18623456566\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("token", token)
                .body(userInfoBody)
                .when()
                .post("/info/user")
                .then()
                .log()
                .status().statusCode(200);
    }

    /**
     * 发送get请求 -- 查询所有用户信息
     */
    @Test
    @DisplayName("查询所有用户信息")
    void findAllUserInfo() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("token", token)
                .when()
                .post("/info/user")
                .then()
                .log()
                .status().statusCode(200);
    }

    /**
     * 发送put请求 -- 更新指定用户的部分信息
     */
    @Test
    @DisplayName("更新指定用户的部分信息")
    void updateUserInfoByUserId() {
        String updateUserInfoById = "{\n" +
                "    \"userId\":" + USER_ID + ",\n" +
                "    \"userName\":\"小红红\",\n" +
                "    \"email\":\"xiaohong红@qq.com\",\n" +
                "    \"phone\":\"18623456543\"\n" +
                "}";
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("token", token)
                .body(updateUserInfoById)
                .when()
                .put("/info/user/" + USER_ID)
                .then()
                .log()
                .status().statusCode(200);
    }

    /**
     * 发送delete请求 -- 删除指定用户信息
     */
    @Test
    @DisplayName("删除指定用户信息")
    void deleteUserInfoByUserId() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("token", token)
                .when()
                .delete("/info/user/" + USER_ID)
                .then()
                .log()
                .status().statusCode(200);
    }
}
