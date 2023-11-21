import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "Backend"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}
configurations {
	create("asciidoctorExt") // (2)
	all {
		exclude("org.springframework.boot", "spring-boot-starter-logging")
	}
}
val asciidoctor by configurations.creating
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")

	implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation ("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")
	implementation("org.springframework.boot:spring-boot-starter-logging:3.1.5");

	implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.springframework.boot:spring-boot-starter-batch:3.1.5")
	implementation("commons-io:commons-io:2.15.0")

	implementation("com.mysql:mysql-connector-j:8.1.0")
	compileOnly("org.projectlombok:lombok:1.18.28")

	annotationProcessor("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.0")
	testImplementation("org.mockito:mockito-core:5.7.0")
	//testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
	testImplementation("io.kotest:kotest-runner-junit5:4.6.2")
	asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.0")

	testImplementation("org.springframework.batch:spring-batch-test:5.0.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

}
val snippetsDir by extra { file("build/generated-snippets") }

// Ascii Doc Create Tasks
tasks {
	// Test 결과를 snippet Directory에 출력
	test {
		outputs.dir(snippetsDir)
	}

	asciidoctor {
		// test 가 성공해야만, 아래 Task 실행
		dependsOn(test)

		// 기존에 존재하는 Docs 삭제(문서 최신화를 위해)
		doFirst {
			delete(file("src/main/resources/static/docs"))
		}

		// snippet Directory 설정
		inputs.dir(snippetsDir)

		// Ascii Doc 파일 생성
		doLast {
			copy {
				from("build/docs/asciidoc")
				into("src/main/resources/static/docs")
			}
		}
	}

	build {
		// Ascii Doc 파일 생성이 성공해야만, Build 진행
		dependsOn(asciidoctor)
	}
}
tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("jasypt.encryptor.password", project.properties["jasypt.encryptor.password"].toString())

}
