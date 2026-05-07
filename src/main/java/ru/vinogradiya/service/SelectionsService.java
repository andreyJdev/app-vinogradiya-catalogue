package ru.vinogradiya.service;

import ru.vinogradiya.models.dto.SelectionCreateDto;
import ru.vinogradiya.models.dto.SelectionItemDto;

public interface SelectionsService {

    SelectionItemDto save(SelectionCreateDto dto);
}