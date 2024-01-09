package com.acme.retail.acmeretailstores.config;

import com.acme.retail.acmeretailstores.model.Product;
import com.acme.retail.acmeretailstores.processor.ProductProcessor;
import com.acme.retail.acmeretailstores.repository.ProductRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfiguration {
    @Autowired
    private ProductRepository productRepository;
    @Value("${inputFile}")
    Resource inputFile;


    @Bean
    @Autowired
    public Job job(JobRepository jobRepository) {
        return new JobBuilder("importProductsJob", jobRepository)
                .flow(importProductsStep(jobRepository, transactionManager())).end().build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    private Step importProductsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importProductsStep", jobRepository)
                .<Product, Product>chunk(50, transactionManager)
                .reader(readFromCsvFile())
                .processor(productItemProcessor())
                .writer(productItemWriter())
                .build();

    }

    @Bean
    public ItemProcessor<Product, Product> productItemProcessor() {
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<Product> productItemWriter() {
        return product -> productRepository.saveAll(product);
    }

    @Bean
    public FlatFileItemReader<Product> readFromCsvFile() {
        FlatFileItemReader<Product> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("ACME");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(inputFile);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Product> lineMapper() {
        DefaultLineMapper<Product> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("id", "name", "sku", "price", "date");
        lineTokenizer.setStrict(false); // Set strict property to false
        defaultLineMapper.setLineTokenizer(lineTokenizer);

        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(Product.class);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;

    }

}
