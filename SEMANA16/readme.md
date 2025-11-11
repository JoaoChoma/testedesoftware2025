rodar docker

```bash
docker compose up -d
```

Criar o proxy que aponta o Toxiproxy → Nginx:

```bash
curl -s -X POST localhost:8474/proxies \
  -H 'Content-Type: application/json' \
  -d '{"name":"web","listen":"0.0.0.0:8080","upstream":"app:80"}'`
```

Rodar o baseline (sem “ataque”/degradação)

```bash
docker compose run --rm k6 run /scripts/script.js`
```

Injetar “condições de DDoS”

curl -s -X POST localhost:8474/proxies/web/toxics \
  -H 'Content-Type: application/json' \
  -d '{"name":"limit","type":"bandwidth","stream":"downstream","attributes":{"rate":256}}'

 - rate em KB/s (256 KB/s aprox). Ajuste para 64/128 etc. para piorar.


Latência e jitter (fila/congestionamento):

curl -s -X POST localhost:8474/proxies/web/toxics \
  -H 'Content-Type: application/json' \
  -d '{"name":"lag","type":"latency","stream":"downstream","attributes":{"latency":300,"jitter":100}}'

 - 300 ms ±100 ms por resposta

 Rode o k6 novamente:

 docker compose run --rm k6 run /scripts/script.js


Remover toxics depois:

curl -s -X DELETE localhost:8474/proxies/web/toxics/limit
curl -s -X DELETE localhost:8474/proxies/web/toxics/lag