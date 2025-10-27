# 📚 Guia Rápido - Métodos de Navegação entre Entidades

## 🔄 Métodos Auxiliares de Navegação

### 1️⃣ Order.getProducts() - "Quais produtos estão neste pedido?"

```java
public List<Product> getProducts() {
    return items.stream().map(x -> x.getProduct()).toList();
}
```

**📍 Navegação:** `Order` → `OrderItem` → `Product`

**❓ Quando usar:**
- ✅ Você tem um pedido e quer saber quais produtos ele contém
- ✅ Precisa apenas dos produtos, sem quantity/price
- ✅ Exibir lista de produtos de um pedido na UI

**💡 Exemplo prático:**
```java
// Buscar pedido #123
Order order = orderRepository.findById(123L).get();

// Obter produtos do pedido
List<Product> products = order.getProducts();
// Resultado: [Notebook Dell, Mouse Logitech, Teclado Mecânico]

// Usar no frontend
for (Product p : products) {
    System.out.println(p.getName());
}
```

**⚠️ Diferença importante:**
- `order.getItems()` → Retorna `Set<OrderItem>` (com quantity, price)
- `order.getProducts()` → Retorna `List<Product>` (apenas produtos)

---

### 2️⃣ Product.getOrders() - "Em quais pedidos este produto aparece?"

```java
public List<Order> getOrders() {
    return items.stream().map(x -> x.getOrder()).toList();
}
```

**📍 Navegação:** `Product` → `OrderItem` → `Order`

**❓ Quando usar:**
- ✅ Você tem um produto e quer saber em quais pedidos ele foi vendido
- ✅ Histórico de vendas de um produto específico
- ✅ Análise de quais clientes compraram determinado produto
- ✅ Relatórios de pedidos por produto

**💡 Exemplo prático:**
```java
// Buscar produto "Notebook Dell"
Product product = productRepository.findById(5L).get();

// Obter todos os pedidos que contêm este produto
List<Order> orders = product.getOrders();
// Resultado: [Order#10, Order#25, Order#38, Order#42]

// Ver histórico de vendas
for (Order order : orders) {
    System.out.println("Pedido " + order.getId() + 
                       " - Cliente: " + order.getClient().getName() +
                       " - Data: " + order.getMoment());
}
```

**📊 Casos de uso comuns:**
```java
// 1. Contar quantas vezes um produto foi vendido
int totalVendas = product.getOrders().size();

// 2. Listar clientes que compraram este produto
List<User> compradores = product.getOrders()
    .stream()
    .map(order -> order.getClient())
    .distinct()
    .toList();

// 3. Receita total de um produto (precisa dos items, não apenas orders)
Double receitaTotal = product.getItems()
    .stream()
    .mapToDouble(item -> item.getPrice() * item.getQuantity())
    .sum();
```

---

## 🗺️ Mapa de Navegação Completo

```
┌─────────────────────────────────────────────────────┐
│                  RELACIONAMENTOS                     │
└─────────────────────────────────────────────────────┘

Order ←──────────────────→ Product
  ↓                           ↑
  │    via OrderItem          │
  └───────────────────────────┘
      (classe associativa)


Navegação 1: Order → Product
─────────────────────────────
order.getItems()      → Set<OrderItem>  (com detalhes)
order.getProducts()   → List<Product>   (apenas produtos)


Navegação 2: Product → Order  
─────────────────────────────
product.getItems()    → Set<OrderItem>  (com detalhes)
product.getOrders()   → List<Order>     (apenas pedidos)
```

---

## 🎯 Comparação Rápida

| Método | Retorna | Com detalhes? | Quando usar |
|--------|---------|---------------|-------------|
| `order.getItems()` | `Set<OrderItem>` | ✅ Sim (quantity, price) | Preciso de quantidade e preço |
| `order.getProducts()` | `List<Product>` | ❌ Não | Só quero lista de produtos |
| `product.getItems()` | `Set<OrderItem>` | ✅ Sim (quantity, price) | Análise detalhada de vendas |
| `product.getOrders()` | `List<Order>` | ❌ Não | Histórico de pedidos |

---

## 🔍 Por que usar Stream API?

```java
// Transformação: OrderItem → Product
items.stream()                    // 1. Cria stream dos items
     .map(x -> x.getProduct())    // 2. Extrai o Product de cada item
     .toList()                    // 3. Coleta em List

// Equivalente sem Stream (mais verboso):
List<Product> products = new ArrayList<>();
for (OrderItem item : items) {
    products.add(item.getProduct());
}
return products;
```

**Vantagens:**
- ✅ Código mais conciso e legível
- ✅ Funcional e declarativo
- ✅ Fácil adicionar filtros/transformações
- ✅ Padrão moderno do Java

---

## 💡 Dicas de Uso

### ✅ BOM - Usar métodos auxiliares
```java
// Simples e direto
List<Product> products = order.getProducts();
```

### ❌ EVITAR - Navegação manual repetitiva
```java
// Verboso e propenso a erros
List<Product> products = new ArrayList<>();
for (OrderItem item : order.getItems()) {
    products.add(item.getProduct());
}
```

### 🎯 MELHOR - Combinar com outras operações
```java
// Produtos acima de R$ 100 no pedido
List<Product> produtosCaros = order.getItems()
    .stream()
    .filter(item -> item.getPrice() > 100.0)
    .map(item -> item.getProduct())
    .toList();
```

---

## 📝 Resumo Final

| Pergunta | Método | Resposta |
|----------|--------|----------|
| Quais produtos estão no pedido #10? | `order.getProducts()` | Lista de Products |
| Em quais pedidos o Notebook foi vendido? | `product.getOrders()` | Lista de Orders |
| Quantos itens tem o pedido #10? | `order.getItems().size()` | Quantidade de itens |
| Quantas vezes vendeu o Notebook? | `product.getOrders().size()` | Quantidade de pedidos |

---

**🔖 SALVE ESTE ARQUIVO PARA CONSULTA RÁPIDA!**

Sempre que precisar navegar entre Order e Product, consulte este guia.

