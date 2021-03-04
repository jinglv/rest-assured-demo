package com.api.demo;

import cn.hutool.core.codec.Base64;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseBuilder;
import io.restassured.config.SessionConfig;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * 接口加解密处理
 *
 * @author jingLv
 * @date 2021/01/07
 */
public class TestEncode {

    /**
     * 直接请求接口
     */
    @Test
    void testEncryption() {
        given()
                .log().all()
                .baseUri("http://localhost:9090")
                .when()
                .get("/api/encryption")
                .then()
                .log().all()
                .extract()
                .response();

    }

    /**
     * 使用filter处理请求返回body加密的情况
     */
    @Test
    void testEncode() {
        given().baseUri("http://localhost:9090")
                .log().all()
                .filter((request, response, context) -> {
                    // 发送原始请求，但是返回的Response不具备set方法，无法修改body
                    Response resOrigin = context.next(request, response);
                    // 解密过程
                    String raw = Base64.decodeStr(resOrigin.body().asString());
                    // 响应构造器，ResponseBuilder的作用主要是在Response的基础上建设出来一个新的可以修改的body对象（克隆出来一个新的）
                    ResponseBuilder resBuilder = new ResponseBuilder().clone(resOrigin);
                    //Response无法直接修改body，所有间接的通过ResponseBuilder构建
                    resBuilder.setBody(raw);
                    //ResponseBuilder在最后通过build方法直接创建一个用于返回的不可修改的Response
                    return resBuilder.build();
                })
                .when()
                .get("/api/encryption").prettyPeek()
                .then()
                .log().all()
                .body("books.name[0]", equalTo("西游记"))
                .statusCode(200);
    }

    @Test
    void testJenkinsLogin() {
        RestAssured.config = RestAssured.config().sessionConfig(
                new SessionConfig().sessionIdName("JSESSIONID.7ef748a1"));
        SessionFilter sessionFilter = new SessionFilter();

        given().log().all()
                .filter(sessionFilter)
                .queryParam("j_password", "Lj123!@#")
                .queryParam("Submit", "登录")
                .queryParam("j_username", "lvjing")
                .when().post("http://60.205.228.49:8888/j_acegi_security_check")
                .then()
                .statusCode(302);

        given().log().all().filter(sessionFilter)
                .when().get("http://60.205.228.49:8888/job/Remodeling/").prettyPeek()
                .then().statusCode(200);
    }
}
