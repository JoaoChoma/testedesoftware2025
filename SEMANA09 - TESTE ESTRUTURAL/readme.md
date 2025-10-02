# Teste estrutural com JUnit

- Pesquisar o que é JUnit

- Pesquisar o que é Jacoco

- Entender o funcionamento do Problema 01, checkout de compras

## Descrição do sistema

Problema: Checkout de um carrinho com cupom, fidelidade, frete e imposto

### Especificação resumida
	•	Itens: cada item tem categoria, precoUnitario e quantidade.
	•	Fidelidade (tier):
	•	BASIC: 0%
	•	SILVER: 5%
	•	GOLD: 10%
	•	Primeira compra: +5% de desconto adicional se subtotal ≥ 50.
	•	Cupom (apenas um por vez):
	•	DESC10: 10% (sem mínimo, não expira)
	•	DESC20: 20% (mínimo subtotal ≥ 100, expira na data informada)
	•	FRETEGRATIS: não dá % de desconto; concede frete grátis somente se peso ≤ 5.
	•	Cupom inválido/expirado/min não atendido → ignorar.
	•	Limite de desconto: soma de descontos (% tier + % cupom + % primeira compra) tem teto de 30%.
	•	Frete (se não for grátis por cupom ou por valor):
	•	Frete grátis se subtotal ≥ 300.
	•	Caso contrário, por região e peso:
	•	Região SUL ou SUDESTE:
	•	peso ≤ 2: 20
	•	2 < peso ≤ 5: 35
	•	peso > 5: 50
	•	Região NORTE:
	•	peso ≤ 2: 30
	•	2 < peso ≤ 5: 55
	•	peso > 5: 80
	•	Outras regiões: tarifa fixa 40.
	•	Imposto: 12% sobre o valor com desconto, antes do frete.
	
	Categoria “BOOK” é isenta (itens dessa categoria não compõem a base do imposto).
	•	Arredondamento: resultados monetários com 2 casas (arredondamento “meio-para-cima”).
	•	Validações:
		precoUnitario < 0 ou quantidade ≤ 0 → IllegalArgumentException.
		peso < 0 → IllegalArgumentException.