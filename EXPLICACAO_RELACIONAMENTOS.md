# Explicação dos Relacionamentos - DSCommerce

## 🔑 Por que usar OrderItemPK (Chave Composta)?

### Conceito
Em relacionamentos **Many-to-Many com atributos extras**, precisamos de uma **classe associativa** e uma **chave composta**.

### Estrutura do Relacionamento
```
Order (1) ----< OrderItem >---- (N) Product
            (tabela associativa)
```

### Motivos para Chave Composta

1. **Tabela associativa tem atributos extras**
   - `quantity` (quantidade do produto no pedido)
   - `price` (preço do produto no momento do pedido)

2. **Chave primária composta pelas duas FKs**
   - PK = `(order_id + product_id)`
   - Garante unicidade: mesmo produto não aparece duplicado no mesmo pedido

3. **Integridade dos dados**
   - Se precisa de mais quantidade, ajusta o campo `quantity`
   - Não cria linhas duplicadas

## 📊 Comparação: Many-to-Many Simples vs Com Classe Associativa

### Many-to-Many SIMPLES (Product <-> Category)
```java
@ManyToMany
@JoinTable(name = "tb_product_category",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
private Set<Category> categories;
```
**Quando usar:**
- ✅ Relacionamento SEM atributos extras
- ✅ Apenas associação entre entidades
- ✅ Tabela intermediária tem apenas 2 colunas (FKs)

**Resultado no banco:**
```
tb_product_category
+------------+-------------+
| product_id | category_id |
+------------+-------------+
```

### Many-to-Many COM CLASSE ASSOCIATIVA (Order <-> Product)
```java
@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id; // Chave composta
    
    private Integer quantity; // Atributo extra
    private Double price;     // Atributo extra
}

@Embeddable
public class OrderItemPK {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
```

**Quando usar:**
- ✅ Relacionamento COM atributos extras
- ✅ Precisa armazenar dados sobre a associação
- ✅ Tabela intermediária tem mais de 2 colunas

**Resultado no banco:**
```
tb_order_item
+----------+------------+----------+-------+
| order_id | product_id | quantity | price |
+----------+------------+----------+-------+
| (PK)     | (PK)       |          |       |
```

## 🔄 Mapeamentos dos Relacionamentos

### 1. Order -> User (Many-to-One)
```java
@ManyToOne
@JoinColumn(name = "client_id")
private User client;
```
- **Cardinalidade:** Muitos pedidos → Um cliente
- **FK:** `client_id` na tabela `tb_order`
- **Significado:** Vários pedidos podem ser do mesmo cliente

### 2. Order -> Payment (One-to-One)
```java
@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
private Payment payment;
```
- **Cardinalidade:** Um pedido → Um pagamento
- **mappedBy:** Payment é o lado proprietário (tem a FK)
- **cascade:** Operações no Order propagam para Payment

### 3. Order -> OrderItem (One-to-Many)
```java
@OneToMany(mappedBy = "id.order")
private Set<OrderItem> items;
```
- **Cardinalidade:** Um pedido → Muitos itens
- **mappedBy:** `id.order` navega pela chave composta
- **Caminho:** OrderItem → OrderItemPK (id) → Order

### 4. Product -> OrderItem (One-to-Many)
```java
@OneToMany(mappedBy = "id.product")
private Set<OrderItem> items;
```
- **Cardinalidade:** Um produto → Muitos itens de pedido
- **mappedBy:** `id.product` navega pela chave composta
- **Caminho:** OrderItem → OrderItemPK (id) → Product

### 5. Product -> Category (Many-to-Many)
```java
@ManyToMany
@JoinTable(name = "tb_product_category",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
private Set<Category> categories;
```
- **Cardinalidade:** Muitos produtos ↔ Muitas categorias
- **Sem atributos extras:** Usa `@ManyToMany` direto
- **Tabela intermediária:** `tb_product_category` (apenas FKs)

## 💡 Por que usar Set ao invés de List?

```java
private Set<OrderItem> items = new HashSet<>();
private Set<Category> categories = new HashSet<>();
```

**Motivos:**
1. ✅ **Não permite duplicatas** - Garante unicidade
2. ✅ **Melhor performance** - Busca/adição/remoção mais rápidas
3. ✅ **Semântica correta** - Reflete a natureza do relacionamento
4. ✅ **Evita bugs** - Não permite adicionar a mesma entidade duas vezes

## 📝 Anotações JPA Importantes

- `@Embeddable` - Classe que será incorporada em outra (não tem tabela própria)
- `@EmbeddedId` - Indica que a PK é uma classe embeddable (chave composta)
- `@ManyToOne` - Muitos para um (tem FK no lado "Many")
- `@OneToMany` - Um para muitos (FK está no lado "Many")
- `@OneToOne` - Um para um (FK em um dos lados)
- `@ManyToMany` - Muitos para muitos (cria tabela intermediária)
- `@JoinColumn` - Define o nome da coluna FK
- `@JoinTable` - Define a tabela intermediária em @ManyToMany
- `mappedBy` - Indica o lado não-proprietário do relacionamento
- `cascade` - Propaga operações para entidades relacionadas

## 🎯 Resumo

| Relacionamento | Tipo | Atributos Extras? | Solução |
|----------------|------|-------------------|---------|
| Order ↔ Product | Many-to-Many | ✅ Sim (quantity, price) | Classe Associativa + Chave Composta |
| Product ↔ Category | Many-to-Many | ❌ Não | @ManyToMany simples |
| Order → User | Many-to-One | - | FK em Order |
| Order → Payment | One-to-One | - | FK em Payment |

---
**Dica:** Sempre que um relacionamento Many-to-Many precisar armazenar informações sobre a associação (quantidade, data, preço, etc), use uma classe associativa com chave composta!

