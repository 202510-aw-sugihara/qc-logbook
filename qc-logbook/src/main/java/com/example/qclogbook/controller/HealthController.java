package com.example.qclogbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 起動確認用の画面を返すコントローラクラス。
 */
@Controller
public class HealthController {

    /**
     * 起動確認用の画面を返す。
     *
     * @return Thymeleafテンプレート名
     */
    @GetMapping("/health")
    public String health() {
        return "health";
    }
}