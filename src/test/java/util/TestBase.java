package util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.browserstack.local.Local;

//import CACTestCases.SignIn;
//import DealerTestCases.signIn;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;



import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class TestBase {
    public static ExtentReports reports;
    public static ExtentHtmlReporter htmlReporter;
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
    public static ThreadLocal<ExtentTest> testInfo = new ThreadLocal<ExtentTest>();
    private static OptionsManager optionsManager = new OptionsManager();
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
    private Local l;
    public String local = "local";
    public String remoteJenkins = "remote-jenkins";

    public static String toAddress;
    public static final int waitTime = 90;

    @Parameters("testEnv")
    public static String myUrl(String testEnv) {

        String myUrl = null;
        if (testEnv.equalsIgnoreCase("stagingData")) {
            myUrl = System.getProperty("instance-url", "https://test.storefront.simplemarket.app/auth/signin");
        } else {
            myUrl = System.getProperty("instance-url", "https://test.storefront.simplemarket.app/auth/signin");
        }

        return myUrl;
    }

    public static String gridUrl = System.getProperty("grid-url", "http://10.152.89.194:4445/wd/hub");

    @BeforeSuite
    @Parameters({"groupReport", "testEnv"})
    public void setUp(String groupReport, String testEnv) throws Exception {

        htmlReporter = new ExtentHtmlReporter(new File(System.getProperty("user.dir") + groupReport));
        // htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir") + "/resources/extent-config.xml"));
        reports = new ExtentReports();
        reports.setSystemInfo("TEST ENVIRONMENT", myUrl(testEnv));
        reports.attachReporter(htmlReporter);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Parameters({"testEnv", "myBrowser", "config", "environment", "server"})
    @BeforeClass
    public void beforeClass(ITestContext iTestContext, String testEnv, String myBrowser, String config_file, String environment, String server) throws Exception {

        ExtentTest parent = reports.createTest(getClass().getName());
        parentTest.set(parent);



        /*if(server.equals(remoteJenkins)) {

            // Remote Server Directory
            String browser = "chrome";
            if(browser.equalsIgnoreCase(myBrowser)) {
                DesiredCapabilities capability = DesiredCapabilities.chrome();
                capability.setCapability(ChromeOptions.CAPABILITY, optionsManager.getChromeOptions());
                capability.setBrowserName(myBrowser);
                capability.setCapability("name", myUrl(testEnv) +" ["+ iTestContext.getName()+"]");
                capability.setPlatform(Platform.ANY);
                capability.setCapability("idleTimeout", 300);
                driver.set(new RemoteWebDriver(new URL(gridUrl), capability));

            }

        }else if(server.equals(local)) {*/
        // Local Directory
        if (myBrowser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver.set(new ChromeDriver(optionsManager.getChromeOptions()));
            getDriver().manage().window().maximize();
            getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            getDriver().get(myUrl(testEnv));


        }
    }


    @AfterClass
    public void afterClass() {
        getDriver().quit();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

   /* @Parameters ({"testEnv","user"})
    @Test
    public void Login(String testEnv, String user) throws Exception {

        SignIn.login(testEnv, user);
        Assert.assertEquals(getDriver().getTitle(), "SIMROP | Settings");
        TestUtils.assertSearchText("XPATH", "(//a[contains(text(),'SIMROP ADMIN')])[2]", "SIMROP ADMIN");
        getDriver().findElement(By.xpath("//*[@id=\"navbarDropdownMenuLink\"]/i")).click();
        Thread.sleep(1000);
        getDriver().findElement(By.linkText("ADMIN")).click();
        Thread.sleep(1000);
    }*/


    @BeforeMethod(description = "fetch test cases name")
    public void register(Method method) throws InterruptedException {

        ExtentTest child = parentTest.get().createNode(method.getName());
        testInfo.set(child);
        testInfo.get().getModel().setDescription(TestUtils.CheckBrowser());
        if (TestUtils.isAlertPresents()) {
            getDriver().switchTo().alert().accept();
            Thread.sleep(1000);
        }

    }

    @AfterMethod(description = "to display the result after each test method")
    public void captureStatus(ITestResult result) throws IOException {
        for (String group : result.getMethod().getGroups())
            testInfo.get().assignCategory(group);
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = TestUtils.addScreenshot();
            testInfo.get().addScreenCaptureFromBase64String(screenshotPath);
            testInfo.get().fail(result.getThrowable());
            getDriver().navigate().refresh();
        } else if (result.getStatus() == ITestResult.SKIP)
            testInfo.get().skip(result.getThrowable());
        else
            testInfo.get().pass(result.getName() + " Test passed");

        reports.flush();
    }

//	@Parameters({"toMails", "groupReport"})
//    @AfterSuite(description = "clean up report after test suite")
//    public void cleanup(String toMails, String groupReport) {
//
//        toAddress = System.getProperty("email_list", toMails);
//        SendMail.ComposeGmail("SIMROP Report <seamfix.test.report@gmail.com>", toAddress, groupReport);
//
//    }


    @Test
    public static String Login() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), waitTime);
        TestUtils.testTitle("Valid Login");
        Thread.sleep(1000);

        String Login = null;

        //Enter Username
        getDriver().findElement(By.xpath("//input[@name='UserName']")).sendKeys("C-7062554521");
        //Enter password
        getDriver().findElement(By.xpath("//input[@name='Password']")).sendKeys("12345678");
        //Proceed to Login
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();

        return Login;
    }
}
