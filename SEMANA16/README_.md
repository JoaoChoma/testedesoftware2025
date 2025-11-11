# Lab seguro: simulando **sintomas de DDoS** com Docker + Toxiproxy + k6

Este guia mostra, **de forma segura e local**, como reproduzir sintomas típicos de DDoS (latência alta, erros, throughput caindo) **sem atacar ninguém**. Todo o tráfego é **legítimo** e limitado ao seu **ambiente Docker**.

> **Resumo do que você fará**
> 1) Subir **Nginx** (alvo), **Toxiproxy** (degrada rede) e **k6** (gera carga).  
> 2) Criar um **proxy** via API do Toxiproxy.  
> 3) Executar um **baseline** (sem degradação).  
> 4) Injetar **limite de banda** e **latência/jitter** (sintomas de DDoS).  
> 5) Executar o teste novamente e **comparar RPS, p95 e erros**.

---

## 1) Pré‑requisitos
- Docker Desktop + Docker Compose

---

## 2) Estrutura de arquivos
```
SEMANA16/
├─ docker-compose.yml
└─ k6/
   └─ script.js
```

### `docker-compose.yml`
```yaml
services:
  app:
    image: nginx:alpine
    container_name: ddos_app
    ports:
      - "8081:80"   # acesso direto ao app (baseline/controle)

  toxiproxy:
    image: ghcr.io/shopify/toxiproxy
    container_name: ddos_toxiproxy
    ports:
      - "8474:8474" # API do Toxiproxy
      - "8080:8080" # Proxy para a demo
    depends_on:
      - app

  k6:
    image: grafana/k6:latest
    # Se estiver em Mac M1/M2 e precisar, descomente a plataforma:
    # platform: linux/arm64
    container_name: ddos_k6
    volumes:
      - ./k6:/scripts
    working_dir: /scripts
    depends_on:
      - toxiproxy
```

### `k6/script.js`
```javascript
import http from 'k6/http';
import { check } from 'k6';

export const options = {
  stages: [
    { duration: '15s', target: 30 },
    { duration: '30s', target: 150 },
    { duration: '15s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'],
    http_req_failed: ['rate<0.05'],
  },
};

export default function () {
  const res = http.get('http://toxiproxy:8080/');
  check(res, { 'status OK-ish': r => [200, 304, 404].includes(r.status) });
}
```

---

## 3) Subir os serviços
```bash
docker compose up -d
```

---

## 4) Criar o **proxy** no Toxiproxy (porta 8080 → app:80)
```bash
curl -s -X POST localhost:8474/proxies \
  -H 'Content-Type: application/json' \
  -d '{"name":"web","listen":"0.0.0.0:8080","upstream":"app:80"}'
```
A resposta indica `enabled:true`. Em alguns sistemas o `listen` pode aparecer como `[::]:8080` (IPv6), o que é OK.

Sanity check (ambos devem responder 200/304/404):
```bash
curl -I http://localhost:8080   # via proxy (com degradação quando ativada)
curl -I http://localhost:8081   # direto no app (controle)
```

---

## 5) **Baseline** (sem degradação)
Rode o teste e observe **RPS**, **p95** e **erros** no resumo:
```bash
docker compose run --rm k6 run /scripts/script.js
```

---

## 6) Injetar **sintomas de DDoS** (degradação controlada)
### 6.1 Limite de banda (simula ataque volumétrico / saturação de link)
```bash
curl -s -X POST localhost:8474/proxies/web/toxics \
  -H 'Content-Type: application/json' \
  -d '{"name":"limit","type":"bandwidth","stream":"downstream","attributes":{"rate":128}}'
# rate em KB/s (128 KB/s). Diminua (64, 32...) para piorar.
```

### 6.2 Latência + jitter (simula congestionamento/filas)
```bash
curl -s -X POST localhost:8474/proxies/web/toxics \
  -H 'Content-Type: application/json' \
  -d '{"name":"lag","type":"latency","stream":"downstream","attributes":{"latency":300,"jitter":150}}'
```

Verificar os toxics ativos:
```bash
curl -s localhost:8474/proxies/web/toxics | jq .
```

### 6.3 Executar o teste **com degradação**
```bash
docker compose run --rm k6 run /scripts/script.js
# ou (Homebrew)
# TARGET=http://localhost:8080/ k6 run k6/script.js
```

**O que você verá**:  
- **RPS** (throughput) tende a **cair**.  
- **p95** (latência 95º percentil) tende a **subir**.  
- **Erros** (timeouts/5xx) tendem a **aumentar**.  
Esses são **sintomas típicos** de um DDoS, reproduzidos de modo seguro.

---

## 7) Limpar degradação (voltar ao normal)
```bash
curl -s -X DELETE localhost:8474/proxies/web/toxics/limit
curl -s -X DELETE localhost:8474/proxies/web/toxics/lag
```

---

## 8) Entendendo as métricas que você vai “apontar no quadro”
- **RPS (Requests per Second)**: quantas requisições/seg o sistema realmente entregou.
- **p95 (latência 95º percentil)**: 95% das reqs terminaram abaixo desse tempo (mede a **cauda**).
- **Erros**: percentual de requisições com falha (5xx/timeout e também 429 quando o rate limit está ativo).

**Regras de bolso**:  
- RPS alto com **p95 estourado** ainda é **experiência ruim**.  
- **p95 importa mais que a média**.  
- **429** indica **controle**; **5xx/timeout** indica **sofrimento** do servidor.

---

### Aviso didático
Use **apenas** em ambiente local ou de laboratório com **autorização**. Não aponte tráfego para domínios externos.
