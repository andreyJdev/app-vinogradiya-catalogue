package ru.vinogradiya.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vinogradiya.models.dto.db.SelectionCreateData;
import ru.vinogradiya.models.dto.request.SelectionCreateInput;
import ru.vinogradiya.models.dto.response.SelectionItem;
import ru.vinogradiya.repositories.SelectionsRepository;
import ru.vinogradiya.utils.common.exception.ApiException;
import ru.vinogradiya.utils.enums.GlobalErrorMessage;
import ru.vinogradiya.utils.mapping.ItemMapper;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SelectionsServiceImpl implements SelectionsService {

    private final ItemMapper selectionsMapper;
    private final SelectionsRepository repository;

    @Override
    @Transactional
    public SelectionItem save(SelectionCreateInput request) {
        log.info(">> Запрос на добавление сорта винограда с именем: {}", request.getName());
        SelectionCreateData data = selectionsMapper.toCreate(request);
        repository.create(data);
        return repository.findById(data.getId())
                .map(selectionsMapper::toDomain)
                .orElseThrow(() -> new ApiException(GlobalErrorMessage.INTERNAL_ERROR));
    }
}