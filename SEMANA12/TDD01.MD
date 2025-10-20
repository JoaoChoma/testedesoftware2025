# TDD - PROJETO 01

Uma loja deseja automatizar o cálculo de descontos para seus clientes. As regras são:
1.	Compras abaixo de R$100,00 → sem desconto.
2.	Compras entre R$100,00 e R$500,00 → 5% de desconto.
3.	Compras acima de R$500,00 → 10% de desconto.
4.	Nenhum valor pode ser negativo — se isso ocorrer, deve ser lançada uma IllegalArgumentException.

Implemente uma classe chamada CalculadoraDeDescontos usando TDD, ou seja:
1.	Crie primeiro os testes JUnit, antes da implementação da lógica.
2.	Escreva apenas o código mínimo necessário para cada teste passar.
3.	Refatore quando todos os testes estiverem verdes.


Dica: Use o assertThrows para tratar o item 4.

```java copy
assertThrows(IllegalArgumentException.class, () -> calc.calcular(-50.0));
```