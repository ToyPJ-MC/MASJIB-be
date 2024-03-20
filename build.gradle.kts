import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

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
	create("asciidoctorExt") //
	all {
		exclude("org.springframework.boot", "spring-boot-starter-logging")
	}
}
val asciidoctor by configurations.creating
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

	implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.0")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation ("org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16")
	implementation("org.springframework.boot:spring-boot-starter-logging:3.1.5");

	implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.springframework.boot:spring-boot-starter-batch:3.1.5")
	implementation("commons-io:commons-io:2.15.0")

	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.0")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.2.0")
	implementation("org.springframework.boot:spring-boot-starter-security:3.2.0")

	implementation("org.locationtech.proj4j:proj4j:1.2.2")
	implementation("com.googlecode.json-simple:json-simple:1.1.1")

	implementation("com.mysql:mysql-connector-j:8.1.0")

	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.12.3")

	compileOnly("org.projectlombok:lombok:1.18.28")

	annotationProcessor("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.0")
	testImplementation("org.mockito:mockito-core:5.7.0")
	//testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
	testImplementation("io.kotest:kotest-runner-junit5:4.6.2")

	asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.0")

	testImplementation("org.springframework.batch:spring-batch-test:5.0.3")
	testImplementation("org.springframework.security:spring-security-test:6.2.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("com.h2database:h2")

}
val snippetsDir by extra { file("build/generated-snippets") } // 스니펫을 저장할 경로 선택

// Ascii Doc Create Tasks
tasks {
	// Test 결과를 snippet Directory에 출력
	test {
		outputs.dir(snippetsDir) // (1)
	}

	asciidoctor {
		// test 가 성공해야만, 아래 Task 실행
		dependsOn(test)

		// 기존에 존재하는 Docs 삭제(문서 최신화를 위해)
		doFirst {
			delete(file("src/main/resources/static/docs")) // (4)
		}

		// snippet Directory 설정
		inputs.dir(snippetsDir) // (5)

		// Ascii Doc 파일 생성
		doLast {
			copy { // (6)
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
tasks.named<Jar>("jar") {
	enabled = false
}

tasks.named<Test>("test") {
	useJUnitPlatform()
	systemProperty("jasypt.encryptor.password", project.findProperty("jasypt.encryptor.password").toString())
}

// -D를 통해 전달하는 경우
tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("jasypt.encryptor.password", System.getProperty("jasypt.encryptor.password"))
}
