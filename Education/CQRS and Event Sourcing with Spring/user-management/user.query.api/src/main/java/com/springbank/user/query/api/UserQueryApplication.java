package com.springbank.user.query.api;

import com.springbank.user.core.conf.AxonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AxonConfig.class})
public class UserQueryApplication {
  private static String s;

  public static void main2(String[] args) {
    SpringApplication.run(UserQueryApplication.class, args);
  }


  // Function that print all combinations of
  // balanced parentheses
  // open store the count of opening parenthesis
  // close store the count of closing parenthesis
  static void _printParenthesis(char str[], int pos, int n, int open, int close) {
    if (close == n) {
      // print the possible combinations
      for (int i = 0; i < str.length; i++) {
        System.out.print(str[i]);
      }
      System.out.println();
      return;
    } else {
      if (open > close) {
        str[pos] = '}';
        _printParenthesis(str, pos + 1, n, open, close + 1);
      }
      if (open < n) {
        str[pos] = '{';
        _printParenthesis(str, pos + 1, n, open + 1, close);
      }
    }
  }

  // Wrapper over _printParenthesis()
  static void printParenthesis(char str[], int n) {
    if (n > 0) {
      _printParenthesis(str, 0, n, 0, 0);
    }
    return;
  }

  // driver program
  public static void main(String[] args) {
    int n = 3;
    char[] str = new char[2 * n];
    printParenthesis(str, n);
  }

// Contributed by Pramod Kumar

}
