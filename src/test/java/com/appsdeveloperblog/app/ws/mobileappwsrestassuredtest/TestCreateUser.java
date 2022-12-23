package com.appsdeveloperblog.app.ws.mobileappwsrestassuredtest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCreateUser {

  private final String CONTEXT_PATH = "/mobile-app-ws";

  @BeforeEach
  public void setUp() throws Exception {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8080;
  }

  @Test
  void TestCreateUser() {

   List<Map<String, Object>> userAddresses = new ArrayList<>();

   Map<String, Object> shippingAddress = new HashMap<>();
   shippingAddress.put("city", "New York");
   shippingAddress.put("country", "USA");
   shippingAddress.put("streetName", "123 Street name");
   shippingAddress.put("postalCode","123456");
   shippingAddress.put("type","shipping");

   Map<String, Object> billingAddress = new HashMap<>();
   shippingAddress.put("city", "New York");
   shippingAddress.put("country", "USA");
   shippingAddress.put("streetName", "123 Street name");
   shippingAddress.put("postalCode","123456");
   shippingAddress.put("type","billing");

   userAddresses.add(shippingAddress);
   userAddresses.add(billingAddress);

   Map<String, Object> userDetails = new HashMap<>();
   userDetails.put("firstName", "John");
   userDetails.put("lastName", "Doe");
   userDetails.put("email", "test@test.com");
   userDetails.put("password", "123456");
   userDetails.put("addresses", userAddresses);


    Response response = given()
      .contentType("application/json")
      .accept("application/json")
      .body(userDetails)
      .when()
      .post(CONTEXT_PATH + "/users")
      .then()
      .statusCode(200)
      .contentType("application/json")
      .extract()
      .response();

     String userId = response.jsonPath().getString("userId");
     assertNotNull(userId);
     assertTrue(userId.length() == 30);

      String bodyString = response.body().asString();
      try {
        JSONObject responseBodyJson = new JSONObject(bodyString);
        JSONArray addresses = responseBodyJson.getJSONArray("addresses");

        assertNotNull(addresses);
        assertTrue(addresses.length() == 2);

        String addressId = addresses.getJSONObject(0).getString("addressId");
        assertNotNull(addressId);
        assertTrue(addressId.length() == 30);


      } catch (JSONException e) {
          fail(e.getMessage());
      }
  }
}
