package com.jvmbytes.autoui.mvnrepo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Try to auto visit mvnrepository.com to get repository CVE information.
 * BUT,IT'S NOT WORK.
 *
 * @author tiltwind
 */
@RestController
@SpringBootApplication
public class MvnRepoAutoUI {
    public static void main(String[] args) {
        SpringApplication.run(MvnRepoAutoUI.class, args);
    }

    private WebDriver driver;

    @PostConstruct
    public void init() {
        // config chrome driver
        // System.setProperty("webdriver.chrome.driver", webDriverPath);

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("no-sandbox");
        options.addArguments("disable-extensions");
        options.addArguments("no-default-browser-check");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        // 禁用保存密码提示框
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);

        //设置寻找一个元素的时间
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @RequestMapping("/repo")
    public String repo() throws Exception {

        String url = "https://mvnrepository.com/artifact/log4j/log4j/1.2.14";

        System.out.println("start to open url:" + url);
        driver.get(url);

        System.out.println("sleep 10s");
        Thread.sleep(10000);

        System.out.println("switch to iframe");
        driver.switchTo().frame(0);

        try {
            System.out.println("click button");

            // 当前这个点击动作没有效果，还是会重新刷新机器人认证
            // cloudflare anti-bot
            driver.findElement(By.xpath("//*[@id=\"cf-stage\"]/div[6]/label/span")).click();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            driver.findElement(By.xpath("//*[@id=\"checkbox\"]")).click();
            System.out.println("click button");
        }

        System.out.println("sleep 5s");
        Thread.sleep(5000);

        System.out.println(driver.getPageSource());

        return "Hello World!";
    }
}
