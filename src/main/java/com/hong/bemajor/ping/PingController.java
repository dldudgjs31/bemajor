package com.hong.bemajor.ping;

import com.hong.bemajor.common.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class PingController {
    @GetMapping("/test")
    public String home(Model model) {
        return "test";
    }

    @GetMapping("/api/ping")
    @ResponseBody
    public ApiResponse<Map<String, String>> ping() {
        return ApiResponse.success(Collections.singletonMap("ping", "pong"));
    }
    @GetMapping("/orders")
    public String orders(Model model) {
        // fragments/header/sidebar/footer + ordersFragment 조합
        model.addAttribute("pageTitle", "Orders");
        return "pages/orders";
    }
    @GetMapping("/api/orders")
    @ResponseBody
      public List<Map<String,Object>> findAll() {
          return List.of(
              Map.of(
                  "id", 1L,
                  "status", "접수완료",
                  "amount", 15000,
                  "orderDate", "2025-04-27"
              ),
              Map.of(
                  "id", 2L,
                  "status", "배송중",
                  "amount", 23000,
                  "orderDate", "2025-04-28"
              ),
              Map.of(
                  "id", 3L,
                  "status", "배송완료",
                  "amount", 9800,
                  "orderDate", "2025-04-26"
              )
          );
      }

      @GetMapping("/api/orders/metadata")
      @ResponseBody
      public Map<String,Object> metadata() {
          return Map.of(
              "statuses", List.of(
                  Map.of("code","접수완료","name","접수완료"),
                  Map.of("code","배송중","name","배송중"),
                  Map.of("code","배송완료","name","배송완료")
              ),
              "defaultPageSize", 20
          );
      }
}
