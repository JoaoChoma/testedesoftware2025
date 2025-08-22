import time
import csv
from pathlib import Path

import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager


def build_driver(headless: bool = True) -> webdriver.Chrome:
    opts = Options()
    if headless:
        opts.add_argument("--headless=new")
    opts.add_argument("--window-size=1366,768")
    opts.add_argument("--no-sandbox")
    opts.add_argument("--disable-dev-shm-usage")

    service = Service(ChromeDriverManager().install())
    return webdriver.Chrome(service=service, options=opts)


def login(driver: webdriver.Chrome, base_url: str, username: str, password: str) -> None:
    driver.get(f"{base_url}/login")
    wait = WebDriverWait(driver, 10)

    # Espera o formulário estar presente
    wait.until(EC.presence_of_element_located((By.TAG_NAME, "form")))

    # Preenche usuário/senha
    driver.find_element(By.NAME, "username").clear()
    driver.find_element(By.NAME, "username").send_keys(username)
    driver.find_element(By.NAME, "password").clear()
    driver.find_element(By.NAME, "password").send_keys(password)

    # Envia o formulário
    driver.find_element(By.CSS_SELECTOR, "input[type='submit']").click()

    # Espera carregar corpo da página
    wait.until(EC.presence_of_element_located((By.TAG_NAME, "body")))


def scrape_all_quotes(driver: webdriver.Chrome, base_url: str):
    wait = WebDriverWait(driver, 10)
    driver.get(base_url)
    all_rows = []

    while True:
        # Aguarda os cards de citação
        wait.until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, ".quote")))

        quotes = driver.find_elements(By.CSS_SELECTOR, ".quote")
        for q in quotes:
            text = q.find_element(By.CSS_SELECTOR, ".text").text.strip('“”"')
            author = q.find_element(By.CSS_SELECTOR, ".author").text.strip()
            tags = [t.text for t in q.find_elements(By.CSS_SELECTOR, ".tags a.tag")]
            all_rows.append({"quote": text, "author": author, "tags": ", ".join(tags)})

        # Tenta achar o link "Next" para paginar; se não houver, acabou
        next_links = driver.find_elements(By.CSS_SELECTOR, "li.next > a")
        if not next_links:
            break

        next_links[0].click()
        wait.until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, ".quote")))

    return all_rows


def save_to_csv(rows, out_path="quotes.csv"):
    df = pd.DataFrame(rows)
    df.to_csv(out_path, index=False, quoting=csv.QUOTE_MINIMAL)
    return Path(out_path).resolve()


def main():
    base_url = "https://quotes.toscrape.com"
    driver = build_driver(headless=True)

    try:
        # 1) Fluxo de login (demo)
        login(driver, base_url, username="usuario_demo", password="senha_demo")

        # 2) Raspa todas as páginas de citações
        rows = scrape_all_quotes(driver, base_url)

        # 3) Salva em CSV
        out_file = save_to_csv(rows, "quotes.csv")
        print(f"[OK] Extraí {len(rows)} citações. Arquivo salvo em: {out_file}")

    finally:
        driver.quit()


if __name__ == "__main__":
    main()