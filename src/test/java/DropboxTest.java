import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static io.restassured.config.EncoderConfig.encoderConfig;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DropboxTest {

    private static final String TOKEN = "aLNzXu6ZQHQAAAAAAAAAAbtsqv2Nc6qEWAj1AHSbWW8qteErwl1qCqQPeyeIMP8e";


    @Test
    @Order(1)
    public void UploadTest() throws IOException{
        String myJson = "{\"mode\": \"add\"," +
                "\"autorename\": true," +
                "\"mute\": false," +
                "\"path\": \"/message.txt\"," +
                "\"strict_conflict\": false}";

        byte[] file = Files.readAllBytes(Paths.get("src/message.txt"));

        RequestSpecification request = RestAssured.given();
        request.config(RestAssured.config().encoderConfig(encoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .header("Authorization", "Bearer " + TOKEN)
                .header("Dropbox-API-Arg", myJson)
                .header("Content-Type","application/octet-stream")
                .body(file)
                .when()
                .post("https://content.dropboxapi.com/2/files/upload")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(2)
    public void GetFileMetadataTest(){
        String myJson = "{\n\t\"path\": \"/message.txt\"\n}";
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type","application/json")
                .body(myJson)
                .when()
                .post("https://api.dropboxapi.com/2/files/get_metadata")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Order(3)
    public void DeleteTest(){
        String myJson = "{\n\t\"path\": \"/message.txt\"\n}";
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + TOKEN)
                .header("Content-Type","application/json")
                .body(myJson)
                .when()
                .post("https://api.dropboxapi.com/2/files/delete_v2")
                .then()
                .assertThat()
                .statusCode(200);
    }
}

