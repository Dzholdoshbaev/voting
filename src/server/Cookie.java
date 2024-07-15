package server;

import utils.Common;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class Cookie<V> {
  private final String name;
  private final V value;
  private Integer maxAge;
  private boolean httpOnly;

  public Cookie(String name, V value) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(value);
    this.name = name.strip();
    this.value = value;
  }

  public static Map<String,String> parse(String cookieStr){
      return Common.parseUrlEncodedLogin(cookieStr,";");
  }
  
  public static <V> Cookie make(String name, V value) {
    return new Cookie<>(name, value);
  }

  public void setMaxAge(Integer maxAgeInSeconds) {
    this.maxAge = maxAgeInSeconds;
  }

  public void setHttpOnly(boolean httpOnly) {
    this.httpOnly = httpOnly;
  }

  private V getValue() { return value; }
  private Integer getMaxAge() { return maxAge; }
  private String getName() { return name; }
  private boolean isHttpOnly() { return httpOnly; }

  @Override
  public String toString() {
   StringBuilder sb = new StringBuilder();
    Charset utf8 = StandardCharsets.UTF_8;
    String encName = URLEncoder.encode(getName().strip(),utf8);
    String strValue = getValue().toString();
    String encValue = URLEncoder.encode(strValue,utf8);
    sb.append(String.format("%s=%s",encName,encValue));

    if (getMaxAge() != null){
      sb.append(String.format("; Max-Age=%s",getMaxAge()));
    }

    if (isHttpOnly()){
      sb.append("; HttpOnly");
    }

    return sb.toString();
  }
}