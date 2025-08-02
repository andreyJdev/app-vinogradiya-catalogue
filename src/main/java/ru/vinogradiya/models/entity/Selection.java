package ru.vinogradiya.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NamedEntityGraph(
        name = "Selection.findAll",
        attributeNodes = @NamedAttributeNode(Selection.PRODUCTS)
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Selection.TABLE_NAME)
public class Selection {

    public static final String TABLE_NAME = "selection";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRODUCTS = "products";

    @Id
    @Column(name = ID)
    private UUID id;

    @Column(name = NAME, unique = true)
    //todo @UniqueName(message = "Выберите другое Название селекции, это занято")
    @NotBlank(message = "Обязательное заполнение Названия селекции")
    @Size(max = 100, message = "Не больше 100 символов")
    private String name;

    @OneToMany(mappedBy = "selection")
    private List<Product> products;

    @Generated
    public String toString() {
        return "Selection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", products=" + products.stream()
                .map(product -> "{id=" + product.getId() + ", name=" + product.getName() + "}")
                .toList() +
                '}';
    }
}