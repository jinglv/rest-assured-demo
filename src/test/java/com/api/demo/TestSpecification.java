package com.api.demo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * @author jingLv
 * @date 2021/01/04
 */
public class TestSpecification {

    private static String token;
    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8886/v1";
        // 请求模板构造类，构造请求实例
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecification = requestSpecBuilder.build();

        // 响应模板构造类，构造响应实例
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(200);
        responseSpecification = responseSpecBuilder.build();
    }

    /**
     * 使用模板功能来简化测试用例编写
     */
    @Test
    void testLogin() {
        String loginBody = "{\n" +
                "    \"username\":\"xiaohong\",\n" +
                "    \"password\":\"123123\"\n" +
                "}";
        given()
                .log().all()
                .spec(requestSpecification)
                .body(loginBody)
                .when()
                .get("/user/login")
                .then()
                .spec(responseSpecification)
                .log();
    }
}
