from pages.login_page import LoginPage
from pages.inventory_page import InventoryPage

def test_login_sucesso(driver):
    login = LoginPage(driver)
    inventory = InventoryPage(driver)

    login.open()
    login.login("standard_user", "secret_sauce")

    assert inventory.is_loaded(), "Inventário não carregou após login."

def test_login_invalido(driver):
    login = LoginPage(driver)
    login.open()
    login.login("usuario_errado", "senha_errada")
    assert "Epic sadface" in login.get_error()
