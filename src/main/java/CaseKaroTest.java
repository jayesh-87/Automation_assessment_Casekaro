import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.regex.Pattern;
import java.util.List;
import java.util.stream.Collectors;

public class CaseKaroTest {

    public static void main(String[] args) {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500));

        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.navigate("https://casekaro.com/", new Page.NavigateOptions().setWaitUntil(WaitUntilState.LOAD));

        Locator mobileCoversLink = page.locator(".header__inline-menu")
                .locator("text=Mobile Covers @ 69")
                .first();
        mobileCoversLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        mobileCoversLink.click();

        Locator modelSearchInput = page.locator("#modelSearch");
        modelSearchInput.scrollIntoViewIfNeeded();

        Locator resultsContainer = page.locator(".rte.scroll-trigger.animate--slide-in");

        searchAndValidate(page, modelSearchInput, resultsContainer, "Apple");
        searchAndValidate(page, modelSearchInput, resultsContainer, "iPhone");

        modelSearchInput.clear();
        modelSearchInput.fill("iPhone 16 Pro");
        page.keyboard().press("Enter");
        page.waitForTimeout(2000);

        Locator modelCategoryLink = page.locator("a[href*='collections/iphone-16-pro-back-covers']")
                .filter(new Locator.FilterOptions().setHasText("iPhone 16 Pro"))
                .first();
        modelCategoryLink.scrollIntoViewIfNeeded();
        modelCategoryLink.click();

        page.waitForURL("**/collections/iphone-16-pro-back-covers**");

        String[] materials = {"Hard", "Glass", "Soft"};

        for (int i = 0; i < materials.length; i++) {

            Locator chooseOptionsBtn = page.locator(".quick-add button[name='add']").first();
            chooseOptionsBtn.scrollIntoViewIfNeeded();
            chooseOptionsBtn.click(new Locator.ClickOptions().setForce(true));


            Locator modal = page.locator(".quick-add-modal__content").first();
            modal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));


            Locator materialLabel = modal.locator("label:text-is('" + materials[i] + "')");


            materialLabel.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            materialLabel.click(new Locator.ClickOptions().setForce(true));


            Locator addToCartBtn = modal.locator("button[type='submit'][name='add']").first();
            addToCartBtn.click();


            page.locator("#CartDrawer").waitFor();
            if (i < materials.length - 1) {
                page.locator("#CartDrawer-Overlay").click(new Locator.ClickOptions().setForce(true));
                page.waitForTimeout(1000); // Give the drawer a moment to slide away
            }
        }
        Locator cartItems = page.locator(".cart-item");
        cartItems.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertThat(cartItems).hasCount(3);

        System.out.println("\n================ FINAL CART AUDIT REPORT ================");
        for (int i = 0; i < 3; i++) {
            Locator item = cartItems.nth(i);

            String materialText = item.locator(".product-option")
                    .filter(new Locator.FilterOptions().setHasText("Material:"))
                    .innerText();

            String priceText = item.locator(".price--end, .cart-item__totals").first().innerText();
            String productPath = item.locator(".cart-item__name").getAttribute("href");

            System.out.printf("ITEM %d:%n", (i + 1));
            System.out.printf(" - %s%n", materialText.trim());
            String cleanPrice = priceText.replaceAll("[^0-9.]", "");
            System.out.printf(" - Price    : Rs. %s%n", cleanPrice);
            System.out.printf(" - Full Link: https://casekaro.com%s%n", productPath);
            System.out.println("---------------------------------------------------------");
        }

        page.waitForTimeout(3000);
        context.close();
        browser.close();
        playwright.close();
    }

    private static void searchAndValidate(Page page,
                                          Locator searchBox,
                                          Locator resultsContainer,
                                          String searchText) {

        searchBox.clear();
        searchBox.fill(searchText);
        page.keyboard().press("Enter");
        page.waitForTimeout(2000);

        List<String> visibleLinks = resultsContainer.locator("a").all().stream()
                .filter(Locator::isVisible)
                .map(Locator::innerText)
                .collect(Collectors.toList());

        if (visibleLinks.isEmpty()) {
            System.out.println("Search Result: No model found for '" + searchText + "'.");
        } else {
            validateNoOtherBrands(visibleLinks, searchText);
        }
    }

    private static void validateNoOtherBrands(List<String> links, String contextQuery) {

        String[] prohibitedBrands = {
                "SAMSUNG", "ONEPLUS", "VIVO", "REALME",
                "OPPO", "REDMI", "MOTO", "NOTHING", "PIXEL"
        };

        for (String text : links) {
            String upperText = text.toUpperCase();
            if (upperText.trim().isEmpty()) continue;

            for (String brand : prohibitedBrands) {
                if (upperText.contains(brand)) {
                    throw new RuntimeException(
                            "Validation Failure: Found prohibited brand '" + brand +
                                    "' in search results for '" + contextQuery + "'. Found in: " + text
                    );
                }
            }
        }
        System.out.println("Negative Validation Passed for '" + contextQuery + "' search results.");
    }
}
