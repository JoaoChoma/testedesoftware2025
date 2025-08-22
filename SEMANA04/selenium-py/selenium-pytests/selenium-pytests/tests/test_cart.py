from pages.login_page import LoginPage
from pages.inventory_page import InventoryPage

def test_adicionar_item_no_carrinho(driver):
    login = LoginPage(driver)
    inv = InventoryPage(driver)

    login.open()
    login.login("standard_user", "secret_sauce")
    assert inv.is_loaded()

    inv.add_first_item_to_cart()
    assert inv.get_cart_count() == 1
