package Modules;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import util.TestBase;
import util.TestUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class QaTest extends TestBase {





    @Parameters("testEnv")
    @Test(groups = {"Regression"})
    public void LoginError() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
        TestUtils.testTitle("Test Login");
        Thread.sleep(1000);

        //Enter Username
        getDriver().findElement(By.xpath("//input[@name='UserName']")).sendKeys("C-7062554521");
        //Enter password
        getDriver().findElement(By.xpath("//input[@name='Password']")).sendKeys("12345");
        //Proceed to Login
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='small p-1']")));
        TestUtils.assertSearchText("XPATH", "//p[@class='small p-1']", "account.login.wrongcredentials.usernotexist");

        //Screenshot of Error Message
        String screenshotPath = TestUtils.addScreenshot();
        testInfo.get().addScreenCaptureFromBase64String(screenshotPath);
        Thread.sleep(1000);


        Thread.sleep(500);


    }

    @Parameters("testEnv")
    @Test(groups = {"Regression"})
    public void AddToCart() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
        TestUtils.testTitle("Add to Cart");
        Thread.sleep(1000);

        TestBase.Login();

        //Click on Stores
        getDriver().findElement(By.xpath("//div[@class='sc-pjSSY gwxrXL']//*[name()='svg']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='sc-fznzqM kcMtBa mb-3']")));
        TestUtils.assertSearchText("XPATH","//h1[@class='sc-fznzqM kcMtBa mb-3']","Magasins");

        //Click on Select
        getDriver().findElement(By.xpath("//p[@class='px-1 store_link']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[normalize-space()='Mon Panier']")));
        TestUtils.assertSearchText("XPATH","//p[normalize-space()='Mon Panier']","Mon Panier");
        Thread.sleep(500);

        // Add required Items

        //Auntie B Spaghettini
        getDriver().findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/main[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[3]/div[2]/span[1]")).click();
        //Caprisonne
        getDriver().findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/main[1]/div[3]/div[1]/div[1]/div[2]/div[1]/div[4]/div[1]/div[3]/div[2]/span[2]")).click();
    }
}