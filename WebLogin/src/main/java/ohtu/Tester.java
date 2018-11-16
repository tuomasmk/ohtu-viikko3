package ohtu;

import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Tester {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/home/tmkontti/Downloads/chromedriver"); 
        WebDriver driver = new ChromeDriver();

        succesfulLogin(driver);
        unSuccesfulLoginCorrectUsername(driver);
        unSuccesfulLoginIncorrectUsername(driver);
        createNewUser(driver);
        createNewUserAndLogout(driver);
        
        driver.quit();
    }
    
    private static void succesfulLogin(WebDriver driver) {
        login(driver, "pekka", "akkep");
    }
    
    private static void login(WebDriver driver, String username, String pw) {
        driver.get("http://localhost:4567");
        
        sleep(2);
        
        WebElement element = driver.findElement(By.linkText("login"));
        element.click();

        sleep(2);

        element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(pw);
        element = driver.findElement(By.name("login"));
        
        sleep(2);
        element.submit();

        sleep(2);
    }
    
    private static void sleep(int n){
        try{
            Thread.sleep(n*1000);
        } catch(Exception e){}
    }

    private static void unSuccesfulLoginCorrectUsername(WebDriver driver) {
        login(driver, "pekka", "pekka");
    }

    private static void unSuccesfulLoginIncorrectUsername(WebDriver driver) {
        login(driver, "akkep", "pekka");
    }

    private static void createNewUser(WebDriver driver) {
        driver.get("http://localhost:4567");
        
        sleep(2);
        
        WebElement element = driver.findElement(By.linkText("register new user"));
        element.click();

        sleep(2);

        element = driver.findElement(By.name("username"));
        Random r = new Random();
        element.sendKeys("tuomas"+r.nextInt(100000));
        element = driver.findElement(By.name("password"));
        element.sendKeys("pekka");
        element = driver.findElement(By.name("passwordConfirmation"));
        element.sendKeys("pekka");
        element = driver.findElement(By.name("signup"));
        
        sleep(2);
        element.submit();

        sleep(2);
    }

    private static void createNewUserAndLogout(WebDriver driver) {
        createNewUser(driver);
        WebElement element = driver.findElement(By.linkText("continue to application mainpage"));
        element.click();

        sleep(2);
        
        element = driver.findElement(By.linkText("logout"));
        element.click();

        sleep(2);
    }
}
