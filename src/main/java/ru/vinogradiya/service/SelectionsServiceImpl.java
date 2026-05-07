package ru.vinogradiya.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vinogradiya.models.dto.SelectionCreateDto;
import ru.vinogradiya.models.dto.SelectionItemDto;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.repositories.SelectionsRepository;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.GlobalErrorMessage;
import ru.vinogradiya.utils.mapping.ItemMapper;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SelectionsServiceImpl implements SelectionsService {

    private final ItemMapper<Selection, SelectionItemDto> selectionsMapper;
    private final SelectionsRepository repository;

    @Override
    @Transactional
    public SelectionItemDto save(SelectionCreateDto dto) {
        log.info(">> Запрос на добавление сорта винограда с именем: {}", dto.getName());
        repository.create(dto);
        return repository.findById(dto.getId())
                .map(selectionsMapper::toDomain)
                .orElseThrow(() -> new ApiException(GlobalErrorMessage.INTERNAL_ERROR));
    }
}