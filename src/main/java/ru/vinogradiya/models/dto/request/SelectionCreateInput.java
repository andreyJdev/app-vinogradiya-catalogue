package ru.vinogradiya.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Запрос на добавление селекции")
public class SelectionCreateInput extends SelectionInput {

}