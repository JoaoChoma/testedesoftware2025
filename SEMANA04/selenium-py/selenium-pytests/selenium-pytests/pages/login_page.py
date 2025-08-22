from selenium.webdriver.common.by import By
from .base_page import BasePage

class LoginPage(BasePage):
    URL = "https://www.saucedemo.com/"

    USERNAME = (By.ID, "user-name")
    PASSWORD = (By.ID, "password")
    SUBMIT   = (By.ID, "login-button")
    ERROR    = (By.CSS_SELECTOR, "[data-test='error']")

    def open(self):
        self.visit(self.URL)

    def login(self, username: str, password: str):
        self.wait_visible(self.USERNAME).clear()
        self.driver.find_element(*self.USERNAME).send_keys(username)
        self.driver.find_element(*self.PASSWORD).clear()
        self.driver.find_element(*self.PASSWORD).send_keys(password)
        self.driver.find_element(*self.SUBMIT).click()

    def get_error(self) -> str:
        try:
            return self.wait_visible(self.ERROR).text
        except Exception:
            return ""
