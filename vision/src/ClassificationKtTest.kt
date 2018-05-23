import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File

internal class ClassificationKtTest {

    @org.junit.jupiter.api.Test
    fun mainTest() {
        /*
         * Pass the image to the Vision API, expect a non-error response
         * Test passes when Vision API response is NOT an error
         */

        // Setup phase

        var requests = ArrayList<AnnotateImageRequest>()

        val file = File("./resources/doggo.jpg")

        var imgProto = ByteString.copyFrom(file.readBytes()) // Load image into proto buffer

        var vision = ImageAnnotatorClient.create() // Create an Image Annotator
        var img = Image.newBuilder().setContent(imgProto).build()
        var feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build() // Image builder
        var request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build()
        requests.add(request)

        // Execute phase

        var response = vision.batchAnnotateImages(requests)
        var responses = response.getResponsesList()

        // Test phase

        for (resp in responses) {

            val respError: Boolean = resp.hasError()

            assertFalse(respError) // No error is recorded

        }

    }

//    @org.junit.jupiter.api.Test
//    fun mainNoImageTest() {
//        /*
//         * Pass no image to the Vision API, expect a non-error response
//         * Test passes when Vision API response is an error
//         */
//
//        // Setup phase
//
//       // final ExpectGeneralException generalEx = new GeneralException()
//
//        val file = File("./resources/cat.jpg") // no such file exists
//
//        if (!file.exists()) {
//
//        //    assertThrows(NoSuchFileException.class) {
//
//         //       throw NoSuchFileException(file = file, reason = "The file you specified does not exist")
//            }
//        }
//
//    }
}