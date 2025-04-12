package ru.vinogradiya.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.vinogradiya.models.dto.ProductCreateDto;
import ru.vinogradiya.models.dto.ProductFilter;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.ProductItem;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.queries.product.ProductFilterValuesQuery;
import ru.vinogradiya.queries.product.SearchBarConditionBuilder;
import ru.vinogradiya.utils.common.Paged;
import ru.vinogradiya.utils.enums.ProductListSortValuesEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Primary
@Repository
public class ProductsRepositorySql implements ProductsRepository {

    private final Map<String, ProductFilterValuesQuery> productFilterValuesQueryMap;
    private final SearchBarConditionBuilder searchBarConditionBuilder;
    @PersistenceContext
    private EntityManager entityManager;

    public ProductsRepositorySql(List<ProductFilterValuesQuery> list, SearchBarConditionBuilder searchBarConditionBuilder) {
        this.productFilterValuesQueryMap = list.stream().collect(Collectors.toMap(ProductFilterValuesQuery::field, Function.identity()));
        this.searchBarConditionBuilder = searchBarConditionBuilder;
    }

    public Paged<Product> findAll(String search, ProductFilter filter, Pageable pageable) {
        List<ProductFilterValuesQuery> queryListToSetParams = new ArrayList<>();
        String sql = getSelectForFindAll() +
                getFromForFindAll() +
                getWhereForFindAll(search, filter, queryListToSetParams) +
                getOrderForFindAll(pageable) +
                " limit " + (pageable.getPageSize() + 1) +
                " offset " + pageable.getOffset();
        Query query = entityManager.createNativeQuery(sql, "ProductItemMapping");
        queryListToSetParams.forEach(x -> x.setValueForQuery(filter, query));
        List<ProductItem> result = query.getResultList();

        boolean hasNext = result.size() > pageable.getPageSize();

        if (hasNext) {
            result.remove(result.size() - 1);
        }

        return new Paged<>(convertProductItemListToProductList(result).subList(0, Math.min(result.size(), pageable.getPageSize())),
                pageable,
                hasNext);
    }

    private String getSelectForFindAll() {
        return "select" + getSelectedField(Product.TABLE_NAME, Product.ID, "id") +
                "," + getSelectedField(Product.TABLE_NAME, Product.NAME, "name") +
                "," + getSelectedField(Product.TABLE_NAME, Product.TIME, "time") +
                "," + getSelectedField(Product.TABLE_NAME, Product.STRENGTH, "strength") +
                "," + getSelectedField(Product.TABLE_NAME, Product.CLUSTER, "cluster") +
                "," + getSelectedField(Product.TABLE_NAME, Product.BERRY, "berry") +
                "," + getSelectedField(Product.TABLE_NAME, Product.TASTE, "taste") +
                "," + getSelectedField(Product.TABLE_NAME, Product.RESISTANCE_COLD, "resistance_cold") +
                "," + getSelectedField(Product.TABLE_NAME, Product.PRICE_SEED, "price_seed") +
                "," + getSelectedField(Product.TABLE_NAME, Product.PRICE_CUT, "price_cut") +
                "," + getSelectedField(Product.TABLE_NAME, Product.IMAGE, "image") +
                "," + getSelectedField(Product.TABLE_NAME, Product.DESCRIPTION, "description") +
                "," + getSelectedField(Product.TABLE_NAME, Product.SELECTION_MINI, "selection_mini") +
                "," + getSelectedField(Product.TABLE_NAME, Product.AVAILABLE_SEED, "available_seed") +
                "," + getSelectedField(Product.TABLE_NAME, Product.AVAILABLE_CUT, "available_cut") +
                "," + getSelectedField(Product.TABLE_NAME, Product.SOLD_SEED, "sold_seed") +
                "," + getSelectedField(Product.TABLE_NAME, Product.SOLD_CUT, "sold_cut") +
                "," + getSelectedField(Selection.TABLE_NAME, Selection.ID, "selection_id");
    }

    private String getSelectedField(String table, String column, String name) {
        String selectedField = " %s.%s as %s";
        return selectedField.formatted(table, column, name);
    }

    private String getFromForFindAll() {
        return " from " + Product.TABLE_NAME + " as " + "product" +
                " left join " + Selection.TABLE_NAME + " as " + "selection" +
                " on (" + "(%s.%s)".formatted(Product.TABLE_NAME, Product.SELECTION_ID) + " = " + "(%s.%s)".formatted(Selection.TABLE_NAME, Selection.ID) + ")";
    }

    private String getWhereForFindAll(String search, ProductFilter filter, List<ProductFilterValuesQuery> queryListToSetParams) {
        StringBuilder whereString = new StringBuilder(" where %s.%s is not null".formatted(Product.TABLE_NAME, Product.NAME));

        for (ProductFilterValuesQuery condition : productFilterValuesQueryMap.values()) {
            Map.Entry<Boolean, String> doesSqlNeedSetting = condition.getWhereForFindAll(filter);
            if (Boolean.TRUE.equals(doesSqlNeedSetting.getKey())) {
                queryListToSetParams.add(condition);
            }

            String sqlCondition = doesSqlNeedSetting.getValue();
            if (!sqlCondition.isBlank()) {
                whereString.append(" and ").append(sqlCondition);
            }
        }

        String searchBarCondition = searchBarConditionBuilder.applySearchBarCondition(search);
        if (Objects.nonNull(searchBarCondition) && !searchBarCondition.isBlank()) {
            whereString.append(" and ").append(searchBarCondition);
        }

        return whereString.toString();
    }

    private String getOrderForFindAll(Pageable pageable) {
        Sort sort = this.prepareSort(pageable);
        if (sort.isEmpty()) {
            return "";
        }
        StringBuilder orderString = new StringBuilder(" order by ");
        sort.iterator().forEachRemaining(order ->
                orderString.append(order.getProperty()).append(" ").append(order.getDirection().name()).append(", "));
        orderString.delete(orderString.length() - 2, orderString.length());
        return orderString.toString();
    }

    private Sort prepareSort(Pageable pageable) {
        List<Sort.Order> orders = pageable.getSort().stream().map(order -> {
            Optional<ProductListSortValuesEnum> matchingEnum = Arrays.stream(ProductListSortValuesEnum.values())
                    .filter(x -> x.getSort().equals(order.getProperty()))
                    .findFirst();

            String sqlName = "";
            if (matchingEnum.isPresent()) {
                sqlName = matchingEnum.get().getSqlName();
            }

            return order.getDirection().isAscending()
                    ? Sort.Order.asc(sqlName)
                    : Sort.Order.desc(sqlName);
        }).toList();

        return Sort.by(orders);
    }

    private List<Product> convertProductItemListToProductList(List<ProductItem> productList) {
        List<UUID> ids = productList.stream()
                .map(ProductItem::getSelectionId)
                .toList();

        Session session = entityManager.unwrap(Session.class);
        session.enableFetchProfile("withProduct");
        List<Selection> selections = session.createQuery("from Selection where id in (:ids)", Selection.class)
                .setParameter("ids", ids)
                .getResultList();

        return productList.stream().map(productItem -> buildProduct(productItem, selections)).toList();
    }

    private Product buildProduct(ProductItem productItem, List<Selection> selections) {
        Product product = new Product(
                productItem.getId(),
                productItem.getName(),
                productItem.getTime(),
                productItem.getStrength(),
                productItem.getCluster(),
                productItem.getBerry(),
                productItem.getTaste(),
                productItem.getResistanceCold(),
                productItem.getPriceSeed(),
                productItem.getPriceCut(),
                productItem.getImage(),
                productItem.getDescription(),
                productItem.getSelectionMini(),
                productItem.getAvailableSeed(),
                productItem.getAvailableCut(),
                productItem.getSoldSeed(),
                productItem.getSoldCut(),
                null);

        for (Selection selection : selections) {
            if (selection.getId().equals(productItem.getSelectionId())) {
                product.setSelection(selection);
            }
        }

        return product;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Product> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Product create(ProductCreateDto createDto) {
        return null;
    }
}