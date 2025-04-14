package ru.vinogradiya.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import ru.vinogradiya.models.entity.Product;
import ru.vinogradiya.models.entity.Selection;
import ru.vinogradiya.repositories.ProductsRepository;
import ru.vinogradiya.repositories.SelectionsRepository;

import java.util.UUID;

@Configuration
public class JpaRepositoryTestConfig {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public JpaRepositoryFactoryBean<ProductsRepository, Product, UUID> productsRepository() {
        return new JpaRepositoryFactoryBean<>(ProductsRepository.class);
    }

    @Bean
    public JpaRepositoryFactoryBean<SelectionsRepository, Selection, UUID> selectionsRepository() {
        return new JpaRepositoryFactoryBean<>(SelectionsRepository.class);
    }
}