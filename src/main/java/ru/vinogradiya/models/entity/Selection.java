package ru.vinogradiya.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import java.util.List;
import java.util.UUID;

@Entity
@FetchProfile(name = "withProduct", fetchOverrides = {
        @FetchProfile.FetchOverride(entity = Selection.class, association = "products", mode = FetchMode.JOIN)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "selection")
public class Selection {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", unique = true)
    //todo @UniqueName(message = "Выберите другое Название селекции, это занято")
    @NotBlank(message = "Обязательное заполнение Названия селекции")
    @Size(max = 100, message = "Не больше 100 символов")
    private String name;

    @OneToMany(mappedBy = "selection")
    private List<Product> products;
}