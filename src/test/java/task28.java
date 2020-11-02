import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class task28 {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void before(){

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\IdeaProjects\\HomeTask28\\Drivers\\chromedriver.exe");

        driver = new ChromeDriver();
        wait = (new WebDriverWait(driver, 8));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://rozetka.com.ua");
    }

    @Test
    public void test() throws IOException {
        WebElement SearchInput = driver.findElement(By.xpath("//input[@name='search']"));
        SearchInput.sendKeys("Mac");

        WebElement SearchButton = driver.findElement(By.xpath("//button[contains(text(), 'Найти')]"));
        SearchButton.click();

        WebElement FirstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("goods-tile__heading")));
        String FirstProductTitle = FirstProduct.getAttribute("title");
        // System.out.println("title of first product is - " + FirstProductTitle);

        WebElement FirstProductLink = driver.findElement(By.className("goods-tile__title"));
        FirstProductLink.click();

        WebElement ProductTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='product__title']")));
        String ProductTitleText = ProductTitle.getAttribute("innerText");
        // System.out.println("title on product page is - " + ProductTitleText);

        //C помощью метода assertEquals() проверить, что тайтлы с поисковой странице и продуктовой одинаковые и если нет, вывести сообщение «Titles does’nt equals»
        Assert.assertEquals(FirstProductTitle.trim(), ProductTitleText.trim(), "Titles doesn't equals");

        //C помощью метода getCssValue и assertEquals проверить что цвет активной ссылки «Все о товаре» - зелёный
        WebElement firstActiveLink = driver.findElement(By.xpath("//a[@class='tabs__link tabs__link--active']"));
        String firstActiveLinkColor = firstActiveLink.getCssValue("color");
        Assert.assertEquals(firstActiveLinkColor, "rgba(0, 160, 70, 1)", "Color on first active tab is not green");

        //Если присутствует плашка «Есть в наличии» и цвет ее зеленый, записать в файл тайтл и цену товара.
        //Проверить присутствие плашки можно с помощью условного оператора и метода isDisplayed()
        WebElement ProductStatus = driver.findElement(By.xpath("//p[@class='product__status product__status_color_green']"));
        String ProductStatusColor = ProductStatus.getCssValue("Color");
        if(ProductStatus.isDisplayed() && ProductStatusColor.equals("rgb(0, 160, 70)")){
            WebElement ProductPrice = driver.findElement(By.xpath("//p[@class='product-prices__big product-prices__big_color_red']"));
            String ProductPriceText = ProductPrice.getAttribute("innerText");
            FileWriter fw = new FileWriter("product_info");
            fw.write("ProductTitle: " + ProductTitleText + ", " + "ProductPrice " + ProductPriceText);
            fw.close();
        }   else {
            System.out.println("плашка «Есть в наличии» отсутствует или ее цвет не зеленый");
        }

    }

    @AfterTest
    public void after(){
        driver.quit();
    }
}
