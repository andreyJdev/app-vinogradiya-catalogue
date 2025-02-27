package ru.vinogradiya.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "selection")
public class Selection {
    @Id
    @Column(name = "id")
    @SequenceGenerator(
            name = "selection_sequence",
            sequenceName = "selection_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "selection_sequence")
    private Long id;

    @Column(name = "name", unique = true)
    //@UniqueName(message = "Выберите другое Название селекции, это занято")
    @NotBlank(message = "Обязательное заполнение Названия селекции")
    @Size(max = 100, message = "Не больше 100 символов")
    private String name;

    @OneToMany(mappedBy = "selection")
    private List<Product> products;

    public Selection(String name) {
        this.name = name;
    }
}
