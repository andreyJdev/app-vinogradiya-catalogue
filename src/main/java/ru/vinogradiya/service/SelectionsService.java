package ru.vinogradiya.service;

import ru.vinogradiya.models.dto.request.SelectionCreateInput;
import ru.vinogradiya.models.dto.response.SelectionItem;

public interface SelectionsService {

    SelectionItem save(SelectionCreateInput request);
}