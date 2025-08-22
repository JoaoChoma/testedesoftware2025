from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Abre o Chrome
navegador = webdriver.Chrome()

# Acessa o Google
navegador.get("https://selenium-python.readthedocs.io/")
navegador.maximize_window()

wait = WebDriverWait(navegador, 15)

link = wait.until(EC.element_to_be_clickable((By.CSS_SELECTOR, "a.reference.internal")))
link.click()

# Espera o título da página ser carregado
assert wait.until(EC.text_to_be_present_in_element((By.TAG_NAME, "body"), "Selenium with Python"))
time.sleep(5)


navegador.quit()