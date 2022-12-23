package com.appsdeveloperblog.app.ws.mobileappwsrestassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UsersWebServiceEndpointTest {

  private final String CONTEXT_PATH = "/mobile-app-ws";
  private final String EMAIL_ADDRESS = "test@test.com";
  private final String JSON = "application/json";

  @BeforeEach
  void setUp() throws Exception {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8080;
  }

  @Test
  void testGetLogin() {
    Map<String, String> loginDetails = new HashMap<>();
    loginDetails.put("email", EMAIL_ADDRESS);
    loginDetails.put("password", "123456");

    Response response = RestAssured
      .given()
      .contentType(JSON)
      .accept(JSON)
      .body(loginDetails)
      .when()
      .post(CONTEXT_PATH + "/users/login")
      .then()
      .statusCode(200)
      .extract()
      .response();

      String authorizationHeader = response.header("Authorization");
      String userId = response.header("UserID");

      assertNotNull(authorizationHeader);
      assertNotNull(userId);
  }
}
