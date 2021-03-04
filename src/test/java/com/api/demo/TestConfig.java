package com.api.demo;

import io.restassured.RestAssured;
import io.restassured.config.HeaderConfig;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * @author jingLv
 * @date 2021/01/04
 */
public class TestConfig {

    private static String token;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8886/v1";
    }

    /**
     * Header配置实例
     */
    @Test
    void headerConfig() {
        // 使用headerConfig重写的方法，对test的header参数进行覆盖处理
        RestAssured.config = RestAssured.config().headerConfig(HeaderConfig.headerConfig().overwriteHeadersWithName("test"));
        String loginBody = "{\n" +
                "    \"username\":\"xiaohong\",\n" +
                "    \"password\":\"123123\"\n" +
                "}";
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("test", "aaa")
                .header("test", "bbb")
                .body(loginBody)
                .when()
                .get("/user/login")
                .then()
                .log()
                .body().statusCode(200);
    }

    /**
     * Log配置实例
     */
    @Test
    void logConfig() {
        // 使用logConfig的enablePrettyPrinting设置为false，响应返回body则不会进行格式化显示
        RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().enablePrettyPrinting(false));
        String loginBody = "{\n" +
                "    \"username\":\"xiaohong\",\n" +
                "    \"password\":\"123123\"\n" +
                "}";
        given()
                .log().all()
                .contentType(ContentType.JSON)

                .body(loginBody)
                .when()
                .get("/user/login")
                .then()
                .log()
                .body().statusCode(200);
    }
}
