from selenium.webdriver.common.by import By
from .base_page import BasePage

class InventoryPage(BasePage):
    INVENTORY_CONTAINER = (By.ID, "inventory_container")
    CART_BADGE = (By.CLASS_NAME, "shopping_cart_badge")
    FIRST_ADD_TO_CART = (By.CSS_SELECTOR, ".inventory_item:first-of-type button.btn")

    def is_loaded(self) -> bool:
        try:
            self.wait_visible(self.INVENTORY_CONTAINER)
            return True
        except Exception:
            return False

    def add_first_item_to_cart(self):
        self.wait_visible(self.FIRST_ADD_TO_CART).click()

    def get_cart_count(self) -> int:
        badges = self.driver.find_elements(*self.CART_BADGE)
        return int(badges[0].text) if badges else 0
