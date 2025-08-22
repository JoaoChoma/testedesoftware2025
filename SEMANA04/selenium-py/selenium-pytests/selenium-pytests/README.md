# Selenium + pytest Starter

Este projeto demonstra uma stack moderna de testes E2E com Selenium 4 + pytest,
seguindo Page Object Model (POM), esperas explícitas, screenshots em falha,
execução headless e paralelismo.

## Requisitos
- Python 3.10+ (recomendado)
- Chrome/Chromium instalado

## Instalação
```bash
python -m venv .venv
source .venv/bin/activate  # Windows: .venv\Scripts\activate
pip install -r requirements.txt
```

## Execução
```bash
pytest
```

### Modo visível (sem headless)
```bash
pytest --headed -s
```

### Paralelo + relatório HTML
```bash
pytest -n auto --html=report.html
```

Os screenshots de falha ficam em `screenshots/`.
Relatório HTML em `report.html`.

## Estrutura
```
selenium-pytests/
├─ requirements.txt
├─ pytest.ini
├─ conftest.py
├─ pages/
│  ├─ base_page.py
│  ├─ login_page.py
│  └─ inventory_page.py
└─ tests/
   ├─ test_login.py
   └─ test_cart.py
```
