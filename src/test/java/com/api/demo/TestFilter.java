package com.api.demo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * @author jingLv
 * @date 2021/01/04
 */
public class TestFilter {

    private static String token;
    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8886/v1";
        String loginBody = "{\n" +
                "    \"username\":\"xiaohong\",\n" +
                "    \"password\":\"123123\"\n" +
                "}";
        // 请求模板构造类，构造请求实例
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBody(loginBody);
        requestSpecification = requestSpecBuilder.build();

        // 响应模板构造类，构造响应实例
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(200);
        responseSpecification = responseSpecBuilder.build();
    }

    /**
     * 利用Filter过滤器实现request请求的修改
     */
    @Test
    void testLoginRequest() {
        given()
                .filter((filterableRequestSpecification, filterableResponseSpecification, filterContext) -> {
                    String loginBody = "{\n" +
                            "    \"username\":\"xiaohei\",\n" +
                            "    \"password\":\"123123\"\n" +
                            "}";
                    // 修改ContentType的类型
                    filterableRequestSpecification.body(loginBody);
                    return filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
                })
                .log().all()
                .spec(requestSpecification)
                .when()
                .get("/user/login")
                .then()
                .spec(responseSpecification)
                .log();
    }

    /**
     * 利用Filter过滤器实现response请求的修改
     */
    @Test
    void testLoginResponse() {
        Response response = given()
                .log().all()
                .spec(requestSpecification)
                .when()
                .get("/user/login");
        Response newResponse = new ResponseBuilder().clone(response)
                .setBody("这是一个修改的Response").build();
        newResponse.then()
                .spec(responseSpecification)
                .log().all();
    }
}
