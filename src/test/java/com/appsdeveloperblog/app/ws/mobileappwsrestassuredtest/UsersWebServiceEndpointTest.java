package com.appsdeveloperblog.app.ws.mobileappwsrestassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class UsersWebServiceEndpointTest {

  private final String CONTEXT_PATH = "/mobile-app-ws";
  private final String EMAIL_ADDRESS = "contact.sean.mayer@gmail.com";
  private final String JSON = "application/json";
  private static String authorizationHeader;
  private static String userId;

  @BeforeEach
  void setUp() throws Exception {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8080;
  }

  /*
   * Test user login
   */
  @Test
  void a() {
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

      authorizationHeader = response.header("Authorization");
      userId = response.header("UserID");

      assertNotNull(authorizationHeader);
      assertNotNull(userId);
  }

  /*
   * Test get user details
   */
  @Test
  void b() {
    Response response = RestAssured
      .given()
      .pathParam("id", userId)
      .header("Authorization", authorizationHeader)
      .accept(JSON)
      .when()
      .get(CONTEXT_PATH + "/users/{id}")
      .then()
      .statusCode(200)
      .contentType(JSON)
      .extract()
      .response();

      String userPublicId = response.jsonPath().getString("userId");
      String email = response.jsonPath().getString("email");
      String firstName = response.jsonPath().getString("firstName");
      String lastName = response.jsonPath().getString("lastName");
      List<Map<String, String>> addresses = response.jsonPath().getList("addresses");
      /* addressId currently null, this is a one to many relationship which can be invoked with /mobile-app-ws/users/{id}/addresses */
      //String addressId = addresses.get(0).get("addressId");

      assertNotNull(userPublicId);
      assertNotNull(email);
      assertNotNull(firstName);
      assertNotNull(lastName);
      //assertTrue(addresses.size() == 2);
      //assertEquals(EMAIL_ADDRESS, email);
      //assertTrue(addressId.length() == 30);
  }
}
