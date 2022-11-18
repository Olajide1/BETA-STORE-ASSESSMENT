package Modules;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import util.TestBase;
import util.TestUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Login  extends TestBase {

    private String email;
    private String pw;


    private StringBuffer verificationErrors = new StringBuffer();

    @Parameters({"testEnv"})
    @BeforeMethod
    public void parseJson(String testEnv) throws IOException, ParseException {
        File path = null;
        File classpathRoot = new File(System.getProperty("user.dir"));
        if (testEnv.equalsIgnoreCase("StagingData")) {
            path = new File(classpathRoot, "stagingData/data.conf.json");

            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(new FileReader(path));
            JSONObject envs = (JSONObject) config.get("Agent_Login");

            email = (String) envs.get("email");
            pw = (String) envs.get("pw");

        }
    }

    @Parameters("testEnv")
    @Test(groups = {"Regression"})
    public void validLogin() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
        TestUtils.testTitle("Test Login");
        Thread.sleep(1000);

        //Enter Email
        getDriver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(email);
        //Enter password
        getDriver().findElement(By.xpath("//*[@id=\"pass\"]")).sendKeys(pw);

        Thread.sleep(500);
    }
}