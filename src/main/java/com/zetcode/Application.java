package com.zetcode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

@ComponentScan(basePackages = "com.zetcode")
public class Application {

	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(Application.class);
	 */
	private static RestHighLevelClient client;

	/*
	 * @Autowired private Environment env;
	 * 
	 * @Value("${app.name}") String appName;
	 * 
	 * @Value("${app.version}") String appVersion;
	 * 
	 */

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

		AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext(AppConfig.class);

		ElasticsearchProperties properties1 = context1.getBean(ElasticsearchProperties.class);
		System.out.println("*Name: " + properties1.getHost());
		System.out.println("*Description: " + properties1.getPort());

		client = new RestHighLevelClient(
				RestClient.builder(new HttpHost(properties1.getHost(), properties1.getPort(), "http")));

		StudentJDBCTemplate studentJDBCTemplate = (StudentJDBCTemplate) context.getBean("studentJDBCTemplate");

		System.out.println("------Insert Record--------");
		studentJDBCTemplate.create("Lukman1", 11);
		studentJDBCTemplate.create("Santosh1", 2);
		studentJDBCTemplate.create("Sesu1", 15);

		System.out.println("------Get Record--------");
		List<Student> students = studentJDBCTemplate.listStudents();

		for (Student record : students) {
			System.out.print("ID : " + record.getId());
			System.out.print(", Name : " + record.getName());
			System.out.println(", Age : " + record.getAge());
		}

		ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
		// var app = ctx.getBean(Application.class);
		Application app = ctx.getBean(Application.class);
		// app.run();

		for (Student stu : students) {

			// Create
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("ID", stu.getId().toString());
			jsonMap.put("Name", stu.getName().toString());
			jsonMap.put("Age", stu.getAge().toString());
			try {
				createDocument("student", stu.getId().toString(), jsonMap);
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		context1.close();
	}

	/*
	 * private void run() {
	 * 
	 * logger.info("From Environment"); logger.info("Application name: {}",
	 * env.getProperty("app.name")); logger.info("Application version: {}",
	 * env.getProperty("app.version"));
	 * 
	 * logger.info("Using @Value injection"); logger.info("Application name: {}",
	 * appName); logger.info("Application version: {}", appVersion); }
	 */

	private static void createDocument(String index, String id, Map<String, Object> jsonMap) throws IOException {
		IndexRequest request = new IndexRequest(index).id(id).source(jsonMap, XContentType.JSON);
		IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
		System.out.println("Create document: " + indexResponse.getResult());
	}
}