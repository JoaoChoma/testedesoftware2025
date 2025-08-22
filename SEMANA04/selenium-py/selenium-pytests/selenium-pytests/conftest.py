import os
import pytest
from datetime import datetime
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.service import Service

def pytest_addoption(parser):
    parser.addoption("--headed", action="store_true", help="Executa com janela (sem headless).")

@pytest.fixture
def driver(request):
    headed = request.config.getoption("--headed")
    options = Options()
    if not headed:
        options.add_argument("--headless=new")
    options.add_argument("--window-size=1366,768")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")

    # Usando webdriver-manager para provisionar o ChromeDriver
    service = Service(ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service, options=options)
    driver.set_page_load_timeout(30)

    yield driver

     # Screenshot em falha (setup/call)
    for report_attr in ("rep_setup", "rep_call"):
        rep = getattr(request.node, report_attr, None)
        if rep and rep.failed:
            os.makedirs("screenshots", exist_ok=True)
            ts = datetime.now().strftime("%Y%m%d-%H%M%S")
            fname = f"screenshots/{request.node.name}-{ts}.png"
            try:
                driver.save_screenshot(fname)
                print(f"\n[debug] Screenshot salvo em: {fname}")
            except Exception:
                pass

    driver.quit()

@pytest.hookimpl(hookwrapper=True, tryfirst=True)
def pytest_runtest_makereport(item, call):
    # Anexa os objetos de resultado no item para a fixture ler
    outcome = yield
    rep = outcome.get_result()
    setattr(item, "rep_" + rep.when, rep)
