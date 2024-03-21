package data;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static final Faker  FAKER = new Faker(new Locale("en")); //рандомный пользователь на английском

    private DataGenerator() {
    }

    public static Registration.RegistrationDto sendRequest (Registration.RegistrationDto user ) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body( user ) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
        return user;
    }
    public static String getRandomLogin(){
        return FAKER.name().username(); //рандомный логин
    }
    public static String getRandomPassword() {
        return FAKER.internet().password(); //рандомный пароль
    }
    public static class Registration {
        private Registration (){
        }

        public static RegistrationDto getUser(String status){
            return new RegistrationDto(getRandomLogin() ,getRandomPassword() ,status );
        } //случайный незарегистрированный пользователь
        public static RegistrationDto getRegisteredUser(String status){
            return sendRequest(getUser(status)); //регистрация пользователя
        }

        @Value
        public static class RegistrationDto {
            String login;
            String password;
            String status;
        }
    }
}

