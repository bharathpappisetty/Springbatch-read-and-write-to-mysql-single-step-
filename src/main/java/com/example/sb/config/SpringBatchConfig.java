package com.example.sb.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.example.sb.model.DeviceInfo;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Value("file:C:/output/output*.csv")
	private Resource[] inputResources;

	
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   MultiResourceItemReader<DeviceInfo> multiResourceItemReader,
                   ItemWriter<DeviceInfo> itemWriter
    ) {

        Step step = stepBuilderFactory.get("file-load")
                .<DeviceInfo, DeviceInfo>chunk(100)
                .reader(multiResourceItemReader)
//                .processor(itemProcessor)
                .writer(itemWriter)
                .build();


        return jobBuilderFactory.get("load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader<DeviceInfo> itemReader() {

        FlatFileItemReader<DeviceInfo> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }
    
    
    
    @Bean
	public MultiResourceItemReader<DeviceInfo> multiResourceItemReader() {
		MultiResourceItemReader<DeviceInfo> resourceItemReader = new MultiResourceItemReader<DeviceInfo>();
		resourceItemReader.setResources(inputResources);
		resourceItemReader.setDelegate(itemReader());
		return resourceItemReader;
	}

    @Bean
    public LineMapper<DeviceInfo> lineMapper() {

        DefaultLineMapper<DeviceInfo> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{ "username", "userid", "devicename", "datetime", "parameter", "metricvalue",  "location" });

        BeanWrapperFieldSetMapper<DeviceInfo> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(DeviceInfo.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

}
