package com.rodmontiel.ec.config;

import java.util.HashMap;

public class ResponseCodes {
  private final HashMap<String, String> codes;

  public ResponseCodes() {
    codes = new HashMap<String, String>();
    codes.put("100", "Generic Error");
    // User errors
    codes.put("110", "Invalid Credentials");
    codes.put("111", "The required user does not exists");
    codes.put("112", "The email you want to use is not available");
    codes.put("113", "User not found");
    codes.put("114", "User disabled");
    codes.put("115", "User does not exists, needs to register [OAuth2, third party, Google, Github, etc]");
    codes.put("116", "Unable to read your email address, probably you do not have any public email");
    codes.put("117", "Account not active");
    codes.put("118", "Incorrect password");
    codes.put("119", "OAuth needs to register");
    codes.put("120", "Invalid confirmation token");
    codes.put("121", "Confirmation token expired");
    // Session errors
    codes.put("130", "Invalid session");
    codes.put("131", "Expired session ");
    // OK code
    codes.put("200", "OK");
    // Category errors
    codes.put("281", "The category already exists");
    codes.put("282", "The category you want to delete does not exists");
    codes.put("283", "You cannot delete this category because it has associated expenses");
    codes.put("284", "You cannot delete this category because it has associated incomes");
    codes.put("285", "The category you want to modify does not exists");
    codes.put("286", "The new name you want to use is already in use");
    codes.put("287", "Category not found");
    // 
    codes.put("301", "Incorrect dates");
    // Expenses errors
    codes.put("311", "The expense you want to edit does not exists");
    codes.put("312", "Category linked to the expense does not exists");
    codes.put("313", "Expense not found");
    codes.put("314", "Error saving expense");
    codes.put("315", "The expense you want to delete does not exists");
    // Income errors
    codes.put("341", "The income you want to edit does not exists");
    codes.put("342", "The income you want to delete does not exists");
    codes.put("343", "Income not found");
    // Unhandled error
    codes.put("500", "Something wrong happened");
  }

  public String getErrorDescription(int code) {
    if(codes.containsKey("" + code))
      return codes.get("" + code);
    return "Generic message";
  }
}
